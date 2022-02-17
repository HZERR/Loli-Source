/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.sg.prism.NGParallelCamera;

public class NGDefaultCamera
extends NGParallelCamera {
    public void validate(int n2, int n3) {
        if ((double)n2 != this.viewWidth || (double)n3 != this.viewHeight) {
            this.setViewWidth(n2);
            this.setViewHeight(n3);
            double d2 = n2 > n3 ? (double)n2 / 2.0 : (double)n3 / 2.0;
            this.projViewTx.ortho(0.0, n2, n3, 0.0, -d2, d2);
        }
    }
}

