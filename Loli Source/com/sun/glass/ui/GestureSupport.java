/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;

public final class GestureSupport {
    private static final double THRESHOLD_SCROLL = 1.0;
    private static final double THRESHOLD_SCALE = 0.01;
    private static final double THRESHOLD_EXPANSION = 0.01;
    private static final double THRESHOLD_ROTATE = Math.toDegrees(Math.PI / 180);
    private final GestureState scrolling = new GestureState();
    private final GestureState rotating = new GestureState();
    private final GestureState zooming = new GestureState();
    private final GestureState swiping = new GestureState();
    private double totalScrollX = Double.NaN;
    private double totalScrollY = Double.NaN;
    private double totalScale = 1.0;
    private double totalExpansion = Double.NaN;
    private double totalRotation = 0.0;
    private double multiplierX = 1.0;
    private double multiplierY = 1.0;
    private boolean zoomWithExpansion;

    public GestureSupport(boolean bl) {
        this.zoomWithExpansion = bl;
    }

    private static double multiplicativeDelta(double d2, double d3) {
        if (d2 == 0.0) {
            return Double.NaN;
        }
        return d3 / d2;
    }

    private int setScrolling(boolean bl) {
        return this.scrolling.updateProgress(bl);
    }

    private int setRotating(boolean bl) {
        return this.rotating.updateProgress(bl);
    }

    private int setZooming(boolean bl) {
        return this.zooming.updateProgress(bl);
    }

    private int setSwiping(boolean bl) {
        return this.swiping.updateProgress(bl);
    }

    public boolean isScrolling() {
        return !this.scrolling.isIdle();
    }

    public boolean isRotating() {
        return !this.rotating.isIdle();
    }

    public boolean isZooming() {
        return !this.zooming.isIdle();
    }

    public boolean isSwiping() {
        return !this.swiping.isIdle();
    }

    public void handleScrollingEnd(View view, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7) {
        this.scrolling.setIdle();
        if (bl2) {
            return;
        }
        view.notifyScrollGestureEvent(3, n2, bl, bl2, n3, n4, n5, n6, n7, 0.0, 0.0, this.totalScrollX, this.totalScrollY, this.multiplierX, this.multiplierY);
    }

    public void handleRotationEnd(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6) {
        this.rotating.setIdle();
        if (bl2) {
            return;
        }
        view.notifyRotateGestureEvent(3, n2, bl, bl2, n3, n4, n5, n6, 0.0, this.totalRotation);
    }

    public void handleZoomingEnd(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6) {
        this.zooming.setIdle();
        if (bl2) {
            return;
        }
        view.notifyZoomGestureEvent(3, n2, bl, bl2, n3, n4, n5, n6, Double.NaN, 0.0, this.totalScale, this.totalExpansion);
    }

    public void handleSwipeEnd(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6) {
        this.swiping.setIdle();
        if (bl2) {
            return;
        }
        view.notifySwipeGestureEvent(3, n2, bl, bl2, Integer.MAX_VALUE, Integer.MAX_VALUE, n3, n4, n5, n6);
    }

    public void handleTotalZooming(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, double d2, double d3) {
        double d4 = this.totalScale;
        double d5 = this.totalExpansion;
        if (this.zooming.doesGestureStart(bl2)) {
            d4 = 1.0;
            d5 = 0.0;
        }
        if (Math.abs(d2 - d4) < 0.01 && (!this.zoomWithExpansion || Math.abs(d3 - d5) < 0.01)) {
            return;
        }
        double d6 = Double.NaN;
        if (this.zoomWithExpansion) {
            d6 = d3 - d5;
        } else {
            d3 = Double.NaN;
        }
        this.totalScale = d2;
        this.totalExpansion = d3;
        int n7 = this.setZooming(bl2);
        view.notifyZoomGestureEvent(n7, n2, bl, bl2, n3, n4, n5, n6, GestureSupport.multiplicativeDelta(d4, this.totalScale), d6, d2, d3);
    }

    public void handleTotalRotation(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, double d2) {
        double d3 = this.totalRotation;
        if (this.rotating.doesGestureStart(bl2)) {
            d3 = 0.0;
        }
        if (Math.abs(d2 - d3) < THRESHOLD_ROTATE) {
            return;
        }
        this.totalRotation = d2;
        int n7 = this.setRotating(bl2);
        view.notifyRotateGestureEvent(n7, n2, bl, bl2, n3, n4, n5, n6, d2 - d3, d2);
    }

