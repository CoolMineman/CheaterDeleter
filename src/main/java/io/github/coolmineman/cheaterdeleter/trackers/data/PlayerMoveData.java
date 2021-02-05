package io.github.coolmineman.cheaterdeleter.trackers.data;

import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

public class PlayerMoveData implements Data {
    public TeleportConfirmC2SPacket teleportConfirmC2SPacket = null;
    public boolean lastWasTeleportConfirm = false;
}
