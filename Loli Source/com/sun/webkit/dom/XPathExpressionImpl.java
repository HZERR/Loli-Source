/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.XPathResultImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathResult;

public class XPathExpressionImpl
implements XPathExpression {
    private final long peer;

    XPathExpressionImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static XPathExpression create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new XPathExpressionImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof XPathExpressionImpl && this.peer == ((XPathExpressionImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(XPathExpression xPathExpression) {
        return xPathExpression == null ? 0L : ((XPathExpressionImpl)xPathExpression).getPeer();
    }

    private static native void dispose(long var0);

    static XPathExpression getImpl(long l2) {
        return XPathExpressionImpl.create(l2);
    }

    @Override
    public Object evaluate(Node node, short s2, Object object) throws DOMException {
        return this.evaluate(node, s2, (XPathResult)object);
    }

    public XPathResult evaluate(Node node, short s2, XPathResult xPathResult) throws DOMException {
        return XPathResultImpl.getImpl(XPathExpressionImpl.evaluateImpl(this.getPeer(), NodeImpl.getPeer(node), s2, XPathResultImpl.getPeer(xPathResult)));
    }

    static native long evaluateImpl(long var0, long var2, short var4, long var5);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            XPathExpressionImpl.dispose(this.peer);
        }
    }
}

