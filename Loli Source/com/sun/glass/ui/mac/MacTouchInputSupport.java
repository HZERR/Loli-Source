/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MacTouchInputSupport
extends TouchInputSupport {
    private final Map<Long, WeakReference<View>> touchIdToView = new HashMap<Long, WeakReference<View>>();
    private int curModifiers;
    private boolean curIsDirect;
    private List<TouchPoint> curTouchPoints;

    MacTouchInputSupport(TouchInputSupport.TouchCountListener touchCountListener, boolean bl) {
        super(touchCountListener, bl);
    }

    @Override
    public void notifyBeginTouchEvent(View view, int n2, boolean bl, int n3) {
        this.curModifiers = n2;
        this.curIsDirect = bl;
        this.curTouchPoints = new ArrayList<TouchPoint>(n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void notifyEndTouchEvent(View view) {
        if (this.curTouchPoints.isEmpty()) {
            return;
        }
        try {
            super.notifyBeginTouchEvent(view, this.curModifiers, this.curIsDirect, this.curTouchPoints.size());
            for (TouchPoint touchPoint : this.curTouchPoints) {
                super.notifyNextTouchEvent(view, touchPoint.state, touchPoint.id, touchPoint.x, touchPoint.y, touchPoint.xAbs, touchPoint.yAbs);
            }
            super.notifyEndTouchEvent(view);
        }
        finally {
            this.curTouchPoints = null;
        }
    }

    @Override
    public void notifyNextTouchEvent(View view, int n2, long l2, int n3, int n4, int n5, int n6) {
        View view2 = null;
        if (n2 == 811) {
            view2 = view;
            this.touchIdToView.put(l2, new WeakReference<View>(view));
        } else {
            view2 = (View)this.touchIdToView.get(l2).get();
            if (n2 == 813) {
                this.touchIdToView.remove(l2);
            }
        }
        if (view2 == view) {
            this.curTouchPoints.add(new TouchPoint(n2, l2, n3, n4, n5, n6));
        } else {
            if (view2 != null && view2.isClosed()) {
                view2 = null;
            }
            super.notifyBeginTouchEvent(view2, this.curModifiers, this.curIsDirect, 1);
            super.notifyNextTouchEvent(view2, n2, l2, n3, n4, n5, n6);
            super.notifyEndTouchEvent(view2);
        }
    }

    private static class TouchPoint {
        final int state;
        final long id;
        final int x;
        final int y;
        final int xAbs;
        final int yAbs;

        TouchPoint(int n2, long l2, int n3, int n4, int n5, int n6) {
            this.state = n2;
            this.id = l2;
            this.x = n3;
            this.y = n4;
            this.xAbs = n5;
            this.yAbs = n6;
        }
    }
}

