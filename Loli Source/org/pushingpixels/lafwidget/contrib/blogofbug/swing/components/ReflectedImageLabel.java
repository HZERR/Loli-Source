/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.RichComponent;
import org.pushingpixels.lafwidget.contrib.blogofbug.utility.ImageUtilities;

public class ReflectedImageLabel
extends JLabel
implements RichComponent {
    private String text = "";
    private BufferedImage bufferedImage = null;
    private static final Font reference = new Font("Arial", 1, 14);
    private AlphaComposite alphaComposite = AlphaComposite.getInstance(3, 1.0f);

    public ReflectedImageLabel(Image image, int width, int height) {
        this.setRichImage(image);
    }

    public ReflectedImageLabel(Image image, String text, int width, int height) {
        this.setRichImage(image);
        this.setRichText(text);
    }

    public ReflectedImageLabel(Image image, String text) {
        this(image, text, image.getWidth(null), image.getHeight(null));
    }

    public ReflectedImageLabel(String imageURL) {
        try {
            this.setRichImage(new URL(imageURL));
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public ReflectedImageLabel(String imageURL, int width, int height) {
        try {
            this.setRichImage(new URL(imageURL));
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public ReflectedImageLabel(String imageURL, String text, int width, int height) {
        this(imageURL, width, height);
        this.text = text;
    }

    public void setLabel(String text) {
        this.text = text;
    }

    private void setupImage(String imageURL) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(imageURL));
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.setupImage(image);
    }

    protected void setupImage(Image image) {
        if (image == null) {
            return;
        }
        BufferedImage originalImage = ImageUtilities.createCompatibleImage(image.getWidth(null), (int)((double)image.getHeight(null) * 1.5));
        Graphics2D g2 = originalImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        BufferedImage reflection = ImageUtilities.createCompatibleImage(image.getWidth(null), image.getHeight(null));
        g2 = reflection.createGraphics();
        int drawHeight = image.getHeight(null);
        AffineTransform tranform = AffineTransform.getScaleInstance(1.0, -1.0);
        tranform.translate(0.0, -drawHeight);
        AffineTransform oldTransform = g2.getTransform();
        g2.setTransform(tranform);
        g2.drawImage(image, 0, 0, image.getWidth(null), drawHeight, 0, 0, image.getWidth(null), image.getHeight(null), null);
        g2.setTransform(oldTransform);
        GradientPaint painter = new GradientPaint(0.0f, 0.0f, new Color(0.0f, 0.0f, 0.0f, 0.5f), 0.0f, (float)drawHeight / 2.0f, new Color(0.0f, 0.0f, 0.0f, 1.0f));
        g2.setComposite(AlphaComposite.DstOut);
        g2.setPaint(painter);
        g2.fill(new Rectangle2D.Double(0.0, 0.0, image.getWidth(null), drawHeight));
        g2.dispose();
        g2 = originalImage.createGraphics();
        g2.drawImage((Image)reflection, 0, drawHeight, null);
        g2.dispose();
        this.bufferedImage = originalImage;
        this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    }

    @Override
    public void setAlpha(float alphaLevel) {
        this.alphaComposite = AlphaComposite.getInstance(3, alphaLevel);
    }

    public void setImage(Image image) {
        this.setupImage(image);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d2 = super.getPreferredSize();
        d2.height = (int)((double)d2.height * 1.5);
        return d2;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (this.getX() + this.getWidth() < 0 && this.getY() + this.getHeight() < 0) {
            return;
        }
        Graphics2D g2 = (Graphics2D)graphics;
        BufferedImage image = this.bufferedImage;
        int drawHeight = (int)((double)this.getHeight() / 1.5);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Composite oldAc = g2.getComposite();
        g2.setComposite(this.alphaComposite);
        g2.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), 0, 0, ((Image)image).getWidth(null), ((Image)image).getHeight(null), null);
        if (this.text != null && this.text.length() > 0) {
            Graphics2D g2d = (Graphics2D)graphics;
            Rectangle2D bounds = reference.getStringBounds(this.text, g2d.getFontRenderContext());
            double scaleFactor = (double)this.getWidth() / (double)((Image)image).getWidth(null);
            double scaleFactor2 = (double)this.getWidth() / bounds.getWidth();
            int fontSize = (int)Math.min(25.0 * scaleFactor, 14.0 * scaleFactor2);
            Font font = new Font("Arial", 1, fontSize);
            g2d.setFont(font);
            int dx = (this.getWidth() - (int)font.getStringBounds(this.text, g2d.getFontRenderContext()).getWidth()) / 2;
            int dy = drawHeight + 2 * (int)(bounds.getHeight() * scaleFactor);
            Color background = this.getBackground();
            int red = background.getRed();
            int green = background.getRed();
            int blue = background.getRed();
            graphics.setColor(new Color(red, green, blue, 96));
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(this.text, graphics);
            graphics.fillRoundRect(dx - (int)rect.getHeight() / 2, dy - g2d.getFontMetrics().getAscent(), (int)rect.getWidth() + (int)rect.getHeight(), fm.getAscent() + fm.getDescent(), (int)rect.getHeight(), (int)rect.getHeight());
            graphics.setColor(this.getForeground());
            graphics.drawString(this.text, dx, dy);
        }
        g2.setComposite(oldAc);
    }

    @Override
    public void setRichImage(URL image) {
        this.setRichImage(ImageUtilities.loadCompatibleImage(image.toString()));
    }

    @Override
    public void setRichImage(File image) {
        try {
            this.setRichImage(image.toURL());
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setRichImage(Image image) {
        this.setImage(image);
    }

    @Override
    public void prePaintImage() {
    }

    @Override
    public void setRichText(String text) {
        this.setLabel(text);
    }

    @Override
    public String getRichText() {
        return this.text;
    }
}

