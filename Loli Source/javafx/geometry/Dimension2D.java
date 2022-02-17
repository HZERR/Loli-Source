/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.beans.NamedArg;

public class Dimension2D {
    private double width;
    private double height;
    private int hash = 0;

    public Dimension2D(@NamedArg(value="width") double d2, @NamedArg(value="height") double d3) {
        this.width = d2;
        this.height = d3;
    }

    public final double getWidth() {
        return this.width;
    }

    public final double getHeight() {
        return this.height;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Dimension2D) {
            Dimension2D dimension2D = (Dimension2D)object;
            return this.getWidth() == dimension2D.getWidth() && this.getHeight() == dimension2D.getHeight();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 7L;
            l2 = 31L * l2 + Double.doubleToLongBits(this.getWidth());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getHeight());
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "Dimension2D [width = " + this.getWidth() + ", height = " + this.getHeight() + "]";
    }
}

