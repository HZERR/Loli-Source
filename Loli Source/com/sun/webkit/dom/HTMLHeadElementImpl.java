/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLHeadElement;

public class HTMLHeadElementImpl
extends HTMLElementImpl
implements HTMLHeadElement {
    HTMLHeadElementImpl(long l2) {
        super(l2);
    }

    static HTMLHeadElement getImpl(long l2) {
        return (HTMLHeadElement)HTMLHeadElementImpl.create(l2);
    }

    @Override
    public String getProfile() {
        return HTMLHeadElementImpl.getProfileImpl(this.getPeer());
    }

    static native String getProfileImpl(long var0);

    @Override
    public void setProfile(String string) {
        HTMLHeadElementImpl.setProfileImpl(this.getPeer(), string);
    }

    static native void setProfileImpl(long var0, String var2);
}

