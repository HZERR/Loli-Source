/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class TransformUtils {
    public static Transform immutableTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        return new ImmutableTransform(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
    }

    public static Transform immutableTransform(Transform transform) {
        return new ImmutableTransform(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    public static Transform immutableTransform(Transform transform, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (transform == null) {
            return new ImmutableTransform(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        }
        ((ImmutableTransform)transform).setToTransform(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        return transform;
    }

    public static Transform immutableTransform(Transform transform, Transform transform2) {
        return TransformUtils.immutableTransform((ImmutableTransform)transform, transform2.getMxx(), transform2.getMxy(), transform2.getMxz(), transform2.getTx(), transform2.getMyx(), transform2.getMyy(), transform2.getMyz(), transform2.getTy(), transform2.getMzx(), transform2.getMzy(), transform2.getMzz(), transform2.getTz());
    }

    public static Transform immutableTransform(Transform transform, Transform transform2, Transform transform3) {
        if (transform == null) {
            transform = new ImmutableTransform();
        }
        ((ImmutableTransform)transform).setToConcatenation((ImmutableTransform)transform2, (ImmutableTransform)transform3);
        return transform;
    }

    static class ImmutableTransform
    extends Transform {
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

        public ImmutableTransform() {
            this.zz = 1.0;
            this.yy = 1.0;
            this.xx = 1.0;
        }

        public ImmutableTransform(Transform transform) {
            this(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
        }

        public ImmutableTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
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

        private void setToTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
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

        private void setToConcatenation(ImmutableTransform immutableTransform, ImmutableTransform immutableTransform2) {
            if (immutableTransform.state3d == 0 && immutableTransform2.state3d == 0) {
                this.xx = immutableTransform.xx * immutableTransform2.xx + immutableTransform.xy * immutableTransform2.yx;
                this.xy = immutableTransform.xx * immutableTransform2.xy + immutableTransform.xy * immutableTransform2.yy;
                this.xt = immutableTransform.xx * immutableTransform2.xt + immutableTransform.xy * immutableTransform2.yt + immutableTransform.xt;
                this.yx = immutableTransform.yx * immutableTransform2.xx + immutableTransform.yy * immutableTransform2.yx;
                this.yy = immutableTransform.yx * immutableTransform2.xy + immutableTransform.yy * immutableTransform2.yy;
                this.yt = immutableTransform.yx * immutableTransform2.xt + immutableTransform.yy * immutableTransform2.yt + immutableTransform.yt;
                if (this.state3d != 0) {
                    this.zt = 0.0;
                    this.zy = 0.0;
                    this.zx = 0.0;
                    this.yz = 0.0;
                    this.xz = 0.0;
                    this.zz = 1.0;
                    this.state3d = 0;
                }
                this.updateState2D();
            } else {
                this.xx = immutableTransform.xx * immutableTransform2.xx + immutableTransform.xy * immutableTransform2.yx + immutableTransform.xz * immutableTransform2.zx;
                this.xy = immutableTransform.xx * immutableTransform2.xy + immutableTransform.xy * immutableTransform2.yy + immutableTransform.xz * immutableTransform2.zy;
                this.xz = immutableTransform.xx * immutableTransform2.xz + immutableTransform.xy * immutableTransform2.yz + immutableTransform.xz * immutableTransform2.zz;
                this.xt = immutableTransform.xx * immutableTransform2.xt + immutableTransform.xy * immutableTransform2.yt + immutableTransform.xz * immutableTransform2.zt + immutableTransform.xt;
                this.yx = immutableTransform.yx * immutableTransform2.xx + immutableTransform.yy * immutableTransform2.yx + immutableTransform.yz * immutableTransform2.zx;
                this.yy = immutableTransform.yx * immutableTransform2.xy + immutableTransform.yy * immutableTransform2.yy + immutableTransform.yz * immutableTransform2.zy;
                this.yz = immutableTransform.yx * immutableTransform2.xz + immutableTransform.yy * immutableTransform2.yz + immutableTransform.yz * immutableTransform2.zz;
                this.yt = immutableTransform.yx * immutableTransform2.xt + immutableTransform.yy * immutableTransform2.yt + immutableTransform.yz * immutableTransform2.zt + immutableTransform.yt;
                this.zx = immutableTransform.zx * immutableTransform2.xx + immutableTransform.zy * immutableTransform2.yx + immutableTransform.zz * immutableTransform2.zx;
                this.zy = immutableTransform.zx * immutableTransform2.xy + immutableTransform.zy * immutableTransform2.yy + immutableTransform.zz * immutableTransform2.zy;
                this.zz = immutableTransform.zx * immutableTransform2.xz + immutableTransform.zy * immutableTransform2.yz + immutableTransform.zz * immutableTransform2.zz;
                this.zt = immutableTransform.zx * immutableTransform2.xt + immutableTransform.zy * immutableTransform2.yt + immutableTransform.zz * immutableTransform2.zt + immutableTransform.zt;
                this.updateState();
            }
        }

        @Override
        public double getMxx() {
            return this.xx;
        }

        @Override
        public double getMxy() {
            return this.xy;
        }

        @Override
        public double getMxz() {
            return this.xz;
        }

        @Override
        public double getTx() {
            return this.xt;
        }

        @Override
        public double getMyx() {
            return this.yx;
        }

        @Override
        public double getMyy() {
            return this.yy;
        }

        @Override
        public double getMyz() {
            return this.yz;
        }

        @Override
        public double getTy() {
            return this.yt;
        }

        @Override
        public double getMzx() {
            return this.zx;
        }

        @Override
        public double getMzy() {
            return this.zy;
        }

        @Override
        public double getMzz() {
            return this.zz;
        }

        @Override
        public double getTz() {
            return this.zt;
        }

        @Override
        public double determinant() {
            switch (this.state3d) {
                default: {
                    ImmutableTransform.stateError();
                }
                case 0: {
                    switch (this.state2d) {
                        default: {
                            ImmutableTransform.stateError();
                        }
                        case 6: 
                        case 7: {
                            return this.xx * this.yy - this.xy * this.yx;
                        }
                        case 4: 
                        case 5: {
                            return -(this.xy * this.yx);
                        }
                        case 2: 
                        case 3: {
                            return this.xx * this.yy;
                        }
                        case 0: 
                        case 1: 
                    }
                    return 1.0;
                }
                case 1: {
                    return 1.0;
                }
                case 2: 
                case 3: {
                    return this.xx * this.yy * this.zz;
                }
                case 4: 
            }
            return this.xx * (this.yy * this.zz - this.zy * this.yz) + this.xy * (this.yz * this.zx - this.zz * this.yx) + this.xz * (this.yx * this.zy - this.zx * this.yy);
        }

        @Override
        public Transform createConcatenation(Transform transform) {
            Affine affine = new Affine(this);
            affine.append(transform);
            return affine;
        }

        @Override
        public Affine createInverse() throws NonInvertibleTransformException {
            Affine affine = new Affine(this);
            affine.invert();
            return affine;
        }

        @Override
        public Transform clone() {
            return new ImmutableTransform(this);
        }

        @Override
        public Point2D transform(double d2, double d3) {
            this.ensureCanTransform2DPoint();
            switch (this.state2d) {
                default: {
                    ImmutableTransform.stateError();
                }
                case 7: {
                    return new Point2D(this.xx * d2 + this.xy * d3 + this.xt, this.yx * d2 + this.yy * d3 + this.yt);
                }
                case 6: {
                    return new Point2D(this.xx * d2 + this.xy * d3, this.yx * d2 + this.yy * d3);
                }
                case 5: {
                    return new Point2D(this.xy * d3 + this.xt, this.yx * d2 + this.yt);
                }
                case 4: {
                    return new Point2D(this.xy * d3, this.yx * d2);
                }
                case 3: {
                    return new Point2D(this.xx * d2 + this.xt, this.yy * d3 + this.yt);
                }
                case 2: {
                    return new Point2D(this.xx * d2, this.yy * d3);
                }
                case 1: {
                    return new Point2D(d2 + this.xt, d3 + this.yt);
                }
                case 0: 
            }
            return new Point2D(d2, d3);
        }

        @Override
        public Point3D transform(double d2, double d3, double d4) {
            switch (this.state3d) {
                default: {
                    ImmutableTransform.stateError();
                }
                case 0: {
                    switch (this.state2d) {
                        default: {
                            ImmutableTransform.stateError();
                        }
                        case 7: {
                            return new Point3D(this.xx * d2 + this.xy * d3 + this.xt, this.yx * d2 + this.yy * d3 + this.yt, d4);
                        }
                        case 6: {
                            return new Point3D(this.xx * d2 + this.xy * d3, this.yx * d2 + this.yy * d3, d4);
                        }
                        case 5: {
                            return new Point3D(this.xy * d3 + this.xt, this.yx * d2 + this.yt, d4);
                        }
                        case 4: {
                            return new Point3D(this.xy * d3, this.yx * d2, d4);
                        }
                        case 3: {
                            return new Point3D(this.xx * d2 + this.xt, this.yy * d3 + this.yt, d4);
                        }
                        case 2: {
                            return new Point3D(this.xx * d2, this.yy * d3, d4);
                        }
                        case 1: {
                            return new Point3D(d2 + this.xt, d3 + this.yt, d4);
                        }
                        case 0: 
                    }
                    return new Point3D(d2, d3, d4);
                }
                case 1: {
                    return new Point3D(d2 + this.xt, d3 + this.yt, d4 + this.zt);
                }
                case 2: {
                    return new Point3D(this.xx * d2, this.yy * d3, this.zz * d4);
                }
                case 3: {
                    return new Point3D(this.xx * d2 + this.xt, this.yy * d3 + this.yt, this.zz * d4 + this.zt);
                }
                case 4: 
            }
            return new Point3D(this.xx * d2 + this.xy * d3 + this.xz * d4 + this.xt, this.yx * d2 + this.yy * d3 + this.yz * d4 + this.yt, this.zx * d2 + this.zy * d3 + this.zz * d4 + this.zt);
        }

        @Override
        public Point2D deltaTransform(double d2, double d3) {
            this.ensureCanTransform2DPoint();
            switch (this.state2d) {
                default: {
                    ImmutableTransform.stateError();
                }
                case 6: 
                case 7: {
                    return new Point2D(this.xx * d2 + this.xy * d3, this.yx * d2 + this.yy * d3);
                }
                case 4: 
                case 5: {
                    return new Point2D(this.xy * d3, this.yx * d2);
                }
                case 2: 
                case 3: {
                    return new Point2D(this.xx * d2, this.yy * d3);
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
                    ImmutableTransform.stateError();
                }
                case 0: {
                    switch (this.state2d) {
                        default: {
                            ImmutableTransform.stateError();
                        }
                        case 6: 
                        case 7: {
                            return new Point3D(this.xx * d2 + this.xy * d3, this.yx * d2 + this.yy * d3, d4);
                        }
                        case 4: 
                        case 5: {
                            return new Point3D(this.xy * d3, this.yx * d2, d4);
                        }
                        case 2: 
                        case 3: {
                            return new Point3D(this.xx * d2, this.yy * d3, d4);
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
                    return new Point3D(this.xx * d2, this.yy * d3, this.zz * d4);
                }
                case 4: 
            }
            return new Point3D(this.xx * d2 + this.xy * d3 + this.xz * d4, this.yx * d2 + this.yy * d3 + this.yz * d4, this.zx * d2 + this.zy * d3 + this.zz * d4);
        }

        @Override
        public Point2D inverseTransform(double d2, double d3) throws NonInvertibleTransformException {
            this.ensureCanTransform2DPoint();
            switch (this.state2d) {
                default: {
                    return super.inverseTransform(d2, d3);
                }
                case 5: {
                    if (this.xy == 0.0 || this.yx == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(1.0 / this.yx * d3 - this.yt / this.yx, 1.0 / this.xy * d2 - this.xt / this.xy);
                }
                case 4: {
                    if (this.xy == 0.0 || this.yx == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(1.0 / this.yx * d3, 1.0 / this.xy * d2);
                }
                case 3: {
                    if (this.xx == 0.0 || this.yy == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(1.0 / this.xx * d2 - this.xt / this.xx, 1.0 / this.yy * d3 - this.yt / this.yy);
                }
                case 2: {
                    if (this.xx == 0.0 || this.yy == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(1.0 / this.xx * d2, 1.0 / this.yy * d3);
                }
                case 1: {
                    return new Point2D(d2 - this.xt, d3 - this.yt);
                }
                case 0: 
            }
            return new Point2D(d2, d3);
        }

        @Override
        public Point3D inverseTransform(double d2, double d3, double d4) throws NonInvertibleTransformException {
            switch (this.state3d) {
                default: {
                    ImmutableTransform.stateError();
                }
                case 0: {
                    switch (this.state2d) {
                        default: {
                            return super.inverseTransform(d2, d3, d4);
                        }
                        case 5: {
                            if (this.xy == 0.0 || this.yx == 0.0) {
                                throw new NonInvertibleTransformException("Determinant is 0");
                            }
                            return new Point3D(1.0 / this.yx * d3 - this.yt / this.yx, 1.0 / this.xy * d2 - this.xt / this.xy, d4);
                        }
                        case 4: {
                            if (this.xy == 0.0 || this.yx == 0.0) {
                                throw new NonInvertibleTransformException("Determinant is 0");
                            }
                            return new Point3D(1.0 / this.yx * d3, 1.0 / this.xy * d2, d4);
                        }
                        case 3: {
                            if (this.xx == 0.0 || this.yy == 0.0) {
                                throw new NonInvertibleTransformException("Determinant is 0");
                            }
                            return new Point3D(1.0 / this.xx * d2 - this.xt / this.xx, 1.0 / this.yy * d3 - this.yt / this.yy, d4);
                        }
                        case 2: {
                            if (this.xx == 0.0 || this.yy == 0.0) {
                                throw new NonInvertibleTransformException("Determinant is 0");
                            }
                            return new Point3D(1.0 / this.xx * d2, 1.0 / this.yy * d3, d4);
                        }
                        case 1: {
                            return new Point3D(d2 - this.xt, d3 - this.yt, d4);
                        }
                        case 0: 
                    }
                    return new Point3D(d2, d3, d4);
                }
                case 1: {
                    return new Point3D(d2 - this.xt, d3 - this.yt, d4 - this.zt);
                }
                case 2: {
                    if (this.xx == 0.0 || this.yy == 0.0 || this.zz == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D(1.0 / this.xx * d2, 1.0 / this.yy * d3, 1.0 / this.zz * d4);
                }
                case 3: {
                    if (this.xx == 0.0 || this.yy == 0.0 || this.zz == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D(1.0 / this.xx * d2 - this.xt / this.xx, 1.0 / this.yy * d3 - this.yt / this.yy, 1.0 / this.zz * d4 - this.zt / this.zz);
                }
                case 4: 
            }
            return super.inverseTransform(d2, d3, d4);
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
                    if (this.xy == 0.0 || this.yx == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(1.0 / this.yx * d3, 1.0 / this.xy * d2);
                }
                case 2: 
                case 3: {
                    if (this.xx == 0.0 || this.yy == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point2D(1.0 / this.xx * d2, 1.0 / this.yy * d3);
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
                    ImmutableTransform.stateError();
                }
                case 0: {
                    switch (this.state2d) {
                        default: {
                            return super.inverseDeltaTransform(d2, d3, d4);
                        }
                        case 4: 
                        case 5: {
                            if (this.xy == 0.0 || this.yx == 0.0) {
                                throw new NonInvertibleTransformException("Determinant is 0");
                            }
                            return new Point3D(1.0 / this.yx * d3, 1.0 / this.xy * d2, d4);
                        }
                        case 2: 
                        case 3: {
                            if (this.xx == 0.0 || this.yy == 0.0) {
                                throw new NonInvertibleTransformException("Determinant is 0");
                            }
                            return new Point3D(1.0 / this.xx * d2, 1.0 / this.yy * d3, d4);
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
                    if (this.xx == 0.0 || this.yy == 0.0 || this.zz == 0.0) {
                        throw new NonInvertibleTransformException("Determinant is 0");
                    }
                    return new Point3D(1.0 / this.xx * d2, 1.0 / this.yy * d3, 1.0 / this.zz * d4);
                }
                case 4: 
            }
            return super.inverseDeltaTransform(d2, d3, d4);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("Transform [\n");
            stringBuilder.append("\t").append(this.xx);
            stringBuilder.append(", ").append(this.xy);
            stringBuilder.append(", ").append(this.xz);
            stringBuilder.append(", ").append(this.xt);
            stringBuilder.append('\n');
            stringBuilder.append("\t").append(this.yx);
            stringBuilder.append(", ").append(this.yy);
            stringBuilder.append(", ").append(this.yz);
            stringBuilder.append(", ").append(this.yt);
            stringBuilder.append('\n');
            stringBuilder.append("\t").append(this.zx);
            stringBuilder.append(", ").append(this.zy);
            stringBuilder.append(", ").append(this.zz);
            stringBuilder.append(", ").append(this.zt);
            return stringBuilder.append("\n]").toString();
        }

        private void updateState() {
            this.updateState2D();
            this.state3d = 0;
            if (this.xz != 0.0 || this.yz != 0.0 || this.zx != 0.0 || this.zy != 0.0) {
                this.state3d = 4;
            } else if ((this.state2d & 4) == 0) {
                if (this.zt != 0.0) {
                    this.state3d |= 1;
                }
                if (this.zz != 1.0) {
                    this.state3d |= 2;
                }
                if (this.state3d != 0) {
                    this.state3d |= this.state2d & 3;
                }
            } else if (this.zz != 1.0 || this.zt != 0.0) {
                this.state3d = 4;
            }
        }

        private void updateState2D() {
            this.state2d = this.xy == 0.0 && this.yx == 0.0 ? (this.xx == 1.0 && this.yy == 1.0 ? (this.xt == 0.0 && this.yt == 0.0 ? 0 : 1) : (this.xt == 0.0 && this.yt == 0.0 ? 2 : 3)) : (this.xx == 0.0 && this.yy == 0.0 ? (this.xt == 0.0 && this.yt == 0.0 ? 4 : 5) : (this.xt == 0.0 && this.yt == 0.0 ? 6 : 7));
        }

        void ensureCanTransform2DPoint() throws IllegalStateException {
            if (this.state3d != 0) {
                throw new IllegalStateException("Cannot transform 2D point with a 3D transform");
            }
        }

        private static void stateError() {
            throw new InternalError("missing case in a switch");
        }

        @Override
        @Deprecated
        public void impl_apply(Affine3D affine3D) {
            affine3D.concatenate(this.xx, this.xy, this.xz, this.xt, this.yx, this.yy, this.yz, this.yt, this.zx, this.zy, this.zz, this.zt);
        }

        @Override
        @Deprecated
        public BaseTransform impl_derive(BaseTransform baseTransform) {
            return baseTransform.deriveWithConcatenation(this.xx, this.xy, this.xz, this.xt, this.yx, this.yy, this.yz, this.yt, this.zx, this.zy, this.zz, this.zt);
        }

        int getState2d() {
            return this.state2d;
        }

        int getState3d() {
            return this.state3d;
        }
    }
}

