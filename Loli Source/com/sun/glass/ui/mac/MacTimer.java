/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Timer;

final class MacTimer
extends Timer {
    private static final int minPeriod = MacTimer._getMinPeriod();
    private static final int maxPeriod = MacTimer._getMaxPeriod();

    protected MacTimer(Runnable runnable) {
        super(runnable);
    }

    private static native int _getMinPeriod();

    private static native int _getMaxPeriod();

    static int getMinPeriod_impl() {
        return minPeriod;
    }

    static int getMaxPeriod_impl() {
        return maxPeriod;
    }

    @Override
    protected native long _start(Runnable var1);

    @Override
    protected native long _start(Runnable var1, int var2);

    @Override
    protected native void _stop(long var1);

    private static native void _initIDs();

    static {
        MacTimer._initIDs();
    }
}

