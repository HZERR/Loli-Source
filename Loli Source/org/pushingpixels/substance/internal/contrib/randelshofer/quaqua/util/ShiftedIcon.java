/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.Icon;

public class ShiftedIcon
implements Icon {
    private Icon target;
    private Rectangle shift;

    public ShiftedIcon(Icon target, Point shift) {
        this.target = target;
        this.shift = new Rectangle(shift.x, shift.y, target.getIconWidth(), target.getIconHeight());
    }

    public ShiftedIcon(Icon target, Rectangle shiftAndSize) {
        this.target = target;
        this.shift = shiftAndSize;
    }

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        this.target.paintIcon(c2, g2, x2 + this.shift.x, y2 + this.shift.y);
    }

    @Override
    public int getIconWidth() {
        return this.shift.width;
    }

    @Override
    public int getIconHeight() {
        return this.shift.height;
    }
}

