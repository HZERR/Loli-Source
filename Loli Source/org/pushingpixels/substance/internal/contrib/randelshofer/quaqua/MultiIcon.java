/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util.Images;

public abstract class MultiIcon
implements Icon {
    protected Icon[] icons;
    private Image tiledImage;
    private int tileCount;
    private boolean isTiledHorizontaly;

    public MultiIcon(Icon[] icons) {
        this.icons = icons;
        this.generateMissingIcons();
    }

    public MultiIcon(Image[] images) {
        this.icons = new Icon[images.length];
        int n2 = this.icons.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (images[i2] == null) continue;
            this.icons[i2] = new ImageIcon(images[i2]);
        }
        this.generateMissingIcons();
    }

    public MultiIcon(Image tiledImage, int tileCount, boolean isTiledHorizontaly) {
        this.tiledImage = tiledImage;
        this.tileCount = tileCount;
        this.isTiledHorizontaly = isTiledHorizontaly;
    }

    @Override
    public int getIconHeight() {
        this.generateIconsFromTiledImage();
        return this.icons[0].getIconHeight();
    }

    @Override
    public int getIconWidth() {
        this.generateIconsFromTiledImage();
        return this.icons[0].getIconWidth();
    }

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        this.generateIconsFromTiledImage();
        Icon icon = this.getIcon(c2);
        if (icon != null) {
            icon.paintIcon(c2, g2, x2, y2);
        }
    }

    private void generateIconsFromTiledImage() {
        if (this.icons == null) {
            this.icons = new Icon[this.tileCount];
            BufferedImage[] images = Images.split(this.tiledImage, this.tileCount, this.isTiledHorizontaly);
            int n2 = Math.min(images.length, this.icons.length);
            for (int i2 = 0; i2 < n2; ++i2) {
                if (images[i2] == null) continue;
                this.icons[i2] = new ImageIcon(images[i2]);
            }
            this.generateMissingIcons();
            this.tiledImage = null;
        }
    }

    protected abstract Icon getIcon(Component var1);

    protected abstract void generateMissingIcons();
}

