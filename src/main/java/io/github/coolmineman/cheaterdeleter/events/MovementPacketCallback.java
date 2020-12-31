package io.github.coolmineman.cheaterdeleter.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface MovementPacketCallback {
    Event<MovementPacketCallback> EVENT = EventFactory.createArrayBacked(MovementPacketCallback.class,
        listeners -> (player, packet) -> {
            for (MovementPacketCallback listener : listeners) {
                ActionResult result = listener.onMovementPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }

        return ActionResult.PASS;
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> {
            return packet instanceof PlayerMoveC2SPacket ? MovementPacketCallback.EVENT.invoker().onMovementPacket(player, (PlayerMoveC2SPacket)packet) : ActionResult.PASS;
        });
    }

    ActionResult onMovementPacket(ServerPlayerEntity player, PlayerMoveC2SPacket packet);
}
