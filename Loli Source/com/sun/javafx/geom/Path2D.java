/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

public class Path2D
extends Shape
implements PathConsumer2D {
    static final int[] curvecoords = new int[]{2, 2, 4, 6, 0};
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private static final byte SEG_MOVETO = 0;
    private static final byte SEG_LINETO = 1;
    private static final byte SEG_QUADTO = 2;
    private static final byte SEG_CUBICTO = 3;
    private static final byte SEG_CLOSE = 4;
    byte[] pointTypes;
    int numTypes;
    int numCoords;
    int windingRule;
    static final int INIT_SIZE = 20;
    static final int EXPAND_MAX = 500;
    float[] floatCoords;
    float moveX;
    float moveY;
    float prevX;
    float prevY;
    float currX;
    float currY;

    public Path2D() {
        this(1, 20);
    }

    public Path2D(int n2) {
        this(n2, 20);
    }

    public Path2D(int n2, int n3) {
        this.setWindingRule(n2);
        this.pointTypes = new byte[n3];
        this.floatCoords = new float[n3 * 2];
    }

    public Path2D(Shape shape) {
        this(shape, null);
    }

    public Path2D(Shape shape, BaseTransform baseTransform) {
        if (shape instanceof Path2D) {
            Path2D path2D = (Path2D)shape;
            this.setWindingRule(path2D.windingRule);
            this.numTypes = path2D.numTypes;
            this.pointTypes = Path2D.copyOf(path2D.pointTypes, path2D.pointTypes.length);
            this.numCoords = path2D.numCoords;
            if (baseTransform == null || baseTransform.isIdentity()) {
                this.floatCoords = Path2D.copyOf(path2D.floatCoords, this.numCoords);
                this.moveX = path2D.moveX;
                this.moveY = path2D.moveY;
                this.prevX = path2D.prevX;
                this.prevY = path2D.prevY;
                this.currX = path2D.currX;
                this.currY = path2D.currY;
            } else {
                this.floatCoords = new float[this.numCoords + 6];
                baseTransform.transform(path2D.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2);
                this.floatCoords[this.numCoords + 0] = this.moveX;
                this.floatCoords[this.numCoords + 1] = this.moveY;
                this.floatCoords[this.numCoords + 2] = this.prevX;
                this.floatCoords[this.numCoords + 3] = this.prevY;
                this.floatCoords[this.numCoords + 4] = this.currX;
                this.floatCoords[this.numCoords + 5] = this.currY;
                baseTransform.transform(this.floatCoords, this.numCoords, this.floatCoords, this.numCoords, 3);
                this.moveX = this.floatCoords[this.numCoords + 0];
                this.moveY = this.floatCoords[this.numCoords + 1];
                this.prevX = this.floatCoords[this.numCoords + 2];
                this.prevY = this.floatCoords[this.numCoords + 3];
                this.currX = this.floatCoords[this.numCoords + 4];
                this.currY = this.floatCoords[this.numCoords + 5];
            }
        } else {
            PathIterator pathIterator = shape.getPathIterator(baseTransform);
            this.setWindingRule(pathIterator.getWindingRule());
            this.pointTypes = new byte[20];
            this.floatCoords = new float[40];
            this.append(pathIterator, false);
        }
    }

    public Path2D(int n2, byte[] arrby, int n3, float[] arrf, int n4) {
        this.windingRule = n2;
        this.pointTypes = arrby;
        this.numTypes = n3;
        this.floatCoords = arrf;
        this.numCoords = n4;
    }

    Point2D getPoint(int n2) {
        return new Point2D(this.floatCoords[n2], this.floatCoords[n2 + 1]);
    }

    private boolean close(int n2, float f2, float f3) {
        return Math.abs((float)n2 - f2) <= f3;
    }

    public boolean checkAndGetIntRect(Rectangle rectangle, float f2) {
        if (this.numTypes == 5) {
            if (this.pointTypes[4] != 1 && this.pointTypes[4] != 4) {
                return false;
            }
        } else if (this.numTypes == 6) {
            if (this.pointTypes[4] != 1) {
                return false;
            }
            if (this.pointTypes[5] != 4) {
                return false;
            }
        } else if (this.numTypes != 4) {
            return false;
        }
        if (this.pointTypes[0] != 0) {
            return false;
        }
        if (this.pointTypes[1] != 1) {
            return false;
        }
        if (this.pointTypes[2] != 1) {
            return false;
        }
        if (this.pointTypes[3] != 1) {
            return false;
        }
        int n2 = (int)(this.floatCoords[0] + 0.5f);
        int n3 = (int)(this.floatCoords[1] + 0.5f);
        if (!this.close(n2, this.floatCoords[0], f2)) {
            return false;
        }
        if (!this.close(n3, this.floatCoords[1], f2)) {
            return false;
        }
        int n4 = (int)(this.floatCoords[2] + 0.5f);
        int n5 = (int)(this.floatCoords[3] + 0.5f);
        if (!this.close(n4, this.floatCoords[2], f2)) {
            return false;
        }
        if (!this.close(n5, this.floatCoords[3], f2)) {
            return false;
        }
        int n6 = (int)(this.floatCoords[4] + 0.5f);
        int n7 = (int)(this.floatCoords[5] + 0.5f);
        if (!this.close(n6, this.floatCoords[4], f2)) {
            return false;
        }
        if (!this.close(n7, this.floatCoords[5], f2)) {
            return false;
        }
        int n8 = (int)(this.floatCoords[6] + 0.5f);
        int n9 = (int)(this.floatCoords[7] + 0.5f);
        if (!this.close(n8, this.floatCoords[6], f2)) {
            return false;
        }
        if (!this.close(n9, this.floatCoords[7], f2)) {
            return false;
        }
        if (this.numTypes > 4 && this.pointTypes[4] == 1) {
            if (!this.close(n2, this.floatCoords[8], f2)) {
                return false;
            }
            if (!this.close(n3, this.floatCoords[9], f2)) {
                return false;
            }
        }
        if (n2 == n4 && n6 == n8 && n3 == n9 && n5 == n7 || n3 == n5 && n7 == n9 && n2 == n8 && n4 == n6) {
            int n10;
            int n11;
            int n12;
            int n13;
            if (n6 < n2) {
                n13 = n6;
                n12 = n2 - n6;
            } else {
                n13 = n2;
                n12 = n6 - n2;
            }
            if (n7 < n3) {
                n11 = n7;
                n10 = n3 - n7;
            } else {
                n11 = n3;
                n10 = n7 - n3;
            }
            if (n12 < 0) {
                return false;
            }
            if (n10 < 0) {
                return false;
            }
            if (rectangle != null) {
                rectangle.setBounds(n13, n11, n12, n10);
            }
            return true;
        }
        return false;
    }

    void needRoom(boolean bl, int n2) {
        int n3;
        if (bl && this.numTypes == 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        int n4 = this.pointTypes.length;
        if (n4 == 0) {
            this.pointTypes = new byte[2];
        } else if (this.numTypes >= n4) {
            n3 = n4;
            if (n3 > 500) {
                n3 = 500;
            }
            this.pointTypes = Path2D.copyOf(this.pointTypes, n4 + n3);
        }
        n4 = this.floatCoords.length;
        if (this.numCoords + n2 > n4) {
            n3 = n4;
            if (n3 > 1000) {
                n3 = 1000;
            }
            if (n3 < n2) {
                n3 = n2;
            }
            this.floatCoords = Path2D.copyOf(this.floatCoords, n4 + n3);
        }
    }

    @Override
    public final void moveTo(float f2, float f3) {
        if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
            this.prevX = this.currX = f2;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords - 2] = this.currX;
            this.prevY = this.currY = f3;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords - 1] = this.currY;
        } else {
            this.needRoom(false, 2);
            this.pointTypes[this.numTypes++] = 0;
            this.prevX = this.currX = f2;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords++] = this.currX;
            this.prevY = this.currY = f3;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords++] = this.currY;
        }
    }

    public final void moveToRel(float f2, float f3) {
        if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
            this.prevX = this.currX += f2;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords - 2] = this.currX;
            this.prevY = this.currY += f3;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords - 1] = this.currY;
        } else {
            this.needRoom(true, 2);
            this.pointTypes[this.numTypes++] = 0;
            this.prevX = this.currX += f2;
            this.moveX = this.currX;
            this.floatCoords[this.numCoords++] = this.currX;
            this.prevY = this.currY += f3;
            this.moveY = this.currY;
            this.floatCoords[this.numCoords++] = this.currY;
        }
    }

    @Override
    public final void lineTo(float f2, float f3) {
        this.needRoom(true, 2);
        this.pointTypes[this.numTypes++] = 1;
        this.prevX = this.currX = f2;
        this.floatCoords[this.numCoords++] = this.currX;
        this.prevY = this.currY = f3;
        this.floatCoords[this.numCoords++] = this.currY;
    }

    public final void lineToRel(float f2, float f3) {
        this.needRoom(true, 2);
        this.pointTypes[this.numTypes++] = 1;
        this.prevX = this.currX += f2;
        this.floatCoords[this.numCoords++] = this.currX;
        this.prevY = this.currY += f3;
        this.floatCoords[this.numCoords++] = this.currY;
    }

    @Override
    public final void quadTo(float f2, float f3, float f4, float f5) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = f2;
        this.floatCoords[this.numCoords++] = this.prevY = f3;
        this.floatCoords[this.numCoords++] = this.currX = f4;
        this.floatCoords[this.numCoords++] = this.currY = f5;
    }

    public final void quadToRel(float f2, float f3, float f4, float f5) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX + f2;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY + f3;
        this.floatCoords[this.numCoords++] = this.currX += f4;
        this.floatCoords[this.numCoords++] = this.currY += f5;
    }

    public final void quadToSmooth(float f2, float f3) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.currX = f2;
        this.floatCoords[this.numCoords++] = this.currY = f3;
    }

    public final void quadToSmoothRel(float f2, float f3) {
        this.needRoom(true, 4);
        this.pointTypes[this.numTypes++] = 2;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.currX += f2;
        this.floatCoords[this.numCoords++] = this.currY += f3;
    }

    @Override
    public final void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = f2;
        this.floatCoords[this.numCoords++] = f3;
        this.floatCoords[this.numCoords++] = this.prevX = f4;
        this.floatCoords[this.numCoords++] = this.prevY = f5;
        this.floatCoords[this.numCoords++] = this.currX = f6;
        this.floatCoords[this.numCoords++] = this.currY = f7;
    }

    public final void curveToRel(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = this.currX + f2;
        this.floatCoords[this.numCoords++] = this.currY + f3;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX + f4;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY + f5;
        this.floatCoords[this.numCoords++] = this.currX += f6;
        this.floatCoords[this.numCoords++] = this.currY += f7;
    }

    public final void curveToSmooth(float f2, float f3, float f4, float f5) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.prevX = f2;
        this.floatCoords[this.numCoords++] = this.prevY = f3;
        this.floatCoords[this.numCoords++] = this.currX = f4;
        this.floatCoords[this.numCoords++] = this.currY = f5;
    }

    public final void curveToSmoothRel(float f2, float f3, float f4, float f5) {
        this.needRoom(true, 6);
        this.pointTypes[this.numTypes++] = 3;
        this.floatCoords[this.numCoords++] = this.currX * 2.0f - this.prevX;
        this.floatCoords[this.numCoords++] = this.currY * 2.0f - this.prevY;
        this.floatCoords[this.numCoords++] = this.prevX = this.currX + f2;
        this.floatCoords[this.numCoords++] = this.prevY = this.currY + f3;
        this.floatCoords[this.numCoords++] = this.currX += f4;
        this.floatCoords[this.numCoords++] = this.currY += f5;
    }

    public final void ovalQuadrantTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        this.appendOvalQuadrant(this.currX, this.currY, f2, f3, f4, f5, f6, f7, CornerPrefix.CORNER_ONLY);
    }

    public final void appendOvalQuadrant(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, CornerPrefix cornerPrefix) {
        if (!(f8 >= 0.0f && f8 <= f9 && f9 <= 1.0f)) {
            throw new IllegalArgumentException("0 <= tfrom <= tto <= 1 required");
        }
        float f10 = (float)((double)f2 + (double)(f4 - f2) * 0.5522847498307933);
        float f11 = (float)((double)f3 + (double)(f5 - f3) * 0.5522847498307933);
        float f12 = (float)((double)f6 + (double)(f4 - f6) * 0.5522847498307933);
        float f13 = (float)((double)f7 + (double)(f5 - f7) * 0.5522847498307933);
        if (f9 < 1.0f) {
            float f14 = 1.0f - f9;
            f6 += (f12 - f6) * f14;
            f7 += (f13 - f7) * f14;
            f12 += (f10 - f12) * f14;
            f13 += (f11 - f13) * f14;
            f10 += (f2 - f10) * f14;
            f11 += (f3 - f11) * f14;
            f6 += (f12 - f6) * f14;
            f7 += (f13 - f7) * f14;
            f12 += (f10 - f12) * f14;
            f13 += (f11 - f13) * f14;
            f6 += (f12 - f6) * f14;
            f7 += (f13 - f7) * f14;
        }
        if (f8 > 0.0f) {
            if (f9 < 1.0f) {
                f8 /= f9;
            }
            f2 += (f10 - f2) * f8;
            f3 += (f11 - f3) * f8;
            f10 += (f12 - f10) * f8;
            f11 += (f13 - f11) * f8;
            f12 += (f6 - f12) * f8;
            f13 += (f7 - f13) * f8;
            f2 += (f10 - f2) * f8;
            f3 += (f11 - f3) * f8;
            f10 += (f12 - f10) * f8;
            f11 += (f13 - f11) * f8;
            f2 += (f10 - f2) * f8;
            f3 += (f11 - f3) * f8;
        }
        if (cornerPrefix == CornerPrefix.MOVE_THEN_CORNER) {
            this.moveTo(f2, f3);
        } else if (cornerPrefix == CornerPrefix.LINE_THEN_CORNER && (this.numTypes == 1 || f2 != this.currX || f3 != this.currY)) {
            this.lineTo(f2, f3);
        }
        if (f8 == f9 || f2 == f10 && f10 == f12 && f12 == f6 && f3 == f11 && f11 == f13 && f13 == f7) {
            if (cornerPrefix != CornerPrefix.LINE_THEN_CORNER) {
                this.lineTo(f6, f7);
            }
        } else {
            this.curveTo(f10, f11, f12, f13, f6, f7);
        }
    }

    public void arcTo(float f2, float f3, float f4, boolean bl, boolean bl2, float f5, float f6) {
        double d2;
        double d3;
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        double d4 = Math.abs(f2);
        double d5 = Math.abs(f3);
        if (d4 == 0.0 || d5 == 0.0) {
            this.lineTo(f5, f6);
            return;
        }
        double d6 = this.currX;
        double d7 = this.currY;
        double d8 = f5;
        double d9 = f6;
        if (d6 == d8 && d7 == d9) {
            return;
        }
        if ((double)f4 == 0.0) {
            d3 = 1.0;
            d2 = 0.0;
        } else {
            d3 = Math.cos(f4);
            d2 = Math.sin(f4);
        }
        double d10 = (d6 + d8) / 2.0;
        double d11 = (d7 + d9) / 2.0;
        double d12 = d6 - d10;
        double d13 = d7 - d11;
        double d14 = (d3 * d12 + d2 * d13) / d4;
        double d15 = (d3 * d13 - d2 * d12) / d5;
        double d16 = d14 * d14 + d15 * d15;
        if (d16 >= 1.0) {
            double d17 = d15 * d4;
            double d18 = d14 * d5;
            if (bl2) {
                d17 = -d17;
            } else {
                d18 = -d18;
            }
            double d19 = d3 * d17 - d2 * d18;
            double d20 = d3 * d18 + d2 * d17;
            double d21 = d10 + d19;
            double d22 = d11 + d20;
            double d23 = d6 + d19;
            double d24 = d7 + d20;
            this.appendOvalQuadrant((float)d6, (float)d7, (float)d23, (float)d24, (float)d21, (float)d22, 0.0f, 1.0f, CornerPrefix.CORNER_ONLY);
            d23 = d8 + d19;
            d24 = d9 + d20;
            this.appendOvalQuadrant((float)d21, (float)d22, (float)d23, (float)d24, (float)d8, (float)d9, 0.0f, 1.0f, CornerPrefix.CORNER_ONLY);
            return;
        }
        double d25 = Math.sqrt((1.0 - d16) / d16);
        double d26 = d25 * d15;
        double d27 = d25 * d14;
        if (bl == bl2) {
            d26 = -d26;
        } else {
            d27 = -d27;
        }
        d10 += d3 * d26 * d4 - d2 * d27 * d5;
        d11 += d3 * d27 * d5 + d2 * d26 * d4;
        double d28 = d14 - d26;
        double d29 = d15 - d27;
        double d30 = -(d14 + d26);
        double d31 = -(d15 + d27);
        boolean bl3 = false;
        float f7 = 1.0f;
        boolean bl4 = false;
        do {
            double d32;
            double d33 = d29;
            double d34 = d28;
            if (bl2) {
                d33 = -d33;
            } else {
                d34 = -d34;
            }
            if (d33 * d30 + d34 * d31 > 0.0) {
                d32 = d28 * d30 + d29 * d31;
                if (d32 >= 0.0) {
                    f7 = (float)(Math.acos(d32) / 1.5707963267948966);
                    bl3 = true;
                }
                bl4 = true;
            } else if (bl4) break;
            d32 = d3 * d33 * d4 - d2 * d34 * d5;
            double d35 = d3 * d34 * d5 + d2 * d33 * d4;
            double d36 = d10 + d32;
            double d37 = d11 + d35;
            double d38 = d6 + d32;
            double d39 = d7 + d35;
            this.appendOvalQuadrant((float)d6, (float)d7, (float)d38, (float)d39, (float)d36, (float)d37, 0.0f, f7, CornerPrefix.CORNER_ONLY);
            d6 = d36;
            d7 = d37;
            d28 = d33;
            d29 = d34;
        } while (!bl3);
    }

    public void arcToRel(float f2, float f3, float f4, boolean bl, boolean bl2, float f5, float f6) {
        this.arcTo(f2, f3, f4, bl, bl2, this.currX + f5, this.currY + f6);
    }

    int pointCrossings(float f2, float f3) {
        float f4;
        float f5;
        float[] arrf = this.floatCoords;
        float f6 = f5 = arrf[0];
        float f7 = f4 = arrf[1];
        int n2 = 0;
        int n3 = 2;
        block7: for (int i2 = 1; i2 < this.numTypes; ++i2) {
            switch (this.pointTypes[i2]) {
                case 0: {
                    if (f7 != f4) {
                        n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f5, f4);
                    }
                    f5 = f6 = arrf[n3++];
                    f4 = f7 = arrf[n3++];
                    continue block7;
                }
                case 1: {
                    float f8 = arrf[n3++];
                    float f9 = arrf[n3++];
                    n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f8, f9);
                    f6 = f8;
                    f7 = f9;
                    continue block7;
                }
                case 2: {
                    int n4 = n3++;
                    int n5 = n3++;
                    float f8 = arrf[n3++];
                    float f9 = arrf[n3++];
                    n2 += Shape.pointCrossingsForQuad(f2, f3, f6, f7, arrf[n4], arrf[n5], f8, f9, 0);
                    f6 = f8;
                    f7 = f9;
                    continue block7;
                }
                case 3: {
                    int n6 = n3++;
                    int n7 = n3++;
                    int n8 = n3++;
                    int n9 = n3++;
                    float f8 = arrf[n3++];
                    float f9 = arrf[n3++];
                    n2 += Shape.pointCrossingsForCubic(f2, f3, f6, f7, arrf[n6], arrf[n7], arrf[n8], arrf[n9], f8, f9, 0);
                    f6 = f8;
                    f7 = f9;
                    continue block7;
                }
                case 4: {
                    if (f7 != f4) {
                        n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f5, f4);
                    }
                    f6 = f5;
                    f7 = f4;
                }
            }
        }
        if (f7 != f4) {
            n2 += Shape.pointCrossingsForLine(f2, f3, f6, f7, f5, f4);
        }
        return n2;
    }

    int rectCrossings(float f2, float f3, float f4, float f5) {
        float f6;
        float f7;
        float[] arrf = this.floatCoords;
        float f8 = f7 = arrf[0];
        float f9 = f6 = arrf[1];
        int n2 = 0;
        int n3 = 2;
        block7: for (int i2 = 1; n2 != Integer.MIN_VALUE && i2 < this.numTypes; ++i2) {
            switch (this.pointTypes[i2]) {
                case 0: {
                    if (f8 != f7 || f9 != f6) {
                        n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f7, f6);
                    }
                    f7 = f8 = arrf[n3++];
                    f6 = f9 = arrf[n3++];
                    continue block7;
                }
                case 1: {
                    float f10 = arrf[n3++];
                    float f11 = arrf[n3++];
                    n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f10, f11);
                    f8 = f10;
                    f9 = f11;
                    continue block7;
                }
                case 2: {
                    int n4 = n3++;
                    int n5 = n3++;
                    float f10 = arrf[n3++];
                    float f11 = arrf[n3++];
                    n2 = Shape.rectCrossingsForQuad(n2, f2, f3, f4, f5, f8, f9, arrf[n4], arrf[n5], f10, f11, 0);
                    f8 = f10;
                    f9 = f11;
                    continue block7;
                }
                case 3: {
                    int n6 = n3++;
                    int n7 = n3++;
                    int n8 = n3++;
                    int n9 = n3++;
                    float f10 = arrf[n3++];
                    float f11 = arrf[n3++];
                    n2 = Shape.rectCrossingsForCubic(n2, f2, f3, f4, f5, f8, f9, arrf[n6], arrf[n7], arrf[n8], arrf[n9], f10, f11, 0);
                    f8 = f10;
                    f9 = f11;
                    continue block7;
                }
                case 4: {
                    if (f8 != f7 || f9 != f6) {
                        n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f7, f6);
                    }
                    f8 = f7;
                    f9 = f6;
                }
            }
        }
        if (n2 != Integer.MIN_VALUE && (f8 != f7 || f9 != f6)) {
            n2 = Shape.rectCrossingsForLine(n2, f2, f3, f4, f5, f8, f9, f7, f6);
        }
        return n2;
    }

    public final void append(PathIterator pathIterator, boolean bl) {
        float[] arrf = new float[6];
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (!bl || this.numTypes < 1 || this.numCoords < 1) {
                        this.moveTo(arrf[0], arrf[1]);
                        break;
                    }
                    if (this.pointTypes[this.numTypes - 1] != 4 && this.floatCoords[this.numCoords - 2] == arrf[0] && this.floatCoords[this.numCoords - 1] == arrf[1]) break;
                }
                case 1: {
                    this.lineTo(arrf[0], arrf[1]);
                    break;
                }
                case 2: {
                    this.quadTo(arrf[0], arrf[1], arrf[2], arrf[3]);
                    break;
                }
                case 3: {
                    this.curveTo(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                    break;
                }
                case 4: {
                    this.closePath();
                }
            }
            pathIterator.next();
            bl = false;
        }
    }

    public final void transform(BaseTransform baseTransform) {
        if (this.numCoords == 0) {
            return;
        }
        this.needRoom(false, 6);
        this.floatCoords[this.numCoords + 0] = this.moveX;
        this.floatCoords[this.numCoords + 1] = this.moveY;
        this.floatCoords[this.numCoords + 2] = this.prevX;
        this.floatCoords[this.numCoords + 3] = this.prevY;
        this.floatCoords[this.numCoords + 4] = this.currX;
        this.floatCoords[this.numCoords + 5] = this.currY;
        baseTransform.transform(this.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2 + 3);
        this.moveX = this.floatCoords[this.numCoords + 0];
        this.moveY = this.floatCoords[this.numCoords + 1];
        this.prevX = this.floatCoords[this.numCoords + 2];
        this.prevY = this.floatCoords[this.numCoords + 3];
        this.currX = this.floatCoords[this.numCoords + 4];
        this.currY = this.floatCoords[this.numCoords + 5];
    }

    @Override
    public final RectBounds getBounds() {
        float f2;
        float f3;
        float f4;
        float f5;
        int n2 = this.numCoords;
        if (n2 > 0) {
            f4 = f5 = this.floatCoords[--n2];
            f2 = f3 = this.floatCoords[--n2];
            while (n2 > 0) {
                float f6;
                float f7 = this.floatCoords[--n2];
                if ((f6 = this.floatCoords[--n2]) < f2) {
                    f2 = f6;
                }
                if (f7 < f4) {
                    f4 = f7;
                }
                if (f6 > f3) {
                    f3 = f6;
                }
                if (!(f7 > f5)) continue;
                f5 = f7;
            }
        } else {
            f5 = 0.0f;
            f3 = 0.0f;
            f4 = 0.0f;
            f2 = 0.0f;
        }
        return new RectBounds(f2, f4, f3, f5);
    }

    public final int getNumCommands() {
        return this.numTypes;
    }

    public final byte[] getCommandsNoClone() {
        return this.pointTypes;
    }

    public final float[] getFloatCoordsNoClone() {
        return this.floatCoords;
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        if (baseTransform == null) {
            return new CopyIterator(this);
        }
        return new TxIterator(this, baseTransform);
    }

    @Override
    public final void closePath() {
        if (this.numTypes == 0 || this.pointTypes[this.numTypes - 1] != 4) {
            this.needRoom(true, 0);
            this.pointTypes[this.numTypes++] = 4;
            this.prevX = this.currX = this.moveX;
            this.prevY = this.currY = this.moveY;
        }
    }

    @Override
    public void pathDone() {
    }

    public final void append(Shape shape, boolean bl) {
        this.append(shape.getPathIterator(null), bl);
    }

    public final void appendSVGPath(String string) {
        SVGParser sVGParser = new SVGParser(string);
        sVGParser.allowcomma = false;
        while (!sVGParser.isDone()) {
            sVGParser.allowcomma = false;
            char c2 = sVGParser.getChar();
            switch (c2) {
                case 'M': {
                    this.moveTo(sVGParser.f(), sVGParser.f());
                    while (sVGParser.nextIsNumber()) {
                        this.lineTo(sVGParser.f(), sVGParser.f());
                    }
                    break;
                }
                case 'm': {
                    if (this.numTypes > 0) {
                        this.moveToRel(sVGParser.f(), sVGParser.f());
                    } else {
                        this.moveTo(sVGParser.f(), sVGParser.f());
                    }
                    while (sVGParser.nextIsNumber()) {
                        this.lineToRel(sVGParser.f(), sVGParser.f());
                    }
                    break;
                }
                case 'L': {
                    do {
                        this.lineTo(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'l': {
                    do {
                        this.lineToRel(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'H': {
                    do {
                        this.lineTo(sVGParser.f(), this.currY);
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'h': {
                    do {
                        this.lineToRel(sVGParser.f(), 0.0f);
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'V': {
                    do {
                        this.lineTo(this.currX, sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'v': {
                    do {
                        this.lineToRel(0.0f, sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'Q': {
                    do {
                        this.quadTo(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'q': {
                    do {
                        this.quadToRel(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'T': {
                    do {
                        this.quadToSmooth(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 't': {
                    do {
                        this.quadToSmoothRel(sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'C': {
                    do {
                        this.curveTo(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'c': {
                    do {
                        this.curveToRel(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'S': {
                    do {
                        this.curveToSmooth(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 's': {
                    do {
                        this.curveToSmoothRel(sVGParser.f(), sVGParser.f(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'A': {
                    do {
                        this.arcTo(sVGParser.f(), sVGParser.f(), sVGParser.a(), sVGParser.b(), sVGParser.b(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'a': {
                    do {
                        this.arcToRel(sVGParser.f(), sVGParser.f(), sVGParser.a(), sVGParser.b(), sVGParser.b(), sVGParser.f(), sVGParser.f());
                    } while (sVGParser.nextIsNumber());
                    break;
                }
                case 'Z': 
                case 'z': {
                    this.closePath();
                    break;
                }
                default: {
                    throw new IllegalArgumentException("invalid command (" + c2 + ") in SVG path at pos=" + sVGParser.pos);
                }
            }
            sVGParser.allowcomma = false;
        }
    }

    public final int getWindingRule() {
        return this.windingRule;
    }

    public final void setWindingRule(int n2) {
        if (n2 != 0 && n2 != 1) {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        }
        this.windingRule = n2;
    }

    public final Point2D getCurrentPoint() {
        if (this.numTypes < 1) {
            return null;
        }
        return new Point2D(this.currX, this.currY);
    }

    public final float getCurrentX() {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("no current point in empty path");
        }
        return this.currX;
    }

    public final float getCurrentY() {
        if (this.numTypes < 1) {
            throw new IllegalPathStateException("no current point in empty path");
        }
        return this.currY;
    }

    public final void reset() {
        this.numCoords = 0;
        this.numTypes = 0;
        this.currY = 0.0f;
        this.currX = 0.0f;
        this.prevY = 0.0f;
        this.prevX = 0.0f;
        this.moveY = 0.0f;
        this.moveX = 0.0f;
    }

    public final Shape createTransformedShape(BaseTransform baseTransform) {
        return new Path2D(this, baseTransform);
    }

    @Override
    public Path2D copy() {
        return new Path2D(this);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Path2D) {
            Path2D path2D = (Path2D)object;
            if (path2D.numTypes == this.numTypes && path2D.numCoords == this.numCoords && path2D.windingRule == this.windingRule) {
                int n2;
                for (n2 = 0; n2 < this.numTypes; ++n2) {
                    if (path2D.pointTypes[n2] == this.pointTypes[n2]) continue;
                    return false;
                }
                for (n2 = 0; n2 < this.numCoords; ++n2) {
                    if (path2D.floatCoords[n2] == this.floatCoords[n2]) continue;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int n2;
        int n3 = 7;
        n3 = 11 * n3 + this.numTypes;
        n3 = 11 * n3 + this.numCoords;
        n3 = 11 * n3 + this.windingRule;
        for (n2 = 0; n2 < this.numTypes; ++n2) {
            n3 = 11 * n3 + this.pointTypes[n2];
        }
        for (n2 = 0; n2 < this.numCoords; ++n2) {
            n3 = 11 * n3 + Float.floatToIntBits(this.floatCoords[n2]);
        }
        return n3;
    }

    public static boolean contains(PathIterator pathIterator, float f2, float f3) {
        if (f2 * 0.0f + f3 * 0.0f == 0.0f) {
            int n2 = pathIterator.getWindingRule() == 1 ? -1 : 1;
            int n3 = Shape.pointCrossingsForPath(pathIterator, f2, f3);
            return (n3 & n2) != 0;
        }
        return false;
    }

    public static boolean contains(PathIterator pathIterator, Point2D point2D) {
        return Path2D.contains(pathIterator, point2D.x, point2D.y);
    }

    @Override
    public final boolean contains(float f2, float f3) {
        if (f2 * 0.0f + f3 * 0.0f == 0.0f) {
            if (this.numTypes < 2) {
                return false;
            }
            int n2 = this.windingRule == 1 ? -1 : 1;
            return (this.pointCrossings(f2, f3) & n2) != 0;
        }
        return false;
    }

    @Override
    public final boolean contains(Point2D point2D) {
        return this.contains(point2D.x, point2D.y);
    }

    public static boolean contains(PathIterator pathIterator, float f2, float f3, float f4, float f5) {
        if (Float.isNaN(f2 + f4) || Float.isNaN(f3 + f5)) {
            return false;
        }
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        int n2 = pathIterator.getWindingRule() == 1 ? -1 : 2;
        int n3 = Shape.rectCrossingsForPath(pathIterator, f2, f3, f2 + f4, f3 + f5);
        return n3 != Integer.MIN_VALUE && (n3 & n2) != 0;
    }

    @Override
    public final boolean contains(float f2, float f3, float f4, float f5) {
        if (Float.isNaN(f2 + f4) || Float.isNaN(f3 + f5)) {
            return false;
        }
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        int n2 = this.windingRule == 1 ? -1 : 2;
        int n3 = this.rectCrossings(f2, f3, f2 + f4, f3 + f5);
        return n3 != Integer.MIN_VALUE && (n3 & n2) != 0;
    }

    public static boolean intersects(PathIterator pathIterator, float f2, float f3, float f4, float f5) {
        if (Float.isNaN(f2 + f4) || Float.isNaN(f3 + f5)) {
            return false;
        }
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        int n2 = pathIterator.getWindingRule() == 1 ? -1 : 2;
        int n3 = Shape.rectCrossingsForPath(pathIterator, f2, f3, f2 + f4, f3 + f5);
        return n3 == Integer.MIN_VALUE || (n3 & n2) != 0;
    }

    @Override
    public final boolean intersects(float f2, float f3, float f4, float f5) {
        if (Float.isNaN(f2 + f4) || Float.isNaN(f3 + f5)) {
            return false;
        }
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return false;
        }
        int n2 = this.windingRule == 1 ? -1 : 2;
        int n3 = this.rectCrossings(f2, f3, f2 + f4, f3 + f5);
        return n3 == Integer.MIN_VALUE || (n3 & n2) != 0;
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
        return new FlatteningPathIterator(this.getPathIterator(baseTransform), f2);
    }

    static byte[] copyOf(byte[] arrby, int n2) {
        byte[] arrby2 = new byte[n2];
        System.arraycopy(arrby, 0, arrby2, 0, Math.min(arrby.length, n2));
        return arrby2;
    }

    static float[] copyOf(float[] arrf, int n2) {
        float[] arrf2 = new float[n2];
        System.arraycopy(arrf, 0, arrf2, 0, Math.min(arrf.length, n2));
        return arrf2;
    }

    public void setTo(Path2D path2D) {
        this.numTypes = path2D.numTypes;
        this.numCoords = path2D.numCoords;
        if (this.numTypes > this.pointTypes.length) {
            this.pointTypes = new byte[this.numTypes];
        }
        System.arraycopy(path2D.pointTypes, 0, this.pointTypes, 0, this.numTypes);
        if (this.numCoords > this.floatCoords.length) {
            this.floatCoords = new float[this.numCoords];
        }
        System.arraycopy(path2D.floatCoords, 0, this.floatCoords, 0, this.numCoords);
        this.windingRule = path2D.windingRule;
        this.moveX = path2D.moveX;
        this.moveY = path2D.moveY;
        this.prevX = path2D.prevX;
        this.prevY = path2D.prevY;
        this.currX = path2D.currX;
        this.currY = path2D.currY;
    }

    static abstract class Iterator
    implements PathIterator {
        int typeIdx;
        int pointIdx;
        Path2D path;

        Iterator(Path2D path2D) {
            this.path = path2D;
        }

        @Override
        public int getWindingRule() {
            return this.path.getWindingRule();
        }

        @Override
        public boolean isDone() {
            return this.typeIdx >= this.path.numTypes;
        }

        @Override
        public void next() {
            byte by = this.path.pointTypes[this.typeIdx++];
            this.pointIdx += curvecoords[by];
        }
    }

    static class SVGParser {
        final String svgpath;
        final int len;
        int pos;
        boolean allowcomma;

        public SVGParser(String string) {
            this.svgpath = string;
            this.len = string.length();
        }

        public boolean isDone() {
            return this.toNextNonWsp() >= this.len;
        }

        public char getChar() {
            return this.svgpath.charAt(this.pos++);
        }

        public boolean nextIsNumber() {
            if (this.toNextNonWsp() < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '+': 
                    case '-': 
                    case '.': 
                    case '0': 
                    case '1': 
                    case '2': 
                    case '3': 
                    case '4': 
                    case '5': 
                    case '6': 
                    case '7': 
                    case '8': 
                    case '9': {
                        return true;
                    }
                }
            }
            return false;
        }

        public float f() {
            return this.getFloat();
        }

        public float a() {
            return (float)Math.toRadians(this.getFloat());
        }

        public float getFloat() {
            int n2 = this.toNextNonWsp();
            this.allowcomma = true;
            int n3 = this.toNumberEnd();
            if (n2 < n3) {
                String string = this.svgpath.substring(n2, n3);
                try {
                    return Float.parseFloat(string);
                }
                catch (NumberFormatException numberFormatException) {
                    throw new IllegalArgumentException("invalid float (" + string + ") in path at pos=" + n2);
                }
            }
            throw new IllegalArgumentException("end of path looking for float");
        }

        public boolean b() {
            this.toNextNonWsp();
            this.allowcomma = true;
            if (this.pos < this.len) {
                char c2 = this.svgpath.charAt(this.pos);
                switch (c2) {
                    case '0': {
                        ++this.pos;
                        return false;
                    }
                    case '1': {
                        ++this.pos;
                        return true;
                    }
                }
                throw new IllegalArgumentException("invalid boolean flag (" + c2 + ") in path at pos=" + this.pos);
            }
            throw new IllegalArgumentException("end of path looking for boolean");
        }

        private int toNextNonWsp() {
            boolean bl = this.allowcomma;
            while (this.pos < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case ',': {
                        if (!bl) {
                            return this.pos;
                        }
                        bl = false;
                        break;
                    }
                    case '\t': 
                    case '\n': 
                    case '\r': 
                    case ' ': {
                        break;
                    }
                    default: {
                        return this.pos;
                    }
                }
                ++this.pos;
            }
            return this.pos;
        }

        private int toNumberEnd() {
            boolean bl = true;
            boolean bl2 = false;
            boolean bl3 = false;
            while (this.pos < this.len) {
                switch (this.svgpath.charAt(this.pos)) {
                    case '+': 
                    case '-': {
                        if (!bl) {
                            return this.pos;
                        }
                        bl = false;
                        break;
                    }
                    case '0': 
                    case '1': 
                    case '2': 
                    case '3': 
                    case '4': 
                    case '5': 
                    case '6': 
                    case '7': 
                    case '8': 
                    case '9': {
                        bl = false;
                        break;
                    }
                    case 'E': 
                    case 'e': {
                        if (bl2) {
                            return this.pos;
                        }
                        bl = true;
                        bl2 = true;
                        break;
                    }
                    case '.': {
                        if (bl2 || bl3) {
                            return this.pos;
                        }
                        bl3 = true;
                        bl = false;
                        break;
                    }
                    default: {
                        return this.pos;
                    }
                }
                ++this.pos;
            }
            return this.pos;
        }
    }

    static class TxIterator
    extends Iterator {
        float[] floatCoords;
        BaseTransform transform;

        TxIterator(Path2D path2D, BaseTransform baseTransform) {
            super(path2D);
            this.floatCoords = path2D.floatCoords;
            this.transform = baseTransform;
        }

        @Override
        public int currentSegment(float[] arrf) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n2 = curvecoords[by];
            if (n2 > 0) {
                this.transform.transform(this.floatCoords, this.pointIdx, arrf, 0, n2 / 2);
            }
            return by;
        }

        public int currentSegment(double[] arrd) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n2 = curvecoords[by];
            if (n2 > 0) {
                this.transform.transform(this.floatCoords, this.pointIdx, arrd, 0, n2 / 2);
            }
            return by;
        }
    }

    static class CopyIterator
    extends Iterator {
        float[] floatCoords;

        CopyIterator(Path2D path2D) {
            super(path2D);
            this.floatCoords = path2D.floatCoords;
        }

        @Override
        public int currentSegment(float[] arrf) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n2 = curvecoords[by];
            if (n2 > 0) {
                System.arraycopy(this.floatCoords, this.pointIdx, arrf, 0, n2);
            }
            return by;
        }

        public int currentSegment(double[] arrd) {
            byte by = this.path.pointTypes[this.typeIdx];
            int n2 = curvecoords[by];
            if (n2 > 0) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    arrd[i2] = this.floatCoords[this.pointIdx + i2];
                }
            }
            return by;
        }
    }

    public static enum CornerPrefix {
        CORNER_ONLY,
        MOVE_THEN_CORNER,
        LINE_THEN_CORNER;

    }
}

