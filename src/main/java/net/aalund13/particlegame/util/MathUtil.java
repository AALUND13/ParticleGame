package net.aalund13.particlegame.util;

public class MathUtil {
    public static double clamp(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }
}
