/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CharacterDataImpl
extends NodeImpl
implements CharacterData {
    CharacterDataImpl(long l2) {
        super(l2);
    }

    static Node getImpl(long l2) {
        return CharacterDataImpl.create(l2);
    }

    @Override
    public String getData() {
        return CharacterDataImpl.getDataImpl(this.getPeer());
    }

    static native String getDataImpl(long var0);

    @Override
    public void setData(String string) {
        CharacterDataImpl.setDataImpl(this.getPeer(), string);
    }

    static native void setDataImpl(long var0, String var2);

    @Override
    public int getLength() {
        return CharacterDataImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    public Element getPreviousElementSibling() {
        return ElementImpl.getImpl(CharacterDataImpl.getPreviousElementSiblingImpl(this.getPeer()));
    }

    static native long getPreviousElementSiblingImpl(long var0);

    public Element getNextElementSibling() {
        return ElementImpl.getImpl(CharacterDataImpl.getNextElementSiblingImpl(this.getPeer()));
    }

    static native long getNextElementSiblingImpl(long var0);

    @Override
    public String substringData(int n2, int n3) throws DOMException {
        return CharacterDataImpl.substringDataImpl(this.getPeer(), n2, n3);
    }

    static native String substringDataImpl(long var0, int var2, int var3);

    @Override
    public void appendData(String string) {
        CharacterDataImpl.appendDataImpl(this.getPeer(), string);
    }

    static native void appendDataImpl(long var0, String var2);

    @Override
    public void insertData(int n2, String string) throws DOMException {
        CharacterDataImpl.insertDataImpl(this.getPeer(), n2, string);
    }

    static native void insertDataImpl(long var0, int var2, String var3);

    @Override
    public void deleteData(int n2, int n3) throws DOMException {
        CharacterDataImpl.deleteDataImpl(this.getPeer(), n2, n3);
    }

    static native void deleteDataImpl(long var0, int var2, int var3);

    @Override
    public void replaceData(int n2, int n3, String string) throws DOMException {
        CharacterDataImpl.replaceDataImpl(this.getPeer(), n2, n3, string);
    }

    static native void replaceDataImpl(long var0, int var2, int var3, String var4);

    public void remove() throws DOMException {
        CharacterDataImpl.removeImpl(this.getPeer());
    }

    static native void removeImpl(long var0);
}

