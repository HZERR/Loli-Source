/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.scroll;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;
import org.pushingpixels.substance.internal.painter.SimplisticSoftBorderPainter;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceScrollPaneBorder
implements Border,
UIResource {
    @Override
    public Insets getBorderInsets(Component c2) {
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        int ins = (int)borderStrokeWidth;
        return new Insets(ins, ins, ins, ins);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        boolean hasRowHeader;
        if (!(c2 instanceof JScrollPane)) {
            return;
        }
        JScrollPane scrollPane = (JScrollPane)c2;
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
        JViewport columnHeader = scrollPane.getColumnHeader();
        SimplisticSoftBorderPainter painter = new SimplisticSoftBorderPainter();
        SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.BORDER, c2.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2));
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2)) / 2.0);
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setStroke(new BasicStroke(borderStrokeWidth, 2, 0));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        x2 += borderDelta;
        y2 += borderDelta;
        width -= 2 * borderDelta;
        height -= 2 * borderDelta;
        boolean horizontalVisible = horizontal != null && horizontal.isVisible();
        boolean verticalVisible = vertical != null && vertical.isVisible();
        boolean bl = hasRowHeader = scrollPane.getRowHeader() != null;
        if (scrollPane.getComponentOrientation().isLeftToRight()) {
            g2d.setColor(((StandardBorderPainter)painter).getTopBorderColor(scheme));
            if (verticalVisible && columnHeader == null) {
                g2d.drawLine(x2, y2, x2 + width - vertical.getWidth(), y2);
            } else {
                g2d.drawLine(x2, y2, x2 + width, y2);
            }
            g2d.setColor(((StandardBorderPainter)painter).getTopBorderColor(scheme));
            if (horizontalVisible && !hasRowHeader) {
                g2d.drawLine(x2, y2, x2, y2 + height - horizontal.getHeight());
            } else {
                g2d.drawLine(x2, y2, x2, y2 + height);
            }
            g2d.setColor(((StandardBorderPainter)painter).getBottomBorderColor(scheme));
            if (horizontalVisible) {
                if (hasRowHeader) {
                    g2d.drawLine(x2, y2 + height - 1, x2 + scrollPane.getRowHeader().getSize().width, y2 + height - 1);
                }
            } else if (verticalVisible) {
                g2d.drawLine(x2, y2 + height - 1, x2 + width - vertical.getWidth(), y2 + height - 1);
            } else {
                g2d.drawLine(x2, y2 + height - 1, x2 + width, y2 + height - 1);
            }
            g2d.setColor(((StandardBorderPainter)painter).getBottomBorderColor(scheme));
            if (verticalVisible) {
                if (columnHeader != null) {
                    g2d.drawLine(x2 + width - 1, y2, x2 + width - 1, y2 + columnHeader.getHeight());
                }
            } else if (horizontalVisible) {
                g2d.drawLine(x2 + width - 1, y2, x2 + width - 1, y2 + height - horizontal.getHeight());
            } else {
                g2d.drawLine(x2 + width - 1, y2, x2 + width - 1, y2 + height);
            }
        } else {
            g2d.setColor(((StandardBorderPainter)painter).getTopBorderColor(scheme));
            if (verticalVisible && columnHeader == null) {
                g2d.drawLine(x2 + vertical.getWidth(), y2, x2 + width, y2);
            } else {
                g2d.drawLine(x2, y2, x2 + width, y2);
            }
            g2d.setColor(((StandardBorderPainter)painter).getBottomBorderColor(scheme));
            if (verticalVisible) {
                if (columnHeader != null) {
                    g2d.drawLine(x2, y2, x2, y2 + columnHeader.getHeight());
                }
            } else if (horizontalVisible) {
                g2d.drawLine(x2, y2, x2, y2 + height - horizontal.getHeight());
            } else {
                g2d.drawLine(x2, y2, x2, y2 + height - 1);
            }
            g2d.setColor(((StandardBorderPainter)painter).getBottomBorderColor(scheme));
            if (horizontalVisible) {
                if (hasRowHeader) {
                    g2d.drawLine(x2 + width - scrollPane.getRowHeader().getSize().width, y2 + height - 1, x2 + width - 1, y2 + height - 1);
                }
            } else if (verticalVisible) {
                g2d.drawLine(x2 + vertical.getWidth(), y2 + height - 1, x2 + width, y2 + height - 1);
            } else {
                g2d.drawLine(x2, y2 + height - 1, x2 + width, y2 + height - 1);
            }
            g2d.setColor(((StandardBorderPainter)painter).getTopBorderColor(scheme));
            if (horizontalVisible && !hasRowHeader) {
                g2d.drawLine(x2 + width - 1, y2, x2 + width - 1, y2 + height - horizontal.getHeight());
            } else {
                g2d.drawLine(x2 + width - 1, y2, x2 + width - 1, y2 + height);
            }
        }
        g2d.dispose();
    }
}

