/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geometry.BoundsUtils;
import com.sun.javafx.scene.transform.TransformUtils;
import com.sun.javafx.util.WeakReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.MatrixType;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.TransformChangedEvent;
import javafx.scene.transform.Translate;

public abstract class Transform
implements Cloneable,
EventTarget {
    private SoftReference<Transform> inverseCache = null;
    private WeakReferenceQueue impl_nodes = new WeakReferenceQueue();
    private LazyBooleanProperty type2D;
    private LazyBooleanProperty identity;
    private EventHandlerManager internalEventDispatcher;
    private ObjectProperty<EventHandler<? super TransformChangedEvent>> onTransformChanged;

    public static Affine affine(double d2, double d3, double d4, double d5, double d6, double d7) {
        Affine affine = new Affine();
        affine.setMxx(d2);
        affine.setMxy(d4);
        affine.setTx(d6);
        affine.setMyx(d3);
        affine.setMyy(d5);
        affine.setTy(d7);
        return affine;
    }

    public static Affine affine(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        Affine affine = new Affine();
        affine.setMxx(d2);
        affine.setMxy(d3);
        affine.setMxz(d4);
        affine.setTx(d5);
        affine.setMyx(d6);
        affine.setMyy(d7);
        affine.setMyz(d8);
        affine.setTy(d9);
        affine.setMzx(d10);
        affine.setMzy(d11);
        affine.setMzz(d12);
        affine.setTz(d13);
        return affine;
    }

    public static Translate translate(double d2, double d3) {
        Translate translate = new Translate();
        translate.setX(d2);
        translate.setY(d3);
        return translate;
    }

    public static Rotate rotate(double d2, double d3, double d4) {
        Rotate rotate = new Rotate();
        rotate.setAngle(d2);
        rotate.setPivotX(d3);
        rotate.setPivotY(d4);
        return rotate;
    }

    public static Scale scale(double d2, double d3) {
        Scale scale = new Scale();
        scale.setX(d2);
        scale.setY(d3);
        return scale;
    }

    public static Scale scale(double d2, double d3, double d4, double d5) {
        Scale scale = new Scale();
        scale.setX(d2);
        scale.setY(d3);
        scale.setPivotX(d4);
        scale.setPivotY(d5);
        return scale;
    }

    public static Shear shear(double d2, double d3) {
        Shear shear = new Shear();
        shear.setX(d2);
        shear.setY(d3);
        return shear;
    }

    public static Shear shear(double d2, double d3, double d4, double d5) {
        Shear shear = new Shear();
        shear.setX(d2);
        shear.setY(d3);
        shear.setPivotX(d4);
        shear.setPivotY(d5);
        return shear;
    }

    public double getMxx() {
        return 1.0;
    }

    public double getMxy() {
        return 0.0;
    }

    public double getMxz() {
        return 0.0;
    }

    public double getTx() {
        return 0.0;
    }

    public double getMyx() {
        return 0.0;
    }

    public double getMyy() {
        return 1.0;
    }

    public double getMyz() {
        return 0.0;
    }

    public double getTy() {
        return 0.0;
    }

    public double getMzx() {
        return 0.0;
    }

    public double getMzy() {
        return 0.0;
    }

    public double getMzz() {
        return 1.0;
    }

    public double getTz() {
        return 0.0;
    }

    public double getElement(MatrixType matrixType, int n2, int n3) {
        if (n2 < 0 || n2 >= matrixType.rows() || n3 < 0 || n3 >= matrixType.columns()) {
            throw new IndexOutOfBoundsException("Index outside of affine matrix " + (Object)((Object)matrixType) + ": [" + n2 + ", " + n3 + "]");
        }
        switch (matrixType) {
            case MT_2D_2x3: 
            case MT_2D_3x3: {
                if (!this.isType2D()) {
                    throw new IllegalArgumentException("Cannot access 2D matrix of a 3D transform");
                }
                switch (n2) {
                    case 0: {
                        switch (n3) {
                            case 0: {
                                return this.getMxx();
                            }
                            case 1: {
                                return this.getMxy();
                            }
                            case 2: {
                                return this.getTx();
                            }
                        }
                    }
                    case 1: {
                        switch (n3) {
                            case 0: {
                                return this.getMyx();
                            }
                            case 1: {
                                return this.getMyy();
                            }
                            case 2: {
                                return this.getTy();
                            }
                        }
                    }
                    case 2: {
                        switch (n3) {
                            case 0: {
                                return 0.0;
                            }
                            case 1: {
                                return 0.0;
                            }
                            case 2: {
                                return 1.0;
                            }
                        }
                    }
                }
                break;
            }
            case MT_3D_3x4: 
            case MT_3D_4x4: {
                switch (n2) {
                    case 0: {
                        switch (n3) {
                            case 0: {
                                return this.getMxx();
                            }
                            case 1: {
                                return this.getMxy();
                            }
                            case 2: {
                                return this.getMxz();
                            }
                            case 3: {
                                return this.getTx();
                            }
                        }
                    }
                    case 1: {
                        switch (n3) {
                            case 0: {
                                return this.getMyx();
                            }
                            case 1: {
                                return this.getMyy();
                            }
                            case 2: {
                                return this.getMyz();
                            }
                            case 3: {
                                return this.getTy();
                            }
                        }
                    }
                    case 2: {
                        switch (n3) {
                            case 0: {
                                return this.getMzx();
                            }
                            case 1: {
                                return this.getMzy();
                            }
                            case 2: {
                                return this.getMzz();
                            }
                            case 3: {
                                return this.getTz();
                            }
                        }
                    }
                    case 3: {
                        switch (n3) {
                            case 0: {
                                return 0.0;
                            }
                            case 1: {
                                return 0.0;
                            }
                            case 2: {
                                return 0.0;
                            }
                            case 3: {
                                return 1.0;
                            }
                        }
                    }
                }
            }
        }
        throw new InternalError("Unsupported matrix type " + (Object)((Object)matrixType));
    }

    boolean computeIs2D() {
        return this.getMxz() == 0.0 && this.getMzx() == 0.0 && this.getMzy() == 0.0 && this.getMzz() == 1.0 && this.getTz() == 0.0;
    }

    boolean computeIsIdentity() {
        return this.getMxx() == 1.0 && this.getMxy() == 0.0 && this.getMxz() == 0.0 && this.getTx() == 0.0 && this.getMyx() == 0.0 && this.getMyy() == 1.0 && this.getMyz() == 0.0 && this.getTy() == 0.0 && this.getMzx() == 0.0 && this.getMzy() == 0.0 && this.getMzz() == 1.0 && this.getTz() == 0.0;
    }

    public double determinant() {
        double d2 = this.getMyx();
        double d3 = this.getMyy();
        double d4 = this.getMyz();
        double d5 = this.getMzx();
        double d6 = this.getMzy();
        double d7 = this.getMzz();
        return this.getMxx() * (d3 * d7 - d6 * d4) + this.getMxy() * (d4 * d5 - d7 * d2) + this.getMxz() * (d2 * d6 - d5 * d3);
    }

    public final boolean isType2D() {
        return this.type2D == null ? this.computeIs2D() : this.type2D.get();
    }

    public final ReadOnlyBooleanProperty type2DProperty() {
        if (this.type2D == null) {
            this.type2D = new LazyBooleanProperty(){

                @Override
                protected boolean computeValue() {
                    return Transform.this.computeIs2D();
                }

                @Override
                public Object getBean() {
                    return Transform.this;
                }

                @Override
                public String getName() {
                    return "type2D";
                }
            };
        }
        return this.type2D;
    }

    public final boolean isIdentity() {
        return this.identity == null ? this.computeIsIdentity() : this.identity.get();
    }

    public final ReadOnlyBooleanProperty identityProperty() {
        if (this.identity == null) {
            this.identity = new LazyBooleanProperty(){

                @Override
                protected boolean computeValue() {
                    return Transform.this.computeIsIdentity();
                }

                @Override
                public Object getBean() {
                    return Transform.this;
                }

                @Override
                public String getName() {
                    return "identity";
                }
            };
        }
        return this.identity;
    }

    private double transformDiff(Transform transform, double d2, double d3) {
        Point2D point2D = this.transform(d2, d3);
        Point2D point2D2 = transform.transform(d2, d3);
        return point2D.distance(point2D2);
    }

    private double transformDiff(Transform transform, double d2, double d3, double d4) {
        Point3D point3D = this.transform(d2, d3, d4);
        Point3D point3D2 = transform.transform(d2, d3, d4);
        return point3D.distance(point3D2);
    }

    public boolean similarTo(Transform transform, Bounds bounds, double d2) {
        double d3;
        double d4;
        if (this.isType2D() && transform.isType2D()) {
            double d5;
            double d6 = bounds.getMinX();
            if (this.transformDiff(transform, d6, d5 = bounds.getMinY()) > d2) {
                return false;
            }
            d5 = bounds.getMaxY();
            if (this.transformDiff(transform, d6, d5) > d2) {
                return false;
            }
            d6 = bounds.getMaxX();
            if (this.transformDiff(transform, d6, d5 = bounds.getMinY()) > d2) {
                return false;
            }
            d5 = bounds.getMaxY();
            return !(this.transformDiff(transform, d6, d5) > d2);
        }
        double d7 = bounds.getMinX();
        if (this.transformDiff(transform, d7, d4 = bounds.getMinY(), d3 = bounds.getMinZ()) > d2) {
            return false;
        }
        d4 = bounds.getMaxY();
        if (this.transformDiff(transform, d7, d4, d3) > d2) {
            return false;
        }
        d7 = bounds.getMaxX();
        if (this.transformDiff(transform, d7, d4 = bounds.getMinY(), d3) > d2) {
            return false;
        }
        d4 = bounds.getMaxY();
        if (this.transformDiff(transform, d7, d4, d3) > d2) {
            return false;
        }
        if (bounds.getDepth() != 0.0) {
            d7 = bounds.getMinX();
            if (this.transformDiff(transform, d7, d4 = bounds.getMinY(), d3 = bounds.getMaxZ()) > d2) {
                return false;
            }
            d4 = bounds.getMaxY();
            if (this.transformDiff(transform, d7, d4, d3) > d2) {
                return false;
            }
            d7 = bounds.getMaxX();
            if (this.transformDiff(transform, d7, d4 = bounds.getMinY(), d3) > d2) {
                return false;
            }
            d4 = bounds.getMaxY();
            if (this.transformDiff(transform, d7, d4, d3) > d2) {
                return false;
            }
        }
        return true;
    }

    void fill2DArray(double[] arrd) {
        arrd[0] = this.getMxx();
        arrd[1] = this.getMxy();
        arrd[2] = this.getTx();
        arrd[3] = this.getMyx();
        arrd[4] = this.getMyy();
        arrd[5] = this.getTy();
    }

    void fill3DArray(double[] arrd) {
        arrd[0] = this.getMxx();
        arrd[1] = this.getMxy();
        arrd[2] = this.getMxz();
        arrd[3] = this.getTx();
        arrd[4] = this.getMyx();
        arrd[5] = this.getMyy();
        arrd[6] = this.getMyz();
        arrd[7] = this.getTy();
        arrd[8] = this.getMzx();
        arrd[9] = this.getMzy();
        arrd[10] = this.getMzz();
        arrd[11] = this.getTz();
    }

    public double[] toArray(MatrixType matrixType, double[] arrd) {
        this.checkRequestedMAT(matrixType);
        if (arrd == null || arrd.length < matrixType.elements()) {
            arrd = new double[matrixType.elements()];
        }
        switch (matrixType) {
            case MT_2D_3x3: {
                arrd[6] = 0.0;
                arrd[7] = 0.0;
                arrd[8] = 1.0;
            }
            case MT_2D_2x3: {
                this.fill2DArray(arrd);
                break;
            }
            case MT_3D_4x4: {
                arrd[12] = 0.0;
                arrd[13] = 0.0;
                arrd[14] = 0.0;
                arrd[15] = 1.0;
            }
            case MT_3D_3x4: {
                this.fill3DArray(arrd);
                break;
            }
            default: {
                throw new InternalError("Unsupported matrix type " + (Object)((Object)matrixType));
            }
        }
        return arrd;
    }

    public double[] toArray(MatrixType matrixType) {
        return this.toArray(matrixType, null);
    }

    public double[] row(MatrixType matrixType, int n2, double[] arrd) {
        this.checkRequestedMAT(matrixType);
        if (n2 < 0 || n2 >= matrixType.rows()) {
            throw new IndexOutOfBoundsException("Cannot get row " + n2 + " from " + (Object)((Object)matrixType));
        }
        if (arrd == null || arrd.length < matrixType.columns()) {
            arrd = new double[matrixType.columns()];
        }
        block0 : switch (matrixType) {
            case MT_2D_2x3: 
            case MT_2D_3x3: {
                switch (n2) {
                    case 0: {
                        arrd[0] = this.getMxx();
                        arrd[1] = this.getMxy();
                        arrd[2] = this.getTx();
                        break block0;
                    }
                    case 1: {
                        arrd[0] = this.getMyx();
                        arrd[1] = this.getMyy();
                        arrd[2] = this.getTy();
                        break block0;
                    }
                    case 2: {
                        arrd[0] = 0.0;
                        arrd[1] = 0.0;
                        arrd[2] = 1.0;
                    }
                }
                break;
            }
            case MT_3D_3x4: 
            case MT_3D_4x4: {
                switch (n2) {
                    case 0: {
                        arrd[0] = this.getMxx();
                        arrd[1] = this.getMxy();
                        arrd[2] = this.getMxz();
                        arrd[3] = this.getTx();
                        break block0;
                    }
                    case 1: {
                        arrd[0] = this.getMyx();
                        arrd[1] = this.getMyy();
                        arrd[2] = this.getMyz();
                        arrd[3] = this.getTy();
                        break block0;
                    }
                    case 2: {
                        arrd[0] = this.getMzx();
                        arrd[1] = this.getMzy();
                        arrd[2] = this.getMzz();
                        arrd[3] = this.getTz();
                        break block0;
                    }
                    case 3: {
                        arrd[0] = 0.0;
                        arrd[1] = 0.0;
                        arrd[2] = 0.0;
                        arrd[3] = 1.0;
                    }
                }
                break;
            }
            default: {
                throw new InternalError("Unsupported row " + n2 + " of " + (Object)((Object)matrixType));
            }
        }
        return arrd;
    }

    public double[] row(MatrixType matrixType, int n2) {
        return this.row(matrixType, n2, null);
    }

    public double[] column(MatrixType matrixType, int n2, double[] arrd) {
        this.checkRequestedMAT(matrixType);
        if (n2 < 0 || n2 >= matrixType.columns()) {
            throw new IndexOutOfBoundsException("Cannot get row " + n2 + " from " + (Object)((Object)matrixType));
        }
        if (arrd == null || arrd.length < matrixType.rows()) {
            arrd = new double[matrixType.rows()];
        }
        block0 : switch (matrixType) {
            case MT_2D_2x3: {
                switch (n2) {
                    case 0: {
                        arrd[0] = this.getMxx();
                        arrd[1] = this.getMyx();
                        break block0;
                    }
                    case 1: {
                        arrd[0] = this.getMxy();
                        arrd[1] = this.getMyy();
                        break block0;
                    }
                    case 2: {
                        arrd[0] = this.getTx();
                        arrd[1] = this.getTy();
                    }
                }
                break;
            }
            case MT_2D_3x3: {
                switch (n2) {
                    case 0: {
                        arrd[0] = this.getMxx();
                        arrd[1] = this.getMyx();
                        arrd[2] = 0.0;
                        break block0;
                    }
                    case 1: {
                        arrd[0] = this.getMxy();
                        arrd[1] = this.getMyy();
                        arrd[2] = 0.0;
                        break block0;
                    }
                    case 2: {
                        arrd[0] = this.getTx();
                        arrd[1] = this.getTy();
                        arrd[2] = 1.0;
                    }
                }
                break;
            }
            case MT_3D_3x4: {
                switch (n2) {
                    case 0: {
                        arrd[0] = this.getMxx();
                        arrd[1] = this.getMyx();
                        arrd[2] = this.getMzx();
                        break block0;
                    }
                    case 1: {
                        arrd[0] = this.getMxy();
                        arrd[1] = this.getMyy();
                        arrd[2] = this.getMzy();
                        break block0;
                    }
                    case 2: {
                        arrd[0] = this.getMxz();
                        arrd[1] = this.getMyz();
                        arrd[2] = this.getMzz();
                        break block0;
                    }
                    case 3: {
                        arrd[0] = this.getTx();
                        arrd[1] = this.getTy();
                        arrd[2] = this.getTz();
                    }
                }
                break;
            }
            case MT_3D_4x4: {
                switch (n2) {
                    case 0: {
                        arrd[0] = this.getMxx();
                        arrd[1] = this.getMyx();
                        arrd[2] = this.getMzx();
                        arrd[3] = 0.0;
                        break block0;
                    }
                    case 1: {
                        arrd[0] = this.getMxy();
                        arrd[1] = this.getMyy();
                        arrd[2] = this.getMzy();
                        arrd[3] = 0.0;
                        break block0;
                    }
                    case 2: {
                        arrd[0] = this.getMxz();
                        arrd[1] = this.getMyz();
                        arrd[2] = this.getMzz();
                        arrd[3] = 0.0;
                        break block0;
                    }
                    case 3: {
                        arrd[0] = this.getTx();
                        arrd[1] = this.getTy();
                        arrd[2] = this.getTz();
                        arrd[3] = 1.0;
                    }
                }
                break;
            }
            default: {
                throw new InternalError("Unsupported column " + n2 + " of " + (Object)((Object)matrixType));
            }
        }
        return arrd;
    }

    public double[] column(MatrixType matrixType, int n2) {
        return this.column(matrixType, n2, null);
    }

    public Transform createConcatenation(Transform transform) {
        double d2 = transform.getMxx();
        double d3 = transform.getMxy();
        double d4 = transform.getMxz();
        double d5 = transform.getTx();
        double d6 = transform.getMyx();
        double d7 = transform.getMyy();
        double d8 = transform.getMyz();
        double d9 = transform.getTy();
        double d10 = transform.getMzx();
        double d11 = transform.getMzy();
        double d12 = transform.getMzz();
        double d13 = transform.getTz();
        return new Affine(this.getMxx() * d2 + this.getMxy() * d6 + this.getMxz() * d10, this.getMxx() * d3 + this.getMxy() * d7 + this.getMxz() * d11, this.getMxx() * d4 + this.getMxy() * d8 + this.getMxz() * d12, this.getMxx() * d5 + this.getMxy() * d9 + this.getMxz() * d13 + this.getTx(), this.getMyx() * d2 + this.getMyy() * d6 + this.getMyz() * d10, this.getMyx() * d3 + this.getMyy() * d7 + this.getMyz() * d11, this.getMyx() * d4 + this.getMyy() * d8 + this.getMyz() * d12, this.getMyx() * d5 + this.getMyy() * d9 + this.getMyz() * d13 + this.getTy(), this.getMzx() * d2 + this.getMzy() * d6 + this.getMzz() * d10, this.getMzx() * d3 + this.getMzy() * d7 + this.getMzz() * d11, this.getMzx() * d4 + this.getMzy() * d8 + this.getMzz() * d12, this.getMzx() * d5 + this.getMzy() * d9 + this.getMzz() * d13 + this.getTz());
    }

    public Transform createInverse() throws NonInvertibleTransformException {
        return this.getInverseCache().clone();
    }

    public Transform clone() {
        return TransformUtils.immutableTransform(this);
    }

    public Point2D transform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(this.getMxx() * d2 + this.getMxy() * d3 + this.getTx(), this.getMyx() * d2 + this.getMyy() * d3 + this.getTy());
    }

    public Point2D transform(Point2D point2D) {
        return this.transform(point2D.getX(), point2D.getY());
    }

    public Point3D transform(double d2, double d3, double d4) {
        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3 + this.getMxz() * d4 + this.getTx(), this.getMyx() * d2 + this.getMyy() * d3 + this.getMyz() * d4 + this.getTy(), this.getMzx() * d2 + this.getMzy() * d3 + this.getMzz() * d4 + this.getTz());
    }

    public Point3D transform(Point3D point3D) {
        return this.transform(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Bounds transform(Bounds bounds) {
        if (this.isType2D() && bounds.getMinZ() == 0.0 && bounds.getMaxZ() == 0.0) {
            Point2D point2D = this.transform(bounds.getMinX(), bounds.getMinY());
            Point2D point2D2 = this.transform(bounds.getMaxX(), bounds.getMinY());
            Point2D point2D3 = this.transform(bounds.getMaxX(), bounds.getMaxY());
            Point2D point2D4 = this.transform(bounds.getMinX(), bounds.getMaxY());
            return BoundsUtils.createBoundingBox(point2D, point2D2, point2D3, point2D4);
        }
        Point3D point3D = this.transform(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ());
        Point3D point3D2 = this.transform(bounds.getMinX(), bounds.getMinY(), bounds.getMaxZ());
        Point3D point3D3 = this.transform(bounds.getMinX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D point3D4 = this.transform(bounds.getMinX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D point3D5 = this.transform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D point3D6 = this.transform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D point3D7 = this.transform(bounds.getMaxX(), bounds.getMinY(), bounds.getMinZ());
        Point3D point3D8 = this.transform(bounds.getMaxX(), bounds.getMinY(), bounds.getMaxZ());
        return BoundsUtils.createBoundingBox(point3D, point3D2, point3D3, point3D4, point3D5, point3D6, point3D7, point3D8);
    }

    void transform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
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
    }

    void transform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        double d2 = this.getMxx();
        double d3 = this.getMxy();
        double d4 = this.getMxz();
        double d5 = this.getTx();
        double d6 = this.getMyx();
        double d7 = this.getMyy();
        double d8 = this.getMyz();
        double d9 = this.getTy();
        double d10 = this.getMzx();
        double d11 = this.getMzy();
        double d12 = this.getMzz();
        double d13 = this.getTz();
        while (--n4 >= 0) {
            double d14 = arrd[n2++];
            double d15 = arrd[n2++];
            double d16 = arrd[n2++];
            arrd2[n3++] = d2 * d14 + d3 * d15 + d4 * d16 + d5;
            arrd2[n3++] = d6 * d14 + d7 * d15 + d8 * d16 + d9;
            arrd2[n3++] = d10 * d14 + d11 * d15 + d12 * d16 + d13;
        }
    }

    public void transform2DPoints(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        if (arrd == null || arrd2 == null) {
            throw new NullPointerException();
        }
        if (!this.isType2D()) {
            throw new IllegalStateException("Cannot transform 2D points with a 3D transform");
        }
        n2 = this.getFixedSrcOffset(arrd, n2, arrd2, n3, n4, 2);
        this.transform2DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    public void transform3DPoints(double[] arrd, int n2, double[] arrd2, int n3, int n4) {
        if (arrd == null || arrd2 == null) {
            throw new NullPointerException();
        }
        n2 = this.getFixedSrcOffset(arrd, n2, arrd2, n3, n4, 3);
        this.transform3DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    public Point2D deltaTransform(double d2, double d3) {
        this.ensureCanTransform2DPoint();
        return new Point2D(this.getMxx() * d2 + this.getMxy() * d3, this.getMyx() * d2 + this.getMyy() * d3);
    }

    public Point2D deltaTransform(Point2D point2D) {
        return this.deltaTransform(point2D.getX(), point2D.getY());
    }

    public Point3D deltaTransform(double d2, double d3, double d4) {
        return new Point3D(this.getMxx() * d2 + this.getMxy() * d3 + this.getMxz() * d4, this.getMyx() * d2 + this.getMyy() * d3 + this.getMyz() * d4, this.getMzx() * d2 + this.getMzy() * d3 + this.getMzz() * d4);
    }

    public Point3D deltaTransform(Point3D point3D) {
        return this.deltaTransform(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Point2D inverseTransform(double d2, double d3) throws NonInvertibleTransformException {
        this.ensureCanTransform2DPoint();
        return this.getInverseCache().transform(d2, d3);
    }

    public Point2D inverseTransform(Point2D point2D) throws NonInvertibleTransformException {
        return this.inverseTransform(point2D.getX(), point2D.getY());
    }

    public Point3D inverseTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        return this.getInverseCache().transform(d2, d3, d4);
    }

    public Point3D inverseTransform(Point3D point3D) throws NonInvertibleTransformException {
        return this.inverseTransform(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Bounds inverseTransform(Bounds bounds) throws NonInvertibleTransformException {
        if (this.isType2D() && bounds.getMinZ() == 0.0 && bounds.getMaxZ() == 0.0) {
            Point2D point2D = this.inverseTransform(bounds.getMinX(), bounds.getMinY());
            Point2D point2D2 = this.inverseTransform(bounds.getMaxX(), bounds.getMinY());
            Point2D point2D3 = this.inverseTransform(bounds.getMaxX(), bounds.getMaxY());
            Point2D point2D4 = this.inverseTransform(bounds.getMinX(), bounds.getMaxY());
            return BoundsUtils.createBoundingBox(point2D, point2D2, point2D3, point2D4);
        }
        Point3D point3D = this.inverseTransform(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ());
        Point3D point3D2 = this.inverseTransform(bounds.getMinX(), bounds.getMinY(), bounds.getMaxZ());
        Point3D point3D3 = this.inverseTransform(bounds.getMinX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D point3D4 = this.inverseTransform(bounds.getMinX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D point3D5 = this.inverseTransform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMinZ());
        Point3D point3D6 = this.inverseTransform(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ());
        Point3D point3D7 = this.inverseTransform(bounds.getMaxX(), bounds.getMinY(), bounds.getMinZ());
        Point3D point3D8 = this.inverseTransform(bounds.getMaxX(), bounds.getMinY(), bounds.getMaxZ());
        return BoundsUtils.createBoundingBox(point3D, point3D2, point3D3, point3D4, point3D5, point3D6, point3D7, point3D8);
    }

    void inverseTransform2DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        this.getInverseCache().transform2DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    void inverseTransform3DPointsImpl(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        this.getInverseCache().transform3DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    public void inverseTransform2DPoints(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        if (arrd == null || arrd2 == null) {
            throw new NullPointerException();
        }
        if (!this.isType2D()) {
            throw new IllegalStateException("Cannot transform 2D points with a 3D transform");
        }
        n2 = this.getFixedSrcOffset(arrd, n2, arrd2, n3, n4, 2);
        this.inverseTransform2DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    public void inverseTransform3DPoints(double[] arrd, int n2, double[] arrd2, int n3, int n4) throws NonInvertibleTransformException {
        if (arrd == null || arrd2 == null) {
            throw new NullPointerException();
        }
        n2 = this.getFixedSrcOffset(arrd, n2, arrd2, n3, n4, 3);
        this.inverseTransform3DPointsImpl(arrd, n2, arrd2, n3, n4);
    }

    public Point2D inverseDeltaTransform(double d2, double d3) throws NonInvertibleTransformException {
        this.ensureCanTransform2DPoint();
        return this.getInverseCache().deltaTransform(d2, d3);
    }

    public Point2D inverseDeltaTransform(Point2D point2D) throws NonInvertibleTransformException {
        return this.inverseDeltaTransform(point2D.getX(), point2D.getY());
    }

    public Point3D inverseDeltaTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
        return this.getInverseCache().deltaTransform(d2, d3, d4);
    }

    public Point3D inverseDeltaTransform(Point3D point3D) throws NonInvertibleTransformException {
        return this.inverseDeltaTransform(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    private int getFixedSrcOffset(double[] arrd, int n2, double[] arrd2, int n3, int n4, int n5) {
        if (arrd2 == arrd && n3 > n2 && n3 < n2 + n4 * n5) {
            System.arraycopy(arrd, n2, arrd2, n3, n4 * n5);
            return n3;
        }
        return n2;
    }

    private EventHandlerManager getInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = new EventHandlerManager(this);
        }
        return this.internalEventDispatcher;
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain eventDispatchChain) {
        return this.internalEventDispatcher == null ? eventDispatchChain : eventDispatchChain.append(this.getInternalEventDispatcher());
    }

    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().addEventHandler(eventType, eventHandler);
        this.validate();
    }

    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().removeEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().addEventFilter(eventType, eventHandler);
        this.validate();
    }

    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().removeEventFilter(eventType, eventHandler);
    }

    public final void setOnTransformChanged(EventHandler<? super TransformChangedEvent> eventHandler) {
        this.onTransformChangedProperty().set(eventHandler);
        this.validate();
    }

    public final EventHandler<? super TransformChangedEvent> getOnTransformChanged() {
        return this.onTransformChanged == null ? null : (EventHandler)this.onTransformChanged.get();
    }

    public final ObjectProperty<EventHandler<? super TransformChangedEvent>> onTransformChangedProperty() {
        if (this.onTransformChanged == null) {
            this.onTransformChanged = new SimpleObjectProperty<EventHandler<? super TransformChangedEvent>>((Object)this, "onTransformChanged"){

                @Override
                protected void invalidated() {
                    Transform.this.getInternalEventDispatcher().setEventHandler(TransformChangedEvent.TRANSFORM_CHANGED, (EventHandler)this.get());
                }
            };
        }
        return this.onTransformChanged;
    }

    void checkRequestedMAT(MatrixType matrixType) throws IllegalArgumentException {
        if (matrixType.is2D() && !this.isType2D()) {
            throw new IllegalArgumentException("Cannot access 2D matrix for a 3D transform");
        }
    }

    void ensureCanTransform2DPoint() throws IllegalStateException {
        if (!this.isType2D()) {
            throw new IllegalStateException("Cannot transform 2D point with a 3D transform");
        }
    }

    void validate() {
        this.getMxx();
        this.getMxy();
        this.getMxz();
        this.getTx();
        this.getMyx();
        this.getMyy();
        this.getMyz();
        this.getTy();
        this.getMzx();
        this.getMzy();
        this.getMzz();
        this.getTz();
    }

    @Deprecated
    public abstract void impl_apply(Affine3D var1);

    @Deprecated
    public abstract BaseTransform impl_derive(BaseTransform var1);

    @Deprecated
    public void impl_add(Node node) {
        this.impl_nodes.add(node);
    }

    @Deprecated
    public void impl_remove(Node node) {
        this.impl_nodes.remove(node);
    }

    protected void transformChanged() {
        this.inverseCache = null;
        Iterator iterator = this.impl_nodes.iterator();
        while (iterator.hasNext()) {
            ((Node)iterator.next()).impl_transformsChanged();
        }
        if (this.type2D != null) {
            this.type2D.invalidate();
        }
        if (this.identity != null) {
            this.identity.invalidate();
        }
        if (this.internalEventDispatcher != null) {
            this.validate();
            Event.fireEvent(this, new TransformChangedEvent(this, this));
        }
    }

    void appendTo(Affine affine) {
        affine.append(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
    }

    void prependTo(Affine affine) {
        affine.prepend(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
    }

    private Transform getInverseCache() throws NonInvertibleTransformException {
        if (this.inverseCache == null || this.inverseCache.get() == null) {
            Affine affine = new Affine(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
            affine.invert();
            this.inverseCache = new SoftReference<Affine>(affine);
            return affine;
        }
        return this.inverseCache.get();
    }

    void clearInverseCache() {
        if (this.inverseCache != null) {
            this.inverseCache.clear();
        }
    }

    private static abstract class LazyBooleanProperty
    extends ReadOnlyBooleanProperty {
        private ExpressionHelper<Boolean> helper;
        private boolean valid;
        private boolean value;

        private LazyBooleanProperty() {
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, invalidationListener);
        }

        @Override
        public void addListener(ChangeListener<? super Boolean> changeListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
        }

        @Override
        public void removeListener(ChangeListener<? super Boolean> changeListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
        }

        @Override
        public boolean get() {
            if (!this.valid) {
                this.value = this.computeValue();
                this.valid = true;
            }
            return this.value;
        }

        public void invalidate() {
            if (this.valid) {
                this.valid = false;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }

        protected abstract boolean computeValue();
    }
}

