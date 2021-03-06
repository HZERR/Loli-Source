/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSPrimitiveValueImpl;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

public class RGBColorImpl
implements RGBColor {
    private final long peer;

    RGBColorImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static RGBColor create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new RGBColorImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof RGBColorImpl && this.peer == ((RGBColorImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(RGBColor rGBColor) {
        return rGBColor == null ? 0L : ((RGBColorImpl)rGBColor).getPeer();
    }

    private static native void dispose(long var0);

    static RGBColor getImpl(long l2) {
        return RGBColorImpl.create(l2);
    }

    @Override
    public CSSPrimitiveValue getRed() {
        return CSSPrimitiveValueImpl.getImpl(RGBColorImpl.getRedImpl(this.getPeer()));
    }

    static native long getRedImpl(long var0);

    @Override
    public CSSPrimitiveValue getGreen() {
        return CSSPrimitiveValueImpl.getImpl(RGBColorImpl.getGreenImpl(this.getPeer()));
    }

    static native long getGreenImpl(long var0);

    @Override
    public CSSPrimitiveValue getBlue() {
        return CSSPrimitiveValueImpl.getImpl(RGBColorImpl.getBlueImpl(this.getPeer()));
    }

    static native long getBlueImpl(long var0);

    public CSSPrimitiveValue getAlpha() {
        return CSSPrimitiveValueImpl.getImpl(RGBColorImpl.getAlphaImpl(this.getPeer()));
    }

    static native long getAlphaImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            RGBColorImpl.dispose(this.peer);
        }
    }
}

