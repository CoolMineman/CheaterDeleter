package io.github.coolmineman.cheaterdeleter.util;

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

    private static double round(double d) {
        return Math.abs(d) > 0.00001 ? d : 0;
    }
}
