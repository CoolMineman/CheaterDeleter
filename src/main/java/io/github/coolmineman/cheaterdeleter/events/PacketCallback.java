package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.util.ActionResult;

public interface PacketCallback {
    Event<PacketCallback> EVENT = EventFactory.createArrayBacked(PacketCallback.class,
        listeners -> (player, packet) -> {
            for (PacketCallback listener : listeners) {
                ActionResult result = listener.onPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }

        return ActionResult.PASS;
    });

    ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet);
}
