package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public interface MovementPacketCallback {
    Event<MovementPacketCallback> EVENT = EventFactory.createArrayBacked(MovementPacketCallback.class,
        listeners -> (player, packet) -> {
            for (MovementPacketCallback listener : listeners) {
                listener.onMovementPacket(player, packet);
            }
        if (packet.isChangePosition()) player.tickRollback(packet.getX(), packet.getY(), packet.getZ(), false);
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> {
                if (packet instanceof PlayerMoveC2SPacket) {
                    MovementPacketCallback.EVENT.invoker().onMovementPacket(player, (PlayerMoveC2SPacketView)packet);
                }
                return ActionResult.PASS;
            }
        );
    }

    void onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet);
}
