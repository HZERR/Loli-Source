/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

public class DistantLight
extends Light {
    private float azimuth;
    private float elevation;

    public DistantLight() {
        this(0.0f, 0.0f, Color4f.WHITE);
    }

    public DistantLight(float f2, float f3, Color4f color4f) {
        super(Light.Type.DISTANT, color4f);
        this.azimuth = f2;
        this.elevation = f3;
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(float f2) {
        this.azimuth = f2;
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setElevation(float f2) {
        this.elevation = f2;
    }

    @Override
    public float[] getNormalizedLightPosition() {
        float f2;
        float f3;
        double d2 = Math.toRadians(this.azimuth);
        double d3 = Math.toRadians(this.elevation);
        float f4 = (float)(Math.cos(d2) * Math.cos(d3));
        float f5 = (float)Math.sqrt(f4 * f4 + (f3 = (float)(Math.sin(d2) * Math.cos(d3))) * f3 + (f2 = (float)Math.sin(d3)) * f2);
        if (f5 == 0.0f) {
            f5 = 1.0f;
        }
        float[] arrf = new float[]{f4 / f5, f3 / f5, f2 / f5};
        return arrf;
    }
}

