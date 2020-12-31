package io.github.coolmineman.cheaterdeleter.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface PacketEvent {
    Event<PacketEvent> EVENT = EventFactory.createArrayBacked(PacketEvent.class,
        listeners -> (player, packet) -> {
            for (PacketEvent listener : listeners) {
                ActionResult result = listener.onPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }

        return ActionResult.PASS;
    });

    ActionResult onPacket(ServerPlayerEntity player, Packet<ServerPlayPacketListener> packet);
}
