/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.beans.NamedArg;
import javafx.geometry.Point3D;

public class Point2D {
    public static final Point2D ZERO = new Point2D(0.0, 0.0);
    private double x;
    private double y;
    private int hash = 0;

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public Point2D(@NamedArg(value="x") double d2, @NamedArg(value="y") double d3) {
        this.x = d2;
        this.y = d3;
    }

    public double distance(double d2, double d3) {
        double d4 = this.getX() - d2;
        double d5 = this.getY() - d3;
        return Math.sqrt(d4 * d4 + d5 * d5);
    }

    public double distance(Point2D point2D) {
        return this.distance(point2D.getX(), point2D.getY());
    }

    public Point2D add(double d2, double d3) {
        return new Point2D(this.getX() + d2, this.getY() + d3);
    }

    public Point2D add(Point2D point2D) {
        return this.add(point2D.getX(), point2D.getY());
    }

    public Point2D subtract(double d2, double d3) {
        return new Point2D(this.getX() - d2, this.getY() - d3);
    }

    public Point2D multiply(double d2) {
        return new Point2D(this.getX() * d2, this.getY() * d2);
    }

    public Point2D subtract(Point2D point2D) {
        return this.subtract(point2D.getX(), point2D.getY());
    }

    public Point2D normalize() {
        double d2 = this.magnitude();
        if (d2 == 0.0) {
            return new Point2D(0.0, 0.0);
        }
        return new Point2D(this.getX() / d2, this.getY() / d2);
    }

    public Point2D midpoint(double d2, double d3) {
        return new Point2D(d2 + (this.getX() - d2) / 2.0, d3 + (this.getY() - d3) / 2.0);
    }

    public Point2D midpoint(Point2D point2D) {
        return this.midpoint(point2D.getX(), point2D.getY());
    }

    public double angle(double d2, double d3) {
        double d4;
        double d5 = this.getX();
        double d6 = (d5 * d2 + (d4 = this.getY()) * d3) / Math.sqrt((d5 * d5 + d4 * d4) * (d2 * d2 + d3 * d3));
        if (d6 > 1.0) {
            return 0.0;
        }
        if (d6 < -1.0) {
            return 180.0;
        }
        return Math.toDegrees(Math.acos(d6));
    }

    public double angle(Point2D point2D) {
        return this.angle(point2D.getX(), point2D.getY());
    }

    public double angle(Point2D point2D, Point2D point2D2) {
        double d2;
        double d3 = this.getX();
        double d4 = this.getY();
        double d5 = point2D.getX() - d3;
        double d6 = point2D.getY() - d4;
        double d7 = point2D2.getX() - d3;
        double d8 = (d5 * d7 + d6 * (d2 = point2D2.getY() - d4)) / Math.sqrt((d5 * d5 + d6 * d6) * (d7 * d7 + d2 * d2));
        if (d8 > 1.0) {
            return 0.0;
        }
        if (d8 < -1.0) {
            return 180.0;
        }
        return Math.toDegrees(Math.acos(d8));
    }

    public double magnitude() {
        double d2 = this.getX();
        double d3 = this.getY();
        return Math.sqrt(d2 * d2 + d3 * d3);
    }

    public double dotProduct(double d2, double d3) {
        return this.getX() * d2 + this.getY() * d3;
    }

    public double dotProduct(Point2D point2D) {
        return this.dotProduct(point2D.getX(), point2D.getY());
    }

    public Point3D crossProduct(double d2, double d3) {
        double d4 = this.getX();
        double d5 = this.getY();
        return new Point3D(0.0, 0.0, d4 * d3 - d5 * d2);
    }

    public Point3D crossProduct(Point2D point2D) {
        return this.crossProduct(point2D.getX(), point2D.getY());
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Point2D) {
            Point2D point2D = (Point2D)object;
            return this.getX() == point2D.getX() && this.getY() == point2D.getY();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 7L;
            l2 = 31L * l2 + Double.doubleToLongBits(this.getX());
            l2 = 31L * l2 + Double.doubleToLongBits(this.getY());
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "Point2D [x = " + this.getX() + ", y = " + this.getY() + "]";
    }
}

