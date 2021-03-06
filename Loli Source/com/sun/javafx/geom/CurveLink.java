/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.Order0;

final class CurveLink {
    Curve curve;
    double ytop;
    double ybot;
    int etag;
    CurveLink next;

    public CurveLink(Curve curve, double d2, double d3, int n2) {
        this.curve = curve;
        this.ytop = d2;
        this.ybot = d3;
        this.etag = n2;
        if (this.ytop < curve.getYTop() || this.ybot > curve.getYBot()) {
            throw new InternalError("bad curvelink [" + this.ytop + "=>" + this.ybot + "] for " + curve);
        }
    }

    public boolean absorb(CurveLink curveLink) {
        return this.absorb(curveLink.curve, curveLink.ytop, curveLink.ybot, curveLink.etag);
    }

    public boolean absorb(Curve curve, double d2, double d3, int n2) {
        if (this.curve != curve || this.etag != n2 || this.ybot < d2 || this.ytop > d3) {
            return false;
        }
        if (d2 < curve.getYTop() || d3 > curve.getYBot()) {
            throw new InternalError("bad curvelink [" + d2 + "=>" + d3 + "] for " + curve);
        }
        this.ytop = Math.min(this.ytop, d2);
        this.ybot = Math.max(this.ybot, d3);
        return true;
    }

    public boolean isEmpty() {
        return this.ytop == this.ybot;
    }

    public Curve getCurve() {
        return this.curve;
    }

    public Curve getSubCurve() {
        if (this.ytop == this.curve.getYTop() && this.ybot == this.curve.getYBot()) {
            return this.curve.getWithDirection(this.etag);
        }
        return this.curve.getSubCurve(this.ytop, this.ybot, this.etag);
    }

    public Curve getMoveto() {
        return new Order0(this.getXTop(), this.getYTop());
    }

    public double getXTop() {
        return this.curve.XforY(this.ytop);
    }

    public double getYTop() {
        return this.ytop;
    }

    public double getXBot() {
        return this.curve.XforY(this.ybot);
    }

    public double getYBot() {
        return this.ybot;
    }

    public double getX() {
        return this.curve.XforY(this.ytop);
    }

    public int getEdgeTag() {
        return this.etag;
    }

    public void setNext(CurveLink curveLink) {
        this.next = curveLink;
    }

    public CurveLink getNext() {
        return this.next;
    }
}

