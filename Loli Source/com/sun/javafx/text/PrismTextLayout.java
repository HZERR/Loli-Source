/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.CharArrayIterator;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.LayoutCache;
import com.sun.javafx.text.TextLine;
import com.sun.javafx.text.TextRun;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class PrismTextLayout
implements TextLayout {
    private static final BaseTransform IDENTITY = BaseTransform.IDENTITY_TRANSFORM;
    private static final int X_MIN_INDEX = 0;
    private static final int Y_MIN_INDEX = 1;
    private static final int X_MAX_INDEX = 2;
    private static final int Y_MAX_INDEX = 3;
    private static final Hashtable<Integer, LayoutCache> stringCache = new Hashtable();
    private static final Object CACHE_SIZE_LOCK = new Object();
    private static int cacheSize = 0;
    private static final int MAX_STRING_SIZE = 256;
    private static final int MAX_CACHE_SIZE = PrismFontFactory.cacheLayoutSize;
    private char[] text;
    private TextSpan[] spans;
    private PGFont font;
    private FontStrike strike;
    private Integer cacheKey;
    private TextLine[] lines;
    private TextRun[] runs;
    private int runCount;
    private BaseBounds logicalBounds = new RectBounds();
    private RectBounds visualBounds;
    private float layoutWidth;
    private float layoutHeight;
    private float wrapWidth;
    private float spacing;
    private LayoutCache layoutCache;
    private Shape shape;
    private int flags = 262144;

    private void reset() {
        this.layoutCache = null;
        this.runs = null;
        this.flags &= 0xFFFFF800;
        this.relayout();
    }

    private void relayout() {
        this.logicalBounds.makeEmpty();
        this.visualBounds = null;
        this.layoutHeight = 0.0f;
        this.layoutWidth = 0.0f;
        this.flags &= 0xFFFFF97F;
        this.lines = null;
        this.shape = null;
    }

    @Override
    public boolean setContent(TextSpan[] arrtextSpan) {
        if (arrtextSpan == null && this.spans == null) {
            return false;
        }
        if (arrtextSpan != null && this.spans != null && arrtextSpan.length == this.spans.length) {
            int n2;
            for (n2 = 0; n2 < arrtextSpan.length && arrtextSpan[n2] == this.spans[n2]; ++n2) {
            }
            if (n2 == arrtextSpan.length) {
                return false;
            }
        }
        this.reset();
        this.spans = arrtextSpan;
        this.font = null;
        this.strike = null;
        this.text = null;
        this.cacheKey = null;
        return true;
    }

    @Override
    public boolean setContent(String string, Object object) {
        int n2;
        this.reset();
        this.spans = null;
        this.font = (PGFont)object;
        this.strike = ((PGFont)object).getStrike(IDENTITY);
        this.text = string.toCharArray();
        if (MAX_CACHE_SIZE > 0 && 0 < (n2 = string.length()) && n2 <= 256) {
            this.cacheKey = string.hashCode() * this.strike.hashCode();
        }
        return true;
    }

    @Override
    public boolean setDirection(int n2) {
        if ((this.flags & 0x3C00) == n2) {
            return false;
        }
        this.flags &= 0xFFFFC3FF;
        this.flags |= n2 & 0x3C00;
        this.reset();
        return true;
    }

    @Override
    public boolean setBoundsType(int n2) {
        if ((this.flags & 0x4000) == n2) {
            return false;
        }
        this.flags &= 0xFFFFBFFF;
        this.flags |= n2 & 0x4000;
        this.reset();
        return true;
    }

    @Override
    public boolean setAlignment(int n2) {
        int n3 = 262144;
        switch (n2) {
            case 0: {
                n3 = 262144;
                break;
            }
            case 1: {
                n3 = 524288;
                break;
            }
            case 2: {
                n3 = 0x100000;
                break;
            }
            case 3: {
                n3 = 0x200000;
            }
        }
        if ((this.flags & 0x3C0000) == n3) {
            return false;
        }
        if (n3 == 0x200000 || (this.flags & 0x200000) != 0) {
            this.reset();
        }
        this.flags &= 0xFFC3FFFF;
        this.flags |= n3;
        this.relayout();
        return true;
    }

    @Override
    public boolean setWrapWidth(float f2) {
        if (Float.isInfinite(f2)) {
            f2 = 0.0f;
        }
        if (Float.isNaN(f2)) {
            f2 = 0.0f;
        }
        float f3 = this.wrapWidth;
        this.wrapWidth = Math.max(0.0f, f2);
        boolean bl = true;
        if (this.lines != null && f3 != 0.0f && f2 != 0.0f && (this.flags & 0x40000) != 0) {
            if (f2 > f3) {
                if ((this.flags & 0x80) == 0) {
                    bl = false;
                }
            } else if (f2 >= this.layoutWidth) {
                bl = false;
            }
        }
        if (bl) {
            this.relayout();
        }
        return bl;
    }

    @Override
    public boolean setLineSpacing(float f2) {
        if (this.spacing == f2) {
            return false;
        }
        this.spacing = f2;
        this.relayout();
        return true;
    }

    private void ensureLayout() {
        if (this.lines == null) {
            this.layout();
        }
    }

    @Override
    public com.sun.javafx.scene.text.TextLine[] getLines() {
        this.ensureLayout();
        return this.lines;
    }

    @Override
    public GlyphList[] getRuns() {
        this.ensureLayout();
        GlyphList[] arrglyphList = new GlyphList[this.runCount];
        int n2 = 0;
        for (int i2 = 0; i2 < this.lines.length; ++i2) {
            TextRun[] arrtextRun = this.lines[i2].getRuns();
            int n3 = arrtextRun.length;
            System.arraycopy(arrtextRun, 0, arrglyphList, n2, n3);
            n2 += n3;
        }
        return arrglyphList;
    }

    @Override
    public BaseBounds getBounds() {
        this.ensureLayout();
        return this.logicalBounds;
    }

    @Override
    public BaseBounds getBounds(TextSpan textSpan, BaseBounds baseBounds) {
        this.ensureLayout();
        float f2 = Float.POSITIVE_INFINITY;
        float f3 = Float.POSITIVE_INFINITY;
        float f4 = Float.NEGATIVE_INFINITY;
        float f5 = Float.NEGATIVE_INFINITY;
        if (textSpan != null) {
            for (int i2 = 0; i2 < this.lines.length; ++i2) {
                TextLine textLine = this.lines[i2];
                TextRun[] arrtextRun = textLine.getRuns();
                for (int i3 = 0; i3 < arrtextRun.length; ++i3) {
                    TextRun textRun = arrtextRun[i3];
                    TextSpan textSpan2 = textRun.getTextSpan();
                    if (textSpan2 != textSpan) continue;
                    Point2D point2D = textRun.getLocation();
                    float f6 = point2D.x;
                    if (textRun.isLeftBearing()) {
                        f6 += textLine.getLeftSideBearing();
                    }
                    float f7 = point2D.x + textRun.getWidth();
                    if (textRun.isRightBearing()) {
                        f7 += textLine.getRightSideBearing();
                    }
                    float f8 = point2D.y;
                    float f9 = point2D.y + textLine.getBounds().getHeight() + this.spacing;
                    if (f6 < f2) {
                        f2 = f6;
                    }
                    if (f8 < f3) {
                        f3 = f8;
                    }
                    if (f7 > f4) {
                        f4 = f7;
                    }
                    if (!(f9 > f5)) continue;
                    f5 = f9;
                }
            }
        } else {
            f5 = 0.0f;
            f3 = 0.0f;
            for (int i4 = 0; i4 < this.lines.length; ++i4) {
                float f10;
                TextLine textLine = this.lines[i4];
                RectBounds rectBounds = textLine.getBounds();
                float f11 = rectBounds.getMinX() + textLine.getLeftSideBearing();
                if (f11 < f2) {
                    f2 = f11;
                }
                if ((f10 = rectBounds.getMaxX() + textLine.getRightSideBearing()) > f4) {
                    f4 = f10;
                }
                f5 += rectBounds.getHeight();
            }
            if (this.isMirrored()) {
                float f12 = this.getMirroringWidth();
                float f13 = f2;
                f2 = f12 - f4;
                f4 = f12 - f13;
            }
        }
        return baseBounds.deriveWithNewBounds(f2, f3, 0.0f, f4, f5, 0.0f);
    }

    @Override
    public PathElement[] getCaretShape(int n2, boolean bl, float f2, float f3) {
        Point2D point2D;
        int n3;
        int n4;
        TextLine textLine;
        int n5;
        int n6;
        this.ensureLayout();
        int n7 = this.getLineCount();
        for (n6 = 0; n6 < n7 - 1 && (n5 = (textLine = this.lines[n6]).getStart() + textLine.getLength()) <= n2; ++n6) {
        }
        int n8 = -1;
        n5 = 0;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        TextLine textLine2 = this.lines[n6];
        TextRun[] arrtextRun = textLine2.getRuns();
        int n9 = arrtextRun.length;
        int n10 = -1;
        for (n4 = 0; n4 < n9; ++n4) {
            TextRun textRun = arrtextRun[n4];
            int n11 = textRun.getStart();
            n3 = textRun.getEnd();
            if (n11 > n2 || n2 >= n3) continue;
            if (textRun.isLinebreak()) break;
            n10 = n4;
            break;
        }
        if (n10 != -1) {
            TextRun textRun = arrtextRun[n10];
            int n12 = textRun.getStart();
            point2D = textRun.getLocation();
            f4 = point2D.x + textRun.getXAtOffset(n2 - n12, bl);
            f5 = point2D.y;
            f6 = textLine2.getBounds().getHeight();
            if (bl) {
                if (n10 > 0 && n2 == n12) {
                    n5 = textRun.getLevel();
                    n8 = n2 - 1;
                }
            } else {
                n3 = textRun.getEnd();
                if (n10 + 1 < arrtextRun.length && n2 + 1 == n3) {
                    n5 = textRun.getLevel();
                    n8 = n2 + 1;
                }
            }
        } else {
            n4 = 0;
            n10 = 0;
            for (int i2 = 0; i2 < n9; ++i2) {
                TextRun textRun = arrtextRun[i2];
                if (textRun.getStart() < n4 || textRun.isLinebreak()) continue;
                n4 = textRun.getStart();
                n10 = i2;
            }
            TextRun textRun = arrtextRun[n10];
            point2D = textRun.getLocation();
            f4 = point2D.x + (textRun.isLeftToRight() ? textRun.getWidth() : 0.0f);
            f5 = point2D.y;
            f6 = textLine2.getBounds().getHeight();
        }
        if (this.isMirrored()) {
            f4 = this.getMirroringWidth() - f4;
        }
        f4 += f2;
        f5 += f3;
        if (n8 != -1) {
            for (int i3 = 0; i3 < arrtextRun.length; ++i3) {
                TextRun textRun = arrtextRun[i3];
                int n13 = textRun.getStart();
                n3 = textRun.getEnd();
                if (n13 > n8 || n8 >= n3 || (textRun.getLevel() & 1) == (n5 & 1)) continue;
                Point2D point2D2 = textRun.getLocation();
                float f7 = point2D2.x;
                if (bl) {
                    if ((n5 & 1) != 0) {
                        f7 += textRun.getWidth();
                    }
                } else if ((n5 & 1) == 0) {
                    f7 += textRun.getWidth();
                }
                if (this.isMirrored()) {
                    f7 = this.getMirroringWidth() - f7;
                }
                PathElement[] arrpathElement = new PathElement[]{new MoveTo(f4, f5), new LineTo(f4, f5 + f6 / 2.0f), new MoveTo(f7 += f2, f5 + f6 / 2.0f), new LineTo(f7, f5 + f6)};
                return arrpathElement;
            }
        }
        PathElement[] arrpathElement = new PathElement[]{new MoveTo(f4, f5), new LineTo(f4, f5 + f6)};
        return arrpathElement;
    }

    @Override
    public HitInfo getHitInfo(float f2, float f3) {
        this.ensureLayout();
        HitInfo hitInfo = new HitInfo();
        int n2 = this.getLineIndex(f3);
        if (n2 >= this.getLineCount()) {
            hitInfo.setCharIndex(this.getCharCount());
        } else {
            if (this.isMirrored()) {
                f2 = this.getMirroringWidth() - f2;
            }
            TextLine textLine = this.lines[n2];
            TextRun[] arrtextRun = textLine.getRuns();
            RectBounds rectBounds = textLine.getBounds();
            TextRun textRun = null;
            f2 -= rectBounds.getMinX();
            for (int i2 = 0; i2 < arrtextRun.length && !(f2 < (textRun = arrtextRun[i2]).getWidth()); ++i2) {
                if (i2 + 1 >= arrtextRun.length) continue;
                if (arrtextRun[i2 + 1].isLinebreak()) break;
                f2 -= textRun.getWidth();
            }
            if (textRun != null) {
                int[] arrn = new int[1];
                hitInfo.setCharIndex(textRun.getStart() + textRun.getOffsetAtX(f2, arrn));
                hitInfo.setLeading(arrn[0] == 0);
            } else {
                hitInfo.setCharIndex(textLine.getStart());
                hitInfo.setLeading(true);
            }
        }
        return hitInfo;
    }

    @Override
    public PathElement[] getRange(int n2, int n3, int n4, float f2, float f3) {
        this.ensureLayout();
        int n5 = this.getLineCount();
        ArrayList<PathElement> arrayList = new ArrayList<PathElement>();
        float f4 = 0.0f;
        for (int i2 = 0; i2 < n5; ++i2) {
            TextLine textLine = this.lines[i2];
            RectBounds rectBounds = textLine.getBounds();
            int n6 = textLine.getStart();
            if (n6 >= n3) break;
            int n7 = n6 + textLine.getLength();
            if (n2 > n7) {
                f4 += rectBounds.getHeight() + this.spacing;
                continue;
            }
            TextRun[] arrtextRun = textLine.getRuns();
            int n8 = Math.min(n7, n3) - Math.max(n6, n2);
            float f5 = -1.0f;
            float f6 = -1.0f;
            float f7 = rectBounds.getMinX();
            for (int i3 = 0; n8 > 0 && i3 < arrtextRun.length; ++i3) {
                TextRun textRun = arrtextRun[i3];
                int n9 = textRun.getStart();
                int n10 = textRun.getEnd();
                float f8 = textRun.getWidth();
                int n11 = Math.max(n9, Math.min(n2, n10));
                int n12 = Math.max(n9, Math.min(n3, n10));
                int n13 = n12 - n11;
                if (n13 != 0) {
                    float f9;
                    float f10;
                    boolean bl = textRun.isLeftToRight();
                    float f11 = n9 > n2 ? (bl ? f7 : f7 + f8) : f7 + textRun.getXAtOffset(n2 - n9, true);
                    if (f11 > (f10 = n10 < n3 ? (bl ? f7 + f8 : f7) : f7 + textRun.getXAtOffset(n3 - n9, true))) {
                        f9 = f11;
                        f11 = f10;
                        f10 = f9;
                    }
                    n8 -= n13;
                    f9 = 0.0f;
                    float f12 = 0.0f;
                    switch (n4) {
                        case 1: {
                            f9 = f4;
                            f12 = f4 + rectBounds.getHeight();
                            break;
                        }
                        case 2: 
                        case 4: {
                            Object object;
                            FontStrike fontStrike = null;
                            if (this.spans != null) {
                                object = textRun.getTextSpan();
                                PGFont pGFont = (PGFont)object.getFont();
                                if (pGFont == null) break;
                                fontStrike = pGFont.getStrike(IDENTITY);
                            } else {
                                fontStrike = this.strike;
                            }
                            f9 = f4 - textRun.getAscent();
                            object = fontStrike.getMetrics();
                            f12 = n4 == 2 ? (f9 += object.getUnderLineOffset()) + object.getUnderLineThickness() : (f9 += object.getStrikethroughOffset()) + object.getStrikethroughThickness();
                        }
                    }
                    if (f11 != f6) {
                        if (f5 != -1.0f && f6 != -1.0f) {
                            float f13 = f5;
                            float f14 = f6;
                            if (this.isMirrored()) {
                                float f15 = this.getMirroringWidth();
                                f13 = f15 - f13;
                                f14 = f15 - f14;
                            }
                            arrayList.add(new MoveTo(f2 + f13, f3 + f9));
                            arrayList.add(new LineTo(f2 + f14, f3 + f9));
                            arrayList.add(new LineTo(f2 + f14, f3 + f12));
                            arrayList.add(new LineTo(f2 + f13, f3 + f12));
                            arrayList.add(new LineTo(f2 + f13, f3 + f9));
                        }
                        f5 = f11;
                        f6 = f10;
                    }
                    f6 = f10;
                    if (n8 == 0) {
                        float f16 = f5;
                        float f17 = f6;
                        if (this.isMirrored()) {
                            float f18 = this.getMirroringWidth();
                            f16 = f18 - f16;
                            f17 = f18 - f17;
                        }
                        arrayList.add(new MoveTo(f2 + f16, f3 + f9));
                        arrayList.add(new LineTo(f2 + f17, f3 + f9));
                        arrayList.add(new LineTo(f2 + f17, f3 + f12));
                        arrayList.add(new LineTo(f2 + f16, f3 + f12));
                        arrayList.add(new LineTo(f2 + f16, f3 + f9));
                    }
                }
                f7 += f8;
            }
            f4 += rectBounds.getHeight() + this.spacing;
        }
        return arrayList.toArray(new PathElement[arrayList.size()]);
    }

    @Override
    public Shape getShape(int n2, TextSpan textSpan) {
        boolean bl;
        this.ensureLayout();
        boolean bl2 = (n2 & 1) != 0;
        boolean bl3 = (n2 & 2) != 0;
        boolean bl4 = (n2 & 4) != 0;
        boolean bl5 = bl = (n2 & 8) != 0;
        if (this.shape != null && bl2 && !bl3 && !bl4 && bl) {
            return this.shape;
        }
        Path2D path2D = new Path2D();
        Translate2D translate2D = new Translate2D(0.0, 0.0);
        float f2 = 0.0f;
        if (bl) {
            f2 = -this.lines[0].getBounds().getMinY();
        }
        for (int i2 = 0; i2 < this.lines.length; ++i2) {
            TextLine textLine = this.lines[i2];
            TextRun[] arrtextRun = textLine.getRuns();
            RectBounds rectBounds = textLine.getBounds();
            float f3 = -rectBounds.getMinY();
            for (int i3 = 0; i3 < arrtextRun.length; ++i3) {
                Shape shape;
                Object object;
                TextRun textRun = arrtextRun[i3];
                FontStrike fontStrike = null;
                if (this.spans != null) {
                    PGFont pGFont;
                    object = textRun.getTextSpan();
                    if (textSpan != null && object != textSpan || (pGFont = (PGFont)object.getFont()) == null) continue;
                    fontStrike = pGFont.getStrike(IDENTITY);
                } else {
                    fontStrike = this.strike;
                }
                object = textRun.getLocation();
                float f4 = ((Point2D)object).x;
                float f5 = ((Point2D)object).y + f3 - f2;
                Metrics metrics = null;
                if (bl3 || bl4) {
                    metrics = fontStrike.getMetrics();
                }
                if (bl3) {
                    shape = new RoundRectangle2D();
                    shape.x = f4;
                    shape.y = f5 + metrics.getUnderLineOffset();
                    shape.width = textRun.getWidth();
                    shape.height = metrics.getUnderLineThickness();
                    path2D.append(shape, false);
                }
                if (bl4) {
                    shape = new RoundRectangle2D();
                    shape.x = f4;
                    shape.y = f5 + metrics.getStrikethroughOffset();
                    shape.width = textRun.getWidth();
                    shape.height = metrics.getStrikethroughThickness();
                    path2D.append(shape, false);
                }
                if (!bl2 || textRun.getGlyphCount() <= 0) continue;
                ((BaseTransform)translate2D).restoreTransform(1.0, 0.0, 0.0, 1.0, f4, f5);
                shape = (Path2D)fontStrike.getOutline(textRun, translate2D);
                path2D.append(shape, false);
            }
        }
        if (bl2 && !bl3 && !bl4) {
            this.shape = path2D;
        }
        return path2D;
    }

    private int getLineIndex(float f2) {
        int n2;
        float f3 = 0.0f;
        int n3 = this.getLineCount();
        for (n2 = 0; n2 < n3; ++n2) {
            f3 += this.lines[n2].getBounds().getHeight() + this.spacing;
            if (n2 + 1 == n3) {
                f3 -= this.lines[n2].getLeading();
            }
            if (f3 > f2) break;
        }
        return n2;
    }

    private boolean copyCache() {
        int n2 = this.flags & 0x3C0000;
        int n3 = this.flags & 0x4000;
        return this.wrapWidth != 0.0f || n2 != 262144 || n3 == 0 || this.isMirrored();
    }

    private void initCache() {
        if (this.cacheKey != null) {
            LayoutCache layoutCache;
            if (this.layoutCache == null && (layoutCache = stringCache.get(this.cacheKey)) != null && layoutCache.font.equals(this.font) && Arrays.equals(layoutCache.text, this.text)) {
                this.layoutCache = layoutCache;
                this.runs = layoutCache.runs;
                this.runCount = layoutCache.runCount;
                this.flags |= layoutCache.analysis;
            }
            if (this.layoutCache != null) {
                if (this.copyCache()) {
                    if (this.layoutCache.runs == this.runs) {
                        this.runs = new TextRun[this.runCount];
                        System.arraycopy(this.layoutCache.runs, 0, this.runs, 0, this.runCount);
                    }
                } else if (this.layoutCache.lines != null) {
                    this.runs = this.layoutCache.runs;
                    this.runCount = this.layoutCache.runCount;
                    this.flags |= this.layoutCache.analysis;
                    this.lines = this.layoutCache.lines;
                    this.layoutWidth = this.layoutCache.layoutWidth;
                    this.layoutHeight = this.layoutCache.layoutHeight;
                    float f2 = this.lines[0].getBounds().getMinY();
                    this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0f, f2, 0.0f, this.layoutWidth, this.layoutHeight + f2, 0.0f);
                }
            }
        }
    }

    private int getLineCount() {
        return this.lines.length;
    }

    private int getCharCount() {
        if (this.text != null) {
            return this.text.length;
        }
        int n2 = 0;
        for (int i2 = 0; i2 < this.lines.length; ++i2) {
            n2 += this.lines[i2].getLength();
        }
        return n2;
    }

    public TextSpan[] getTextSpans() {
        return this.spans;
    }

    public PGFont getFont() {
        return this.font;
    }

    public int getDirection() {
        if ((this.flags & 0x400) != 0) {
            return 0;
        }
        if ((this.flags & 0x800) != 0) {
            return 1;
        }
        if ((this.flags & 0x1000) != 0) {
            return -2;
        }
        if ((this.flags & 0x2000) != 0) {
            return -1;
        }
        return -2;
    }

    public void addTextRun(TextRun textRun) {
        if (this.runCount + 1 > this.runs.length) {
            TextRun[] arrtextRun = new TextRun[this.runs.length + 64];
            System.arraycopy(this.runs, 0, arrtextRun, 0, this.runs.length);
            this.runs = arrtextRun;
        }
        this.runs[this.runCount++] = textRun;
    }

    private void buildRuns(char[] arrc) {
        this.runCount = 0;
        if (this.runs == null) {
            int n2 = Math.max(4, Math.min(arrc.length / 16, 16));
            this.runs = new TextRun[n2];
        }
        GlyphLayout glyphLayout = GlyphLayout.getInstance();
        this.flags = glyphLayout.breakRuns(this, arrc, this.flags);
        glyphLayout.dispose();
        for (int i2 = this.runCount; i2 < this.runs.length; ++i2) {
            this.runs[i2] = null;
        }
    }

    private void shape(TextRun textRun, char[] arrc, GlyphLayout glyphLayout) {
        float f2;
        FontStrike fontStrike;
        PGFont pGFont;
        Object object;
        if (this.spans != null) {
            if (this.spans.length == 0) {
                return;
            }
            object = textRun.getTextSpan();
            pGFont = (PGFont)object.getFont();
            if (pGFont == null) {
                RectBounds rectBounds = object.getBounds();
                textRun.setEmbedded(rectBounds, object.getText().length());
                return;
            }
            fontStrike = pGFont.getStrike(IDENTITY);
        } else {
            pGFont = this.font;
            fontStrike = this.strike;
        }
        if (textRun.getAscent() == 0.0f) {
            object = fontStrike.getMetrics();
            if ((this.flags & 0x4000) == 16384) {
                float f3 = object.getAscent();
                if (pGFont.getFamilyName().equals("Segoe UI")) {
                    f3 = (float)((double)f3 * 0.8);
                }
                f3 = (int)((double)f3 - 0.75);
                float f4 = (int)((double)object.getDescent() + 0.75);
                f2 = (int)((double)object.getLineGap() + 0.75);
                float f5 = (int)((double)object.getCapHeight() + 0.75);
                float f6 = -f3 - f5;
                if (f6 > f4) {
                    f4 = f6;
                } else {
                    f3 += f6 - f4;
                }
                textRun.setMetrics(f3, f4, f2);
            } else {
                textRun.setMetrics(object.getAscent(), object.getDescent(), object.getLineGap());
            }
        }
        if (textRun.isTab()) {
            return;
        }
        if (textRun.isLinebreak()) {
            return;
        }
        if (textRun.getGlyphCount() > 0) {
            return;
        }
        if (textRun.isComplex()) {
            glyphLayout.layout(textRun, pGFont, fontStrike, arrc);
        } else {
            object = fontStrike.getFontResource();
            int n2 = textRun.getStart();
            int n3 = textRun.getLength();
            if (this.layoutCache == null) {
                f2 = fontStrike.getSize();
                CharToGlyphMapper charToGlyphMapper = object.getGlyphMapper();
                int[] arrn = new int[n3];
                charToGlyphMapper.charsToGlyphs(n2, n3, arrc, arrn);
                float[] arrf = new float[n3 + 1 << 1];
                float f7 = 0.0f;
                for (int i2 = 0; i2 < n3; ++i2) {
                    float f8 = object.getAdvance(arrn[i2], f2);
                    arrf[i2 << 1] = f7;
                    f7 += f8;
                }
                arrf[n3 << 1] = f7;
                textRun.shape(n3, arrn, arrf, null);
            } else {
                if (!this.layoutCache.valid) {
                    f2 = fontStrike.getSize();
                    CharToGlyphMapper charToGlyphMapper = object.getGlyphMapper();
                    charToGlyphMapper.charsToGlyphs(n2, n3, arrc, this.layoutCache.glyphs, n2);
                    int n4 = n2 + n3;
                    float f9 = 0.0f;
                    for (int i3 = n2; i3 < n4; ++i3) {
                        float f10;
                        this.layoutCache.advances[i3] = f10 = object.getAdvance(this.layoutCache.glyphs[i3], f2);
                        f9 += f10;
                    }
                    textRun.setWidth(f9);
                }
                textRun.shape(n3, this.layoutCache.glyphs, this.layoutCache.advances);
            }
        }
    }

    private TextLine createLine(int n2, int n3, int n4) {
        int n5 = n3 - n2 + 1;
        TextRun[] arrtextRun = new TextRun[n5];
        if (n2 < this.runCount) {
            System.arraycopy(this.runs, n2, arrtextRun, 0, n5);
        }
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        int n6 = 0;
        for (int i2 = 0; i2 < arrtextRun.length; ++i2) {
            TextRun textRun = arrtextRun[i2];
            f2 += textRun.getWidth();
            f3 = Math.min(f3, textRun.getAscent());
            f4 = Math.max(f4, textRun.getDescent());
            f5 = Math.max(f5, textRun.getLeading());
            n6 += textRun.getLength();
        }
        if (f2 > this.layoutWidth) {
            this.layoutWidth = f2;
        }
        return new TextLine(n4, n6, arrtextRun, f2, f3, f4, f5);
    }

    private void reorderLine(TextLine textLine) {
        Object[] arrobject = textLine.getRuns();
        int n2 = arrobject.length;
        if (n2 > 0 && arrobject[n2 - 1].isLinebreak()) {
            --n2;
        }
        if (n2 < 2) {
            return;
        }
        byte[] arrby = new byte[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrby[i2] = arrobject[i2].getLevel();
        }
        Bidi.reorderVisually(arrby, 0, arrobject, 0, n2);
    }

    private char[] getText() {
        if (this.text == null) {
            int n2;
            int n3 = 0;
            for (n2 = 0; n2 < this.spans.length; ++n2) {
                n3 += this.spans[n2].getText().length();
            }
            this.text = new char[n3];
            n2 = 0;
            for (int i2 = 0; i2 < this.spans.length; ++i2) {
                String string = this.spans[i2].getText();
                int n4 = string.length();
                string.getChars(0, n4, this.text, n2);
                n2 += n4;
            }
        }
        return this.text;
    }

    private boolean isSimpleLayout() {
        int n2 = this.flags & 0x3C0000;
        boolean bl = this.wrapWidth > 0.0f && n2 == 0x200000;
        int n3 = 24;
        return (this.flags & n3) == 0 && !bl;
    }

    private boolean isMirrored() {
        boolean bl = false;
        switch (this.flags & 0x3C00) {
            case 2048: {
                bl = true;
                break;
            }
            case 1024: {
                bl = false;
                break;
            }
            case 4096: 
            case 8192: {
                bl = (this.flags & 0x100) != 0;
            }
        }
        return bl;
    }

    private float getMirroringWidth() {
        return this.wrapWidth != 0.0f ? this.wrapWidth : this.layoutWidth;
    }

    private void reuseRuns() {
        TextRun textRun;
        this.runCount = 0;
        int n2 = 0;
        block0: while (n2 < this.runs.length && (textRun = this.runs[n2]) != null) {
            TextRun textRun2;
            this.runs[n2] = null;
            ++n2;
            this.runs[this.runCount++] = textRun = textRun.unwrap();
            if (!textRun.isSplit()) continue;
            textRun.merge(null);
            while (n2 < this.runs.length && (textRun2 = this.runs[n2]) != null) {
                textRun.merge(textRun2);
                this.runs[n2] = null;
                ++n2;
                if (!textRun2.isSplitLast()) continue;
                continue block0;
            }
        }
    }

    private float getTabAdvance() {
        float f2 = 0.0f;
        if (this.spans != null) {
            for (int i2 = 0; i2 < this.spans.length; ++i2) {
                TextSpan textSpan = this.spans[i2];
                PGFont pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) continue;
                FontStrike fontStrike = pGFont.getStrike(IDENTITY);
                f2 = fontStrike.getCharAdvance(' ');
                break;
            }
        } else {
            f2 = this.strike.getCharAdvance(' ');
        }
        return 8.0f * f2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void layout() {
        TextRun[] arrtextRun;
        int n2;
        int n3;
        float f2;
        this.initCache();
        if (this.lines != null) {
            return;
        }
        char[] arrc = this.getText();
        if ((this.flags & 2) != 0 && this.isSimpleLayout()) {
            this.reuseRuns();
        } else {
            this.buildRuns(arrc);
        }
        GlyphLayout glyphLayout = null;
        if ((this.flags & 0x10) != 0) {
            glyphLayout = GlyphLayout.getInstance();
        }
        float f3 = 0.0f;
        if ((this.flags & 4) != 0) {
            f3 = this.getTabAdvance();
        }
        BreakIterator breakIterator = null;
        if (this.wrapWidth > 0.0f && (this.flags & 0x50) != 0) {
            breakIterator = BreakIterator.getLineInstance();
            breakIterator.setText(new CharArrayIterator(arrc));
        }
        int n4 = this.flags & 0x3C0000;
        if (this.isSimpleLayout()) {
            if (this.layoutCache == null) {
                this.layoutCache = new LayoutCache();
                this.layoutCache.glyphs = new int[arrc.length];
                this.layoutCache.advances = new float[arrc.length];
            }
        } else {
            this.layoutCache = null;
        }
        float f4 = 0.0f;
        int n5 = 0;
        int n6 = 0;
        ArrayList<TextLine> arrayList = new ArrayList<TextLine>();
        for (int i2 = 0; i2 < this.runCount; ++i2) {
            TextRun textRun = this.runs[i2];
            this.shape(textRun, arrc, glyphLayout);
            if (textRun.isTab()) {
                f2 = (float)((int)(f4 / f3) + 1) * f3;
                textRun.setWidth(f2 - f4);
            }
            f2 = textRun.getWidth();
            if (this.wrapWidth > 0.0f && f4 + f2 > this.wrapWidth && !textRun.isLinebreak()) {
                int n7;
                int n8;
                int n9;
                n3 = textRun.getStart() + textRun.getWrapIndex(this.wrapWidth - f4);
                int n10 = n3;
                if (n10 + 1 >= (n2 = textRun.getEnd()) || arrc[n10] == ' ') {
                    // empty if block
                }
                if (breakIterator != null) {
                    n9 = breakIterator.isBoundary(n10) || arrc[n10] == '\t' ? n10 : breakIterator.preceding(n10);
                } else {
                    n8 = Character.isWhitespace(arrc[n9]);
                    for (n9 = ++n10; n9 > n6; --n9) {
                        boolean bl = Character.isWhitespace(arrc[n9 - 1]);
                        if (n8 == 0 && bl) break;
                        n8 = bl ? 1 : 0;
                    }
                }
                if (n9 < n6) {
                    n9 = n6;
                }
                TextRun textRun2 = null;
                for (n8 = n5; n8 < this.runCount && (textRun2 = this.runs[n8]).getEnd() <= n9; ++n8) {
                }
                if (n9 == n6) {
                    textRun2 = textRun;
                    n8 = i2;
                    n9 = n3;
                }
                if ((n7 = n9 - textRun2.getStart()) == 0 && n8 != n5) {
                    i2 = n8 - 1;
                } else {
                    i2 = n8;
                    if (n7 == 0) {
                        ++n7;
                    }
                    if (n7 < textRun2.getLength()) {
                        if (this.runCount >= this.runs.length) {
                            arrtextRun = new TextRun[this.runs.length + 64];
                            System.arraycopy(this.runs, 0, arrtextRun, 0, i2 + 1);
                            System.arraycopy(this.runs, i2 + 1, arrtextRun, i2 + 2, this.runs.length - i2 - 1);
                            this.runs = arrtextRun;
                        } else {
                            System.arraycopy(this.runs, i2 + 1, this.runs, i2 + 2, this.runCount - i2 - 1);
                        }
                        this.runs[i2 + 1] = textRun2.split(n7);
                        if (textRun2.isComplex()) {
                            this.shape(textRun2, arrc, glyphLayout);
                        }
                        ++this.runCount;
                    }
                }
                if (i2 + 1 < this.runCount && !this.runs[i2 + 1].isLinebreak()) {
                    textRun = this.runs[i2];
                    textRun.setSoftbreak();
                    this.flags |= 0x80;
                }
            }
            f4 += f2;
            if (!textRun.isBreak()) continue;
            TextLine textLine = this.createLine(n5, i2, n6);
            arrayList.add(textLine);
            n5 = i2 + 1;
            n6 += textLine.getLength();
            f4 = 0.0f;
        }
        if (glyphLayout != null) {
            glyphLayout.dispose();
        }
        arrayList.add(this.createLine(n5, this.runCount - 1, n6));
        this.lines = new TextLine[arrayList.size()];
        arrayList.toArray(this.lines);
        float f5 = Math.max(this.wrapWidth, this.layoutWidth);
        float f6 = 0.0f;
        if (this.isMirrored()) {
            f2 = 1.0f;
            if (n4 == 0x100000) {
                f2 = 0.0f;
            }
        } else {
            f2 = 0.0f;
            if (n4 == 0x100000) {
                f2 = 1.0f;
            }
        }
        if (n4 == 524288) {
            f2 = 0.5f;
        }
        for (n3 = 0; n3 < this.lines.length; ++n3) {
            int n11;
            TextRun[] arrtextRun2;
            int n12;
            boolean bl;
            TextLine textLine = this.lines[n3];
            n2 = textLine.getStart();
            RectBounds rectBounds = textLine.getBounds();
            float f7 = (f5 - rectBounds.getWidth()) * f2;
            textLine.setAlignment(f7);
            boolean bl2 = bl = this.wrapWidth > 0.0f && n4 == 0x200000;
            if (bl && (n12 = (arrtextRun2 = textLine.getRuns()).length) > 0 && arrtextRun2[n12 - 1].isSoftbreak()) {
                n11 = n2 + textLine.getLength();
                int n13 = 0;
                boolean bl3 = false;
                for (int i3 = n11 - 1; i3 >= n2; --i3) {
                    if (!bl3 && arrc[i3] != ' ') {
                        bl3 = true;
                    }
                    if (!bl3 || arrc[i3] != ' ') continue;
                    ++n13;
                }
                if (n13 != 0) {
                    float f8 = (f5 - rectBounds.getWidth()) / (float)n13;
                    block8: for (int i4 = 0; i4 < n12; ++i4) {
                        TextRun textRun = arrtextRun2[i4];
                        int n14 = textRun.getStart();
                        int n15 = textRun.getEnd();
                        for (int i5 = n14; i5 < n15; ++i5) {
                            if (arrc[i5] != ' ') continue;
                            textRun.justify(i5 - n14, f8);
                            if (--n13 == 0) break block8;
                        }
                    }
                    f7 = 0.0f;
                    textLine.setAlignment(f7);
                    textLine.setWidth(f5);
                }
            }
            if ((this.flags & 8) != 0) {
                this.reorderLine(textLine);
            }
            this.computeSideBearings(textLine);
            float f9 = f7;
            arrtextRun = textLine.getRuns();
            for (n11 = 0; n11 < arrtextRun.length; ++n11) {
                TextRun textRun = arrtextRun[n11];
                textRun.setLocation(f9, f6);
                textRun.setLine(textLine);
                f9 += textRun.getWidth();
            }
            if (n3 + 1 < this.lines.length) {
                f6 = Math.max(f6, f6 + rectBounds.getHeight() + this.spacing);
                continue;
            }
            f6 += rectBounds.getHeight() - textLine.getLeading();
        }
        float f10 = this.lines[0].getBounds().getMinY();
        this.layoutHeight = f6;
        this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0f, f10, 0.0f, this.layoutWidth, this.layoutHeight + f10, 0.0f);
        if (this.layoutCache != null) {
            if (this.cacheKey != null && !this.layoutCache.valid && !this.copyCache()) {
                this.layoutCache.font = this.font;
                this.layoutCache.text = this.text;
                this.layoutCache.runs = this.runs;
                this.layoutCache.runCount = this.runCount;
                this.layoutCache.lines = this.lines;
                this.layoutCache.layoutWidth = this.layoutWidth;
                this.layoutCache.layoutHeight = this.layoutHeight;
                this.layoutCache.analysis = this.flags & 0x7FF;
                Object object = CACHE_SIZE_LOCK;
                synchronized (object) {
                    n2 = arrc.length;
                    if (cacheSize + n2 > MAX_CACHE_SIZE) {
                        stringCache.clear();
                        cacheSize = 0;
                    }
                    stringCache.put(this.cacheKey, this.layoutCache);
                    cacheSize += n2;
                }
            }
            this.layoutCache.valid = true;
        }
    }

    @Override
    public BaseBounds getVisualBounds(int n2) {
        boolean bl;
        this.ensureLayout();
        if (this.strike == null) {
            return null;
        }
        boolean bl2 = (n2 & 2) != 0;
        boolean bl3 = (this.flags & 0x200) != 0;
        boolean bl4 = (n2 & 4) != 0;
        boolean bl5 = bl = (this.flags & 0x400) != 0;
        if (this.visualBounds != null && bl2 == bl3 && bl4 == bl) {
            return this.visualBounds;
        }
        this.flags &= 0xFFFFF9FF;
        if (bl2) {
            this.flags |= 0x200;
        }
        if (bl4) {
            this.flags |= 0x400;
        }
        this.visualBounds = new RectBounds();
        float f2 = Float.POSITIVE_INFINITY;
        float f3 = Float.POSITIVE_INFINITY;
        float f4 = Float.NEGATIVE_INFINITY;
        float f5 = Float.NEGATIVE_INFINITY;
        float[] arrf = new float[4];
        FontResource fontResource = this.strike.getFontResource();
        Metrics metrics = this.strike.getMetrics();
        float f6 = this.strike.getSize();
        for (int i2 = 0; i2 < this.lines.length; ++i2) {
            TextLine textLine = this.lines[i2];
            TextRun[] arrtextRun = textLine.getRuns();
            for (int i3 = 0; i3 < arrtextRun.length; ++i3) {
                float f7;
                float f8;
                TextRun textRun = arrtextRun[i3];
                Point2D point2D = textRun.getLocation();
                if (textRun.isLinebreak()) continue;
                int n3 = textRun.getGlyphCount();
                for (int i4 = 0; i4 < n3; ++i4) {
                    int n4 = textRun.getGlyphCode(i4);
                    if (n4 == 65535) continue;
                    fontResource.getGlyphBoundingBox(textRun.getGlyphCode(i4), f6, arrf);
                    if (arrf[0] == arrf[2]) continue;
                    f8 = point2D.x + textRun.getPosX(i4);
                    f7 = point2D.y + textRun.getPosY(i4);
                    float f9 = f8 + arrf[0];
                    float f10 = f7 - arrf[3];
                    float f11 = f8 + arrf[2];
                    float f12 = f7 - arrf[1];
                    if (f9 < f2) {
                        f2 = f9;
                    }
                    if (f10 < f3) {
                        f3 = f10;
                    }
                    if (f11 > f4) {
                        f4 = f11;
                    }
                    if (!(f12 > f5)) continue;
                    f5 = f12;
                }
                if (bl2) {
                    float f13 = point2D.x;
                    float f14 = point2D.y + metrics.getUnderLineOffset();
                    f8 = f13 + textRun.getWidth();
                    f7 = f14 + metrics.getUnderLineThickness();
                    if (f13 < f2) {
                        f2 = f13;
                    }
                    if (f14 < f3) {
                        f3 = f14;
                    }
                    if (f8 > f4) {
                        f4 = f8;
                    }
                    if (f7 > f5) {
                        f5 = f7;
                    }
                }
                if (!bl4) continue;
                float f15 = point2D.x;
                float f16 = point2D.y + metrics.getStrikethroughOffset();
                f8 = f15 + textRun.getWidth();
                f7 = f16 + metrics.getStrikethroughThickness();
                if (f15 < f2) {
                    f2 = f15;
                }
                if (f16 < f3) {
                    f3 = f16;
                }
                if (f8 > f4) {
                    f4 = f8;
                }
                if (!(f7 > f5)) continue;
                f5 = f7;
            }
        }
        if (f2 < f4 && f3 < f5) {
            this.visualBounds.setBounds(f2, f3, f4, f5);
        }
        return this.visualBounds;
    }

    private void computeSideBearings(TextLine textLine) {
        Object object;
        int n2;
        TextRun[] arrtextRun = textLine.getRuns();
        if (arrtextRun.length == 0) {
            return;
        }
        float[] arrf = new float[4];
        FontResource fontResource = null;
        float f2 = 0.0f;
        if (this.strike != null) {
            fontResource = this.strike.getFontResource();
            f2 = this.strike.getSize();
        }
        float f3 = 0.0f;
        float f4 = 0.0f;
        block0: for (int i2 = 0; i2 < arrtextRun.length; ++i2) {
            TextRun textRun = arrtextRun[i2];
            int n3 = textRun.getGlyphCount();
            for (n2 = 0; n2 < n3; ++n2) {
                int n4;
                float f5 = textRun.getAdvance(n2);
                if (f5 != 0.0f && (n4 = textRun.getGlyphCode(n2)) != 65535) {
                    FontResource fontResource2 = fontResource;
                    if (fontResource2 == null) {
                        TextSpan textSpan = textRun.getTextSpan();
                        object = (PGFont)textSpan.getFont();
                        f2 = object.getSize();
                        fontResource2 = object.getFontResource();
                    }
                    fontResource2.getGlyphBoundingBox(n4, f2, arrf);
                    float f6 = arrf[0];
                    f3 = Math.min(0.0f, f6 + f4);
                    textRun.setLeftBearing();
                    break block0;
                }
                f4 += f5;
            }
            if (n3 != 0) continue;
            f4 += textRun.getWidth();
        }
        float f7 = 0.0f;
        f4 = 0.0f;
        block2: for (int i3 = arrtextRun.length - 1; i3 >= 0; --i3) {
            TextRun textRun = arrtextRun[i3];
            n2 = textRun.getGlyphCount();
            for (int i4 = n2 - 1; i4 >= 0; --i4) {
                int n5;
                float f8 = textRun.getAdvance(i4);
                if (f8 != 0.0f && (n5 = textRun.getGlyphCode(i4)) != 65535) {
                    FontResource fontResource3 = fontResource;
                    if (fontResource3 == null) {
                        object = textRun.getTextSpan();
                        PGFont pGFont = (PGFont)object.getFont();
                        f2 = pGFont.getSize();
                        fontResource3 = pGFont.getFontResource();
                    }
                    fontResource3.getGlyphBoundingBox(n5, f2, arrf);
                    float f9 = arrf[2] - f8;
                    f7 = Math.max(0.0f, f9 - f4);
                    textRun.setRightBearing();
                    break block2;
                }
                f4 += f8;
            }
            if (n2 != 0) continue;
            f4 += textRun.getWidth();
        }
        textLine.setSideBearings(f3, f7);
    }
}

