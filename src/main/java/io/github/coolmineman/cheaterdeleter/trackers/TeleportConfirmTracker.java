package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.events.TeleportConfirmListener;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.TeleportConfirmData;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;

public class TeleportConfirmTracker extends Tracker<TeleportConfirmData> implements PacketCallback {

    protected TeleportConfirmTracker() {
        super(TeleportConfirmData.class);
        PacketCallback.EVENT.register(this);
    }

    @Override
    public @NotNull TeleportConfirmData get(CDEntity entity) {
        return entity.getOrCreateData(TeleportConfirmData.class, TeleportConfirmData::new);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        TeleportConfirmData teleportConfirmData = get(player);
        if (packet instanceof TeleportConfirmC2SPacket) {
            teleportConfirmData.teleportConfirmC2SPacket = (TeleportConfirmC2SPacket)packet;
            if (teleportConfirmData.lastWasTeleportConfirm) player.kick(new LiteralText("Illegal TeleportConfirmC2SPacket"));
            teleportConfirmData.lastWasTeleportConfirm = true;
        } else {
            if (teleportConfirmData.lastWasTeleportConfirm) {
                if (packet instanceof PlayerMoveC2SPacketView) {
                    PlayerMoveC2SPacketView playerMoveC2SPacketView = (PlayerMoveC2SPacketView)packet;
                    if (playerMoveC2SPacketView.isChangePosition() && playerMoveC2SPacketView.isChangeLook()) {
                        TeleportConfirmListener.EVENT.invoker().onTeleportConfirm(player, teleportConfirmData.teleportConfirmC2SPacket, playerMoveC2SPacketView);
                    } else {
                        player.kick(new LiteralText("Expected PlayerMoveC2SPacket.Both After TeleportConfirmC2SPacket"));
                    }
                } else {
                    player.kick(new LiteralText("Expected PlayerMoveC2SPacket After TeleportConfirmC2SPacket"));
                }
            }
            teleportConfirmData.lastWasTeleportConfirm = false;
        }
        return ActionResult.PASS;
    }
    
}
