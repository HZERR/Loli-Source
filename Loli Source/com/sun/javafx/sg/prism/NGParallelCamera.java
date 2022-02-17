/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.sg.prism.NGCamera;

public class NGParallelCamera
extends NGCamera {
    @Override
    public PickRay computePickRay(float f2, float f3, PickRay pickRay) {
        return PickRay.computeParallelPickRay(f2, f3, this.viewHeight, this.worldTransform, this.zNear, this.zFar, pickRay);
    }
}

