/*
 * Decompiled with CFR 0.150.
 */
package javafx.print;

import javafx.print.PageOrientation;
import javafx.print.Paper;

public final class PageLayout {
    private PageOrientation orient;
    private Paper paper;
    private double lMargin;
    private double rMargin;
    private double tMargin;
    private double bMargin;

    PageLayout(Paper paper, PageOrientation pageOrientation) {
        this(paper, pageOrientation, 56.0, 56.0, 56.0, 56.0);
    }

    PageLayout(Paper paper, PageOrientation pageOrientation, double d2, double d3, double d4, double d5) {
        if (paper == null || pageOrientation == null || d2 < 0.0 || d3 < 0.0 || d4 < 0.0 || d5 < 0.0) {
            throw new IllegalArgumentException("Illegal parameters");
        }
        if (pageOrientation == PageOrientation.PORTRAIT || pageOrientation == PageOrientation.REVERSE_PORTRAIT ? d2 + d3 > paper.getWidth() || d4 + d5 > paper.getHeight() : d2 + d3 > paper.getHeight() || d4 + d5 > paper.getWidth()) {
            throw new IllegalArgumentException("Bad margins");
        }
        this.paper = paper;
        this.orient = pageOrientation;
        this.lMargin = d2;
        this.rMargin = d3;
        this.tMargin = d4;
        this.bMargin = d5;
    }

    public PageOrientation getPageOrientation() {
        return this.orient;
    }

    public Paper getPaper() {
        return this.paper;
    }

    public double getPrintableWidth() {
        double d2 = 0.0;
        d2 = this.orient == PageOrientation.PORTRAIT || this.orient == PageOrientation.REVERSE_PORTRAIT ? this.paper.getWidth() : this.paper.getHeight();
        if ((d2 -= this.lMargin + this.rMargin) < 0.0) {
            d2 = 0.0;
        }
        return d2;
    }

    public double getPrintableHeight() {
        double d2 = 0.0;
        d2 = this.orient == PageOrientation.PORTRAIT || this.orient == PageOrientation.REVERSE_PORTRAIT ? this.paper.getHeight() : this.paper.getWidth();
        if ((d2 -= this.tMargin + this.bMargin) < 0.0) {
            d2 = 0.0;
        }
        return d2;
    }

    public double getLeftMargin() {
        return this.lMargin;
    }

    public double getRightMargin() {
        return this.rMargin;
    }

    public double getTopMargin() {
        return this.tMargin;
    }

    public double getBottomMargin() {
        return this.bMargin;
    }

    public boolean equals(Object object) {
        if (object instanceof PageLayout) {
            PageLayout pageLayout = (PageLayout)object;
            return this.paper.equals(pageLayout.paper) && this.orient.equals((Object)pageLayout.orient) && this.tMargin == pageLayout.tMargin && this.bMargin == pageLayout.bMargin && this.rMargin == pageLayout.rMargin && this.lMargin == pageLayout.lMargin;
        }
        return false;
    }

    public int hashCode() {
        return this.paper.hashCode() + this.orient.hashCode() + (int)(this.tMargin + this.bMargin + this.lMargin + this.rMargin);
    }

    public String toString() {
        return "Paper=" + this.paper + " Orient=" + (Object)((Object)this.orient) + " leftMargin=" + this.lMargin + " rightMargin=" + this.rMargin + " topMargin=" + this.tMargin + " bottomMargin=" + this.bMargin;
    }
}

