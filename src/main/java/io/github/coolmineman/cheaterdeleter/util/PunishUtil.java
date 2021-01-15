package io.github.coolmineman.cheaterdeleter.util;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PunishUtil {
    private PunishUtil() { }

    public static void groundPlayer(CDPlayer player) {
        Box box = BoxUtil.withNewMinY(player.getBox(), 0);
        World world = player.getWorld();
        double newY = BlockPos.stream(box).mapToDouble(pos -> BlockCollisionUtil.getHighestTopIntersection(world.getBlockState(pos).getCollisionShape(world, pos).offset(pos.getX(), pos.getY(), pos.getZ()), box, -100)).max().orElse(-100);
        player.teleport(player.getX(), newY, player.getZ());
    }

    public static void groundBoat(CDPlayer player, CDEntity entity) {
        Box box = BoxUtil.withNewMinY(entity.getBox(), 0);
        World world = entity.getWorld();
        double newY = BlockPos.stream(box).mapToDouble(
            pos -> {
                BlockState state = world.getBlockState(pos);
                if (!state.getFluidState().isEmpty()) return pos.getY() + 1;
                if (state.getMaterial().isLiquid()) return pos.getY() + 1;
                return BlockCollisionUtil.getHighestTopIntersection(state.getCollisionShape(world, pos).offset(pos.getX(), pos.getY(), pos.getZ()), box, -100);
            }
        ).max().orElse(-100);
        entity.asMcEntity().setPos(entity.getX(), newY, entity.getZ());
        player.getNetworkHandler().sendPacket(new VehicleMoveS2CPacket(entity.asMcEntity()));
    }
}
