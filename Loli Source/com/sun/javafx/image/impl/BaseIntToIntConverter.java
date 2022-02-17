/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToIntPixelConverter;
import java.nio.IntBuffer;

public abstract class BaseIntToIntConverter
implements IntToIntPixelConverter {
    protected final IntPixelGetter getter;
    protected final IntPixelSetter setter;

    public BaseIntToIntConverter(IntPixelGetter intPixelGetter, IntPixelSetter intPixelSetter) {
        this.getter = intPixelGetter;
        this.setter = intPixelSetter;
    }

    public final IntPixelGetter getGetter() {
        return this.getter;
    }

    public final IntPixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(IntBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(int[] arrn, int n2, int n3, int[] arrn2, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        this.doConvert(arrn, n2, n3, arrn2, n4, n5, n6, n7);
    }

    @Override
    public final void convert(IntBuffer intBuffer, int n2, int n3, IntBuffer intBuffer2, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        if (intBuffer.hasArray() && intBuffer2.hasArray()) {
            this.doConvert(intBuffer.array(), n2 += intBuffer.arrayOffset(), n3, intBuffer2.array(), n4 += intBuffer2.arrayOffset(), n5, n6, n7);
        } else {
            this.doConvert(intBuffer, n2, n3, intBuffer2, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(IntBuffer intBuffer, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn2 = intBuffer.array();
            this.doConvert(arrn2, n2 += intBuffer.arrayOffset(), n3, arrn, n4, n5, n6, n7);
        } else {
            IntBuffer intBuffer2 = IntBuffer.wrap(arrn);
            this.doConvert(intBuffer, n2, n3, intBuffer2, n4, n5, n6, n7);
        }
    }

    @Override
    public final void convert(int[] arrn, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
        if (n6 <= 0 || n7 <= 0) {
            return;
        }
        if (n3 == n6 && n5 == n6) {
            n6 *= n7;
            n7 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn2 = intBuffer.array();
            this.doConvert(arrn, n2, n3, arrn2, n4 += intBuffer.arrayOffset(), n5, n6, n7);
        } else {
            IntBuffer intBuffer2 = IntBuffer.wrap(arrn);
            this.doConvert(intBuffer2, n2, n3, intBuffer, n4, n5, n6, n7);
        }
    }

    static IntToIntPixelConverter create(IntPixelAccessor intPixelAccessor) {
        return new IntAnyToSameConverter(intPixelAccessor);
    }

    static class IntAnyToSameConverter
    extends BaseIntToIntConverter {
        IntAnyToSameConverter(IntPixelAccessor intPixelAccessor) {
            super(intPixelAccessor, intPixelAccessor);
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, int[] arrn2, int n4, int n5, int n6, int n7) {
            while (--n7 >= 0) {
                System.arraycopy(arrn, n2, arrn2, n4, n6);
                n2 += n3;
                n4 += n5;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        void doConvert(IntBuffer intBuffer, int n2, int n3, IntBuffer intBuffer2, int n4, int n5, int n6, int n7) {
            int n8 = intBuffer.limit();
            int n9 = intBuffer.position();
            int n10 = intBuffer2.position();
            try {
                while (--n7 >= 0) {
                    int n11 = n2 + n6;
                    if (n11 > n8) {
                        throw new IndexOutOfBoundsException("" + n8);
                    }
                    intBuffer.limit(n11);
                    intBuffer.position(n2);
                    intBuffer2.position(n4);
                    intBuffer2.put(intBuffer);
                    n2 += n3;
                    n4 += n5;
                }
            }
            finally {
                intBuffer.limit(n8);
                intBuffer.position(n9);
                intBuffer2.position(n10);
            }
        }
    }
}

