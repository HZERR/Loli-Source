/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.interpolator;

public interface PropertyInterpolator<T> {
    public Class getBasePropertyClass();

    public T interpolate(T var1, T var2, float var3);
}

