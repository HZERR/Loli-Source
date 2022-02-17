/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.text;

import com.sun.javafx.tk.Toolkit;
import java.io.File;
import java.io.FilePermission;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public final class Font {
    private static final String DEFAULT_FAMILY = "System";
    private static final String DEFAULT_FULLNAME = "System Regular";
    private static float defaultSystemFontSize = -1.0f;
    private static Font DEFAULT;
    private String name;
    private String family;
    private String style;
    private double size;
    private int hash = 0;
    private Object nativeFont;

    private static float getDefaultSystemFontSize() {
        if (defaultSystemFontSize == -1.0f) {
            defaultSystemFontSize = Toolkit.getToolkit().getFontLoader().getSystemFontSize();
        }
        return defaultSystemFontSize;
    }

    public static synchronized Font getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new Font(DEFAULT_FULLNAME, Font.getDefaultSystemFontSize());
        }
        return DEFAULT;
    }

    public static List<String> getFamilies() {
        return Toolkit.getToolkit().getFontLoader().getFamilies();
    }

    public static List<String> getFontNames() {
        return Toolkit.getToolkit().getFontLoader().getFontNames();
    }

    public static List<String> getFontNames(String string) {
        return Toolkit.getToolkit().getFontLoader().getFontNames(string);
    }

    public static Font font(String string, FontWeight fontWeight, FontPosture fontPosture, double d2) {
        String string2 = string == null || "".equals(string) ? DEFAULT_FAMILY : string;
        double d3 = d2 < 0.0 ? (double)Font.getDefaultSystemFontSize() : d2;
        return Toolkit.getToolkit().getFontLoader().font(string2, fontWeight, fontPosture, (float)d3);
    }

    public static Font font(String string, FontWeight fontWeight, double d2) {
        return Font.font(string, fontWeight, null, d2);
    }

    public static Font font(String string, FontPosture fontPosture, double d2) {
        return Font.font(string, null, fontPosture, d2);
    }

    public static Font font(String string, double d2) {
        return Font.font(string, null, null, d2);
    }

    public static Font font(String string) {
        return Font.font(string, null, null, -1.0);
    }

    public static Font font(double d2) {
        return Font.font(null, null, null, d2);
    }

    public final String getName() {
        return this.name;
    }

    public final String getFamily() {
        return this.family;
    }

    public final String getStyle() {
        return this.style;
    }

    public final double getSize() {
        return this.size;
    }

    public Font(@NamedArg(value="size") double d2) {
        this(null, d2);
    }

    public Font(@NamedArg(value="name") String string, @NamedArg(value="size") double d2) {
        this.name = string;
        this.size = d2;
        if (string == null || "".equals(string)) {
            this.name = DEFAULT_FULLNAME;
        }
        if (d2 < 0.0) {
            this.size = Font.getDefaultSystemFontSize();
        }
        Toolkit.getToolkit().getFontLoader().loadFont(this);
    }

    private Font(Object object, String string, String string2, String string3, double d2) {
        this.nativeFont = object;
        this.family = string;
        this.name = string2;
        this.style = string3;
        this.size = d2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Font loadFont(String string, double d2) {
        URL uRL = null;
        try {
            uRL = new URL(string);
        }
        catch (Exception exception) {
            return null;
        }
        if (d2 <= 0.0) {
            d2 = Font.getDefaultSystemFontSize();
        }
        if (uRL.getProtocol().equals("file")) {
            String string2 = uRL.getFile();
            string2 = new File(string2).getPath();
            try {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    FilePermission filePermission = new FilePermission(string2, "read");
                    securityManager.checkPermission(filePermission);
                }
            }
            catch (Exception exception) {
                return null;
            }
            return Toolkit.getToolkit().getFontLoader().loadFont(string2, d2);
        }
        Font font = null;
        URLConnection uRLConnection = null;
        InputStream inputStream = null;
        try {
            uRLConnection = uRL.openConnection();
            inputStream = uRLConnection.getInputStream();
            font = Toolkit.getToolkit().getFontLoader().loadFont(inputStream, d2);
        }
        catch (Exception exception) {
            Font font2 = null;
            return font2;
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (Exception exception) {}
        }
        return font;
    }

    public static Font loadFont(InputStream inputStream, double d2) {
        if (d2 <= 0.0) {
            d2 = Font.getDefaultSystemFontSize();
        }
        return Toolkit.getToolkit().getFontLoader().loadFont(inputStream, d2);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Font[name=");
        stringBuilder = stringBuilder.append(this.name);
        stringBuilder = stringBuilder.append(", family=").append(this.family);
        stringBuilder = stringBuilder.append(", style=").append(this.style);
        stringBuilder = stringBuilder.append(", size=").append(this.size);
        stringBuilder = stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Font) {
            Font font = (Font)object;
            return (this.name == null ? font.name == null : this.name.equals(font.name)) && this.size == font.size;
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 17L;
            l2 = 37L * l2 + (long)this.name.hashCode();
            l2 = 37L * l2 + Double.doubleToLongBits(this.size);
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    @Deprecated
    public Object impl_getNativeFont() {
        return this.nativeFont;
    }

    @Deprecated
    public void impl_setNativeFont(Object object, String string, String string2, String string3) {
        this.nativeFont = object;
        this.name = string;
        this.family = string2;
        this.style = string3;
    }

    @Deprecated
    public static Font impl_NativeFont(Object object, String string, String string2, String string3, double d2) {
        Font font = new Font(object, string2, string, string3, d2);
        return font;
    }
}

