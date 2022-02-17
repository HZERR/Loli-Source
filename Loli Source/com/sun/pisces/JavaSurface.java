/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.AbstractSurface;
import java.nio.IntBuffer;

public final class JavaSurface
extends AbstractSurface {
    private IntBuffer dataBuffer;
    private int[] dataInt;

    public JavaSurface(int[] arrn, int n2, int n3, int n4) {
        super(n3, n4);
        if (arrn.length / n3 < n4) {
            throw new IllegalArgumentException("width(=" + n3 + ") * height(=" + n4 + ") is greater than dataInt.length(=" + arrn.length + ")");
        }
        this.dataInt = arrn;
        this.dataBuffer = IntBuffer.wrap(this.dataInt);
        this.initialize(n2, n3, n4);
    }

    public IntBuffer getDataIntBuffer() {
        return this.dataBuffer;
    }

    private native void initialize(int var1, int var2, int var3);
}

