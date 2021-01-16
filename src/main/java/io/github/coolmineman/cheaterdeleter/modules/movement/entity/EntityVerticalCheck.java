package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

import io.github.coolmineman.cheaterdeleter.events.VehicleMoveListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.CollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Box;

public class EntityVerticalCheck extends CDModule implements VehicleMoveListener {
    public EntityVerticalCheck() {
        VehicleMoveListener.EVENT.register(this);
    }

    @Override
    public long getFlagCoolDownMs() {
        return 100;
    }

    private class EntityVerticalCheckData {
        volatile double maxY = 0.0;
        boolean isActive = false;
    }

    @Override
    public void onVehicleMove(CDPlayer player, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket) {
            CDEntity vehicle = player.getVehicleCd();
            if (player.shouldBypassAnticheat() || vehicle == null) return;
            EntityVerticalCheckData verticalCheckData = player.getOrCreateData(EntityVerticalCheckData.class,
                    EntityVerticalCheckData::new);
            if (vehicle.asMcEntity().isSwimming()
                    || BlockCollisionUtil.isNearby(player, 2.0, 4.0, BlockCollisionUtil.NON_SOLID_COLLISION)) {
                verticalCheckData.isActive = false;
                return;
            }
            Box vehicleBox = vehicle.getBoxForPosition(vehicleMoveC2SPacket.getX(), vehicleMoveC2SPacket.getY(), vehicleMoveC2SPacket.getZ()).expand(0.01);
            Box scanBox = vehicleBox.expand(0.6);
            boolean vehicleOnGround = CollisionUtil.isTouching(new CDEntity[] { player, vehicle }, scanBox,
                    vehicle.getWorld(), CollisionUtil.touchingRigidTopPredicates(vehicleBox));
            if (vehicle.isOnGround() && !vehicleOnGround && vehicle.getVelocity().getY() < 0.45) {
                verticalCheckData.maxY = vehicle.getY() + vehicle.getMaxJumpHeight();
                verticalCheckData.isActive = true;
            } else if (vehicleOnGround) {
                if (verticalCheckData != null && verticalCheckData.isActive) {
                    verticalCheckData.isActive = false;
                }
            } else { // Packet off ground
                if (verticalCheckData.isActive && vehicleMoveC2SPacket.getY() > verticalCheckData.maxY) {
                    if (flag(player, FlagSeverity.MAJOR,
                            "Failed Entity Vertical Movement Check " + (verticalCheckData.maxY - vehicleMoveC2SPacket.getY())))
                        PunishUtil.groundBoat(player, vehicle);
                }
                if (!verticalCheckData.isActive && vehicle.getVelocity().getY() < 0.45) {
                    verticalCheckData.maxY = vehicle.getY() + vehicle.getMaxJumpHeight();
                    verticalCheckData.isActive = true;
                }
    
            }
    }
}
