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
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.NominalCMYKColorSliderModel;

public class CMYKChooser
extends AbstractColorChooserPanel
implements UIResource {
    private ColorSliderModel ccModel;
    private int updateRecursion = 0;
    private JTextField blackField;
    private JLabel blackFieldLabel;
    private JPanel blackFieldPanel;
    private JLabel blackLabel;
    private JSlider blackSlider;
    private JTextField cyanField;
    private JLabel cyanFieldLabel;
    private JPanel cyanFieldPanel;
    private JLabel cyanLabel;
    private JSlider cyanSlider;
    private JTextField magentaField;
    private JLabel magentaFieldLabel;
    private JPanel magentaFieldPanel;
    private JLabel magentaLabel;
    private JSlider magentaSlider;
    private JPanel springPanel;
    private JTextField yellowField;
    private JLabel yellowFieldLabel;
    private JPanel yellowFieldPanel;
    private JLabel yellowLabel;
    private JSlider yellowSlider;

    public CMYKChooser() {
        this.initComponents();
        Font font = UIManager.getFont("ColorChooser.font");
        this.cyanLabel.setFont(font);
        this.cyanSlider.setFont(font);
        this.cyanField.setFont(font);
        this.cyanFieldLabel.setFont(font);
        this.magentaLabel.setFont(font);
        this.magentaSlider.setFont(font);
        this.magentaField.setFont(font);
        this.magentaFieldLabel.setFont(font);
        this.yellowLabel.setFont(font);
        this.yellowSlider.setFont(font);
        this.yellowField.setFont(font);
        this.yellowFieldLabel.setFont(font);
        this.blackLabel.setFont(font);
        this.blackSlider.setFont(font);
        this.blackField.setFont(font);
        this.blackFieldLabel.setFont(font);
        int textSliderGap = UIManager.getInt("ColorChooser.textSliderGap");
        if (textSliderGap != 0) {
            EmptyBorder fieldBorder = new EmptyBorder(0, textSliderGap, 0, 0);
            this.cyanFieldPanel.setBorder(fieldBorder);
            this.magentaFieldPanel.setBorder(fieldBorder);
            this.yellowFieldPanel.setBorder(fieldBorder);
            this.blackFieldPanel.setBorder(fieldBorder);
        }
        this.ccModel = new NominalCMYKColorSliderModel();
        this.ccModel.configureColorSlider(0, this.cyanSlider);
        this.ccModel.configureColorSlider(1, this.magentaSlider);
        this.ccModel.configureColorSlider(2, this.yellowSlider);
        this.ccModel.configureColorSlider(3, this.blackSlider);
        this.cyanField.setText(Integer.toString(this.cyanSlider.getValue()));
        this.magentaField.setText(Integer.toString(this.magentaSlider.getValue()));
        this.yellowField.setText(Integer.toString(this.yellowSlider.getValue()));
        this.blackField.setText(Integer.toString(this.blackSlider.getValue()));
        Insets borderMargin = (Insets)UIManager.getInsets("Component.visualMargin").clone();
        borderMargin.left = 3 - borderMargin.left;
        this.cyanFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        this.magentaFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        this.yellowFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        this.blackFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        new ColorSliderTextFieldHandler(this.cyanField, this.ccModel, 0);
        new ColorSliderTextFieldHandler(this.magentaField, this.ccModel, 1);
        new ColorSliderTextFieldHandler(this.yellowField, this.ccModel, 2);
        new ColorSliderTextFieldHandler(this.blackField, this.ccModel, 3);
        this.ccModel.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent evt) {
                CMYKChooser.this.setColorToModel(CMYKChooser.this.ccModel.getColor());
            }
        });
        this.cyanField.setMinimumSize(this.cyanField.getPreferredSize());
        this.magentaField.setMinimumSize(this.magentaField.getPreferredSize());
        this.yellowField.setMinimumSize(this.yellowField.getPreferredSize());
        this.blackField.setMinimumSize(this.blackField.getPreferredSize());
        VisualMargin bm = new VisualMargin(false, false, true, false);
        this.cyanLabel.setBorder(bm);
        this.magentaLabel.setBorder(bm);
        this.yellowLabel.setBorder(bm);
        this.blackLabel.setBorder(bm);
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.cmykSliders");
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
        if (this.updateRecursion == 0) {
            ++this.updateRecursion;
            this.ccModel.setColor(this.getColorFromModel());
            --this.updateRecursion;
        }
    }

    public void setColorToModel(Color color) {
        if (this.updateRecursion == 0) {
            ++this.updateRecursion;
            this.getColorSelectionModel().setSelectedColor(color);
            --this.updateRecursion;
        }
    }

    private void initComponents() {
        this.cyanLabel = new JLabel();
        this.cyanSlider = new JSlider();
        this.cyanFieldPanel = new JPanel();
        this.cyanField = new JTextField();
        this.cyanFieldLabel = new JLabel();
        this.magentaLabel = new JLabel();
        this.magentaSlider = new JSlider();
        this.magentaFieldPanel = new JPanel();
        this.magentaField = new JTextField();
        this.magentaFieldLabel = new JLabel();
        this.yellowLabel = new JLabel();
        this.yellowSlider = new JSlider();
        this.yellowFieldPanel = new JPanel();
        this.yellowField = new JTextField();
        this.yellowFieldLabel = new JLabel();
        this.blackLabel = new JLabel();
        this.blackSlider = new JSlider();
        this.blackFieldPanel = new JPanel();
        this.blackField = new JTextField();
        this.blackFieldLabel = new JLabel();
        this.springPanel = new JPanel();
        this.setLayout(new GridBagLayout());
        this.cyanLabel.setText(UIManager.getString("ColorChooser.cmykCyanText"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.cyanLabel, gridBagConstraints);
        this.cyanSlider.setMajorTickSpacing(100);
        this.cyanSlider.setMinorTickSpacing(50);
        this.cyanSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.cyanSlider, gridBagConstraints);
        this.cyanFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.cyanField.setColumns(3);
        this.cyanField.setHorizontalAlignment(11);
        this.cyanField.setText("0");
        this.cyanField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                CMYKChooser.this.cyanFieldFocusLost(evt);
            }
        });
        this.cyanFieldPanel.add(this.cyanField);
        this.cyanFieldLabel.setText("%");
        this.cyanFieldPanel.add(this.cyanFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.cyanFieldPanel, gridBagConstraints);
        this.magentaLabel.setText(UIManager.getString("ColorChooser.cmykMagentaText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.magentaLabel, gridBagConstraints);
        this.magentaSlider.setMajorTickSpacing(100);
        this.magentaSlider.setMinorTickSpacing(50);
        this.magentaSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.magentaSlider, gridBagConstraints);
        this.magentaFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.magentaField.setColumns(3);
        this.magentaField.setHorizontalAlignment(11);
        this.magentaField.setText("0");
        this.magentaField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                CMYKChooser.this.magentaFieldFocusLost(evt);
            }
        });
        this.magentaFieldPanel.add(this.magentaField);
        this.magentaFieldLabel.setText("%");
        this.magentaFieldPanel.add(this.magentaFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.magentaFieldPanel, gridBagConstraints);
        this.yellowLabel.setText(UIManager.getString("ColorChooser.cmykYellowText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.yellowLabel, gridBagConstraints);
        this.yellowSlider.setMajorTickSpacing(100);
        this.yellowSlider.setMinorTickSpacing(50);
        this.yellowSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.yellowSlider, gridBagConstraints);
        this.yellowFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.yellowField.setColumns(3);
        this.yellowField.setHorizontalAlignment(11);
        this.yellowField.setText("0");
        this.yellowField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                CMYKChooser.this.yellowFieldFocusLost(evt);
            }
        });
        this.yellowFieldPanel.add(this.yellowField);
        this.yellowFieldLabel.setText("%");
        this.yellowFieldPanel.add(this.yellowFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.yellowFieldPanel, gridBagConstraints);
        this.blackLabel.setText(UIManager.getString("ColorChooser.cmykBlackText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.blackLabel, gridBagConstraints);
        this.blackSlider.setMajorTickSpacing(100);
        this.blackSlider.setMinorTickSpacing(50);
        this.blackSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.blackSlider, gridBagConstraints);
        this.blackFieldPanel.setLayout(new FlowLayout(1, 0, 0));
        this.blackField.setColumns(3);
        this.blackField.setHorizontalAlignment(11);
        this.blackField.setText("0");
        this.blackField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                CMYKChooser.this.blackFieldFocusLost(evt);
            }
        });
        this.blackFieldPanel.add(this.blackField);
        this.blackFieldLabel.setText("%");
        this.blackFieldPanel.add(this.blackFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.blackFieldPanel, gridBagConstraints);
        this.springPanel.setLayout(new BorderLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.weighty = 1.0;
        this.add((Component)this.springPanel, gridBagConstraints);
    }

    private void blackFieldFocusLost(FocusEvent evt) {
        this.blackField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(3).getValue()));
    }

    private void yellowFieldFocusLost(FocusEvent evt) {
        this.yellowField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(2).getValue()));
    }

    private void magentaFieldFocusLost(FocusEvent evt) {
        this.magentaField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(1).getValue()));
    }

    private void cyanFieldFocusLost(FocusEvent evt) {
        this.cyanField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(0).getValue()));
    }
}

