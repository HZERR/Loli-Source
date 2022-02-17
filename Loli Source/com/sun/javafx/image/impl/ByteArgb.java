/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;

public class ByteArgb {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;

    static class Accessor
    implements BytePixelAccessor {
        static final BytePixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override
        public AlphaType getAlphaType() {
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 4;
        }

        @Override
        public int getArgb(byte[] arrby, int n2) {
            return arrby[n2] << 24 | (arrby[n2 + 1] & 0xFF) << 16 | (arrby[n2 + 2] & 0xFF) << 8 | arrby[n2 + 3] & 0xFF;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n2) {
            return PixelUtils.NonPretoPre(this.getArgb(arrby, n2));
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2) {
            return byteBuffer.get(n2) << 24 | (byteBuffer.get(n2 + 1) & 0xFF) << 16 | (byteBuffer.get(n2 + 2) & 0xFF) << 8 | byteBuffer.get(n2 + 3) & 0xFF;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n2) {
            return PixelUtils.NonPretoPre(this.getArgb(byteBuffer, n2));
        }

        @Override
        public void setArgb(byte[] arrby, int n2, int n3) {
            arrby[n2] = (byte)(n3 >> 24);
            arrby[n2 + 1] = (byte)(n3 >> 16);
            arrby[n2 + 2] = (byte)(n3 >> 8);
            arrby[n2 + 3] = (byte)n3;
        }

        @Override
        public void setArgbPre(byte[] arrby, int n2, int n3) {
            this.setArgb(arrby, n2, PixelUtils.PretoNonPre(n3));
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n2, int n3) {
            byteBuffer.put(n2, (byte)(n3 >> 24));
            byteBuffer.put(n2 + 1, (byte)(n3 >> 16));
            byteBuffer.put(n2 + 2, (byte)(n3 >> 8));
            byteBuffer.put(n2 + 3, (byte)n3);
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n2, int n3) {
            this.setArgb(byteBuffer, n2, PixelUtils.PretoNonPre(n3));
        }
    }
}

