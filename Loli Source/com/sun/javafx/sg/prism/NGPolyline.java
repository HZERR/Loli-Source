/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;

public class NGPolyline
extends NGShape {
    private Path2D path = new Path2D();

    public void updatePolyline(float[] arrf) {
        this.path.reset();
        if (arrf == null || arrf.length == 0 || arrf.length % 2 != 0) {
            return;
        }
        this.path.moveTo(arrf[0], arrf[1]);
        for (int i2 = 1; i2 < arrf.length / 2; ++i2) {
            float f2 = arrf[i2 * 2 + 0];
            float f3 = arrf[i2 * 2 + 1];
            this.path.lineTo(f2, f3);
        }
        this.geometryChanged();
    }

    @Override
    public Shape getShape() {
        return this.path;
    }
}

