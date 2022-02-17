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
import com.sun.javafx.sg.prism.NGBox;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

public class Box
extends Shape3D {
    private TriangleMesh mesh;
    public static final double DEFAULT_SIZE = 2.0;
    private DoubleProperty depth;
    private DoubleProperty height;
    private DoubleProperty width;

    public Box() {
        this(2.0, 2.0, 2.0);
    }

    public Box(double d2, double d3, double d4) {
        this.setWidth(d2);
        this.setHeight(d3);
        this.setDepth(d4);
    }

    public final void setDepth(double d2) {
        this.depthProperty().set(d2);
    }

    public final double getDepth() {
        return this.depth == null ? 2.0 : this.depth.get();
    }

    public final DoubleProperty depthProperty() {
        if (this.depth == null) {
            this.depth = new SimpleDoubleProperty(this, "depth", 2.0){

                @Override
                public void invalidated() {
                    Box.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Box.this.manager.invalidateBoxMesh(Box.this.key);
                    Box.this.key = 0;
                    Box.this.impl_geomChanged();
                }
            };
        }
        return this.depth;
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
                    Box.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Box.this.manager.invalidateBoxMesh(Box.this.key);
                    Box.this.key = 0;
                    Box.this.impl_geomChanged();
                }
            };
        }
        return this.height;
    }

    public final void setWidth(double d2) {
        this.widthProperty().set(d2);
    }

    public final double getWidth() {
        return this.width == null ? 2.0 : this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new SimpleDoubleProperty(this, "width", 2.0){

                @Override
                public void invalidated() {
                    Box.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    Box.this.manager.invalidateBoxMesh(Box.this.key);
                    Box.this.key = 0;
                    Box.this.impl_geomChanged();
                }
            };
        }
        return this.width;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGBox();
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.MESH_GEOM)) {
            NGBox nGBox = (NGBox)this.impl_getPeer();
            float f2 = (float)this.getWidth();
            float f3 = (float)this.getHeight();
            float f4 = (float)this.getDepth();
            if (f2 < 0.0f || f3 < 0.0f || f4 < 0.0f) {
                nGBox.updateMesh(null);
            } else {
                if (this.key == 0) {
                    this.key = Box.generateKey(f2, f3, f4);
                }
                this.mesh = this.manager.getBoxMesh(f2, f3, f4, this.key);
                this.mesh.impl_updatePG();
                nGBox.updateMesh(this.mesh.impl_getPGTriangleMesh());
            }
        }
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        float f2 = (float)this.getWidth();
        float f3 = (float)this.getHeight();
        float f4 = (float)this.getDepth();
        if (f2 < 0.0f || f3 < 0.0f || f4 < 0.0f) {
            return baseBounds.makeEmpty();
        }
        float f5 = f2 * 0.5f;
        float f6 = f3 * 0.5f;
        float f7 = f4 * 0.5f;
        baseBounds = baseBounds.deriveWithNewBounds(-f5, -f6, -f7, f5, f6, f7);
        baseBounds = baseTransform.transform(baseBounds, baseBounds);
        return baseBounds;
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        return -d4 <= d2 && d2 <= d4 && -d5 <= d3 && d3 <= d5;
    }

    @Override
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResultChooser) {
        double d2;
        double d3;
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        double d6 = this.getDepth();
        double d7 = d4 / 2.0;
        double d8 = d5 / 2.0;
        double d9 = d6 / 2.0;
        Vec3d vec3d = pickRay.getDirectionNoClone();
        double d10 = vec3d.x == 0.0 ? Double.POSITIVE_INFINITY : 1.0 / vec3d.x;
        double d11 = vec3d.y == 0.0 ? Double.POSITIVE_INFINITY : 1.0 / vec3d.y;
        double d12 = vec3d.z == 0.0 ? Double.POSITIVE_INFINITY : 1.0 / vec3d.z;
        Vec3d vec3d2 = pickRay.getOriginNoClone();
        double d13 = vec3d2.x;
        double d14 = vec3d2.y;
        double d15 = vec3d2.z;
        boolean bl = d10 < 0.0;
        boolean bl2 = d11 < 0.0;
        boolean bl3 = d12 < 0.0;
        double d16 = Double.NEGATIVE_INFINITY;
        double d17 = Double.POSITIVE_INFINITY;
        int n2 = 48;
        int n3 = 48;
        if (Double.isInfinite(d10)) {
            if (!(-d7 <= d13) || !(d7 >= d13)) {
                return false;
            }
        } else {
            d16 = ((bl ? d7 : -d7) - d13) * d10;
            d17 = ((bl ? -d7 : d7) - d13) * d10;
            n2 = bl ? 88 : 120;
            int n4 = n3 = bl ? 120 : 88;
        }
        if (Double.isInfinite(d11)) {
            if (!(-d8 <= d14) || !(d8 >= d14)) {
                return false;
            }
        } else {
            d3 = ((bl2 ? d8 : -d8) - d14) * d11;
            d2 = ((bl2 ? -d8 : d8) - d14) * d11;
            if (d16 > d2 || d3 > d17) {
                return false;
            }
            if (d3 > d16) {
                n2 = bl2 ? 89 : 121;
                d16 = d3;
            }
            if (d2 < d17) {
                n3 = bl2 ? 121 : 89;
                d17 = d2;
            }
        }
        if (Double.isInfinite(d12)) {
            if (!(-d9 <= d15) || !(d9 >= d15)) {
                return false;
            }
        } else {
            d3 = ((bl3 ? d9 : -d9) - d15) * d12;
            d2 = ((bl3 ? -d9 : d9) - d15) * d12;
            if (d16 > d2 || d3 > d17) {
                return false;
            }
            if (d3 > d16) {
                n2 = bl3 ? 90 : 122;
                d16 = d3;
            }
            if (d2 < d17) {
                n3 = bl3 ? 122 : 90;
                d17 = d2;
            }
        }
        int n5 = n2;
        double d18 = d16;
        CullFace cullFace = this.getCullFace();
        double d19 = pickRay.getNearClip();
        double d20 = pickRay.getFarClip();
        if (d16 > d20) {
            return false;
        }
        if (d16 < d19 || cullFace == CullFace.FRONT) {
            if (d17 >= d19 && d17 <= d20 && cullFace != CullFace.BACK) {
                n5 = n3;
                d18 = d17;
            } else {
                return false;
            }
        }
        if (Double.isInfinite(d18) || Double.isNaN(d18)) {
            return false;
        }
        if (pickResultChooser != null && pickResultChooser.isCloser(d18)) {
            Point3D point3D = PickResultChooser.computePoint(pickRay, d18);
            Point2D point2D = null;
            switch (n5) {
                case 120: {
                    point2D = new Point2D(0.5 - point3D.getZ() / d6, 0.5 + point3D.getY() / d5);
                    break;
                }
                case 88: {
                    point2D = new Point2D(0.5 + point3D.getZ() / d6, 0.5 + point3D.getY() / d5);
                    break;
                }
                case 121: {
                    point2D = new Point2D(0.5 + point3D.getX() / d4, 0.5 - point3D.getZ() / d6);
                    break;
                }
                case 89: {
                    point2D = new Point2D(0.5 + point3D.getX() / d4, 0.5 + point3D.getZ() / d6);
                    break;
                }
                case 122: {
                    point2D = new Point2D(0.5 + point3D.getX() / d4, 0.5 + point3D.getY() / d5);
                    break;
                }
                case 90: {
                    point2D = new Point2D(0.5 - point3D.getX() / d4, 0.5 + point3D.getY() / d5);
                    break;
                }
                default: {
                    return false;
                }
            }
            pickResultChooser.offer(this, d18, -1, point3D, point2D);
        }
        return true;
    }

    static TriangleMesh createMesh(float f2, float f3, float f4) {
        float f5 = f2 / 2.0f;
        float f6 = f3 / 2.0f;
        float f7 = f4 / 2.0f;
        float[] arrf = new float[]{-f5, -f6, -f7, f5, -f6, -f7, f5, f6, -f7, -f5, f6, -f7, -f5, -f6, f7, f5, -f6, f7, f5, f6, f7, -f5, f6, f7};
        float[] arrf2 = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        int[] arrn = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] arrn2 = new int[]{0, 0, 2, 2, 1, 1, 2, 2, 0, 0, 3, 3, 1, 0, 6, 2, 5, 1, 6, 2, 1, 0, 2, 3, 5, 0, 7, 2, 4, 1, 7, 2, 5, 0, 6, 3, 4, 0, 3, 2, 0, 1, 3, 2, 4, 0, 7, 3, 3, 0, 6, 2, 2, 1, 6, 2, 3, 0, 7, 3, 4, 0, 1, 2, 5, 1, 1, 2, 4, 0, 0, 3};
        TriangleMesh triangleMesh = new TriangleMesh(true);
        triangleMesh.getPoints().setAll(arrf);
        triangleMesh.getTexCoords().setAll(arrf2);
        triangleMesh.getFaces().setAll(arrn2);
        triangleMesh.getFaceSmoothingGroups().setAll(arrn);
        return triangleMesh;
    }

    private static int generateKey(float f2, float f3, float f4) {
        int n2 = 3;
        n2 = 97 * n2 + Float.floatToIntBits(f2);
        n2 = 97 * n2 + Float.floatToIntBits(f3);
        n2 = 97 * n2 + Float.floatToIntBits(f4);
        return n2;
    }
}

