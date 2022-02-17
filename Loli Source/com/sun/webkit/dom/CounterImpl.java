/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.Counter;

public class CounterImpl
implements Counter {
    private final long peer;

    CounterImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static Counter create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new CounterImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CounterImpl && this.peer == ((CounterImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(Counter counter) {
        return counter == null ? 0L : ((CounterImpl)counter).getPeer();
    }

    private static native void dispose(long var0);

    static Counter getImpl(long l2) {
        return CounterImpl.create(l2);
    }

    @Override
    public String getIdentifier() {
        return CounterImpl.getIdentifierImpl(this.getPeer());
    }

    static native String getIdentifierImpl(long var0);

    @Override
    public String getListStyle() {
        return CounterImpl.getListStyleImpl(this.getPeer());
    }

    static native String getListStyleImpl(long var0);

    @Override
    public String getSeparator() {
        return CounterImpl.getSeparatorImpl(this.getPeer());
    }

    static native String getSeparatorImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            CounterImpl.dispose(this.peer);
        }
    }
}

