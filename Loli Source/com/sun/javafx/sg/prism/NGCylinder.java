/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.sg.prism.NGShape3D;
import com.sun.javafx.sg.prism.NGTriangleMesh;

public class NGCylinder
extends NGShape3D {
    public void updateMesh(NGTriangleMesh nGTriangleMesh) {
        this.mesh = nGTriangleMesh;
        this.invalidate();
    }
}

