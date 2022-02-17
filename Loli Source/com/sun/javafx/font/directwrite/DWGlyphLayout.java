/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.directwrite.DWFactory;
import com.sun.javafx.font.directwrite.DWFontFile;
import com.sun.javafx.font.directwrite.DWRITE_SCRIPT_ANALYSIS;
import com.sun.javafx.font.directwrite.IDWriteFactory;
import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontCollection;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IDWriteLocalizedStrings;
import com.sun.javafx.font.directwrite.IDWriteTextAnalyzer;
import com.sun.javafx.font.directwrite.IDWriteTextFormat;
import com.sun.javafx.font.directwrite.IDWriteTextLayout;
import com.sun.javafx.font.directwrite.JFXTextAnalysisSink;
import com.sun.javafx.font.directwrite.JFXTextRenderer;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;

public class DWGlyphLayout
extends GlyphLayout {
    private static final String LOCALE = "en-us";

    @Override
    protected TextRun addTextRun(PrismTextLayout prismTextLayout, char[] arrc, int n2, int n3, PGFont pGFont, TextSpan textSpan, byte by) {
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteTextAnalyzer iDWriteTextAnalyzer = iDWriteFactory.CreateTextAnalyzer();
        if (iDWriteTextAnalyzer == null) {
            return new TextRun(n2, n3, by, false, 0, textSpan, 0, false);
        }
        int n4 = (by & 1) != 0 ? 1 : 0;
        JFXTextAnalysisSink jFXTextAnalysisSink = OS.NewJFXTextAnalysisSink(arrc, n2, n3, LOCALE, n4);
        if (jFXTextAnalysisSink == null) {
            return new TextRun(n2, n3, by, false, 0, textSpan, 0, false);
        }
        jFXTextAnalysisSink.AddRef();
        TextRun textRun = null;
        int n5 = iDWriteTextAnalyzer.AnalyzeScript(jFXTextAnalysisSink, 0, n3, jFXTextAnalysisSink);
        if (n5 == 0) {
            while (jFXTextAnalysisSink.Next()) {
                int n6 = jFXTextAnalysisSink.GetStart();
                int n7 = jFXTextAnalysisSink.GetLength();
                DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS = jFXTextAnalysisSink.GetAnalysis();
                textRun = new TextRun(n2 + n6, n7, by, true, dWRITE_SCRIPT_ANALYSIS.script, textSpan, dWRITE_SCRIPT_ANALYSIS.shapes, false);
                prismTextLayout.addTextRun(textRun);
            }
        }
        iDWriteTextAnalyzer.Release();
        jFXTextAnalysisSink.Release();
        return textRun;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        int n2;
        IDWriteFontFace iDWriteFontFace;
        int n3 = 0;
        FontResource fontResource = pGFont.getFontResource();
        boolean bl = fontResource instanceof CompositeFontResource;
        if (bl) {
            n3 = this.getInitialSlot(fontResource);
            fontResource = ((CompositeFontResource)fontResource).getSlotResource(n3);
        }
        if ((iDWriteFontFace = ((DWFontFile)fontResource).getFontFace()) == null) {
            return;
        }
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteTextAnalyzer iDWriteTextAnalyzer = iDWriteFactory.CreateTextAnalyzer();
        if (iDWriteTextAnalyzer == null) {
            return;
        }
        long[] arrl = null;
        int[] arrn = null;
        int n4 = 0;
        int n5 = textRun.getLength();
        short[] arrs = new short[n5];
        short[] arrs2 = new short[n5];
        int n6 = n5 * 3 / 2 + 16;
        short[] arrs3 = new short[n6];
        short[] arrs4 = new short[n6];
        int[] arrn2 = new int[1];
        boolean bl2 = !textRun.isLeftToRight();
        DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS = new DWRITE_SCRIPT_ANALYSIS();
        dWRITE_SCRIPT_ANALYSIS.script = (short)textRun.getScript();
        dWRITE_SCRIPT_ANALYSIS.shapes = textRun.getSlot();
        int n7 = textRun.getStart();
        int n8 = iDWriteTextAnalyzer.GetGlyphs(arrc, n7, n5, iDWriteFontFace, false, bl2, dWRITE_SCRIPT_ANALYSIS, null, 0L, arrl, arrn, n4, n6, arrs, arrs2, arrs3, arrs4, arrn2);
        if (n8 == -2147024774) {
            arrs3 = new short[n6 *= 2];
            arrs4 = new short[n6];
            n8 = iDWriteTextAnalyzer.GetGlyphs(arrc, n7, n5, iDWriteFontFace, false, bl2, dWRITE_SCRIPT_ANALYSIS, null, 0L, arrl, arrn, n4, n6, arrs, arrs2, arrs3, arrs4, arrn2);
        }
        if (n8 != 0) {
            iDWriteTextAnalyzer.Release();
            return;
        }
        int n9 = arrn2[0];
        int n10 = bl2 ? -1 : 1;
        int[] arrn3 = new int[n9];
        int n11 = n3 << 24;
        boolean bl3 = false;
        int n12 = 0;
        int n13 = n2 = bl2 ? n9 - 1 : 0;
        while (n12 < n9) {
            if (arrs3[n12] == 0) {
                bl3 = true;
                if (bl) break;
            }
            arrn3[n12] = arrs3[n2] | n11;
            ++n12;
            n2 += n10;
        }
        if (bl3 && bl) {
            iDWriteTextAnalyzer.Release();
            this.renderShape(arrc, textRun, pGFont, n3);
            return;
        }
        float f2 = pGFont.getSize();
        float[] arrf = new float[n9];
        float[] arrf2 = new float[n9 * 2];
        iDWriteTextAnalyzer.GetGlyphPlacements(arrc, arrs, arrs2, n7, n5, arrs3, arrs4, n9, iDWriteFontFace, f2, false, bl2, dWRITE_SCRIPT_ANALYSIS, null, arrl, arrn, n4, arrf, arrf2);
        iDWriteTextAnalyzer.Release();
        float[] arrf3 = this.getPositions(arrf, arrf2, n9, bl2);
        int[] arrn4 = this.getIndices(arrs, n9, bl2);
        textRun.shape(n9, arrn3, arrf3, arrn4);
    }

    private float[] getPositions(float[] arrf, float[] arrf2, int n2, boolean bl) {
        float[] arrf3 = new float[n2 * 2 + 2];
        int n3 = 0;
        int n4 = bl ? n2 - 1 : 0;
        int n5 = bl ? -1 : 1;
        float f2 = 0.0f;
        while (n3 < arrf3.length - 2) {
            int n6 = n4 << 1;
            arrf3[n3++] = (bl ? -arrf2[n6] : arrf2[n6]) + f2;
            arrf3[n3++] = -arrf2[n6 + 1];
            f2 += arrf[n4];
            n4 += n5;
        }
        arrf3[n3++] = f2;
        arrf3[n3++] = 0.0f;
        return arrf3;
    }

    private int[] getIndices(short[] arrs, int n2, boolean bl) {
        int n3;
        int n4;
        int[] arrn = new int[n2];
        Arrays.fill(arrn, -1);
        for (n4 = 0; n4 < arrs.length; ++n4) {
            n3 = arrs[n4];
            if (0 > n3 || n3 >= n2 || arrn[n3] != -1) continue;
            arrn[n3] = n4;
        }
        if (arrn.length > 0) {
            if (arrn[0] == -1) {
                arrn[0] = 0;
            }
            for (n4 = 1; n4 < arrn.length; ++n4) {
                if (arrn[n4] != -1) continue;
                arrn[n4] = arrn[n4 - 1];
            }
        }
        if (bl) {
            for (n4 = 0; n4 < arrn.length / 2; ++n4) {
                n3 = arrn[n4];
                arrn[n4] = arrn[arrn.length - n4 - 1];
                arrn[arrn.length - n4 - 1] = n3;
            }
        }
        return arrn;
    }

    private String getName(IDWriteLocalizedStrings iDWriteLocalizedStrings) {
        if (iDWriteLocalizedStrings == null) {
            return null;
        }
        int n2 = iDWriteLocalizedStrings.FindLocaleName(LOCALE);
        String string = null;
        if (n2 >= 0) {
            int n3 = iDWriteLocalizedStrings.GetStringLength(n2);
            string = iDWriteLocalizedStrings.GetString(n2, n3);
        }
        iDWriteLocalizedStrings.Release();
        return string;
    }

    private FontResource checkFontResource(FontResource fontResource, String string, String string2) {
        if (fontResource == null) {
            return null;
        }
        if (string != null && string.equals(fontResource.getPSName())) {
            return fontResource;
        }
        if (string2 != null) {
            if (string2.equals(fontResource.getFullName())) {
                return fontResource;
            }
            String string3 = fontResource.getFamilyName() + " " + fontResource.getStyleName();
            if (string2.equals(string3)) {
                return fontResource;
            }
        }
        return null;
    }

    private int getFontSlot(IDWriteFontFace iDWriteFontFace, CompositeFontResource compositeFontResource, String string, int n2) {
        Object object;
        if (iDWriteFontFace == null) {
            return -1;
        }
        IDWriteFontCollection iDWriteFontCollection = DWFactory.getFontCollection();
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        IDWriteFont iDWriteFont = iDWriteFontCollection.GetFontFromFontFace(iDWriteFontFace);
        if (iDWriteFont == null) {
            return -1;
        }
        IDWriteFontFamily iDWriteFontFamily = iDWriteFont.GetFontFamily();
        String string2 = this.getName(iDWriteFontFamily.GetFamilyNames());
        iDWriteFontFamily.Release();
        boolean bl = iDWriteFont.GetStyle() != 0;
        boolean bl2 = iDWriteFont.GetWeight() > 400;
        int n3 = iDWriteFont.GetSimulations();
        int n4 = 17;
        String string3 = this.getName(iDWriteFont.GetInformationalStrings(n4));
        n4 = 11;
        String string4 = this.getName(iDWriteFont.GetInformationalStrings(n4));
        n4 = 12;
        String string5 = this.getName(iDWriteFont.GetInformationalStrings(n4));
        String string6 = string4 + " " + string5;
        if (PrismFontFactory.debugFonts) {
            object = this.getName(iDWriteFont.GetFaceNames());
            System.err.println("Mapping IDWriteFont=\"" + string2 + " " + (String)object + "\" Postscript name=\"" + string3 + "\" Win32 name=\"" + string6 + "\"");
        }
        iDWriteFont.Release();
        object = prismFontFactory.getFontResource(string2, bl2, bl, false);
        object = this.checkFontResource((FontResource)object, string3, string6);
        if (object == null) {
            object = prismFontFactory.getFontResource(string2, bl2 &= (n3 & 1) == 0, bl &= (n3 & 2) == 0, false);
            object = this.checkFontResource((FontResource)object, string3, string6);
        }
        if (object == null) {
            object = prismFontFactory.getFontResource(string6, null, false);
            object = this.checkFontResource((FontResource)object, string3, string6);
        }
        if (object == null) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\t**** Failed to map IDWriteFont to Prism ****");
            }
            return -1;
        }
        String string7 = object.getFullName();
        if (!string.equalsIgnoreCase(string7)) {
            n2 = compositeFontResource.getSlotForFont(string7);
        }
        if (PrismFontFactory.debugFonts) {
            System.err.println("\tFallback full name=\"" + string7 + "\" Postscript name=\"" + object.getPSName() + "\" Style name=\"" + object.getStyleName() + "\" slot=" + n2);
        }
        return n2;
    }

    private void renderShape(char[] arrc, TextRun textRun, PGFont pGFont, int n2) {
        int n3;
        IDWriteFontCollection iDWriteFontCollection;
        CompositeFontResource compositeFontResource = (CompositeFontResource)pGFont.getFontResource();
        FontResource fontResource = compositeFontResource.getSlotResource(n2);
        String string = fontResource.getFamilyName();
        String string2 = fontResource.getFullName();
        int n4 = fontResource.isBold() ? 700 : 400;
        int n5 = 5;
        int n6 = fontResource.isItalic() ? 2 : 0;
        float f2 = pGFont.getSize();
        float f3 = f2 > 0.0f ? f2 : 1.0f;
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteTextFormat iDWriteTextFormat = iDWriteFactory.CreateTextFormat(string, iDWriteFontCollection = DWFactory.getFontCollection(), n4, n6, n5, f3, LOCALE);
        if (iDWriteTextFormat == null) {
            return;
        }
        int n7 = textRun.getStart();
        IDWriteTextLayout iDWriteTextLayout = iDWriteFactory.CreateTextLayout(arrc, n7, n3 = textRun.getLength(), iDWriteTextFormat, 100000.0f, 100000.0f);
        if (iDWriteTextLayout != null) {
            JFXTextRenderer jFXTextRenderer = OS.NewJFXTextRenderer();
            if (jFXTextRenderer != null) {
                boolean bl;
                int n8;
                jFXTextRenderer.AddRef();
                iDWriteTextLayout.Draw(0L, jFXTextRenderer, 0.0f, 0.0f);
                int n9 = jFXTextRenderer.GetTotalGlyphCount();
                int[] arrn = new int[n9];
                float[] arrf = new float[n9];
                float[] arrf2 = new float[n9 * 2];
                short[] arrs = new short[n3];
                int n10 = 0;
                int n11 = 0;
                while (jFXTextRenderer.Next()) {
                    IDWriteFontFace iDWriteFontFace = jFXTextRenderer.GetFontFace();
                    n8 = this.getFontSlot(iDWriteFontFace, compositeFontResource, string2, n2);
                    if (n8 >= 0) {
                        jFXTextRenderer.GetGlyphIndices(arrn, n10, n8 << 24);
                        jFXTextRenderer.GetGlyphOffsets(arrf2, n10 * 2);
                    }
                    if (f2 > 0.0f) {
                        jFXTextRenderer.GetGlyphAdvances(arrf, n10);
                    }
                    jFXTextRenderer.GetClusterMap(arrs, n11, n10);
                    n10 += jFXTextRenderer.GetGlyphCount();
                    n11 += jFXTextRenderer.GetLength();
                }
                jFXTextRenderer.Release();
                boolean bl2 = bl = !textRun.isLeftToRight();
                if (bl) {
                    for (n8 = 0; n8 < n9 / 2; ++n8) {
                        int n12 = arrn[n8];
                        arrn[n8] = arrn[n9 - n8 - 1];
                        arrn[n9 - n8 - 1] = n12;
                    }
                }
                float[] arrf3 = this.getPositions(arrf, arrf2, n9, bl);
                int[] arrn2 = this.getIndices(arrs, n9, bl);
                textRun.shape(n9, arrn, arrf3, arrn2);
            }
            iDWriteTextLayout.Release();
        }
        iDWriteTextFormat.Release();
    }
}

