package io.github.coolmineman.cheaterdeleter.util;

import net.minecraft.util.math.Box;

public class BoxUtil {
    private BoxUtil() { }

    public static Box withNewMinY(Box box, double minY) {
        return new Box(box.minX, minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }
}
