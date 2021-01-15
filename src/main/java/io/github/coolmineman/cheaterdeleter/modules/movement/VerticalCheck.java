package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.ActionResult;

public class VerticalCheck extends CDModule implements MovementPacketCallback, PlayerDamageListener {
    public VerticalCheck() {
        MovementPacketCallback.EVENT.register(this);

        PlayerDamageListener.EVENT.register(this);
    }

    //TODO: Smarter Bounce Handling
    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (player.shouldBypassAnticheat() || !packet.isChangePosition()) return ActionResult.PASS;
        VerticalCheckData verticalCheckData = player.getOrCreateData(VerticalCheckData.class, VerticalCheckData::new);
        if (player.asMcPlayer().isCreative() || player.asMcPlayer().isSwimming() || player.asMcPlayer().isClimbing() || player.isFallFlying() || BlockCollisionUtil.isNearby(player, 2.0, 4.0, BlockCollisionUtil.NON_SOLID_COLLISION)) {
            verticalCheckData.isActive = false;
            return ActionResult.PASS;
        }
        if (player.isOnGround() && !packet.isOnGround() && player.getVelocity().getY() < 0.45) {
            verticalCheckData.maxY = player.getY() + getMaxJumpHeight(player);
            verticalCheckData.isActive = true;
        } else if (packet.isOnGround()) {
            if (verticalCheckData != null && verticalCheckData.isActive) {
                verticalCheckData.isActive = false;
            }
        } else { //Packet off ground
            if (verticalCheckData.isActive && packet.isChangePosition() && packet.getY() > verticalCheckData.maxY) {
                if (flag(player, FlagSeverity.MINOR, "Failed Vertical Movement Check " + (verticalCheckData.maxY - packet.getY()))) PunishUtil.groundPlayer(player);
            }
            if (!verticalCheckData.isActive && player.getVelocity().getY() < 0.45) {
                verticalCheckData.maxY = player.getY() + getMaxJumpHeight(player);
                verticalCheckData.isActive = true;
            }

        }
        return ActionResult.PASS;
    }

    //TODO: Hard coded (like vanilla)
    private double getMaxJumpHeight(CDPlayer player) {
        double result = 1.25f;
        if (player.asMcPlayer().hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            result += Math.pow(1.5, (player.asMcPlayer().getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1)) - 1; //Acumulates Error With High Jump Boosts, oh well
        }
        result += 0.2; // Give a bit of wiggle room
        return result;
    }

    private class VerticalCheckData {
        volatile double maxY = 0.0;
        boolean isActive = false;
    }

	@Override
	public void onPlayerDamage(CDPlayer player, DamageSource source, float amount) {
        if (player.shouldBypassAnticheat()) return;
		VerticalCheckData verticalCheckData = player.getData(VerticalCheckData.class);
		if (verticalCheckData != null) {
            verticalCheckData.maxY += 0.6;
        }
    }
}
