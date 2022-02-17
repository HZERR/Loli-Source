/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import java.util.HashMap;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

final class PredefinedMeshManager {
    private static final PredefinedMeshManager INSTANCE = new PredefinedMeshManager();
    private static final int INITAL_CAPACITY = 17;
    private static final float LOAD_FACTOR = 0.75f;
    private HashMap<Integer, TriangleMesh> boxCache = null;
    private HashMap<Integer, TriangleMesh> sphereCache = null;
    private HashMap<Integer, TriangleMesh> cylinderCache = null;

    private PredefinedMeshManager() {
    }

    static PredefinedMeshManager getInstance() {
        return INSTANCE;
    }

    synchronized TriangleMesh getBoxMesh(float f2, float f3, float f4, int n2) {
        TriangleMesh triangleMesh;
        if (this.boxCache == null) {
            this.boxCache = BoxCacheLoader.INSTANCE;
        }
        if ((triangleMesh = this.boxCache.get(n2)) == null) {
            triangleMesh = Box.createMesh(f2, f3, f4);
            this.boxCache.put(n2, triangleMesh);
        } else {
            triangleMesh.incRef();
        }
        return triangleMesh;
    }

    synchronized TriangleMesh getSphereMesh(float f2, int n2, int n3) {
        TriangleMesh triangleMesh;
        if (this.sphereCache == null) {
            this.sphereCache = SphereCacheLoader.INSTANCE;
        }
        if ((triangleMesh = this.sphereCache.get(n3)) == null) {
            triangleMesh = Sphere.createMesh(n2, f2);
            this.sphereCache.put(n3, triangleMesh);
        } else {
            triangleMesh.incRef();
        }
        return triangleMesh;
    }

    synchronized TriangleMesh getCylinderMesh(float f2, float f3, int n2, int n3) {
        TriangleMesh triangleMesh;
        if (this.cylinderCache == null) {
            this.cylinderCache = CylinderCacheLoader.INSTANCE;
        }
        if ((triangleMesh = this.cylinderCache.get(n3)) == null) {
            triangleMesh = Cylinder.createMesh(n2, f2, f3);
            this.cylinderCache.put(n3, triangleMesh);
        } else {
            triangleMesh.incRef();
        }
        return triangleMesh;
    }

    synchronized void invalidateBoxMesh(int n2) {
        TriangleMesh triangleMesh;
        if (this.boxCache != null && (triangleMesh = this.boxCache.get(n2)) != null) {
            triangleMesh.decRef();
            int n3 = triangleMesh.getRefCount();
            if (n3 == 0) {
                this.boxCache.remove(n2);
            }
        }
    }

    synchronized void invalidateSphereMesh(int n2) {
        TriangleMesh triangleMesh;
        if (this.sphereCache != null && (triangleMesh = this.sphereCache.get(n2)) != null) {
            triangleMesh.decRef();
            int n3 = triangleMesh.getRefCount();
            if (n3 == 0) {
                this.sphereCache.remove(n2);
            }
        }
    }

    synchronized void invalidateCylinderMesh(int n2) {
        TriangleMesh triangleMesh;
        if (this.cylinderCache != null && (triangleMesh = this.cylinderCache.get(n2)) != null) {
            triangleMesh.decRef();
            int n3 = triangleMesh.getRefCount();
            if (n3 == 0) {
                this.cylinderCache.remove(n2);
            }
        }
    }

    synchronized void dispose() {
        if (this.boxCache != null) {
            this.boxCache.clear();
        }
        if (this.sphereCache != null) {
            this.sphereCache.clear();
        }
        if (this.cylinderCache != null) {
            this.cylinderCache.clear();
        }
    }

    synchronized void printStats() {
        if (this.boxCache != null) {
            System.out.println("BoxCache size:  " + this.boxCache.size());
        }
        if (this.sphereCache != null) {
            System.out.println("SphereCache size:    " + this.sphereCache.size());
        }
        if (this.cylinderCache != null) {
            System.out.println("CylinderCache size:    " + this.cylinderCache.size());
        }
    }

    private static final class CylinderCacheLoader {
        private static final HashMap<Integer, TriangleMesh> INSTANCE = new HashMap(17, 0.75f);

        private CylinderCacheLoader() {
        }
    }

    private static final class SphereCacheLoader {
        private static final HashMap<Integer, TriangleMesh> INSTANCE = new HashMap(17, 0.75f);

        private SphereCacheLoader() {
        }
    }

    private static final class BoxCacheLoader {
        private static final HashMap<Integer, TriangleMesh> INSTANCE = new HashMap(17, 0.75f);

        private BoxCacheLoader() {
        }
    }
}

