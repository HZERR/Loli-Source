/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class TextPainter {
    private Font b = null;
    private Paint c = null;
    private Color d = null;
    private Paint e = null;
    boolean a = true;

    public TextPainter(Font font) {
        this(font, true);
    }

    public TextPainter(Font font, boolean bl) {
        this(font, null, null, null, bl);
    }

    public TextPainter(Font font, Paint paint, Paint paint2, Color color, boolean bl) {
        if (font == null) {
            throw new NullPointerException("font is null");
        }
        this.b = font;
        this.a = bl;
        this.c = paint == null ? Color.BLACK : paint;
        this.e = paint2 == null ? Color.WHITE : paint2;
        this.d = color == null ? new Color(255, 255, 255, 0) : color;
    }

    public Font getFont() {
        return this.b;
    }

    public boolean isAntialiased() {
        return this.a;
    }

    public void setFont(Font font) {
        if (font == null) {
            return;
        }
        this.b = font;
    }

    public void setAntialiased(boolean bl) {
        this.a = bl;
    }

    public Paint getForegroundPaint() {
        return this.c;
    }

    public void setForegroundPaint(Paint paint) {
        if (paint == null) {
            return;
        }
        this.c = paint;
    }

    public Color getBackgroundColor() {
        return this.d;
    }

    public void setBackgroundColor(Color color) {
        if (color == null) {
            return;
        }
        this.d = color;
    }

    public Paint getOutlinePaint() {
        return this.e;
    }

    public void setOutlinePaint(Paint paint) {
        this.e = paint;
    }

    public BufferedImage renderString(String string) {
        return this.renderString(string, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BufferedImage renderString(String string, boolean bl) {
        if (string == null) {
            throw new NullPointerException("string to render is null");
        }
        FontRenderContext fontRenderContext = new FontRenderContext(null, this.a, false);
        Rectangle2D rectangle2D = this.b.getStringBounds(string, fontRenderContext);
        int n2 = (int)Math.round(rectangle2D.getWidth());
        int n3 = (int)Math.round(rectangle2D.getHeight());
        BufferedImage bufferedImage = null;
        if (bl && this.e != null) {
            bufferedImage = new BufferedImage(n2 += 2, n3 += 2, 2);
            Graphics2D graphics2D = null;
            try {
                graphics2D = bufferedImage.createGraphics();
                if (this.a) {
                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }
                graphics2D.setComposite(AlphaComposite.getInstance(2, 1.0f));
                graphics2D.setBackground(this.d);
                graphics2D.clearRect(0, 0, n2, n3);
                graphics2D.setFont(this.b);
                graphics2D.setPaint(this.e);
                float f2 = (float)Math.abs(rectangle2D.getY());
                graphics2D.drawString(string, 0.0f, f2);
                graphics2D.drawString(string, 0.0f, f2 + 1.0f);
                graphics2D.drawString(string, 0.0f, f2 + 2.0f);
                graphics2D.drawString(string, 1.0f, f2);
                graphics2D.drawString(string, 1.0f, f2 + 2.0f);
                graphics2D.drawString(string, 2.0f, f2);
                graphics2D.drawString(string, 2.0f, f2 + 1.0f);
                graphics2D.drawString(string, 2.0f, f2 + 2.0f);
                graphics2D.setPaint(this.c);
                graphics2D.drawString(string, 1.0f, f2 + 1.0f);
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
        }
        bufferedImage = new BufferedImage(n2, n3, 2);
        Graphics2D graphics2D = null;
        try {
            graphics2D = bufferedImage.createGraphics();
            if (this.a) {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            graphics2D.setComposite(AlphaComposite.getInstance(2, 1.0f));
            graphics2D.setBackground(this.d);
            graphics2D.clearRect(0, 0, n2, n3);
            graphics2D.setPaint(this.c);
            graphics2D.setFont(this.b);
            graphics2D.drawString(string, 0.0f, (float)Math.abs(rectangle2D.getY()));
        }
        finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
        return bufferedImage;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BufferedImage renderText(String string, int n2, boolean bl, boolean bl2) {
        Object object;
        if (string == null) {
            throw new NullPointerException("text to render is null");
        }
        if (bl && this.e != null) {
            Object object2;
            AttributedString attributedString = new AttributedString(string);
            attributedString.addAttribute(TextAttribute.FONT, this.b);
            Insets insets = new Insets(2, 2, 2, 2);
            AttributedCharacterIterator attributedCharacterIterator = attributedString.getIterator();
            FontRenderContext fontRenderContext = new FontRenderContext(null, this.a, false);
            LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(attributedCharacterIterator, fontRenderContext);
            float f2 = n2 - insets.left - insets.right - 2;
            float f3 = insets.top;
            while (lineBreakMeasurer.getPosition() < attributedCharacterIterator.getEndIndex()) {
                object2 = lineBreakMeasurer.nextLayout(f2);
                f3 += ((TextLayout)object2).getAscent();
                f3 += ((TextLayout)object2).getDescent() + ((TextLayout)object2).getLeading();
                f3 += 2.0f;
            }
            object2 = new BufferedImage(n2, (int)f3 + insets.bottom, 2);
            Graphics2D graphics2D = null;
            try {
                graphics2D = ((BufferedImage)object2).createGraphics();
                if (this.a) {
                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }
                graphics2D.setComposite(AlphaComposite.getInstance(2, 1.0f));
                graphics2D.setBackground(this.d);
                graphics2D.clearRect(0, 0, n2, ((BufferedImage)object2).getHeight());
                attributedCharacterIterator = attributedString.getIterator();
                lineBreakMeasurer = new LineBreakMeasurer(attributedCharacterIterator, fontRenderContext);
                f2 = n2 - insets.left - insets.right - 2;
                f3 = insets.top;
                while (lineBreakMeasurer.getPosition() < attributedCharacterIterator.getEndIndex()) {
                    TextLayout textLayout = lineBreakMeasurer.nextLayout(f2);
                    float f4 = bl2 ? (float)((int)(((double)f2 - textLayout.getBounds().getWidth()) / 2.0) + insets.left) : (float)insets.left;
                    graphics2D.setPaint(this.e);
                    textLayout.draw(graphics2D, f4, f3 += textLayout.getAscent());
                    textLayout.draw(graphics2D, f4, f3 + 1.0f);
                    textLayout.draw(graphics2D, f4, f3 + 2.0f);
                    textLayout.draw(graphics2D, f4 + 1.0f, f3);
                    textLayout.draw(graphics2D, f4 + 1.0f, f3 + 2.0f);
                    textLayout.draw(graphics2D, f4 + 2.0f, f3);
                    textLayout.draw(graphics2D, f4 + 2.0f, f3 + 1.0f);
                    textLayout.draw(graphics2D, f4 + 2.0f, f3 + 2.0f);
                    graphics2D.setPaint(this.c);
                    textLayout.draw(graphics2D, f4 + 1.0f, f3 + 1.0f);
                    f3 += textLayout.getDescent() + textLayout.getLeading();
                    f3 += 2.0f;
                }
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            return object2;
        }
        AttributedString attributedString = new AttributedString(string);
        attributedString.addAttribute(TextAttribute.FONT, this.b);
        attributedString.addAttribute(TextAttribute.FOREGROUND, this.c);
        attributedString.addAttribute(TextAttribute.BACKGROUND, this.d);
        Insets insets = new Insets(2, 2, 2, 2);
        AttributedCharacterIterator attributedCharacterIterator = attributedString.getIterator();
        FontRenderContext fontRenderContext = new FontRenderContext(null, this.a, false);
        LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(attributedCharacterIterator, fontRenderContext);
        float f5 = n2 - insets.left - insets.right;
        float f6 = insets.top;
        while (lineBreakMeasurer.getPosition() < attributedCharacterIterator.getEndIndex()) {
            object = lineBreakMeasurer.nextLayout(f5);
            f6 += ((TextLayout)object).getAscent();
            f6 += ((TextLayout)object).getDescent() + ((TextLayout)object).getLeading();
        }
        object = new BufferedImage(n2, (int)f6 + insets.bottom, 2);
        Graphics2D graphics2D = null;
        try {
            graphics2D = ((BufferedImage)object).createGraphics();
            if (this.a) {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            graphics2D.setComposite(AlphaComposite.getInstance(2, 1.0f));
            graphics2D.setBackground(this.d);
            graphics2D.clearRect(0, 0, n2, ((BufferedImage)object).getHeight());
            graphics2D.setPaint(this.c);
            attributedCharacterIterator = attributedString.getIterator();
            lineBreakMeasurer = new LineBreakMeasurer(attributedCharacterIterator, fontRenderContext);
            f5 = n2 - insets.left - insets.right;
            f6 = insets.top;
            while (lineBreakMeasurer.getPosition() < attributedCharacterIterator.getEndIndex()) {
                TextLayout textLayout = lineBreakMeasurer.nextLayout(f5);
                f6 += textLayout.getAscent();
                if (bl2) {
                    textLayout.draw(graphics2D, (float)(((double)f5 - textLayout.getBounds().getWidth()) / 2.0) + (float)insets.left, f6);
                } else {
                    textLayout.draw(graphics2D, insets.left, f6);
                }
                f6 += textLayout.getDescent() + textLayout.getLeading();
            }
        }
        finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
        return object;
    }
}

