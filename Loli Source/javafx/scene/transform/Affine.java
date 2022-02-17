/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.MatrixType;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class Affine
extends Transform {
    AffineAtomicChange atomicChange = new AffineAtomicChange();
    private static final int APPLY_IDENTITY = 0;
    private static final int APPLY_TRANSLATE = 1;
    private static final int APPLY_SCALE = 2;
    private static final int APPLY_SHEAR = 4;
    private static final int APPLY_NON_3D = 0;
    private static final int APPLY_3D_COMPLEX = 4;
    private transient int state2d;
    private transient int state3d;
    private double xx;
    private double xy;
    private double xz;
    private double yx;
    private double yy;
    private double yz;
    private double zx;
    private double zy;
    private double zz;
    private double xt;
    private double yt;
    private double zt;
    private AffineElementProperty mxx;
    private AffineElementProperty mxy;
    private AffineElementProperty mxz;
    private AffineElementProperty tx;
    private AffineElementProperty myx;
    private AffineElementProperty myy;
    private AffineElementProperty myz;
    private AffineElementProperty ty;
    private AffineElementProperty mzx;
    private AffineElementProperty mzy;
    private AffineElementProperty mzz;
    private AffineElementProperty tz;
    private static final int[] rot90conversion = new int[]{4, 5, 4, 5, 2, 3, 6, 7};

    public Affine() {
        this.zz = 1.0;
        this.yy = 1.0;
        this.xx = 1.0;
    }

    public Affine(Transform transform) {
        this(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    public Affine(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.xx = d2;
        this.xy = d3;
        this.xt = d4;
        this.yx = d5;
        this.yy = d6;
        this.yt = d7;
        this.zz = 1.0;
        this.updateState2D();
    }

    public Affine(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        this.xx = d2;
        this.xy = d3;
        this.xz = d4;
        this.xt = d5;
        this.yx = d6;
        this.yy = d7;
        this.yz = d8;
        this.yt = d9;
        this.zx = d10;
        this.zy = d11;
        this.zz = d12;
        this.zt = d13;
        this.updateState();
    }

    public Affine(double[] arrd, MatrixType matrixType, int n2) {
        if (arrd.length < n2 + matrixType.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (matrixType) {
            default: {
                Affine.stateError();
            }
            case MT_2D_3x3: {
                if (arrd[n2 + 6] != 0.0 || arrd[n2 + 7] != 0.0 || arrd[n2 + 8] != 1.0) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
            }
            case MT_2D_2x3: {
                this.xx = arrd[n2++];
                this.xy = arrd[n2++];
                this.xt = arrd[n2++];
                this.yx = arrd[n2++];
                this.yy = arrd[n2++];
                this.yt = arrd[n2];
                this.zz = 1.0;
                this.updateState2D();
                return;
            }
            case MT_3D_4x4: {
                if (arrd[n2 + 12] == 0.0 && arrd[n2 + 13] == 0.0 && arrd[n2 + 14] == 0.0 && arrd[n2 + 15] == 1.0) break;
                throw new IllegalArgumentException("The matrix is not affine");
            }
            case MT_3D_3x4: 
        }
        this.xx = arrd[n2++];
        this.xy = arrd[n2++];
        this.xz = arrd[n2++];
        this.xt = arrd[n2++];
        this.yx = arrd[n2++];
        this.yy = arrd[n2++];
        this.yz = arrd[n2++];
        this.yt = arrd[n2++];
        this.zx = arrd[n2++];
        this.zy = arrd[n2++];
        this.zz = arrd[n2++];
        this.zt = arrd[n2];
        this.updateState();
    }

    public final void setMxx(double d2) {
        if (this.mxx == null) {
            if (this.xx != d2) {
                this.xx = d2;
                this.postProcessChange();
            }
        } else {
            this.mxxProperty().set(d2);
        }
    }

    @Override
    public final double getMxx() {
        return this.mxx == null ? this.xx : this.mxx.get();
    }

    public final DoubleProperty mxxProperty() {
        if (this.mxx == null) {
            this.mxx = new AffineElementProperty(this.xx){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "mxx";
                }
            };
        }
        return this.mxx;
    }

    public final void setMxy(double d2) {
        if (this.mxy == null) {
            if (this.xy != d2) {
                this.xy = d2;
                this.postProcessChange();
            }
        } else {
            this.mxyProperty().set(d2);
        }
    }

    @Override
    public final double getMxy() {
        return this.mxy == null ? this.xy : this.mxy.get();
    }

    public final DoubleProperty mxyProperty() {
        if (this.mxy == null) {
            this.mxy = new AffineElementProperty(this.xy){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "mxy";
                }
            };
        }
        return this.mxy;
    }

    public final void setMxz(double d2) {
        if (this.mxz == null) {
            if (this.xz != d2) {
                this.xz = d2;
                this.postProcessChange();
            }
        } else {
            this.mxzProperty().set(d2);
        }
    }

    @Override
    public final double getMxz() {
        return this.mxz == null ? this.xz : this.mxz.get();
    }

    public final DoubleProperty mxzProperty() {
        if (this.mxz == null) {
            this.mxz = new AffineElementProperty(this.xz){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "mxz";
                }
            };
        }
        return this.mxz;
    }

    public final void setTx(double d2) {
        if (this.tx == null) {
            if (this.xt != d2) {
                this.xt = d2;
                this.postProcessChange();
            }
        } else {
            this.txProperty().set(d2);
        }
    }

    @Override
    public final double getTx() {
        return this.tx == null ? this.xt : this.tx.get();
    }

    public final DoubleProperty txProperty() {
        if (this.tx == null) {
            this.tx = new AffineElementProperty(this.xt){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "tx";
                }
            };
        }
        return this.tx;
    }

    public final void setMyx(double d2) {
        if (this.myx == null) {
            if (this.yx != d2) {
                this.yx = d2;
                this.postProcessChange();
            }
        } else {
            this.myxProperty().set(d2);
        }
    }

    @Override
    public final double getMyx() {
        return this.myx == null ? this.yx : this.myx.get();
    }

    public final DoubleProperty myxProperty() {
        if (this.myx == null) {
            this.myx = new AffineElementProperty(this.yx){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "myx";
                }
            };
        }
        return this.myx;
    }

    public final void setMyy(double d2) {
        if (this.myy == null) {
            if (this.yy != d2) {
                this.yy = d2;
                this.postProcessChange();
            }
        } else {
            this.myyProperty().set(d2);
        }
    }

    @Override
    public final double getMyy() {
        return this.myy == null ? this.yy : this.myy.get();
    }

    public final DoubleProperty myyProperty() {
        if (this.myy == null) {
            this.myy = new AffineElementProperty(this.yy){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "myy";
                }
            };
        }
        return this.myy;
    }

    public final void setMyz(double d2) {
        if (this.myz == null) {
            if (this.yz != d2) {
                this.yz = d2;
                this.postProcessChange();
            }
        } else {
            this.myzProperty().set(d2);
        }
    }

    @Override
    public final double getMyz() {
        return this.myz == null ? this.yz : this.myz.get();
    }

    public final DoubleProperty myzProperty() {
        if (this.myz == null) {
            this.myz = new AffineElementProperty(this.yz){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "myz";
                }
            };
        }
        return this.myz;
    }

    public final void setTy(double d2) {
        if (this.ty == null) {
            if (this.yt != d2) {
                this.yt = d2;
                this.postProcessChange();
            }
        } else {
            this.tyProperty().set(d2);
        }
    }

    @Override
    public final double getTy() {
        return this.ty == null ? this.yt : this.ty.get();
    }

    public final DoubleProperty tyProperty() {
        if (this.ty == null) {
            this.ty = new AffineElementProperty(this.yt){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "ty";
                }
            };
        }
        return this.ty;
    }

    public final void setMzx(double d2) {
        if (this.mzx == null) {
            if (this.zx != d2) {
                this.zx = d2;
                this.postProcessChange();
            }
        } else {
            this.mzxProperty().set(d2);
        }
    }

    @Override
    public final double getMzx() {
        return this.mzx == null ? this.zx : this.mzx.get();
    }

    public final DoubleProperty mzxProperty() {
        if (this.mzx == null) {
            this.mzx = new AffineElementProperty(this.zx){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "mzx";
                }
            };
        }
        return this.mzx;
    }

    public final void setMzy(double d2) {
        if (this.mzy == null) {
            if (this.zy != d2) {
                this.zy = d2;
                this.postProcessChange();
            }
        } else {
            this.mzyProperty().set(d2);
        }
    }

    @Override
    public final double getMzy() {
        return this.mzy == null ? this.zy : this.mzy.get();
    }

    public final DoubleProperty mzyProperty() {
        if (this.mzy == null) {
            this.mzy = new AffineElementProperty(this.zy){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "mzy";
                }
            };
        }
        return this.mzy;
    }

    public final void setMzz(double d2) {
        if (this.mzz == null) {
            if (this.zz != d2) {
                this.zz = d2;
                this.postProcessChange();
            }
        } else {
            this.mzzProperty().set(d2);
        }
    }

    @Override
    public final double getMzz() {
        return this.mzz == null ? this.zz : this.mzz.get();
    }

    public final DoubleProperty mzzProperty() {
        if (this.mzz == null) {
            this.mzz = new AffineElementProperty(this.zz){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "mzz";
                }
            };
        }
        return this.mzz;
    }

    public final void setTz(double d2) {
        if (this.tz == null) {
            if (this.zt != d2) {
                this.zt = d2;
                this.postProcessChange();
            }
        } else {
            this.tzProperty().set(d2);
        }
    }

    @Override
    public final double getTz() {
        return this.tz == null ? this.zt : this.tz.get();
    }

    public final DoubleProperty tzProperty() {
        if (this.tz == null) {
            this.tz = new AffineElementProperty(this.zt){

                @Override
                public Object getBean() {
                    return Affine.this;
                }

                @Override
                public String getName() {
                    return "tz";
                }
            };
        }
        return this.tz;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setElement(MatrixType matrixType, int n2, int n3, double d2) {
        if (n2 < 0 || n2 >= matrixType.rows() || n3 < 0 || n3 >= matrixType.columns()) {
            throw new IndexOutOfBoundsException("Index outside of affine matrix " + (Object)((Object)matrixType) + ": [" + n2 + ", " + n3 + "]");
        }
        switch (matrixType) {
            default: {
                Affine.stateError();
            }
            case MT_2D_3x3: 
            case MT_2D_2x3: {
                if (!this.isType2D()) {
                    throw new IllegalArgumentException("Cannot access 2D matrix of a 3D transform");
                }
                switch (n2) {
                    case 0: {
                        switch (n3) {
                            case 0: {
                                this.setMxx(d2);
                                return;
                            }
                            case 1: {
                                this.setMxy(d2);
                                return;
                            }
                            case 2: {
                                this.setTx(d2);
                                return;
                            }
                        }
                    }
                    case 1: {
                        switch (n3) {
                            case 0: {
                                this.setMyx(d2);
                                return;
                            }
                            case 1: {
                                this.setMyy(d2);
                                return;
                            }
                            case 2: {
                                this.setTy(d2);
                                return;
                            }
                        }
                    }
                    case 2: {
                        switch (n3) {
                            case 0: {
                                if (d2 != 0.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                            case 1: {
                                if (d2 != 0.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                            case 2: {
                                if (d2 != 1.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                        }
                    }
                }
                throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
            }
            case MT_3D_4x4: 
            case MT_3D_3x4: {
                switch (n2) {
                    case 0: {
                        switch (n3) {
                            case 0: {
                                this.setMxx(d2);
                                return;
                            }
                            case 1: {
                                this.setMxy(d2);
                                return;
                            }
                            case 2: {
                                this.setMxz(d2);
                                return;
                            }
                            case 3: {
                                this.setTx(d2);
                                return;
                            }
                        }
                    }
                    case 1: {
                        switch (n3) {
                            case 0: {
                                this.setMyx(d2);
                                return;
                            }
                            case 1: {
                                this.setMyy(d2);
                                return;
                            }
                            case 2: {
                                this.setMyz(d2);
                                return;
                            }
                            case 3: {
                                this.setTy(d2);
                                return;
                            }
                        }
                    }
                    case 2: {
                        switch (n3) {
                            case 0: {
                                this.setMzx(d2);
                                return;
                            }
                            case 1: {
                                this.setMzy(d2);
                                return;
                            }
                            case 2: {
                                this.setMzz(d2);
                                return;
                            }
                            case 3: {
                                this.setTz(d2);
                                return;
                            }
                        }
                    }
                    case 3: {
                        switch (n3) {
                            case 0: {
                                if (d2 != 0.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                            case 1: {
                                if (d2 != 0.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                            case 2: {
                                if (d2 != 0.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                            case 3: {
                                if (d2 != 1.0) throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
                                return;
                            }
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("Cannot set affine matrix " + (Object)((Object)matrixType) + " element " + "[" + n2 + ", " + n3 + "] to " + d2);
    }

    private void postProcessChange() {
        if (!this.atomicChange.runs()) {
            this.updateState();
            this.transformChanged();
        }
    }

    @Override
    boolean computeIs2D() {
        return this.state3d == 0;
    }

    @Override
    boolean computeIsIdentity() {
        return this.state3d == 0 && this.state2d == 0;
    }

    @Override
    public double determinant() {
        if (this.state3d == 0) {
            return this.getDeterminant2D();
        }
        return this.getDeterminant3D();
    }

    private double getDeterminant2D() {
        switch (this.state2d) {
            default: {
                Affine.stateError();
            }
            case 6: 
            case 7: {
                return this.getMxx() * this.getMyy() - this.getMxy() * this.getMyx();
            }
            case 4: 
            case 5: {
                return -(this.getMxy() * this.getMyx());
            }
            case 2: 
            case 3: {
                return this.getMxx() * this.getMyy();
            }
            case 0: 
            case 1: 
        }
        return 1.0;
    }

    private double getDeterminant3D() {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 1: {
                return 1.0;
            }
            case 2: 
            case 3: {
                return this.getMxx() * this.getMyy() * this.getMzz();
            }
            case 4: 
        }
        double d2 = this.getMyx();
        double d3 = this.getMyy();
        double d4 = this.getMyz();
        double d5 = this.getMzx();
        double d6 = this.getMzy();
        double d7 = this.getMzz();
        return this.getMxx() * (d3 * d7 - d6 * d4) + this.getMxy() * (d4 * d5 - d7 * d2) + this.getMxz() * (d2 * d6 - d5 * d3);
    }

    @Override
    public Transform createConcatenation(Transform transform) {
        Affine affine = this.clone();
        affine.append(transform);
        return affine;
    }

    @Override
    public Affine createInverse() throws NonInvertibleTransformException {
        Affine affine = this.clone();
        affine.invert();
        return affine;
    }

    @Override
    public Affine clone() {
        return new Affine(this);
    }

    public void setToTransform(Transform transform) {
        this.setToTransform(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    public void setToTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.setToTransform(d2, d3, 0.0, d4, d5, d6, 0.0, d7, 0.0, 0.0, 1.0, 0.0);
    }

    public void setToTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        this.atomicChange.start();
        this.setMxx(d2);
        this.setMxy(d3);
        this.setMxz(d4);
        this.setTx(d5);
        this.setMyx(d6);
        this.setMyy(d7);
        this.setMyz(d8);
        this.setTy(d9);
        this.setMzx(d10);
        this.setMzy(d11);
        this.setMzz(d12);
        this.setTz(d13);
        this.updateState();
        this.atomicChange.end();
    }

    public void setToTransform(double[] arrd, MatrixType matrixType, int n2) {
        if (arrd.length < n2 + matrixType.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (matrixType) {
            default: {
                Affine.stateError();
            }
            case MT_2D_3x3: {
                if (arrd[n2 + 6] != 0.0 || arrd[n2 + 7] != 0.0 || arrd[n2 + 8] != 1.0) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
            }
            case MT_2D_2x3: {
                this.setToTransform(arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++]);
                return;
            }
            case MT_3D_4x4: {
                if (arrd[n2 + 12] == 0.0 && arrd[n2 + 13] == 0.0 && arrd[n2 + 14] == 0.0 && arrd[n2 + 15] == 1.0) break;
                throw new IllegalArgumentException("The matrix is not affine");
            }
            case MT_3D_3x4: 
        }
        this.setToTransform(arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++]);
    }

    public void setToIdentity() {
        this.atomicChange.start();
        if (this.state3d != 0) {
            this.setMxx(1.0);
            this.setMxy(0.0);
            this.setMxz(0.0);
            this.setTx(0.0);
            this.setMyx(0.0);
            this.setMyy(1.0);
            this.setMyz(0.0);
            this.setTy(0.0);
            this.setMzx(0.0);
            this.setMzy(0.0);
            this.setMzz(1.0);
            this.setTz(0.0);
            this.state3d = 0;
            this.state2d = 0;
        } else if (this.state2d != 0) {
            this.setMxx(1.0);
            this.setMxy(0.0);
            this.setTx(0.0);
            this.setMyx(0.0);
            this.setMyy(1.0);
            this.setTy(0.0);
            this.state2d = 0;
        }
        this.atomicChange.end();
    }

    public void invert() throws NonInvertibleTransformException {
        this.atomicChange.start();
        if (this.state3d == 0) {
            this.invert2D();
            this.updateState2D();
        } else {
            this.invert3D();
            this.updateState();
        }
        this.atomicChange.end();
    }

    private void invert2D() throws NonInvertibleTransformException {
        switch (this.state2d) {
            default: {
                Affine.stateError();
            }
            case 7: {
                double d2 = this.getMxx();
                double d3 = this.getMxy();
                double d4 = this.getTx();
                double d5 = this.getMyx();
                double d6 = this.getMyy();
                double d7 = this.getTy();
                double d8 = this.getDeterminant2D();
                if (d8 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMxx(d6 / d8);
                this.setMyx(-d5 / d8);
                this.setMxy(-d3 / d8);
                this.setMyy(d2 / d8);
                this.setTx((d3 * d7 - d6 * d4) / d8);
                this.setTy((d5 * d4 - d2 * d7) / d8);
                return;
            }
            case 6: {
                double d9 = this.getMxx();
                double d10 = this.getMxy();
                double d11 = this.getMyx();
                double d12 = this.getMyy();
                double d13 = this.getDeterminant2D();
                if (d13 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMxx(d12 / d13);
                this.setMyx(-d11 / d13);
                this.setMxy(-d10 / d13);
                this.setMyy(d9 / d13);
                return;
            }
            case 5: {
                double d14 = this.getMxy();
                double d15 = this.getTx();
                double d16 = this.getMyx();
                double d17 = this.getTy();
                if (d14 == 0.0 || d16 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMyx(1.0 / d14);
                this.setMxy(1.0 / d16);
                this.setTx(-d17 / d16);
                this.setTy(-d15 / d14);
                return;
            }
            case 4: {
                double d18 = this.getMxy();
                double d19 = this.getMyx();
                if (d18 == 0.0 || d19 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMyx(1.0 / d18);
                this.setMxy(1.0 / d19);
                return;
            }
            case 3: {
                double d20 = this.getMxx();
                double d21 = this.getTx();
                double d22 = this.getMyy();
                double d23 = this.getTy();
                if (d20 == 0.0 || d22 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMxx(1.0 / d20);
                this.setMyy(1.0 / d22);
                this.setTx(-d21 / d20);
                this.setTy(-d23 / d22);
                return;
            }
            case 2: {
                double d24 = this.getMxx();
                double d25 = this.getMyy();
                if (d24 == 0.0 || d25 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMxx(1.0 / d24);
                this.setMyy(1.0 / d25);
                return;
            }
            case 1: {
                this.setTx(-this.getTx());
                this.setTy(-this.getTy());
                return;
            }
            case 0: 
        }
    }

    private void invert3D() throws NonInvertibleTransformException {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 1: {
                this.setTx(-this.getTx());
                this.setTy(-this.getTy());
                this.setTz(-this.getTz());
                return;
            }
            case 2: {
                double d2 = this.getMxx();
                double d3 = this.getMyy();
                double d4 = this.getMzz();
                if (d2 == 0.0 || d3 == 0.0 || d4 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMxx(1.0 / d2);
                this.setMyy(1.0 / d3);
                this.setMzz(1.0 / d4);
                return;
            }
            case 3: {
                double d5 = this.getMxx();
                double d6 = this.getTx();
                double d7 = this.getMyy();
                double d8 = this.getTy();
                double d9 = this.getMzz();
                double d10 = this.getTz();
                if (d5 == 0.0 || d7 == 0.0 || d9 == 0.0) {
                    this.atomicChange.cancel();
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                this.setMxx(1.0 / d5);
                this.setMyy(1.0 / d7);
                this.setMzz(1.0 / d9);
                this.setTx(-d6 / d5);
                this.setTy(-d8 / d7);
                this.setTz(-d10 / d9);
                return;
            }
            case 4: 
        }
        double d11 = this.getMxx();
        double d12 = this.getMxy();
        double d13 = this.getMxz();
        double d14 = this.getTx();
        double d15 = this.getMyx();
        double d16 = this.getMyy();
        double d17 = this.getMyz();
        double d18 = this.getTy();
        double d19 = this.getMzy();
        double d20 = this.getMzx();
        double d21 = this.getMzz();
        double d22 = this.getTz();
        double d23 = d11 * (d16 * d21 - d19 * d17) + d12 * (d17 * d20 - d21 * d15) + d13 * (d15 * d19 - d20 * d16);
        if (d23 == 0.0) {
            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
        }
        double d24 = d16 * d21 - d17 * d19;
        double d25 = -d15 * d21 + d17 * d20;
        double d26 = d15 * d19 - d16 * d20;
        double d27 = -d12 * (d17 * d22 - d21 * d18) - d13 * (d18 * d19 - d22 * d16) - d14 * (d16 * d21 - d19 * d17);
        double d28 = -d12 * d21 + d13 * d19;
        double d29 = d11 * d21 - d13 * d20;
        double d30 = -d11 * d19 + d12 * d20;
        double d31 = d11 * (d17 * d22 - d21 * d18) + d13 * (d18 * d20 - d22 * d15) + d14 * (d15 * d21 - d20 * d17);
        double d32 = d12 * d17 - d13 * d16;
        double d33 = -d11 * d17 + d13 * d15;
        double d34 = d11 * d16 - d12 * d15;
        double d35 = -d11 * (d16 * d22 - d19 * d18) - d12 * (d18 * d20 - d22 * d15) - d14 * (d15 * d19 - d20 * d16);
        this.setMxx(d24 / d23);
        this.setMxy(d28 / d23);
        this.setMxz(d32 / d23);
        this.setTx(d27 / d23);
        this.setMyx(d25 / d23);
        this.setMyy(d29 / d23);
        this.setMyz(d33 / d23);
        this.setTy(d31 / d23);
        this.setMzx(d26 / d23);
        this.setMzy(d30 / d23);
        this.setMzz(d34 / d23);
        this.setTz(d35 / d23);
    }

    public void append(Transform transform) {
        transform.appendTo(this);
    }

    public void append(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (this.state3d == 0) {
            this.atomicChange.start();
            double d8 = this.getMxx();
            double d9 = this.getMxy();
            double d10 = this.getMyx();
            double d11 = this.getMyy();
            this.setMxx(d8 * d2 + d9 * d5);
            this.setMxy(d8 * d3 + d9 * d6);
            this.setTx(d8 * d4 + d9 * d7 + this.getTx());
            this.setMyx(d10 * d2 + d11 * d5);
            this.setMyy(d10 * d3 + d11 * d6);
            this.setTy(d10 * d4 + d11 * d7 + this.getTy());
            this.updateState();
            this.atomicChange.end();
        } else {
            this.append(d2, d3, 0.0, d4, d5, d6, 0.0, d7, 0.0, 0.0, 1.0, 0.0);
        }
    }

    public void append(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        this.atomicChange.start();
        double d14 = this.getMxx();
        double d15 = this.getMxy();
        double d16 = this.getMxz();
        double d17 = this.getTx();
        double d18 = this.getMyx();
        double d19 = this.getMyy();
        double d20 = this.getMyz();
        double d21 = this.getTy();
        double d22 = this.getMzx();
        double d23 = this.getMzy();
        double d24 = this.getMzz();
        double d25 = this.getTz();
        this.setMxx(d14 * d2 + d15 * d6 + d16 * d10);
        this.setMxy(d14 * d3 + d15 * d7 + d16 * d11);
        this.setMxz(d14 * d4 + d15 * d8 + d16 * d12);
        this.setTx(d14 * d5 + d15 * d9 + d16 * d13 + d17);
        this.setMyx(d18 * d2 + d19 * d6 + d20 * d10);
        this.setMyy(d18 * d3 + d19 * d7 + d20 * d11);
        this.setMyz(d18 * d4 + d19 * d8 + d20 * d12);
        this.setTy(d18 * d5 + d19 * d9 + d20 * d13 + d21);
        this.setMzx(d22 * d2 + d23 * d6 + d24 * d10);
        this.setMzy(d22 * d3 + d23 * d7 + d24 * d11);
        this.setMzz(d22 * d4 + d23 * d8 + d24 * d12);
        this.setTz(d22 * d5 + d23 * d9 + d24 * d13 + d25);
        this.updateState();
        this.atomicChange.end();
    }

    public void append(double[] arrd, MatrixType matrixType, int n2) {
        if (arrd.length < n2 + matrixType.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (matrixType) {
            default: {
                Affine.stateError();
            }
            case MT_2D_3x3: {
                if (arrd[n2 + 6] != 0.0 || arrd[n2 + 7] != 0.0 || arrd[n2 + 8] != 1.0) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
            }
            case MT_2D_2x3: {
                this.append(arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++]);
                return;
            }
            case MT_3D_4x4: {
                if (arrd[n2 + 12] == 0.0 && arrd[n2 + 13] == 0.0 && arrd[n2 + 14] == 0.0 && arrd[n2 + 15] == 1.0) break;
                throw new IllegalArgumentException("The matrix is not affine");
            }
            case MT_3D_3x4: 
        }
        this.append(arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++]);
    }

    @Override
    void appendTo(Affine affine) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    case 0: {
                        return;
                    }
                    case 1: {
                        affine.appendTranslation(this.getTx(), this.getTy());
                        return;
                    }
                    case 2: {
                        affine.appendScale(this.getMxx(), this.getMyy());
                        return;
                    }
                    case 3: {
                        affine.appendTranslation(this.getTx(), this.getTy());
                        affine.appendScale(this.getMxx(), this.getMyy());
                        return;
                    }
                }
                affine.append(this.getMxx(), this.getMxy(), this.getTx(), this.getMyx(), this.getMyy(), this.getTy());
                return;
            }
            case 1: {
                affine.appendTranslation(this.getTx(), this.getTy(), this.getTz());
                return;
            }
            case 2: {
                affine.appendScale(this.getMxx(), this.getMyy(), this.getMzz());
                return;
            }
            case 3: {
                affine.appendTranslation(this.getTx(), this.getTy(), this.getTz());
                affine.appendScale(this.getMxx(), this.getMyy(), this.getMzz());
                return;
            }
            case 4: 
        }
        affine.append(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
    }

    public void prepend(Transform transform) {
        transform.prependTo(this);
    }

    public void prepend(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (this.state3d == 0) {
            this.atomicChange.start();
            double d8 = this.getMxx();
            double d9 = this.getMxy();
            double d10 = this.getTx();
            double d11 = this.getMyx();
            double d12 = this.getMyy();
            double d13 = this.getTy();
            this.setMxx(d2 * d8 + d3 * d11);
            this.setMxy(d2 * d9 + d3 * d12);
            this.setTx(d2 * d10 + d3 * d13 + d4);
            this.setMyx(d5 * d8 + d6 * d11);
            this.setMyy(d5 * d9 + d6 * d12);
            this.setTy(d5 * d10 + d6 * d13 + d7);
            this.updateState2D();
            this.atomicChange.end();
        } else {
            this.prepend(d2, d3, 0.0, d4, d5, d6, 0.0, d7, 0.0, 0.0, 1.0, 0.0);
        }
    }

    public void prepend(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        this.atomicChange.start();
        double d14 = this.getMxx();
        double d15 = this.getMxy();
        double d16 = this.getMxz();
        double d17 = this.getTx();
        double d18 = this.getMyx();
        double d19 = this.getMyy();
        double d20 = this.getMyz();
        double d21 = this.getTy();
        double d22 = this.getMzx();
        double d23 = this.getMzy();
        double d24 = this.getMzz();
        double d25 = this.getTz();
        this.setMxx(d2 * d14 + d3 * d18 + d4 * d22);
        this.setMxy(d2 * d15 + d3 * d19 + d4 * d23);
        this.setMxz(d2 * d16 + d3 * d20 + d4 * d24);
        this.setTx(d2 * d17 + d3 * d21 + d4 * d25 + d5);
        this.setMyx(d6 * d14 + d7 * d18 + d8 * d22);
        this.setMyy(d6 * d15 + d7 * d19 + d8 * d23);
        this.setMyz(d6 * d16 + d7 * d20 + d8 * d24);
        this.setTy(d6 * d17 + d7 * d21 + d8 * d25 + d9);
        this.setMzx(d10 * d14 + d11 * d18 + d12 * d22);
        this.setMzy(d10 * d15 + d11 * d19 + d12 * d23);
        this.setMzz(d10 * d16 + d11 * d20 + d12 * d24);
        this.setTz(d10 * d17 + d11 * d21 + d12 * d25 + d13);
        this.updateState();
        this.atomicChange.end();
    }

    public void prepend(double[] arrd, MatrixType matrixType, int n2) {
        if (arrd.length < n2 + matrixType.elements()) {
            throw new IndexOutOfBoundsException("The array is too short.");
        }
        switch (matrixType) {
            default: {
                Affine.stateError();
            }
            case MT_2D_3x3: {
                if (arrd[n2 + 6] != 0.0 || arrd[n2 + 7] != 0.0 || arrd[n2 + 8] != 1.0) {
                    throw new IllegalArgumentException("The matrix is not affine");
                }
            }
            case MT_2D_2x3: {
                this.prepend(arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++]);
                return;
            }
            case MT_3D_4x4: {
                if (arrd[n2 + 12] == 0.0 && arrd[n2 + 13] == 0.0 && arrd[n2 + 14] == 0.0 && arrd[n2 + 15] == 1.0) break;
                throw new IllegalArgumentException("The matrix is not affine");
            }
            case MT_3D_3x4: 
        }
        this.prepend(arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++], arrd[n2++]);
    }

    @Override
    void prependTo(Affine affine) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    case 0: {
                        return;
                    }
                    case 1: {
                        affine.prependTranslation(this.getTx(), this.getTy());
                        return;
                    }
                    case 2: {
                        affine.prependScale(this.getMxx(), this.getMyy());
                        return;
                    }
                    case 3: {
                        affine.prependScale(this.getMxx(), this.getMyy());
                        affine.prependTranslation(this.getTx(), this.getTy());
                        return;
                    }
                }
                affine.prepend(this.getMxx(), this.getMxy(), this.getTx(), this.getMyx(), this.getMyy(), this.getTy());
                return;
            }
            case 1: {
                affine.prependTranslation(this.getTx(), this.getTy(), this.getTz());
                return;
            }
            case 2: {
                affine.prependScale(this.getMxx(), this.getMyy(), this.getMzz());
                return;
            }
            case 3: {
                affine.prependScale(this.getMxx(), this.getMyy(), this.getMzz());
                affine.prependTranslation(this.getTx(), this.getTy(), this.getTz());
                return;
            }
            case 4: 
        }
        affine.prepend(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
    }

    public void appendTranslation(double d2, double d3) {
        this.atomicChange.start();
        this.translate2D(d2, d3);
        this.atomicChange.end();
    }

    public void appendTranslation(double d2, double d3, double d4) {
        this.atomicChange.start();
        this.translate3D(d2, d3, d4);
        this.atomicChange.end();
    }

    private void translate2D(double d2, double d3) {
        if (this.state3d != 0) {
            this.translate3D(d2, d3, 0.0);
            return;
        }
        switch (this.state2d) {
            default: {
                Affine.stateError();
            }
            case 7: {
                this.setTx(d2 * this.getMxx() + d3 * this.getMxy() + this.getTx());
                this.setTy(d2 * this.getMyx() + d3 * this.getMyy() + this.getTy());
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = 6;
                }
                return;
            }
            case 6: {
                this.setTx(d2 * this.getMxx() + d3 * this.getMxy());
                this.setTy(d2 * this.getMyx() + d3 * this.getMyy());
                if (this.getTx() != 0.0 || this.getTy() != 0.0) {
                    this.state2d = 7;
                }
                return;
            }
            case 5: {
                this.setTx(d3 * this.getMxy() + this.getTx());
                this.setTy(d2 * this.getMyx() + this.getTy());
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = 4;
                }
                return;
            }
            case 4: {
                this.setTx(d3 * this.getMxy());
                this.setTy(d2 * this.getMyx());
                if (this.getTx() != 0.0 || this.getTy() != 0.0) {
                    this.state2d = 5;
                }
                return;
            }
            case 3: {
                this.setTx(d2 * this.getMxx() + this.getTx());
                this.setTy(d3 * this.getMyy() + this.getTy());
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = 2;
                }
                return;
            }
            case 2: {
                this.setTx(d2 * this.getMxx());
                this.setTy(d3 * this.getMyy());
                if (this.getTx() != 0.0 || this.getTy() != 0.0) {
                    this.state2d = 3;
                }
                return;
            }
            case 1: {
                this.setTx(d2 + this.getTx());
                this.setTy(d3 + this.getTy());
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = 0;
                }
                return;
            }
            case 0: 
        }
        this.setTx(d2);
        this.setTy(d3);
        if (d2 != 0.0 || d3 != 0.0) {
            this.state2d = 1;
        }
    }

    private void translate3D(double d2, double d3, double d4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                this.translate2D(d2, d3);
                if (d4 != 0.0) {
                    this.setTz(d4);
                    this.state3d = (this.state2d & 4) == 0 ? this.state2d & 2 | 1 : 4;
                }
                return;
            }
            case 1: {
                this.setTx(d2 + this.getTx());
                this.setTy(d3 + this.getTy());
                this.setTz(d4 + this.getTz());
                if (this.getTz() == 0.0) {
                    this.state3d = 0;
                    this.state2d = this.getTx() == 0.0 && this.getTy() == 0.0 ? 0 : 1;
                }
                return;
            }
            case 2: {
                this.setTx(d2 * this.getMxx());
                this.setTy(d3 * this.getMyy());
                this.setTz(d4 * this.getMzz());
                if (this.getTx() != 0.0 || this.getTy() != 0.0 || this.getTz() != 0.0) {
                    this.state3d |= 1;
                }
                return;
            }
            case 3: {
                this.setTx(d2 * this.getMxx() + this.getTx());
                this.setTy(d3 * this.getMyy() + this.getTy());
                this.setTz(d4 * this.getMzz() + this.getTz());
                if (this.getTz() == 0.0) {
                    if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                        this.state3d = 2;
                    }
                    if (this.getMzz() == 1.0) {
                        this.state2d = this.state3d;
                        this.state3d = 0;
                    }
                }
                return;
            }
            case 4: 
        }
        this.setTx(d2 * this.getMxx() + d3 * this.getMxy() + d4 * this.getMxz() + this.getTx());
        this.setTy(d2 * this.getMyx() + d3 * this.getMyy() + d4 * this.getMyz() + this.getTy());
        this.setTz(d2 * this.getMzx() + d3 * this.getMzy() + d4 * this.getMzz() + this.getTz());
        this.updateState();
    }

    public void prependTranslation(double d2, double d3, double d4) {
        this.atomicChange.start();
        this.preTranslate3D(d2, d3, d4);
        this.atomicChange.end();
    }

    public void prependTranslation(double d2, double d3) {
        this.atomicChange.start();
        this.preTranslate2D(d2, d3);
        this.atomicChange.end();
    }

    private void preTranslate2D(double d2, double d3) {
        if (this.state3d != 0) {
            this.preTranslate3D(d2, d3, 0.0);
            return;
        }
        this.setTx(this.getTx() + d2);
        this.setTy(this.getTy() + d3);
        this.state2d = this.getTx() == 0.0 && this.getTy() == 0.0 ? (this.state2d &= 0xFFFFFFFE) : (this.state2d |= 1);
    }

    private void preTranslate3D(double d2, double d3, double d4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                this.preTranslate2D(d2, d3);
                if (d4 != 0.0) {
                    this.setTz(d4);
                    this.state3d = (this.state2d & 4) == 0 ? this.state2d & 2 | 1 : 4;
                }
                return;
            }
            case 1: {
                this.setTx(this.getTx() + d2);
                this.setTy(this.getTy() + d3);
                this.setTz(this.getTz() + d4);
                if (this.getTz() == 0.0) {
                    this.state3d = 0;
                    this.state2d = this.getTx() == 0.0 && this.getTy() == 0.0 ? 0 : 1;
                }
                return;
            }
            case 2: {
                this.setTx(d2);
                this.setTy(d3);
                this.setTz(d4);
                if (d2 != 0.0 || d3 != 0.0 || d4 != 0.0) {
                    this.state3d |= 1;
                }
                return;
            }
            case 3: {
                this.setTx(this.getTx() + d2);
                this.setTy(this.getTy() + d3);
                this.setTz(this.getTz() + d4);
                if (this.getTz() == 0.0) {
                    if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                        this.state3d = 2;
                    }
                    if (this.getMzz() == 1.0) {
                        this.state2d = this.state3d;
                        this.state3d = 0;
                    }
                }
                return;
            }
            case 4: 
        }
        this.setTx(this.getTx() + d2);
        this.setTy(this.getTy() + d3);
        this.setTz(this.getTz() + d4);
        if (this.getTz() == 0.0 && this.getMxz() == 0.0 && this.getMyz() == 0.0 && this.getMzx() == 0.0 && this.getMzy() == 0.0 && this.getMzz() == 1.0) {
            this.state3d = 0;
            this.updateState2D();
        }
    }

    public void appendScale(double d2, double d3) {
        this.atomicChange.start();
        this.scale2D(d2, d3);
        this.atomicChange.end();
    }

    public void appendScale(double d2, double d3, double d4, double d5) {
        this.atomicChange.start();
        if (d4 != 0.0 || d5 != 0.0) {
            this.translate2D(d4, d5);
            this.scale2D(d2, d3);
            this.translate2D(-d4, -d5);
        } else {
            this.scale2D(d2, d3);
        }
        this.atomicChange.end();
    }

    public void appendScale(double d2, double d3, Point2D point2D) {
        this.appendScale(d2, d3, point2D.getX(), point2D.getY());
    }

    public void appendScale(double d2, double d3, double d4) {
        this.atomicChange.start();
        this.scale3D(d2, d3, d4);
        this.atomicChange.end();
    }

    public void appendScale(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.atomicChange.start();
        if (d5 != 0.0 || d6 != 0.0 || d7 != 0.0) {
            this.translate3D(d5, d6, d7);
            this.scale3D(d2, d3, d4);
            this.translate3D(-d5, -d6, -d7);
        } else {
            this.scale3D(d2, d3, d4);
        }
        this.atomicChange.end();
    }

    public void appendScale(double d2, double d3, double d4, Point3D point3D) {
        this.appendScale(d2, d3, d4, point3D.getX(), point3D.getY(), point3D.getZ());
    }

    private void scale2D(double d2, double d3) {
        if (this.state3d != 0) {
            this.scale3D(d2, d3, 1.0);
            return;
        }
        int n2 = this.state2d;
        switch (n2) {
            default: {
                Affine.stateError();
            }
            case 6: 
            case 7: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
            }
            case 4: 
            case 5: {
                this.setMxy(this.getMxy() * d3);
                this.setMyx(this.getMyx() * d2);
                if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
                    n2 &= 1;
                    if (this.getMxx() != 1.0 || this.getMyy() != 1.0) {
                        n2 |= 2;
                    }
                    this.state2d = n2;
                } else if (this.getMxx() == 0.0 && this.getMyy() == 0.0) {
                    this.state2d &= 0xFFFFFFFD;
                }
                return;
            }
            case 2: 
            case 3: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
                if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
                    this.state2d = n2 &= 1;
                }
                return;
            }
            case 0: 
            case 1: 
        }
        this.setMxx(d2);
        this.setMyy(d3);
        if (d2 != 1.0 || d3 != 1.0) {
            this.state2d = n2 | 2;
        }
    }

    private void scale3D(double d2, double d3, double d4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                this.scale2D(d2, d3);
                if (d4 != 1.0) {
                    this.setMzz(d4);
                    this.state3d = (this.state2d & 4) == 0 ? this.state2d & 1 | 2 : 4;
                }
                return;
            }
            case 1: {
                this.setMxx(d2);
                this.setMyy(d3);
                this.setMzz(d4);
                if (d2 != 1.0 || d3 != 1.0 || d4 != 1.0) {
                    this.state3d |= 2;
                }
                return;
            }
            case 2: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
                this.setMzz(this.getMzz() * d4);
                if (this.getMzz() == 1.0) {
                    this.state3d = 0;
                    this.state2d = this.getMxx() == 1.0 && this.getMyy() == 1.0 ? 0 : 2;
                }
                return;
            }
            case 3: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
                this.setMzz(this.getMzz() * d4);
                if (this.getMxx() == 1.0 && this.getMyy() == 1.0 && this.getMzz() == 1.0) {
                    this.state3d &= 0xFFFFFFFD;
                }
                if (this.getTz() == 0.0 && this.getMzz() == 1.0) {
                    this.state2d = this.state3d;
                    this.state3d = 0;
                }
                return;
            }
            case 4: 
        }
        this.setMxx(this.getMxx() * d2);
        this.setMxy(this.getMxy() * d3);
        this.setMxz(this.getMxz() * d4);
        this.setMyx(this.getMyx() * d2);
        this.setMyy(this.getMyy() * d3);
        this.setMyz(this.getMyz() * d4);
        this.setMzx(this.getMzx() * d2);
        this.setMzy(this.getMzy() * d3);
        this.setMzz(this.getMzz() * d4);
        if (d2 == 0.0 || d3 == 0.0 || d4 == 0.0) {
            this.updateState();
        }
    }

    public void prependScale(double d2, double d3) {
        this.atomicChange.start();
        this.preScale2D(d2, d3);
        this.atomicChange.end();
    }

    public void prependScale(double d2, double d3, double d4, double d5) {
        this.atomicChange.start();
        if (d4 != 0.0 || d5 != 0.0) {
            this.preTranslate2D(-d4, -d5);
            this.preScale2D(d2, d3);
            this.preTranslate2D(d4, d5);
        } else {
            this.preScale2D(d2, d3);
        }
        this.atomicChange.end();
    }

    public void prependScale(double d2, double d3, Point2D point2D) {
        this.prependScale(d2, d3, point2D.getX(), point2D.getY());
    }

    public void prependScale(double d2, double d3, double d4) {
        this.atomicChange.start();
        this.preScale3D(d2, d3, d4);
        this.atomicChange.end();
    }

    public void prependScale(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.atomicChange.start();
        if (d5 != 0.0 || d6 != 0.0 || d7 != 0.0) {
            this.preTranslate3D(-d5, -d6, -d7);
            this.preScale3D(d2, d3, d4);
            this.preTranslate3D(d5, d6, d7);
        } else {
            this.preScale3D(d2, d3, d4);
        }
        this.atomicChange.end();
    }

    public void prependScale(double d2, double d3, double d4, Point3D point3D) {
        this.prependScale(d2, d3, d4, point3D.getX(), point3D.getY(), point3D.getZ());
    }

    private void preScale2D(double d2, double d3) {
        if (this.state3d != 0) {
            this.preScale3D(d2, d3, 1.0);
            return;
        }
        int n2 = this.state2d;
        switch (n2) {
            default: {
                Affine.stateError();
            }
            case 7: {
                this.setTx(this.getTx() * d2);
                this.setTy(this.getTy() * d3);
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = n2 &= 0xFFFFFFFE;
                }
            }
            case 6: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
            }
            case 4: {
                this.setMxy(this.getMxy() * d2);
                this.setMyx(this.getMyx() * d3);
                if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
                    n2 &= 1;
                    if (this.getMxx() != 1.0 || this.getMyy() != 1.0) {
                        n2 |= 2;
                    }
                    this.state2d = n2;
                }
                return;
            }
            case 5: {
                this.setTx(this.getTx() * d2);
                this.setTy(this.getTy() * d3);
                this.setMxy(this.getMxy() * d2);
                this.setMyx(this.getMyx() * d3);
                if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
                    this.state2d = this.getTx() == 0.0 && this.getTy() == 0.0 ? 2 : 3;
                } else if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = 4;
                }
                return;
            }
            case 3: {
                this.setTx(this.getTx() * d2);
                this.setTy(this.getTy() * d3);
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = n2 &= 0xFFFFFFFE;
                }
            }
            case 2: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
                if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
                    this.state2d = n2 &= 1;
                }
                return;
            }
            case 1: {
                this.setTx(this.getTx() * d2);
                this.setTy(this.getTy() * d3);
                if (this.getTx() != 0.0 || this.getTy() != 0.0) break;
                this.state2d = n2 &= 0xFFFFFFFE;
            }
            case 0: 
        }
        this.setMxx(d2);
        this.setMyy(d3);
        if (d2 != 1.0 || d3 != 1.0) {
            this.state2d = n2 | 2;
        }
    }

    private void preScale3D(double d2, double d3, double d4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                this.preScale2D(d2, d3);
                if (d4 != 1.0) {
                    this.setMzz(d4);
                    this.state3d = (this.state2d & 4) == 0 ? this.state2d & 1 | 2 : 4;
                }
                return;
            }
            case 3: {
                this.setTx(this.getTx() * d2);
                this.setTy(this.getTy() * d3);
                this.setTz(this.getTz() * d4);
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
                this.setMzz(this.getMzz() * d4);
                if (this.getTx() == 0.0 && this.getTy() == 0.0 && this.getTz() == 0.0) {
                    this.state3d &= 0xFFFFFFFE;
                }
                if (this.getMxx() == 1.0 && this.getMyy() == 1.0 && this.getMzz() == 1.0) {
                    this.state3d &= 0xFFFFFFFD;
                }
                if (this.getTz() == 0.0 && this.getMzz() == 1.0) {
                    this.state2d = this.state3d;
                    this.state3d = 0;
                }
                return;
            }
            case 2: {
                this.setMxx(this.getMxx() * d2);
                this.setMyy(this.getMyy() * d3);
                this.setMzz(this.getMzz() * d4);
                if (this.getMzz() == 1.0) {
                    this.state3d = 0;
                    this.state2d = this.getMxx() == 1.0 && this.getMyy() == 1.0 ? 0 : 2;
                }
                return;
            }
            case 1: {
                this.setTx(this.getTx() * d2);
                this.setTy(this.getTy() * d3);
                this.setTz(this.getTz() * d4);
                this.setMxx(d2);
                this.setMyy(d3);
                this.setMzz(d4);
                if (this.getTx() == 0.0 && this.getTy() == 0.0 && this.getTz() == 0.0) {
                    this.state3d &= 0xFFFFFFFE;
                }
                if (d2 != 1.0 || d3 != 1.0 || d4 != 1.0) {
                    this.state3d |= 2;
                }
                return;
            }
            case 4: 
        }
        this.setMxx(this.getMxx() * d2);
        this.setMxy(this.getMxy() * d2);
        this.setMxz(this.getMxz() * d2);
        this.setTx(this.getTx() * d2);
        this.setMyx(this.getMyx() * d3);
        this.setMyy(this.getMyy() * d3);
        this.setMyz(this.getMyz() * d3);
        this.setTy(this.getTy() * d3);
        this.setMzx(this.getMzx() * d4);
        this.setMzy(this.getMzy() * d4);
        this.setMzz(this.getMzz() * d4);
        this.setTz(this.getTz() * d4);
        if (d2 == 0.0 || d3 == 0.0 || d4 == 0.0) {
            this.updateState();
        }
    }

    public void appendShear(double d2, double d3) {
        this.atomicChange.start();
        this.shear2D(d2, d3);
        this.atomicChange.end();
    }

    public void appendShear(double d2, double d3, double d4, double d5) {
        this.atomicChange.start();
        if (d4 != 0.0 || d5 != 0.0) {
            this.translate2D(d4, d5);
            this.shear2D(d2, d3);
            this.translate2D(-d4, -d5);
        } else {
            this.shear2D(d2, d3);
        }
        this.atomicChange.end();
    }

    public void appendShear(double d2, double d3, Point2D point2D) {
        this.appendShear(d2, d3, point2D.getX(), point2D.getY());
    }

    private void shear2D(double d2, double d3) {
        if (this.state3d != 0) {
            this.shear3D(d2, d3);
            return;
        }
        int n2 = this.state2d;
        switch (n2) {
            default: {
                Affine.stateError();
            }
            case 6: 
            case 7: {
                double d4 = this.getMxx();
                double d5 = this.getMxy();
                this.setMxx(d4 + d5 * d3);
                this.setMxy(d4 * d2 + d5);
                d4 = this.getMyx();
                d5 = this.getMyy();
                this.setMyx(d4 + d5 * d3);
                this.setMyy(d4 * d2 + d5);
                this.updateState2D();
                return;
            }
            case 4: 
            case 5: {
                this.setMxx(this.getMxy() * d3);
                this.setMyy(this.getMyx() * d2);
                if (this.getMxx() != 0.0 || this.getMyy() != 0.0) {
                    this.state2d = n2 | 2;
                }
                return;
            }
            case 2: 
            case 3: {
                this.setMxy(this.getMxx() * d2);
                this.setMyx(this.getMyy() * d3);
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state2d = n2 | 4;
                }
                return;
            }
            case 0: 
            case 1: 
        }
        this.setMxy(d2);
        this.setMyx(d3);
        if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
            this.state2d = n2 | 2 | 4;
        }
    }

    private void shear3D(double d2, double d3) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                this.shear2D(d2, d3);
                return;
            }
            case 1: {
                this.setMxy(d2);
                this.setMyx(d3);
                if (d2 != 0.0 || d3 != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 2: 
            case 3: {
                this.setMxy(this.getMxx() * d2);
                this.setMyx(this.getMyy() * d3);
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 4: 
        }
        double d4 = this.getMxx();
        double d5 = this.getMxy();
        double d6 = this.getMyx();
        double d7 = this.getMyy();
        double d8 = this.getMzx();
        double d9 = this.getMzy();
        this.setMxx(d4 + d5 * d3);
        this.setMxy(d5 + d4 * d2);
        this.setMyx(d6 + d7 * d3);
        this.setMyy(d7 + d6 * d2);
        this.setMzx(d8 + d9 * d3);
        this.setMzy(d9 + d8 * d2);
        this.updateState();
    }

    public void prependShear(double d2, double d3) {
        this.atomicChange.start();
        this.preShear2D(d2, d3);
        this.atomicChange.end();
    }

    public void prependShear(double d2, double d3, double d4, double d5) {
        this.atomicChange.start();
        if (d4 != 0.0 || d5 != 0.0) {
            this.preTranslate2D(-d4, -d5);
            this.preShear2D(d2, d3);
            this.preTranslate2D(d4, d5);
        } else {
            this.preShear2D(d2, d3);
        }
        this.atomicChange.end();
    }

    public void prependShear(double d2, double d3, Point2D point2D) {
        this.prependShear(d2, d3, point2D.getX(), point2D.getY());
    }

    private void preShear2D(double d2, double d3) {
        if (this.state3d != 0) {
            this.preShear3D(d2, d3);
            return;
        }
        int n2 = this.state2d;
        switch (n2) {
            default: {
                Affine.stateError();
            }
            case 5: 
            case 7: {
                double d4 = this.getTx();
                double d5 = this.getTy();
                this.setTx(d4 + d2 * d5);
                this.setTy(d5 + d3 * d4);
            }
            case 4: 
            case 6: {
                double d6 = this.getMxx();
                double d7 = this.getMxy();
                double d8 = this.getMyx();
                double d9 = this.getMyy();
                this.setMxx(d6 + d2 * d8);
                this.setMxy(d7 + d2 * d9);
                this.setMyx(d3 * d6 + d8);
                this.setMyy(d3 * d7 + d9);
                this.updateState2D();
                return;
            }
            case 3: {
                double d10 = this.getTx();
                double d11 = this.getTy();
                this.setTx(d10 + d2 * d11);
                this.setTy(d11 + d3 * d10);
                if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                    this.state2d = n2 &= 0xFFFFFFFE;
                }
            }
            case 2: {
                this.setMxy(d2 * this.getMyy());
                this.setMyx(d3 * this.getMxx());
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state2d = n2 | 4;
                }
                return;
            }
            case 1: {
                double d12 = this.getTx();
                double d13 = this.getTy();
                this.setTx(d12 + d2 * d13);
                this.setTy(d13 + d3 * d12);
                if (this.getTx() != 0.0 || this.getTy() != 0.0) break;
                this.state2d = n2 &= 0xFFFFFFFE;
            }
            case 0: 
        }
        this.setMxy(d2);
        this.setMyx(d3);
        if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
            this.state2d = n2 | 2 | 4;
        }
    }

    private void preShear3D(double d2, double d3) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                this.preShear2D(d2, d3);
                return;
            }
            case 1: {
                double d4 = this.getTx();
                this.setMxy(d2);
                this.setTx(d4 + this.getTy() * d2);
                this.setMyx(d3);
                this.setTy(d4 * d3 + this.getTy());
                if (d2 != 0.0 || d3 != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 2: {
                this.setMxy(this.getMyy() * d2);
                this.setMyx(this.getMxx() * d3);
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 3: {
                double d5 = this.getTx();
                this.setMxy(this.getMyy() * d2);
                this.setTx(d5 + this.getTy() * d2);
                this.setMyx(this.getMxx() * d3);
                this.setTy(d5 * d3 + this.getTy());
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 4: 
        }
        double d6 = this.getMxx();
        double d7 = this.getMxy();
        double d8 = this.getMyx();
        double d9 = this.getTx();
        double d10 = this.getMyy();
        double d11 = this.getMxz();
        double d12 = this.getMyz();
        double d13 = this.getTy();
        this.setMxx(d6 + d8 * d2);
        this.setMxy(d7 + d10 * d2);
        this.setMxz(d11 + d12 * d2);
        this.setTx(d9 + d13 * d2);
        this.setMyx(d6 * d3 + d8);
        this.setMyy(d7 * d3 + d10);
        this.setMyz(d11 * d3 + d12);
        this.setTy(d9 * d3 + d13);
        this.updateState();
    }

    public void appendRotation(double d2) {
        this.atomicChange.start();
        this.rotate2D(d2);
        this.atomicChange.end();
    }

    public void appendRotation(double d2, double d3, double d4) {
        this.atomicChange.start();
        if (d3 != 0.0 || d4 != 0.0) {
            this.translate2D(d3, d4);
            this.rotate2D(d2);
            this.translate2D(-d3, -d4);
        } else {
            this.rotate2D(d2);
        }
        this.atomicChange.end();
    }

    public void appendRotation(double d2, Point2D point2D) {
        this.appendRotation(d2, point2D.getX(), point2D.getY());
    }

    public void appendRotation(double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        this.atomicChange.start();
        if (d3 != 0.0 || d4 != 0.0 || d5 != 0.0) {
            this.translate3D(d3, d4, d5);
            this.rotate3D(d2, d6, d7, d8);
            this.translate3D(-d3, -d4, -d5);
        } else {
            this.rotate3D(d2, d6, d7, d8);
        }
        this.atomicChange.end();
    }

    public void appendRotation(double d2, double d3, double d4, double d5, Point3D point3D) {
        this.appendRotation(d2, d3, d4, d5, point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public void appendRotation(double d2, Point3D point3D, Point3D point3D2) {
        this.appendRotation(d2, point3D.getX(), point3D.getY(), point3D.getZ(), point3D2.getX(), point3D2.getY(), point3D2.getZ());
    }

    private void rotate3D(double d2, double d3, double d4, double d5) {
        if (d3 == 0.0 && d4 == 0.0) {
            if (d5 > 0.0) {
                this.rotate3D(d2);
            } else if (d5 < 0.0) {
                this.rotate3D(-d2);
            }
            return;
        }
        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
        if (d6 == 0.0) {
            return;
        }
        d6 = 1.0 / d6;
        double d7 = d3 * d6;
        double d8 = d4 * d6;
        double d9 = d5 * d6;
        double d10 = Math.sin(Math.toRadians(d2));
        double d11 = Math.cos(Math.toRadians(d2));
        double d12 = 1.0 - d11;
        double d13 = d7 * d9;
        double d14 = d7 * d8;
        double d15 = d8 * d9;
        double d16 = d12 * d7 * d7 + d11;
        double d17 = d12 * d14 - d10 * d9;
        double d18 = d12 * d13 + d10 * d8;
        double d19 = d12 * d14 + d10 * d9;
        double d20 = d12 * d8 * d8 + d11;
        double d21 = d12 * d15 - d10 * d7;
        double d22 = d12 * d13 - d10 * d8;
        double d23 = d12 * d15 + d10 * d7;
        double d24 = d12 * d9 * d9 + d11;
        block0 : switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        Affine.stateError();
                    }
                    case 6: 
                    case 7: {
                        double d25 = this.getMxx();
                        double d26 = this.getMxy();
                        double d27 = this.getMyx();
                        double d28 = this.getMyy();
                        this.setMxx(d25 * d16 + d26 * d19);
                        this.setMxy(d25 * d17 + d26 * d20);
                        this.setMxz(d25 * d18 + d26 * d21);
                        this.setMyx(d27 * d16 + d28 * d19);
                        this.setMyy(d27 * d17 + d28 * d20);
                        this.setMyz(d27 * d18 + d28 * d21);
                        this.setMzx(d22);
                        this.setMzy(d23);
                        this.setMzz(d24);
                        break block0;
                    }
                    case 4: 
                    case 5: {
                        double d29 = this.getMxy();
                        double d30 = this.getMyx();
                        this.setMxx(d29 * d19);
                        this.setMxy(d29 * d20);
                        this.setMxz(d29 * d21);
                        this.setMyx(d30 * d16);
                        this.setMyy(d30 * d17);
                        this.setMyz(d30 * d18);
                        this.setMzx(d22);
                        this.setMzy(d23);
                        this.setMzz(d24);
                        break block0;
                    }
                    case 2: 
                    case 3: {
                        double d31 = this.getMxx();
                        double d32 = this.getMyy();
                        this.setMxx(d31 * d16);
                        this.setMxy(d31 * d17);
                        this.setMxz(d31 * d18);
                        this.setMyx(d32 * d19);
                        this.setMyy(d32 * d20);
                        this.setMyz(d32 * d21);
                        this.setMzx(d22);
                        this.setMzy(d23);
                        this.setMzz(d24);
                        break block0;
                    }
                    case 0: 
                    case 1: 
                }
                this.setMxx(d16);
                this.setMxy(d17);
                this.setMxz(d18);
                this.setMyx(d19);
                this.setMyy(d20);
                this.setMyz(d21);
                this.setMzx(d22);
                this.setMzy(d23);
                this.setMzz(d24);
                break;
            }
            case 1: {
                this.setMxx(d16);
                this.setMxy(d17);
                this.setMxz(d18);
                this.setMyx(d19);
                this.setMyy(d20);
                this.setMyz(d21);
                this.setMzx(d22);
                this.setMzy(d23);
                this.setMzz(d24);
                break;
            }
            case 2: 
            case 3: {
                double d33 = this.getMxx();
                double d34 = this.getMyy();
                double d35 = this.getMzz();
                this.setMxx(d33 * d16);
                this.setMxy(d33 * d17);
                this.setMxz(d33 * d18);
                this.setMyx(d34 * d19);
                this.setMyy(d34 * d20);
                this.setMyz(d34 * d21);
                this.setMzx(d35 * d22);
                this.setMzy(d35 * d23);
                this.setMzz(d35 * d24);
                break;
            }
            case 4: {
                double d36 = this.getMxx();
                double d37 = this.getMxy();
                double d38 = this.getMxz();
                double d39 = this.getMyx();
                double d40 = this.getMyy();
                double d41 = this.getMyz();
                double d42 = this.getMzx();
                double d43 = this.getMzy();
                double d44 = this.getMzz();
                this.setMxx(d36 * d16 + d37 * d19 + d38 * d22);
                this.setMxy(d36 * d17 + d37 * d20 + d38 * d23);
                this.setMxz(d36 * d18 + d37 * d21 + d38 * d24);
                this.setMyx(d39 * d16 + d40 * d19 + d41 * d22);
                this.setMyy(d39 * d17 + d40 * d20 + d41 * d23);
                this.setMyz(d39 * d18 + d40 * d21 + d41 * d24);
                this.setMzx(d42 * d16 + d43 * d19 + d44 * d22);
                this.setMzy(d42 * d17 + d43 * d20 + d44 * d23);
                this.setMzz(d42 * d18 + d43 * d21 + d44 * d24);
            }
        }
        this.updateState();
    }

    private void rotate2D(double d2) {
        if (this.state3d != 0) {
            this.rotate3D(d2);
            return;
        }
        double d3 = Math.sin(Math.toRadians(d2));
        if (d3 == 1.0) {
            this.rotate2D_90();
        } else if (d3 == -1.0) {
            this.rotate2D_270();
        } else {
            double d4 = Math.cos(Math.toRadians(d2));
            if (d4 == -1.0) {
                this.rotate2D_180();
            } else if (d4 != 1.0) {
                double d5 = this.getMxx();
                double d6 = this.getMxy();
                this.setMxx(d4 * d5 + d3 * d6);
                this.setMxy(-d3 * d5 + d4 * d6);
                d5 = this.getMyx();
                d6 = this.getMyy();
                this.setMyx(d4 * d5 + d3 * d6);
                this.setMyy(-d3 * d5 + d4 * d6);
                this.updateState2D();
            }
        }
    }

    private void rotate2D_90() {
        double d2 = this.getMxx();
        this.setMxx(this.getMxy());
        this.setMxy(-d2);
        d2 = this.getMyx();
        this.setMyx(this.getMyy());
        this.setMyy(-d2);
        int n2 = rot90conversion[this.state2d];
        if ((n2 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
            n2 -= 2;
        } else if ((n2 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
            n2 = n2 & 0xFFFFFFFB | 2;
        }
        this.state2d = n2;
    }

    private void rotate2D_180() {
        this.setMxx(-this.getMxx());
        this.setMyy(-this.getMyy());
        int n2 = this.state2d;
        if ((n2 & 4) != 0) {
            this.setMxy(-this.getMxy());
            this.setMyx(-this.getMyx());
        } else {
            this.state2d = this.getMxx() == 1.0 && this.getMyy() == 1.0 ? n2 & 0xFFFFFFFD : n2 | 2;
        }
    }

    private void rotate2D_270() {
        double d2 = this.getMxx();
        this.setMxx(-this.getMxy());
        this.setMxy(d2);
        d2 = this.getMyx();
        this.setMyx(-this.getMyy());
        this.setMyy(d2);
        int n2 = rot90conversion[this.state2d];
        if ((n2 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
            n2 -= 2;
        } else if ((n2 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
            n2 = n2 & 0xFFFFFFFB | 2;
        }
        this.state2d = n2;
    }

    private void rotate3D(double d2) {
        if (this.state3d == 0) {
            this.rotate2D(d2);
            return;
        }
        double d3 = Math.sin(Math.toRadians(d2));
        if (d3 == 1.0) {
            this.rotate3D_90();
        } else if (d3 == -1.0) {
            this.rotate3D_270();
        } else {
            double d4 = Math.cos(Math.toRadians(d2));
            if (d4 == -1.0) {
                this.rotate3D_180();
            } else if (d4 != 1.0) {
                double d5 = this.getMxx();
                double d6 = this.getMxy();
                this.setMxx(d4 * d5 + d3 * d6);
                this.setMxy(-d3 * d5 + d4 * d6);
                d5 = this.getMyx();
                d6 = this.getMyy();
                this.setMyx(d4 * d5 + d3 * d6);
                this.setMyy(-d3 * d5 + d4 * d6);
                d5 = this.getMzx();
                d6 = this.getMzy();
                this.setMzx(d4 * d5 + d3 * d6);
                this.setMzy(-d3 * d5 + d4 * d6);
                this.updateState();
            }
        }
    }

    private void rotate3D_90() {
        double d2 = this.getMxx();
        this.setMxx(this.getMxy());
        this.setMxy(-d2);
        d2 = this.getMyx();
        this.setMyx(this.getMyy());
        this.setMyy(-d2);
        d2 = this.getMzx();
        this.setMzx(this.getMzy());
        this.setMzy(-d2);
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 1: {
                this.state3d = 4;
                return;
            }
            case 2: 
            case 3: {
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 4: 
        }
        this.updateState();
    }

    private void rotate3D_180() {
        double d2 = this.getMxx();
        double d3 = this.getMyy();
        this.setMxx(-d2);
        this.setMyy(-d3);
        if (this.state3d == 4) {
            this.setMxy(-this.getMxy());
            this.setMyx(-this.getMyx());
            this.setMzx(-this.getMzx());
            this.setMzy(-this.getMzy());
            this.updateState();
            return;
        }
        this.state3d = d2 == -1.0 && d3 == -1.0 && this.getMzz() == 1.0 ? (this.state3d &= 0xFFFFFFFD) : (this.state3d |= 2);
    }

    private void rotate3D_270() {
        double d2 = this.getMxx();
        this.setMxx(-this.getMxy());
        this.setMxy(d2);
        d2 = this.getMyx();
        this.setMyx(-this.getMyy());
        this.setMyy(d2);
        d2 = this.getMzx();
        this.setMzx(-this.getMzy());
        this.setMzy(d2);
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 1: {
                this.state3d = 4;
                return;
            }
            case 2: 
            case 3: {
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 4: 
        }
        this.updateState();
    }

    public void prependRotation(double d2) {
        this.atomicChange.start();
        this.preRotate2D(d2);
        this.atomicChange.end();
    }

    public void prependRotation(double d2, double d3, double d4) {
        this.atomicChange.start();
        if (d3 != 0.0 || d4 != 0.0) {
            this.preTranslate2D(-d3, -d4);
            this.preRotate2D(d2);
            this.preTranslate2D(d3, d4);
        } else {
            this.preRotate2D(d2);
        }
        this.atomicChange.end();
    }

    public void prependRotation(double d2, Point2D point2D) {
        this.prependRotation(d2, point2D.getX(), point2D.getY());
    }

    public void prependRotation(double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        this.atomicChange.start();
        if (d3 != 0.0 || d4 != 0.0 || d5 != 0.0) {
            this.preTranslate3D(-d3, -d4, -d5);
            this.preRotate3D(d2, d6, d7, d8);
            this.preTranslate3D(d3, d4, d5);
        } else {
            this.preRotate3D(d2, d6, d7, d8);
        }
        this.atomicChange.end();
    }

    public void prependRotation(double d2, double d3, double d4, double d5, Point3D point3D) {
        this.prependRotation(d2, d3, d4, d5, point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public void prependRotation(double d2, Point3D point3D, Point3D point3D2) {
        this.prependRotation(d2, point3D.getX(), point3D.getY(), point3D.getZ(), point3D2.getX(), point3D2.getY(), point3D2.getZ());
    }

    private void preRotate3D(double d2, double d3, double d4, double d5) {
        if (d3 == 0.0 && d4 == 0.0) {
            if (d5 > 0.0) {
                this.preRotate3D(d2);
            } else if (d5 < 0.0) {
                this.preRotate3D(-d2);
            }
            return;
        }
        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
        if (d6 == 0.0) {
            return;
        }
        d6 = 1.0 / d6;
        double d7 = d3 * d6;
        double d8 = d4 * d6;
        double d9 = d5 * d6;
        double d10 = Math.sin(Math.toRadians(d2));
        double d11 = Math.cos(Math.toRadians(d2));
        double d12 = 1.0 - d11;
        double d13 = d7 * d9;
        double d14 = d7 * d8;
        double d15 = d8 * d9;
        double d16 = d12 * d7 * d7 + d11;
        double d17 = d12 * d14 - d10 * d9;
        double d18 = d12 * d13 + d10 * d8;
        double d19 = d12 * d14 + d10 * d9;
        double d20 = d12 * d8 * d8 + d11;
        double d21 = d12 * d15 - d10 * d7;
        double d22 = d12 * d13 - d10 * d8;
        double d23 = d12 * d15 + d10 * d7;
        double d24 = d12 * d9 * d9 + d11;
        block0 : switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        Affine.stateError();
                    }
                    case 7: {
                        double d25 = this.getMxx();
                        double d26 = this.getMxy();
                        double d27 = this.getTx();
                        double d28 = this.getMyx();
                        double d29 = this.getMyy();
                        double d30 = this.getTy();
                        this.setMxx(d16 * d25 + d17 * d28);
                        this.setMxy(d16 * d26 + d17 * d29);
                        this.setMxz(d18);
                        this.setTx(d16 * d27 + d17 * d30);
                        this.setMyx(d19 * d25 + d20 * d28);
                        this.setMyy(d19 * d26 + d20 * d29);
                        this.setMyz(d21);
                        this.setTy(d19 * d27 + d20 * d30);
                        this.setMzx(d22 * d25 + d23 * d28);
                        this.setMzy(d22 * d26 + d23 * d29);
                        this.setMzz(d24);
                        this.setTz(d22 * d27 + d23 * d30);
                        break block0;
                    }
                    case 6: {
                        double d31 = this.getMxx();
                        double d32 = this.getMxy();
                        double d33 = this.getMyx();
                        double d34 = this.getMyy();
                        this.setMxx(d16 * d31 + d17 * d33);
                        this.setMxy(d16 * d32 + d17 * d34);
                        this.setMxz(d18);
                        this.setMyx(d19 * d31 + d20 * d33);
                        this.setMyy(d19 * d32 + d20 * d34);
                        this.setMyz(d21);
                        this.setMzx(d22 * d31 + d23 * d33);
                        this.setMzy(d22 * d32 + d23 * d34);
                        this.setMzz(d24);
                        break block0;
                    }
                    case 5: {
                        double d35 = this.getMxy();
                        double d36 = this.getTx();
                        double d37 = this.getMyx();
                        double d38 = this.getTy();
                        this.setMxx(d17 * d37);
                        this.setMxy(d16 * d35);
                        this.setMxz(d18);
                        this.setTx(d16 * d36 + d17 * d38);
                        this.setMyx(d20 * d37);
                        this.setMyy(d19 * d35);
                        this.setMyz(d21);
                        this.setTy(d19 * d36 + d20 * d38);
                        this.setMzx(d23 * d37);
                        this.setMzy(d22 * d35);
                        this.setMzz(d24);
                        this.setTz(d22 * d36 + d23 * d38);
                        break block0;
                    }
                    case 4: {
                        double d39 = this.getMxy();
                        double d40 = this.getMyx();
                        this.setMxx(d17 * d40);
                        this.setMxy(d16 * d39);
                        this.setMxz(d18);
                        this.setMyx(d20 * d40);
                        this.setMyy(d19 * d39);
                        this.setMyz(d21);
                        this.setMzx(d23 * d40);
                        this.setMzy(d22 * d39);
                        this.setMzz(d24);
                        break block0;
                    }
                    case 3: {
                        double d41 = this.getMxx();
                        double d42 = this.getTx();
                        double d43 = this.getMyy();
                        double d44 = this.getTy();
                        this.setMxx(d16 * d41);
                        this.setMxy(d17 * d43);
                        this.setMxz(d18);
                        this.setTx(d16 * d42 + d17 * d44);
                        this.setMyx(d19 * d41);
                        this.setMyy(d20 * d43);
                        this.setMyz(d21);
                        this.setTy(d19 * d42 + d20 * d44);
                        this.setMzx(d22 * d41);
                        this.setMzy(d23 * d43);
                        this.setMzz(d24);
                        this.setTz(d22 * d42 + d23 * d44);
                        break block0;
                    }
                    case 2: {
                        double d45 = this.getMxx();
                        double d46 = this.getMyy();
                        this.setMxx(d16 * d45);
                        this.setMxy(d17 * d46);
                        this.setMxz(d18);
                        this.setMyx(d19 * d45);
                        this.setMyy(d20 * d46);
                        this.setMyz(d21);
                        this.setMzx(d22 * d45);
                        this.setMzy(d23 * d46);
                        this.setMzz(d24);
                        break block0;
                    }
                    case 1: {
                        double d47 = this.getTx();
                        double d48 = this.getTy();
                        this.setMxx(d16);
                        this.setMxy(d17);
                        this.setMxz(d18);
                        this.setTx(d16 * d47 + d17 * d48);
                        this.setMyx(d19);
                        this.setMyy(d20);
                        this.setMyz(d21);
                        this.setTy(d19 * d47 + d20 * d48);
                        this.setMzx(d22);
                        this.setMzy(d23);
                        this.setMzz(d24);
                        this.setTz(d22 * d47 + d23 * d48);
                        break block0;
                    }
                    case 0: 
                }
                this.setMxx(d16);
                this.setMxy(d17);
                this.setMxz(d18);
                this.setMyx(d19);
                this.setMyy(d20);
                this.setMyz(d21);
                this.setMzx(d22);
                this.setMzy(d23);
                this.setMzz(d24);
                break;
            }
            case 1: {
                double d49 = this.getTx();
                double d50 = this.getTy();
                double d51 = this.getTz();
                this.setMxx(d16);
                this.setMxy(d17);
                this.setMxz(d18);
                this.setMyx(d19);
                this.setMyy(d20);
                this.setMyz(d21);
                this.setMzx(d22);
                this.setMzy(d23);
                this.setMzz(d24);
                this.setTx(d16 * d49 + d17 * d50 + d18 * d51);
                this.setTy(d19 * d49 + d20 * d50 + d21 * d51);
                this.setTz(d22 * d49 + d23 * d50 + d24 * d51);
                break;
            }
            case 2: {
                double d52 = this.getMxx();
                double d53 = this.getMyy();
                double d54 = this.getMzz();
                this.setMxx(d16 * d52);
                this.setMxy(d17 * d53);
                this.setMxz(d18 * d54);
                this.setMyx(d19 * d52);
                this.setMyy(d20 * d53);
                this.setMyz(d21 * d54);
                this.setMzx(d22 * d52);
                this.setMzy(d23 * d53);
                this.setMzz(d24 * d54);
                break;
            }
            case 3: {
                double d55 = this.getMxx();
                double d56 = this.getTx();
                double d57 = this.getMyy();
                double d58 = this.getTy();
                double d59 = this.getMzz();
                double d60 = this.getTz();
                this.setMxx(d16 * d55);
                this.setMxy(d17 * d57);
                this.setMxz(d18 * d59);
                this.setTx(d16 * d56 + d17 * d58 + d18 * d60);
                this.setMyx(d19 * d55);
                this.setMyy(d20 * d57);
                this.setMyz(d21 * d59);
                this.setTy(d19 * d56 + d20 * d58 + d21 * d60);
                this.setMzx(d22 * d55);
                this.setMzy(d23 * d57);
                this.setMzz(d24 * d59);
                this.setTz(d22 * d56 + d23 * d58 + d24 * d60);
                break;
            }
            case 4: {
                double d61 = this.getMxx();
                double d62 = this.getMxy();
                double d63 = this.getMxz();
                double d64 = this.getTx();
                double d65 = this.getMyx();
                double d66 = this.getMyy();
                double d67 = this.getMyz();
                double d68 = this.getTy();
                double d69 = this.getMzx();
                double d70 = this.getMzy();
                double d71 = this.getMzz();
                double d72 = this.getTz();
                this.setMxx(d16 * d61 + d17 * d65 + d18 * d69);
                this.setMxy(d16 * d62 + d17 * d66 + d18 * d70);
                this.setMxz(d16 * d63 + d17 * d67 + d18 * d71);
                this.setTx(d16 * d64 + d17 * d68 + d18 * d72);
                this.setMyx(d19 * d61 + d20 * d65 + d21 * d69);
                this.setMyy(d19 * d62 + d20 * d66 + d21 * d70);
                this.setMyz(d19 * d63 + d20 * d67 + d21 * d71);
                this.setTy(d19 * d64 + d20 * d68 + d21 * d72);
                this.setMzx(d22 * d61 + d23 * d65 + d24 * d69);
                this.setMzy(d22 * d62 + d23 * d66 + d24 * d70);
                this.setMzz(d22 * d63 + d23 * d67 + d24 * d71);
                this.setTz(d22 * d64 + d23 * d68 + d24 * d72);
            }
        }
        this.updateState();
    }

    private void preRotate2D(double d2) {
        if (this.state3d != 0) {
            this.preRotate3D(d2);
            return;
        }
        double d3 = Math.sin(Math.toRadians(d2));
        if (d3 == 1.0) {
            this.preRotate2D_90();
        } else if (d3 == -1.0) {
            this.preRotate2D_270();
        } else {
            double d4 = Math.cos(Math.toRadians(d2));
            if (d4 == -1.0) {
                this.preRotate2D_180();
            } else if (d4 != 1.0) {
                double d5 = this.getMxx();
                double d6 = this.getMyx();
                this.setMxx(d4 * d5 - d3 * d6);
                this.setMyx(d3 * d5 + d4 * d6);
                d5 = this.getMxy();
                d6 = this.getMyy();
                this.setMxy(d4 * d5 - d3 * d6);
                this.setMyy(d3 * d5 + d4 * d6);
                d5 = this.getTx();
                d6 = this.getTy();
                this.setTx(d4 * d5 - d3 * d6);
                this.setTy(d3 * d5 + d4 * d6);
                this.updateState2D();
            }
        }
    }

    private void preRotate2D_90() {
        double d2 = this.getMxx();
        this.setMxx(-this.getMyx());
        this.setMyx(d2);
        d2 = this.getMxy();
        this.setMxy(-this.getMyy());
        this.setMyy(d2);
        d2 = this.getTx();
        this.setTx(-this.getTy());
        this.setTy(d2);
        int n2 = rot90conversion[this.state2d];
        if ((n2 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
            n2 -= 2;
        } else if ((n2 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
            n2 = n2 & 0xFFFFFFFB | 2;
        }
        this.state2d = n2;
    }

    private void preRotate2D_180() {
        this.setMxx(-this.getMxx());
        this.setMxy(-this.getMxy());
        this.setTx(-this.getTx());
        this.setMyx(-this.getMyx());
        this.setMyy(-this.getMyy());
        this.setTy(-this.getTy());
        this.state2d = (this.state2d & 4) != 0 ? (this.getMxx() == 0.0 && this.getMyy() == 0.0 ? (this.state2d &= 0xFFFFFFFD) : (this.state2d |= 2)) : (this.getMxx() == 1.0 && this.getMyy() == 1.0 ? (this.state2d &= 0xFFFFFFFD) : (this.state2d |= 2));
    }

    private void preRotate2D_270() {
        double d2 = this.getMxx();
        this.setMxx(this.getMyx());
        this.setMyx(-d2);
        d2 = this.getMxy();
        this.setMxy(this.getMyy());
        this.setMyy(-d2);
        d2 = this.getTx();
        this.setTx(this.getTy());
        this.setTy(-d2);
        int n2 = rot90conversion[this.state2d];
        if ((n2 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
            n2 -= 2;
        } else if ((n2 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
            n2 = n2 & 0xFFFFFFFB | 2;
        }
        this.state2d = n2;
    }

    private void preRotate3D(double d2) {
        if (this.state3d == 0) {
            this.preRotate2D(d2);
            return;
        }
        double d3 = Math.sin(Math.toRadians(d2));
        if (d3 == 1.0) {
            this.preRotate3D_90();
        } else if (d3 == -1.0) {
            this.preRotate3D_270();
        } else {
            double d4 = Math.cos(Math.toRadians(d2));
            if (d4 == -1.0) {
                this.preRotate3D_180();
            } else if (d4 != 1.0) {
                double d5 = this.getMxx();
                double d6 = this.getMyx();
                this.setMxx(d4 * d5 - d3 * d6);
                this.setMyx(d3 * d5 + d4 * d6);
                d5 = this.getMxy();
                d6 = this.getMyy();
                this.setMxy(d4 * d5 - d3 * d6);
                this.setMyy(d3 * d5 + d4 * d6);
                d5 = this.getMxz();
                d6 = this.getMyz();
                this.setMxz(d4 * d5 - d3 * d6);
                this.setMyz(d3 * d5 + d4 * d6);
                d5 = this.getTx();
                d6 = this.getTy();
                this.setTx(d4 * d5 - d3 * d6);
                this.setTy(d3 * d5 + d4 * d6);
                this.updateState();
            }
        }
    }

    private void preRotate3D_90() {
        double d2 = this.getMxx();
        this.setMxx(-this.getMyx());
        this.setMyx(d2);
        d2 = this.getMxy();
        this.setMxy(-this.getMyy());
        this.setMyy(d2);
        d2 = this.getMxz();
        this.setMxz(-this.getMyz());
        this.setMyz(d2);
        d2 = this.getTx();
        this.setTx(-this.getTy());
        this.setTy(d2);
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 1: {
                this.state3d = 4;
                return;
            }
            case 2: 
            case 3: {
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 4: 
        }
        this.updateState();
    }

    private void preRotate3D_180() {
        double d2 = this.getMxx();
        double d3 = this.getMyy();
        this.setMxx(-d2);
        this.setMyy(-d3);
        this.setTx(-this.getTx());
        this.setTy(-this.getTy());
        if (this.state3d == 4) {
            this.setMxy(-this.getMxy());
            this.setMxz(-this.getMxz());
            this.setMyx(-this.getMyx());
            this.setMyz(-this.getMyz());
            this.updateState();
            return;
        }
        this.state3d = d2 == -1.0 && d3 == -1.0 && this.getMzz() == 1.0 ? (this.state3d &= 0xFFFFFFFD) : (this.state3d |= 2);
    }

    private void preRotate3D_270() {
        double d2 = this.getMxx();
        this.setMxx(this.getMyx());
        this.setMyx(-d2);
        d2 = this.getMxy();
        this.setMxy(this.getMyy());
        this.setMyy(-d2);
        d2 = this.getMxz();
        this.setMxz(this.getMyz());
        this.setMyz(-d2);
        d2 = this.getTx();
        this.setTx(this.getTy());
        this.setTy(-d2);
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 1: {
                this.state3d = 4;
                return;
            }
            case 2: 
            case 3: {
                if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                    this.state3d = 4;
                }
                return;
            }
            case 4: 
        }
        this.updateState();
    }

    @Override
    public Point2D transform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        switch (this.state2d) {
            default: {
                Affine.stateError();
            }
            case 7: {
                return new Point2D(this.getMxx() * d2 + this.getMxy() * d3 + this.getTx(), this.getMyx() * d2 + this.getMyy() * d3 + this.getTy());
            }
            case 6: {
                return new Point2D(this.getMxx() * d2 + this.getMxy() * d3, this.getMyx() * d2 + this.getMyy() * d3);
            }
            case 5: {
                return new Point2D(this.getMxy() * d3 + this.getTx(), this.getMyx() * d2 + this.getTy());
            }
            case 4: {
                return new Point2D(this.getMxy() * d3, this.getMyx() * d2);
            }
            case 3: {
                return new Point2D(this.getMxx() * d2 + this.getTx(), this.getMyy() * d3 + this.getTy());
            }
            case 2: {
                return new Point2D(this.getMxx() * d2, this.getMyy() * d3);
            }
            case 1: {
                return new Point2D(d2 + this.getTx(), d3 + this.getTy());
            }
            case 0: 
        }
        return new Point2D(d2, d3);
    }

    @Override
    public Point3D transform(double d2, double d3, double d4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        Affine.stateError();
                    }
                    case 7: {
                        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3 + this.getTx(), this.getMyx() * d2 + this.getMyy() * d3 + this.getTy(), d4);
                    }
                    case 6: {
                        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3, this.getMyx() * d2 + this.getMyy() * d3, d4);
                    }
                    case 5: {
                        return new Point3D(this.getMxy() * d3 + this.getTx(), this.getMyx() * d2 + this.getTy(), d4);
                    }
                    case 4: {
                        return new Point3D(this.getMxy() * d3, this.getMyx() * d2, d4);
                    }
                    case 3: {
                        return new Point3D(this.getMxx() * d2 + this.getTx(), this.getMyy() * d3 + this.getTy(), d4);
                    }
                    case 2: {
                        return new Point3D(this.getMxx() * d2, this.getMyy() * d3, d4);
                    }
                    case 1: {
                        return new Point3D(d2 + this.getTx(), d3 + this.getTy(), d4);
                    }
                    case 0: 
                }
                return new Point3D(d2, d3, d4);
            }
            case 1: {
                return new Point3D(d2 + this.getTx(), d3 + this.getTy(), d4 + this.getTz());
            }
            case 2: {
                return new Point3D(this.getMxx() * d2, this.getMyy() * d3, this.getMzz() * d4);
            }
            case 3: {
                return new Point3D(this.getMxx() * d2 + this.getTx(), this.getMyy() * d3 + this.getTy(), this.getMzz() * d4 + this.getTz());
            }
            case 4: 
        }
        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3 + this.getMxz() * d4 + this.getTx(), this.getMyx() * d2 + this.getMyy() * d3 + this.getMyz() * d4 + this.getTy(), this.getMzx() * d2 + this.getMzy() * d3 + this.getMzz() * d4 + this.getTz());
    }

    @Override
    void transform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        switch (this.state2d) {
            default: {
                Affine.stateError();
            }
            case 7: {
                double d2 = this.getMxx();
                double d3 = this.getMxy();
                double d4 = this.getTx();
                double d5 = this.getMyx();
                double d6 = this.getMyy();
                double d7 = this.getTy();
                while (--n4 >= 0) {
                    double d8 = arrd[n2++];
                    double d9 = arrd[n2++];
                    arrd2[n3++] = d2 * d8 + d3 * d9 + d4;
                    arrd2[n3++] = d5 * d8 + d6 * d9 + d7;
                }
                return;
            }
            case 6: {
                double d10 = this.getMxx();
                double d11 = this.getMxy();
                double d12 = this.getMyx();
                double d13 = this.getMyy();
                while (--n4 >= 0) {
                    double d14 = arrd[n2++];
                    double d15 = arrd[n2++];
                    arrd2[n3++] = d10 * d14 + d11 * d15;
                    arrd2[n3++] = d12 * d14 + d13 * d15;
                }
                return;
            }
            case 5: {
                double d16 = this.getMxy();
                double d17 = this.getTx();
                double d18 = this.getMyx();
                double d19 = this.getTy();
                while (--n4 >= 0) {
                    double d20 = arrd[n2++];
                    arrd2[n3++] = d16 * arrd[n2++] + d17;
                    arrd2[n3++] = d18 * d20 + d19;
                }
                return;
            }
            case 4: {
                double d21 = this.getMxy();
                double d22 = this.getMyx();
                while (--n4 >= 0) {
                    double d23 = arrd[n2++];
                    arrd2[n3++] = d21 * arrd[n2++];
                    arrd2[n3++] = d22 * d23;
                }
                return;
            }
            case 3: {
                double d24 = this.getMxx();
                double d25 = this.getTx();
                double d26 = this.getMyy();
                double d27 = this.getTy();
                while (--n4 >= 0) {
                    arrd2[n3++] = d24 * arrd[n2++] + d25;
                    arrd2[n3++] = d26 * arrd[n2++] + d27;
                }
                return;
            }
            case 2: {
                double d28 = this.getMxx();
                double d29 = this.getMyy();
                while (--n4 >= 0) {
                    arrd2[n3++] = d28 * arrd[n2++];
                    arrd2[n3++] = d29 * arrd[n2++];
                }
                return;
            }
            case 1: {
                double d30 = this.getTx();
                double d31 = this.getTy();
                while (--n4 >= 0) {
                    arrd2[n3++] = arrd[n2++] + d30;
                    arrd2[n3++] = arrd[n2++] + d31;
                }
                return;
            }
            case 0: 
        }
        if (arrd != arrd2 || n2 != n3) {
            System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
        }
    }

    @Override
    void transform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        Affine.stateError();
                    }
                    case 7: {
                        double d2 = this.getMxx();
                        double d3 = this.getMxy();
                        double d4 = this.getTx();
                        double d5 = this.getMyx();
                        double d6 = this.getMyy();
                        double d7 = this.getTy();
                        while (--n4 >= 0) {
                            double d8 = arrd[n2++];
                            double d9 = arrd[n2++];
                            arrd2[n3++] = d2 * d8 + d3 * d9 + d4;
                            arrd2[n3++] = d5 * d8 + d6 * d9 + d7;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 6: {
                        double d10 = this.getMxx();
                        double d11 = this.getMxy();
                        double d12 = this.getMyx();
                        double d13 = this.getMyy();
                        while (--n4 >= 0) {
                            double d14 = arrd[n2++];
                            double d15 = arrd[n2++];
                            arrd2[n3++] = d10 * d14 + d11 * d15;
                            arrd2[n3++] = d12 * d14 + d13 * d15;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 5: {
                        double d16 = this.getMxy();
                        double d17 = this.getTx();
                        double d18 = this.getMyx();
                        double d19 = this.getTy();
                        while (--n4 >= 0) {
                            double d20 = arrd[n2++];
                            arrd2[n3++] = d16 * arrd[n2++] + d17;
                            arrd2[n3++] = d18 * d20 + d19;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 4: {
                        double d21 = this.getMxy();
                        double d22 = this.getMyx();
                        while (--n4 >= 0) {
                            double d23 = arrd[n2++];
                            arrd2[n3++] = d21 * arrd[n2++];
                            arrd2[n3++] = d22 * d23;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 3: {
                        double d24 = this.getMxx();
                        double d25 = this.getTx();
                        double d26 = this.getMyy();
                        double d27 = this.getTy();
                        while (--n4 >= 0) {
                            arrd2[n3++] = d24 * arrd[n2++] + d25;
                            arrd2[n3++] = d26 * arrd[n2++] + d27;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 2: {
                        double d28 = this.getMxx();
                        double d29 = this.getMyy();
                        while (--n4 >= 0) {
                            arrd2[n3++] = d28 * arrd[n2++];
                            arrd2[n3++] = d29 * arrd[n2++];
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 1: {
                        double d30 = this.getTx();
                        double d31 = this.getTy();
                        while (--n4 >= 0) {
                            arrd2[n3++] = arrd[n2++] + d30;
                            arrd2[n3++] = arrd[n2++] + d31;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 0: 
                }
                if (arrd != arrd2 || n2 != n3) {
                    System.arraycopy(arrd, n2, arrd2, n3, n4 * 3);
                }
                return;
            }
            case 1: {
                double d32 = this.getTx();
                double d33 = this.getTy();
                double d34 = this.getTz();
                while (--n4 >= 0) {
                    arrd2[n3++] = arrd[n2++] + d32;
                    arrd2[n3++] = arrd[n2++] + d33;
                    arrd2[n3++] = arrd[n2++] + d34;
                }
                return;
            }
            case 2: {
                double d35 = this.getMxx();
                double d36 = this.getMyy();
                double d37 = this.getMzz();
                while (--n4 >= 0) {
                    arrd2[n3++] = d35 * arrd[n2++];
                    arrd2[n3++] = d36 * arrd[n2++];
                    arrd2[n3++] = d37 * arrd[n2++];
                }
                return;
            }
            case 3: {
                double d38 = this.getMxx();
                double d39 = this.getTx();
                double d40 = this.getMyy();
                double d41 = this.getTy();
                double d42 = this.getMzz();
                double d43 = this.getTz();
                while (--n4 >= 0) {
                    arrd2[n3++] = d38 * arrd[n2++] + d39;
                    arrd2[n3++] = d40 * arrd[n2++] + d41;
                    arrd2[n3++] = d42 * arrd[n2++] + d43;
                }
                return;
            }
            case 4: 
        }
        double d44 = this.getMxx();
        double d45 = this.getMxy();
        double d46 = this.getMxz();
        double d47 = this.getTx();
        double d48 = this.getMyx();
        double d49 = this.getMyy();
        double d50 = this.getMyz();
        double d51 = this.getTy();
        double d52 = this.getMzx();
        double d53 = this.getMzy();
        double d54 = this.getMzz();
        double d55 = this.getTz();
        while (--n4 >= 0) {
            double d56 = arrd[n2++];
            double d57 = arrd[n2++];
            double d58 = arrd[n2++];
            arrd2[n3++] = d44 * d56 + d45 * d57 + d46 * d58 + d47;
            arrd2[n3++] = d48 * d56 + d49 * d57 + d50 * d58 + d51;
            arrd2[n3++] = d52 * d56 + d53 * d57 + d54 * d58 + d55;
        }
    }

    @Override
    public Point2D deltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        switch (this.state2d) {
            default: {
                Affine.stateError();
            }
            case 6: 
            case 7: {
                return new Point2D(this.getMxx() * d2 + this.getMxy() * d3, this.getMyx() * d2 + this.getMyy() * d3);
            }
            case 4: 
            case 5: {
                return new Point2D(this.getMxy() * d3, this.getMyx() * d2);
            }
            case 2: 
            case 3: {
                return new Point2D(this.getMxx() * d2, this.getMyy() * d3);
            }
            case 0: 
            case 1: 
        }
        return new Point2D(d2, d3);
    }

    @Override
    public Point3D deltaTransform(double d2, double d3, double d4) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        Affine.stateError();
                    }
                    case 6: 
                    case 7: {
                        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3, this.getMyx() * d2 + this.getMyy() * d3, d4);
                    }
                    case 4: 
                    case 5: {
                        return new Point3D(this.getMxy() * d3, this.getMyx() * d2, d4);
                    }
                    case 2: 
                    case 3: {
                        return new Point3D(this.getMxx() * d2, this.getMyy() * d3, d4);
                    }
                    case 0: 
                    case 1: 
                }
                return new Point3D(d2, d3, d4);
            }
            case 1: {
                return new Point3D(d2, d3, d4);
            }
            case 2: 
            case 3: {
                return new Point3D(this.getMxx() * d2, this.getMyy() * d3, this.getMzz() * d4);
            }
            case 4: 
        }
        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3 + this.getMxz() * d4, this.getMyx() * d2 + this.getMyy() * d3 + this.getMyz() * d4, this.getMzx() * d2 + this.getMzy() * d3 + this.getMzz() * d4);
    }

    @Override
    public Point2D inverseTransform(double d2, double d3) throws NonInvertibleTransformException {
        this.ensureCanTransform2DPoint();
        switch (this.state2d) {
            default: {
                return super.inverseTransform(d2, d3);
            }
            case 5: {
                double d4 = this.getMxy();
                double d5 = this.getMyx();
                if (d4 == 0.0 || d5 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(1.0 / d5 * d3 - this.getTy() / d5, 1.0 / d4 * d2 - this.getTx() / d4);
            }
            case 4: {
                double d6 = this.getMxy();
                double d7 = this.getMyx();
                if (d6 == 0.0 || d7 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(1.0 / d7 * d3, 1.0 / d6 * d2);
            }
            case 3: {
                double d8 = this.getMxx();
                double d9 = this.getMyy();
                if (d8 == 0.0 || d9 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(1.0 / d8 * d2 - this.getTx() / d8, 1.0 / d9 * d3 - this.getTy() / d9);
            }
            case 2: {
                double d10 = this.getMxx();
                double d11 = this.getMyy();
                if (d10 == 0.0 || d11 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(1.0 / d10 * d2, 1.0 / d11 * d3);
            }
            case 1: {
                return new Point2D(d2 - this.getTx(), d3 - this.getTy());
            }
            case 0: 
        }
        return new Point2D(d2, d3);
    }

    @Override
    public Point3D inverseTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        return super.inverseTransform(d2, d3, d4);
                    }
                    case 5: {
                        double d5 = this.getMxy();
                        double d6 = this.getMyx();
                        if (d5 == 0.0 || d6 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        return new Point3D(1.0 / d6 * d3 - this.getTy() / d6, 1.0 / d5 * d2 - this.getTx() / d5, d4);
                    }
                    case 4: {
                        double d7 = this.getMxy();
                        double d8 = this.getMyx();
                        if (d7 == 0.0 || d8 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        return new Point3D(1.0 / d8 * d3, 1.0 / d7 * d2, d4);
                    }
                    case 3: {
                        double d9 = this.getMxx();
                        double d10 = this.getMyy();
                        if (d9 == 0.0 || d10 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        return new Point3D(1.0 / d9 * d2 - this.getTx() / d9, 1.0 / d10 * d3 - this.getTy() / d10, d4);
                    }
                    case 2: {
                        double d11 = this.getMxx();
                        double d12 = this.getMyy();
                        if (d11 == 0.0 || d12 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        return new Point3D(1.0 / d11 * d2, 1.0 / d12 * d3, d4);
                    }
                    case 1: {
                        return new Point3D(d2 - this.getTx(), d3 - this.getTy(), d4);
                    }
                    case 0: 
                }
                return new Point3D(d2, d3, d4);
            }
            case 1: {
                return new Point3D(d2 - this.getTx(), d3 - this.getTy(), d4 - this.getTz());
            }
            case 2: {
                double d13 = this.getMxx();
                double d14 = this.getMyy();
                double d15 = this.getMzz();
                if (d13 == 0.0 || d14 == 0.0 || d15 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D(1.0 / d13 * d2, 1.0 / d14 * d3, 1.0 / d15 * d4);
            }
            case 3: {
                double d16 = this.getMxx();
                double d17 = this.getMyy();
                double d18 = this.getMzz();
                if (d16 == 0.0 || d17 == 0.0 || d18 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D(1.0 / d16 * d2 - this.getTx() / d16, 1.0 / d17 * d3 - this.getTy() / d17, 1.0 / d18 * d4 - this.getTz() / d18);
            }
            case 4: 
        }
        return super.inverseTransform(d2, d3, d4);
    }

    @Override
    void inverseTransform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        switch (this.state2d) {
            default: {
                super.inverseTransform2DPointsImpl(arrd, n2, arrd2, n3, n4);
                return;
            }
            case 5: {
                double d2 = this.getMxy();
                double d3 = this.getTx();
                double d4 = this.getMyx();
                double d5 = this.getTy();
                if (d2 == 0.0 || d4 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double d6 = d3;
                d3 = -d5 / d4;
                d5 = -d6 / d2;
                d6 = d4;
                d4 = 1.0 / d2;
                d2 = 1.0 / d6;
                while (--n4 >= 0) {
                    double d7 = arrd[n2++];
                    arrd2[n3++] = d2 * arrd[n2++] + d3;
                    arrd2[n3++] = d4 * d7 + d5;
                }
                return;
            }
            case 4: {
                double d8 = this.getMxy();
                double d9 = this.getMyx();
                if (d8 == 0.0 || d9 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                double d10 = d9;
                d9 = 1.0 / d8;
                d8 = 1.0 / d10;
                while (--n4 >= 0) {
                    double d11 = arrd[n2++];
                    arrd2[n3++] = d8 * arrd[n2++];
                    arrd2[n3++] = d9 * d11;
                }
                return;
            }
            case 3: {
                double d12 = this.getMxx();
                double d13 = this.getTx();
                double d14 = this.getMyy();
                double d15 = this.getTy();
                if (d12 == 0.0 || d14 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                d13 = -d13 / d12;
                d15 = -d15 / d14;
                d12 = 1.0 / d12;
                d14 = 1.0 / d14;
                while (--n4 >= 0) {
                    arrd2[n3++] = d12 * arrd[n2++] + d13;
                    arrd2[n3++] = d14 * arrd[n2++] + d15;
                }
                return;
            }
            case 2: {
                double d16 = this.getMxx();
                double d17 = this.getMyy();
                if (d16 == 0.0 || d17 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                d16 = 1.0 / d16;
                d17 = 1.0 / d17;
                while (--n4 >= 0) {
                    arrd2[n3++] = d16 * arrd[n2++];
                    arrd2[n3++] = d17 * arrd[n2++];
                }
                return;
            }
            case 1: {
                double d18 = this.getTx();
                double d19 = this.getTy();
                while (--n4 >= 0) {
                    arrd2[n3++] = arrd[n2++] - d18;
                    arrd2[n3++] = arrd[n2++] - d19;
                }
                return;
            }
            case 0: 
        }
        if (arrd != arrd2 || n2 != n3) {
            System.arraycopy(arrd, n2, arrd2, n3, n4 * 2);
        }
    }

    @Override
    void inverseTransform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        super.inverseTransform3DPointsImpl(arrd, n2, arrd2, n3, n4);
                        return;
                    }
                    case 5: {
                        double d2 = this.getMxy();
                        double d3 = this.getTx();
                        double d4 = this.getMyx();
                        double d5 = this.getTy();
                        if (d2 == 0.0 || d4 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        double d6 = d3;
                        d3 = -d5 / d4;
                        d5 = -d6 / d2;
                        d6 = d4;
                        d4 = 1.0 / d2;
                        d2 = 1.0 / d6;
                        while (--n4 >= 0) {
                            double d7 = arrd[n2++];
                            arrd2[n3++] = d2 * arrd[n2++] + d3;
                            arrd2[n3++] = d4 * d7 + d5;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 4: {
                        double d8 = this.getMxy();
                        double d9 = this.getMyx();
                        if (d8 == 0.0 || d9 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        double d10 = d9;
                        d9 = 1.0 / d8;
                        d8 = 1.0 / d10;
                        while (--n4 >= 0) {
                            double d11 = arrd[n2++];
                            arrd2[n3++] = d8 * arrd[n2++];
                            arrd2[n3++] = d9 * d11;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 3: {
                        double d12 = this.getMxx();
                        double d13 = this.getTx();
                        double d14 = this.getMyy();
                        double d15 = this.getTy();
                        if (d12 == 0.0 || d14 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        d13 = -d13 / d12;
                        d15 = -d15 / d14;
                        d12 = 1.0 / d12;
                        d14 = 1.0 / d14;
                        while (--n4 >= 0) {
                            arrd2[n3++] = d12 * arrd[n2++] + d13;
                            arrd2[n3++] = d14 * arrd[n2++] + d15;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 2: {
                        double d16 = this.getMxx();
                        double d17 = this.getMyy();
                        if (d16 == 0.0 || d17 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        d16 = 1.0 / d16;
                        d17 = 1.0 / d17;
                        while (--n4 >= 0) {
                            arrd2[n3++] = d16 * arrd[n2++];
                            arrd2[n3++] = d17 * arrd[n2++];
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 1: {
                        double d18 = this.getTx();
                        double d19 = this.getTy();
                        while (--n4 >= 0) {
                            arrd2[n3++] = arrd[n2++] - d18;
                            arrd2[n3++] = arrd[n2++] - d19;
                            arrd2[n3++] = arrd[n2++];
                        }
                        return;
                    }
                    case 0: 
                }
                if (arrd != arrd2 || n2 != n3) {
                    System.arraycopy(arrd, n2, arrd2, n3, n4 * 3);
                }
                return;
            }
            case 1: {
                double d20 = this.getTx();
                double d21 = this.getTy();
                double d22 = this.getTz();
                while (--n4 >= 0) {
                    arrd2[n3++] = arrd[n2++] - d20;
                    arrd2[n3++] = arrd[n2++] - d21;
                    arrd2[n3++] = arrd[n2++] - d22;
                }
                return;
            }
            case 2: {
                double d23 = this.getMxx();
                double d24 = this.getMyy();
                double d25 = this.getMzz();
                if (d23 == 0.0 || d24 == 0.0 | d25 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                d23 = 1.0 / d23;
                d24 = 1.0 / d24;
                d25 = 1.0 / d25;
                while (--n4 >= 0) {
                    arrd2[n3++] = d23 * arrd[n2++];
                    arrd2[n3++] = d24 * arrd[n2++];
                    arrd2[n3++] = d25 * arrd[n2++];
                }
                return;
            }
            case 3: {
                double d26 = this.getMxx();
                double d27 = this.getTx();
                double d28 = this.getMyy();
                double d29 = this.getTy();
                double d30 = this.getMzz();
                double d31 = this.getTz();
                if (d26 == 0.0 || d28 == 0.0 || d30 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                d27 = -d27 / d26;
                d29 = -d29 / d28;
                d31 = -d31 / d30;
                d26 = 1.0 / d26;
                d28 = 1.0 / d28;
                d30 = 1.0 / d30;
                while (--n4 >= 0) {
                    arrd2[n3++] = d26 * arrd[n2++] + d27;
                    arrd2[n3++] = d28 * arrd[n2++] + d29;
                    arrd2[n3++] = d30 * arrd[n2++] + d31;
                }
                return;
            }
            case 4: 
        }
        super.inverseTransform3DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    @Override
    public Point2D inverseDeltaTransform(double d2, double d3) throws NonInvertibleTransformException {
        this.ensureCanTransform2DPoint();
        switch (this.state2d) {
            default: {
                return super.inverseDeltaTransform(d2, d3);
            }
            case 4: 
            case 5: {
                double d4 = this.getMxy();
                double d5 = this.getMyx();
                if (d4 == 0.0 || d5 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(1.0 / d5 * d3, 1.0 / d4 * d2);
            }
            case 2: 
            case 3: {
                double d6 = this.getMxx();
                double d7 = this.getMyy();
                if (d6 == 0.0 || d7 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point2D(1.0 / d6 * d2, 1.0 / d7 * d3);
            }
            case 0: 
            case 1: 
        }
        return new Point2D(d2, d3);
    }

    @Override
    public Point3D inverseDeltaTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    default: {
                        return super.inverseDeltaTransform(d2, d3, d4);
                    }
                    case 4: 
                    case 5: {
                        double d5 = this.getMxy();
                        double d6 = this.getMyx();
                        if (d5 == 0.0 || d6 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        return new Point3D(1.0 / d6 * d3, 1.0 / d5 * d2, d4);
                    }
                    case 2: 
                    case 3: {
                        double d7 = this.getMxx();
                        double d8 = this.getMyy();
                        if (d7 == 0.0 || d8 == 0.0) {
                            throw new NonInvertibleTransformException("Determinant is 0");
                        }
                        return new Point3D(1.0 / d7 * d2, 1.0 / d8 * d3, d4);
                    }
                    case 0: 
                    case 1: 
                }
                return new Point3D(d2, d3, d4);
            }
            case 1: {
                return new Point3D(d2, d3, d4);
            }
            case 2: 
            case 3: {
                double d9 = this.getMxx();
                double d10 = this.getMyy();
                double d11 = this.getMzz();
                if (d9 == 0.0 || d10 == 0.0 || d11 == 0.0) {
                    throw new NonInvertibleTransformException("Determinant is 0");
                }
                return new Point3D(1.0 / d9 * d2, 1.0 / d10 * d3, 1.0 / d11 * d4);
            }
            case 4: 
        }
        return super.inverseDeltaTransform(d2, d3, d4);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Affine [\n");
        stringBuilder.append("\t").append(this.getMxx());
        stringBuilder.append(", ").append(this.getMxy());
        stringBuilder.append(", ").append(this.getMxz());
        stringBuilder.append(", ").append(this.getTx());
        stringBuilder.append('\n');
        stringBuilder.append("\t").append(this.getMyx());
        stringBuilder.append(", ").append(this.getMyy());
        stringBuilder.append(", ").append(this.getMyz());
        stringBuilder.append(", ").append(this.getTy());
        stringBuilder.append('\n');
        stringBuilder.append("\t").append(this.getMzx());
        stringBuilder.append(", ").append(this.getMzy());
        stringBuilder.append(", ").append(this.getMzz());
        stringBuilder.append(", ").append(this.getTz());
        return stringBuilder.append("\n]").toString();
    }

    private void updateState() {
        this.updateState2D();
        this.state3d = 0;
        if (this.getMxz() != 0.0 || this.getMyz() != 0.0 || this.getMzx() != 0.0 || this.getMzy() != 0.0) {
            this.state3d = 4;
        } else if ((this.state2d & 4) == 0) {
            if (this.getTz() != 0.0) {
                this.state3d |= 1;
            }
            if (this.getMzz() != 1.0) {
                this.state3d |= 2;
            }
            if (this.state3d != 0) {
                this.state3d |= this.state2d & 3;
            }
        } else if (this.getMzz() != 1.0 || this.getTz() != 0.0) {
            this.state3d = 4;
        }
    }

    private void updateState2D() {
        this.state2d = this.getMxy() == 0.0 && this.getMyx() == 0.0 ? (this.getMxx() == 1.0 && this.getMyy() == 1.0 ? (this.getTx() == 0.0 && this.getTy() == 0.0 ? 0 : 1) : (this.getTx() == 0.0 && this.getTy() == 0.0 ? 2 : 3)) : (this.getMxx() == 0.0 && this.getMyy() == 0.0 ? (this.getTx() == 0.0 && this.getTy() == 0.0 ? 4 : 5) : (this.getTx() == 0.0 && this.getTy() == 0.0 ? 6 : 7));
    }

    private static void stateError() {
        throw new InternalError("missing case in a switch");
    }

    @Override
    @Deprecated
    public void impl_apply(Affine3D affine3D) {
        affine3D.concatenate(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
    }

    @Override
    @Deprecated
    public BaseTransform impl_derive(BaseTransform baseTransform) {
        switch (this.state3d) {
            default: {
                Affine.stateError();
            }
            case 0: {
                switch (this.state2d) {
                    case 0: {
                        return baseTransform;
                    }
                    case 1: {
                        return baseTransform.deriveWithTranslation(this.getTx(), this.getTy());
                    }
                    case 2: {
                        return baseTransform.deriveWithScale(this.getMxx(), this.getMyy(), 1.0);
                    }
                }
                return baseTransform.deriveWithConcatenation(this.getMxx(), this.getMyx(), this.getMxy(), this.getMyy(), this.getTx(), this.getTy());
            }
            case 1: {
                return baseTransform.deriveWithTranslation(this.getTx(), this.getTy(), this.getTz());
            }
            case 2: {
                return baseTransform.deriveWithScale(this.getMxx(), this.getMyy(), this.getMzz());
            }
            case 3: 
            case 4: 
        }
        return baseTransform.deriveWithConcatenation(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
    }

    int getState2d() {
        return this.state2d;
    }

    int getState3d() {
        return this.state3d;
    }

    boolean atomicChangeRuns() {
        return this.atomicChange.runs();
    }

    private class AffineAtomicChange {
        private boolean running = false;

        private AffineAtomicChange() {
        }

        private void start() {
            if (this.running) {
                throw new InternalError("Affine internal error: trying to run inner atomic operation");
            }
            if (Affine.this.mxx != null) {
                Affine.this.mxx.preProcessAtomicChange();
            }
            if (Affine.this.mxy != null) {
                Affine.this.mxy.preProcessAtomicChange();
            }
            if (Affine.this.mxz != null) {
                Affine.this.mxz.preProcessAtomicChange();
            }
            if (Affine.this.tx != null) {
                Affine.this.tx.preProcessAtomicChange();
            }
            if (Affine.this.myx != null) {
                Affine.this.myx.preProcessAtomicChange();
            }
            if (Affine.this.myy != null) {
                Affine.this.myy.preProcessAtomicChange();
            }
            if (Affine.this.myz != null) {
                Affine.this.myz.preProcessAtomicChange();
            }
            if (Affine.this.ty != null) {
                Affine.this.ty.preProcessAtomicChange();
            }
            if (Affine.this.mzx != null) {
                Affine.this.mzx.preProcessAtomicChange();
            }
            if (Affine.this.mzy != null) {
                Affine.this.mzy.preProcessAtomicChange();
            }
            if (Affine.this.mzz != null) {
                Affine.this.mzz.preProcessAtomicChange();
            }
            if (Affine.this.tz != null) {
                Affine.this.tz.preProcessAtomicChange();
            }
            this.running = true;
        }

        private void end() {
            this.running = false;
            Affine.this.transformChanged();
            if (Affine.this.mxx != null) {
                Affine.this.mxx.postProcessAtomicChange();
            }
            if (Affine.this.mxy != null) {
                Affine.this.mxy.postProcessAtomicChange();
            }
            if (Affine.this.mxz != null) {
                Affine.this.mxz.postProcessAtomicChange();
            }
            if (Affine.this.tx != null) {
                Affine.this.tx.postProcessAtomicChange();
            }
            if (Affine.this.myx != null) {
                Affine.this.myx.postProcessAtomicChange();
            }
            if (Affine.this.myy != null) {
                Affine.this.myy.postProcessAtomicChange();
            }
            if (Affine.this.myz != null) {
                Affine.this.myz.postProcessAtomicChange();
            }
            if (Affine.this.ty != null) {
                Affine.this.ty.postProcessAtomicChange();
            }
            if (Affine.this.mzx != null) {
                Affine.this.mzx.postProcessAtomicChange();
            }
            if (Affine.this.mzy != null) {
                Affine.this.mzy.postProcessAtomicChange();
            }
            if (Affine.this.mzz != null) {
                Affine.this.mzz.postProcessAtomicChange();
            }
            if (Affine.this.tz != null) {
                Affine.this.tz.postProcessAtomicChange();
            }
        }

        private void cancel() {
            this.running = false;
        }

        private boolean runs() {
            return this.running;
        }
    }

    private class AffineElementProperty
    extends SimpleDoubleProperty {
        private boolean needsValueChangedEvent;
        private double oldValue;

        public AffineElementProperty(double d2) {
            super(d2);
            this.needsValueChangedEvent = false;
        }

        @Override
        public void invalidated() {
            if (!Affine.this.atomicChange.runs()) {
                Affine.this.updateState();
                Affine.this.transformChanged();
            }
        }

        @Override
        protected void fireValueChangedEvent() {
            if (!Affine.this.atomicChange.runs()) {
                super.fireValueChangedEvent();
            } else {
                this.needsValueChangedEvent = true;
            }
        }

        private void preProcessAtomicChange() {
            this.oldValue = this.get();
        }

        private void postProcessAtomicChange() {
            if (this.needsValueChangedEvent) {
                this.needsValueChangedEvent = false;
                if (this.oldValue != this.get()) {
                    super.fireValueChangedEvent();
                }
            }
        }
    }
}

