/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.EmbeddedScene;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.List;
import javafx.application.Platform;

final class EmbeddedStage
extends GlassStage
implements EmbeddedStageInterface {
    private HostInterface host;

    public EmbeddedStage(HostInterface hostInterface) {
        this.host = hostInterface;
    }

    @Override
    public TKScene createTKScene(boolean bl, boolean bl2, AccessControlContext accessControlContext) {
        EmbeddedScene embeddedScene = new EmbeddedScene(this.host, bl, bl2);
        embeddedScene.setSecurityContext(accessControlContext);
        return embeddedScene;
    }

    @Override
    public void setScene(TKScene tKScene) {
        if (tKScene != null && !$assertionsDisabled && !(tKScene instanceof EmbeddedScene)) {
            throw new AssertionError();
        }
        super.setScene(tKScene);
    }

    @Override
    public void setBounds(float f2, float f3, boolean bl, boolean bl2, float f4, float f5, float f6, float f7, float f8, float f9) {
        float f10;
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setBounds: x=" + f2 + " y=" + f3 + " xSet=" + bl + " ySet=" + bl2 + " w=" + f4 + " h=" + " cw=" + f6 + " ch=" + f7);
        }
        float f11 = f4 > 0.0f ? f4 : f6;
        float f12 = f10 = f5 > 0.0f ? f5 : f7;
        if (f11 > 0.0f && f10 > 0.0f) {
            this.host.setPreferredSize((int)f11, (int)f10);
        }
    }

    @Override
    public float getUIScale() {
        return 1.0f;
    }

    @Override
    public float getRenderScale() {
        GlassScene glassScene = this.getScene();
        if (glassScene instanceof EmbeddedScene) {
            return ((EmbeddedScene)glassScene).getRenderScale();
        }
        return 1.0f;
    }

    @Override
    public void setMinimumSize(int n2, int n3) {
    }

    @Override
    public void setMaximumSize(int n2, int n3) {
    }

    @Override
    protected void setPlatformEnabled(boolean bl) {
        super.setPlatformEnabled(bl);
        this.host.setEnabled(bl);
    }

    @Override
    public void setIcons(List list) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setIcons");
        }
    }

    @Override
    public void setTitle(String string) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setTitle " + string);
        }
    }

    @Override
    public void setVisible(boolean bl) {
        this.host.setEmbeddedStage(bl ? this : null);
        super.setVisible(bl);
    }

    @Override
    public void setOpacity(float f2) {
    }

    @Override
    public void setIconified(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setIconified " + bl);
        }
    }

    @Override
    public void setMaximized(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setMaximized " + bl);
        }
    }

    @Override
    public void setAlwaysOnTop(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setAlwaysOnTop " + bl);
        }
    }

    @Override
    public void setResizable(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setResizable " + bl);
        }
    }

    @Override
    public void setFullScreen(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setFullScreen " + bl);
        }
    }

    @Override
    public void requestFocus() {
        if (!this.host.requestFocus()) {
            return;
        }
        super.requestFocus();
    }

    @Override
    public void toBack() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.toBack");
        }
    }

    @Override
    public void toFront() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.toFront");
        }
    }

    @Override
    public boolean grabFocus() {
        return this.host.grabFocus();
    }

    @Override
    public void ungrabFocus() {
        this.host.ungrabFocus();
    }

    private void notifyStageListener(Runnable runnable) {
        AccessControlContext accessControlContext = this.getAccessControlContext();
        AccessController.doPrivileged(() -> {
            runnable.run();
            return null;
        }, accessControlContext);
    }

    private void notifyStageListenerLater(Runnable runnable) {
        Platform.runLater(() -> this.notifyStageListener(runnable));
    }

    @Override
    public void setLocation(int n2, int n3) {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedLocation(n2, n3);
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void setSize(int n2, int n3) {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedSize(n2, n3);
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void setFocused(boolean bl, int n2) {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedFocused(bl, AbstractEvents.focusCauseToPeerFocusCause(n2));
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void focusUngrab() {
        Runnable runnable = () -> {
            if (this.stageListener != null) {
                this.stageListener.focusUngrab();
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.notifyStageListener(runnable);
        } else {
            this.notifyStageListenerLater(runnable);
        }
    }

    @Override
    public void requestInput(String string, int n2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void releaseInput() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRTL(boolean bl) {
    }
}

