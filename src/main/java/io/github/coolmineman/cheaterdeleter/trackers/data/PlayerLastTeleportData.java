package io.github.coolmineman.cheaterdeleter.trackers.data;

public class PlayerLastTeleportData implements Data {
    public volatile double lastTeleportX = 0.0;
    public volatile double lastTeleportY = 0.0;
    public volatile double lastTeleportZ = 0.0;
    public volatile float lastTeleportYaw = 0.0f;
    public volatile float lastTeleportPitch = 0.0f;
    public volatile long lastTeleport = Long.MIN_VALUE;
}
