/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.effect;

public enum EffectDirtyBits {
    EFFECT_DIRTY,
    BOUNDS_CHANGED;

    private int mask = 1 << this.ordinal();

    public final int getMask() {
        return this.mask;
    }

    public static boolean isSet(int n2, EffectDirtyBits effectDirtyBits) {
        return (n2 & effectDirtyBits.getMask()) != 0;
    }
}

