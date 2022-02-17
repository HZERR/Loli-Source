/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.jgoodies.looks;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.substance.internal.contrib.jgoodies.looks.FontSizeHints;
import org.pushingpixels.substance.internal.contrib.jgoodies.looks.common.ShadowPopup;

public final class Options {
    public static final String PLASTIC_NAME = "com.jgoodies.looks.plastic.PlasticLookAndFeel";
    public static final String PLASTIC3D_NAME = "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
    public static final String PLASTICXP_NAME = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
    public static final String JGOODIES_WINDOWS_NAME = "com.jgoodies.looks.windows.WindowsLookAndFeel";
    @Deprecated
    public static final String EXT_WINDOWS_NAME = "com.jgoodies.looks.windows.WindowsLookAndFeel";
    public static final String DEFAULT_LOOK_NAME = "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
    private static final Map LAF_REPLACEMENTS = new HashMap();
    public static final String MENU_FONT_KEY = "jgoodies.menuFont";
    public static final String CONTROL_FONT_KEY = "jgoodies.controlFont";
    public static final String FONT_SIZE_HINTS_KEY = "jgoodies.fontSizeHints";
    public static final String USE_SYSTEM_FONTS_KEY = "swing.useSystemFontSettings";
    public static final String USE_SYSTEM_FONTS_APP_KEY = "Application.useSystemFontSettings";
    public static final String DEFAULT_ICON_SIZE_KEY = "jgoodies.defaultIconSize";
    public static final String USE_NARROW_BUTTONS_KEY = "jgoodies.useNarrowButtons";
    public static final String TAB_ICONS_ENABLED_KEY = "jgoodies.tabIconsEnabled";
    public static final String POPUP_DROP_SHADOW_ENABLED_KEY = "jgoodies.popupDropShadowEnabled";
    public static final String IS_NARROW_KEY = "jgoodies.isNarrow";
    public static final String IS_ETCHED_KEY = "jgoodies.isEtched";
    public static final String HEADER_STYLE_KEY = "jgoodies.headerStyle";
    public static final String NO_ICONS_KEY = "jgoodies.noIcons";
    public static final String TREE_LINE_STYLE_KEY = "JTree.lineStyle";
    public static final String TREE_LINE_STYLE_ANGLED_VALUE = "Angled";
    public static final String TREE_LINE_STYLE_NONE_VALUE = "None";
    public static final String NO_CONTENT_BORDER_KEY = "jgoodies.noContentBorder";
    public static final String EMBEDDED_TABS_KEY = "jgoodies.embeddedTabs";
    private static final Boolean TAB_ICONS_ENABLED_SYSTEM_VALUE;
    private static final Boolean POPUP_DROP_SHADOW_ENABLED_SYSTEM_VALUE;
    private static final Dimension DEFAULT_ICON_SIZE;

    private Options() {
    }

    public static boolean getUseSystemFonts() {
        return UIManager.get(USE_SYSTEM_FONTS_APP_KEY).equals(Boolean.TRUE);
    }

    public static void setUseSystemFonts(boolean useSystemFonts) {
        UIManager.put(USE_SYSTEM_FONTS_APP_KEY, useSystemFonts);
    }

    public static Dimension getDefaultIconSize() {
        Dimension size = UIManager.getDimension(DEFAULT_ICON_SIZE_KEY);
        return size == null ? DEFAULT_ICON_SIZE : size;
    }

    public static void setDefaultIconSize(Dimension defaultIconSize) {
        UIManager.put(DEFAULT_ICON_SIZE_KEY, defaultIconSize);
    }

    public static FontSizeHints getGlobalFontSizeHints() {
        Object value = UIManager.get(FONT_SIZE_HINTS_KEY);
        if (value != null) {
            return (FontSizeHints)value;
        }
        String name = LookUtils.getSystemProperty(FONT_SIZE_HINTS_KEY, "");
        try {
            return FontSizeHints.valueOf(name);
        }
        catch (IllegalArgumentException e2) {
            return FontSizeHints.DEFAULT;
        }
    }

    public static void setGlobalFontSizeHints(FontSizeHints hints) {
        UIManager.put(FONT_SIZE_HINTS_KEY, hints);
    }

    public static boolean getUseNarrowButtons() {
        return UIManager.getBoolean(USE_NARROW_BUTTONS_KEY);
    }

    public static void setUseNarrowButtons(boolean b2) {
        UIManager.put(USE_NARROW_BUTTONS_KEY, b2);
    }

    public static boolean isTabIconsEnabled() {
        return TAB_ICONS_ENABLED_SYSTEM_VALUE == null ? !Boolean.FALSE.equals(UIManager.get(TAB_ICONS_ENABLED_KEY)) : TAB_ICONS_ENABLED_SYSTEM_VALUE;
    }

    public static void setTabIconsEnabled(boolean b2) {
        UIManager.put(TAB_ICONS_ENABLED_KEY, b2);
    }

    public static boolean isPopupDropShadowActive() {
        return !LookUtils.getToolkitUsesNativeDropShadows() && ShadowPopup.canSnapshot() && Options.isPopupDropShadowEnabled();
    }

    public static boolean isPopupDropShadowEnabled() {
        if (POPUP_DROP_SHADOW_ENABLED_SYSTEM_VALUE != null) {
            return POPUP_DROP_SHADOW_ENABLED_SYSTEM_VALUE;
        }
        Object value = UIManager.get(POPUP_DROP_SHADOW_ENABLED_KEY);
        return value == null ? Options.isPopupDropShadowEnabledDefault() : Boolean.TRUE.equals(value);
    }

    public static void setPopupDropShadowEnabled(boolean b2) {
        UIManager.put(POPUP_DROP_SHADOW_ENABLED_KEY, b2);
    }

    private static boolean isPopupDropShadowEnabledDefault() {
        return LookUtils.IS_OS_WINDOWS_MODERN;
    }

    public static void putLookAndFeelReplacement(String original, String replacement) {
        LAF_REPLACEMENTS.put(original, replacement);
    }

    public static void removeLookAndFeelReplacement(String original) {
        LAF_REPLACEMENTS.remove(original);
    }

    public static void initializeDefaultReplacements() {
        Options.putLookAndFeelReplacement("javax.swing.plaf.metal.MetalLookAndFeel", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        Options.putLookAndFeelReplacement("com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "com.jgoodies.looks.windows.WindowsLookAndFeel");
    }

    public static String getReplacementClassNameFor(String className) {
        String replacement = (String)LAF_REPLACEMENTS.get(className);
        return replacement == null ? className : replacement;
    }

    public static String getCrossPlatformLookAndFeelClassName() {
        return "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
    }

    public static String getSystemLookAndFeelClassName() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return "com.jgoodies.looks.windows.WindowsLookAndFeel";
        }
        if (osName.startsWith("Mac")) {
            return UIManager.getSystemLookAndFeelClassName();
        }
        return Options.getCrossPlatformLookAndFeelClassName();
    }

    static {
        Options.initializeDefaultReplacements();
        TAB_ICONS_ENABLED_SYSTEM_VALUE = LookUtils.getBooleanSystemProperty(TAB_ICONS_ENABLED_KEY, "Icons in tabbed panes");
        POPUP_DROP_SHADOW_ENABLED_SYSTEM_VALUE = LookUtils.getBooleanSystemProperty(POPUP_DROP_SHADOW_ENABLED_KEY, "Popup drop shadows");
        DEFAULT_ICON_SIZE = new Dimension(20, 20);
    }
}

