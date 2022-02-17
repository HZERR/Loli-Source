/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaUtilities;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.VisualMargin;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;
import org.pushingpixels.substance.internal.utils.RolloverControlListener;

public class ColorSliderUI
extends BasicSliderUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    private static final Color foreground = new Color(0x949494);
    private static final Color trackBackground = new Color(0xFFFFFF);
    protected Integer componentIndex;
    protected ColorSliderModel colorSliderModel;
    private ButtonModel thumbModel = new DefaultButtonModel();
    private RolloverControlListener substanceRolloverListener;
    private PropertyChangeListener substancePropertyChangeListener;
    protected StateTransitionTracker stateTransitionTracker;
    private static final Dimension PREFERRED_HORIZONTAL_SIZE = new Dimension(36, 16);
    private static final Dimension PREFERRED_VERTICAL_SIZE = new Dimension(26, 100);
    private static final Dimension MINIMUM_HORIZONTAL_SIZE = new Dimension(36, 16);
    private static final Dimension MINIMUM_VERTICAL_SIZE = new Dimension(26, 36);

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__installListeners(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__installDefaults(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__uninstallListeners(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__uninstallDefaults(JSlider jSlider) {
        super.uninstallDefaults(jSlider);
    }

    @Override
    protected void uninstallDefaults(JSlider jSlider) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__uninstallDefaults(jSlider);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public ColorSliderUI(JSlider b2) {
        super(b2);
        this.thumbModel.setArmed(false);
        this.thumbModel.setSelected(false);
        this.thumbModel.setPressed(false);
        this.thumbModel.setRollover(false);
        this.thumbModel.setEnabled(b2.isEnabled());
        this.stateTransitionTracker = new StateTransitionTracker(b2, this.thumbModel);
        b2.setLabelTable(new Hashtable());
    }

    public static ComponentUI createUI(JComponent b2) {
        return new ColorSliderUI((JSlider)b2);
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__installDefaults(JSlider slider) {
        super.installDefaults(slider);
        this.focusInsets = new Insets(0, 0, 0, 0);
        slider.setOpaque(false);
        if (slider.getOrientation() == 0) {
            slider.setBorder(new VisualMargin(0, 1, -1, 1));
        } else {
            slider.setBorder(new VisualMargin(0, 0, 0, 1));
        }
        slider.setRequestFocusEnabled(true);
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__installListeners(final JSlider slider) {
        super.installListeners(slider);
        this.substanceRolloverListener = new RolloverControlListener(this, this.thumbModel);
        slider.addMouseListener(this.substanceRolloverListener);
        slider.addMouseMotionListener(this.substanceRolloverListener);
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    ColorSliderUI.this.thumbModel.setEnabled(slider.isEnabled());
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

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__colorchooser__ColorSliderUI__uninstallListeners(JSlider slider) {
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
    protected Dimension getThumbSize() {
        Icon thumb = this.getThumbIcon();
        return new Dimension(thumb.getIconWidth(), thumb.getIconHeight());
    }

    @Override
    public Dimension getPreferredHorizontalSize() {
        return PREFERRED_HORIZONTAL_SIZE;
    }

    @Override
    public Dimension getPreferredVerticalSize() {
        return PREFERRED_VERTICAL_SIZE;
    }

    @Override
    public Dimension getMinimumHorizontalSize() {
        return MINIMUM_HORIZONTAL_SIZE;
    }

    @Override
    public Dimension getMinimumVerticalSize() {
        return MINIMUM_VERTICAL_SIZE;
    }

    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (this.slider.getOrientation() == 0) {
            --this.thumbRect.y;
        } else {
            --this.thumbRect.x;
        }
    }

    protected Icon getThumbIcon() {
        if (this.slider.getOrientation() == 0) {
            return UIManager.getIcon("Slider.upThumbSmall");
        }
        return UIManager.getIcon("Slider.leftThumbSmall");
    }

    @Override
    public void paintThumb(Graphics g2) {
        Rectangle knobBounds = this.thumbRect;
        int w2 = knobBounds.width;
        int h2 = knobBounds.height;
        this.getThumbIcon().paintIcon(this.slider, g2, knobBounds.x, knobBounds.y);
    }

    @Override
    public void paintTrack(Graphics g2) {
        int ch;
        int cw;
        int cy;
        int cx;
        Rectangle trackBounds = this.trackRect;
        if (this.slider.getOrientation() == 0) {
            int pad = this.trackBuffer;
            cx = trackBounds.x - pad + 1;
            cy = trackBounds.y;
            cw = trackBounds.width + pad * 2 - 2;
            ch = trackBounds.height;
        } else {
            int pad = this.trackBuffer;
            cx = trackBounds.x;
            cy = this.contentRect.y + 2;
            cw = trackBounds.width - 1;
            ch = trackBounds.height + pad * 2 - 5;
        }
        g2.setColor(trackBackground);
        g2.fillRect(cx, cy, cw, ch);
        g2.setColor(foreground);
        g2.drawRect(cx, cy, cw - 1, ch - 1);
        this.paintColorTrack(g2, cx + 2, cy + 2, cw - 4, ch - 4, this.trackBuffer);
    }

    @Override
    public void paintTicks(Graphics g2) {
        Rectangle tickBounds = this.tickRect;
        int w2 = tickBounds.width;
        int h2 = tickBounds.height;
        g2.setColor(foreground);
        if (this.slider.getOrientation() == 0) {
            int xPos;
            int value;
            g2.translate(0, tickBounds.y);
            if (this.slider.getMinorTickSpacing() > 0) {
                for (value = this.slider.getMinimum(); value <= this.slider.getMaximum(); value += this.slider.getMinorTickSpacing()) {
                    xPos = this.xPositionForValue(value);
                    this.paintMinorTickForHorizSlider(g2, tickBounds, xPos);
                }
            }
            if (this.slider.getMajorTickSpacing() > 0) {
                for (value = this.slider.getMinimum(); value <= this.slider.getMaximum(); value += this.slider.getMajorTickSpacing()) {
                    xPos = this.xPositionForValue(value);
                    this.paintMajorTickForHorizSlider(g2, tickBounds, xPos);
                }
            }
            g2.translate(0, -tickBounds.y);
        } else {
            int yPos;
            g2.translate(tickBounds.x, 0);
            int value = this.slider.getMinimum();
            if (this.slider.getMinorTickSpacing() > 0) {
                int offset = 0;
                if (!QuaquaUtilities.isLeftToRight(this.slider)) {
                    offset = tickBounds.width - tickBounds.width / 2;
                    g2.translate(offset, 0);
                }
                while (value <= this.slider.getMaximum()) {
                    yPos = this.yPositionForValue(value);
                    this.paintMinorTickForVertSlider(g2, tickBounds, yPos);
                    value += this.slider.getMinorTickSpacing();
                }
                if (!QuaquaUtilities.isLeftToRight(this.slider)) {
                    g2.translate(-offset, 0);
                }
            }
            if (this.slider.getMajorTickSpacing() > 0) {
                value = this.slider.getMinimum();
                if (!QuaquaUtilities.isLeftToRight(this.slider)) {
                    g2.translate(2, 0);
                }
                while (value <= this.slider.getMaximum()) {
                    yPos = this.yPositionForValue(value);
                    this.paintMajorTickForVertSlider(g2, tickBounds, yPos);
                    value += this.slider.getMajorTickSpacing();
                }
                if (!QuaquaUtilities.isLeftToRight(this.slider)) {
                    g2.translate(-2, 0);
                }
            }
            g2.translate(-tickBounds.x, 0);
        }
    }

    @Override
    protected void paintMajorTickForHorizSlider(Graphics g2, Rectangle tickBounds, int x2) {
        g2.drawLine(x2, 0, x2, tickBounds.height - 1);
    }

    @Override
    protected void paintMinorTickForHorizSlider(Graphics g2, Rectangle tickBounds, int x2) {
        g2.drawLine(x2, 0, x2, tickBounds.height - 1);
    }

    @Override
    protected void paintMinorTickForVertSlider(Graphics g2, Rectangle tickBounds, int y2) {
        g2.drawLine(tickBounds.width / 2, y2, tickBounds.width / 2 - 1, y2);
    }

    @Override
    protected void paintMajorTickForVertSlider(Graphics g2, Rectangle tickBounds, int y2) {
        g2.drawLine(0, y2, tickBounds.width - 1, y2);
    }

    @Override
    public void paintFocus(Graphics g2) {
    }

    public void paintColorTrack(Graphics g2, int x2, int y2, int width, int height, int buffer) {
        int x22 = x2;
        int y22 = y2;
        if (this.slider.getOrientation() == 0) {
            x22 += width;
        } else {
            y22 += height;
        }
        if (this.componentIndex == null) {
            this.componentIndex = (Integer)this.slider.getClientProperty("ColorComponentIndex");
        }
        if (this.colorSliderModel == null) {
            this.colorSliderModel = (ColorSliderModel)this.slider.getClientProperty("ColorSliderModel");
        }
        float[] rgbRatios = this.slider.getOrientation() == 0 ? new float[]{0.0f, 1.0f} : new float[]{1.0f, 0.0f};
        Graphics2D gg = (Graphics2D)g2.create();
        gg.setPaint(new LinearGradientPaint(x2, y2, x22, y22, new float[]{0.0f, 1.0f}, new Color[]{new Color(this.colorSliderModel.getInterpolatedRGB(this.componentIndex, rgbRatios[0]), true), new Color(this.colorSliderModel.getInterpolatedRGB(this.componentIndex, rgbRatios[1]))}));
        gg.fillRect(x2, y2, width, height);
        gg.dispose();
    }

    @Override
    protected void calculateTrackRect() {
        if (this.slider.getOrientation() == 0) {
            int centerSpacing = this.thumbRect.height;
            if (this.slider.getPaintTicks()) {
                centerSpacing += this.getTickLength();
            }
            if (this.slider.getPaintLabels()) {
                centerSpacing += this.getHeightOfTallestLabel();
            }
            this.trackRect.x = this.contentRect.x + this.trackBuffer + 1;
            this.trackRect.height = 13;
            this.trackRect.y = this.contentRect.y + this.contentRect.height - this.trackRect.height;
            this.trackRect.width = this.contentRect.width - this.trackBuffer * 2 - 1;
        } else {
            this.trackRect.width = 14;
            this.trackRect.x = this.contentRect.x + this.contentRect.width - this.trackRect.width;
            this.trackRect.y = this.contentRect.y + this.trackBuffer;
            this.trackRect.height = this.contentRect.height - this.trackBuffer * 2 + 1;
        }
    }

    @Override
    protected void calculateTickRect() {
        if (this.slider.getOrientation() == 0) {
            this.tickRect.x = this.trackRect.x;
            this.tickRect.y = this.trackRect.y - this.getTickLength();
            this.tickRect.width = this.trackRect.width;
            this.tickRect.height = this.getTickLength();
            if (!this.slider.getPaintTicks()) {
                --this.tickRect.y;
                this.tickRect.height = 0;
            }
        } else {
            this.tickRect.width = this.getTickLength();
            this.tickRect.x = this.contentRect.x;
            this.tickRect.y = this.trackRect.y;
            this.tickRect.height = this.trackRect.height;
            if (!this.slider.getPaintTicks()) {
                --this.tickRect.x;
                this.tickRect.width = 0;
            }
        }
    }

    @Override
    protected int getTickLength() {
        return 4;
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener(JSlider slider) {
        return new CSUIPropertyChangeHandler();
    }

    @Override
    protected BasicSliderUI.TrackListener createTrackListener(JSlider slider) {
        return new QuaquaTrackListener();
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

    public class QuaquaTrackListener
    extends BasicSliderUI.TrackListener {
        public QuaquaTrackListener() {
            super(ColorSliderUI.this);
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            if (!ColorSliderUI.this.slider.isEnabled()) {
                return;
            }
            this.currentMouseX = e2.getX();
            this.currentMouseY = e2.getY();
            if (ColorSliderUI.this.slider.isRequestFocusEnabled()) {
                ColorSliderUI.this.slider.requestFocus();
            }
            if (ColorSliderUI.this.thumbRect.contains(this.currentMouseX, this.currentMouseY)) {
                super.mousePressed(e2);
            } else {
                Dimension sbSize = ColorSliderUI.this.slider.getSize();
                boolean direction = true;
                switch (ColorSliderUI.this.slider.getOrientation()) {
                    case 1: {
                        ColorSliderUI.this.slider.setValue(ColorSliderUI.this.valueForYPosition(this.currentMouseY));
                        break;
                    }
                    case 0: {
                        ColorSliderUI.this.slider.setValue(ColorSliderUI.this.valueForXPosition(this.currentMouseX));
                    }
                }
            }
        }
    }

    public class CSUIPropertyChangeHandler
    extends BasicSliderUI.PropertyChangeHandler {
        public CSUIPropertyChangeHandler() {
            super(ColorSliderUI.this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent e2) {
            String propertyName = e2.getPropertyName();
            if (propertyName.equals("Frame.active")) {
                ColorSliderUI.this.slider.repaint();
            } else if (propertyName.equals("ColorSliderModel")) {
                ColorSliderUI.this.colorSliderModel = (ColorSliderModel)e2.getNewValue();
                ColorSliderUI.this.slider.repaint();
            } else if (propertyName.equals("snapToTicks")) {
                ColorSliderUI.this.slider.repaint();
            } else if (propertyName.equals("ColorComponentIndex")) {
                ColorSliderUI.this.componentIndex = (Integer)e2.getNewValue();
                ColorSliderUI.this.slider.repaint();
            } else if (propertyName.equals("ColorComponentChange")) {
                Integer value = (Integer)e2.getNewValue();
                ColorSliderUI.this.slider.repaint();
            } else if (propertyName.equals("ColorComponentValue")) {
                ColorSliderUI.this.slider.repaint();
            } else if (propertyName.equals("Orientation")) {
                if (ColorSliderUI.this.slider.getOrientation() == 0) {
                    ColorSliderUI.this.slider.setBorder(new VisualMargin(0, 1, -1, 1));
                } else {
                    ColorSliderUI.this.slider.setBorder(new VisualMargin(0, 0, 0, 1));
                }
            }
            super.propertyChange(e2);
        }
    }
}

