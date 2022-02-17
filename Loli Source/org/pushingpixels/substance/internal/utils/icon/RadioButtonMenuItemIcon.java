/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.icon;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class RadioButtonMenuItemIcon
implements Icon,
UIResource {
    private int size;
    private JMenuItem menuItem;
    private static LazyResettableHashMap<Icon> iconMap = new LazyResettableHashMap("RadioButtonMenuItemIcon");

    public RadioButtonMenuItemIcon(JMenuItem menuItem, int size) {
        this.menuItem = menuItem;
        this.size = size;
    }

    private Icon getIconToPaint() {
        if (this.menuItem == null) {
            return null;
        }
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)this.menuItem.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        int fontSize = SubstanceSizeUtils.getComponentFontSize(this.menuItem);
        int checkMarkSize = this.size;
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(this.menuItem);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(this.menuItem);
        ComponentState currState = modelStateInfo.getCurrModelState();
        SubstanceColorScheme baseFillColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.menuItem, ColorSchemeAssociationKind.FILL, currState);
        SubstanceColorScheme baseMarkColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.menuItem, ColorSchemeAssociationKind.MARK, currState);
        SubstanceColorScheme baseBorderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.menuItem, ColorSchemeAssociationKind.BORDER, currState);
        float visibility = stateTransitionTracker.getFacetStrength(ComponentStateFacet.SELECTION);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(fontSize, checkMarkSize, fillPainter.getDisplayName(), borderPainter.getDisplayName(), baseFillColorScheme.getDisplayName(), baseMarkColorScheme.getDisplayName(), baseBorderColorScheme.getDisplayName(), Float.valueOf(visibility));
        Icon iconBase = iconMap.get(keyBase);
        if (iconBase == null) {
            iconBase = new ImageIcon(SubstanceImageCreator.getRadioButton(this.menuItem, fillPainter, borderPainter, checkMarkSize, currState, 0, baseFillColorScheme, baseMarkColorScheme, baseBorderColorScheme, visibility));
            iconMap.put(keyBase, iconBase);
        }
        if (currState.isDisabled() || activeStates.size() == 1) {
            return iconBase;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(iconBase.getIconWidth(), iconBase.getIconHeight());
        Graphics2D g2d = result.createGraphics();
        iconBase.paintIcon(this.menuItem, g2d, 0, 0);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float stateContribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
            SubstanceColorScheme fillColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.menuItem, ColorSchemeAssociationKind.FILL, activeState);
            SubstanceColorScheme markColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.menuItem, ColorSchemeAssociationKind.MARK, activeState);
            SubstanceColorScheme borderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.menuItem, ColorSchemeAssociationKind.BORDER, activeState);
            HashMapKey keyLayer = SubstanceCoreUtilities.getHashKey(fontSize, checkMarkSize, fillPainter.getDisplayName(), borderPainter.getDisplayName(), fillColorScheme.getDisplayName(), markColorScheme.getDisplayName(), borderColorScheme.getDisplayName(), Float.valueOf(visibility));
            Icon iconLayer = iconMap.get(keyLayer);
            if (iconLayer == null) {
                iconLayer = new ImageIcon(SubstanceImageCreator.getRadioButton(this.menuItem, fillPainter, borderPainter, checkMarkSize, currState, 0, fillColorScheme, markColorScheme, borderColorScheme, visibility));
                iconMap.put(keyLayer, iconLayer);
            }
            iconLayer.paintIcon(this.menuItem, g2d, 0, 0);
        }
        g2d.dispose();
        return new ImageIcon(result);
    }

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        Icon iconToDraw = this.getIconToPaint();
        if (iconToDraw != null) {
            iconToDraw.paintIcon(c2, g2, x2, y2);
        }
    }

    @Override
    public int getIconWidth() {
        return this.size + 2;
    }

    @Override
    public int getIconHeight() {
        return this.size + 2;
    }
}

