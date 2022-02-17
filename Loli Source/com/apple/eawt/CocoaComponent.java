/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import java.awt.Canvas;
import java.awt.Dimension;

public abstract class CocoaComponent
extends Canvas {
    public abstract int createNSView();

    public long createNSViewLong() {
        return 0L;
    }

    public abstract Dimension getMaximumSize();

    public abstract Dimension getMinimumSize();

    public abstract Dimension getPreferredSize();

    public final void sendMessage(int n2, Object object) {
    }
}

