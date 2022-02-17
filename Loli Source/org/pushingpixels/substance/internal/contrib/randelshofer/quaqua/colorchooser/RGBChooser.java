/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.VisualMargin;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderTextFieldHandler;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.RGBColorSliderModel;

public class RGBChooser
extends AbstractColorChooserPanel
implements UIResource {
    private ColorSliderModel ccModel = new RGBColorSliderModel();
    private JTextField blueField;
    private JLabel blueLabel;
    private JSlider blueSlider;
    private JTextField greenField;
    private JLabel greenLabel;
    private JSlider greenSlider;
    private JTextField redField;
    private JLabel redLabel;
    private JSlider redSlider;
    private JPanel springPanel;

    public RGBChooser() {
        this.initComponents();
        Font font = UIManager.getFont("ColorChooser.font");
        this.redLabel.setFont(font);
        this.redSlider.setFont(font);
        this.redField.setFont(font);
        this.greenLabel.setFont(font);
        this.greenSlider.setFont(font);
        this.greenField.setFont(font);
        this.blueLabel.setFont(font);
        this.blueSlider.setFont(font);
        this.blueField.setFont(font);
        int textSliderGap = UIManager.getInt("ColorChooser.textSliderGap");
        if (textSliderGap != 0) {
            Insets fieldInsets = new Insets(0, textSliderGap, 0, 0);
            GridBagLayout layout = (GridBagLayout)this.getLayout();
            GridBagConstraints gbc = layout.getConstraints(this.redField);
            gbc.insets = fieldInsets;
            layout.setConstraints(this.redField, gbc);
            gbc = layout.getConstraints(this.greenField);
            gbc.insets = fieldInsets;
            layout.setConstraints(this.greenField, gbc);
            gbc = layout.getConstraints(this.blueField);
            gbc.insets = fieldInsets;
            layout.setConstraints(this.blueField, gbc);
        }
        this.ccModel.configureColorSlider(0, this.redSlider);
        this.ccModel.configureColorSlider(1, this.greenSlider);
        this.ccModel.configureColorSlider(2, this.blueSlider);
        this.redField.setText(Integer.toString(this.redSlider.getValue()));
        this.greenField.setText(Integer.toString(this.greenSlider.getValue()));
        this.blueField.setText(Integer.toString(this.blueSlider.getValue()));
        new ColorSliderTextFieldHandler(this.redField, this.ccModel, 0);
        new ColorSliderTextFieldHandler(this.greenField, this.ccModel, 1);
        new ColorSliderTextFieldHandler(this.blueField, this.ccModel, 2);
        this.ccModel.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent evt) {
                RGBChooser.this.setColorToModel(RGBChooser.this.ccModel.getColor());
            }
        });
        this.redField.setMinimumSize(this.redField.getPreferredSize());
        this.greenField.setMinimumSize(this.greenField.getPreferredSize());
        this.blueField.setMinimumSize(this.blueField.getPreferredSize());
        VisualMargin bm = new VisualMargin(false, false, true, false);
        this.redLabel.setBorder(bm);
        this.greenLabel.setBorder(bm);
        this.blueLabel.setBorder(bm);
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.rgbSliders");
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
        this.redLabel = new JLabel();
        this.redSlider = new JSlider();
        this.redField = new JTextField();
        this.greenLabel = new JLabel();
        this.greenSlider = new JSlider();
        this.greenField = new JTextField();
        this.blueLabel = new JLabel();
        this.blueSlider = new JSlider();
        this.blueField = new JTextField();
        this.springPanel = new JPanel();
        this.setLayout(new GridBagLayout());
        this.redLabel.setText(UIManager.getString("ColorChooser.rgbRedText"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.redLabel, gridBagConstraints);
        this.redSlider.setMajorTickSpacing(255);
        this.redSlider.setMaximum(255);
        this.redSlider.setMinorTickSpacing(128);
        this.redSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 15;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.redSlider, gridBagConstraints);
        this.redField.setColumns(3);
        this.redField.setHorizontalAlignment(11);
        this.redField.setText("0");
        this.redField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                RGBChooser.this.redFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.redField, gridBagConstraints);
        this.greenLabel.setText(UIManager.getString("ColorChooser.rgbGreenText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.greenLabel, gridBagConstraints);
        this.greenSlider.setMajorTickSpacing(255);
        this.greenSlider.setMaximum(255);
        this.greenSlider.setMinorTickSpacing(128);
        this.greenSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 15;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.greenSlider, gridBagConstraints);
        this.greenField.setColumns(3);
        this.greenField.setHorizontalAlignment(11);
        this.greenField.setText("0");
        this.greenField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                RGBChooser.this.greenFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.greenField, gridBagConstraints);
        this.blueLabel.setText(UIManager.getString("ColorChooser.rgbBlueText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.blueLabel, gridBagConstraints);
        this.blueSlider.setMajorTickSpacing(255);
        this.blueSlider.setMaximum(255);
        this.blueSlider.setMinorTickSpacing(128);
        this.blueSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 15;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.blueSlider, gridBagConstraints);
        this.blueField.setColumns(3);
        this.blueField.setHorizontalAlignment(11);
        this.blueField.setText("0");
        this.blueField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                RGBChooser.this.blueFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.blueField, gridBagConstraints);
        this.springPanel.setLayout(new BorderLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.weighty = 1.0;
        this.add((Component)this.springPanel, gridBagConstraints);
    }

    private void blueFieldFocusLost(FocusEvent evt) {
        this.blueField.setText(Integer.toString(this.ccModel.getValue(2)));
    }

    private void greenFieldFocusLost(FocusEvent evt) {
        this.greenField.setText(Integer.toString(this.ccModel.getValue(1)));
    }

    private void redFieldFocusLost(FocusEvent evt) {
        this.redField.setText(Integer.toString(this.ccModel.getValue(0)));
    }
}

