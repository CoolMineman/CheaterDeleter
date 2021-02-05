package io.github.coolmineman.cheaterdeleter.trackers;

public class Trackers {
    private Trackers() { }

    private static int trackerCount = 0;
    public static final PlayerHitGroundTracker PLAYER_HIT_GROUND_TRACKER = registerTracker(new PlayerHitGroundTracker());
    public static final PlayerLastTeleportTracker PLAYER_LAST_TELEPORT_TRACKER = registerTracker(new PlayerLastTeleportTracker());
    public static final VehicleMovePacketsTracker VEHICLE_MOVE_PACKETS_TRACKER = registerTracker(new VehicleMovePacketsTracker());
    public static final PhaseBypassTracker PHASE_BYPASS_TRACKER = registerTracker(new PhaseBypassTracker());
    public static final PlayerMoveTracker PLAYER_MOVE_TRACKER = registerTracker(new PlayerMoveTracker());

    public static void init() {
        //Static Init
    }

    public static <T extends Tracker<?>> T registerTracker(T tracker) {
        trackerCount++;
        return tracker;
    }

    public static int getTrackerCount() {
        return trackerCount;
    }
}
