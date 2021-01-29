package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public class VerticalCheck extends CDModule implements MovementPacketCallback, PlayerDamageListener {
    public VerticalCheck() {
        super("vertical_check");
        MovementPacketCallback.EVENT.register(this);
        PlayerDamageListener.EVENT.register(this);
    }

    //TODO: Smarter Bounce Handling
    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (!enabledFor(player) || !packet.isChangePosition()) return ActionResult.PASS;
        VerticalCheckData verticalCheckData = player.getOrCreateData(VerticalCheckData.class, VerticalCheckData::new);
        if (player.asMcPlayer().isCreative() || player.asMcPlayer().isSwimming() || player.asMcPlayer().isClimbing() || player.isFallFlying() || BlockCollisionUtil.isNearby(player, 2.0, 4.0, BlockCollisionUtil.NON_SOLID_COLLISION)) {
            verticalCheckData.isActive = false;
            return ActionResult.PASS;
        }
        if (player.isOnGround() && !packet.isOnGround() && player.getVelocity().getY() < 0.45) {
            verticalCheckData.maxY = player.getY() + player.getMaxJumpHeight();
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
                verticalCheckData.maxY = player.getY() + player.getMaxJumpHeight();
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
	public void onPlayerDamage(CDPlayer player, DamageSource source, float amount) {
        if (!enabledFor(player)) return;
		VerticalCheckData verticalCheckData = player.getData(VerticalCheckData.class);
		if (verticalCheckData != null) {
            verticalCheckData.maxY += 0.6;
        }
    }
}
