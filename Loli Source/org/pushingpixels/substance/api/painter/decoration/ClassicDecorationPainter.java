/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class ClassicDecorationPainter
implements SubstanceDecorationPainter {
    public static final String DISPLAY_NAME = "Classic";
    protected static final LazyResettableHashMap<BufferedImage> smallImageCache = new LazyResettableHashMap("ClassicDecorationPainter");
    protected ClassicFillPainter painter = new ClassicFillPainter();

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void paintDecorationArea(Graphics2D graphics, Component comp, DecorationAreaType decorationAreaType, int width, int height, SubstanceSkin skin) {
        SubstanceColorScheme scheme = skin.getBackgroundColorScheme(decorationAreaType);
        if (width * height < 100000) {
            HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height, scheme.getDisplayName());
            BufferedImage result = smallImageCache.get(key);
            if (result == null) {
                result = SubstanceCoreUtilities.getBlankImage(width, height);
                this.internalPaint((Graphics2D)result.getGraphics(), comp, width, height, scheme);
                smallImageCache.put(key, result);
            }
            graphics.drawImage((Image)result, 0, 0, null);
            return;
        }
        this.internalPaint(graphics, comp, width, height, scheme);
    }

    protected void internalPaint(Graphics2D graphics, Component comp, int width, int height, SubstanceColorScheme scheme) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        g2d.translate(-3, -3);
        this.painter.paintContourBackground(g2d, comp, width + 6, height + 6, new Rectangle(width + 6, height + 6), false, scheme, false);
        g2d.dispose();
    }
}

