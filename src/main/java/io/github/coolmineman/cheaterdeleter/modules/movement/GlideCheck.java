package io.github.coolmineman.cheaterdeleter.modules.movement;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.events.PlayerDamageListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.entity.damage.DamageSource;

public class GlideCheck extends CDModule implements PlayerMovementListener, PlayerDamageListener {
    public GlideCheck() {
        super("glide_check");
        PlayerMovementListener.EVENT.register(this);
        PlayerDamageListener.EVENT.register(this);
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || player.isSpectator()) return;
        GlideCheckData data = player.getOrCreateData(GlideCheckData.class, GlideCheckData::new);
        if (!packet.isOnGround() && packet.isChangePosition() && !player.isFallFlying()) {
            if (BlockCollisionUtil.isNearby(player, 5, 5, BlockCollisionUtil.BOUNCY)) {
                data.enableAfter = System.currentTimeMillis() + 10000;
            }
            if (data.isActive && System.currentTimeMillis() > data.enableAfter) {
                //Violations should probably be double based not boolean based but this works for now
                double velocity = player.getVelocity().getY();
                
                if (velocity < -1) {
                    boolean failedCheck = player.getPacketY() - packet.getY() < 0.75;
                    if (failedCheck) {
                        data.violations.incrementAndGet();
                    } else {
                        data.violations.decrementAndGet();
                    }
                    int violations = data.violations.get();
                    if (violations >= 4) {
                        fail(player, player.getPacketY() - packet.getY());
                        data.violations.set(0);
                    }
                    if (violations < 0) data.violations.getAndAdd(-1 * violations);
                }
            }
            data.isActive = true;
        }
    }

    private void fail(CDPlayer player, double failamount) {
        if (failamount > 0.3) {
            flag(player, FlagSeverity.MINOR, "Failed Glide Check (Minor) " + failamount);
        } else {
            if (flag(player, FlagSeverity.MAJOR, "Failed Glide Check " + failamount)) PunishUtil.groundPlayer(player);
        }
    }

    @Override
    public long getFlagCoolDownMs() {
        return 0;
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
        long enableAfter = 0;
    }

}
