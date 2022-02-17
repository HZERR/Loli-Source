/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.VisualMargin;

public class QuaquaColorPreviewPanel
extends JPanel
implements UIResource {
    private static final Color previewBorderColor = new Color(0x949494);
    private static final Color previewBackgroundColor = new Color(0xFFFFFF);

    public QuaquaColorPreviewPanel() {
        this.initComponents();
        this.setBorder(new VisualMargin(3, 0, 3, 0));
        this.setToolTipText("on");
    }

    @Override
    public void paintComponent(Graphics g2) {
        Insets insets = this.getInsets();
        int x2 = insets.left;
        int y2 = insets.top;
        int w2 = this.getWidth() - insets.left - insets.right;
        int h2 = this.getHeight() - insets.top - insets.bottom;
        g2.setColor(previewBackgroundColor);
        g2.fillRect(x2 + 1, y2 + 1, w2 - 2, h2 - 2);
        g2.setColor(previewBorderColor);
        g2.drawRect(x2, y2, w2 - 1, h2 - 1);
        g2.setColor(this.getForeground());
        g2.fillRect(x2 + 2, y2 + 2, w2 - 4, h2 - 4);
    }

    @Override
    public String getToolTipText(MouseEvent evt) {
        Color color = this.getForeground();
        return color == null ? null : color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(26, 26));
    }
}

