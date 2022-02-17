/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSValueImpl;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

public class CSSValueListImpl
extends CSSValueImpl
implements CSSValueList {
    CSSValueListImpl(long l2) {
        super(l2);
    }

    static CSSValueList getImpl(long l2) {
        return (CSSValueList)CSSValueListImpl.create(l2);
    }

    @Override
    public int getLength() {
        return CSSValueListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public CSSValue item(int n2) {
        return CSSValueImpl.getImpl(CSSValueListImpl.itemImpl(this.getPeer(), n2));
    }

    static native long itemImpl(long var0, int var2);
}

