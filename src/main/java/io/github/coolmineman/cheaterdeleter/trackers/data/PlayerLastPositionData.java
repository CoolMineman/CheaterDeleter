package io.github.coolmineman.cheaterdeleter.trackers.data;

public class PlayerLastPositionData implements Data {
    public PlayerLastPositionData(double x, double y, double z) {
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
    }

    public double lastX;
    public double lastY;
    public double lastZ;
}
