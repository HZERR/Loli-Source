/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;

public final class Edge {
    static final int INIT_PARTS = 4;
    static final int GROW_PARTS = 10;
    Curve curve;
    int ctag;
    int etag;
    double activey;
    int equivalence;
    private Edge lastEdge;
    private int lastResult;
    private double lastLimit;

    public Edge(Curve curve, int n2) {
        this(curve, n2, 0);
    }

    public Edge(Curve curve, int n2, int n3) {
        this.curve = curve;
        this.ctag = n2;
        this.etag = n3;
    }

    public Curve getCurve() {
        return this.curve;
    }

    public int getCurveTag() {
        return this.ctag;
    }

    public int getEdgeTag() {
        return this.etag;
    }

    public void setEdgeTag(int n2) {
        this.etag = n2;
    }

    public int getEquivalence() {
        return this.equivalence;
    }

    public void setEquivalence(int n2) {
        this.equivalence = n2;
    }

    public int compareTo(Edge edge, double[] arrd) {
        if (edge == this.lastEdge && arrd[0] < this.lastLimit) {
            if (arrd[1] > this.lastLimit) {
                arrd[1] = this.lastLimit;
            }
            return this.lastResult;
        }
        if (this == edge.lastEdge && arrd[0] < edge.lastLimit) {
            if (arrd[1] > edge.lastLimit) {
                arrd[1] = edge.lastLimit;
            }
            return 0 - edge.lastResult;
        }
        int n2 = this.curve.compareTo(edge.curve, arrd);
        this.lastEdge = edge;
        this.lastLimit = arrd[1];
        this.lastResult = n2;
        return n2;
    }

    public void record(double d2, int n2) {
        this.activey = d2;
        this.etag = n2;
    }

    public boolean isActiveFor(double d2, int n2) {
        return this.etag == n2 && this.activey >= d2;
    }

    public String toString() {
        return "Edge[" + this.curve + ", " + (this.ctag == 0 ? "L" : "R") + ", " + (this.etag == 1 ? "I" : (this.etag == -1 ? "O" : "N")) + "]";
    }
}

