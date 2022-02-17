/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.GestureSupport;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;
import com.sun.glass.ui.mac.MacTouchInputSupport;

final class MacGestureSupport {
    private static final int GESTURE_ROTATE = 100;
    private static final int GESTURE_MAGNIFY = 101;
    private static final int GESTURE_SWIPE = 102;
    private static final int SCROLL_SRC_WHEEL = 50;
    private static final int SCROLL_SRC_GESTURE = 51;
    private static final int SCROLL_SRC_INERTIA = 52;
    private static final double multiplier = 10.0;
    private static final boolean isDirect = false;
    private static final GestureSupport gestures;
    private static final TouchInputSupport touches;

    MacGestureSupport() {
    }

    private static native void _initIDs();

    public static void notifyBeginTouchEvent(View view, int n2, int n3) {
        touches.notifyBeginTouchEvent(view, n2, false, n3);
    }

    public static void notifyNextTouchEvent(View view, int n2, long l2, float f2, float f3) {
        int n3 = (int)(10000.0f * f2);
        int n4 = 10000 - (int)(10000.0f * f3);
        touches.notifyNextTouchEvent(view, n2, l2, n3, n4, n3, n4);
    }

    public static void notifyEndTouchEvent(View view) {
        touches.notifyEndTouchEvent(view);
    }

    public static void rotateGesturePerformed(View view, int n2, int n3, int n4, int n5, int n6, float f2) {
        gestures.handleDeltaRotation(view, n2, false, false, n3, n4, n5, n6, -f2);
    }

    public static void scrollGesturePerformed(View view, int n2, int n3, int n4, int n5, int n6, int n7, float f2, float f3) {
        int n8 = touches.getTouchCount();
        boolean bl = n3 == 52;
        switch (n3) {
            case 50: 
            case 52: {
                GestureSupport.handleScrollingPerformed(view, n2, false, bl, n8, n4, n5, n6, n7, f2, f3, 10.0, 10.0);
                break;
            }
            case 51: {
                gestures.handleDeltaScrolling(view, n2, false, bl, n8, n4, n5, n6, n7, f2, f3, 10.0, 10.0);
                break;
            }
            default: {
                System.err.println("Unknown scroll gesture sender: " + n3);
            }
        }
    }

    public static void swipeGesturePerformed(View view, int n2, int n3, int n4, int n5, int n6, int n7) {
        GestureSupport.handleSwipePerformed(view, n2, false, false, touches.getTouchCount(), n3, n4, n5, n6, n7);
    }

    public static void magnifyGesturePerformed(View view, int n2, int n3, int n4, int n5, int n6, float f2) {
        gestures.handleDeltaZooming(view, n2, false, false, n3, n4, n5, n6, f2, Double.NaN);
    }

    public static void gestureFinished(View view, int n2, int n3, int n4, int n5, int n6) {
        if (gestures.isScrolling()) {
            gestures.handleScrollingEnd(view, n2, touches.getTouchCount(), false, false, n3, n4, n5, n6);
        }
        if (gestures.isRotating()) {
            gestures.handleRotationEnd(view, n2, false, false, n3, n4, n5, n6);
        }
        if (gestures.isZooming()) {
            gestures.handleZoomingEnd(view, n2, false, false, n3, n4, n5, n6);
        }
    }

    static {
        MacGestureSupport._initIDs();
        gestures = new GestureSupport(false);
        touches = new MacTouchInputSupport(gestures.createTouchCountListener(), false);
    }
}