    public void handleTotalScrolling(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        this.multiplierX = d4;
        this.multiplierY = d5;
        double d6 = this.totalScrollX;
        double d7 = this.totalScrollY;
        if (this.scrolling.doesGestureStart(bl2)) {
            d6 = 0.0;
            d7 = 0.0;
        }
        if (Math.abs(d2 - this.totalScrollX) < 1.0 && Math.abs(d3 - this.totalScrollY) < 1.0) {
            return;
        }
        this.totalScrollX = d2;
        this.totalScrollY = d3;
        int n8 = this.setScrolling(bl2);
        view.notifyScrollGestureEvent(n8, n2, bl, bl2, n3, n4, n5, n6, n7, d2 - d6, d3 - d7, d2, d3, d4, d5);
    }

    public void handleDeltaZooming(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, double d2, double d3) {
        double d4 = this.totalScale;
        double d5 = this.totalExpansion;
        if (this.zooming.doesGestureStart(bl2)) {
            d4 = 1.0;
            d5 = 0.0;
        }
        this.totalScale = d4 * (1.0 + d2);
        this.totalExpansion = this.zoomWithExpansion ? d5 + d3 : Double.NaN;
        int n7 = this.setZooming(bl2);
        view.notifyZoomGestureEvent(n7, n2, bl, bl2, n3, n4, n5, n6, GestureSupport.multiplicativeDelta(d4, this.totalScale), d3, this.totalScale, this.totalExpansion);
    }

    public void handleDeltaRotation(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, double d2) {
        double d3 = this.totalRotation;
        if (this.rotating.doesGestureStart(bl2)) {
            d3 = 0.0;
        }
        this.totalRotation = d3 + d2;
        int n7 = this.setRotating(bl2);
        view.notifyRotateGestureEvent(n7, n2, bl, bl2, n3, n4, n5, n6, d2, this.totalRotation);
    }

    public void handleDeltaScrolling(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        this.multiplierX = d4;
        this.multiplierY = d5;
        double d6 = this.totalScrollX;
        double d7 = this.totalScrollY;
        if (this.scrolling.doesGestureStart(bl2)) {
            d6 = 0.0;
            d7 = 0.0;
        }
        this.totalScrollX = d6 + d2;
        this.totalScrollY = d7 + d3;
        int n8 = this.setScrolling(bl2);
        view.notifyScrollGestureEvent(n8, n2, bl, bl2, n3, n4, n5, n6, n7, d2, d3, this.totalScrollX, this.totalScrollY, d4, d5);
    }

    public void handleSwipe(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = this.setSwiping(bl2);
        view.notifySwipeGestureEvent(n9, n2, bl, bl2, n3, n4, n5, n6, n7, n8);
    }

    public static void handleSwipePerformed(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, int n8) {
        view.notifySwipeGestureEvent(2, n2, bl, bl2, n3, n4, n5, n6, n7, n8);
    }

    public static void handleScrollingPerformed(View view, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        view.notifyScrollGestureEvent(2, n2, bl, bl2, n3, n4, n5, n6, n7, d2, d3, d2, d3, d4, d5);
    }

    public TouchInputSupport.TouchCountListener createTouchCountListener() {
        Application.checkEventThread();
        return (touchInputSupport, view, n2, bl) -> {
            boolean bl2 = false;
            if (this.isScrolling()) {
                this.handleScrollingEnd(view, n2, touchInputSupport.getTouchCount(), bl, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            if (this.isRotating()) {
                this.handleRotationEnd(view, n2, bl, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            if (this.isZooming()) {
                this.handleZoomingEnd(view, n2, bl, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        };
    }

    private static class GestureState {
        private StateId id = StateId.Idle;

        private GestureState() {
        }

        void setIdle() {
            this.id = StateId.Idle;
        }

        boolean isIdle() {
            return this.id == StateId.Idle;
        }

        int updateProgress(boolean bl) {
            int n2 = 2;
            if (this.doesGestureStart(bl) && !bl) {
                n2 = 1;
            }
            this.id = bl ? StateId.Inertia : StateId.Running;
            return n2;
        }

        boolean doesGestureStart(boolean bl) {
            switch (this.id) {
                case Running: {
                    return bl;
                }
                case Inertia: {
                    return !bl;
                }
            }
            return true;
        }

        static enum StateId {
            Idle,
            Running,
            Inertia;

        }
    }
}

