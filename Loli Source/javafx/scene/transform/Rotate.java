/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class Rotate
extends Transform {
    public static final Point3D X_AXIS = new Point3D(1.0, 0.0, 0.0);
    public static final Point3D Y_AXIS = new Point3D(0.0, 1.0, 0.0);
    public static final Point3D Z_AXIS = new Point3D(0.0, 0.0, 1.0);
    private MatrixCache cache;
    private MatrixCache inverseCache;
    private DoubleProperty angle;
    private DoubleProperty pivotX;
    private DoubleProperty pivotY;
    private DoubleProperty pivotZ;
    private ObjectProperty<Point3D> axis;

    public Rotate() {
    }

    public Rotate(double d2) {
        this.setAngle(d2);
    }

    public Rotate(double d2, Point3D point3D) {
        this.setAngle(d2);
        this.setAxis(point3D);
    }

    public Rotate(double d2, double d3, double d4) {
        this.setAngle(d2);
        this.setPivotX(d3);
        this.setPivotY(d4);
    }

    public Rotate(double d2, double d3, double d4, double d5) {
        this(d2, d3, d4);
        this.setPivotZ(d5);
    }

    public Rotate(double d2, double d3, double d4, double d5, Point3D point3D) {
        this(d2, d3, d4);
        this.setPivotZ(d5);
        this.setAxis(point3D);
    }

    public final void setAngle(double d2) {
        this.angleProperty().set(d2);
    }

    public final double getAngle() {
        return this.angle == null ? 0.0 : this.angle.get();
    }

    public final DoubleProperty angleProperty() {
        if (this.angle == null) {
            this.angle = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Rotate.this;
                }

                @Override
                public String getName() {
                    return "angle";
                }
            };
        }
        return this.angle;
    }

    public final void setPivotX(double d2) {
        this.pivotXProperty().set(d2);
    }

    public final double getPivotX() {
        return this.pivotX == null ? 0.0 : this.pivotX.get();
    }

    public final DoubleProperty pivotXProperty() {
        if (this.pivotX == null) {
            this.pivotX = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Rotate.this;
                }

                @Override
                public String getName() {
                    return "pivotX";
                }
            };
        }
        return this.pivotX;
    }

    public final void setPivotY(double d2) {
        this.pivotYProperty().set(d2);
    }

    public final double getPivotY() {
        return this.pivotY == null ? 0.0 : this.pivotY.get();
    }

    public final DoubleProperty pivotYProperty() {
        if (this.pivotY == null) {
            this.pivotY = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Rotate.this;
                }

                @Override
                public String getName() {
                    return "pivotY";
                }
            };
        }
        return this.pivotY;
    }

    public final void setPivotZ(double d2) {
        this.pivotZProperty().set(d2);
    }

    public final double getPivotZ() {
        return this.pivotZ == null ? 0.0 : this.pivotZ.get();
    }

    public final DoubleProperty pivotZProperty() {
        if (this.pivotZ == null) {
            this.pivotZ = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Rotate.this;
                }

                @Override
                public String getName() {
                    return "pivotZ";
                }
            };
        }
        return this.pivotZ;
    }

    public final void setAxis(Point3D point3D) {
        this.axisProperty().set(point3D);
    }

    public final Point3D getAxis() {
        return this.axis == null ? Z_AXIS : (Point3D)this.axis.get();
    }

    public final ObjectProperty<Point3D> axisProperty() {
        if (this.axis == null) {
            this.axis = new ObjectPropertyBase<Point3D>(Z_AXIS){

                @Override
                public void invalidated() {
                    Rotate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Rotate.this;
                }

                @Override
                public String getName() {
                    return "axis";
                }
            };
        }
        return this.axis;
    }

    @Override
    public double getMxx() {
        this.updateCache();
        return this.cache.mxx;
    }

    @Override
    public double getMxy() {
        this.updateCache();
        return this.cache.mxy;
    }

    @Override
    public double getMxz() {
        this.updateCache();
        return this.cache.mxz;
    }

    @Override
    public double getTx() {
        this.updateCache();
        return this.cache.tx;
    }

    @Override
    public double getMyx() {
        this.updateCache();
        return this.cache.myx;
    }

    @Override
    public double getMyy() {
        this.updateCache();
        return this.cache.myy;
    }

    @Override
    public double getMyz() {
        this.updateCache();
        return this.cache.myz;
    }

    @Override
    public double getTy() {
        this.updateCache();
        return this.cache.ty;
    }

    @Override
    public double getMzx() {
        this.updateCache();
        return this.cache.mzx;
    }

    @Override
    public double getMzy() {
        this.updateCache();
        return this.cache.mzy;
    }

    @Override
    public double getMzz() {
        this.updateCache();
        return this.cache.mzz;
    }

    @Override
    public double getTz() {
        this.updateCache();
        return this.cache.tz;
    }

    @Override
    boolean computeIs2D() {
        Point3D point3D = this.getAxis();
        return point3D.getX() == 0.0 && point3D.getY() == 0.0 || this.getAngle() == 0.0;
    }

    @Override
    boolean computeIsIdentity() {
        if (this.getAngle() == 0.0) {
            return true;
        }
        Point3D point3D = this.getAxis();
        return point3D.getX() == 0.0 && point3D.getY() == 0.0 && point3D.getZ() == 0.0;
    }

    @Override
    void fill2DArray(double[] arrd) {
        this.updateCache();
        arrd[0] = this.cache.mxx;
        arrd[1] = this.cache.mxy;
        arrd[2] = this.cache.tx;
        arrd[3] = this.cache.myx;
        arrd[4] = this.cache.myy;
        arrd[5] = this.cache.ty;
    }

    @Override
    void fill3DArray(double[] arrd) {
        this.updateCache();
        arrd[0] = this.cache.mxx;
        arrd[1] = this.cache.mxy;
        arrd[2] = this.cache.mxz;
        arrd[3] = this.cache.tx;
        arrd[4] = this.cache.myx;
        arrd[5] = this.cache.myy;
        arrd[6] = this.cache.myz;
        arrd[7] = this.cache.ty;
        arrd[8] = this.cache.mzx;
        arrd[9] = this.cache.mzy;
        arrd[10] = this.cache.mzz;
        arrd[11] = this.cache.tz;
    }

    @Override
    public Transform createConcatenation(Transform transform) {
        Transform transform2;
        if (transform instanceof Rotate) {
            transform2 = (Rotate)transform;
            double d2 = this.getPivotX();
            double d3 = this.getPivotY();
            double d4 = this.getPivotZ();
            if ((((Rotate)transform2).getAxis() == this.getAxis() || ((Rotate)transform2).getAxis().normalize().equals(this.getAxis().normalize())) && d2 == ((Rotate)transform2).getPivotX() && d3 == ((Rotate)transform2).getPivotY() && d4 == ((Rotate)transform2).getPivotZ()) {
                return new Rotate(this.getAngle() + ((Rotate)transform2).getAngle(), d2, d3, d4, this.getAxis());
            }
        }
        if (transform instanceof Affine) {
            transform2 = (Affine)transform.clone();
            ((Affine)transform2).prepend(this);
            return transform2;
        }
        return super.createConcatenation(transform);
    }

    @Override
    public Transform createInverse() throws NonInvertibleTransformException {
        return new Rotate(-this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
    }

    @Override
    public Rotate clone() {
        return new Rotate(this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
    }

    @Override
    public Point2D transform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        this.updateCache();
        return new Point2D(this.cache.mxx * d2 + this.cache.mxy * d3 + this.cache.tx, this.cache.myx * d2 + this.cache.myy * d3 + this.cache.ty);
    }

    @Override
    public Point3D transform(double d2, double d3, double d4) {
        this.updateCache();
        return new Point3D(this.cache.mxx * d2 + this.cache.mxy * d3 + this.cache.mxz * d4 + this.cache.tx, this.cache.myx * d2 + this.cache.myy * d3 + this.cache.myz * d4 + this.cache.ty, this.cache.mzx * d2 + this.cache.mzy * d3 + this.cache.mzz * d4 + this.cache.tz);
    }

    @Override
    void transform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        this.updateCache();
        while (--n4 >= 0) {
            double d2 = arrd[n2++];
            double d3 = arrd[n2++];
            arrd2[n3++] = this.cache.mxx * d2 + this.cache.mxy * d3 + this.cache.tx;
            arrd2[n3++] = this.cache.myx * d2 + this.cache.myy * d3 + this.cache.ty;
        }
    }

    @Override
    void transform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        this.updateCache();
        while (--n4 >= 0) {
            double d2 = arrd[n2++];
            double d3 = arrd[n2++];
            double d4 = arrd[n2++];
            arrd2[n3++] = this.cache.mxx * d2 + this.cache.mxy * d3 + this.cache.mxz * d4 + this.cache.tx;
            arrd2[n3++] = this.cache.myx * d2 + this.cache.myy * d3 + this.cache.myz * d4 + this.cache.ty;
            arrd2[n3++] = this.cache.mzx * d2 + this.cache.mzy * d3 + this.cache.mzz * d4 + this.cache.tz;
        }
    }

    @Override
    public Point2D deltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        this.updateCache();
        return new Point2D(this.cache.mxx * d2 + this.cache.mxy * d3, this.cache.myx * d2 + this.cache.myy * d3);
    }

    @Override
    public Point3D deltaTransform(double d2, double d3, double d4) {
        this.updateCache();
        return new Point3D(this.cache.mxx * d2 + this.cache.mxy * d3 + this.cache.mxz * d4, this.cache.myx * d2 + this.cache.myy * d3 + this.cache.myz * d4, this.cache.mzx * d2 + this.cache.mzy * d3 + this.cache.mzz * d4);
    }

    @Override
    public Point2D inverseTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        this.updateInverseCache();
        return new Point2D(this.inverseCache.mxx * d2 + this.inverseCache.mxy * d3 + this.inverseCache.tx, this.inverseCache.myx * d2 + this.inverseCache.myy * d3 + this.inverseCache.ty);
    }

    @Override
    public Point3D inverseTransform(double d2, double d3, double d4) {
        this.updateInverseCache();
        return new Point3D(this.inverseCache.mxx * d2 + this.inverseCache.mxy * d3 + this.inverseCache.mxz * d4 + this.inverseCache.tx, this.inverseCache.myx * d2 + this.inverseCache.myy * d3 + this.inverseCache.myz * d4 + this.inverseCache.ty, this.inverseCache.mzx * d2 + this.inverseCache.mzy * d3 + this.inverseCache.mzz * d4 + this.inverseCache.tz);
    }

    @Override
    void inverseTransform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        this.updateInverseCache();
        while (--n4 >= 0) {
            double d2 = arrd[n2++];
            double d3 = arrd[n2++];
            arrd2[n3++] = this.inverseCache.mxx * d2 + this.inverseCache.mxy * d3 + this.inverseCache.tx;
            arrd2[n3++] = this.inverseCache.myx * d2 + this.inverseCache.myy * d3 + this.inverseCache.ty;
        }
    }

    @Override
    void inverseTransform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        this.updateInverseCache();
        while (--n4 >= 0) {
            double d2 = arrd[n2++];
            double d3 = arrd[n2++];
            double d4 = arrd[n2++];
            arrd2[n3++] = this.inverseCache.mxx * d2 + this.inverseCache.mxy * d3 + this.inverseCache.mxz * d4 + this.inverseCache.tx;
            arrd2[n3++] = this.inverseCache.myx * d2 + this.inverseCache.myy * d3 + this.inverseCache.myz * d4 + this.inverseCache.ty;
            arrd2[n3++] = this.inverseCache.mzx * d2 + this.inverseCache.mzy * d3 + this.inverseCache.mzz * d4 + this.inverseCache.tz;
        }
    }

    @Override
    public Point2D inverseDeltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        this.updateInverseCache();
        return new Point2D(this.inverseCache.mxx * d2 + this.inverseCache.mxy * d3, this.inverseCache.myx * d2 + this.inverseCache.myy * d3);
    }

    @Override
    public Point3D inverseDeltaTransform(double d2, double d3, double d4) {
        this.updateInverseCache();
        return new Point3D(this.inverseCache.mxx * d2 + this.inverseCache.mxy * d3 + this.inverseCache.mxz * d4, this.inverseCache.myx * d2 + this.inverseCache.myy * d3 + this.inverseCache.myz * d4, this.inverseCache.mzx * d2 + this.inverseCache.mzy * d3 + this.inverseCache.mzz * d4);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Rotate [");
        stringBuilder.append("angle=").append(this.getAngle());
        stringBuilder.append(", pivotX=").append(this.getPivotX());
        stringBuilder.append(", pivotY=").append(this.getPivotY());
        stringBuilder.append(", pivotZ=").append(this.getPivotZ());
        stringBuilder.append(", axis=").append(this.getAxis());
        return stringBuilder.append("]").toString();
    }

    @Override
    @Deprecated
    public void impl_apply(Affine3D affine3D) {
        double d2 = this.getPivotX();
        double d3 = this.getPivotY();
        double d4 = this.getPivotZ();
        double d5 = this.getAngle();
        if (d2 != 0.0 || d3 != 0.0 || d4 != 0.0) {
            affine3D.translate(d2, d3, d4);
            affine3D.rotate(Math.toRadians(d5), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
            affine3D.translate(-d2, -d3, -d4);
        } else {
            affine3D.rotate(Math.toRadians(d5), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
        }
    }

    @Override
    @Deprecated
    public BaseTransform impl_derive(BaseTransform baseTransform) {
        if (this.isIdentity()) {
            return baseTransform;
        }
        double d2 = this.getPivotX();
        double d3 = this.getPivotY();
        double d4 = this.getPivotZ();
        double d5 = this.getAngle();
        if (d2 != 0.0 || d3 != 0.0 || d4 != 0.0) {
            baseTransform = baseTransform.deriveWithTranslation(d2, d3, d4);
            baseTransform = baseTransform.deriveWithRotation(Math.toRadians(d5), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
            return baseTransform.deriveWithTranslation(-d2, -d3, -d4);
        }
        return baseTransform.deriveWithRotation(Math.toRadians(d5), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
    }

    @Override
    void validate() {
        this.getAxis();
        this.getAngle();
        this.getPivotX();
        this.getPivotY();
        this.getPivotZ();
    }

    @Override
    protected void transformChanged() {
        if (this.cache != null) {
            this.cache.invalidate();
        }
        super.transformChanged();
    }

    @Override
    void appendTo(Affine affine) {
        affine.appendRotation(this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
    }

    @Override
    void prependTo(Affine affine) {
        affine.prependRotation(this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
    }

    private void updateCache() {
        if (this.cache == null) {
            this.cache = new MatrixCache();
        }
        if (!this.cache.valid) {
            this.cache.update(this.getAngle(), this.getAxis(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
        }
    }

    private void updateInverseCache() {
        if (this.inverseCache == null) {
            this.inverseCache = new MatrixCache();
        }
        if (!this.inverseCache.valid) {
            this.inverseCache.update(-this.getAngle(), this.getAxis(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
        }
    }

    private static class MatrixCache {
        boolean valid = false;
        boolean is3D = false;
        double mxx;
        double mxy;
        double mxz;
        double tx;
        double myx;
        double myy;
        double myz;
        double ty;
        double mzx;
        double mzy;
        double mzz = 1.0;
        double tz;

        public void update(double d2, Point3D point3D, double d3, double d4, double d5) {
            double d6;
            double d7;
            double d8;
            double d9 = Math.toRadians(d2);
            double d10 = Math.sin(d9);
            double d11 = Math.cos(d9);
            if (point3D == Z_AXIS || point3D.getX() == 0.0 && point3D.getY() == 0.0 && point3D.getZ() > 0.0) {
                this.mxx = d11;
                this.mxy = -d10;
                this.tx = d3 * (1.0 - d11) + d4 * d10;
                this.myx = d10;
                this.myy = d11;
                this.ty = d4 * (1.0 - d11) - d3 * d10;
                if (this.is3D) {
                    this.mxz = 0.0;
                    this.myz = 0.0;
                    this.mzx = 0.0;
                    this.mzy = 0.0;
                    this.mzz = 1.0;
                    this.tz = 0.0;
                    this.is3D = false;
                }
                this.valid = true;
                return;
            }
            this.is3D = true;
            if (point3D == X_AXIS || point3D == Y_AXIS || point3D == Z_AXIS) {
                d8 = point3D.getX();
                d7 = point3D.getY();
                d6 = point3D.getZ();
            } else {
                double d12 = Math.sqrt(point3D.getX() * point3D.getX() + point3D.getY() * point3D.getY() + point3D.getZ() * point3D.getZ());
                if (d12 == 0.0) {
                    this.mxx = 1.0;
                    this.mxy = 0.0;
                    this.mxz = 0.0;
                    this.tx = 0.0;
                    this.myx = 0.0;
                    this.myy = 1.0;
                    this.myz = 0.0;
                    this.ty = 0.0;
                    this.mzx = 0.0;
                    this.mzy = 0.0;
                    this.mzz = 1.0;
                    this.tz = 0.0;
                    this.valid = true;
                    return;
                }
                d8 = point3D.getX() / d12;
                d7 = point3D.getY() / d12;
                d6 = point3D.getZ() / d12;
            }
            this.mxx = d11 + d8 * d8 * (1.0 - d11);
            this.mxy = d8 * d7 * (1.0 - d11) - d6 * d10;
            this.mxz = d8 * d6 * (1.0 - d11) + d7 * d10;
            this.tx = d3 * (1.0 - this.mxx) - d4 * this.mxy - d5 * this.mxz;
            this.myx = d7 * d8 * (1.0 - d11) + d6 * d10;
            this.myy = d11 + d7 * d7 * (1.0 - d11);
            this.myz = d7 * d6 * (1.0 - d11) - d8 * d10;
            this.ty = d4 * (1.0 - this.myy) - d3 * this.myx - d5 * this.myz;
            this.mzx = d6 * d8 * (1.0 - d11) - d7 * d10;
            this.mzy = d6 * d7 * (1.0 - d11) + d8 * d10;
            this.mzz = d11 + d6 * d6 * (1.0 - d11);
            this.tz = d5 * (1.0 - this.mzz) - d3 * this.mzx - d4 * this.mzy;
            this.valid = true;
        }

        public void invalidate() {
            this.valid = false;
        }
    }
}

