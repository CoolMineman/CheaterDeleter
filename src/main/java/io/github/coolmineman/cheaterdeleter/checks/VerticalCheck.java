package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.PlayerDataManager;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class VerticalCheck extends Check implements MovementPacketCallback {
    public VerticalCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(ServerPlayerEntity player, PlayerMoveC2SPacket packet) {
        if (player.isCreative() || player.isSwimming() || player.isTouchingWater() || player.isClimbing()) { //TODO Fix exiting lava edge case 
            PlayerDataManager.put(player, VerticalCheckData.class, null);
            return ActionResult.PASS;
        }
        VerticalCheckData verticalCheckData = PlayerDataManager.getOrCreate(player, VerticalCheckData.class, VerticalCheckData::new);
        if (player.isOnGround() && !packet.isOnGround() && player.getVelocity().getY() < 0.45) {
            verticalCheckData.maxY = player.getY() + 1.45;
            verticalCheckData.isActive = true;
        } else if (packet.isOnGround()) {
            if (verticalCheckData != null && verticalCheckData.isActive) {
                verticalCheckData.isActive = false;
            }
        } else { //Packet off ground
            if (verticalCheckData.isActive && packet.getY(player.getY()) > verticalCheckData.maxY) flag(player, "Failed Vertical Movement Check");
            if (!verticalCheckData.isActive && player.getVelocity().getY() < 0.45) {
                verticalCheckData.maxY = player.getY() + 1.45;
                verticalCheckData.isActive = true;
            }

        }
        return ActionResult.PASS;
    }

    private class VerticalCheckData {
        double maxY = 0.0;
        boolean isActive = false;
    }
    
}
