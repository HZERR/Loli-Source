/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.pushingpixels.substance.internal.animation.IconGlowTracker;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class GlowingIcon
implements Icon {
    protected Icon delegate;
    protected IconGlowTracker iconGlowTracker;
    protected Map<Float, Icon> iconMap;

    public GlowingIcon(Icon delegate, IconGlowTracker iconGlowTracker) {
        this.delegate = delegate;
        this.iconGlowTracker = iconGlowTracker;
        this.iconMap = new HashMap<Float, Icon>();
    }

    public Icon getDelegate() {
        return this.delegate;
    }

    @Override
    public int getIconHeight() {
        if (this.delegate == null) {
            return 0;
        }
        return this.delegate.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        if (this.delegate == null) {
            return 0;
        }
        return this.delegate.getIconWidth();
    }

    @Override
    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
        if (this.delegate == null) {
            return;
        }
        float fadePos = this.iconGlowTracker.getIconGlowPosition();
        Icon toPaint = this.iconMap.get(Float.valueOf(fadePos));
        if (toPaint == null) {
            int width = this.getIconWidth();
            int height = this.getIconHeight();
            BufferedImage image = SubstanceCoreUtilities.getBlankImage(width, height);
            Graphics2D graphics = (Graphics2D)image.getGraphics();
            this.delegate.paintIcon(c2, graphics, 0, 0);
            for (int i2 = 0; i2 < width; ++i2) {
                for (int j2 = 0; j2 < height; ++j2) {
                    int rgba = image.getRGB(i2, j2);
                    int transp = rgba >>> 24 & 0xFF;
                    double coef = Math.sin(Math.PI * 2 * (double)fadePos / 2.0) / 3.0;
                    Color newColor = coef >= 0.0 ? SubstanceColorUtilities.getLighterColor(new Color(rgba), coef) : SubstanceColorUtilities.getDarkerColor(new Color(rgba), -coef);
                    image.setRGB(i2, j2, transp << 24 | newColor.getRed() << 16 | newColor.getGreen() << 8 | newColor.getBlue());
                }
            }
            toPaint = new ImageIcon(image);
            this.iconMap.put(Float.valueOf(fadePos), toPaint);
        }
        toPaint.paintIcon(c2, g2, x2, y2);
    }
}

