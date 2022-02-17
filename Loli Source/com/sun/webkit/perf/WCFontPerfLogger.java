/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.perf;

import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGlyphBuffer;
import com.sun.webkit.perf.PerfLogger;
import java.util.logging.Logger;

public final class WCFontPerfLogger
extends WCFont {
    private static final Logger log = Logger.getLogger(WCFontPerfLogger.class.getName());
    private static final PerfLogger logger = PerfLogger.getLogger(log);
    private final WCFont fnt;

    public WCFontPerfLogger(WCFont wCFont) {
        this.fnt = wCFont;
    }

    public static synchronized boolean isEnabled() {
        return logger.isEnabled();
    }

    public static void log() {
        logger.log();
    }

    public static void reset() {
        logger.reset();
    }

    @Override
    public Object getPlatformFont() {
        return this.fnt.getPlatformFont();
    }

    @Override
    public WCFont deriveFont(float f2) {
        logger.resumeCount("DERIVEFONT");
        WCFont wCFont = this.fnt.deriveFont(f2);
        logger.suspendCount("DERIVEFONT");
        return wCFont;
    }

    @Override
    public int getOffsetForPosition(String string, float f2) {
        logger.resumeCount("GETOFFSETFORPOSITION");
        int n2 = this.fnt.getOffsetForPosition(string, f2);
        logger.suspendCount("GETOFFSETFORPOSITION");
        return n2;
    }

    @Override
    public WCGlyphBuffer getGlyphsAndAdvances(String string, int n2, int n3, boolean bl) {
        logger.resumeCount("GETGLYPHSANDADVANCESFORCOMPLEXTEXT");
        WCGlyphBuffer wCGlyphBuffer = this.fnt.getGlyphsAndAdvances(string, n2, n3, bl);
        logger.suspendCount("GETGLYPHSANDADVANCESFORCOMPLEXTEXT");
        return wCGlyphBuffer;
    }

    @Override
    public int[] getGlyphCodes(char[] arrc) {
        logger.resumeCount("GETGLYPHCODES");
        int[] arrn = this.fnt.getGlyphCodes(arrc);
        logger.suspendCount("GETGLYPHCODES");
        return arrn;
    }

    @Override
    public float getXHeight() {
        logger.resumeCount("GETXHEIGHT");
        float f2 = this.fnt.getXHeight();
        logger.suspendCount("GETXHEIGHT");
        return f2;
    }

    @Override
    public double getGlyphWidth(int n2) {
        logger.resumeCount("GETGLYPHWIDTH");
        double d2 = this.fnt.getGlyphWidth(n2);
        logger.suspendCount("GETGLYPHWIDTH");
        return d2;
    }

    @Override
    public double getStringWidth(String string) {
        logger.resumeCount("GETSTRINGLENGTH");
        double d2 = this.fnt.getStringWidth(string);
        logger.suspendCount("GETSTRINGLENGTH");
        return d2;
    }

    @Override
    public double[] getStringBounds(String string, int n2, int n3, boolean bl) {
        logger.resumeCount("GETSTRINGBOUNDS");
        double[] arrd = this.fnt.getStringBounds(string, n2, n3, bl);
        logger.suspendCount("GETSTRINGBOUNDS");
        return arrd;
    }

    @Override
    public int hashCode() {
        logger.resumeCount("HASH");
        int n2 = this.fnt.hashCode();
        logger.suspendCount("HASH");
        return n2;
    }

    @Override
    public boolean equals(Object object) {
        logger.resumeCount("COMPARE");
        boolean bl = this.fnt.equals(object);
        logger.suspendCount("COMPARE");
        return bl;
    }

    @Override
    public float getAscent() {
        logger.resumeCount("GETASCENT");
        float f2 = this.fnt.getAscent();
        logger.suspendCount("GETASCENT");
        return f2;
    }

    @Override
    public float getDescent() {
        logger.resumeCount("GETDESCENT");
        float f2 = this.fnt.getDescent();
        logger.suspendCount("GETDESCENT");
        return f2;
    }

    @Override
    public float getLineSpacing() {
        logger.resumeCount("GETLINESPACING");
        float f2 = this.fnt.getLineSpacing();
        logger.suspendCount("GETLINESPACING");
        return f2;
    }

    @Override
    public float getLineGap() {
        logger.resumeCount("GETLINEGAP");
        float f2 = this.fnt.getLineGap();
        logger.suspendCount("GETLINEGAP");
        return f2;
    }

    @Override
    public boolean hasUniformLineMetrics() {
        logger.resumeCount("HASUNIFORMLINEMETRICS");
        boolean bl = this.fnt.hasUniformLineMetrics();
        logger.suspendCount("HASUNIFORMLINEMETRICS");
        return bl;
    }

    @Override
    public float getCapHeight() {
        logger.resumeCount("GETCAPHEIGHT");
        float f2 = this.fnt.getCapHeight();
        logger.suspendCount("GETCAPHEIGHT");
        return f2;
    }
}

