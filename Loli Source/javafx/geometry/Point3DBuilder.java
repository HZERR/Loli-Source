/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.geometry.Point3D;
import javafx.util.Builder;

@Deprecated
public class Point3DBuilder<B extends Point3DBuilder<B>>
implements Builder<Point3D> {
    private double x;
    private double y;
    private double z;

    protected Point3DBuilder() {
    }

    public static Point3DBuilder<?> create() {
        return new Point3DBuilder();
    }

    public B x(double d2) {
        this.x = d2;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        return (B)this;
    }

    public B z(double d2) {
        this.z = d2;
        return (B)this;
    }

    @Override
    public Point3D build() {
        Point3D point3D = new Point3D(this.x, this.y, this.z);
        return point3D;
    }
}

