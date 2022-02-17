/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.collections.FloatArraySyncer;
import com.sun.javafx.collections.IntegerArraySyncer;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.shape.ObservableFaceArrayImpl;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ArrayChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.VertexFormat;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import sun.util.logging.PlatformLogger;

public class TriangleMesh
extends Mesh {
    private final ObservableFloatArray points = FXCollections.observableFloatArray();
    private final ObservableFloatArray normals = FXCollections.observableFloatArray();
    private final ObservableFloatArray texCoords = FXCollections.observableFloatArray();
    private final ObservableFaceArray faces = new ObservableFaceArrayImpl();
    private final ObservableIntegerArray faceSmoothingGroups = FXCollections.observableIntegerArray();
    private final Listener pointsSyncer = new Listener(this, (ObservableArray)this.points);
    private final Listener normalsSyncer = new Listener(this, (ObservableArray)this.normals);
    private final Listener texCoordsSyncer = new Listener(this, (ObservableArray)this.texCoords);
    private final Listener facesSyncer = new Listener(this, (ObservableArray)this.faces);
    private final Listener faceSmoothingGroupsSyncer = new Listener(this, (ObservableArray)this.faceSmoothingGroups);
    private final boolean isPredefinedShape;
    private boolean isValidDirty = true;
    private boolean isPointsValid;
    private boolean isNormalsValid;
    private boolean isTexCoordsValid;
    private boolean isFacesValid;
    private boolean isFaceSmoothingGroupValid;
    private int refCount = 1;
    private BaseBounds cachedBounds;
    private ObjectProperty<VertexFormat> vertexFormat;
    private NGTriangleMesh peer;

    public TriangleMesh() {
        this(false);
    }

    public TriangleMesh(VertexFormat vertexFormat) {
        this(false);
        this.setVertexFormat(vertexFormat);
    }

    TriangleMesh(boolean bl) {
        this.isPredefinedShape = bl;
        if (bl) {
            this.isPointsValid = true;
            this.isNormalsValid = true;
            this.isTexCoordsValid = true;
            this.isFacesValid = true;
            this.isFaceSmoothingGroupValid = true;
        } else {
            this.isPointsValid = false;
            this.isNormalsValid = false;
            this.isTexCoordsValid = false;
            this.isFacesValid = false;
            this.isFaceSmoothingGroupValid = false;
        }
    }

    public final void setVertexFormat(VertexFormat vertexFormat) {
        this.vertexFormatProperty().set(vertexFormat);
    }

    public final VertexFormat getVertexFormat() {
        return this.vertexFormat == null ? VertexFormat.POINT_TEXCOORD : (VertexFormat)this.vertexFormat.get();
    }

    public final ObjectProperty<VertexFormat> vertexFormatProperty() {
        if (this.vertexFormat == null) {
            this.vertexFormat = new SimpleObjectProperty<VertexFormat>((Object)this, "vertexFormat"){

                @Override
                protected void invalidated() {
                    TriangleMesh.this.setDirty(true);
                    TriangleMesh.this.facesSyncer.setDirty(true);
                    TriangleMesh.this.faceSmoothingGroupsSyncer.setDirty(true);
                }
            };
        }
        return this.vertexFormat;
    }

    public final int getPointElementSize() {
        return this.getVertexFormat().getPointElementSize();
    }

    public final int getNormalElementSize() {
        return this.getVertexFormat().getNormalElementSize();
    }

    public final int getTexCoordElementSize() {
        return this.getVertexFormat().getTexCoordElementSize();
    }

    public final int getFaceElementSize() {
        return this.getVertexFormat().getVertexIndexSize() * 3;
    }

    public final ObservableFloatArray getPoints() {
        return this.points;
    }

    public final ObservableFloatArray getNormals() {
        return this.normals;
    }

    public final ObservableFloatArray getTexCoords() {
        return this.texCoords;
    }

    public final ObservableFaceArray getFaces() {
        return this.faces;
    }

    public final ObservableIntegerArray getFaceSmoothingGroups() {
        return this.faceSmoothingGroups;
    }

    @Override
    void setDirty(boolean bl) {
        super.setDirty(bl);
        if (!bl) {
            this.pointsSyncer.setDirty(false);
            this.normalsSyncer.setDirty(false);
            this.texCoordsSyncer.setDirty(false);
            this.facesSyncer.setDirty(false);
            this.faceSmoothingGroupsSyncer.setDirty(false);
        }
    }

    int getRefCount() {
        return this.refCount;
    }

    synchronized void incRef() {
        ++this.refCount;
    }

    synchronized void decRef() {
        --this.refCount;
    }

    @Deprecated
    NGTriangleMesh impl_getPGTriangleMesh() {
        if (this.peer == null) {
            this.peer = new NGTriangleMesh();
        }
        return this.peer;
    }

    @Override
    NGTriangleMesh getPGMesh() {
        return this.impl_getPGTriangleMesh();
    }

    private boolean validatePoints() {
        if (this.points.size() == 0) {
            return false;
        }
        if (this.points.size() % this.getVertexFormat().getPointElementSize() != 0) {
            String string = TriangleMesh.class.getName();
            PlatformLogger.getLogger(string).warning("points.size() has to be divisible by getPointElementSize(). It is to store multiple x, y, and z coordinates of this mesh");
            return false;
        }
        return true;
    }

    private boolean validateNormals() {
        if (this.getVertexFormat() != VertexFormat.POINT_NORMAL_TEXCOORD) {
            return true;
        }
        if (this.normals.size() == 0) {
            return false;
        }
        if (this.normals.size() % this.getVertexFormat().getNormalElementSize() != 0) {
            String string = TriangleMesh.class.getName();
            PlatformLogger.getLogger(string).warning("normals.size() has to be divisible by getNormalElementSize(). It is to store multiple nx, ny, and nz coordinates of this mesh");
            return false;
        }
        return true;
    }

    private boolean validateTexCoords() {
        if (this.texCoords.size() == 0) {
            return false;
        }
        if (this.texCoords.size() % this.getVertexFormat().getTexCoordElementSize() != 0) {
            String string = TriangleMesh.class.getName();
            PlatformLogger.getLogger(string).warning("texCoords.size() has to be divisible by getTexCoordElementSize(). It is to store multiple u and v texture coordinates of this mesh");
            return false;
        }
        return true;
    }

    private boolean validateFaces() {
        if (this.faces.size() == 0) {
            return false;
        }
        String string = TriangleMesh.class.getName();
        if (this.faces.size() % this.getFaceElementSize() != 0) {
            PlatformLogger.getLogger(string).warning("faces.size() has to be divisible by getFaceElementSize().");
            return false;
        }
        if (this.getVertexFormat() == VertexFormat.POINT_TEXCOORD) {
            int n2 = this.points.size() / this.getVertexFormat().getPointElementSize();
            int n3 = this.texCoords.size() / this.getVertexFormat().getTexCoordElementSize();
            for (int i2 = 0; i2 < this.faces.size(); ++i2) {
                if ((i2 % 2 != 0 || this.faces.get(i2) < n2 && this.faces.get(i2) >= 0) && (i2 % 2 == 0 || this.faces.get(i2) < n3 && this.faces.get(i2) >= 0)) continue;
                PlatformLogger.getLogger(string).warning("The values in the faces array must be within the range of the number of vertices in the points array (0 to points.length / 3 - 1) for the point indices and within the range of the number of the vertices in the texCoords array (0 to texCoords.length / 2 - 1) for the texture coordinate indices.");
                return false;
            }
        } else if (this.getVertexFormat() == VertexFormat.POINT_NORMAL_TEXCOORD) {
            int n4 = this.points.size() / this.getVertexFormat().getPointElementSize();
            int n5 = this.normals.size() / this.getVertexFormat().getNormalElementSize();
            int n6 = this.texCoords.size() / this.getVertexFormat().getTexCoordElementSize();
            for (int i3 = 0; i3 < this.faces.size(); i3 += 3) {
                if (this.faces.get(i3) < n4 && this.faces.get(i3) >= 0 && this.faces.get(i3 + 1) < n5 && this.faces.get(i3 + 1) >= 0 && this.faces.get(i3 + 2) < n6 && this.faces.get(i3 + 2) >= 0) continue;
                PlatformLogger.getLogger(string).warning("The values in the faces array must be within the range of the number of vertices in the points array (0 to points.length / 3 - 1) for the point indices, and within the range of the number of the vertices in the normals array (0 to normals.length / 3 - 1) for the normals indices, and number of the vertices in the texCoords array (0 to texCoords.length / 2 - 1) for the texture coordinate indices.");
                return false;
            }
        } else {
            PlatformLogger.getLogger(string).warning("Unsupported VertexFormat: " + this.getVertexFormat().toString());
            return false;
        }
        return true;
    }

    private boolean validateFaceSmoothingGroups() {
        if (this.faceSmoothingGroups.size() != 0 && this.faceSmoothingGroups.size() != this.faces.size() / this.getFaceElementSize()) {
            String string = TriangleMesh.class.getName();
            PlatformLogger.getLogger(string).warning("faceSmoothingGroups.size() has to equal to number of faces.");
            return false;
        }
        return true;
    }

    private boolean validate() {
        if (this.isPredefinedShape) {
            return true;
        }
        if (this.isValidDirty) {
            if (this.pointsSyncer.dirtyInFull) {
                this.isPointsValid = this.validatePoints();
            }
            if (this.normalsSyncer.dirtyInFull) {
                this.isNormalsValid = this.validateNormals();
            }
            if (this.texCoordsSyncer.dirtyInFull) {
                this.isTexCoordsValid = this.validateTexCoords();
            }
            if (this.facesSyncer.dirty || this.pointsSyncer.dirtyInFull || this.normalsSyncer.dirtyInFull || this.texCoordsSyncer.dirtyInFull) {
                boolean bl = this.isFacesValid = this.isPointsValid && this.isNormalsValid && this.isTexCoordsValid && this.validateFaces();
            }
            if (this.faceSmoothingGroupsSyncer.dirtyInFull || this.facesSyncer.dirtyInFull) {
                this.isFaceSmoothingGroupValid = this.isFacesValid && this.validateFaceSmoothingGroups();
            }
            this.isValidDirty = false;
        }
        return this.isPointsValid && this.isNormalsValid && this.isTexCoordsValid && this.isFaceSmoothingGroupValid && this.isFacesValid;
    }

    @Override
    @Deprecated
    void impl_updatePG() {
        if (!this.isDirty()) {
            return;
        }
        NGTriangleMesh nGTriangleMesh = this.impl_getPGTriangleMesh();
        if (this.validate()) {
            nGTriangleMesh.setUserDefinedNormals(this.getVertexFormat() == VertexFormat.POINT_NORMAL_TEXCOORD);
            nGTriangleMesh.syncPoints(this.pointsSyncer);
            nGTriangleMesh.syncNormals(this.normalsSyncer);
            nGTriangleMesh.syncTexCoords(this.texCoordsSyncer);
            nGTriangleMesh.syncFaces(this.facesSyncer);
            nGTriangleMesh.syncFaceSmoothingGroups(this.faceSmoothingGroupsSyncer);
        } else {
            nGTriangleMesh.setUserDefinedNormals(false);
            nGTriangleMesh.syncPoints(null);
            nGTriangleMesh.syncNormals(null);
            nGTriangleMesh.syncTexCoords(null);
            nGTriangleMesh.syncFaces(null);
            nGTriangleMesh.syncFaceSmoothingGroups(null);
        }
        this.setDirty(false);
    }

    @Override
    BaseBounds computeBounds(BaseBounds baseBounds) {
        if (this.isDirty() || this.cachedBounds == null) {
            this.cachedBounds = new BoxBounds();
            if (this.validate()) {
                double d2 = this.points.size();
                int n2 = 0;
                while ((double)n2 < d2) {
                    this.cachedBounds.add(this.points.get(n2), this.points.get(n2 + 1), this.points.get(n2 + 2));
                    n2 += this.getVertexFormat().getPointElementSize();
                }
            }
        }
        return baseBounds.deriveWithNewBounds(this.cachedBounds);
    }

    private Point3D computeCentroid(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10) {
        return new Point3D(d2 + (d8 + (d5 - d8) / 2.0 - d2) / 3.0, d3 + (d9 + (d6 - d9) / 2.0 - d3) / 3.0, d4 + (d10 + (d7 - d10) / 2.0 - d4) / 3.0);
    }

    private Point2D computeCentroid(Point2D point2D, Point2D point2D2, Point2D point2D3) {
        Point2D point2D4 = point2D2.midpoint(point2D3);
        Point2D point2D5 = point2D4.subtract(point2D);
        return point2D.add(new Point2D(point2D5.getX() / 3.0, point2D5.getY() / 3.0));
    }

    private boolean computeIntersectsFace(PickRay pickRay, Vec3d vec3d, Vec3d vec3d2, int n2, CullFace cullFace, Node node, boolean bl, PickResultChooser pickResultChooser) {
        double d2;
        float f2;
        float f3;
        double d3;
        float f4;
        float f5;
        int n3 = this.getVertexFormat().getVertexIndexSize();
        int n4 = this.getVertexFormat().getPointElementSize();
        int n5 = this.faces.get(n2) * n4;
        int n6 = this.faces.get(n2 + n3) * n4;
        int n7 = this.faces.get(n2 + 2 * n3) * n4;
        float f6 = this.points.get(n5);
        float f7 = this.points.get(n5 + 1);
        float f8 = this.points.get(n5 + 2);
        float f9 = this.points.get(n6);
        float f10 = this.points.get(n6 + 1);
        float f11 = this.points.get(n6 + 2);
        float f12 = this.points.get(n7);
        float f13 = this.points.get(n7 + 1);
        float f14 = f9 - f6;
        float f15 = this.points.get(n7 + 2);
        float f16 = f15 - f8;
        double d4 = vec3d2.y * (double)f16 - vec3d2.z * (double)(f5 = f13 - f7);
        double d5 = (double)f14 * d4 + (double)(f4 = f10 - f7) * (d3 = vec3d2.z * (double)(f3 = f12 - f6) - vec3d2.x * (double)f16) + (double)(f2 = f11 - f8) * (d2 = vec3d2.x * (double)f5 - vec3d2.y * (double)f3);
        if (d5 == 0.0) {
            return false;
        }
        double d6 = 1.0 / d5;
        double d7 = vec3d.x - (double)f6;
        double d8 = vec3d.y - (double)f7;
        double d9 = vec3d.z - (double)f8;
        double d10 = d6 * (d7 * d4 + d8 * d3 + d9 * d2);
        if (d10 < 0.0 || d10 > 1.0) {
            return false;
        }
        double d11 = d8 * (double)f2 - d9 * (double)f4;
        double d12 = d9 * (double)f14 - d7 * (double)f2;
        double d13 = d7 * (double)f4 - d8 * (double)f14;
        double d14 = d6 * (vec3d2.x * d11 + vec3d2.y * d12 + vec3d2.z * d13);
        if (d14 < 0.0 || d10 + d14 > 1.0) {
            return false;
        }
        double d15 = d6 * ((double)f3 * d11 + (double)f5 * d12 + (double)f16 * d13);
        if (d15 >= pickRay.getNearClip() && d15 <= pickRay.getFarClip()) {
            Point3D point3D;
            Point3D point3D2;
            double d16;
            if (cullFace != CullFace.NONE && ((d16 = (point3D2 = new Point3D(f4 * f16 - f2 * f5, f2 * f3 - f14 * f16, f14 * f5 - f4 * f3)).angle(new Point3D(-vec3d2.x, -vec3d2.y, -vec3d2.z))) >= 90.0 || cullFace != CullFace.BACK) && (d16 <= 90.0 || cullFace != CullFace.FRONT)) {
                return false;
            }
            if (Double.isInfinite(d15) || Double.isNaN(d15)) {
                return false;
            }
            if (pickResultChooser == null || !pickResultChooser.isCloser(d15)) {
                return true;
            }
            point3D2 = PickResultChooser.computePoint(pickRay, d15);
            Point3D point3D3 = this.computeCentroid(f6, f7, f8, f9, f10, f11, f12, f13, f15);
            Point3D point3D4 = new Point3D((double)f6 - point3D3.getX(), (double)f7 - point3D3.getY(), (double)f8 - point3D3.getZ());
            Point3D point3D5 = new Point3D((double)f9 - point3D3.getX(), (double)f10 - point3D3.getY(), (double)f11 - point3D3.getZ());
            Point3D point3D6 = new Point3D((double)f12 - point3D3.getX(), (double)f13 - point3D3.getY(), (double)f15 - point3D3.getZ());
            Point3D point3D7 = point3D5.subtract(point3D4);
            Point3D point3D8 = point3D7.crossProduct(point3D = point3D6.subtract(point3D4));
            if (point3D8.getZ() < 0.0) {
                point3D8 = new Point3D(-point3D8.getX(), -point3D8.getY(), -point3D8.getZ());
            }
            Point3D point3D9 = point3D8.crossProduct(Rotate.Z_AXIS);
            double d17 = Math.atan2(point3D9.magnitude(), point3D8.dotProduct(Rotate.Z_AXIS));
            Rotate rotate = new Rotate(Math.toDegrees(d17), point3D9);
            Point3D point3D10 = rotate.transform(point3D4);
            Point3D point3D11 = rotate.transform(point3D5);
            Point3D point3D12 = rotate.transform(point3D6);
            Point3D point3D13 = rotate.transform(point3D2.subtract(point3D3));
            Point2D point2D = new Point2D(point3D10.getX(), point3D10.getY());
            Point2D point2D2 = new Point2D(point3D11.getX(), point3D11.getY());
            Point2D point2D3 = new Point2D(point3D12.getX(), point3D12.getY());
            Point2D point2D4 = new Point2D(point3D13.getX(), point3D13.getY());
            int n8 = this.getVertexFormat().getTexCoordElementSize();
            int n9 = this.getVertexFormat().getTexCoordIndexOffset();
            int n10 = this.faces.get(n2 + n9) * n8;
            int n11 = this.faces.get(n2 + n3 + n9) * n8;
            int n12 = this.faces.get(n2 + n3 * 2 + n9) * n8;
            Point2D point2D5 = new Point2D(this.texCoords.get(n10), this.texCoords.get(n10 + 1));
            Point2D point2D6 = new Point2D(this.texCoords.get(n11), this.texCoords.get(n11 + 1));
            Point2D point2D7 = new Point2D(this.texCoords.get(n12), this.texCoords.get(n12 + 1));
            Point2D point2D8 = this.computeCentroid(point2D5, point2D6, point2D7);
            Point2D point2D9 = point2D5.subtract(point2D8);
            Point2D point2D10 = point2D6.subtract(point2D8);
            Point2D point2D11 = point2D7.subtract(point2D8);
            Affine affine = new Affine(point2D.getX(), point2D2.getX(), point2D3.getX(), point2D.getY(), point2D2.getY(), point2D3.getY());
            Affine affine2 = new Affine(point2D9.getX(), point2D10.getX(), point2D11.getX(), point2D9.getY(), point2D10.getY(), point2D11.getY());
            Point2D point2D12 = null;
            try {
                affine.invert();
                affine2.append(affine);
                point2D12 = point2D8.add(affine2.transform(point2D4));
            }
            catch (NonInvertibleTransformException nonInvertibleTransformException) {
                // empty catch block
            }
            pickResultChooser.offer(node, d15, bl ? n2 / this.getFaceElementSize() : -1, point3D2, point2D12);
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResultChooser, Node node, CullFace cullFace, boolean bl) {
        boolean bl2 = false;
        if (this.validate()) {
            int n2 = this.faces.size();
            Vec3d vec3d = pickRay.getOriginNoClone();
            Vec3d vec3d2 = pickRay.getDirectionNoClone();
            for (int i2 = 0; i2 < n2; i2 += this.getFaceElementSize()) {
                if (!this.computeIntersectsFace(pickRay, vec3d, vec3d2, i2, cullFace, node, bl, pickResultChooser)) continue;
                bl2 = true;
            }
        }
        return bl2;
    }

    private static class Listener<T extends ObservableArray<T>>
    implements ArrayChangeListener<T>,
    FloatArraySyncer,
    IntegerArraySyncer {
        protected final T array;
        protected boolean dirty = true;
        protected boolean dirtyInFull = true;
        protected int dirtyRangeFrom;
        protected int dirtyRangeLength;
        final /* synthetic */ TriangleMesh this$0;

        public Listener(T t2) {
            this.this$0 = var1_1;
            this.array = t2;
            t2.addListener(this);
        }

        protected final void addDirtyRange(int n2, int n3) {
            if (n3 > 0 && !this.dirtyInFull) {
                this.markDirty();
                if (this.dirtyRangeLength == 0) {
                    this.dirtyRangeFrom = n2;
                    this.dirtyRangeLength = n3;
                } else {
                    int n4 = Math.min(this.dirtyRangeFrom, n2);
                    int n5 = Math.max(this.dirtyRangeFrom + this.dirtyRangeLength, n2 + n3);
                    this.dirtyRangeFrom = n4;
                    this.dirtyRangeLength = n5 - n4;
                }
            }
        }

        protected void markDirty() {
            this.dirty = true;
            this.this$0.setDirty(true);
        }

        @Override
        public void onChanged(T t2, boolean bl, int n2, int n3) {
            if (bl) {
                this.setDirty(true);
            } else {
                this.addDirtyRange(n2, n3 - n2);
            }
            this.this$0.isValidDirty = true;
        }

        public final void setDirty(boolean bl) {
            this.dirtyInFull = bl;
            if (bl) {
                this.markDirty();
                this.dirtyRangeFrom = 0;
                this.dirtyRangeLength = this.array.size();
            } else {
                this.dirty = false;
                this.dirtyRangeLength = 0;
                this.dirtyRangeFrom = 0;
            }
        }

        @Override
        public float[] syncTo(float[] arrf, int[] arrn) {
            assert (arrn != null && arrn.length == 2);
            ObservableFloatArray observableFloatArray = (ObservableFloatArray)this.array;
            if (this.dirtyInFull || arrf == null || arrf.length != observableFloatArray.size()) {
                arrn[0] = 0;
                arrn[1] = observableFloatArray.size();
                return observableFloatArray.toArray(null);
            }
            arrn[0] = this.dirtyRangeFrom;
            arrn[1] = this.dirtyRangeLength;
            observableFloatArray.copyTo(this.dirtyRangeFrom, arrf, this.dirtyRangeFrom, this.dirtyRangeLength);
            return arrf;
        }

        @Override
        public int[] syncTo(int[] arrn, int[] arrn2) {
            assert (arrn2 != null && arrn2.length == 2);
            ObservableIntegerArray observableIntegerArray = (ObservableIntegerArray)this.array;
            if (this.dirtyInFull || arrn == null || arrn.length != observableIntegerArray.size()) {
                arrn2[0] = 0;
                arrn2[1] = observableIntegerArray.size();
                return observableIntegerArray.toArray(null);
            }
            arrn2[0] = this.dirtyRangeFrom;
            arrn2[1] = this.dirtyRangeLength;
            observableIntegerArray.copyTo(this.dirtyRangeFrom, arrn, this.dirtyRangeFrom, this.dirtyRangeLength);
            return arrn;
        }
    }
}

