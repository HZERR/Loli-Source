/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLDListElement;

public class HTMLDListElementImpl
extends HTMLElementImpl
implements HTMLDListElement {
    HTMLDListElementImpl(long l2) {
        super(l2);
    }

    static HTMLDListElement getImpl(long l2) {
        return (HTMLDListElement)HTMLDListElementImpl.create(l2);
    }

    @Override
    public boolean getCompact() {
        return HTMLDListElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLDListElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);
}

