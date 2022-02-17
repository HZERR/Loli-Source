/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.icon;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.utils.icon.ArrowButtonTransitionAwareIcon;

public class MenuArrowIcon
implements Icon,
UIResource {
    private Icon ltrIcon;
    private Icon rtlIcon;

    public MenuArrowIcon(JMenu menu) {
        this.ltrIcon = new ArrowButtonTransitionAwareIcon(menu, 3);
        this.rtlIcon = new ArrowButtonTransitionAwareIcon(menu, 7);
    }

    @Override
    public int getIconHeight() {
        return this.ltrIcon.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        return this.ltrIcon.getIconWidth();
    }

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        if (c2.getComponentOrientation().isLeftToRight()) {
            this.ltrIcon.paintIcon(c2, g2, x2, y2);
        } else {
            this.rtlIcon.paintIcon(c2, g2, x2, y2);
        }
    }
}

