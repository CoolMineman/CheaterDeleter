package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.PlayerDataManager;
import io.github.coolmineman.cheaterdeleter.duck.IPlayerMoveC2SPacket;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class VerticalCheck extends Check implements MovementPacketCallback {
    public VerticalCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(ServerPlayerEntity player, IPlayerMoveC2SPacket packet) {
        VerticalCheckData verticalCheckData = PlayerDataManager.getOrCreate(player, VerticalCheckData.class, VerticalCheckData::new);
        if (player.isCreative() || player.isSwimming() || player.isTouchingWater() || player.isClimbing()) { //TODO Fix exiting lava edge case need isTouchingLiquid
            verticalCheckData.isActive = false;
            return ActionResult.PASS;
        }
        if (player.isOnGround() && !packet.isOnGround() && player.getVelocity().getY() < 0.45) {
            verticalCheckData.maxY = player.getY() + 1.45;
            verticalCheckData.isActive = true;
        } else if (packet.isOnGround()) {
            if (verticalCheckData != null && verticalCheckData.isActive) {
                verticalCheckData.isActive = false;
            }
        } else { //Packet off ground
            if (verticalCheckData.isActive && packet.isChangePosition() && packet.getY() > verticalCheckData.maxY) flag(player, "Failed Vertical Movement Check");
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
