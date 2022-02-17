/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Screen {
    private static volatile List<Screen> screens = null;
    private static final int dpiOverride = AccessController.doPrivileged(() -> Integer.getInteger("com.sun.javafx.screenDPI", 0));
    private static EventHandler eventHandler;
    private volatile long ptr;
    private volatile int adapter;
    private final int depth;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int visibleX;
    private final int visibleY;
    private final int visibleWidth;
    private final int visibleHeight;
    private final int resolutionX;
    private final int resolutionY;
    private final float uiScale;
    private final float renderScale;

    public static double getVideoRefreshPeriod() {
        Application.checkEventThread();
        return Application.GetApplication().staticScreen_getVideoRefreshPeriod();
    }

    public static Screen getMainScreen() {
        return Screen.getScreens().get(0);
    }

    public static List<Screen> getScreens() {
        if (screens == null) {
            throw new RuntimeException("Internal graphics not initialized yet");
        }
        return screens;
    }

    protected Screen(long l2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, float f2) {
        this(l2, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, 1.0f, f2);
    }

    protected Screen(long l2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, float f2, float f3) {
        this.ptr = l2;
        this.depth = n2;
        this.x = n3;
        this.y = n4;
        this.width = n5;
        this.height = n6;
        this.visibleX = n7;
        this.visibleY = n8;
        this.visibleWidth = n9;
        this.visibleHeight = n10;
        if (dpiOverride > 0) {
            this.resolutionX = this.resolutionY = dpiOverride;
        } else {
            this.resolutionX = n11;
            this.resolutionY = n12;
        }
        this.uiScale = f2;
        this.renderScale = f3;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getVisibleX() {
        return this.visibleX;
    }

    public int getVisibleY() {
        return this.visibleY;
    }

    public int getVisibleWidth() {
        return this.visibleWidth;
    }

    public int getVisibleHeight() {
        return this.visibleHeight;
    }

    public int getResolutionX() {
        return this.resolutionX;
    }

    public int getResolutionY() {
        return this.resolutionY;
    }

    public float getUIScale() {
        return this.uiScale;
    }

    public float getRenderScale() {
        return this.renderScale;
    }

    public long getNativeScreen() {
        return this.ptr;
    }

    private void dispose() {
        this.ptr = 0L;
    }

    public int getAdapterOrdinal() {
        return this.adapter;
    }

    public void setAdapterOrdinal(int n2) {
        this.adapter = n2;
    }

    public static void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        Screen.eventHandler = eventHandler;
    }

    public static void notifySettingsChanged() {
        List<Screen> list = screens;
        Screen.initScreens();
        if (eventHandler != null) {
            eventHandler.handleSettingsChanged();
        }
        List<Window> list2 = Window.getWindows();
        block0: for (Window object : list2) {
            Screen screen = object.getScreen();
            for (Screen screen2 : screens) {
                if (screen.getNativeScreen() != screen2.getNativeScreen()) continue;
                object.setScreen(screen2);
                continue block0;
            }
        }
        if (list != null) {
            for (Screen screen : list) {
                screen.dispose();
            }
        }
    }

    static void initScreens() {
        Application.checkEventThread();
        Screen[] arrscreen = Application.GetApplication().staticScreen_getScreens();
        if (arrscreen == null) {
            throw new RuntimeException("Internal graphics failed to initialize");
        }
        screens = Collections.unmodifiableList(Arrays.asList(arrscreen));
    }

    public String toString() {
        return "Screen:\n    ptr:" + this.getNativeScreen() + "\n" + "    adapter:" + this.getAdapterOrdinal() + "\n" + "    depth:" + this.getDepth() + "\n" + "    x:" + this.getX() + "\n" + "    y:" + this.getY() + "\n" + "    width:" + this.getWidth() + "\n" + "    height:" + this.getHeight() + "\n" + "    visibleX:" + this.getVisibleX() + "\n" + "    visibleY:" + this.getVisibleY() + "\n" + "    visibleWidth:" + this.getVisibleWidth() + "\n" + "    visibleHeight:" + this.getVisibleHeight() + "\n" + "    uiScale:" + this.getUIScale() + "\n" + "    RenderScale:" + this.getRenderScale() + "\n" + "    resolutionX:" + this.getResolutionX() + "\n" + "    resolutionY:" + this.getResolutionY() + "\n";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Screen screen = (Screen)object;
        return this.ptr == screen.ptr && this.adapter == screen.adapter && this.depth == screen.depth && this.x == screen.x && this.y == screen.y && this.width == screen.width && this.height == screen.height && this.visibleX == screen.visibleX && this.visibleY == screen.visibleY && this.visibleWidth == screen.visibleWidth && this.visibleHeight == screen.visibleHeight && this.resolutionX == screen.resolutionX && this.resolutionY == screen.resolutionY && Float.compare(screen.uiScale, this.uiScale) == 0 && Float.compare(screen.renderScale, this.renderScale) == 0;
    }

    public int hashCode() {
        int n2 = 17;
        n2 = 31 * n2 + (int)(this.ptr ^ this.ptr >>> 32);
        n2 = 31 * n2 + this.adapter;
        n2 = 31 * n2 + this.depth;
        n2 = 31 * n2 + this.x;
        n2 = 31 * n2 + this.y;
        n2 = 31 * n2 + this.width;
        n2 = 31 * n2 + this.height;
        n2 = 31 * n2 + this.visibleX;
        n2 = 31 * n2 + this.visibleY;
        n2 = 31 * n2 + this.visibleWidth;
        n2 = 31 * n2 + this.visibleHeight;
        n2 = 31 * n2 + this.resolutionX;
        n2 = 31 * n2 + this.resolutionY;
        n2 = 31 * n2 + (this.uiScale != 0.0f ? Float.floatToIntBits(this.uiScale) : 0);
        n2 = 31 * n2 + (this.renderScale != 0.0f ? Float.floatToIntBits(this.renderScale) : 0);
        return n2;
    }

    public static class EventHandler {
        public void handleSettingsChanged() {
        }
    }
}

