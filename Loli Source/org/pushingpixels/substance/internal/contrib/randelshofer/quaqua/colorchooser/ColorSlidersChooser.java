/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.CMYKChooser;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.GrayChooser;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HSBChooser;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HTMLChooser;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.RGBChooser;

public class ColorSlidersChooser
extends AbstractColorChooserPanel
implements UIResource {
    private static int lastSelectedPanelIndex = 1;
    private JComboBox slidersComboBox;
    private JPanel slidersHolder;

    public ColorSlidersChooser() {
        this.initComponents();
        this.slidersComboBox.setFont(UIManager.getFont("ColorChooser.font"));
    }

    private void initComponents() {
        this.slidersComboBox = new JComboBox();
        this.slidersHolder = new JPanel();
        this.setLayout(new BorderLayout());
        this.add((Component)this.slidersComboBox, "North");
        this.slidersHolder.setLayout(new CardLayout());
        this.add((Component)this.slidersHolder, "Center");
    }

    @Override
    protected void buildChooser() {
        this.slidersHolder.add((Component)new GrayChooser(), UIManager.getString("ColorChooser.grayScaleSlider"));
        this.slidersHolder.add((Component)new RGBChooser(), UIManager.getString("ColorChooser.rgbSliders"));
        this.slidersHolder.add((Component)new CMYKChooser(), UIManager.getString("ColorChooser.cmykSliders"));
        this.slidersHolder.add((Component)new HSBChooser(), UIManager.getString("ColorChooser.hsbSliders"));
        this.slidersHolder.add((Component)new HTMLChooser(), UIManager.getString("ColorChooser.htmlSliders"));
        DefaultComboBoxModel<String> cbm = new DefaultComboBoxModel<String>();
        cbm.addElement(UIManager.getString("ColorChooser.grayScaleSlider"));
        cbm.addElement(UIManager.getString("ColorChooser.rgbSliders"));
        cbm.addElement(UIManager.getString("ColorChooser.cmykSliders"));
        cbm.addElement(UIManager.getString("ColorChooser.hsbSliders"));
        cbm.addElement(UIManager.getString("ColorChooser.htmlSliders"));
        this.slidersComboBox.setModel(cbm);
        this.slidersComboBox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == 1) {
                    ((CardLayout)ColorSlidersChooser.this.slidersHolder.getLayout()).show(ColorSlidersChooser.this.slidersHolder, (String)evt.getItem());
                    lastSelectedPanelIndex = ColorSlidersChooser.this.slidersComboBox.getSelectedIndex();
                }
            }
        });
        this.slidersComboBox.setSelectedIndex(lastSelectedPanelIndex);
    }

    @Override
    public void installChooserPanel(JColorChooser enclosingChooser) {
        super.installChooserPanel(enclosingChooser);
        Component[] components = this.slidersHolder.getComponents();
        for (int i2 = 0; i2 < components.length; ++i2) {
            AbstractColorChooserPanel ccp = (AbstractColorChooserPanel)components[i2];
            ccp.installChooserPanel(enclosingChooser);
        }
    }

    @Override
    public void uninstallChooserPanel(JColorChooser enclosingChooser) {
        Component[] components = this.slidersHolder.getComponents();
        for (int i2 = 0; i2 < components.length; ++i2) {
            AbstractColorChooserPanel ccp = (AbstractColorChooserPanel)components[i2];
            ccp.uninstallChooserPanel(enclosingChooser);
        }
        super.uninstallChooserPanel(enclosingChooser);
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.colorSliders");
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return UIManager.getIcon("ColorChooser.colorSlidersIcon");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return this.getLargeDisplayIcon();
    }

    @Override
    public void updateChooser() {
    }
}

