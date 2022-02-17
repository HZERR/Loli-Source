/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.beans.NamedArg;

public class Insets {
    public static final Insets EMPTY = new Insets(0.0, 0.0, 0.0, 0.0);
    private double top;
    private double right;
    private double bottom;
    private double left;
    private int hash = 0;

    public final double getTop() {
        return this.top;
    }

    public final double getRight() {
        return this.right;
    }

    public final double getBottom() {
        return this.bottom;
    }

    public final double getLeft() {
        return this.left;
    }

    public Insets(@NamedArg(value="top") double d2, @NamedArg(value="right") double d3, @NamedArg(value="bottom") double d4, @NamedArg(value="left") double d5) {
        this.top = d2;
        this.right = d3;
        this.bottom = d4;
        this.left = d5;
    }

    public Insets(@NamedArg(value="topRightBottomLeft") double d2) {
        this.top = d2;
        this.right = d2;
        this.bottom = d2;
        this.left = d2;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Insets) {
            Insets insets = (Insets)object;
            return this.top == insets.top && this.right == insets.right && this.bottom == insets.bottom && this.left == insets.left;
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 17L;
            l2 = 37L * l2 + Double.doubleToLongBits(this.top);
            l2 = 37L * l2 + Double.doubleToLongBits(this.right);
            l2 = 37L * l2 + Double.doubleToLongBits(this.bottom);
            l2 = 37L * l2 + Double.doubleToLongBits(this.left);
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "Insets [top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + ", left=" + this.left + "]";
    }
}

