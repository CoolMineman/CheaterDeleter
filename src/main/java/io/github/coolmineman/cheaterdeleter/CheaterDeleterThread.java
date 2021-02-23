package io.github.coolmineman.cheaterdeleter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mojang.datafixers.util.Pair;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.text.LiteralText;

public class CheaterDeleterThread extends Thread {
    public static final CheaterDeleterThread INSTANCE = new CheaterDeleterThread();
    public static final Queue<Pair<CDPlayer, Packet<ServerPlayPacketListener>>> PACKET_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        INSTANCE.setName("CheaterDeleter");
        INSTANCE.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            Pair<CDPlayer, Packet<ServerPlayPacketListener>> packetContext = PACKET_QUEUE.poll();
            if (packetContext != null) {
                try {
                    PacketCallback.EVENT.invoker().onPacket(packetContext.getFirst(), packetContext.getSecond());
                } catch (Throwable t) {
                   t.printStackTrace();
                   packetContext.getFirst().kick(new LiteralText("Crashed AntiCheat"));
                }
            }
        }
    }
}
