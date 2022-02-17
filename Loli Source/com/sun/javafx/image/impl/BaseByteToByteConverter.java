/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import java.nio.ByteBuffer;

abstract class BaseByteToByteConverter
implements ByteToBytePixelConverter {
    protected final BytePixelGetter getter;
    protected final BytePixelSetter setter;
    protected final int nSrcElems;
    protected final int nDstElems;

    BaseByteToByteConverter(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
        this.getter = bytePixelGetter;
        this.setter = bytePixelSetter;
        this.nSrcElems = bytePixelGetter.getNumElements();
        this.nDstElems = bytePixelSetter.getNumElements();
    }

    public final BytePixelGetter getGetter() {
        return this.getter;
    }

    public final BytePixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        this.doConvert(arrby, n2, n3, arrby2, n4, n5, n6, n7);
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        if (byteBuffer.hasArray() && byteBuffer2.hasArray()) {
            this.doConvert(byteBuffer.array(), n2 += byteBuffer.arrayOffset(), n3, byteBuffer2.array(), n4 += byteBuffer2.arrayOffset(), n5, n6, n7);
        } else {
            this.doConvert(byteBuffer, n2, n3, byteBuffer2, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby2 = byteBuffer.array();
            this.doConvert(arrby2, n2 += byteBuffer.arrayOffset(), n3, arrby, n4, n5, n6, n7);
        } else {
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(arrby);
            this.doConvert(byteBuffer, n2, n3, byteBuffer2, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(byte[] arrby, int n2, int n3, ByteBuffer byteBuffer, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 * this.nSrcElems && n5 == n6 * this.nDstElems) {
            n6 *= n7;
            n7 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby2 = byteBuffer.array();
            this.doConvert(arrby, n2, n3, arrby2, n4 += byteBuffer.arrayOffset(), n5, n6, n7);
        } else {
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(arrby);
            this.doConvert(byteBuffer2, n2, n3, byteBuffer, n4, n5, n6, n7);
        }
    }

    static ByteToBytePixelConverter create(BytePixelAccessor bytePixelAccessor) {
        return new ByteAnyToSameConverter(bytePixelAccessor);
    }

    public static ByteToBytePixelConverter createReorderer(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter, int n2, int n3, int n4, int n5) {
        return new FourByteReorderer(bytePixelGetter, bytePixelSetter, n2, n3, n4, n5);
    }

    static class FourByteReorderer
    extends BaseByteToByteConverter {
        private final int c0;
        private final int c1;
        private final int c2;
        private final int c3;

        FourByteReorderer(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter, int n2, int n3, int n4, int n5) {
            super(bytePixelGetter, bytePixelSetter);
            this.c0 = n2;
            this.c1 = n3;
            this.c2 = n4;
            this.c3 = n5;
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by = arrby[n2 + this.c0];
                    byte by2 = arrby[n2 + this.c1];
                    byte by3 = arrby[n2 + this.c2];
                    byte by4 = arrby[n2 + this.c3];
                    arrby2[n4++] = by;
                    arrby2[n4++] = by2;
                    arrby2[n4++] = by3;
                    arrby2[n4++] = by4;
                    n2 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by = byteBuffer.get(n2 + this.c0);
                    byte by2 = byteBuffer.get(n2 + this.c1);
                    byte by3 = byteBuffer.get(n2 + this.c2);
                    byte by4 = byteBuffer.get(n2 + this.c3);
                    byteBuffer2.put(n4, by);
                    byteBuffer2.put(n4 + 1, by2);
                    byteBuffer2.put(n4 + 2, by3);
                    byteBuffer2.put(n4 + 3, by4);
                    n2 += 4;
                    n4 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ByteAnyToSameConverter
    extends BaseByteToByteConverter {
        ByteAnyToSameConverter(BytePixelAccessor bytePixelAccessor) {
            super(bytePixelAccessor, bytePixelAccessor);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            while (--n7 >= 0) {
                System.arraycopy(arrby, n2, arrby2, n4, n6 * this.nSrcElems);
                n2 += n3;
                n4 += n5;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            int n8 = byteBuffer.limit();
            int n9 = byteBuffer.position();
            int n10 = byteBuffer2.position();
            try {
                while (--n7 >= 0) {
                    int n11 = n2 + n6 * this.nSrcElems;
                    if (n11 > n8) {
                        throw new IndexOutOfBoundsException("" + n8);
                    }
                    byteBuffer.limit(n11);
                    byteBuffer.position(n2);
                    byteBuffer2.position(n4);
                    byteBuffer2.put(byteBuffer);
                    n2 += n3;
                    n4 += n5;
                }
            }
            finally {
                byteBuffer.limit(n8);
                byteBuffer.position(n9);
                byteBuffer2.position(n10);
            }
        }
    }
}

