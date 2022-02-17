/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Platform;
import com.sun.glass.ui.Window;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Map;

public abstract class View {
    public static final int GESTURE_NO_VALUE = Integer.MAX_VALUE;
    public static final double GESTURE_NO_DOUBLE_VALUE = Double.NaN;
    public static final byte IME_ATTR_INPUT = 0;
    public static final byte IME_ATTR_TARGET_CONVERTED = 1;
    public static final byte IME_ATTR_CONVERTED = 2;
    public static final byte IME_ATTR_TARGET_NOTCONVERTED = 3;
    public static final byte IME_ATTR_INPUT_ERROR = 4;
    static final boolean accessible = AccessController.doPrivileged(() -> {
        String string = System.getProperty("glass.accessible.force");
        if (string != null) {
            return Boolean.parseBoolean(string);
        }
        try {
            String string2 = Platform.determinePlatform();
            String string3 = System.getProperty("os.version").replaceFirst("(\\d+)\\.\\d+.*", "$1");
            String string4 = System.getProperty("os.version").replaceFirst("\\d+\\.(\\d+).*", "$1");
            int n2 = Integer.parseInt(string3) * 100 + Integer.parseInt(string4);
            return string2.equals("Mac") && n2 >= 1009 || string2.equals("Win") && n2 >= 601;
        }
        catch (Exception exception) {
            return false;
        }
    });
    private volatile long ptr;
    private Window window;
    private EventHandler eventHandler;
    private int width = -1;
    private int height = -1;
    private boolean isValid = false;
    private boolean isVisible = false;
    private boolean inFullscreen = false;
    private static WeakReference<View> lastClickedView = null;
    private static int lastClickedButton;
    private static long lastClickedTime;
    private static int lastClickedX;
    private static int lastClickedY;
    private static int clickCount;
    private static boolean dragProcessed;
    private ClipboardAssistance dropSourceAssistant;
    ClipboardAssistance dropTargetAssistant;

    public static long getMultiClickTime() {
        Application.checkEventThread();
        return Application.GetApplication().staticView_getMultiClickTime();
    }

    public static int getMultiClickMaxX() {
        Application.checkEventThread();
        return Application.GetApplication().staticView_getMultiClickMaxX();
    }

    public static int getMultiClickMaxY() {
        Application.checkEventThread();
        return Application.GetApplication().staticView_getMultiClickMaxY();
    }

    protected abstract void _enableInputMethodEvents(long var1, boolean var3);

    protected void _finishInputMethodComposition(long l2) {
    }

    protected abstract long _create(Map var1);

    protected View() {
        Application.checkEventThread();
        Application.GetApplication();
        this.ptr = this._create(Application.getDeviceDetails());
        if (this.ptr == 0L) {
            throw new RuntimeException("could not create platform view");
        }
    }

    private void checkNotClosed() {
        if (this.ptr == 0L) {
            throw new IllegalStateException("The view has already been closed");
        }
    }

    public boolean isClosed() {
        Application.checkEventThread();
        return this.ptr == 0L;
    }

    protected abstract long _getNativeView(long var1);

    public long getNativeView() {
        Application.checkEventThread();
        this.checkNotClosed();
        return this._getNativeView(this.ptr);
    }

    public int getNativeRemoteLayerId(String string) {
        Application.checkEventThread();
        throw new RuntimeException("This operation is not supported on this platform");
    }

    public Window getWindow() {
        Application.checkEventThread();
        return this.window;
    }

    protected abstract int _getX(long var1);

    public int getX() {
        Application.checkEventThread();
        this.checkNotClosed();
        return this._getX(this.ptr);
    }

    protected abstract int _getY(long var1);

    public int getY() {
        Application.checkEventThread();
        this.checkNotClosed();
        return this._getY(this.ptr);
    }

    public int getWidth() {
        Application.checkEventThread();
        return this.width;
    }

    public int getHeight() {
        Application.checkEventThread();
        return this.height;
    }

    protected abstract void _setParent(long var1, long var3);

    void setWindow(Window window) {
        Application.checkEventThread();
        this.checkNotClosed();
        this.window = window;
        this._setParent(this.ptr, window == null ? 0L : window.getNativeHandle());
        this.isValid = this.ptr != 0L && window != null;
    }

