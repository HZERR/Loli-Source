/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.ComponentUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.Quaqua14ColorChooserUI;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceColorChooserUI
extends Quaqua14ColorChooserUI {
    protected Set lafWidgets;

    public void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__installListeners() {
        super.installListeners();
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__installDefaults() {
        super.installDefaults();
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__uninstallListeners() {
        super.uninstallListeners();
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__uninstallDefaults() {
        super.uninstallDefaults();
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceColorChooserUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceColorChooserUI();
    }

    @Override
    protected AbstractColorChooserPanel[] createDefaultChoosers() {
        String[] defaultChoosers = (String[])UIManager.get("ColorChooser.defaultChoosers");
        LinkedList<AbstractColorChooserPanel> panelList = new LinkedList<AbstractColorChooserPanel>();
        for (int i2 = 0; i2 < defaultChoosers.length; ++i2) {
            try {
                Method setBundleMethod = Class.forName(defaultChoosers[i2]).getMethod("setLabelBundle", ResourceBundle.class);
                setBundleMethod.invoke(null, SubstanceCoreUtilities.getResourceBundle(null));
            }
            catch (Throwable t2) {
                // empty catch block
            }
            try {
                AbstractColorChooserPanel panel = (AbstractColorChooserPanel)Class.forName(defaultChoosers[i2]).newInstance();
                panelList.add(panel);
                continue;
            }
            catch (AccessControlException e2) {
                continue;
            }
            catch (Exception e3) {
                e3.printStackTrace();
                throw new InternalError("Unable to instantiate " + defaultChoosers[i2]);
            }
        }
        return panelList.toArray(new AbstractColorChooserPanel[0]);
    }
}

