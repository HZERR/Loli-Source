/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.jgoodies.looks.common;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;

final class ShadowPopupBorder
extends AbstractBorder {
    private static final int SHADOW_SIZE = 5;
    private static ShadowPopupBorder instance = new ShadowPopupBorder();
    private static Image shadow = new ImageIcon(ShadowPopupBorder.class.getResource("shadow.png")).getImage();

    ShadowPopupBorder() {
    }

    public static ShadowPopupBorder getInstance() {
        return instance;
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        Image vShadowBg;
        JComponent popup = (JComponent)c2;
        Image hShadowBg = (Image)popup.getClientProperty("jgoodies.hShadowBg");
        if (hShadowBg != null) {
            g2.drawImage(hShadowBg, x2, y2 + height - 5, c2);
        }
        if ((vShadowBg = (Image)popup.getClientProperty("jgoodies.vShadowBg")) != null) {
            g2.drawImage(vShadowBg, x2 + width - 5, y2, c2);
        }
        g2.drawImage(shadow, x2 + 5, y2 + height - 5, x2 + 10, y2 + height, 0, 6, 5, 11, null, c2);
        g2.drawImage(shadow, x2 + 10, y2 + height - 5, x2 + width - 5, y2 + height, 5, 6, 6, 11, null, c2);
        g2.drawImage(shadow, x2 + width - 5, y2 + 5, x2 + width, y2 + 10, 6, 0, 11, 5, null, c2);
        g2.drawImage(shadow, x2 + width - 5, y2 + 10, x2 + width, y2 + height - 5, 6, 5, 11, 6, null, c2);
        g2.drawImage(shadow, x2 + width - 5, y2 + height - 5, x2 + width, y2 + height, 6, 6, 11, 11, null, c2);
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        return new Insets(0, 0, 5, 5);
    }

    @Override
    public Insets getBorderInsets(Component c2, Insets insets) {
        insets.top = 0;
        insets.left = 0;
        insets.bottom = 5;
        insets.right = 5;
        return insets;
    }
}

