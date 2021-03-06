/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.pisces.Transform6;

final class SWUtils {
    static final int TO_PISCES = 65536;

    SWUtils() {
    }

    static int fastFloor(float f2) {
        int n2 = (int)f2;
        return f2 < 0.0f && f2 != (float)n2 ? n2 - 1 : n2;
    }

    static int fastCeil(float f2) {
        int n2 = (int)f2;
        return f2 >= 0.0f && f2 != (float)n2 ? n2 + 1 : n2;
    }

    static void convertToPiscesTransform(BaseTransform baseTransform, Transform6 transform6) {
        transform6.m00 = (int)(65536.0 * baseTransform.getMxx());
        transform6.m10 = (int)(65536.0 * baseTransform.getMyx());
        transform6.m01 = (int)(65536.0 * baseTransform.getMxy());
        transform6.m11 = (int)(65536.0 * baseTransform.getMyy());
        transform6.m02 = (int)(65536.0 * baseTransform.getMxt());
        transform6.m12 = (int)(65536.0 * baseTransform.getMyt());
    }
}

