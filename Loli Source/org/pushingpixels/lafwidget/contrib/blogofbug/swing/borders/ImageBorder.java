/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.borders;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.border.AbstractBorder;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.borders.AbstractImageBorder;

public class ImageBorder
extends AbstractBorder {
    protected AbstractImageBorder borderRenderer;
    boolean paintBorder = true;

    public ImageBorder(BufferedImage borderImage, Insets imageInsets) {
        this.borderRenderer = new AbstractImageBorder(borderImage, imageInsets);
    }

    public ImageBorder(URL imageURL, Insets imageInsets) {
        this.borderRenderer = new AbstractImageBorder(imageURL, imageInsets);
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
        if (!this.paintBorder) {
            return;
        }
        this.borderRenderer.paintBorder(c2.getWidth(), c2.getHeight(), g2, x2, y2, width, height);
    }

    public void setPaintBorder(boolean paintBorder) {
        this.paintBorder = paintBorder;
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        return this.borderRenderer.getImageInsets();
    }

    @Override
    public Insets getBorderInsets(Component c2, Insets i2) {
        Insets imageInsets = this.borderRenderer.getImageInsets();
        i2.top = imageInsets.top;
        i2.bottom = imageInsets.bottom;
        i2.left = imageInsets.left;
        i2.right = imageInsets.right;
        return i2;
    }

    public void paintCenter(Graphics2D g2, Component c2) {
        if (this.paintBorder) {
            this.borderRenderer.paintCenter(g2, c2.getWidth(), c2.getHeight());
        }
    }
}

