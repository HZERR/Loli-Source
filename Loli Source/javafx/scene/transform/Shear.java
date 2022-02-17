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
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class Shear
extends Transform {
    private DoubleProperty x;
    private DoubleProperty y;
    private DoubleProperty pivotX;
    private DoubleProperty pivotY;

    public Shear() {
    }

    public Shear(double d2, double d3) {
        this.setX(d2);
        this.setY(d3);
    }

    public Shear(double d2, double d3, double d4, double d5) {
        this.setX(d2);
        this.setY(d3);
        this.setPivotX(d4);
        this.setPivotY(d5);
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
                    Shear.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Shear.this;
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
                    Shear.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Shear.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
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
                    Shear.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Shear.this;
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
                    Shear.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Shear.this;
                }

                @Override
                public String getName() {
                    return "pivotY";
                }
            };
        }
        return this.pivotY;
    }

    @Override
    public double getMxy() {
        return this.getX();
    }

    @Override
    public double getMyx() {
        return this.getY();
    }

    @Override
    public double getTx() {
        return -this.getX() * this.getPivotY();
    }

    @Override
    public double getTy() {
        return -this.getY() * this.getPivotX();
    }

    @Override
    boolean computeIs2D() {
        return true;
    }

    @Override
    boolean computeIsIdentity() {
        return this.getX() == 0.0 && this.getY() == 0.0;
    }

    @Override
    void fill2DArray(double[] arrd) {
        double d2 = this.getX();
        double d3 = this.getY();
        arrd[0] = 1.0;
        arrd[1] = d2;
        arrd[2] = -d2 * this.getPivotY();
        arrd[3] = d3;
        arrd[4] = 1.0;
        arrd[5] = -d3 * this.getPivotX();
    }

    @Override
    void fill3DArray(double[] arrd) {
        double d2 = this.getX();
        double d3 = this.getY();
        arrd[0] = 1.0;
        arrd[1] = d2;
        arrd[2] = 0.0;
        arrd[3] = -d2 * this.getPivotY();
        arrd[4] = d3;
        arrd[5] = 1.0;
        arrd[6] = 0.0;
        arrd[7] = -d3 * this.getPivotX();
        arrd[8] = 0.0;
        arrd[9] = 0.0;
        arrd[10] = 1.0;
        arrd[11] = 0.0;
    }

    @Override
    public Transform createConcatenation(Transform transform) {
        if (transform instanceof Affine) {
            Affine affine = (Affine)transform.clone();
            affine.prepend(this);
            return affine;
        }
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = transform.getMxx();
        double d5 = transform.getMxy();
        double d6 = transform.getMxz();
        double d7 = transform.getTx();
        double d8 = transform.getMyx();
        double d9 = transform.getMyy();
        double d10 = transform.getMyz();
        double d11 = transform.getTy();
        return new Affine(d4 + d2 * d8, d5 + d2 * d9, d6 + d2 * d10, d7 + d2 * d11 - d2 * this.getPivotY(), d3 * d4 + d8, d3 * d5 + d9, d3 * d6 + d10, d3 * d7 + d11 - d3 * this.getPivotX(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    @Override
    public Transform createInverse() {
        double d2 = this.getX();
        double d3 = this.getY();
        if (d3 == 0.0) {
            return new Shear(-d2, 0.0, 0.0, this.getPivotY());
        }
        if (d2 == 0.0) {
            return new Shear(0.0, -d3, this.getPivotX(), 0.0);
        }
        double d4 = this.getPivotX();
        double d5 = this.getPivotY();
        double d6 = 1.0 / (1.0 - d2 * d3);
        return new Affine(d6, -d2 * d6, 0.0, d2 * (d5 - d3 * d4) * d6, -d3 * d6, 1.0 + d2 * d3 * d6, 0.0, d3 * d4 + d3 * (d2 * d3 * d4 - d2 * d5) * d6, 0.0, 0.0, 1.0, 0.0);
    }

    @Override
    public Shear clone() {
        return new Shear(this.getX(), this.getY(), this.getPivotX(), this.getPivotY());
    }

    @Override
    public Point2D transform(double d2, double d3) {
        double d4 = this.getX();
        double d5 = this.getY();
        return new Point2D(d2 + d4 * d3 - d4 * this.getPivotY(), d5 * d2 + d3 - d5 * this.getPivotX());
    }

    @Override
    public Point3D transform(double d2, double d3, double d4) {
        double d5 = this.getX();
        double d6 = this.getY();
        return new Point3D(d2 + d5 * d3 - d5 * this.getPivotY(), d6 * d2 + d3 - d6 * this.getPivotX(), d4);
    }

    @Override
    void transform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getPivotX();
        double d5 = this.getPivotY();
        while (--n4 >= 0) {
            double d6 = arrd[n2++];
            double d7 = arrd[n2++];
            arrd2[n3++] = d6 + d2 * d7 - d2 * d5;
            arrd2[n3++] = d3 * d6 + d7 - d3 * d4;
        }
    }

    @Override
    void transform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getPivotX();
        double d5 = this.getPivotY();
        while (--n4 >= 0) {
            double d6 = arrd[n2++];
            double d7 = arrd[n2++];
            arrd2[n3++] = d6 + d2 * d7 - d2 * d5;
            arrd2[n3++] = d3 * d6 + d7 - d3 * d4;
            arrd2[n3++] = arrd[n2++];
        }
    }

    @Override
    public Point2D deltaTransform(double d2, double d3) {
        return new Point2D(d2 + this.getX() * d3, this.getY() * d2 + d3);
    }

    @Override
    public Point3D deltaTransform(double d2, double d3, double d4) {
        return new Point3D(d2 + this.getX() * d3, this.getY() * d2 + d3, d4);
    }

    @Override
    public Point2D inverseTransform(double d2, double d3) throws NonInvertibleTransformException {
        double d4 = this.getX();
        double d5 = this.getY();
        if (d5 == 0.0) {
            double d6 = -this.getX();
            return new Point2D(d2 + d6 * d3 - d6 * this.getPivotY(), d3);
        }
        if (d4 == 0.0) {
            double d7 = -this.getY();
            return new Point2D(d2, d7 * d2 + d3 - d7 * this.getPivotX());
        }
        return super.inverseTransform(d2, d3);
    }

    @Override
    public Point3D inverseTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        double d5 = this.getX();
        double d6 = this.getY();
        if (d6 == 0.0) {
            double d7 = -this.getX();
            return new Point3D(d2 + d7 * d3 - d7 * this.getPivotY(), d3, d4);
        }
        if (d5 == 0.0) {
            double d8 = -this.getY();
            return new Point3D(d2, d8 * d2 + d3 - d8 * this.getPivotX(), d4);
        }
        return super.inverseTransform(d2, d3, d4);
    }

    @Override
    void inverseTransform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        double d2 = this.getPivotX();
        double d3 = this.getPivotY();
        double d4 = this.getX();
        double d5 = this.getY();
        if (d5 == 0.0) {
            double d6 = -d4;
            while (--n4 >= 0) {
                double d7 = arrd[n2++];
                double d8 = arrd[n2++];
                arrd2[n3++] = d7 + d6 * d8 - d6 * d3;
                arrd2[n3++] = d8;
            }
            return;
        }
        if (d4 == 0.0) {
            double d9 = -d5;
            while (--n4 >= 0) {
                double d10 = arrd[n2++];
                double d11 = arrd[n2++];
                arrd2[n3++] = d10;
                arrd2[n3++] = d9 * d10 + d11 - d9 * d2;
            }
            return;
        }
        super.inverseTransform2DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    @Override
    void inverseTransform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        double d2 = this.getPivotX();
        double d3 = this.getPivotY();
        double d4 = this.getX();
        double d5 = this.getY();
        if (d5 == 0.0) {
            double d6 = -d4;
            while (--n4 >= 0) {
                double d7 = arrd[n2++];
                double d8 = arrd[n2++];
                arrd2[n3++] = d7 + d6 * d8 - d6 * d3;
                arrd2[n3++] = d8;
                arrd2[n3++] = arrd[n2++];
            }
            return;
        }
        if (d4 == 0.0) {
            double d9 = -d5;
            while (--n4 >= 0) {
                double d10 = arrd[n2++];
                double d11 = arrd[n2++];
                arrd2[n3++] = d10;
                arrd2[n3++] = d9 * d10 + d11 - d9 * d2;
                arrd2[n3++] = arrd[n2++];
            }
            return;
        }
        super.inverseTransform3DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    @Override
    public Point2D inverseDeltaTransform(double d2, double d3) throws NonInvertibleTransformException {
        double d4 = this.getX();
        double d5 = this.getY();
        if (d5 == 0.0) {
            return new Point2D(d2 - this.getX() * d3, d3);
        }
        if (d4 == 0.0) {
            return new Point2D(d2, -this.getY() * d2 + d3);
        }
        return super.inverseDeltaTransform(d2, d3);
    }

    @Override
    public Point3D inverseDeltaTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        double d5 = this.getX();
        double d6 = this.getY();
        if (d6 == 0.0) {
            return new Point3D(d2 - this.getX() * d3, d3, d4);
        }
        if (d5 == 0.0) {
            return new Point3D(d2, -this.getY() * d2 + d3, d4);
        }
        return super.inverseDeltaTransform(d2, d3, d4);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Shear [");
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", pivotX=").append(this.getPivotX());
        stringBuilder.append(", pivotY=").append(this.getPivotY());
        return stringBuilder.append("]").toString();
    }

    @Override
    @Deprecated
    public void impl_apply(Affine3D affine3D) {
        if (this.getPivotX() != 0.0 || this.getPivotY() != 0.0) {
            affine3D.translate(this.getPivotX(), this.getPivotY());
            affine3D.shear(this.getX(), this.getY());
            affine3D.translate(-this.getPivotX(), -this.getPivotY());
        } else {
            affine3D.shear(this.getX(), this.getY());
        }
    }

    @Override
    @Deprecated
    public BaseTransform impl_derive(BaseTransform baseTransform) {
        return baseTransform.deriveWithConcatenation(1.0, this.getY(), this.getX(), 1.0, this.getTx(), this.getTy());
    }

    @Override
    void validate() {
        this.getX();
        this.getPivotX();
        this.getY();
        this.getPivotY();
    }

    @Override
    void appendTo(Affine affine) {
        affine.appendShear(this.getX(), this.getY(), this.getPivotX(), this.getPivotY());
    }

    @Override
    void prependTo(Affine affine) {
        affine.prependShear(this.getX(), this.getY(), this.getPivotX(), this.getPivotY());
    }
}

