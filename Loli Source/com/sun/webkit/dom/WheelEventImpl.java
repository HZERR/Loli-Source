/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.MouseEventImpl;
import org.w3c.dom.views.AbstractView;

public class WheelEventImpl
extends MouseEventImpl {
    public static final int DOM_DELTA_PIXEL = 0;
    public static final int DOM_DELTA_LINE = 1;
    public static final int DOM_DELTA_PAGE = 2;

    WheelEventImpl(long l2) {
        super(l2);
    }

    static WheelEventImpl getImpl(long l2) {
        return (WheelEventImpl)WheelEventImpl.create(l2);
    }

    public double getDeltaX() {
        return WheelEventImpl.getDeltaXImpl(this.getPeer());
    }

    static native double getDeltaXImpl(long var0);

    public double getDeltaY() {
        return WheelEventImpl.getDeltaYImpl(this.getPeer());
    }

    static native double getDeltaYImpl(long var0);

    public double getDeltaZ() {
        return WheelEventImpl.getDeltaZImpl(this.getPeer());
    }

    static native double getDeltaZImpl(long var0);

    public int getDeltaMode() {
        return WheelEventImpl.getDeltaModeImpl(this.getPeer());
    }

    static native int getDeltaModeImpl(long var0);

    public int getWheelDeltaX() {
        return WheelEventImpl.getWheelDeltaXImpl(this.getPeer());
    }

    static native int getWheelDeltaXImpl(long var0);

    public int getWheelDeltaY() {
        return WheelEventImpl.getWheelDeltaYImpl(this.getPeer());
    }

    static native int getWheelDeltaYImpl(long var0);

    public int getWheelDelta() {
        return WheelEventImpl.getWheelDeltaImpl(this.getPeer());
    }

    static native int getWheelDeltaImpl(long var0);

    public boolean getWebkitDirectionInvertedFromDevice() {
        return WheelEventImpl.getWebkitDirectionInvertedFromDeviceImpl(this.getPeer());
    }

    static native boolean getWebkitDirectionInvertedFromDeviceImpl(long var0);

    public void initWheelEvent(int n2, int n3, AbstractView abstractView, int n4, int n5, int n6, int n7, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        WheelEventImpl.initWheelEventImpl(this.getPeer(), n2, n3, DOMWindowImpl.getPeer(abstractView), n4, n5, n6, n7, bl, bl2, bl3, bl4);
    }

    static native void initWheelEventImpl(long var0, int var2, int var3, long var4, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13);
}

