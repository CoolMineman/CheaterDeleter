package io.github.coolmineman.cheaterdeleter.util;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.apache.commons.lang3.mutable.MutableBoolean;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.EntityPose;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class CollisionUtil {
    private CollisionUtil() { }

    public static final BiPredicate<World, BlockPos> NON_SOLID_COLLISION = (world, pos) -> {
        BlockState state = world.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) return true;
        if (state.getMaterial().isLiquid()) return true;
        if (state.getBlock() instanceof SlimeBlock) return true;
        if (state.getBlock() instanceof BedBlock) return true;
        return false;
    };

    public static BiPredicate<World, BlockPos> steppablePredicate(float stepheight) {
        return (world, pos) -> {
            VoxelShape shape = world.getBlockState(pos).getCollisionShape(world, pos);
            MutableBoolean steppable = new MutableBoolean(false);
            shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                if (maxY <= stepheight) steppable.setTrue();
            });
            return steppable.booleanValue();
        };
    }

    public static boolean isNearby(ServerPlayerEntity player, double expandHorizontal, double expandVertical, BiPredicate<World, BlockPos> predicate) {
        return isTouching(player.getBoundingBox().expand(expandHorizontal, expandVertical, expandHorizontal), player.world, predicate);
    }

    public static boolean isNearby(ServerPlayerEntity player, double posx, double posy, double posz, double expandHorizontal, double expandVertical, BiPredicate<World, BlockPos> predicate) {
        return isTouching(player.getDimensions(EntityPose.STANDING).method_30231(posx, posy, posz).expand(expandHorizontal, expandVertical, expandHorizontal), player.world, predicate); // method_30231 -> withPos
    }

    public static boolean isTouching(Box box, World world, Predicate<BlockState> predicate) {
        return isTouching(box, world, (world1, pos) -> predicate.test(world1.getBlockState(pos)));
    }

    public static boolean isTouching(Box box, World world, BiPredicate<World, BlockPos> predicate) {
        return BlockPos.stream(box).anyMatch(pos -> predicate.test(world, pos));
    }
}
