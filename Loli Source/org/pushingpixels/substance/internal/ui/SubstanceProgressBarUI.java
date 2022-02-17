/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.BoundedRangeModel;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelinePropertyBuilder;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Spline;

public class SubstanceProgressBarUI
extends BasicProgressBarUI {
    protected Set lafWidgets;
    private static final ComponentState DETERMINATE_SELECTED = new ComponentState("determinate enabled", new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.DETERMINATE, ComponentStateFacet.SELECTION}, null);
    private static final ComponentState DETERMINATE_SELECTED_DISABLED = new ComponentState("determinate disabled", new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE, ComponentStateFacet.SELECTION}, new ComponentStateFacet[]{ComponentStateFacet.ENABLE});
    private static final ComponentState INDETERMINATE_SELECTED = new ComponentState("indeterminate enabled", new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.SELECTION}, new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE});
    private static final ComponentState INDETERMINATE_SELECTED_DISABLED = new ComponentState("indeterminate disabled", null, new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE, ComponentStateFacet.ENABLE, ComponentStateFacet.SELECTION});
    private static LazyResettableHashMap<BufferedImage> stripeMap = new LazyResettableHashMap("SubstanceProgressBarUI.stripeMap");
    private static LazyResettableHashMap<BufferedImage> backgroundMap = new LazyResettableHashMap("SubstanceProgressBarUI.backgroundMap");
    private float animationPosition;
    protected ChangeListener substanceValueChangeListener;
    protected PropertyChangeListener substancePropertyChangeListener;
    protected int margin;
    protected float speed;
    protected int displayedValue;
    protected Timeline displayTimeline;
    protected Timeline indeterminateLoopTimeline;

    public void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceProgressBarUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__installDefaults() {
        super.installDefaults();
        this.displayedValue = this.progressBar.getValue();
        LookAndFeel.installProperty(this.progressBar, "opaque", Boolean.FALSE);
        this.speed = 20.0f * (float)UIManager.getInt("ProgressBar.repaintInterval") / (float)UIManager.getInt("ProgressBar.cycleTime");
        float borderThickness = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.progressBar));
        this.margin = (int)Math.ceil(1.5 * (double)borderThickness);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__installListeners() {
        super.installListeners();
        this.substanceValueChangeListener = new SubstanceChangeListener();
        this.progressBar.addChangeListener(this.substanceValueChangeListener);
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceProgressBarUI.this.progressBar != null) {
                                SubstanceProgressBarUI.this.progressBar.updateUI();
                            }
                        }
                    });
                }
            }
        };
        this.progressBar.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceProgressBarUI__uninstallListeners() {
        this.progressBar.removeChangeListener(this.substanceValueChangeListener);
        this.substanceValueChangeListener = null;
        this.progressBar.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        super.uninstallListeners();
    }

    private static BufferedImage getStripe(int baseSize, boolean isRotated, SubstanceColorScheme colorScheme) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(baseSize, isRotated, colorScheme.getDisplayName());
        BufferedImage result = stripeMap.get(key);
        if (result == null) {
            result = SubstanceImageCreator.getStripe(baseSize, colorScheme.getUltraLightColor());
            if (isRotated) {
                result = SubstanceImageCreator.getRotated(result, 1);
            }
            stripeMap.put(key, result);
        }
        return result;
    }

    private static BufferedImage getDeterminateBackground(JProgressBar bar, int width, int height, SubstanceColorScheme scheme, SubstanceFillPainter gp, int orientation, ComponentOrientation componentOrientation) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, scheme.getDisplayName(), gp.getDisplayName(), orientation, componentOrientation);
        BufferedImage result = backgroundMap.get(key);
        if (result == null) {
            result = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D g2d = result.createGraphics();
            GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, 0.0f, null);
            gp.paintContourBackground(g2d, bar, width, height, contour, false, scheme, true);
            g2d.dispose();
            if (orientation == 1) {
                result = componentOrientation.isLeftToRight() ? SubstanceImageCreator.getRotated(result, 3) : SubstanceImageCreator.getRotated(result, 1);
            }
            backgroundMap.put(key, result);
        }
        return result;
    }

    @Override
    public void paintDeterminate(Graphics g2, JComponent c2) {
        BufferedImage back;
        if (!(g2 instanceof Graphics2D)) {
            return;
        }
        ComponentState fillState = this.getFillState();
        ComponentState progressState = this.getProgressState();
        int barRectWidth = this.progressBar.getWidth() - 2 * this.margin;
        int barRectHeight = this.progressBar.getHeight() - 2 * this.margin;
        int amountFull = this.getAmountFull(new Insets(this.margin, this.margin, this.margin, this.margin), barRectWidth, barRectHeight);
        Graphics2D g2d = (Graphics2D)g2.create();
        float stateAlpha = SubstanceColorSchemeUtilities.getAlpha(this.progressBar, fillState);
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.progressBar, stateAlpha, g2));
        SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.progressBar, fillState);
        SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(this.progressBar);
        if (this.progressBar.getOrientation() == 0) {
            back = SubstanceProgressBarUI.getDeterminateBackground(this.progressBar, barRectWidth + 1, barRectHeight + 1, fillScheme, fillPainter, this.progressBar.getOrientation(), this.progressBar.getComponentOrientation());
            g2d.drawImage((Image)back, this.margin, this.margin, null);
        } else {
            back = SubstanceProgressBarUI.getDeterminateBackground(this.progressBar, barRectHeight + 1, barRectWidth + 1, fillScheme, fillPainter, this.progressBar.getOrientation(), this.progressBar.getComponentOrientation());
            g2d.drawImage((Image)back, this.margin, this.margin, null);
        }
        if (amountFull > 0) {
            int borderDelta = 0;
            SubstanceColorScheme fillColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.progressBar, progressState);
            if (this.progressBar.getOrientation() == 0) {
                int barWidth = amountFull - 2 * borderDelta;
                int barHeight = barRectHeight - 2 * borderDelta;
                if (barWidth > 0 && barHeight > 0) {
                    if (this.progressBar.getComponentOrientation().isLeftToRight()) {
                        SubstanceImageCreator.paintRectangularBackground(this.progressBar, g2, this.margin + borderDelta, this.margin + borderDelta, barWidth, barHeight, fillColorScheme, 0.6f, false);
                    } else {
                        SubstanceImageCreator.paintRectangularBackground(this.progressBar, g2, this.margin + barRectWidth - amountFull - 2 * borderDelta, this.margin + borderDelta, barWidth, barHeight, fillColorScheme, 0.6f, false);
                    }
                }
            } else {
                int barWidth = amountFull - 2 * borderDelta;
                int barHeight = barRectWidth - 2 * borderDelta;
                if (amountFull > 0 && barHeight > 0) {
                    SubstanceImageCreator.paintRectangularBackground(this.progressBar, g2, this.margin + borderDelta, this.margin + barRectHeight - barWidth - borderDelta, barHeight, barWidth, fillColorScheme, 0.6f, true);
                }
            }
        }
        if (this.progressBar.isStringPainted()) {
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.progressBar, 1.0f, g2));
            this.paintString(g2d, this.margin, this.margin, barRectWidth, barRectHeight, amountFull, new Insets(this.margin, this.margin, this.margin, this.margin));
        }
        g2d.dispose();
    }

    @Override
    protected Color getSelectionBackground() {
        ComponentState fillState = this.getFillState();
        SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.progressBar, fillState);
        return SubstanceColorUtilities.getForegroundColor(scheme);
    }

    @Override
    protected Color getSelectionForeground() {
        ComponentState progressState = this.getProgressState();
        SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.progressBar, progressState);
        return SubstanceColorUtilities.getForegroundColor(scheme);
    }

    @Override
    public void paintIndeterminate(Graphics g2, JComponent c2) {
        if (!(g2 instanceof Graphics2D)) {
            return;
        }
        ComponentState progressState = this.getProgressState();
        int barRectWidth = this.progressBar.getWidth() - 2 * this.margin;
        int barRectHeight = this.progressBar.getHeight() - 2 * this.margin;
        int valComplete = 0;
        valComplete = this.progressBar.getOrientation() == 0 ? (int)(this.animationPosition * (float)(2 * barRectHeight + 1)) : (int)(this.animationPosition * (float)(2 * barRectWidth + 1));
        Graphics2D g2d = (Graphics2D)g2.create();
        float stateAlpha = SubstanceColorSchemeUtilities.getAlpha(this.progressBar, progressState);
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.progressBar, stateAlpha, g2));
        SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.progressBar, progressState);
        if (this.progressBar.getOrientation() == 0) {
            SubstanceImageCreator.paintRectangularStripedBackground(this.progressBar, g2d, this.margin, this.margin, barRectWidth, barRectHeight, scheme, SubstanceProgressBarUI.getStripe(barRectHeight, false, scheme), valComplete, 0.6f, false);
        } else {
            SubstanceImageCreator.paintRectangularStripedBackground(this.progressBar, g2d, this.margin, this.margin, barRectWidth, barRectHeight, scheme, SubstanceProgressBarUI.getStripe(barRectWidth, true, scheme), 2 * barRectWidth - valComplete, 0.6f, true);
        }
        if (this.progressBar.isStringPainted()) {
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.progressBar, 1.0f, g2));
            this.paintString(g2d, this.margin, this.margin, barRectWidth, barRectHeight, barRectWidth, new Insets(this.margin, this.margin, this.margin, this.margin));
        }
        g2d.dispose();
    }

    private ComponentState getFillState() {
        return this.progressBar.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
    }

    private ComponentState getProgressState() {
        if (this.progressBar.isIndeterminate()) {
            return this.progressBar.isEnabled() ? INDETERMINATE_SELECTED : INDETERMINATE_SELECTED_DISABLED;
        }
        return this.progressBar.isEnabled() ? DETERMINATE_SELECTED : DETERMINATE_SELECTED_DISABLED;
    }

    @Override
    protected Rectangle getBox(Rectangle r2) {
        int barRectWidth = this.progressBar.getWidth() - 2 * this.margin;
        int barRectHeight = this.progressBar.getHeight() - 2 * this.margin;
        return new Rectangle(this.margin, this.margin, barRectWidth, barRectHeight);
    }

    @Override
    protected void startAnimationTimer() {
        this.indeterminateLoopTimeline = new Timeline(this);
        Integer cycleDuration = UIManager.getInt("ProgressBar.cycleTime");
        if (cycleDuration == null) {
            cycleDuration = 1000;
        }
        this.indeterminateLoopTimeline.setDuration(cycleDuration.intValue());
        this.indeterminateLoopTimeline.addCallback(new TimelineCallback(){

            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (SubstanceProgressBarUI.this.progressBar != null && SubstanceProgressBarUI.this.progressBar.isVisible()) {
                    SubstanceProgressBarUI.this.progressBar.repaint();
                }
            }

            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
                if (SubstanceProgressBarUI.this.progressBar != null && SubstanceProgressBarUI.this.progressBar.isVisible()) {
                    SubstanceProgressBarUI.this.progressBar.repaint();
                }
            }
        });
        this.indeterminateLoopTimeline.addPropertyToInterpolate(Timeline.property("animationPosition").from(Float.valueOf(0.0f)).to(Float.valueOf(1.0f)).setWith(new TimelinePropertyBuilder.PropertySetter<Float>(){

            @Override
            public void set(Object obj, String fieldName, Float value) {
                SubstanceProgressBarUI.this.animationPosition = value.floatValue();
            }
        }));
        this.indeterminateLoopTimeline.playLoop(Timeline.RepeatBehavior.LOOP);
    }

    @Override
    protected void stopAnimationTimer() {
        this.indeterminateLoopTimeline.abort();
    }

    public static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubstanceProgressBarUI: \n");
        sb.append("\t" + stripeMap.size() + " stripes");
        return sb.toString();
    }

    @Override
    protected int getAmountFull(Insets b2, int width, int height) {
        int amountFull = 0;
        BoundedRangeModel model = this.progressBar.getModel();
        long span = model.getMaximum() - model.getMinimum();
        double percentComplete = (double)(this.displayedValue - model.getMinimum()) / (double)span;
        if (model.getMaximum() - model.getMinimum() != 0) {
            amountFull = this.progressBar.getOrientation() == 0 ? (int)Math.round((double)width * percentComplete) : (int)Math.round((double)height * percentComplete);
        }
        return amountFull;
    }

    @Override
    protected Dimension getPreferredInnerHorizontal() {
        int size = SubstanceSizeUtils.getComponentFontSize(this.progressBar);
        size += 2 * SubstanceSizeUtils.getAdjustedSize(size, 1, 4, 1, false);
        return new Dimension(146 + SubstanceSizeUtils.getAdjustedSize(size, 0, 1, 10, false), size);
    }

    @Override
    protected Dimension getPreferredInnerVertical() {
        int size = SubstanceSizeUtils.getComponentFontSize(this.progressBar);
        size += 2 * SubstanceSizeUtils.getAdjustedSize(size, 1, 4, 1, false);
        return new Dimension(size, 146 + SubstanceSizeUtils.getAdjustedSize(size, 0, 1, 10, false));
    }

    @Override
    protected void paintString(Graphics g2, int x2, int y2, int width, int height, int amountFull, Insets b2) {
        if (this.progressBar.getOrientation() == 0) {
            if (this.progressBar.getComponentOrientation().isLeftToRight()) {
                if (this.progressBar.isIndeterminate()) {
                    this.boxRect = this.getBox(this.boxRect);
                    this.paintString(g2, x2, y2, width, height, this.boxRect.x, this.boxRect.width, b2);
                } else {
                    this.paintString(g2, x2, y2, width, height, x2, amountFull, b2);
                }
            } else {
                this.paintString(g2, x2, y2, width, height, x2 + width - amountFull, amountFull, b2);
            }
        } else if (this.progressBar.isIndeterminate()) {
            this.boxRect = this.getBox(this.boxRect);
            this.paintString(g2, x2, y2, width, height, this.boxRect.y, this.boxRect.height, b2);
        } else {
            this.paintString(g2, x2, y2, width, height, y2 + height - amountFull, amountFull, b2);
        }
    }

    private void paintString(Graphics g2, int x2, int y2, int width, int height, int fillStart, int amountFull, Insets b2) {
        String progressString = this.progressBar.getString();
        Rectangle renderRectangle = this.getStringRectangle(progressString, x2, y2, width, height);
        if (this.progressBar.getOrientation() == 0) {
            SubstanceTextUtilities.paintText(g2, this.progressBar, renderRectangle, progressString, -1, this.progressBar.getFont(), this.getSelectionBackground(), new Rectangle(amountFull, y2, this.progressBar.getWidth() - amountFull, height));
            SubstanceTextUtilities.paintText(g2, this.progressBar, renderRectangle, progressString, -1, this.progressBar.getFont(), this.getSelectionForeground(), new Rectangle(fillStart, y2, amountFull, height));
        } else {
            SubstanceTextUtilities.paintVerticalText(g2, this.progressBar, renderRectangle, progressString, -1, this.progressBar.getFont(), this.getSelectionBackground(), new Rectangle(x2, y2, width, this.progressBar.getHeight() - amountFull), this.progressBar.getComponentOrientation().isLeftToRight());
            SubstanceTextUtilities.paintVerticalText(g2, this.progressBar, renderRectangle, progressString, -1, this.progressBar.getFont(), this.getSelectionForeground(), new Rectangle(x2, fillStart, width, amountFull), this.progressBar.getComponentOrientation().isLeftToRight());
        }
    }

    protected Rectangle getStringRectangle(String progressString, int x2, int y2, int width, int height) {
        FontMetrics fontSizer = this.progressBar.getFontMetrics(this.progressBar.getFont());
        int stringWidth = fontSizer.stringWidth(progressString);
        if (this.progressBar.getOrientation() == 0) {
            return new Rectangle(x2 + Math.round(width / 2 - stringWidth / 2), y2 + (height - fontSizer.getHeight()) / 2, stringWidth, fontSizer.getHeight());
        }
        return new Rectangle(x2 + (width - fontSizer.getHeight()) / 2, y2 + Math.round(height / 2 - stringWidth / 2), fontSizer.getHeight(), stringWidth);
    }

    private final class SubstanceChangeListener
    implements ChangeListener {
        private SubstanceChangeListener() {
        }

        @Override
        public void stateChanged(ChangeEvent e2) {
            boolean isInCellRenderer;
            int pixelDelta;
            SubstanceCoreUtilities.testComponentStateChangeThreadingViolation(SubstanceProgressBarUI.this.progressBar);
            int currValue = SubstanceProgressBarUI.this.progressBar.getValue();
            int span = SubstanceProgressBarUI.this.progressBar.getMaximum() - SubstanceProgressBarUI.this.progressBar.getMinimum();
            int barRectWidth = SubstanceProgressBarUI.this.progressBar.getWidth() - 2 * SubstanceProgressBarUI.this.margin;
            int barRectHeight = SubstanceProgressBarUI.this.progressBar.getHeight() - 2 * SubstanceProgressBarUI.this.margin;
            int totalPixels = SubstanceProgressBarUI.this.progressBar.getOrientation() == 0 ? barRectWidth : barRectHeight;
            int n2 = pixelDelta = span <= 0 ? 0 : (currValue - SubstanceProgressBarUI.this.displayedValue) * totalPixels / span;
            if (SubstanceProgressBarUI.this.displayTimeline != null) {
                SubstanceProgressBarUI.this.displayTimeline.abort();
            }
            SubstanceProgressBarUI.this.displayTimeline = new Timeline(SubstanceProgressBarUI.this.progressBar);
            SubstanceProgressBarUI.this.displayTimeline.addPropertyToInterpolate(Timeline.property("displayedValue").from(SubstanceProgressBarUI.this.displayedValue).to(currValue).setWith(new TimelinePropertyBuilder.PropertySetter<Integer>(){

                @Override
                public void set(Object obj, String fieldName, Integer value) {
                    SubstanceProgressBarUI.this.displayedValue = value;
                    SubstanceProgressBarUI.this.progressBar.repaint();
                }
            }));
            SubstanceProgressBarUI.this.displayTimeline.setEase(new Spline(0.4f));
            AnimationConfigurationManager.getInstance().configureTimeline(SubstanceProgressBarUI.this.displayTimeline);
            boolean bl = isInCellRenderer = SwingUtilities.getAncestorOfClass(CellRendererPane.class, SubstanceProgressBarUI.this.progressBar) != null;
            if (!isInCellRenderer && Math.abs(pixelDelta) > 5) {
                SubstanceProgressBarUI.this.displayTimeline.play();
            } else {
                SubstanceProgressBarUI.this.displayedValue = currValue;
                SubstanceProgressBarUI.this.progressBar.repaint();
            }
        }
    }
}

