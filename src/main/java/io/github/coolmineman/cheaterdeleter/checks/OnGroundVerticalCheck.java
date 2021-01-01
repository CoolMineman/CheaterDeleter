package io.github.coolmineman.cheaterdeleter.checks;

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
        // System.out.println(System.currentTimeMillis() - TrackerManager.get(PlayerHitGroundData.class, player).lastHitGround.get());
        if (packet.isChangePosition() && packet.isOnGround() && player.isOnGround() && System.currentTimeMillis() - TrackerManager.get(PlayerHitGroundData.class, player).lastInAir.get() > 500) {
            boolean nearNonSolid = CollisionUtil.isNearby(player, CollisionUtil.NON_SOLID_COLLISION);
            if (!nearNonSolid) {
                double ydelta = packet.getY() - player.getY();
                if (ydelta > 0.3 || ydelta < -0.9) flag(player, "Player Moved Vertically While onGround " + ydelta);
            }
        }
        return ActionResult.PASS;
    }
    
}
