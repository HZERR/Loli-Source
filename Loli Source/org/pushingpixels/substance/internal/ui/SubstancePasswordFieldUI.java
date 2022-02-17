/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.Position;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.RolloverTextControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;

public class SubstancePasswordFieldUI
extends BasicPasswordFieldUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    protected StateTransitionTracker stateTransitionTracker;
    protected JPasswordField passwordField;
    protected PropertyChangeListener substancePropertyChangeListener;
    private RolloverTextControlListener substanceRolloverListener;
    private ButtonModel transitionModel;

    public void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstancePasswordFieldUI(comp);
    }

    public SubstancePasswordFieldUI(JComponent c2) {
        this.passwordField = (JPasswordField)c2;
        this.transitionModel = new DefaultButtonModel();
        this.transitionModel.setArmed(false);
        this.transitionModel.setSelected(false);
        this.transitionModel.setPressed(false);
        this.transitionModel.setRollover(false);
        this.transitionModel.setEnabled(this.passwordField.isEnabled());
        this.stateTransitionTracker = new StateTransitionTracker(this.passwordField, this.transitionModel);
    }

    @Override
    public View create(Element elem) {
        return new SubstancePasswordView(this.passwordField, elem);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__installListeners() {
        super.installListeners();
        this.substanceRolloverListener = new RolloverTextControlListener(this.passwordField, this, this.transitionModel);
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
                            int caretPos = SubstancePasswordFieldUI.this.passwordField.getCaretPosition();
                            SubstancePasswordFieldUI.this.passwordField.updateUI();
                            SubstancePasswordFieldUI.this.passwordField.setCaretPosition(caretPos);
                            Container parent = SubstancePasswordFieldUI.this.passwordField.getParent();
                            if (parent != null) {
                                parent.invalidate();
                                parent.validate();
                            }
                        }
                    });
                }
                if ("enabled".equals(evt.getPropertyName())) {
                    SubstancePasswordFieldUI.this.transitionModel.setEnabled(SubstancePasswordFieldUI.this.passwordField.isEnabled());
                }
            }
        };
        this.passwordField.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__uninstallListeners() {
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
        this.passwordField.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        this.passwordField.removeMouseListener(this.substanceRolloverListener);
        this.passwordField.removeMouseMotionListener(this.substanceRolloverListener);
        this.substanceRolloverListener = null;
        super.uninstallListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstancePasswordFieldUI__installDefaults() {
        super.installDefaults();
        Border b2 = this.passwordField.getBorder();
        if (b2 == null || b2 instanceof UIResource) {
            BorderUIResource.CompoundBorderUIResource newB = new BorderUIResource.CompoundBorderUIResource(new SubstanceTextComponentBorder(SubstanceSizeUtils.getTextBorderInsets(SubstanceSizeUtils.getComponentFontSize(this.passwordField))), new BasicBorders.MarginBorder());
            this.passwordField.setBorder(newB);
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                if (SubstancePasswordFieldUI.this.passwordField == null) {
                    return;
                }
                Color foregr = SubstancePasswordFieldUI.this.passwordField.getForeground();
                if (foregr == null || foregr instanceof UIResource) {
                    SubstancePasswordFieldUI.this.passwordField.setForeground(SubstanceColorUtilities.getForegroundColor(SubstanceLookAndFeel.getCurrentSkin(SubstancePasswordFieldUI.this.passwordField).getEnabledColorScheme(SubstanceLookAndFeel.getDecorationType(SubstancePasswordFieldUI.this.passwordField))));
                }
            }
        });
    }

    @Override
    protected void paintBackground(Graphics g2) {
        SubstanceTextUtilities.paintTextCompBackground(g2, this.passwordField);
    }

    @Override
    public boolean isInside(MouseEvent me) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return false;
        }
        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(this.passwordField, 2.0f * SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(this.passwordField)), null);
        return contour.contains(me.getPoint());
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }

    private static class SubstancePasswordView
    extends FieldView {
        private JPasswordField field;

        public SubstancePasswordView(JPasswordField field, Element element) {
            super(element);
            this.field = field;
        }

        protected int drawEchoCharacter(Graphics g2, int x2, int y2, char c2, boolean isSelected) {
            Container container = this.getContainer();
            Graphics2D graphics = (Graphics2D)g2;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            JPasswordField field = (JPasswordField)container;
            int fontSize = SubstanceSizeUtils.getComponentFontSize(this.field);
            int dotDiameter = SubstanceSizeUtils.getPasswordDotDiameter(fontSize);
            int dotGap = SubstanceSizeUtils.getPasswordDotGap(fontSize);
            ComponentState state = field.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(field, state);
            Color topColor = isSelected ? scheme.getSelectionForegroundColor() : SubstanceColorUtilities.getForegroundColor(scheme);
            Color bottomColor = topColor.brighter();
            graphics.setPaint(new GradientPaint(x2, y2 - dotDiameter, topColor, x2, y2, bottomColor));
            int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(field);
            for (int i2 = 0; i2 < echoPerChar; ++i2) {
                graphics.fillOval(x2 + dotGap / 2, y2 - dotDiameter, dotDiameter, dotDiameter);
                x2 += dotDiameter + dotGap;
            }
            return x2;
        }

        protected int getEchoCharAdvance() {
            int fontSize = SubstanceSizeUtils.getComponentFontSize(this.field);
            int dotDiameter = SubstanceSizeUtils.getPasswordDotDiameter(fontSize);
            int dotGap = SubstanceSizeUtils.getPasswordDotGap(fontSize);
            int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(this.field);
            return echoPerChar * (dotDiameter + dotGap);
        }

        @Override
        protected int drawSelectedText(Graphics g2, int x2, int y2, int p0, int p1) throws BadLocationException {
            Container c2 = this.getContainer();
            if (c2 instanceof JPasswordField) {
                JPasswordField f2 = (JPasswordField)c2;
                if (!f2.echoCharIsSet()) {
                    return super.drawSelectedText(g2, x2, y2, p0, p1);
                }
                int n2 = p1 - p0;
                char echoChar = f2.getEchoChar();
                int currPos = x2;
                for (int i2 = 0; i2 < n2; ++i2) {
                    currPos = this.drawEchoCharacter(g2, currPos, y2, echoChar, true);
                }
                return x2 + n2 * this.getEchoCharAdvance();
            }
            return x2;
        }

        @Override
        protected int drawUnselectedText(Graphics g2, int x2, int y2, int p0, int p1) throws BadLocationException {
            Container c2 = this.getContainer();
            if (c2 instanceof JPasswordField) {
                JPasswordField f2 = (JPasswordField)c2;
                if (!f2.echoCharIsSet()) {
                    return super.drawUnselectedText(g2, x2, y2, p0, p1);
                }
                int n2 = p1 - p0;
                char echoChar = f2.getEchoChar();
                int currPos = x2;
                for (int i2 = 0; i2 < n2; ++i2) {
                    currPos = this.drawEchoCharacter(g2, currPos, y2, echoChar, false);
                }
                return x2 + n2 * this.getEchoCharAdvance();
            }
            return x2;
        }

        @Override
        public Shape modelToView(int pos, Shape a2, Position.Bias b2) throws BadLocationException {
            Container c2 = this.getContainer();
            if (c2 instanceof JPasswordField) {
                JPasswordField f2 = (JPasswordField)c2;
                if (!f2.echoCharIsSet()) {
                    return super.modelToView(pos, a2, b2);
                }
                Rectangle alloc = this.adjustAllocation(a2).getBounds();
                int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(f2);
                int fontSize = SubstanceSizeUtils.getComponentFontSize(this.field);
                int dotWidth = SubstanceSizeUtils.getPasswordDotDiameter(fontSize) + SubstanceSizeUtils.getPasswordDotGap(fontSize);
                int dx = (pos - this.getStartOffset()) * echoPerChar * dotWidth;
                alloc.x += dx;
                alloc.width = 1;
                return alloc;
            }
            return null;
        }

        @Override
        public int viewToModel(float fx, float fy, Shape a2, Position.Bias[] bias) {
            bias[0] = Position.Bias.Forward;
            int n2 = 0;
            Container c2 = this.getContainer();
            if (c2 instanceof JPasswordField) {
                int fontSize;
                int dotWidth;
                JPasswordField f2 = (JPasswordField)c2;
                if (!f2.echoCharIsSet()) {
                    return super.viewToModel(fx, fy, a2, bias);
                }
                Rectangle alloc = (a2 = this.adjustAllocation(a2)) instanceof Rectangle ? (Rectangle)a2 : a2.getBounds();
                int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(f2);
                n2 = ((int)fx - alloc.x) / (echoPerChar * (dotWidth = SubstanceSizeUtils.getPasswordDotDiameter(fontSize = SubstanceSizeUtils.getComponentFontSize(this.field)) + SubstanceSizeUtils.getPasswordDotGap(fontSize)));
                if (n2 < 0) {
                    n2 = 0;
                } else if (n2 > this.getStartOffset() + this.getDocument().getLength()) {
                    n2 = this.getDocument().getLength() - this.getStartOffset();
                }
            }
            return this.getStartOffset() + n2;
        }

        @Override
        public float getPreferredSpan(int axis) {
            switch (axis) {
                case 0: {
                    JPasswordField f2;
                    Container c2 = this.getContainer();
                    if (!(c2 instanceof JPasswordField) || !(f2 = (JPasswordField)c2).echoCharIsSet()) break;
                    int echoPerChar = SubstanceCoreUtilities.getEchoPerChar(f2);
                    int fontSize = SubstanceSizeUtils.getComponentFontSize(this.field);
                    int dotWidth = SubstanceSizeUtils.getPasswordDotDiameter(fontSize) + SubstanceSizeUtils.getPasswordDotGap(fontSize);
                    return echoPerChar * dotWidth * this.getDocument().getLength();
                }
            }
            return super.getPreferredSpan(axis);
        }
    }
}

