package io.github.coolmineman.cheaterdeleter.util;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.util.math.Box;

public class BoxUtil {
    private BoxUtil() { }

    public static Box getBoxForPosition(CDPlayer player, double posx, double posy, double posz) {
        return player.asMcPlayer().getDimensions(player.asMcPlayer().getPose()).method_30231(posx, posy, posz); // method_30231 -> withPos
    }

    public static Box withNewMinY(Box box, double minY) {
        return new Box(box.minX, minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }
}
