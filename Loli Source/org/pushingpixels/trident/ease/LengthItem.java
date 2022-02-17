/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.ease;

class LengthItem {
    float length;
    float t;
    float fraction;

    LengthItem(float length, float t2, float fraction) {
        this.length = length;
        this.t = t2;
        this.fraction = fraction;
    }

    LengthItem(float length, float t2) {
        this.length = length;
        this.t = t2;
    }

    public float getLength() {
        return this.length;
    }

    public float getT() {
        return this.t;
    }

    public float getFraction() {
        return this.fraction;
    }

    void setFraction(float totalLength) {
        this.fraction = this.length / totalLength;
    }
}

