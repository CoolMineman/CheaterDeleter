package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public interface VehicleMoveListener {
    Event<VehicleMoveListener> EVENT = EventFactory.createArrayBacked(VehicleMoveListener.class,
        listeners -> (player, playerLook, playerInput, vehicleMoveC2SPacket) -> {
            for (VehicleMoveListener listener : listeners) {
                listener.onVehicleMove(player, playerLook, playerInput, vehicleMoveC2SPacket);
            }
    });
    
    void onVehicleMove(CDPlayer player, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket);
}
