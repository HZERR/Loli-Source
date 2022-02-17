/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.RolloverTextControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;

public class SubstanceEditorPaneUI
extends BasicEditorPaneUI
implements TransitionAwareUI {
    protected Set lafWidgets;
    protected StateTransitionTracker stateTransitionTracker;
    protected JEditorPane editorPane;
    protected PropertyChangeListener substancePropertyChangeListener;
    private RolloverTextControlListener substanceRolloverListener;
    private ButtonModel transitionModel;

    public void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceEditorPaneUI(comp);
    }

    public SubstanceEditorPaneUI(JComponent c2) {
        this.editorPane = (JEditorPane)c2;
        this.transitionModel = new DefaultButtonModel();
        this.transitionModel.setArmed(false);
        this.transitionModel.setSelected(false);
        this.transitionModel.setPressed(false);
        this.transitionModel.setRollover(false);
        this.transitionModel.setEnabled(this.editorPane.isEnabled());
        this.stateTransitionTracker = new StateTransitionTracker(this.editorPane, this.transitionModel);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__installListeners() {
        super.installListeners();
        super.installListeners();
        this.substanceRolloverListener = new RolloverTextControlListener(this.editorPane, this, this.transitionModel);
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
                            int caretPos = SubstanceEditorPaneUI.this.editorPane.getCaretPosition();
                            SubstanceEditorPaneUI.this.editorPane.updateUI();
                            SubstanceEditorPaneUI.this.editorPane.setCaretPosition(caretPos);
                            Container parent = SubstanceEditorPaneUI.this.editorPane.getParent();
                            if (parent != null) {
                                parent.invalidate();
                                parent.validate();
                            }
                        }
                    });
                }
                if ("enabled".equals(evt.getPropertyName())) {
                    SubstanceEditorPaneUI.this.transitionModel.setEnabled(SubstanceEditorPaneUI.this.editorPane.isEnabled());
                }
            }
        };
        this.editorPane.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__uninstallListeners() {
        this.stateTransitionTracker.unregisterModelListeners();
        this.stateTransitionTracker.unregisterFocusListeners();
        this.editorPane.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        super.uninstallListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceEditorPaneUI__installDefaults() {
        super.installDefaults();
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                if (SubstanceEditorPaneUI.this.editorPane == null) {
                    return;
                }
                Color foregr = SubstanceEditorPaneUI.this.editorPane.getForeground();
                if (foregr == null || foregr instanceof UIResource) {
                    SubstanceEditorPaneUI.this.editorPane.setForeground(SubstanceColorUtilities.getForegroundColor(SubstanceLookAndFeel.getCurrentSkin(SubstanceEditorPaneUI.this.editorPane).getEnabledColorScheme(SubstanceLookAndFeel.getDecorationType(SubstanceEditorPaneUI.this.editorPane))));
                }
            }
        });
    }

    @Override
    protected void paintBackground(Graphics g2) {
        SubstanceTextUtilities.paintTextCompBackground(g2, this.editorPane);
    }

    @Override
    public boolean isInside(MouseEvent me) {
        return true;
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }
}

