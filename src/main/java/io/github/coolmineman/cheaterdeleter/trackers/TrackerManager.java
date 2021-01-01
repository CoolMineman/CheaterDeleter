package io.github.coolmineman.cheaterdeleter.trackers;

import java.util.HashMap;

import io.github.coolmineman.cheaterdeleter.trackers.data.Data;
import net.minecraft.server.network.ServerPlayerEntity;

public class TrackerManager {
    private TrackerManager() { }

    private static final HashMap<Class<? extends Data>, Tracker<? extends Data>> TRACKERS = new HashMap<>();

    public static void init() {
        registerTracker(new PlayerHitGroundTracker());
    }

    public static void registerTracker(Tracker<?> tracker) {
        TRACKERS.put(tracker.getClazz(), tracker);
    }

    public static <T> T get(Class<T> clazz, ServerPlayerEntity player) {
        return clazz.cast(TRACKERS.get(clazz).get(player));
    }

    public static int getTrackerCount() {
        return TRACKERS.size();
    }
}
