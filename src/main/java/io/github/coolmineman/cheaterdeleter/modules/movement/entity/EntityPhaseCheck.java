package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

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
    public void onVehicleMove(CDPlayer player, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket) {
        if (!enabledFor(player)) return;
        World world = player.getWorld();
        CDEntity entity = player.getVehicleCd();
        if (entity == null) return;
        Box box = entity.getBoxForPosition(vehicleMoveC2SPacket.getX(), vehicleMoveC2SPacket.getY(), vehicleMoveC2SPacket.getZ()).expand(-0.1);
        if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(entity.getStepHeight(), box, entity.getY(), vehicleMoveC2SPacket.getY()))), player, FlagSeverity.MAJOR, "Failed Entity Phase Check1")) return;
        double currentX = entity.getX();
        double currentY = entity.getY();
        double currentZ = entity.getZ();
        double targetX = vehicleMoveC2SPacket.getX();
        boolean targetXPositive = targetX > entity.getX();
        boolean hitTargetX = false;
        double targetY = vehicleMoveC2SPacket.getY();
        boolean targetYPositive = targetY > entity.getY();
        boolean hitTargetY = false;
        double targetZ = vehicleMoveC2SPacket.getZ();
        boolean targetZPositive = targetZ > entity.getZ();
        boolean hitTargetZ = false;
        while (!(hitTargetX && hitTargetY && hitTargetZ)) {
            box = entity.getBoxForPosition(currentX, currentY, currentZ).expand(-0.1);
            if (assertOrFlag(!BlockCollisionUtil.isTouching(box, world, Trackers.PHASE_BYPASS_TRACKER.isNotBypassed(player).and(BlockCollisionUtil.touchingNonSteppablePredicate(entity.getStepHeight(), box, entity.getY(), vehicleMoveC2SPacket.getY()))), player, FlagSeverity.MAJOR, "Failed Entity Phase Check2")) return;
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
