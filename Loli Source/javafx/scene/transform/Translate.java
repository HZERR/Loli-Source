/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

public class Translate
extends Transform {
    private DoubleProperty x;
    private DoubleProperty y;
    private DoubleProperty z;

    public Translate() {
    }

    public Translate(double d2, double d3) {
        this.setX(d2);
        this.setY(d3);
    }

    public Translate(double d2, double d3, double d4) {
        this(d2, d3);
        this.setZ(d4);
    }

    public final void setX(double d2) {
        this.xProperty().set(d2);
    }

    public final double getX() {
        return this.x == null ? 0.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Translate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Translate.this;
                }

                @Override
                public String getName() {
                    return "x";
                }
            };
        }
        return this.x;
    }

    public final void setY(double d2) {
        this.yProperty().set(d2);
    }

    public final double getY() {
        return this.y == null ? 0.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Translate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Translate.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
    }

    public final void setZ(double d2) {
        this.zProperty().set(d2);
    }

    public final double getZ() {
        return this.z == null ? 0.0 : this.z.get();
    }

    public final DoubleProperty zProperty() {
        if (this.z == null) {
            this.z = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Translate.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Translate.this;
                }

                @Override
                public String getName() {
                    return "z";
                }
            };
        }
        return this.z;
    }

    @Override
    public double getTx() {
        return this.getX();
    }

    @Override
    public double getTy() {
        return this.getY();
    }

    @Override
    public double getTz() {
        return this.getZ();
    }

    @Override
    boolean computeIs2D() {
        return this.getZ() == 0.0;
    }

    @Override
    boolean computeIsIdentity() {
        return this.getX() == 0.0 && this.getY() == 0.0 && this.getZ() == 0.0;
    }

    @Override
    void fill2DArray(double[] arrd) {
        arrd[0] = 1.0;
        arrd[1] = 0.0;
        arrd[2] = this.getX();
        arrd[3] = 0.0;
        arrd[4] = 1.0;
        arrd[5] = this.getY();
    }

    @Override
    void fill3DArray(double[] arrd) {
        arrd[0] = 1.0;
        arrd[1] = 0.0;
        arrd[2] = 0.0;
        arrd[3] = this.getX();
        arrd[4] = 0.0;
        arrd[5] = 1.0;
        arrd[6] = 0.0;
        arrd[7] = this.getY();
        arrd[8] = 0.0;
        arrd[9] = 0.0;
        arrd[10] = 1.0;
        arrd[11] = this.getZ();
    }

    @Override
    public Transform createConcatenation(Transform transform) {
        Transform transform2;
        if (transform instanceof Translate) {
            Translate translate = (Translate)transform;
            return new Translate(this.getX() + translate.getX(), this.getY() + translate.getY(), this.getZ() + translate.getZ());
        }
        if (transform instanceof Scale) {
            transform2 = (Scale)transform;
            double d2 = ((Scale)transform2).getX();
            double d3 = ((Scale)transform2).getY();
            double d4 = ((Scale)transform2).getZ();
            double d5 = this.getX();
            double d6 = this.getY();
            double d7 = this.getZ();
            if (!(d5 != 0.0 && d2 == 1.0 || d6 != 0.0 && d3 == 1.0 || d7 != 0.0 && d4 == 1.0)) {
                return new Scale(d2, d3, d4, ((Scale)transform2).getPivotX() + (d2 == 1.0 ? 0.0 : d5 / (1.0 - d2)), ((Scale)transform2).getPivotY() + (d3 == 1.0 ? 0.0 : d6 / (1.0 - d3)), ((Scale)transform2).getPivotZ() + (d4 == 1.0 ? 0.0 : d7 / (1.0 - d4)));
            }
        }
        if (transform instanceof Affine) {
            transform2 = (Affine)transform.clone();
            ((Affine)transform2).prepend(this);
            return transform2;
        }
        double d8 = transform.getMxx();
        double d9 = transform.getMxy();
        double d10 = transform.getMxz();
        double d11 = transform.getTx();
        double d12 = transform.getMyx();
        double d13 = transform.getMyy();
        double d14 = transform.getMyz();
        double d15 = transform.getTy();
        double d16 = transform.getMzx();
        double d17 = transform.getMzy();
        double d18 = transform.getMzz();
        double d19 = transform.getTz();
        return new Affine(d8, d9, d10, d11 + this.getX(), d12, d13, d14, d15 + this.getY(), d16, d17, d18, d19 + this.getZ());
    }

    @Override
    public Translate createInverse() {
        return new Translate(-this.getX(), -this.getY(), -this.getZ());
    }

    @Override
    public Translate clone() {
        return new Translate(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public Point2D transform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(d2 + this.getX(), d3 + this.getY());
    }

    @Override
    public Point3D transform(double d2, double d3, double d4) {
        return new Point3D(d2 + this.getX(), d3 + this.getY(), d4 + this.getZ());
    }

    @Override
    void transform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        while (--n4 >= 0) {
            double d4 = arrd[n2++];
            double d5 = arrd[n2++];
            arrd2[n3++] = d4 + d2;
            arrd2[n3++] = d5 + d3;
        }
    }

    @Override
    void transform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        while (--n4 >= 0) {
            double d5 = arrd[n2++];
            double d6 = arrd[n2++];
            double d7 = arrd[n2++];
            arrd2[n3++] = d5 + d2;
            arrd2[n3++] = d6 + d3;
            arrd2[n3++] = d7 + d4;
        }
    }

    @Override
    public Point2D deltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(d2, d3);
    }

    @Override
    public Point2D deltaTransform(Point2D point2D) {
        if (point2D == null) {
            throw new NullPointerException();
        }
        this.ensureCanTransform2DPoint();
        return point2D;
    }

    @Override
    public Point3D deltaTransform(double d2, double d3, double d4) {
        return new Point3D(d2, d3, d4);
    }

    @Override
    public Point3D deltaTransform(Point3D point3D) {
        if (point3D == null) {
            throw new NullPointerException();
        }
        return point3D;
    }

    @Override
    public Point2D inverseTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(d2 - this.getX(), d3 - this.getY());
    }

    @Override
    public Point3D inverseTransform(double d2, double d3, double d4) {
        return new Point3D(d2 - this.getX(), d3 - this.getY(), d4 - this.getZ());
    }

    @Override
    void inverseTransform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        while (--n4 >= 0) {
            arrd2[n3++] = arrd[n2++] - d2;
            arrd2[n3++] = arrd[n2++] - d3;
        }
    }

    @Override
    void inverseTransform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        while (--n4 >= 0) {
            arrd2[n3++] = arrd[n2++] - d2;
            arrd2[n3++] = arrd[n2++] - d3;
            arrd2[n3++] = arrd[n2++] - d4;
        }
    }

    @Override
    public Point2D inverseDeltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(d2, d3);
    }

    @Override
    public Point2D inverseDeltaTransform(Point2D point2D) {
        if (point2D == null) {
            throw new NullPointerException();
        }
        this.ensureCanTransform2DPoint();
        return point2D;
    }

    @Override
    public Point3D inverseDeltaTransform(double d2, double d3, double d4) {
        return new Point3D(d2, d3, d4);
    }

    @Override
    public Point3D inverseDeltaTransform(Point3D point3D) {
        if (point3D == null) {
            throw new NullPointerException();
        }
        return point3D;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Translate [");
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", z=").append(this.getZ());
        return stringBuilder.append("]").toString();
    }

    @Override
    @Deprecated
    public void impl_apply(Affine3D affine3D) {
        affine3D.translate(this.getX(), this.getY(), this.getZ());
    }

    @Override
    @Deprecated
    public BaseTransform impl_derive(BaseTransform baseTransform) {
        return baseTransform.deriveWithTranslation(this.getX(), this.getY(), this.getZ());
    }

    @Override
    void validate() {
        this.getX();
        this.getY();
        this.getZ();
    }

    @Override
    void appendTo(Affine affine) {
        affine.appendTranslation(this.getTx(), this.getTy(), this.getTz());
    }

    @Override
    void prependTo(Affine affine) {
        affine.prependTranslation(this.getTx(), this.getTy(), this.getTz());
    }
}

