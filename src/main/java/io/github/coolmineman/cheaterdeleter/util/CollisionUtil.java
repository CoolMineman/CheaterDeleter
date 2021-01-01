package io.github.coolmineman.cheaterdeleter.util;

import java.util.function.Predicate;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class CollisionUtil {
    private CollisionUtil() { }

    public static final Predicate<BlockState> NON_SOLID_COLLISION = state -> {
        if (!state.getFluidState().isEmpty()) return true;
        if (state.getMaterial().isLiquid()) return true;
        if (state.getBlock() instanceof SlimeBlock) return true;
        if (state.getBlock() instanceof BedBlock) return true;
        return false;
    };

    public static boolean isNearby(ServerPlayerEntity player, Predicate<BlockState> predicate) {
        return isNearby(player.getBoundingBox().expand(2.0, 4.0, 2.0), player.world, predicate);
    }

    public static boolean isNearby(Box box, World world, Predicate<BlockState> predicate) {
        return BlockPos.stream(box).anyMatch(pos -> predicate.test(world.getBlockState(pos)));
    }
}
