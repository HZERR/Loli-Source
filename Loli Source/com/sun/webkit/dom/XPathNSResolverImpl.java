/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.xpath.XPathNSResolver;

public class XPathNSResolverImpl
implements XPathNSResolver {
    private final long peer;

    XPathNSResolverImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static XPathNSResolver create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new XPathNSResolverImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof XPathNSResolverImpl && this.peer == ((XPathNSResolverImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(XPathNSResolver xPathNSResolver) {
        return xPathNSResolver == null ? 0L : ((XPathNSResolverImpl)xPathNSResolver).getPeer();
    }

    private static native void dispose(long var0);

    static XPathNSResolver getImpl(long l2) {
        return XPathNSResolverImpl.create(l2);
    }

    @Override
    public String lookupNamespaceURI(String string) {
        return XPathNSResolverImpl.lookupNamespaceURIImpl(this.getPeer(), string);
    }

    static native String lookupNamespaceURIImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            XPathNSResolverImpl.dispose(this.peer);
        }
    }
}

