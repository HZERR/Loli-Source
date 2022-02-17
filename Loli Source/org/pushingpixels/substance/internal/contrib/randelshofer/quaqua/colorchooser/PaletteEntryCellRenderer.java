/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.UIManager;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.PaletteEntry;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.PaletteListModel;

public class PaletteEntryCellRenderer
extends DefaultListCellRenderer {
    private Color closestMarker1 = new Color(0xE6E6E6);
    private Color closestMarker2 = new Color(0xEDEDED);
    private Color closestMarker3 = new Color(0xF0F0F0);
    private ColorIcon icon = new ColorIcon();
    private boolean isClosestColor;

    public PaletteEntryCellRenderer() {
        this.setIcon(this.icon);
        this.setOpaque(false);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        this.setComponentOrientation(list.getComponentOrientation());
        if (isSelected) {
            this.setBackground(UIManager.getColor("ColorChooser.listSelectionBackground"));
            this.setForeground(UIManager.getColor("ColorChooser.listSelectionForeground"));
            this.isClosestColor = false;
        } else {
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
            PaletteListModel model = (PaletteListModel)list.getModel();
            this.isClosestColor = model.getClosestIndex() == index;
        }
        this.setEnabled(list.isEnabled());
        this.setFont(list.getFont());
        PaletteEntry entry = (PaletteEntry)value;
        this.icon.setColor(entry.getColor());
        this.setText(entry.getName());
        return this;
    }

    @Override
    public void paintComponent(Graphics g2) {
        int width = this.getWidth();
        int height = this.getHeight();
        g2.setColor(this.getBackground());
        g2.fillRect(0, 0, width, height);
        if (this.isClosestColor) {
            g2.setColor(this.closestMarker1);
            g2.fillRect(0, 0, width, 2);
            g2.fillRect(0, height - 2, width, 2);
            g2.setColor(this.closestMarker2);
            g2.fillRect(0, 2, width, 1);
            g2.fillRect(0, height - 3, width, 1);
            g2.setColor(this.closestMarker3);
            g2.fillRect(0, 3, width, 1);
            g2.fillRect(0, height - 4, width, 1);
        }
        super.paintComponent(g2);
    }

    static class ColorIcon
    implements Icon {
        private Color color = Color.black;

        ColorIcon() {
        }

        public void setColor(Color c2) {
            this.color = c2;
        }

        public Color getColor() {
            return this.color;
        }

        @Override
        public int getIconHeight() {
            return 15;
        }

        @Override
        public int getIconWidth() {
            return 25;
        }

        @Override
        public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
            g2.setColor(this.getColor());
            g2.fillRect(x2 + 1, y2 + 1, this.getIconWidth() - 2, this.getIconHeight() - 2);
            g2.setColor(this.getColor().darker());
            g2.drawRect(x2, y2, this.getIconWidth() - 1, this.getIconHeight() - 1);
        }
    }
}

