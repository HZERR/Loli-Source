/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.ScriptMapper;
import com.sun.javafx.text.TextRun;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Bidi;

public abstract class GlyphLayout {
    public static final int CANONICAL_SUBSTITUTION = 0x40000000;
    public static final int LAYOUT_LEFT_TO_RIGHT = 1;
    public static final int LAYOUT_RIGHT_TO_LEFT = 2;
    public static final int LAYOUT_NO_START_CONTEXT = 4;
    public static final int LAYOUT_NO_LIMIT_CONTEXT = 8;
    public static final int HINTING = 16;
    private static Method isIdeographicMethod = null;
    private static GlyphLayout reusableGL;
    private static boolean inUse;

    protected TextRun addTextRun(PrismTextLayout prismTextLayout, char[] arrc, int n2, int n3, PGFont pGFont, TextSpan textSpan, byte by) {
        TextRun textRun = new TextRun(n2, n3, by, true, 0, textSpan, 0, false);
        prismTextLayout.addTextRun(textRun);
        return textRun;
    }

    private TextRun addTextRun(PrismTextLayout prismTextLayout, char[] arrc, int n2, int n3, PGFont pGFont, TextSpan textSpan, byte by, boolean bl) {
        if (bl || (by & 1) != 0) {
            return this.addTextRun(prismTextLayout, arrc, n2, n3, pGFont, textSpan, by);
        }
        TextRun textRun = new TextRun(n2, n3, by, false, 0, textSpan, 0, false);
        prismTextLayout.addTextRun(textRun);
        return textRun;
    }

