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
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.TransitionAware;

@TransitionAware
public class TransitionAwareIcon
implements Icon {
    private JComponent comp;
    private TransitionAwareUIDelegate transitionAwareUIDelegate;
    protected Delegate delegate;
    protected ColorSchemeAssociationKindDelegate colorSchemeAssociationKindDelegate;
    protected String uniqueIconTypeId;
    private static LazyResettableHashMap<Icon> iconMap = new LazyResettableHashMap("TransitionAwareIcon");
    private int iconWidth;
    private int iconHeight;

    public TransitionAwareIcon(final AbstractButton button, Delegate delegate, String uniqueIconTypeId) {
        this(button, button == null ? null : new TransitionAwareUIDelegate(){

            @Override
            public TransitionAwareUI getTransitionAwareUI() {
                return (TransitionAwareUI)((Object)button.getUI());
            }
        }, delegate, null, uniqueIconTypeId);
    }

    public TransitionAwareIcon(JComponent comp, TransitionAwareUIDelegate transitionAwareUIDelegate, Delegate delegate, ColorSchemeAssociationKindDelegate colorSchemeAssociationKindDelegate, String uniqueIconTypeId) {
        this.comp = comp;
        this.transitionAwareUIDelegate = transitionAwareUIDelegate;
        this.delegate = delegate;
        this.colorSchemeAssociationKindDelegate = colorSchemeAssociationKindDelegate;
        this.uniqueIconTypeId = uniqueIconTypeId;
        this.iconWidth = this.delegate.getColorSchemeIcon(SubstanceColorSchemeUtilities.getColorScheme(comp, ColorSchemeAssociationKind.MARK, ComponentState.ENABLED)).getIconWidth();
        this.iconHeight = this.delegate.getColorSchemeIcon(SubstanceColorSchemeUtilities.getColorScheme(comp, ColorSchemeAssociationKind.MARK, ComponentState.ENABLED)).getIconHeight();
    }

    private synchronized Icon getIconToPaint() {
        StateTransitionTracker stateTransitionTracker = this.transitionAwareUIDelegate.getTransitionAwareUI().getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        ComponentState currState = modelStateInfo.getCurrModelState();
        boolean buttonNeverPainted = SubstanceCoreUtilities.isButtonNeverPainted(this.comp);
        if (buttonNeverPainted && currState.isFacetActive(ComponentStateFacet.ENABLE)) {
            currState = ComponentState.ENABLED;
        }
        ColorSchemeAssociationKind baseAssociationKind = this.colorSchemeAssociationKindDelegate == null ? ColorSchemeAssociationKind.MARK : this.colorSchemeAssociationKindDelegate.getColorSchemeAssociationKind(currState);
        SubstanceColorScheme baseScheme = SubstanceColorSchemeUtilities.getColorScheme(this.comp, baseAssociationKind, currState);
        float baseAlpha = SubstanceColorSchemeUtilities.getAlpha(this.comp, currState);
        HashMapKey keyBase = SubstanceCoreUtilities.getHashKey(this.uniqueIconTypeId, SubstanceSizeUtils.getComponentFontSize(this.comp), baseScheme.getDisplayName(), Float.valueOf(baseAlpha));
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
                baseFullOpacity.paintIcon(this.comp, g2base, 0, 0);
                g2base.dispose();
                layerBase = new ImageIcon(baseImage);
                iconMap.put(keyBase, layerBase);
            }
        }
        if (currState.isDisabled() || activeStates.size() == 1 || buttonNeverPainted) {
            return layerBase;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(layerBase.getIconWidth(), layerBase.getIconHeight());
        Graphics2D g2d = result.createGraphics();
        layerBase.paintIcon(this.comp, g2d, 0, 0);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float stateContribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
            ColorSchemeAssociationKind associationKind = this.colorSchemeAssociationKindDelegate == null ? ColorSchemeAssociationKind.MARK : this.colorSchemeAssociationKindDelegate.getColorSchemeAssociationKind(activeState);
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.comp, associationKind, activeState);
            float alpha = SubstanceColorSchemeUtilities.getAlpha(this.comp, activeState);
            HashMapKey key = SubstanceCoreUtilities.getHashKey(this.uniqueIconTypeId, SubstanceSizeUtils.getComponentFontSize(this.comp), scheme.getDisplayName(), Float.valueOf(alpha));
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
                    fullOpacity.paintIcon(this.comp, g2layer, 0, 0);
                    g2layer.dispose();
                    layer = new ImageIcon(image);
                    iconMap.put(key, layer);
                }
            }
            layer.paintIcon(this.comp, g2d, 0, 0);
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

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        this.getIconToPaint().paintIcon(c2, g2, x2, y2);
    }

    public static interface TransitionAwareUIDelegate {
        public TransitionAwareUI getTransitionAwareUI();
    }

    public static interface ColorSchemeAssociationKindDelegate {
        public ColorSchemeAssociationKind getColorSchemeAssociationKind(ComponentState var1);
    }

    public static interface Delegate {
        public Icon getColorSchemeIcon(SubstanceColorScheme var1);
    }
}

