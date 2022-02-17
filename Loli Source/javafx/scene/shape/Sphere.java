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
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSphere;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

public class Sphere
extends Shape3D {
    static final int DEFAULT_DIVISIONS = 64;
    static final double DEFAULT_RADIUS = 1.0;
    private int divisions = 64;
    private TriangleMesh mesh;
    private DoubleProperty radius;

    public Sphere() {
        this(1.0, 64);
    }

    public Sphere(double d2) {
        this(d2, 64);
    }

    public Sphere(double d2, int n2) {
        this.divisions = n2 < 1 ? 1 : n2;
        this.setRadius(d2);
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
                    Sphere.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Sphere.this.manager.invalidateSphereMesh(Sphere.this.key);
                    Sphere.this.key = 0;
                    Sphere.this.impl_geomChanged();
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
    protected NGNode impl_createPeer() {
        return new NGSphere();
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.MESH_GEOM)) {
            NGSphere nGSphere = (NGSphere)this.impl_getPeer();
            float f2 = (float)this.getRadius();
            if (f2 < 0.0f) {
                nGSphere.updateMesh(null);
            } else {
                if (this.key == 0) {
                    this.key = Sphere.generateKey(f2, this.divisions);
                }
                this.mesh = this.manager.getSphereMesh(f2, this.divisions, this.key);
                this.mesh.impl_updatePG();
                nGSphere.updateMesh(this.mesh.impl_getPGTriangleMesh());
            }
        }
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        float f2 = (float)this.getRadius();
        if (f2 < 0.0f) {
            return baseBounds.makeEmpty();
        }
        baseBounds = baseBounds.deriveWithNewBounds(-f2, -f2, -f2, f2, f2, f2);
        baseBounds = baseTransform.transform(baseBounds, baseBounds);
        return baseBounds;
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        double d4 = d2 * d2 + d3 * d3;
        double d5 = this.getRadius();
        return d4 <= d5 * d5;
    }

    @Override
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResultChooser) {
        double d2;
        double d3;
        boolean bl = this.divisions < 64 && this.mesh != null;
        double d4 = this.getRadius();
        Vec3d vec3d = pickRay.getDirectionNoClone();
        double d5 = vec3d.x;
        double d6 = vec3d.y;
        double d7 = vec3d.z;
        Vec3d vec3d2 = pickRay.getOriginNoClone();
        double d8 = vec3d2.x;
        double d9 = vec3d2.y;
        double d10 = vec3d2.z;
        double d11 = 2.0 * (d5 * d8 + d6 * d9 + d7 * d10);
        double d12 = d5 * d5 + d6 * d6 + d7 * d7;
        double d13 = d8 * d8 + d9 * d9 + d10 * d10 - d4 * d4;
        double d14 = d11 * d11 - 4.0 * d12 * d13;
        if (d14 < 0.0) {
            return false;
        }
        double d15 = Math.sqrt(d14);
        double d16 = d11 < 0.0 ? (-d11 - d15) / 2.0 : (-d11 + d15) / 2.0;
        double d17 = d16 / d12;
        if (d17 > (d3 = d13 / d16)) {
            d2 = d17;
            d17 = d3;
            d3 = d2;
        }
        d2 = pickRay.getNearClip();
        double d18 = pickRay.getFarClip();
        if (d3 < d2 || d17 > d18) {
            return false;
        }
        double d19 = d17;
        CullFace cullFace = this.getCullFace();
        if (d17 < d2 || cullFace == CullFace.FRONT) {
            if (d3 <= d18 && this.getCullFace() != CullFace.BACK) {
                d19 = d3;
            } else if (!bl) {
                return false;
            }
        }
        if (Double.isInfinite(d19) || Double.isNaN(d19)) {
            return false;
        }
        if (bl) {
            return this.mesh.impl_computeIntersects(pickRay, pickResultChooser, this, cullFace, false);
        }
        if (pickResultChooser != null && pickResultChooser.isCloser(d19)) {
            Point3D point3D = PickResultChooser.computePoint(pickRay, d19);
            Point3D point3D2 = new Point3D(point3D.getX(), 0.0, point3D.getZ());
            Point3D point3D3 = point3D2.crossProduct(Rotate.Z_AXIS);
            double d20 = point3D2.angle(Rotate.Z_AXIS);
            if (point3D3.getY() > 0.0) {
                d20 = 360.0 - d20;
            }
            Point2D point2D = new Point2D(1.0 - d20 / 360.0, 0.5 + point3D.getY() / (2.0 * d4));
            pickResultChooser.offer(this, d19, -1, point3D, point2D);
        }
        return true;
    }

    private static int correctDivisions(int n2) {
        return (n2 + 3) / 4 * 4;
    }

    static TriangleMesh createMesh(int n2, float f2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        float f3;
        int n9;
        n2 = Sphere.correctDivisions(n2);
        int n10 = n2 / 2;
        int n11 = n2 * (n10 - 1) + 2;
        int n12 = (n2 + 1) * (n10 - 1) + n2 * 2;
        int n13 = n2 * (n10 - 2) * 2 + n2 * 2;
        float f4 = 1.0f / (float)n2;
        float[] arrf = new float[n11 * 3];
        float[] arrf2 = new float[n12 * 2];
        int[] arrn = new int[n13 * 6];
        int n14 = 0;
        int n15 = 0;
        for (n9 = 0; n9 < n10 - 1; ++n9) {
            f3 = f4 * (float)(n9 + 1 - n10 / 2) * 2.0f * (float)Math.PI;
            float f5 = (float)Math.sin(f3);
            float f6 = (float)Math.cos(f3);
            float f7 = 0.5f + f5 * 0.5f;
            for (n8 = 0; n8 < n2; ++n8) {
                double d2 = f4 * (float)n8 * 2.0f * (float)Math.PI;
                float f8 = (float)Math.sin(d2);
                float f9 = (float)Math.cos(d2);
                arrf[n14 + 0] = f8 * f6 * f2;
                arrf[n14 + 2] = f9 * f6 * f2;
                arrf[n14 + 1] = f5 * f2;
                arrf2[n15 + 0] = 1.0f - f4 * (float)n8;
                arrf2[n15 + 1] = f7;
                n14 += 3;
                n15 += 2;
            }
            arrf2[n15 + 0] = 0.0f;
            arrf2[n15 + 1] = f7;
            n15 += 2;
        }
        arrf[n14 + 0] = 0.0f;
        arrf[n14 + 1] = -f2;
        arrf[n14 + 2] = 0.0f;
        arrf[n14 + 3] = 0.0f;
        arrf[n14 + 4] = f2;
        arrf[n14 + 5] = 0.0f;
        n14 += 6;
        n9 = (n10 - 1) * n2;
        f3 = 0.00390625f;
        for (n7 = 0; n7 < n2; ++n7) {
            arrf2[n15 + 0] = 1.0f - f4 * (0.5f + (float)n7);
            arrf2[n15 + 1] = f3;
            n15 += 2;
        }
        for (n7 = 0; n7 < n2; ++n7) {
            arrf2[n15 + 0] = 1.0f - f4 * (0.5f + (float)n7);
            arrf2[n15 + 1] = 1.0f - f3;
            n15 += 2;
        }
        n7 = 0;
        for (n6 = 0; n6 < n10 - 2; ++n6) {
            for (int i2 = 0; i2 < n2; ++i2) {
                n8 = n6 * n2 + i2;
                int n16 = n8 + 1;
                n5 = n8 + n2;
                int n17 = n16 + n2;
                int n18 = n8 + n6;
                n4 = n18 + 1;
                n3 = n18 + (n2 + 1);
                int n19 = n4 + (n2 + 1);
                arrn[n7 + 0] = n8;
                arrn[n7 + 1] = n18;
                arrn[n7 + 2] = n16 % n2 == 0 ? n16 - n2 : n16;
                arrn[n7 + 3] = n4;
                arrn[n7 + 4] = n5;
                arrn[n7 + 5] = n3;
                arrn[(n7 += 6) + 0] = n17 % n2 == 0 ? n17 - n2 : n17;
                arrn[n7 + 1] = n19;
                arrn[n7 + 2] = n5;
                arrn[n7 + 3] = n3;
                arrn[n7 + 4] = n16 % n2 == 0 ? n16 - n2 : n16;
                arrn[n7 + 5] = n4;
                n7 += 6;
            }
        }
        n6 = n9;
        int n20 = (n10 - 1) * (n2 + 1);
        for (n8 = 0; n8 < n2; ++n8) {
            int n21 = n8;
            n5 = n8 + 1;
            int n22 = n20 + n8;
            arrn[n7 + 0] = n6;
            arrn[n7 + 1] = n22;
            arrn[n7 + 2] = n5 == n2 ? 0 : n5;
            arrn[n7 + 3] = n5;
            arrn[n7 + 4] = n21;
            arrn[n7 + 5] = n21;
            n7 += 6;
        }
        ++n6;
        n20 += n2;
        n8 = (n10 - 2) * n2;
        for (int i3 = 0; i3 < n2; ++i3) {
            n5 = n8 + i3;
            int n23 = n8 + i3 + 1;
            int n24 = n20 + i3;
            n4 = (n10 - 2) * (n2 + 1) + i3;
            n3 = n4 + 1;
            arrn[n7 + 0] = n6;
            arrn[n7 + 1] = n24;
            arrn[n7 + 2] = n5;
            arrn[n7 + 3] = n4;
            arrn[n7 + 4] = n23 % n2 == 0 ? n23 - n2 : n23;
            arrn[n7 + 5] = n3;
            n7 += 6;
        }
        TriangleMesh triangleMesh = new TriangleMesh(true);
        triangleMesh.getPoints().setAll(arrf);
        triangleMesh.getTexCoords().setAll(arrf2);
        triangleMesh.getFaces().setAll(arrn);
        return triangleMesh;
    }

    private static int generateKey(float f2, int n2) {
        int n3 = 5;
        n3 = 23 * n3 + Float.floatToIntBits(f2);
        n3 = 23 * n3 + n2;
        return n3;
    }
}

