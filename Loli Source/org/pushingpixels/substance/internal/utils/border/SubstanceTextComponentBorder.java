/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.border;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceTextComponentBorder
implements Border,
UIResource {
    protected Insets myInsets;
    private static LazyResettableHashMap<BufferedImage> smallImageCache = new LazyResettableHashMap("SubstanceTextComponentBorder");

    public SubstanceTextComponentBorder(Insets insets) {
        this.myInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    private void paintBorder(JComponent c2, Graphics g2, int x2, int y2, int width, int height, boolean isEnabled, boolean hasFocus) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        float radius = 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(c2));
        JTextComponent componentForTransitions = SubstanceCoreUtilities.getTextComponentForTransitions(c2);
        if (componentForTransitions != null) {
            TextUI ui = componentForTransitions.getUI();
            if (ui instanceof TransitionAwareUI) {
                TransitionAwareUI trackable = (TransitionAwareUI)((Object)ui);
                StateTransitionTracker stateTransitionTracker = trackable.getTransitionTracker();
                StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                ComponentState currState = modelStateInfo.getCurrModelState();
                if (currState.isDisabled()) {
                    currState = ComponentState.DISABLED_SELECTED;
                }
                if (width * height < 100000) {
                    SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(componentForTransitions, ColorSchemeAssociationKind.BORDER, currState);
                    HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(SubstanceSizeUtils.getComponentFontSize(c2), width, height, Float.valueOf(radius), baseBorderScheme.getDisplayName());
                    BufferedImage baseLayer = smallImageCache.get(baseKey);
                    float baseAlpha = SubstanceColorSchemeUtilities.getAlpha(c2, currState);
                    if (baseLayer == null) {
                        baseLayer = SubstanceCoreUtilities.getBlankImage(width, height);
                        Graphics2D g2d = baseLayer.createGraphics();
                        SubstanceImageCreator.paintTextComponentBorder(c2, g2d, 0, 0, width, height, radius, baseBorderScheme);
                        g2d.dispose();
                        smallImageCache.put(baseKey, baseLayer);
                    }
                    graphics.setComposite(AlphaComposite.SrcOver.derive(baseAlpha));
                    graphics.drawImage((Image)baseLayer, x2, y2, null);
                    if (!currState.isDisabled() && activeStates.size() > 1) {
                        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                            float contribution;
                            ComponentState activeState = activeEntry.getKey();
                            if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(componentForTransitions, ColorSchemeAssociationKind.BORDER, activeState);
                            HashMapKey key = SubstanceCoreUtilities.getHashKey(SubstanceSizeUtils.getComponentFontSize(c2), width, height, Float.valueOf(radius), borderScheme.getDisplayName());
                            BufferedImage layer = smallImageCache.get(key);
                            float alpha = SubstanceColorSchemeUtilities.getAlpha(c2, activeState);
                            if (layer == null) {
                                layer = SubstanceCoreUtilities.getBlankImage(width, height);
                                Graphics2D g2d = layer.createGraphics();
                                SubstanceImageCreator.paintTextComponentBorder(c2, g2d, 0, 0, width, height, radius, borderScheme);
                                g2d.dispose();
                                smallImageCache.put(key, layer);
                            }
                            graphics.setComposite(AlphaComposite.SrcOver.derive(alpha * contribution));
                            graphics.drawImage((Image)layer, x2, y2, null);
                        }
                    }
                } else {
                    graphics.translate(x2, y2);
                    SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(componentForTransitions, ColorSchemeAssociationKind.BORDER, currState);
                    float baseAlpha = SubstanceColorSchemeUtilities.getAlpha(c2, currState);
                    graphics.setComposite(AlphaComposite.SrcOver.derive(baseAlpha));
                    SubstanceImageCreator.paintSimpleBorder(c2, graphics, width, height, baseBorderScheme);
                    if (!currState.isDisabled() && activeStates.size() > 1) {
                        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                            float contribution;
                            ComponentState activeState = activeEntry.getKey();
                            if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(componentForTransitions, ColorSchemeAssociationKind.BORDER, activeState);
                            float alpha = SubstanceColorSchemeUtilities.getAlpha(c2, activeState);
                            graphics.setComposite(AlphaComposite.SrcOver.derive(alpha * contribution));
                            SubstanceImageCreator.paintSimpleBorder(c2, graphics, width, height, borderScheme);
                        }
                    }
                }
            }
        } else {
            ComponentState currState = isEnabled ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
            SubstanceColorScheme borderColorScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.BORDER, currState);
            if (width * height < 100000) {
                HashMapKey hashKey = SubstanceCoreUtilities.getHashKey(SubstanceSizeUtils.getComponentFontSize(c2), width, height, Float.valueOf(radius), borderColorScheme.getDisplayName());
                BufferedImage result = smallImageCache.get(hashKey);
                if (result == null) {
                    result = SubstanceCoreUtilities.getBlankImage(width, height);
                    Graphics2D g2d = result.createGraphics();
                    SubstanceImageCreator.paintTextComponentBorder(c2, g2d, 0, 0, width, height, radius, borderColorScheme);
                    g2d.dispose();
                    smallImageCache.put(hashKey, result);
                }
                graphics.drawImage((Image)result, x2, y2, null);
            } else {
                graphics.translate(x2, y2);
                SubstanceImageCreator.paintSimpleBorder(c2, graphics, width, height, borderColorScheme);
            }
        }
        graphics.dispose();
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        this.paintBorder((JComponent)c2, g2, x2, y2, width, height, c2.isEnabled(), c2.hasFocus());
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        return this.myInsets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}

