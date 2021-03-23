package io.github.coolmineman.cheaterdeleter.modules.rotation;

import java.util.UUID;

import io.github.coolmineman.cheaterdeleter.events.PlayerRotationListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.world.RaycastContext.FluidHandling;

public class LockDetector extends CDModule implements PlayerRotationListener {
    public LockDetector() {
        super("lock_detector");
        PlayerRotationListener.EVENT.register(this);
    }

    @Override
    public void onRotate(CDPlayer player, float yawDelta, float pitchDelta, double move, PlayerMoveC2SPacketView packet) {
        if (!enabledFor(player)) return;
        double x = packet.isChangePosition() ? packet.getX() : player.getPacketX();
        double y = packet.isChangePosition() ? packet.getY() : player.getPacketY();
        double z = packet.isChangePosition() ? packet.getZ() : player.getPacketZ();
        HitResult result = player.raycast(x, y, z, packet.getYaw(), packet.getPitch(), 6, FluidHandling.NONE);

        if (result.getType() == Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult)result;
            double offsetx = result.getPos().getX() - entityHitResult.getEntity().getX();
            double offsetz = result.getPos().getZ() - entityHitResult.getEntity().getZ();

            LockDetectorData data = player.getOrCreateData(LockDetectorData.class, LockDetectorData::new);
            if (data.target == null || !data.target.equals(entityHitResult.getEntity().getUuid())) {
                data.offsetx = offsetx;
                data.offsetz = offsetz;
                data.lock = 0;
                data.target = entityHitResult.getEntity().getUuid();
            } else {
                double distance = MathUtil.getDistanceSquared(offsetx, offsetz, data.offsetx, data.offsetz);
                if (move > 2 && pitchDelta > 0.01 && distance < 0.001) {
                    ++data.lock;
                    if (data.lock > 3) {
                        flag(player, FlagSeverity.MAJOR, "Entity Lock (Killaura)");
                    }
                } else if (move > 2) {
                    data.target = null;
                }
            }
        } 
    }

    public static class LockDetectorData {
        double offsetx;
        double offsetz;
        UUID target;
        int lock;
    }
}
