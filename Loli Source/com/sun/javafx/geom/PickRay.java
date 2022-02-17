/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

public class PickRay {
    private Vec3d origin = new Vec3d();
    private Vec3d direction = new Vec3d();
    private double nearClip = 0.0;
    private double farClip = Double.POSITIVE_INFINITY;
    static final double EPS = (double)1.0E-5f;
    private static final double EPSILON_ABSOLUTE = 1.0E-5;

    public PickRay() {
    }

    public PickRay(Vec3d vec3d, Vec3d vec3d2, double d2, double d3) {
        this.set(vec3d, vec3d2, d2, d3);
    }

    public PickRay(double d2, double d3, double d4, double d5, double d6) {
        this.set(d2, d3, d4, d5, d6);
    }

    public static PickRay computePerspectivePickRay(double d2, double d3, boolean bl, double d4, double d5, double d6, boolean bl2, Affine3D affine3D, double d7, double d8, PickRay pickRay) {
        if (pickRay == null) {
            pickRay = new PickRay();
        }
        Vec3d vec3d = pickRay.getDirectionNoClone();
        double d9 = d4 / 2.0;
        double d10 = d5 / 2.0;
        double d11 = bl2 ? d10 : d9;
        double d12 = d11 / Math.tan(d6 / 2.0);
        vec3d.x = d2 - d9;
        vec3d.y = d3 - d10;
        vec3d.z = d12;
        Vec3d vec3d2 = pickRay.getOriginNoClone();
        if (bl) {
            vec3d2.set(0.0, 0.0, 0.0);
        } else {
            vec3d2.set(d9, d10, -d12);
        }
        pickRay.nearClip = d7 * (vec3d.length() / (bl ? d12 : 1.0));
        pickRay.farClip = d8 * (vec3d.length() / (bl ? d12 : 1.0));
        pickRay.transform(affine3D);
        return pickRay;
    }

    public static PickRay computeParallelPickRay(double d2, double d3, double d4, Affine3D affine3D, double d5, double d6, PickRay pickRay) {
        if (pickRay == null) {
            pickRay = new PickRay();
        }
        double d7 = d4 / 2.0 / Math.tan(Math.toRadians(15.0));
        pickRay.set(d2, d3, d7, d5 * d7, d6 * d7);
        if (affine3D != null) {
            pickRay.transform(affine3D);
        }
        return pickRay;
    }

    public final void set(Vec3d vec3d, Vec3d vec3d2, double d2, double d3) {
        this.setOrigin(vec3d);
        this.setDirection(vec3d2);
        this.nearClip = d2;
        this.farClip = d3;
    }

    public final void set(double d2, double d3, double d4, double d5, double d6) {
        this.setOrigin(d2, d3, -d4);
        this.setDirection(0.0, 0.0, d4);
        this.nearClip = d5;
        this.farClip = d6;
    }

    public void setPickRay(PickRay pickRay) {
        this.setOrigin(pickRay.origin);
        this.setDirection(pickRay.direction);
        this.nearClip = pickRay.nearClip;
        this.farClip = pickRay.farClip;
    }

    public PickRay copy() {
        return new PickRay(this.origin, this.direction, this.nearClip, this.farClip);
    }

    public void setOrigin(Vec3d vec3d) {
        this.origin.set(vec3d);
    }

    public void setOrigin(double d2, double d3, double d4) {
        this.origin.set(d2, d3, d4);
    }

    public Vec3d getOrigin(Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        vec3d.set(this.origin);
        return vec3d;
    }

    public Vec3d getOriginNoClone() {
        return this.origin;
    }

    public void setDirection(Vec3d vec3d) {
        this.direction.set(vec3d);
    }

    public void setDirection(double d2, double d3, double d4) {
        this.direction.set(d2, d3, d4);
    }

    public Vec3d getDirection(Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        vec3d.set(this.direction);
        return vec3d;
    }

    public Vec3d getDirectionNoClone() {
        return this.direction;
    }

    public double getNearClip() {
        return this.nearClip;
    }

    public double getFarClip() {
        return this.farClip;
    }

    public double distance(Vec3d vec3d) {
        double d2 = vec3d.x - this.origin.x;
        double d3 = vec3d.y - this.origin.y;
        double d4 = vec3d.z - this.origin.z;
        return Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
    }

    public Point2D projectToZeroPlane(BaseTransform baseTransform, boolean bl, Vec3d vec3d, Point2D point2D) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        baseTransform.transform(this.origin, vec3d);
        double d2 = vec3d.x;
        double d3 = vec3d.y;
        double d4 = vec3d.z;
        vec3d.add(this.origin, this.direction);
        baseTransform.transform(vec3d, vec3d);
        double d5 = vec3d.x - d2;
        double d6 = vec3d.y - d3;
        double d7 = vec3d.z - d4;
        if (PickRay.almostZero(d7)) {
            return null;
        }
        double d8 = -d4 / d7;
        if (bl && d8 < 0.0) {
            return null;
        }
        if (point2D == null) {
            point2D = new Point2D();
        }
        point2D.setLocation((float)(d2 + d5 * d8), (float)(d3 + d6 * d8));
        return point2D;
    }

    static boolean almostZero(double d2) {
        return d2 < 1.0E-5 && d2 > -1.0E-5;
    }

    private static boolean isNonZero(double d2) {
        return d2 > (double)1.0E-5f || d2 < (double)-1.0E-5f;
    }

    public void transform(BaseTransform baseTransform) {
        baseTransform.transform(this.origin, this.origin);
        baseTransform.deltaTransform(this.direction, this.direction);
    }

    public void inverseTransform(BaseTransform baseTransform) throws NoninvertibleTransformException {
        baseTransform.inverseTransform(this.origin, this.origin);
        baseTransform.inverseDeltaTransform(this.direction, this.direction);
    }

    public PickRay project(BaseTransform baseTransform, boolean bl, Vec3d vec3d, Point2D point2D) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        baseTransform.transform(this.origin, vec3d);
        double d2 = vec3d.x;
        double d3 = vec3d.y;
        double d4 = vec3d.z;
        vec3d.add(this.origin, this.direction);
        baseTransform.transform(vec3d, vec3d);
        double d5 = vec3d.x - d2;
        double d6 = vec3d.y - d3;
        double d7 = vec3d.z - d4;
        PickRay pickRay = new PickRay();
        pickRay.origin.x = d2;
        pickRay.origin.y = d3;
        pickRay.origin.z = d4;
        pickRay.direction.x = d5;
        pickRay.direction.y = d6;
        pickRay.direction.z = d7;
        return pickRay;
    }

    public String toString() {
        return "origin: " + this.origin + "  direction: " + this.direction;
    }
}