    public int breakRuns(PrismTextLayout prismTextLayout, char[] arrc, int n2) {
        int n3;
        int n4;
        int n5 = arrc.length;
        boolean bl = false;
        boolean bl2 = false;
        int n6 = 0;
        int n7 = 0;
        boolean bl3 = true;
        boolean bl4 = true;
        if ((n2 & 2) != 0) {
            bl3 = (n2 & 0x10) != 0;
            bl4 = (n2 & 8) != 0;
        }
        TextRun textRun = null;
        Bidi bidi = null;
        byte by = 0;
        int n8 = n5;
        int n9 = 0;
        int n10 = 0;
        TextSpan textSpan = null;
        int n11 = n5;
        PGFont pGFont = null;
        TextSpan[] arrtextSpan = prismTextLayout.getTextSpans();
        if (arrtextSpan != null) {
            if (arrtextSpan.length > 0) {
                textSpan = arrtextSpan[n10];
                n11 = textSpan.getText().length();
                pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) {
                    n2 |= 0x20;
                }
            }
        } else {
            pGFont = prismTextLayout.getFont();
        }
        if (pGFont != null) {
            FontResource fontResource = pGFont.getFontResource();
            n4 = pGFont.getFeatures();
            boolean bl5 = bl2 = (n4 & (n3 = fontResource.getFeatures())) != 0;
        }
        if (bl4 && n5 > 0) {
            int n12 = prismTextLayout.getDirection();
            bidi = new Bidi(arrc, 0, null, 0, n5, n12);
            by = (byte)bidi.getLevelAt(bidi.getRunStart(n9));
            n8 = bidi.getRunLimit(n9);
            if ((by & 1) != 0) {
                n2 |= 0x18;
            }
        }
        int n13 = 0;
        n4 = 0;
        while (n4 < n5) {
            boolean bl6;
            int n14 = n3 = arrc[n4];
            boolean bl7 = bl6 = n3 == 9 || n3 == 10 || n3 == 13;
            if (bl6 && n4 != n13) {
                textRun = this.addTextRun(prismTextLayout, arrc, n13, n4 - n13, pGFont, textSpan, by, bl);
                if (bl) {
                    n2 |= 0x10;
                    bl = false;
                }
                n13 = n4;
            }
            boolean bl8 = n4 >= n11 && n4 < n5;
            boolean bl9 = n4 >= n8 && n4 < n5;
            boolean bl10 = false;
            if (!bl6) {
                boolean bl11 = bl;
                if (bl3) {
                    if (Character.isHighSurrogate((char)n3) && n4 + 1 < n11 && Character.isLowSurrogate(arrc[n4 + 1])) {
                        n14 = Character.toCodePoint((char)n3, arrc[++n4]);
                    }
                    if (GlyphLayout.isIdeographic(n14)) {
                        n2 |= 0x40;
                    }
                    n7 = ScriptMapper.getScript(n14);
                    if (n6 > 1 && n7 > 1 && n7 != n6) {
                        bl10 = true;
                    }
                    if (!bl) {
                        boolean bl12 = bl = bl2 || ScriptMapper.isComplexCharCode(n14);
                    }
                }
                if ((bl8 || bl9 || bl10) && n13 != n4) {
                    textRun = this.addTextRun(prismTextLayout, arrc, n13, n4 - n13, pGFont, textSpan, by, bl11);
                    if (bl) {
                        n2 |= 0x10;
                        bl = false;
                    }
                    n13 = n4;
                }
                ++n4;
            }
            if (bl8) {
                textSpan = arrtextSpan[++n10];
                n11 += textSpan.getText().length();
                pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) {
                    n2 |= 0x20;
                } else {
                    int n15;
                    FontResource fontResource = pGFont.getFontResource();
                    int n16 = pGFont.getFeatures();
                    boolean bl13 = bl2 = (n16 & (n15 = fontResource.getFeatures())) != 0;
                }
            }
            if (bl9) {
                by = (byte)bidi.getLevelAt(bidi.getRunStart(++n9));
                n8 = bidi.getRunLimit(n9);
                if ((by & 1) != 0) {
                    n2 |= 0x18;
                }
            }
            if (bl10) {
                n6 = n7;
            }
            if (!bl6) continue;
            if (n3 == 13 && ++n4 < n11 && arrc[n4] == '\n') {
                ++n4;
            }
            textRun = new TextRun(n13, n4 - n13, by, false, 0, textSpan, 0, false);
            if (n3 == 9) {
                textRun.setTab();
                n2 |= 4;
            } else {
                textRun.setLinebreak();
            }
            prismTextLayout.addTextRun(textRun);
            n13 = n4;
        }
        if (n13 < n5) {
            this.addTextRun(prismTextLayout, arrc, n13, n5 - n13, pGFont, textSpan, by, bl);
            if (bl) {
                n2 |= 0x10;
            }
        } else if (textRun == null || textRun.isLinebreak()) {
            textRun = new TextRun(n13, 0, 0, false, 0, textSpan, 0, false);
            prismTextLayout.addTextRun(textRun);
        }
        if (bidi != null && !bidi.baseIsLeftToRight()) {
            n2 |= 0x100;
        }
        return n2 |= 2;
    }

    public abstract void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4);

    protected int getInitialSlot(FontResource fontResource) {
        if (PrismFontFactory.isJreFont(fontResource)) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("Avoiding JRE Font: " + fontResource.getFullName());
            }
            return 1;
        }
        return 0;
    }

    private static GlyphLayout newInstance() {
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        return prismFontFactory.createGlyphLayout();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static GlyphLayout getInstance() {
        if (inUse) {
            return GlyphLayout.newInstance();
        }
        Class<GlyphLayout> class_ = GlyphLayout.class;
        synchronized (GlyphLayout.class) {
            if (inUse) {
                // ** MonitorExit[var0] (shouldn't be in output)
                return GlyphLayout.newInstance();
            }
            inUse = true;
            // ** MonitorExit[var0] (shouldn't be in output)
            return reusableGL;
        }
    }

    public void dispose() {
        if (this == reusableGL) {
            inUse = false;
        }
    }

    private static boolean isIdeographic(int n2) {
        if (isIdeographicMethod != null) {
            try {
                return (Boolean)isIdeographicMethod.invoke(null, n2);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                return false;
            }
        }
        return false;
    }

    static {
        try {
            isIdeographicMethod = Character.class.getMethod("isIdeographic", Integer.TYPE);
        }
        catch (NoSuchMethodException | SecurityException exception) {
            isIdeographicMethod = null;
        }
        reusableGL = GlyphLayout.newInstance();
    }
}

