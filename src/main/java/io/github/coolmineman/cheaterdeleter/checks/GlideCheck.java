package io.github.coolmineman.cheaterdeleter.checks;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.TrackerManager;
import io.github.coolmineman.cheaterdeleter.trackers.data.PlayerLastPositionData;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public class GlideCheck extends Check implements MovementPacketCallback, PlayerDamageListener {
    public GlideCheck() {
        MovementPacketCallback.EVENT.register(this);
        PlayerDamageListener.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        GlideCheckData data = player.getOrCreateData(GlideCheckData.class, GlideCheckData::new);
        if (!packet.isOnGround() && packet.isChangePosition()) {
            if (data.isActive) {
                //TODO Make violations double based not boolean
                double velocity = player.getVelocity().getY();
                if (velocity < -1) {
                    boolean failedCheck = TrackerManager.get(PlayerLastPositionData.class, player).lastY - packet.getY() < 0.9;
                    if (failedCheck) {
                        data.violations.incrementAndGet();
                    } else {
                        data.violations.decrementAndGet();
                    }
                    int violations = data.violations.get();
                    if (violations >= 4) {
                        flag(player, "Failed Glide Check");
                        data.violations.set(0);
                    }
                    if (violations < 0) data.violations.getAndAdd(-1 * violations);
                }
            }
            data.isActive = true;
        }
        return ActionResult.PASS;
    }

    //Should avoid edge cases good enough for now
    @Override
    public void onPlayerDamage(CDPlayer player, DamageSource source, float amount) {
        GlideCheckData data = player.getOrCreateData(GlideCheckData.class, GlideCheckData::new);
        int violations = data.violations.get();
        if (violations > 0) {
            data.violations.decrementAndGet();
        }
    }

    private class GlideCheckData {
        AtomicInteger violations = new AtomicInteger(0);
        boolean isActive = false;
    }

}
