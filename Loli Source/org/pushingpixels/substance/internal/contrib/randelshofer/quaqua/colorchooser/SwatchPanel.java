/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class SwatchPanel
extends JPanel {
    protected Color[] colors;
    protected Dimension swatchSize = new Dimension();
    protected Dimension defaultSwatchSize;
    protected Dimension numSwatches;
    protected Dimension gap;
    private static final Color gridColor = new Color(0xAAAAAA);

    public SwatchPanel() {
        this.initComponents();
        this.initValues();
        this.initColors();
        this.setToolTipText("");
        this.setOpaque(false);
        this.setRequestFocusEnabled(false);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }

    @Override
    public boolean isFocusTraversable() {
        return false;
    }

    protected void initValues() {
        this.defaultSwatchSize = UIManager.getDimension("ColorChooser.swatchesSwatchSize");
        this.swatchSize.width = this.defaultSwatchSize.width;
        this.swatchSize.height = this.defaultSwatchSize.height;
        this.gap = new Dimension(1, 1);
    }

    @Override
    public void setBounds(int x2, int y2, int width, int height) {
        super.setBounds(x2, y2, width, height);
        this.swatchSize.width = width > this.getPreferredSize().width ? (width - this.numSwatches.width * this.gap.width) / this.numSwatches.width : this.defaultSwatchSize.width;
        this.swatchSize.height = height > this.getPreferredSize().height ? (height - this.numSwatches.height * this.gap.height) / this.numSwatches.height : this.defaultSwatchSize.height;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public void setNumSwatches(int rows, int columns) {
        this.numSwatches = new Dimension(rows, columns);
    }

    @Override
    public void paintComponent(Graphics g2) {
        Dimension preferredSize = this.getSwatchesSize();
        int xoffset = (this.getWidth() - preferredSize.width) / 2;
        int yoffset = 0;
        for (int row = 0; row < this.numSwatches.height; ++row) {
            for (int column = 0; column < this.numSwatches.width; ++column) {
                Color cellColor = this.getColorForCell(column, row);
                g2.setColor(cellColor);
                int x2 = xoffset + column * (this.swatchSize.width + this.gap.width) + 1;
                int y2 = yoffset + row * (this.swatchSize.height + this.gap.height) + 1;
                g2.fillRect(x2, y2, this.swatchSize.width, this.swatchSize.height);
                g2.setColor(cellColor.darker());
                g2.fillRect(x2 - 1, y2 - 1, this.swatchSize.width + 1, 1);
                g2.fillRect(x2 - 1, y2, 1, this.swatchSize.height);
            }
        }
    }

    public Dimension getSwatchesSize() {
        int x2 = this.numSwatches.width * (this.swatchSize.width + this.gap.width);
        int y2 = this.numSwatches.height * (this.swatchSize.height + this.gap.height);
        return new Dimension(x2, y2);
    }

    @Override
    public Dimension getPreferredSize() {
        int x2 = this.numSwatches.width * (this.defaultSwatchSize.width + this.gap.width);
        int y2 = this.numSwatches.height * (this.defaultSwatchSize.height + this.gap.height);
        return new Dimension(x2, y2);
    }

    protected void initColors() {
    }

    @Override
    public String getToolTipText(MouseEvent e2) {
        Color color = this.getColorForLocation(e2.getX(), e2.getY());
        return color == null ? null : color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
    }

    public Color getColorForLocation(int x2, int y2) {
        Dimension preferredSize = this.getSwatchesSize();
        int column = !this.getComponentOrientation().isLeftToRight() ? this.numSwatches.width - x2 / (this.swatchSize.width + this.gap.width) - 1 : (x2 -= (this.getWidth() - preferredSize.width) / 2) / (this.swatchSize.width + this.gap.width);
        int row = y2 / (this.swatchSize.height + this.gap.height);
        return this.getColorForCell(column, row);
    }

    private Color getColorForCell(int column, int row) {
        int index = row * this.numSwatches.width + column;
        return index < this.colors.length ? this.colors[index] : null;
    }
}

