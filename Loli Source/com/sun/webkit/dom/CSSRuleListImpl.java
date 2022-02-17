/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSRuleImpl;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

public class CSSRuleListImpl
implements CSSRuleList {
    private final long peer;

    CSSRuleListImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static CSSRuleList create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new CSSRuleListImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CSSRuleListImpl && this.peer == ((CSSRuleListImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(CSSRuleList cSSRuleList) {
        return cSSRuleList == null ? 0L : ((CSSRuleListImpl)cSSRuleList).getPeer();
    }

    private static native void dispose(long var0);

    static CSSRuleList getImpl(long l2) {
        return CSSRuleListImpl.create(l2);
    }

    @Override
    public int getLength() {
        return CSSRuleListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public CSSRule item(int n2) {
        return CSSRuleImpl.getImpl(CSSRuleListImpl.itemImpl(this.getPeer(), n2));
    }

    static native long itemImpl(long var0, int var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            CSSRuleListImpl.dispose(this.peer);
        }
    }
}

