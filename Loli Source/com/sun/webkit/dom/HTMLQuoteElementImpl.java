/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLQuoteElement;

public class HTMLQuoteElementImpl
extends HTMLElementImpl
implements HTMLQuoteElement {
    HTMLQuoteElementImpl(long l2) {
        super(l2);
    }

    static HTMLQuoteElement getImpl(long l2) {
        return (HTMLQuoteElement)HTMLQuoteElementImpl.create(l2);
    }

    @Override
    public String getCite() {
        return HTMLQuoteElementImpl.getCiteImpl(this.getPeer());
    }

    static native String getCiteImpl(long var0);

    @Override
    public void setCite(String string) {
        HTMLQuoteElementImpl.setCiteImpl(this.getPeer(), string);
    }

    static native void setCiteImpl(long var0, String var2);
}

