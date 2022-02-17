/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class BaseIntToByteConverter
implements IntToBytePixelConverter {
    protected final IntPixelGetter getter;
    protected final BytePixelSetter setter;
    protected final int nDstElems;

    BaseIntToByteConverter(IntPixelGetter intPixelGetter, BytePixelSetter bytePixelSetter) {
        this.getter = intPixelGetter;
        this.setter = bytePixelSetter;
        this.nDstElems = bytePixelSetter.getNumElements();
    }

    public final IntPixelGetter getGetter() {
        return this.getter;
    }

    public final BytePixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(IntBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(int[] arrn, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        this.doConvert(arrn, n2, n3, arrby, n4, n5, n6, n7);
    }

    @Override
    public final void convert(IntBuffer intBuffer, int n2, int n3, ByteBuffer byteBuffer, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        if (intBuffer.hasArray() && byteBuffer.hasArray()) {
            this.doConvert(intBuffer.array(), n2 += intBuffer.arrayOffset(), n3, byteBuffer.array(), n4 += byteBuffer.arrayOffset(), n5, n6, n7);
        } else {
            this.doConvert(intBuffer, n2, n3, byteBuffer, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(IntBuffer intBuffer, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn = intBuffer.array();
            this.doConvert(arrn, n2 += intBuffer.arrayOffset(), n3, arrby, n4, n5, n6, n7);
        } else {
            ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
            this.doConvert(intBuffer, n2, n3, byteBuffer, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(int[] arrn, int n2, int n3, ByteBuffer byteBuffer, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby = byteBuffer.array();
            this.doConvert(arrn, n2, n3, arrby, n4 += byteBuffer.arrayOffset(), n5, n6, n7);
        } else {
            IntBuffer intBuffer = IntBuffer.wrap(arrn);
            this.doConvert(intBuffer, n2, n3, byteBuffer, n4, n5, n6, n7);
        }
    }
}

