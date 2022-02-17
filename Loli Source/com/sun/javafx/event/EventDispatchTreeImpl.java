/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.event;

import com.sun.javafx.event.EventDispatchTree;
import javafx.event.Event;
import javafx.event.EventDispatcher;

public final class EventDispatchTreeImpl
implements EventDispatchTree {
    private static final int CAPACITY_GROWTH_FACTOR = 8;
    private static final int NULL_INDEX = -1;
    private EventDispatcher[] dispatchers;
    private int[] nextChildren;
    private int[] nextSiblings;
    private int reservedCount;
    private int rootIndex = -1;
    private int tailFirstIndex = -1;
    private int tailLastIndex = -1;
    private boolean expandTailFirstPath;

    public void reset() {
        for (int i2 = 0; i2 < this.reservedCount; ++i2) {
            this.dispatchers[i2] = null;
        }
        this.reservedCount = 0;
        this.rootIndex = -1;
        this.tailFirstIndex = -1;
        this.tailLastIndex = -1;
    }

    @Override
    public EventDispatchTree createTree() {
        return new EventDispatchTreeImpl();
    }

    @Override
    public EventDispatchTree mergeTree(EventDispatchTree eventDispatchTree) {
        int n2;
        if (this.tailFirstIndex != -1) {
            if (this.rootIndex != -1) {
                this.expandTailFirstPath = true;
                this.expandTail(this.rootIndex);
            } else {
                this.rootIndex = this.tailFirstIndex;
            }
            this.tailFirstIndex = -1;
            this.tailLastIndex = -1;
        }
        EventDispatchTreeImpl eventDispatchTreeImpl = (EventDispatchTreeImpl)eventDispatchTree;
        int n3 = n2 = eventDispatchTreeImpl.rootIndex != -1 ? eventDispatchTreeImpl.rootIndex : eventDispatchTreeImpl.tailFirstIndex;
        if (this.rootIndex == -1) {
            this.rootIndex = this.copyTreeLevel(eventDispatchTreeImpl, n2);
        } else {
            this.mergeTreeLevel(eventDispatchTreeImpl, this.rootIndex, n2);
        }
        return this;
    }

    @Override
    public EventDispatchTree append(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextSiblings[this.reservedCount] = -1;
        this.nextChildren[this.reservedCount] = -1;
        if (this.tailFirstIndex == -1) {
            this.tailFirstIndex = this.reservedCount;
        } else {
            this.nextChildren[this.tailLastIndex] = this.reservedCount;
        }
        this.tailLastIndex = this.reservedCount++;
        return this;
    }

    @Override
    public EventDispatchTree prepend(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextSiblings[this.reservedCount] = -1;
        this.nextChildren[this.reservedCount] = this.rootIndex;
        this.rootIndex = this.reservedCount++;
        return this;
    }

    @Override
    public Event dispatchEvent(Event event) {
        if (this.rootIndex == -1) {
            if (this.tailFirstIndex == -1) {
                return event;
            }
            this.rootIndex = this.tailFirstIndex;
            this.tailFirstIndex = -1;
            this.tailLastIndex = -1;
        }
        int n2 = this.reservedCount;
        int n3 = this.rootIndex;
        int n4 = this.tailFirstIndex;
        int n5 = this.tailLastIndex;
        Event event2 = null;
        int n6 = this.rootIndex;
        do {
            this.rootIndex = this.nextChildren[n6];
            Event event3 = this.dispatchers[n6].dispatchEvent(event, this);
            if (event3 == null) continue;
            Event event4 = event2 = event2 != null ? event : event3;
        } while ((n6 = this.nextSiblings[n6]) != -1);
        this.reservedCount = n2;
        this.rootIndex = n3;
        this.tailFirstIndex = n4;
        this.tailLastIndex = n5;
        return event2;
    }

    public String toString() {
        int n2;
        int n3 = n2 = this.rootIndex != -1 ? this.rootIndex : this.tailFirstIndex;
        if (n2 == -1) {
            return "()";
        }
        StringBuilder stringBuilder = new StringBuilder();
        this.appendTreeLevel(stringBuilder, n2);
        return stringBuilder.toString();
    }

    private void ensureCapacity(int n2) {
        int n3 = n2 + 8 - 1 & 0xFFFFFFF8;
        if (n3 == 0) {
            return;
        }
        if (this.dispatchers == null || this.dispatchers.length < n3) {
            EventDispatcher[] arreventDispatcher = new EventDispatcher[n3];
            int[] arrn = new int[n3];
            int[] arrn2 = new int[n3];
            if (this.reservedCount > 0) {
                System.arraycopy(this.dispatchers, 0, arreventDispatcher, 0, this.reservedCount);
                System.arraycopy(this.nextChildren, 0, arrn, 0, this.reservedCount);
                System.arraycopy(this.nextSiblings, 0, arrn2, 0, this.reservedCount);
            }
            this.dispatchers = arreventDispatcher;
            this.nextChildren = arrn;
            this.nextSiblings = arrn2;
        }
    }

    private void expandTail(int n2) {
        int n3 = n2;
        while (n3 != -1) {
            if (this.nextChildren[n3] != -1) {
                this.expandTail(this.nextChildren[n3]);
            } else if (this.expandTailFirstPath) {
                this.nextChildren[n3] = this.tailFirstIndex;
                this.expandTailFirstPath = false;
            } else {
                int n4;
                this.nextChildren[n3] = n4 = this.copyTreeLevel(this, this.tailFirstIndex);
            }
            n3 = this.nextSiblings[n3];
        }
    }

    private void mergeTreeLevel(EventDispatchTreeImpl eventDispatchTreeImpl, int n2, int n3) {
        int n4 = n3;
        while (n4 != -1) {
            int n5;
            EventDispatcher eventDispatcher = eventDispatchTreeImpl.dispatchers[n4];
            int n6 = n2;
            int n7 = n2;
            while (n6 != -1 && eventDispatcher != this.dispatchers[n6]) {
                n7 = n6;
                n6 = this.nextSiblings[n6];
            }
            if (n6 == -1) {
                this.nextSiblings[n7] = n5 = this.copySubtree(eventDispatchTreeImpl, n4);
                this.nextSiblings[n5] = -1;
            } else {
                n5 = this.nextChildren[n6];
                int n8 = EventDispatchTreeImpl.getChildIndex(eventDispatchTreeImpl, n4);
                if (n5 != -1) {
                    this.mergeTreeLevel(eventDispatchTreeImpl, n5, n8);
                } else {
                    this.nextChildren[n6] = n5 = this.copyTreeLevel(eventDispatchTreeImpl, n8);
                }
            }
            n4 = eventDispatchTreeImpl.nextSiblings[n4];
        }
    }

    private int copyTreeLevel(EventDispatchTreeImpl eventDispatchTreeImpl, int n2) {
        int n3;
        if (n2 == -1) {
            return -1;
        }
        int n4 = n2;
        int n5 = n3 = this.copySubtree(eventDispatchTreeImpl, n4);
        n4 = eventDispatchTreeImpl.nextSiblings[n4];
        while (n4 != -1) {
            int n6;
            this.nextSiblings[n5] = n6 = this.copySubtree(eventDispatchTreeImpl, n4);
            n5 = n6;
            n4 = eventDispatchTreeImpl.nextSiblings[n4];
        }
        this.nextSiblings[n5] = -1;
        return n3;
    }

    private int copySubtree(EventDispatchTreeImpl eventDispatchTreeImpl, int n2) {
        this.ensureCapacity(this.reservedCount + 1);
        int n3 = this.reservedCount++;
        int n4 = this.copyTreeLevel(eventDispatchTreeImpl, EventDispatchTreeImpl.getChildIndex(eventDispatchTreeImpl, n2));
        this.dispatchers[n3] = eventDispatchTreeImpl.dispatchers[n2];
        this.nextChildren[n3] = n4;
        return n3;
    }

    private void appendTreeLevel(StringBuilder stringBuilder, int n2) {
        stringBuilder.append('(');
        int n3 = n2;
        this.appendSubtree(stringBuilder, n3);
        n3 = this.nextSiblings[n3];
        while (n3 != -1) {
            stringBuilder.append(",");
            this.appendSubtree(stringBuilder, n3);
            n3 = this.nextSiblings[n3];
        }
        stringBuilder.append(')');
    }

    private void appendSubtree(StringBuilder stringBuilder, int n2) {
        stringBuilder.append(this.dispatchers[n2]);
        int n3 = EventDispatchTreeImpl.getChildIndex(this, n2);
        if (n3 != -1) {
            stringBuilder.append("->");
            this.appendTreeLevel(stringBuilder, n3);
        }
    }

    private static int getChildIndex(EventDispatchTreeImpl eventDispatchTreeImpl, int n2) {
        int n3 = eventDispatchTreeImpl.nextChildren[n2];
        if (n3 == -1 && n2 != eventDispatchTreeImpl.tailLastIndex) {
            n3 = eventDispatchTreeImpl.tailFirstIndex;
        }
        return n3;
    }
}

