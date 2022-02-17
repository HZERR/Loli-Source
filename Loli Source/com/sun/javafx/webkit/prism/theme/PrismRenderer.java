/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism.theme;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.prism.Graphics;
import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.Scene;
import javafx.scene.control.Control;

public final class PrismRenderer
extends Renderer {
    @Override
    protected void render(Control control, WCGraphicsContext wCGraphicsContext) {
        Scene.impl_setAllowPGAccess(true);
        Object p2 = control.impl_getPeer();
        Scene.impl_setAllowPGAccess(false);
        ((NGNode)p2).render((Graphics)wCGraphicsContext.getPlatformGraphics());
    }
}

