/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLModElement;

public class HTMLModElementImpl
extends HTMLElementImpl
implements HTMLModElement {
    HTMLModElementImpl(long l2) {
        super(l2);
    }

    static HTMLModElement getImpl(long l2) {
        return (HTMLModElement)HTMLModElementImpl.create(l2);
    }

    @Override
    public String getCite() {
        return HTMLModElementImpl.getCiteImpl(this.getPeer());
    }

    static native String getCiteImpl(long var0);

    @Override
    public void setCite(String string) {
        HTMLModElementImpl.setCiteImpl(this.getPeer(), string);
    }

    static native void setCiteImpl(long var0, String var2);

    @Override
    public String getDateTime() {
        return HTMLModElementImpl.getDateTimeImpl(this.getPeer());
    }

    static native String getDateTimeImpl(long var0);

    @Override
    public void setDateTime(String string) {
        HTMLModElementImpl.setDateTimeImpl(this.getPeer(), string);
    }

    static native void setDateTimeImpl(long var0, String var2);
}

