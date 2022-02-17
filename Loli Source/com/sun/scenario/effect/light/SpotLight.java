/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;
import com.sun.scenario.effect.light.PointLight;

public class SpotLight
extends PointLight {
    private float pointsAtX = 0.0f;
    private float pointsAtY = 0.0f;
    private float pointsAtZ = 0.0f;
    private float specularExponent = 1.0f;

    public SpotLight() {
        this(0.0f, 0.0f, 0.0f, Color4f.WHITE);
    }

    public SpotLight(float f2, float f3, float f4, Color4f color4f) {
        super(Light.Type.SPOT, f2, f3, f4, color4f);
    }

    public float getPointsAtX() {
        return this.pointsAtX;
    }

    public void setPointsAtX(float f2) {
        this.pointsAtX = f2;
    }

    public float getPointsAtY() {
        return this.pointsAtY;
    }

    public void setPointsAtY(float f2) {
        float f3 = this.pointsAtY;
        this.pointsAtY = f2;
    }

    public float getPointsAtZ() {
        return this.pointsAtZ;
    }

    public void setPointsAtZ(float f2) {
        this.pointsAtZ = f2;
    }

    public float getSpecularExponent() {
        return this.specularExponent;
    }

    public void setSpecularExponent(float f2) {
        if (f2 < 0.0f || f2 > 4.0f) {
            throw new IllegalArgumentException("Specular exponent must be in the range [0,4]");
        }
        this.specularExponent = f2;
    }

    @Override
    public float[] getNormalizedLightPosition() {
        float f2;
        float f3;
        float f4 = this.getX();
        float f5 = (float)Math.sqrt(f4 * f4 + (f3 = this.getY()) * f3 + (f2 = this.getZ()) * f2);
        if (f5 == 0.0f) {
            f5 = 1.0f;
        }
        float[] arrf = new float[]{f4 / f5, f3 / f5, f2 / f5};
        return arrf;
    }

    public float[] getNormalizedLightDirection() {
        float f2;
        float f3;
        float f4 = this.pointsAtX - this.getX();
        float f5 = (float)Math.sqrt(f4 * f4 + (f3 = this.pointsAtY - this.getY()) * f3 + (f2 = this.pointsAtZ - this.getZ()) * f2);
        if (f5 == 0.0f) {
            f5 = 1.0f;
        }
        float[] arrf = new float[]{f4 / f5, f3 / f5, f2 / f5};
        return arrf;
    }
}

