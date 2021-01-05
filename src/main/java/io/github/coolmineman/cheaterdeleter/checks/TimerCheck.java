package io.github.coolmineman.cheaterdeleter.checks;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.coolmineman.cheaterdeleter.duck.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.events.MovementPacketCallback;
import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.util.ActionResult;

public class TimerCheck extends Check implements MovementPacketCallback, PlayerEndTickCallback {
    private static final int CHECK_PERIOD = 5;
    private static final int MAX_PACKETS = 21 * CHECK_PERIOD; // 20 is target give some wiggle room

    public TimerCheck() {
        MovementPacketCallback.EVENT.register(this);

        PlayerEndTickCallback.EVENT.register(this);
    }

    private class PlayerTimerInfo {
        public AtomicInteger movementPackets = new AtomicInteger(0);
        public long time = System.currentTimeMillis();
    }

    @Override
    public ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet) {
        if (player.shouldBypassAnticheat()) return ActionResult.PASS;
        PlayerTimerInfo info = player.getOrCreateData(PlayerTimerInfo.class, PlayerTimerInfo::new);
        info.movementPackets.addAndGet(1);
        return ActionResult.PASS;
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        if (player.shouldBypassAnticheat()) return;
        PlayerTimerInfo info = player.getData(PlayerTimerInfo.class);
        if (info != null) {
            long timediff = System.currentTimeMillis() - info.time;
            if (timediff > 1000 * CHECK_PERIOD) {
                int movementPackets = info.movementPackets.getAndSet(0);
                info.time = System.currentTimeMillis();
                if (movementPackets > MAX_PACKETS) {
                    if (flag(player, FlagSeverity.MINOR, "Failed Timer Check")) player.rollback();
                }
            }
        }
    }
}
