package io.github.coolmineman.cheaterdeleter.util;

import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class EntityCollisionUtil {
    private EntityCollisionUtil() { }

    public static Predicate<CDEntity> touchingRigidTopPredicate(Box box) {
        return entity -> CollisionUtil.intersectsTop(entity.getRigidCollision(), box);
    }

    public static Predicate<CDEntity> steppablePredicate(float stepheight) {
        return entity -> {
            Box box = entity.getRigidCollision();
            if (box == null) return false;
            return box.maxY - entity.getY() <= stepheight;
        };
    }

    public static boolean isTouching(@Nullable CDEntity excludeEntity, Box box, World world, Predicate<CDEntity> predicate) {
        for (Entity e : world.getOtherEntities(excludeEntity == null ? null : excludeEntity.asMcEntity(), box, null)) {
            if (predicate.test(CDEntity.of(e))) return true;
        }
        return false;
    }

    public static boolean isTouching(CDEntity[] excludeEntites, Box box, World world, Predicate<CDEntity> predicate) {
        for (Entity e : world.getOtherEntities(null, box, null)) {
            CDEntity e1 = CDEntity.of(e);
            if (ArrayUtils.contains(excludeEntites, e1)) continue;
            if (predicate.test(e1)) return true;
        }
        return false;
    }
}
