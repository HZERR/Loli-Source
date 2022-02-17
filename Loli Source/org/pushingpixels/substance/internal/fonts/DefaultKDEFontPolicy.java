/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.fonts;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.UIDefaults;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.FontSets;

public class DefaultKDEFontPolicy
implements FontPolicy {
    private static final String SANS_SERIF = "SansSerif";
    private static FontSet fontSet = null;

    public static boolean isKDERunning() {
        return "true".equals(System.getenv("KDE_FULL_SESSION"));
    }

    private static boolean isKDE4Running() {
        if (!DefaultKDEFontPolicy.isKDERunning()) {
            throw new IllegalStateException("KDE is not running");
        }
        return "4".equals(System.getenv("KDE_SESSION_VERSION")) || "kde4".equals(System.getenv("DESKTOP_SESSION"));
    }

    @Override
    public synchronized FontSet getFontSet(String lafName, UIDefaults table) {
        if (fontSet == null) {
            fontSet = this.getInternalFontSet(lafName, table);
        }
        return fontSet;
    }

    private FontSet getInternalFontSet(String lafName, UIDefaults table) {
        int dpi;
        String titleFamily;
        String menuFamily;
        String commonFamily;
        int titleStyle;
        int menuStyle;
        int commonStyle;
        int titleSize;
        int menuSize;
        int commonSize;
        block18: {
            commonSize = 10;
            menuSize = 10;
            titleSize = 10;
            commonStyle = 0;
            menuStyle = 0;
            titleStyle = 1;
            commonFamily = SANS_SERIF;
            menuFamily = SANS_SERIF;
            titleFamily = SANS_SERIF;
            try {
                boolean italic;
                boolean bold;
                String kdeglobals = this.getFileContent("kdeglobals");
                Pattern pattern = Pattern.compile(",");
                try {
                    String fontParam = this.getIniParam(kdeglobals, "[General]", "font");
                    String[] fontParams = pattern.split(fontParam);
                    commonFamily = fontParams[0];
                    commonSize = Integer.parseInt(fontParams[1]);
                    bold = "75".equals(fontParams[4]);
                    italic = "1".equals(fontParams[5]);
                    commonStyle = bold && italic ? 3 : (italic ? 2 : (bold ? 1 : 0));
                }
                catch (Exception commonReadException) {
                    commonFamily = SANS_SERIF;
                    commonSize = 10;
                    commonStyle = 0;
                }
                try {
                    String menuFontParam = this.getIniParam(kdeglobals, "[General]", "menuFont");
                    String[] menuFontParams = pattern.split(menuFontParam);
                    menuFamily = menuFontParams[0];
                    menuSize = Integer.parseInt(menuFontParams[1]);
                    bold = "75".equals(menuFontParams[4]);
                    italic = "1".equals(menuFontParams[5]);
                    menuStyle = bold && italic ? 3 : (italic ? 2 : (bold ? 1 : 0));
                }
                catch (Exception menuReadException) {
                    menuFamily = SANS_SERIF;
                    menuSize = 10;
                    menuStyle = 0;
                }
                try {
                    String activeFontParam = this.getIniParam(kdeglobals, "[WM]", "activeFont");
                    String[] activeFontParams = pattern.split(activeFontParam);
                    titleFamily = activeFontParams[0];
                    titleSize = Integer.parseInt(activeFontParams[1]);
                    bold = "75".equals(activeFontParams[4]);
                    italic = "1".equals(activeFontParams[5]);
                    if (bold && italic) {
                        titleStyle = 3;
                        break block18;
                    }
                    if (italic) {
                        titleStyle = 2;
                        break block18;
                    }
                    if (bold) {
                        titleStyle = 1;
                        break block18;
                    }
                    titleStyle = 0;
                }
                catch (Exception titleReadException) {
                    titleFamily = SANS_SERIF;
                    titleSize = 10;
                    titleStyle = 1;
                }
            }
            catch (Exception kdeglobalsReadException) {
                commonFamily = SANS_SERIF;
                commonSize = 10;
                commonStyle = 0;
                menuFamily = SANS_SERIF;
                menuSize = 10;
                menuStyle = 0;
                titleFamily = SANS_SERIF;
                titleSize = 10;
                titleStyle = 1;
            }
        }
        double dcommonSize = commonSize;
        double dmenuSize = menuSize;
        double dtitleSize = titleSize;
        try {
            String dpiParam = this.getIniParam(this.getFileContent("kcmfonts"), "[General]", "forceFontDPI");
            dpi = Integer.parseInt(dpiParam);
        }
        catch (Exception dpiReadException) {
            dpi = 96;
        }
        if (dpi <= 0) {
            dpi = 96;
        }
        if (dpi < 50) {
            dpi = 50;
        }
        dcommonSize = (double)(commonSize * dpi) / 72.0;
        dmenuSize = (double)(menuSize * dpi) / 72.0;
        dtitleSize = (double)(titleSize * dpi) / 72.0;
        commonSize = (int)(dcommonSize + 0.5);
        if (commonSize < 1) {
            commonSize = 1;
        }
        if ((menuSize = (int)(dmenuSize + 0.5)) < 1) {
            menuSize = 1;
        }
        if ((titleSize = (int)(dtitleSize + 0.5)) < 1) {
            titleSize = 1;
        }
        Font commonFont = new Font(commonFamily, commonStyle, commonSize);
        Font menuFont = new Font(menuFamily, menuStyle, menuSize);
        Font titleFont = new Font(titleFamily, titleStyle, titleSize);
        return FontSets.createDefaultFontSet(commonFont, menuFont, commonFont, commonFont, commonFont, titleFont);
    }

    private String getIniParam(String content, String category, String param) throws Exception {
        int paramIndex;
        int categoryIndex = content.indexOf(category);
        String lineSeparator = System.getProperty("line.separator");
        int nextCategoryIndex = content.indexOf(lineSeparator + '[', categoryIndex + 1);
        if (nextCategoryIndex < 0) {
            nextCategoryIndex = content.length();
        }
        if ((paramIndex = content.indexOf(param, categoryIndex)) >= 0 && paramIndex < nextCategoryIndex) {
            int equalityIndex;
            String paramString;
            String result;
            int lineEnd = content.indexOf(lineSeparator, paramIndex);
            if (lineEnd <= 0) {
                lineEnd = content.length();
            }
            if ((result = (paramString = content.substring(paramIndex, lineEnd)).substring((equalityIndex = paramString.indexOf(61)) + 1).trim()).length() > 0) {
                return result;
            }
        }
        throw new Exception("No such param in current category");
    }

    private String getFileContent(String fileName) throws IOException {
        File kdeHomeFolder;
        String userHome = System.getProperty("user.home");
        String kdeHome = null;
        if (DefaultKDEFontPolicy.isKDE4Running() && (kdeHomeFolder = new File(userHome, ".kde4")).exists()) {
            kdeHome = ".kde4";
        }
        if (kdeHome == null) {
            kdeHome = ".kde";
        }
        char fs = File.separatorChar;
        fileName = userHome + fs + kdeHome + "/share/config/" + fileName;
        if (fs != '/') {
            fileName = fileName.replace('/', fs);
        }
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[8192];
        int read = 0;
        while ((read = in.read(buffer)) >= 0) {
            sb.append(buffer, 0, read);
        }
        in.close();
        return sb.toString();
    }
}

