/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.IconGlowTracker;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.icon.GlowingIcon;

public class SubstanceOptionPaneUI
extends BasicOptionPaneUI {
    protected Set lafWidgets;
    private OptionPaneLabel substanceIconLabel;
    private IconGlowTracker iconGlowTracker;

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installListeners() {
        super.installListeners();
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installDefaults() {
        super.installDefaults();
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallComponents() {
        super.uninstallComponents();
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallListeners() {
        super.uninstallListeners();
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceOptionPaneUI();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        BackgroundPaintingUtils.updateIfOpaque(g2, c2);
    }

    @Override
    protected void addIcon(Container top) {
        Icon sideIcon;
        Icon icon = sideIcon = this.optionPane == null ? null : this.optionPane.getIcon();
        if (sideIcon == null && this.optionPane != null) {
            sideIcon = super.getIconForType(this.optionPane.getMessageType());
        }
        if (sideIcon != null) {
            if (!SubstanceLookAndFeel.isToUseConstantThemesOnDialogs()) {
                sideIcon = SubstanceCoreUtilities.getThemedIcon(null, sideIcon);
            }
            this.substanceIconLabel = new OptionPaneLabel();
            this.iconGlowTracker = new IconGlowTracker(this.substanceIconLabel);
            this.substanceIconLabel.setIcon(new GlowingIcon(sideIcon, this.iconGlowTracker));
            this.substanceIconLabel.setName("OptionPane.iconLabel");
            this.substanceIconLabel.setVerticalAlignment(1);
            top.add((Component)this.substanceIconLabel, "Before");
        }
    }

    @Override
    protected Icon getIconForType(int messageType) {
        switch (messageType) {
            case 0: {
                return SubstanceCoreUtilities.getIcon("resource/32/dialog-error.png");
            }
            case 1: {
                return SubstanceCoreUtilities.getIcon("resource/32/dialog-information.png");
            }
            case 2: {
                return SubstanceCoreUtilities.getIcon("resource/32/dialog-warning.png");
            }
            case 3: {
                return SubstanceCoreUtilities.getIcon("resource/32/help-browser.png");
            }
        }
        return null;
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceOptionPaneUI__installComponents() {
        super.installComponents();
        if (this.substanceIconLabel != null && !this.iconGlowTracker.isPlaying()) {
            this.iconGlowTracker.play(3);
        }
    }

    static {
        AnimationConfigurationManager.getInstance().allowAnimations(AnimationFacet.ICON_GLOW, OptionPaneLabel.class);
    }

    protected static class OptionPaneLabel
    extends JLabel {
        protected OptionPaneLabel() {
        }
    }
}

