/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class IntTo4ByteSameConverter
extends BaseIntToByteConverter {
    IntTo4ByteSameConverter(IntPixelGetter intPixelGetter, BytePixelSetter bytePixelSetter) {
        super(intPixelGetter, bytePixelSetter);
    }

    @Override
    void doConvert(int[] arrn, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
        n3 -= n6;
        n5 -= n6 * 4;
        while (--n7 >= 0) {
            for (int i2 = 0; i2 < n6; ++i2) {
                int n8 = arrn[n2++];
                arrby[n4++] = (byte)n8;
                arrby[n4++] = (byte)(n8 >> 8);
                arrby[n4++] = (byte)(n8 >> 16);
                arrby[n4++] = (byte)(n8 >> 24);
            }
            n2 += n3;
            n4 += n5;
        }
    }

    @Override
    void doConvert(IntBuffer intBuffer, int n2, int n3, ByteBuffer byteBuffer, int n4, int n5, int n6, int n7) {
        n5 -= n6 * 4;
        while (--n7 >= 0) {
            for (int i2 = 0; i2 < n6; ++i2) {
                int n8 = intBuffer.get(n2 + i2);
                byteBuffer.put(n4, (byte)n8);
                byteBuffer.put(n4 + 1, (byte)(n8 >> 8));
                byteBuffer.put(n4 + 2, (byte)(n8 >> 16));
                byteBuffer.put(n4 + 3, (byte)(n8 >> 24));
                n4 += 4;
            }
            n2 += n3;
            n4 += n5;
        }
    }
}

