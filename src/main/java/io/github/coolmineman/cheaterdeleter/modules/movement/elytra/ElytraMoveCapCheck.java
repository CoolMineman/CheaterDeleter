package io.github.coolmineman.cheaterdeleter.modules.movement.elytra;

import io.github.coolmineman.cheaterdeleter.events.ClientCommandC2SPacketListener;
import io.github.coolmineman.cheaterdeleter.events.InteractItemListener;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

/**
 * Based on max distance / fall speed from https://minecraft.gamepedia.com/Elytra#Speed_and_altitude
 * Simple cap so cheats can't pull too many shenanigans
 */
public class ElytraMoveCapCheck extends CDModule implements ClientCommandC2SPacketListener, MovementPacketCallback, InteractItemListener {
    public ElytraMoveCapCheck() {
        super("elytra_move_cap");
        ClientCommandC2SPacketListener.EVENT.register(this);
        MovementPacketCallback.EVENT.register(this);
        InteractItemListener.EVENT.register(this);
    }

    public static class ElytraMoveCapCheckData {
        double yCap = Double.MAX_VALUE;
        boolean isActive = false;
        double lastUpdate = 0;
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        ElytraMoveCapCheckData data = player.getData(ElytraMoveCapCheckData.class);
        if (data != null) {
            if (data.isActive && !player.isFallFlying()) {
                data.isActive = false;
            } else if (packet.isChangePosition() && data.isActive) {
                if (player.getY() > data.yCap) {
                    flag(player, FlagSeverity.MINOR, "Elytra Too High " + (player.getY() - data.yCap));
                    PunishUtil.groundPlayer(player);
                } else if (System.currentTimeMillis() - data.lastUpdate >= 1000) {
                    data.yCap -= 1.5;
                    data.lastUpdate = System.currentTimeMillis();
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onClientCommandC2SPacket(CDPlayer player, ClientCommandC2SPacket packet) {
        if (packet.getMode() == Mode.START_FALL_FLYING && !player.isFallFlying()) {
            ElytraMoveCapCheckData data = player.getOrCreateData(ElytraMoveCapCheckData.class, ElytraMoveCapCheckData::new);
            data.yCap = player.getY() + player.getMaxJumpHeight() + 3; // Give some slack
            data.lastUpdate = System.currentTimeMillis();
            data.isActive = true;
        }
    }

    @Override
    public void onInteractItem(CDPlayer player, Hand hand, ItemStack stackInHand) {
        ElytraMoveCapCheckData data = player.getData(ElytraMoveCapCheckData.class);
        if (data != null && data.isActive && stackInHand.getItem() == Items.FIREWORK_ROCKET) {
            data.yCap += 50; //Not Exact
        }
    }
}