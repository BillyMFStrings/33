package com.gaspoweredcake.client33.utils.render;

import com.gaspoweredcake.client33.renderer.Renderer3D;
import com.gaspoweredcake.client33.utils.render.color.Color;

/** Bounded procedural geometry shared by entity and combat visuals. */
public final class VisualGeometry {
    private VisualGeometry() {}

    public static void ring(Renderer3D renderer, double x, double y, double z, double radius,
                            double phase, double sweep, int segments, Color color) {
        for (int i = 0; i < segments; i++) {
            double a = phase + sweep * i / segments;
            double b = phase + sweep * (i + 1) / segments;
            renderer.line(x + Math.cos(a) * radius, y, z + Math.sin(a) * radius,
                x + Math.cos(b) * radius, y, z + Math.sin(b) * radius, color);
        }
    }

    public static void band(Renderer3D renderer, double x, double y, double z, double radius,
                            double height, int segments, Color color) {
        Color clear = new Color(color.r, color.g, color.b, 0);
        for (int i = 0; i < segments; i++) {
            double a = Math.PI * 2 * i / segments, b = Math.PI * 2 * (i + 1) / segments;
            double ax = x + Math.cos(a) * radius, az = z + Math.sin(a) * radius;
            double bx = x + Math.cos(b) * radius, bz = z + Math.sin(b) * radius;
            renderer.quad(ax, y, az, bx, y, bz, bx, y + height, bz, ax, y + height, az,
                color, color, clear, clear);
        }
    }

    public static double progress(double elapsed, double duration) {
        if (!Double.isFinite(elapsed) || !Double.isFinite(duration) || duration <= 0) return 1;
        return Math.max(0, Math.min(1, elapsed / duration));
    }
}
