/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.Sideable;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.border.SubstanceButtonBorder;

public class SubstanceSpinnerButton
extends JButton
implements Sideable,
SubstanceInternalArrowButton {
    private int orientation;

    public SubstanceSpinnerButton(JSpinner spinner, final int orientation) {
        this.setEnabled(spinner.isEnabled());
        this.setFocusable(false);
        this.setRequestFocusEnabled(false);
        this.setMargin(new Insets(0, 0, 0, 2));
        this.setBorder(new SpinnerButtonBorder(ClassicButtonShaper.class){

            @Override
            public Insets getBorderInsets(Component c2) {
                int extraPadding = SubstanceSizeUtils.getExtraPadding(SubstanceSizeUtils.getComponentFontSize(c2));
                int delta = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getComponentFontSize(c2), 3, 3, 1, false);
                int deltaTop = orientation == 1 ? delta : 0;
                int deltaBottom = orientation == 1 ? 0 : delta;
                return new Insets(extraPadding + deltaTop, extraPadding, extraPadding + deltaBottom, extraPadding);
            }
        });
        this.orientation = orientation;
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
    }

    @Override
    public void setBorder(Border border) {
        if (border instanceof SpinnerButtonBorder) {
            super.setBorder(border);
        }
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    @Override
    public SubstanceConstants.Side getSide() {
        switch (this.orientation) {
            case 1: {
                return SubstanceConstants.Side.BOTTOM;
            }
            case 7: {
                return SubstanceConstants.Side.RIGHT;
            }
            case 5: {
                return SubstanceConstants.Side.TOP;
            }
            case 3: {
                return SubstanceConstants.Side.LEFT;
            }
        }
        return null;
    }

    @Override
    protected void paintBorder(Graphics g2) {
        if (SubstanceCoreUtilities.isButtonNeverPainted(this)) {
            return;
        }
        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)this.getUI());
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        ComponentState currState = modelStateInfo.getCurrModelState();
        float extraAlpha = stateTransitionTracker.getActiveStrength();
        if (currState == ComponentState.DISABLED_UNSELECTED) {
            extraAlpha = 0.0f;
        }
        if (extraAlpha == 0.0f) {
            return;
        }
        boolean isNextButton = "Spinner.nextButton".equals(this.getName());
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(this);
        int borderDelta = (int)Math.floor(1.5 * (double)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize));
        float radius = Math.max(0.0f, 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(componentFontSize) - (float)borderDelta);
        int width = this.getWidth();
        int height = this.getHeight();
        JSpinner parent = (JSpinner)this.getParent();
        BufferedImage offscreen = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D g2offscreen = offscreen.createGraphics();
        int offsetX = this.getX();
        int offsetY = this.getY();
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(this, ColorSchemeAssociationKind.BORDER, currState);
        if (isNextButton) {
            SubstanceImageCreator.paintTextComponentBorder(this, g2offscreen, 0, 0, width, 1 * height, radius, baseBorderScheme);
            g2offscreen.translate(-offsetX, -offsetY);
            SubstanceImageCreator.paintTextComponentBorder(parent, g2offscreen, 0, 0, parent.getWidth(), parent.getHeight(), radius, baseBorderScheme);
            g2offscreen.translate(offsetX, offsetY);
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                g2offscreen.setComposite(AlphaComposite.SrcOver.derive(contribution));
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this, ColorSchemeAssociationKind.BORDER, activeState);
                SubstanceImageCreator.paintTextComponentBorder(this, g2offscreen, 0, 0, width, 1 * height, radius, borderScheme);
                g2offscreen.translate(-offsetX, -offsetY);
                SubstanceImageCreator.paintTextComponentBorder(parent, g2offscreen, 0, 0, parent.getWidth(), parent.getHeight(), radius, borderScheme);
                g2offscreen.translate(offsetX, offsetY);
            }
        } else {
            SubstanceImageCreator.paintTextComponentBorder(this, g2offscreen, 0, 0, width, 1 * height, radius, baseBorderScheme);
            g2offscreen.translate(-offsetX, -offsetY);
            SubstanceImageCreator.paintTextComponentBorder(parent, g2offscreen, 0, 0, parent.getWidth(), parent.getHeight(), radius, baseBorderScheme);
            g2offscreen.translate(offsetX, offsetY);
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                g2offscreen.setComposite(AlphaComposite.SrcOver.derive(contribution));
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this, ColorSchemeAssociationKind.BORDER, activeState);
                SubstanceImageCreator.paintTextComponentBorder(this, g2offscreen, 0, 0, width, 1 * height, radius, borderScheme);
                g2offscreen.translate(-offsetX, -offsetY);
                SubstanceImageCreator.paintTextComponentBorder(parent, g2offscreen, 0, 0, parent.getWidth(), parent.getHeight(), radius, borderScheme);
                g2offscreen.translate(offsetX, offsetY);
            }
        }
        g2offscreen.dispose();
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this, extraAlpha, g2));
        g2d.drawImage((Image)offscreen, 0, 0, null);
        g2d.dispose();
    }

    @Override
    public void paint(Graphics g2) {
        Graphics2D g2d = (Graphics2D)g2.create();
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(this);
        int width = this.getWidth();
        int height = this.getHeight();
        int clipDelta = (int)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize);
        if (this.getComponentOrientation().isLeftToRight()) {
            g2d.clipRect(clipDelta, 0, width - clipDelta, height);
        } else {
            g2d.clipRect(0, 0, width - clipDelta, height);
        }
        super.paint(g2d);
        g2d.dispose();
    }

    static {
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_BUTTON_PRESS, SubstanceSpinnerButton.class);
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_ICON_ROLLOVER, SubstanceSpinnerButton.class);
    }

    private static abstract class SpinnerButtonBorder
    extends SubstanceButtonBorder {
        public SpinnerButtonBorder(Class<?> buttonShaperClass) {
            super(buttonShaperClass);
        }
    }
}

