/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities;

public class LockBorder
implements Border,
UIResource {
    private Border originalBorder;

    public LockBorder(Border originalBorder) {
        this.originalBorder = originalBorder != null ? originalBorder : new EmptyBorder(0, 0, 0, 0);
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
        Icon lockIcon = lafSupport.getLockIcon(c2);
        if (lockIcon == null) {
            lockIcon = LafWidgetUtilities.getSmallLockIcon();
        }
        Insets origInsets = this.originalBorder.getBorderInsets(c2);
        if (c2.getComponentOrientation().isLeftToRight()) {
            return new Insets(origInsets.top, Math.max(origInsets.left, lockIcon.getIconWidth() + 2), origInsets.bottom, origInsets.right);
        }
        return new Insets(origInsets.top, origInsets.left, origInsets.bottom, Math.max(origInsets.right, lockIcon.getIconWidth() + 2));
    }

    @Override
    public boolean isBorderOpaque() {
        return this.originalBorder.isBorderOpaque();
    }

    public Border getOriginalBorder() {
        return this.originalBorder;
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        this.originalBorder.paintBorder(c2, g2, x2, y2, width, height);
        LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
        Icon lockIcon = lafSupport.getLockIcon(c2);
        if (lockIcon == null) {
            lockIcon = LafWidgetUtilities.getSmallLockIcon();
        }
        int offsetY = 0;
        if (c2.getParent() instanceof JViewport) {
            JViewport viewport = (JViewport)c2.getParent();
            if (viewport.getScrollMode() != 0) {
                viewport.setScrollMode(0);
            }
            Rectangle viewRect = viewport.getViewRect();
            offsetY = c2.getHeight() - viewRect.y - viewRect.height;
        }
        if (c2.getComponentOrientation().isLeftToRight()) {
            lockIcon.paintIcon(c2, g2, x2, y2 + height - lockIcon.getIconHeight() - offsetY);
        } else {
            lockIcon.paintIcon(c2, g2, x2 + width - lockIcon.getIconWidth(), y2 + height - lockIcon.getIconHeight() - offsetY);
        }
    }
}

