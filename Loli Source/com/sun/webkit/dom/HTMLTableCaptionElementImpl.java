/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLTableCaptionElement;

public class HTMLTableCaptionElementImpl
extends HTMLElementImpl
implements HTMLTableCaptionElement {
    HTMLTableCaptionElementImpl(long l2) {
        super(l2);
    }

    static HTMLTableCaptionElement getImpl(long l2) {
        return (HTMLTableCaptionElement)HTMLTableCaptionElementImpl.create(l2);
    }

    @Override
    public String getAlign() {
        return HTMLTableCaptionElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLTableCaptionElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);
}

