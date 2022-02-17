/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.Resettable;
import org.pushingpixels.lafwidget.tabbed.TabPagerManager;
import org.pushingpixels.lafwidget.tabbed.TabPagerMouseWheelListener;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;

public class TabPagerWidget
extends LafWidgetAdapter<JTabbedPane>
implements Resettable {
    protected MouseWheelListener mouseWheelListener;
    protected MouseListener mouseListener;
    protected PropertyChangeListener propertyChangeListener;

    @Override
    public void installUI() {
        super.installUI();
        if (LafWidgetUtilities2.getTabPreviewPainter((JTabbedPane)this.jcomp) != null) {
            this.installMaps();
        }
    }

    private void installMaps() {
        InputMap currMap = SwingUtilities.getUIInputMap(this.jcomp, 1);
        InputMap newMap = new InputMap();
        if (currMap != null) {
            KeyStroke[] kss = currMap.allKeys();
            for (int i2 = 0; i2 < kss.length; ++i2) {
                KeyStroke stroke = kss[i2];
                Object val2 = currMap.get(stroke);
                newMap.put(stroke, val2);
            }
        }
        newMap.put(KeyStroke.getKeyStroke(39, 2), "tabSwitcherForward");
        newMap.put(KeyStroke.getKeyStroke(37, 2), "tabSwitcherBackward");
        newMap.put(KeyStroke.getKeyStroke(17, 0, true), "tabSwitcherClose");
        newMap.put(KeyStroke.getKeyStroke(27, 0), "tabSwitcherHide");
        ((JTabbedPane)this.jcomp).getActionMap().put("tabSwitcherForward", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter((JTabbedPane)TabPagerWidget.this.jcomp);
                if (tpp == null) {
                    return;
                }
                TabPagerManager te = TabPagerManager.getPager();
                te.page((JTabbedPane)TabPagerWidget.this.jcomp, true);
            }
        });
        ((JTabbedPane)this.jcomp).getActionMap().put("tabSwitcherBackward", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter((JTabbedPane)TabPagerWidget.this.jcomp);
                if (tpp == null) {
                    return;
                }
                TabPagerManager te = TabPagerManager.getPager();
                te.page((JTabbedPane)TabPagerWidget.this.jcomp, false);
            }
        });
        ((JTabbedPane)this.jcomp).getActionMap().put("tabSwitcherClose", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter((JTabbedPane)TabPagerWidget.this.jcomp);
                        if (tpp == null) {
                            return;
                        }
                        TabPagerManager te = TabPagerManager.getPager();
                        int index = te.hide();
                        if (index >= 0 && tpp.isSensitiveToEvents((JTabbedPane)TabPagerWidget.this.jcomp, index)) {
                            ((JTabbedPane)TabPagerWidget.this.jcomp).setSelectedIndex(index);
                        }
                    }
                });
            }
        });
        ((JTabbedPane)this.jcomp).getActionMap().put("tabSwitcherHide", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                TabPagerManager te = TabPagerManager.getPager();
                if (te.isVisible()) {
                    te.hide();
                } else {
                    for (Container comp = ((JTabbedPane)TabPagerWidget.this.jcomp).getParent(); comp != null; comp = comp.getParent()) {
                        KeyStroke escapeKeyStroke;
                        JComponent jc;
                        ActionListener al;
                        if (!(comp instanceof JComponent) || (al = (jc = (JComponent)comp).getActionForKeyStroke(escapeKeyStroke = KeyStroke.getKeyStroke(27, 0, false))) == null) continue;
                        al.actionPerformed(e2);
                        return;
                    }
                }
            }
        });
        SwingUtilities.replaceUIInputMap(this.jcomp, 1, newMap);
    }

    private void uninstallMaps() {
        InputMap currMap = SwingUtilities.getUIInputMap(this.jcomp, 1);
        InputMap newMap = new InputMap();
        if (currMap != null) {
            KeyStroke[] kss = currMap.allKeys();
            for (int i2 = 0; i2 < kss.length; ++i2) {
                KeyStroke stroke = kss[i2];
                Object val2 = currMap.get(stroke);
                if (stroke.equals(KeyStroke.getKeyStroke(39, 2)) && "tabSwitcherForward".equals(val2) || stroke.equals(KeyStroke.getKeyStroke(37, 2)) && "tabSwitcherBackward".equals(val2) || stroke.equals(KeyStroke.getKeyStroke(17, 0, true)) && "tabSwitcherClose".equals(val2) || stroke.equals(KeyStroke.getKeyStroke(27, 0)) && "tabSwitcherHide".equals(val2)) continue;
                newMap.put(stroke, val2);
            }
        }
        ((JTabbedPane)this.jcomp).getActionMap().remove("tabSwitcherForward");
        ((JTabbedPane)this.jcomp).getActionMap().remove("tabSwitcherBackward");
        ((JTabbedPane)this.jcomp).getActionMap().remove("tabSwitcherClose");
        ((JTabbedPane)this.jcomp).getActionMap().remove("tabSwitcherHide");
        SwingUtilities.replaceUIInputMap(this.jcomp, 1, newMap);
    }

    @Override
    public void uninstallUI() {
        this.uninstallMaps();
        super.uninstallUI();
    }

    @Override
    public void installListeners() {
        this.mouseWheelListener = new TabPagerMouseWheelListener((JTabbedPane)this.jcomp);
        ((JTabbedPane)this.jcomp).addMouseWheelListener(this.mouseWheelListener);
        this.mouseListener = new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e2) {
                if ((e2.getModifiers() & 2) != 0) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            TabPagerManager te = TabPagerManager.getPager();
                            int index = te.hide();
                            if (index >= 0) {
                                ((JTabbedPane)TabPagerWidget.this.jcomp).setSelectedIndex(index);
                            }
                        }
                    });
                }
            }
        };
        ((JTabbedPane)this.jcomp).addMouseListener(this.mouseListener);
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lafwidgets.tabbedpanePreviewPainter".equals(evt.getPropertyName())) {
                    TabPreviewPainter oldValue = (TabPreviewPainter)evt.getOldValue();
                    TabPreviewPainter newValue = (TabPreviewPainter)evt.getNewValue();
                    if (oldValue == null && newValue != null) {
                        TabPagerWidget.this.installMaps();
                    }
                    if (oldValue != null && newValue == null) {
                        TabPagerWidget.this.uninstallMaps();
                    }
                }
            }
        };
        ((JTabbedPane)this.jcomp).addPropertyChangeListener(this.propertyChangeListener);
    }

    @Override
    public void uninstallListeners() {
        ((JTabbedPane)this.jcomp).removeMouseWheelListener(this.mouseWheelListener);
        this.mouseWheelListener = null;
        ((JTabbedPane)this.jcomp).removeMouseListener(this.mouseListener);
        this.mouseListener = null;
        ((JTabbedPane)this.jcomp).removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void reset() {
        TabPagerManager.reset();
    }
}

