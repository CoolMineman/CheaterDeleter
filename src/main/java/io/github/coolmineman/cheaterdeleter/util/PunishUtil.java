package io.github.coolmineman.cheaterdeleter.util;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PunishUtil {
    private PunishUtil() { }

    public static void groundPlayer(CDPlayer player) {
        Box box = BoxUtil.withNewMinY(player.getBox(), 0);
        World world = player.getWorld();
        double newY = BlockPos.stream(box).mapToDouble(pos -> CollisionUtil.getHighestTopIntersection(world.getBlockState(pos).getCollisionShape(world, pos).offset(pos.getX(), pos.getY(), pos.getZ()), box, -100)).max().orElse(-100);
        player.teleport(player.getX(), newY, player.getZ());
    }
}
