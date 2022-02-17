/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.fonts;

import java.awt.Font;
import javax.swing.plaf.FontUIResource;
import org.pushingpixels.substance.api.fonts.FontSet;

public final class FontSets {
    private static FontSet logicalFontSet;

    private FontSets() {
    }

    public static FontSet createDefaultFontSet(Font controlFont) {
        return FontSets.createDefaultFontSet(controlFont, null);
    }

    public static FontSet createDefaultFontSet(Font controlFont, Font menuFont) {
        return FontSets.createDefaultFontSet(controlFont, menuFont, null, null, null, null);
    }

    public static FontSet createDefaultFontSet(Font controlFont, Font menuFont, Font titleFont) {
        return FontSets.createDefaultFontSet(controlFont, menuFont, titleFont, null, null, null);
    }

    public static FontSet createDefaultFontSet(Font controlFont, Font menuFont, Font titleFont, Font messageFont, Font smallFont, Font windowTitleFont) {
        return new DefaultFontSet(controlFont, menuFont, titleFont, messageFont, smallFont, windowTitleFont);
    }

    public static FontSet getLogicalFontSet() {
        if (logicalFontSet == null) {
            logicalFontSet = new LogicalFontSet();
        }
        return logicalFontSet;
    }

    private static final class LogicalFontSet
    implements FontSet {
        private FontUIResource controlFont;
        private FontUIResource titleFont;
        private FontUIResource systemFont;
        private FontUIResource smallFont;

        private LogicalFontSet() {
        }

        @Override
        public FontUIResource getControlFont() {
            if (this.controlFont == null) {
                this.controlFont = new FontUIResource(Font.getFont("swing.plaf.metal.controlFont", new Font("Dialog", 0, 12)));
            }
            return this.controlFont;
        }

        @Override
        public FontUIResource getMenuFont() {
            return this.getControlFont();
        }

        @Override
        public FontUIResource getTitleFont() {
            if (this.titleFont == null) {
                this.titleFont = new FontUIResource(this.getControlFont().deriveFont(1));
            }
            return this.titleFont;
        }

        @Override
        public FontUIResource getSmallFont() {
            if (this.smallFont == null) {
                this.smallFont = new FontUIResource(Font.getFont("swing.plaf.metal.smallFont", new Font("Dialog", 0, 10)));
            }
            return this.smallFont;
        }

        @Override
        public FontUIResource getMessageFont() {
            if (this.systemFont == null) {
                this.systemFont = new FontUIResource(Font.getFont("swing.plaf.metal.systemFont", new Font("Dialog", 0, 12)));
            }
            return this.systemFont;
        }

        @Override
        public FontUIResource getWindowTitleFont() {
            return this.getTitleFont();
        }
    }

    private static final class DefaultFontSet
    implements FontSet {
        private final FontUIResource controlFont;
        private final FontUIResource menuFont;
        private final FontUIResource titleFont;
        private final FontUIResource messageFont;
        private final FontUIResource smallFont;
        private final FontUIResource windowTitleFont;

        public DefaultFontSet(Font controlFont, Font menuFont, Font titleFont, Font messageFont, Font smallFont, Font windowTitleFont) {
            this.controlFont = new FontUIResource(controlFont);
            this.menuFont = menuFont != null ? new FontUIResource(menuFont) : this.controlFont;
            this.titleFont = titleFont != null ? new FontUIResource(titleFont) : this.controlFont;
            this.messageFont = messageFont != null ? new FontUIResource(messageFont) : this.controlFont;
            this.smallFont = new FontUIResource(smallFont != null ? smallFont : controlFont.deriveFont(controlFont.getSize2D() - 2.0f));
            this.windowTitleFont = windowTitleFont != null ? new FontUIResource(windowTitleFont) : this.titleFont;
        }

        @Override
        public FontUIResource getControlFont() {
            return this.controlFont;
        }

        @Override
        public FontUIResource getMenuFont() {
            return this.menuFont;
        }

        @Override
        public FontUIResource getTitleFont() {
            return this.titleFont;
        }

        @Override
        public FontUIResource getWindowTitleFont() {
            return this.windowTitleFont;
        }

        @Override
        public FontUIResource getSmallFont() {
            return this.smallFont;
        }

        @Override
        public FontUIResource getMessageFont() {
            return this.messageFont;
        }
    }
}

