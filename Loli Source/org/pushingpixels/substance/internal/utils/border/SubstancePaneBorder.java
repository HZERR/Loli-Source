/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstancePaneBorder
extends AbstractBorder
implements UIResource {
    private static final int BORDER_THICKNESS = 4;
    private static final int BORDER_ROUNDNESS = 12;
    private static final Insets INSETS = new Insets(4, 4, 4, 4);

    public static DecorationAreaType getRootPaneType(JRootPane rootPane) {
        DecorationAreaType type = SubstanceLookAndFeel.getDecorationType(rootPane);
        if (type == null || type == DecorationAreaType.NONE) {
            type = SubstanceCoreUtilities.isPaintRootPaneActivated(rootPane) ? (SubstanceCoreUtilities.isSecondaryWindow(rootPane) ? DecorationAreaType.SECONDARY_TITLE_PANE : DecorationAreaType.PRIMARY_TITLE_PANE) : (SubstanceCoreUtilities.isSecondaryWindow(rootPane) ? DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE : DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE);
        } else if (type == DecorationAreaType.PRIMARY_TITLE_PANE) {
            if (!SubstanceCoreUtilities.isPaintRootPaneActivated(rootPane)) {
                type = DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE;
            }
        } else if (type == DecorationAreaType.SECONDARY_TITLE_PANE && !SubstanceCoreUtilities.isPaintRootPaneActivated(rootPane)) {
            type = DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE;
        }
        return type;
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int w2, int h2) {
        if (SubstanceCoreUtilities.isRoundedCorners(c2)) {
            this.paintRoundedBorder(c2, g2, x2, y2, w2, h2);
        } else {
            this.paintSquareBorder(c2, g2, x2, y2, w2, h2);
        }
    }

    public void paintSquareBorder(Component c2, Graphics g2, int x2, int y2, int w2, int h2) {
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        if (skin == null) {
            return;
        }
        SubstanceColorScheme scheme = skin.getBackgroundColorScheme(SubstancePaneBorder.getRootPaneType(SwingUtilities.getRootPane(c2)));
        JComponent titlePaneComp = SubstanceLookAndFeel.getTitlePaneComponent(SwingUtilities.windowForComponent(c2));
        SubstanceColorScheme borderScheme = skin.getColorScheme(titlePaneComp, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
        Graphics2D graphics = (Graphics2D)g2;
        graphics.setColor(borderScheme.getUltraDarkColor());
        graphics.drawLine(x2, y2 + h2 - 1, x2 + w2 - 1, y2 + h2 - 1);
        graphics.drawLine(x2 + w2 - 1, y2, x2 + w2 - 1, y2 + h2 - 1);
        graphics.setColor(borderScheme.getDarkColor());
        graphics.drawLine(x2, y2, x2 + w2 - 2, y2);
        graphics.drawLine(x2, y2, x2, y2 + h2 - 2);
        graphics.setColor(scheme.getMidColor());
        graphics.drawLine(x2 + 1, y2 + h2 - 2, x2 + w2 - 2, y2 + h2 - 2);
        graphics.drawLine(x2 + w2 - 2, y2 + 1, x2 + w2 - 2, y2 + h2 - 2);
        graphics.setColor(scheme.getMidColor());
        graphics.drawLine(x2 + 1, y2 + 1, x2 + w2 - 3, y2 + 1);
        graphics.drawLine(x2 + 1, y2 + 1, x2 + 1, y2 + h2 - 3);
        graphics.setColor(scheme.getLightColor());
        graphics.drawRect(x2 + 2, y2 + 2, w2 - 5, h2 - 5);
        graphics.drawRect(x2 + 3, y2 + 3, w2 - 7, h2 - 7);
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        return INSETS;
    }

    @Override
    public Insets getBorderInsets(Component c2, Insets newInsets) {
        newInsets.top = SubstancePaneBorder.INSETS.top;
        newInsets.left = SubstancePaneBorder.INSETS.left;
        newInsets.bottom = SubstancePaneBorder.INSETS.bottom;
        newInsets.right = SubstancePaneBorder.INSETS.right;
        return newInsets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public void paintRoundedBorder(Component c2, Graphics g2, int x2, int y2, int w2, int h2) {
        SubstanceColorScheme scheme = this.getColorScheme(c2);
        if (scheme == null) {
            return;
        }
        SubstanceColorScheme borderScheme = this.getBorderColorScheme(c2);
        Graphics2D graphics = (Graphics2D)g2;
        int xl = x2 + 4 + 2;
        int xr = x2 + w2 - 4 - 3;
        int yt = y2 + 4 + 2;
        int yb = y2 + h2 - 4 - 3;
        Object rh = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(borderScheme.getUltraDarkColor());
        graphics.drawLine(xl, y2 + h2 - 1, xr, y2 + h2 - 1);
        graphics.drawLine(x2 + w2 - 1, yt, x2 + w2 - 1, yb);
        graphics.fillOval(x2 + w2 - 12, y2 + h2 - 12, 12, 12);
        graphics.setColor(borderScheme.getDarkColor());
        graphics.drawLine(xl, y2, xr, y2);
        graphics.drawLine(x2, yt, x2, yb);
        graphics.fillOval(0, 0, 12, 12);
        graphics.fillOval(0, y2 + h2 - 12, 12, 12);
        graphics.fillOval(x2 + w2 - 12, 0, 12, 12);
        graphics.setColor(scheme.getMidColor());
        graphics.drawLine(xl, y2 + h2 - 2, xr, y2 + h2 - 2);
        graphics.drawLine(x2 + w2 - 2, yt, x2 + w2 - 2, yb);
        graphics.drawLine(xl, y2 + 1, xr, y2 + 1);
        graphics.drawLine(x2 + 1, yt, x2 + 1, yb);
        graphics.fillOval(1, 1, 12, 12);
        graphics.fillOval(1, y2 + h2 - 12 - 1, 12, 12);
        graphics.fillOval(x2 + w2 - 12 - 1, 1, 12, 12);
        graphics.fillOval(x2 + w2 - 12 - 1, y2 + h2 - 12 - 1, 12, 12);
        graphics.setColor(scheme.getLightColor());
        graphics.drawLine(xl, y2 + 2, xr, y2 + 2);
        graphics.drawLine(x2 + 2, yt, x2 + 2, yb);
        graphics.drawLine(xl, y2 + h2 - 3, xr, y2 + h2 - 3);
        graphics.drawLine(x2 + w2 - 3, yt, x2 + w2 - 3, yb);
        graphics.drawLine(xl, y2 + 3, xr, y2 + 3);
        graphics.drawLine(x2 + 3, yt, x2 + 3, yb);
        graphics.drawLine(xl, y2 + h2 - 4, xr, y2 + h2 - 4);
        graphics.drawLine(x2 + w2 - 4, yt, x2 + w2 - 4, yb);
        graphics.fillOval(2, 2, 12, 12);
        graphics.fillOval(2, y2 + h2 - 12 - 2, 12, 12);
        graphics.fillOval(x2 + w2 - 12 - 2, 2, 12, 12);
        graphics.fillOval(x2 + w2 - 12 - 2, y2 + h2 - 12 - 2, 12, 12);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, rh);
    }

    private SubstanceColorScheme getColorScheme(Component c2) {
        JRootPane rp = c2 instanceof JRootPane ? (JRootPane)c2 : SwingUtilities.getRootPane(c2);
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        if (skin == null) {
            return null;
        }
        DecorationAreaType type = SubstancePaneBorder.getRootPaneType(rp);
        return skin.getBackgroundColorScheme(type);
    }

    private SubstanceColorScheme getBorderColorScheme(Component c2) {
        JRootPane rp = c2 instanceof JRootPane ? (JRootPane)c2 : SwingUtilities.getRootPane(c2);
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(c2);
        if (skin == null) {
            return null;
        }
        JComponent titlePaneComp = SubstanceLookAndFeel.getTitlePaneComponent(SwingUtilities.windowForComponent(c2));
        return skin.getColorScheme(SubstancePaneBorder.getRootPaneType(rp), ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
    }
}

