/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util.Methods;

public class ColorChooserMainPanel
extends JPanel {
    private static String lastSelectedChooserName = null;
    private JPanel chooserPanelHolder;
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel previewPanelHolder;
    private JToolBar toolBar;
    private ButtonGroup toolBarButtonGroup;

    public ColorChooserMainPanel() {
        this.initComponents();
        this.toolBar.putClientProperty("Quaqua.ToolBar.isDividerDrawn", Boolean.TRUE);
    }

    public void setPreviewPanel(JComponent c2) {
        this.previewPanelHolder.removeAll();
        if (c2 != null) {
            this.previewPanelHolder.add(c2);
        }
    }

    public void addColorChooserPanel(AbstractColorChooserPanel ccp) {
        final String displayName = ccp.getDisplayName();
        if (displayName.equals("Color Picker")) {
            this.northPanel.add((Component)ccp, "West");
        } else {
            Icon displayIcon = ccp.getLargeDisplayIcon();
            JToggleButton tb = new JToggleButton(null, displayIcon);
            tb.setToolTipText(displayName);
            Methods.invokeIfExists((Object)tb, "setFocusable", false);
            tb.setHorizontalTextPosition(0);
            tb.setVerticalTextPosition(3);
            tb.setFont(UIManager.getFont("ColorChooser.font"));
            tb.putClientProperty("Quaqua.Button.style", "toolBarTab");
            JPanel centerView = new JPanel(new BorderLayout());
            centerView.add(ccp);
            this.chooserPanelHolder.add((Component)centerView, displayName);
            this.toolBarButtonGroup.add(tb);
            this.toolBar.add(tb);
            if (this.toolBar.getComponentCount() == 1 || lastSelectedChooserName != null && lastSelectedChooserName.equals(displayName)) {
                tb.setSelected(true);
                CardLayout cl = (CardLayout)this.chooserPanelHolder.getLayout();
                cl.show(this.chooserPanelHolder, displayName);
            }
            tb.addItemListener(new ItemListener(){

                @Override
                public void itemStateChanged(ItemEvent evt) {
                    if (evt.getStateChange() == 1) {
                        CardLayout cl = (CardLayout)ColorChooserMainPanel.this.chooserPanelHolder.getLayout();
                        cl.show(ColorChooserMainPanel.this.chooserPanelHolder, displayName);
                        lastSelectedChooserName = displayName;
                    }
                }
            });
        }
    }

    public void removeAllColorChooserPanels() {
        Component[] tb = this.toolBar.getComponents();
        for (int i2 = 0; i2 < tb.length; ++i2) {
            if (!(tb[i2] instanceof AbstractButton)) continue;
            this.toolBarButtonGroup.remove((AbstractButton)tb[i2]);
        }
        this.toolBar.removeAll();
        this.chooserPanelHolder.removeAll();
        this.northPanel.removeAll();
        this.northPanel.add(this.previewPanelHolder);
    }

    private void initComponents() {
        this.toolBarButtonGroup = new ButtonGroup();
        this.toolBar = new JToolBar();
        this.mainPanel = new JPanel();
        this.northPanel = new JPanel();
        this.previewPanelHolder = new JPanel();
        this.chooserPanelHolder = new JPanel();
        this.setLayout(new BorderLayout());
        this.toolBar.setFloatable(false);
        this.add((Component)this.toolBar, "North");
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBorder(new EmptyBorder(new Insets(5, 4, 7, 4)));
        this.northPanel.setLayout(new BorderLayout());
        this.previewPanelHolder.setLayout(new BorderLayout());
        this.northPanel.add((Component)this.previewPanelHolder, "Center");
        this.mainPanel.add((Component)this.northPanel, "North");
        this.chooserPanelHolder.setLayout(new CardLayout());
        this.chooserPanelHolder.setBorder(new EmptyBorder(new Insets(5, 0, 0, 0)));
        this.mainPanel.add((Component)this.chooserPanelHolder, "Center");
        this.add((Component)this.mainPanel, "Center");
    }
}

