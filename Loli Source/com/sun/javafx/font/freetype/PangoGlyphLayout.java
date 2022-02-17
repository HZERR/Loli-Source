/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.freetype.OSPango;
import com.sun.javafx.font.freetype.PangoGlyphString;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;

class PangoGlyphLayout
extends GlyphLayout {
    private long str = 0L;

    PangoGlyphLayout() {
    }

    private int getSlot(PGFont pGFont, PangoGlyphString pangoGlyphString) {
        CompositeFontResource compositeFontResource = (CompositeFontResource)pGFont.getFontResource();
        long l2 = pangoGlyphString.font;
        long l3 = OSPango.pango_font_describe(l2);
        String string = OSPango.pango_font_description_get_family(l3);
        int n2 = OSPango.pango_font_description_get_style(l3);
        int n3 = OSPango.pango_font_description_get_weight(l3);
        OSPango.pango_font_description_free(l3);
        boolean bl = n3 == 700;
        boolean bl2 = n2 != 0;
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        PGFont pGFont2 = prismFontFactory.createFont(string, bl, bl2, pGFont.getSize());
        String string2 = pGFont2.getFullName();
        String string3 = compositeFontResource.getSlotResource(0).getFullName();
        int n4 = 0;
        if (!string2.equalsIgnoreCase(string3)) {
            n4 = compositeFontResource.getSlotForFont(string2);
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tFallback font= " + string2 + " slot=" + (n4 >> 24));
            }
        }
        return n4;
    }

    private boolean check(long l2, String string, long l3, long l4, long l5, long l6) {
        if (l2 != 0L) {
            return false;
        }
        if (string != null && PrismFontFactory.debugFonts) {
            System.err.println(string);
        }
        if (l6 != 0L) {
            OSPango.pango_attr_list_unref(l6);
        }
        if (l5 != 0L) {
            OSPango.pango_font_description_free(l5);
        }
        if (l4 != 0L) {
            OSPango.g_object_unref(l4);
        }
        if (l3 != 0L) {
            OSPango.g_object_unref(l3);
        }
        return true;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        long l2;
        long l3;
        long l4;
        boolean bl;
        long l5;
        FontResource fontResource = pGFont.getFontResource();
        boolean bl2 = fontResource instanceof CompositeFontResource;
        if (bl2) {
            fontResource = ((CompositeFontResource)fontResource).getSlotResource(0);
        }
        if (this.check(l5 = OSPango.pango_ft2_font_map_new(), "Failed allocating PangoFontMap.", 0L, 0L, 0L, 0L)) {
            return;
        }
        long l6 = OSPango.pango_font_map_create_context(l5);
        if (this.check(l6, "Failed allocating PangoContext.", l5, 0L, 0L, 0L)) {
            return;
        }
        boolean bl3 = bl = (textRun.getLevel() & 1) != 0;
        if (bl) {
            OSPango.pango_context_set_base_dir(l6, 1);
        }
        float f2 = pGFont.getSize();
        int n2 = fontResource.isItalic() ? 2 : 0;
        int n3 = fontResource.isBold() ? 700 : 400;
        long l7 = OSPango.pango_font_description_new();
        if (this.check(l7, "Failed allocating FontDescription.", l5, l6, 0L, 0L)) {
            return;
        }
        OSPango.pango_font_description_set_family(l7, fontResource.getFamilyName());
        OSPango.pango_font_description_set_absolute_size(l7, f2 * 1024.0f);
        OSPango.pango_font_description_set_stretch(l7, 4);
        OSPango.pango_font_description_set_style(l7, n2);
        OSPango.pango_font_description_set_weight(l7, n3);
        long l8 = OSPango.pango_attr_list_new();
        if (this.check(l8, "Failed allocating PangoAttributeList.", l5, l6, l7, 0L)) {
            return;
        }
        long l9 = OSPango.pango_attr_font_desc_new(l7);
        if (this.check(l9, "Failed allocating PangoAttribute.", l5, l6, l7, l8)) {
            return;
        }
        OSPango.pango_attr_list_insert(l8, l9);
        if (!bl2) {
            l9 = OSPango.pango_attr_fallback_new(false);
            OSPango.pango_attr_list_insert(l8, l9);
        }
        if (this.str == 0L) {
            this.str = OSPango.g_utf16_to_utf8(arrc);
            if (this.check(this.str, "Failed allocating UTF-8 buffer.", l5, l6, l7, l8)) {
                return;
            }
        }
        if ((l4 = OSPango.pango_itemize(l6, this.str, (int)((l3 = OSPango.g_utf8_offset_to_pointer(this.str, textRun.getStart())) - this.str), (int)((l2 = OSPango.g_utf8_offset_to_pointer(this.str, textRun.getEnd())) - l3), l8, 0L)) != 0L) {
            int n4;
            int n5 = OSPango.g_list_length(l4);
            PangoGlyphString[] arrpangoGlyphString = new PangoGlyphString[n5];
            for (n4 = 0; n4 < n5; ++n4) {
                long l10 = OSPango.g_list_nth_data(l4, n4);
                if (l10 == 0L) continue;
                arrpangoGlyphString[n4] = OSPango.pango_shape(this.str, l10);
                OSPango.pango_item_free(l10);
            }
            OSPango.g_list_free(l4);
            n4 = 0;
            for (PangoGlyphString n6 : arrpangoGlyphString) {
                if (n6 == null) continue;
                n4 += n6.num_glyphs;
            }
            int[] arrn = new int[n4];
            float[] arrf = new float[n4 * 2 + 2];
            int[] arrn2 = new int[n4];
            int n6 = 0;
            int n7 = bl ? textRun.getLength() : 0;
            int n8 = 0;
            for (PangoGlyphString pangoGlyphString : arrpangoGlyphString) {
                int n9;
                if (pangoGlyphString == null) continue;
                int n10 = n9 = bl2 ? this.getSlot(pGFont, pangoGlyphString) : 0;
                if (bl) {
                    n7 -= pangoGlyphString.num_chars;
                }
                for (int i2 = 0; i2 < pangoGlyphString.num_glyphs; ++i2) {
                    int n11;
                    int n12 = n6 + i2;
                    if (n9 != -1 && 0 <= (n11 = pangoGlyphString.glyphs[i2]) && n11 <= 0xFFFFFF) {
                        arrn[n12] = n9 << 24 | n11;
                    }
                    if (f2 != 0.0f) {
                        arrf[2 + (n12 << 1)] = (float)(n8 += pangoGlyphString.widths[i2]) / 1024.0f;
                    }
                    arrn2[n12] = pangoGlyphString.log_clusters[i2] + n7;
                }
                if (!bl) {
                    n7 += pangoGlyphString.num_chars;
                }
                n6 += pangoGlyphString.num_glyphs;
            }
            textRun.shape(n4, arrn, arrf, arrn2);
        }
        this.check(0L, null, l5, l6, l7, l8);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.str != 0L) {
            OSPango.g_free(this.str);
            this.str = 0L;
        }
    }
}

