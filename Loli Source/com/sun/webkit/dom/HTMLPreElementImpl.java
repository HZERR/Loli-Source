/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLPreElement;

public class HTMLPreElementImpl
extends HTMLElementImpl
implements HTMLPreElement {
    HTMLPreElementImpl(long l2) {
        super(l2);
    }

    static HTMLPreElement getImpl(long l2) {
        return (HTMLPreElement)HTMLPreElementImpl.create(l2);
    }

    @Override
    public int getWidth() {
        return HTMLPreElementImpl.getWidthImpl(this.getPeer());
    }

    static native int getWidthImpl(long var0);

    @Override
    public void setWidth(int n2) {
        HTMLPreElementImpl.setWidthImpl(this.getPeer(), n2);
    }

    static native void setWidthImpl(long var0, int var2);

    public boolean getWrap() {
        return HTMLPreElementImpl.getWrapImpl(this.getPeer());
    }

    static native boolean getWrapImpl(long var0);

    public void setWrap(boolean bl) {
        HTMLPreElementImpl.setWrapImpl(this.getPeer(), bl);
    }

    static native void setWrapImpl(long var0, boolean var2);
}

