package io.github.coolmineman.cheaterdeleter.modules.packetcount;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;

//TODO: Fails when walking into a glitched boat
//TODO: Fails when player "moved" by something like a piston
public class TimerCheck extends CDModule implements PlayerMovementListener, PlayerEndTickCallback {
    private static final int CHECK_PERIOD = 5;
    private static final int MAX_PACKETS = 21 * CHECK_PERIOD; // 20 is target give some wiggle room

    public TimerCheck() {
        super("timer_check");
        PlayerMovementListener.EVENT.register(this);
        PlayerEndTickCallback.EVENT.register(this);
    }

    private class PlayerTimerInfo {
        public AtomicInteger movementPackets = new AtomicInteger(0);
        public long time = System.currentTimeMillis();
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || cause.isTeleport() || player.getVehicleCd() != null) return;
        PlayerTimerInfo info = player.getOrCreateData(PlayerTimerInfo.class, PlayerTimerInfo::new);
        info.movementPackets.addAndGet(1);
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        if (!enabledFor(player)|| player.getVehicleCd() != null) return;
        PlayerTimerInfo info = player.getData(PlayerTimerInfo.class);
        if (info != null) {
            long timediff = System.currentTimeMillis() - info.time;
            if (timediff > 1000 * CHECK_PERIOD) {
                int movementPackets = info.movementPackets.getAndSet(0);
                info.time = System.currentTimeMillis();
                if (movementPackets > MAX_PACKETS) {
                    if (flag(player, FlagSeverity.MINOR, "Failed Timer Check " + (movementPackets - MAX_PACKETS))) player.rollback();
                }
            }
        }
    }
}
