package io.github.coolmineman.cheaterdeleter.trackers;

import org.jetbrains.annotations.NotNull;

import io.github.coolmineman.cheaterdeleter.events.OutgoingPacketListener;
import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.events.VehicleMoveListener;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.VehicleMoveS2CPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.data.VehicleMovePacketsData;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;

public class VehicleMovePacketsTracker extends Tracker<VehicleMovePacketsData> implements PacketCallback, OutgoingPacketListener {

    protected VehicleMovePacketsTracker() {
        super(VehicleMovePacketsData.class);
        PacketCallback.EVENT.register(this);
        OutgoingPacketListener.EVENT.register(this);
    }

    @Override
    public @NotNull VehicleMovePacketsData get(CDEntity entity) {
        return entity.getOrCreateData(VehicleMovePacketsData.class, VehicleMovePacketsData::new);
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        VehicleMovePacketsData data = get(player);
        if (packet instanceof PlayerMoveC2SPacketView) {
            PlayerMoveC2SPacketView playerLook = (PlayerMoveC2SPacketView) packet;
            CDEntity vehicle = player.getVehicleCd();
            if ((data.magic == -1 || data.magic == 0 || (vehicle != null && vehicle.asMcEntity().isLogicalSideForUpdatingMovement() && data.magic == 1)) && playerLook.isChangeLook() && !playerLook.isChangePosition()) {
                data.magic = 0;
                data.playerLook = playerLook;
            }
        } else if (packet instanceof PlayerInputC2SPacket) {
            PlayerInputC2SPacket playerInput = (PlayerInputC2SPacket) packet;
            if (data.magic == 0) {
                data.magic = 1;
                data.playerInput = playerInput;
            } else {
                player.kick(new LiteralText("Illegal PlayerInputC2SPacket"));
            }
        } else if (packet instanceof VehicleMoveC2SPacket) {
            VehicleMoveC2SPacket vehicleMove = (VehicleMoveC2SPacket) packet;
            if (data.magic == 1) {
                data.magic = -1;
                CDEntity vehicle = player.getVehicleCd();
                if (vehicle != null)
                    VehicleMoveListener.EVENT.invoker().onVehicleMove(player, vehicle, data.playerLook, data.playerInput, vehicleMove, data.lastVehicleMoveC2SPacket);
                data.lastVehicleMoveC2SPacket = vehicleMove;
                data.playerInput = null;
                data.playerLook = null;
            } else {
                if (!(data.lastVehicleMoveS2CPacket != null && (System.currentTimeMillis() - data.lastVehicleMoveS2CPacketTime < 30000) && areEqual(data.lastVehicleMoveS2CPacket, vehicleMove)))
                    player.kick(new LiteralText("Illegal VehicleMoveC2SPacket"));
            }
        } else {
            data.magic = -1;
        }
        return ActionResult.PASS;
    }

    private boolean areEqual(VehicleMoveS2CPacketView packet1, VehicleMoveC2SPacket packet2) {
        return packet1.getX() == packet2.getX() && packet1.getY() == packet2.getY() && packet1.getZ() == packet2.getZ() && packet1.getYaw() == packet2.getYaw() && packet1.getPitch() == packet2.getPitch();
    }

    @Override
    public void onOutgoingPacket(CDPlayer player, Packet<ClientPlayPacketListener> packet) {
        if (packet instanceof VehicleMoveS2CPacket) {
            VehicleMovePacketsData data = get(player);
            data.lastVehicleMoveS2CPacket = (VehicleMoveS2CPacketView)packet;
            data.lastVehicleMoveS2CPacketTime = System.currentTimeMillis();
        }
    }
    
}
