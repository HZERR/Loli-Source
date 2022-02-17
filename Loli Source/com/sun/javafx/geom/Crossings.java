/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import java.util.Enumeration;
import java.util.Vector;

public abstract class Crossings {
    public static final boolean debug = false;
    int limit = 0;
    double[] yranges = new double[10];
    double xlo;
    double ylo;
    double xhi;
    double yhi;

    public Crossings(double d2, double d3, double d4, double d5) {
        this.xlo = d2;
        this.ylo = d3;
        this.xhi = d4;
        this.yhi = d5;
    }

    public final double getXLo() {
        return this.xlo;
    }

    public final double getYLo() {
        return this.ylo;
    }

    public final double getXHi() {
        return this.xhi;
    }

    public final double getYHi() {
        return this.yhi;
    }

    public abstract void record(double var1, double var3, int var5);

    public void print() {
        System.out.println("Crossings [");
        System.out.println("  bounds = [" + this.ylo + ", " + this.yhi + "]");
        for (int i2 = 0; i2 < this.limit; i2 += 2) {
            System.out.println("  [" + this.yranges[i2] + ", " + this.yranges[i2 + 1] + "]");
        }
        System.out.println("]");
    }

    public final boolean isEmpty() {
        return this.limit == 0;
    }

    public abstract boolean covers(double var1, double var3);

    public static Crossings findCrossings(Vector vector, double d2, double d3, double d4, double d5) {
        EvenOdd evenOdd = new EvenOdd(d2, d3, d4, d5);
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            Curve curve = (Curve)enumeration.nextElement();
            if (!curve.accumulateCrossings(evenOdd)) continue;
            return null;
        }
        return evenOdd;
    }

    public static final class EvenOdd
    extends Crossings {
        public EvenOdd(double d2, double d3, double d4, double d5) {
            super(d2, d3, d4, d5);
        }

        @Override
        public final boolean covers(double d2, double d3) {
            return this.limit == 2 && this.yranges[0] <= d2 && this.yranges[1] >= d3;
        }

        @Override
        public void record(double d2, double d3, int n2) {
            int n3;
            if (d2 >= d3) {
                return;
            }
            for (n3 = 0; n3 < this.limit && d2 > this.yranges[n3 + 1]; n3 += 2) {
            }
            int n4 = n3;
            while (n3 < this.limit) {
                double d4;
                double d5;
                double d6;
                double d7;
                double d8 = this.yranges[n3++];
                double d9 = this.yranges[n3++];
                if (d3 < d8) {
                    this.yranges[n4++] = d2;
                    this.yranges[n4++] = d3;
                    d2 = d8;
                    d3 = d9;
                    continue;
                }
                if (d2 < d8) {
                    d7 = d2;
                    d6 = d8;
                } else {
                    d7 = d8;
                    d6 = d2;
                }
                if (d3 < d9) {
                    d5 = d3;
                    d4 = d9;
                } else {
                    d5 = d9;
                    d4 = d3;
                }
                if (d6 == d5) {
                    d2 = d7;
                    d3 = d4;
                } else {
                    if (d6 > d5) {
                        d2 = d5;
                        d5 = d6;
                        d6 = d2;
                    }
                    if (d7 != d6) {
                        this.yranges[n4++] = d7;
                        this.yranges[n4++] = d6;
                    }
                    d2 = d5;
                    d3 = d4;
                }
                if (!(d2 >= d3)) continue;
                break;
            }
            if (n4 < n3 && n3 < this.limit) {
                System.arraycopy(this.yranges, n3, this.yranges, n4, this.limit - n3);
            }
            n4 += this.limit - n3;
            if (d2 < d3) {
                if (n4 >= this.yranges.length) {
                    double[] arrd = new double[n4 + 10];
                    System.arraycopy(this.yranges, 0, arrd, 0, n4);
                    this.yranges = arrd;
                }
                this.yranges[n4++] = d2;
                this.yranges[n4++] = d3;
            }
            this.limit = n4;
        }
    }
}

