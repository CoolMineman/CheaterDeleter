package io.github.coolmineman.cheaterdeleter.checks;

import java.util.concurrent.ThreadLocalRandom;

import io.github.coolmineman.cheaterdeleter.events.OutgoingPacketListener;
import io.github.coolmineman.cheaterdeleter.objects.MutableEntityPositionS2CPacket;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;

public class AntiTeleportFinder extends Check implements OutgoingPacketListener {
    public AntiTeleportFinder() {
        OutgoingPacketListener.EVENT.register(this);
    }

    @Override
    public void onOutgoingPacket(CDPlayer player, Packet<ClientPlayPacketListener> packet) {
        if (packet instanceof MutableEntityPositionS2CPacket) {
            MutableEntityPositionS2CPacket packet2 = (MutableEntityPositionS2CPacket) packet;
            int viewDistance = player.asMcPlayer().server.getPlayerManager().getViewDistance();
            if (MathUtil.getDistanceSquared(player.getX(), player.getZ(), packet2.getX(), packet2.getZ()) > (viewDistance * viewDistance * 256) + 1) {
                scrambleTeleportPacket(player, viewDistance, packet2);
            }
        }
    }

    private void scrambleTeleportPacket(CDPlayer player, int viewDistance, MutableEntityPositionS2CPacket packet) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        packet.setX(getFakeDouble(random, player.getX(), (double) viewDistance + 16, 50000));
        packet.setY(random.nextDouble() * 256.0);
        packet.setZ(getFakeDouble(random, player.getZ(), (double) viewDistance + 16, 50000));
    }

    private double getFakeDouble(ThreadLocalRandom random, double origin, double lowerdeltabound, double upperdeltabound) {
        double a = ((random.nextDouble() - 0.5) * 2 * (upperdeltabound - lowerdeltabound));
        return origin + (a > 0 ? a + lowerdeltabound : a - lowerdeltabound);
    }
}
