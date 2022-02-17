/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.plaf.IconUIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.ButtonStateIcon;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util.Images;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util.ShiftedIcon;

public class QuaquaIconFactory {
    private static BufferedImage applicationImage;

    private QuaquaIconFactory() {
    }

    public static URL getResource(String location) {
        URL url = QuaquaIconFactory.class.getResource(location);
        if (url == null) {
            throw new InternalError("image resource missing: " + location);
        }
        return url;
    }

    public static Image createImage(String location) {
        return QuaquaIconFactory.createImage(QuaquaIconFactory.class, location);
    }

    public static Image createImage(Class baseClass, String location) {
        return Images.createImage(baseClass.getResource(location));
    }

    public static Image createBufferedImage(String location) {
        return Images.toBufferedImage(QuaquaIconFactory.createImage(location));
    }

    public static Icon[] createIcons(String location, int count, boolean horizontal) {
        Icon[] icons = new Icon[count];
        BufferedImage[] images = Images.split(QuaquaIconFactory.createImage(location), count, horizontal);
        for (int i2 = 0; i2 < count; ++i2) {
            icons[i2] = new IconUIResource(new ImageIcon(images[i2]));
        }
        return icons;
    }

    public static Icon createIcon(String location, int count, boolean horizontal, int index) {
        return QuaquaIconFactory.createIcons(location, count, horizontal)[index];
    }

    public static Icon createButtonStateIcon(String location, int states) {
        return new ButtonStateIcon(QuaquaIconFactory.createImage(location), states, true);
    }

    public static Icon createButtonStateIcon(String location, int states, Point shift) {
        return new ShiftedIcon((Icon)new ButtonStateIcon(QuaquaIconFactory.createImage(location), states, true), shift);
    }

    public static Icon createButtonStateIcon(String location, int states, Rectangle shift) {
        return new ShiftedIcon((Icon)new ButtonStateIcon(QuaquaIconFactory.createImage(location), states, true), shift);
    }

    public static Icon createIcon(Class baseClass, String location) {
        return new ImageIcon(QuaquaIconFactory.createImage(baseClass, location));
    }

    public static Icon createIcon(Class baseClass, String location, Point shift) {
        return new ShiftedIcon((Icon)new ImageIcon(QuaquaIconFactory.createImage(baseClass, location)), shift);
    }

    public static Icon createIcon(Class baseClass, String location, Rectangle shiftAndSize) {
        return new ShiftedIcon((Icon)new ImageIcon(QuaquaIconFactory.createImage(baseClass, location)), shiftAndSize);
    }
}

