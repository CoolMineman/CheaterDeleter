package io.github.coolmineman.cheaterdeleter.checks;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.compat.CompatManager;
import io.github.coolmineman.cheaterdeleter.compat.StepHeightEntityAttributeCompat;
import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.trackers.TrackerManager;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerHitGroundData;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class OnGroundVerticalCheck extends Check implements MovementPacketCallback {
    public OnGroundVerticalCheck() {
        MovementPacketCallback.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(ServerPlayerEntity player, PlayerMoveC2SPacketView packet) {
        StepHeightEntityAttributeCompat compat = CompatManager.getCompatHolder(StepHeightEntityAttributeCompat.class).compat;
        float stepHeight = getStepHeight(compat, player);
        if (packet.isChangePosition() &&
            packet.isOnGround() &&
            player.isOnGround() &&
            System.currentTimeMillis() - TrackerManager.get(PlayerHitGroundData.class, player).lastInAir.get() > 500 &&
            !CollisionUtil.isNearby(player, 2.0, 4.0, CollisionUtil.NON_SOLID_COLLISION) &&
            ((getStepHeight(compat, player) > 1f) || !CollisionUtil.isNearby(player, packet.getX(), packet.getY(), packet.getZ(), 0.2, 0.5, CollisionUtil.steppablePredicate(stepHeight)))
        ) {
            double ydelta = packet.getY() - player.getY();
            if (ydelta > (stepHeight < 1f ? 0.3 : stepHeight)) flag(player, "Player Moved Vertically While onGround " + ydelta);
            if (ydelta < -0.9) flag(player, "Player Moved Vertically While onGround " + ydelta);
        }
        return ActionResult.PASS;
    }

    private float getStepHeight(@Nullable StepHeightEntityAttributeCompat compat, ServerPlayerEntity player) {
        if (compat == null) {
            return 0.6f;
        } else {
            return compat.getStepHeightAddition(player) + 0.6f;
        }
    }
    
}
