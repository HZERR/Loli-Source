/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGCylinder;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

public class Cylinder
extends Shape3D {
    static final int DEFAULT_DIVISIONS = 64;
    static final double DEFAULT_RADIUS = 1.0;
    static final double DEFAULT_HEIGHT = 2.0;
    private int divisions = 64;
    private TriangleMesh mesh;
    private DoubleProperty height;
    private DoubleProperty radius;

    public Cylinder() {
        this(1.0, 2.0, 64);
    }

    public Cylinder(double d2, double d3) {
        this(d2, d3, 64);
    }

    public Cylinder(double d2, double d3, int n2) {
        this.divisions = n2 < 3 ? 3 : n2;
        this.setRadius(d2);
        this.setHeight(d3);
    }

    public final void setHeight(double d2) {
        this.heightProperty().set(d2);
    }

    public final double getHeight() {
        return this.height == null ? 2.0 : this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new SimpleDoubleProperty(this, "height", 2.0){

                @Override
                public void invalidated() {
                    Cylinder.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Cylinder.this.manager.invalidateCylinderMesh(Cylinder.this.key);
                    Cylinder.this.key = 0;
                    Cylinder.this.impl_geomChanged();
                }
            };
        }
        return this.height;
    }

    public final void setRadius(double d2) {
        this.radiusProperty().set(d2);
    }

    public final double getRadius() {
        return this.radius == null ? 1.0 : this.radius.get();
    }

    public final DoubleProperty radiusProperty() {
        if (this.radius == null) {
            this.radius = new SimpleDoubleProperty(this, "radius", 1.0){

                @Override
                public void invalidated() {
                    Cylinder.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Cylinder.this.manager.invalidateCylinderMesh(Cylinder.this.key);
                    Cylinder.this.key = 0;
                    Cylinder.this.impl_geomChanged();
                }
            };
        }
        return this.radius;
    }

    public int getDivisions() {
        return this.divisions;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.MESH_GEOM)) {
            NGCylinder nGCylinder = (NGCylinder)this.impl_getPeer();
            float f2 = (float)this.getHeight();
            float f3 = (float)this.getRadius();
            if (f2 < 0.0f || f3 < 0.0f) {
                nGCylinder.updateMesh(null);
            } else {
                if (this.key == 0) {
                    this.key = Cylinder.generateKey(f2, f3, this.divisions);
                }
                this.mesh = this.manager.getCylinderMesh(f2, f3, this.divisions, this.key);
                this.mesh.impl_updatePG();
                nGCylinder.updateMesh(this.mesh.impl_getPGTriangleMesh());
            }
        }
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCylinder();
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        float f2 = (float)this.getHeight();
        float f3 = (float)this.getRadius();
        if (f3 < 0.0f || f2 < 0.0f) {
            return baseBounds.makeEmpty();
        }
        float f4 = f2 * 0.5f;
        baseBounds = baseBounds.deriveWithNewBounds(-f3, -f4, -f3, f3, f4, f3);
        baseBounds = baseTransform.transform(baseBounds, baseBounds);
        return baseBounds;
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        double d4 = this.getRadius();
        double d5 = this.getHeight() * 0.5;
        return -d4 <= d2 && d2 <= d4 && -d5 <= d3 && d3 <= d5;
    }

    @Override
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResultChooser) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        boolean bl = this.divisions < 64 && this.mesh != null;
        double d7 = this.getRadius();
        Vec3d vec3d = pickRay.getDirectionNoClone();
        double d8 = vec3d.x;
        double d9 = vec3d.y;
        double d10 = vec3d.z;
        Vec3d vec3d2 = pickRay.getOriginNoClone();
        double d11 = vec3d2.x;
        double d12 = vec3d2.y;
        double d13 = vec3d2.z;
        double d14 = this.getHeight();
        double d15 = d14 / 2.0;
        CullFace cullFace = this.getCullFace();
        double d16 = d8 * d8 + d10 * d10;
        double d17 = 2.0 * (d8 * d11 + d10 * d13);
        double d18 = d11 * d11 + d13 * d13 - d7 * d7;
        double d19 = d17 * d17 - 4.0 * d16 * d18;
        double d20 = Double.POSITIVE_INFINITY;
        double d21 = pickRay.getNearClip();
        double d22 = pickRay.getFarClip();
        if (d19 >= 0.0 && (d8 != 0.0 || d10 != 0.0)) {
            double d23 = Math.sqrt(d19);
            d6 = d17 < 0.0 ? (-d17 - d23) / 2.0 : (-d17 + d23) / 2.0;
            d5 = d6 / d16;
            if (d5 > (d4 = d18 / d6)) {
                d3 = d5;
                d5 = d4;
                d4 = d3;
            }
            d3 = d12 + d5 * d9;
            if (d5 < d21 || d3 < -d15 || d3 > d15 || cullFace == CullFace.FRONT) {
                d2 = d12 + d4 * d9;
                if (d4 >= d21 && d4 <= d22 && d2 >= -d15 && d2 <= d15 && (cullFace != CullFace.BACK || bl)) {
                    d20 = d4;
                }
            } else if (d5 <= d22) {
                d20 = d5;
            }
        }
        boolean bl2 = false;
        boolean bl3 = false;
        if (d20 == Double.POSITIVE_INFINITY || !bl) {
            double d24;
            double d25;
            d6 = (-d15 - d12) / d9;
            d3 = (d15 - d12) / d9;
            boolean bl4 = false;
            if (d6 < d3) {
                d5 = d6;
                d4 = d3;
                bl4 = true;
            } else {
                d5 = d3;
                d4 = d6;
            }
            if (d5 >= d21 && d5 <= d22 && d5 < d20 && cullFace != CullFace.FRONT && (d25 = d11 + d8 * d5) * d25 + (d24 = d13 + d10 * d5) * d24 <= d7 * d7) {
                bl3 = bl4;
                bl2 = !bl4;
                d20 = d5;
            }
            if (d4 >= d21 && d4 <= d22 && d4 < d20 && (cullFace != CullFace.BACK || bl) && (d25 = d11 + d8 * d4) * d25 + (d24 = d13 + d10 * d4) * d24 <= d7 * d7) {
                bl2 = bl4;
                bl3 = !bl4;
                d20 = d4;
            }
        }
        if (Double.isInfinite(d20) || Double.isNaN(d20)) {
            return false;
        }
        if (bl) {
            return this.mesh.impl_computeIntersects(pickRay, pickResultChooser, this, cullFace, false);
        }
        if (pickResultChooser != null && pickResultChooser.isCloser(d20)) {
            Point2D point2D;
            Point3D point3D = PickResultChooser.computePoint(pickRay, d20);
            if (bl2) {
                point2D = new Point2D(0.5 + point3D.getX() / (2.0 * d7), 0.5 + point3D.getZ() / (2.0 * d7));
            } else if (bl3) {
                point2D = new Point2D(0.5 + point3D.getX() / (2.0 * d7), 0.5 - point3D.getZ() / (2.0 * d7));
            } else {
                Point3D point3D2 = new Point3D(point3D.getX(), 0.0, point3D.getZ());
                Point3D point3D3 = point3D2.crossProduct(Rotate.Z_AXIS);
                d2 = point3D2.angle(Rotate.Z_AXIS);
                if (point3D3.getY() > 0.0) {
                    d2 = 360.0 - d2;
                }
                point2D = new Point2D(1.0 - d2 / 360.0, 0.5 + point3D.getY() / d14);
            }
            pickResultChooser.offer(this, d20, -1, point3D, point2D);
        }
        return true;
    }

    static TriangleMesh createMesh(int n2, float f2, float f3) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        double d2;
        int n10;
        int n11 = n2 * 2 + 2;
        int n12 = (n2 + 1) * 4 + 1;
        int n13 = n2 * 4;
        float f4 = 0.00390625f;
        float f5 = 1.0f / (float)n2;
        f2 *= 0.5f;
        float[] arrf = new float[n11 * 3];
        float[] arrf2 = new float[n12 * 2];
        int[] arrn = new int[n13 * 6];
        int[] arrn2 = new int[n13];
        int n14 = 0;
        int n15 = 0;
        for (n10 = 0; n10 < n2; ++n10) {
            d2 = (double)(f5 * (float)n10 * 2.0f) * Math.PI;
            arrf[n14 + 0] = (float)(Math.sin(d2) * (double)f3);
            arrf[n14 + 2] = (float)(Math.cos(d2) * (double)f3);
            arrf[n14 + 1] = f2;
            arrf2[n15 + 0] = 1.0f - f5 * (float)n10;
            arrf2[n15 + 1] = 1.0f - f4;
            n14 += 3;
            n15 += 2;
        }
        arrf2[n15 + 0] = 0.0f;
        arrf2[n15 + 1] = 1.0f - f4;
        n15 += 2;
        for (n10 = 0; n10 < n2; ++n10) {
            d2 = (double)(f5 * (float)n10 * 2.0f) * Math.PI;
            arrf[n14 + 0] = (float)(Math.sin(d2) * (double)f3);
            arrf[n14 + 2] = (float)(Math.cos(d2) * (double)f3);
            arrf[n14 + 1] = -f2;
            arrf2[n15 + 0] = 1.0f - f5 * (float)n10;
            arrf2[n15 + 1] = f4;
            n14 += 3;
            n15 += 2;
        }
        arrf2[n15 + 0] = 0.0f;
        arrf2[n15 + 1] = f4;
        n15 += 2;
        arrf[n14 + 0] = 0.0f;
        arrf[n14 + 1] = f2;
        arrf[n14 + 2] = 0.0f;
        arrf[n14 + 3] = 0.0f;
        arrf[n14 + 4] = -f2;
        arrf[n14 + 5] = 0.0f;
        n14 += 6;
        for (n10 = 0; n10 <= n2; ++n10) {
            d2 = n10 < n2 ? (double)(f5 * (float)n10 * 2.0f) * Math.PI : 0.0;
            arrf2[n15 + 0] = (float)(Math.sin(d2) * 0.5) + 0.5f;
            arrf2[n15 + 1] = (float)(Math.cos(d2) * 0.5) + 0.5f;
            n15 += 2;
        }
        for (n10 = 0; n10 <= n2; ++n10) {
            d2 = n10 < n2 ? (double)(f5 * (float)n10 * 2.0f) * Math.PI : 0.0;
            arrf2[n15 + 0] = 0.5f + (float)(Math.sin(d2) * 0.5);
            arrf2[n15 + 1] = 0.5f - (float)(Math.cos(d2) * 0.5);
            n15 += 2;
        }
        arrf2[n15 + 0] = 0.5f;
        arrf2[n15 + 1] = 0.5f;
        n15 += 2;
        n10 = 0;
        for (n9 = 0; n9 < n2; ++n9) {
            n8 = n9 + 1;
            n7 = n9 + n2;
            n6 = n8 + n2;
            arrn[n10 + 0] = n9;
            arrn[n10 + 1] = n9;
            arrn[n10 + 2] = n7;
            arrn[n10 + 3] = n7 + 1;
            arrn[n10 + 4] = n8 == n2 ? 0 : n8;
            arrn[n10 + 5] = n8;
            arrn[(n10 += 6) + 0] = n6 % n2 == 0 ? n6 - n2 : n6;
            arrn[n10 + 1] = n6 + 1;
            arrn[n10 + 2] = n8 == n2 ? 0 : n8;
            arrn[n10 + 3] = n8;
            arrn[n10 + 4] = n7;
            arrn[n10 + 5] = n7 + 1;
            n10 += 6;
        }
        n9 = (n2 + 1) * 2;
        n8 = (n2 + 1) * 4;
        n7 = n2 * 2;
        for (n6 = 0; n6 < n2; ++n6) {
            n5 = n6 + 1;
            n4 = n9 + n6;
            n3 = n4 + 1;
            arrn[n10 + 0] = n6;
            arrn[n10 + 1] = n4;
            arrn[n10 + 2] = n5 == n2 ? 0 : n5;
            arrn[n10 + 3] = n3;
            arrn[n10 + 4] = n7;
            arrn[n10 + 5] = n8;
            n10 += 6;
        }
        n7 = n2 * 2 + 1;
        n9 = (n2 + 1) * 3;
        for (n6 = 0; n6 < n2; ++n6) {
            n5 = n6 + 1 + n2;
            n4 = n9 + n6;
            n3 = n4 + 1;
            arrn[n10 + 0] = n6 + n2;
            arrn[n10 + 1] = n4;
            arrn[n10 + 2] = n7;
            arrn[n10 + 3] = n8;
            arrn[n10 + 4] = n5 % n2 == 0 ? n5 - n2 : n5;
            arrn[n10 + 5] = n3;
            n10 += 6;
        }
        for (n6 = 0; n6 < n2 * 2; ++n6) {
            arrn2[n6] = 1;
        }
        for (n6 = n2 * 2; n6 < n2 * 4; ++n6) {
            arrn2[n6] = 2;
        }
        TriangleMesh triangleMesh = new TriangleMesh(true);
        triangleMesh.getPoints().setAll(arrf);
        triangleMesh.getTexCoords().setAll(arrf2);
        triangleMesh.getFaces().setAll(arrn);
        triangleMesh.getFaceSmoothingGroups().setAll(arrn2);
        return triangleMesh;
    }

    private static int generateKey(float f2, float f3, int n2) {
        int n3 = 7;
        n3 = 47 * n3 + Float.floatToIntBits(f2);
        n3 = 47 * n3 + Float.floatToIntBits(f3);
        n3 = 47 * n3 + n2;
        return n3;
    }
}

