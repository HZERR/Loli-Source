/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.uitoolkit.SynthesizedEventListener
 *  com.sun.deploy.uitoolkit.Window
 *  com.sun.deploy.uitoolkit.Window$DisposeListener
 *  com.sun.deploy.uitoolkit.Window$WindowSize
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.deploy.uitoolkit.SynthesizedEventListener;
import com.sun.deploy.uitoolkit.Window;
import com.sun.deploy.uitoolkit.impl.fx.AppletStageManager;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.Toolkit;
import java.util.Map;
import javafx.stage.Stage;

public class FXWindow
extends Window
implements AppletStageManager,
SynthesizedEventListener {
    private AppletWindow appletWindow;
    private int width = 0;
    private int height = 0;
    private Stage appletStage;
    private Stage preloaderStage;
    private Stage errorStage;

    FXWindow(long l2, String string) {
        this.appletWindow = Toolkit.getToolkit().createAppletWindow(l2, string);
        this.width = this.appletWindow.getWidth();
        this.height = this.appletWindow.getHeight();
    }

    public Object getWindowObject() {
        return this.appletWindow;
    }

    public void synthesizeEvent(Map map) {
        this.appletWindow.dispatchEvent(map);
    }

    public void requestFocus() {
    }

    public int getWindowLayerID() {
        return this.appletWindow.getRemoteLayerId();
    }

    public void setBackground(int n2) {
        this.appletWindow.setBackgroundColor(n2);
    }

    public void setForeground(int n2) {
        this.appletWindow.setForegroundColor(n2);
    }

    public void setVisible(boolean bl) {
        this.appletWindow.setVisible(bl);
    }

    @Override
    public void setSize(int n2, int n3) {
        this.width = n2;
        this.height = n3;
        this.appletWindow.setSize(n2, n3);
        float f2 = this.appletWindow.getUIScale();
        if (this.appletStage != null) {
            this.appletStage.setWidth((float)n2 / f2);
            this.appletStage.setHeight((float)n3 / f2);
        }
        if (this.preloaderStage != null) {
            this.preloaderStage.setWidth((float)n2 / f2);
            this.preloaderStage.setHeight((float)n3 / f2);
        }
        if (this.errorStage != null) {
            this.errorStage.setWidth((float)n2 / f2);
            this.errorStage.setHeight((float)n3 / f2);
        }
    }

    public Window.WindowSize getSize() {
        this.width = this.appletWindow.getWidth();
        this.height = this.appletWindow.getHeight();
        return new Window.WindowSize((Window)this, this.width, this.height);
    }

    public void dispose() {
        if (null != this.appletStage) {
            this.appletStage.close();
            this.appletStage = null;
        }
        if (null != this.preloaderStage) {
            this.preloaderStage.close();
            this.preloaderStage = null;
        }
        if (null != this.errorStage) {
            this.errorStage.close();
            this.errorStage = null;
        }
        this.appletWindow = null;
        Toolkit.getToolkit().closeAppletWindow();
    }

    public void dispose(Window.DisposeListener disposeListener, long l2) {
        this.dispose();
    }

    public void setPosition(int n2, int n3) {
        this.appletWindow.setPosition(n2, n3);
    }

    @Override
    public synchronized Stage getAppletStage() {
        if (this.appletStage == null) {
            this.appletStage = this.createNewStage();
        }
        return this.appletStage;
    }

    @Override
    public Stage getPreloaderStage() {
        if (this.preloaderStage == null) {
            this.preloaderStage = this.createNewStage();
            if (null == this.errorStage) {
                this.appletWindow.setStageOnTop(this.preloaderStage);
            }
        }
        return this.preloaderStage;
    }

    @Override
    public Stage getErrorStage() {
        if (this.errorStage == null) {
            this.errorStage = this.createNewStage();
            this.appletWindow.setStageOnTop(this.errorStage);
        }
        return this.errorStage;
    }

    private Stage createNewStage() {
        Stage stage = new Stage();
        stage.impl_setPrimary(true);
        float f2 = this.appletWindow.getUIScale();
        stage.setWidth((float)this.width / f2);
        stage.setHeight((float)this.height / f2);
        stage.setX((float)this.appletWindow.getPositionX() / f2);
        stage.setY((float)this.appletWindow.getPositionY() / f2);
        stage.setResizable(false);
        return stage;
    }
}

