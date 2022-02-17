/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorWheel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HSBColorSliderModel;

public class ColorWheelChooser
extends AbstractColorChooserPanel
implements UIResource {
    private ColorWheel colorWheel;
    private HSBColorSliderModel ccModel = new HSBColorSliderModel();
    private JSlider brightnessSlider;

    public ColorWheelChooser() {
        this.initComponents();
        int textSliderGap = UIManager.getInt("ColorChooser.textSliderGap");
        if (textSliderGap != 0) {
            BorderLayout layout = (BorderLayout)this.getLayout();
            layout.setHgap(textSliderGap);
        }
        this.colorWheel = new ColorWheel();
        this.add(this.colorWheel);
        this.ccModel.configureColorSlider(2, this.brightnessSlider);
        this.colorWheel.setModel(this.ccModel);
        this.ccModel.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent evt) {
                ColorWheelChooser.this.setColorToModel(ColorWheelChooser.this.ccModel.getColor());
            }
        });
    }

    private void initComponents() {
        this.brightnessSlider = new JSlider();
        this.setLayout(new BorderLayout());
        this.brightnessSlider.setMajorTickSpacing(50);
        this.brightnessSlider.setOrientation(1);
        this.brightnessSlider.setPaintTicks(true);
        this.add((Component)this.brightnessSlider, "East");
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.colorWheel");
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return UIManager.getIcon("ColorChooser.colorWheelIcon");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return this.getLargeDisplayIcon();
    }

    @Override
    public void updateChooser() {
        this.ccModel.setColor(this.getColorFromModel());
    }

    public void setColorToModel(Color color) {
        this.getColorSelectionModel().setSelectedColor(color);
    }
}

