package io.github.coolmineman.cheaterdeleter.util;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class MathUtil {
    private MathUtil() { }

    public static double getDistanceSquared(double x1, double z1, double x2, double z2) {
        double deltaX = x2 - x1;
        double deltaZ = z2 - z1;
        return round((deltaX * deltaX) + (deltaZ * deltaZ));
    }

    public static double getDistanceSquared(double x1, double y1, double z1, double x2, double y2, double z2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double deltaZ = z2 - z1;
        return round((deltaX * deltaX) + (deltaY * deltaY)  + (deltaZ * deltaZ));
    }

    public static boolean roughlyEqual(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }

    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double)(i * j), (double)(-k), (double)(h * j));
    }

    public static boolean intersects(Box b, Vec3d from, Vec3d to) {
        return b.intersects(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z), Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
    }

    public static HitResult raycastInDirection(CDEntity entity, double x, double y, double z, Vec3d direction, double maxDistance, RaycastContext.FluidHandling fluidHandling) {
        HitResult target = raycast(entity.asMcEntity(), x, y, z, maxDistance, fluidHandling, direction);
     
        Vec3d cameraPos = new Vec3d(x, y + entity.asMcEntity().getStandingEyeHeight(), z);
     
        double extendedReach = maxDistance * maxDistance;
        if (target != null && target.getType() != HitResult.Type.MISS) {
            extendedReach = target.getPos().squaredDistanceTo(cameraPos);
        }
     
        Vec3d endPos = cameraPos.add(direction.multiply(maxDistance));
        Box box = entity
                .getBox()
                .stretch(entity.asMcEntity().getRotationVec(1.0F).multiply(maxDistance))
                .expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = projectileUtilRaycast(
                entity.asMcEntity(),
                cameraPos,
                endPos,
                box,
                entityx -> !entityx.isSpectator() && entityx.collides(),
                extendedReach
        );

        if (entityHitResult == null) {
            return target;
        }

        Vec3d vec3d4 = entityHitResult.getPos();
        double g = cameraPos.squaredDistanceTo(vec3d4);
        if (g < extendedReach || target == null) {
            target = entityHitResult;
        }
     
        return target;
    }

    private static EntityHitResult projectileUtilRaycast(Entity entity, Vec3d startPos, Vec3d endPos, Box box, Predicate<Entity> predicate, double maxDistance) {
        World world = entity.world;
        double searchDistance = maxDistance;
        Entity resultEntity = null;
        Vec3d resultPos = null;
        for (Entity other : world.getOtherEntities(entity, box, predicate)) {
            Box otherBox = other.getBoundingBox().expand((double)other.getTargetingMargin());
            Optional<Vec3d> oOtherBoxHit = otherBox.raycast(startPos, endPos);
            if (otherBox.contains(startPos)) {
                if (searchDistance >= 0.0D) {
                    resultEntity = other;
                    resultPos = oOtherBoxHit.orElse(startPos);
                    searchDistance = 0.0D;
                }
            } else if (oOtherBoxHit.isPresent()) {
                Vec3d otherBoxHit = oOtherBoxHit.get();
                double otherBoxHitDistance = startPos.squaredDistanceTo(otherBoxHit);
                if (otherBoxHitDistance < searchDistance || searchDistance == 0.0D) {
                    if (other.getRootVehicle() == entity.getRootVehicle()) {
                        if (searchDistance == 0.0D) {
                            resultEntity = other;
                            resultPos = otherBoxHit;
                        }
                    } else {
                        resultEntity = other;
                        resultPos = otherBoxHit;
                        searchDistance = otherBoxHitDistance;
                    }
                }
            }
        }

        if (resultEntity == null) {
            return null;
        }

        return new EntityHitResult(resultEntity, resultPos);
    }
     
    private static HitResult raycast(
            Entity entity,
            double x,
            double y,
            double z,
            double maxDistance,
            RaycastContext.FluidHandling fluidHandling,
            Vec3d direction
    ) {
        Vec3d end = offsetInDirection(x, y + entity.getStandingEyeHeight(), z, direction, maxDistance);
        return entity.world.raycast(new RaycastContext(
                new Vec3d(x, y + entity.getStandingEyeHeight(), z),
                end,
                RaycastContext.ShapeType.COLLIDER,
                fluidHandling,
                entity
        ));
    }

    public static Vec3d offsetInDirection(double x, double y, double z, Vec3d direction, double distance) {
        return new Vec3d((direction.x * distance) + x, (direction.y * distance) + y, (direction.z * distance) + z);
    }

    private static double round(double d) {
        return Math.abs(d) > 0.00001 ? d : 0;
    }
}
