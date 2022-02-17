/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.CanTransformVec3d;
import com.sun.javafx.geom.transform.SingularMatrixException;
import com.sun.javafx.geom.transform.TransformHelper;

public final class GeneralTransform3D
implements CanTransformVec3d {
    protected double[] mat = new double[16];
    private boolean identity;
    private Vec3d tempV3d;
    private static final double EPSILON_ABSOLUTE = 1.0E-5;

    public GeneralTransform3D() {
        this.setIdentity();
    }

    public boolean isAffine() {
        return !this.isInfOrNaN() && GeneralTransform3D.almostZero(this.mat[12]) && GeneralTransform3D.almostZero(this.mat[13]) && GeneralTransform3D.almostZero(this.mat[14]) && GeneralTransform3D.almostOne(this.mat[15]);
    }

    public GeneralTransform3D set(GeneralTransform3D generalTransform3D) {
        System.arraycopy(generalTransform3D.mat, 0, this.mat, 0, this.mat.length);
        this.updateState();
        return this;
    }

    public GeneralTransform3D set(double[] arrd) {
        System.arraycopy(arrd, 0, this.mat, 0, this.mat.length);
        this.updateState();
        return this;
    }

    public double[] get(double[] arrd) {
        if (arrd == null) {
            arrd = new double[this.mat.length];
        }
        System.arraycopy(this.mat, 0, arrd, 0, this.mat.length);
        return arrd;
    }

    public double get(int n2) {
        assert (n2 >= 0 && n2 < this.mat.length);
        return this.mat[n2];
    }

    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (this.tempV3d == null) {
            this.tempV3d = new Vec3d();
        }
        return TransformHelper.general3dBoundsTransform(this, baseBounds, baseBounds2, this.tempV3d);
    }

    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D();
        }
        double d2 = (float)(this.mat[12] * (double)point2D.x + this.mat[13] * (double)point2D.y + this.mat[15]);
        point2D2.x = (float)(this.mat[0] * (double)point2D.x + this.mat[1] * (double)point2D.y + this.mat[3]);
        point2D2.y = (float)(this.mat[4] * (double)point2D.x + this.mat[5] * (double)point2D.y + this.mat[7]);
        point2D2.x = (float)((double)point2D2.x / d2);
        point2D2.y = (float)((double)point2D2.y / d2);
        return point2D2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d2 = (float)(this.mat[12] * vec3d.x + this.mat[13] * vec3d.y + this.mat[14] * vec3d.z + this.mat[15]);
        vec3d2.x = (float)(this.mat[0] * vec3d.x + this.mat[1] * vec3d.y + this.mat[2] * vec3d.z + this.mat[3]);
        vec3d2.y = (float)(this.mat[4] * vec3d.x + this.mat[5] * vec3d.y + this.mat[6] * vec3d.z + this.mat[7]);
        vec3d2.z = (float)(this.mat[8] * vec3d.x + this.mat[9] * vec3d.y + this.mat[10] * vec3d.z + this.mat[11]);
        if (d2 != 0.0) {
            vec3d2.x /= d2;
            vec3d2.y /= d2;
            vec3d2.z /= d2;
        }
        return vec3d2;
    }

    public Vec3d transform(Vec3d vec3d) {
        return this.transform(vec3d, vec3d);
    }

    public Vec3f transformNormal(Vec3f vec3f, Vec3f vec3f2) {
        vec3f.x = (float)(this.mat[0] * (double)vec3f.x + this.mat[1] * (double)vec3f.y + this.mat[2] * (double)vec3f.z);
        vec3f.y = (float)(this.mat[4] * (double)vec3f.x + this.mat[5] * (double)vec3f.y + this.mat[6] * (double)vec3f.z);
        vec3f.z = (float)(this.mat[8] * (double)vec3f.x + this.mat[9] * (double)vec3f.y + this.mat[10] * (double)vec3f.z);
        return vec3f2;
    }

    public Vec3f transformNormal(Vec3f vec3f) {
        return this.transformNormal(vec3f, vec3f);
    }

    public GeneralTransform3D perspective(boolean bl, double d2, double d3, double d4, double d5) {
        double d6 = d2 * 0.5;
        double d7 = d5 - d4;
        double d8 = Math.sin(d6);
        double d9 = Math.cos(d6) / d8;
        this.mat[0] = bl ? d9 / d3 : d9;
        this.mat[5] = bl ? d9 : d9 * d3;
        this.mat[10] = -(d5 + d4) / d7;
        this.mat[11] = -2.0 * d4 * d5 / d7;
        this.mat[14] = -1.0;
        this.mat[15] = 0.0;
        this.mat[13] = 0.0;
        this.mat[12] = 0.0;
        this.mat[9] = 0.0;
        this.mat[8] = 0.0;
        this.mat[7] = 0.0;
        this.mat[6] = 0.0;
        this.mat[4] = 0.0;
        this.mat[3] = 0.0;
        this.mat[2] = 0.0;
        this.mat[1] = 0.0;
        this.updateState();
        return this;
    }

    public GeneralTransform3D ortho(double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8 = 1.0 / (d3 - d2);
        double d9 = 1.0 / (d5 - d4);
        double d10 = 1.0 / (d7 - d6);
        this.mat[0] = 2.0 * d8;
        this.mat[3] = -(d3 + d2) * d8;
        this.mat[5] = 2.0 * d9;
        this.mat[7] = -(d5 + d4) * d9;
        this.mat[10] = 2.0 * d10;
        this.mat[11] = (d7 + d6) * d10;
        this.mat[14] = 0.0;
        this.mat[13] = 0.0;
        this.mat[12] = 0.0;
        this.mat[9] = 0.0;
        this.mat[8] = 0.0;
        this.mat[6] = 0.0;
        this.mat[4] = 0.0;
        this.mat[2] = 0.0;
        this.mat[1] = 0.0;
        this.mat[15] = 1.0;
        this.updateState();
        return this;
    }

    public double computeClipZCoord() {
        double d2 = (1.0 - this.mat[15]) / this.mat[14];
        double d3 = this.mat[10] * d2 + this.mat[11];
        return d3;
    }

    public double determinant() {
        return this.mat[0] * (this.mat[5] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) - this.mat[6] * (this.mat[9] * this.mat[15] - this.mat[11] * this.mat[13]) + this.mat[7] * (this.mat[9] * this.mat[14] - this.mat[10] * this.mat[13])) - this.mat[1] * (this.mat[4] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) - this.mat[6] * (this.mat[8] * this.mat[15] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[8] * this.mat[14] - this.mat[10] * this.mat[12])) + this.mat[2] * (this.mat[4] * (this.mat[9] * this.mat[15] - this.mat[11] * this.mat[13]) - this.mat[5] * (this.mat[8] * this.mat[15] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[8] * this.mat[13] - this.mat[9] * this.mat[12])) - this.mat[3] * (this.mat[4] * (this.mat[9] * this.mat[14] - this.mat[10] * this.mat[13]) - this.mat[5] * (this.mat[8] * this.mat[14] - this.mat[10] * this.mat[12]) + this.mat[6] * (this.mat[8] * this.mat[13] - this.mat[9] * this.mat[12]));
    }

    public GeneralTransform3D invert() {
        return this.invert(this);
    }

    private GeneralTransform3D invert(GeneralTransform3D generalTransform3D) {
        double[] arrd = new double[16];
        int[] arrn = new int[4];
        System.arraycopy(generalTransform3D.mat, 0, arrd, 0, arrd.length);
        if (!GeneralTransform3D.luDecomposition(arrd, arrn)) {
            throw new SingularMatrixException();
        }
        this.mat[0] = 1.0;
        this.mat[1] = 0.0;
        this.mat[2] = 0.0;
        this.mat[3] = 0.0;
        this.mat[4] = 0.0;
        this.mat[5] = 1.0;
        this.mat[6] = 0.0;
        this.mat[7] = 0.0;
        this.mat[8] = 0.0;
        this.mat[9] = 0.0;
        this.mat[10] = 1.0;
        this.mat[11] = 0.0;
        this.mat[12] = 0.0;
        this.mat[13] = 0.0;
        this.mat[14] = 0.0;
        this.mat[15] = 1.0;
        GeneralTransform3D.luBacksubstitution(arrd, arrn, this.mat);
        this.updateState();
        return this;
    }

    private static boolean luDecomposition(double[] arrd, int[] arrn) {
        int n2;
        double[] arrd2 = new double[4];
        int n3 = 0;
        int n4 = 0;
        int n5 = 4;
        while (n5-- != 0) {
            double d2 = 0.0;
            n2 = 4;
            while (n2-- != 0) {
                double d3 = arrd[n3++];
                if (!((d3 = Math.abs(d3)) > d2)) continue;
                d2 = d3;
            }
            if (d2 == 0.0) {
                return false;
            }
            arrd2[n4++] = 1.0 / d2;
        }
        n2 = 0;
        for (n5 = 0; n5 < 4; ++n5) {
            double d4;
            int n6;
            int n7;
            int n8;
            double d5;
            int n9;
            for (n3 = 0; n3 < n5; ++n3) {
                n9 = n2 + 4 * n3 + n5;
                d5 = arrd[n9];
                int n10 = n3;
                int n11 = n2 + 4 * n3;
                n8 = n2 + n5;
                while (n10-- != 0) {
                    d5 -= arrd[n11] * arrd[n8];
                    ++n11;
                    n8 += 4;
                }
                arrd[n9] = d5;
            }
            double d6 = 0.0;
            n4 = -1;
            for (n3 = n5; n3 < 4; ++n3) {
                double d7;
                n9 = n2 + 4 * n3 + n5;
                d5 = arrd[n9];
                n7 = n5;
                n6 = n2 + 4 * n3;
                n8 = n2 + n5;
                while (n7-- != 0) {
                    d5 -= arrd[n6] * arrd[n8];
                    ++n6;
                    n8 += 4;
                }
                arrd[n9] = d5;
                d4 = arrd2[n3] * Math.abs(d5);
                if (!(d7 >= d6)) continue;
                d6 = d4;
                n4 = n3;
            }
            if (n4 < 0) {
                return false;
            }
            if (n5 != n4) {
                n7 = 4;
                n6 = n2 + 4 * n4;
                n8 = n2 + 4 * n5;
                while (n7-- != 0) {
                    d4 = arrd[n6];
                    arrd[n6++] = arrd[n8];
                    arrd[n8++] = d4;
                }
                arrd2[n4] = arrd2[n5];
            }
            arrn[n5] = n4;
            if (arrd[n2 + 4 * n5 + n5] == 0.0) {
                return false;
            }
            if (n5 == 3) continue;
            d4 = 1.0 / arrd[n2 + 4 * n5 + n5];
            n9 = n2 + 4 * (n5 + 1) + n5;
            n3 = 3 - n5;
            while (n3-- != 0) {
                int n12 = n9;
                arrd[n12] = arrd[n12] * d4;
                n9 += 4;
            }
        }
        return true;
    }

    private static void luBacksubstitution(double[] arrd, int[] arrn, double[] arrd2) {
        int n2 = 0;
        for (int i2 = 0; i2 < 4; ++i2) {
            int n3;
            int n4 = i2;
            int n5 = -1;
            for (int i3 = 0; i3 < 4; ++i3) {
                int n6 = arrn[n2 + i3];
                double d2 = arrd2[n4 + 4 * n6];
                arrd2[n4 + 4 * n6] = arrd2[n4 + 4 * i3];
                if (n5 >= 0) {
                    n3 = i3 * 4;
                    for (int i4 = n5; i4 <= i3 - 1; ++i4) {
                        d2 -= arrd[n3 + i4] * arrd2[n4 + 4 * i4];
                    }
                } else if (d2 != 0.0) {
                    n5 = i3;
                }
                arrd2[n4 + 4 * i3] = d2;
            }
            n3 = 12;
            int n7 = n4 + 12;
            arrd2[n7] = arrd2[n7] / arrd[n3 + 3];
            arrd2[n4 + 8] = (arrd2[n4 + 8] - arrd[(n3 -= 4) + 3] * arrd2[n4 + 12]) / arrd[n3 + 2];
            arrd2[n4 + 4] = (arrd2[n4 + 4] - arrd[(n3 -= 4) + 2] * arrd2[n4 + 8] - arrd[n3 + 3] * arrd2[n4 + 12]) / arrd[n3 + 1];
            arrd2[n4 + 0] = (arrd2[n4 + 0] - arrd[(n3 -= 4) + 1] * arrd2[n4 + 4] - arrd[n3 + 2] * arrd2[n4 + 8] - arrd[n3 + 3] * arrd2[n4 + 12]) / arrd[n3 + 0];
        }
    }

    public GeneralTransform3D mul(BaseTransform baseTransform) {
        double d2;
        double d3;
        double d4;
        double d5;
        if (baseTransform.isIdentity()) {
            return this;
        }
        double d6 = baseTransform.getMxx();
        double d7 = baseTransform.getMxy();
        double d8 = baseTransform.getMxz();
        double d9 = baseTransform.getMxt();
        double d10 = baseTransform.getMyx();
        double d11 = baseTransform.getMyy();
        double d12 = baseTransform.getMyz();
        double d13 = baseTransform.getMyt();
        double d14 = baseTransform.getMzx();
        double d15 = baseTransform.getMzy();
        double d16 = baseTransform.getMzz();
        double d17 = baseTransform.getMzt();
        double d18 = this.mat[0] * d6 + this.mat[1] * d10 + this.mat[2] * d14;
        double d19 = this.mat[0] * d7 + this.mat[1] * d11 + this.mat[2] * d15;
        double d20 = this.mat[0] * d8 + this.mat[1] * d12 + this.mat[2] * d16;
        double d21 = this.mat[0] * d9 + this.mat[1] * d13 + this.mat[2] * d17 + this.mat[3];
        double d22 = this.mat[4] * d6 + this.mat[5] * d10 + this.mat[6] * d14;
        double d23 = this.mat[4] * d7 + this.mat[5] * d11 + this.mat[6] * d15;
        double d24 = this.mat[4] * d8 + this.mat[5] * d12 + this.mat[6] * d16;
        double d25 = this.mat[4] * d9 + this.mat[5] * d13 + this.mat[6] * d17 + this.mat[7];
        double d26 = this.mat[8] * d6 + this.mat[9] * d10 + this.mat[10] * d14;
        double d27 = this.mat[8] * d7 + this.mat[9] * d11 + this.mat[10] * d15;
        double d28 = this.mat[8] * d8 + this.mat[9] * d12 + this.mat[10] * d16;
        double d29 = this.mat[8] * d9 + this.mat[9] * d13 + this.mat[10] * d17 + this.mat[11];
        if (this.isAffine()) {
            d5 = 0.0;
            d4 = 0.0;
            d3 = 0.0;
            d2 = 1.0;
        } else {
            d3 = this.mat[12] * d6 + this.mat[13] * d10 + this.mat[14] * d14;
            d4 = this.mat[12] * d7 + this.mat[13] * d11 + this.mat[14] * d15;
            d5 = this.mat[12] * d8 + this.mat[13] * d12 + this.mat[14] * d16;
            d2 = this.mat[12] * d9 + this.mat[13] * d13 + this.mat[14] * d17 + this.mat[15];
        }
        this.mat[0] = d18;
        this.mat[1] = d19;
        this.mat[2] = d20;
        this.mat[3] = d21;
        this.mat[4] = d22;
        this.mat[5] = d23;
        this.mat[6] = d24;
        this.mat[7] = d25;
        this.mat[8] = d26;
        this.mat[9] = d27;
        this.mat[10] = d28;
        this.mat[11] = d29;
        this.mat[12] = d3;
        this.mat[13] = d4;
        this.mat[14] = d5;
        this.mat[15] = d2;
        this.updateState();
        return this;
    }

    public GeneralTransform3D scale(double d2, double d3, double d4) {
        boolean bl = false;
        if (d2 != 1.0) {
            this.mat[0] = this.mat[0] * d2;
            this.mat[4] = this.mat[4] * d2;
            this.mat[8] = this.mat[8] * d2;
            this.mat[12] = this.mat[12] * d2;
            bl = true;
        }
        if (d3 != 1.0) {
            this.mat[1] = this.mat[1] * d3;
            this.mat[5] = this.mat[5] * d3;
            this.mat[9] = this.mat[9] * d3;
            this.mat[13] = this.mat[13] * d3;
            bl = true;
        }
        if (d4 != 1.0) {
            this.mat[2] = this.mat[2] * d4;
            this.mat[6] = this.mat[6] * d4;
            this.mat[10] = this.mat[10] * d4;
            this.mat[14] = this.mat[14] * d4;
            bl = true;
        }
        if (bl) {
            this.updateState();
        }
        return this;
    }

    public GeneralTransform3D mul(GeneralTransform3D generalTransform3D) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14;
        double d15;
        double d16;
        double d17;
        if (generalTransform3D.isIdentity()) {
            return this;
        }
        if (generalTransform3D.isAffine()) {
            d17 = this.mat[0] * generalTransform3D.mat[0] + this.mat[1] * generalTransform3D.mat[4] + this.mat[2] * generalTransform3D.mat[8];
            d16 = this.mat[0] * generalTransform3D.mat[1] + this.mat[1] * generalTransform3D.mat[5] + this.mat[2] * generalTransform3D.mat[9];
            d15 = this.mat[0] * generalTransform3D.mat[2] + this.mat[1] * generalTransform3D.mat[6] + this.mat[2] * generalTransform3D.mat[10];
            d14 = this.mat[0] * generalTransform3D.mat[3] + this.mat[1] * generalTransform3D.mat[7] + this.mat[2] * generalTransform3D.mat[11] + this.mat[3];
            d13 = this.mat[4] * generalTransform3D.mat[0] + this.mat[5] * generalTransform3D.mat[4] + this.mat[6] * generalTransform3D.mat[8];
            d12 = this.mat[4] * generalTransform3D.mat[1] + this.mat[5] * generalTransform3D.mat[5] + this.mat[6] * generalTransform3D.mat[9];
            d11 = this.mat[4] * generalTransform3D.mat[2] + this.mat[5] * generalTransform3D.mat[6] + this.mat[6] * generalTransform3D.mat[10];
            d10 = this.mat[4] * generalTransform3D.mat[3] + this.mat[5] * generalTransform3D.mat[7] + this.mat[6] * generalTransform3D.mat[11] + this.mat[7];
            d9 = this.mat[8] * generalTransform3D.mat[0] + this.mat[9] * generalTransform3D.mat[4] + this.mat[10] * generalTransform3D.mat[8];
            d8 = this.mat[8] * generalTransform3D.mat[1] + this.mat[9] * generalTransform3D.mat[5] + this.mat[10] * generalTransform3D.mat[9];
            d7 = this.mat[8] * generalTransform3D.mat[2] + this.mat[9] * generalTransform3D.mat[6] + this.mat[10] * generalTransform3D.mat[10];
            d6 = this.mat[8] * generalTransform3D.mat[3] + this.mat[9] * generalTransform3D.mat[7] + this.mat[10] * generalTransform3D.mat[11] + this.mat[11];
            if (this.isAffine()) {
                d5 = 0.0;
                d4 = 0.0;
                d3 = 0.0;
                d2 = 1.0;
            } else {
                d3 = this.mat[12] * generalTransform3D.mat[0] + this.mat[13] * generalTransform3D.mat[4] + this.mat[14] * generalTransform3D.mat[8];
                d4 = this.mat[12] * generalTransform3D.mat[1] + this.mat[13] * generalTransform3D.mat[5] + this.mat[14] * generalTransform3D.mat[9];
                d5 = this.mat[12] * generalTransform3D.mat[2] + this.mat[13] * generalTransform3D.mat[6] + this.mat[14] * generalTransform3D.mat[10];
                d2 = this.mat[12] * generalTransform3D.mat[3] + this.mat[13] * generalTransform3D.mat[7] + this.mat[14] * generalTransform3D.mat[11] + this.mat[15];
            }
        } else {
            d17 = this.mat[0] * generalTransform3D.mat[0] + this.mat[1] * generalTransform3D.mat[4] + this.mat[2] * generalTransform3D.mat[8] + this.mat[3] * generalTransform3D.mat[12];
            d16 = this.mat[0] * generalTransform3D.mat[1] + this.mat[1] * generalTransform3D.mat[5] + this.mat[2] * generalTransform3D.mat[9] + this.mat[3] * generalTransform3D.mat[13];
            d15 = this.mat[0] * generalTransform3D.mat[2] + this.mat[1] * generalTransform3D.mat[6] + this.mat[2] * generalTransform3D.mat[10] + this.mat[3] * generalTransform3D.mat[14];
            d14 = this.mat[0] * generalTransform3D.mat[3] + this.mat[1] * generalTransform3D.mat[7] + this.mat[2] * generalTransform3D.mat[11] + this.mat[3] * generalTransform3D.mat[15];
            d13 = this.mat[4] * generalTransform3D.mat[0] + this.mat[5] * generalTransform3D.mat[4] + this.mat[6] * generalTransform3D.mat[8] + this.mat[7] * generalTransform3D.mat[12];
            d12 = this.mat[4] * generalTransform3D.mat[1] + this.mat[5] * generalTransform3D.mat[5] + this.mat[6] * generalTransform3D.mat[9] + this.mat[7] * generalTransform3D.mat[13];
            d11 = this.mat[4] * generalTransform3D.mat[2] + this.mat[5] * generalTransform3D.mat[6] + this.mat[6] * generalTransform3D.mat[10] + this.mat[7] * generalTransform3D.mat[14];
            d10 = this.mat[4] * generalTransform3D.mat[3] + this.mat[5] * generalTransform3D.mat[7] + this.mat[6] * generalTransform3D.mat[11] + this.mat[7] * generalTransform3D.mat[15];
            d9 = this.mat[8] * generalTransform3D.mat[0] + this.mat[9] * generalTransform3D.mat[4] + this.mat[10] * generalTransform3D.mat[8] + this.mat[11] * generalTransform3D.mat[12];
            d8 = this.mat[8] * generalTransform3D.mat[1] + this.mat[9] * generalTransform3D.mat[5] + this.mat[10] * generalTransform3D.mat[9] + this.mat[11] * generalTransform3D.mat[13];
            d7 = this.mat[8] * generalTransform3D.mat[2] + this.mat[9] * generalTransform3D.mat[6] + this.mat[10] * generalTransform3D.mat[10] + this.mat[11] * generalTransform3D.mat[14];
            d6 = this.mat[8] * generalTransform3D.mat[3] + this.mat[9] * generalTransform3D.mat[7] + this.mat[10] * generalTransform3D.mat[11] + this.mat[11] * generalTransform3D.mat[15];
            if (this.isAffine()) {
                d3 = generalTransform3D.mat[12];
                d4 = generalTransform3D.mat[13];
                d5 = generalTransform3D.mat[14];
                d2 = generalTransform3D.mat[15];
            } else {
                d3 = this.mat[12] * generalTransform3D.mat[0] + this.mat[13] * generalTransform3D.mat[4] + this.mat[14] * generalTransform3D.mat[8] + this.mat[15] * generalTransform3D.mat[12];
                d4 = this.mat[12] * generalTransform3D.mat[1] + this.mat[13] * generalTransform3D.mat[5] + this.mat[14] * generalTransform3D.mat[9] + this.mat[15] * generalTransform3D.mat[13];
                d5 = this.mat[12] * generalTransform3D.mat[2] + this.mat[13] * generalTransform3D.mat[6] + this.mat[14] * generalTransform3D.mat[10] + this.mat[15] * generalTransform3D.mat[14];
                d2 = this.mat[12] * generalTransform3D.mat[3] + this.mat[13] * generalTransform3D.mat[7] + this.mat[14] * generalTransform3D.mat[11] + this.mat[15] * generalTransform3D.mat[15];
            }
        }
        this.mat[0] = d17;
        this.mat[1] = d16;
        this.mat[2] = d15;
        this.mat[3] = d14;
        this.mat[4] = d13;
        this.mat[5] = d12;
        this.mat[6] = d11;
        this.mat[7] = d10;
        this.mat[8] = d9;
        this.mat[9] = d8;
        this.mat[10] = d7;
        this.mat[11] = d6;
        this.mat[12] = d3;
        this.mat[13] = d4;
        this.mat[14] = d5;
        this.mat[15] = d2;
        this.updateState();
        return this;
    }

    public GeneralTransform3D setIdentity() {
        this.mat[0] = 1.0;
        this.mat[1] = 0.0;
        this.mat[2] = 0.0;
        this.mat[3] = 0.0;
        this.mat[4] = 0.0;
        this.mat[5] = 1.0;
        this.mat[6] = 0.0;
        this.mat[7] = 0.0;
        this.mat[8] = 0.0;
        this.mat[9] = 0.0;
        this.mat[10] = 1.0;
        this.mat[11] = 0.0;
        this.mat[12] = 0.0;
        this.mat[13] = 0.0;
        this.mat[14] = 0.0;
        this.mat[15] = 1.0;
        this.identity = true;
        return this;
    }

    public boolean isIdentity() {
        return this.identity;
    }

    private void updateState() {
        this.identity = this.mat[0] == 1.0 && this.mat[5] == 1.0 && this.mat[10] == 1.0 && this.mat[15] == 1.0 && this.mat[1] == 0.0 && this.mat[2] == 0.0 && this.mat[3] == 0.0 && this.mat[4] == 0.0 && this.mat[6] == 0.0 && this.mat[7] == 0.0 && this.mat[8] == 0.0 && this.mat[9] == 0.0 && this.mat[11] == 0.0 && this.mat[12] == 0.0 && this.mat[13] == 0.0 && this.mat[14] == 0.0;
    }

    boolean isInfOrNaN() {
        double d2 = 0.0;
        for (int i2 = 0; i2 < this.mat.length; ++i2) {
            d2 *= this.mat[i2];
        }
        return d2 != 0.0;
    }

    static boolean almostZero(double d2) {
        return d2 < 1.0E-5 && d2 > -1.0E-5;
    }

    static boolean almostOne(double d2) {
        return d2 < 1.00001 && d2 > 0.99999;
    }

    public GeneralTransform3D copy() {
        GeneralTransform3D generalTransform3D = new GeneralTransform3D();
        generalTransform3D.set(this);
        return generalTransform3D;
    }

    public String toString() {
        return this.mat[0] + ", " + this.mat[1] + ", " + this.mat[2] + ", " + this.mat[3] + "\n" + this.mat[4] + ", " + this.mat[5] + ", " + this.mat[6] + ", " + this.mat[7] + "\n" + this.mat[8] + ", " + this.mat[9] + ", " + this.mat[10] + ", " + this.mat[11] + "\n" + this.mat[12] + ", " + this.mat[13] + ", " + this.mat[14] + ", " + this.mat[15] + "\n";
    }
}

