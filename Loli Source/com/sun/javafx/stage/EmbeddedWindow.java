/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.stage;

import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Scene;
import javafx.stage.Window;

public class EmbeddedWindow
extends Window {
    private HostInterface host;

    public EmbeddedWindow(HostInterface hostInterface) {
        this.host = hostInterface;
    }

    @Override
    public final void setScene(Scene scene) {
        super.setScene(scene);
    }

    @Override
    public final void show() {
        super.show();
    }

    @Override
    protected void impl_visibleChanging(boolean bl) {
        super.impl_visibleChanging(bl);
        Toolkit toolkit = Toolkit.getToolkit();
        if (bl && this.impl_peer == null) {
            this.impl_peer = toolkit.createTKEmbeddedStage(this.host, WindowHelper.getAccessControlContext(this));
            this.peerListener = new WindowPeerListener(this);
        }
    }
}

