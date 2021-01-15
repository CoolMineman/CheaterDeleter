package io.github.coolmineman.cheaterdeleter.trackers;

public class Trackers {
    private Trackers() { }

    private static int trackerCount = 0;
    public static final PlayerHitGroundTracker PLAYER_HIT_GROUND_TRACKER = registerTracker(new PlayerHitGroundTracker());
    public static final PlayerLastTeleportTracker PLAYER_LAST_TELEPORT_TRACKER = registerTracker(new PlayerLastTeleportTracker());

    public static void init() {
        registerTracker(new PlayerHitGroundTracker());
        registerTracker(new PlayerLastTeleportTracker());
    }

    public static <T extends Tracker<?>> T registerTracker(T tracker) {
        trackerCount++;
        return tracker;
    }

    public static int getTrackerCount() {
        return trackerCount;
    }
}
