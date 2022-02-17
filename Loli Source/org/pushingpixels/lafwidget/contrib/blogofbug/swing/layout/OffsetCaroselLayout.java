/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.RichComponent;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.layout.CaroselLayout;

public class OffsetCaroselLayout
extends CaroselLayout {
    public OffsetCaroselLayout(Container forContainer) {
        super(forContainer);
    }

    @Override
    protected boolean shouldHide(Component comp, double angle, double scale) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        if (sin > -0.5 && sin < 1.0 && cos < 0.3) {
            if (scale > 1.0) {
                scale = Math.abs(scale - 1.0);
                scale *= 3.0;
                scale = 1.0 - scale;
            } else {
                scale = 1.0;
            }
            if (comp instanceof RichComponent) {
                ((RichComponent)((Object)comp)).setAlpha((float)scale);
            }
            return false;
        }
        return true;
    }

    @Override
    protected Dimension getCarouselRadius(Container target, Insets insets, int width, int height, int widestComponent) {
        width = target.getSize().width - (insets.left + insets.right + widestComponent / 2);
        height = (int)((double)height / 2.5);
        return new Dimension(width, height);
    }

    @Override
    protected Point calculateCenter(Insets insets, int width, int height, int widest) {
        return new Point(0, (height - insets.bottom) / 2 + widest / 3);
    }

    @Override
    public void setFrontMostComponent(Component component) {
        this.setTarget(-this.getPosition(component).getTargetAngle() + 1.5707963267948966);
    }
}

