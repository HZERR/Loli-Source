/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.preview;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

public abstract class PreviewPainter {
    public void previewComponent(Container parent, Component component, int componentIndex, Graphics g2, int x2, int y2, int w2, int h2) {
    }

    public boolean hasPreview(Container parent, Component component, int componentIndex) {
        return false;
    }

    public boolean hasPreviewWindow(Container parent, Component component, int componentIndex) {
        return false;
    }

    public Dimension getPreviewWindowDimension(Container parent, Component component, int componentIndex) {
        return new Dimension(300, 200);
    }

    public int getPreviewWindowExtraDelay(Container parent, Component component, int componentIndex) {
        return 0;
    }

    public boolean toUpdatePeriodically(Container parent, Component component, int componentIndex) {
        return false;
    }

    public int getUpdateCycle(Container parent, Component component, int componentIndex) {
        return 10000;
    }
}

