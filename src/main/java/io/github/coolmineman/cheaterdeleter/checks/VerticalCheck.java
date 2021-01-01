package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.util.PlayerDataManager;
import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.trackers.TrackerManager;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerHitGroundData;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class VerticalCheck extends Check implements MovementPacketCallback, PlayerDamageListener {
    public VerticalCheck() {
        MovementPacketCallback.EVENT.register(this);

        PlayerDamageListener.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(ServerPlayerEntity player, PlayerMoveC2SPacketView packet) {
        VerticalCheckData verticalCheckData = PlayerDataManager.getOrCreate(player, VerticalCheckData.class, VerticalCheckData::new);
        if (player.isCreative() || player.isSwimming() || player.isTouchingWater() || player.isClimbing()) { //TODO Fix exiting lava edge case need isTouchingLiquid and disable when using an elytra and replace w/ elytra specific one
            verticalCheckData.isActive = false;
            return ActionResult.PASS;
        }
        if (player.isOnGround() && !packet.isOnGround() && player.getVelocity().getY() < 0.45) {
            verticalCheckData.maxY = player.getY() + 1.45; //TODO Slime/Bed Bouncing
            verticalCheckData.isActive = true;
        } else if (packet.isOnGround()) {
            if (verticalCheckData != null && verticalCheckData.isActive) {
                verticalCheckData.isActive = false;
            }
        } else { //Packet off ground
            if (verticalCheckData.isActive && packet.isChangePosition() && packet.getY() > verticalCheckData.maxY) {
                flag(player, "Failed Vertical Movement Check " + (verticalCheckData.maxY - packet.getY()));
            }
            if (!verticalCheckData.isActive && player.getVelocity().getY() < 0.45) {
                verticalCheckData.maxY = player.getY() + 1.45;
                verticalCheckData.isActive = true;
            }

        }
        return ActionResult.PASS;
    }

    private class VerticalCheckData {
        volatile double maxY = 0.0;
        boolean isActive = false;
    }

	@Override
	public void onPlayerDamage(ServerPlayerEntity player, DamageSource source, float amount) {
		VerticalCheckData verticalCheckData = PlayerDataManager.get(player, VerticalCheckData.class);
		if (verticalCheckData != null) {
            verticalCheckData.maxY += 0.5;
        }
	}
    
}
