package io.github.coolmineman.cheaterdeleter.checks;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.coolmineman.cheaterdeleter.util.PlayerDataManager;
import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class GlideCheck extends Check implements MovementPacketCallback, PlayerDamageListener {
    public GlideCheck() {
        MovementPacketCallback.EVENT.register(this);
        PlayerDamageListener.EVENT.register(this);
    }

    @Override
    public ActionResult onMovementPacket(ServerPlayerEntity player, PlayerMoveC2SPacketView packet) {
        GlideCheckData data = PlayerDataManager.getOrCreate(player, GlideCheckData.class, GlideCheckData::new);
        if (!packet.isOnGround() && packet.isChangePosition()) {
            if (data.isActive) {
                //TODO Make violations double based not boolean
                double velocity = player.getVelocity().getY();
                if (velocity < -1) {
                    boolean failedCheck = data.lasty - packet.getY() < 0.9;
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
            data.lasty = packet.getY();
            data.isActive = true;
        }
        return ActionResult.PASS;
    }

    //Should avoid edge cases good enough for now
    @Override
    public void onPlayerDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        GlideCheckData data = PlayerDataManager.getOrCreate(player, GlideCheckData.class, GlideCheckData::new);
        int violations = data.violations.get();
        if (violations > 0) {
            data.violations.decrementAndGet();
        }
    }

    private class GlideCheckData {
        double lasty = 0.0;
        AtomicInteger violations = new AtomicInteger(0);
        boolean isActive = false;
    }

}
