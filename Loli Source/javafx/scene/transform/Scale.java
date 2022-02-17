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
import javafx.scene.transform.Translate;

public class Scale
extends Transform {
    private DoubleProperty x;
    private DoubleProperty y;
    private DoubleProperty z;
    private DoubleProperty pivotX;
    private DoubleProperty pivotY;
    private DoubleProperty pivotZ;

    public Scale() {
    }

    public Scale(double d2, double d3) {
        this.setX(d2);
        this.setY(d3);
    }

    public Scale(double d2, double d3, double d4, double d5) {
        this(d2, d3);
        this.setPivotX(d4);
        this.setPivotY(d5);
    }

    public Scale(double d2, double d3, double d4) {
        this(d2, d3);
        this.setZ(d4);
    }

    public Scale(double d2, double d3, double d4, double d5, double d6, double d7) {
        this(d2, d3, d5, d6);
        this.setZ(d4);
        this.setPivotZ(d7);
    }

    public final void setX(double d2) {
        this.xProperty().set(d2);
    }

    public final double getX() {
        return this.x == null ? 1.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(1.0){

                @Override
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Scale.this;
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
        return this.y == null ? 1.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(1.0){

                @Override
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Scale.this;
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
        return this.z == null ? 1.0 : this.z.get();
    }

    public final DoubleProperty zProperty() {
        if (this.z == null) {
            this.z = new DoublePropertyBase(1.0){

                @Override
                public void invalidated() {
                    Scale.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Scale.this;
                }

                @Override
                public String getName() {
                    return "z";
                }
            };
        }
        return this.z;
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
                    Scale.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Scale.this;
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
                    Scale.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Scale.this;
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
                    Scale.this.transformChanged();
                }

                @Override
                public Object getBean() {
                    return Scale.this;
                }

                @Override
                public String getName() {
                    return "pivotZ";
                }
            };
        }
        return this.pivotZ;
    }

    @Override
    public double getMxx() {
        return this.getX();
    }

    @Override
    public double getMyy() {
        return this.getY();
    }

    @Override
    public double getMzz() {
        return this.getZ();
    }

    @Override
    public double getTx() {
        return (1.0 - this.getX()) * this.getPivotX();
    }

    @Override
    public double getTy() {
        return (1.0 - this.getY()) * this.getPivotY();
    }

    @Override
    public double getTz() {
        return (1.0 - this.getZ()) * this.getPivotZ();
    }

    @Override
    boolean computeIs2D() {
        return this.getZ() == 1.0;
    }

    @Override
    boolean computeIsIdentity() {
        return this.getX() == 1.0 && this.getY() == 1.0 && this.getZ() == 1.0;
    }

    @Override
    void fill2DArray(double[] arrd) {
        double d2 = this.getX();
        double d3 = this.getY();
        arrd[0] = d2;
        arrd[1] = 0.0;
        arrd[2] = (1.0 - d2) * this.getPivotX();
        arrd[3] = 0.0;
        arrd[4] = d3;
        arrd[5] = (1.0 - d3) * this.getPivotY();
    }

    @Override
    void fill3DArray(double[] arrd) {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        arrd[0] = d2;
        arrd[1] = 0.0;
        arrd[2] = 0.0;
        arrd[3] = (1.0 - d2) * this.getPivotX();
        arrd[4] = 0.0;
        arrd[5] = d3;
        arrd[6] = 0.0;
        arrd[7] = (1.0 - d3) * this.getPivotY();
        arrd[8] = 0.0;
        arrd[9] = 0.0;
        arrd[10] = d4;
        arrd[11] = (1.0 - d4) * this.getPivotZ();
    }

    @Override
    public Transform createConcatenation(Transform transform) {
        Transform transform2;
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        if (transform instanceof Scale && ((Scale)(transform2 = (Scale)transform)).getPivotX() == this.getPivotX() && ((Scale)transform2).getPivotY() == this.getPivotY() && ((Scale)transform2).getPivotZ() == this.getPivotZ()) {
            return new Scale(d2 * ((Scale)transform2).getX(), d3 * ((Scale)transform2).getY(), d4 * ((Scale)transform2).getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
        }
        if (transform instanceof Translate) {
            transform2 = (Translate)transform;
            double d5 = ((Translate)transform2).getX();
            double d6 = ((Translate)transform2).getY();
            double d7 = ((Translate)transform2).getZ();
            if ((d5 == 0.0 || d2 != 1.0 && d2 != 0.0) && (d6 == 0.0 || d3 != 1.0 && d3 != 0.0) && (d7 == 0.0 || d4 != 1.0 && d4 != 0.0)) {
                return new Scale(d2, d3, d4, (d2 != 1.0 ? d2 * d5 / (1.0 - d2) : 0.0) + this.getPivotX(), (d3 != 1.0 ? d3 * d6 / (1.0 - d3) : 0.0) + this.getPivotY(), (d4 != 1.0 ? d4 * d7 / (1.0 - d4) : 0.0) + this.getPivotZ());
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
        return new Affine(d2 * d8, d2 * d9, d2 * d10, d2 * d11 + (1.0 - d2) * this.getPivotX(), d3 * d12, d3 * d13, d3 * d14, d3 * d15 + (1.0 - d3) * this.getPivotY(), d4 * d16, d4 * d17, d4 * d18, d4 * d19 + (1.0 - d4) * this.getPivotZ());
    }

    @Override
    public Scale createInverse() throws NonInvertibleTransformException {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        if (d2 == 0.0 || d3 == 0.0 || d4 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        return new Scale(1.0 / d2, 1.0 / d3, 1.0 / d4, this.getPivotX(), this.getPivotY(), this.getPivotZ());
    }

    @Override
    public Scale clone() {
        return new Scale(this.getX(), this.getY(), this.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
    }

    @Override
    public Point2D transform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        double d4 = this.getX();
        double d5 = this.getY();
        return new Point2D(d4 * d2 + (1.0 - d4) * this.getPivotX(), d5 * d3 + (1.0 - d5) * this.getPivotY());
    }

    @Override
    public Point3D transform(double d2, double d3, double d4) {
        double d5 = this.getX();
        double d6 = this.getY();
        double d7 = this.getZ();
        return new Point3D(d5 * d2 + (1.0 - d5) * this.getPivotX(), d6 * d3 + (1.0 - d6) * this.getPivotY(), d7 * d4 + (1.0 - d7) * this.getPivotZ());
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
            arrd2[n3++] = d2 * d6 + (1.0 - d2) * d4;
            arrd2[n3++] = d3 * d7 + (1.0 - d3) * d5;
        }
    }

    @Override
    void transform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        double d5 = this.getPivotX();
        double d6 = this.getPivotY();
        double d7 = this.getPivotZ();
        while (--n4 >= 0) {
            arrd2[n3++] = d2 * arrd[n2++] + (1.0 - d2) * d5;
            arrd2[n3++] = d3 * arrd[n2++] + (1.0 - d3) * d6;
            arrd2[n3++] = d4 * arrd[n2++] + (1.0 - d4) * d7;
        }
    }

    @Override
    public Point2D deltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(this.getX() * d2, this.getY() * d3);
    }

    @Override
    public Point3D deltaTransform(double d2, double d3, double d4) {
        return new Point3D(this.getX() * d2, this.getY() * d3, this.getZ() * d4);
    }

    @Override
    public Point2D inverseTransform(double d2, double d3) throws NonInvertibleTransformException {
        this.ensureCanTransform2DPoint();
        double d4 = this.getX();
        double d5 = this.getY();
        if (d4 == 0.0 || d5 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double d6 = 1.0 / d4;
        double d7 = 1.0 / d5;
        return new Point2D(d6 * d2 + (1.0 - d6) * this.getPivotX(), d7 * d3 + (1.0 - d7) * this.getPivotY());
    }

    @Override
    public Point3D inverseTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        double d5 = this.getX();
        double d6 = this.getY();
        double d7 = this.getZ();
        if (d5 == 0.0 || d6 == 0.0 || d7 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double d8 = 1.0 / d5;
        double d9 = 1.0 / d6;
        double d10 = 1.0 / d7;
        return new Point3D(d8 * d2 + (1.0 - d8) * this.getPivotX(), d9 * d3 + (1.0 - d9) * this.getPivotY(), d10 * d4 + (1.0 - d10) * this.getPivotZ());
    }

    @Override
    void inverseTransform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        double d2 = this.getX();
        double d3 = this.getY();
        if (d2 == 0.0 || d3 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double d4 = 1.0 / d2;
        double d5 = 1.0 / d3;
        double d6 = this.getPivotX();
        double d7 = this.getPivotY();
        while (--n4 >= 0) {
            arrd2[n3++] = d4 * arrd[n2++] + (1.0 - d4) * d6;
            arrd2[n3++] = d5 * arrd[n2++] + (1.0 - d5) * d7;
        }
    }

    @Override
    void inverseTransform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        double d2 = this.getX();
        double d3 = this.getY();
        double d4 = this.getZ();
        if (d2 == 0.0 || d3 == 0.0 || d4 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        double d5 = 1.0 / d2;
        double d6 = 1.0 / d3;
        double d7 = 1.0 / d4;
        double d8 = this.getPivotX();
        double d9 = this.getPivotY();
        double d10 = this.getPivotZ();
        while (--n4 >= 0) {
            arrd2[n3++] = d5 * arrd[n2++] + (1.0 - d5) * d8;
            arrd2[n3++] = d6 * arrd[n2++] + (1.0 - d6) * d9;
            arrd2[n3++] = d7 * arrd[n2++] + (1.0 - d7) * d10;
        }
    }

    @Override
    public Point2D inverseDeltaTransform(double d2, double d3) throws NonInvertibleTransformException {
        this.ensureCanTransform2DPoint();
        double d4 = this.getX();
        double d5 = this.getY();
        if (d4 == 0.0 || d5 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        return new Point2D(1.0 / d4 * d2, 1.0 / d5 * d3);
    }

    @Override
    public Point3D inverseDeltaTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        double d5 = this.getX();
        double d6 = this.getY();
        double d7 = this.getZ();
        if (d5 == 0.0 || d6 == 0.0 || d7 == 0.0) {
            throw new NonInvertibleTransformException("Zero scale is not invertible");
        }
        return new Point3D(1.0 / d5 * d2, 1.0 / d6 * d3, 1.0 / d7 * d4);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Scale [");
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", z=").append(this.getZ());
        stringBuilder.append(", pivotX=").append(this.getPivotX());
        stringBuilder.append(", pivotY=").append(this.getPivotY());
        stringBuilder.append(", pivotZ=").append(this.getPivotZ());
        return stringBuilder.append("]").toString();
    }

    @Override
    @Deprecated
    public void impl_apply(Affine3D affine3D) {
        if (this.getPivotX() != 0.0 || this.getPivotY() != 0.0 || this.getPivotZ() != 0.0) {
            affine3D.translate(this.getPivotX(), this.getPivotY(), this.getPivotZ());
            affine3D.scale(this.getX(), this.getY(), this.getZ());
            affine3D.translate(-this.getPivotX(), -this.getPivotY(), -this.getPivotZ());
        } else {
            affine3D.scale(this.getX(), this.getY(), this.getZ());
        }
    }

    @Override
    @Deprecated
    public BaseTransform impl_derive(BaseTransform baseTransform) {
        if (this.isIdentity()) {
            return baseTransform;
        }
        if (this.getPivotX() != 0.0 || this.getPivotY() != 0.0 || this.getPivotZ() != 0.0) {
            baseTransform = baseTransform.deriveWithTranslation(this.getPivotX(), this.getPivotY(), this.getPivotZ());
            baseTransform = baseTransform.deriveWithScale(this.getX(), this.getY(), this.getZ());
            return baseTransform.deriveWithTranslation(-this.getPivotX(), -this.getPivotY(), -this.getPivotZ());
        }
        return baseTransform.deriveWithScale(this.getX(), this.getY(), this.getZ());
    }

    @Override
    void validate() {
        this.getX();
        this.getPivotX();
        this.getY();
        this.getPivotY();
        this.getZ();
        this.getPivotZ();
    }

    @Override
    void appendTo(Affine affine) {
        affine.appendScale(this.getX(), this.getY(), this.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
    }

    @Override
    void prependTo(Affine affine) {
        affine.prependScale(this.getX(), this.getY(), this.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
    }
}

