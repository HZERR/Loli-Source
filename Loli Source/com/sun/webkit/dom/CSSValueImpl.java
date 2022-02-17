/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSPrimitiveValueImpl;
import com.sun.webkit.dom.CSSValueListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

public class CSSValueImpl
implements CSSValue {
    private final long peer;
    public static final int CSS_INHERIT = 0;
    public static final int CSS_PRIMITIVE_VALUE = 1;
    public static final int CSS_VALUE_LIST = 2;
    public static final int CSS_CUSTOM = 3;

    CSSValueImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static CSSValue create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        switch (CSSValueImpl.getCssValueTypeImpl(l2)) {
            case 1: {
                return new CSSPrimitiveValueImpl(l2);
            }
            case 2: {
                return new CSSValueListImpl(l2);
            }
        }
        return new CSSValueImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CSSValueImpl && this.peer == ((CSSValueImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(CSSValue cSSValue) {
        return cSSValue == null ? 0L : ((CSSValueImpl)cSSValue).getPeer();
    }

    private static native void dispose(long var0);

    static CSSValue getImpl(long l2) {
        return CSSValueImpl.create(l2);
    }

    @Override
    public String getCssText() {
        return CSSValueImpl.getCssTextImpl(this.getPeer());
    }

    static native String getCssTextImpl(long var0);

    @Override
    public void setCssText(String string) throws DOMException {
        CSSValueImpl.setCssTextImpl(this.getPeer(), string);
    }

    static native void setCssTextImpl(long var0, String var2);

    @Override
    public short getCssValueType() {
        return CSSValueImpl.getCssValueTypeImpl(this.getPeer());
    }

    static native short getCssValueTypeImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            CSSValueImpl.dispose(this.peer);
        }
    }
}

