package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.events.VehicleMoveListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Box;

public class BoatFlyCheck extends CDModule implements VehicleMoveListener {
    public BoatFlyCheck() {
        super("boat_fly_check");
        VehicleMoveListener.EVENT.register(this);
    }

    @Override
    public long getFlagCoolDownMs() {
        return 100;
    }

    // TODO: Slime Support
    @Override
    public void onVehicleMove(CDPlayer player, CDEntity vehicle, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket, @Nullable VehicleMoveC2SPacket lastVehicleMoveC2SPacket) {
        if (!enabledFor(player)) return;
        if (vehicle != null && lastVehicleMoveC2SPacket != null) {
            double ydelta = vehicleMoveC2SPacket.getY() - lastVehicleMoveC2SPacket.getY() - 0.001;
            Box box = vehicle.getBoxForPosition(vehicleMoveC2SPacket.getX(), vehicleMoveC2SPacket.getY(), vehicleMoveC2SPacket.getZ());
            if (vehicle.getStepHeight() == 0 && ydelta > 0 && vehicle.getVelocity().getY() <= 0) {
                if (assertOrFlag(player.getWorld().getTime() - vehicle.getPistonMovementTick() < 100 || BlockCollisionUtil.isTouching(box, player.getWorld(), BlockCollisionUtil.LIQUID), player, FlagSeverity.MAJOR, "Boat Fly " + ydelta + " " + vehicle.getVelocity().getY()))
                    PunishUtil.groundBoat(player, vehicle);
            }
        }

    }
    
}
