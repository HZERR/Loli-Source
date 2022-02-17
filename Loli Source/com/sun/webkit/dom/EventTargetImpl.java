/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.EventImpl;
import com.sun.webkit.dom.EventListenerImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class EventTargetImpl
implements EventTarget {
    private final long peer;

    EventTargetImpl(long l2) {
        this.peer = l2;
        Disposer.addRecord(this, new SelfDisposer(l2));
    }

    static EventTarget create(long l2) {
        if (l2 == 0L) {
            return null;
        }
        return new EventTargetImpl(l2);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof EventTargetImpl && this.peer == ((EventTargetImpl)object).peer;
    }

    public int hashCode() {
        long l2 = this.peer;
        return (int)(l2 ^ l2 >> 17);
    }

    static long getPeer(EventTarget eventTarget) {
        return eventTarget == null ? 0L : ((EventTargetImpl)eventTarget).getPeer();
    }

    private static native void dispose(long var0);

    static EventTarget getImpl(long l2) {
        return EventTargetImpl.create(l2);
    }

    @Override
    public void addEventListener(String string, EventListener eventListener, boolean bl) {
        EventTargetImpl.addEventListenerImpl(this.getPeer(), string, EventListenerImpl.getPeer(eventListener), bl);
    }

    static native void addEventListenerImpl(long var0, String var2, long var3, boolean var5);

    @Override
    public void removeEventListener(String string, EventListener eventListener, boolean bl) {
        EventTargetImpl.removeEventListenerImpl(this.getPeer(), string, EventListenerImpl.getPeer(eventListener), bl);
    }

    static native void removeEventListenerImpl(long var0, String var2, long var3, boolean var5);

    @Override
    public boolean dispatchEvent(Event event) throws DOMException {
        return EventTargetImpl.dispatchEventImpl(this.getPeer(), EventImpl.getPeer(event));
    }

    static native boolean dispatchEventImpl(long var0, long var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l2) {
            this.peer = l2;
        }

        @Override
        public void dispose() {
            EventTargetImpl.dispose(this.peer);
        }
    }
}

