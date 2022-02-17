/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.HTMLOptionsCollectionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLCollection;

public class HTMLCollectionImpl
implements HTMLCollection {
    private final long peer;
    private static final int TYPE_HTMLOptionsCollection = 1;

    HTMLCollectionImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static HTMLCollection create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        switch (HTMLCollectionImpl.getCPPTypeImpl(l2)) {
            case 1: {
                return new HTMLOptionsCollectionImpl(l2);
            }
        }
        return new HTMLCollectionImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof HTMLCollectionImpl && this.peer == ((HTMLCollectionImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(HTMLCollection hTMLCollection) {
        return hTMLCollection == null ? 0L : ((HTMLCollectionImpl)hTMLCollection).getPeer();
    }

    private static native void dispose(long var0);

    private static native int getCPPTypeImpl(long var0);

    static HTMLCollection getImpl(long l2) {
        return HTMLCollectionImpl.create(l2);
    }

    @Override
    public int getLength() {
        return HTMLCollectionImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public Element item(int n2) {
        return ElementImpl.getImpl(HTMLCollectionImpl.itemImpl(this.getPeer(), n2));
    }

    static native long itemImpl(long var0, int var2);

    @Override
    public Element namedItem(String string) {
        return ElementImpl.getImpl(HTMLCollectionImpl.namedItemImpl(this.getPeer(), string));
    }

    static native long namedItemImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            HTMLCollectionImpl.dispose(this.peer);
        }
    }
}

