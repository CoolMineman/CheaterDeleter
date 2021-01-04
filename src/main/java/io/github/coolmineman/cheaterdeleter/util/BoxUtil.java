package io.github.coolmineman.cheaterdeleter.util;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.util.math.Box;

public class BoxUtil {
    private BoxUtil() { }

    public static Box getBoxForPosition(CDPlayer player, double posx, double posy, double posz) {
        return player.mcPlayer.getDimensions(player.mcPlayer.getPose()).method_30231(posx, posy, posz); // method_30231 -> withPos
    }

    public static Box withNewMinY(Box box, double minY) {
        return new Box(box.minX, minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }
}
