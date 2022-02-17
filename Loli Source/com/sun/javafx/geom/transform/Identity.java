/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;

public final class Identity
extends BaseTransform {
    @Override
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.IDENTITY;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public boolean isIdentity() {
        return true;
    }

    @Override
    public boolean isTranslateOrIdentity() {
        return true;
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public double getDeterminant() {
        return 1.0;
    }

    @Override
    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = Identity.makePoint(point2D, point2D2);
        }
        point2D2.setLocation(point2D);
        return point2D2;
    }

    @Override
    public Point2D inverseTransform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = Identity.makePoint(point2D, point2D2);
        }
        point2D2.setLocation(point2D);
        return point2D2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d deltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d inverseTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public void transform(float[] arrf, int n2, float[] arrf2, int n3, int n4) {
        if (arrf != arrf2 || n2 != n3) {
            System.arraycopy(arrf, n2, arrf2, n3, n4 * 2);
        }
    }

    @Override
    public void transform(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        if (arrd != arrd2 || n2 != n3) {
            System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
        }
    }

    @Override
    public void transform(float[] arrf, int n2, double[] arrd, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            arrd[n3++] = arrf[n2++];
            arrd[n3++] = arrf[n2++];
        }
    }

    @Override
    public void transform(double[] arrd, int n2, float[] arrf, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            arrf[n3++] = (float)arrd[n2++];
            arrf[n3++] = (float)arrd[n2++];
        }
    }

    @Override
    public void deltaTransform(float[] arrf, int n2, float[] arrf2, int n3, int n4) {
        if (arrf != arrf2 || n2 != n3) {
            System.arraycopy(arrf, n2, arrf2, n3, n4 * 2);
        }
    }

    @Override
    public void deltaTransform(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        if (arrd != arrd2 || n2 != n3) {
            System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
        }
    }

    @Override
    public void inverseTransform(float[] arrf, int n2, float[] arrf2, int n3, int n4) {
        if (arrf != arrf2 || n2 != n3) {
            System.arraycopy(arrf, n2, arrf2, n3, n4 * 2);
        }
    }

    @Override
    public void inverseDeltaTransform(float[] arrf, int n2, float[] arrf2, int n3, int n4) {
        if (arrf != arrf2 || n2 != n3) {
            System.arraycopy(arrf, n2, arrf2, n3, n4 * 2);
        }
    }

    @Override
    public void inverseTransform(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        if (arrd != arrd2 || n2 != n3) {
            System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
        }
    }

    @Override
    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (baseBounds2 != baseBounds) {
            baseBounds2 = baseBounds2.deriveWithNewBounds(baseBounds);
        }
        return baseBounds2;
    }

    @Override
    public void transform(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2 != rectangle) {
            rectangle2.setBounds(rectangle);
        }
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (baseBounds2 != baseBounds) {
            baseBounds2 = baseBounds2.deriveWithNewBounds(baseBounds);
        }
        return baseBounds2;
    }

    @Override
    public void inverseTransform(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2 != rectangle) {
            rectangle2.setBounds(rectangle);
        }
    }

    @Override
    public Shape createTransformedShape(Shape shape) {
        return new Path2D(shape);
    }

    @Override
    public void setToIdentity() {
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (!baseTransform.isIdentity()) {
            Identity.degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override
    public void invert() {
    }

    @Override
    public void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d2 != 1.0 || d3 != 0.0 || d4 != 0.0 || d5 != 1.0 || d6 != 0.0 || d7 != 0.0) {
            Identity.degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override
    public void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d2 != 1.0 || d3 != 0.0 || d4 != 0.0 || d5 != 0.0 || d6 != 0.0 || d7 != 1.0 || d8 != 0.0 || d9 != 0.0 || d10 != 0.0 || d11 != 0.0 || d12 != 1.0 || d13 != 0.0) {
            Identity.degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override
    public BaseTransform deriveWithTranslation(double d2, double d3) {
        return Translate2D.getInstance(d2, d3);
    }

    @Override
    public BaseTransform deriveWithPreTranslation(double d2, double d3) {
        return Translate2D.getInstance(d2, d3);
    }

    @Override
    public BaseTransform deriveWithTranslation(double d2, double d3, double d4) {
        if (d4 == 0.0) {
            if (d2 == 0.0 && d3 == 0.0) {
                return this;
            }
            return new Translate2D(d2, d3);
        }
        Affine3D affine3D = new Affine3D();
        affine3D.translate(d2, d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithScale(double d2, double d3, double d4) {
        if (d4 == 1.0) {
            if (d2 == 1.0 && d3 == 1.0) {
                return this;
            }
            Affine2D affine2D = new Affine2D();
            affine2D.scale(d2, d3);
            return affine2D;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.scale(d2, d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithRotation(double d2, double d3, double d4, double d5) {
        if (d2 == 0.0) {
            return this;
        }
        if (Identity.almostZero(d3) && Identity.almostZero(d4)) {
            if (d5 == 0.0) {
                return this;
            }
            Affine2D affine2D = new Affine2D();
            if (d5 > 0.0) {
                affine2D.rotate(d2);
            } else if (d5 < 0.0) {
                affine2D.rotate(-d2);
            }
            return affine2D;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.rotate(d2, d3, d4, d5);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7) {
        return Identity.getInstance(d2, d3, d4, d5, d6, d7);
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        return Identity.getInstance(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
    }

    @Override
    public BaseTransform deriveWithConcatenation(BaseTransform baseTransform) {
        return Identity.getInstance(baseTransform);
    }

    @Override
    public BaseTransform deriveWithPreConcatenation(BaseTransform baseTransform) {
        return Identity.getInstance(baseTransform);
    }

    @Override
    public BaseTransform deriveWithNewTransform(BaseTransform baseTransform) {
        return Identity.getInstance(baseTransform);
    }

    @Override
    public BaseTransform createInverse() {
        return this;
    }

    @Override
    public String toString() {
        return "Identity[]";
    }

    @Override
    public BaseTransform copy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof BaseTransform && ((BaseTransform)object).isIdentity();
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

