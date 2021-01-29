package io.github.coolmineman.cheaterdeleter.events;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public interface VehicleMoveListener {
    Event<VehicleMoveListener> EVENT = EventFactory.createArrayBacked(VehicleMoveListener.class,
        listeners -> (player, vehicle, playerLook, playerInput, vehicleMoveC2SPacket, lastVehicleMoveC2SPacket) -> {
            for (VehicleMoveListener listener : listeners) {
                listener.onVehicleMove(player, vehicle, playerLook, playerInput, vehicleMoveC2SPacket, lastVehicleMoveC2SPacket);
            }
            player.tickRollback(player.getX(), player.getY(), player.getZ(), false); //TODO Make this good
    });
    
    void onVehicleMove(CDPlayer player, CDEntity vehicle, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket, @Nullable VehicleMoveC2SPacket lastVehicleMoveC2SPacket);
}
