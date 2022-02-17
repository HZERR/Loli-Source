/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultComboBoxRenderer;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.RolloverTextControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceDropDownButton;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;
import org.pushingpixels.substance.internal.utils.combo.ComboBoxBackgroundDelegate;
import org.pushingpixels.substance.internal.utils.combo.SubstanceComboBoxEditor;
import org.pushingpixels.substance.internal.utils.combo.SubstanceComboPopup;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

public class SubstanceComboBoxUI
extends BasicComboBoxUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    protected ComboBoxPropertyChangeHandler substanceChangeHandler;
    protected StateTransitionTracker stateTransitionTracker;
    protected ButtonModel transitionModel;
    protected RolloverTextControlListener substanceRolloverListener;
    protected ComboBoxBackgroundDelegate delegate;
    protected Icon uneditableArrowIcon;
    protected Insets layoutInsets;

    protected void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installComponents() {
        super.installComponents();
    }

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallComponents() {
        super.uninstallComponents();
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        SubstanceComboBoxUI ui = new SubstanceComboBoxUI((JComboBox)comp);
        ui.comboBox = (JComboBox)comp;
        ui.comboBox.setOpaque(false);
        return ui;
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installUI(JComponent c2) {
        super.installUI(c2);
        c2.putClientProperty("substancelaf.internal.textComponentAware", new SubstanceCoreUtilities.TextComponentAware<JComboBox>(){

            @Override
            public JTextComponent getTextComponent(JComboBox t2) {
                Component editorComp;
                if (t2.isEditable() && (editorComp = t2.getEditor().getEditorComponent()) instanceof JTextComponent) {
                    return (JTextComponent)editorComp;
                }
                return null;
            }
        });
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallUI(JComponent c2) {
        c2.putClientProperty("substancelaf.internal.textComponentAware", null);
        super.uninstallUI(c2);
    }

    public SubstanceComboBoxUI(JComboBox combo) {
        this.comboBox = combo;
        this.transitionModel = new DefaultButtonModel();
        this.transitionModel.setArmed(false);
        this.transitionModel.setSelected(false);
        this.transitionModel.setPressed(false);
        this.transitionModel.setRollover(false);
        this.transitionModel.setEnabled(combo.isEnabled());
        this.stateTransitionTracker = new StateTransitionTracker(this.comboBox, this.transitionModel);
        this.delegate = new ComboBoxBackgroundDelegate();
    }

    @Override
    protected JButton createArrowButton() {
        SubstanceDropDownButton result = new SubstanceDropDownButton(this.comboBox);
        result.setFont(this.comboBox.getFont());
        result.setIcon(this.getCurrentIcon(result));
        return result;
    }

    private Icon getCurrentIcon(JButton button) {
        Icon icon = SubstanceCoreUtilities.getArrowIcon(button, SubstanceCoreUtilities.getPopupFlyoutOrientation(this.comboBox));
        return icon;
    }

    @Override
    protected ListCellRenderer createRenderer() {
        return new SubstanceDefaultComboBoxRenderer.SubstanceUIResource(this.comboBox);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installListeners() {
        super.installListeners();
        this.substanceChangeHandler = new ComboBoxPropertyChangeHandler();
        this.comboBox.addPropertyChangeListener(this.substanceChangeHandler);
        this.substanceRolloverListener = new RolloverTextControlListener(this.comboBox, this, this.transitionModel);
        this.substanceRolloverListener.registerListeners();
        this.stateTransitionTracker.registerModelListeners();
        this.stateTransitionTracker.registerFocusListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__uninstallListeners() {
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
        this.comboBox.removePropertyChangeListener(this.substanceChangeHandler);
        this.substanceChangeHandler = null;
        this.substanceRolloverListener.unregisterListeners();
        this.substanceRolloverListener = null;
        super.uninstallListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceComboBoxUI__installDefaults() {
        super.installDefaults();
        this.uneditableArrowIcon = SubstanceCoreUtilities.getArrowIcon(this.comboBox, new TransitionAwareIcon.TransitionAwareUIDelegate(){

            @Override
            public TransitionAwareUI getTransitionAwareUI() {
                return (TransitionAwareUI)((Object)SubstanceComboBoxUI.this.comboBox.getUI());
            }
        }, SubstanceCoreUtilities.getPopupFlyoutOrientation(this.comboBox));
        this.updateComboBoxBorder();
    }

    @Override
    protected LayoutManager createLayoutManager() {
        return new SubstanceComboBoxLayoutManager();
    }

    @Override
    protected Rectangle rectangleForCurrentValue() {
        int width = this.comboBox.getWidth();
        int height = this.comboBox.getHeight();
        Insets insets = this.layoutInsets;
        int buttonWidth = SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getComponentFontSize(this.comboBox));
        if (this.comboBox.getComponentOrientation().isLeftToRight()) {
            return new Rectangle(insets.left, insets.top, width - insets.left - insets.right - buttonWidth, height - insets.top - insets.bottom);
        }
        int startX = insets.left + buttonWidth;
        return new Rectangle(startX, insets.top, width - startX - insets.right, height - insets.top - insets.bottom);
    }

    @Override
    protected Dimension getDefaultSize() {
        Component rend = new SubstanceDefaultComboBoxRenderer(this.comboBox).getListCellRendererComponent(this.listBox, (Object)" ", -1, false, false);
        rend.setFont(this.comboBox.getFont());
        return rend.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize(JComponent c2) {
        if (!this.isMinimumSizeDirty) {
            return new Dimension(this.cachedMinimumSize);
        }
        SubstanceDropDownButton button = (SubstanceDropDownButton)this.arrowButton;
        Insets buttonInsets = button.getInsets();
        Insets insets = this.comboBox.getInsets();
        Dimension size = this.getDisplaySize();
        size.width += insets.left + insets.right;
        size.width += buttonInsets.left + buttonInsets.right;
        size.width = (int)((double)size.width + button.getMinimumSize().getWidth());
        size.height += insets.top + insets.bottom;
        this.cachedMinimumSize.setSize(size.width, size.height);
        this.isMinimumSizeDirty = false;
        return new Dimension(this.cachedMinimumSize);
    }

    @Override
    protected ComboPopup createPopup() {
        final SubstanceComboPopup sPopup = new SubstanceComboPopup(this.comboBox);
        final ComponentOrientation currOrientation = this.comboBox.getComponentOrientation();
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                ComboBoxEditor editor;
                ListCellRenderer cellRenderer;
                if (SubstanceComboBoxUI.this.comboBox == null) {
                    return;
                }
                if (sPopup instanceof Component) {
                    Component cPopup = (Component)((Object)sPopup);
                    cPopup.applyComponentOrientation(currOrientation);
                    cPopup.doLayout();
                }
                if ((cellRenderer = SubstanceComboBoxUI.this.comboBox.getRenderer()) instanceof Component) {
                    ((Component)((Object)cellRenderer)).applyComponentOrientation(currOrientation);
                }
                if ((editor = SubstanceComboBoxUI.this.comboBox.getEditor()) != null && editor.getEditorComponent() != null) {
                    editor.getEditorComponent().applyComponentOrientation(currOrientation);
                }
                SubstanceComboBoxUI.this.comboBox.repaint();
            }
        });
        return sPopup;
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        Graphics2D graphics = (Graphics2D)g2.create();
        int width = this.comboBox.getWidth();
        int height = this.comboBox.getHeight();
        Insets insets = this.comboBox.getInsets();
        int componentFontSize = SubstanceSizeUtils.getComponentFontSize(this.comboBox);
        if (this.comboBox.isEditable()) {
            int borderDelta = (int)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(componentFontSize));
            GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width, height, Math.max(0.0f, 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(componentFontSize) - (float)borderDelta), null, borderDelta);
            graphics.setColor(SubstanceTextUtilities.getTextBackgroundFillColor(this.comboBox));
            graphics.fill(contour);
        } else {
            this.delegate.updateBackground(graphics, this.comboBox, this.transitionModel);
            Icon icon = this.uneditableArrowIcon;
            int iw = icon.getIconWidth();
            int ih = icon.getIconHeight();
            int origButtonWidth = SubstanceSizeUtils.getScrollBarWidth(componentFontSize);
            if (this.comboBox.getComponentOrientation().isLeftToRight()) {
                int iconX = width - origButtonWidth - insets.right / 2 + (origButtonWidth - iw) / 2;
                int iconY = insets.top + (height - insets.top - insets.bottom - ih) / 2;
                icon.paintIcon(this.comboBox, graphics, iconX, iconY);
            } else {
                int iconX = insets.left / 2 + (origButtonWidth - iw) / 2;
                int iconY = insets.top + (height - insets.top - insets.bottom - ih) / 2;
                icon.paintIcon(this.comboBox, graphics, iconX, iconY);
            }
        }
        this.hasFocus = this.comboBox.hasFocus();
        if (!this.comboBox.isEditable()) {
            Rectangle r2 = this.rectangleForCurrentValue();
            ListCellRenderer<Object> renderer = this.comboBox.getRenderer();
            Component rendererComponent = this.hasFocus ? renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, this.hasFocus) : renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, this.hasFocus);
            rendererComponent.setFont(this.comboBox.getFont());
            boolean shouldValidate = false;
            if (rendererComponent instanceof JPanel) {
                shouldValidate = true;
            }
            if (this.comboBox.getComponentOrientation().isLeftToRight()) {
                this.currentValuePane.paintComponent(graphics, rendererComponent, this.comboBox, r2.x, r2.y, r2.width, r2.height, shouldValidate);
            } else {
                this.currentValuePane.paintComponent(graphics, rendererComponent, this.comboBox, r2.x, r2.y, r2.width, r2.height, shouldValidate);
            }
        }
        if (!this.comboBox.isEditable()) {
            Rectangle r3 = new Rectangle(insets.left, this.layoutInsets.top, width - insets.left - insets.right, height - this.layoutInsets.top - this.layoutInsets.bottom);
            this.paintFocus(graphics, r3);
        }
        graphics.dispose();
    }

    protected void paintFocus(Graphics g2, Rectangle bounds) {
        int fontSize = SubstanceSizeUtils.getComponentFontSize(this.comboBox);
        int focusRingPadding = SubstanceSizeUtils.getFocusRingPadding(fontSize) / 2;
        int x2 = bounds.x;
        int y2 = bounds.y;
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.translate(x2, y2);
        SubstanceCoreUtilities.paintFocus(g2d, this.comboBox, this.comboBox, this, SubstanceOutlineUtilities.getBaseOutline(bounds.width, bounds.height, SubstanceSizeUtils.getClassicButtonCornerRadius(fontSize), null, 0), bounds, 1.0f, focusRingPadding);
        g2d.dispose();
    }

    public ComboPopup getPopup() {
        return this.popup;
    }

    @Override
    public void configureArrowButton() {
        super.configureArrowButton();
        this.arrowButton.setFocusable(false);
        this.configureArrowButtonStraightSide();
    }

    protected void configureArrowButtonStraightSide() {
        this.arrowButton.putClientProperty("substancelaf.buttonside", (Object)(this.comboBox.getComponentOrientation().isLeftToRight() ? SubstanceConstants.Side.LEFT : SubstanceConstants.Side.RIGHT));
    }

    @Override
    public void unconfigureArrowButton() {
        super.unconfigureArrowButton();
    }

    @Override
    protected void configureEditor() {
        super.configureEditor();
        if (this.editor instanceof JComponent) {
            Insets ins = SubstanceSizeUtils.getComboTextBorderInsets(SubstanceSizeUtils.getComponentFontSize(this.editor));
            ((JComponent)this.editor).setBorder(new EmptyBorder(ins.top, ins.left, ins.bottom, ins.right));
            this.editor.setBackground(this.comboBox.getBackground());
        }
    }

    @Override
    protected ComboBoxEditor createEditor() {
        return new SubstanceComboBoxEditor.UIResource();
    }

    private void updateComboBoxBorder() {
        Border b2 = this.comboBox.getBorder();
        if (b2 == null || b2 instanceof UIResource) {
            int comboFontSize = SubstanceSizeUtils.getComponentFontSize(this.comboBox);
            Insets comboBorderInsets = SubstanceSizeUtils.getComboBorderInsets(comboFontSize);
            if (this.comboBox.isEditable()) {
                this.comboBox.setBorder(new SubstanceTextComponentBorder(comboBorderInsets));
            } else {
                this.comboBox.setBorder(new BorderUIResource.EmptyBorderUIResource(comboBorderInsets));
                this.comboBox.setPrototypeDisplayValue(this.comboBox.getPrototypeDisplayValue());
            }
            this.layoutInsets = SubstanceSizeUtils.getComboLayoutInsets(comboFontSize);
        } else {
            this.layoutInsets = new Insets(0, 0, 0, 0);
        }
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }

    @Override
    public boolean isInside(MouseEvent me) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return false;
        }
        ClassicButtonShaper shaper = ClassicButtonShaper.INSTANCE;
        if (shaper == null) {
            return false;
        }
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(this.comboBox, SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(this.comboBox)), null);
        return contour.contains(me.getPoint());
    }

    public class ComboBoxPropertyChangeHandler
    extends BasicComboBoxUI.PropertyChangeHandler {
        public ComboBoxPropertyChangeHandler() {
            super(SubstanceComboBoxUI.this);
        }

        @Override
        public void propertyChange(final PropertyChangeEvent e2) {
            String propertyName = e2.getPropertyName();
            if (propertyName.equals("componentOrientation")) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (SubstanceComboBoxUI.this.comboBox == null) {
                            return;
                        }
                        ComponentOrientation newOrientation = (ComponentOrientation)e2.getNewValue();
                        ListCellRenderer cellRenderer = SubstanceComboBoxUI.this.comboBox.getRenderer();
                        ComboBoxEditor editor = SubstanceComboBoxUI.this.comboBox.getEditor();
                        if (SubstanceComboBoxUI.this.popup instanceof Component) {
                            Component cPopup = (Component)((Object)SubstanceComboBoxUI.this.popup);
                            cPopup.applyComponentOrientation(newOrientation);
                            cPopup.doLayout();
                        }
                        if (cellRenderer instanceof Component) {
                            ((Component)((Object)cellRenderer)).applyComponentOrientation(newOrientation);
                        }
                        if (editor != null && editor.getEditorComponent() != null) {
                            editor.getEditorComponent().applyComponentOrientation(newOrientation);
                        }
                        if (SubstanceComboBoxUI.this.comboBox != null) {
                            SubstanceComboBoxUI.this.comboBox.repaint();
                        }
                        SubstanceComboBoxUI.this.configureArrowButtonStraightSide();
                    }
                });
            }
            if ("substancelaf.comboboxpopupFlyoutOrientation".equals(propertyName)) {
                ((SubstanceDropDownButton)SubstanceComboBoxUI.this.arrowButton).setIcon(SubstanceCoreUtilities.getArrowIcon(SubstanceComboBoxUI.this.arrowButton, SubstanceCoreUtilities.getPopupFlyoutOrientation(SubstanceComboBoxUI.this.comboBox)));
            }
            if ("font".equals(propertyName)) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (SubstanceComboBoxUI.this.comboBox != null) {
                            SubstanceComboBoxUI.this.comboBox.updateUI();
                        }
                    }
                });
            }
            if ("background".equals(propertyName) && SubstanceComboBoxUI.this.comboBox.isEditable()) {
                SubstanceComboBoxUI.this.comboBox.getEditor().getEditorComponent().setBackground(SubstanceComboBoxUI.this.comboBox.getBackground());
                SubstanceComboBoxUI.this.popup.getList().setBackground(SubstanceComboBoxUI.this.comboBox.getBackground());
            }
            if ("editable".equals(propertyName)) {
                SubstanceComboBoxUI.this.updateComboBoxBorder();
                SubstanceComboBoxUI.this.isMinimumSizeDirty = true;
            }
            if ("enabled".equals(propertyName)) {
                SubstanceComboBoxUI.this.transitionModel.setEnabled(SubstanceComboBoxUI.this.comboBox.isEnabled());
            }
        }
    }

    private class SubstanceComboBoxLayoutManager
    extends BasicComboBoxUI.ComboBoxLayoutManager {
        private SubstanceComboBoxLayoutManager() {
            super(SubstanceComboBoxUI.this);
        }

        @Override
        public void layoutContainer(Container parent) {
            JComboBox cb = (JComboBox)parent;
            int width = cb.getWidth();
            int height = cb.getHeight();
            Insets insets = SubstanceComboBoxUI.this.layoutInsets;
            int buttonWidth = SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getComponentFontSize(SubstanceComboBoxUI.this.comboBox));
            if (SubstanceComboBoxUI.this.arrowButton != null) {
                if (!SubstanceComboBoxUI.this.comboBox.isEditable()) {
                    SubstanceComboBoxUI.this.arrowButton.setBounds(0, 0, 0, 0);
                } else if (cb.getComponentOrientation().isLeftToRight()) {
                    SubstanceComboBoxUI.this.arrowButton.setBounds(width - buttonWidth - insets.right, 0, buttonWidth + insets.right, height);
                } else {
                    SubstanceComboBoxUI.this.arrowButton.setBounds(0, 0, buttonWidth + insets.left, height);
                }
            }
            if (SubstanceComboBoxUI.this.editor != null) {
                Rectangle r2 = SubstanceComboBoxUI.this.rectangleForCurrentValue();
                SubstanceComboBoxUI.this.editor.setBounds(r2);
            }
        }
    }
}

