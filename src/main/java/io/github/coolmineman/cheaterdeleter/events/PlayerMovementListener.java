package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public interface PlayerMovementListener {
    Event<PlayerMovementListener> EVENT = EventFactory.createArrayBacked(PlayerMovementListener.class,
        listeners -> (player, packet, cause) -> {
            for (PlayerMovementListener listener : listeners) {
                listener.onMovement(player, packet, cause);
            }
        if (packet.isChangePosition()) player.tickRollback(packet.getX(), packet.getY(), packet.getZ(), false);
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> {
                if (packet instanceof PlayerMoveC2SPacket) {
                    PlayerMovementListener.EVENT.invoker().onMovement(player, (PlayerMoveC2SPacketView)packet, MoveCause.OTHER);
                }
                return ActionResult.PASS;
            }
        );
    }

    public enum MoveCause {
        TELEPORT,
        OTHER;

        public boolean isTeleport() {
            return this == TELEPORT;
        }
    }

    void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause);
}
