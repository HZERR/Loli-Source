/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.api.tabbed.BaseTabCloseListener;
import org.pushingpixels.substance.api.tabbed.MultipleTabCloseListener;
import org.pushingpixels.substance.api.tabbed.TabCloseCallback;
import org.pushingpixels.substance.api.tabbed.TabCloseListener;
import org.pushingpixels.substance.api.tabbed.VetoableMultipleTabCloseListener;
import org.pushingpixels.substance.api.tabbed.VetoableTabCloseListener;
import org.pushingpixels.substance.internal.animation.StateTransitionMultiTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollButton;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class SubstanceTabbedPaneUI
extends BasicTabbedPaneUI {
    protected Set lafWidgets;
    protected Point substanceMouseLocation;
    private static LazyResettableHashMap<BufferedImage> backgroundMap = new LazyResettableHashMap("SubstanceTabbedPaneUI.background");
    private static LazyResettableHashMap<BufferedImage> closeButtonMap = new LazyResettableHashMap("SubstanceTabbedPaneUI.closeButton");
    private Map<Component, Timeline> modifiedTimelines;
    private int currSelectedIndex = -1;
    private StateTransitionMultiTracker<Integer> stateTransitionMultiTracker = new StateTransitionMultiTracker();
    protected MouseRolloverHandler substanceRolloverHandler;
    protected TabbedContainerListener substanceContainerListener;
    protected ChangeListener substanceSelectionListener;
    private boolean substanceContentOpaque;

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installComponents() {
        super.installComponents();
    }

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallComponents() {
        super.uninstallComponents();
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceTabbedPaneUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installListeners() {
        super.installListeners();
        this.substanceRolloverHandler = new MouseRolloverHandler();
        this.tabPane.addMouseMotionListener(this.substanceRolloverHandler);
        this.tabPane.addMouseListener(this.substanceRolloverHandler);
        this.substanceContainerListener = new TabbedContainerListener();
        this.substanceContainerListener.trackExistingTabs();
        for (int i2 = 0; i2 < this.tabPane.getTabCount(); ++i2) {
            Component tabComp = this.tabPane.getComponentAt(i2);
            if (!SubstanceCoreUtilities.isTabModified(tabComp)) continue;
            this.trackTabModification(i2, tabComp);
        }
        this.tabPane.addContainerListener(this.substanceContainerListener);
        this.substanceSelectionListener = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        StateTransitionTracker tracker;
                        if (SubstanceTabbedPaneUI.this.tabPane == null) {
                            return;
                        }
                        int selected = SubstanceTabbedPaneUI.this.tabPane.getSelectedIndex();
                        if (SubstanceTabbedPaneUI.this.currSelectedIndex >= 0 && SubstanceTabbedPaneUI.this.currSelectedIndex < SubstanceTabbedPaneUI.this.tabPane.getTabCount() && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(SubstanceTabbedPaneUI.this.currSelectedIndex)) {
                            tracker = SubstanceTabbedPaneUI.this.getTracker(SubstanceTabbedPaneUI.this.currSelectedIndex, SubstanceTabbedPaneUI.this.getRolloverTabIndex() == SubstanceTabbedPaneUI.this.currSelectedIndex, true);
                            tracker.getModel().setSelected(false);
                        }
                        SubstanceTabbedPaneUI.this.currSelectedIndex = selected;
                        if (selected >= 0 && selected < SubstanceTabbedPaneUI.this.tabPane.getTabCount() && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(selected)) {
                            tracker = SubstanceTabbedPaneUI.this.getTracker(selected, SubstanceTabbedPaneUI.this.getRolloverTabIndex() == selected, false);
                            tracker.getModel().setSelected(true);
                        }
                    }
                });
            }
        };
        this.tabPane.getModel().addChangeListener(this.substanceSelectionListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallListeners() {
        super.uninstallListeners();
        if (this.substanceRolloverHandler != null) {
            this.tabPane.removeMouseMotionListener(this.substanceRolloverHandler);
            this.tabPane.removeMouseListener(this.substanceRolloverHandler);
            this.substanceRolloverHandler = null;
        }
        if (this.substanceContainerListener != null) {
            for (Map.Entry entry : this.substanceContainerListener.listeners.entrySet()) {
                Component comp = (Component)entry.getKey();
                for (PropertyChangeListener pcl : (List)entry.getValue()) {
                    comp.removePropertyChangeListener(pcl);
                }
            }
            this.substanceContainerListener.listeners.clear();
            this.tabPane.removeContainerListener(this.substanceContainerListener);
            this.substanceContainerListener = null;
        }
        this.tabPane.getModel().removeChangeListener(this.substanceSelectionListener);
        this.substanceSelectionListener = null;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__installDefaults() {
        super.installDefaults();
        this.substanceContentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
        this.modifiedTimelines = new HashMap<Component, Timeline>();
        this.currSelectedIndex = this.tabPane.getSelectedIndex();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTabbedPaneUI__uninstallDefaults() {
        for (Timeline timeline : this.modifiedTimelines.values()) {
            timeline.cancel();
        }
        this.modifiedTimelines.clear();
        super.uninstallDefaults();
    }

    private static BufferedImage getTabBackground(JTabbedPane tabPane, int width, int height, int tabPlacement, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme, boolean paintOnlyBorder) {
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(tabPane);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(tabPane);
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(tabPane);
        int borderDelta = (int)Math.ceil(2.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(tabPane)));
        int borderInsets = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(tabPane)) / 2.0);
        int dy = 2 + borderDelta;
        EnumSet<SubstanceConstants.Side> straightSides = EnumSet.of(SubstanceConstants.Side.BOTTOM);
        int cornerRadius = height / 3;
        if (shaper instanceof ClassicButtonShaper) {
            cornerRadius = (int)SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(tabPane));
            if (tabPlacement == 1 || tabPlacement == 3) {
                --width;
            } else {
                --height;
            }
        }
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height + dy, (float)cornerRadius, straightSides, borderInsets);
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D resGraphics = result.createGraphics();
        if (!paintOnlyBorder) {
            fillPainter.paintContourBackground(resGraphics, tabPane, width, height + dy, contour, false, fillScheme, true);
        }
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(tabPane));
        GeneralPath contourInner = borderPainter.isPaintingInnerContour() ? SubstanceOutlineUtilities.getBaseOutline(width, height + dy, (float)(cornerRadius - borderThickness), straightSides, borderThickness + borderInsets) : null;
        borderPainter.paintBorder(resGraphics, tabPane, width, height + dy, contour, contourInner, borderScheme);
        resGraphics.dispose();
        return result;
    }

    private static BufferedImage getFinalTabBackgroundImage(JTabbedPane tabPane, int tabIndex, int x2, int y2, int width, int height, boolean isSelected, int tabPlacement, SubstanceConstants.Side side, SubstanceColorScheme colorScheme, SubstanceColorScheme borderScheme) {
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(tabPane);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(tabPane);
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(tabPane);
        Component compForBackground = tabPane.getTabComponentAt(tabIndex);
        if (compForBackground == null) {
            compForBackground = tabPane.getComponentAt(tabIndex);
        }
        if (compForBackground == null) {
            compForBackground = tabPane;
        }
        Color tabColor = compForBackground.getBackground();
        if (isSelected && tabColor instanceof UIResource) {
            tabColor = SubstanceColorUtilities.getBackgroundFillColor(compForBackground);
        }
        HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, isSelected, tabPlacement, fillPainter.getDisplayName(), borderPainter.getDisplayName(), shaper.getDisplayName(), tabPlacement == 3, side.name(), colorScheme.getDisplayName(), borderScheme.getDisplayName(), tabColor);
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(tabPane);
        BufferedImage result = backgroundMap.get(key);
        if (result == null) {
            BufferedImage backgroundImage = null;
            switch (tabPlacement) {
                case 3: {
                    return SubstanceImageCreator.getRotated(SubstanceTabbedPaneUI.getFinalTabBackgroundImage(tabPane, tabIndex, x2, y2, width, height, isSelected, 1, side, colorScheme, borderScheme), 2);
                }
                case 1: 
                case 2: 
                case 4: {
                    backgroundImage = SubstanceTabbedPaneUI.getTabBackground(tabPane, width, height, 1, colorScheme, borderScheme, false);
                    if (!isSelected) break;
                    int fw = backgroundImage.getWidth();
                    int fh = backgroundImage.getHeight();
                    BufferedImage fade = SubstanceCoreUtilities.getBlankImage(fw, fh);
                    Graphics2D fadeGraphics = fade.createGraphics();
                    fadeGraphics.setColor(tabColor);
                    fadeGraphics.fillRect(0, 0, fw, fh);
                    if (skin.getWatermark() != null) {
                        fadeGraphics.translate(-x2, -y2);
                        skin.getWatermark().drawWatermarkImage(fadeGraphics, tabPane, x2, y2, fw, fh);
                        fadeGraphics.translate(x2, y2);
                    }
                    fadeGraphics.drawImage((Image)SubstanceTabbedPaneUI.getTabBackground(tabPane, width, height, tabPlacement, colorScheme, borderScheme, true), 0, 0, null);
                    backgroundImage = SubstanceCoreUtilities.blendImagesVertical(backgroundImage, fade, skin.getSelectedTabFadeStart(), skin.getSelectedTabFadeEnd());
                }
            }
            backgroundMap.put(key, backgroundImage);
        }
        return backgroundMap.get(key);
    }

    private static BufferedImage getCloseButtonImage(JTabbedPane tabPane, int width, int height, boolean toPaintBorder, SubstanceColorScheme fillScheme, SubstanceColorScheme markScheme) {
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(tabPane);
        if (fillPainter == null) {
            return null;
        }
        HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, toPaintBorder, fillPainter.getDisplayName(), fillScheme.getDisplayName(), markScheme.getDisplayName());
        BufferedImage result = closeButtonMap.get(key);
        if (result == null) {
            result = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D finalGraphics = (Graphics2D)result.getGraphics();
            if (toPaintBorder) {
                GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, 1.0f, null);
                fillPainter.paintContourBackground(finalGraphics, tabPane, width, height, contour, false, fillScheme, true);
                SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(tabPane);
                finalGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                borderPainter.paintBorder(finalGraphics, tabPane, width, height, contour, null, markScheme);
            }
            finalGraphics.setStroke(new BasicStroke(SubstanceSizeUtils.getTabCloseButtonStrokeWidth(SubstanceSizeUtils.getComponentFontSize(tabPane))));
            int delta = (int)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(tabPane)));
            if (delta % 2 != 0) {
                --delta;
            }
            int iconSize = width - delta;
            Icon closeIcon = SubstanceImageCreator.getCloseIcon(iconSize, markScheme, markScheme);
            closeIcon.paintIcon(tabPane, finalGraphics, delta / 2, delta / 2);
            closeButtonMap.put(key, result);
        }
        return result;
    }

    @Override
    protected void paintTabBackground(Graphics g2, int tabPlacement, int tabIndex, int x2, int y2, int w2, int h2, boolean isSelected) {
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.tabPane, g2));
        boolean isEnabled = this.tabPane.isEnabledAt(tabIndex);
        ComponentState currState = this.getTabState(tabIndex);
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(tabIndex);
        SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB_BORDER, currState);
        SubstanceColorScheme baseColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB, currState);
        BufferedImage fullOpacity = null;
        Component comp = this.tabPane.getComponentAt(tabIndex);
        boolean isWindowModified = SubstanceCoreUtilities.isTabModified(comp);
        boolean toMarkModifiedCloseButton = SubstanceCoreUtilities.toAnimateCloseIconOfModifiedTab(this.tabPane, tabIndex);
        if (isWindowModified && isEnabled && !toMarkModifiedCloseButton) {
            SubstanceColorScheme colorScheme2 = SubstanceColorSchemeUtilities.YELLOW;
            SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.ORANGE;
            float cyclePos = this.modifiedTimelines.get(comp).getTimelinePosition();
            BufferedImage layer1 = SubstanceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane, tabIndex, x2, y2, w2, h2, isSelected, tabPlacement, SubstanceConstants.Side.BOTTOM, colorScheme, baseBorderScheme);
            BufferedImage layer2 = SubstanceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane, tabIndex, x2, y2, w2, h2, isSelected, tabPlacement, SubstanceConstants.Side.BOTTOM, colorScheme2, baseBorderScheme);
            fullOpacity = SubstanceCoreUtilities.getBlankImage(w2, h2);
            Graphics2D g2d = fullOpacity.createGraphics();
            if (cyclePos < 1.0f) {
                g2d.drawImage((Image)layer1, 0, 0, null);
            }
            if (cyclePos > 0.0f) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(cyclePos));
                g2d.drawImage((Image)layer2, 0, 0, null);
            }
            g2d.dispose();
        } else {
            BufferedImage layerBase = SubstanceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane, tabIndex, x2, y2, w2, h2, isSelected, tabPlacement, SubstanceConstants.Side.BOTTOM, baseColorScheme, baseBorderScheme);
            if (modelStateInfo == null || currState.isDisabled() || modelStateInfo.getStateContributionMap().size() == 1) {
                fullOpacity = layerBase;
            } else {
                fullOpacity = SubstanceCoreUtilities.getBlankImage(w2, h2);
                Graphics2D g2d = fullOpacity.createGraphics();
                g2d.drawImage((Image)layerBase, 0, 0, null);
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                    float stateContribution;
                    ComponentState activeState = activeEntry.getKey();
                    if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
                    g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
                    SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB, activeState);
                    SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB_BORDER, activeState);
                    BufferedImage layer = SubstanceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane, tabIndex, x2, y2, w2, h2, isSelected, tabPlacement, SubstanceConstants.Side.BOTTOM, fillScheme, borderScheme);
                    g2d.drawImage((Image)layer, 0, 0, null);
                }
            }
        }
        SubstanceColorScheme baseMarkScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.MARK, currState);
        graphics.clip(new Rectangle(x2, y2, w2, h2));
        boolean isRollover = this.getRolloverTab() == tabIndex;
        float finalAlpha = 0.5f;
        StateTransitionTracker tabTracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)tabIndex);
        if (modelStateInfo != null) {
            finalAlpha += 0.5f * tabTracker.getFacetStrength(ComponentStateFacet.ROLLOVER);
            if (tabTracker.getFacetStrength(ComponentStateFacet.SELECTION) == 1.0f) {
                finalAlpha = 1.0f;
            }
        } else {
            ComponentState tabState = this.getTabState(tabIndex);
            if (tabState.isFacetActive(ComponentStateFacet.ROLLOVER) || tabState.isFacetActive(ComponentStateFacet.SELECTION)) {
                finalAlpha = 1.0f;
            }
        }
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.tabPane, finalAlpha *= SubstanceColorSchemeUtilities.getAlpha(this.tabPane.getComponentAt(tabIndex), currState), g2));
        graphics.drawImage((Image)fullOpacity, x2, y2, null);
        if (SubstanceCoreUtilities.hasCloseButton(this.tabPane, tabIndex) && isEnabled) {
            float alpha;
            float f2 = alpha = isSelected || isRollover ? 1.0f : 0.0f;
            if (!isSelected && tabTracker != null) {
                alpha = tabTracker.getFacetStrength(ComponentStateFacet.ROLLOVER);
            }
            if ((double)alpha > 0.0) {
                graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.tabPane, finalAlpha * alpha, g2));
                Rectangle orig = this.getCloseButtonRectangleForDraw(tabIndex, x2, y2, w2, h2);
                boolean toPaintCloseBorder = false;
                if (isRollover && this.substanceMouseLocation != null) {
                    Rectangle rect;
                    Rectangle bounds = new Rectangle();
                    bounds = this.getTabBounds(tabIndex, bounds);
                    if (this.toRotateTabsOnPlacement(tabPlacement)) {
                        bounds = new Rectangle(bounds.x, bounds.y, bounds.height, bounds.width);
                    }
                    if ((rect = this.getCloseButtonRectangleForEvents(tabIndex, bounds.x, bounds.y, bounds.width, bounds.height)).contains(this.substanceMouseLocation)) {
                        toPaintCloseBorder = true;
                    }
                }
                if (isWindowModified && isEnabled && toMarkModifiedCloseButton) {
                    SubstanceColorScheme colorScheme2 = SubstanceColorSchemeUtilities.YELLOW;
                    SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.ORANGE;
                    float cyclePos = this.modifiedTimelines.get(comp).getTimelinePosition();
                    BufferedImage layer1 = SubstanceTabbedPaneUI.getCloseButtonImage(this.tabPane, orig.width, orig.height, toPaintCloseBorder, colorScheme, baseMarkScheme);
                    BufferedImage layer2 = SubstanceTabbedPaneUI.getCloseButtonImage(this.tabPane, orig.width, orig.height, toPaintCloseBorder, colorScheme2, baseMarkScheme);
                    if (cyclePos < 1.0f) {
                        graphics.drawImage((Image)layer1, orig.x, orig.y, null);
                    }
                    if (cyclePos > 0.0f) {
                        graphics.setComposite(AlphaComposite.SrcOver.derive(cyclePos));
                        graphics.drawImage((Image)layer2, orig.x, orig.y, null);
                    }
                } else {
                    BufferedImage layerBase = SubstanceTabbedPaneUI.getCloseButtonImage(this.tabPane, orig.width, orig.height, toPaintCloseBorder, baseColorScheme, baseMarkScheme);
                    if (modelStateInfo == null || currState.isDisabled() || modelStateInfo.getStateContributionMap().size() == 1) {
                        graphics.drawImage((Image)layerBase, orig.x, orig.y, null);
                    } else {
                        BufferedImage complete = SubstanceCoreUtilities.getBlankImage(orig.width, orig.height);
                        Graphics2D g2d = complete.createGraphics();
                        g2d.drawImage((Image)layerBase, 0, 0, null);
                        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                            float stateContribution;
                            ComponentState activeState = activeEntry.getKey();
                            if (activeState == currState || !((stateContribution = activeEntry.getValue().getContribution()) > 0.0f)) continue;
                            g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
                            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB, activeState);
                            SubstanceColorScheme markScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.MARK, activeState);
                            BufferedImage layer = SubstanceTabbedPaneUI.getCloseButtonImage(this.tabPane, orig.width, orig.height, toPaintCloseBorder, fillScheme, markScheme);
                            g2d.drawImage((Image)layer, 0, 0, null);
                        }
                        g2d.dispose();
                        graphics.drawImage((Image)complete, orig.x, orig.y, null);
                    }
                }
            }
        }
        graphics.dispose();
    }

    @Override
    protected void paintFocusIndicator(Graphics g2, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    }

    @Override
    protected void paintTabBorder(Graphics g2, int tabPlacement, int tabIndex, int x2, int y2, int w2, int h2, boolean isSelected) {
    }

    @Override
    protected JButton createScrollButton(final int direction) {
        SubstanceScrollButton ssb = new SubstanceScrollButton(direction);
        TransitionAwareIcon icon = new TransitionAwareIcon(ssb, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceTabbedPaneUI.this.tabPane);
                return SubstanceImageCreator.getArrowIcon(fontSize, direction, scheme);
            }
        }, "substance.tabbedpane.scroll." + direction);
        ssb.setIcon(icon);
        return ssb;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        boolean toSwap = this.toRotateTabsOnPlacement(tabPlacement);
        if (toSwap) {
            return this.getTabExtraWidth(tabPlacement, tabIndex) + super.calculateTabWidth(tabPlacement, tabIndex, this.getFontMetrics());
        }
        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        boolean toSwap = this.toRotateTabsOnPlacement(tabPlacement);
        if (toSwap) {
            return super.calculateTabHeight(tabPlacement, tabIndex, metrics.getHeight());
        }
        int result = this.getTabExtraWidth(tabPlacement, tabIndex) + super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        return result;
    }

    @Override
    protected int calculateMaxTabHeight(int tabPlacement) {
        if (this.toRotateTabsOnPlacement(tabPlacement)) {
            return super.calculateMaxTabHeight(tabPlacement);
        }
        int result = 0;
        for (int i2 = 0; i2 < this.tabPane.getTabCount(); ++i2) {
            result = Math.max(result, this.calculateTabHeight(tabPlacement, i2, this.getFontMetrics().getHeight()));
        }
        return result;
    }

    @Override
    protected int getTabRunOverlay(int tabPlacement) {
        boolean toSwap = this.toRotateTabsOnPlacement(tabPlacement);
        if (toSwap) {
            return super.getTabRunOverlay(tabPlacement);
        }
        return 0;
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        int selectedIndex = this.tabPane.getSelectedIndex();
        int tabPlacement = this.tabPane.getTabPlacement();
        this.ensureCurrentLayout();
        if (this.tabPane.getLayout().getClass() == TabbedPaneLayout.class) {
            this.paintTabArea(g2, tabPlacement, selectedIndex);
        }
        int width = this.tabPane.getWidth();
        int height = this.tabPane.getHeight();
        Insets insets = this.tabPane.getInsets();
        int x2 = insets.left;
        int y2 = insets.top;
        int w2 = width - insets.right - insets.left;
        int h2 = height - insets.top - insets.bottom;
        switch (tabPlacement) {
            case 2: {
                w2 -= (x2 += this.calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth)) - insets.left;
                break;
            }
            case 4: {
                w2 -= this.calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
                break;
            }
            case 3: {
                h2 -= this.calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
                break;
            }
            default: {
                h2 -= (y2 += this.calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight)) - insets.top;
            }
        }
        Graphics2D g2d = (Graphics2D)g2.create(x2, y2, w2, h2);
        BackgroundPaintingUtils.update(g2d, c2, false);
        this.paintContentBorder(g2, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintTab(Graphics g2, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        boolean toSwap = this.toRotateTabsOnPlacement(tabPlacement);
        if (toSwap) {
            Graphics2D tempG = (Graphics2D)g2.create();
            Rectangle tabRect = rects[tabIndex];
            Rectangle correctRect = new Rectangle(tabRect.x, tabRect.y, tabRect.height, tabRect.width);
            if (tabPlacement == 2) {
                tempG.rotate(-1.5707963267948966, tabRect.x, tabRect.y);
                tempG.translate(-tabRect.height, 0);
            } else {
                tempG.rotate(1.5707963267948966, tabRect.x, tabRect.y);
                tempG.translate(0.0, -tabRect.getWidth());
            }
            tempG.setColor(Color.red);
            rects[tabIndex] = correctRect;
            super.paintTab(tempG, tabPlacement, rects, tabIndex, iconRect, textRect);
            rects[tabIndex] = tabRect;
            tempG.dispose();
        } else if (this.tabPane.getLayout().getClass() == TabbedPaneLayout.class) {
            super.paintTab(g2, tabPlacement, rects, tabIndex, iconRect, textRect);
        } else {
            Graphics2D g2d = (Graphics2D)g2.create();
            RenderingUtils.installDesktopHints(g2d, this.tabPane);
            super.paintTab(g2d, tabPlacement, rects, tabIndex, iconRect, textRect);
            g2d.dispose();
        }
    }

    @Override
    protected void paintTabArea(Graphics g2, int tabPlacement, int selectedIndex) {
        if (this.substanceContentOpaque) {
            int width = this.calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
            if (tabPlacement == 1 || tabPlacement == 3) {
                width = Math.max(width, this.tabPane.getWidth());
            }
            int height = this.calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
            if (this.toRotateTabsOnPlacement(tabPlacement)) {
                height = Math.max(height, this.tabPane.getHeight());
            }
            Graphics2D g2d = (Graphics2D)g2.create(0, 0, width, height);
            BackgroundPaintingUtils.update(g2d, this.tabPane, true);
            g2d.dispose();
        }
        super.paintTabArea(g2, tabPlacement, selectedIndex);
    }

    protected Rectangle getCloseButtonRectangleForDraw(int tabIndex, int x2, int y2, int width, int height) {
        int dimension = SubstanceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex);
        int borderDelta = (int)Math.ceil(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)));
        int xs = this.tabPane.getComponentOrientation().isLeftToRight() ? x2 + width - dimension - borderDelta : x2 + borderDelta;
        int ys = y2 + (height - dimension) / 2 + 1;
        return new Rectangle(xs, ys, dimension, dimension);
    }

    protected Rectangle getCloseButtonRectangleForEvents(int tabIndex, int x2, int y2, int w2, int h2) {
        int tabPlacement = this.tabPane.getTabPlacement();
        boolean toSwap = this.toRotateTabsOnPlacement(tabPlacement);
        if (!toSwap) {
            return this.getCloseButtonRectangleForDraw(tabIndex, x2, y2, w2, h2);
        }
        int dimension = SubstanceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex);
        Point2D transCorner = null;
        Rectangle rectForDraw = this.getCloseButtonRectangleForDraw(tabIndex, x2, y2, h2, w2);
        if (tabPlacement == 2) {
            AffineTransform trans = new AffineTransform();
            trans.rotate(-1.5707963267948966, x2, y2);
            trans.translate(-h2, 0.0);
            Point2D.Double origCorner = new Point2D.Double(rectForDraw.getMaxX(), rectForDraw.getMinY());
            transCorner = trans.transform(origCorner, null);
        } else {
            AffineTransform trans = new AffineTransform();
            trans.rotate(1.5707963267948966, x2, y2);
            trans.translate(0.0, -w2);
            Point2D.Double origCorner = new Point2D.Double(rectForDraw.getMinX(), rectForDraw.getMaxY());
            transCorner = trans.transform(origCorner, null);
        }
        return new Rectangle((int)transCorner.getX(), (int)transCorner.getY(), dimension, dimension);
    }

    protected void ensureCurrentLayout() {
        LayoutManager lm;
        if (!this.tabPane.isValid()) {
            this.tabPane.validate();
        }
        if (!this.tabPane.isValid() && (lm = this.tabPane.getLayout()) instanceof BasicTabbedPaneUI.TabbedPaneLayout) {
            BasicTabbedPaneUI.TabbedPaneLayout layout = (BasicTabbedPaneUI.TabbedPaneLayout)lm;
            layout.calculateLayoutInfo();
        }
    }

    protected void tryCloseTabs(int tabIndex, SubstanceConstants.TabCloseKind tabCloseKind) {
        if (tabCloseKind == null) {
            return;
        }
        if (tabCloseKind == SubstanceConstants.TabCloseKind.NONE) {
            return;
        }
        if (tabCloseKind == SubstanceConstants.TabCloseKind.ALL_BUT_THIS) {
            HashSet<Integer> indexes = new HashSet<Integer>();
            for (int i2 = 0; i2 < this.tabPane.getTabCount(); ++i2) {
                if (i2 == tabIndex) continue;
                indexes.add(i2);
            }
            this.tryCloseTabs(indexes);
            return;
        }
        if (tabCloseKind == SubstanceConstants.TabCloseKind.ALL) {
            HashSet<Integer> indexes = new HashSet<Integer>();
            for (int i3 = 0; i3 < this.tabPane.getTabCount(); ++i3) {
                indexes.add(i3);
            }
            this.tryCloseTabs(indexes);
            return;
        }
        this.tryCloseTab(tabIndex);
    }

    protected void tryCloseTab(int tabIndex) {
        Component component = this.tabPane.getComponentAt(tabIndex);
        HashSet<Component> componentSet = new HashSet<Component>();
        componentSet.add(component);
        boolean isVetoed = false;
        for (BaseTabCloseListener listener : SubstanceLookAndFeel.getAllTabCloseListeners(this.tabPane)) {
            BaseTabCloseListener vetoableListener;
            if (listener instanceof VetoableTabCloseListener) {
                vetoableListener = (VetoableTabCloseListener)listener;
                boolean bl = isVetoed = isVetoed || vetoableListener.vetoTabClosing(this.tabPane, component);
            }
            if (!(listener instanceof VetoableMultipleTabCloseListener)) continue;
            vetoableListener = (VetoableMultipleTabCloseListener)listener;
            isVetoed = isVetoed || vetoableListener.vetoTabsClosing(this.tabPane, componentSet);
        }
        if (isVetoed) {
            return;
        }
        for (BaseTabCloseListener listener : SubstanceLookAndFeel.getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof TabCloseListener) {
                ((TabCloseListener)listener).tabClosing(this.tabPane, component);
            }
            if (!(listener instanceof MultipleTabCloseListener)) continue;
            ((MultipleTabCloseListener)listener).tabsClosing(this.tabPane, componentSet);
        }
        this.tabPane.remove(tabIndex);
        if (this.tabPane.getTabCount() > 0) {
            this.selectPreviousTab(0);
            this.selectNextTab(this.tabPane.getSelectedIndex());
        }
        this.tabPane.repaint();
        for (BaseTabCloseListener listener : SubstanceLookAndFeel.getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof TabCloseListener) {
                ((TabCloseListener)listener).tabClosed(this.tabPane, component);
            }
            if (!(listener instanceof MultipleTabCloseListener)) continue;
            ((MultipleTabCloseListener)listener).tabsClosed(this.tabPane, componentSet);
        }
    }

    protected void tryCloseTabs(Set<Integer> tabIndexes) {
        HashSet<Component> componentSet = new HashSet<Component>();
        for (int tabIndex : tabIndexes) {
            componentSet.add(this.tabPane.getComponentAt(tabIndex));
        }
        boolean isVetoed = false;
        for (BaseTabCloseListener listener : SubstanceLookAndFeel.getAllTabCloseListeners(this.tabPane)) {
            if (!(listener instanceof VetoableMultipleTabCloseListener)) continue;
            VetoableMultipleTabCloseListener vetoableListener = (VetoableMultipleTabCloseListener)listener;
            isVetoed = isVetoed || vetoableListener.vetoTabsClosing(this.tabPane, componentSet);
        }
        if (isVetoed) {
            return;
        }
        for (BaseTabCloseListener listener : SubstanceLookAndFeel.getAllTabCloseListeners(this.tabPane)) {
            if (!(listener instanceof MultipleTabCloseListener)) continue;
            ((MultipleTabCloseListener)listener).tabsClosing(this.tabPane, componentSet);
        }
        for (Component toRemove : componentSet) {
            this.tabPane.remove(toRemove);
        }
        if (this.tabPane.getTabCount() > 0) {
            this.selectPreviousTab(0);
            this.selectNextTab(this.tabPane.getSelectedIndex());
        }
        this.tabPane.repaint();
        for (BaseTabCloseListener listener : SubstanceLookAndFeel.getAllTabCloseListeners(this.tabPane)) {
            if (!(listener instanceof MultipleTabCloseListener)) continue;
            ((MultipleTabCloseListener)listener).tabsClosed(this.tabPane, componentSet);
        }
    }

    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        int delta = 0;
        if (SubstanceCoreUtilities.hasCloseButton(this.tabPane, tabIndex)) {
            delta = this.tabPane.getComponentOrientation().isLeftToRight() ? 5 - SubstanceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex) : SubstanceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex) - 5;
        }
        return delta + super.getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
    }

    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        int result = 0;
        result = tabPlacement == 3 ? -1 : 1;
        return result;
    }

    protected int getTabExtraWidth(int tabPlacement, int tabIndex) {
        int extraWidth = 0;
        SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(this.tabPane);
        extraWidth = shaper instanceof ClassicButtonShaper ? (int)(2.0 * (double)SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(this.tabPane))) : super.calculateTabHeight(tabPlacement, tabIndex, this.getFontMetrics().getHeight()) / 3;
        if (SubstanceCoreUtilities.hasCloseButton(this.tabPane, tabIndex) && this.tabPane.isEnabledAt(tabIndex)) {
            extraWidth += 4 + SubstanceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex);
        }
        return extraWidth;
    }

    public int getRolloverTabIndex() {
        return this.getRolloverTab();
    }

    public void setTabAreaInsets(Insets insets) {
        Insets old = this.tabAreaInsets;
        this.tabAreaInsets = insets;
        LafWidgetUtilities.firePropertyChangeEvent(this.tabPane, "tabAreaInsets", old, this.tabAreaInsets);
    }

    public Insets getTabAreaInsets() {
        return this.tabAreaInsets;
    }

    public Rectangle getTabRectangle(int tabIndex) {
        return this.rects[tabIndex];
    }

    public static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceTabbedPaneUI: \n");
        sb.append("\t" + backgroundMap.size() + " backgrounds");
        return sb.toString();
    }

    @Override
    protected boolean shouldPadTabRun(int tabPlacement, int run) {
        return this.runCount > 1 && run < this.runCount - 1;
    }

    @Override
    protected LayoutManager createLayoutManager() {
        if (this.tabPane.getTabLayoutPolicy() == 1) {
            return super.createLayoutManager();
        }
        return new TabbedPaneLayout();
    }

    @Override
    protected Insets getContentBorderInsets(int tabPlacement) {
        int delta;
        Insets insets = SubstanceSizeUtils.getTabbedPaneContentInsets(SubstanceSizeUtils.getComponentFontSize(this.tabPane));
        SubstanceConstants.TabContentPaneBorderKind kind = SubstanceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_FULL || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        boolean isPlacement = kind == SubstanceConstants.TabContentPaneBorderKind.SINGLE_PLACEMENT || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        int n2 = delta = isDouble ? (int)(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane))) : 0;
        if (isPlacement) {
            switch (tabPlacement) {
                case 1: {
                    return new Insets(insets.top + delta, 0, 0, 0);
                }
                case 2: {
                    return new Insets(0, insets.left + delta, 0, 0);
                }
                case 4: {
                    return new Insets(0, 0, 0, insets.right + delta);
                }
                case 3: {
                    return new Insets(0, 0, insets.bottom + delta, 0);
                }
            }
        } else {
            switch (tabPlacement) {
                case 1: {
                    return new Insets(insets.top + delta, insets.left, insets.bottom, insets.right);
                }
                case 2: {
                    return new Insets(insets.top, insets.left + delta, insets.bottom, insets.right);
                }
                case 4: {
                    return new Insets(insets.top, insets.left, insets.bottom, insets.right + delta);
                }
                case 3: {
                    return new Insets(insets.top, insets.left, insets.bottom + delta, insets.right);
                }
            }
        }
        return insets;
    }

    @Override
    protected void paintContentBorder(Graphics g2, int tabPlacement, int selectedIndex) {
        SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, selectedIndex, ColorSchemeAssociationKind.TAB, ComponentState.ENABLED);
        this.highlight = scheme.isDark() ? SubstanceColorUtilities.getAlphaColor(scheme.getUltraDarkColor(), 100) : scheme.getLightColor();
        super.paintContentBorder(g2, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintContentBorderBottomEdge(Graphics g2, int tabPlacement, int selectedIndex, int x2, int y2, int w2, int h2) {
        boolean isPlacement;
        SubstanceConstants.TabContentPaneBorderKind kind = SubstanceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_FULL || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        boolean bl = isPlacement = kind == SubstanceConstants.TabContentPaneBorderKind.SINGLE_PLACEMENT || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        if (isPlacement && tabPlacement != 3) {
            return;
        }
        int ribbonDelta = (int)(2.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)));
        Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane));
        int joinKind = 1;
        int capKind = 0;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int)((double)strokeWidth / 2.0);
        boolean isUnbroken = tabPlacement != 3 || selectedIndex < 0 || selRect.y - 1 > h2 || selRect.x < x2 || selRect.x > x2 + w2;
        x2 += offset;
        y2 += offset;
        w2 -= 2 * offset;
        h2 -= 2 * offset;
        SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, selectedIndex, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.SELECTED);
        Color darkShadowColor = SubstanceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x2, y2 + h2 - 1, x2 + w2 - 1, y2 + h2 - 1);
        } else {
            SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = shaper instanceof ClassicButtonShaper ? 1 : 0;
            int borderInsets = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2.0);
            GeneralPath bottomOutline = new GeneralPath();
            bottomOutline.moveTo(x2, y2 + h2 - 1);
            bottomOutline.lineTo(selRect.x + borderInsets, y2 + h2 - 1);
            int bumpHeight = super.calculateTabHeight(tabPlacement, 0, SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            bottomOutline.lineTo(selRect.x + borderInsets, y2 + h2 + bumpHeight);
            if (selRect.x + selRect.width < x2 + w2 - 1) {
                int selectionEndX = selRect.x + selRect.width - delta - 1 - borderInsets;
                bottomOutline.lineTo(selectionEndX, y2 + h2 - 1 + bumpHeight);
                bottomOutline.lineTo(selectionEndX, y2 + h2 - 1);
                bottomOutline.lineTo(x2 + w2 - 1, y2 + h2 - 1);
            }
            g2d.setPaint(new GradientPaint(x2, y2 + h2 - 1, darkShadowColor, x2, y2 + h2 - 1 + bumpHeight, SubstanceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(bottomOutline);
        }
        if (isDouble) {
            if (tabPlacement == 3) {
                g2d.setColor(this.highlight);
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x2, y2 + h2 - 1 - ribbonDelta, x2 + w2 - 1, y2 + h2 - 1 - ribbonDelta);
            }
            if (tabPlacement == 2) {
                g2d.setPaint(new GradientPaint(x2, y2 + h2 - 1, darkShadowColor, x2 + 4 * ribbonDelta, y2 + h2 - 1, this.highlight));
                g2d.drawLine(x2, y2 + h2 - 1, x2 + 4 * ribbonDelta, y2 + h2 - 1);
            }
            if (tabPlacement == 4) {
                g2d.setPaint(new GradientPaint(x2 + w2 - 1 - 4 * ribbonDelta, y2 + h2 - 1, this.highlight, x2 + w2 - 1, y2 + h2 - 1, darkShadowColor));
                g2d.drawLine(x2 + w2 - 1 - 4 * ribbonDelta, y2 + h2 - 1, x2 + w2 - 1, y2 + h2 - 1);
            }
        }
        g2d.dispose();
    }

    @Override
    protected void paintContentBorderLeftEdge(Graphics g2, int tabPlacement, int selectedIndex, int x2, int y2, int w2, int h2) {
        boolean isPlacement;
        SubstanceConstants.TabContentPaneBorderKind kind = SubstanceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_FULL || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        boolean bl = isPlacement = kind == SubstanceConstants.TabContentPaneBorderKind.SINGLE_PLACEMENT || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        if (isPlacement && tabPlacement != 2) {
            return;
        }
        int ribbonDelta = (int)(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)));
        Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane));
        int joinKind = 1;
        int capKind = 0;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int)((double)strokeWidth / 2.0);
        boolean isUnbroken = tabPlacement != 2 || selectedIndex < 0 || selRect.x + selRect.width + 1 < x2 || selRect.y < y2 || selRect.y > y2 + h2;
        x2 += offset;
        y2 += offset;
        h2 -= 2 * offset;
        SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, selectedIndex, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.SELECTED);
        Color darkShadowColor = SubstanceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x2, y2, x2, y2 + h2);
        } else {
            SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = shaper instanceof ClassicButtonShaper ? 1 : 0;
            int borderInsets = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2.0);
            GeneralPath leftOutline = new GeneralPath();
            leftOutline.moveTo(x2, y2);
            leftOutline.lineTo(x2, selRect.y + borderInsets);
            int bumpWidth = super.calculateTabHeight(tabPlacement, 0, SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            leftOutline.lineTo(x2 - bumpWidth, selRect.y + borderInsets);
            if (selRect.y + selRect.height < y2 + h2) {
                int selectionEndY = selRect.y + selRect.height - delta - 1 - borderInsets;
                leftOutline.lineTo(x2 - bumpWidth, selectionEndY);
                leftOutline.lineTo(x2, selectionEndY);
                leftOutline.lineTo(x2, y2 + h2);
            }
            g2d.setPaint(new GradientPaint(x2, y2, darkShadowColor, x2 - bumpWidth, y2, SubstanceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(leftOutline);
        }
        if (isDouble) {
            if (tabPlacement == 2) {
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x2 + ribbonDelta, y2, x2 + ribbonDelta, y2 + h2);
            }
            if (tabPlacement == 1) {
                g2d.setPaint(new GradientPaint(x2, y2, darkShadowColor, x2, y2 + 4 * ribbonDelta, this.highlight));
                g2d.drawLine(x2, y2, x2, y2 + 4 * ribbonDelta);
            }
            if (tabPlacement == 3) {
                g2d.setPaint(new GradientPaint(x2, y2 + h2 - 1 - 4 * ribbonDelta, this.highlight, x2, y2 + h2 - 1, darkShadowColor));
                g2d.drawLine(x2, y2 + h2 - 1 - 4 * ribbonDelta, x2, y2 + h2 - 1);
            }
        }
        g2d.dispose();
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g2, int tabPlacement, int selectedIndex, int x2, int y2, int w2, int h2) {
        boolean isPlacement;
        SubstanceConstants.TabContentPaneBorderKind kind = SubstanceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_FULL || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        boolean bl = isPlacement = kind == SubstanceConstants.TabContentPaneBorderKind.SINGLE_PLACEMENT || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        if (isPlacement && tabPlacement != 4) {
            return;
        }
        int ribbonDelta = (int)(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)));
        Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane));
        int joinKind = 1;
        int capKind = 0;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int)((double)strokeWidth / 2.0);
        boolean isUnbroken = tabPlacement != 4 || selectedIndex < 0 || selRect.x - 1 > w2 || selRect.y < y2 || selRect.y > y2 + h2;
        x2 += offset;
        y2 += offset;
        w2 -= 2 * offset;
        h2 -= 2 * offset;
        SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, selectedIndex, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.SELECTED);
        Color darkShadowColor = SubstanceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x2 + w2 - 1, y2, x2 + w2 - 1, y2 + h2);
        } else {
            SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = shaper instanceof ClassicButtonShaper ? 1 : 0;
            int borderInsets = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2.0);
            GeneralPath rightOutline = new GeneralPath();
            rightOutline.moveTo(x2 + w2 - 1, y2);
            rightOutline.lineTo(x2 + w2 - 1, selRect.y + borderInsets);
            int bumpWidth = super.calculateTabHeight(tabPlacement, 0, SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            rightOutline.lineTo(x2 + w2 - 1 + bumpWidth, selRect.y + borderInsets);
            if (selRect.y + selRect.height < y2 + h2) {
                int selectionEndY = selRect.y + selRect.height - delta - 1 - borderInsets;
                rightOutline.lineTo(x2 + w2 - 1 + bumpWidth, selectionEndY);
                rightOutline.lineTo(x2 + w2 - 1, selectionEndY);
                rightOutline.lineTo(x2 + w2 - 1, y2 + h2);
            }
            g2d.setPaint(new GradientPaint(x2 + w2 - 1, y2, darkShadowColor, x2 + w2 - 1 + bumpWidth, y2, SubstanceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(rightOutline);
        }
        if (isDouble) {
            if (tabPlacement == 4) {
                g2d.setColor(this.highlight);
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x2 + w2 - 1 - ribbonDelta, y2, x2 + w2 - 1 - ribbonDelta, y2 + h2);
            }
            if (tabPlacement == 1) {
                g2d.setPaint(new GradientPaint(x2 + w2 - 1, y2, darkShadowColor, x2 + w2 - 1, y2 + 4 * ribbonDelta, this.highlight));
                g2d.drawLine(x2 + w2 - 1, y2, x2 + w2 - 1, y2 + 4 * ribbonDelta);
            }
            if (tabPlacement == 3) {
                g2d.setPaint(new GradientPaint(x2 + w2 - 1, y2 + h2 - 1 - 4 * ribbonDelta, this.highlight, x2 + w2 - 1, y2 + h2 - 1, darkShadowColor));
                g2d.drawLine(x2 + w2 - 1, y2 + h2 - 1 - 4 * ribbonDelta, x2 + w2 - 1, y2 + h2 - 1);
            }
        }
        g2d.dispose();
    }

    @Override
    protected void paintContentBorderTopEdge(Graphics g2, int tabPlacement, int selectedIndex, int x2, int y2, int w2, int h2) {
        boolean isPlacement;
        SubstanceConstants.TabContentPaneBorderKind kind = SubstanceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_FULL || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        boolean bl = isPlacement = kind == SubstanceConstants.TabContentPaneBorderKind.SINGLE_PLACEMENT || kind == SubstanceConstants.TabContentPaneBorderKind.DOUBLE_PLACEMENT;
        if (isPlacement && tabPlacement != 1) {
            return;
        }
        int ribbonDelta = (int)(3.0 * (double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)));
        Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane));
        int joinKind = 1;
        int capKind = 0;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int)((double)strokeWidth / 2.0);
        boolean isUnbroken = tabPlacement != 1 || selectedIndex < 0 || selRect.y + selRect.height + 1 < y2 || selRect.x < x2 || selRect.x > x2 + w2;
        x2 += offset;
        y2 += offset;
        w2 -= 2 * offset;
        SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, selectedIndex, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.SELECTED);
        Color darkShadowColor = SubstanceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x2, y2, x2 + w2 - 1, y2);
        } else {
            SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = shaper instanceof ClassicButtonShaper ? 1 : 0;
            int borderInsets = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2.0);
            GeneralPath topOutline = new GeneralPath();
            topOutline.moveTo(x2, y2);
            topOutline.lineTo(selRect.x + borderInsets, y2);
            int bumpHeight = super.calculateTabHeight(tabPlacement, 0, SubstanceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            topOutline.lineTo(selRect.x + borderInsets, y2 - bumpHeight);
            if (selRect.x + selRect.width < x2 + w2 - 1) {
                int selectionEndX = selRect.x + selRect.width - delta - 1 - borderInsets;
                topOutline.lineTo(selectionEndX, y2 - bumpHeight);
                topOutline.lineTo(selectionEndX, y2);
                topOutline.lineTo(x2 + w2 - 1, y2);
            }
            g2d.setPaint(new GradientPaint(x2, y2, darkShadowColor, x2, y2 - bumpHeight, SubstanceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(topOutline);
        }
        if (isDouble) {
            if (tabPlacement == 1) {
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x2, y2 + ribbonDelta, x2 + w2 - 1, y2 + ribbonDelta);
                g2d.setColor(this.highlight);
            }
            if (tabPlacement == 2) {
                g2d.setPaint(new GradientPaint(x2, y2, darkShadowColor, x2 + 4 * ribbonDelta, y2, this.highlight));
                g2d.drawLine(x2, y2, x2 + 4 * ribbonDelta, y2);
            }
            if (tabPlacement == 4) {
                g2d.setPaint(new GradientPaint(x2 + w2 - 1 - 4 * ribbonDelta, y2, this.highlight, x2 + w2 - 1, y2, darkShadowColor));
                g2d.drawLine(x2 + w2 - 1 - 4 * ribbonDelta, y2, x2 + w2 - 1, y2);
            }
        }
        g2d.dispose();
    }

    @Override
    public Rectangle getTabBounds(JTabbedPane pane, int i2) {
        this.ensureCurrentLayout();
        Rectangle tabRect = new Rectangle();
        return this.getTabBounds(i2, tabRect);
    }

    protected StateTransitionTracker.ModelStateInfo getModelStateInfo(int tabIndex) {
        if (this.stateTransitionMultiTracker.size() == 0) {
            return null;
        }
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)tabIndex);
        if (tracker == null) {
            return null;
        }
        return tracker.getModelStateInfo();
    }

    protected ComponentState getTabState(int tabIndex) {
        boolean isEnabled = this.tabPane.isEnabledAt(tabIndex);
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)tabIndex);
        if (tracker == null) {
            boolean isRollover = this.getRolloverTabIndex() == tabIndex;
            boolean isSelected = this.tabPane.getSelectedIndex() == tabIndex;
            return ComponentState.getState(isEnabled, isRollover, isSelected);
        }
        ComponentState fromTracker = tracker.getModelStateInfo().getCurrModelState();
        return ComponentState.getState(isEnabled, fromTracker.isFacetActive(ComponentStateFacet.ROLLOVER), fromTracker.isFacetActive(ComponentStateFacet.SELECTION));
    }

    @Override
    protected void paintText(Graphics g2, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        g2.setFont(font);
        View v2 = this.getTextViewForTab(tabIndex);
        if (v2 != null) {
            v2.paint(g2, textRect);
        } else {
            int mnemIndex = this.tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(tabIndex);
            ComponentState currState = this.getTabState(tabIndex);
            Color fg = null;
            if (modelStateInfo != null) {
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB, currState);
                if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
                    fg = colorScheme.getForegroundColor();
                } else {
                    float aggrRed = 0.0f;
                    float aggrGreen = 0.0f;
                    float aggrBlue = 0.0f;
                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                        ComponentState activeState = activeEntry.getKey();
                        SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB, activeState);
                        Color schemeFg = scheme.getForegroundColor();
                        float contribution = activeEntry.getValue().getContribution();
                        aggrRed += (float)schemeFg.getRed() * contribution;
                        aggrGreen += (float)schemeFg.getGreen() * contribution;
                        aggrBlue += (float)schemeFg.getBlue() * contribution;
                    }
                    fg = new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue);
                }
            } else {
                SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.tabPane, tabIndex, ColorSchemeAssociationKind.TAB, currState);
                fg = scheme.getForegroundColor();
            }
            Graphics2D graphics = (Graphics2D)g2.create();
            if (currState.isDisabled()) {
                Color bgFillColor = SubstanceColorUtilities.getBackgroundFillColor(this.tabPane);
                fg = SubstanceColorUtilities.getInterpolatedColor(fg, bgFillColor, SubstanceColorSchemeUtilities.getAlpha(this.tabPane.getComponentAt(tabIndex), currState));
            }
            graphics.clip(this.getTabRectangle(tabIndex));
            SubstanceTextUtilities.paintText(graphics, this.tabPane, textRect, title, mnemIndex, graphics.getFont(), fg, null);
            graphics.dispose();
        }
    }

    @Override
    protected void paintIcon(Graphics g2, int tabPlacement, int tabIndex, Icon icon, Rectangle iconRect, boolean isSelected) {
        if (icon == null) {
            return;
        }
        if (SubstanceCoreUtilities.useThemedDefaultIcon(this.tabPane)) {
            ComponentState currState = this.getTabState(tabIndex);
            StateTransitionTracker tabTracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)tabIndex);
            if (tabTracker == null && (currState.isFacetActive(ComponentStateFacet.ROLLOVER) || currState.isFacetActive(ComponentStateFacet.SELECTION) || currState.isDisabled())) {
                super.paintIcon(g2, tabPlacement, tabIndex, icon, iconRect, isSelected);
                return;
            }
            Icon themed = SubstanceCoreUtilities.getThemedIcon(this.tabPane, tabIndex, icon);
            if (tabTracker == null) {
                super.paintIcon(g2, tabPlacement, tabIndex, themed, iconRect, isSelected);
            } else {
                Graphics2D g2d = (Graphics2D)g2.create();
                super.paintIcon(g2d, tabPlacement, tabIndex, icon, iconRect, isSelected);
                g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.tabPane, 1.0f - tabTracker.getFacetStrength(ComponentStateFacet.ROLLOVER), g2d));
                super.paintIcon(g2d, tabPlacement, tabIndex, themed, iconRect, isSelected);
                g2d.dispose();
            }
        } else {
            super.paintIcon(g2, tabPlacement, tabIndex, icon, iconRect, isSelected);
        }
    }

    @Override
    protected MouseListener createMouseListener() {
        return null;
    }

    protected boolean toRotateTabsOnPlacement(int tabPlacement) {
        Object rotateProperty = this.tabPane.getClientProperty("substancelaf.rotate");
        if (!(rotateProperty instanceof Boolean)) {
            rotateProperty = UIManager.get("substancelaf.rotate");
        }
        boolean rotate = rotateProperty instanceof Boolean ? (Boolean)rotateProperty : true;
        return rotate && (tabPlacement == 2 || tabPlacement == 4);
    }

    private StateTransitionTracker getTracker(final int tabIndex, boolean initialRollover, boolean initialSelected) {
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)tabIndex);
        if (tracker == null) {
            DefaultButtonModel model = new DefaultButtonModel();
            model.setSelected(initialSelected);
            model.setRollover(initialRollover);
            tracker = new StateTransitionTracker(this.tabPane, model);
            tracker.registerModelListeners();
            tracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

                @Override
                public TimelineCallback getRepaintCallback() {
                    return new TabRepaintCallback(SubstanceTabbedPaneUI.this.tabPane, tabIndex);
                }
            });
            this.stateTransitionMultiTracker.addTracker((Comparable<Integer>)tabIndex, tracker);
        }
        return tracker;
    }

    private void trackTabModification(int tabIndex, Component tabComponent) {
        Timeline modifiedTimeline = new Timeline(this.tabPane);
        AnimationConfigurationManager.getInstance().configureModifiedTimeline(modifiedTimeline);
        modifiedTimeline.addCallback(new TabRepaintCallback(this.tabPane, tabIndex));
        modifiedTimeline.playLoop(Timeline.RepeatBehavior.REVERSE);
        this.modifiedTimelines.put(tabComponent, modifiedTimeline);
    }

    public class TabbedPaneLayout
    extends BasicTabbedPaneUI.TabbedPaneLayout {
        public TabbedPaneLayout() {
            super(SubstanceTabbedPaneUI.this);
        }

        @Override
        protected void normalizeTabRuns(int tabPlacement, int tabCount, int start, int max) {
            if (tabPlacement == 1 || tabPlacement == 3) {
                super.normalizeTabRuns(tabPlacement, tabCount, start, max);
            }
        }

        @Override
        protected void rotateTabRuns(int tabPlacement, int selectedRun) {
        }

        @Override
        protected void padSelectedTab(int tabPlacement, int selectedIndex) {
        }
    }

    protected class TabRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JTabbedPane tabbedPane;
        protected int tabIndex;

        public TabRepaintCallback(JTabbedPane tabPane, int tabIndex) {
            this.tabbedPane = tabPane;
            this.tabIndex = tabIndex;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintTab();
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintTab();
        }

        protected void repaintTab() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (SubstanceTabbedPaneUI.this.tabPane == null) {
                        return;
                    }
                    SubstanceTabbedPaneUI.this.ensureCurrentLayout();
                    int tabCount = SubstanceTabbedPaneUI.this.tabPane.getTabCount();
                    if (tabCount > 0 && TabRepaintCallback.this.tabIndex < tabCount && TabRepaintCallback.this.tabIndex < SubstanceTabbedPaneUI.this.rects.length) {
                        Rectangle rect = SubstanceTabbedPaneUI.this.getTabBounds(SubstanceTabbedPaneUI.this.tabPane, TabRepaintCallback.this.tabIndex);
                        SubstanceTabbedPaneUI.this.tabPane.repaint(rect);
                    }
                }
            });
        }
    }

    protected class MouseRolloverHandler
    implements MouseListener,
    MouseMotionListener {
        int prevRolledOver = -1;
        boolean prevInCloseButton = false;
        int tabOfPressedCloseButton = -1;

        protected MouseRolloverHandler() {
        }

        @Override
        public void mouseClicked(MouseEvent e2) {
            final int tabIndex = SubstanceTabbedPaneUI.this.tabForCoordinate(SubstanceTabbedPaneUI.this.tabPane, e2.getX(), e2.getY());
            TabCloseCallback closeCallback = SubstanceCoreUtilities.getTabCloseCallback(e2, SubstanceTabbedPaneUI.this.tabPane, tabIndex);
            if (closeCallback == null) {
                return;
            }
            final SubstanceConstants.TabCloseKind tabCloseKind = closeCallback.onAreaClick(SubstanceTabbedPaneUI.this.tabPane, tabIndex, e2);
            if (tabCloseKind == SubstanceConstants.TabCloseKind.NONE) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    SubstanceTabbedPaneUI.this.tryCloseTabs(tabIndex, tabCloseKind);
                }
            });
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            this.handleMouseMoveDrag(e2);
        }

        @Override
        public void mouseEntered(MouseEvent e2) {
            SubstanceTabbedPaneUI.this.setRolloverTab(SubstanceTabbedPaneUI.this.tabForCoordinate(SubstanceTabbedPaneUI.this.tabPane, e2.getX(), e2.getY()));
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            if (!SubstanceTabbedPaneUI.this.tabPane.isEnabled()) {
                return;
            }
            int tabIndex = SubstanceTabbedPaneUI.this.tabForCoordinate(SubstanceTabbedPaneUI.this.tabPane, e2.getX(), e2.getY());
            if (tabIndex >= 0 && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex)) {
                Rectangle rect = new Rectangle();
                rect = SubstanceTabbedPaneUI.this.getTabBounds(tabIndex, rect);
                Rectangle close = SubstanceTabbedPaneUI.this.getCloseButtonRectangleForEvents(tabIndex, rect.x, rect.y, rect.width, rect.height);
                boolean inCloseButton = close.contains(e2.getPoint());
                int n2 = this.tabOfPressedCloseButton = inCloseButton ? tabIndex : -1;
                if (tabIndex != SubstanceTabbedPaneUI.this.tabPane.getSelectedIndex()) {
                    if (inCloseButton) {
                        return;
                    }
                    SubstanceTabbedPaneUI.this.tabPane.setSelectedIndex(tabIndex);
                } else if (SubstanceTabbedPaneUI.this.tabPane.isRequestFocusEnabled()) {
                    SubstanceTabbedPaneUI.this.tabPane.requestFocus();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
            this.handleMouseMoveDrag(e2);
        }

        private void handleMouseMoveDrag(MouseEvent e2) {
            if (e2.getSource() != SubstanceTabbedPaneUI.this.tabPane) {
                return;
            }
            SubstanceTabbedPaneUI.this.setRolloverTab(SubstanceTabbedPaneUI.this.tabForCoordinate(SubstanceTabbedPaneUI.this.tabPane, e2.getX(), e2.getY()));
            if (!AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.ROLLOVER, SubstanceTabbedPaneUI.this.tabPane)) {
                return;
            }
            SubstanceTabbedPaneUI.this.substanceMouseLocation = e2.getPoint();
            int currRolledOver = SubstanceTabbedPaneUI.this.getRolloverTab();
            TabCloseCallback tabCloseCallback = SubstanceCoreUtilities.getTabCloseCallback(e2, SubstanceTabbedPaneUI.this.tabPane, currRolledOver);
            if (currRolledOver == this.prevRolledOver) {
                if (currRolledOver >= 0) {
                    Rectangle rect = new Rectangle();
                    rect = SubstanceTabbedPaneUI.this.getTabBounds(currRolledOver, rect);
                    Rectangle close = SubstanceTabbedPaneUI.this.getCloseButtonRectangleForEvents(currRolledOver, rect.x, rect.y, rect.width, rect.height);
                    boolean inCloseButton = close.contains(e2.getPoint());
                    if (this.prevInCloseButton == inCloseButton) {
                        return;
                    }
                    this.prevInCloseButton = inCloseButton;
                    if (tabCloseCallback != null) {
                        if (inCloseButton) {
                            String closeButtonTooltip = tabCloseCallback.getCloseButtonTooltip(SubstanceTabbedPaneUI.this.tabPane, currRolledOver);
                            SubstanceTabbedPaneUI.this.tabPane.setToolTipTextAt(currRolledOver, closeButtonTooltip);
                        } else {
                            String areaTooltip = tabCloseCallback.getAreaTooltip(SubstanceTabbedPaneUI.this.tabPane, currRolledOver);
                            SubstanceTabbedPaneUI.this.tabPane.setToolTipTextAt(currRolledOver, areaTooltip);
                        }
                    }
                    if (currRolledOver >= 0 && currRolledOver < SubstanceTabbedPaneUI.this.tabPane.getTabCount()) {
                        StateTransitionTracker tracker = SubstanceTabbedPaneUI.this.getTracker(currRolledOver, true, currRolledOver == SubstanceTabbedPaneUI.this.currSelectedIndex);
                        tracker.getModel().setRollover(false);
                        tracker.endTransition();
                    }
                }
            } else {
                StateTransitionTracker tracker;
                if (this.prevRolledOver >= 0 && this.prevRolledOver < SubstanceTabbedPaneUI.this.tabPane.getTabCount() && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(this.prevRolledOver)) {
                    tracker = SubstanceTabbedPaneUI.this.getTracker(this.prevRolledOver, true, this.prevRolledOver == SubstanceTabbedPaneUI.this.currSelectedIndex);
                    tracker.getModel().setRollover(false);
                }
                if (currRolledOver >= 0 && currRolledOver < SubstanceTabbedPaneUI.this.tabPane.getTabCount() && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(currRolledOver)) {
                    tracker = SubstanceTabbedPaneUI.this.getTracker(currRolledOver, false, currRolledOver == SubstanceTabbedPaneUI.this.currSelectedIndex);
                    tracker.getModel().setRollover(true);
                }
            }
            this.prevRolledOver = currRolledOver;
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            SubstanceTabbedPaneUI.this.setRolloverTab(-1);
            if (this.prevRolledOver >= 0 && this.prevRolledOver < SubstanceTabbedPaneUI.this.tabPane.getTabCount() && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(this.prevRolledOver)) {
                StateTransitionTracker tracker = SubstanceTabbedPaneUI.this.getTracker(this.prevRolledOver, true, this.prevRolledOver == SubstanceTabbedPaneUI.this.currSelectedIndex);
                tracker.getModel().setRollover(false);
                if (SubstanceCoreUtilities.getTabCloseCallback(e2, SubstanceTabbedPaneUI.this.tabPane, this.prevRolledOver) != null) {
                    SubstanceTabbedPaneUI.this.tabPane.setToolTipTextAt(this.prevRolledOver, null);
                }
            }
            this.prevRolledOver = -1;
        }

        @Override
        public void mouseReleased(final MouseEvent e2) {
            final int tabIndex = SubstanceTabbedPaneUI.this.tabForCoordinate(SubstanceTabbedPaneUI.this.tabPane, e2.getX(), e2.getY());
            if (SubstanceCoreUtilities.hasCloseButton(SubstanceTabbedPaneUI.this.tabPane, tabIndex) && tabIndex == this.tabOfPressedCloseButton) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (tabIndex >= 0 && SubstanceTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex)) {
                            Rectangle rect = new Rectangle();
                            rect = SubstanceTabbedPaneUI.this.getTabBounds(tabIndex, rect);
                            Rectangle close = SubstanceTabbedPaneUI.this.getCloseButtonRectangleForEvents(tabIndex, rect.x, rect.y, rect.width, rect.height);
                            if (close.contains(e2.getPoint())) {
                                TabCloseCallback closeCallback = SubstanceCoreUtilities.getTabCloseCallback(e2, SubstanceTabbedPaneUI.this.tabPane, tabIndex);
                                SubstanceConstants.TabCloseKind tabCloseKind = closeCallback == null ? SubstanceConstants.TabCloseKind.THIS : closeCallback.onCloseButtonClick(SubstanceTabbedPaneUI.this.tabPane, tabIndex, e2);
                                SubstanceTabbedPaneUI.this.tryCloseTabs(tabIndex, tabCloseKind);
                            }
                        }
                    }
                });
                this.tabOfPressedCloseButton = -1;
            }
        }
    }

    protected final class TabbedContainerListener
    extends ContainerAdapter {
        private Map<Component, List<PropertyChangeListener>> listeners = new HashMap<Component, List<PropertyChangeListener>>();

        protected void trackExistingTabs() {
            for (int i2 = 0; i2 < SubstanceTabbedPaneUI.this.tabPane.getTabCount(); ++i2) {
                this.trackTab(SubstanceTabbedPaneUI.this.tabPane.getComponentAt(i2));
            }
        }

        protected void trackTab(final Component tabComponent) {
            int tabIndex;
            if (tabComponent == null) {
                return;
            }
            PropertyChangeListener tabModifiedListener = new PropertyChangeListener(){

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("windowModified".equals(evt.getPropertyName())) {
                        int tabIndex;
                        Object oldValue = evt.getOldValue();
                        Object newValue = evt.getNewValue();
                        boolean wasModified = Boolean.TRUE.equals(oldValue);
                        boolean isModified = Boolean.TRUE.equals(newValue);
                        if (wasModified) {
                            if (!isModified) {
                                Timeline modifiedTimeline = (Timeline)SubstanceTabbedPaneUI.this.modifiedTimelines.get(tabComponent);
                                modifiedTimeline.cancel();
                                SubstanceTabbedPaneUI.this.modifiedTimelines.remove(tabComponent);
                            }
                        } else if (isModified && (tabIndex = SubstanceTabbedPaneUI.this.tabPane.indexOfComponent(tabComponent)) >= 0) {
                            SubstanceTabbedPaneUI.this.trackTabModification(tabIndex, tabComponent);
                        }
                    }
                }
            };
            tabComponent.addPropertyChangeListener(tabModifiedListener);
            List<PropertyChangeListener> currList = this.listeners.get(tabComponent);
            if (currList == null) {
                currList = new LinkedList<PropertyChangeListener>();
            }
            currList.add(tabModifiedListener);
            this.listeners.put(tabComponent, currList);
            if (tabComponent instanceof JComponent && Boolean.TRUE.equals(((JComponent)tabComponent).getClientProperty("windowModified")) && (tabIndex = SubstanceTabbedPaneUI.this.tabPane.indexOfComponent(tabComponent)) >= 0) {
                SubstanceTabbedPaneUI.this.trackTabModification(tabIndex, tabComponent);
            }
        }

        protected void stopTrackTab(Component tabComponent) {
            if (tabComponent == null) {
                return;
            }
            List<PropertyChangeListener> pclList = this.listeners.get(tabComponent);
            if (pclList != null) {
                for (PropertyChangeListener pcl : pclList) {
                    tabComponent.removePropertyChangeListener(pcl);
                }
            }
            this.listeners.put(tabComponent, null);
        }

        protected void stopTrackExistingTabs() {
            for (int i2 = 0; i2 < SubstanceTabbedPaneUI.this.tabPane.getTabCount(); ++i2) {
                this.stopTrackTab(SubstanceTabbedPaneUI.this.tabPane.getComponentAt(i2));
            }
        }

        @Override
        public void componentAdded(ContainerEvent e2) {
            Component tabComponent = e2.getChild();
            if (tabComponent instanceof UIResource) {
                return;
            }
            this.trackTab(tabComponent);
        }

        @Override
        public void componentRemoved(ContainerEvent e2) {
            Component tabComponent = e2.getChild();
            if (tabComponent == null) {
                return;
            }
            if (tabComponent instanceof UIResource) {
                return;
            }
            for (PropertyChangeListener pcl : this.listeners.get(tabComponent)) {
                tabComponent.removePropertyChangeListener(pcl);
            }
            this.listeners.get(tabComponent).clear();
            this.listeners.remove(tabComponent);
            Timeline timeline = (Timeline)SubstanceTabbedPaneUI.this.modifiedTimelines.get(tabComponent);
            if (timeline != null) {
                timeline.cancel();
                SubstanceTabbedPaneUI.this.modifiedTimelines.remove(tabComponent);
            }
        }
    }
}

