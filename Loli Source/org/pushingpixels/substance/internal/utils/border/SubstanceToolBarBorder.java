/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceToolBarBorder
extends AbstractBorder
implements UIResource {
    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int w2, int h2) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2;
        graphics.translate(x2, y2);
        if (((JToolBar)c2).isFloatable()) {
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.SEPARATOR, ComponentState.ENABLED);
            int dragBumpsWidth = (int)(0.75 * (double)SubstanceSizeUtils.getToolBarDragInset(SubstanceSizeUtils.getComponentFontSize(c2)));
            if (((JToolBar)c2).getOrientation() == 0) {
                int height = c2.getHeight() - 4;
                if (height > 0) {
                    if (c2.getComponentOrientation().isLeftToRight()) {
                        graphics.drawImage((Image)SubstanceImageCreator.getDragImage(c2, scheme, dragBumpsWidth, height, 2), 2, 1, null);
                    } else {
                        graphics.drawImage((Image)SubstanceImageCreator.getDragImage(c2, scheme, dragBumpsWidth, height, 2), c2.getBounds().width - dragBumpsWidth - 2, 1, null);
                    }
                }
            } else {
                int width = c2.getWidth() - 4;
                if (width > 0) {
                    graphics.drawImage((Image)SubstanceImageCreator.getDragImage(c2, scheme, width, dragBumpsWidth, 2), 2, 2, null);
                }
            }
        }
        graphics.translate(-x2, -y2);
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        return this.getBorderInsets(c2, new Insets(0, 0, 0, 0));
    }

    @Override
    public Insets getBorderInsets(Component c2, Insets newInsets) {
        Insets margin;
        Insets defaultInsets = SubstanceSizeUtils.getToolBarInsets(SubstanceSizeUtils.getComponentFontSize(c2));
        newInsets.set(defaultInsets.top, defaultInsets.left, defaultInsets.bottom, defaultInsets.right);
        JToolBar toolbar = (JToolBar)c2;
        if (toolbar.isFloatable()) {
            int dragInset = SubstanceSizeUtils.getToolBarDragInset(SubstanceSizeUtils.getComponentFontSize(c2));
            if (toolbar.getOrientation() == 0) {
                if (toolbar.getComponentOrientation().isLeftToRight()) {
                    newInsets.left = dragInset;
                } else {
                    newInsets.right = dragInset;
                }
            } else {
                newInsets.top = dragInset;
            }
        }
        if ((margin = toolbar.getMargin()) != null) {
            newInsets.left += margin.left;
            newInsets.top += margin.top;
            newInsets.right += margin.right;
            newInsets.bottom += margin.bottom;
        }
        return newInsets;
    }
}

