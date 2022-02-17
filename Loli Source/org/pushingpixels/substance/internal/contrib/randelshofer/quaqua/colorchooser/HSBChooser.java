/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.VisualMargin;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderTextFieldHandler;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HSBColorSliderModel;

public class HSBChooser
extends AbstractColorChooserPanel
implements UIResource {
    private ColorSliderModel ccModel = new HSBColorSliderModel();
    private JTextField brightnessField;
    private JLabel brightnessFieldLabel;
    private JPanel brightnessFieldPanel;
    private JLabel brightnessLabel;
    private JSlider brightnessSlider;
    private JTextField hueField;
    private JLabel hueFieldLabel;
    private JPanel hueFieldPanel;
    private JLabel hueLabel;
    private JSlider hueSlider;
    private JTextField saturationField;
    private JLabel saturationFieldLabel;
    private JPanel saturationFieldPanel;
    private JLabel saturationLabel;
    private JSlider saturationSlider;
    private JPanel springPanel;

    public HSBChooser() {
        this.initComponents();
        Font font = UIManager.getFont("ColorChooser.font");
        this.hueLabel.setFont(font);
        this.hueSlider.setFont(font);
        this.hueField.setFont(font);
        this.hueFieldLabel.setFont(font);
        this.saturationLabel.setFont(font);
        this.saturationSlider.setFont(font);
        this.saturationField.setFont(font);
        this.saturationFieldLabel.setFont(font);
        this.brightnessLabel.setFont(font);
        this.brightnessSlider.setFont(font);
        this.brightnessField.setFont(font);
        this.brightnessFieldLabel.setFont(font);
        int textSliderGap = UIManager.getInt("ColorChooser.textSliderGap");
        if (textSliderGap != 0) {
            EmptyBorder fieldBorder = new EmptyBorder(0, textSliderGap, 0, 0);
            this.hueFieldPanel.setBorder(fieldBorder);
            this.saturationFieldPanel.setBorder(fieldBorder);
            this.brightnessFieldPanel.setBorder(fieldBorder);
        }
        this.ccModel.configureColorSlider(0, this.hueSlider);
        this.ccModel.configureColorSlider(1, this.saturationSlider);
        this.ccModel.configureColorSlider(2, this.brightnessSlider);
        this.hueField.setText(Integer.toString(this.hueSlider.getValue()));
        this.saturationField.setText(Integer.toString(this.saturationSlider.getValue()));
        this.brightnessField.setText(Integer.toString(this.brightnessSlider.getValue()));
        Insets borderMargin = (Insets)UIManager.getInsets("Component.visualMargin").clone();
        borderMargin.left = 3 - borderMargin.left;
        this.hueFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        this.saturationFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        this.brightnessFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        new ColorSliderTextFieldHandler(this.hueField, this.ccModel, 0);
        new ColorSliderTextFieldHandler(this.saturationField, this.ccModel, 1);
        new ColorSliderTextFieldHandler(this.brightnessField, this.ccModel, 2);
        this.ccModel.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent evt) {
                HSBChooser.this.setColorToModel(HSBChooser.this.ccModel.getColor());
            }
        });
        this.hueField.setMinimumSize(this.hueField.getPreferredSize());
        this.saturationField.setMinimumSize(this.saturationField.getPreferredSize());
        this.brightnessField.setMinimumSize(this.brightnessField.getPreferredSize());
        VisualMargin bm = new VisualMargin(false, false, true, false);
        this.hueLabel.setBorder(bm);
        this.saturationLabel.setBorder(bm);
        this.brightnessLabel.setBorder(bm);
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.hsbSliders");
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
        this.ccModel.setColor(this.getColorFromModel());
    }

    public void setColorToModel(Color color) {
        this.getColorSelectionModel().setSelectedColor(color);
    }

    private void initComponents() {
        this.hueLabel = new JLabel();
        this.hueSlider = new JSlider();
        this.hueFieldPanel = new JPanel();
        this.hueField = new JTextField();
        this.hueFieldLabel = new JLabel();
        this.saturationLabel = new JLabel();
        this.saturationSlider = new JSlider();
        this.saturationFieldPanel = new JPanel();
        this.saturationField = new JTextField();
        this.saturationFieldLabel = new JLabel();
        this.brightnessLabel = new JLabel();
        this.brightnessSlider = new JSlider();
        this.brightnessFieldPanel = new JPanel();
        this.brightnessField = new JTextField();
        this.brightnessFieldLabel = new JLabel();
        this.springPanel = new JPanel();
        this.setLayout(new GridBagLayout());
        this.hueLabel.setText(UIManager.getString("ColorChooser.hsbHueText"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.hueLabel, gridBagConstraints);
        this.hueSlider.setMajorTickSpacing(359);
        this.hueSlider.setMaximum(359);
        this.hueSlider.setMinorTickSpacing(180);
        this.hueSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.hueSlider, gridBagConstraints);
        this.hueFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.hueField.setColumns(3);
        this.hueField.setHorizontalAlignment(11);
        this.hueField.setText("0");
        this.hueField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HSBChooser.this.hueFieldFocusLost(evt);
            }
        });
        this.hueFieldPanel.add(this.hueField);
        this.hueFieldLabel.setText("\u00b0");
        this.hueFieldPanel.add(this.hueFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.hueFieldPanel, gridBagConstraints);
        this.saturationLabel.setText(UIManager.getString("ColorChooser.hsbSaturationText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.saturationLabel, gridBagConstraints);
        this.saturationSlider.setMajorTickSpacing(100);
        this.saturationSlider.setMinorTickSpacing(50);
        this.saturationSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.saturationSlider, gridBagConstraints);
        this.saturationFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.saturationField.setColumns(3);
        this.saturationField.setHorizontalAlignment(11);
        this.saturationField.setText("0");
        this.saturationField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HSBChooser.this.saturationFieldFocusLost(evt);
            }
        });
        this.saturationFieldPanel.add(this.saturationField);
        this.saturationFieldLabel.setText("%");
        this.saturationFieldPanel.add(this.saturationFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.saturationFieldPanel, gridBagConstraints);
        this.brightnessLabel.setText(UIManager.getString("ColorChooser.hsbBrightnessText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.brightnessLabel, gridBagConstraints);
        this.brightnessSlider.setMajorTickSpacing(100);
        this.brightnessSlider.setMinorTickSpacing(50);
        this.brightnessSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.brightnessSlider, gridBagConstraints);
        this.brightnessFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.brightnessField.setColumns(3);
        this.brightnessField.setHorizontalAlignment(11);
        this.brightnessField.setText("0");
        this.brightnessField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HSBChooser.this.brightnessFieldFocusLost(evt);
            }
        });
        this.brightnessFieldPanel.add(this.brightnessField);
        this.brightnessFieldLabel.setText("%");
        this.brightnessFieldPanel.add(this.brightnessFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.brightnessFieldPanel, gridBagConstraints);
        this.springPanel.setLayout(new BorderLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.weighty = 1.0;
        this.add((Component)this.springPanel, gridBagConstraints);
    }

    private void brightnessFieldFocusLost(FocusEvent evt) {
        this.brightnessField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(2).getValue()));
    }

    private void saturationFieldFocusLost(FocusEvent evt) {
        this.saturationField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(1).getValue()));
    }

    private void hueFieldFocusLost(FocusEvent evt) {
        this.hueField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(0).getValue()));
    }
}

