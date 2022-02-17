/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public class EventDispatchChainImpl
implements EventDispatchChain {
    private static final int CAPACITY_GROWTH_FACTOR = 8;
    private EventDispatcher[] dispatchers;
    private int[] nextLinks;
    private int reservedCount;
    private int activeCount;
    private int headIndex;
    private int tailIndex;

    public void reset() {
        for (int i2 = 0; i2 < this.reservedCount; ++i2) {
            this.dispatchers[i2] = null;
        }
        this.reservedCount = 0;
        this.activeCount = 0;
        this.headIndex = 0;
        this.tailIndex = 0;
    }

    @Override
    public EventDispatchChain append(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        if (this.activeCount == 0) {
            this.insertFirst(eventDispatcher);
            return this;
        }
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextLinks[this.tailIndex] = this.reservedCount;
        this.tailIndex = this.reservedCount++;
        ++this.activeCount;
        return this;
    }

    @Override
    public EventDispatchChain prepend(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        if (this.activeCount == 0) {
            this.insertFirst(eventDispatcher);
            return this;
        }
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextLinks[this.reservedCount] = this.headIndex;
        this.headIndex = this.reservedCount++;
        ++this.activeCount;
        return this;
    }

    @Override
    public Event dispatchEvent(Event event) {
        if (this.activeCount == 0) {
            return event;
        }
        int n2 = this.headIndex;
        int n3 = this.tailIndex;
        int n4 = this.activeCount--;
        int n5 = this.reservedCount;
        EventDispatcher eventDispatcher = this.dispatchers[this.headIndex];
        this.headIndex = this.nextLinks[this.headIndex];
        Event event2 = eventDispatcher.dispatchEvent(event, this);
        this.headIndex = n2;
        this.tailIndex = n3;
        this.activeCount = n4;
        this.reservedCount = n5;
        return event2;
    }

    private void insertFirst(EventDispatcher eventDispatcher) {
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.headIndex = this.reservedCount;
        this.tailIndex = this.reservedCount++;
        this.activeCount = 1;
    }

    private void ensureCapacity(int n2) {
        int n3 = n2 + 8 - 1 & 0xFFFFFFF8;
        if (n3 == 0) {
            return;
        }
        if (this.dispatchers == null || this.dispatchers.length < n3) {
            EventDispatcher[] arreventDispatcher = new EventDispatcher[n3];
            int[] arrn = new int[n3];
            if (this.reservedCount > 0) {
                System.arraycopy(this.dispatchers, 0, arreventDispatcher, 0, this.reservedCount);
                System.arraycopy(this.nextLinks, 0, arrn, 0, this.reservedCount);
            }
            this.dispatchers = arreventDispatcher;
            this.nextLinks = arrn;
        }
    }
}

