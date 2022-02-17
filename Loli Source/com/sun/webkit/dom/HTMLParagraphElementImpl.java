/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLParagraphElement;

public class HTMLParagraphElementImpl
extends HTMLElementImpl
implements HTMLParagraphElement {
    HTMLParagraphElementImpl(long l2) {
        super(l2);
    }

    static HTMLParagraphElement getImpl(long l2) {
        return (HTMLParagraphElement)HTMLParagraphElementImpl.create(l2);
    }

    @Override
    public String getAlign() {
        return HTMLParagraphElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLParagraphElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);
}

