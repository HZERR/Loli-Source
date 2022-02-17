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

public class Translate2D
extends BaseTransform {
    private double mxt;
    private double myt;
    private static final long BASE_HASH;

    public static BaseTransform getInstance(double d2, double d3) {
        if (d2 == 0.0 && d3 == 0.0) {
            return IDENTITY_TRANSFORM;
        }
        return new Translate2D(d2, d3);
    }

    public Translate2D(double d2, double d3) {
        this.mxt = d2;
        this.myt = d3;
    }

    public Translate2D(BaseTransform baseTransform) {
        if (!baseTransform.isTranslateOrIdentity()) {
            Translate2D.degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = baseTransform.getMxt();
        this.myt = baseTransform.getMyt();
    }

    @Override
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.TRANSLATE_2D;
    }

    @Override
    public double getDeterminant() {
        return 1.0;
    }

    @Override
    public double getMxt() {
        return this.mxt;
    }

    @Override
    public double getMyt() {
        return this.myt;
    }

    @Override
    public int getType() {
        return this.mxt == 0.0 && this.myt == 0.0 ? 0 : 1;
    }

    @Override
    public boolean isIdentity() {
        return this.mxt == 0.0 && this.myt == 0.0;
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
    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = Translate2D.makePoint(point2D, point2D2);
        }
        point2D2.setLocation((float)((double)point2D.x + this.mxt), (float)((double)point2D.y + this.myt));
        return point2D2;
    }

    @Override
    public Point2D inverseTransform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = Translate2D.makePoint(point2D, point2D2);
        }
        point2D2.setLocation((float)((double)point2D.x - this.mxt), (float)((double)point2D.y - this.myt));
        return point2D2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        vec3d2.x = vec3d.x + this.mxt;
        vec3d2.y = vec3d.y + this.myt;
        vec3d2.z = vec3d.z;
        return vec3d2;
    }

    @Override
    public Vec3d deltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d inverseTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        vec3d2.x = vec3d.x - this.mxt;
        vec3d2.y = vec3d.y - this.myt;
        vec3d2.z = vec3d.z;
        return vec3d2;
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public void transform(float[] arrf, int n2, float[] arrf2, int n3, int n4) {
        float f2 = (float)this.mxt;
        float f3 = (float)this.myt;
        if (arrf2 == arrf) {
            if (n3 > n2 && n3 < n2 + n4 * 2) {
                System.arraycopy(arrf, n2, arrf2, n3, n4 * 2);
                n2 = n3;
            }
            if (n3 == n2 && f2 == 0.0f && f3 == 0.0f) {
                return;
            }
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            arrf2[n3++] = arrf[n2++] + f2;
            arrf2[n3++] = arrf[n2++] + f3;
        }
    }

    @Override
    public void transform(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.mxt;
        double d3 = this.myt;
        if (arrd2 == arrd) {
            if (n3 > n2 && n3 < n2 + n4 * 2) {
                System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
                n2 = n3;
            }
            if (n3 == n2 && d2 == 0.0 && d3 == 0.0) {
                return;
            }
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            arrd2[n3++] = arrd[n2++] + d2;
            arrd2[n3++] = arrd[n2++] + d3;
        }
    }

    @Override
    public void transform(float[] arrf, int n2, double[] arrd, int n3, int n4) {
        double d2 = this.mxt;
        double d3 = this.myt;
        for (int i2 = 0; i2 < n4; ++i2) {
            arrd[n3++] = (double)arrf[n2++] + d2;
            arrd[n3++] = (double)arrf[n2++] + d3;
        }
    }

    @Override
    public void transform(double[] arrd, int n2, float[] arrf, int n3, int n4) {
        double d2 = this.mxt;
        double d3 = this.myt;
        for (int i2 = 0; i2 < n4; ++i2) {
            arrf[n3++] = (float)(arrd[n2++] + d2);
            arrf[n3++] = (float)(arrd[n2++] + d3);
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
        float f2 = (float)this.mxt;
        float f3 = (float)this.myt;
        if (arrf2 == arrf) {
            if (n3 > n2 && n3 < n2 + n4 * 2) {
                System.arraycopy(arrf, n2, arrf2, n3, n4 * 2);
                n2 = n3;
            }
            if (n3 == n2 && f2 == 0.0f && f3 == 0.0f) {
                return;
            }
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            arrf2[n3++] = arrf[n2++] - f2;
            arrf2[n3++] = arrf[n2++] - f3;
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
        double d2 = this.mxt;
        double d3 = this.myt;
        if (arrd2 == arrd) {
            if (n3 > n2 && n3 < n2 + n4 * 2) {
                System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
                n2 = n3;
            }
            if (n3 == n2 && d2 == 0.0 && d3 == 0.0) {
                return;
            }
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            arrd2[n3++] = arrd[n2++] - d2;
            arrd2[n3++] = arrd[n2++] - d3;
        }
    }

    @Override
    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        float f2 = (float)((double)baseBounds.getMinX() + this.mxt);
        float f3 = (float)((double)baseBounds.getMinY() + this.myt);
        float f4 = baseBounds.getMinZ();
        float f5 = (float)((double)baseBounds.getMaxX() + this.mxt);
        float f6 = (float)((double)baseBounds.getMaxY() + this.myt);
        float f7 = baseBounds.getMaxZ();
        return baseBounds2.deriveWithNewBounds(f2, f3, f4, f5, f6, f7);
    }

    @Override
    public void transform(Rectangle rectangle, Rectangle rectangle2) {
        Translate2D.transform(rectangle, rectangle2, this.mxt, this.myt);
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        float f2 = (float)((double)baseBounds.getMinX() - this.mxt);
        float f3 = (float)((double)baseBounds.getMinY() - this.myt);
        float f4 = baseBounds.getMinZ();
        float f5 = (float)((double)baseBounds.getMaxX() - this.mxt);
        float f6 = (float)((double)baseBounds.getMaxY() - this.myt);
        float f7 = baseBounds.getMaxZ();
        return baseBounds2.deriveWithNewBounds(f2, f3, f4, f5, f6, f7);
    }

    @Override
    public void inverseTransform(Rectangle rectangle, Rectangle rectangle2) {
        Translate2D.transform(rectangle, rectangle2, -this.mxt, -this.myt);
    }

    static void transform(Rectangle rectangle, Rectangle rectangle2, double d2, double d3) {
        int n2 = (int)d2;
        int n3 = (int)d3;
        if ((double)n2 == d2 && (double)n3 == d3) {
            rectangle2.setBounds(rectangle);
            rectangle2.translate(n2, n3);
        } else {
            double d4 = (double)rectangle.x + d2;
            double d5 = (double)rectangle.y + d3;
            double d6 = Math.ceil(d4 + (double)rectangle.width);
            double d7 = Math.ceil(d5 + (double)rectangle.height);
            d4 = Math.floor(d4);
            d5 = Math.floor(d5);
            rectangle2.setBounds((int)d4, (int)d5, (int)(d6 - d4), (int)(d7 - d5));
        }
    }

    @Override
    public Shape createTransformedShape(Shape shape) {
        return new Path2D(shape, this);
    }

    @Override
    public void setToIdentity() {
        this.myt = 0.0;
        this.mxt = 0.0;
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (!baseTransform.isTranslateOrIdentity()) {
            Translate2D.degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = baseTransform.getMxt();
        this.myt = baseTransform.getMyt();
    }

    @Override
    public void invert() {
        this.mxt = -this.mxt;
        this.myt = -this.myt;
    }

    @Override
    public void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d2 != 1.0 || d3 != 0.0 || d4 != 0.0 || d5 != 1.0) {
            Translate2D.degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = d6;
        this.myt = d7;
    }

    @Override
    public void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d2 != 1.0 || d3 != 0.0 || d4 != 0.0 || d6 != 0.0 || d7 != 1.0 || d8 != 0.0 || d10 != 0.0 || d11 != 0.0 || d12 != 1.0 || d13 != 0.0) {
            Translate2D.degreeError(BaseTransform.Degree.TRANSLATE_2D);
        }
        this.mxt = d5;
        this.myt = d9;
    }

    @Override
    public BaseTransform deriveWithTranslation(double d2, double d3) {
        this.mxt += d2;
        this.myt += d3;
        return this;
    }

    @Override
    public BaseTransform deriveWithTranslation(double d2, double d3, double d4) {
        if (d4 == 0.0) {
            this.mxt += d2;
            this.myt += d3;
            return this;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.translate(this.mxt + d2, this.myt + d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithScale(double d2, double d3, double d4) {
        if (d4 == 1.0) {
            if (d2 == 1.0 && d3 == 1.0) {
                return this;
            }
            Affine2D affine2D = new Affine2D();
            affine2D.translate(this.mxt, this.myt);
            affine2D.scale(d2, d3);
            return affine2D;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.translate(this.mxt, this.myt);
        affine3D.scale(d2, d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithRotation(double d2, double d3, double d4, double d5) {
        if (d2 == 0.0) {
            return this;
        }
        if (Translate2D.almostZero(d3) && Translate2D.almostZero(d4)) {
            if (d5 == 0.0) {
                return this;
            }
            Affine2D affine2D = new Affine2D();
            affine2D.translate(this.mxt, this.myt);
            if (d5 > 0.0) {
                affine2D.rotate(d2);
            } else if (d5 < 0.0) {
                affine2D.rotate(-d2);
            }
            return affine2D;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.translate(this.mxt, this.myt);
        affine3D.rotate(d2, d3, d4, d5);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithPreTranslation(double d2, double d3) {
        this.mxt += d2;
        this.myt += d3;
        return this;
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d2 == 1.0 && d3 == 0.0 && d4 == 0.0 && d5 == 1.0) {
            this.mxt += d6;
            this.myt += d7;
            return this;
        }
        return new Affine2D(d2, d3, d4, d5, this.mxt + d6, this.myt + d7);
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d4 == 0.0 && d8 == 0.0 && d10 == 0.0 && d11 == 0.0 && d12 == 1.0 && d13 == 0.0) {
            return this.deriveWithConcatenation(d2, d6, d3, d7, d5, d9);
        }
        return new Affine3D(d2, d3, d4, d5 + this.mxt, d6, d7, d8, d9 + this.myt, d10, d11, d12, d13);
    }

    @Override
    public BaseTransform deriveWithConcatenation(BaseTransform baseTransform) {
        if (baseTransform.isTranslateOrIdentity()) {
            this.mxt += baseTransform.getMxt();
            this.myt += baseTransform.getMyt();
            return this;
        }
        if (baseTransform.is2D()) {
            return Translate2D.getInstance(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), this.mxt + baseTransform.getMxt(), this.myt + baseTransform.getMyt());
        }
        Affine3D affine3D = new Affine3D(baseTransform);
        affine3D.preTranslate(this.mxt, this.myt, 0.0);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithPreConcatenation(BaseTransform baseTransform) {
        if (baseTransform.isTranslateOrIdentity()) {
            this.mxt += baseTransform.getMxt();
            this.myt += baseTransform.getMyt();
            return this;
        }
        if (baseTransform.is2D()) {
            Affine2D affine2D = new Affine2D(baseTransform);
            affine2D.translate(this.mxt, this.myt);
            return affine2D;
        }
        Affine3D affine3D = new Affine3D(baseTransform);
        affine3D.translate(this.mxt, this.myt, 0.0);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithNewTransform(BaseTransform baseTransform) {
        if (baseTransform.isTranslateOrIdentity()) {
            this.mxt = baseTransform.getMxt();
            this.myt = baseTransform.getMyt();
            return this;
        }
        return Translate2D.getInstance(baseTransform);
    }

    @Override
    public BaseTransform createInverse() {
        if (this.isIdentity()) {
            return IDENTITY_TRANSFORM;
        }
        return new Translate2D(-this.mxt, -this.myt);
    }

    private static double _matround(double d2) {
        return Math.rint(d2 * 1.0E15) / 1.0E15;
    }

    @Override
    public String toString() {
        return "Translate2D[" + Translate2D._matround(this.mxt) + ", " + Translate2D._matround(this.myt) + "]";
    }

    @Override
    public BaseTransform copy() {
        return new Translate2D(this.mxt, this.myt);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BaseTransform) {
            BaseTransform baseTransform = (BaseTransform)object;
            return baseTransform.isTranslateOrIdentity() && baseTransform.getMxt() == this.mxt && baseTransform.getMyt() == this.myt;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.isIdentity()) {
            return 0;
        }
        long l2 = BASE_HASH;
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMyt());
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMxt());
        return (int)l2 ^ (int)(l2 >> 32);
    }

    static {
        long l2 = 0L;
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyy());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyx());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxy());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxx());
        BASE_HASH = l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzt());
    }
}

