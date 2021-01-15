package io.github.coolmineman.cheaterdeleter.util;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class CollisionUtil {
    private CollisionUtil() { }

    /**
     * @return If box2 intersects the top of box1
     */
    public static boolean intersectsTop(@Nullable Box box1, @Nullable Box box2) {
        if (box1 == null || box2 == null) return false;
        return box1.maxY <= box2.maxY && box1.maxY >= box2.minY;
    }

    public static Pair<BiPredicate<World, BlockPos>, Predicate<CDEntity>> touchingRigidTopPredicates(Box box) {
        return new Pair<>(BlockCollisionUtil.touchingPredicate(box), EntityCollisionUtil.touchingRigidTopPredicate(box));
    }

    public static boolean isTouching(@Nullable CDEntity excludeEntity, Box box, World world, Pair<BiPredicate<World, BlockPos>, Predicate<CDEntity>> predicates) {
        return BlockCollisionUtil.isTouching(box, world, predicates.getFirst()) || EntityCollisionUtil.isTouching(excludeEntity, box, world, predicates.getSecond());
    }
}
