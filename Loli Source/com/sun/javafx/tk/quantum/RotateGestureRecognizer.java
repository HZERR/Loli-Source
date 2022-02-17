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
import javafx.scene.input.RotateEvent;
import javafx.util.Duration;

class RotateGestureRecognizer
implements GestureRecognizer {
    private ViewScene scene;
    private static double ROTATATION_THRESHOLD = 5.0;
    private static boolean ROTATION_INERTIA_ENABLED = true;
    private static double MAX_INITIAL_VELOCITY = 500.0;
    private static double ROTATION_INERTIA_MILLIS = 1500.0;
    private RotateRecognitionState state = RotateRecognitionState.IDLE;
    private Timeline inertiaTimeline = new Timeline();
    private DoubleProperty inertiaRotationVelocity = new SimpleDoubleProperty();
    private double initialInertiaRotationVelocity = 0.0;
    private double rotationStartTime = 0.0;
    private double lastTouchEventTime = 0.0;
    Map<Long, TouchPointTracker> trackers = new HashMap<Long, TouchPointTracker>();
    int modifiers;
    boolean direct;
    private int currentTouchCount = 0;
    private boolean touchPointsSetChanged;
    private boolean touchPointsPressed;
    int touchPointsInEvent;
    long touchPointID1 = -1L;
    long touchPointID2 = -1L;
    double centerX;
    double centerY;
    double centerAbsX;
    double centerAbsY;
    double currentRotation;
    double angleReference;
    double totalRotation = 0.0;
    double inertiaLastTime = 0.0;

    RotateGestureRecognizer(ViewScene viewScene) {
        this.scene = viewScene;
        this.inertiaRotationVelocity.addListener(observable -> {
            double d2 = this.inertiaTimeline.getCurrentTime().toSeconds();
            double d3 = d2 - this.inertiaLastTime;
            this.inertiaLastTime = d2;
            this.currentRotation = d3 * this.inertiaRotationVelocity.get();
            this.totalRotation += this.currentRotation;
            this.sendRotateEvent(true);
        });
    }

    @Override
    public void notifyBeginTouchEvent(long l2, int n2, boolean bl, int n3) {
        this.params(n2, bl);
        this.touchPointsSetChanged = false;
        this.touchPointsPressed = false;
        this.touchPointsInEvent = 0;
    }

    @Override
    public void notifyNextTouchEvent(long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        ++this.touchPointsInEvent;
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
                throw new RuntimeException("Error in Rotate gesture recognition: unknown touch state: " + (Object)((Object)this.state));
            }
        }
    }

    private void calculateCenter() {
        if (this.currentTouchCount <= 0) {
            throw new RuntimeException("Error in Rotate gesture recognition: touch count is zero!");
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

    private double getAngle(TouchPointTracker touchPointTracker, TouchPointTracker touchPointTracker2) {
        double d2 = touchPointTracker2.getAbsX() - touchPointTracker.getAbsX();
        double d3 = -(touchPointTracker2.getAbsY() - touchPointTracker.getAbsY());
        double d4 = Math.toDegrees(Math.atan2(d3, d2));
        return d4;
    }

    private double getNormalizedDelta(double d2, double d3) {
        double d4 = -(d3 - d2);
        if (d4 > 180.0) {
            d4 -= 360.0;
        } else if (d4 < -180.0) {
            d4 += 360.0;
        }
        return d4;
    }

    private void assignActiveTouchpoints() {
        boolean bl = false;
        if (!this.trackers.containsKey(this.touchPointID1)) {
            this.touchPointID1 = -1L;
            bl = true;
        }
        if (!this.trackers.containsKey(this.touchPointID2)) {
            this.touchPointID2 = -1L;
            bl = true;
        }
        if (bl) {
            for (Long l2 : this.trackers.keySet()) {
                if (l2 == this.touchPointID1 || l2 == this.touchPointID2) continue;
                if (this.touchPointID1 == -1L) {
                    this.touchPointID1 = l2;
                    continue;
                }
                if (this.touchPointID2 != -1L) break;
                this.touchPointID2 = l2;
            }
        }
    }

    @Override
    public void notifyEndTouchEvent(long l2) {
        this.lastTouchEventTime = l2;
        if (this.currentTouchCount != this.trackers.size()) {
            throw new RuntimeException("Error in Rotate gesture recognition: touch count is wrong: " + this.currentTouchCount);
        }
        if (this.currentTouchCount == 0) {
            if (this.state == RotateRecognitionState.ACTIVE) {
                this.sendRotateFinishedEvent();
            }
            if (ROTATION_INERTIA_ENABLED && (this.state == RotateRecognitionState.PRE_INERTIA || this.state == RotateRecognitionState.ACTIVE)) {
                double d2 = ((double)l2 - this.rotationStartTime) / 1000000.0;
                if (d2 < 300.0) {
                    this.state = RotateRecognitionState.INERTIA;
                    this.inertiaLastTime = 0.0;
                    if (this.initialInertiaRotationVelocity > MAX_INITIAL_VELOCITY) {
                        this.initialInertiaRotationVelocity = MAX_INITIAL_VELOCITY;
                    } else if (this.initialInertiaRotationVelocity < -MAX_INITIAL_VELOCITY) {
                        this.initialInertiaRotationVelocity = -MAX_INITIAL_VELOCITY;
                    }
                    this.inertiaTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(0.0), new KeyValue(this.inertiaRotationVelocity, this.initialInertiaRotationVelocity, Interpolator.LINEAR)), new KeyFrame(Duration.millis(ROTATION_INERTIA_MILLIS * Math.abs(this.initialInertiaRotationVelocity) / MAX_INITIAL_VELOCITY), actionEvent -> this.reset(), new KeyValue(this.inertiaRotationVelocity, 0, Interpolator.LINEAR)));
                    this.inertiaTimeline.playFromStart();
                } else {
                    this.reset();
                }
            }
        } else {
            if (this.touchPointsPressed && this.state == RotateRecognitionState.INERTIA) {
                this.inertiaTimeline.stop();
                this.reset();
            }
            if (this.currentTouchCount == 1) {
                if (this.state == RotateRecognitionState.ACTIVE) {
                    this.sendRotateFinishedEvent();
                    if (ROTATION_INERTIA_ENABLED) {
                        this.state = RotateRecognitionState.PRE_INERTIA;
                    } else {
                        this.reset();
                    }
                }
            } else {
                if (this.state == RotateRecognitionState.IDLE) {
                    this.state = RotateRecognitionState.TRACKING;
                    this.assignActiveTouchpoints();
                }
                this.calculateCenter();
                if (this.touchPointsSetChanged) {
                    this.assignActiveTouchpoints();
                }
                TouchPointTracker touchPointTracker = this.trackers.get(this.touchPointID1);
                TouchPointTracker touchPointTracker2 = this.trackers.get(this.touchPointID2);
                double d3 = this.getAngle(touchPointTracker, touchPointTracker2);
                if (this.touchPointsSetChanged) {
                    this.angleReference = d3;
                } else {
                    this.currentRotation = this.getNormalizedDelta(this.angleReference, d3);
                    if (this.state == RotateRecognitionState.TRACKING && Math.abs(this.currentRotation) > ROTATATION_THRESHOLD) {
                        this.state = RotateRecognitionState.ACTIVE;
                        this.sendRotateStartedEvent();
                    }
                    if (this.state == RotateRecognitionState.ACTIVE) {
                        this.totalRotation += this.currentRotation;
                        this.sendRotateEvent(false);
                        this.angleReference = d3;
                        double d4 = ((double)l2 - this.rotationStartTime) / 1.0E9;
                        if (d4 > 1.0E-4) {
                            this.initialInertiaRotationVelocity = this.currentRotation / d4;
                            this.rotationStartTime = l2;
                        }
                    }
                }
            }
        }
    }

    private void sendRotateStartedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent(RotateEvent.ROTATION_STARTED, 0.0, 0.0, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendRotateEvent(boolean bl) {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent(RotateEvent.ROTATE, this.currentRotation, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, bl);
            }
            return null;
        }, this.scene.getAccessControlContext());
    }

    private void sendRotateFinishedEvent() {
        AccessController.doPrivileged(() -> {
            if (this.scene.sceneListener != null) {
                this.scene.sceneListener.rotateEvent(RotateEvent.ROTATION_FINISHED, 0.0, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 0x10) != 0, this.direct, false);
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
        if (this.state != RotateRecognitionState.FAILURE) {
            TouchPointTracker touchPointTracker = this.trackers.get(l2);
            if (touchPointTracker == null) {
                this.state = RotateRecognitionState.FAILURE;
                throw new RuntimeException("Error in Rotate gesture recognition: released unknown touch point");
            }
            this.trackers.remove(l2);
        }
        --this.currentTouchCount;
    }

    public void touchMoved(long l2, long l3, int n2, int n3, int n4, int n5) {
        if (this.state == RotateRecognitionState.FAILURE) {
            return;
        }
        TouchPointTracker touchPointTracker = this.trackers.get(l2);
        if (touchPointTracker == null) {
            this.state = RotateRecognitionState.FAILURE;
            throw new RuntimeException("Error in rotate gesture recognition: reported unknown touch point");
        }
        touchPointTracker.update(l3, n2, n3, n4, n5);
    }

    void reset() {
        this.state = RotateRecognitionState.IDLE;
        this.touchPointID1 = -1L;
        this.touchPointID2 = -1L;
        this.currentRotation = 0.0;
        this.totalRotation = 0.0;
    }

    static {
        AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.gestures.rotate.threshold");
            if (string != null) {
                ROTATATION_THRESHOLD = Double.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.gestures.rotate.inertia")) != null) {
                ROTATION_INERTIA_ENABLED = Boolean.valueOf(string);
            }
            return null;
        });
    }

    private static enum RotateRecognitionState {
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

