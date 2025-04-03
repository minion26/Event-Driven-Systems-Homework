package org.example.util;

public class MathUtil {
    public static int clampInteger(int val, float min, float max) {
        return (int)Math.max(min, Math.min(max, val));
    }
}
