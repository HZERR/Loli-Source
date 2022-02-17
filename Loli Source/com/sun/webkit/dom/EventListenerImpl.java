/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.EventImpl;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

final class EventListenerImpl
implements EventListener {
    private static final Map<EventListener, Long> EL2peer = new WeakHashMap<EventListener, Long>();
    private static final Map<Long, WeakReference<EventListener>> peer2EL = new HashMap<Long, WeakReference<EventListener>>();
    private final EventListener eventListener;
    private final long jsPeer;

    static long getPeer(EventListener eventListener) {
        if (eventListener == null) {
            return 0L;
        }
        Long l2 = EL2peer.get(eventListener);
        if (l2 != null) {
            return l2;
        }
        EventListenerImpl eventListenerImpl = new EventListenerImpl(eventListener, 0L);
        l2 = eventListenerImpl.twkCreatePeer();
        EL2peer.put(eventListener, l2);
        peer2EL.put(l2, new WeakReference<EventListener>(eventListener));
        return l2;
    }

    private native long twkCreatePeer();

    private static EventListener getELfromPeer(long l2) {
        WeakReference<EventListener> weakReference = peer2EL.get(l2);
        return weakReference == null ? null : (EventListener)weakReference.get();
    }

    static EventListener getImpl(long l2) {
        if (l2 == 0L) {
            return null;
        }
        EventListener eventListener = EventListenerImpl.getELfromPeer(l2);
        if (eventListener != null) {
            EventListenerImpl.twkDisposeJSPeer(l2);
            return eventListener;
        }
        EventListenerImpl eventListenerImpl = new EventListenerImpl(null, l2);
        EL2peer.put(eventListenerImpl, l2);
        peer2EL.put(l2, new WeakReference<EventListenerImpl>(eventListenerImpl));
        Disposer.addRecord(eventListenerImpl, new SelfDisposer(l2));
        return eventListenerImpl;
    }

    @Override
    public void handleEvent(Event event) {
        if (this.jsPeer != 0L && event instanceof EventImpl) {
            EventListenerImpl.twkDispatchEvent(this.jsPeer, ((EventImpl)event).getPeer());
        }
    }

    private static native void twkDispatchEvent(long var0, long var2);

    private EventListenerImpl(EventListener eventListener, long l2) {
        this.eventListener = eventListener;
        this.jsPeer = l2;
    }

    private static void dispose(long l2) {
        EventListener eventListener = EventListenerImpl.getELfromPeer(l2);
        if (eventListener != null) {
            EL2peer.remove(eventListener);
        }
        peer2EL.remove(l2);
    }

    private static native void twkDisposeJSPeer(long var0);

    private void fwkHandleEvent(long l2) {
        this.eventListener.handleEvent(EventImpl.getImpl(l2));
    }

    private static final class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        private SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            EventListenerImpl.dispose(this.peer);
            EventListenerImpl.twkDisposeJSPeer(this.peer);
        }
    }
}

