package io.github.coolmineman.cheaterdeleter.trackers.data;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.VehicleMoveS2CPacketView;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public class VehicleMovePacketsData implements Data {
    // -1 = Previous Packet Was Unrelated Packet
    // 0 = Previous Packet Was Player Look
    // 1 = Previous Packet Was Input After Player Look
    public int magic = -1;
    public PlayerMoveC2SPacketView playerLook = null;
    public PlayerInputC2SPacket playerInput = null;
    public VehicleMoveS2CPacketView lastVehicleMoveS2CPacket = null;
    public VehicleMoveC2SPacket lastVehicleMoveC2SPacket = null;
    public long lastVehicleMoveS2CPacketTime = Long.MIN_VALUE;
}
