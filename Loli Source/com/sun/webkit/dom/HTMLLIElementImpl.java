/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLLIElement;

public class HTMLLIElementImpl
extends HTMLElementImpl
implements HTMLLIElement {
    HTMLLIElementImpl(long l2) {
        super(l2);
    }

    static HTMLLIElement getImpl(long l2) {
        return (HTMLLIElement)HTMLLIElementImpl.create(l2);
    }

    @Override
    public String getType() {
        return HTMLLIElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLLIElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    @Override
    public int getValue() {
        return HTMLLIElementImpl.getValueImpl(this.getPeer());
    }

    static native int getValueImpl(long var0);

    @Override
    public void setValue(int n2) {
        HTMLLIElementImpl.setValueImpl(this.getPeer(), n2);
    }

    static native void setValueImpl(long var0, int var2);
}

