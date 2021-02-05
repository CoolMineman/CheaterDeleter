package io.github.coolmineman.cheaterdeleter.modules.movement;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;

public class OnGroundVerticalCheck extends CDModule implements PlayerMovementListener {
    public OnGroundVerticalCheck() {
        super("onground_vertical_check");
        PlayerMovementListener.EVENT.register(this);
    }

    // TODO: Boats and Shulker
    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player)) return;
        float stepHeight = player.getStepHeight();
        if (packet.isChangePosition() &&
            packet.isOnGround() &&
            player.isOnGround() &&
            System.currentTimeMillis() - player.getTracked(Trackers.PLAYER_HIT_GROUND_TRACKER).lastInAir.get() > 500 &&
            !BlockCollisionUtil.isNearby(player, 2.0, 4.0, BlockCollisionUtil.NON_SOLID_COLLISION) &&
            ((stepHeight > 1f) || !CollisionUtil.isNearby(player, packet.getX(), packet.getY(), packet.getZ(), 0.2, 0.5, CollisionUtil.steppablePredicates(stepHeight)))
        ) {
            double ydelta = packet.getY() - player.getY();
            if (ydelta > (stepHeight < 1f ? 0.3 : stepHeight)) flagRollback(player, FlagSeverity.MAJOR, "Player Moved Vertically While onGround " + ydelta);
            if (ydelta < -0.9) flagRollback(player, FlagSeverity.MAJOR, "Player Moved Vertically While onGround " + ydelta);
        }
    }

    public void flagRollback(CDPlayer player, FlagSeverity severity, String message) {
        if (super.flag(player, severity, message)) player.rollback();        
    }
    
}
