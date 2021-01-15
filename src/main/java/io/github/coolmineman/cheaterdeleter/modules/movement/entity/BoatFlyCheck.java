package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

import io.github.coolmineman.cheaterdeleter.events.VehicleMoveC2SPacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;

public class BoatFlyCheck extends CDModule implements VehicleMoveC2SPacketCallback {
    public BoatFlyCheck() {
        VehicleMoveC2SPacketCallback.EVENT.register(this);
    }

    @Override
    public long getFlagCoolDownMs() {
        return 100;
    }

    //TODO: Steppable Entity Support
    //TODO: Piston Support
    @Override
    public ActionResult onVehicleMoveC2SPacket(CDPlayer player, VehicleMoveC2SPacket packet) {
        CDEntity vehicle = player.getVehicleCd();
        if (vehicle != null) {
            double ydelta = packet.getY() - vehicle.getY() - 0.001;
            Box box = vehicle.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ());
            if (vehicle.getStepHeight() == 0 && ydelta > 0 && vehicle.getVelocity().getY() <= 0) {
                if (assertOrFlag(BlockCollisionUtil.isTouching(box, player.getWorld(), BlockCollisionUtil.steppablePredicate(vehicle.getStepHeight()).or(BlockCollisionUtil.LIQUID)), player, FlagSeverity.MAJOR, "Boat Fly " + ydelta + " " + vehicle.getVelocity().getY()))
                    PunishUtil.groundBoat(player, vehicle);
            }
        }
        return ActionResult.PASS;
    }
    
}
