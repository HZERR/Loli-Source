/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Crossings;
import com.sun.javafx.geom.Order0;
import com.sun.javafx.geom.Order1;
import com.sun.javafx.geom.Order2;
import com.sun.javafx.geom.Order3;
import com.sun.javafx.geom.RectBounds;
import java.util.Vector;

public abstract class Curve {
    public static final int INCREASING = 1;
    public static final int DECREASING = -1;
    protected int direction;
    public static final double TMIN = 0.001;

    public static void insertMove(Vector vector, double d2, double d3) {
        vector.add(new Order0(d2, d3));
    }

    public static void insertLine(Vector vector, double d2, double d3, double d4, double d5) {
        if (d3 < d5) {
            vector.add(new Order1(d2, d3, d4, d5, 1));
        } else if (d3 > d5) {
            vector.add(new Order1(d4, d5, d2, d3, -1));
        }
    }

    public static void insertQuad(Vector vector, double[] arrd, double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d3 > d7) {
            Order2.insert(vector, arrd, d6, d7, d4, d5, d2, d3, -1);
        } else {
            if (d3 == d7 && d3 == d5) {
                return;
            }
            Order2.insert(vector, arrd, d2, d3, d4, d5, d6, d7, 1);
        }
    }

    public static void insertCubic(Vector vector, double[] arrd, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        if (d3 > d9) {
            Order3.insert(vector, arrd, d8, d9, d6, d7, d4, d5, d2, d3, -1);
        } else {
            if (d3 == d9 && d3 == d5 && d3 == d7) {
                return;
            }
            Order3.insert(vector, arrd, d2, d3, d4, d5, d6, d7, d8, d9, 1);
        }
    }

    public Curve(int n2) {
        this.direction = n2;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final Curve getWithDirection(int n2) {
        return this.direction == n2 ? this : this.getReversedCurve();
    }

    public static double round(double d2) {
        return d2;
    }

    public static int orderof(double d2, double d3) {
        if (d2 < d3) {
            return -1;
        }
        if (d2 > d3) {
            return 1;
        }
        return 0;
    }

    public static long signeddiffbits(double d2, double d3) {
        return Double.doubleToLongBits(d2) - Double.doubleToLongBits(d3);
    }

    public static long diffbits(double d2, double d3) {
        return Math.abs(Double.doubleToLongBits(d2) - Double.doubleToLongBits(d3));
    }

    public static double prev(double d2) {
        return Double.longBitsToDouble(Double.doubleToLongBits(d2) - 1L);
    }

    public static double next(double d2) {
        return Double.longBitsToDouble(Double.doubleToLongBits(d2) + 1L);
    }

    public String toString() {
        return "Curve[" + this.getOrder() + ", " + "(" + Curve.round(this.getX0()) + ", " + Curve.round(this.getY0()) + "), " + this.controlPointString() + "(" + Curve.round(this.getX1()) + ", " + Curve.round(this.getY1()) + "), " + (this.direction == 1 ? "D" : "U") + "]";
    }

    public String controlPointString() {
        return "";
    }

    public abstract int getOrder();

    public abstract double getXTop();

    public abstract double getYTop();

    public abstract double getXBot();

    public abstract double getYBot();

    public abstract double getXMin();

    public abstract double getXMax();

    public abstract double getX0();

    public abstract double getY0();

    public abstract double getX1();

    public abstract double getY1();

    public abstract double XforY(double var1);

    public abstract double TforY(double var1);

    public abstract double XforT(double var1);

    public abstract double YforT(double var1);

    public abstract double dXforT(double var1, int var3);

    public abstract double dYforT(double var1, int var3);

    public abstract double nextVertical(double var1, double var3);

    public int crossingsFor(double d2, double d3) {
        if (d3 >= this.getYTop() && d3 < this.getYBot() && d2 < this.getXMax() && (d2 < this.getXMin() || d2 < this.XforY(d3))) {
            return 1;
        }
        return 0;
    }

    public boolean accumulateCrossings(Crossings crossings) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6 = crossings.getXHi();
        if (this.getXMin() >= d6) {
            return false;
        }
        double d7 = crossings.getXLo();
        double d8 = crossings.getYLo();
        double d9 = crossings.getYHi();
        double d10 = this.getYTop();
        double d11 = this.getYBot();
        if (d10 < d8) {
            if (d11 <= d8) {
                return false;
            }
            d5 = d8;
            d4 = this.TforY(d8);
        } else {
            if (d10 >= d9) {
                return false;
            }
            d5 = d10;
            d4 = 0.0;
        }
        if (d11 > d9) {
            d3 = d9;
            d2 = this.TforY(d9);
        } else {
            d3 = d11;
            d2 = 1.0;
        }
        boolean bl = false;
        boolean bl2 = false;
        while (true) {
            double d12;
            if ((d12 = this.XforT(d4)) < d6) {
                if (bl2 || d12 > d7) {
                    return true;
                }
                bl = true;
            } else {
                if (bl) {
                    return true;
                }
                bl2 = true;
            }
            if (d4 >= d2) break;
            d4 = this.nextVertical(d4, d2);
        }
        if (bl) {
            crossings.record(d5, d3, this.direction);
        }
        return false;
    }

    public abstract void enlarge(RectBounds var1);

    public Curve getSubCurve(double d2, double d3) {
        return this.getSubCurve(d2, d3, this.direction);
    }

    public abstract Curve getReversedCurve();

    public abstract Curve getSubCurve(double var1, double var3, int var5);

    public int compareTo(Curve curve, double[] arrd) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10 = arrd[0];
        double d11 = arrd[1];
        if ((d11 = Math.min(Math.min(d11, this.getYBot()), curve.getYBot())) <= arrd[0]) {
            System.err.println("this == " + this);
            System.err.println("that == " + curve);
            System.out.println("target range = " + arrd[0] + "=>" + arrd[1]);
            throw new InternalError("backstepping from " + arrd[0] + " to " + d11);
        }
        arrd[1] = d11;
        if (this.getXMax() <= curve.getXMin()) {
            if (this.getXMin() == curve.getXMax()) {
                return 0;
            }
            return -1;
        }
        if (this.getXMin() >= curve.getXMax()) {
            return 1;
        }
        double d12 = this.TforY(d10);
        double d13 = this.YforT(d12);
        if (d13 < d10) {
            d12 = this.refineTforY(d12, d10);
            d13 = this.YforT(d12);
        }
        if (this.YforT(d9 = this.TforY(d11)) < d10) {
            d9 = this.refineTforY(d9, d10);
        }
        if ((d8 = curve.YforT(d7 = curve.TforY(d10))) < d10) {
            d7 = curve.refineTforY(d7, d10);
            d8 = curve.YforT(d7);
        }
        if (curve.YforT(d6 = curve.TforY(d11)) < d10) {
            d6 = curve.refineTforY(d6, d10);
        }
        double d14 = this.XforT(d12);
        double d15 = curve.XforT(d7);
        double d16 = Math.max(Math.abs(d10), Math.abs(d11));
        double d17 = Math.max(d16 * 1.0E-14, 1.0E-300);
        if (this.fairlyClose(d14, d15)) {
            d5 = d17;
            d4 = Math.min(d17 * 1.0E13, (d11 - d10) * 0.1);
            for (d3 = d10 + d5; d3 <= d11; d3 += d5) {
                if (this.fairlyClose(this.XforY(d3), curve.XforY(d3))) {
                    double d18;
                    d5 *= 2.0;
                    if (!(d18 > d4)) continue;
                    d5 = d4;
                    continue;
                }
                d3 -= d5;
                while (!((d2 = d3 + (d5 /= 2.0)) <= d3)) {
                    if (!this.fairlyClose(this.XforY(d2), curve.XforY(d2))) continue;
                    d3 = d2;
                }
                break;
            }
            if (d3 > d10) {
                if (d3 < d11) {
                    arrd[1] = d3;
                }
                return 0;
            }
        }
        if (d17 <= 0.0) {
            System.out.println("ymin = " + d17);
        }
        while (d12 < d9 && d7 < d6) {
            d5 = this.nextVertical(d12, d9);
            d4 = this.XforT(d5);
            d3 = this.YforT(d5);
            d2 = curve.nextVertical(d7, d6);
            double d19 = curve.XforT(d2);
            double d20 = curve.YforT(d2);
            try {
                if (this.findIntersect(curve, arrd, d17, 0, 0, d12, d14, d13, d5, d4, d3, d7, d15, d8, d2, d19, d20)) {
                    break;
                }
            }
            catch (Throwable throwable) {
                System.err.println("Error: " + throwable);
                System.err.println("y range was " + arrd[0] + "=>" + arrd[1]);
                System.err.println("s y range is " + d13 + "=>" + d3);
                System.err.println("t y range is " + d8 + "=>" + d20);
                System.err.println("ymin is " + d17);
                return 0;
            }
            if (d3 < d20) {
                if (d3 > arrd[0]) {
                    if (!(d3 < arrd[1])) break;
                    arrd[1] = d3;
                    break;
                }
                d12 = d5;
                d14 = d4;
                d13 = d3;
                continue;
            }
            if (d20 > arrd[0]) {
                if (!(d20 < arrd[1])) break;
                arrd[1] = d20;
                break;
            }
            d7 = d2;
            d15 = d19;
            d8 = d20;
        }
        d5 = (arrd[0] + arrd[1]) / 2.0;
        return Curve.orderof(this.XforY(d5), curve.XforY(d5));
    }

    public boolean findIntersect(Curve curve, double[] arrd, double d2, int n2, int n3, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14) {
        if (d5 > d14 || d11 > d8) {
            return false;
        }
        if (Math.min(d4, d7) > Math.max(d10, d13) || Math.max(d4, d7) < Math.min(d10, d13)) {
            return false;
        }
        if (d6 - d3 > 0.001) {
            double d15 = (d3 + d6) / 2.0;
            double d16 = this.XforT(d15);
            double d17 = this.YforT(d15);
            if (d15 == d3 || d15 == d6) {
                System.out.println("s0 = " + d3);
                System.out.println("s1 = " + d6);
                throw new InternalError("no s progress!");
            }
            if (d12 - d9 > 0.001) {
                double d18 = (d9 + d12) / 2.0;
                double d19 = curve.XforT(d18);
                double d20 = curve.YforT(d18);
                if (d18 == d9 || d18 == d12) {
                    System.out.println("t0 = " + d9);
                    System.out.println("t1 = " + d12);
                    throw new InternalError("no t progress!");
                }
                if (d17 >= d11 && d20 >= d5 && this.findIntersect(curve, arrd, d2, n2 + 1, n3 + 1, d3, d4, d5, d15, d16, d17, d9, d10, d11, d18, d19, d20)) {
                    return true;
                }
                if (d17 >= d20 && this.findIntersect(curve, arrd, d2, n2 + 1, n3 + 1, d3, d4, d5, d15, d16, d17, d18, d19, d20, d12, d13, d14)) {
                    return true;
                }
                if (d20 >= d17 && this.findIntersect(curve, arrd, d2, n2 + 1, n3 + 1, d15, d16, d17, d6, d7, d8, d9, d10, d11, d18, d19, d20)) {
                    return true;
                }
                if (d8 >= d20 && d14 >= d17 && this.findIntersect(curve, arrd, d2, n2 + 1, n3 + 1, d15, d16, d17, d6, d7, d8, d18, d19, d20, d12, d13, d14)) {
                    return true;
                }
            } else {
                if (d17 >= d11 && this.findIntersect(curve, arrd, d2, n2 + 1, n3, d3, d4, d5, d15, d16, d17, d9, d10, d11, d12, d13, d14)) {
                    return true;
                }
                if (d14 >= d17 && this.findIntersect(curve, arrd, d2, n2 + 1, n3, d15, d16, d17, d6, d7, d8, d9, d10, d11, d12, d13, d14)) {
                    return true;
                }
            }
        } else if (d12 - d9 > 0.001) {
            double d21 = (d9 + d12) / 2.0;
            double d22 = curve.XforT(d21);
            double d23 = curve.YforT(d21);
            if (d21 == d9 || d21 == d12) {
                System.out.println("t0 = " + d9);
                System.out.println("t1 = " + d12);
                throw new InternalError("no t progress!");
            }
            if (d23 >= d5 && this.findIntersect(curve, arrd, d2, n2, n3 + 1, d3, d4, d5, d6, d7, d8, d9, d10, d11, d21, d22, d23)) {
                return true;
            }
            if (d8 >= d23 && this.findIntersect(curve, arrd, d2, n2, n3 + 1, d3, d4, d5, d6, d7, d8, d21, d22, d23, d12, d13, d14)) {
                return true;
            }
        } else {
            double d24 = d7 - d4;
            double d25 = d8 - d5;
            double d26 = d13 - d10;
            double d27 = d14 - d11;
            double d28 = d10 - d4;
            double d29 = d11 - d5;
            double d30 = d26 * d25 - d27 * d24;
            if (d30 != 0.0) {
                double d31 = 1.0 / d30;
                double d32 = (d26 * d29 - d27 * d28) * d31;
                double d33 = (d24 * d29 - d25 * d28) * d31;
                if (d32 >= 0.0 && d32 <= 1.0 && d33 >= 0.0 && d33 <= 1.0) {
                    double d34;
                    d32 = d3 + d32 * (d6 - d3);
                    d33 = d9 + d33 * (d12 - d9);
                    if (d32 < 0.0 || d32 > 1.0 || d33 < 0.0 || d33 > 1.0) {
                        System.out.println("Uh oh!");
                    }
                    if ((d34 = (this.YforT(d32) + curve.YforT(d33)) / 2.0) <= arrd[1] && d34 > arrd[0]) {
                        arrd[1] = d34;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public double refineTforY(double d2, double d3) {
        double d4 = 1.0;
        while (true) {
            double d5;
            if ((d5 = (d2 + d4) / 2.0) == d2 || d5 == d4) {
                return d4;
            }
            double d6 = this.YforT(d5);
            if (d6 < d3) {
                d2 = d5;
                continue;
            }
            if (!(d6 > d3)) break;
            d4 = d5;
        }
        return d4;
    }

    public boolean fairlyClose(double d2, double d3) {
        return Math.abs(d2 - d3) < Math.max(Math.abs(d2), Math.abs(d3)) * 1.0E-10;
    }

    public abstract int getSegment(float[] var1);
}

