/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.prism.Mesh;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.MeshTempState;
import com.sun.prism.impl.MeshUtil;
import com.sun.prism.impl.MeshVertex;
import com.sun.prism.impl.PrismSettings;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.shape.VertexFormat;
import sun.util.logging.PlatformLogger;

public abstract class BaseMesh
extends BaseGraphicsResource
implements Mesh {
    private int nVerts;
    private int nTVerts;
    private int nFaces;
    private float[] pos;
    private float[] uv;
    private int[] faces;
    private int[] smoothing;
    private boolean allSameSmoothing;
    private boolean allHardEdges;
    protected static final int POINT_SIZE = 3;
    protected static final int NORMAL_SIZE = 3;
    protected static final int TEXCOORD_SIZE = 2;
    protected static final int POINT_SIZE_VB = 3;
    protected static final int TEXCOORD_SIZE_VB = 2;
    protected static final int NORMAL_SIZE_VB = 4;
    protected static final int VERTEX_SIZE_VB = 9;
    public static final int FACE_MEMBERS_SIZE = 7;
    private boolean[] dirtyVertices;
    private float[] cachedNormals;
    private float[] cachedTangents;
    private float[] cachedBitangents;
    private float[] vertexBuffer;
    private int[] indexBuffer;
    private short[] indexBufferShort;
    private int indexBufferSize;
    private int numberOfVertices;
    private HashMap<Integer, MeshGeomComp2VB> point2vbMap;
    private HashMap<Integer, MeshGeomComp2VB> normal2vbMap;
    private HashMap<Integer, MeshGeomComp2VB> texCoord2vbMap;

    protected BaseMesh(Disposer.Record record) {
        super(record);
    }

    public abstract boolean buildNativeGeometry(float[] var1, int var2, int[] var3, int var4);

    public abstract boolean buildNativeGeometry(float[] var1, int var2, short[] var3, int var4);

    private boolean updateSkipMeshNormalGeometry(int[] arrn, int[] arrn2) {
        int n2;
        int n3;
        int n4;
        int n5 = arrn2[0] / 2;
        int n6 = arrn2[1] / 2;
        if (arrn2[1] % 2 > 0) {
            ++n6;
        }
        if (n6 > 0) {
            for (n4 = 0; n4 < n6; ++n4) {
                int n7;
                n3 = (n5 + n4) * 2;
                MeshGeomComp2VB meshGeomComp2VB = this.texCoord2vbMap.get(n3);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn3 = meshGeomComp2VB.getLocs();
                int n8 = meshGeomComp2VB.getValidLocs();
                if (arrn3 != null) {
                    for (n7 = 0; n7 < n8; ++n7) {
                        n2 = arrn3[n7] * 9 + 3;
                        this.vertexBuffer[n2] = this.uv[n3];
                        this.vertexBuffer[n2 + 1] = this.uv[n3 + 1];
                    }
                    continue;
                }
                n7 = meshGeomComp2VB.getLoc();
                n2 = n7 * 9 + 3;
                this.vertexBuffer[n2] = this.uv[n3];
                this.vertexBuffer[n2 + 1] = this.uv[n3 + 1];
            }
        }
        n4 = arrn[0] / 3;
        n3 = arrn[1] / 3;
        if (arrn[1] % 3 > 0) {
            ++n3;
        }
        if (n3 > 0) {
            for (int i2 = 0; i2 < n3; ++i2) {
                int n9;
                int n10;
                int n11 = (n4 + i2) * 3;
                MeshGeomComp2VB meshGeomComp2VB = this.point2vbMap.get(n11);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn4 = meshGeomComp2VB.getLocs();
                n2 = meshGeomComp2VB.getValidLocs();
                if (arrn4 != null) {
                    for (n10 = 0; n10 < n2; ++n10) {
                        n9 = arrn4[n10] * 9;
                        this.vertexBuffer[n9] = this.pos[n11];
                        this.vertexBuffer[n9 + 1] = this.pos[n11 + 1];
                        this.vertexBuffer[n9 + 2] = this.pos[n11 + 2];
                    }
                    continue;
                }
                n10 = meshGeomComp2VB.getLoc();
                n9 = n10 * 9;
                this.vertexBuffer[n9] = this.pos[n11];
                this.vertexBuffer[n9 + 1] = this.pos[n11 + 1];
                this.vertexBuffer[n9 + 2] = this.pos[n11 + 2];
            }
        }
        if (this.indexBuffer != null) {
            return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.nFaces * 3);
        }
        return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.nFaces * 3);
    }

    private boolean buildSkipMeshNormalGeometry() {
        int n2;
        int n3;
        HashMap<Long, Integer> hashMap = new HashMap<Long, Integer>();
        if (this.point2vbMap == null) {
            this.point2vbMap = new HashMap();
        } else {
            this.point2vbMap.clear();
        }
        if (this.texCoord2vbMap == null) {
            this.texCoord2vbMap = new HashMap();
        } else {
            this.texCoord2vbMap.clear();
        }
        this.vertexBuffer = new float[this.nVerts * 9];
        this.indexBuffer = new int[this.nFaces * 3];
        int n4 = 0;
        int n5 = 0;
        for (n3 = 0; n3 < this.nFaces; ++n3) {
            n2 = n3 * 6;
            for (int i2 = 0; i2 < 3; ++i2) {
                int n6 = i2 * 2;
                int n7 = n2 + n6;
                int n8 = n7 + 1;
                long l2 = (long)this.faces[n7] << 32 | (long)this.faces[n8];
                Integer n9 = (Integer)hashMap.get(l2);
                if (n9 == null) {
                    n9 = n5 / 9;
                    hashMap.put(l2, n9);
                    if (this.vertexBuffer.length <= n5) {
                        float[] arrf = new float[n5 + 90];
                        System.arraycopy(this.vertexBuffer, 0, arrf, 0, this.vertexBuffer.length);
                        this.vertexBuffer = arrf;
                    }
                    int n10 = this.faces[n7] * 3;
                    int n11 = this.faces[n8] * 2;
                    this.vertexBuffer[n5] = this.pos[n10];
                    this.vertexBuffer[n5 + 1] = this.pos[n10 + 1];
                    this.vertexBuffer[n5 + 2] = this.pos[n10 + 2];
                    this.vertexBuffer[n5 + 3] = this.uv[n11];
                    this.vertexBuffer[n5 + 4] = this.uv[n11 + 1];
                    this.vertexBuffer[n5 + 5] = 0.0f;
                    this.vertexBuffer[n5 + 6] = 0.0f;
                    this.vertexBuffer[n5 + 7] = 0.0f;
                    this.vertexBuffer[n5 + 8] = 0.0f;
                    n5 += 9;
                    MeshGeomComp2VB meshGeomComp2VB = this.point2vbMap.get(n10);
                    if (meshGeomComp2VB == null) {
                        meshGeomComp2VB = new MeshGeomComp2VB(n10, n9);
                        this.point2vbMap.put(n10, meshGeomComp2VB);
                    } else {
                        meshGeomComp2VB.addLoc(n9);
                    }
                    MeshGeomComp2VB meshGeomComp2VB2 = this.texCoord2vbMap.get(n11);
                    if (meshGeomComp2VB2 == null) {
                        meshGeomComp2VB2 = new MeshGeomComp2VB(n11, n9);
                        this.texCoord2vbMap.put(n11, meshGeomComp2VB2);
                    } else {
                        meshGeomComp2VB2.addLoc(n9);
                    }
                }
                this.indexBuffer[n4++] = n9;
            }
        }
        this.numberOfVertices = n5 / 9;
        if (this.numberOfVertices > 65536) {
            return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.nFaces * 3);
        }
        if (this.indexBufferShort == null || this.indexBufferShort.length < this.nFaces * 3) {
            this.indexBufferShort = new short[this.nFaces * 3];
        }
        n3 = 0;
        for (n2 = 0; n2 < this.nFaces; ++n2) {
            this.indexBufferShort[n3] = (short)this.indexBuffer[n3++];
            this.indexBufferShort[n3] = (short)this.indexBuffer[n3++];
            this.indexBufferShort[n3] = (short)this.indexBuffer[n3++];
        }
        this.indexBuffer = null;
        return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.nFaces * 3);
    }

    private void convertNormalsToQuats(MeshTempState meshTempState, int n2, float[] arrf, float[] arrf2, float[] arrf3, float[] arrf4, boolean[] arrbl) {
        Vec3f vec3f = meshTempState.vec3f1;
        Vec3f vec3f2 = meshTempState.vec3f2;
        Vec3f vec3f3 = meshTempState.vec3f3;
        int n3 = 0;
        int n4 = 0;
        while (n3 < n2) {
            if (arrbl == null || arrbl[n3]) {
                int n5 = n3 * 3;
                vec3f.x = arrf[n5];
                vec3f.y = arrf[n5 + 1];
                vec3f.z = arrf[n5 + 2];
                vec3f.normalize();
                vec3f2.x = arrf2[n5];
                vec3f2.y = arrf2[n5 + 1];
                vec3f2.z = arrf2[n5 + 2];
                vec3f3.x = arrf3[n5];
                vec3f3.y = arrf3[n5 + 1];
                vec3f3.z = arrf3[n5 + 2];
                meshTempState.triNormals[0].set(vec3f);
                meshTempState.triNormals[1].set(vec3f2);
                meshTempState.triNormals[2].set(vec3f3);
                MeshUtil.fixTSpace(meshTempState.triNormals);
                this.buildVSQuat(meshTempState.triNormals, meshTempState.quat);
                arrf4[n4 + 5] = meshTempState.quat.x;
                arrf4[n4 + 6] = meshTempState.quat.y;
                arrf4[n4 + 7] = meshTempState.quat.z;
                arrf4[n4 + 8] = meshTempState.quat.w;
            }
            ++n3;
            n4 += 9;
        }
    }

    private boolean doBuildPNTGeometry(float[] arrf, float[] arrf2, float[] arrf3, int[] arrn) {
        int n2;
        int n3;
        if (this.point2vbMap == null) {
            this.point2vbMap = new HashMap();
        } else {
            this.point2vbMap.clear();
        }
        if (this.normal2vbMap == null) {
            this.normal2vbMap = new HashMap();
        } else {
            this.normal2vbMap.clear();
        }
        if (this.texCoord2vbMap == null) {
            this.texCoord2vbMap = new HashMap();
        } else {
            this.texCoord2vbMap.clear();
        }
        int n4 = VertexFormat.POINT_NORMAL_TEXCOORD.getVertexIndexSize();
        int n5 = n4 * 3;
        int n6 = VertexFormat.POINT_NORMAL_TEXCOORD.getPointIndexOffset();
        int n7 = VertexFormat.POINT_NORMAL_TEXCOORD.getNormalIndexOffset();
        int n8 = VertexFormat.POINT_NORMAL_TEXCOORD.getTexCoordIndexOffset();
        int n9 = arrf.length / 3;
        int n10 = arrf2.length / 3;
        int n11 = arrf3.length / 2;
        int n12 = arrn.length / n5;
        assert (n9 > 0 && n10 > 0 && n11 > 0 && n12 > 0);
        this.cachedNormals = new float[n9 * 3];
        this.cachedTangents = new float[n9 * 3];
        this.cachedBitangents = new float[n9 * 3];
        this.vertexBuffer = new float[n9 * 9];
        this.indexBuffer = new int[n12 * 3];
        int n13 = 0;
        int n14 = 0;
        MeshTempState meshTempState = MeshTempState.getInstance();
        for (n3 = 0; n3 < 3; ++n3) {
            if (meshTempState.triPoints[n3] == null) {
                meshTempState.triPoints[n3] = new Vec3f();
            }
            if (meshTempState.triTexCoords[n3] != null) continue;
            meshTempState.triTexCoords[n3] = new Vec2f();
        }
        for (n3 = 0; n3 < n12; ++n3) {
            int n15;
            int n16;
            n2 = n3 * n5;
            for (n16 = 0; n16 < 3; ++n16) {
                int n17;
                n15 = n2 + n16 * n4;
                int n18 = n15 + n6;
                int n19 = n15 + n7;
                int n20 = n15 + n8;
                Integer n21 = n14 / 9;
                if (this.vertexBuffer.length <= n14) {
                    n17 = n14 + 20;
                    float[] arrf4 = new float[n17 * 9];
                    System.arraycopy(this.vertexBuffer, 0, arrf4, 0, this.vertexBuffer.length);
                    this.vertexBuffer = arrf4;
                    arrf4 = new float[n17 * 3];
                    System.arraycopy(this.cachedNormals, 0, arrf4, 0, this.cachedNormals.length);
                    this.cachedNormals = arrf4;
                    arrf4 = new float[n17 * 3];
                    System.arraycopy(this.cachedTangents, 0, arrf4, 0, this.cachedTangents.length);
                    this.cachedTangents = arrf4;
                    arrf4 = new float[n17 * 3];
                    System.arraycopy(this.cachedBitangents, 0, arrf4, 0, this.cachedBitangents.length);
                    this.cachedBitangents = arrf4;
                }
                n17 = arrn[n18] * 3;
                int n22 = arrn[n19] * 3;
                int n23 = arrn[n20] * 2;
                meshTempState.triPointIndex[n16] = n17;
                meshTempState.triTexCoordIndex[n16] = n23;
                meshTempState.triVerts[n16] = n14 / 9;
                this.vertexBuffer[n14] = arrf[n17];
                this.vertexBuffer[n14 + 1] = arrf[n17 + 1];
                this.vertexBuffer[n14 + 2] = arrf[n17 + 2];
                this.vertexBuffer[n14 + 3] = arrf3[n23];
                this.vertexBuffer[n14 + 4] = arrf3[n23 + 1];
                int n24 = meshTempState.triVerts[n16] * 3;
                this.cachedNormals[n24] = arrf2[n22];
                this.cachedNormals[n24 + 1] = arrf2[n22 + 1];
                this.cachedNormals[n24 + 2] = arrf2[n22 + 2];
                n14 += 9;
                MeshGeomComp2VB meshGeomComp2VB = this.point2vbMap.get(n17);
                if (meshGeomComp2VB == null) {
                    meshGeomComp2VB = new MeshGeomComp2VB(n17, n21);
                    this.point2vbMap.put(n17, meshGeomComp2VB);
                } else {
                    meshGeomComp2VB.addLoc(n21);
                }
                MeshGeomComp2VB meshGeomComp2VB2 = this.normal2vbMap.get(n22);
                if (meshGeomComp2VB2 == null) {
                    meshGeomComp2VB2 = new MeshGeomComp2VB(n22, n21);
                    this.normal2vbMap.put(n22, meshGeomComp2VB2);
                } else {
                    meshGeomComp2VB2.addLoc(n21);
                }
                MeshGeomComp2VB meshGeomComp2VB3 = this.texCoord2vbMap.get(n23);
                if (meshGeomComp2VB3 == null) {
                    meshGeomComp2VB3 = new MeshGeomComp2VB(n23, n21);
                    this.texCoord2vbMap.put(n23, meshGeomComp2VB3);
                } else {
                    meshGeomComp2VB3.addLoc(n21);
                }
                this.indexBuffer[n13++] = n21;
            }
            for (n16 = 0; n16 < 3; ++n16) {
                meshTempState.triPoints[n16].x = arrf[meshTempState.triPointIndex[n16]];
                meshTempState.triPoints[n16].y = arrf[meshTempState.triPointIndex[n16] + 1];
                meshTempState.triPoints[n16].z = arrf[meshTempState.triPointIndex[n16] + 2];
                meshTempState.triTexCoords[n16].x = arrf3[meshTempState.triTexCoordIndex[n16]];
                meshTempState.triTexCoords[n16].y = arrf3[meshTempState.triTexCoordIndex[n16] + 1];
            }
            MeshUtil.computeTBNNormalized(meshTempState.triPoints[0], meshTempState.triPoints[1], meshTempState.triPoints[2], meshTempState.triTexCoords[0], meshTempState.triTexCoords[1], meshTempState.triTexCoords[2], meshTempState.triNormals);
            for (n16 = 0; n16 < 3; ++n16) {
                n15 = meshTempState.triVerts[n16] * 3;
                this.cachedTangents[n15] = meshTempState.triNormals[1].x;
                this.cachedTangents[n15 + 1] = meshTempState.triNormals[1].y;
                this.cachedTangents[n15 + 2] = meshTempState.triNormals[1].z;
                this.cachedBitangents[n15] = meshTempState.triNormals[2].x;
                this.cachedBitangents[n15 + 1] = meshTempState.triNormals[2].y;
                this.cachedBitangents[n15 + 2] = meshTempState.triNormals[2].z;
            }
        }
        this.numberOfVertices = n14 / 9;
        this.convertNormalsToQuats(meshTempState, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, null);
        this.indexBufferSize = n12 * 3;
        if (this.numberOfVertices > 65536) {
            return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
        }
        if (this.indexBufferShort == null || this.indexBufferShort.length < this.indexBufferSize) {
            this.indexBufferShort = new short[this.indexBufferSize];
        }
        n3 = 0;
        for (n2 = 0; n2 < n12; ++n2) {
            this.indexBufferShort[n3] = (short)this.indexBuffer[n3++];
            this.indexBufferShort[n3] = (short)this.indexBuffer[n3++];
            this.indexBufferShort[n3] = (short)this.indexBuffer[n3++];
        }
        this.indexBuffer = null;
        return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
    }

    private boolean updatePNTGeometry(float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, float[] arrf3, int[] arrn3) {
        int n2;
        int n3;
        int n4;
        int n5;
        if (this.dirtyVertices == null) {
            this.dirtyVertices = new boolean[this.numberOfVertices];
        }
        Arrays.fill(this.dirtyVertices, false);
        int n6 = arrn[0] / 3;
        int n7 = arrn[1] / 3;
        if (arrn[1] % 3 > 0) {
            ++n7;
        }
        if (n7 > 0) {
            for (n5 = 0; n5 < n7; ++n5) {
                int n8;
                n4 = (n6 + n5) * 3;
                MeshGeomComp2VB meshGeomComp2VB = this.point2vbMap.get(n4);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn4 = meshGeomComp2VB.getLocs();
                int n9 = meshGeomComp2VB.getValidLocs();
                if (arrn4 != null) {
                    for (n8 = 0; n8 < n9; ++n8) {
                        n3 = arrn4[n8] * 9;
                        this.vertexBuffer[n3] = arrf[n4];
                        this.vertexBuffer[n3 + 1] = arrf[n4 + 1];
                        this.vertexBuffer[n3 + 2] = arrf[n4 + 2];
                        this.dirtyVertices[arrn4[n8]] = true;
                    }
                    continue;
                }
                n8 = meshGeomComp2VB.getLoc();
                n3 = n8 * 9;
                this.vertexBuffer[n3] = arrf[n4];
                this.vertexBuffer[n3 + 1] = arrf[n4 + 1];
                this.vertexBuffer[n3 + 2] = arrf[n4 + 2];
                this.dirtyVertices[n8] = true;
            }
        }
        n5 = arrn3[0] / 2;
        n4 = arrn3[1] / 2;
        if (arrn3[1] % 2 > 0) {
            ++n4;
        }
        if (n4 > 0) {
            for (int i2 = 0; i2 < n4; ++i2) {
                int n10;
                int n11;
                int n12 = (n5 + i2) * 2;
                MeshGeomComp2VB meshGeomComp2VB = this.texCoord2vbMap.get(n12);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn5 = meshGeomComp2VB.getLocs();
                n3 = meshGeomComp2VB.getValidLocs();
                if (arrn5 != null) {
                    for (n11 = 0; n11 < n3; ++n11) {
                        n10 = arrn5[n11] * 9 + 3;
                        this.vertexBuffer[n10] = arrf3[n12];
                        this.vertexBuffer[n10 + 1] = arrf3[n12 + 1];
                        this.dirtyVertices[arrn5[n11]] = true;
                    }
                    continue;
                }
                n11 = meshGeomComp2VB.getLoc();
                n10 = n11 * 9 + 3;
                this.vertexBuffer[n10] = arrf3[n12];
                this.vertexBuffer[n10 + 1] = arrf3[n12 + 1];
                this.dirtyVertices[n11] = true;
            }
        }
        int n13 = arrn2[0] / 3;
        int n14 = arrn2[1] / 3;
        if (arrn2[1] % 3 > 0) {
            ++n14;
        }
        if (n14 > 0) {
            MeshTempState meshTempState = MeshTempState.getInstance();
            for (int i3 = 0; i3 < n14; ++i3) {
                int n15;
                int n16;
                n3 = (n13 + i3) * 3;
                MeshGeomComp2VB meshGeomComp2VB = this.normal2vbMap.get(n3);
                assert (meshGeomComp2VB != null);
                if (meshGeomComp2VB == null) continue;
                int[] arrn6 = meshGeomComp2VB.getLocs();
                int n17 = meshGeomComp2VB.getValidLocs();
                if (arrn6 != null) {
                    for (n16 = 0; n16 < n17; ++n16) {
                        n15 = arrn6[n16] * 3;
                        this.cachedNormals[n15] = arrf2[n3];
                        this.cachedNormals[n15 + 1] = arrf2[n3 + 1];
                        this.cachedNormals[n15 + 2] = arrf2[n3 + 2];
                        this.dirtyVertices[arrn6[n16]] = true;
                    }
                    continue;
                }
                n16 = meshGeomComp2VB.getLoc();
                n15 = n16 * 3;
                this.cachedNormals[n15] = arrf2[n3];
                this.cachedNormals[n15 + 1] = arrf2[n3 + 1];
                this.cachedNormals[n15 + 2] = arrf2[n3 + 2];
                this.dirtyVertices[n16] = true;
            }
        }
        MeshTempState meshTempState = MeshTempState.getInstance();
        for (n2 = 0; n2 < 3; ++n2) {
            if (meshTempState.triPoints[n2] == null) {
                meshTempState.triPoints[n2] = new Vec3f();
            }
            if (meshTempState.triTexCoords[n2] != null) continue;
            meshTempState.triTexCoords[n2] = new Vec2f();
        }
        for (n2 = 0; n2 < this.numberOfVertices; n2 += 3) {
            int n18;
            if (!this.dirtyVertices[n2] && !this.dirtyVertices[n2 + 1] && !this.dirtyVertices[n2 + 2]) continue;
            n3 = n2 * 9;
            for (n18 = 0; n18 < 3; ++n18) {
                meshTempState.triPoints[n18].x = this.vertexBuffer[n3];
                meshTempState.triPoints[n18].y = this.vertexBuffer[n3 + 1];
                meshTempState.triPoints[n18].z = this.vertexBuffer[n3 + 2];
                meshTempState.triTexCoords[n18].x = this.vertexBuffer[n3 + 3];
                meshTempState.triTexCoords[n18].y = this.vertexBuffer[n3 + 3 + 1];
                n3 += 9;
            }
            MeshUtil.computeTBNNormalized(meshTempState.triPoints[0], meshTempState.triPoints[1], meshTempState.triPoints[2], meshTempState.triTexCoords[0], meshTempState.triTexCoords[1], meshTempState.triTexCoords[2], meshTempState.triNormals);
            n18 = n2 * 3;
            for (int i4 = 0; i4 < 3; ++i4) {
                this.cachedTangents[n18] = meshTempState.triNormals[1].x;
                this.cachedTangents[n18 + 1] = meshTempState.triNormals[1].y;
                this.cachedTangents[n18 + 2] = meshTempState.triNormals[1].z;
                this.cachedBitangents[n18] = meshTempState.triNormals[2].x;
                this.cachedBitangents[n18 + 1] = meshTempState.triNormals[2].y;
                this.cachedBitangents[n18 + 2] = meshTempState.triNormals[2].z;
                n18 += 3;
            }
        }
        this.convertNormalsToQuats(meshTempState, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, this.dirtyVertices);
        if (this.indexBuffer != null) {
            return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
        }
        return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
    }

    @Override
    public boolean buildGeometry(boolean bl, float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, float[] arrf3, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int[] arrn7) {
        if (bl) {
            return this.buildPNTGeometry(arrf, arrn, arrf2, arrn2, arrf3, arrn3, arrn4, arrn5);
        }
        return this.buildPTGeometry(arrf, arrn, arrf3, arrn3, arrn4, arrn5, arrn6, arrn7);
    }

    private boolean buildPNTGeometry(float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, float[] arrf3, int[] arrn3, int[] arrn4, int[] arrn5) {
        boolean bl;
        boolean bl2 = arrn[1] > 0;
        boolean bl3 = arrn2[1] > 0;
        boolean bl4 = arrn3[1] > 0;
        boolean bl5 = arrn5[1] > 0;
        boolean bl6 = bl = !bl2 && !bl3 && !bl4 && !bl5;
        if (bl5) {
            bl = true;
        }
        if (!(bl || this.vertexBuffer == null || this.indexBuffer == null && this.indexBufferShort == null)) {
            return this.updatePNTGeometry(arrf, arrn, arrf2, arrn2, arrf3, arrn3);
        }
        return this.doBuildPNTGeometry(arrf, arrf2, arrf3, arrn4);
    }

    private boolean buildPTGeometry(float[] arrf, int[] arrn, float[] arrf2, int[] arrn2, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6) {
        this.nVerts = arrf.length / 3;
        this.nTVerts = arrf2.length / 2;
        this.nFaces = arrn3.length / (VertexFormat.POINT_TEXCOORD.getVertexIndexSize() * 3);
        assert (this.nVerts > 0 && this.nFaces > 0 && this.nTVerts > 0);
        this.pos = arrf;
        this.uv = arrf2;
        this.faces = arrn3;
        Object object = this.smoothing = (Object)(arrn5.length == this.nFaces ? arrn5 : null);
        if (PrismSettings.skipMeshNormalComputation) {
            boolean bl;
            boolean bl2 = arrn[1] > 0;
            boolean bl3 = arrn2[1] > 0;
            boolean bl4 = arrn4[1] > 0;
            boolean bl5 = arrn6[1] > 0;
            boolean bl6 = bl = !bl2 && !bl3 && !bl4 && !bl5;
            if (bl4 || bl5) {
                bl = true;
            }
            if (!(bl || this.vertexBuffer == null || this.indexBuffer == null && this.indexBufferShort == null)) {
                return this.updateSkipMeshNormalGeometry(arrn, arrn2);
            }
            return this.buildSkipMeshNormalGeometry();
        }
        MeshTempState meshTempState = MeshTempState.getInstance();
        if (meshTempState.pool == null || meshTempState.pool.length < this.nFaces * 3) {
            meshTempState.pool = new MeshVertex[this.nFaces * 3];
        }
        if (meshTempState.indexBuffer == null || meshTempState.indexBuffer.length < this.nFaces * 3) {
            meshTempState.indexBuffer = new int[this.nFaces * 3];
        }
        if (meshTempState.pVertex == null || meshTempState.pVertex.length < this.nVerts) {
            meshTempState.pVertex = new MeshVertex[this.nVerts];
        } else {
            Arrays.fill(meshTempState.pVertex, 0, meshTempState.pVertex.length, null);
        }
        this.checkSmoothingGroup();
        this.computeTBNormal(meshTempState.pool, meshTempState.pVertex, meshTempState.indexBuffer);
        int n2 = MeshVertex.processVertices(meshTempState.pVertex, this.nVerts, this.allHardEdges, this.allSameSmoothing);
        if (meshTempState.vertexBuffer == null || meshTempState.vertexBuffer.length < n2 * 9) {
            meshTempState.vertexBuffer = new float[n2 * 9];
        }
        this.buildVertexBuffer(meshTempState.pVertex, meshTempState.vertexBuffer);
        if (n2 > 65536) {
            this.buildIndexBuffer(meshTempState.pool, meshTempState.indexBuffer, null);
            return this.buildNativeGeometry(meshTempState.vertexBuffer, n2 * 9, meshTempState.indexBuffer, this.nFaces * 3);
        }
        if (meshTempState.indexBufferShort == null || meshTempState.indexBufferShort.length < this.nFaces * 3) {
            meshTempState.indexBufferShort = new short[this.nFaces * 3];
        }
        this.buildIndexBuffer(meshTempState.pool, meshTempState.indexBuffer, meshTempState.indexBufferShort);
        return this.buildNativeGeometry(meshTempState.vertexBuffer, n2 * 9, meshTempState.indexBufferShort, this.nFaces * 3);
    }

    private void computeTBNormal(MeshVertex[] arrmeshVertex, MeshVertex[] arrmeshVertex2, int[] arrn) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        int[] arrn2 = meshTempState.smFace;
        int[] arrn3 = meshTempState.triVerts;
        Vec3f[] arrvec3f = meshTempState.triPoints;
        Vec2f[] arrvec2f = meshTempState.triTexCoords;
        Vec3f[] arrvec3f2 = meshTempState.triNormals;
        String string = BaseMesh.class.getName();
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < this.nFaces; ++i2) {
            int n4;
            int n5 = i2 * 3;
            arrn2 = this.getFace(i2, arrn2);
            arrn3[0] = arrn2[FaceMembers.POINT0.ordinal()];
            arrn3[1] = arrn2[FaceMembers.POINT1.ordinal()];
            arrn3[2] = arrn2[FaceMembers.POINT2.ordinal()];
            if (MeshUtil.isDeadFace(arrn3) && PlatformLogger.getLogger(string).isLoggable(PlatformLogger.Level.FINE)) {
                PlatformLogger.getLogger(string).fine("Dead face [" + arrn3[0] + ", " + arrn3[1] + ", " + arrn3[2] + "] @ face group " + i2 + "; nEmptyFaces = " + ++n2);
            }
            for (n4 = 0; n4 < 3; ++n4) {
                arrvec3f[n4] = this.getVertex(arrn3[n4], arrvec3f[n4]);
            }
            arrn3[0] = arrn2[FaceMembers.TEXCOORD0.ordinal()];
            arrn3[1] = arrn2[FaceMembers.TEXCOORD1.ordinal()];
            arrn3[2] = arrn2[FaceMembers.TEXCOORD2.ordinal()];
            for (n4 = 0; n4 < 3; ++n4) {
                arrvec2f[n4] = this.getTVertex(arrn3[n4], arrvec2f[n4]);
            }
            MeshUtil.computeTBNNormalized(arrvec3f[0], arrvec3f[1], arrvec3f[2], arrvec2f[0], arrvec2f[1], arrvec2f[2], arrvec3f2);
            for (n4 = 0; n4 < 3; ++n4) {
                int n6;
                int n7;
                arrmeshVertex[n3] = arrmeshVertex[n3] == null ? new MeshVertex() : arrmeshVertex[n3];
                for (n7 = 0; n7 < 3; ++n7) {
                    arrmeshVertex[n3].norm[n7].set(arrvec3f2[n7]);
                }
                arrmeshVertex[n3].smGroup = arrn2[FaceMembers.SMOOTHING_GROUP.ordinal()];
                arrmeshVertex[n3].fIdx = i2;
                arrmeshVertex[n3].tVert = arrn3[n4];
                arrmeshVertex[n3].index = -1;
                n7 = n4 == 0 ? FaceMembers.POINT0.ordinal() : (n4 == 1 ? FaceMembers.POINT1.ordinal() : FaceMembers.POINT2.ordinal());
                arrmeshVertex[n3].pVert = n6 = arrn2[n7];
                arrn[n5 + n4] = n6;
                arrmeshVertex[n3].next = arrmeshVertex2[n6];
                arrmeshVertex2[n6] = arrmeshVertex[n3];
                ++n3;
            }
        }
    }

    private void buildVSQuat(Vec3f[] arrvec3f, Quat4f quat4f) {
        Vec3f vec3f = MeshTempState.getInstance().vec3f1;
        vec3f.cross(arrvec3f[1], arrvec3f[2]);
        float f2 = arrvec3f[0].dot(vec3f);
        if (f2 < 0.0f) {
            arrvec3f[2].mul(-1.0f);
        }
        MeshUtil.buildQuat(arrvec3f, quat4f);
        if (f2 < 0.0f) {
            if (quat4f.w == 0.0f) {
                quat4f.w = 1.0E-10f;
            }
            quat4f.scale(-1.0f);
        }
    }

    private void buildVertexBuffer(MeshVertex[] arrmeshVertex, float[] arrf) {
        Quat4f quat4f = MeshTempState.getInstance().quat;
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < this.nVerts; ++i2) {
            MeshVertex meshVertex = arrmeshVertex[i2];
            while (meshVertex != null) {
                if (meshVertex.index == n2) {
                    int n4 = meshVertex.pVert * 3;
                    arrf[n3++] = this.pos[n4];
                    arrf[n3++] = this.pos[n4 + 1];
                    arrf[n3++] = this.pos[n4 + 2];
                    n4 = meshVertex.tVert * 2;
                    arrf[n3++] = this.uv[n4];
                    arrf[n3++] = this.uv[n4 + 1];
                    this.buildVSQuat(meshVertex.norm, quat4f);
                    arrf[n3++] = quat4f.x;
                    arrf[n3++] = quat4f.y;
                    arrf[n3++] = quat4f.z;
                    arrf[n3++] = quat4f.w;
                    ++n2;
                }
                meshVertex = meshVertex.next;
            }
        }
    }

    private void buildIndexBuffer(MeshVertex[] arrmeshVertex, int[] arrn, short[] arrs) {
        for (int i2 = 0; i2 < this.nFaces; ++i2) {
            int n2;
            int n3 = i2 * 3;
            if (arrn[n3] != -1) {
                for (n2 = 0; n2 < 3; ++n2) {
                    assert (arrmeshVertex[n3].fIdx == i2);
                    if (arrs != null) {
                        arrs[n3 + n2] = (short)arrmeshVertex[n3 + n2].index;
                    } else {
                        arrn[n3 + n2] = arrmeshVertex[n3 + n2].index;
                    }
                    arrmeshVertex[n3 + n2].next = null;
                }
                continue;
            }
            for (n2 = 0; n2 < 3; ++n2) {
                if (arrs != null) {
                    arrs[n3 + n2] = 0;
                    continue;
                }
                arrn[n3 + n2] = 0;
            }
        }
    }

    public int getNumVerts() {
        return this.nVerts;
    }

    public int getNumTVerts() {
        return this.nTVerts;
    }

    public int getNumFaces() {
        return this.nFaces;
    }

    public Vec3f getVertex(int n2, Vec3f vec3f) {
        if (vec3f == null) {
            vec3f = new Vec3f();
        }
        int n3 = n2 * 3;
        vec3f.set(this.pos[n3], this.pos[n3 + 1], this.pos[n3 + 2]);
        return vec3f;
    }

    public Vec2f getTVertex(int n2, Vec2f vec2f) {
        if (vec2f == null) {
            vec2f = new Vec2f();
        }
        int n3 = n2 * 2;
        vec2f.set(this.uv[n3], this.uv[n3 + 1]);
        return vec2f;
    }

    private void checkSmoothingGroup() {
        if (this.smoothing == null || this.smoothing.length == 0) {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
            return;
        }
        int n2 = 0;
        while (n2 + 1 < this.smoothing.length) {
            if (this.smoothing[n2] != this.smoothing[n2 + 1]) {
                this.allSameSmoothing = false;
                this.allHardEdges = false;
                return;
            }
            ++n2;
        }
        if (this.smoothing[0] == 0) {
            this.allSameSmoothing = false;
            this.allHardEdges = true;
        } else {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
        }
    }

    public int[] getFace(int n2, int[] arrn) {
        int n3 = n2 * 6;
        if (arrn == null || arrn.length < 7) {
            arrn = new int[7];
        }
        for (int i2 = 0; i2 < 6; ++i2) {
            arrn[i2] = this.faces[n3 + i2];
        }
        arrn[6] = this.smoothing != null ? this.smoothing[n2] : 1;
        return arrn;
    }

    class MeshGeomComp2VB {
        private final int key;
        private final int loc;
        private int[] locs;
        private int validLocs;

        MeshGeomComp2VB(int n2, int n3) {
            assert (n3 >= 0);
            this.key = n2;
            this.loc = n3;
            this.locs = null;
            this.validLocs = 0;
        }

        void addLoc(int n2) {
            if (this.locs == null) {
                this.locs = new int[3];
                this.locs[0] = this.loc;
                this.locs[1] = n2;
                this.validLocs = 2;
            } else if (this.locs.length > this.validLocs) {
                this.locs[this.validLocs] = n2;
                ++this.validLocs;
            } else {
                int[] arrn = new int[this.validLocs * 2];
                System.arraycopy(this.locs, 0, arrn, 0, this.locs.length);
                this.locs = arrn;
                this.locs[this.validLocs] = n2;
                ++this.validLocs;
            }
        }

        int getKey() {
            return this.key;
        }

        int getLoc() {
            return this.loc;
        }

        int[] getLocs() {
            return this.locs;
        }

        int getValidLocs() {
            return this.validLocs;
        }
    }

    public static enum FaceMembers {
        POINT0,
        TEXCOORD0,
        POINT1,
        TEXCOORD1,
        POINT2,
        TEXCOORD2,
        SMOOTHING_GROUP;

    }
}

