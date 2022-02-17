/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageUtilities {
    public static BufferedImage createCompatibleImage(int width, int height) {
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return configuration.createCompatibleImage(width, height, 3);
    }

    public static BufferedImage loadCompatibleImage(String imageURL) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(imageURL));
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        if (image == null) {
            return null;
        }
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage originalImage = configuration.createCompatibleImage(((Image)image).getWidth(null), ((Image)image).getHeight(null), 3);
        Graphics2D g2 = originalImage.createGraphics();
        g2.drawImage((Image)image, 0, 0, null);
        g2.dispose();
        return originalImage;
    }

    public static BufferedImage scaledImage(BufferedImage image, int width, int height) {
        BufferedImage newImage = ImageUtilities.createCompatibleImage(width, height);
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(image, 0, 0, width, height, null);
        graphics.dispose();
        return newImage;
    }

    public static BufferedImage copyImage(BufferedImage image) {
        return ImageUtilities.scaledImage(image, image.getWidth(), image.getHeight());
    }

    public static BufferedImage renderTextToImage(Font font, Color textColor, String text, int width) {
        Hashtable<TextAttribute, Font> map = new Hashtable<TextAttribute, Font>();
        map.put(TextAttribute.FONT, font);
        AttributedString attributedString = new AttributedString(text, map);
        AttributedCharacterIterator paragraph = attributedString.getIterator();
        FontRenderContext frc = new FontRenderContext(null, false, false);
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
        float drawPosY = 0.0f;
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }
        BufferedImage image = ImageUtilities.createCompatibleImage(width, (int)drawPosY);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawPosY = 0.0f;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);
            float drawPosX = layout.isLeftToRight() ? 0.0f : (float)width - layout.getAdvance();
            layout.draw(graphics, drawPosX, drawPosY += layout.getAscent());
            drawPosY += layout.getDescent() + layout.getLeading();
        }
        graphics.dispose();
        return image;
    }

    public static BufferedImage renderTextToImage(Font font, Color textColor, String[] text, int width) {
        LinkedList<BufferedImage> images = new LinkedList<BufferedImage>();
        int totalHeight = 0;
        for (String paragraph : text) {
            BufferedImage paraImage = ImageUtilities.renderTextToImage(font, textColor, paragraph, width);
            totalHeight += paraImage.getHeight();
            images.add(paraImage);
        }
        BufferedImage image = ImageUtilities.createCompatibleImage(width, totalHeight);
        Graphics2D graphics = image.createGraphics();
        int y2 = 0;
        for (BufferedImage paraImage : images) {
            graphics.drawImage((Image)paraImage, 0, y2, null);
            y2 += paraImage.getHeight();
        }
        graphics.dispose();
        return image;
    }

    public static BufferedImage renderComponentToImage(JComponent component) {
        BufferedImage image = ImageUtilities.createCompatibleImage(component.getWidth(), component.getHeight());
        Graphics2D graphics = image.createGraphics();
        component.paint(graphics);
        graphics.dispose();
        return image;
    }
}

