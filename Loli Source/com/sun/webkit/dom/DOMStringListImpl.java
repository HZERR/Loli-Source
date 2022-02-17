/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMStringList;

public class DOMStringListImpl
implements DOMStringList {
    private final long peer;

    DOMStringListImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static DOMStringList create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new DOMStringListImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof DOMStringListImpl && this.peer == ((DOMStringListImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(DOMStringList dOMStringList) {
        return dOMStringList == null ? 0L : ((DOMStringListImpl)dOMStringList).getPeer();
    }

    private static native void dispose(long var0);

    static DOMStringList getImpl(long l2) {
        return DOMStringListImpl.create(l2);
    }

    @Override
    public int getLength() {
        return DOMStringListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public String item(int n2) {
        return DOMStringListImpl.itemImpl(this.getPeer(), n2);
    }

    static native String itemImpl(long var0, int var2);

    @Override
    public boolean contains(String string) {
        return DOMStringListImpl.containsImpl(this.getPeer(), string);
    }

    static native boolean containsImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            DOMStringListImpl.dispose(this.peer);
        }
    }
}

