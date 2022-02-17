/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.fonts;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.DefaultGnomeFontPolicy;
import org.pushingpixels.substance.internal.fonts.DefaultKDEFontPolicy;
import org.pushingpixels.substance.internal.fonts.DefaultMacFontPolicy;
import org.pushingpixels.substance.internal.fonts.FontPolicies;
import org.pushingpixels.substance.internal.fonts.ScaledFontSet;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceFontUtilities {
    public static FontPolicy getDefaultFontPolicy() {
        FontPolicy defaultPolicy = FontPolicies.getDefaultPlasticPolicy();
        boolean isKDE = false;
        try {
            isKDE = DefaultKDEFontPolicy.isKDERunning();
        }
        catch (Throwable t2) {
            // empty catch block
        }
        if (LookUtils.IS_OS_WINDOWS) {
            defaultPolicy = FontPolicies.getDefaultWindowsPolicy();
        } else if (LookUtils.IS_OS_MAC) {
            defaultPolicy = new DefaultMacFontPolicy();
        } else if (isKDE) {
            defaultPolicy = new DefaultKDEFontPolicy();
        } else {
            try {
                String desktop = AccessController.doPrivileged(new PrivilegedAction<String>(){

                    @Override
                    public String run() {
                        return System.getProperty("sun.desktop");
                    }
                });
                if ("gnome".equals(desktop)) {
                    defaultPolicy = new DefaultGnomeFontPolicy();
                }
            }
            catch (Throwable t3) {
                // empty catch block
            }
        }
        SubstanceSizeUtils.resetPointsToPixelsRatio(defaultPolicy);
        final FontPolicy fontPolicy = FontPolicies.customSettingsPolicy(defaultPolicy);
        if (LookUtils.IS_OS_MAC || isKDE) {
            return fontPolicy;
        }
        return new FontPolicy(){

            @Override
            public FontSet getFontSet(String lafName, UIDefaults table) {
                FontSet baseResult = fontPolicy.getFontSet(lafName, table);
                SubstanceFontSet substanceFontSet = new SubstanceFontSet(baseResult);
                return substanceFontSet;
            }
        };
    }

    public static FontPolicy getScaledFontPolicy(final float scaleFactor) {
        final FontSet substanceCoreFontSet = SubstanceFontUtilities.getDefaultFontPolicy().getFontSet("Substance", null);
        FontPolicy newFontPolicy = new FontPolicy(){

            @Override
            public FontSet getFontSet(String lafName, UIDefaults table) {
                return new ScaledFontSet(substanceCoreFontSet, scaleFactor);
            }
        };
        return newFontPolicy;
    }

    private static class SubstanceFontSet
    implements FontSet {
        private FontSet systemFontSet;

        public SubstanceFontSet(FontSet systemFontSet) {
            this.systemFontSet = systemFontSet;
        }

        private FontUIResource getSubstanceFont(FontUIResource systemFont) {
            return systemFont;
        }

        private FontUIResource getSubstanceFont(FontUIResource systemFont, boolean toBoldify, int extraFontSize) {
            boolean isOrigItalic = systemFont.isItalic();
            int newStyle = systemFont.getStyle();
            if (toBoldify) {
                newStyle = isOrigItalic ? 3 : 1;
            }
            return new FontUIResource(systemFont.deriveFont((float)(systemFont.getSize() + extraFontSize)).deriveFont(newStyle));
        }

        @Override
        public FontUIResource getControlFont() {
            return this.getSubstanceFont(this.systemFontSet.getControlFont());
        }

        @Override
        public FontUIResource getMenuFont() {
            return this.getSubstanceFont(this.systemFontSet.getMenuFont());
        }

        @Override
        public FontUIResource getMessageFont() {
            return this.getSubstanceFont(this.systemFontSet.getMessageFont());
        }

        @Override
        public FontUIResource getSmallFont() {
            return this.getSubstanceFont(this.systemFontSet.getSmallFont(), false, 1);
        }

        @Override
        public FontUIResource getTitleFont() {
            return this.getSubstanceFont(this.systemFontSet.getTitleFont());
        }

        @Override
        public FontUIResource getWindowTitleFont() {
            return this.getSubstanceFont(this.systemFontSet.getWindowTitleFont(), true, 1);
        }
    }
}

