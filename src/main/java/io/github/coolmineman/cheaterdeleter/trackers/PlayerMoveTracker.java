package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.events.TeleportConfirmListener;
import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener.MoveCause;
import io.github.coolmineman.cheaterdeleter.mixin.ServerPlayNetworkHandlerAccessor;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerMoveData;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;

public class PlayerMoveTracker extends Tracker<PlayerMoveData> implements PacketCallback {

    protected PlayerMoveTracker() {
        super(PlayerMoveData.class);
        PacketCallback.EVENT.register(this);
    }

    @Override
    public @NotNull PlayerMoveData get(CDEntity entity) {
        return entity.getOrCreateData(PlayerMoveData.class, PlayerMoveData::new);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        PlayerMoveData teleportConfirmData = get(player);
        if (packet instanceof TeleportConfirmC2SPacket) {
            teleportConfirmData.teleportConfirmC2SPacket = (TeleportConfirmC2SPacket)packet;
            if (teleportConfirmData.lastWasTeleportConfirm) player.kick(new LiteralText("Illegal TeleportConfirmC2SPacket"));
            checkTeleportConfirmId(player, teleportConfirmData, teleportConfirmData.teleportConfirmC2SPacket.getTeleportId());
            teleportConfirmData.lastWasTeleportConfirm = true;
        } else {
            if (teleportConfirmData.lastWasTeleportConfirm) {
                if (packet instanceof PlayerMoveC2SPacketView) {
                    PlayerMoveC2SPacketView playerMoveC2SPacketView = (PlayerMoveC2SPacketView)packet;
                    if (playerMoveC2SPacketView.isChangePosition() && playerMoveC2SPacketView.isChangeLook()) {
                        TeleportConfirmListener.EVENT.invoker().onTeleportConfirm(player, teleportConfirmData.teleportConfirmC2SPacket, playerMoveC2SPacketView);
                        PlayerMovementListener.EVENT.invoker().onMovement(player, playerMoveC2SPacketView, MoveCause.TELEPORT);
                        teleportConfirmData.lastWasTeleportConfirm = false;
                        return ActionResult.PASS;
                    } else {
                        player.kick(new LiteralText("Expected PlayerMoveC2SPacket.Both After TeleportConfirmC2SPacket"));
                    }
                } else {
                    player.kick(new LiteralText("Expected PlayerMoveC2SPacket After TeleportConfirmC2SPacket"));
                }
            } else if (packet instanceof PlayerMoveC2SPacketView) {
                PlayerMovementListener.EVENT.invoker().onMovement(player, (PlayerMoveC2SPacketView)packet, MoveCause.OTHER);
            }
            teleportConfirmData.lastWasTeleportConfirm = false;
        }
        return ActionResult.PASS;
    }
    
    private void checkTeleportConfirmId(CDPlayer player, PlayerMoveData data, int id) {
        if (id != data.expectedTeleportId) player.kick(new LiteralText("Illegal TeleportConfirmC2SPacket 2 " + id + " " + data.expectedTeleportId));
        int max = ((ServerPlayNetworkHandlerAccessor)player.getNetworkHandler()).getRequestedTeleportId();
        if (id > max && max > data.expectedTeleportId) {
            player.kick(new LiteralText("Illegal TeleportConfirmC2SPacket 3"));
        }
        if (id < 0) {
            player.kick(new LiteralText("Illegal TeleportConfirmC2SPacket 4"));
        }
        if (++data.expectedTeleportId == Integer.MAX_VALUE) {
            data.expectedTeleportId = 0;
        }
    }
}
