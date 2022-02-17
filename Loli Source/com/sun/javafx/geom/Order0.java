/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Crossings;
import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.RectBounds;

final class Order0
extends Curve {
    private double x;
    private double y;

    public Order0(double d2, double d3) {
        super(1);
        this.x = d2;
        this.y = d3;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public double getXTop() {
        return this.x;
    }

    @Override
    public double getYTop() {
        return this.y;
    }

    @Override
    public double getXBot() {
        return this.x;
    }

    @Override
    public double getYBot() {
        return this.y;
    }

    @Override
    public double getXMin() {
        return this.x;
    }

    @Override
    public double getXMax() {
        return this.x;
    }

    @Override
    public double getX0() {
        return this.x;
    }

    @Override
    public double getY0() {
        return this.y;
    }

    @Override
    public double getX1() {
        return this.x;
    }

    @Override
    public double getY1() {
        return this.y;
    }

    @Override
    public double XforY(double d2) {
        return d2;
    }

    @Override
    public double TforY(double d2) {
        return 0.0;
    }

    @Override
    public double XforT(double d2) {
        return this.x;
    }

    @Override
    public double YforT(double d2) {
        return this.y;
    }

    @Override
    public double dXforT(double d2, int n2) {
        return 0.0;
    }

    @Override
    public double dYforT(double d2, int n2) {
        return 0.0;
    }

    @Override
    public double nextVertical(double d2, double d3) {
        return d3;
    }

    @Override
    public int crossingsFor(double d2, double d3) {
        return 0;
    }

    @Override
    public boolean accumulateCrossings(Crossings crossings) {
        return this.x > crossings.getXLo() && this.x < crossings.getXHi() && this.y > crossings.getYLo() && this.y < crossings.getYHi();
    }

    @Override
    public void enlarge(RectBounds rectBounds) {
        rectBounds.add((float)this.x, (float)this.y);
    }

    @Override
    public Curve getSubCurve(double d2, double d3, int n2) {
        return this;
    }

    @Override
    public Curve getReversedCurve() {
        return this;
    }

    @Override
    public int getSegment(float[] arrf) {
        arrf[0] = (float)this.x;
        arrf[1] = (float)this.y;
        return 0;
    }
}

