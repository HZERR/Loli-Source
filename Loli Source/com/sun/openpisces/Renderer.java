/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Curve;
import com.sun.openpisces.Helpers;
import java.util.Arrays;

public final class Renderer
implements PathConsumer2D {
    private static final int YMAX = 0;
    private static final int CURX = 1;
    private static final int OR = 2;
    private static final int SLOPE = 3;
    private static final int NEXT = 4;
    private static final int SIZEOF_EDGE = 5;
    private int sampleRowMin;
    private int sampleRowMax;
    private float edgeMinX;
    private float edgeMaxX;
    private float[] edges;
    private int[] edgeBuckets;
    private int numEdges;
    private static final float DEC_BND = 1.0f;
    private static final float INC_BND = 0.4f;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private final int SUBPIXEL_LG_POSITIONS_X;
    private final int SUBPIXEL_LG_POSITIONS_Y;
    private final int SUBPIXEL_POSITIONS_X;
    private final int SUBPIXEL_POSITIONS_Y;
    private final int SUBPIXEL_MASK_X;
    private final int SUBPIXEL_MASK_Y;
    final int MAX_AA_ALPHA;
    private int boundsMinX;
    private int boundsMinY;
    private int boundsMaxX;
    private int boundsMaxY;
    private int windingRule;
    private float x0;
    private float y0;
    private float pix_sx0;
    private float pix_sy0;
    private Curve c = new Curve();
    private int[] savedAlpha;
    private ScanlineIterator savedIterator;

    private void addEdgeToBucket(int n2, int n3) {
        this.edges[n2 + 4] = this.edgeBuckets[n3 * 2];
        this.edgeBuckets[n3 * 2] = n2 + 1;
        int n4 = n3 * 2 + 1;
        this.edgeBuckets[n4] = this.edgeBuckets[n4] + 2;
    }

    private void quadBreakIntoLinesAndAdd(float f2, float f3, Curve curve, float f4, float f5) {
        int n2 = 16;
        int n3 = n2 * n2;
        float f6 = Math.max(curve.dbx / (float)n3, curve.dby / (float)n3);
        while (f6 > 32.0f) {
            f6 /= 4.0f;
            n2 <<= 1;
        }
        n3 = n2 * n2;
        float f7 = curve.dbx / (float)n3;
        float f8 = curve.dby / (float)n3;
        float f9 = curve.bx / (float)n3 + curve.cx / (float)n2;
        float f10 = curve.by / (float)n3 + curve.cy / (float)n2;
        while (n2-- > 1) {
            float f11 = f2 + f9;
            f9 += f7;
            float f12 = f3 + f10;
            f10 += f8;
            this.addLine(f2, f3, f11, f12);
            f2 = f11;
            f3 = f12;
        }
        this.addLine(f2, f3, f4, f5);
    }

    private void curveBreakIntoLinesAndAdd(float f2, float f3, Curve curve, float f4, float f5) {
        int n2 = 8;
        float f6 = 2.0f * curve.dax / 512.0f;
        float f7 = 2.0f * curve.day / 512.0f;
        float f8 = f6 + curve.dbx / 64.0f;
        float f9 = f7 + curve.dby / 64.0f;
        float f10 = curve.ax / 512.0f + curve.bx / 64.0f + curve.cx / 8.0f;
        float f11 = curve.ay / 512.0f + curve.by / 64.0f + curve.cy / 8.0f;
        float f12 = f2;
        float f13 = f3;
        while (n2 > 0) {
            while (Math.abs(f8) > 1.0f || Math.abs(f9) > 1.0f) {
                f8 = f8 / 4.0f - (f6 /= 8.0f);
                f9 = f9 / 4.0f - (f7 /= 8.0f);
                f10 = (f10 - f8) / 2.0f;
                f11 = (f11 - f9) / 2.0f;
                n2 <<= 1;
            }
            while (n2 % 2 == 0 && Math.abs(f10) <= 0.4f && Math.abs(f11) <= 0.4f) {
                f10 = 2.0f * f10 + f8;
                f11 = 2.0f * f11 + f9;
                f8 = 4.0f * (f8 + f6);
                f9 = 4.0f * (f9 + f7);
                f6 = 8.0f * f6;
                f7 = 8.0f * f7;
                n2 >>= 1;
            }
            if (--n2 > 0) {
                f12 += f10;
                f10 += f8;
                f8 += f6;
                f13 += f11;
                f11 += f9;
                f9 += f7;
            } else {
                f12 = f4;
                f13 = f5;
            }
            this.addLine(f2, f3, f12, f13);
            f2 = f12;
            f3 = f13;
        }
    }

    private void addLine(float f2, float f3, float f4, float f5) {
        float f6;
        int n2;
        int n3;
        float f7 = 1.0f;
        if (f5 < f3) {
            f7 = f5;
            f5 = f3;
            f3 = f7;
            f7 = f4;
            f4 = f2;
            f2 = f7;
            f7 = 0.0f;
        }
        if ((n3 = Math.max((int)Math.ceil(f3 - 0.5f), this.boundsMinY)) >= (n2 = Math.min((int)Math.ceil(f5 - 0.5f), this.boundsMaxY))) {
            return;
        }
        if (n3 < this.sampleRowMin) {
            this.sampleRowMin = n3;
        }
        if (n2 > this.sampleRowMax) {
            this.sampleRowMax = n2;
        }
        if ((f6 = (f4 - f2) / (f5 - f3)) > 0.0f) {
            if (f2 < this.edgeMinX) {
                this.edgeMinX = f2;
            }
            if (f4 > this.edgeMaxX) {
                this.edgeMaxX = f4;
            }
        } else {
            if (f4 < this.edgeMinX) {
                this.edgeMinX = f4;
            }
            if (f2 > this.edgeMaxX) {
                this.edgeMaxX = f2;
            }
        }
        int n4 = this.numEdges * 5;
        this.edges = Helpers.widenArray(this.edges, n4, 5);
        ++this.numEdges;
        this.edges[n4 + 2] = f7;
        this.edges[n4 + 1] = f2 + ((float)n3 + 0.5f - f3) * f6;
        this.edges[n4 + 3] = f6;
        this.edges[n4 + 0] = n2;
        int n5 = n3 - this.boundsMinY;
        this.addEdgeToBucket(n4, n5);
        int n6 = (n2 - this.boundsMinY) * 2 + 1;
        this.edgeBuckets[n6] = this.edgeBuckets[n6] | 1;
    }

    public Renderer(int n2, int n3) {
        this.SUBPIXEL_LG_POSITIONS_X = n2;
        this.SUBPIXEL_LG_POSITIONS_Y = n3;
        this.SUBPIXEL_POSITIONS_X = 1 << this.SUBPIXEL_LG_POSITIONS_X;
        this.SUBPIXEL_POSITIONS_Y = 1 << this.SUBPIXEL_LG_POSITIONS_Y;
        this.SUBPIXEL_MASK_X = this.SUBPIXEL_POSITIONS_X - 1;
        this.SUBPIXEL_MASK_Y = this.SUBPIXEL_POSITIONS_Y - 1;
        this.MAX_AA_ALPHA = this.SUBPIXEL_POSITIONS_X * this.SUBPIXEL_POSITIONS_Y;
    }

    public Renderer(int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this(n2, n3);
        this.reset(n4, n5, n6, n7, n8);
    }

    public void reset(int n2, int n3, int n4, int n5, int n6) {
        this.windingRule = n6;
        this.boundsMinX = n2 * this.SUBPIXEL_POSITIONS_X;
        this.boundsMinY = n3 * this.SUBPIXEL_POSITIONS_Y;
        this.boundsMaxX = (n2 + n4) * this.SUBPIXEL_POSITIONS_X;
        this.boundsMaxY = (n3 + n5) * this.SUBPIXEL_POSITIONS_Y;
        this.edgeMinX = Float.POSITIVE_INFINITY;
        this.edgeMaxX = Float.NEGATIVE_INFINITY;
        this.sampleRowMax = this.boundsMinY;
        this.sampleRowMin = this.boundsMaxY;
        int n7 = this.boundsMaxY - this.boundsMinY;
        if (this.edgeBuckets == null || this.edgeBuckets.length < n7 * 2 + 2) {
            this.edgeBuckets = new int[n7 * 2 + 2];
        } else {
            Arrays.fill(this.edgeBuckets, 0, n7 * 2, 0);
        }
        if (this.edges == null) {
            this.edges = new float[160];
        }
        this.numEdges = 0;
        this.y0 = 0.0f;
        this.x0 = 0.0f;
        this.pix_sy0 = 0.0f;
        this.pix_sx0 = 0.0f;
    }

    private float tosubpixx(float f2) {
        return f2 * (float)this.SUBPIXEL_POSITIONS_X;
    }

    private float tosubpixy(float f2) {
        return f2 * (float)this.SUBPIXEL_POSITIONS_Y;
    }

    @Override
    public void moveTo(float f2, float f3) {
        this.closePath();
        this.pix_sx0 = f2;
        this.pix_sy0 = f3;
        this.y0 = this.tosubpixy(f3);
        this.x0 = this.tosubpixx(f2);
    }

    @Override
    public void lineTo(float f2, float f3) {
        float f4 = this.tosubpixx(f2);
        float f5 = this.tosubpixy(f3);
        this.addLine(this.x0, this.y0, f4, f5);
        this.x0 = f4;
        this.y0 = f5;
    }

    @Override
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = this.tosubpixx(f6);
        float f9 = this.tosubpixy(f7);
        this.c.set(this.x0, this.y0, this.tosubpixx(f2), this.tosubpixy(f3), this.tosubpixx(f4), this.tosubpixy(f5), f8, f9);
        this.curveBreakIntoLinesAndAdd(this.x0, this.y0, this.c, f8, f9);
        this.x0 = f8;
        this.y0 = f9;
    }

    @Override
    public void quadTo(float f2, float f3, float f4, float f5) {
        float f6 = this.tosubpixx(f4);
        float f7 = this.tosubpixy(f5);
        this.c.set(this.x0, this.y0, this.tosubpixx(f2), this.tosubpixy(f3), f6, f7);
        this.quadBreakIntoLinesAndAdd(this.x0, this.y0, this.c, f6, f7);
        this.x0 = f6;
        this.y0 = f7;
    }

    @Override
    public void closePath() {
        this.lineTo(this.pix_sx0, this.pix_sy0);
    }

    @Override
    public void pathDone() {
        this.closePath();
    }

    public void produceAlphas(AlphaConsumer alphaConsumer) {
        alphaConsumer.setMaxAlpha(this.MAX_AA_ALPHA);
        int n2 = this.windingRule == 0 ? 1 : -1;
        int n3 = alphaConsumer.getWidth();
        int[] arrn = this.savedAlpha;
        if (arrn == null || arrn.length < n3 + 2) {
            this.savedAlpha = arrn = new int[n3 + 2];
        } else {
            Arrays.fill(arrn, 0, n3 + 2, 0);
        }
        int n4 = alphaConsumer.getOriginX() << this.SUBPIXEL_LG_POSITIONS_X;
        int n5 = n4 + (n3 << this.SUBPIXEL_LG_POSITIONS_X);
        int n6 = n5 >> this.SUBPIXEL_LG_POSITIONS_X;
        int n7 = n4 >> this.SUBPIXEL_LG_POSITIONS_Y;
        int n8 = this.boundsMinY;
        ScanlineIterator scanlineIterator = this.savedIterator;
        if (scanlineIterator == null) {
            this.savedIterator = scanlineIterator = new ScanlineIterator();
        } else {
            scanlineIterator.reset();
        }
        while (scanlineIterator.hasNext()) {
            int n9;
            int n10;
            int n11;
            int n12;
            int n13 = scanlineIterator.next();
            int[] arrn2 = scanlineIterator.crossings;
            n8 = scanlineIterator.curY();
            if (n13 > 0) {
                n12 = arrn2[0] >> 1;
                n11 = arrn2[n13 - 1] >> 1;
                n10 = Math.max(n12, n4);
                n9 = Math.min(n11, n5);
                n7 = Math.min(n7, n10 >> this.SUBPIXEL_LG_POSITIONS_X);
                n6 = Math.max(n6, n9 >> this.SUBPIXEL_LG_POSITIONS_X);
            }
            n12 = 0;
            n11 = n4;
            for (n10 = 0; n10 < n13; ++n10) {
                int n14;
                int n15;
                n9 = arrn2[n10];
                int n16 = n9 >> 1;
                int n17 = ((n9 & 1) << 1) - 1;
                if ((n12 & n2) != 0 && (n15 = Math.max(n11, n4)) < (n14 = Math.min(n16, n5))) {
                    int n18 = (n15 -= n4) >> this.SUBPIXEL_LG_POSITIONS_X;
                    int n19 = (n14 -= n4) - 1 >> this.SUBPIXEL_LG_POSITIONS_X;
                    if (n18 == n19) {
                        int n20 = n18;
                        arrn[n20] = arrn[n20] + (n14 - n15);
                        int n21 = n18 + 1;
                        arrn[n21] = arrn[n21] - (n14 - n15);
                    } else {
                        int n22 = n14 >> this.SUBPIXEL_LG_POSITIONS_X;
                        int n23 = n18;
                        arrn[n23] = arrn[n23] + (this.SUBPIXEL_POSITIONS_X - (n15 & this.SUBPIXEL_MASK_X));
                        int n24 = n18 + 1;
                        arrn[n24] = arrn[n24] + (n15 & this.SUBPIXEL_MASK_X);
                        int n25 = n22;
                        arrn[n25] = arrn[n25] - (this.SUBPIXEL_POSITIONS_X - (n14 & this.SUBPIXEL_MASK_X));
                        int n26 = n22 + 1;
                        arrn[n26] = arrn[n26] - (n14 & this.SUBPIXEL_MASK_X);
                    }
                }
                n12 += n17;
                n11 = n16;
            }
            if ((n8 & this.SUBPIXEL_MASK_Y) != this.SUBPIXEL_MASK_Y) continue;
            alphaConsumer.setAndClearRelativeAlphas(arrn, n8 >> this.SUBPIXEL_LG_POSITIONS_Y, n7, n6);
            n6 = n5 >> this.SUBPIXEL_LG_POSITIONS_X;
            n7 = n4 >> this.SUBPIXEL_LG_POSITIONS_Y;
        }
        if ((n8 & this.SUBPIXEL_MASK_Y) < this.SUBPIXEL_MASK_Y) {
            alphaConsumer.setAndClearRelativeAlphas(arrn, n8 >> this.SUBPIXEL_LG_POSITIONS_Y, n7, n6);
        }
    }

    public int getSubpixMinX() {
        int n2 = (int)Math.ceil(this.edgeMinX - 0.5f);
        if (n2 < this.boundsMinX) {
            n2 = this.boundsMinX;
        }
        return n2;
    }

    public int getSubpixMaxX() {
        int n2 = (int)Math.ceil(this.edgeMaxX - 0.5f);
        if (n2 > this.boundsMaxX) {
            n2 = this.boundsMaxX;
        }
        return n2;
    }

    public int getSubpixMinY() {
        return this.sampleRowMin;
    }

    public int getSubpixMaxY() {
        return this.sampleRowMax;
    }

    public int getOutpixMinX() {
        return this.getSubpixMinX() >> this.SUBPIXEL_LG_POSITIONS_X;
    }

    public int getOutpixMaxX() {
        return this.getSubpixMaxX() + this.SUBPIXEL_MASK_X >> this.SUBPIXEL_LG_POSITIONS_X;
    }

    public int getOutpixMinY() {
        return this.sampleRowMin >> this.SUBPIXEL_LG_POSITIONS_Y;
    }

    public int getOutpixMaxY() {
        return this.sampleRowMax + this.SUBPIXEL_MASK_Y >> this.SUBPIXEL_LG_POSITIONS_Y;
    }

    private final class ScanlineIterator {
        private int[] crossings = new int[10];
        private int[] edgePtrs = new int[10];
        private int edgeCount;
        private int nextY;
        private static final int INIT_CROSSINGS_SIZE = 10;

        private ScanlineIterator() {
            this.reset();
        }

        public void reset() {
            this.nextY = Renderer.this.sampleRowMin;
            this.edgeCount = 0;
        }

        private int next() {
            int n2;
            int n3;
            int n4;
            int n5 = this.nextY++;
            int n6 = n5 - Renderer.this.boundsMinY;
            int n7 = this.edgeCount;
            int[] arrn = this.edgePtrs;
            float[] arrf = Renderer.this.edges;
            int n8 = Renderer.this.edgeBuckets[n6 * 2 + 1];
            if ((n8 & 1) != 0) {
                n4 = 0;
                for (n3 = 0; n3 < n7; ++n3) {
                    n2 = arrn[n3];
                    if (!(arrf[n2 + 0] > (float)n5)) continue;
                    arrn[n4++] = n2;
                }
                n7 = n4;
            }
            arrn = Helpers.widenArray(arrn, n7, n8 >> 1);
            n4 = Renderer.this.edgeBuckets[n6 * 2];
            while (n4 != 0) {
                arrn[n7++] = --n4;
                n4 = (int)arrf[n4 + 4];
            }
            this.edgePtrs = arrn;
            this.edgeCount = n7;
            int[] arrn2 = this.crossings;
            if (arrn2.length < n7) {
                this.crossings = arrn2 = new int[arrn.length];
            }
            for (n3 = 0; n3 < n7; ++n3) {
                int n9;
                n2 = arrn[n3];
                float f2 = arrf[n2 + 1];
                int n10 = (int)Math.ceil(f2 - 0.5f) << 1;
                arrf[n2 + 1] = f2 + arrf[n2 + 3];
                if (arrf[n2 + 2] > 0.0f) {
                    n10 |= 1;
                }
                int n11 = n3;
                while (--n11 >= 0 && (n9 = arrn2[n11]) > n10) {
                    arrn2[n11 + 1] = n9;
                    arrn[n11 + 1] = arrn[n11];
                }
                arrn2[n11 + 1] = n10;
                arrn[n11 + 1] = n2;
            }
            return n7;
        }

        private boolean hasNext() {
            return this.nextY < Renderer.this.sampleRowMax;
        }

        private int curY() {
            return this.nextY - 1;
        }
    }
}

