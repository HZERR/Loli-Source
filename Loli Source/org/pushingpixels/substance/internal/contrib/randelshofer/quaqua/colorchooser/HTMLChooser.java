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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBox;
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
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderTextFieldHandler;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HTMLColorSliderModel;

public class HTMLChooser
extends AbstractColorChooserPanel
implements UIResource {
    private HTMLColorSliderModel ccModel = new HTMLColorSliderModel();
    private ChangeListener htmlListener;
    private static boolean lastWebSaveSelectionState = false;
    private int updateRecursion;
    private static final Object[][] colorNames = new Object[][]{{"Black", new Color(0)}, {"Green", new Color(32768)}, {"Silver", new Color(0xC0C0C0)}, {"Lime", new Color(65280)}, {"Gray", new Color(0x808080)}, {"Olive", new Color(0x808000)}, {"White", new Color(0xFFFFFF)}, {"Yellow", new Color(0xFFFF00)}, {"Maroon", new Color(0x800000)}, {"Navy", new Color(128)}, {"Red", new Color(0xFF0000)}, {"Blue", new Color(255)}, {"Purple", new Color(0x800080)}, {"Teal", new Color(32896)}, {"Fuchsia", new Color(0xFF00FF)}, {"Aqua", new Color(255)}};
    private static final HashMap nameToColorMap = new HashMap();
    private JTextField blueField;
    private JLabel blueLabel;
    private JSlider blueSlider;
    private JTextField greenField;
    private JLabel greenLabel;
    private JSlider greenSlider;
    private JTextField htmlField;
    private JLabel htmlLabel;
    private JPanel htmlPanel;
    private JTextField redField;
    private JLabel redLabel;
    private JSlider redSlider;
    private JPanel springPanel;
    private JCheckBox webSaveCheckBox;

    public HTMLChooser() {
        this.initComponents();
        Font font = UIManager.getFont("ColorChooser.font");
        this.redLabel.setFont(font);
        this.redSlider.setFont(font);
        this.redField.setFont(font);
        this.greenLabel.setFont(font);
        this.greenField.setFont(font);
        this.greenSlider.setFont(font);
        this.blueLabel.setFont(font);
        this.blueSlider.setFont(font);
        this.blueField.setFont(font);
        this.htmlLabel.setFont(font);
        this.htmlField.setFont(font);
        this.webSaveCheckBox.setFont(font);
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
        this.webSaveCheckBox.setSelected(lastWebSaveSelectionState);
        this.redSlider.setSnapToTicks(lastWebSaveSelectionState);
        this.greenSlider.setSnapToTicks(lastWebSaveSelectionState);
        this.blueSlider.setSnapToTicks(lastWebSaveSelectionState);
        this.htmlListener = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent evt) {
                Color c2 = HTMLChooser.this.ccModel.getColor();
                HTMLChooser.this.setColorToModel(c2);
                if (!c2.equals(nameToColorMap.get(HTMLChooser.this.htmlField.getText().toLowerCase())) && !HTMLChooser.this.htmlField.hasFocus()) {
                    String hex = Integer.toHexString(0xFFFFFF & c2.getRGB());
                    StringBuffer buf = new StringBuffer(7);
                    buf.append('#');
                    for (int i2 = hex.length(); i2 < 6; ++i2) {
                        buf.append('0');
                    }
                    buf.append(hex.toUpperCase());
                    if (!HTMLChooser.this.htmlField.getText().equals(buf.toString())) {
                        HTMLChooser.this.htmlField.setText(buf.toString());
                    }
                }
            }
        };
        ++this.updateRecursion;
        this.ccModel = new HTMLColorSliderModel();
        this.ccModel.setWebSaveOnly(lastWebSaveSelectionState);
        this.ccModel.configureColorSlider(0, this.redSlider);
        this.ccModel.configureColorSlider(1, this.greenSlider);
        this.ccModel.configureColorSlider(2, this.blueSlider);
        new ColorSliderTextFieldHandler(this.redField, this.ccModel, 0);
        new ColorSliderTextFieldHandler(this.greenField, this.ccModel, 1);
        new ColorSliderTextFieldHandler(this.blueField, this.ccModel, 2);
        this.ccModel.addChangeListener(this.htmlListener);
        this.redFieldFocusLost(null);
        this.greenFieldFocusLost(null);
        this.blueFieldFocusLost(null);
        this.htmlFieldFocusLost(null);
        --this.updateRecursion;
        this.redField.setMinimumSize(this.redField.getPreferredSize());
        this.greenField.setMinimumSize(this.greenField.getPreferredSize());
        this.blueField.setMinimumSize(this.blueField.getPreferredSize());
        this.htmlPanel.setMinimumSize(this.htmlPanel.getPreferredSize());
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
        return UIManager.getString("ColorChooser.htmlSliders");
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
            Color c2;
            ++this.updateRecursion;
            if (this.ccModel.isWebSaveOnly() && ((c2 = this.getColorFromModel()) == null || !HTMLColorSliderModel.isWebSave(c2.getRGB()))) {
                this.webSaveCheckBox.setSelected(false);
            }
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
        this.redLabel = new JLabel();
        this.redSlider = new JSlider();
        this.redField = new JTextField();
        this.greenLabel = new JLabel();
        this.greenField = new JTextField();
        this.greenSlider = new JSlider();
        this.blueLabel = new JLabel();
        this.blueSlider = new JSlider();
        this.blueField = new JTextField();
        this.htmlPanel = new JPanel();
        this.htmlLabel = new JLabel();
        this.htmlField = new JTextField();
        this.webSaveCheckBox = new JCheckBox();
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
        this.redSlider.setMinorTickSpacing(51);
        this.redSlider.setPaintTicks(true);
        this.redSlider.setSnapToTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.redSlider, gridBagConstraints);
        this.redField.setColumns(3);
        this.redField.setHorizontalAlignment(11);
        this.redField.setText("0");
        this.redField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HTMLChooser.this.redFieldFocusLost(evt);
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
        this.greenField.setColumns(3);
        this.greenField.setHorizontalAlignment(11);
        this.greenField.setText("0");
        this.greenField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HTMLChooser.this.greenFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.greenField, gridBagConstraints);
        this.greenSlider.setMajorTickSpacing(255);
        this.greenSlider.setMaximum(255);
        this.greenSlider.setMinorTickSpacing(51);
        this.greenSlider.setPaintTicks(true);
        this.greenSlider.setSnapToTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.greenSlider, gridBagConstraints);
        this.blueLabel.setText(UIManager.getString("ColorChooser.rgbBlueText"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 16;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        this.add((Component)this.blueLabel, gridBagConstraints);
        this.blueSlider.setMajorTickSpacing(255);
        this.blueSlider.setMaximum(255);
        this.blueSlider.setMinorTickSpacing(51);
        this.blueSlider.setPaintTicks(true);
        this.blueSlider.setSnapToTicks(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.blueSlider, gridBagConstraints);
        this.blueField.setColumns(3);
        this.blueField.setHorizontalAlignment(11);
        this.blueField.setText("0");
        this.blueField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HTMLChooser.this.blueFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = 15;
        this.add((Component)this.blueField, gridBagConstraints);
        this.htmlPanel.setLayout(new FlowLayout(1, 0, 5));
        this.htmlLabel.setText(UIManager.getString("ColorChooser.htmlText"));
        this.htmlLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        this.htmlPanel.add(this.htmlLabel);
        this.htmlField.setColumns(7);
        this.htmlField.setText("#000000");
        this.htmlField.addFocusListener(new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent evt) {
                HTMLChooser.this.htmlFieldFocusLost(evt);
            }
        });
        this.htmlPanel.add(this.htmlField);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.anchor = 13;
        gridBagConstraints.insets = new Insets(12, 0, 0, 0);
        this.add((Component)this.htmlPanel, gridBagConstraints);
        this.webSaveCheckBox.setText(UIManager.getString("ColorChooser.htmlChooseOnlyWebSaveColorsText"));
        this.webSaveCheckBox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent evt) {
                HTMLChooser.this.webSaveChanged(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.anchor = 17;
        this.add((Component)this.webSaveCheckBox, gridBagConstraints);
        this.springPanel.setLayout(new BorderLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.weighty = 1.0;
        this.add((Component)this.springPanel, gridBagConstraints);
    }

    private void blueFieldFocusLost(FocusEvent evt) {
        String hex = Integer.toHexString(this.ccModel.getBoundedRangeModel(2).getValue()).toUpperCase();
        this.blueField.setText(hex.length() == 1 ? "0" + hex : hex);
    }

    private void greenFieldFocusLost(FocusEvent evt) {
        String hex = Integer.toHexString(this.ccModel.getBoundedRangeModel(1).getValue()).toUpperCase();
        this.greenField.setText(hex.length() == 1 ? "0" + hex : hex);
    }

    private void redFieldFocusLost(FocusEvent evt) {
        String hex = Integer.toHexString(this.ccModel.getBoundedRangeModel(0).getValue()).toUpperCase();
        this.redField.setText(hex.length() == 1 ? "0" + hex : hex);
    }

    private void htmlFieldFocusLost(FocusEvent evt) {
        Color mc = this.ccModel.getColor();
        Color fc = (Color)nameToColorMap.get(this.htmlField.getText().toLowerCase());
        if (fc == null || !fc.equals(mc)) {
            String hex = Integer.toHexString(0xFFFFFF & mc.getRGB());
            StringBuffer buf = new StringBuffer(7);
            buf.append('#');
            for (int i2 = hex.length(); i2 < 6; ++i2) {
                buf.append('0');
            }
            buf.append(hex.toUpperCase());
            this.htmlField.setText(buf.toString());
        }
    }

    private void webSaveChanged(ItemEvent evt) {
        boolean b2 = this.webSaveCheckBox.isSelected();
        this.redSlider.setSnapToTicks(b2);
        this.greenSlider.setSnapToTicks(b2);
        this.blueSlider.setSnapToTicks(b2);
        lastWebSaveSelectionState = b2;
        this.ccModel.setWebSaveOnly(b2);
    }

    static {
        for (int i2 = 0; i2 < colorNames.length; ++i2) {
            nameToColorMap.put(((String)colorNames[i2][0]).toLowerCase(), colorNames[i2][1]);
        }
    }
}

