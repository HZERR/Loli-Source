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
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.TransitionAware;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

@TransitionAware
public class ArrowButtonTransitionAwareIcon
implements Icon {
    private static LazyResettableHashMap<Icon> iconMap = new LazyResettableHashMap("ButtonArrowTransitionAwareIcon");
    private int orientation;
    protected int iconWidth;
    protected int iconHeight;
    protected TransitionAwareIcon.Delegate delegate;
    protected JComponent component;
    private TransitionAwareIcon.TransitionAwareUIDelegate transitionAwareUIDelegate;

    public ArrowButtonTransitionAwareIcon(final AbstractButton button, int orientation) {
        this(button, new TransitionAwareIcon.TransitionAwareUIDelegate(){

            @Override
            public TransitionAwareUI getTransitionAwareUI() {
                return (TransitionAwareUI)((Object)button.getUI());
            }
        }, orientation);
    }

    public ArrowButtonTransitionAwareIcon(final JComponent component, TransitionAwareIcon.TransitionAwareUIDelegate transitionAwareUIDelegate, final int orientation) {
        this.component = component;
        this.transitionAwareUIDelegate = transitionAwareUIDelegate;
        this.orientation = orientation;
        this.delegate = new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(component);
                return SubstanceImageCreator.getArrowIcon(fontSize, orientation, scheme);
            }
        };
        this.iconWidth = this.delegate.getColorSchemeIcon(SubstanceColorSchemeUtilities.getColorScheme(component, ComponentState.ENABLED)).getIconWidth();
        this.iconHeight = this.delegate.getColorSchemeIcon(SubstanceColorSchemeUtilities.getColorScheme(component, ComponentState.ENABLED)).getIconHeight();
    }

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        this.getIconToPaint().paintIcon(c2, g2, x2, y2);
    }

    private Icon getIconToPaint() {
        boolean isMenu = this.component instanceof JMenu;
        StateTransitionTracker stateTransitionTracker = this.transitionAwareUIDelegate.getTransitionAwareUI().getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = isMenu ? modelStateInfo.getStateNoSelectionContributionMap() : modelStateInfo.getStateContributionMap();
        ComponentState currState = isMenu ? modelStateInfo.getCurrModelStateNoSelection() : modelStateInfo.getCurrModelState();
        ColorSchemeAssociationKind baseAssociationKind = isMenu && currState.isFacetActive(ComponentStateFacet.ROLLOVER) ? ColorSchemeAssociationKind.HIGHLIGHT : ColorSchemeAssociationKind.MARK;
        SubstanceColorScheme baseScheme = SubstanceColorSchemeUtilities.getColorScheme(this.component, baseAssociationKind, currState);
        float baseAlpha = SubstanceColorSchemeUtilities.getAlpha(this.component, currState);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(this.component.getClass().getName(), this.orientation, SubstanceSizeUtils.getComponentFontSize(this.component), baseScheme.getDisplayName(), Float.valueOf(baseAlpha));
        Icon layerBase = iconMap.get(keyBase);
        if (layerBase == null) {
            Icon baseFullOpacity = this.delegate.getColorSchemeIcon(baseScheme);
            if (baseAlpha == 1.0f) {
                layerBase = baseFullOpacity;
                iconMap.put(keyBase, layerBase);
            } else {
                BufferedImage baseImage = SubstanceCoreUtilities.getBlankImage(baseFullOpacity.getIconWidth(), baseFullOpacity.getIconHeight());
                Graphics2D g2base = baseImage.createGraphics();
                g2base.setComposite(AlphaComposite.SrcOver.derive(baseAlpha));
                baseFullOpacity.paintIcon(this.component, g2base, 0, 0);
                g2base.dispose();
                layerBase = new ImageIcon(baseImage);
                iconMap.put(keyBase, layerBase);
            }
        }
        if (currState.isDisabled() || activeStates.size() == 1) {
            return layerBase;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(layerBase.getIconWidth(), layerBase.getIconHeight());
        Graphics2D g2d = result.createGraphics();
        layerBase.paintIcon(this.component, g2d, 0, 0);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float stateContribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
            ColorSchemeAssociationKind associationKind = isMenu && activeState.isFacetActive(ComponentStateFacet.ROLLOVER) ? ColorSchemeAssociationKind.HIGHLIGHT : ColorSchemeAssociationKind.MARK;
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.component, associationKind, activeState);
            float alpha = SubstanceColorSchemeUtilities.getAlpha(this.component, activeState);
            HashMapKey key = SubstanceCoreUtilities.getHashKey(this.component.getClass().getName(), this.orientation, SubstanceSizeUtils.getComponentFontSize(this.component), scheme.getDisplayName(), Float.valueOf(alpha));
            Icon layer = iconMap.get(key);
            if (layer == null) {
                Icon fullOpacity = this.delegate.getColorSchemeIcon(scheme);
                if (alpha == 1.0f) {
                    layer = fullOpacity;
                    iconMap.put(key, layer);
                } else {
                    BufferedImage image = SubstanceCoreUtilities.getBlankImage(fullOpacity.getIconWidth(), fullOpacity.getIconHeight());
                    Graphics2D g2layer = image.createGraphics();
                    g2layer.setComposite(AlphaComposite.SrcOver.derive(alpha));
                    fullOpacity.paintIcon(this.component, g2layer, 0, 0);
                    g2layer.dispose();
                    layer = new ImageIcon(image);
                    iconMap.put(key, layer);
                }
            }
            layer.paintIcon(this.component, g2d, 0, 0);
        }
        g2d.dispose();
        return new ImageIcon(result);
    }

    @Override
    public int getIconHeight() {
        return this.iconHeight;
    }

    @Override
    public int getIconWidth() {
        return this.iconWidth;
    }
}

