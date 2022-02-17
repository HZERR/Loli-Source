/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontUtils;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.FontMetrics;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class PrismFontLoader
extends FontLoader {
    private static PrismFontLoader theInstance = new PrismFontLoader();
    private boolean embeddedFontsLoaded = false;
    FontFactory installedFontFactory = null;

    public static PrismFontLoader getInstance() {
        return theInstance;
    }

    Properties loadEmbeddedFontDefinitions() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL uRL = classLoader.getResource("META-INF/fonts.mf");
        if (uRL == null) {
            return properties;
        }
        try (InputStream inputStream = uRL.openStream();){
            properties.load(inputStream);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return properties;
    }

    private void loadEmbeddedFonts() {
        if (!this.embeddedFontsLoaded) {
            FontFactory fontFactory = this.getFontFactoryFromPipeline();
            if (!fontFactory.hasPermission()) {
                this.embeddedFontsLoaded = true;
                return;
            }
            Properties properties = this.loadEmbeddedFontDefinitions();
            Enumeration enumeration = properties.keys();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            while (enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                String string2 = properties.getProperty(string);
                if (!string2.startsWith("/")) continue;
                string2 = string2.substring(1);
                try {
                    InputStream inputStream = classLoader.getResourceAsStream(string2);
                    Throwable throwable = null;
                    try {
                        fontFactory.loadEmbeddedFont(string, inputStream, 0.0f, true);
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    finally {
                        if (inputStream == null) continue;
                        if (throwable != null) {
                            try {
                                inputStream.close();
                            }
                            catch (Throwable throwable3) {
                                throwable.addSuppressed(throwable3);
                            }
                            continue;
                        }
                        inputStream.close();
                    }
                }
                catch (Exception exception) {}
            }
            this.embeddedFontsLoaded = true;
        }
    }

    @Override
    public Font loadFont(InputStream inputStream, double d2) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        PGFont pGFont = fontFactory.loadEmbeddedFont(null, inputStream, (float)d2, true);
        if (pGFont != null) {
            return this.createFont(pGFont);
        }
        return null;
    }

    @Override
    public Font loadFont(String string, double d2) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        PGFont pGFont = fontFactory.loadEmbeddedFont(null, string, (float)d2, true);
        if (pGFont != null) {
            return this.createFont(pGFont);
        }
        return null;
    }

    private Font createFont(PGFont pGFont) {
        return Font.impl_NativeFont(pGFont, pGFont.getName(), pGFont.getFamilyName(), pGFont.getStyleName(), pGFont.getSize());
    }

    @Override
    public List<String> getFamilies() {
        this.loadEmbeddedFonts();
        return Arrays.asList(this.getFontFactoryFromPipeline().getFontFamilyNames());
    }

    @Override
    public List<String> getFontNames() {
        this.loadEmbeddedFonts();
        return Arrays.asList(this.getFontFactoryFromPipeline().getFontFullNames());
    }

    @Override
    public List<String> getFontNames(String string) {
        this.loadEmbeddedFonts();
        return Arrays.asList(this.getFontFactoryFromPipeline().getFontFullNames(string));
    }

    @Override
    public Font font(String string, FontWeight fontWeight, FontPosture fontPosture, float f2) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        if (!this.embeddedFontsLoaded && !fontFactory.isPlatformFont(string)) {
            this.loadEmbeddedFonts();
        }
        boolean bl = fontWeight != null && fontWeight.ordinal() >= FontWeight.BOLD.ordinal();
        boolean bl2 = fontPosture == FontPosture.ITALIC;
        PGFont pGFont = fontFactory.createFont(string, bl, bl2, f2);
        Font font = Font.impl_NativeFont(pGFont, pGFont.getName(), pGFont.getFamilyName(), pGFont.getStyleName(), f2);
        return font;
    }

    @Override
    public void loadFont(Font font) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        String string = font.getName();
        if (!this.embeddedFontsLoaded && !fontFactory.isPlatformFont(string)) {
            this.loadEmbeddedFonts();
        }
        PGFont pGFont = fontFactory.createFont(string, (float)font.getSize());
        String string2 = pGFont.getName();
        String string3 = pGFont.getFamilyName();
        String string4 = pGFont.getStyleName();
        font.impl_setNativeFont(pGFont, string2, string3, string4);
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        if (font != null) {
            PGFont pGFont = (PGFont)font.impl_getNativeFont();
            Metrics metrics = PrismFontUtils.getFontMetrics(pGFont);
            float f2 = -metrics.getAscent();
            float f3 = -metrics.getAscent();
            float f4 = metrics.getXHeight();
            float f5 = metrics.getDescent();
            float f6 = metrics.getDescent();
            float f7 = metrics.getLineGap();
            return FontMetrics.impl_createFontMetrics(f2, f3, f4, f5, f6, f7, font);
        }
        return null;
    }

    @Override
    public float computeStringWidth(String string, Font font) {
        PGFont pGFont = (PGFont)font.impl_getNativeFont();
        return (float)PrismFontUtils.computeStringWidth(pGFont, string);
    }

    @Override
    public float getSystemFontSize() {
        return PrismFontFactory.getSystemFontSize();
    }

    private FontFactory getFontFactoryFromPipeline() {
        if (this.installedFontFactory != null) {
            return this.installedFontFactory;
        }
        try {
            Class<?> class_ = Class.forName("com.sun.prism.GraphicsPipeline");
            Method method = class_.getMethod("getPipeline", null);
            Object object = method.invoke(null, new Object[0]);
            Method method2 = class_.getMethod("getFontFactory", null);
            Object object2 = method2.invoke(object, new Object[0]);
            this.installedFontFactory = (FontFactory)object2;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return this.installedFontFactory;
    }
}

