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

    public static HitResult raycastInDirection(CDEntity entity, Vec3d direction, double maxDistance, RaycastContext.FluidHandling fluidHandling) {
        HitResult target = raycast(entity.asMcEntity(), maxDistance, fluidHandling, direction);
     
        Vec3d cameraPos = entity.asMcEntity().getCameraPosVec(1);
     
        double extendedReach = maxDistance * maxDistance;
        if (target != null && target.getType() != HitResult.Type.MISS) {
            extendedReach = target.getPos().squaredDistanceTo(cameraPos);
        }
     
        Vec3d vec3d3 = cameraPos.add(direction.multiply(maxDistance));
        Box box = entity
                .getBox()
                .stretch(entity.asMcEntity().getRotationVec(1.0F).multiply(maxDistance))
                .expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = projectileUtilRaycast(
                entity.asMcEntity(),
                cameraPos,
                vec3d3,
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

    private static EntityHitResult projectileUtilRaycast(Entity entity, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, double d) {
        World world = entity.world;
        double e = d;
        Entity entity2 = null;
        Vec3d vec3d3 = null;
        Iterator<Entity> var12 = world.getOtherEntities(entity, box, predicate).iterator();

        while(true) {
            while(var12.hasNext()) {
                Entity entity3 = var12.next();
                Box box2 = entity3.getBoundingBox().expand((double)entity3.getTargetingMargin());
                Optional<Vec3d> optional = box2.raycast(vec3d, vec3d2);
                if (box2.contains(vec3d)) {
                if (e >= 0.0D) {
                    entity2 = entity3;
                    vec3d3 = optional.orElse(vec3d);
                    e = 0.0D;
                }
                } else if (optional.isPresent()) {
                Vec3d vec3d4 = optional.get();
                double f = vec3d.squaredDistanceTo(vec3d4);
                if (f < e || e == 0.0D) {
                    if (entity3.getRootVehicle() == entity.getRootVehicle()) {
                        if (e == 0.0D) {
                            entity2 = entity3;
                            vec3d3 = vec3d4;
                        }
                    } else {
                        entity2 = entity3;
                        vec3d3 = vec3d4;
                        e = f;
                    }
                }
                }
            }

            if (entity2 == null) {
                return null;
            }

            return new EntityHitResult(entity2, vec3d3);
        }
    }
     
    private static HitResult raycast(
            Entity entity,
            double maxDistance,
            RaycastContext.FluidHandling fluidHandling,
            Vec3d direction
    ) {
        Vec3d end = entity.getCameraPosVec(1).add(direction.multiply(maxDistance));
        return entity.world.raycast(new RaycastContext(
                entity.getCameraPosVec(1),
                end,
                RaycastContext.ShapeType.COLLIDER,
                fluidHandling,
                entity
        ));
    }

    private static double round(double d) {
        return Math.abs(d) > 0.00001 ? d : 0;
    }
}
