/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.Stop;
import java.util.List;

public final class RadialGradient
extends Gradient {
    private final float centerX;
    private final float centerY;
    private final float focusAngle;
    private final float focusDistance;
    private final float radius;

    public RadialGradient(float f2, float f3, float f4, float f5, float f6, BaseTransform baseTransform, boolean bl, int n2, List<Stop> list) {
        super(Paint.Type.RADIAL_GRADIENT, baseTransform, bl, n2, list);
        this.centerX = f2;
        this.centerY = f3;
        this.focusAngle = f4;
        this.focusDistance = f5;
        this.radius = f6;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public float getFocusAngle() {
        return this.focusAngle;
    }

    public float getFocusDistance() {
        return this.focusDistance;
    }

    public float getRadius() {
        return this.radius;
    }

    public String toString() {
        return "RadialGradient: FocusAngle: " + this.focusAngle + " FocusDistance: " + this.focusDistance + " CenterX: " + this.centerX + " CenterY " + this.centerY + " Radius: " + this.radius + "stops:" + this.getStops();
    }
}

