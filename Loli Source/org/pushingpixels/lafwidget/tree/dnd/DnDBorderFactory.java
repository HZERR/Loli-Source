/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

class DnDBorderFactory {
    private Border dropAllowedBorder;
    private Border dropNotAllowedBorder;
    private Border offsetBorder;
    private Border emptyBorder;

    public DnDBorderFactory() {
        this.setDropAllowedBorder(new DropAllowedBorder());
        this.setDropNotAllowedBorder(new DropNotAllowedBorder());
        this.setOffsetBorder(new OffsetBorder());
        this.setEmptyBorder(BorderFactory.createEmptyBorder());
    }

    public Border getDropAllowedBorder() {
        return this.dropAllowedBorder;
    }

    public void setDropAllowedBorder(Border dropAllowedBorder) {
        this.dropAllowedBorder = dropAllowedBorder;
    }

    public Border getDropNotAllowedBorder() {
        return this.dropNotAllowedBorder;
    }

    public void setDropNotAllowedBorder(Border dropNotAllowedBorder) {
        this.dropNotAllowedBorder = dropNotAllowedBorder;
    }

    public Border getOffsetBorder() {
        return this.offsetBorder;
    }

    public void setOffsetBorder(Border offsetBorder) {
        this.offsetBorder = offsetBorder;
    }

    public Border getEmptyBorder() {
        return this.emptyBorder;
    }

    public void setEmptyBorder(Border anEmptyBorder) {
        this.emptyBorder = anEmptyBorder;
    }

    class DropNotAllowedBorder
    implements Border {
        private Insets insets = new Insets(0, 0, 0, 0);
        private ImageIcon plusIcon;

        public DropNotAllowedBorder() {
            URL iconURL = DnDBorderFactory.class.getResource("icons/drop-not-allowed.png");
            if (iconURL != null) {
                this.plusIcon = new ImageIcon(iconURL);
            }
        }

        @Override
        public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
            if (this.plusIcon != null) {
                this.plusIcon.paintIcon(c2, g2, x2, y2);
            }
        }

        @Override
        public Insets getBorderInsets(Component c2) {
            return this.insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    class OffsetBorder
    implements Border {
        private Insets insets = new Insets(5, 0, 0, 0);

        OffsetBorder() {
        }

        @Override
        public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        }

        @Override
        public Insets getBorderInsets(Component c2) {
            return this.insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    static class DropAllowedBorder
    implements Border {
        private static Insets insets = new Insets(0, 0, 3, 0);
        private ImageIcon plusIcon;

        public DropAllowedBorder() {
            URL iconURL = DropAllowedBorder.class.getResource("icons/drop-on-leaf.png");
            if (iconURL != null) {
                this.plusIcon = new ImageIcon(iconURL);
            }
        }

        @Override
        public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
            int yh = y2 + height - 1;
            if (this.plusIcon != null) {
                this.plusIcon.paintIcon(c2, g2, x2 + 8, yh - 8);
            }
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(x2 + 24, yh -= 4, x2 + 48, yh);
        }

        @Override
        public Insets getBorderInsets(Component c2) {
            return insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}

