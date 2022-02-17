/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaUtilities;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.DefaultPalettes;

public class Crayons
extends JPanel {
    private Image crayonsImage;
    private static final int[] crayonXPoints = new int[]{10, 12, 20, 22, 22, 0, 0, 2};
    private static final int[] crayonYPoints = new int[]{0, 0, 21, 21, 104, 104, 21, 21};
    private Color color = Color.white;
    private Crayon selectedCrayon = null;
    private MouseHandler mouseHandler;
    private Crayon[] crayons;

    public Crayons() {
        this.initComponents();
        this.setForeground(new Color(0x808080));
        this.setPreferredSize(new Dimension(195, 208));
        this.setFont(UIManager.getFont("ColorChooser.crayonsFont"));
        this.crayonsImage = this.createCrayonsImage();
        this.crayons = this.createCrayons();
        this.mouseHandler = new MouseHandler();
        this.addMouseListener(this.mouseHandler);
    }

    protected Image createCrayonsImage() {
        return (Image)UIManager.get("ColorChooser.crayonsImage");
    }

    protected Crayon[] createCrayons() {
        Color[] colors = DefaultPalettes.CRAYONS;
        this.crayons = new Crayon[colors.length];
        for (int i2 = 0; i2 < colors.length; ++i2) {
            this.crayons[i2] = new Crayon(colors[i2], UIManager.getString("ColorChooser.crayon." + Integer.toHexString(0xFF000000 | colors[i2].getRGB()).substring(2)), new Polygon((int[])crayonXPoints.clone(), (int[])crayonYPoints.clone(), crayonXPoints.length));
            this.crayons[i2].shape.translate(i2 % 8 * 22 + 4 + i2 / 8 % 2 * 11, i2 / 8 * 20 + 23);
        }
        return this.crayons;
    }

    public void setColor(Color newValue) {
        Color oldValue = this.color;
        this.color = newValue;
        Crayon newSelectedCrayon = null;
        int newRGB = newValue.getRGB() & 0xFFFFFF;
        for (int i2 = 0; i2 < this.crayons.length; ++i2) {
            if ((this.crayons[i2].color.getRGB() & 0xFFFFFF) != newRGB) continue;
            newSelectedCrayon = this.crayons[i2];
        }
        if (newSelectedCrayon != this.selectedCrayon) {
            this.selectedCrayon = newSelectedCrayon;
            this.repaint();
        }
        this.firePropertyChange("Color", oldValue, newValue);
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D g2 = (Graphics2D)gr;
        Object oldHints = QuaquaUtilities.beginGraphics(g2);
        g2.drawImage(this.crayonsImage, 0, 0, this);
        if (this.selectedCrayon != null) {
            g2.setColor(this.getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int nameWidth = fm.stringWidth(this.selectedCrayon.name);
            g2.drawString(this.selectedCrayon.name, (this.crayonsImage.getWidth(this) - nameWidth) / 2, fm.getAscent() + 1);
        }
        QuaquaUtilities.endGraphics(g2, oldHints);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }

    private class MouseHandler
    extends MouseAdapter {
        private MouseHandler() {
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            int x2 = evt.getX();
            int y2 = evt.getY();
            if (x2 > 0 && x2 < Crayons.this.crayonsImage.getWidth(Crayons.this) && y2 > 0 && y2 < Crayons.this.crayonsImage.getHeight(Crayons.this)) {
                for (int i2 = Crayons.this.crayons.length - 1; i2 >= 0; --i2) {
                    if (!((Crayons)Crayons.this).crayons[i2].shape.contains(x2, y2)) continue;
                    Crayons.this.setColor(((Crayons)Crayons.this).crayons[i2].color);
                    break;
                }
            }
        }
    }

    private class Crayon {
        Polygon shape;
        Color color;
        String name;

        public Crayon(Color color, String name, Polygon shape) {
            this.color = color;
            this.name = name;
            this.shape = shape;
        }
    }
}

