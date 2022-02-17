/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.WindowStage;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.stage.Stage;

class GlassAppletWindow
implements AppletWindow {
    private final Window glassWindow;
    private WeakReference<Stage> topStage;
    private String serverName;

    GlassAppletWindow(long l2, String string) {
        if (0L == l2) {
            if (string != null) {
                throw new RuntimeException("GlassAppletWindow constructor used incorrectly.");
            }
            this.glassWindow = Application.GetApplication().createWindow(null, 0);
        } else {
            this.serverName = string;
            this.glassWindow = Application.GetApplication().createWindow(l2);
        }
    }

    Window getGlassWindow() {
        return this.glassWindow;
    }

    @Override
    public void setBackgroundColor(int n2) {
        Application.invokeLater(() -> {
            float f2 = (float)(n2 >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(n2 >> 8 & 0xFF) / 255.0f;
            float f4 = (float)(n2 & 0xFF) / 255.0f;
            this.glassWindow.setBackground(f2, f3, f4);
        });
    }

    @Override
    public void setForegroundColor(int n2) {
    }

    @Override
    public void setVisible(boolean bl) {
        Application.invokeLater(() -> this.glassWindow.setVisible(bl));
    }

    @Override
    public void setSize(int n2, int n3) {
        Application.invokeLater(() -> this.glassWindow.setSize(n2, n3));
    }

    @Override
    public int getWidth() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getWidth()));
        return atomicInteger.get();
    }

    @Override
    public int getHeight() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getHeight()));
        return atomicInteger.get();
    }

    @Override
    public void setPosition(int n2, int n3) {
        Application.invokeLater(() -> this.glassWindow.setPosition(n2, n3));
    }

    @Override
    public int getPositionX() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getX()));
        return atomicInteger.get();
    }

    @Override
    public int getPositionY() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Application.invokeAndWait(() -> atomicInteger.set(this.glassWindow.getY()));
        return atomicInteger.get();
    }

    @Override
    public float getUIScale() {
        AtomicReference<Float> atomicReference = new AtomicReference<Float>(Float.valueOf(0.0f));
        Application.invokeAndWait(() -> atomicReference.set(Float.valueOf(this.glassWindow.getPlatformScale())));
        return atomicReference.get().floatValue();
    }

    void dispose() {
        QuantumToolkit.runWithRenderLock(() -> {
            this.glassWindow.close();
            return null;
        });
    }

    @Override
    public void setStageOnTop(Stage stage) {
        this.topStage = null != stage ? new WeakReference<Stage>(stage) : null;
    }

    @Override
    public int getRemoteLayerId() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        Application.invokeAndWait(() -> {
            View view = this.glassWindow.getView();
            if (view != null) {
                atomicInteger.set(view.getNativeRemoteLayerId(this.serverName));
            }
        });
        return atomicInteger.get();
    }

    @Override
    public void dispatchEvent(Map map) {
        Application.invokeAndWait(() -> this.glassWindow.dispatchNpapiEvent(map));
    }

    void assertStageOrder() {
        Window window;
        TKStage tKStage;
        Stage stage;
        if (null != this.topStage && null != (stage = (Stage)this.topStage.get()) && (tKStage = stage.impl_getPeer()) instanceof WindowStage && ((WindowStage)tKStage).isVisible() && null != (window = ((WindowStage)tKStage).getPlatformWindow())) {
            window.toFront();
        }
    }
}

