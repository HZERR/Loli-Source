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
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceTableUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceTableCellBorder
implements Border,
UIResource {
    protected Insets myInsets;
    protected SubstanceTableUI ui;
    protected SubstanceTableUI.TableCellId cellId;
    protected float alpha;
    private static LazyResettableHashMap<BufferedImage> smallImageCache = new LazyResettableHashMap("SubstanceTableCellBorder");

    public SubstanceTableCellBorder(Insets insets, SubstanceTableUI ui, SubstanceTableUI.TableCellId cellId) {
        this.myInsets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
        this.ui = ui;
        this.cellId = cellId;
        this.alpha = 1.0f;
    }

    private void paintBorder(JComponent c2, Graphics g2, int x2, int y2, int width, int height, boolean isEnabled, boolean hasFocus) {
        ComponentState currState;
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        float radius = 0.0f;
        StateTransitionTracker stateTransitionTracker = this.ui.getStateTransitionTracker(this.cellId);
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker == null ? null : stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo == null ? null : modelStateInfo.getStateContributionMap();
        ComponentState componentState = currState = modelStateInfo == null ? this.ui.getCellState(this.cellId) : modelStateInfo.getCurrModelState();
        if (currState.isDisabled()) {
            currState = ComponentState.DISABLED_SELECTED;
        }
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, currState);
        HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(SubstanceSizeUtils.getComponentFontSize(c2), width, height, Float.valueOf(radius), baseBorderScheme.getDisplayName());
        BufferedImage baseLayer = smallImageCache.get(baseKey);
        float baseAlpha = SubstanceColorSchemeUtilities.getAlpha(c2, currState);
        if (baseLayer == null) {
            baseLayer = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D g2d = baseLayer.createGraphics();
            SubstanceImageCreator.paintBorder(c2, g2d, 0, 0, width, height, radius, baseBorderScheme);
            g2d.dispose();
            smallImageCache.put(baseKey, baseLayer);
        }
        graphics.setComposite(AlphaComposite.SrcOver.derive(baseAlpha * this.alpha));
        graphics.drawImage((Image)baseLayer, x2, y2, null);
        if (!currState.isDisabled() && activeStates != null && activeStates.size() > 1) {
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, activeState);
                HashMapKey key = SubstanceCoreUtilities.getHashKey(SubstanceSizeUtils.getComponentFontSize(c2), width, height, Float.valueOf(radius), borderScheme.getDisplayName());
                BufferedImage layer = smallImageCache.get(key);
                float activeAlpha = SubstanceColorSchemeUtilities.getAlpha(c2, activeState);
                if (layer == null) {
                    layer = SubstanceCoreUtilities.getBlankImage(width, height);
                    Graphics2D g2d = layer.createGraphics();
                    SubstanceImageCreator.paintBorder(c2, g2d, 0, 0, width, height, radius, borderScheme);
                    g2d.dispose();
                    smallImageCache.put(key, layer);
                }
                graphics.setComposite(AlphaComposite.SrcOver.derive(activeAlpha * this.alpha * contribution));
                graphics.drawImage((Image)layer, x2, y2, null);
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

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}

