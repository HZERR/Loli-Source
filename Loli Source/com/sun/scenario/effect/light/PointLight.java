/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

public class PointLight
extends Light {
    private float x;
    private float y;
    private float z;

    public PointLight() {
        this(0.0f, 0.0f, 0.0f, Color4f.WHITE);
    }

    public PointLight(float f2, float f3, float f4, Color4f color4f) {
        this(Light.Type.POINT, f2, f3, f4, color4f);
    }

    PointLight(Light.Type type, float f2, float f3, float f4, Color4f color4f) {
        super(type, color4f);
        this.x = f2;
        this.y = f3;
        this.z = f4;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float f2) {
        this.x = f2;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f2) {
        this.y = f2;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float f2) {
        this.z = f2;
    }

    @Override
    public float[] getNormalizedLightPosition() {
        float f2 = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (f2 == 0.0f) {
            f2 = 1.0f;
        }
        float[] arrf = new float[]{this.x / f2, this.y / f2, this.z / f2};
        return arrf;
    }
}

