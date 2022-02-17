/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.fonts;

import java.awt.Font;
import javax.swing.UIDefaults;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.FontSets;
import org.pushingpixels.substance.internal.fonts.Fonts;

public final class FontPolicies {
    private FontPolicies() {
    }

    public static FontPolicy createFixedPolicy(FontSet fontSet) {
        return new FixedPolicy(fontSet);
    }

    public static FontPolicy customSettingsPolicy(FontPolicy defaultPolicy) {
        return new CustomSettingsPolicy(defaultPolicy);
    }

    public static FontPolicy getDefaultPlasticOnWindowsPolicy() {
        return new DefaultPlasticOnWindowsPolicy();
    }

    public static FontPolicy getDefaultPlasticPolicy() {
        if (LookUtils.IS_OS_WINDOWS) {
            return FontPolicies.getDefaultPlasticOnWindowsPolicy();
        }
        return FontPolicies.getLogicalFontsPolicy();
    }

    public static FontPolicy getDefaultWindowsPolicy() {
        return new DefaultWindowsPolicy();
    }

    public static FontPolicy getLogicalFontsPolicy() {
        return FontPolicies.createFixedPolicy(FontSets.getLogicalFontSet());
    }

    public static FontPolicy getLooks1xPlasticPolicy() {
        Font controlFont;
        Font menuFont = controlFont = Fonts.getDefaultGUIFontWesternModernWindowsNormal();
        Font titleFont = controlFont.deriveFont(1);
        FontSet fontSet = FontSets.createDefaultFontSet(controlFont, menuFont, titleFont);
        return FontPolicies.createFixedPolicy(fontSet);
    }

    public static FontPolicy getLooks1xWindowsPolicy() {
        return new Looks1xWindowsPolicy();
    }

    public static FontPolicy getTransitionalPlasticPolicy() {
        return LookUtils.IS_OS_WINDOWS ? FontPolicies.getDefaultPlasticOnWindowsPolicy() : FontPolicies.getLooks1xPlasticPolicy();
    }

    private static FontSet getCustomFontSet(String lafName) {
        String controlFontKey = lafName + ".controlFont";
        String menuFontKey = lafName + ".menuFont";
        String decodedControlFont = LookUtils.getSystemProperty(controlFontKey);
        if (decodedControlFont == null) {
            return null;
        }
        Font controlFont = Font.decode(decodedControlFont);
        String decodedMenuFont = LookUtils.getSystemProperty(menuFontKey);
        Font menuFont = decodedMenuFont != null ? Font.decode(decodedMenuFont) : null;
        Font titleFont = "Plastic".equals(lafName) ? controlFont.deriveFont(1) : controlFont;
        return FontSets.createDefaultFontSet(controlFont, menuFont, titleFont);
    }

    private static FontPolicy getCustomPolicy(String lafName) {
        return null;
    }

    private static final class Looks1xWindowsPolicy
    implements FontPolicy {
        private Looks1xWindowsPolicy() {
        }

        @Override
        public FontSet getFontSet(String lafName, UIDefaults table) {
            Font windowsControlFont = Fonts.getLooks1xWindowsControlFont();
            Font controlFont = windowsControlFont != null ? windowsControlFont : (table != null ? table.getFont("Button.font") : new Font("Dialog", 0, 12));
            return FontSets.createDefaultFontSet(controlFont);
        }
    }

    private static final class FixedPolicy
    implements FontPolicy {
        private final FontSet fontSet;

        FixedPolicy(FontSet fontSet) {
            this.fontSet = fontSet;
        }

        @Override
        public FontSet getFontSet(String lafName, UIDefaults table) {
            return this.fontSet;
        }
    }

    private static final class DefaultWindowsPolicy
    implements FontPolicy {
        private DefaultWindowsPolicy() {
        }

        @Override
        public FontSet getFontSet(String lafName, UIDefaults table) {
            Font windowsControlFont = Fonts.getWindowsControlFont();
            Font controlFont = windowsControlFont != null ? windowsControlFont : (table != null ? table.getFont("Button.font") : new Font("Dialog", 0, 12));
            Font menuFont = table == null ? controlFont : table.getFont("Menu.font");
            Font titleFont = controlFont;
            Font messageFont = table == null ? controlFont : table.getFont("OptionPane.font");
            Font smallFont = table == null ? controlFont.deriveFont(controlFont.getSize2D() - 2.0f) : table.getFont("ToolTip.font");
            Font windowTitleFont = table == null ? controlFont : table.getFont("InternalFrame.titleFont");
            return FontSets.createDefaultFontSet(controlFont, menuFont, titleFont, messageFont, smallFont, windowTitleFont);
        }
    }

    private static final class DefaultPlasticOnWindowsPolicy
    implements FontPolicy {
        private DefaultPlasticOnWindowsPolicy() {
        }

        @Override
        public FontSet getFontSet(String lafName, UIDefaults table) {
            Font windowsControlFont = Fonts.getWindowsControlFont();
            Font controlFont = windowsControlFont != null ? windowsControlFont : (table != null ? table.getFont("Button.font") : new Font("Dialog", 0, 12));
            Font menuFont = table == null ? controlFont : table.getFont("Menu.font");
            Font titleFont = controlFont.deriveFont(1);
            return FontSets.createDefaultFontSet(controlFont, menuFont, titleFont);
        }
    }

    private static final class CustomSettingsPolicy
    implements FontPolicy {
        private final FontPolicy wrappedPolicy;

        CustomSettingsPolicy(FontPolicy wrappedPolicy) {
            this.wrappedPolicy = wrappedPolicy;
        }

        @Override
        public FontSet getFontSet(String lafName, UIDefaults table) {
            FontPolicy customPolicy = FontPolicies.getCustomPolicy(lafName);
            if (customPolicy != null) {
                return customPolicy.getFontSet(null, table);
            }
            FontSet customFontSet = FontPolicies.getCustomFontSet(lafName);
            if (customFontSet != null) {
                return customFontSet;
            }
            return this.wrappedPolicy.getFontSet(lafName, table);
        }
    }
}

