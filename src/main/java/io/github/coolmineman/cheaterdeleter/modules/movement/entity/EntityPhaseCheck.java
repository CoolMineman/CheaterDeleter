package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.events.VehicleMoveListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

//TODO Rollback
public class EntityPhaseCheck extends CDModule implements VehicleMoveListener {
    private static final double INTERP = 0.7;

    public EntityPhaseCheck() {
        super("entity_phase_check");
        VehicleMoveListener.EVENT.register(this);
    }

    @Override
    public void onVehicleMove(CDPlayer player, CDEntity vehicle, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket, @Nullable VehicleMoveC2SPacket lastVehicleMoveC2SPacket) {
        if (!enabledFor(player)) return;
        World world = player.getWorld();
        if (vehicle == null) return;
        Box box = vehicle.getBoxForPosition(vehicleMoveC2SPacket.getX(), vehicleMoveC2SPacket.getY(), vehicleMoveC2SPacket.getZ()).expand(-0.1);
        if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(vehicle.getStepHeight(), box, vehicle.getY(), vehicleMoveC2SPacket.getY()))), player, FlagSeverity.MAJOR, "Failed Entity Phase Check1")) return;
        double currentX = vehicle.getX();
        double currentY = vehicle.getY();
        double currentZ = vehicle.getZ();
        double targetX = vehicleMoveC2SPacket.getX();
        boolean targetXPositive = targetX > vehicle.getX();
        boolean hitTargetX = false;
        double targetY = vehicleMoveC2SPacket.getY();
        boolean targetYPositive = targetY > vehicle.getY();
        boolean hitTargetY = false;
        double targetZ = vehicleMoveC2SPacket.getZ();
        boolean targetZPositive = targetZ > vehicle.getZ();
        boolean hitTargetZ = false;
        while (!(hitTargetX && hitTargetY && hitTargetZ)) {
            box = vehicle.getBoxForPosition(currentX, currentY, currentZ).expand(-0.1);
            if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(vehicle.getStepHeight(), box, vehicle.getY(), vehicleMoveC2SPacket.getY()))), player, FlagSeverity.MAJOR, "Failed Entity Phase Check2")) return;
            if (!hitTargetX) {
                if (targetXPositive) {
                    if (currentX + INTERP < targetX) {
                        currentX += INTERP;
                    } else {
                        hitTargetX = true;
                    }
                } else {
                    if (currentX - INTERP > targetX) {
                        currentX -= INTERP;
                    } else {
                        hitTargetX = true;
                    }
                }
            }
            if (!hitTargetY) {
                if (targetYPositive) {
                    if (currentY + INTERP < targetY) {
                        currentY += INTERP;
                    } else {
                        hitTargetY = true;
                    }
                } else {
                    if (currentY - INTERP > targetY) {
                        currentY -= INTERP;
                    } else {
                        hitTargetY = true;
                    }
                }
            }
            if (!hitTargetZ) {
                if (targetZPositive) {
                    if (currentZ + INTERP < targetZ) {
                        currentZ += INTERP;
                    } else {
                        hitTargetZ = true;
                    }
                } else {
                    if (currentZ - INTERP > targetZ) {
                        currentZ -= INTERP;
                    } else {
                        hitTargetZ = true;
                    }
                }
            }
        }
    }

    @Override
    public long getFlagCoolDownMs() {
        return 0;
    }
}
