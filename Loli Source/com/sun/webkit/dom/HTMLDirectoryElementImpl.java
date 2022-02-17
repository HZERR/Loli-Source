/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLDirectoryElement;

public class HTMLDirectoryElementImpl
extends HTMLElementImpl
implements HTMLDirectoryElement {
    HTMLDirectoryElementImpl(long l2) {
        super(l2);
    }

    static HTMLDirectoryElement getImpl(long l2) {
        return (HTMLDirectoryElement)HTMLDirectoryElementImpl.create(l2);
    }

    @Override
    public boolean getCompact() {
        return HTMLDirectoryElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLDirectoryElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);
}

