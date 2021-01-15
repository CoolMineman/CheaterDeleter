package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

import io.github.coolmineman.cheaterdeleter.events.VehicleMoveC2SPacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;

public class EntityVerticalCheck extends CDModule implements VehicleMoveC2SPacketCallback {
    public EntityVerticalCheck() {
        VehicleMoveC2SPacketCallback.EVENT.register(this);
    }

    @Override
    public long getFlagCoolDownMs() {
        return 100;
    }

    @Override
    public ActionResult onVehicleMoveC2SPacket(CDPlayer player, VehicleMoveC2SPacket packet) {
        CDEntity vehicle = player.getVehicleCd();
        if (player.shouldBypassAnticheat() || vehicle == null) return ActionResult.PASS;
        EntityVerticalCheckData verticalCheckData = player.getOrCreateData(EntityVerticalCheckData.class, EntityVerticalCheckData::new);
        if (vehicle.asMcEntity().isSwimming() || BlockCollisionUtil.isNearby(player, 2.0, 4.0, BlockCollisionUtil.NON_SOLID_COLLISION)) {
            verticalCheckData.isActive = false;
            return ActionResult.PASS;
        }
        Box vehicleBox = vehicle.getBoxForPosition(packet.getX(), packet.getY(), packet.getZ()).expand(0.01);
        Box scanBox = vehicleBox.expand(0.6);
        boolean vehicleOnGround = CollisionUtil.isTouching(new CDEntity[]{player, vehicle}, scanBox, vehicle.getWorld(), CollisionUtil.touchingRigidTopPredicates(vehicleBox));
        if (vehicle.isOnGround() && !vehicleOnGround && vehicle.getVelocity().getY() < 0.45) {
            verticalCheckData.maxY = vehicle.getY() + vehicle.getMaxJumpHeight();
            verticalCheckData.isActive = true;
        } else if (vehicleOnGround) {
            if (verticalCheckData != null && verticalCheckData.isActive) {
                verticalCheckData.isActive = false;
            }
        } else { //Packet off ground
            if (verticalCheckData.isActive && packet.getY() > verticalCheckData.maxY) {
                if (flag(player, FlagSeverity.MINOR, "Failed Entity Vertical Movement Check " + (verticalCheckData.maxY - packet.getY()))) PunishUtil.groundBoat(player, vehicle);
            }
            if (!verticalCheckData.isActive && vehicle.getVelocity().getY() < 0.45) {
                verticalCheckData.maxY = vehicle.getY() + vehicle.getMaxJumpHeight();
                verticalCheckData.isActive = true;
            }

        }
        return ActionResult.PASS;
    }
    
    private class EntityVerticalCheckData {
        volatile double maxY = 0.0;
        boolean isActive = false;
    }
}
