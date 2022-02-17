/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.View;
import java.util.HashMap;
import java.util.Map;

public class TouchInputSupport {
    private int touchCount = 0;
    private boolean filterTouchCoordinates;
    private Map<Long, TouchCoord> touch;
    private TouchCountListener listener;
    private int curTouchCount;
    private View curView;
    private int curModifiers;
    private boolean curIsDirect;

    public TouchInputSupport(TouchCountListener touchCountListener, boolean bl) {
        Application.checkEventThread();
        this.listener = touchCountListener;
        this.filterTouchCoordinates = bl;
        if (bl) {
            this.touch = new HashMap<Long, TouchCoord>();
        }
    }

    public int getTouchCount() {
        Application.checkEventThread();
        return this.touchCount;
    }

    public void notifyBeginTouchEvent(View view, int n2, boolean bl, int n3) {
        if (this.curView != null && view != this.curView && this.touchCount != 0 && this.touch != null) {
            if (!this.curView.isClosed()) {
                this.curView.notifyBeginTouchEvent(0, true, this.touchCount);
                for (Map.Entry<Long, TouchCoord> entry : this.touch.entrySet()) {
                    TouchCoord touchCoord = entry.getValue();
                    this.curView.notifyNextTouchEvent(813, entry.getKey(), touchCoord.x, touchCoord.y, touchCoord.xAbs, touchCoord.yAbs);
                }
                this.curView.notifyEndTouchEvent();
            }
            this.touch.clear();
            this.touchCount = 0;
            if (this.listener != null) {
                this.listener.touchCountChanged(this, this.curView, 0, true);
            }
        }
        this.curTouchCount = this.touchCount;
        this.curView = view;
        this.curModifiers = n2;
        this.curIsDirect = bl;
        if (view != null) {
            view.notifyBeginTouchEvent(n2, bl, n3);
        }
    }

    public void notifyEndTouchEvent(View view) {
        if (view == null) {
            return;
        }
        view.notifyEndTouchEvent();
        if (this.curTouchCount != 0 && this.touchCount != 0 && this.curTouchCount != this.touchCount && this.listener != null) {
            this.listener.touchCountChanged(this, this.curView, this.curModifiers, this.curIsDirect);
        }
    }

    public void notifyNextTouchEvent(View view, int n2, long l2, int n3, int n4, int n5, int n6) {
        switch (n2) {
            case 813: {
                --this.touchCount;
                break;
            }
            case 811: {
                ++this.touchCount;
                break;
            }
            case 812: 
            case 814: {
                break;
            }
            default: {
                System.err.println("Unknown touch state: " + n2);
                return;
            }
        }
        if (this.filterTouchCoordinates) {
            n2 = this.filterTouchInputState(n2, l2, n3, n4, n5, n6);
        }
        if (view != null) {
            view.notifyNextTouchEvent(n2, l2, n3, n4, n5, n6);
        }
    }

    private int filterTouchInputState(int n2, long l2, int n3, int n4, int n5, int n6) {
        switch (n2) {
            case 813: {
                this.touch.remove(l2);
                break;
            }
            case 812: {
                TouchCoord touchCoord = this.touch.get(l2);
                if (n3 == touchCoord.x && n4 == touchCoord.y) {
                    n2 = 814;
                    break;
                }
            }
            case 811: {
                this.touch.put(l2, new TouchCoord(n3, n4, n5, n6));
                break;
            }
            case 814: {
                break;
            }
            default: {
                System.err.println("Unknown touch state: " + n2);
            }
        }
        return n2;
    }

    public static interface TouchCountListener {
        public void touchCountChanged(TouchInputSupport var1, View var2, int var3, boolean var4);
    }

    private static class TouchCoord {
        private final int x;
        private final int y;
        private final int xAbs;
        private final int yAbs;

        private TouchCoord(int n2, int n3, int n4, int n5) {
            this.x = n2;
            this.y = n3;
            this.xAbs = n4;
            this.yAbs = n5;
        }
    }
}

