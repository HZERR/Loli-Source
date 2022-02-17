/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.quantum.GestureRecognizer;
import com.sun.javafx.tk.quantum.ViewScene;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventType;
import javafx.scene.input.SwipeEvent;

class SwipeGestureRecognizer
implements GestureRecognizer {
    private static final double TANGENT_30_DEGREES = 0.577;
    private static final double TANGENT_45_DEGREES = 1.0;
    private static final boolean VERBOSE = false;
    private static final double DISTANCE_THRESHOLD = 10.0;
    private static final double BACKWARD_DISTANCE_THRASHOLD = 5.0;
    private SwipeRecognitionState state = SwipeRecognitionState.IDLE;
    MultiTouchTracker tracker = new MultiTouchTracker();
    private ViewScene scene;

    SwipeGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
    }

    @Override
    public void notifyBeginTouchEvent(long l2, int n2, boolean bl, int n3) {
        this.tracker.params(n2, bl);
    }

    @Override
    public void notifyNextTouchEvent(long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        switch (n2) {
            case 811: {
                this.tracker.pressed(l3, l2, n3, n4, n5, n6);
                break;
            }
            case 812: 
            case 814: {
                this.tracker.progress(l3, l2, n5, n6);
                break;
            }
            case 813: {
                this.tracker.released(l3, l2, n3, n4, n5, n6);
                break;
            }
            default: {
                throw new RuntimeException("Error in swipe gesture recognition: unknown touch state: " + (Object)((Object)this.state));
            }
        }
    }

    @Override
    public void notifyEndTouchEvent(long l2) {
    }

    private EventType<SwipeEvent> calcSwipeType(TouchPointTracker touchPointTracker) {
        double d2;
        double d3;
        double d4 = touchPointTracker.getDistanceX();
        double d5 = touchPointTracker.getDistanceY();
        double d6 = Math.abs(d4);
        boolean bl = d6 > (d3 = Math.abs(d5));
        double d7 = bl ? d4 : d5;
        double d8 = bl ? d6 : d3;
        double d9 = bl ? d3 : d6;
        double d10 = bl ? touchPointTracker.lengthX : touchPointTracker.lengthY;
        double d11 = bl ? touchPointTracker.maxDeviationY : touchPointTracker.maxDeviationX;
        double d12 = d2 = bl ? touchPointTracker.lastXMovement : touchPointTracker.lastYMovement;
        if (d8 <= 10.0) {
            return null;
        }
        if (d9 > d8 * 0.577) {
            return null;
        }
        if (d11 > d8 * 1.0) {
            return null;
        }
        int n2 = Integer.getInteger("com.sun.javafx.gestures.swipe.maxduration", 300);
        if (touchPointTracker.getDuration() > (long)n2) {
            return null;
        }
        if (d10 > d8 * 1.5) {
            return null;
        }
        if (Math.signum(d7) != Math.signum(d2) && Math.abs(d2) > 5.0) {
            return null;
        }
        if (bl) {
            return touchPointTracker.getDistanceX() < 0.0 ? SwipeEvent.SWIPE_LEFT : SwipeEvent.SWIPE_RIGHT;
        }
        return touchPointTracker.getDistanceY() < 0.0 ? SwipeEvent.SWIPE_UP : SwipeEvent.SWIPE_DOWN;
    }

    private void handleSwipeType(EventType<SwipeEvent> eventType, CenterComputer centerComputer, int n2, int n3, boolean bl) {
        if (eventType == null) {
            return;
        }
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.swipeEvent(eventType, n2, centerComputer.getX(), centerComputer.getY(), centerComputer.getAbsX(), centerComputer.getAbsY(), (n3 & 1) != 0, (n3 & 4) != 0, (n3 & 8) != 0, (n3 & 0x10) != 0, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private static enum SwipeRecognitionState {
        IDLE,
        ADDING,
        REMOVING,
        FAILURE;

    }

    private static class TouchPointTracker {
        long beginTime;
        long endTime;
        double beginX;
        double beginY;
        double endX;
        double endY;
        double beginAbsX;
        double beginAbsY;
        double endAbsX;
        double endAbsY;
        double lengthX;
        double lengthY;
        double maxDeviationX;
        double maxDeviationY;
        double lastXMovement;
        double lastYMovement;
        double lastX;
        double lastY;

        private TouchPointTracker() {
        }

        public void start(long l2, double d2, double d3, double d4, double d5) {
            this.beginX = d2;
            this.beginY = d3;
            this.beginAbsX = d4;
            this.beginAbsY = d5;
            this.lastX = d4;
            this.lastY = d5;
            this.beginTime = l2 / 1000000L;
        }

        public void end(long l2, double d2, double d3, double d4, double d5) {
            this.progress(l2, d4, d5);
            this.endX = d2;
            this.endY = d3;
            this.endAbsX = d4;
            this.endAbsY = d5;
            this.endTime = l2 / 1000000L;
        }

        public void progress(long l2, double d2, double d3) {
            double d4;
            double d5 = d2 - this.lastX;
            double d6 = d3 - this.lastY;
            this.lengthX += Math.abs(d5);
            this.lengthY += Math.abs(d6);
            this.lastX = d2;
            this.lastY = d3;
            double d7 = Math.abs(d2 - this.beginAbsX);
            if (d7 > this.maxDeviationX) {
                this.maxDeviationX = d7;
            }
            if ((d4 = Math.abs(d3 - this.beginAbsY)) > this.maxDeviationY) {
                this.maxDeviationY = d4;
            }
            this.lastXMovement = Math.signum(d5) == Math.signum(this.lastXMovement) ? (this.lastXMovement += d5) : d5;
            this.lastYMovement = Math.signum(d6) == Math.signum(this.lastYMovement) ? (this.lastYMovement += d6) : d6;
        }

        public double getDistanceX() {
            return this.endX - this.beginX;
        }

        public double getDistanceY() {
            return this.endY - this.beginY;
        }

        public long getDuration() {
            return this.endTime - this.beginTime;
        }
    }

    private class MultiTouchTracker {
        SwipeRecognitionState state = SwipeRecognitionState.IDLE;
        Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
        CenterComputer cc = new CenterComputer();
        int modifiers;
        boolean direct;
        private int touchCount;
        private int currentTouchCount;
        private EventType<SwipeEvent> type;

        private MultiTouchTracker() {
        }

        public void params(int n2, boolean bl) {
            this.modifiers = n2;
            this.direct = bl;
        }

        public void pressed(long l2, long l3, int n2, int n3, int n4, int n5) {
            ++this.currentTouchCount;
            switch (this.state) {
                case IDLE: {
                    this.currentTouchCount = 1;
                    this.state = SwipeRecognitionState.ADDING;
                }
                case ADDING: {
                    TouchPointTracker touchPointTracker = new TouchPointTracker();
                    touchPointTracker.start(l3, n2, n3, n4, n5);
                    this.trackers.put(l2, touchPointTracker);
                    break;
                }
                case REMOVING: {
                    this.state = SwipeRecognitionState.FAILURE;
                    break;
                }
            }
        }

        public void released(long l2, long l3, int n2, int n3, int n4, int n5) {
            if (this.state != SwipeRecognitionState.FAILURE) {
                TouchPointTracker touchPointTracker = this.trackers.get(l2);
                if (touchPointTracker == null) {
                    this.state = SwipeRecognitionState.FAILURE;
                    throw new RuntimeException("Error in swipe gesture recognition: released unknown touch point");
                }
                touchPointTracker.end(l3, n2, n3, n4, n5);
                this.cc.add(touchPointTracker.beginX, touchPointTracker.beginY, touchPointTracker.beginAbsX, touchPointTracker.beginAbsY);
                this.cc.add(touchPointTracker.endX, touchPointTracker.endY, touchPointTracker.endAbsX, touchPointTracker.endAbsY);
                EventType eventType = SwipeGestureRecognizer.this.calcSwipeType(touchPointTracker);
                switch (this.state) {
                    case IDLE: {
                        this.reset();
                        throw new RuntimeException("Error in swipe gesture recognition: released touch point outside of gesture");
                    }
                    case ADDING: {
                        this.state = SwipeRecognitionState.REMOVING;
                        this.touchCount = this.currentTouchCount;
                        this.type = eventType;
                        break;
                    }
                    case REMOVING: {
                        if (this.type == eventType) break;
                        this.state = SwipeRecognitionState.FAILURE;
                        break;
                    }
                }
                this.trackers.remove(l2);
            }
            --this.currentTouchCount;
            if (this.currentTouchCount == 0) {
                if (this.state == SwipeRecognitionState.REMOVING) {
                    SwipeGestureRecognizer.this.handleSwipeType(this.type, this.cc, this.touchCount, this.modifiers, this.direct);
                }
                this.state = SwipeRecognitionState.IDLE;
                this.reset();
            }
        }

        public void progress(long l2, long l3, int n2, int n3) {
            if (this.state == SwipeRecognitionState.FAILURE) {
                return;
            }
            TouchPointTracker touchPointTracker = this.trackers.get(l2);
            if (touchPointTracker == null) {
                this.state = SwipeRecognitionState.FAILURE;
                throw new RuntimeException("Error in swipe gesture recognition: reported unknown touch point");
            }
            touchPointTracker.progress(l3, n2, n3);
        }

        void reset() {
            this.trackers.clear();
            this.cc.reset();
            this.state = SwipeRecognitionState.IDLE;
        }
    }

    private static class CenterComputer {
        double totalAbsX = 0.0;
        double totalAbsY = 0.0;
        double totalX = 0.0;
        double totalY = 0.0;
        int count = 0;

        private CenterComputer() {
        }

        public void add(double d2, double d3, double d4, double d5) {
            this.totalAbsX += d4;
            this.totalAbsY += d5;
            this.totalX += d2;
            this.totalY += d3;
            ++this.count;
        }

        public double getX() {
            return this.count == 0 ? 0.0 : this.totalX / (double)this.count;
        }

        public double getY() {
            return this.count == 0 ? 0.0 : this.totalY / (double)this.count;
        }

        public double getAbsX() {
            return this.count == 0 ? 0.0 : this.totalAbsX / (double)this.count;
        }

        public double getAbsY() {
            return this.count == 0 ? 0.0 : this.totalAbsY / (double)this.count;
        }

        public void reset() {
            this.totalX = 0.0;
            this.totalY = 0.0;
            this.totalAbsX = 0.0;
            this.totalAbsY = 0.0;
            this.count = 0;
        }
    }
}

