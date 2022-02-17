/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSPrimitiveValueImpl;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

public class RectImpl
implements Rect {
    private final long peer;

    RectImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static Rect create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new RectImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof RectImpl && this.peer == ((RectImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(Rect rect) {
        return rect == null ? 0L : ((RectImpl)rect).getPeer();
    }

    private static native void dispose(long var0);

    static Rect getImpl(long l2) {
        return RectImpl.create(l2);
    }

    @Override
    public CSSPrimitiveValue getTop() {
        return CSSPrimitiveValueImpl.getImpl(RectImpl.getTopImpl(this.getPeer()));
    }

    static native long getTopImpl(long var0);

    @Override
    public CSSPrimitiveValue getRight() {
        return CSSPrimitiveValueImpl.getImpl(RectImpl.getRightImpl(this.getPeer()));
    }

    static native long getRightImpl(long var0);

    @Override
    public CSSPrimitiveValue getBottom() {
        return CSSPrimitiveValueImpl.getImpl(RectImpl.getBottomImpl(this.getPeer()));
    }

    static native long getBottomImpl(long var0);

    @Override
    public CSSPrimitiveValue getLeft() {
        return CSSPrimitiveValueImpl.getImpl(RectImpl.getLeftImpl(this.getPeer()));
    }

    static native long getLeftImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            RectImpl.dispose(this.peer);
        }
    }
}