    void setVisible(boolean bl) {
        this.isVisible = bl;
    }

    protected abstract boolean _close(long var1);

    public void close() {
        Window window;
        Application.checkEventThread();
        if (this.ptr == 0L) {
            return;
        }
        if (this.isInFullscreen()) {
            this._exitFullscreen(this.ptr, false);
        }
        if ((window = this.getWindow()) != null) {
            window.setView(null);
        }
        this.isValid = false;
        this._close(this.ptr);
        this.ptr = 0L;
    }

    public EventHandler getEventHandler() {
        Application.checkEventThread();
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        this.eventHandler = eventHandler;
    }

    private void handleViewEvent(long l2, int n2) {
        if (this.eventHandler != null) {
            this.eventHandler.handleViewEvent(this, l2, n2);
        }
    }

    private void handleKeyEvent(long l2, int n2, int n3, char[] arrc, int n4) {
        if (this.eventHandler != null) {
            this.eventHandler.handleKeyEvent(this, l2, n2, n3, arrc, n4);
        }
    }

    private void handleMouseEvent(long l2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, boolean bl2) {
        if (this.eventHandler != null) {
            this.eventHandler.handleMouseEvent(this, l2, n2, n3, n4, n5, n6, n7, n8, bl, bl2);
        }
    }

    private void handleMenuEvent(int n2, int n3, int n4, int n5, boolean bl) {
        if (this.eventHandler != null) {
            this.eventHandler.handleMenuEvent(this, n2, n3, n4, n5, bl);
        }
    }

    public void handleBeginTouchEvent(View view, long l2, int n2, boolean bl, int n3) {
        if (this.eventHandler != null) {
            this.eventHandler.handleBeginTouchEvent(view, l2, n2, bl, n3);
        }
    }

    public void handleNextTouchEvent(View view, long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        if (this.eventHandler != null) {
            this.eventHandler.handleNextTouchEvent(view, l2, n2, l3, n3, n4, n5, n6);
        }
    }

    public void handleEndTouchEvent(View view, long l2) {
        if (this.eventHandler != null) {
            this.eventHandler.handleEndTouchEvent(view, l2);
        }
    }

