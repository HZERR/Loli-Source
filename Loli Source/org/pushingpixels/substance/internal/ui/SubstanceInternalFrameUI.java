/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.ui.SubstanceDesktopIconUI;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

public class SubstanceInternalFrameUI
extends BasicInternalFrameUI {
    protected Set lafWidgets;
    private SubstanceInternalFrameTitlePane titlePane;
    protected PropertyChangeListener substancePropertyListener;

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installDefaults() {
        super.installDefaults();
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public SubstanceInternalFrameUI(JInternalFrame b2) {
        super(b2);
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceInternalFrameUI((JInternalFrame)comp);
    }

    @Override
    protected JComponent createNorthPane(JInternalFrame w2) {
        this.titlePane = new SubstanceInternalFrameTitlePane(w2);
        return this.titlePane;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installComponents() {
        if (SubstanceCoreUtilities.isRoundedCorners(this.frame)) {
            this.frame.setOpaque(false);
        }
        super.installComponents();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallComponents() {
        this.titlePane.uninstall();
        super.uninstallComponents();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__installListeners() {
        super.installListeners();
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("closed".equals(propertyName)) {
                    SubstanceInternalFrameUI.this.titlePane.uninstall();
                    JInternalFrame.JDesktopIcon jdi = SubstanceInternalFrameUI.this.frame.getDesktopIcon();
                    SubstanceDesktopIconUI ui = (SubstanceDesktopIconUI)jdi.getUI();
                    ui.uninstallIfNecessary(jdi);
                } else if ("background".equals(propertyName)) {
                    Color newBackgr = (Color)evt.getNewValue();
                    if (!(newBackgr instanceof UIResource)) {
                        SubstanceInternalFrameUI.this.getTitlePane().setBackground(newBackgr);
                        SubstanceInternalFrameUI.this.frame.getDesktopIcon().setBackground(newBackgr);
                    }
                } else if ("ancestor".equals(propertyName)) {
                    JInternalFrame.JDesktopIcon jdi = SubstanceInternalFrameUI.this.frame.getDesktopIcon();
                    SubstanceDesktopIconUI ui = (SubstanceDesktopIconUI)jdi.getUI();
                    ui.installIfNecessary(jdi);
                } else if ("selected".equals(propertyName)) {
                    SubstanceInternalFrameUI.this.titlePane.setActive((Boolean)evt.getNewValue());
                } else if (("windowRoundedCorners".equals(propertyName) || "maximum".equals(propertyName)) && SubstanceCoreUtilities.isRoundedCorners(SubstanceInternalFrameUI.this.frame)) {
                    SubstanceInternalFrameUI.this.frame.setOpaque(false);
                }
            }
        };
        this.frame.addPropertyChangeListener(this.substancePropertyListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceInternalFrameUI__uninstallListeners() {
        this.frame.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        super.uninstallListeners();
    }

    public SubstanceInternalFrameTitlePane getTitlePane() {
        return this.titlePane;
    }

    void setWindowModified(boolean isWindowModified) {
        this.titlePane.getCloseButton().putClientProperty("windowModified", isWindowModified);
        SubstanceDesktopIconUI desktopIconUi = (SubstanceDesktopIconUI)this.frame.getDesktopIcon().getUI();
        desktopIconUi.setWindowModified(isWindowModified);
    }
}

