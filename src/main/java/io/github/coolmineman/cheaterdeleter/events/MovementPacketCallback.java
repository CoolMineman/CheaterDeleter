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
                ActionResult result = listener.onMovementPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }
        if (packet.isChangePosition()) player.tickRollback(packet.getX(), packet.getY(), packet.getZ(), false);
        return ActionResult.PASS;
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> 
            packet instanceof PlayerMoveC2SPacket ? MovementPacketCallback.EVENT.invoker().onMovementPacket(player, (PlayerMoveC2SPacketView)packet) : ActionResult.PASS
        );
    }

    ActionResult onMovementPacket(CDPlayer player, PlayerMoveC2SPacketView packet);
}
