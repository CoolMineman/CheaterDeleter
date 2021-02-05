package io.github.coolmineman.cheaterdeleter.modules.movement.entity;

import com.google.common.util.concurrent.AtomicDouble;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.events.PlayerEndTickCallback;
import io.github.coolmineman.cheaterdeleter.events.VehicleMoveListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import io.github.coolmineman.cheaterdeleter.util.BlockCollisionUtil;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Box;

public class EntitySpeedCheck extends CDModule implements VehicleMoveListener, PlayerEndTickCallback {

    public EntitySpeedCheck() {
        super("entity_speed_check");
        VehicleMoveListener.EVENT.register(this);
        PlayerEndTickCallback.EVENT.register(this);
    }

    public static class SpeedCheckData {
        AtomicDouble distance = new AtomicDouble(0.0);
    }

    @Override
    public void onPlayerEndTick(CDPlayer player) {
        if (!enabledFor(player))
            return;
        CDEntity entity = player.getVehicleCd();
        if (player.getWorld().getTime() % 40 == 0 && entity != null) {
            SpeedCheckData data = player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new);
            double distance = data.distance.getAndSet(0.0);
            double speed = entity.getSpeed();
            if (!Double.isFinite(speed)) return;
            if (speed < 1) speed += (1 + speed) * (1 + speed) * 0.1f; // B r u h
            double magicNumber = distance / speed;
            if (magicNumber > 71) {
                if (flag(player, FlagSeverity.MINOR, "Entity Speed Check " + magicNumber + " " + entity.getSpeed()))
                    player.rollback();
            }
        }
    }

    @Override
    public void onVehicleMove(CDPlayer player, CDEntity vehicle, PlayerMoveC2SPacketView playerLook, PlayerInputC2SPacket playerInput, VehicleMoveC2SPacket vehicleMoveC2SPacket, @Nullable VehicleMoveC2SPacket lastVehicleMoveC2SPacket) {
        if (!enabledFor(player) || vehicle == null)
            return;
        double oldX;
        // double oldY;
        double oldZ;
        if (lastVehicleMoveC2SPacket == null) {
            oldX = vehicle.getX();
            // oldY = vehicle.getY();
            oldZ = vehicle.getZ();
        } else {
            oldX = lastVehicleMoveC2SPacket.getX();
            // oldY = lastVehicleMoveC2SPacket.getY();
            oldZ = lastVehicleMoveC2SPacket.getZ();
        }
        if (!(System.currentTimeMillis() - player.getTracked(Trackers.PLAYER_LAST_TELEPORT_TRACKER).lastTeleport < 100 || BlockCollisionUtil.isNearby(player, 2, 4, BlockCollisionUtil.SPLIPPERY))) {
            double distanceSquared = MathUtil.getDistanceSquared(oldX, oldZ, vehicleMoveC2SPacket.getX(), vehicleMoveC2SPacket.getZ());
            double distance = Math.sqrt(distanceSquared);
            Box box = vehicle.getBoxForPosition(vehicleMoveC2SPacket.getX(), vehicleMoveC2SPacket.getY(), vehicleMoveC2SPacket.getZ()).expand(0.1, -0.1, 0.1);
            if (!BlockCollisionUtil.isTouching(box, player.getWorld(), BlockCollisionUtil.touchingPredicate(box)))
                player.getOrCreateData(SpeedCheckData.class, SpeedCheckData::new).distance.addAndGet(distance); // Bruh
        }
    }
    
}
