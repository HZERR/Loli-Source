/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListImpl
implements NodeList {
    private final long peer;

    NodeListImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static NodeList create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new NodeListImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof NodeListImpl && this.peer == ((NodeListImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(NodeList nodeList) {
        return nodeList == null ? 0L : ((NodeListImpl)nodeList).getPeer();
    }

    private static native void dispose(long var0);

    static NodeList getImpl(long l2) {
        return NodeListImpl.create(l2);
    }

    @Override
    public int getLength() {
        return NodeListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public Node item(int n2) {
        return NodeImpl.getImpl(NodeListImpl.itemImpl(this.getPeer(), n2));
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
            NodeListImpl.dispose(this.peer);
        }
    }
}

