/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.fonts;

import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.FontSets;

public class DefaultGnomeFontPolicy
implements FontPolicy {
    private static double fontScale;

    @Override
    public FontSet getFontSet(String lafName, UIDefaults table) {
        double dsize;
        Object defaultGtkFontName = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Gtk/FontName");
        String family = "";
        int style = 0;
        int size = 10;
        if (defaultGtkFontName instanceof String) {
            String pangoName = (String)defaultGtkFontName;
            StringTokenizer tok = new StringTokenizer(pangoName);
            while (tok.hasMoreTokens()) {
                String word = tok.nextToken();
                boolean allDigits = true;
                for (int i2 = 0; i2 < word.length(); ++i2) {
                    if (Character.isDigit(word.charAt(i2))) continue;
                    allDigits = false;
                    break;
                }
                if (word.equalsIgnoreCase("italic")) {
                    style |= 2;
                    continue;
                }
                if (word.equalsIgnoreCase("bold")) {
                    style |= 1;
                    continue;
                }
                if (allDigits) {
                    try {
                        size = Integer.parseInt(word);
                    }
                    catch (NumberFormatException nfe) {
                        size = 10;
                    }
                    continue;
                }
                if (family.length() > 0) {
                    family = family + " ";
                }
                family = family + word;
            }
        }
        if ((size = (int)((dsize = (double)size * DefaultGnomeFontPolicy.getPointsToPixelsRatio()) + 0.5)) < 1) {
            size = 1;
        }
        if (family.length() == 0) {
            family = "sans";
        }
        Font controlFont = null;
        String fcFamilyLC = family.toLowerCase();
        try {
            Class<?> fontManagerClass = Class.forName("sun.font.FontManager");
            Method mapFcMethod = fontManagerClass.getMethod("mapFcName", String.class);
            Object mapFcMethodResult = mapFcMethod.invoke(null, fcFamilyLC);
            if (mapFcMethodResult != null) {
                Method getFontConfigFUIRMethod = fontManagerClass.getMethod("getFontConfigFUIR", String.class, Integer.TYPE, Integer.TYPE);
                controlFont = (Font)getFontConfigFUIRMethod.invoke(null, fcFamilyLC, style, size);
            } else {
                FontUIResource font = new FontUIResource(family, style, size);
                Method getCompositeFontUIResourceMethod = fontManagerClass.getMethod("getCompositeFontUIResource", Font.class);
                controlFont = (Font)getCompositeFontUIResourceMethod.invoke(null, font);
            }
        }
        catch (Throwable t2) {
            controlFont = new Font(family, style, size);
        }
        return FontSets.createDefaultFontSet(controlFont);
    }

    public static double getPointsToPixelsRatio() {
        int dpi = 96;
        Object value = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Xft/DPI");
        if (value instanceof Integer) {
            dpi = (Integer)value / 1024;
            if (dpi == -1) {
                dpi = 96;
            }
            if (dpi < 50) {
                dpi = 50;
            }
            return (double)dpi / 72.0;
        }
        return fontScale;
    }

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        AffineTransform at = gc.getNormalizingTransform();
        fontScale = at.getScaleY();
    }
}

