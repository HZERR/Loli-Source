/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.beans.NamedArg;
import javafx.geometry.Point2D;

public class Rectangle2D {
    public static final Rectangle2D EMPTY = new Rectangle2D(0.0, 0.0, 0.0, 0.0);
    private double minX;
    private double minY;
    private double width;
    private double height;
    private double maxX;
    private double maxY;
    private int hash = 0;

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public Rectangle2D(@NamedArg(value="minX") double d2, @NamedArg(value="minY") double d3, @NamedArg(value="width") double d4, @NamedArg(value="height") double d5) {
        if (d4 < 0.0 || d5 < 0.0) {
            throw new IllegalArgumentException("Both width and height must be >= 0");
        }
        this.minX = d2;
        this.minY = d3;
        this.width = d4;
        this.height = d5;
        this.maxX = d2 + d4;
        this.maxY = d3 + d5;
    }

    public boolean contains(Point2D point2D) {
        if (point2D == null) {
            return false;
        }
        return this.contains(point2D.getX(), point2D.getY());
    }

    public boolean contains(double d2, double d3) {
        return d2 >= this.minX && d2 <= this.maxX && d3 >= this.minY && d3 <= this.maxY;
    }

    public boolean contains(Rectangle2D rectangle2D) {
        if (rectangle2D == null) {
            return false;
        }
        return rectangle2D.minX >= this.minX && rectangle2D.minY >= this.minY && rectangle2D.maxX <= this.maxX && rectangle2D.maxY <= this.maxY;
    }

    public boolean contains(double d2, double d3, double d4, double d5) {
        return d2 >= this.minX && d3 >= this.minY && d4 <= this.maxX - d2 && d5 <= this.maxY - d3;
    }

    public boolean intersects(Rectangle2D rectangle2D) {
        if (rectangle2D == null) {
            return false;
        }
        return rectangle2D.maxX > this.minX && rectangle2D.maxY > this.minY && rectangle2D.minX < this.maxX && rectangle2D.minY < this.maxY;
    }

    public boolean intersects(double d2, double d3, double d4, double d5) {
        return d2 < this.maxX && d3 < this.maxY && d2 + d4 > this.minX && d3 + d5 > this.minY;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Rectangle2D) {
            Rectangle2D rectangle2D = (Rectangle2D)object;
            return this.minX == rectangle2D.minX && this.minY == rectangle2D.minY && this.width == rectangle2D.width && this.height == rectangle2D.height;
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 7L;
            l2 = 31L * l2 + Double.doubleToLongBits(this.minX);
            l2 = 31L * l2 + Double.doubleToLongBits(this.minY);
            l2 = 31L * l2 + Double.doubleToLongBits(this.width);
            l2 = 31L * l2 + Double.doubleToLongBits(this.height);
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "Rectangle2D [minX = " + this.minX + ", minY=" + this.minY + ", maxX=" + this.maxX + ", maxY=" + this.maxY + ", width=" + this.width + ", height=" + this.height + "]";
    }
}

