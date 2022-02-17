/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.RolloverTextControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class SubstanceFormattedTextFieldUI
extends BasicFormattedTextFieldUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    protected StateTransitionTracker stateTransitionTracker;
    protected JFormattedTextField textField;
    protected PropertyChangeListener substancePropertyChangeListener;
    private RolloverTextControlListener substanceRolloverListener;
    private ButtonModel transitionModel;

    public void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceFormattedTextFieldUI(comp);
    }

    public SubstanceFormattedTextFieldUI(JComponent c2) {
        this.textField = (JFormattedTextField)c2;
        this.transitionModel = new DefaultButtonModel();
        this.transitionModel.setArmed(false);
        this.transitionModel.setSelected(false);
        this.transitionModel.setPressed(false);
        this.transitionModel.setRollover(false);
        this.transitionModel.setEnabled(this.textField.isEnabled());
        this.stateTransitionTracker = new StateTransitionTracker(this.textField, this.transitionModel);
        this.stateTransitionTracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

            @Override
            public SwingRepaintCallback getRepaintCallback() {
                return SubstanceCoreUtilities.getTextComponentRepaintCallback(SubstanceFormattedTextFieldUI.this.textField);
            }
        });
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__installListeners() {
        super.installListeners();
        this.substanceRolloverListener = new RolloverTextControlListener(this.textField, this, this.transitionModel);
        this.substanceRolloverListener.registerListeners();
        this.stateTransitionTracker.registerModelListeners();
        this.stateTransitionTracker.registerFocusListeners();
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            int caretPos = SubstanceFormattedTextFieldUI.this.textField.getCaretPosition();
                            SubstanceFormattedTextFieldUI.this.textField.updateUI();
                            SubstanceFormattedTextFieldUI.this.textField.setCaretPosition(caretPos);
                            Container parent = SubstanceFormattedTextFieldUI.this.textField.getParent();
                            if (parent != null) {
                                parent.invalidate();
                                parent.validate();
                            }
                        }
                    });
                }
                if ("enabled".equals(evt.getPropertyName())) {
                    SubstanceFormattedTextFieldUI.this.transitionModel.setEnabled(SubstanceFormattedTextFieldUI.this.textField.isEnabled());
                }
            }
        };
        this.textField.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__uninstallListeners() {
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
        this.textField.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        this.substanceRolloverListener.unregisterListeners();
        this.substanceRolloverListener = null;
        super.uninstallListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFormattedTextFieldUI__installDefaults() {
        super.installDefaults();
        Border b2 = this.textField.getBorder();
        if (b2 == null || b2 instanceof UIResource) {
            BorderUIResource.CompoundBorderUIResource newB = new BorderUIResource.CompoundBorderUIResource(new SubstanceTextComponentBorder(SubstanceSizeUtils.getTextBorderInsets(SubstanceSizeUtils.getComponentFontSize(this.textField))), new BasicBorders.MarginBorder());
            this.textField.setBorder(newB);
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                if (SubstanceFormattedTextFieldUI.this.textField == null) {
                    return;
                }
                Color foregr = SubstanceFormattedTextFieldUI.this.textField.getForeground();
                if (foregr == null || foregr instanceof UIResource) {
                    SubstanceFormattedTextFieldUI.this.textField.setForeground(SubstanceColorUtilities.getForegroundColor(SubstanceLookAndFeel.getCurrentSkin(SubstanceFormattedTextFieldUI.this.textField).getEnabledColorScheme(SubstanceLookAndFeel.getDecorationType(SubstanceFormattedTextFieldUI.this.textField))));
                }
            }
        });
    }

    @Override
    protected void paintBackground(Graphics g2) {
        SubstanceTextUtilities.paintTextCompBackground(g2, this.textField);
    }

    @Override
    public boolean isInside(MouseEvent me) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return false;
        }
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(this.textField, 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(this.textField)), null);
        return contour.contains(me.getPoint());
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }
}

