/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.Quaqua13ColorChooserUI;

public class Quaqua14ColorChooserUI
extends Quaqua13ColorChooserUI {
    protected Set lafWidgets;
    private static TransferHandler defaultTransferHandler = new ColorTransferHandler();
    private MouseListener previewMouseListener;

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent c2) {
        return new Quaqua14ColorChooserUI();
    }

    public void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__installUI(JComponent c2) {
        super.installUI(c2);
        this.chooser.applyComponentOrientation(c2.getComponentOrientation());
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__installDefaults() {
        super.installDefaults();
        TransferHandler th = this.chooser.getTransferHandler();
        if (th == null || th instanceof UIResource) {
            this.chooser.setTransferHandler(defaultTransferHandler);
        }
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__uninstallDefaults() {
        if (this.chooser.getTransferHandler() instanceof UIResource) {
            this.chooser.setTransferHandler(null);
        }
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__installListeners() {
        super.installListeners();
        this.previewMouseListener = new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e2) {
                if (Quaqua14ColorChooserUI.this.chooser.getDragEnabled()) {
                    TransferHandler th = Quaqua14ColorChooserUI.this.chooser.getTransferHandler();
                    th.exportAsDrag(Quaqua14ColorChooserUI.this.chooser, e2, 1);
                }
            }
        };
    }

    protected void __org__pushingpixels__substance__internal__contrib__randelshofer__quaqua__Quaqua14ColorChooserUI__uninstallListeners() {
        super.uninstallListeners();
        this.previewPanel.removeMouseListener(this.previewMouseListener);
    }

    @Override
    protected void installPreviewPanel() {
        if (this.previewPanel != null) {
            this.previewPanel.removeMouseListener(this.previewMouseListener);
        }
        super.installPreviewPanel();
        this.previewPanel.addMouseListener(this.previewMouseListener);
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        return new PropertyHandler();
    }

    static class ColorTransferHandler
    extends TransferHandler
    implements UIResource {
        ColorTransferHandler() {
            super("color");
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
                    Container wrapper;
                    if (oldPanels[i2] == null || (wrapper = oldPanels[i2].getParent()) == null) continue;
                    Container parent = wrapper.getParent();
                    if (parent != null) {
                        parent.remove(wrapper);
                    }
                    oldPanels[i2].uninstallChooserPanel(Quaqua14ColorChooserUI.this.chooser);
                }
                Quaqua14ColorChooserUI.this.mainPanel.removeAllColorChooserPanels();
                for (i2 = 0; i2 < newPanels.length; ++i2) {
                    if (newPanels[i2] == null) continue;
                    Quaqua14ColorChooserUI.this.mainPanel.addColorChooserPanel(newPanels[i2]);
                }
                Quaqua14ColorChooserUI.this.chooser.applyComponentOrientation(Quaqua14ColorChooserUI.this.chooser.getComponentOrientation());
                for (i2 = 0; i2 < newPanels.length; ++i2) {
                    if (newPanels[i2] == null) continue;
                    newPanels[i2].installChooserPanel(Quaqua14ColorChooserUI.this.chooser);
                }
            }
            if (e2.getPropertyName().equals("previewPanel") && e2.getNewValue() != Quaqua14ColorChooserUI.this.previewPanel) {
                Quaqua14ColorChooserUI.this.installPreviewPanel();
            }
            if (e2.getPropertyName().equals("componentOrientation")) {
                ComponentOrientation o2 = (ComponentOrientation)e2.getNewValue();
                JColorChooser cc = (JColorChooser)e2.getSource();
                if (o2 != (ComponentOrientation)e2.getOldValue()) {
                    cc.applyComponentOrientation(o2);
                    cc.updateUI();
                }
            }
        }
    }
}

