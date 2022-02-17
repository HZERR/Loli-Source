/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.Locale;

public final class LookUtils {
    private static final String JAVA_VERSION = LookUtils.getSystemProperty("java.version");
    private static final String OS_NAME = LookUtils.getSystemProperty("os.name");
    private static final String OS_VERSION = LookUtils.getSystemProperty("os.version");
    public static final boolean IS_JAVA_1_4 = LookUtils.startsWith(JAVA_VERSION, "1.4");
    public static final boolean IS_JAVA_1_4_0 = LookUtils.startsWith(JAVA_VERSION, "1.4.0");
    public static final boolean IS_JAVA_1_4_2_OR_LATER = !LookUtils.startsWith(JAVA_VERSION, "1.4.0") && !LookUtils.startsWith(JAVA_VERSION, "1.4.1");
    public static final boolean IS_JAVA_5 = LookUtils.startsWith(JAVA_VERSION, "1.5");
    public static final boolean IS_JAVA_5_OR_LATER = !IS_JAVA_1_4;
    public static final boolean IS_JAVA_6 = LookUtils.startsWith(JAVA_VERSION, "1.6");
    public static final boolean IS_JAVA_6_OR_LATER = !IS_JAVA_1_4 && !IS_JAVA_5;
    public static final boolean IS_JAVA_1_4_OR_5 = IS_JAVA_1_4 || IS_JAVA_5;
    public static final boolean IS_OS_FREEBSD = LookUtils.startsWithIgnoreCase(OS_NAME, "FreeBSD");
    public static final boolean IS_OS_LINUX = LookUtils.startsWithIgnoreCase(OS_NAME, "Linux");
    public static final boolean IS_OS_OS2 = LookUtils.startsWith(OS_NAME, "OS/2");
    public static final boolean IS_OS_MAC = LookUtils.startsWith(OS_NAME, "Mac");
    public static final boolean IS_OS_WINDOWS = LookUtils.startsWith(OS_NAME, "Windows");
    public static final boolean IS_OS_WINDOWS_MODERN = LookUtils.startsWith(OS_NAME, "Windows") && !LookUtils.startsWith(OS_VERSION, "4.0");
    public static final boolean IS_OS_WINDOWS_95 = LookUtils.startsWith(OS_NAME, "Windows 9") && LookUtils.startsWith(OS_VERSION, "4.0");
    public static final boolean IS_OS_WINDOWS_98 = LookUtils.startsWith(OS_NAME, "Windows 9") && LookUtils.startsWith(OS_VERSION, "4.1");
    public static final boolean IS_OS_WINDOWS_NT = LookUtils.startsWith(OS_NAME, "Windows NT");
    public static final boolean IS_OS_WINDOWS_ME = LookUtils.startsWith(OS_NAME, "Windows") && LookUtils.startsWith(OS_VERSION, "4.9");
    public static final boolean IS_OS_WINDOWS_2000 = LookUtils.startsWith(OS_NAME, "Windows") && LookUtils.startsWith(OS_VERSION, "5.0");
    public static final boolean IS_OS_WINDOWS_XP = LookUtils.startsWith(OS_NAME, "Windows") && LookUtils.startsWith(OS_VERSION, "5.1");
    public static final boolean IS_OS_WINDOWS_VISTA = LookUtils.startsWith(OS_NAME, "Windows") && LookUtils.startsWith(OS_VERSION, "6.0");
    public static final boolean IS_OS_SOLARIS = LookUtils.startsWith(OS_NAME, "Solaris");
    public static final boolean IS_LAF_WINDOWS_XP_ENABLED = LookUtils.isWindowsXPLafEnabled();
    public static final boolean IS_LOW_RESOLUTION = LookUtils.isLowResolution();
    private static boolean loggingEnabled = true;

    private LookUtils() {
    }

    public static String getSystemProperty(String key) {
        try {
            return System.getProperty(key);
        }
        catch (SecurityException e2) {
            return null;
        }
    }

    public static String getSystemProperty(String key, String defaultValue) {
        try {
            return System.getProperty(key, defaultValue);
        }
        catch (SecurityException e2) {
            return defaultValue;
        }
    }

    public static Boolean getBooleanSystemProperty(String key, String logMessage) {
        String value = LookUtils.getSystemProperty(key, "");
        Boolean result = value.equalsIgnoreCase("false") ? Boolean.FALSE : (value.equalsIgnoreCase("true") ? Boolean.TRUE : null);
        if (result != null) {
            LookUtils.log(logMessage + " have been " + (result != false ? "en" : "dis") + "abled in the system properties.");
        }
        return result;
    }

    private static boolean isWindowsXPLafEnabled() {
        return (IS_OS_WINDOWS_XP || IS_OS_WINDOWS_VISTA) && IS_JAVA_1_4_2_OR_LATER && Boolean.TRUE.equals(Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.themeActive")) && LookUtils.getSystemProperty("swing.noxp") == null;
    }

    public static boolean isTrueColor(Component c2) {
        return c2.getToolkit().getColorModel().getPixelSize() >= 24;
    }

    public static boolean getToolkitUsesNativeDropShadows() {
        return IS_OS_MAC;
    }

    public static Color getSlightlyBrighter(Color color) {
        return LookUtils.getSlightlyBrighter(color, 1.1f);
    }

    public static Color getSlightlyBrighter(Color color, float factor) {
        float[] hsbValues = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbValues);
        float hue = hsbValues[0];
        float saturation = hsbValues[1];
        float brightness = hsbValues[2];
        float newBrightness = Math.min(brightness * factor, 1.0f);
        return Color.getHSBColor(hue, saturation, newBrightness);
    }

    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static void log() {
        if (loggingEnabled) {
            System.out.println();
        }
    }

    public static void log(String message) {
        if (loggingEnabled) {
            System.out.println("JGoodies Looks: " + message);
        }
    }

    private static boolean isLowResolution() {
        return Toolkit.getDefaultToolkit().getScreenResolution() < 120;
    }

    private static boolean startsWith(String str, String prefix) {
        return str != null && str.startsWith(prefix);
    }

    private static boolean startsWithIgnoreCase(String str, String prefix) {
        return str != null && str.toUpperCase(Locale.ENGLISH).startsWith(prefix.toUpperCase(Locale.ENGLISH));
    }
}

