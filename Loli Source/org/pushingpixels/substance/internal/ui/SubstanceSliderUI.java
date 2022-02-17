/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSliderUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.SeparatorPainterUtils;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.RolloverControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.SubstanceIconFactory;

public class SubstanceSliderUI
extends BasicSliderUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    private ButtonModel thumbModel = new DefaultButtonModel();
    private RolloverControlListener substanceRolloverListener;
    private PropertyChangeListener substancePropertyChangeListener;
    protected StateTransitionTracker stateTransitionTracker;
    protected Icon horizontalIcon;
    protected Icon roundIcon;
    protected Icon verticalIcon;
    protected static final LazyResettableHashMap<BufferedImage> trackCache = new LazyResettableHashMap("SubstanceSliderUI.track");

    public void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__installListeners(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__installDefaults(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__uninstallListeners(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__uninstallDefaults(JSlider jSlider) {
        super.uninstallDefaults(jSlider);
    }

    @Override
    protected void uninstallDefaults(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__uninstallDefaults(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSliderUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceSliderUI((JSlider)comp);
    }

    public SubstanceSliderUI(JSlider slider) {
        super(null);
        this.thumbModel.setArmed(false);
        this.thumbModel.setSelected(false);
        this.thumbModel.setPressed(false);
        this.thumbModel.setRollover(false);
        this.thumbModel.setEnabled(slider.isEnabled());
        this.stateTransitionTracker = new StateTransitionTracker(slider, this.thumbModel);
    }

    @Override
    protected void calculateTrackRect() {
        super.calculateTrackRect();
        if (this.slider.getOrientation() == 0) {
            this.trackRect.y = 3 + (int)Math.ceil(SubstanceSizeUtils.getFocusStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.slider))) + this.insetCache.top;
        }
    }

    private Rectangle getPaintTrackRect() {
        int trackRight;
        int trackLeft = 0;
        int trackTop = 0;
        int trackWidth = this.getTrackWidth();
        if (this.slider.getOrientation() == 0) {
            trackTop = 3 + this.insetCache.top + 2 * this.focusInsets.top;
            int trackBottom = trackTop + trackWidth - 1;
            int trackRight2 = this.trackRect.width;
            return new Rectangle(this.trackRect.x + trackLeft, trackTop, trackRight2 - trackLeft, trackBottom - trackTop);
        }
        if (this.slider.getPaintLabels() || this.slider.getPaintTicks()) {
            if (this.slider.getComponentOrientation().isLeftToRight()) {
                trackLeft = this.trackRect.x + this.insetCache.left + this.focusInsets.left;
                trackRight = trackLeft + trackWidth - 1;
            } else {
                trackRight = this.trackRect.x + this.trackRect.width - this.insetCache.right - this.focusInsets.right;
                trackLeft = trackRight - trackWidth - 1;
            }
        } else if (this.slider.getComponentOrientation().isLeftToRight()) {
            trackLeft = (this.insetCache.left + this.focusInsets.left + this.slider.getWidth() - this.insetCache.right - this.focusInsets.right) / 2 - trackWidth / 2;
            trackRight = trackLeft + trackWidth - 1;
        } else {
            trackRight = (this.insetCache.left + this.focusInsets.left + this.slider.getWidth() - this.insetCache.right - this.focusInsets.right) / 2 + trackWidth / 2;
            trackLeft = trackRight - trackWidth - 1;
        }
        int trackBottom = this.trackRect.height - 1;
        return new Rectangle(trackLeft, this.trackRect.y + trackTop, trackRight - trackLeft, trackBottom - trackTop);
    }

    @Override
    public void paintTrack(Graphics g2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        boolean drawInverted = this.drawInverted();
        Rectangle paintRect = this.getPaintTrackRect();
        int width = paintRect.width;
        int height = paintRect.height;
        if (this.slider.getOrientation() == 1) {
            int temp = width;
            width = height;
            height = temp;
            AffineTransform at = graphics.getTransform();
            at.translate(paintRect.x, width + paintRect.y);
            at.rotate(-1.5707963267948966);
            graphics.setTransform(at);
        } else {
            graphics.translate(paintRect.x, paintRect.y);
        }
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.stateTransitionTracker.getModelStateInfo();
        SubstanceColorScheme trackSchemeUnselected = SubstanceColorSchemeUtilities.getColorScheme(this.slider, this.slider.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme trackBorderSchemeUnselected = SubstanceColorSchemeUtilities.getColorScheme(this.slider, ColorSchemeAssociationKind.BORDER, this.slider.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        this.paintSliderTrack(graphics, drawInverted, trackSchemeUnselected, trackBorderSchemeUnselected, width, height);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            float contribution;
            ComponentState activeState = activeEntry.getKey();
            if (!activeState.isActive() || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
            graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.slider, contribution, g2));
            SubstanceColorScheme activeFillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.slider, activeState);
            SubstanceColorScheme activeBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.slider, ColorSchemeAssociationKind.BORDER, activeState);
            this.paintSliderTrackSelected(graphics, drawInverted, paintRect, activeFillScheme, activeBorderScheme, width, height);
        }
        graphics.dispose();
    }

    private void paintSliderTrack(Graphics2D graphics, boolean drawInverted, SubstanceColorScheme fillColorScheme, SubstanceColorScheme borderScheme, int width, int height) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        ClassicFillPainter fillPainter = ClassicFillPainter.INSTANCE;
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(this.slider);
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(this.slider);
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize) / 2.0);
        float radius = SubstanceSizeUtils.getClassicButtonCornerRadius(componentFontSize) / 2.0f;
        int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, Float.valueOf(radius), borderDelta, borderThickness, fillColorScheme.getDisplayName(), borderScheme.getDisplayName());
        BufferedImage trackImage = trackCache.get(key);
        if (trackImage == null) {
            trackImage = SubstanceCoreUtilities.getBlankImage(width + 1, height + 1);
            Graphics2D cacheGraphics = trackImage.createGraphics();
            GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width + 1, height + 1, radius, null, borderDelta);
            fillPainter.paintContourBackground(cacheGraphics, this.slider, width, height, contour, false, fillColorScheme, false);
            GeneralPath contourInner = SubstanceOutlineUtilities.getBaseOutline(width + 1, height + 1, radius - (float)borderThickness, null, borderThickness + borderDelta);
            borderPainter.paintBorder(cacheGraphics, this.slider, width + 1, height + 1, contour, contourInner, borderScheme);
            trackCache.put(key, trackImage);
            cacheGraphics.dispose();
        }
        g2d.drawImage((Image)trackImage, 0, 0, null);
        g2d.dispose();
    }

    private void paintSliderTrackSelected(Graphics2D graphics, boolean drawInverted, Rectangle paintRect, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme, int width, int height) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        Insets insets = this.slider.getInsets();
        insets.top /= 2;
        insets.left /= 2;
        insets.bottom /= 2;
        insets.right /= 2;
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(this.slider);
        SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(this.slider);
        float radius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(this.slider)) / 2.0f;
        int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.slider)) / 2.0);
        if (this.slider.isEnabled()) {
            if (this.slider.getOrientation() == 0) {
                int fillMaxX;
                int fillMinX;
                int middleOfThumb = this.thumbRect.x + this.thumbRect.width / 2 - paintRect.x;
                if (drawInverted) {
                    fillMinX = middleOfThumb;
                    fillMaxX = width;
                } else {
                    fillMinX = 0;
                    fillMaxX = middleOfThumb;
                }
                int fillWidth = fillMaxX - fillMinX;
                int fillHeight = height + 1;
                if (fillWidth > 0 && fillHeight > 0) {
                    GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(fillWidth, fillHeight, radius, null, borderDelta);
                    g2d.translate(fillMinX, 0);
                    fillPainter.paintContourBackground(g2d, this.slider, fillWidth, fillHeight, contour, false, fillScheme, false);
                    borderPainter.paintBorder(g2d, this.slider, fillWidth, fillHeight, contour, null, borderScheme);
                }
            } else {
                int fillMax;
                int fillMin;
                int middleOfThumb = this.thumbRect.y + this.thumbRect.height / 2 - paintRect.y;
                if (this.drawInverted()) {
                    fillMin = 0;
                    fillMax = middleOfThumb;
                    g2d.translate(width + 2 - middleOfThumb, 0);
                } else {
                    fillMin = middleOfThumb;
                    fillMax = width + 1;
                }
                int fillWidth = fillMax - fillMin;
                int fillHeight = height + 1;
                if (fillWidth > 0 && fillHeight > 0) {
                    GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(fillWidth, fillHeight, radius, null, borderDelta);
                    fillPainter.paintContourBackground(g2d, this.slider, fillWidth, fillHeight, contour, false, fillScheme, false);
                    borderPainter.paintBorder(g2d, this.slider, fillWidth, fillHeight, contour, null, borderScheme);
                }
            }
        }
        g2d.dispose();
    }

    @Override
    protected Dimension getThumbSize() {
        Icon thumbIcon = this.getIcon();
        return new Dimension(thumbIcon.getIconWidth(), thumbIcon.getIconHeight());
    }

    protected Icon getIcon() {
        if (this.slider.getOrientation() == 0) {
            if (this.slider.getPaintTicks() || this.slider.getPaintLabels()) {
                return this.horizontalIcon;
            }
            return this.roundIcon;
        }
        if (this.slider.getPaintTicks() || this.slider.getPaintLabels()) {
            return this.verticalIcon;
        }
        return this.roundIcon;
    }

    @Override
    public void paintThumb(Graphics g2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        Rectangle knobBounds = this.thumbRect;
        graphics.translate(knobBounds.x, knobBounds.y);
        Icon icon = this.getIcon();
        if (this.slider.getOrientation() == 0) {
            if (icon != null) {
                icon.paintIcon(this.slider, graphics, -1, 0);
            }
        } else if (this.slider.getComponentOrientation().isLeftToRight()) {
            if (icon != null) {
                icon.paintIcon(this.slider, graphics, 0, -1);
            }
        } else if (icon != null) {
            icon.paintIcon(this.slider, graphics, 0, 1);
        }
        graphics.dispose();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        ComponentState currState = ComponentState.getState(this.thumbModel, this.slider);
        float alpha = SubstanceColorSchemeUtilities.getAlpha(this.slider, currState);
        BackgroundPaintingUtils.updateIfOpaque(graphics, c2);
        this.recalculateIfInsetsChanged();
        this.recalculateIfOrientationChanged();
        Rectangle clip = graphics.getClipBounds();
        if (!clip.intersects(this.trackRect) && this.slider.getPaintTrack()) {
            this.calculateGeometry();
        }
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.slider, alpha, g2));
        if (this.slider.getPaintTrack() && clip.intersects(this.trackRect)) {
            this.paintTrack(graphics);
        }
        if (this.slider.getPaintTicks() && clip.intersects(this.tickRect)) {
            this.paintTicks(graphics);
        }
        this.paintFocus(graphics);
        if (clip.intersects(this.thumbRect)) {
            this.paintThumb(graphics);
        }
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.slider, 1.0f, g2));
        if (this.slider.getPaintLabels() && clip.intersects(this.labelRect)) {
            this.paintLabels(graphics);
        }
        graphics.dispose();
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }

    @Override
    public boolean isInside(MouseEvent me) {
        Rectangle thumbB = this.thumbRect;
        return thumbB != null && thumbB.contains(me.getX(), me.getY());
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__installDefaults(JSlider slider) {
        super.installDefaults(slider);
        Font f2 = slider.getFont();
        if (f2 == null || f2 instanceof UIResource) {
            slider.setFont(new FontUIResource(SubstanceLookAndFeel.getFontPolicy().getFontSet("Substance", null).getControlFont()));
        }
        int size = SubstanceSizeUtils.getSliderIconSize(SubstanceSizeUtils.getComponentFontSize(slider));
        this.horizontalIcon = SubstanceIconFactory.getSliderHorizontalIcon(size, false);
        this.roundIcon = SubstanceIconFactory.getSliderRoundIcon(size);
        this.verticalIcon = SubstanceIconFactory.getSliderVerticalIcon(size, false);
        int focusIns = (int)Math.ceil(2.0 * (double)SubstanceSizeUtils.getFocusStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider)));
        this.focusInsets = new Insets(focusIns, focusIns, focusIns, focusIns);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__installListeners(final JSlider slider) {
        super.installListeners(slider);
        this.substanceRolloverListener = new RolloverControlListener(this, this.thumbModel);
        slider.addMouseListener(this.substanceRolloverListener);
        slider.addMouseMotionListener(this.substanceRolloverListener);
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    SubstanceSliderUI.this.thumbModel.setEnabled(slider.isEnabled());
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            slider.updateUI();
                        }
                    });
                }
            }
        };
        this.slider.addPropertyChangeListener(this.substancePropertyChangeListener);
        this.stateTransitionTracker.registerModelListeners();
        this.stateTransitionTracker.registerFocusListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSliderUI__uninstallListeners(JSlider slider) {
        super.uninstallListeners(slider);
        slider.removeMouseListener(this.substanceRolloverListener);
        slider.removeMouseMotionListener(this.substanceRolloverListener);
        this.substanceRolloverListener = null;
        slider.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
    }

    @Override
    public void paintFocus(Graphics g2) {
        SubstanceCoreUtilities.paintFocus(g2, this.slider, this.slider, this, null, null, 1.0f, (int)Math.ceil(SubstanceSizeUtils.getFocusStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.slider))) / 2);
    }

    protected int getThumbOverhang() {
        return (int)(this.getThumbSize().getHeight() - (double)this.getTrackWidth()) / 2;
    }

    protected int getTrackWidth() {
        return SubstanceSizeUtils.getSliderTrackSize(SubstanceSizeUtils.getComponentFontSize(this.slider));
    }

    @Override
    protected int getTickLength() {
        return SubstanceSizeUtils.getSliderTickSize(SubstanceSizeUtils.getComponentFontSize(this.slider));
    }

    @Override
    public void paintTicks(Graphics g2) {
        Rectangle tickBounds = this.tickRect;
        SubstanceColorScheme tickScheme = SubstanceColorSchemeUtilities.getColorScheme(this.slider, ColorSchemeAssociationKind.SEPARATOR, this.slider.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        if (this.slider.getOrientation() == 0) {
            int xPos;
            int value;
            if (this.slider.getMinorTickSpacing() > 0 && this.slider.getMajorTickSpacing() > 0) {
                ArrayList<Integer> minorXs = new ArrayList<Integer>();
                for (value = this.slider.getMinimum() + this.slider.getMinorTickSpacing(); value < this.slider.getMaximum(); value += this.slider.getMinorTickSpacing()) {
                    int delta = value - this.slider.getMinimum();
                    if (delta % this.slider.getMajorTickSpacing() == 0) continue;
                    xPos = this.xPositionForValue(value);
                    minorXs.add(xPos - 1);
                }
                SeparatorPainterUtils.paintVerticalLines(g2, this.slider, tickScheme, tickBounds.y, minorXs, tickBounds.height / 2, 0.75f);
            }
            if (this.slider.getMajorTickSpacing() > 0) {
                ArrayList<Integer> majorXs = new ArrayList<Integer>();
                for (value = this.slider.getMinimum() + this.slider.getMajorTickSpacing(); value < this.slider.getMaximum(); value += this.slider.getMajorTickSpacing()) {
                    xPos = this.xPositionForValue(value);
                    majorXs.add(xPos - 1);
                }
                SeparatorPainterUtils.paintVerticalLines(g2, this.slider, tickScheme, tickBounds.y, majorXs, tickBounds.height, 0.75f);
            }
        } else {
            int yPos;
            g2.translate(tickBounds.x, 0);
            int value = this.slider.getMinimum() + this.slider.getMinorTickSpacing();
            boolean ltr = this.slider.getComponentOrientation().isLeftToRight();
            if (this.slider.getMinorTickSpacing() > 0) {
                ArrayList<Integer> minorYs = new ArrayList<Integer>();
                int offset = 0;
                if (!ltr) {
                    offset = tickBounds.width - tickBounds.width / 2;
                }
                while (value < this.slider.getMaximum()) {
                    yPos = this.yPositionForValue(value);
                    minorYs.add(yPos);
                    value += this.slider.getMinorTickSpacing();
                }
                SeparatorPainterUtils.paintHorizontalLines(g2, this.slider, tickScheme, offset, minorYs, tickBounds.width / 2, ltr ? 0.75f : 0.25f, ltr);
            }
            if (this.slider.getMajorTickSpacing() > 0) {
                ArrayList<Integer> majorYs = new ArrayList<Integer>();
                for (value = this.slider.getMinimum() + this.slider.getMajorTickSpacing(); value < this.slider.getMaximum(); value += this.slider.getMajorTickSpacing()) {
                    yPos = this.yPositionForValue(value);
                    majorYs.add(yPos);
                }
                SeparatorPainterUtils.paintHorizontalLines(g2, this.slider, tickScheme, 0, majorYs, tickBounds.width, ltr ? 0.75f : 0.25f, ltr);
            }
            g2.translate(-tickBounds.x, 0);
        }
    }

    @Override
    protected void calculateTickRect() {
        if (this.slider.getOrientation() == 0) {
            this.tickRect.x = this.trackRect.x;
            this.tickRect.y = this.trackRect.y + this.trackRect.height;
            this.tickRect.width = this.trackRect.width;
            this.tickRect.height = this.slider.getPaintTicks() ? this.getTickLength() : 0;
        } else {
            this.tickRect.width = this.slider.getPaintTicks() ? this.getTickLength() : 0;
            this.tickRect.x = this.slider.getComponentOrientation().isLeftToRight() ? this.trackRect.x + this.trackRect.width : this.trackRect.x - this.tickRect.width;
            this.tickRect.y = this.trackRect.y;
            this.tickRect.height = this.trackRect.height;
        }
        if (this.slider.getPaintTicks()) {
            if (this.slider.getOrientation() == 0) {
                this.tickRect.y -= 3;
            } else {
                this.tickRect.x = this.slider.getComponentOrientation().isLeftToRight() ? (this.tickRect.x -= 2) : (this.tickRect.x += 2);
            }
        }
    }

    @Override
    protected void calculateLabelRect() {
        super.calculateLabelRect();
        if (this.slider.getOrientation() == 1 && !this.slider.getPaintTicks() && this.slider.getComponentOrientation().isLeftToRight()) {
            this.labelRect.x += 3;
        }
    }

    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        Rectangle trackRect = this.getPaintTrackRect();
        if (this.slider.getOrientation() == 0) {
            int valuePosition = this.xPositionForValue(this.slider.getValue());
            double centerY = (double)trackRect.y + (double)trackRect.height / 2.0;
            this.thumbRect.y = (int)(centerY - (double)this.thumbRect.height / 2.0) + 1;
            this.thumbRect.x = valuePosition - this.thumbRect.width / 2;
        } else {
            int valuePosition = this.yPositionForValue(this.slider.getValue());
            double centerX = (double)trackRect.x + (double)trackRect.width / 2.0;
            this.thumbRect.x = (int)(centerX - (double)this.thumbRect.width / 2.0) + 1;
            this.thumbRect.y = valuePosition - this.thumbRect.height / 2;
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        Dimension d2;
        this.recalculateIfInsetsChanged();
        if (this.slider.getOrientation() == 1) {
            d2 = new Dimension(this.getPreferredVerticalSize());
            d2.width = this.insetCache.left + this.insetCache.right;
            d2.width += this.focusInsets.left + this.focusInsets.right;
            d2.width += this.trackRect.width;
            if (this.slider.getPaintTicks()) {
                d2.width += this.getTickLength();
            }
            if (this.slider.getPaintLabels()) {
                d2.width += this.getWidthOfWidestLabel();
            }
            d2.width += 3;
        } else {
            d2 = new Dimension(this.getPreferredHorizontalSize());
            d2.height = this.insetCache.top + this.insetCache.bottom;
            d2.height += this.focusInsets.top + this.focusInsets.bottom;
            d2.height += this.trackRect.height;
            if (this.slider.getPaintTicks()) {
                d2.height += this.getTickLength();
            }
            if (this.slider.getPaintLabels()) {
                d2.height += this.getHeightOfTallestLabel();
            }
            d2.height += 3;
        }
        return d2;
    }

    @Override
    public void setThumbLocation(int x2, int y2) {
        super.setThumbLocation(x2, y2);
        this.slider.repaint();
    }

    @Override
    public Dimension getPreferredHorizontalSize() {
        return new Dimension(SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getComponentFontSize(this.slider), 200, 1, 20, false), 21);
    }

    @Override
    public Dimension getPreferredVerticalSize() {
        return new Dimension(21, SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getComponentFontSize(this.slider), 200, 1, 20, false));
    }
}

