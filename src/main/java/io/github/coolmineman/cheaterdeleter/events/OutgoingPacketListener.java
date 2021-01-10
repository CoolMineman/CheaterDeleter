package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;

public interface OutgoingPacketListener {
    Event<OutgoingPacketListener> EVENT = EventFactory.createArrayBacked(OutgoingPacketListener.class,
        listeners -> (player, packet) -> {
            for (OutgoingPacketListener listener : listeners) {
                listener.onOutgoingPacket(player, packet);
            }
    });

    public void onOutgoingPacket(CDPlayer player, Packet<ClientPlayPacketListener> packet);
}
