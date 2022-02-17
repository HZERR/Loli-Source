/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.TextLine;

public class TextRun
implements GlyphList {
    int glyphCount;
    int[] gids;
    float[] positions;
    int[] charIndices;
    int start;
    int length;
    float width = -1.0f;
    byte level;
    int script;
    TextSpan span;
    TextLine line;
    Point2D location;
    private float ascent;
    private float descent;
    private float leading;
    int flags = 0;
    int slot = 0;
    static final int FLAGS_TAB = 1;
    static final int FLAGS_LINEBREAK = 2;
    static final int FLAGS_SOFTBREAK = 4;
    static final int FLAGS_NO_LINK_BEFORE = 8;
    static final int FLAGS_NO_LINK_AFTER = 16;
    static final int FLAGS_COMPLEX = 32;
    static final int FLAGS_EMBEDDED = 64;
    static final int FLAGS_SPLIT = 128;
    static final int FLAGS_SPLIT_LAST = 256;
    static final int FLAGS_LEFT_BEARING = 512;
    static final int FLAGS_RIGHT_BEARING = 1024;
    static final int FLAGS_CANONICAL = 2048;
    static final int FLAGS_COMPACT = 4096;
    float cacheWidth = 0.0f;
    int cacheIndex = 0;

    public TextRun(int n2, int n3, byte by, boolean bl, int n4, TextSpan textSpan, int n5, boolean bl2) {
        this.start = n2;
        this.length = n3;
        this.level = by;
        this.script = n4;
        this.span = textSpan;
        this.slot = n5;
        if (bl) {
            this.flags |= 0x20;
        }
        if (bl2) {
            this.flags |= 0x800;
        }
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.start + this.length;
    }

    public int getLength() {
        return this.length;
    }

    public byte getLevel() {
        return this.level;
    }

    @Override
    public RectBounds getLineBounds() {
        return this.line.getBounds();
    }

    public void setLine(TextLine textLine) {
        this.line = textLine;
    }

    public int getScript() {
        return this.script;
    }

    @Override
    public TextSpan getTextSpan() {
        return this.span;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isLinebreak() {
        return (this.flags & 2) != 0;
    }

    public boolean isCanonical() {
        return (this.flags & 0x800) != 0;
    }

    public boolean isSoftbreak() {
        return (this.flags & 4) != 0;
    }

    public boolean isBreak() {
        return (this.flags & 6) != 0;
    }

    public boolean isTab() {
        return (this.flags & 1) != 0;
    }

    public boolean isEmbedded() {
        return (this.flags & 0x40) != 0;
    }

    public boolean isNoLinkBefore() {
        return (this.flags & 8) != 0;
    }

    public boolean isNoLinkAfter() {
        return (this.flags & 0x10) != 0;
    }

    public boolean isSplit() {
        return (this.flags & 0x80) != 0;
    }

    public boolean isSplitLast() {
        return (this.flags & 0x100) != 0;
    }

    @Override
    public boolean isComplex() {
        return (this.flags & 0x20) != 0;
    }

    public boolean isLeftBearing() {
        return (this.flags & 0x200) != 0;
    }

    public boolean isRightBearing() {
        return (this.flags & 0x400) != 0;
    }

    public boolean isLeftToRight() {
        return (this.level & 1) == 0;
    }

    public void setComplex(boolean bl) {
        this.flags = bl ? (this.flags |= 0x20) : (this.flags &= 0xFFFFFFDF);
    }

    @Override
    public float getWidth() {
        if (this.width != -1.0f) {
            return this.width;
        }
        if (this.positions != null) {
            if ((this.flags & 0x1000) != 0) {
                this.width = 0.0f;
                for (int i2 = 0; i2 < this.glyphCount; ++i2) {
                    this.width += this.positions[this.start + i2];
                }
                return this.width;
            }
            return this.positions[this.glyphCount << 1];
        }
        return 0.0f;
    }

    @Override
    public float getHeight() {
        return -this.ascent + this.descent + this.leading;
    }

    public void setWidth(float f2) {
        this.width = f2;
    }

    public void setMetrics(float f2, float f3, float f4) {
        this.ascent = f2;
        this.descent = f3;
        this.leading = f4;
    }

    public float getAscent() {
        return this.ascent;
    }

    public float getDescent() {
        return this.descent;
    }

    public float getLeading() {
        return this.leading;
    }

    public void setLocation(float f2, float f3) {
        this.location = new Point2D(f2, f3);
    }

    @Override
    public Point2D getLocation() {
        return this.location;
    }

    public void setTab() {
        this.flags |= 1;
    }

    public void setEmbedded(RectBounds rectBounds, int n2) {
        this.width = rectBounds.getWidth() * (float)n2;
        this.ascent = rectBounds.getMinY();
        this.descent = rectBounds.getHeight() + this.ascent;
        this.length = n2;
        this.flags |= 0x40;
    }

    public void setLinebreak() {
        this.flags |= 2;
    }

    public void setSoftbreak() {
        this.flags |= 4;
    }

    public void setLeftBearing() {
        this.flags |= 0x200;
    }

    public void setRightBearing() {
        this.flags |= 0x400;
    }

    public int getWrapIndex(float f2) {
        if (this.glyphCount == 0) {
            return 0;
        }
        if (this.isLeftToRight()) {
            int n2;
            if ((this.flags & 0x1000) != 0) {
                float f3 = 0.0f;
                for (n2 = 0; n2 < this.glyphCount; ++n2) {
                    if (!((f3 += this.positions[this.start + n2]) > f2)) continue;
                    return this.getCharOffset(n2);
                }
            } else {
                while (n2 < this.glyphCount) {
                    if (this.positions[n2 + 1 << 1] > f2) {
                        return this.getCharOffset(n2);
                    }
                    ++n2;
                }
            }
        } else {
            int n3 = 0;
            float f4 = this.positions[this.glyphCount << 1];
            while (f4 > f2) {
                float f5 = this.positions[n3 + 1 << 1] - this.positions[n3 << 1];
                if (f4 - f5 <= f2) {
                    return this.getCharOffset(n3);
                }
                f4 -= f5;
                ++n3;
            }
        }
        return 0;
    }

    @Override
    public int getGlyphCount() {
        return this.glyphCount;
    }

    @Override
    public int getGlyphCode(int n2) {
        if (0 <= n2 && n2 < this.glyphCount) {
            if ((this.flags & 0x1000) != 0) {
                return this.gids[this.start + n2];
            }
            return this.gids[n2];
        }
        return 65535;
    }

    @Override
    public float getPosX(int n2) {
        if (0 <= n2 && n2 <= this.glyphCount) {
            if ((this.flags & 0x1000) != 0) {
                if (this.cacheIndex == n2) {
                    return this.cacheWidth;
                }
                float f2 = 0.0f;
                if (this.cacheIndex + 1 == n2) {
                    f2 = this.cacheWidth + this.positions[this.start + n2 - 1];
                } else {
                    for (int i2 = 0; i2 < n2; ++i2) {
                        f2 += this.positions[this.start + i2];
                    }
                }
                this.cacheIndex = n2;
                this.cacheWidth = f2;
                return f2;
            }
            return this.positions[n2 << 1];
        }
        return n2 == 0 ? 0.0f : this.getWidth();
    }

    @Override
    public float getPosY(int n2) {
        if ((this.flags & 0x1000) != 0) {
            return 0.0f;
        }
        if (0 <= n2 && n2 <= this.glyphCount) {
            return this.positions[(n2 << 1) + 1];
        }
        return 0.0f;
    }

    public float getAdvance(int n2) {
        if ((this.flags & 0x1000) != 0) {
            return this.positions[this.start + n2];
        }
        return this.positions[n2 + 1 << 1] - this.positions[n2 << 1];
    }

    public void shape(int n2, int[] arrn, float[] arrf, int[] arrn2) {
        this.glyphCount = n2;
        this.gids = arrn;
        this.positions = arrf;
        this.charIndices = arrn2;
    }

    public void shape(int n2, int[] arrn, float[] arrf) {
        this.glyphCount = n2;
        this.gids = arrn;
        this.positions = arrf;
        this.charIndices = null;
        this.flags |= 0x1000;
    }

    public float getXAtOffset(int n2, boolean bl) {
        boolean bl2 = this.isLeftToRight();
        if (n2 == this.length) {
            return bl2 ? this.getWidth() : 0.0f;
        }
        if (this.glyphCount > 0) {
            int n3 = this.getGlyphIndex(n2);
            if (bl2) {
                return this.getPosX(n3 + (bl ? 0 : 1));
            }
            return this.getPosX(n3 + (bl ? 1 : 0));
        }
        if (this.isTab()) {
            if (bl2) {
                return bl ? 0.0f : this.getWidth();
            }
            return bl ? this.getWidth() : 0.0f;
        }
        return 0.0f;
    }

    public int getGlyphAtX(float f2, int[] arrn) {
        boolean bl = this.isLeftToRight();
        float f3 = 0.0f;
        for (int i2 = 0; i2 < this.glyphCount; ++i2) {
            float f4 = this.getAdvance(i2);
            if (f3 + f4 > f2) {
                if (arrn != null) {
                    arrn[0] = f2 - f3 > f4 / 2.0f ? (bl ? 1 : 0) : (bl ? 0 : 1);
                }
                return i2;
            }
            f3 += f4;
        }
        if (arrn != null) {
            arrn[0] = bl ? 1 : 0;
        }
        return Math.max(0, this.glyphCount - 1);
    }

    public int getOffsetAtX(float f2, int[] arrn) {
        if (this.glyphCount > 0) {
            int n2 = this.getGlyphAtX(f2, arrn);
            return this.getCharOffset(n2);
        }
        if (this.width != -1.0f && this.length > 0 && arrn != null && f2 > this.width / 2.0f) {
            arrn[0] = 1;
        }
        return 0;
    }

    private void reset() {
        this.positions = null;
        this.charIndices = null;
        this.gids = null;
        this.width = -1.0f;
        this.leading = 0.0f;
        this.descent = 0.0f;
        this.ascent = 0.0f;
        this.glyphCount = 0;
    }

    public TextRun split(int n2) {
        int n3 = this.length - n2;
        this.length = n2;
        boolean bl = this.isComplex();
        TextRun textRun = new TextRun(this.start + this.length, n3, this.level, bl, this.script, this.span, this.slot, this.isCanonical());
        this.flags |= 0x10;
        textRun.flags |= 8;
        this.flags |= 0x80;
        this.flags &= 0xFFFFFEFF;
        textRun.flags |= 0x100;
        textRun.setMetrics(this.ascent, this.descent, this.leading);
        if (!bl) {
            this.glyphCount = this.length;
            if ((this.flags & 0x1000) != 0) {
                textRun.shape(n3, this.gids, this.positions);
                if (this.width != -1.0f) {
                    if (n3 > this.length) {
                        float f2 = this.width;
                        this.width = -1.0f;
                        textRun.setWidth(f2 - this.getWidth());
                    } else {
                        this.width -= textRun.getWidth();
                    }
                }
            } else {
                int[] arrn = new int[n3];
                float[] arrf = new float[n3 + 1 << 1];
                System.arraycopy(this.gids, n2, arrn, 0, n3);
                float f3 = this.getWidth();
                int n4 = n2 << 1;
                for (int i2 = 2; i2 < arrf.length; i2 += 2) {
                    arrf[i2] = this.positions[i2 + n4] - f3;
                }
                textRun.shape(n3, arrn, arrf, null);
            }
        } else {
            this.reset();
        }
        return textRun;
    }

    public void merge(TextRun textRun) {
        if (textRun != null) {
            this.length += textRun.length;
            this.glyphCount += textRun.glyphCount;
            this.width = this.width != -1.0f && textRun.width != -1.0f ? (this.width += textRun.width) : -1.0f;
        }
        this.flags &= 0xFFFFFF7F;
        this.flags &= 0xFFFFFEFF;
    }

    public TextRun unwrap() {
        TextRun textRun = new TextRun(this.start, this.length, this.level, this.isComplex(), this.script, this.span, this.slot, this.isCanonical());
        textRun.shape(this.glyphCount, this.gids, this.positions);
        textRun.setWidth(this.width);
        textRun.setMetrics(this.ascent, this.descent, this.leading);
        int n2 = 28;
        textRun.flags = this.flags & ~n2;
        return textRun;
    }

    public void justify(int n2, float f2) {
        if (this.positions != null) {
            int n3 = this.getGlyphIndex(n2);
            if (n3 != -1) {
                for (int i2 = n3 + 1; i2 <= this.glyphCount; ++i2) {
                    int n4 = i2 << 1;
                    this.positions[n4] = this.positions[n4] + f2;
                }
                this.width = -1.0f;
            }
            this.setComplex(true);
        }
    }

    public int getGlyphIndex(int n2) {
        if (this.charIndices == null) {
            return n2;
        }
        for (int i2 = 0; i2 < this.charIndices.length && i2 < this.glyphCount; ++i2) {
            if (this.charIndices[i2] != n2) continue;
            return i2;
        }
        if (this.isLeftToRight()) {
            if (n2 > 0) {
                return this.getGlyphIndex(n2 - 1);
            }
        } else if (n2 + 1 < this.length) {
            return this.getGlyphIndex(n2 + 1);
        }
        return 0;
    }

    @Override
    public int getCharOffset(int n2) {
        return this.charIndices == null ? n2 : this.charIndices[n2];
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("TextRun start=");
        stringBuffer.append(this.start);
        stringBuffer.append(", length=");
        stringBuffer.append(this.length);
        stringBuffer.append(", script=");
        stringBuffer.append(this.script);
        stringBuffer.append(", linebreak=");
        stringBuffer.append(this.isLinebreak());
        stringBuffer.append(", softbreak=");
        stringBuffer.append(this.isSoftbreak());
        stringBuffer.append(", complex=");
        stringBuffer.append(this.isComplex());
        stringBuffer.append(", tab=");
        stringBuffer.append(this.isTab());
        stringBuffer.append(", compact=");
        stringBuffer.append((this.flags & 0x1000) != 0);
        stringBuffer.append(", ltr=");
        stringBuffer.append(this.isLeftToRight());
        stringBuffer.append(", split=");
        stringBuffer.append(this.isSplit());
        return stringBuffer.toString();
    }
}

