package io.github.coolmineman.cheaterdeleter.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface MovementPacketEvent {
    Event<MovementPacketEvent> EVENT = EventFactory.createArrayBacked(MovementPacketEvent.class,
        listeners -> (player, packet) -> {
            for (MovementPacketEvent listener : listeners) {
                ActionResult result = listener.onMovementPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }

        return ActionResult.PASS;
    });

    public static void init() {
        PacketEvent.EVENT.register((player, packet) -> {
            return packet instanceof PlayerMoveC2SPacket ? MovementPacketEvent.EVENT.invoker().onMovementPacket(player, (PlayerMoveC2SPacket)packet) : ActionResult.PASS;
        });
    }

    ActionResult onMovementPacket(ServerPlayerEntity player, PlayerMoveC2SPacket packet);
}
