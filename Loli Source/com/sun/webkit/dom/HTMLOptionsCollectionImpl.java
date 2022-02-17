/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLOptionElementImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;

public class HTMLOptionsCollectionImpl
extends HTMLCollectionImpl {
    HTMLOptionsCollectionImpl(long l2) {
        super(l2);
    }

    static HTMLOptionsCollectionImpl getImpl(long l2) {
        return (HTMLOptionsCollectionImpl)HTMLOptionsCollectionImpl.create(l2);
    }

    public int getSelectedIndex() {
        return HTMLOptionsCollectionImpl.getSelectedIndexImpl(this.getPeer());
    }

    static native int getSelectedIndexImpl(long var0);

    public void setSelectedIndex(int n2) {
        HTMLOptionsCollectionImpl.setSelectedIndexImpl(this.getPeer(), n2);
    }

    static native void setSelectedIndexImpl(long var0, int var2);

    @Override
    public int getLength() {
        return HTMLOptionsCollectionImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    public void setLength(int n2) throws DOMException {
        HTMLOptionsCollectionImpl.setLengthImpl(this.getPeer(), n2);
    }

    static native void setLengthImpl(long var0, int var2);

    @Override
    public HTMLOptionElement namedItem(String string) {
        return HTMLOptionElementImpl.getImpl(HTMLOptionsCollectionImpl.namedItemImpl(this.getPeer(), string));
    }

    static native long namedItemImpl(long var0, String var2);

    public void add(HTMLElement hTMLElement, HTMLElement hTMLElement2) throws DOMException {
        HTMLOptionsCollectionImpl.addImpl(this.getPeer(), HTMLElementImpl.getPeer(hTMLElement), HTMLElementImpl.getPeer(hTMLElement2));
    }

    static native void addImpl(long var0, long var2, long var4);

    public void addEx(HTMLElement hTMLElement, int n2) throws DOMException {
        HTMLOptionsCollectionImpl.addExImpl(this.getPeer(), HTMLElementImpl.getPeer(hTMLElement), n2);
    }

    static native void addExImpl(long var0, long var2, int var4);
}

