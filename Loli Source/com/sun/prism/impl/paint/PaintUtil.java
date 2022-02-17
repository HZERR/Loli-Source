/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.paint.LinearGradientContext;
import com.sun.prism.impl.paint.MultipleGradientContext;
import com.sun.prism.impl.paint.RadialGradientContext;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;

public class PaintUtil {
    private static final Affine2D gradXform = new Affine2D();

    public static void fillImageWithGradient(int[] arrn, Gradient gradient, BaseTransform baseTransform, int n2, int n3, int n4, int n5, float f2, float f3, float f4, float f5) {
        MultipleGradientContext multipleGradientContext;
        Object object;
        Gradient gradient2 = gradient;
        int n6 = gradient2.getNumStops();
        float[] arrf = new float[n6];
        Color[] arrcolor = new Color[n6];
        for (int i2 = 0; i2 < n6; ++i2) {
            object = gradient2.getStops().get(i2);
            arrf[i2] = ((Stop)object).getOffset();
            arrcolor[i2] = ((Stop)object).getColor();
        }
        if (gradient.getType() == Paint.Type.LINEAR_GRADIENT) {
            float f6;
            float f7;
            float f8;
            float f9;
            object = (LinearGradient)gradient;
            if (((Paint)object).isProportional()) {
                f9 = ((LinearGradient)object).getX1() * f4 + f2;
                f8 = ((LinearGradient)object).getY1() * f5 + f3;
                f7 = ((LinearGradient)object).getX2() * f4 + f2;
                f6 = ((LinearGradient)object).getY2() * f5 + f3;
            } else {
                f9 = ((LinearGradient)object).getX1();
                f8 = ((LinearGradient)object).getY1();
                f7 = ((LinearGradient)object).getX2();
                f6 = ((LinearGradient)object).getY2();
            }
            if (f9 == f7 && f8 == f6) {
                f9 -= 1.0E-6f;
                f7 += 1.0E-6f;
            }
            multipleGradientContext = new LinearGradientContext((LinearGradient)object, baseTransform, f9, f8, f7, f6, arrf, arrcolor, ((Gradient)object).getSpreadMethod());
        } else {
            float f10;
            float f11;
            object = (RadialGradient)gradient;
            gradXform.setTransform(baseTransform);
            float f12 = ((RadialGradient)object).getRadius();
            float f13 = ((RadialGradient)object).getCenterX();
            float f14 = ((RadialGradient)object).getCenterY();
            double d2 = Math.toRadians(((RadialGradient)object).getFocusAngle());
            float f15 = ((RadialGradient)object).getFocusDistance();
            if (((Paint)object).isProportional()) {
                f11 = f2 + f4 / 2.0f;
                f10 = f3 + f5 / 2.0f;
                float f16 = Math.min(f4, f5);
                f13 = (f13 - 0.5f) * f16 + f11;
                f14 = (f14 - 0.5f) * f16 + f10;
                if (f4 != f5 && f4 != 0.0f && f5 != 0.0f) {
                    gradXform.translate(f11, f10);
                    gradXform.scale(f4 / f16, f5 / f16);
                    gradXform.translate(-f11, -f10);
                }
                f12 *= f16;
            }
            if (f12 <= 0.0f) {
                f12 = 0.001f;
            }
            f11 = (float)((double)f13 + (double)(f15 *= f12) * Math.cos(d2));
            f10 = (float)((double)f14 + (double)f15 * Math.sin(d2));
            multipleGradientContext = new RadialGradientContext((RadialGradient)object, gradXform, f13, f14, f12, f11, f10, arrf, arrcolor, ((Gradient)object).getSpreadMethod());
        }
        ((MultipleGradientContext)multipleGradientContext).fillRaster(arrn, 0, 0, n2, n3, n4, n5);
    }
}

