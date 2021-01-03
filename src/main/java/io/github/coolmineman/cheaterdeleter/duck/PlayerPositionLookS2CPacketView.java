package io.github.coolmineman.cheaterdeleter.duck;

import java.util.Set;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public interface PlayerPositionLookS2CPacketView {
    double getX();
    double getY();
    double getZ();
    float getYaw();
    float getPitch();
    Set<PlayerPositionLookS2CPacket.Flag> getFlags();
    int getTeleportId();
}
