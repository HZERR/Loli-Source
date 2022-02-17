/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Vec3f;
import com.sun.prism.impl.MeshTempState;
import com.sun.prism.impl.MeshUtil;

class MeshVertex {
    int smGroup;
    int pVert;
    int tVert;
    int fIdx;
    int index;
    Vec3f[] norm = new Vec3f[3];
    MeshVertex next = null;
    static final int IDX_UNDEFINED = -1;
    static final int IDX_SET_SMOOTH = -2;
    static final int IDX_UNITE = -3;

    MeshVertex() {
        for (int i2 = 0; i2 < this.norm.length; ++i2) {
            this.norm[i2] = new Vec3f();
        }
    }

    static void avgSmNormals(MeshVertex meshVertex) {
        Vec3f vec3f = MeshTempState.getInstance().vec3f1;
        while (meshVertex != null) {
            if (meshVertex.index == -1) {
                vec3f.set(meshVertex.norm[0]);
                int n2 = meshVertex.smGroup;
                MeshVertex meshVertex2 = meshVertex.next;
                while (meshVertex2 != null) {
                    if (meshVertex2.smGroup == n2) {
                        assert (meshVertex2.index == -1);
                        meshVertex2.index = -2;
                        vec3f.add(meshVertex2.norm[0]);
                    }
                    meshVertex2 = meshVertex2.next;
                }
                if (MeshUtil.isNormalOkAfterWeld(vec3f)) {
                    vec3f.normalize();
                    meshVertex2 = meshVertex;
                    while (meshVertex2 != null) {
                        if (meshVertex2.smGroup == n2) {
                            meshVertex2.norm[0].set(vec3f);
                        }
                        meshVertex2 = meshVertex2.next;
                    }
                }
            }
            meshVertex = meshVertex.next;
        }
    }

    static boolean okToWeldVertsTB(MeshVertex meshVertex, MeshVertex meshVertex2) {
        return meshVertex.tVert == meshVertex2.tVert && MeshUtil.isTangentOk(meshVertex.norm, meshVertex2.norm);
    }

    static int weldWithTB(MeshVertex meshVertex, int n2) {
        Vec3f[] arrvec3f = MeshTempState.getInstance().triNormals;
        while (meshVertex != null) {
            if (meshVertex.index < 0) {
                int n3 = 0;
                for (int i2 = 0; i2 < 3; ++i2) {
                    arrvec3f[i2].set(meshVertex.norm[i2]);
                }
                MeshVertex meshVertex2 = meshVertex.next;
                while (meshVertex2 != null) {
                    if (meshVertex2.index < 0 && MeshVertex.okToWeldVertsTB(meshVertex, meshVertex2)) {
                        meshVertex2.index = -3;
                        ++n3;
                        for (int i3 = 0; i3 < 3; ++i3) {
                            arrvec3f[i3].add(meshVertex2.norm[i3]);
                        }
                    }
                    meshVertex2 = meshVertex2.next;
                }
                if (n3 != 0) {
                    if (MeshUtil.isTangentOK(arrvec3f)) {
                        MeshUtil.fixTSpace(arrvec3f);
                        meshVertex.index = n2;
                        for (int i4 = 0; i4 < 3; ++i4) {
                            meshVertex.norm[i4].set(arrvec3f[i4]);
                        }
                        meshVertex2 = meshVertex.next;
                        while (meshVertex2 != null) {
                            if (meshVertex2.index == -3) {
                                meshVertex2.index = n2;
                                meshVertex2.norm[0].set(0.0f, 0.0f, 0.0f);
                            }
                            meshVertex2 = meshVertex2.next;
                        }
                    } else {
                        n3 = 0;
                    }
                }
                if (n3 == 0) {
                    MeshUtil.fixTSpace(meshVertex.norm);
                    meshVertex.index = n2;
                }
                ++n2;
            }
            meshVertex = meshVertex.next;
        }
        return n2;
    }

    static void mergeSmIndexes(MeshVertex meshVertex) {
        MeshVertex meshVertex2 = meshVertex;
        while (meshVertex2 != null) {
            boolean bl = false;
            MeshVertex meshVertex3 = meshVertex2.next;
            while (meshVertex3 != null) {
                if ((meshVertex2.smGroup & meshVertex3.smGroup) != 0 && meshVertex2.smGroup != meshVertex3.smGroup) {
                    meshVertex3.smGroup = meshVertex2.smGroup = meshVertex3.smGroup | meshVertex2.smGroup;
                    bl = true;
                }
                meshVertex3 = meshVertex3.next;
            }
            if (bl) continue;
            meshVertex2 = meshVertex2.next;
        }
    }

    static void correctSmNormals(MeshVertex meshVertex) {
        MeshVertex meshVertex2 = meshVertex;
        while (meshVertex2 != null) {
            if (meshVertex2.smGroup != 0) {
                MeshVertex meshVertex3 = meshVertex2.next;
                while (meshVertex3 != null) {
                    if ((meshVertex3.smGroup & meshVertex2.smGroup) != 0 && MeshUtil.isOppositeLookingNormals(meshVertex3.norm, meshVertex2.norm)) {
                        meshVertex2.smGroup = 0;
                        meshVertex3.smGroup = 0;
                        break;
                    }
                    meshVertex3 = meshVertex3.next;
                }
            }
            meshVertex2 = meshVertex2.next;
        }
    }

    static int processVertices(MeshVertex[] arrmeshVertex, int n2, boolean bl, boolean bl2) {
        int n3 = 0;
        Vec3f vec3f = MeshTempState.getInstance().vec3f1;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (arrmeshVertex[i2] == null) continue;
            if (!bl) {
                if (bl2) {
                    vec3f.set(arrmeshVertex[i2].norm[0]);
                    MeshVertex meshVertex = arrmeshVertex[i2].next;
                    while (meshVertex != null) {
                        vec3f.add(meshVertex.norm[0]);
                        meshVertex = meshVertex.next;
                    }
                    if (MeshUtil.isNormalOkAfterWeld(vec3f)) {
                        vec3f.normalize();
                        meshVertex = arrmeshVertex[i2];
                        while (meshVertex != null) {
                            meshVertex.norm[0].set(vec3f);
                            meshVertex = meshVertex.next;
                        }
                    }
                } else {
                    MeshVertex.mergeSmIndexes(arrmeshVertex[i2]);
                    MeshVertex.avgSmNormals(arrmeshVertex[i2]);
                }
            }
            n3 = MeshVertex.weldWithTB(arrmeshVertex[i2], n3);
        }
        return n3;
    }

    public String toString() {
        return "MeshVertex : " + this.getClass().getName() + "@0x" + Integer.toHexString(this.hashCode()) + ":: smGroup = " + this.smGroup + "\n" + "\tnorm[0] = " + this.norm[0] + "\n" + "\tnorm[1] = " + this.norm[1] + "\n" + "\tnorm[2] = " + this.norm[2] + "\n" + "\ttIndex = " + this.tVert + ", fIndex = " + this.fIdx + "\n" + "\tpIdx = " + this.index + "\n" + "\tnext = " + (this.next == null ? this.next : this.next.getClass().getName() + "@0x" + Integer.toHexString(this.next.hashCode())) + "\n";
    }

    static void dumpInfo(MeshVertex meshVertex) {
        System.err.println("** dumpInfo: ");
        MeshVertex meshVertex2 = meshVertex;
        while (meshVertex2 != null) {
            System.err.println(meshVertex2);
            meshVertex2 = meshVertex2.next;
        }
        System.err.println("***********************************");
    }
}

