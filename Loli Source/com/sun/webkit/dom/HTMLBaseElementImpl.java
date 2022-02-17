/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLBaseElement;

public class HTMLBaseElementImpl
extends HTMLElementImpl
implements HTMLBaseElement {
    HTMLBaseElementImpl(long l2) {
        super(l2);
    }

    static HTMLBaseElement getImpl(long l2) {
        return (HTMLBaseElement)HTMLBaseElementImpl.create(l2);
    }

    @Override
    public String getHref() {
        return HTMLBaseElementImpl.getHrefImpl(this.getPeer());
    }

    static native String getHrefImpl(long var0);

    @Override
    public void setHref(String string) {
        HTMLBaseElementImpl.setHrefImpl(this.getPeer(), string);
    }

    static native void setHrefImpl(long var0, String var2);

    @Override
    public String getTarget() {
        return HTMLBaseElementImpl.getTargetImpl(this.getPeer());
    }

    static native String getTargetImpl(long var0);

    @Override
    public void setTarget(String string) {
        HTMLBaseElementImpl.setTargetImpl(this.getPeer(), string);
    }

    static native void setTargetImpl(long var0, String var2);
}

