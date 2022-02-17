/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorChooserUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorChooserMainPanel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.QuaquaColorPreviewPanel;

public class Quaqua13ColorChooserUI
extends ColorChooserUI {
    protected Set lafWidgets;
    protected ColorChooserMainPanel mainPanel;
    protected JColorChooser chooser;
    protected ChangeListener previewListener;
    protected PropertyChangeListener propertyChangeListener;
    protected AbstractColorChooserPanel[] defaultChoosers;
    protected JComponent previewPanel;

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent c2) {
        return new Quaqua13ColorChooserUI();
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__installUI(JComponent c2) {
        this.chooser = (JColorChooser)c2;
        this.installDefaults();
        this.installListeners();
        this.chooser.setLayout(new BorderLayout());
        this.mainPanel = new ColorChooserMainPanel();
        this.chooser.add(this.mainPanel);
        this.defaultChoosers = this.createDefaultChoosers();
        this.chooser.setChooserPanels(this.defaultChoosers);
        this.installPreviewPanel();
    }

    protected AbstractColorChooserPanel[] createDefaultChoosers() {
        String[] defaultChoosers = (String[])UIManager.get("ColorChooser.defaultChoosers");
        ArrayList panels = new ArrayList(defaultChoosers.length);
        for (int i2 = 0; i2 < defaultChoosers.length; ++i2) {
            try {
                panels.add(Class.forName(defaultChoosers[i2]).newInstance());
                continue;
            }
            catch (AccessControlException e2) {
                System.err.println("Quaqua13ColorChooserUI warning: unable to instantiate " + defaultChoosers[i2]);
                continue;
            }
            catch (Exception e3) {
                throw new InternalError("Unable to instantiate " + defaultChoosers[i2]);
            }
            catch (UnsupportedClassVersionError e4) {
                System.err.println("Quaqua13ColorChooserUI warning: unable to instantiate " + defaultChoosers[i2]);
            }
        }
        return panels.toArray(new AbstractColorChooserPanel[panels.size()]);
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__uninstallUI(JComponent c2) {
        this.chooser.remove(this.mainPanel);
        this.uninstallListeners();
        this.uninstallDefaultChoosers();
        this.uninstallDefaults();
        this.mainPanel.setPreviewPanel(null);
        if (this.previewPanel instanceof UIResource) {
            this.chooser.setPreviewPanel(null);
        }
        this.mainPanel = null;
        this.previewPanel = null;
        this.defaultChoosers = null;
        this.chooser = null;
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__installDefaults() {
        LookAndFeel.installColorsAndFont(this.chooser, "ColorChooser.background", "ColorChooser.foreground", "ColorChooser.font");
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__uninstallDefaults() {
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__installListeners() {
        this.propertyChangeListener = this.createPropertyChangeListener();
        this.chooser.addPropertyChangeListener(this.propertyChangeListener);
        this.previewListener = new PreviewListener();
        this.chooser.getSelectionModel().addChangeListener(this.previewListener);
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua13ColorChooserUI__uninstallListeners() {
        this.chooser.removePropertyChangeListener(this.propertyChangeListener);
        this.chooser.getSelectionModel().removeChangeListener(this.previewListener);
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return new PropertyHandler();
    }

    protected void installPreviewPanel() {
        if (this.previewPanel != null) {
            this.mainPanel.setPreviewPanel(null);
        }
        this.previewPanel = this.chooser.getPreviewPanel();
        if (this.previewPanel != null && "javax.swing.colorchooser.DefaultPreviewPanel".equals(this.previewPanel.getClass().getName())) {
            this.previewPanel = null;
        }
        if (this.previewPanel != null && this.mainPanel != null && this.chooser != null && this.previewPanel.getSize().getHeight() + this.previewPanel.getSize().getWidth() == 0.0) {
            this.mainPanel.setPreviewPanel(null);
            return;
        }
        if (this.previewPanel == null || this.previewPanel instanceof UIResource) {
            this.previewPanel = new QuaquaColorPreviewPanel();
            this.chooser.setPreviewPanel(this.previewPanel);
        }
        this.previewPanel.setForeground(this.chooser.getColor());
        this.mainPanel.setPreviewPanel(this.previewPanel);
    }

    protected void uninstallDefaultChoosers() {
        for (int i2 = 0; i2 < this.defaultChoosers.length; ++i2) {
            this.chooser.removeChooserPanel(this.defaultChoosers[i2]);
        }
    }

    public class PropertyHandler
    implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent e2) {
            if (e2.getPropertyName().equals("chooserPanels")) {
                int i2;
                AbstractColorChooserPanel[] oldPanels = (AbstractColorChooserPanel[])e2.getOldValue();
                AbstractColorChooserPanel[] newPanels = (AbstractColorChooserPanel[])e2.getNewValue();
                for (i2 = 0; i2 < oldPanels.length; ++i2) {
                    Container wrapper = oldPanels[i2].getParent();
                    if (wrapper == null) continue;
                    Container parent = wrapper.getParent();
                    if (parent != null) {
                        parent.remove(wrapper);
                    }
                    oldPanels[i2].uninstallChooserPanel(Quaqua13ColorChooserUI.this.chooser);
                }
                Quaqua13ColorChooserUI.this.mainPanel.removeAllColorChooserPanels();
                for (i2 = 0; i2 < newPanels.length; ++i2) {
                    if (newPanels[i2] == null) continue;
                    Quaqua13ColorChooserUI.this.mainPanel.addColorChooserPanel(newPanels[i2]);
                }
                for (i2 = 0; i2 < newPanels.length; ++i2) {
                    if (newPanels[i2] == null) continue;
                    newPanels[i2].installChooserPanel(Quaqua13ColorChooserUI.this.chooser);
                }
            }
            if (e2.getPropertyName().equals("previewPanel") && e2.getNewValue() != Quaqua13ColorChooserUI.this.previewPanel) {
                Quaqua13ColorChooserUI.this.installPreviewPanel();
            }
        }
    }

    class PreviewListener
    implements ChangeListener {
        PreviewListener() {
        }

        @Override
        public void stateChanged(ChangeEvent e2) {
            ColorSelectionModel model = (ColorSelectionModel)e2.getSource();
            if (Quaqua13ColorChooserUI.this.previewPanel != null) {
                Quaqua13ColorChooserUI.this.previewPanel.setForeground(model.getSelectedColor());
                Quaqua13ColorChooserUI.this.previewPanel.repaint();
            }
        }
    }
}

