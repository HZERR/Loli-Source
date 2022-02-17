/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.xoetrope.editor.color;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.pushingpixels.substance.internal.contrib.xoetrope.editor.color.ModelColor;

public class ColorWheelPanel
extends AbstractColorChooserPanel
implements ActionListener,
MouseListener,
MouseMotionListener,
MouseWheelListener,
ChangeListener {
    public static final int MONOCHROMATIC_SCHEME = 0;
    public static final int CONTRASTING_SCHEME = 1;
    public static final int SOFT_CONTRAST_SCHEME = 2;
    public static final int DOUBLE_CONTRAST_SCHEME = 3;
    public static final int ANALOGIC_SCHEME = 4;
    public static final int CTRL_ADJUST = 0;
    public static final int ALWAYS_ADJUST = 1;
    public static final int NEVER_ADJUST = 2;
    protected JTextField hueEdit;
    protected JTextField satEdit;
    protected JTextField brightEdit;
    protected JTextField baseColorEdit;
    protected BufferedImage pickerImage;
    protected ColorWheel imagePicker;
    protected JPanel fixedPanel;
    protected JButton resetBtn;
    protected JSlider brightnessSlider;
    protected JSlider saturationSlider;
    protected JLabel baseColorLabel;
    protected Ellipse2D innerCircle;
    protected Ellipse2D outerCircle;
    protected Ellipse2D borderCircle;
    protected JCheckBox useWebColors;
    protected JCheckBox decimalRGB;
    protected Font font9pt;
    protected ModelColor chooserColor;
    protected ModelColor[] selectedIttenColours;
    private float[] values = new float[3];
    private double h;
    private double s;
    private double b;
    private int colorScheme = 0;
    private boolean busy = false;
    private boolean displayScheme = false;
    private boolean hasChooser = false;
    private ArrayList<ChangeListener> changeListeners = new ArrayList();
    private static double[] arcDelta = new double[]{-7.5, -7.5, -7.5, -7.5, -7.5, -1.0, 4.0, 7.5};
    private double ringThickness;
    private GeneralPath[] paths;
    private static ResourceBundle labelBundle;
    private GeneralPath rolloverPath;
    private GeneralPath selectedPath;
    private boolean showRollovers;
    private Color rolloverColor;
    private Color selectedColor;
    private Color systemColor;
    private String fontFamily;
    private int adjustWheel = 0;
    private boolean adjustRollover = true;
    private boolean ctrlKeyDown = false;
    private double saturationMultipler = 1.0;
    private double brightnessMultipler = 1.0;

    public ColorWheelPanel() {
        this.font9pt = UIManager.getFont("ColorChooser.smallFont");
        if (this.font9pt == null) {
            this.font9pt = new Font("Arial", 0, 9);
        }
        this.fontFamily = this.font9pt.getFamily();
        this.showRollovers = true;
        this.innerCircle = new Ellipse2D.Double(96.0, 96.0, 36.0, 36.0);
        this.outerCircle = new Ellipse2D.Double(6.0, 6.0, 214.0, 214.0);
        this.borderCircle = new Ellipse2D.Double(0.0, 0.0, 227.0, 227.0);
        this.fixedPanel = new JPanel();
        this.fixedPanel.setLayout(null);
        this.fixedPanel.setOpaque(false);
        this.fixedPanel.setBounds(0, 0, 255, 328);
        this.fixedPanel.setPreferredSize(new Dimension(255, 328));
        this.setLayout(new LayoutManager(){

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public void layoutContainer(Container parent) {
                Dimension fpp = ColorWheelPanel.this.fixedPanel.getPreferredSize();
                int dx = (parent.getWidth() - fpp.width) / 2;
                int dy = (parent.getHeight() - fpp.height) / 2;
                ColorWheelPanel.this.fixedPanel.setBounds(dx, dy, fpp.width, fpp.height);
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return this.preferredLayoutSize(parent);
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return ColorWheelPanel.this.fixedPanel.getPreferredSize();
            }
        });
        this.imagePicker = new ColorWheel();
        this.imagePicker.setBounds(0, 0, 228, 228);
        this.imagePicker.addMouseListener(this);
        this.imagePicker.addMouseMotionListener(this);
        this.imagePicker.setOpaque(false);
        this.imagePicker.addMouseWheelListener(this);
        this.fixedPanel.add(this.imagePicker);
        this.brightnessSlider = new JSlider(1);
        this.brightnessSlider.setBounds(230, 0, 25, 108);
        this.brightnessSlider.setMinimum(0);
        this.brightnessSlider.setMaximum(100);
        this.brightnessSlider.setValue(100);
        this.brightnessSlider.setOpaque(false);
        this.brightnessSlider.setPaintLabels(true);
        this.brightnessSlider.addChangeListener(this);
        this.brightnessSlider.addMouseWheelListener(this);
        this.brightnessSlider.addMouseMotionListener(this);
        this.brightnessSlider.setToolTipText(ColorWheelPanel.getLabel("Xoetrope.ctrlDrag", "CTRL+drag to adjust the color wheel"));
        this.fixedPanel.add(this.brightnessSlider);
        this.resetBtn = new JButton();
        this.resetBtn.setBounds(237, 109, 10, 10);
        this.resetBtn.setBackground(this.getBackground());
        this.resetBtn.addActionListener(this);
        this.resetBtn.setToolTipText(ColorWheelPanel.getLabel("Xoetrope.reset", "Reset the color wheel sauturation and brightness"));
        this.fixedPanel.add(this.resetBtn);
        this.saturationSlider = new JSlider(1);
        this.saturationSlider.setBounds(230, 120, 25, 110);
        this.saturationSlider.setMinimum(0);
        this.saturationSlider.setMaximum(100);
        this.saturationSlider.setValue(100);
        this.saturationSlider.setOpaque(false);
        this.saturationSlider.setInverted(true);
        this.saturationSlider.setPaintLabels(true);
        this.saturationSlider.addChangeListener(this);
        this.saturationSlider.addMouseWheelListener(this);
        this.saturationSlider.addMouseMotionListener(this);
        this.saturationSlider.setToolTipText(ColorWheelPanel.getLabel("Xoetrope.ctrlDrag", "CTRL+drag to adjust the color wheel"));
        this.fixedPanel.add(this.saturationSlider);
        this.useWebColors = new JCheckBox(ColorWheelPanel.getLabel("Xoetrope.webSafeColors", "Use web safe colors"));
        this.useWebColors.setBounds(8, 248, 160, 18);
        this.useWebColors.addActionListener(this);
        this.useWebColors.setOpaque(false);
        this.useWebColors.setFont(this.font9pt);
        this.fixedPanel.add(this.useWebColors);
        this.decimalRGB = new JCheckBox(ColorWheelPanel.getLabel("Xoetrope.decimalRGB", "Decimal RGB"));
        this.decimalRGB.setBounds(173, 248, 88, 18);
        this.decimalRGB.addActionListener(this);
        this.decimalRGB.setOpaque(false);
        this.decimalRGB.setFont(this.font9pt);
        this.fixedPanel.add(this.decimalRGB);
        this.baseColorLabel = new JLabel();
        this.baseColorLabel.setBounds(10, 268, 160, 18);
        this.baseColorLabel.setBackground(Color.red);
        this.baseColorLabel.setOpaque(true);
        this.baseColorLabel.setToolTipText(ColorWheelPanel.getLabel("Xoetrope.systemColorsTooltip", "Right click for system colours"));
        this.fixedPanel.add(this.baseColorLabel);
        this.baseColorLabel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent me) {
                ColorWheelPanel.this.showSystemColorList(me.getPoint());
            }
        });
        this.baseColorEdit = new JTextField();
        this.baseColorEdit.setBounds(180, 268, 75, 18);
        this.baseColorEdit.setOpaque(true);
        this.fixedPanel.add(this.baseColorEdit);
        this.baseColorEdit.addActionListener(this);
        this.hueEdit = new JTextField();
        this.hueEdit.setBounds(10, 288, 75, 20);
        this.fixedPanel.add(this.hueEdit);
        this.hueEdit.setText("0");
        this.hueEdit.getDocument().addDocumentListener(new ColorDocumentListener(this.hueEdit));
        JLabel hueLabel = new JLabel(ColorWheelPanel.getLabel("Xoetrope.hue", "Hue") + " \u00b0");
        hueLabel.setBounds(10, 308, 75, 20);
        hueLabel.setFont(this.font9pt);
        this.fixedPanel.add(hueLabel);
        this.satEdit = new JTextField();
        this.satEdit.setBounds(95, 288, 75, 20);
        this.fixedPanel.add(this.satEdit);
        this.satEdit.setText("0");
        this.satEdit.getDocument().addDocumentListener(new ColorDocumentListener(this.satEdit));
        JLabel satLabel = new JLabel(ColorWheelPanel.getLabel("Xoetrope.saturation", "Saturation") + " %");
        satLabel.setBounds(95, 308, 75, 20);
        satLabel.setFont(this.font9pt);
        this.fixedPanel.add(satLabel);
        this.brightEdit = new JTextField();
        this.brightEdit.setBounds(180, 288, 75, 20);
        this.fixedPanel.add(this.brightEdit);
        this.brightEdit.setText("0");
        this.brightEdit.getDocument().addDocumentListener(new ColorDocumentListener(this.brightEdit));
        JLabel brightLabel = new JLabel(ColorWheelPanel.getLabel("Xoetrope.brightness", "Brightness") + " %");
        brightLabel.setBounds(180, 308, 75, 20);
        brightLabel.setFont(this.font9pt);
        this.fixedPanel.add(brightLabel);
        this.add(this.fixedPanel);
    }

    public void setSelectedColors(ModelColor[] clrs) {
        this.selectedIttenColours = clrs;
    }

    public void addChangeListener(ChangeListener l2) {
        this.changeListeners.add(l2);
    }

    public void removeChangeListener(ChangeListener l2) {
        this.changeListeners.remove(l2);
    }

    public boolean useDecimalRGB() {
        return this.decimalRGB.isSelected();
    }

    public boolean useWebColors() {
        return this.useWebColors.isSelected();
    }

    public void setDisplayScheme(boolean disp) {
        this.displayScheme = disp;
    }

    public int getHue() {
        try {
            return Integer.parseInt(this.hueEdit.getText());
        }
        catch (NumberFormatException numberFormatException) {
            return 128;
        }
    }

    public void setHue(int h2) {
        try {
            if (h2 < 0) {
                h2 = 360 + h2;
            }
            int selHue = Math.max(0, Math.min(h2, 360));
            this.hueEdit.setText(Integer.toString(selHue));
            this.resetColor();
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }

    public int getSaturation() {
        try {
            return Integer.parseInt(this.satEdit.getText());
        }
        catch (NumberFormatException numberFormatException) {
            return 128;
        }
    }

    public int getBrightness() {
        try {
            return Integer.parseInt(this.brightEdit.getText());
        }
        catch (NumberFormatException numberFormatException) {
            return 128;
        }
    }

    public void setColorScheme(int scheme) {
        this.colorScheme = scheme;
    }

    boolean moveHue(Point pt) {
        if (this.borderCircle.contains(pt) && !this.outerCircle.contains(pt) || this.innerCircle.contains(pt)) {
            int h2 = this.getAngle(pt);
            this.hueEdit.setText(Integer.toString(h2));
            this.selectedPath = null;
            this.resetColor();
            return true;
        }
        return false;
    }

    private int getAngle(Point pt) {
        int eX = pt.x > 0 ? pt.x : 96;
        int eY = pt.y > 0 ? pt.y : 96;
        int x2 = eX - 112;
        int y2 = eY - 114;
        return (int)Math.round((Math.atan2(-x2, y2) * 180.0 / Math.PI + 180.0) % 360.0);
    }

    public void setColor(Color c2) {
        this.systemColor = null;
        if (c2 != null) {
            int r2 = c2.getRed();
            int g2 = c2.getGreen();
            int b2 = c2.getBlue();
            if (this.useWebColors.isSelected()) {
                r2 = Math.round(r2 / 51) * 51;
                g2 = Math.round(g2 / 51) * 51;
                b2 = Math.round(b2 / 51) * 51;
            }
            this.chooserColor = new ModelColor(r2, g2, b2);
        }
        c2 = new Color(this.chooserColor.R, this.chooserColor.G, this.chooserColor.B);
        float[] oldValues = this.values;
        this.values = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), this.values);
        if (this.values[1] == 0.0f) {
            this.s = this.values[1];
            this.b = this.values[2];
        } else if (this.values[2] == 0.0f) {
            this.b = this.values[2];
        } else {
            this.h = this.values[0];
            this.s = this.values[1];
            this.b = this.values[2];
        }
        this.h = Math.min(Math.max(this.h, 0.0), 1.0);
        this.s = Math.min(Math.max(this.s, 0.0), 1.0);
        this.b = Math.min(Math.max(this.b, 0.0), 1.0);
        if (this.values[1] != 0.0f) {
            if (this.values[1] != 0.0f) {
                this.setHue();
            }
            this.setSaturation();
        }
        this.setBrightness();
        this.busy = true;
        this.brightnessSlider.setValue(Integer.parseInt(this.brightEdit.getText()));
        this.saturationSlider.setValue(Integer.parseInt(this.satEdit.getText()));
        this.busy = false;
        this.baseColorLabel.setBackground(new Color(this.chooserColor.R, this.chooserColor.G, this.chooserColor.B));
        if (0.5 * (double)c2.getRed() + (double)c2.getGreen() + 0.3 * (double)c2.getBlue() < 220.0) {
            this.baseColorLabel.setForeground(Color.white);
        } else {
            this.baseColorLabel.setForeground(Color.black);
        }
        String colorStr = this.decimalRGB.isSelected() ? " " + Integer.toString(c2.getRed()) + "." + Integer.toString(c2.getGreen()) + "." + Integer.toString(c2.getBlue()) : " " + ModelColor.toHexString(c2.getRed()) + ModelColor.toHexString(c2.getGreen()) + ModelColor.toHexString(c2.getBlue());
        this.baseColorLabel.setText(colorStr);
        this.baseColorEdit.setText(colorStr);
        ChangeEvent evt = new ChangeEvent(this);
        int numListeners = this.changeListeners.size();
        for (int i2 = 0; i2 < numListeners; ++i2) {
            ChangeListener l2 = this.changeListeners.get(i2);
            l2.stateChanged(evt);
        }
        if (this.hasChooser) {
            this.getColorSelectionModel().setSelectedColor(c2);
        }
    }

    public Color getColor() {
        return new Color(this.chooserColor.R, this.chooserColor.G, this.chooserColor.B);
    }

    public ModelColor getChooserColour() {
        return this.chooserColor;
    }

    private void setHue() {
        this.hueEdit.setText(Integer.toString(this.chooserColor.getHue()));
    }

    private void setSaturation() {
        this.satEdit.setText(Integer.toString((int)(100.0 * this.chooserColor.S)));
    }

    private void setBrightness() {
        this.brightEdit.setText(Integer.toString((int)(100.0 * this.chooserColor.V)));
    }

    @Override
    public void actionPerformed(ActionEvent e2) {
        Object source = e2.getSource();
        if (source == this.resetBtn) {
            this.resetColorWheel();
        } else {
            if (source instanceof JMenuItem) {
                Color sysColor = this.getSystemColor(((JMenuItem)source).getText());
                if (sysColor != null) {
                    this.setColor(sysColor);
                }
                this.resetColor();
                this.systemColor = sysColor;
                if (this.hasChooser) {
                    this.hasChooser = false;
                    this.getColorSelectionModel().setSelectedColor(this.systemColor);
                    this.hasChooser = true;
                }
                return;
            }
            if (source == this.useWebColors) {
                boolean snap = this.useWebColors.isSelected();
                this.chooserColor.setWebSnap(snap);
                if (snap) {
                    this.resetColor();
                } else {
                    source = this.baseColorEdit;
                }
            } else if (source == this.baseColorEdit) {
                String hex = this.baseColorEdit.getText().trim();
                if (hex.length() == 0) {
                    this.resetColor();
                } else if (this.decimalRGB.isSelected()) {
                    int pos = 0;
                    try {
                        int r2 = 255;
                        int g2 = 0;
                        int b2 = 0;
                        int pos2 = hex.indexOf(46, pos);
                        if (pos2 > 0) {
                            r2 = Integer.parseInt(hex.substring(pos, pos2));
                            ++pos2;
                            pos = pos2;
                            if ((pos2 = hex.indexOf(46, pos)) > 0) {
                                g2 = Integer.parseInt(hex.substring(pos, pos2));
                                pos = ++pos2;
                                if (pos2 < hex.length()) {
                                    b2 = Integer.parseInt(hex.substring(pos));
                                }
                            }
                        }
                        this.setColor(new Color(r2, g2, b2));
                    }
                    catch (NumberFormatException nfe) {
                        this.setColor(Color.red);
                        this.baseColorEdit.setText("255.0.0");
                    }
                } else {
                    for (int i2 = hex.length(); i2 < 6; ++i2) {
                        hex = hex + "0";
                    }
                    try {
                        this.setColor(new Color(ModelColor.hex2dec(hex.substring(0, 2)), ModelColor.hex2dec(hex.substring(2, 4)), ModelColor.hex2dec(hex.substring(4, 6))));
                    }
                    catch (NumberFormatException nfe) {
                        this.setColor(Color.red);
                        this.baseColorEdit.setText("FF0000");
                    }
                }
            } else {
                this.resetColor();
            }
        }
    }

    private void resetColor() {
        if (this.chooserColor != null) {
            if (!this.busy) {
                this.busy = true;
                int h2 = 0;
                try {
                    h2 = Integer.parseInt(this.hueEdit.getText());
                    this.selectedPath = null;
                }
                catch (NumberFormatException nfe) {
                    this.hueEdit.setText("0");
                }
                if (h2 >= 360) {
                    this.hueEdit.setText(Integer.toString(h2 %= 360));
                }
                if (h2 < 0) {
                    h2 = (int)(((double)h2 + (Math.floor(-h2 / 360) + 1.0) * 360.0) % 360.0);
                    this.hueEdit.setText(Integer.toString(h2));
                }
                double s2 = 1.0;
                try {
                    s2 = (double)Integer.parseInt(this.satEdit.getText()) / 100.0;
                }
                catch (NumberFormatException nfe) {
                    this.satEdit.setText("100");
                }
                if (s2 > 1.0 || s2 < 0.0) {
                    s2 = s2 < 0.0 ? 0.0 : 1.0;
                    this.satEdit.setText(Integer.toString((int)(s2 * 100.0)));
                }
                double v2 = 1.0;
                try {
                    v2 = (double)Integer.parseInt(this.brightEdit.getText()) / 100.0;
                }
                catch (NumberFormatException nfe) {
                    this.brightEdit.setText("100");
                }
                if (v2 > 1.0 || v2 < 0.0) {
                    v2 = v2 < 0.0 ? 0.0 : 1.0;
                    this.brightEdit.setText(Integer.toString((int)(v2 * 100.0)));
                }
                if (this.shouldAdjustWheel()) {
                    this.saturationMultipler = s2;
                    this.brightnessMultipler = v2;
                }
                if (this.selectedIttenColours != null) {
                    this.selectedIttenColours[0].setHSV(h2, s2, v2);
                }
                this.chooserColor.setHSV(h2, s2, v2);
                this.busy = false;
            }
            this.setColor(null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e2) {
        Point pt;
        Object src = e2.getSource();
        if (src == this.imagePicker && this.borderCircle.contains(pt = e2.getPoint())) {
            this.selectedColor = this.rolloverColor;
            this.selectedPath = this.rolloverPath;
            if (!this.moveHue(pt) && this.outerCircle.contains(pt)) {
                int width = this.imagePicker.getWidth();
                int center = width / 2;
                int dx = Math.abs(pt.x - center);
                int dy = Math.abs(pt.y - center);
                double dr = Math.pow(dx * dx + dy * dy, 0.5);
                int bandIdx = (int)((dr -= this.ringThickness * 1.5) / this.ringThickness);
                int hue = 0;
                int bandOffset = bandIdx * 24;
                for (int i2 = 0; i2 < 24; ++i2) {
                    if (!this.paths[bandOffset + i2].contains(pt)) continue;
                    hue = i2 * 15;
                }
                int hueInc = hue / 15 % 2;
                ModelColor mc = new ModelColor((double)hue, ModelColor.SATURATION_BANDS[bandIdx], ModelColor.BRIGHTNESS_BANDS[bandIdx + 1 - hueInc]);
                mc = new ModelColor(mc.H, this.saturationMultipler * mc.S, this.brightnessMultipler * mc.V);
                Color pixelColor = new Color(mc.getRed(), mc.getGreen(), mc.getBlue());
                if (!pixelColor.equals(Color.white)) {
                    this.setColor(pixelColor);
                }
            }
        }
        if (this.displayScheme) {
            this.imagePicker.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e2) {
        this.imagePicker.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e2) {
    }

    @Override
    public void mouseEntered(MouseEvent e2) {
    }

    @Override
    public void mouseExited(MouseEvent e2) {
        this.rolloverPath = null;
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e2) {
        GeneralPath oldPath = this.rolloverPath;
        this.rolloverPath = null;
        if (e2.getSource() == this.imagePicker) {
            Point pt = e2.getPoint();
            if (this.paths != null) {
                int numPaths = this.paths.length;
                for (int i2 = 0; i2 < numPaths; ++i2) {
                    if (!this.paths[i2].contains(pt.x, pt.y)) continue;
                    this.rolloverPath = this.paths[i2];
                    ModelColor[][] baseColors = ModelColor.getBaseColors();
                    int ring = i2 / 24;
                    ModelColor modelColor = baseColors[i2 % 24][ring];
                    if (this.adjustRollover) {
                        modelColor = new ModelColor(modelColor.H, this.saturationMultipler * modelColor.S, this.brightnessMultipler * modelColor.V);
                    }
                    this.rolloverColor = new Color(modelColor.getRed(), modelColor.getGreen(), modelColor.getBlue());
                    if (ring < 4) {
                        this.rolloverColor = this.rolloverColor.darker();
                        break;
                    }
                    this.rolloverColor = this.rolloverColor.brighter().brighter();
                    break;
                }
            }
        }
        if (this.rolloverPath != oldPath) {
            this.repaint();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e2) {
        Object src = e2.getSource();
        this.ctrlKeyDown = e2.isControlDown();
        int notches = e2.getWheelRotation();
        if (src == this.brightnessSlider) {
            this.brightnessSlider.setValue(this.brightnessSlider.getValue() - 2 * notches);
        } else if (src == this.saturationSlider) {
            this.saturationSlider.setValue(this.saturationSlider.getValue() + 2 * notches);
        } else if (src == this.imagePicker) {
            this.setHue(this.getHue() + 2 * notches);
        }
        this.ctrlKeyDown = false;
    }

    @Override
    public void mouseDragged(MouseEvent e2) {
        this.ctrlKeyDown = e2.isControlDown();
    }

    @Override
    public void stateChanged(ChangeEvent e2) {
        Object source = e2.getSource();
        if (source == this.saturationSlider) {
            this.satEdit.setText(Integer.toString(this.saturationSlider.getValue()));
            this.resetColor();
        } else if (source == this.brightnessSlider) {
            this.brightEdit.setText(Integer.toString(this.brightnessSlider.getValue()));
            this.resetColor();
        }
        if (this.hasChooser) {
            this.getColorSelectionModel().setSelectedColor(new Color(this.chooserColor.getRed(), this.chooserColor.getGreen(), this.chooserColor.getBlue()));
        }
        this.ctrlKeyDown = false;
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return "Xoetrope Color Wheel";
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
    public Dimension getPreferredSize() {
        return new Dimension(255, 328);
    }

    @Override
    public void updateChooser() {
        if (this.hasChooser) {
            Color selected = this.getColorFromModel();
            if (selected == null) {
                this.setSelectedColors(new ModelColor[0]);
                this.setColor(null);
            } else {
                ModelColor selectedModelColor = new ModelColor(selected.getRed(), selected.getGreen(), selected.getBlue());
                this.setSelectedColors(new ModelColor[]{selectedModelColor});
                this.setColor(selected);
            }
        }
    }

    @Override
    public void installChooserPanel(JColorChooser enclosingChooser) {
        this.hasChooser = enclosingChooser != null;
        super.installChooserPanel(enclosingChooser);
        this.setDisplayScheme(true);
    }

    public static void setLabelBundle(ResourceBundle labelBundle) {
        ColorWheelPanel.labelBundle = labelBundle;
    }

    private static String getLabel(String labelName, String defaultValue) {
        if (labelBundle == null) {
            return defaultValue;
        }
        try {
            return labelBundle.getString(labelName);
        }
        catch (MissingResourceException mre) {
            return defaultValue;
        }
    }

    private void showSystemColorList(Point p2) {
        JPopupMenu popupMenu = new JPopupMenu();
        String[] systemColors = new String[]{"activeCaption", "desktop", "activeCaptionText", "activeCaptionBorder", "inactiveCaption", "inactiveCaptionText", "inactiveCaptionBorder", "window", "windowBorder", "windowText", "menu", "menuText", "text", "textText", "textHighlight", "textHighlightText", "textInactiveText", "control", "controlText", "controlHighlight", "controlLtHighlight", "controlShadow", "controlDkShadow", "scrollbar", "info", "infoText", "white", "lightGray", "gray", "darkGray", "black", "red", "pink", "orange", "yellow", "green", "magenta", "cyan", "blue"};
        for (int i2 = 0; i2 < systemColors.length; ++i2) {
            if (systemColors[i2].equals("white")) {
                popupMenu.addSeparator();
                continue;
            }
            JMenuItem mi = new JMenuItem(systemColors[i2]);
            mi.addActionListener(this);
            BufferedImage image = new BufferedImage(8, 8, 1);
            Graphics g2 = image.getGraphics();
            g2.setColor(this.getSystemColor(systemColors[i2]));
            g2.fillRect(0, 0, 8, 8);
            g2.setColor(SystemColor.windowBorder);
            g2.drawRect(0, 0, 7, 7);
            g2.dispose();
            ImageIcon icon = new ImageIcon(image);
            mi.setIcon(icon);
            popupMenu.add(mi);
        }
        popupMenu.show(this, p2.x, p2.y);
    }

    public Color getSystemColor(String temp) {
        Color clr = null;
        if (temp.equals("activeCaption")) {
            clr = SystemColor.activeCaption;
        } else if (temp.equals("desktop")) {
            clr = SystemColor.desktop;
        } else if (temp.equals("activeCaptionText")) {
            clr = SystemColor.activeCaptionText;
        } else if (temp.equals("activeCaptionBorder")) {
            clr = SystemColor.activeCaptionBorder;
        } else if (temp.equals("inactiveCaption")) {
            clr = SystemColor.inactiveCaption;
        } else if (temp.equals("inactiveCaptionText")) {
            clr = SystemColor.inactiveCaptionText;
        } else if (temp.equals("inactiveCaptionBorder")) {
            clr = SystemColor.inactiveCaptionBorder;
        } else if (temp.equals("window")) {
            clr = SystemColor.window;
        } else if (temp.equals("windowBorder")) {
            clr = SystemColor.windowBorder;
        } else if (temp.equals("windowText")) {
            clr = SystemColor.windowText;
        } else if (temp.equals("menu")) {
            clr = SystemColor.menu;
        } else if (temp.equals("menuText")) {
            clr = SystemColor.menuText;
        } else if (temp.equals("text")) {
            clr = SystemColor.text;
        } else if (temp.equals("textText")) {
            clr = SystemColor.textText;
        } else if (temp.equals("textHighlight")) {
            clr = SystemColor.textHighlight;
        } else if (temp.equals("textHighlightText")) {
            clr = SystemColor.textHighlightText;
        } else if (temp.equals("textInactiveText")) {
            clr = SystemColor.textInactiveText;
        } else if (temp.equals("control")) {
            clr = SystemColor.control;
        } else if (temp.equals("controlText")) {
            clr = SystemColor.controlText;
        } else if (temp.equals("controlHighlight")) {
            clr = SystemColor.controlHighlight;
        } else if (temp.equals("controlLtHighlight")) {
            clr = SystemColor.controlLtHighlight;
        } else if (temp.equals("controlShadow")) {
            clr = SystemColor.controlShadow;
        } else if (temp.equals("controlDkShadow")) {
            clr = SystemColor.controlDkShadow;
        } else if (temp.equals("scrollbar")) {
            clr = SystemColor.scrollbar;
        } else if (temp.equals("info")) {
            clr = SystemColor.info;
        } else if (temp.equals("infoText")) {
            clr = SystemColor.infoText;
        } else if (temp.equals("white")) {
            clr = Color.white;
        } else if (temp.equals("lightGray")) {
            clr = Color.lightGray;
        } else if (temp.equals("gray")) {
            clr = Color.gray;
        } else if (temp.equals("darkGray")) {
            clr = Color.darkGray;
        } else if (temp.equals("black")) {
            clr = Color.black;
        } else if (temp.equals("red")) {
            clr = Color.red;
        } else if (temp.equals("pink")) {
            clr = Color.pink;
        } else if (temp.equals("orange")) {
            clr = Color.orange;
        } else if (temp.equals("yellow")) {
            clr = Color.yellow;
        } else if (temp.equals("green")) {
            clr = Color.green;
        } else if (temp.equals("magenta")) {
            clr = Color.magenta;
        } else if (temp.equals("cyan")) {
            clr = Color.green;
        } else if (temp.equals("blue")) {
            clr = Color.blue;
        }
        return clr;
    }

    private boolean shouldAdjustWheel() {
        if (this.adjustWheel == 2) {
            return false;
        }
        if (this.adjustWheel == 1) {
            return true;
        }
        return this.ctrlKeyDown;
    }

    public int getAdjustWheel() {
        return this.adjustWheel;
    }

    public void setAdjustWheel(int state) {
        this.adjustWheel = state;
    }

    public boolean getRollover() {
        return this.adjustRollover;
    }

    public void setRollover(boolean state) {
        this.adjustRollover = state;
    }

    public void resetColorWheel() {
        this.brightnessMultipler = 1.0;
        this.saturationMultipler = 1.0;
        this.resetColor();
    }

    static /* synthetic */ GeneralPath[] access$302(ColorWheelPanel x0, GeneralPath[] x1) {
        x0.paths = x1;
        return x1;
    }

    private class ColorDocumentListener
    implements DocumentListener {
        private JTextField originator;
        private static final String MARKER = "Xoetrope.XUI.ColorWheel.DocumentEvent";

        public ColorDocumentListener(JTextField originator) {
            this.originator = originator;
        }

        @Override
        public void insertUpdate(DocumentEvent evt) {
            this.synchronize(evt);
        }

        @Override
        public void removeUpdate(DocumentEvent evt) {
            this.synchronize(evt);
        }

        @Override
        public void changedUpdate(DocumentEvent evt) {
            this.synchronize(evt);
        }

        public void synchronize(DocumentEvent evt) {
            boolean _hasAllValues = true;
            if (ColorWheelPanel.this.hueEdit.getText().length() == 0) {
                _hasAllValues = false;
            }
            if (ColorWheelPanel.this.brightEdit.getText().length() == 0) {
                _hasAllValues = false;
            }
            if (ColorWheelPanel.this.satEdit.getText().length() == 0) {
                _hasAllValues = false;
            }
            final boolean hasAllValues = _hasAllValues;
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    ColorWheelPanel.this.useWebColors.setEnabled(hasAllValues);
                    ColorWheelPanel.this.decimalRGB.setEnabled(hasAllValues);
                }
            });
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (hasAllValues && ColorDocumentListener.this.originator.hasFocus()) {
                        if (Boolean.TRUE.equals(ColorDocumentListener.this.originator.getClientProperty(ColorDocumentListener.MARKER))) {
                            ColorDocumentListener.this.originator.putClientProperty(ColorDocumentListener.MARKER, null);
                        } else {
                            ColorDocumentListener.this.originator.putClientProperty(ColorDocumentListener.MARKER, Boolean.TRUE);
                            ColorWheelPanel.this.resetColor();
                        }
                    }
                    if (ColorWheelPanel.this.displayScheme) {
                        ColorWheelPanel.this.imagePicker.repaint();
                    }
                }
            });
        }
    }

    class ColorWheel
    extends JLabel {
        @Override
        public void paintComponent(Graphics g2) {
            super.paintComponent(g2);
            this.paintWheel((Graphics2D)g2);
            if (ColorWheelPanel.this.displayScheme) {
                int selIdx = ColorWheelPanel.this.colorScheme;
                int numColours = Math.min(selIdx + 1, 4);
                for (int i2 = 0; i2 < numColours; ++i2) {
                    double r2 = (ColorWheelPanel.this.selectedIttenColours[i2].H - 90.0) / 360.0 * 2.0 * Math.PI;
                    double x2 = Math.round(111.0 + 110.0 * Math.cos(r2));
                    double y2 = Math.round(111.0 + 110.0 * Math.sin(r2));
                    g2.setColor(Color.gray);
                    g2.fillOval((int)x2, (int)y2, 4, 4);
                    g2.setColor(Color.darkGray);
                    g2.drawOval((int)x2, (int)y2, 4, 4);
                }
            }
        }

        public void paintWheel(Graphics2D g2d) {
            int i2;
            if (ColorWheelPanel.this.paths == null) {
                ColorWheelPanel.access$302(ColorWheelPanel.this, new GeneralPath[168]);
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            ModelColor[][] baseColors = ModelColor.getBaseColors();
            int idx = 0;
            double width = this.getWidth() - 1;
            double center = width / 2.0;
            ColorWheelPanel.this.ringThickness = width / 18.0;
            double fontHeight = ColorWheelPanel.this.ringThickness / 2.0;
            double inset = ColorWheelPanel.this.ringThickness / 2.0;
            g2d.setColor(new Color(228, 228, 228));
            Arc2D.Double innerArc = new Arc2D.Double(inset, inset, width - inset - inset, width - inset - inset, 0.0, 360.0, 0);
            Arc2D.Double outerArc = new Arc2D.Double(0.0, 0.0, width, width, 360.0, -360.0, 0);
            GeneralPath gp = new GeneralPath();
            gp.append(innerArc, true);
            gp.append(outerArc, true);
            gp.closePath();
            g2d.fill(gp);
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(0.3f));
            g2d.draw(outerArc);
            g2d.setColor(new Color(255, 253, 220));
            innerArc = new Arc2D.Double(center - ColorWheelPanel.this.ringThickness / 2.0, center - ColorWheelPanel.this.ringThickness / 2.0, ColorWheelPanel.this.ringThickness, ColorWheelPanel.this.ringThickness, -30.0, 180.0, 0);
            outerArc = new Arc2D.Double(center - ColorWheelPanel.this.ringThickness, center - ColorWheelPanel.this.ringThickness, ColorWheelPanel.this.ringThickness * 2.0, ColorWheelPanel.this.ringThickness * 2.0, 150.0, -180.0, 0);
            gp = new GeneralPath();
            gp.append(innerArc, true);
            gp.append(outerArc, true);
            gp.closePath();
            g2d.fill(gp);
            g2d.setColor(new Color(202, 230, 252));
            innerArc = new Arc2D.Double(center - ColorWheelPanel.this.ringThickness / 2.0, center - ColorWheelPanel.this.ringThickness / 2.0, ColorWheelPanel.this.ringThickness, ColorWheelPanel.this.ringThickness, 150.0, 180.0, 0);
            outerArc = new Arc2D.Double(center - ColorWheelPanel.this.ringThickness, center - ColorWheelPanel.this.ringThickness, ColorWheelPanel.this.ringThickness * 2.0, ColorWheelPanel.this.ringThickness * 2.0, 330.0, -180.0, 0);
            gp = new GeneralPath();
            gp.append(innerArc, true);
            gp.append(outerArc, true);
            gp.closePath();
            g2d.fill(gp);
            g2d.setColor(Color.black);
            AffineTransform identityTransform = g2d.getTransform();
            AffineTransform at = (AffineTransform)identityTransform.clone();
            at.translate(center, center);
            at.rotate(0.5235987755982988);
            g2d.setTransform(at);
            gp = new GeneralPath();
            gp.moveTo((float)(-ColorWheelPanel.this.ringThickness / 2.0), 0.0f);
            gp.lineTo((float)(-ColorWheelPanel.this.ringThickness * 1.2), 0.0f);
            gp.lineTo((float)(-ColorWheelPanel.this.ringThickness * 1.2), (float)(-fontHeight));
            gp.lineTo((float)(-ColorWheelPanel.this.ringThickness * 1.4), (float)(-fontHeight + ColorWheelPanel.this.ringThickness * 0.2));
            gp.moveTo((float)(-ColorWheelPanel.this.ringThickness * 1.2), (float)(-fontHeight));
            gp.lineTo((float)(-ColorWheelPanel.this.ringThickness), (float)(-fontHeight + ColorWheelPanel.this.ringThickness * 0.2));
            g2d.draw(gp);
            gp = new GeneralPath();
            gp.moveTo((float)(ColorWheelPanel.this.ringThickness / 2.0), 0.0f);
            gp.lineTo((float)(ColorWheelPanel.this.ringThickness * 1.2), 0.0f);
            gp.lineTo((float)(ColorWheelPanel.this.ringThickness * 1.2), (float)fontHeight);
            gp.lineTo((float)(ColorWheelPanel.this.ringThickness * 1.4), (float)(fontHeight - ColorWheelPanel.this.ringThickness * 0.2));
            gp.moveTo((float)(ColorWheelPanel.this.ringThickness * 1.2), (float)fontHeight);
            gp.lineTo((float)ColorWheelPanel.this.ringThickness, (float)(fontHeight - ColorWheelPanel.this.ringThickness * 0.2));
            g2d.draw(gp);
            double r1 = center;
            double r2 = r1 - fontHeight;
            double r3 = r1 - ColorWheelPanel.this.ringThickness / 2.3;
            double r4 = r1 + ColorWheelPanel.this.ringThickness / 2.7;
            double inc = 0.2617993877991494;
            g2d.setColor(Color.black);
            for (i2 = 0; i2 < 24; ++i2) {
                double angle = (double)i2 * inc;
                double sin = Math.sin(angle);
                double cos = Math.cos(angle);
                gp = new GeneralPath();
                if (width > 200.0 && i2 % 2 == 0) {
                    AttributedString as = new AttributedString("" + (i2 * 15 + 90) % 360 + "\u00ef\u00bf\u00bd");
                    as.addAttribute(TextAttribute.FAMILY, ColorWheelPanel.this.fontFamily);
                    as.addAttribute(TextAttribute.SIZE, Float.valueOf((float)fontHeight));
                    as.addAttribute(TextAttribute.FOREGROUND, Color.black);
                    at = (AffineTransform)identityTransform.clone();
                    at.translate(center + fontHeight / 5.0 + r3 * cos, center + r3 * sin);
                    at.rotate(angle + 1.5707963267948966);
                    g2d.setTransform(at);
                    g2d.drawString(as.getIterator(), 0.0f, 0.0f);
                    continue;
                }
                g2d.setTransform(identityTransform);
                gp.moveTo((float)(center + r1 * cos), (float)(center + r1 * sin));
                gp.lineTo((float)(center + r2 * cos), (float)(center + r2 * sin));
                g2d.draw(gp);
            }
            for (i2 = 0; i2 < 7; ++i2) {
                double outerX = inset + (double)(7 - (i2 + 1)) * ColorWheelPanel.this.ringThickness;
                double outerW = width - outerX - outerX;
                double innerX = outerX + ColorWheelPanel.this.ringThickness;
                double innerW = outerW - 2.0 * ColorWheelPanel.this.ringThickness;
                for (int j2 = 0; j2 < 24; ++j2) {
                    ModelColor modelColor = baseColors[j2][i2];
                    modelColor = new ModelColor(modelColor.H, ColorWheelPanel.this.saturationMultipler * modelColor.S, ColorWheelPanel.this.brightnessMultipler * modelColor.V);
                    Color c2 = new Color(modelColor.getRed(), modelColor.getGreen(), modelColor.getBlue());
                    g2d.setColor(c2);
                    double startAngle = (82.5 - (double)j2 * 15.0 + 360.0) % 360.0;
                    double delta1 = j2 % 2 == 0 ? arcDelta[i2] : -arcDelta[i2];
                    double delta2 = j2 % 2 == 0 ? arcDelta[i2 + 1] : -arcDelta[i2 + 1];
                    innerArc = new Arc2D.Double(innerX, innerX, innerW, innerW, startAngle + delta1, 15.0 - 2.0 * delta1, 0);
                    outerArc = new Arc2D.Double(outerX, outerX, outerW, outerW, startAngle + 15.0 - delta2, -15.0 + 2.0 * delta2, 0);
                    gp = new GeneralPath();
                    gp.append(innerArc, true);
                    gp.append(outerArc, true);
                    gp.closePath();
                    g2d.fill(gp);
                    ((ColorWheelPanel)ColorWheelPanel.this).paths[idx++] = gp;
                }
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (width > 200.0) {
                double angle = -1.0471975511965979;
                double angle2 = angle - 0.055;
                double sin = Math.sin(angle2);
                double cos = Math.cos(angle2);
                AttributedString as = new AttributedString(ColorWheelPanel.getLabel("Xoetrope.warm", "WARM"));
                as.addAttribute(TextAttribute.FAMILY, ColorWheelPanel.this.fontFamily);
                as.addAttribute(TextAttribute.SIZE, Float.valueOf((float)(ColorWheelPanel.this.ringThickness / 1.5)));
                as.addAttribute(TextAttribute.FOREGROUND, new Color(92, 0, 0));
                at = (AffineTransform)identityTransform.clone();
                at.translate(center + fontHeight / 5.0 + r4 * cos, center + r4 * sin);
                at.rotate(angle + 1.5707963267948966 + 0.05);
                g2d.setTransform(at);
                g2d.drawString(as.getIterator(), 0.0f, 0.0f);
                sin = Math.sin(angle += Math.PI);
                cos = Math.cos(angle);
                as = new AttributedString(ColorWheelPanel.getLabel("Xoetrope.cold", "COLD"));
                as.addAttribute(TextAttribute.FAMILY, ColorWheelPanel.this.fontFamily);
                as.addAttribute(TextAttribute.SIZE, Float.valueOf((float)(ColorWheelPanel.this.ringThickness / 1.5)));
                as.addAttribute(TextAttribute.FOREGROUND, new Color(0, 0, 92));
                at = (AffineTransform)identityTransform.clone();
                at.translate(center + fontHeight / 5.0 + r4 * cos, center + r4 * sin);
                at.rotate(angle + 1.5707963267948966 + 0.05);
                g2d.setTransform(at);
                g2d.drawString(as.getIterator(), 0.0f, 0.0f);
                angle = Math.PI;
                sin = Math.sin(angle);
                cos = Math.cos(angle);
                as = new AttributedString(ColorWheelPanel.getLabel("Xoetrope.saturation", "Saturation"));
                as.addAttribute(TextAttribute.FAMILY, ColorWheelPanel.this.fontFamily);
                as.addAttribute(TextAttribute.SIZE, Float.valueOf((float)(ColorWheelPanel.this.ringThickness / 1.3)));
                as.addAttribute(TextAttribute.FOREGROUND, UIManager.getColor("Label.foreground"));
                at = (AffineTransform)identityTransform.clone();
                at.translate(width - fontHeight, width);
                at.rotate(angle + 1.5707963267948966);
                g2d.setTransform(at);
                g2d.drawString(as.getIterator(), 0.0f, 0.0f);
                String brightnessText = ColorWheelPanel.getLabel("Xoetrope.brightness", "Brightness");
                as = new AttributedString(brightnessText);
                as.addAttribute(TextAttribute.FAMILY, ColorWheelPanel.this.fontFamily);
                as.addAttribute(TextAttribute.SIZE, Float.valueOf((float)(ColorWheelPanel.this.ringThickness / 1.3)));
                as.addAttribute(TextAttribute.FOREGROUND, UIManager.getColor("Label.foreground"));
                at = (AffineTransform)identityTransform.clone();
                at.translate(width - fontHeight, ColorWheelPanel.this.ringThickness * (double)brightnessText.length() / 2.3);
                at.rotate(angle + 1.5707963267948966);
                g2d.setTransform(at);
                g2d.drawString(as.getIterator(), 0.0f, 0.0f);
            }
            g2d.setTransform(identityTransform);
            if (ColorWheelPanel.this.showRollovers) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (ColorWheelPanel.this.rolloverPath != null) {
                    g2d.setColor(ColorWheelPanel.this.rolloverColor);
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.draw(ColorWheelPanel.this.rolloverPath);
                }
                if (ColorWheelPanel.this.selectedPath != null) {
                    g2d.setColor(ColorWheelPanel.this.selectedColor);
                    g2d.setStroke(new BasicStroke(1.0f));
                    g2d.draw(ColorWheelPanel.this.selectedPath);
                }
            }
        }
    }
}

