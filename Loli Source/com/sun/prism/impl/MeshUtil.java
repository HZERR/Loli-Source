/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.prism.impl.MeshTempState;

class MeshUtil {
    static final float NORMAL_WELD_COS = 0.9952f;
    static final float TANGENT_WELD_COS = 0.866f;
    static final float G_UV_PARALLEL = 0.9988f;
    static final float COS_1_DEGREE = 0.9998477f;
    static final float BIG_ENOUGH_NORMA2 = 0.0625f;
    static final double PI = Math.PI;
    static final float INV_SQRT2 = 0.70710677f;
    static final float DEAD_FACE = 9.094947E-13f;
    static final float MAGIC_SMALL = 1.0E-10f;
    static final float COS110 = -0.33333334f;

    private MeshUtil() {
    }

    static boolean isDeadFace(float f2) {
        return f2 < 9.094947E-13f;
    }

    static boolean isDeadFace(int[] arrn) {
        return arrn[0] == arrn[1] || arrn[1] == arrn[2] || arrn[2] == arrn[0];
    }

    static boolean isNormalAlmostEqual(Vec3f vec3f, Vec3f vec3f2) {
        return vec3f.dot(vec3f2) >= 0.9998477f;
    }

    static boolean isTangentOk(Vec3f[] arrvec3f, Vec3f[] arrvec3f2) {
        return arrvec3f[0].dot(arrvec3f2[0]) >= 0.9952f && arrvec3f[1].dot(arrvec3f2[1]) >= 0.866f && arrvec3f[2].dot(arrvec3f2[2]) >= 0.866f;
    }

    static boolean isNormalOkAfterWeld(Vec3f vec3f) {
        return vec3f.dot(vec3f) > 0.0625f;
    }

    static boolean isTangentOK(Vec3f[] arrvec3f) {
        return MeshUtil.isTangentOk(arrvec3f, arrvec3f);
    }

    static boolean isOppositeLookingNormals(Vec3f[] arrvec3f, Vec3f[] arrvec3f2) {
        float f2 = arrvec3f[0].dot(arrvec3f2[0]);
        return f2 < -0.33333334f;
    }

    static float fabs(float f2) {
        return f2 < 0.0f ? -f2 : f2;
    }

    static void getOrt(Vec3f vec3f, Vec3f vec3f2) {
        vec3f2.cross(vec3f, vec3f2);
        vec3f2.cross(vec3f2, vec3f);
    }

    static void orthogonalizeTB(Vec3f[] arrvec3f) {
        MeshUtil.getOrt(arrvec3f[0], arrvec3f[1]);
        MeshUtil.getOrt(arrvec3f[0], arrvec3f[2]);
        arrvec3f[1].normalize();
        arrvec3f[2].normalize();
    }

    static void computeTBNNormalized(Vec3f vec3f, Vec3f vec3f2, Vec3f vec3f3, Vec2f vec2f, Vec2f vec2f2, Vec2f vec2f3, Vec3f[] arrvec3f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f4 = meshTempState.vec3f1;
        Vec3f vec3f5 = meshTempState.vec3f2;
        Vec3f vec3f6 = meshTempState.vec3f3;
        vec3f5.sub(vec3f2, vec3f);
        vec3f6.sub(vec3f3, vec3f);
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[0].set(vec3f4);
        arrvec3f[0].normalize();
        vec3f5.set(0.0f, vec2f2.x - vec2f.x, vec2f2.y - vec2f.y);
        vec3f6.set(0.0f, vec2f3.x - vec2f.x, vec2f3.y - vec2f.y);
        if (vec3f5.y * vec3f6.z == vec3f5.z * vec3f6.y) {
            MeshUtil.generateTB(vec3f, vec3f2, vec3f3, arrvec3f);
            return;
        }
        vec3f5.x = vec3f2.x - vec3f.x;
        vec3f6.x = vec3f3.x - vec3f.x;
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[1].x = -vec3f4.y / vec3f4.x;
        arrvec3f[2].x = -vec3f4.z / vec3f4.x;
        vec3f5.x = vec3f2.y - vec3f.y;
        vec3f6.x = vec3f3.y - vec3f.y;
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[1].y = -vec3f4.y / vec3f4.x;
        arrvec3f[2].y = -vec3f4.z / vec3f4.x;
        vec3f5.x = vec3f2.z - vec3f.z;
        vec3f6.x = vec3f3.z - vec3f.z;
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[1].z = -vec3f4.y / vec3f4.x;
        arrvec3f[2].z = -vec3f4.z / vec3f4.x;
        arrvec3f[1].normalize();
        arrvec3f[2].normalize();
    }

    static void fixParallelTB(Vec3f[] arrvec3f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f = meshTempState.vec3f1;
        vec3f.add(arrvec3f[1], arrvec3f[2]);
        Vec3f vec3f2 = meshTempState.vec3f2;
        vec3f2.cross(arrvec3f[0], vec3f);
        vec3f.normalize();
        vec3f2.normalize();
        arrvec3f[1].add(vec3f, vec3f2);
        arrvec3f[1].mul(0.70710677f);
        arrvec3f[2].sub(vec3f, vec3f2);
        arrvec3f[2].mul(0.70710677f);
    }

