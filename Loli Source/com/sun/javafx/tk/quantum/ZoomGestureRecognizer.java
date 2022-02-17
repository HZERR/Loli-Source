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
import javafx.scene.input.ZoomEvent;
import javafx.util.Duration;

class ZoomGestureRecognizer
implements GestureRecognizer {
    private static double ZOOM_FACTOR_THRESHOLD = 0.1;
    private static boolean ZOOM_INERTIA_ENABLED = true;
    private static double MAX_ZOOMIN_VELOCITY = 3.0;
    private static double MAX_ZOOMOUT_VELOCITY = 0.3333;
    private static double ZOOM_INERTIA_MILLIS = 500.0;
    private static double MAX_ZOOM_IN_FACTOR = 10.0;
    private static double MAX_ZOOM_OUT_FACTOR = 0.1;
    private ViewScene scene;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaZoomVelocity = new SimpleDoubleProperty();
    private double initialInertiaZoomVelocity = 0.0;
    private double zoomStartTime = 0.0;
    private double lastTouchEventTime = 0.0;
    private ZoomRecognitionState state = ZoomRecognitionState.IDLE;
    private Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
    private int modifiers;
    private boolean direct;
    private int currentTouchCount = 0;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    private double centerX;
    private double centerY;
    private double centerAbsX;
    private double centerAbsY;
    private double currentDistance;
    private double distanceReference;
    private double zoomFactor = 1.0;
    private double totalZoomFactor = 1.0;
    double inertiaLastTime = 0.0;

    ZoomGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
        this.inertiaZoomVelocity.addListener(observable -> {
            double d2 = this.inertiaTimeline.getCurrentTime().toSeconds();
            double d3 = d2 - this.inertiaLastTime;
            this.inertiaLastTime = d2;
            double d4 = this.totalZoomFactor;
            this.totalZoomFactor += d3 * this.inertiaZoomVelocity.get();
            this.zoomFactor = this.totalZoomFactor / d4;
            this.sendZoomEvent(true);
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
                throw new RuntimeException("Error in Zoom gesture recognition: unknown touch state: " + (Object)((Object)this.state));
            }
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Zoom gesture recognition: touch count is zero!");
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

    private double calculateMaxDistance() {
        double d2 = 0.0;
        for (TouchPointTracker touchPointTracker : this.trackers.values()) {
            double d3;
            double d4 = touchPointTracker.getAbsX() - this.centerAbsX;
            double d5 = d4 * d4 + (d3 = touchPointTracker.getAbsY() - this.centerAbsY) * d3;
            if (!(d5 > d2)) continue;
            d2 = d5;
        }
        return Math.sqrt(d2);
    }

    @Override
    public void notifyEndTouchEvent(long l2) {
        this.lastTouchEventTime = l2;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Zoom gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount == 0) {
            if (this.state == ZoomRecognitionState.ACTIVE) {
                this.sendZoomFinishedEvent();
            }
            if (ZOOM_INERTIA_ENABLED && (this.state == ZoomRecognitionState.PRE_INERTIA || this.state == ZoomRecognitionState.ACTIVE)) {
                double d2 = ((double)l2 - this.zoomStartTime) / 1000000.0;
                if (this.initialInertiaZoomVelocity != 0.0 && d2 < 200.0) {
                    this.state = ZoomRecognitionState.INERTIA;
                    this.inertiaLastTime = 0.0;
                    double d3 = ZOOM_INERTIA_MILLIS / 1000.0;
                    double d4 = this.totalZoomFactor + this.initialInertiaZoomVelocity * d3;
                    if (this.initialInertiaZoomVelocity > 0.0) {
                        if (d4 / this.totalZoomFactor > MAX_ZOOM_IN_FACTOR) {
                            d4 = this.totalZoomFactor * MAX_ZOOM_IN_FACTOR;
                            d3 = (d4 - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                        }
                    } else if (d4 / this.totalZoomFactor < MAX_ZOOM_OUT_FACTOR) {
                        d4 = this.totalZoomFactor * MAX_ZOOM_OUT_FACTOR;
                        d3 = (d4 - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                    }
                    this.inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(0.0), new KeyValue(this.inertiaZoomVelocity, this.initialInertiaZoomVelocity, Interpolator.LINEAR)), new KeyFrame(Duration.seconds(d3), actionEvent -> this.reset(), new KeyValue(this.inertiaZoomVelocity, 0, Interpolator.LINEAR)));
                    this.inertiaTimeline.playFromStart();
                } else {
                    this.reset();
                }
            } else {
                this.reset();
            }
        } else {
            if (this.touchPointsPressed && this.state == ZoomRecognitionState.INERTIA) {
                this.inertiaTimeline.stop();
                this.reset();
            }
            if (this.currentTouchCount == 1) {
                if (this.state == ZoomRecognitionState.ACTIVE) {
                    this.sendZoomFinishedEvent();
                    if (ZOOM_INERTIA_ENABLED) {
                        this.state = ZoomRecognitionState.PRE_INERTIA;
                    } else {
                        this.reset();
                    }
                }
            } else {
                if (this.state == ZoomRecognitionState.IDLE) {
                    this.state = ZoomRecognitionState.TRACKING;
                    this.zoomStartTime = l2;
                }
                this.calculateCenter();
                double d5 = this.calculateMaxDistance();
                if (this.touchPointsSetChanged) {
                    this.distanceReference = d5;
                } else {
                    this.zoomFactor = d5 / this.distanceReference;
                    if (this.state == ZoomRecognitionState.TRACKING && Math.abs(this.zoomFactor - 1.0) > ZOOM_FACTOR_THRESHOLD) {
                        this.state = ZoomRecognitionState.ACTIVE;
                        this.sendZoomStartedEvent();
                    }
                    if (this.state == ZoomRecognitionState.ACTIVE) {
                        double d6 = this.totalZoomFactor;
                        this.totalZoomFactor *= this.zoomFactor;
                        this.sendZoomEvent(false);
                        this.distanceReference = d5;
                        double d7 = ((double)l2 - this.zoomStartTime) / 1.0E9;
                        if (d7 > 1.0E-4) {
                            this.initialInertiaZoomVelocity = (this.totalZoomFactor - d6) / d7;
                            this.zoomStartTime = l2;
                        } else {
                            this.initialInertiaZoomVelocity = 0.0;
                        }
                    }
                }
            }
        }
    }

    private void sendZoomStartedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM_STARTED, 1.0, 1.0, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendZoomEvent(boolean bl) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM, this.zoomFactor, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendZoomFinishedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM_FINISHED, 1.0, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
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
        if (this.state != ZoomRecognitionState.FAILURE) {
            TouchPointTracker touchPointTracker = this.trackers.get(l2);
            if (touchPointTracker == null) {
                this.state = ZoomRecognitionState.FAILURE;
                throw new RuntimeException("Error in Zoom gesture recognition: released unknown touch point");
            }
            this.trackers.remove(l2);
        }
        --this.currentTouchCount;
    }

    public void touchMoved(long l2, long l3, int n2, int n3, int n4, int n5) {
        if (this.state == ZoomRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker touchPointTracker = this.trackers.get(l2);
        if (touchPointTracker == null) {
            this.state = ZoomRecognitionState.FAILURE;
            throw new RuntimeException("Error in zoom gesture recognition: reported unknown touch point");
        }
        touchPointTracker.update(l3, n2, n3, n4, n5);
    }

    void reset() {
        this.state = ZoomRecognitionState.IDLE;
        this.zoomFactor = 1.0;
        this.totalZoomFactor = 1.0;
    }

    static {
        AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.gestures.zoom.threshold");
            if (string != null) {
                ZOOM_FACTOR_THRESHOLD = Double.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.gestures.zoom.inertia")) != null) {
                ZOOM_INERTIA_ENABLED = Boolean.valueOf(string);
            }
            return null;
        });
    }

    private static enum ZoomRecognitionState {
        IDLE,
        TRACKING,
        ACTIVE,
        PRE_INERTIA,
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

