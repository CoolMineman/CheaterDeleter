package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.ActionResult;

public interface VehicleMoveC2SPacketCallback {
    Event<VehicleMoveC2SPacketCallback> EVENT = EventFactory.createArrayBacked(VehicleMoveC2SPacketCallback.class,
        listeners -> (player, packet) -> {
            for (VehicleMoveC2SPacketCallback listener : listeners) {
                ActionResult result = listener.onVehicleMoveC2SPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }
        return ActionResult.PASS;
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> 
            packet instanceof VehicleMoveC2SPacket ? VehicleMoveC2SPacketCallback.EVENT.invoker().onVehicleMoveC2SPacket(player, (VehicleMoveC2SPacket)packet) : ActionResult.PASS
        );
    }

    ActionResult onVehicleMoveC2SPacket(CDPlayer player, VehicleMoveC2SPacket packet);
}
