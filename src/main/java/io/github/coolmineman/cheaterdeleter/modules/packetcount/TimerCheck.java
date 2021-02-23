package io.github.coolmineman.cheaterdeleter.modules.packetcount;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;

//TODO Laggy Connections
public class TimerCheck extends CDModule implements PlayerMovementListener {
    private static final int CHECK_PERIOD = 5;
    private static final int MAX_PACKETS_PER_SEC = 21; // 20 is target give some wiggle room

    public TimerCheck() {
        super("timer_check");
        PlayerMovementListener.EVENT.register(this);
    }

    private class PlayerTimerInfo {
        public int movementPackets = 0;
        public long time = System.currentTimeMillis();
    }

    @Override
    public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || cause.isTeleport() || player.getVehicleCd() != null) return;
        PlayerTimerInfo info = player.getOrCreateData(PlayerTimerInfo.class, PlayerTimerInfo::new);
        ++info.movementPackets;
        long timedelta = System.currentTimeMillis() - info.time;
        if (timedelta > CHECK_PERIOD * 1000 || info.movementPackets > MAX_PACKETS_PER_SEC * CHECK_PERIOD) {
            long expectedmax = MAX_PACKETS_PER_SEC * (timedelta/999);
            if (info.movementPackets >= expectedmax) {
                if (flag(player, FlagSeverity.MINOR, "Failed Timer Check " + (info.movementPackets - expectedmax))) player.rollback();
            }
            info.movementPackets = 0;
            info.time = System.currentTimeMillis();
        }
    }
}
