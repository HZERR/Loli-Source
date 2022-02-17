/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.pushingpixels.lafwidget.utils.TrackableThread;

public class DeltaQueue {
    protected ArrayList<Deltable> queue = new ArrayList();

    public synchronized void queue(Deltable deltable) {
        int currDiff = deltable.getDelta();
        for (int i2 = 0; i2 < this.queue.size(); ++i2) {
            Deltable currDeltable = this.queue.get(i2);
            if ((currDiff -= currDeltable.getDelta()) > 0) continue;
            if (currDiff == 0) {
                deltable.setDelta(0);
                for (int j2 = i2 + 1; j2 < this.queue.size(); ++j2) {
                    Deltable nextDeltable = this.queue.get(j2);
                    if (nextDeltable.getDelta() <= 0) continue;
                    this.queue.add(j2, deltable);
                    return;
                }
                this.queue.add(this.queue.size(), deltable);
                return;
            }
            deltable.setDelta(currDiff + currDeltable.getDelta());
            currDeltable.decrementDelta(deltable.getDelta());
            this.queue.add(i2, deltable);
            return;
        }
        deltable.setDelta(currDiff);
        this.queue.add(this.queue.size(), deltable);
    }

    public synchronized List<Deltable> dequeue(int delay) {
        LinkedList<Deltable> result = new LinkedList<Deltable>();
        while (this.queue.size() > 0) {
            Deltable next = this.queue.get(0);
            int timeToExpire = next.getDelta();
            next.decrementDelta(delay);
            if (next.getDelta() > 0) break;
            if (timeToExpire > 0) {
                delay -= timeToExpire;
            }
            result.add(next);
            this.queue.remove(0);
        }
        return result;
    }

    public synchronized void removeMatching(DeltaMatcher matcher) {
        while (true) {
            int toRemoveInd = -1;
            Deltable toRemove = null;
            for (int i2 = this.queue.size() - 1; i2 >= 0; --i2) {
                Deltable deltable = this.queue.get(i2);
                if (!matcher.matches(deltable)) continue;
                toRemoveInd = i2;
                toRemove = deltable;
                break;
            }
            if (toRemoveInd < 0) break;
            if (toRemoveInd < this.queue.size() - 1) {
                Deltable next = this.queue.get(toRemoveInd + 1);
                next.incrementDelta(toRemove.getDelta());
            }
            this.queue.remove(toRemoveInd);
        }
    }

    public void dump() {
        System.out.println("Dump");
        for (int i2 = 0; i2 < this.queue.size(); ++i2) {
            System.out.println("\t" + this.queue.get(i2));
        }
    }

    public static void main(String[] args) {
        DeltaQueue dq = new DeltaQueue();
        DeltableTest tpi11 = new DeltableTest(11, 100);
        dq.queue(tpi11);
        dq.dump();
        DeltableTest tpi12 = new DeltableTest(12, 100);
        dq.queue(tpi12);
        dq.dump();
        DeltableTest tpi21 = new DeltableTest(21, 200);
        dq.queue(tpi21);
        dq.dump();
        DeltableTest tpi31 = new DeltableTest(31, 300);
        dq.queue(tpi31);
        dq.dump();
        DeltableTest tpi13 = new DeltableTest(13, 100);
        dq.queue(tpi13);
        dq.dump();
        DeltableTest tpi22 = new DeltableTest(22, 200);
        dq.queue(tpi22);
        dq.dump();
        DeltableTest tpi25 = new DeltableTest(25, 250);
        dq.queue(tpi25);
        dq.dump();
        DeltableTest tpi51 = new DeltableTest(51, 500);
        dq.queue(tpi51);
        dq.dump();
        DeltableTest tpi05 = new DeltableTest(5, 50);
        dq.queue(tpi05);
        dq.dump();
        List<Deltable> gr150 = dq.dequeue(100);
        System.out.println("Dump 150");
        for (int i2 = 0; i2 < gr150.size(); ++i2) {
            DeltableTest tpi = (DeltableTest)gr150.get(i2);
            System.out.println("\t" + tpi);
        }
        dq.dump();
        dq.removeMatching(new DeltaMatcher(){

            @Override
            public boolean matches(Deltable deltable) {
                return ((DeltableTest)deltable).id < 30;
            }
        });
        dq.dump();
        TrackableThread.requestStopAllThreads();
    }

    private static class DeltableTest
    extends Deltable {
        private int id;

        public DeltableTest(int id, int delta) {
            this.id = id;
            this.delta = delta;
        }

        public String toString() {
            return this.id + ":" + this.delta;
        }
    }

    public static interface DeltaMatcher {
        public boolean matches(Deltable var1);
    }

    public static abstract class Deltable {
        protected int delta;

        public int getDelta() {
            return this.delta;
        }

        public void setDelta(int delta) {
            this.delta = delta;
        }

        public void incrementDelta(int diff) {
            this.delta += diff;
        }

        public void decrementDelta(int diff) {
            this.delta -= diff;
        }
    }
}

