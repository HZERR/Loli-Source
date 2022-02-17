/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.beans.NamedArg;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class BoundingBox
extends Bounds {
    private int hash = 0;

    public BoundingBox(@NamedArg(value="minX") double d2, @NamedArg(value="minY") double d3, @NamedArg(value="minZ") double d4, @NamedArg(value="width") double d5, @NamedArg(value="height") double d6, @NamedArg(value="depth") double d7) {
        super(d2, d3, d4, d5, d6, d7);
    }

    public BoundingBox(@NamedArg(value="minX") double d2, @NamedArg(value="minY") double d3, @NamedArg(value="width") double d4, @NamedArg(value="height") double d5) {
        super(d2, d3, 0.0, d4, d5, 0.0);
    }

    @Override
    public boolean isEmpty() {
        return this.getMaxX() < this.getMinX() || this.getMaxY() < this.getMinY() || this.getMaxZ() < this.getMinZ();
    }

    @Override
    public boolean contains(Point2D point2D) {
        if (point2D == null) {
            return false;
        }
        return this.contains(point2D.getX(), point2D.getY(), 0.0);
    }

    @Override
    public boolean contains(Point3D point3D) {
        if (point3D == null) {
            return false;
        }
        return this.contains(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    @Override
    public boolean contains(double d2, double d3) {
        return this.contains(d2, d3, 0.0);
    }

    @Override
    public boolean contains(double d2, double d3, double d4) {
        if (this.isEmpty()) {
            return false;
        }
        return d2 >= this.getMinX() && d2 <= this.getMaxX() && d3 >= this.getMinY() && d3 <= this.getMaxY() && d4 >= this.getMinZ() && d4 <= this.getMaxZ();
    }

    @Override
    public boolean contains(Bounds bounds) {
        if (bounds == null || bounds.isEmpty()) {
            return false;
        }
        return this.contains(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ(), bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
    }

    @Override
    public boolean contains(double d2, double d3, double d4, double d5) {
        return this.contains(d2, d3) && this.contains(d2 + d4, d3 + d5);
    }

    @Override
    public boolean contains(double d2, double d3, double d4, double d5, double d6, double d7) {
        return this.contains(d2, d3, d4) && this.contains(d2 + d5, d3 + d6, d4 + d7);
    }

    @Override
    public boolean intersects(Bounds bounds) {
        if (bounds == null || bounds.isEmpty()) {
            return false;
        }
        return this.intersects(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ(), bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
    }

    @Override
    public boolean intersects(double d2, double d3, double d4, double d5) {
        return this.intersects(d2, d3, 0.0, d4, d5, 0.0);
    }

    @Override
    public boolean intersects(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (this.isEmpty() || d5 < 0.0 || d6 < 0.0 || d7 < 0.0) {
            return false;
        }
        return d2 + d5 >= this.getMinX() && d3 + d6 >= this.getMinY() && d4 + d7 >= this.getMinZ() && d2 <= this.getMaxX() && d3 <= this.getMaxY() && d4 <= this.getMaxZ();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof BoundingBox) {
            BoundingBox boundingBox = (BoundingBox)object;
            return this.getMinX() == boundingBox.getMinX() && this.getMinY() == boundingBox.getMinY() && this.getMinZ() == boundingBox.getMinZ() && this.getWidth() == boundingBox.getWidth() && this.getHeight() == boundingBox.getHeight() && this.getDepth() == boundingBox.getDepth();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 7L;
            l2 = 31L * l2 + Double.doubleToLongBits(this.getMinX());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getMinY());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getMinZ());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getWidth());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getHeight());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getDepth());
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "BoundingBox [minX:" + this.getMinX() + ", minY:" + this.getMinY() + ", minZ:" + this.getMinZ() + ", width:" + this.getWidth() + ", height:" + this.getHeight() + ", depth:" + this.getDepth() + ", maxX:" + this.getMaxX() + ", maxY:" + this.getMaxY() + ", maxZ:" + this.getMaxZ() + "]";
    }
}

