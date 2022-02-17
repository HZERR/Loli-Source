/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLHeadingElement;

public class HTMLHeadingElementImpl
extends HTMLElementImpl
implements HTMLHeadingElement {
    HTMLHeadingElementImpl(long l2) {
        super(l2);
    }

    static HTMLHeadingElement getImpl(long l2) {
        return (HTMLHeadingElement)HTMLHeadingElementImpl.create(l2);
    }

    @Override
    public String getAlign() {
        return HTMLHeadingElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLHeadingElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);
}