    public void handleScrollGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, double d2, double d3, double d4, double d5, double d6, double d7) {
        if (this.eventHandler != null) {
            this.eventHandler.handleScrollGestureEvent(view, l2, n2, n3, bl, bl2, n4, n5, n6, n7, n8, d2, d3, d4, d5, d6, d7);
        }
    }

    public void handleZoomGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        if (this.eventHandler != null) {
            this.eventHandler.handleZoomGestureEvent(view, l2, n2, n3, bl, bl2, n4, n5, n6, n7, d2, d3, d4, d5);
        }
    }

    public void handleRotateGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3) {
        if (this.eventHandler != null) {
            this.eventHandler.handleRotateGestureEvent(view, l2, n2, n3, bl, bl2, n4, n5, n6, n7, d2, d3);
        }
    }

    public void handleSwipeGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, int n9) {
        if (this.eventHandler != null) {
            this.eventHandler.handleSwipeGestureEvent(view, l2, n2, n3, bl, bl2, n4, n5, n6, n7, n8, n9);
        }
    }

    private void handleInputMethodEvent(long l2, String string, int[] arrn, int[] arrn2, byte[] arrby, int n2, int n3) {
        if (this.eventHandler != null) {
            this.eventHandler.handleInputMethodEvent(l2, string, arrn, arrn2, arrby, n2, n3);
        }
    }

    public void enableInputMethodEvents(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._enableInputMethodEvents(this.ptr, bl);
    }

    public void finishInputMethodComposition() {
        Application.checkEventThread();
        this.checkNotClosed();
        this._finishInputMethodComposition(this.ptr);
    }

    private double[] getInputMethodCandidatePos(int n2) {
        if (this.eventHandler != null) {
            return this.eventHandler.getInputMethodCandidatePos(n2);
        }
        return null;
    }

    private void handleDragStart(int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        if (this.eventHandler != null) {
            this.eventHandler.handleDragStart(this, n2, n3, n4, n5, n6, clipboardAssistance);
        }
    }

    private void handleDragEnd(int n2) {
        if (this.eventHandler != null) {
            this.eventHandler.handleDragEnd(this, n2);
        }
    }

    private int handleDragEnter(int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        if (this.eventHandler != null) {
            return this.eventHandler.handleDragEnter(this, n2, n3, n4, n5, n6, clipboardAssistance);
        }
        return n6;
    }

    private int handleDragOver(int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        if (this.eventHandler != null) {
            return this.eventHandler.handleDragOver(this, n2, n3, n4, n5, n6, clipboardAssistance);
        }
        return n6;
    }

    private void handleDragLeave(ClipboardAssistance clipboardAssistance) {
        if (this.eventHandler != null) {
            this.eventHandler.handleDragLeave(this, clipboardAssistance);
        }
    }

    private int handleDragDrop(int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        if (this.eventHandler != null) {
            return this.eventHandler.handleDragDrop(this, n2, n3, n4, n5, n6, clipboardAssistance);
        }
        return 0;
    }

    protected abstract void _scheduleRepaint(long var1);

    public void scheduleRepaint() {
        Application.checkEventThread();
        this.checkNotClosed();
        this._scheduleRepaint(this.ptr);
    }

    protected abstract void _begin(long var1);

    public void lock() {
        this.checkNotClosed();
        this._begin(this.ptr);
    }

    protected abstract void _end(long var1);

    public void unlock() {
        this.checkNotClosed();
        this._end(this.ptr);
    }

    protected abstract int _getNativeFrameBuffer(long var1);

    public int getNativeFrameBuffer() {
        return this._getNativeFrameBuffer(this.ptr);
    }

    protected abstract void _uploadPixels(long var1, Pixels var3);

    public void uploadPixels(Pixels pixels) {
        Application.checkEventThread();
        this.checkNotClosed();
        this.lock();
        try {
            this._uploadPixels(this.ptr, pixels);
        }
        finally {
            this.unlock();
        }
    }

    protected abstract boolean _enterFullscreen(long var1, boolean var3, boolean var4, boolean var5);

    public boolean enterFullscreen(boolean bl, boolean bl2, boolean bl3) {
        Application.checkEventThread();
        this.checkNotClosed();
        return this._enterFullscreen(this.ptr, bl, bl2, bl3);
    }

    protected abstract void _exitFullscreen(long var1, boolean var3);

    public void exitFullscreen(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._exitFullscreen(this.ptr, bl);
    }

    public boolean isInFullscreen() {
        Application.checkEventThread();
        return this.inFullscreen;
    }

    public boolean toggleFullscreen(boolean bl, boolean bl2, boolean bl3) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (!this.inFullscreen) {
            this.enterFullscreen(bl, bl2, bl3);
        } else {
            this.exitFullscreen(bl);
        }
        this._scheduleRepaint(this.ptr);
        return this.inFullscreen;
    }

    protected void notifyView(int n2) {
        if (n2 == 421) {
            if (this.isValid) {
                this.handleViewEvent(System.nanoTime(), n2);
            }
        } else {
            boolean bl = false;
            switch (n2) {
                case 412: {
                    this.isValid = false;
                    bl = true;
                    break;
                }
                case 411: {
                    this.isValid = true;
                    bl = true;
                    break;
                }
                case 431: {
                    this.inFullscreen = true;
                    bl = true;
                    if (this.getWindow() == null) break;
                    this.getWindow().notifyFullscreen(true);
                    break;
                }
                case 432: {
                    this.inFullscreen = false;
                    bl = true;
                    if (this.getWindow() == null) break;
                    this.getWindow().notifyFullscreen(false);
                    break;
                }
                case 422: 
                case 423: {
                    break;
                }
                default: {
                    System.err.println("Unknown view event type: " + n2);
                    return;
                }
            }
            this.handleViewEvent(System.nanoTime(), n2);
            if (bl) {
                this.handleViewEvent(System.nanoTime(), 423);
            }
        }
    }

    protected void notifyResize(int n2, int n3) {
        if (this.width == n2 && this.height == n3) {
            return;
        }
        this.width = n2;
        this.height = n3;
        this.handleViewEvent(System.nanoTime(), 422);
    }

    protected void notifyRepaint(int n2, int n3, int n4, int n5) {
        this.notifyView(421);
    }

    protected void notifyMenu(int n2, int n3, int n4, int n5, boolean bl) {
        this.handleMenuEvent(n2, n3, n4, n5, bl);
    }

    protected void notifyMouse(int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, boolean bl2) {
        if (this.window != null && this.window.handleMouseEvent(n2, n3, n4, n5, n6, n7)) {
            return;
        }
        long l2 = System.nanoTime();
        if (n2 == 221) {
            View view;
            View view2 = view = lastClickedView == null ? null : (View)lastClickedView.get();
            if (view == this && lastClickedButton == n3 && l2 - lastClickedTime <= 1000000L * View.getMultiClickTime() && Math.abs(n4 - lastClickedX) <= View.getMultiClickMaxX() && Math.abs(n5 - lastClickedY) <= View.getMultiClickMaxY()) {
                ++clickCount;
            } else {
                clickCount = 1;
                lastClickedView = new WeakReference<View>(this);
                lastClickedButton = n3;
                lastClickedX = n4;
                lastClickedY = n5;
            }
            lastClickedTime = l2;
        }
        this.handleMouseEvent(l2, n2, n3, n4, n5, n6, n7, n8, bl, bl2);
        if (n2 == 223) {
            if (!dragProcessed) {
                this.notifyDragStart(n3, n4, n5, n6, n7);
                dragProcessed = true;
            }
        } else {
            dragProcessed = false;
        }
    }

    protected void notifyScroll(int n2, int n3, int n4, int n5, double d2, double d3, int n6, int n7, int n8, int n9, int n10, double d4, double d5) {
        if (this.eventHandler != null) {
            this.eventHandler.handleScrollEvent(this, System.nanoTime(), n2, n3, n4, n5, d2, d3, n6, n7, n8, n9, n10, d4, d5);
        }
    }

    protected void notifyKey(int n2, int n3, char[] arrc, int n4) {
        this.handleKeyEvent(System.nanoTime(), n2, n3, arrc, n4);
    }

    protected void notifyInputMethod(String string, int[] arrn, int[] arrn2, byte[] arrby, int n2, int n3, int n4) {
        this.handleInputMethodEvent(System.nanoTime(), string, arrn, arrn2, arrby, n2, n3);
    }

    protected double[] notifyInputMethodCandidatePosRequest(int n2) {
        double[] arrd = this.getInputMethodCandidatePos(n2);
        if (arrd == null) {
            arrd = new double[]{0.0, 0.0};
        }
        return arrd;
    }

    protected void notifyDragStart(int n2, int n3, int n4, int n5, int n6) {
        this.dropSourceAssistant = new ClipboardAssistance("DND"){

            @Override
            public void actionPerformed(int n2) {
                View.this.notifyDragEnd(n2);
            }
        };
        this.handleDragStart(n2, n3, n4, n5, n6, this.dropSourceAssistant);
        if (this.dropSourceAssistant != null) {
            this.dropSourceAssistant.close();
            this.dropSourceAssistant = null;
        }
    }

    protected void notifyDragEnd(int n2) {
        this.handleDragEnd(n2);
        if (this.dropSourceAssistant != null) {
            this.dropSourceAssistant.close();
            this.dropSourceAssistant = null;
        }
    }

    protected int notifyDragEnter(int n2, int n3, int n4, int n5, int n6) {
        this.dropTargetAssistant = new ClipboardAssistance("DND"){

            @Override
            public void flush() {
                throw new UnsupportedOperationException("Flush is forbidden from target!");
            }
        };
        return this.handleDragEnter(n2, n3, n4, n5, n6, this.dropTargetAssistant);
    }

    protected int notifyDragOver(int n2, int n3, int n4, int n5, int n6) {
        return this.handleDragOver(n2, n3, n4, n5, n6, this.dropTargetAssistant);
    }

    protected void notifyDragLeave() {
        this.handleDragLeave(this.dropTargetAssistant);
        this.dropTargetAssistant.close();
    }

    protected int notifyDragDrop(int n2, int n3, int n4, int n5, int n6) {
        int n7 = this.handleDragDrop(n2, n3, n4, n5, n6, this.dropTargetAssistant);
        this.dropTargetAssistant.close();
        return n7;
    }

    public void notifyBeginTouchEvent(int n2, boolean bl, int n3) {
        this.handleBeginTouchEvent(this, System.nanoTime(), n2, bl, n3);
    }

    public void notifyNextTouchEvent(int n2, long l2, int n3, int n4, int n5, int n6) {
        this.handleNextTouchEvent(this, System.nanoTime(), n2, l2, n3, n4, n5, n6);
    }

    public void notifyEndTouchEvent() {
        this.handleEndTouchEvent(this, System.nanoTime());
    }

    public void notifyScrollGestureEvent(int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, double d2, double d3, double d4, double d5, double d6, double d7) {
        this.handleScrollGestureEvent(this, System.nanoTime(), n2, n3, bl, bl2, n4, n5, n6, n7, n8, d2, d3, d4, d5, d6, d7);
    }

    public void notifyZoomGestureEvent(int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        this.handleZoomGestureEvent(this, System.nanoTime(), n2, n3, bl, bl2, n4, n5, n6, n7, d2, d3, d4, d5);
    }

    public void notifyRotateGestureEvent(int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3) {
        this.handleRotateGestureEvent(this, System.nanoTime(), n2, n3, bl, bl2, n4, n5, n6, n7, d2, d3);
    }

    public void notifySwipeGestureEvent(int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, int n9) {
        this.handleSwipeGestureEvent(this, System.nanoTime(), n2, n3, bl, bl2, n4, n5, n6, n7, n8, n9);
    }

    long getAccessible() {
        Accessible accessible;
        Application.checkEventThread();
        this.checkNotClosed();
        if (View.accessible && (accessible = this.eventHandler.getSceneAccessible()) != null) {
            accessible.setView(this);
            return accessible.getNativeAccessible();
        }
        return 0L;
    }

    static {
        dragProcessed = false;
    }

    public static final class Capability {
        public static final int k3dKeyValue = 0;
        public static final int kSyncKeyValue = 1;
        public static final int k3dProjectionKeyValue = 2;
        public static final int k3dProjectionAngleKeyValue = 3;
        public static final int k3dDepthKeyValue = 4;
        public static final int kHiDPIAwareKeyValue = 5;
        public static final Object k3dKey = 0;
        public static final Object kSyncKey = 1;
        public static final Object k3dProjectionKey = 2;
        public static final Object k3dProjectionAngleKey = 3;
        public static final Object k3dDepthKey = 4;
        public static final Object kHiDPIAwareKey = 5;
    }

    public static class EventHandler {
        public void handleViewEvent(View view, long l2, int n2) {
        }

        public void handleKeyEvent(View view, long l2, int n2, int n3, char[] arrc, int n4) {
        }

        public void handleMenuEvent(View view, int n2, int n3, int n4, int n5, boolean bl) {
        }

        public void handleMouseEvent(View view, long l2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, boolean bl2) {
        }

        public void handleScrollEvent(View view, long l2, int n2, int n3, int n4, int n5, double d2, double d3, int n6, int n7, int n8, int n9, int n10, double d4, double d5) {
        }

        public void handleInputMethodEvent(long l2, String string, int[] arrn, int[] arrn2, byte[] arrby, int n2, int n3) {
        }

        public double[] getInputMethodCandidatePos(int n2) {
            return null;
        }

        public void handleDragStart(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        }

        public void handleDragEnd(View view, int n2) {
        }

        public int handleDragEnter(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
            return n6;
        }

        public int handleDragOver(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
            return n6;
        }

        public void handleDragLeave(View view, ClipboardAssistance clipboardAssistance) {
        }

        public int handleDragDrop(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
            return 0;
        }

        public void handleBeginTouchEvent(View view, long l2, int n2, boolean bl, int n3) {
        }

        public void handleNextTouchEvent(View view, long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        }

        public void handleEndTouchEvent(View view, long l2) {
        }

        public void handleScrollGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, double d2, double d3, double d4, double d5, double d6, double d7) {
        }

        public void handleZoomGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        }

        public void handleRotateGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3) {
        }

        public void handleSwipeGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, int n9) {
        }

        public Accessible getSceneAccessible() {
            return null;
        }
    }
}

