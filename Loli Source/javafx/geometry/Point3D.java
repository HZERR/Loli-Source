/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.beans.NamedArg;

public class Point3D {
    public static final Point3D ZERO = new Point3D(0.0, 0.0, 0.0);
    private double x;
    private double y;
    private double z;
    private int hash = 0;

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public Point3D(@NamedArg(value="x") double d2, @NamedArg(value="y") double d3, @NamedArg(value="z") double d4) {
        this.x = d2;
        this.y = d3;
        this.z = d4;
    }

    public double distance(double d2, double d3, double d4) {
        double d5 = this.getX() - d2;
        double d6 = this.getY() - d3;
        double d7 = this.getZ() - d4;
        return Math.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
    }

    public double distance(Point3D point3D) {
        return this.distance(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Point3D add(double d2, double d3, double d4) {
        return new Point3D(this.getX() + d2, this.getY() + d3, this.getZ() + d4);
    }

    public Point3D add(Point3D point3D) {
        return this.add(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Point3D subtract(double d2, double d3, double d4) {
        return new Point3D(this.getX() - d2, this.getY() - d3, this.getZ() - d4);
    }

    public Point3D subtract(Point3D point3D) {
        return this.subtract(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Point3D multiply(double d2) {
        return new Point3D(this.getX() * d2, this.getY() * d2, this.getZ() * d2);
    }

    public Point3D normalize() {
        double d2 = this.magnitude();
        if (d2 == 0.0) {
            return new Point3D(0.0, 0.0, 0.0);
        }
        return new Point3D(this.getX() / d2, this.getY() / d2, this.getZ() / d2);
    }

    public Point3D midpoint(double d2, double d3, double d4) {
        return new Point3D(d2 + (this.getX() - d2) / 2.0, d3 + (this.getY() - d3) / 2.0, d4 + (this.getZ() - d4) / 2.0);
    }

    public Point3D midpoint(Point3D point3D) {
        return this.midpoint(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public double angle(double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7 = this.getX();
        double d8 = (d7 * d2 + (d6 = this.getY()) * d3 + (d5 = this.getZ()) * d4) / Math.sqrt((d7 * d7 + d6 * d6 + d5 * d5) * (d2 * d2 + d3 * d3 + d4 * d4));
        if (d8 > 1.0) {
            return 0.0;
        }
        if (d8 < -1.0) {
            return 180.0;
        }
        return Math.toDegrees(Math.acos(d8));
    }

    public double angle(Point3D point3D) {
        return this.angle(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public double angle(Point3D point3D, Point3D point3D2) {
        double d2;
        double d3;
        double d4 = this.getX();
        double d5 = this.getY();
        double d6 = this.getZ();
        double d7 = point3D.getX() - d4;
        double d8 = point3D.getY() - d5;
        double d9 = point3D.getZ() - d6;
        double d10 = point3D2.getX() - d4;
        double d11 = (d7 * d10 + d8 * (d3 = point3D2.getY() - d5) + d9 * (d2 = point3D2.getZ() - d6)) / Math.sqrt((d7 * d7 + d8 * d8 + d9 * d9) * (d10 * d10 + d3 * d3 + d2 * d2));
        if (d11 > 1.0) {
            return 0.0;
        }
        if (d11 < -1.0) {
            return 180.0;
        }
        return Math.toDegrees(Math.acos(d11));
    }

    public double magnitude() {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        return Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
    }

    public double dotProduct(double d2, double d3, double d4) {
        return this.getX() * d2 + this.getY() * d3 + this.getZ() * d4;
    }

    public double dotProduct(Point3D point3D) {
        return this.dotProduct(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Point3D crossProduct(double d2, double d3, double d4) {
        double d5 = this.getX();
        double d6 = this.getY();
        double d7 = this.getZ();
        return new Point3D(d6 * d4 - d7 * d3, d7 * d2 - d5 * d4, d5 * d3 - d6 * d2);
    }

    public Point3D crossProduct(Point3D point3D) {
        return this.crossProduct(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Point3D) {
            Point3D point3D = (Point3D)object;
            return this.getX() == point3D.getX() && this.getY() == point3D.getY() && this.getZ() == point3D.getZ();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 7L;
            l2 = 31L * l2 + Double.doubleToLongBits(this.getX());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getY());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getZ());
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "Point3D [x = " + this.getX() + ", y = " + this.getY() + ", z = " + this.getZ() + "]";
    }
}

