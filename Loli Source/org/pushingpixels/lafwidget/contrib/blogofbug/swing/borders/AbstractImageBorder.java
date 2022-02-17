/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.borders;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.net.URL;
import org.pushingpixels.lafwidget.contrib.blogofbug.utility.ImageUtilities;

public class AbstractImageBorder {
    protected BufferedImage borderImage;
    protected Insets imageInsets;

    public AbstractImageBorder(URL imageURL, Insets imageInsets) {
        this.imageInsets = imageInsets;
        this.borderImage = ImageUtilities.loadCompatibleImage(imageURL.toString());
    }

    public AbstractImageBorder(BufferedImage borderImage, Insets imageInsets) {
        this.borderImage = borderImage;
        this.imageInsets = imageInsets;
    }

    public void paintBorder(int compWidth, int compHeight, Graphics g2, int x2, int y2, int width, int height) {
        Graphics2D g22 = (Graphics2D)g2;
        int imageWidth = this.borderImage.getWidth();
        int imageHeight = this.borderImage.getHeight();
        this.drawSlice(g22, 0, 0, this.imageInsets.left, this.imageInsets.top, 0, 0);
        this.drawSlice(g22, imageWidth - this.imageInsets.right, 0, this.imageInsets.right, this.imageInsets.bottom, compWidth - this.imageInsets.right, 0);
        this.drawSlice(g22, 0, imageHeight - this.imageInsets.bottom, this.imageInsets.left, this.imageInsets.bottom, 0, compHeight - this.imageInsets.bottom);
        this.drawSlice(g22, imageWidth - this.imageInsets.right, imageHeight - this.imageInsets.bottom, this.imageInsets.left, this.imageInsets.bottom, compWidth - this.imageInsets.right, compHeight - this.imageInsets.bottom);
        g22.drawImage(this.borderImage, 0, this.imageInsets.top, this.imageInsets.left, compHeight - this.imageInsets.bottom, 0, this.imageInsets.top, this.imageInsets.left, imageHeight - this.imageInsets.bottom, null);
        g22.drawImage(this.borderImage, compWidth - this.imageInsets.right, this.imageInsets.top + 6, compWidth, compHeight - this.imageInsets.bottom, imageWidth - this.imageInsets.right, this.imageInsets.top, imageWidth, imageHeight - this.imageInsets.bottom, null);
        g22.drawImage(this.borderImage, this.imageInsets.left, 0, compWidth - this.imageInsets.left, this.imageInsets.top, this.imageInsets.left, 0, imageWidth - this.imageInsets.right, this.imageInsets.top, null);
        g22.drawImage(this.borderImage, this.imageInsets.left, compHeight - this.imageInsets.bottom, compWidth - this.imageInsets.left, compHeight, this.imageInsets.left, imageHeight - this.imageInsets.bottom, imageWidth - this.imageInsets.right, imageHeight, null);
    }

    public void setInsets(Insets insets) {
        this.imageInsets = insets;
    }

    public Insets getImageInsets() {
        return (Insets)this.imageInsets.clone();
    }

    public void paintCenter(Graphics2D g2, int compWidth, int compHeight) {
        int imageWidth = this.borderImage.getWidth();
        int imageHeight = this.borderImage.getHeight();
        g2.drawImage(this.borderImage, this.imageInsets.left, this.imageInsets.top, compWidth - this.imageInsets.right, compHeight - this.imageInsets.bottom, this.imageInsets.left, this.imageInsets.top, imageWidth - this.imageInsets.right, imageHeight - this.imageInsets.bottom, null);
    }

    private void drawSlice(Graphics2D g2, int sliceX, int sliceY, int sliceWidth, int sliceHeight, int destX, int destY) {
        g2.drawImage(this.borderImage, destX, destY, destX + sliceWidth, destY + sliceHeight, sliceX, sliceY, sliceX + sliceWidth, sliceY + sliceHeight, null);
    }
}

