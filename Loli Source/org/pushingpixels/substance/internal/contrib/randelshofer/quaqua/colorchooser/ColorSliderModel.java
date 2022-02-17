/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.util.LinkedList;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderUI;

public abstract class ColorSliderModel {
    private LinkedList sliders = new LinkedList();
    private LinkedList listeners = new LinkedList();
    protected DefaultBoundedRangeModel[] components;
    protected int[] values;

    protected ColorSliderModel(DefaultBoundedRangeModel[] components) {
        this.components = components;
        this.values = new int[components.length];
        for (int i2 = 0; i2 < components.length; ++i2) {
            final int componentIndex = i2;
            components[i2].addChangeListener(new ChangeListener(){

                @Override
                public void stateChanged(ChangeEvent e2) {
                    ColorSliderModel.this.fireColorChanged(componentIndex);
                    ColorSliderModel.this.fireStateChanged();
                }
            });
        }
    }

    public void configureColorSlider(int component, JSlider slider) {
        if (slider.getClientProperty("ColorSliderModel") != null) {
            ((ColorSliderModel)slider.getClientProperty("ColorSliderModel")).unconfigureColorSlider(slider);
        }
        if (!(slider.getUI() instanceof ColorSliderUI)) {
            slider.setUI(new ColorSliderUI(slider));
            slider.createStandardLabels(16);
        }
        slider.setModel(this.getBoundedRangeModel(component));
        slider.putClientProperty("ColorSliderModel", this);
        slider.putClientProperty("ColorComponentIndex", component);
        this.addColorSlider(slider);
    }

    public void unconfigureColorSlider(JSlider slider) {
        if (slider.getClientProperty("ColorSliderModel") == this) {
            slider.setModel(new DefaultBoundedRangeModel());
            slider.putClientProperty("ColorSliderModel", null);
            slider.putClientProperty("ColorComponentIndex", null);
            this.removeColorSlider(slider);
        }
    }

    public int getComponentCount() {
        return this.components.length;
    }

    public DefaultBoundedRangeModel getBoundedRangeModel(int component) {
        return this.components[component];
    }

    public int getValue(int component) {
        return this.components[component].getValue();
    }

    public void setValue(int component, int value) {
        this.components[component].setValue(value);
    }

    public int getInterpolatedRGB(int component, float ratio) {
        int n2 = this.getComponentCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.values[i2] = this.components[i2].getValue();
        }
        this.values[component] = (int)(ratio * (float)this.components[component].getMaximum());
        return this.toRGB(this.values);
    }

    protected void addColorSlider(JSlider slider) {
        this.sliders.add(slider);
    }

    protected void removeColorSlider(JSlider slider) {
        this.sliders.remove(slider);
    }

    public void addChangeListener(ChangeListener l2) {
        this.listeners.add(l2);
    }

    public void removeChangeListener(ChangeListener l2) {
        this.listeners.remove(l2);
    }

    protected void fireColorChanged(int componentIndex) {
        Integer index = componentIndex;
        Color value = this.getColor();
        for (JSlider slider : this.sliders) {
            slider.putClientProperty("ColorComponentChange", index);
            slider.putClientProperty("ColorComponentValue", value);
        }
    }

    public void fireStateChanged() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener l2 : this.listeners) {
            l2.stateChanged(event);
        }
    }

    public Color getColor() {
        return new Color(this.getRGB());
    }

    public void setColor(Color color) {
        int rgb = color.getRGB();
        if (rgb != this.getRGB()) {
            this.setRGB(rgb);
        }
    }

    public abstract void setRGB(int var1);

    public abstract int getRGB();

    public abstract int toRGB(int[] var1);
}

