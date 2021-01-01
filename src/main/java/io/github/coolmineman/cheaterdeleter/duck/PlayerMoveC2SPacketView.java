package io.github.coolmineman.cheaterdeleter.duck;

public interface PlayerMoveC2SPacketView {
    double getX();
    double getY();
    double getZ();
    float getYaw();
    float getPitch();
    boolean isOnGround();
    boolean isChangePosition();
    boolean isChangeLook();
}