    static void generateTB(Vec3f vec3f, Vec3f vec3f2, Vec3f vec3f3, Vec3f[] arrvec3f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f4 = meshTempState.vec3f1;
        vec3f4.sub(vec3f2, vec3f);
        Vec3f vec3f5 = meshTempState.vec3f2;
        vec3f5.sub(vec3f3, vec3f);
        if (vec3f4.dot(vec3f4) > vec3f5.dot(vec3f5)) {
            arrvec3f[1].set(vec3f4);
            arrvec3f[1].normalize();
            arrvec3f[2].cross(arrvec3f[0], arrvec3f[1]);
        } else {
            arrvec3f[2].set(vec3f5);
            arrvec3f[2].normalize();
            arrvec3f[1].cross(arrvec3f[2], arrvec3f[0]);
        }
    }

    static double clamp(double d2, double d3, double d4) {
        return d2 < d4 ? (d2 > d3 ? d2 : d3) : d4;
    }

    static void fixTSpace(Vec3f[] arrvec3f) {
        float f2 = arrvec3f[0].length();
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f = meshTempState.vec3f1;
        vec3f.set(arrvec3f[1]);
        Vec3f vec3f2 = meshTempState.vec3f2;
        vec3f2.set(arrvec3f[2]);
        MeshUtil.getOrt(arrvec3f[0], vec3f);
        MeshUtil.getOrt(arrvec3f[0], vec3f2);
        float f3 = vec3f.length();
        float f4 = vec3f2.length();
        double d2 = vec3f.dot(vec3f2) / (f3 * f4);
        Vec3f vec3f3 = meshTempState.vec3f3;
        Vec3f vec3f4 = meshTempState.vec3f4;
        if ((double)MeshUtil.fabs((float)d2) > 0.998) {
            Vec3f vec3f5 = meshTempState.vec3f5;
            vec3f5.cross(arrvec3f[0], vec3f);
            vec3f5.normalize();
            vec3f4.set(vec3f5);
            if (vec3f5.dot(vec3f2) < 0.0f) {
                vec3f4.mul(-1.0f);
            }
            vec3f3.set(vec3f);
            vec3f3.mul(1.0f / f3);
        } else {
            double d3 = Math.acos(MeshUtil.clamp(d2, -1.0, 1.0));
            double d4 = (1.5707963267948966 - d3) * 0.5;
            Vec2f vec2f = meshTempState.vec2f1;
            vec2f.set((float)Math.sin(d4), (float)Math.cos(d4));
            Vec2f vec2f2 = meshTempState.vec2f2;
            vec2f2.set((float)Math.sin(d4 + d3), (float)Math.cos(d4 + d3));
            Vec3f vec3f6 = meshTempState.vec3f5;
            vec3f6.set(vec3f2);
            MeshUtil.getOrt(vec3f, vec3f6);
            float f5 = vec3f6.length();
            vec3f3.set(vec3f);
            vec3f3.mul(vec2f.y / f3);
            Vec3f vec3f7 = meshTempState.vec3f6;
            vec3f7.set(vec3f6);
            vec3f7.mul(vec2f.x / f5);
            vec3f3.sub(vec3f7);
            vec3f4.set(vec3f);
            vec3f4.mul(vec2f2.y / f3);
            vec3f7.set(vec3f6);
            vec3f7.mul(vec2f2.x / f5);
            vec3f4.add(vec3f7);
            float f6 = vec3f3.dot(vec3f);
            float f7 = vec3f4.dot(vec3f2);
        }
        arrvec3f[1].set(vec3f3);
        arrvec3f[2].set(vec3f4);
        arrvec3f[0].mul(1.0f / f2);
    }

    static void buildQuat(Vec3f[] arrvec3f, Quat4f quat4f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        float[][] arrf = meshTempState.matrix;
        float[] arrf2 = meshTempState.vector;
        for (int i2 = 0; i2 < 3; ++i2) {
            arrf[i2][0] = arrvec3f[i2].x;
            arrf[i2][1] = arrvec3f[i2].y;
            arrf[i2][2] = arrvec3f[i2].z;
        }
        float f2 = arrf[0][0] + arrf[1][1] + arrf[2][2];
        if (f2 > 0.0f) {
            float f3 = (float)Math.sqrt(f2 + 1.0f);
            float f4 = 0.5f / f3;
            quat4f.w = 0.5f * f3;
            quat4f.x = (arrf[1][2] - arrf[2][1]) * f4;
            quat4f.y = (arrf[2][0] - arrf[0][2]) * f4;
            quat4f.z = (arrf[0][1] - arrf[1][0]) * f4;
        } else {
            int[] arrn = new int[]{1, 2, 0};
            int n2 = 0;
            if (arrf[1][1] > arrf[0][0]) {
                n2 = 1;
            }
            if (arrf[2][2] > arrf[n2][n2]) {
                n2 = 2;
            }
            int n3 = arrn[n2];
            int n4 = arrn[n3];
            float f5 = (float)Math.sqrt(arrf[n2][n2] - arrf[n3][n3] - arrf[n4][n4] + 1.0f);
            if (arrf[n3][n4] < arrf[n4][n3]) {
                f5 = -f5;
            }
            float f6 = 0.5f / f5;
            arrf2[n2] = 0.5f * f5;
            quat4f.w = (arrf[n3][n4] - arrf[n4][n3]) * f6;
            arrf2[n3] = (arrf[n2][n3] + arrf[n3][n2]) * f6;
            arrf2[n4] = (arrf[n2][n4] + arrf[n4][n2]) * f6;
            quat4f.x = arrf2[0];
            quat4f.y = arrf2[1];
            quat4f.z = arrf2[2];
        }
    }
}

