/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.internal.utils.SubstanceColorResource;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceSpinnerButton;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

public class SubstanceSpinnerUI
extends BasicSpinnerUI {
    protected Set lafWidgets;
    protected PropertyChangeListener substancePropertyChangeListener;
    protected SubstanceSpinnerButton nextButton;
    protected SubstanceSpinnerButton prevButton;

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceSpinnerUI();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__installUI(JComponent c2) {
        super.installUI(c2);
        c2.putClientProperty("substancelaf.internal.textComponentAware", new SubstanceCoreUtilities.TextComponentAware<JSpinner>(){

            @Override
            public JTextComponent getTextComponent(JSpinner t2) {
                JComponent editor = t2.getEditor();
                if (editor != null && editor instanceof JSpinner.DefaultEditor) {
                    return ((JSpinner.DefaultEditor)editor).getTextField();
                }
                return null;
            }
        });
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__uninstallUI(JComponent c2) {
        c2.putClientProperty("substancelaf.internal.textComponentAware", null);
        super.uninstallUI(c2);
    }

    @Override
    protected Component createNextButton() {
        this.nextButton = new SubstanceSpinnerButton(this.spinner, 1);
        this.nextButton.setFont(this.spinner.getFont());
        this.nextButton.setName("Spinner.nextButton");
        TransitionAwareIcon icon = new TransitionAwareIcon(this.nextButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSpinnerUI.this.nextButton);
                return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSpinnerArrowIconWidth(fontSize), SubstanceSizeUtils.getSpinnerArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 1, scheme);
            }
        }, "substance.spinner.nextButton");
        this.nextButton.setIcon(icon);
        int spinnerButtonSize = SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getComponentFontSize(this.spinner));
        this.nextButton.setPreferredSize(new Dimension(spinnerButtonSize, spinnerButtonSize));
        this.nextButton.setMinimumSize(new Dimension(5, 5));
        this.nextButton.putClientProperty("substancelaf.buttonopenSide", EnumSet.of(SubstanceConstants.Side.BOTTOM));
        this.nextButton.putClientProperty("substancelaf.buttonside", EnumSet.of(SubstanceConstants.Side.BOTTOM));
        this.installNextButtonListeners(this.nextButton);
        Color spinnerBg = this.spinner.getBackground();
        if (!(spinnerBg instanceof UIResource)) {
            this.nextButton.setBackground(spinnerBg);
        }
        return this.nextButton;
    }

    @Override
    protected Component createPreviousButton() {
        this.prevButton = new SubstanceSpinnerButton(this.spinner, 5);
        this.prevButton.setFont(this.spinner.getFont());
        this.prevButton.setName("Spinner.previousButton");
        TransitionAwareIcon icon = new TransitionAwareIcon(this.prevButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSpinnerUI.this.prevButton);
                float spinnerArrowIconHeight = SubstanceSizeUtils.getSpinnerArrowIconHeight(fontSize);
                return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSpinnerArrowIconWidth(fontSize), spinnerArrowIconHeight, SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 5, scheme);
            }
        }, "substance.spinner.prevButton");
        this.prevButton.setIcon(icon);
        int spinnerButtonSize = SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getComponentFontSize(this.prevButton));
        this.prevButton.setPreferredSize(new Dimension(spinnerButtonSize, spinnerButtonSize));
        this.prevButton.setMinimumSize(new Dimension(5, 5));
        this.prevButton.putClientProperty("substancelaf.buttonopenSide", EnumSet.of(SubstanceConstants.Side.TOP));
        this.prevButton.putClientProperty("substancelaf.buttonside", EnumSet.of(SubstanceConstants.Side.TOP));
        this.installPreviousButtonListeners(this.prevButton);
        Color spinnerBg = this.spinner.getBackground();
        if (!(spinnerBg instanceof UIResource)) {
            this.nextButton.setBackground(spinnerBg);
        }
        return this.prevButton;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__installDefaults() {
        Border b2;
        JFormattedTextField tf;
        super.installDefaults();
        JComponent editor = this.spinner.getEditor();
        if (editor != null && editor instanceof JSpinner.DefaultEditor && (tf = ((JSpinner.DefaultEditor)editor).getTextField()) != null) {
            int fontSize = SubstanceSizeUtils.getComponentFontSize(this.spinner);
            Insets ins = SubstanceSizeUtils.getSpinnerTextBorderInsets(fontSize);
            tf.setBorder(new EmptyBorder(ins.top, ins.left, ins.bottom, ins.right));
            tf.setFont(this.spinner.getFont());
            tf.setOpaque(false);
        }
        if (editor != null) {
            editor.setOpaque(false);
        }
        if ((b2 = this.spinner.getBorder()) == null || b2 instanceof UIResource) {
            this.spinner.setBorder(new SubstanceTextComponentBorder(SubstanceSizeUtils.getSpinnerBorderInsets(SubstanceSizeUtils.getComponentFontSize(this.spinner))));
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__installListeners() {
        super.installListeners();
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("editor".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            JFormattedTextField tf;
                            if (SubstanceSpinnerUI.this.spinner == null) {
                                return;
                            }
                            JComponent editor = SubstanceSpinnerUI.this.spinner.getEditor();
                            if (editor != null && editor instanceof JSpinner.DefaultEditor && (tf = ((JSpinner.DefaultEditor)editor).getTextField()) != null) {
                                Insets ins = SubstanceSizeUtils.getSpinnerTextBorderInsets(SubstanceSizeUtils.getComponentFontSize(SubstanceSpinnerUI.this.spinner));
                                tf.setBorder(new EmptyBorder(ins.top, ins.left, ins.bottom, ins.right));
                                tf.revalidate();
                            }
                        }
                    });
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceSpinnerUI.this.spinner != null) {
                                SubstanceSpinnerUI.this.spinner.updateUI();
                            }
                        }
                    });
                }
                if ("background".equals(evt.getPropertyName())) {
                    JFormattedTextField tf;
                    JComponent editor = SubstanceSpinnerUI.this.spinner.getEditor();
                    if (editor != null && editor instanceof JSpinner.DefaultEditor && (tf = ((JSpinner.DefaultEditor)editor).getTextField()) != null) {
                        Color tfBackground = tf.getBackground();
                        boolean canReplace = SubstanceCoreUtilities.canReplaceChildBackgroundColor(tfBackground);
                        if (SubstanceSpinnerUI.this.spinner.getBackground() == null) {
                            canReplace = false;
                        }
                        if (canReplace) {
                            tf.setBackground(new SubstanceColorResource(SubstanceSpinnerUI.this.spinner.getBackground()));
                        }
                    }
                    SubstanceSpinnerUI.this.nextButton.setBackground(SubstanceSpinnerUI.this.spinner.getBackground());
                    SubstanceSpinnerUI.this.prevButton.setBackground(SubstanceSpinnerUI.this.spinner.getBackground());
                }
            }
        };
        this.spinner.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__uninstallListeners() {
        this.spinner.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        super.uninstallListeners();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        super.paint(g2, c2);
        Graphics2D graphics = (Graphics2D)g2.create();
        int width = this.spinner.getWidth();
        int height = this.spinner.getHeight();
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(this.spinner);
        int borderDelta = (int)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize));
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, Math.max(0.0f, 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(componentFontSize) - (float)borderDelta), null, borderDelta);
        graphics.setColor(SubstanceTextUtilities.getTextBackgroundFillColor(this.spinner));
        graphics.fill(contour);
        graphics.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c2) {
        Dimension nextD = this.nextButton.getPreferredSize();
        Dimension previousD = this.prevButton.getPreferredSize();
        Dimension editorD = this.spinner.getEditor().getPreferredSize();
        Dimension size = new Dimension(editorD.width, editorD.height);
        size.width += Math.max(nextD.width, previousD.width);
        Insets insets = this.spinner.getInsets();
        size.width += insets.left + insets.right;
        size.height += insets.top + insets.bottom;
        return size;
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceSpinnerUI__update(Graphics g2, JComponent c2) {
        SubstanceTextUtilities.paintTextCompBackground(g2, c2);
        this.paint(g2, c2);
    }

    @Override
    protected LayoutManager createLayout() {
        return new SpinnerLayoutManager();
    }

    protected class SpinnerLayoutManager
    implements LayoutManager {
        protected SpinnerLayoutManager() {
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return this.preferredLayoutSize(parent);
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension nextD = SubstanceSpinnerUI.this.nextButton.getPreferredSize();
            Dimension previousD = SubstanceSpinnerUI.this.prevButton.getPreferredSize();
            Dimension editorD = SubstanceSpinnerUI.this.spinner.getEditor().getPreferredSize();
            editorD.height = (editorD.height + 1) / 2 * 2;
            Dimension size = new Dimension(editorD.width, editorD.height);
            size.width += Math.max(nextD.width, previousD.width);
            Insets insets = parent.getInsets();
            size.width += insets.left + insets.right;
            size.height += insets.top + insets.bottom;
            Insets buttonInsets = SubstanceSizeUtils.getSpinnerArrowButtonInsets(SubstanceSizeUtils.getComponentFontSize(SubstanceSpinnerUI.this.spinner));
            size.width += buttonInsets.left + buttonInsets.right;
            return size;
        }

        @Override
        public void layoutContainer(Container parent) {
            int buttonsX;
            int editorWidth;
            int editorX;
            int width = parent.getWidth();
            int height = parent.getHeight();
            Insets insets = parent.getInsets();
            Dimension nextD = SubstanceSpinnerUI.this.nextButton.getPreferredSize();
            Dimension previousD = SubstanceSpinnerUI.this.prevButton.getPreferredSize();
            int buttonsWidth = Math.max(nextD.width, previousD.width);
            int editorHeight = height - (insets.top + insets.bottom);
            Insets buttonInsets = SubstanceSizeUtils.getSpinnerArrowButtonInsets(SubstanceSizeUtils.getComponentFontSize(SubstanceSpinnerUI.this.spinner));
            if (parent.getComponentOrientation().isLeftToRight()) {
                editorX = insets.left;
                editorWidth = width - insets.left - buttonsWidth;
                buttonsX = width - buttonsWidth;
            } else {
                buttonsX = 0;
                editorX = buttonsX + buttonsWidth;
                editorWidth = width - editorX - insets.right;
            }
            int nextY = 0;
            int nextHeight = height / 2 + height % 2 - nextY;
            int previousY = 0 * buttonInsets.top + nextHeight;
            int previousHeight = height - previousY;
            SubstanceSpinnerUI.this.spinner.getEditor().setBounds(editorX, insets.top, editorWidth, editorHeight);
            SubstanceSpinnerUI.this.nextButton.setBounds(buttonsX, nextY, buttonsWidth, nextHeight);
            SubstanceSpinnerUI.this.prevButton.setBounds(buttonsX, previousY, buttonsWidth, previousHeight);
        }
    }
}

