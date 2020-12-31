package io.github.coolmineman.cheaterdeleter.checks;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.MapMaker;

import io.github.coolmineman.cheaterdeleter.events.MovementPacketEvent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class TimerCheck extends Check {
    private static final int CHECK_PERIOD = 5;
    private static final int MAX_PACKETS = 21 * CHECK_PERIOD; // 20 is target give some wiggle room
    private ConcurrentMap<ServerPlayerEntity, PlayerTimerInfo> map = new MapMaker().weakKeys().makeMap();

    public TimerCheck() {
        MovementPacketEvent.EVENT.register((player, packet) -> {
            if (!map.containsKey(player)) map.put(player, new PlayerTimerInfo());
            PlayerTimerInfo info = map.get(player);
            info.movementPackets.addAndGet(1);
            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (map.containsKey(player)) {
                    PlayerTimerInfo timerInfo = map.get(player);
                    long timediff = System.currentTimeMillis() - timerInfo.time;
                    if (timediff > 1000 * CHECK_PERIOD) {
                        int movementPackets = timerInfo.movementPackets.getAndSet(0);
                        timerInfo.time = System.currentTimeMillis();
                        System.out.println(movementPackets);
                        if (movementPackets > MAX_PACKETS) {
                            flag(player, "Failed Timer Check");
                        }
                    }
                }
            }
        });
    }

    private class PlayerTimerInfo {
        public AtomicInteger movementPackets = new AtomicInteger(0);
        public long time = System.currentTimeMillis();
    }
}
