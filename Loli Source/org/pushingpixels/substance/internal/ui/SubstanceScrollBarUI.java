/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.SimplisticFillPainter;
import org.pushingpixels.substance.internal.painter.SimplisticSoftBorderPainter;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.RolloverControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.ArrowButtonTransitionAwareIcon;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollButton;

public class SubstanceScrollBarUI
extends BasicScrollBarUI
implements TransitionAwareUI {
    protected boolean containerGhostingMarker;
    protected Set lafWidgets;
    protected JButton mySecondDecreaseButton;
    protected JButton mySecondIncreaseButton;
    private ButtonModel thumbModel = new DefaultButtonModel();
    private static LazyResettableHashMap<BufferedImage> thumbVerticalMap = new LazyResettableHashMap("SubstanceScrollBarUI.thumbVertical");
    private static LazyResettableHashMap<BufferedImage> thumbHorizontalMap = new LazyResettableHashMap("SubstanceScrollBarUI.thumbHorizontal");
    private MouseListener substanceMouseListener;
    private RolloverControlListener substanceThumbRolloverListener;
    protected StateTransitionTracker compositeStateTransitionTracker;
    private PropertyChangeListener substancePropertyListener;
    protected int scrollBarWidth;
    private static LazyResettableHashMap<BufferedImage> trackHorizontalMap = new LazyResettableHashMap("SubstanceScrollBarUI.trackHorizontal");
    private static LazyResettableHashMap<BufferedImage> trackVerticalMap = new LazyResettableHashMap("SubstanceScrollBarUI.trackVertical");
    protected AdjustmentListener substanceAdjustmentListener;
    protected CompositeButtonModel compositeScrollTrackModel;

    @Override
    protected void update(Graphics graphics, JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__container__update(graphics, jComponent);
        GhostPaintingUtils.paintGhostImages(jComponent, graphics);
    }

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__container__update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceScrollBarUI(comp);
    }

    protected SubstanceScrollBarUI(JComponent b2) {
        this.thumbModel.setArmed(false);
        this.thumbModel.setSelected(false);
        this.thumbModel.setPressed(false);
        this.thumbModel.setRollover(false);
        b2.setOpaque(false);
    }

    protected JButton createGeneralDecreaseButton(int orientation, boolean isRegular) {
        SubstanceScrollButton result = new SubstanceScrollButton(orientation);
        result.setName("Decrease " + (isRegular ? "regular" : "additional"));
        result.setFont(this.scrollbar.getFont());
        ArrowButtonTransitionAwareIcon icon = new ArrowButtonTransitionAwareIcon(result, orientation);
        result.setIcon(icon);
        result.setFont(this.scrollbar.getFont());
        result.setPreferredSize(new Dimension(this.scrollBarWidth, this.scrollBarWidth));
        EnumSet<SubstanceConstants.Side> openSides = EnumSet.noneOf(SubstanceConstants.Side.class);
        EnumSet<SubstanceConstants.Side> straightSides = EnumSet.noneOf(SubstanceConstants.Side.class);
        switch (orientation) {
            case 1: {
                openSides.add(SubstanceConstants.Side.BOTTOM);
                if (!isRegular) {
                    openSides.add(SubstanceConstants.Side.TOP);
                }
                if (!isRegular) break;
                straightSides.add(SubstanceConstants.Side.TOP);
                break;
            }
            case 3: {
                openSides.add(SubstanceConstants.Side.LEFT);
                if (!isRegular) {
                    openSides.add(SubstanceConstants.Side.RIGHT);
                }
                if (!isRegular) break;
                straightSides.add(SubstanceConstants.Side.RIGHT);
                break;
            }
            case 7: {
                openSides.add(SubstanceConstants.Side.RIGHT);
                if (!isRegular) {
                    openSides.add(SubstanceConstants.Side.LEFT);
                }
                if (!isRegular) break;
                straightSides.add(SubstanceConstants.Side.LEFT);
            }
        }
        result.putClientProperty("substancelaf.buttonopenSide", openSides);
        result.putClientProperty("substancelaf.buttonside", straightSides);
        return result;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return this.createGeneralDecreaseButton(orientation, true);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return this.createGeneralIncreaseButton(orientation, true);
    }

    protected JButton createGeneralIncreaseButton(int orientation, boolean isRegular) {
        SubstanceScrollButton result = new SubstanceScrollButton(orientation);
        result.setName("Increase " + (isRegular ? "regular" : "additional"));
        result.setFont(this.scrollbar.getFont());
        ArrowButtonTransitionAwareIcon icon = new ArrowButtonTransitionAwareIcon(result, orientation);
        result.setIcon(icon);
        result.setFont(this.scrollbar.getFont());
        result.setPreferredSize(new Dimension(this.scrollBarWidth, this.scrollBarWidth));
        EnumSet<SubstanceConstants.Side> openSides = EnumSet.noneOf(SubstanceConstants.Side.class);
        EnumSet<SubstanceConstants.Side> straightSides = EnumSet.noneOf(SubstanceConstants.Side.class);
        switch (orientation) {
            case 5: {
                openSides.add(SubstanceConstants.Side.TOP);
                if (!isRegular) {
                    openSides.add(SubstanceConstants.Side.BOTTOM);
                }
                if (!isRegular) break;
                straightSides.add(SubstanceConstants.Side.BOTTOM);
                break;
            }
            case 3: {
                openSides.add(SubstanceConstants.Side.LEFT);
                if (!isRegular) {
                    openSides.add(SubstanceConstants.Side.RIGHT);
                }
                if (!isRegular) break;
                straightSides.add(SubstanceConstants.Side.RIGHT);
                break;
            }
            case 7: {
                openSides.add(SubstanceConstants.Side.RIGHT);
                if (!isRegular) {
                    openSides.add(SubstanceConstants.Side.LEFT);
                }
                if (!isRegular) break;
                straightSides.add(SubstanceConstants.Side.LEFT);
            }
        }
        result.putClientProperty("substancelaf.buttonopenSide", openSides);
        result.putClientProperty("substancelaf.buttonside", straightSides);
        return result;
    }

    private void paintTrackHorizontal(Graphics g2, Rectangle trackBounds, SubstanceScrollButton leftActiveButton, SubstanceScrollButton rightActiveButton) {
        int width = Math.max(1, trackBounds.width);
        int height = Math.max(1, trackBounds.height);
        SubstanceScrollBarUI.paintTrackBackHorizontal(g2, this.scrollbar, leftActiveButton, rightActiveButton, width, height);
        BufferedImage horizontalTrack = SubstanceScrollBarUI.getTrackHorizontal(this.scrollbar, width, height);
        g2.drawImage(horizontalTrack, 0, 0, null);
    }

    private static BufferedImage getTrackHorizontal(JScrollBar scrollBar, int width, int height) {
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(scrollBar);
        SubstanceColorScheme mainScheme = SubstanceColorSchemeUtilities.getColorScheme(scrollBar, scrollBar.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme mainBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(scrollBar, ColorSchemeAssociationKind.BORDER, scrollBar.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(mainScheme.getDisplayName(), mainBorderScheme.getDisplayName(), width, height, shaper.getDisplayName());
        float radius = height / 2;
        if (shaper instanceof ClassicButtonShaper) {
            radius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(scrollBar));
        }
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(scrollBar)) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, radius, null, borderDelta);
        BufferedImage result = trackHorizontalMap.get(key);
        if (result == null) {
            result = SubstanceCoreUtilities.getBlankImage(width, height);
            SimplisticFillPainter.INSTANCE.paintContourBackground(result.createGraphics(), scrollBar, width, height, contour, false, mainScheme, true);
            SimplisticSoftBorderPainter borderPainter = new SimplisticSoftBorderPainter();
            borderPainter.paintBorder(result.getGraphics(), scrollBar, width, height, contour, null, mainBorderScheme);
            trackHorizontalMap.put(key, result);
        }
        return result;
    }

    private static void paintTrackBackHorizontal(Graphics g2, JScrollBar scrollBar, AbstractButton leftActiveButton, AbstractButton rightActiveButton, int width, int height) {
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(scrollBar);
        int radius = height / 2;
        if (shaper instanceof ClassicButtonShaper) {
            radius = 2;
        }
        SubstanceImageCreator.paintCompositeRoundedBackground(scrollBar, g2, width, height, radius, leftActiveButton, rightActiveButton, false);
    }

    private void paintTrackVertical(Graphics g2, Rectangle trackBounds, SubstanceScrollButton topActiveButton, SubstanceScrollButton bottomActiveButton) {
        int width = Math.max(1, trackBounds.width);
        int height = Math.max(1, trackBounds.height);
        SubstanceScrollBarUI.paintTrackBackVertical(g2, this.scrollbar, topActiveButton, bottomActiveButton, width, height);
        BufferedImage horizontalTrack = SubstanceScrollBarUI.getTrackVertical(this.scrollbar, width, height);
        g2.drawImage(horizontalTrack, 0, 0, null);
    }

    private static BufferedImage getTrackVertical(JScrollBar scrollBar, int width, int height) {
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(scrollBar);
        SubstanceColorScheme mainScheme = SubstanceColorSchemeUtilities.getColorScheme(scrollBar, scrollBar.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme mainBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(scrollBar, ColorSchemeAssociationKind.BORDER, scrollBar.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(mainScheme.getDisplayName(), mainBorderScheme.getDisplayName(), width, height, shaper.getDisplayName());
        BufferedImage result = trackVerticalMap.get(key);
        if (result == null) {
            float radius = width / 2;
            if (shaper instanceof ClassicButtonShaper) {
                radius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(scrollBar));
            }
            int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(scrollBar)) / 2.0);
            GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(height, width, radius, null, borderDelta);
            result = SubstanceCoreUtilities.getBlankImage(height, width);
            SimplisticFillPainter.INSTANCE.paintContourBackground(result.createGraphics(), scrollBar, height, width, contour, false, mainScheme, true);
            SimplisticSoftBorderPainter borderPainter = new SimplisticSoftBorderPainter();
            borderPainter.paintBorder(result.getGraphics(), scrollBar, height, width, contour, null, mainBorderScheme);
            result = SubstanceImageCreator.getRotated(result, 3);
            trackVerticalMap.put(key, result);
        }
        return result;
    }

    private static void paintTrackBackVertical(Graphics g2, JScrollBar scrollBar, AbstractButton topActiveButton, AbstractButton bottomActiveButton, int width, int height) {
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(scrollBar);
        int radius = width / 2;
        if (shaper instanceof ClassicButtonShaper) {
            radius = 2;
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        AffineTransform at = AffineTransform.getTranslateInstance(0.0, height);
        at.rotate(-1.5707963267948966);
        g2d.transform(at);
        SubstanceImageCreator.paintCompositeRoundedBackground(scrollBar, g2d, height, width, radius, topActiveButton, bottomActiveButton, true);
        g2d.dispose();
    }

    private BufferedImage getThumbVertical(Rectangle thumbBounds) {
        int width = Math.max(1, thumbBounds.width);
        int height = Math.max(1, thumbBounds.height);
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.compositeStateTransitionTracker.getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        SubstanceColorScheme baseFillScheme = currState != ComponentState.ENABLED ? SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, currState) : SubstanceColorSchemeUtilities.getActiveColorScheme(this.scrollbar, currState);
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, ColorSchemeAssociationKind.BORDER, currState);
        BufferedImage baseLayer = SubstanceScrollBarUI.getThumbVertical(this.scrollbar, width, height, baseFillScheme, baseBorderScheme);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        if (currState.isDisabled() || activeStates.size() == 1) {
            return baseLayer;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(baseLayer.getWidth(), baseLayer.getHeight());
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage((Image)baseLayer, 0, 0, null);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float contribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == modelStateInfo.getCurrModelState() || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
            SubstanceColorScheme fillScheme = activeState != ComponentState.ENABLED ? SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, activeState) : SubstanceColorSchemeUtilities.getActiveColorScheme(this.scrollbar, activeState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, ColorSchemeAssociationKind.BORDER, activeState);
            BufferedImage layer = SubstanceScrollBarUI.getThumbVertical(this.scrollbar, width, height, fillScheme, borderScheme);
            g2d.drawImage((Image)layer, 0, 0, null);
        }
        g2d.dispose();
        return result;
    }

    private static BufferedImage getThumbVertical(JScrollBar scrollBar, int width, int height, SubstanceColorScheme scheme, SubstanceColorScheme borderScheme) {
        SubstanceFillPainter painter = SubstanceCoreUtilities.getFillPainter(scrollBar);
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(scrollBar);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(scrollBar);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, scheme.getDisplayName(), borderScheme.getDisplayName(), painter.getDisplayName(), shaper.getDisplayName(), borderPainter.getDisplayName());
        BufferedImage result = thumbVerticalMap.get(key);
        if (result == null) {
            float radius = width / 2;
            if (shaper instanceof ClassicButtonShaper) {
                radius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(scrollBar));
            }
            int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(scrollBar)) / 2.0);
            GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(height, width, radius, null, borderDelta);
            result = SubstanceCoreUtilities.getBlankImage(height, width);
            painter.paintContourBackground(result.createGraphics(), scrollBar, height, width, contour, false, scheme, true);
            borderPainter.paintBorder(result.getGraphics(), scrollBar, height, width, contour, null, borderScheme);
            result = SubstanceImageCreator.getRotated(result, 3);
            thumbVerticalMap.put(key, result);
        }
        return result;
    }

    private BufferedImage getThumbHorizontal(Rectangle thumbBounds) {
        int width = Math.max(1, thumbBounds.width);
        int height = Math.max(1, thumbBounds.height);
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.compositeStateTransitionTracker.getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        SubstanceColorScheme baseFillScheme = currState != ComponentState.ENABLED ? SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, currState) : SubstanceColorSchemeUtilities.getActiveColorScheme(this.scrollbar, currState);
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, ColorSchemeAssociationKind.BORDER, currState);
        BufferedImage baseLayer = SubstanceScrollBarUI.getThumbHorizontal(this.scrollbar, width, height, baseFillScheme, baseBorderScheme);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        if (currState.isDisabled() || activeStates.size() == 1) {
            return baseLayer;
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(baseLayer.getWidth(), baseLayer.getHeight());
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage((Image)baseLayer, 0, 0, null);
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float contribution;
            ComponentState activeState = activeEntry.getKey();
            if (activeState == modelStateInfo.getCurrModelState() || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
            g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
            SubstanceColorScheme fillScheme = activeState != ComponentState.ENABLED ? SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, activeState) : SubstanceColorSchemeUtilities.getActiveColorScheme(this.scrollbar, activeState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.scrollbar, ColorSchemeAssociationKind.BORDER, activeState);
            BufferedImage layer = SubstanceScrollBarUI.getThumbHorizontal(this.scrollbar, width, height, fillScheme, borderScheme);
            g2d.drawImage((Image)layer, 0, 0, null);
        }
        g2d.dispose();
        return result;
    }

    private static BufferedImage getThumbHorizontal(JScrollBar scrollBar, int width, int height, SubstanceColorScheme scheme, SubstanceColorScheme borderScheme) {
        SubstanceFillPainter painter = SubstanceCoreUtilities.getFillPainter(scrollBar);
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(scrollBar);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(scrollBar);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, scheme.getDisplayName(), borderScheme.getDisplayName(), painter.getDisplayName(), shaper.getDisplayName(), borderPainter.getDisplayName());
        float radius = height / 2;
        if (shaper instanceof ClassicButtonShaper) {
            radius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(scrollBar));
        }
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(scrollBar)) / 2.0);
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, radius, null, borderDelta);
        BufferedImage opaque = thumbHorizontalMap.get(key);
        if (opaque == null) {
            opaque = SubstanceCoreUtilities.getBlankImage(width, height);
            painter.paintContourBackground(opaque.createGraphics(), scrollBar, width, height, contour, false, scheme, true);
            borderPainter.paintBorder(opaque.getGraphics(), scrollBar, width, height, contour, null, borderScheme);
            thumbHorizontalMap.put(key, opaque);
        }
        return opaque;
    }

    protected ComponentState getState(JButton scrollButton) {
        if (scrollButton == null) {
            return null;
        }
        ComponentState result = ((TransitionAwareUI)((Object)scrollButton.getUI())).getTransitionTracker().getModelStateInfo().getCurrModelState();
        if (result == ComponentState.ENABLED && SubstanceCoreUtilities.hasFlatAppearance(this.scrollbar, false)) {
            result = null;
        }
        if (SubstanceCoreUtilities.isButtonNeverPainted(scrollButton)) {
            result = null;
        }
        return result;
    }

    @Override
    protected void paintTrack(Graphics g2, JComponent c2, Rectangle trackBounds) {
        Graphics2D graphics = (Graphics2D)g2.create();
        SubstanceConstants.ScrollPaneButtonPolicyKind buttonPolicy = SubstanceCoreUtilities.getScrollPaneButtonsPolicyKind(this.scrollbar);
        SubstanceScrollButton compTopState = null;
        SubstanceScrollButton compBottomState = null;
        if (this.decrButton.isShowing() && this.incrButton.isShowing() && this.mySecondDecreaseButton.isShowing() && this.mySecondIncreaseButton.isShowing()) {
            switch (buttonPolicy) {
                case OPPOSITE: {
                    compTopState = (SubstanceScrollButton)this.decrButton;
                    compBottomState = (SubstanceScrollButton)this.incrButton;
                    break;
                }
                case ADJACENT: {
                    compBottomState = (SubstanceScrollButton)this.mySecondDecreaseButton;
                    break;
                }
                case MULTIPLE: {
                    compTopState = (SubstanceScrollButton)this.decrButton;
                    compBottomState = (SubstanceScrollButton)this.mySecondDecreaseButton;
                    break;
                }
                case MULTIPLE_BOTH: {
                    compTopState = (SubstanceScrollButton)this.mySecondIncreaseButton;
                    compBottomState = (SubstanceScrollButton)this.mySecondDecreaseButton;
                }
            }
        }
        graphics.translate(trackBounds.x, trackBounds.y);
        if (this.scrollbar.getOrientation() == 1) {
            this.paintTrackVertical(graphics, trackBounds, compTopState, compBottomState);
        } else if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            this.paintTrackHorizontal(graphics, trackBounds, compTopState, compBottomState);
        } else {
            this.paintTrackHorizontal(graphics, trackBounds, compBottomState, compTopState);
        }
        graphics.dispose();
    }

    @Override
    protected void paintThumb(Graphics g2, JComponent c2, Rectangle thumbBounds) {
        boolean isVertical;
        Graphics2D graphics = (Graphics2D)g2.create();
        this.thumbModel.setSelected(this.thumbModel.isSelected() || this.isDragging);
        this.thumbModel.setEnabled(c2.isEnabled());
        boolean bl = isVertical = this.scrollbar.getOrientation() == 1;
        if (isVertical) {
            Rectangle adjustedBounds = new Rectangle(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            BufferedImage thumbImage = this.getThumbVertical(adjustedBounds);
            graphics.drawImage((Image)thumbImage, adjustedBounds.x, adjustedBounds.y, null);
        } else {
            Rectangle adjustedBounds = new Rectangle(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            BufferedImage thumbImage = this.getThumbHorizontal(adjustedBounds);
            graphics.drawImage((Image)thumbImage, adjustedBounds.x, adjustedBounds.y, null);
        }
        graphics.dispose();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        BackgroundPaintingUtils.update(graphics, c2, false);
        float alpha = SubstanceColorSchemeUtilities.getAlpha(this.scrollbar, ComponentState.getState(this.thumbModel, this.scrollbar));
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(c2, alpha, g2));
        super.paint(graphics, c2);
        graphics.dispose();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installDefaults() {
        super.installDefaults();
        this.scrollBarWidth = SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getComponentFontSize(this.scrollbar));
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installComponents() {
        super.installComponents();
        switch (this.scrollbar.getOrientation()) {
            case 1: {
                this.mySecondDecreaseButton = this.createGeneralDecreaseButton(1, false);
                this.mySecondIncreaseButton = this.createGeneralIncreaseButton(5, false);
                break;
            }
            case 0: {
                if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
                    this.mySecondDecreaseButton = this.createGeneralDecreaseButton(7, false);
                    this.mySecondIncreaseButton = this.createGeneralIncreaseButton(3, false);
                    break;
                }
                this.mySecondDecreaseButton = this.createGeneralDecreaseButton(3, false);
                this.mySecondIncreaseButton = this.createGeneralIncreaseButton(7, false);
            }
        }
        this.scrollbar.add(this.mySecondDecreaseButton);
        this.scrollbar.add(this.mySecondIncreaseButton);
        this.compositeScrollTrackModel = new CompositeButtonModel(this.thumbModel, this.incrButton, this.decrButton, this.mySecondDecreaseButton, this.mySecondIncreaseButton);
        this.compositeScrollTrackModel.registerListeners();
        this.compositeStateTransitionTracker = new StateTransitionTracker(this.scrollbar, this.compositeScrollTrackModel);
        this.compositeStateTransitionTracker.registerModelListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallComponents() {
        this.compositeScrollTrackModel.unregisterListeners();
        this.compositeStateTransitionTracker.unregisterModelListeners();
        this.scrollbar.remove(this.mySecondDecreaseButton);
        this.scrollbar.remove(this.mySecondIncreaseButton);
        super.uninstallComponents();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__installListeners() {
        super.installListeners();
        this.substanceMouseListener = new MouseAdapter(){

            @Override
            public void mouseEntered(MouseEvent e2) {
                SubstanceScrollBarUI.this.scrollbar.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e2) {
                SubstanceScrollBarUI.this.scrollbar.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e2) {
                SubstanceScrollBarUI.this.scrollbar.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e2) {
                SubstanceScrollBarUI.this.scrollbar.repaint();
            }
        };
        this.incrButton.addMouseListener(this.substanceMouseListener);
        this.decrButton.addMouseListener(this.substanceMouseListener);
        this.mySecondDecreaseButton.addMouseListener(this.substanceMouseListener);
        this.mySecondIncreaseButton.addMouseListener(this.substanceMouseListener);
        this.substanceThumbRolloverListener = new RolloverControlListener(this, this.thumbModel);
        this.scrollbar.addMouseListener(this.substanceThumbRolloverListener);
        this.scrollbar.addMouseMotionListener(this.substanceThumbRolloverListener);
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Color newBackgr;
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceScrollBarUI.this.scrollbar != null) {
                                SubstanceScrollBarUI.this.scrollbar.updateUI();
                            }
                        }
                    });
                }
                if ("background".equals(evt.getPropertyName()) && !((newBackgr = (Color)evt.getNewValue()) instanceof UIResource)) {
                    if (SubstanceScrollBarUI.this.mySecondDecreaseButton != null && SubstanceScrollBarUI.this.mySecondDecreaseButton.getBackground() instanceof UIResource) {
                        SubstanceScrollBarUI.this.mySecondDecreaseButton.setBackground(newBackgr);
                    }
                    if (SubstanceScrollBarUI.this.mySecondIncreaseButton != null && SubstanceScrollBarUI.this.mySecondIncreaseButton.getBackground() instanceof UIResource) {
                        SubstanceScrollBarUI.this.mySecondIncreaseButton.setBackground(newBackgr);
                    }
                    if (SubstanceScrollBarUI.this.incrButton != null && SubstanceScrollBarUI.this.incrButton.getBackground() instanceof UIResource) {
                        SubstanceScrollBarUI.this.incrButton.setBackground(newBackgr);
                    }
                    if (SubstanceScrollBarUI.this.decrButton != null && SubstanceScrollBarUI.this.decrButton.getBackground() instanceof UIResource) {
                        SubstanceScrollBarUI.this.decrButton.setBackground(newBackgr);
                    }
                }
            }
        };
        this.scrollbar.addPropertyChangeListener(this.substancePropertyListener);
        this.mySecondDecreaseButton.addMouseListener(this.buttonListener);
        this.mySecondIncreaseButton.addMouseListener(this.buttonListener);
        this.substanceAdjustmentListener = new AdjustmentListener(){

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e2) {
                SubstanceCoreUtilities.testComponentStateChangeThreadingViolation(SubstanceScrollBarUI.this.scrollbar);
                Container parent = SubstanceScrollBarUI.this.scrollbar.getParent();
                if (parent instanceof JScrollPane) {
                    JScrollPane jsp = (JScrollPane)parent;
                    JScrollBar hor = jsp.getHorizontalScrollBar();
                    JScrollBar ver = jsp.getVerticalScrollBar();
                    JScrollBar other = null;
                    if (SubstanceScrollBarUI.this.scrollbar == hor) {
                        other = ver;
                    }
                    if (SubstanceScrollBarUI.this.scrollbar == ver) {
                        other = hor;
                    }
                    if (other != null && other.isVisible()) {
                        other.repaint();
                    }
                    SubstanceScrollBarUI.this.scrollbar.repaint();
                }
            }
        };
        this.scrollbar.addAdjustmentListener(this.substanceAdjustmentListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollBarUI__uninstallListeners() {
        this.incrButton.removeMouseListener(this.substanceMouseListener);
        this.decrButton.removeMouseListener(this.substanceMouseListener);
        this.mySecondDecreaseButton.removeMouseListener(this.substanceMouseListener);
        this.mySecondIncreaseButton.removeMouseListener(this.substanceMouseListener);
        this.substanceMouseListener = null;
        this.scrollbar.removeMouseListener(this.substanceThumbRolloverListener);
        this.scrollbar.removeMouseMotionListener(this.substanceThumbRolloverListener);
        this.substanceThumbRolloverListener = null;
        this.scrollbar.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        this.mySecondDecreaseButton.removeMouseListener(this.buttonListener);
        this.mySecondIncreaseButton.removeMouseListener(this.buttonListener);
        this.scrollbar.removeAdjustmentListener(this.substanceAdjustmentListener);
        this.substanceAdjustmentListener = null;
        super.uninstallListeners();
    }

    @Override
    public boolean isInside(MouseEvent me) {
        Rectangle trackB = this.getTrackBounds();
        if (trackB == null) {
            return false;
        }
        return trackB.contains(me.getX(), me.getY());
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.compositeStateTransitionTracker;
    }

    @Override
    public void scrollByBlock(int direction) {
        int oldValue = this.scrollbar.getValue();
        int blockIncrement = this.scrollbar.getBlockIncrement(direction);
        int delta = blockIncrement * (direction > 0 ? 1 : -1);
        int newValue = oldValue + delta;
        if (delta > 0 && newValue < oldValue) {
            newValue = this.scrollbar.getMaximum();
        } else if (delta < 0 && newValue > oldValue) {
            newValue = this.scrollbar.getMinimum();
        }
        this.scrollbar.setValue(newValue);
    }

    public void scrollByUnits(int direction, int units) {
        for (int i2 = 0; i2 < units; ++i2) {
            int delta = direction > 0 ? this.scrollbar.getUnitIncrement(direction) : -this.scrollbar.getUnitIncrement(direction);
            int oldValue = this.scrollbar.getValue();
            int newValue = oldValue + delta;
            if (delta > 0 && newValue < oldValue) {
                newValue = this.scrollbar.getMaximum();
            } else if (delta < 0 && newValue > oldValue) {
                newValue = this.scrollbar.getMinimum();
            }
            if (oldValue == newValue) break;
            this.scrollbar.setValue(newValue);
        }
    }

    @Override
    protected void layoutVScrollbar(JScrollBar sb) {
        SubstanceConstants.ScrollPaneButtonPolicyKind buttonPolicy = SubstanceCoreUtilities.getScrollPaneButtonsPolicyKind(this.scrollbar);
        this.mySecondDecreaseButton.setBounds(0, 0, 0, 0);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        switch (buttonPolicy) {
            case OPPOSITE: {
                super.layoutVScrollbar(sb);
                break;
            }
            case NONE: {
                this.layoutVScrollbarNone(sb);
                break;
            }
            case ADJACENT: {
                this.layoutVScrollbarAdjacent(sb);
                break;
            }
            case MULTIPLE: {
                this.layoutVScrollbarMultiple(sb);
                break;
            }
            case MULTIPLE_BOTH: {
                this.layoutVScrollbarMultipleBoth(sb);
            }
        }
    }

    @Override
    protected void layoutHScrollbar(JScrollBar sb) {
        this.mySecondDecreaseButton.setBounds(0, 0, 0, 0);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        SubstanceConstants.ScrollPaneButtonPolicyKind buttonPolicy = SubstanceCoreUtilities.getScrollPaneButtonsPolicyKind(this.scrollbar);
        switch (buttonPolicy) {
            case OPPOSITE: {
                super.layoutHScrollbar(sb);
                break;
            }
            case NONE: {
                this.layoutHScrollbarNone(sb);
                break;
            }
            case ADJACENT: {
                this.layoutHScrollbarAdjacent(sb);
                break;
            }
            case MULTIPLE: {
                this.layoutHScrollbarMultiple(sb);
                break;
            }
            case MULTIPLE_BOTH: {
                this.layoutHScrollbarMultipleBoth(sb);
            }
        }
    }

    protected void layoutVScrollbarAdjacent(JScrollBar sb) {
        int sbAvailButtonH;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        int incrButtonH = itemW;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int decrButton2H = itemW;
        int decrButton2Y = incrButtonY - decrButton2H;
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButton2H + incrButtonH;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = (float)sb.getMaximum() - min;
        float value = sb.getValue();
        int thumbH = range <= 0.0f ? this.getMaximumThumbSize().height : (int)(trackH * (extent / range));
        thumbH = Math.max(thumbH, this.getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, this.getMaximumThumbSize().height);
        int thumbY = decrButton2Y - thumbH;
        if (value < (float)(sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - (float)thumbH;
            thumbY = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
        }
        if ((sbAvailButtonH = sbSize.height - sbInsetsH) < sbButtonsH) {
            incrButtonH = decrButton2H = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        this.decrButton.setBounds(0, 0, 0, 0);
        this.mySecondDecreaseButton.setBounds(itemX, incrButtonY - decrButton2H, itemW, decrButton2H);
        this.incrButton.setBounds(itemX, incrButtonY - 1, itemW, incrButtonH + 1);
        int itrackY = 0;
        int itrackH = decrButton2Y - itrackY;
        this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int)trackH) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbY + thumbH > decrButton2Y) {
                thumbY = decrButton2Y - thumbH;
            }
            if (thumbY < 0) {
                thumbY = 0;
            }
            this.setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutVScrollbarNone(JScrollBar sb) {
        int sbAvailButtonH;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        int incrButtonH = 0;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int decrButton2H = 0;
        int decrButton2Y = incrButtonY - decrButton2H;
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButton2H + incrButtonH;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = (float)sb.getMaximum() - min;
        float value = sb.getValue();
        int thumbH = range <= 0.0f ? this.getMaximumThumbSize().height : (int)(trackH * (extent / range));
        thumbH = Math.max(thumbH, this.getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, this.getMaximumThumbSize().height);
        int thumbY = decrButton2Y - thumbH;
        if (value < (float)(sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - (float)thumbH;
            thumbY = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
        }
        if ((sbAvailButtonH = sbSize.height - sbInsetsH) < sbButtonsH) {
            incrButtonH = 0;
        }
        this.decrButton.setBounds(0, 0, 0, 0);
        this.mySecondDecreaseButton.setBounds(0, 0, 0, 0);
        this.incrButton.setBounds(0, 0, 0, 0);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        int itrackY = 0;
        int itrackH = decrButton2Y - itrackY;
        this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int)trackH) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbY + thumbH > decrButton2Y) {
                thumbY = decrButton2Y - thumbH;
            }
            if (thumbY < 0) {
                thumbY = 0;
            }
            this.setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutVScrollbarMultiple(JScrollBar sb) {
        int sbAvailButtonH;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        int incrButtonH = itemW;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int decrButton2H = itemW;
        int decrButton2Y = incrButtonY - decrButton2H;
        int decrButtonH = itemW;
        int decrButtonY = sbInsets.top;
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButton2H + incrButtonH + decrButtonH;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = (float)sb.getMaximum() - min;
        float value = sb.getValue();
        int thumbH = range <= 0.0f ? this.getMaximumThumbSize().height : (int)(trackH * (extent / range));
        thumbH = Math.max(thumbH, this.getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, this.getMaximumThumbSize().height);
        int thumbY = decrButton2Y - thumbH;
        if (value < (float)(sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - (float)thumbH;
            thumbY = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
            thumbY += decrButtonY + decrButtonH;
        }
        if ((sbAvailButtonH = sbSize.height - sbInsetsH) < sbButtonsH) {
            decrButton2H = decrButtonH = sbAvailButtonH / 2;
            incrButtonH = decrButtonH;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }
        this.decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
        this.mySecondDecreaseButton.setBounds(itemX, incrButtonY - decrButton2H, itemW, decrButton2H);
        this.incrButton.setBounds(itemX, incrButtonY - 1, itemW, incrButtonH + 1);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        int itrackY = decrButtonY + decrButtonH;
        int itrackH = decrButton2Y - itrackY;
        this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int)trackH) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbY + thumbH > decrButton2Y) {
                thumbY = decrButton2Y - thumbH;
            }
            if (thumbY < decrButtonY + decrButtonH) {
                thumbY = decrButtonY + decrButtonH + 1;
            }
            this.setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutVScrollbarMultipleBoth(JScrollBar sb) {
        int sbAvailButtonH;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        int incrButtonH = itemW;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int decrButton2H = itemW;
        int decrButton2Y = incrButtonY - decrButton2H;
        int decrButtonH = itemW;
        int decrButtonY = sbInsets.top;
        int incrButton2H = itemW;
        int incrButton2Y = decrButtonY + decrButtonH;
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButton2H + incrButtonH + decrButtonH + incrButton2H;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = (float)sb.getMaximum() - min;
        float value = sb.getValue();
        int thumbH = range <= 0.0f ? this.getMaximumThumbSize().height : (int)(trackH * (extent / range));
        thumbH = Math.max(thumbH, this.getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, this.getMaximumThumbSize().height);
        int thumbY = decrButton2Y - thumbH;
        if (value < (float)(sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - (float)thumbH;
            thumbY = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
            thumbY += incrButton2Y + incrButton2H;
        }
        if ((sbAvailButtonH = sbSize.height - sbInsetsH) < sbButtonsH) {
            decrButtonH = incrButton2H = sbAvailButtonH / 4;
            decrButton2H = incrButton2H;
            incrButtonH = incrButton2H;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }
        this.decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
        this.mySecondDecreaseButton.setBounds(itemX, incrButtonY - decrButton2H, itemW, decrButton2H);
        this.incrButton.setBounds(itemX, incrButtonY - 1, itemW, incrButtonH + 1);
        this.mySecondIncreaseButton.setBounds(itemX, decrButtonY + decrButtonH - 1, itemW, incrButton2H + 1);
        int itrackY = incrButton2Y + incrButton2H;
        int itrackH = decrButton2Y - itrackY;
        this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int)trackH) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbY + thumbH > decrButton2Y) {
                thumbY = decrButton2Y - thumbH;
            }
            if (thumbY < incrButton2Y + incrButton2H) {
                thumbY = incrButton2Y + incrButton2H + 1;
            }
            this.setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutHScrollbarAdjacent(JScrollBar sb) {
        int sbAvailButtonW;
        int thumbX;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        int decrButton2W = itemH;
        int incrButtonW = itemH;
        int incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        int decrButton2X = ltr ? incrButtonX - decrButton2W : incrButtonX + decrButton2W;
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = decrButton2W + incrButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();
        int thumbW = range <= 0.0f ? this.getMaximumThumbSize().width : (int)(trackW * (extent / range));
        thumbW = Math.max(thumbW, this.getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, this.getMaximumThumbSize().width);
        int n2 = thumbX = ltr ? decrButton2X - thumbW : sbInsets.left;
        if (value < max - (float)sb.getVisibleAmount()) {
            float thumbRange = trackW - (float)thumbW;
            if (ltr) {
                thumbX = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
            } else {
                thumbX = (int)(0.5f + thumbRange * ((max - extent - value) / (range - extent)));
                thumbX += decrButton2X + decrButton2W;
            }
        }
        if ((sbAvailButtonW = sbSize.width - sbInsetsW) < sbButtonsW) {
            incrButtonW = decrButton2W = sbAvailButtonW / 2;
            incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        }
        this.mySecondDecreaseButton.setBounds(decrButton2X + (ltr ? 0 : -1), itemY, decrButton2W + 1, itemH);
        this.incrButton.setBounds(incrButtonX, itemY, incrButtonW, itemH);
        this.decrButton.setBounds(0, 0, 0, 0);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        if (ltr) {
            int itrackX = sbInsets.left;
            int itrackW = decrButton2X - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        } else {
            int itrackX = decrButton2X + decrButton2W;
            int itrackW = sbSize.width - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        }
        if (thumbW >= (int)trackW) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (ltr) {
                if (thumbX + thumbW > decrButton2X) {
                    thumbX = decrButton2X - thumbW;
                }
                if (thumbX < 0) {
                    thumbX = 1;
                }
            } else {
                if (thumbX + thumbW > sbSize.width - sbInsets.left) {
                    thumbX = sbSize.width - sbInsets.left - thumbW;
                }
                if (thumbX < decrButton2X + decrButton2W) {
                    thumbX = decrButton2X + decrButton2W + 1;
                }
            }
            this.setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    protected void layoutHScrollbarNone(JScrollBar sb) {
        int sbAvailButtonW;
        int thumbX;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        int decrButton2W = 0;
        int incrButtonW = 0;
        int incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        int decrButton2X = ltr ? incrButtonX - decrButton2W : incrButtonX + decrButton2W;
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = decrButton2W + incrButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();
        int thumbW = range <= 0.0f ? this.getMaximumThumbSize().width : (int)(trackW * (extent / range));
        thumbW = Math.max(thumbW, this.getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, this.getMaximumThumbSize().width);
        int n2 = thumbX = ltr ? decrButton2X - thumbW : sbInsets.left;
        if (value < max - (float)sb.getVisibleAmount()) {
            float thumbRange = trackW - (float)thumbW;
            if (ltr) {
                thumbX = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
            } else {
                thumbX = (int)(0.5f + thumbRange * ((max - extent - value) / (range - extent)));
                thumbX += decrButton2X + decrButton2W;
            }
        }
        if ((sbAvailButtonW = sbSize.width - sbInsetsW) < sbButtonsW) {
            decrButton2W = 0;
            incrButtonW = 0;
        }
        this.incrButton.setBounds(0, 0, 0, 0);
        this.decrButton.setBounds(0, 0, 0, 0);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        this.mySecondDecreaseButton.setBounds(0, 0, 0, 0);
        if (ltr) {
            int itrackX = sbInsets.left;
            int itrackW = decrButton2X - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        } else {
            int itrackX = decrButton2X + decrButton2W;
            int itrackW = sbSize.width - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        }
        if (thumbW >= (int)trackW) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (ltr) {
                if (thumbX + thumbW > decrButton2X) {
                    thumbX = decrButton2X - thumbW;
                }
                if (thumbX < 0) {
                    thumbX = 1;
                }
            } else {
                if (thumbX + thumbW > sbSize.width - sbInsets.left) {
                    thumbX = sbSize.width - sbInsets.left - thumbW;
                }
                if (thumbX < decrButton2X + decrButton2W) {
                    thumbX = decrButton2X + decrButton2W + 1;
                }
            }
            this.setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    protected void layoutHScrollbarMultiple(JScrollBar sb) {
        int sbAvailButtonW;
        int thumbX;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        int decrButton2W = itemH;
        int decrButtonW = itemH;
        int incrButtonW = itemH;
        int incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        int decrButton2X = ltr ? incrButtonX - decrButton2W : incrButtonX + decrButton2W;
        int decrButtonX = ltr ? sbInsets.left : sbSize.width - sbInsets.right - decrButtonW;
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = decrButton2W + incrButtonW + decrButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();
        int thumbW = range <= 0.0f ? this.getMaximumThumbSize().width : (int)(trackW * (extent / range));
        thumbW = Math.max(thumbW, this.getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, this.getMaximumThumbSize().width);
        int n2 = thumbX = ltr ? decrButton2X - thumbW : sbInsets.left;
        if (value < max - (float)sb.getVisibleAmount()) {
            float thumbRange = trackW - (float)thumbW;
            if (ltr) {
                thumbX = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
                thumbX += decrButtonX + decrButtonW;
            } else {
                thumbX = (int)(0.5f + thumbRange * ((max - extent - value) / (range - extent)));
                thumbX += decrButton2X + decrButton2W;
            }
        }
        if ((sbAvailButtonW = sbSize.width - sbInsetsW) < sbButtonsW) {
            decrButton2W = decrButtonW = sbAvailButtonW / 2;
            incrButtonW = decrButtonW;
            incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        }
        this.mySecondDecreaseButton.setBounds(decrButton2X + (ltr ? 0 : -1), itemY, decrButton2W + 1, itemH);
        this.incrButton.setBounds(incrButtonX, itemY, incrButtonW, itemH);
        this.decrButton.setBounds(decrButtonX, itemY, decrButtonW, itemH);
        this.mySecondIncreaseButton.setBounds(0, 0, 0, 0);
        if (ltr) {
            int itrackX = decrButtonX + decrButtonW;
            int itrackW = decrButton2X - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        } else {
            int itrackX = decrButton2X + decrButton2W;
            int itrackW = decrButtonX - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        }
        if (thumbW >= (int)trackW) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (ltr) {
                if (thumbX + thumbW > decrButton2X) {
                    thumbX = decrButton2X - thumbW;
                }
                if (thumbX < decrButtonX + decrButtonW) {
                    thumbX = decrButtonX + decrButtonW + 1;
                }
            } else {
                if (thumbX + thumbW > decrButtonX) {
                    thumbX = decrButtonX - thumbW;
                }
                if (thumbX < decrButton2X + decrButton2W) {
                    thumbX = decrButton2X + decrButton2W + 1;
                }
            }
            this.setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    protected void layoutHScrollbarMultipleBoth(JScrollBar sb) {
        int sbAvailButtonW;
        int thumbX;
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        int decrButton2W = itemH;
        int incrButton2W = itemH;
        int decrButtonW = itemH;
        int incrButtonW = itemH;
        int incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        int decrButton2X = ltr ? incrButtonX - decrButton2W : incrButtonX + decrButton2W;
        int decrButtonX = ltr ? sbInsets.left : sbSize.width - sbInsets.right - decrButtonW;
        int incrButton2X = ltr ? decrButtonX + decrButtonW : decrButtonX - incrButton2W;
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = decrButton2W + incrButtonW + decrButtonW + incrButton2W;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();
        int thumbW = range <= 0.0f ? this.getMaximumThumbSize().width : (int)(trackW * (extent / range));
        thumbW = Math.max(thumbW, this.getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, this.getMaximumThumbSize().width);
        int n2 = thumbX = ltr ? decrButton2X - thumbW : sbInsets.left;
        if (value < max - (float)sb.getVisibleAmount()) {
            float thumbRange = trackW - (float)thumbW;
            if (ltr) {
                thumbX = (int)(0.5f + thumbRange * ((value - min) / (range - extent)));
                thumbX += incrButton2X + incrButton2W;
            } else {
                thumbX = (int)(0.5f + thumbRange * ((max - extent - value) / (range - extent)));
                thumbX += decrButton2X + decrButton2W;
            }
        }
        if ((sbAvailButtonW = sbSize.width - sbInsetsW) < sbButtonsW) {
            decrButtonW = incrButton2W = sbAvailButtonW / 4;
            decrButton2W = incrButton2W;
            incrButtonW = incrButton2W;
            incrButtonX = ltr ? sbSize.width - (sbInsets.right + incrButtonW) : sbInsets.left;
        }
        this.mySecondDecreaseButton.setBounds(decrButton2X + (ltr ? 0 : -1), itemY, decrButton2W + 1, itemH);
        this.mySecondIncreaseButton.setBounds(incrButton2X + (ltr ? -1 : 0), itemY, incrButton2W + 1, itemH);
        this.incrButton.setBounds(incrButtonX, itemY, incrButtonW, itemH);
        this.decrButton.setBounds(decrButtonX, itemY, decrButtonW, itemH);
        if (ltr) {
            int itrackX = incrButton2X + incrButton2W;
            int itrackW = decrButton2X - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        } else {
            int itrackX = decrButton2X + decrButton2W;
            int itrackW = incrButton2X - itrackX;
            this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        }
        if (thumbW >= (int)trackW) {
            this.setThumbBounds(0, 0, 0, 0);
        } else {
            if (ltr) {
                if (thumbX + thumbW > decrButton2X) {
                    thumbX = decrButton2X - thumbW;
                }
                if (thumbX < incrButton2X + incrButton2W) {
                    thumbX = incrButton2X + incrButton2W + 1;
                }
            } else {
                if (thumbX + thumbW > incrButton2X) {
                    thumbX = incrButton2X - thumbW;
                }
                if (thumbX < decrButton2X + decrButton2W) {
                    thumbX = decrButton2X + decrButton2W + 1;
                }
            }
            this.setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    public static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceScrollBarUI: \n");
        sb.append("\t" + thumbHorizontalMap.size() + " thumb horizontal, " + thumbVerticalMap.size() + " thumb vertical");
        sb.append("\t" + trackHorizontalMap.size() + " track horizontal, " + trackVerticalMap.size() + " track vertical");
        return sb.toString();
    }

    @Override
    protected BasicScrollBarUI.TrackListener createTrackListener() {
        return new SubstanceTrackListener();
    }

    @Override
    protected BasicScrollBarUI.ArrowButtonListener createArrowButtonListener() {
        return new SubstanceArrowButtonListener();
    }

    private void updateThumbState(int x2, int y2) {
        Rectangle rect = this.getThumbBounds();
        this.setThumbRollover(rect.contains(x2, y2));
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        if (this.scrollbar.getOrientation() == 1) {
            return new Dimension(this.scrollBarWidth, Math.max(48, 5 * this.scrollBarWidth));
        }
        return new Dimension(Math.max(48, 5 * this.scrollBarWidth), this.scrollBarWidth);
    }

    private class CompositeButtonModel
    extends DefaultButtonModel {
        protected ButtonModel primaryModel;
        protected ButtonModel[] secondaryModels;
        protected ChangeListener listener;

        public CompositeButtonModel(ButtonModel primaryModel, AbstractButton ... secondaryButtons) {
            this.primaryModel = primaryModel;
            LinkedList<ButtonModel> bmList = new LinkedList<ButtonModel>();
            for (AbstractButton secondary : secondaryButtons) {
                if (secondary == null) continue;
                bmList.add(secondary.getModel());
            }
            this.secondaryModels = bmList.toArray(new ButtonModel[0]);
            this.listener = new ChangeListener(){

                @Override
                public void stateChanged(ChangeEvent e2) {
                    CompositeButtonModel.this.syncModels();
                }
            };
            this.syncModels();
        }

        private void syncModels() {
            this.setEnabled(this.primaryModel.isEnabled());
            this.setSelected(this.primaryModel.isSelected());
            boolean isArmed = this.primaryModel.isArmed();
            for (ButtonModel secondary : this.secondaryModels) {
                isArmed = isArmed || secondary.isArmed();
            }
            this.setArmed(isArmed);
            boolean isPressed = this.primaryModel.isPressed();
            for (ButtonModel secondary : this.secondaryModels) {
                isPressed = isPressed || secondary.isPressed();
            }
            this.setPressed(isPressed);
            boolean isRollover = this.primaryModel.isRollover();
            for (ButtonModel secondary : this.secondaryModels) {
                isRollover = isRollover || secondary.isRollover();
            }
            this.setRollover(isRollover);
        }

        public void registerListeners() {
            this.primaryModel.addChangeListener(this.listener);
            for (ButtonModel secondary : this.secondaryModels) {
                secondary.addChangeListener(this.listener);
            }
        }

        public void unregisterListeners() {
            this.primaryModel.removeChangeListener(this.listener);
            for (ButtonModel secondary : this.secondaryModels) {
                secondary.removeChangeListener(this.listener);
            }
            this.listener = null;
        }
    }

    protected class SubstanceArrowButtonListener
    extends BasicScrollBarUI.ArrowButtonListener {
        boolean handledEvent;

        protected SubstanceArrowButtonListener() {
            super(SubstanceScrollBarUI.this);
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            if (!SubstanceScrollBarUI.this.scrollbar.isEnabled()) {
                return;
            }
            if (!SwingUtilities.isLeftMouseButton(e2)) {
                return;
            }
            int direction = e2.getSource() == SubstanceScrollBarUI.this.incrButton || e2.getSource() == SubstanceScrollBarUI.this.mySecondIncreaseButton ? 1 : -1;
            SubstanceScrollBarUI.this.scrollByUnit(direction);
            SubstanceScrollBarUI.this.scrollTimer.stop();
            SubstanceScrollBarUI.this.scrollListener.setDirection(direction);
            SubstanceScrollBarUI.this.scrollListener.setScrollByBlock(false);
            SubstanceScrollBarUI.this.scrollTimer.start();
            this.handledEvent = true;
            if (!SubstanceScrollBarUI.this.scrollbar.hasFocus() && SubstanceScrollBarUI.this.scrollbar.isRequestFocusEnabled()) {
                SubstanceScrollBarUI.this.scrollbar.requestFocus();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
            SubstanceScrollBarUI.this.scrollTimer.stop();
            this.handledEvent = false;
            SubstanceScrollBarUI.this.scrollbar.setValueIsAdjusting(false);
        }
    }

    protected class SubstanceTrackListener
    extends BasicScrollBarUI.TrackListener {
        private transient int direction;

        protected SubstanceTrackListener() {
            super(SubstanceScrollBarUI.this);
            this.direction = 1;
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
            if (SubstanceScrollBarUI.this.isDragging) {
                SubstanceScrollBarUI.this.updateThumbState(e2.getX(), e2.getY());
            }
            if (SwingUtilities.isRightMouseButton(e2) || !SubstanceScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e2)) {
                return;
            }
            if (!SubstanceScrollBarUI.this.scrollbar.isEnabled()) {
                return;
            }
            Rectangle r2 = SubstanceScrollBarUI.this.getTrackBounds();
            SubstanceScrollBarUI.this.scrollbar.repaint(r2.x, r2.y, r2.width, r2.height);
            SubstanceScrollBarUI.this.trackHighlight = 0;
            SubstanceScrollBarUI.this.isDragging = false;
            this.offset = 0;
            SubstanceScrollBarUI.this.scrollTimer.stop();
            SubstanceScrollBarUI.this.scrollbar.setValueIsAdjusting(false);
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            if (SwingUtilities.isRightMouseButton(e2) || !SubstanceScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e2)) {
                return;
            }
            if (!SubstanceScrollBarUI.this.scrollbar.isEnabled()) {
                return;
            }
            if (!SubstanceScrollBarUI.this.scrollbar.hasFocus() && SubstanceScrollBarUI.this.scrollbar.isRequestFocusEnabled()) {
                SubstanceScrollBarUI.this.scrollbar.requestFocus();
            }
            SubstanceScrollBarUI.this.scrollbar.setValueIsAdjusting(true);
            this.currentMouseX = e2.getX();
            this.currentMouseY = e2.getY();
            if (SubstanceScrollBarUI.this.getThumbBounds().contains(this.currentMouseX, this.currentMouseY)) {
                switch (SubstanceScrollBarUI.this.scrollbar.getOrientation()) {
                    case 1: {
                        this.offset = this.currentMouseY - ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().y;
                        break;
                    }
                    case 0: {
                        this.offset = this.currentMouseX - ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().x;
                    }
                }
                SubstanceScrollBarUI.this.isDragging = true;
                return;
            }
            if (SubstanceScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e2)) {
                switch (SubstanceScrollBarUI.this.scrollbar.getOrientation()) {
                    case 1: {
                        this.offset = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().height / 2;
                        break;
                    }
                    case 0: {
                        this.offset = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().width / 2;
                    }
                }
                SubstanceScrollBarUI.this.isDragging = true;
                this.setValueFrom(e2);
                return;
            }
            SubstanceScrollBarUI.this.isDragging = false;
            Dimension sbSize = SubstanceScrollBarUI.this.scrollbar.getSize();
            this.direction = 1;
            switch (SubstanceScrollBarUI.this.scrollbar.getOrientation()) {
                case 1: {
                    if (SubstanceScrollBarUI.this.getThumbBounds().isEmpty()) {
                        int scrollbarCenter = sbSize.height / 2;
                        this.direction = this.currentMouseY < scrollbarCenter ? -1 : 1;
                        break;
                    }
                    int thumbY = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().y;
                    this.direction = this.currentMouseY < thumbY ? -1 : 1;
                    break;
                }
                case 0: {
                    if (SubstanceScrollBarUI.this.getThumbBounds().isEmpty()) {
                        int scrollbarCenter = sbSize.width / 2;
                        this.direction = this.currentMouseX < scrollbarCenter ? -1 : 1;
                    } else {
                        int thumbX = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().x;
                        int n2 = this.direction = this.currentMouseX < thumbX ? -1 : 1;
                    }
                    if (SubstanceScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) break;
                    this.direction = -this.direction;
                }
            }
            SubstanceScrollBarUI.this.scrollByBlock(this.direction);
            SubstanceScrollBarUI.this.scrollTimer.stop();
            SubstanceScrollBarUI.this.scrollListener.setDirection(this.direction);
            SubstanceScrollBarUI.this.scrollListener.setScrollByBlock(true);
            this.startScrollTimerIfNecessary();
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            if (SwingUtilities.isRightMouseButton(e2) || !SubstanceScrollBarUI.this.getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e2)) {
                return;
            }
            if (!SubstanceScrollBarUI.this.scrollbar.isEnabled() || SubstanceScrollBarUI.this.getThumbBounds().isEmpty()) {
                return;
            }
            if (SubstanceScrollBarUI.this.isDragging) {
                this.setValueFrom(e2);
            } else {
                this.currentMouseX = e2.getX();
                this.currentMouseY = e2.getY();
                SubstanceScrollBarUI.this.updateThumbState(this.currentMouseX, this.currentMouseY);
                this.startScrollTimerIfNecessary();
            }
        }

        private void setValueFrom(MouseEvent e2) {
            int thumbPos;
            boolean active = SubstanceScrollBarUI.this.isThumbRollover();
            BoundedRangeModel model = SubstanceScrollBarUI.this.scrollbar.getModel();
            Rectangle thumbR = SubstanceScrollBarUI.this.getThumbBounds();
            int thumbMin = 0;
            int thumbMax = 0;
            SubstanceConstants.ScrollPaneButtonPolicyKind buttonPolicy = SubstanceCoreUtilities.getScrollPaneButtonsPolicyKind(SubstanceScrollBarUI.this.scrollbar);
            if (SubstanceScrollBarUI.this.scrollbar.getOrientation() == 1) {
                switch (buttonPolicy) {
                    case OPPOSITE: {
                        thumbMin = SubstanceScrollBarUI.this.decrButton.getY() + SubstanceScrollBarUI.this.decrButton.getHeight();
                        thumbMax = SubstanceScrollBarUI.this.incrButton.getY() - thumbR.height;
                        break;
                    }
                    case ADJACENT: {
                        thumbMin = 0;
                        thumbMax = SubstanceScrollBarUI.this.mySecondDecreaseButton.getY() - thumbR.height;
                        break;
                    }
                    case NONE: {
                        thumbMin = 0;
                        thumbMax = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getSize().height - ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getInsets().bottom - thumbR.height;
                        break;
                    }
                    case MULTIPLE: {
                        thumbMin = SubstanceScrollBarUI.this.decrButton.getY() + SubstanceScrollBarUI.this.decrButton.getHeight();
                        thumbMax = SubstanceScrollBarUI.this.mySecondDecreaseButton.getY() - thumbR.height;
                        break;
                    }
                    case MULTIPLE_BOTH: {
                        thumbMin = SubstanceScrollBarUI.this.mySecondIncreaseButton.getY() + SubstanceScrollBarUI.this.mySecondIncreaseButton.getHeight();
                        thumbMax = SubstanceScrollBarUI.this.mySecondDecreaseButton.getY() - thumbR.height;
                    }
                }
                thumbPos = Math.min(thumbMax, Math.max(thumbMin, e2.getY() - this.offset));
                SubstanceScrollBarUI.this.setThumbBounds(thumbR.x, thumbPos, thumbR.width, thumbR.height);
            } else {
                if (SubstanceScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
                    switch (buttonPolicy) {
                        case OPPOSITE: {
                            thumbMin = SubstanceScrollBarUI.this.decrButton.getX() + SubstanceScrollBarUI.this.decrButton.getWidth();
                            thumbMax = SubstanceScrollBarUI.this.incrButton.getX() - thumbR.width;
                            break;
                        }
                        case ADJACENT: {
                            thumbMin = 0;
                            thumbMax = SubstanceScrollBarUI.this.mySecondDecreaseButton.getX() - thumbR.width;
                            break;
                        }
                        case MULTIPLE: {
                            thumbMin = SubstanceScrollBarUI.this.decrButton.getX() + SubstanceScrollBarUI.this.decrButton.getWidth();
                            thumbMax = SubstanceScrollBarUI.this.mySecondDecreaseButton.getX() - thumbR.width;
                            break;
                        }
                        case MULTIPLE_BOTH: {
                            thumbMin = SubstanceScrollBarUI.this.mySecondIncreaseButton.getX() + SubstanceScrollBarUI.this.mySecondIncreaseButton.getWidth();
                            thumbMax = SubstanceScrollBarUI.this.mySecondDecreaseButton.getX() - thumbR.width;
                            break;
                        }
                        case NONE: {
                            thumbMin = 0;
                            thumbMax = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getSize().width - ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getInsets().right - thumbR.width;
                        }
                    }
                } else {
                    switch (buttonPolicy) {
                        case OPPOSITE: {
                            thumbMin = SubstanceScrollBarUI.this.incrButton.getX() + SubstanceScrollBarUI.this.incrButton.getWidth();
                            thumbMax = SubstanceScrollBarUI.this.decrButton.getX() - thumbR.width;
                            break;
                        }
                        case ADJACENT: {
                            thumbMin = SubstanceScrollBarUI.this.mySecondDecreaseButton.getX() + SubstanceScrollBarUI.this.mySecondDecreaseButton.getWidth();
                            thumbMax = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getSize().width - ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getInsets().right - thumbR.width;
                            break;
                        }
                        case MULTIPLE: {
                            thumbMin = SubstanceScrollBarUI.this.mySecondDecreaseButton.getX() + SubstanceScrollBarUI.this.mySecondDecreaseButton.getWidth();
                            thumbMax = SubstanceScrollBarUI.this.decrButton.getX() - thumbR.width;
                            break;
                        }
                        case MULTIPLE_BOTH: {
                            thumbMin = SubstanceScrollBarUI.this.mySecondDecreaseButton.getX() + SubstanceScrollBarUI.this.mySecondDecreaseButton.getWidth();
                            thumbMax = SubstanceScrollBarUI.this.mySecondIncreaseButton.getX() - thumbR.width;
                            break;
                        }
                        case NONE: {
                            thumbMin = 0;
                            thumbMax = ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getSize().width - ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).scrollbar.getInsets().right - thumbR.width;
                        }
                    }
                }
                thumbPos = Math.min(thumbMax, Math.max(thumbMin, e2.getX() - this.offset));
                SubstanceScrollBarUI.this.setThumbBounds(thumbPos, thumbR.y, thumbR.width, thumbR.height);
            }
            if (thumbPos == thumbMax) {
                if (SubstanceScrollBarUI.this.scrollbar.getOrientation() == 1 || SubstanceScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
                    SubstanceScrollBarUI.this.scrollbar.setValue(model.getMaximum() - model.getExtent());
                } else {
                    SubstanceScrollBarUI.this.scrollbar.setValue(model.getMinimum());
                }
            } else {
                float valueMax = model.getMaximum() - model.getExtent();
                float valueRange = valueMax - (float)model.getMinimum();
                float thumbValue = thumbPos - thumbMin;
                float thumbRange = thumbMax - thumbMin;
                int value = SubstanceScrollBarUI.this.scrollbar.getOrientation() == 1 || SubstanceScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight() ? (int)(0.5 + (double)(thumbValue / thumbRange * valueRange)) : (int)(0.5 + (double)((float)(thumbMax - thumbPos) / thumbRange * valueRange));
                SubstanceScrollBarUI.this.scrollbar.setValue(value + model.getMinimum());
            }
            SubstanceScrollBarUI.this.setThumbRollover(active);
        }

        private void startScrollTimerIfNecessary() {
            if (SubstanceScrollBarUI.this.scrollTimer.isRunning()) {
                return;
            }
            switch (SubstanceScrollBarUI.this.scrollbar.getOrientation()) {
                case 1: {
                    if (this.direction > 0) {
                        if (((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().y + ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().height >= ((SubstanceTrackListener)((SubstanceScrollBarUI)SubstanceScrollBarUI.this).trackListener).currentMouseY) break;
                        SubstanceScrollBarUI.this.scrollTimer.start();
                        break;
                    }
                    if (((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().y <= ((SubstanceTrackListener)((SubstanceScrollBarUI)SubstanceScrollBarUI.this).trackListener).currentMouseY) break;
                    SubstanceScrollBarUI.this.scrollTimer.start();
                    break;
                }
                case 0: {
                    if (this.direction > 0) {
                        if (((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().x + ((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().width >= ((SubstanceTrackListener)((SubstanceScrollBarUI)SubstanceScrollBarUI.this).trackListener).currentMouseX) break;
                        SubstanceScrollBarUI.this.scrollTimer.start();
                        break;
                    }
                    if (((SubstanceScrollBarUI)SubstanceScrollBarUI.this).getThumbBounds().x <= ((SubstanceTrackListener)((SubstanceScrollBarUI)SubstanceScrollBarUI.this).trackListener).currentMouseX) break;
                    SubstanceScrollBarUI.this.scrollTimer.start();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
            if (!SubstanceScrollBarUI.this.isDragging) {
                SubstanceScrollBarUI.this.updateThumbState(e2.getX(), e2.getY());
            }
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            if (!SubstanceScrollBarUI.this.isDragging) {
                SubstanceScrollBarUI.this.setThumbRollover(false);
            }
        }
    }
}

