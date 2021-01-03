package io.github.coolmineman.cheaterdeleter.trackers;

import java.util.HashMap;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.Data;

public class TrackerManager {
    private TrackerManager() { }

    private static final HashMap<Class<? extends Data>, Tracker<? extends Data>> TRACKERS = new HashMap<>();

    public static void init() {
        registerTracker(new PlayerHitGroundTracker());
        registerTracker(new PlayerLastPositionTracker());
        registerTracker(new PlayerLastTeleportTracker());
    }

    public static void registerTracker(Tracker<?> tracker) {
        TRACKERS.put(tracker.getClazz(), tracker);
    }

    public static <T extends Data> T get(Class<T> clazz, CDPlayer player) {
        return clazz.cast(TRACKERS.get(clazz).get(player));
    }

    public static int getTrackerCount() {
        return TRACKERS.size();
    }
}
