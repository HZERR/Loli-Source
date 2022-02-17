/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.VisualMargin;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderTextFieldHandler;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.GrayColorSliderModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.SmallColorWellBorder;

public class GrayChooser
extends AbstractColorChooserPanel
implements UIResource {
    private ColorSliderModel ccModel = new GrayColorSliderModel();
    private int updateRecursion;
    private JTextField brightnessField;
    private JLabel brightnessFieldLabel;
    private JPanel brightnessFieldPanel;
    private JLabel brightnessLabel;
    private JSlider brightnessSlider;
    private JButton fiftyPercentButton;
    private JButton hundredPercentButton;
    private JPanel percentPanel;
    private JButton seventyFivePercentButton;
    private JPanel springPanel;
    private JButton twentyFivePercentButton;
    private JButton zeroPercentButton;

    public GrayChooser() {
        this.initComponents();
        Font font = UIManager.getFont("ColorChooser.font");
        this.brightnessLabel.setFont(font);
        this.brightnessSlider.setFont(font);
        this.brightnessField.setFont(font);
        this.brightnessFieldLabel.setFont(font);
        this.zeroPercentButton.setFont(font);
        this.twentyFivePercentButton.setFont(font);
        this.fiftyPercentButton.setFont(font);
        this.seventyFivePercentButton.setFont(font);
        int textSliderGap = UIManager.getInt("ColorChooser.textSliderGap");
        if (textSliderGap != 0) {
            EmptyBorder fieldBorder = new EmptyBorder(0, textSliderGap, 0, 0);
            this.brightnessFieldPanel.setBorder(fieldBorder);
        }
        this.ccModel.configureColorSlider(0, this.brightnessSlider);
        this.brightnessField.setText(Integer.toString(this.brightnessSlider.getValue()));
        Insets borderMargin = (Insets)UIManager.getInsets("Component.visualMargin").clone();
        borderMargin.left = 3 - borderMargin.left;
        this.brightnessFieldLabel.putClientProperty("Quaqua.Component.visualMargin", borderMargin);
        new ColorSliderTextFieldHandler(this.brightnessField, this.ccModel, 0);
        this.ccModel.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent evt) {
                if (GrayChooser.this.updateRecursion == 0) {
                    GrayChooser.this.setColorToModel(GrayChooser.this.ccModel.getColor());
                }
            }
        });
        this.brightnessField.setMinimumSize(this.brightnessField.getPreferredSize());
        VisualMargin bm = new VisualMargin(false, false, true, false);
        this.brightnessLabel.setBorder(bm);
        this.zeroPercentButton.putClientProperty("Quaqua.Button.style", "colorWell");
        this.twentyFivePercentButton.putClientProperty("Quaqua.Button.style", "colorWell");
        this.fiftyPercentButton.putClientProperty("Quaqua.Button.style", "colorWell");
        this.seventyFivePercentButton.putClientProperty("Quaqua.Button.style", "colorWell");
        this.hundredPercentButton.putClientProperty("Quaqua.Button.style", "colorWell");
        CompoundBorder b2 = new CompoundBorder(new VisualMargin(), new SmallColorWellBorder());
        this.zeroPercentButton.setBorder(b2);
        this.twentyFivePercentButton.setBorder(b2);
        this.fiftyPercentButton.setBorder(b2);
        this.seventyFivePercentButton.setBorder(b2);
        this.hundredPercentButton.setBorder(b2);
        Insets bmInsets = UIManager.getInsets("Component.visualMargin");
        Dimension d2 = new Dimension(12 + bmInsets.left + bmInsets.right, 12 + bmInsets.top + bmInsets.bottom);
        this.zeroPercentButton.setPreferredSize(d2);
        this.twentyFivePercentButton.setPreferredSize(d2);
        this.fiftyPercentButton.setPreferredSize(d2);
        this.seventyFivePercentButton.setPreferredSize(d2);
        this.hundredPercentButton.setPreferredSize(d2);
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.grayScaleSlider");
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
        ++this.updateRecursion;
        Color cfm = this.getColorFromModel();
        this.ccModel.setColor(cfm);
        --this.updateRecursion;
    }

    public void setColorToModel(Color color) {
        this.getColorSelectionModel().setSelectedColor(color);
    }

    private void initComponents() {
        this.brightnessLabel = new JLabel();
        this.brightnessSlider = new JSlider();
        this.brightnessFieldPanel = new JPanel();
        this.brightnessField = new JTextField();
        this.brightnessFieldLabel = new JLabel();
        this.springPanel = new JPanel();
        this.percentPanel = new JPanel();
        this.zeroPercentButton = new JButton();
        this.twentyFivePercentButton = new JButton();
        this.fiftyPercentButton = new JButton();
        this.seventyFivePercentButton = new JButton();
        this.hundredPercentButton = new JButton();
        this.setLayout(new GridBagLayout());
        this.brightnessLabel.setText(UIManager.getString("ColorChooser.hsbBrightnessText"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.brightnessLabel, gridBagConstraints);
        this.brightnessSlider.setMajorTickSpacing(100);
        this.brightnessSlider.setMinorTickSpacing(50);
        this.brightnessSlider.setPaintTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 5;
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
                GrayChooser.this.brightnessFieldFocusLost(evt);
            }
        });
        this.brightnessFieldPanel.add(this.brightnessField);
        this.brightnessFieldLabel.setText("%");
        this.brightnessFieldPanel.add(this.brightnessFieldLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.brightnessFieldPanel, gridBagConstraints);
        this.springPanel.setLayout(new BorderLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.weighty = 1.0;
        this.add((Component)this.springPanel, gridBagConstraints);
        this.percentPanel.setLayout(new GridBagLayout());
        this.zeroPercentButton.setBackground(new Color(0, 0, 0));
        this.zeroPercentButton.setToolTipText("0 %");
        this.zeroPercentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                GrayChooser.this.percentActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = 17;
        this.percentPanel.add((Component)this.zeroPercentButton, gridBagConstraints);
        this.twentyFivePercentButton.setBackground(new Color(64, 64, 64));
        this.twentyFivePercentButton.setToolTipText("25 %");
        this.twentyFivePercentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                GrayChooser.this.percentActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        this.percentPanel.add((Component)this.twentyFivePercentButton, gridBagConstraints);
        this.fiftyPercentButton.setBackground(new Color(128, 128, 128));
        this.fiftyPercentButton.setToolTipText("50 %");
        this.fiftyPercentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                GrayChooser.this.percentActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        this.percentPanel.add((Component)this.fiftyPercentButton, gridBagConstraints);
        this.seventyFivePercentButton.setBackground(new Color(192, 192, 192));
        this.seventyFivePercentButton.setToolTipText("75 %");
        this.seventyFivePercentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                GrayChooser.this.percentActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        this.percentPanel.add((Component)this.seventyFivePercentButton, gridBagConstraints);
        this.hundredPercentButton.setBackground(new Color(255, 255, 255));
        this.hundredPercentButton.setToolTipText("100 %");
        this.hundredPercentButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                GrayChooser.this.percentActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = 13;
        this.percentPanel.add((Component)this.hundredPercentButton, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = 2;
        this.add((Component)this.percentPanel, gridBagConstraints);
    }

    private void percentActionPerformed(ActionEvent evt) {
        this.setColorToModel(((JButton)evt.getSource()).getBackground());
    }

    private void brightnessFieldFocusLost(FocusEvent evt) {
        this.brightnessField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(0).getValue()));
    }
}

