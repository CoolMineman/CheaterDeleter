package io.github.coolmineman.cheaterdeleter.duck;

public interface IPlayerMoveC2SPacket {
    double getX();
    double getY();
    double getZ();
    float getYaw();
    float getPitch();
    boolean isOnGround();
    boolean isChangePosition();
    boolean isChangeLook();
}
