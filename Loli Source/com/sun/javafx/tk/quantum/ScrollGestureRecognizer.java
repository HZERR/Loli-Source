/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.quantum.GestureRecognizer;
import com.sun.javafx.tk.quantum.ViewScene;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

class ScrollGestureRecognizer
implements GestureRecognizer {
    private static double SCROLL_THRESHOLD = 10.0;
    private static boolean SCROLL_INERTIA_ENABLED = true;
    private static double MAX_INITIAL_VELOCITY = 1000.0;
    private static double SCROLL_INERTIA_MILLIS = 1500.0;
    private ViewScene scene;
    private ScrollRecognitionState state = ScrollRecognitionState.IDLE;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaScrollVelocity = new SimpleDoubleProperty();
    private double initialInertiaScrollVelocity = 0.0;
    private double scrollStartTime = 0.0;
    private double lastTouchEventTime = 0.0;
    private Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
    private int modifiers;
    private boolean direct;
    private int currentTouchCount = 0;
    private int lastTouchCount;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    private double centerX;
    private double centerY;
    private double centerAbsX;
    private double centerAbsY;
    private double lastCenterAbsX;
    private double lastCenterAbsY;
    private double deltaX;
    private double deltaY;
    private double totalDeltaX;
    private double totalDeltaY;
    private double factorX;
    private double factorY;
    double inertiaLastTime = 0.0;

    ScrollGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
        this.inertiaScrollVelocity.addListener(observable -> {
            double d2 = this.inertiaTimeline.getCurrentTime().toSeconds();
            double d3 = d2 - this.inertiaLastTime;
            this.inertiaLastTime = d2;
            double d4 = d3 * this.inertiaScrollVelocity.get();
            this.deltaX = d4 * this.factorX;
            this.totalDeltaX += this.deltaX;
            this.deltaY = d4 * this.factorY;
            this.totalDeltaY += this.deltaY;
            this.sendScrollEvent(true, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
        });
    }

    @Override
    public void notifyBeginTouchEvent(long l2, int n2, boolean bl, int n3) {
        this.params(n2, bl);
        this.touchPointsSetChanged = false;
        this.touchPointsPressed = false;
    }

    @Override
    public void notifyNextTouchEvent(long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        switch (n2) {
            case 811: {
                this.touchPointsSetChanged = true;
                this.touchPointsPressed = true;
                this.touchPressed(l3, l2, n3, n4, n5, n6);
                break;
            }
            case 814: {
                break;
            }
            case 812: {
                this.touchMoved(l3, l2, n3, n4, n5, n6);
                break;
            }
            case 813: {
                this.touchPointsSetChanged = true;
                this.touchReleased(l3, l2, n3, n4, n5, n6);
                break;
            }
            default: {
                throw new RuntimeException("Error in Scroll gesture recognition: unknown touch state: " + (Object)((Object)this.state));
            }
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Scroll gesture recognition: touch count is zero!");
        }
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        for (TouchPointTracker touchPointTracker : this.trackers.values()) {
            d2 += touchPointTracker.getX();
            d3 += touchPointTracker.getY();
            d4 += touchPointTracker.getAbsX();
            d5 += touchPointTracker.getAbsY();
        }
        this.centerX = d2 / (double)this.currentTouchCount;
        this.centerY = d3 / (double)this.currentTouchCount;
        this.centerAbsX = d4 / (double)this.currentTouchCount;
        this.centerAbsY = d5 / (double)this.currentTouchCount;
    }

    @Override
    public void notifyEndTouchEvent(long l2) {
        this.lastTouchEventTime = l2;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Scroll gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount < 1) {
            if (this.state == ScrollRecognitionState.ACTIVE) {
                this.sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                if (SCROLL_INERTIA_ENABLED) {
                    double d2 = ((double)l2 - this.scrollStartTime) / 1000000.0;
                    if (d2 < 300.0) {
                        this.state = ScrollRecognitionState.INERTIA;
                        this.inertiaLastTime = 0.0;
                        if (this.initialInertiaScrollVelocity > MAX_INITIAL_VELOCITY) {
                            this.initialInertiaScrollVelocity = MAX_INITIAL_VELOCITY;
                        }
                        this.inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(0.0), new KeyValue(this.inertiaScrollVelocity, this.initialInertiaScrollVelocity, Interpolator.LINEAR)), new KeyFrame(Duration.millis(SCROLL_INERTIA_MILLIS * Math.abs(this.initialInertiaScrollVelocity) / MAX_INITIAL_VELOCITY), actionEvent -> this.reset(), new KeyValue(this.inertiaScrollVelocity, 0, Interpolator.LINEAR)));
                        this.inertiaTimeline.playFromStart();
                    } else {
                        this.reset();
                    }
                } else {
                    this.reset();
                }
            }
        } else {
            this.calculateCenter();
            if (this.touchPointsPressed && this.state == ScrollRecognitionState.INERTIA) {
                this.inertiaTimeline.stop();
                this.reset();
            }
            if (this.touchPointsSetChanged) {
                if (this.state == ScrollRecognitionState.IDLE) {
                    this.state = ScrollRecognitionState.TRACKING;
                }
                if (this.state == ScrollRecognitionState.ACTIVE) {
                    this.sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                    this.totalDeltaX = 0.0;
                    this.totalDeltaY = 0.0;
                    this.sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                }
                this.lastTouchCount = this.currentTouchCount;
                this.lastCenterAbsX = this.centerAbsX;
                this.lastCenterAbsY = this.centerAbsY;
            } else {
                this.deltaX = this.centerAbsX - this.lastCenterAbsX;
                this.deltaY = this.centerAbsY - this.lastCenterAbsY;
                if (this.state == ScrollRecognitionState.TRACKING && (Math.abs(this.deltaX) > SCROLL_THRESHOLD || Math.abs(this.deltaY) > SCROLL_THRESHOLD)) {
                    this.state = ScrollRecognitionState.ACTIVE;
                    this.sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                }
                if (this.state == ScrollRecognitionState.ACTIVE) {
                    this.totalDeltaX += this.deltaX;
                    this.totalDeltaY += this.deltaY;
                    this.sendScrollEvent(false, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                    double d3 = ((double)l2 - this.scrollStartTime) / 1.0E9;
                    if (d3 > 1.0E-4) {
                        double d4 = Math.sqrt(this.deltaX * this.deltaX + this.deltaY * this.deltaY);
                        this.factorX = this.deltaX / d4;
                        this.factorY = this.deltaY / d4;
                        this.initialInertiaScrollVelocity = d4 / d3;
                        this.scrollStartTime = l2;
                    }
                    this.lastCenterAbsX = this.centerAbsX;
                    this.lastCenterAbsY = this.centerAbsY;
                }
            }
        }
    }

    private void sendScrollStartedEvent(double d2, double d3, int n2) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL_STARTED, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, n2, 0, 0, 0, 0, d2, d3, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendScrollEvent(boolean bl, double d2, double d3, int n2) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL, this.deltaX, this.deltaY, this.totalDeltaX, this.totalDeltaY, 1.0, 1.0, n2, 0, 0, 0, 0, d2, d3, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendScrollFinishedEvent(double d2, double d3, int n2) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL_FINISHED, 0.0, 0.0, this.totalDeltaX, this.totalDeltaY, 1.0, 1.0, n2, 0, 0, 0, 0, d2, d3, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    public void params(int n2, boolean bl) {
        this.modifiers = n2;
        this.direct = bl;
    }

    public void touchPressed(long l2, long l3, int n2, int n3, int n4, int n5) {
        ++this.currentTouchCount;
        TouchPointTracker touchPointTracker = new TouchPointTracker();
        touchPointTracker.update(l3, n2, n3, n4, n5);
        this.trackers.put(l2, touchPointTracker);
    }

    public void touchReleased(long l2, long l3, int n2, int n3, int n4, int n5) {
        if (this.state != ScrollRecognitionState.FAILURE) {
            TouchPointTracker touchPointTracker = this.trackers.get(l2);
            if (touchPointTracker == null) {
                this.state = ScrollRecognitionState.FAILURE;
                throw new RuntimeException("Error in Scroll gesture recognition: released unknown touch point");
            }
            this.trackers.remove(l2);
        }
        --this.currentTouchCount;
    }

    public void touchMoved(long l2, long l3, int n2, int n3, int n4, int n5) {
        if (this.state == ScrollRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker touchPointTracker = this.trackers.get(l2);
        if (touchPointTracker == null) {
            this.state = ScrollRecognitionState.FAILURE;
            throw new RuntimeException("Error in scroll gesture recognition: reported unknown touch point");
        }
        touchPointTracker.update(l3, n2, n3, n4, n5);
    }

    void reset() {
        this.state = ScrollRecognitionState.IDLE;
        this.totalDeltaX = 0.0;
        this.totalDeltaY = 0.0;
    }

    static {
        AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.gestures.scroll.threshold");
            if (string != null) {
                SCROLL_THRESHOLD = Double.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.gestures.scroll.inertia")) != null) {
                SCROLL_INERTIA_ENABLED = Boolean.valueOf(string);
            }
            return null;
        });
    }

    private static enum ScrollRecognitionState {
        IDLE,
        TRACKING,
        ACTIVE,
        INERTIA,
        FAILURE;

    }

    private static class TouchPointTracker {
        double x;
        double y;
        double absX;
        double absY;

        private TouchPointTracker() {
        }

        public void update(long l2, double d2, double d3, double d4, double d5) {
            this.x = d2;
            this.y = d3;
            this.absX = d4;
            this.absY = d5;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getAbsX() {
            return this.absX;
        }

        public double getAbsY() {
            return this.absY;
        }
    }
}

