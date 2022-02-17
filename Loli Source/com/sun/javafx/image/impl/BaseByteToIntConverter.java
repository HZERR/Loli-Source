/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class BaseByteToIntConverter
implements ByteToIntPixelConverter {
    protected final BytePixelGetter getter;
    protected final IntPixelSetter setter;
    protected final int nSrcElems;

    BaseByteToIntConverter(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
        this.getter = bytePixelGetter;
        this.setter = intPixelSetter;
        this.nSrcElems = bytePixelGetter.getNumElements();
    }

    public final BytePixelGetter getGetter() {
        return this.getter;
    }

    public final IntPixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(byte[] arrby, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        this.doConvert(arrby, n2, n3, arrn, n4, n5, n6, n7);
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        if (byteBuffer.hasArray() && intBuffer.hasArray()) {
            this.doConvert(byteBuffer.array(), n2 += byteBuffer.arrayOffset(), n3, intBuffer.array(), n4 += intBuffer.arrayOffset(), n5, n6, n7);
        } else {
            this.doConvert(byteBuffer, n2, n3, intBuffer, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby = byteBuffer.array();
            this.doConvert(arrby, n2 += byteBuffer.arrayOffset(), n3, arrn, n4, n5, n6, n7);
        } else {
            IntBuffer intBuffer = IntBuffer.wrap(arrn);
            this.doConvert(byteBuffer, n2, n3, intBuffer, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(byte[] arrby, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn = intBuffer.array();
            this.doConvert(arrby, n2, n3, arrn, n4 += intBuffer.arrayOffset(), n5, n6, n7);
        } else {
            ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
            this.doConvert(byteBuffer, n2, n3, intBuffer, n4, n5, n6, n7);
        }
    }
}

