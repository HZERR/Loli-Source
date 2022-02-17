/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

public class SmallColorWellBorder
implements Border {
    private static Color inner = Color.white;
    private static Color outer = new Color(0x949494);

    @Override
    public Insets getBorderInsets(Component c2) {
        return new Insets(1, 1, 1, 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c2, Graphics gr, int x2, int y2, int width, int height) {
        gr.setColor(c2.getBackground());
        gr.fillRect(x2 + 2, y2 + 2, width - 4, height - 4);
        gr.setColor(inner);
        gr.drawRect(x2 + 1, y2 + 1, width - 3, height - 3);
        gr.setColor(outer);
        gr.drawRect(x2, y2, width - 1, height - 1);
    }
}

