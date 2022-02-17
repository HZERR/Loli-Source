/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageLabel
extends JLabel {
    protected ImageIcon imageIcon = null;

    public ImageLabel(ImageIcon icon) {
        super(icon);
        this.imageIcon = icon;
        this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }

    public ImageLabel(ImageIcon icon, int width, int height) {
        this(icon);
        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Image image = this.imageIcon.getImage();
        ImageObserver observer = this.imageIcon.getImageObserver();
        ((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), 0, 0, image.getWidth(observer), image.getHeight(observer), observer);
    }
}

