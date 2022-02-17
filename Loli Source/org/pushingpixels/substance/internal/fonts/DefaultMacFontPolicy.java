/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.fonts;

import java.awt.Font;
import javax.swing.UIDefaults;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.FontSets;

public class DefaultMacFontPolicy
implements FontPolicy {
    @Override
    public FontSet getFontSet(String lafName, UIDefaults table) {
        Font menuFont;
        Font controlFont = new Font("Lucida Grande", 0, 13);
        Font titleFont = menuFont = table == null ? new Font("Lucida Grande", 0, 14) : table.getFont("Menu.font");
        Font messageFont = table == null ? controlFont : table.getFont("OptionPane.font");
        Font smallFont = table == null ? controlFont.deriveFont(controlFont.getSize2D() - 2.0f) : table.getFont("ToolTip.font");
        Font windowTitleFont = table == null ? titleFont : table.getFont("InternalFrame.titleFont");
        return FontSets.createDefaultFontSet(controlFont, menuFont, titleFont, messageFont, smallFont, windowTitleFont);
    }
}

