/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import com.sun.javafx.image.impl.BaseIntToIntConverter;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntTo4ByteSameConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class IntArgbPre {
    public static final IntPixelGetter getter = Accessor.instance;
    public static final IntPixelSetter setter = Accessor.instance;
    public static final IntPixelAccessor accessor = Accessor.instance;
    private static IntToBytePixelConverter ToByteBgraPreObj;
    private static IntToIntPixelConverter ToIntArgbPreObj;

    public static IntToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgraConv.instance;
    }

    public static IntToBytePixelConverter ToByteBgraPreConverter() {
        if (ToByteBgraPreObj == null) {
            ToByteBgraPreObj = new IntTo4ByteSameConverter(getter, ByteBgraPre.setter);
        }
        return ToByteBgraPreObj;
    }

    public static IntToIntPixelConverter ToIntArgbConverter() {
        return ToIntArgbConv.instance;
    }

    public static IntToIntPixelConverter ToIntArgbPreConverter() {
        if (ToIntArgbPreObj == null) {
            ToIntArgbPreObj = BaseIntToIntConverter.create(accessor);
        }
        return ToIntArgbPreObj;
    }

    static class ToByteBgraConv
    extends BaseIntToByteConverter {
        public static final IntToBytePixelConverter instance = new ToByteBgraConv();

        private ToByteBgraConv() {
            super(getter, ByteBgra.setter);
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrn[n2++];
                    int n9 = n8 >>> 24;
                    int n10 = n8 >> 16 & 0xFF;
                    int n11 = n8 >> 8 & 0xFF;
                    int n12 = n8 & 0xFF;
                    if (n9 > 0 && n9 < 255) {
                        int n13 = n9 >> 1;
                        n10 = (n10 * 255 + n13) / n9;
                        n11 = (n11 * 255 + n13) / n9;
                        n12 = (n12 * 255 + n13) / n9;
                    }
                    arrby[n4++] = (byte)n12;
                    arrby[n4++] = (byte)n11;
                    arrby[n4++] = (byte)n10;
                    arrby[n4++] = (byte)n9;
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
                    int n9 = n8 >>> 24;
                    int n10 = n8 >> 16 & 0xFF;
                    int n11 = n8 >> 8 & 0xFF;
                    int n12 = n8 & 0xFF;
                    if (n9 > 0 && n9 < 255) {
                        int n13 = n9 >> 1;
                        n10 = (n10 * 255 + n13) / n9;
                        n11 = (n11 * 255 + n13) / n9;
                        n12 = (n12 * 255 + n13) / n9;
                    }
                    byteBuffer.put(n4, (byte)n12);
                    byteBuffer.put(n4 + 1, (byte)n11);
                    byteBuffer.put(n4 + 2, (byte)n10);
                    byteBuffer.put(n4 + 3, (byte)n9);
                    n4 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    public static class ToIntArgbConv
    extends BaseIntToIntConverter {
        public static final IntToIntPixelConverter instance = new ToIntArgbConv();

        private ToIntArgbConv() {
            super(getter, IntArgb.setter);
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, int[] arrn2, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8;
                    int n9;
                    if ((n9 = (n8 = arrn[n2++]) >>> 24) > 0 && n9 < 255) {
                        int n10 = n9 >> 1;
                        int n11 = ((n8 >> 16 & 0xFF) * 255 + n10) / n9;
                        int n12 = ((n8 >> 8 & 0xFF) * 255 + n10) / n9;
                        int n13 = ((n8 & 0xFF) * 255 + n10) / n9;
                        n8 = n9 << 24 | n11 << 16 | n12 << 8 | n13;
                    }
                    arrn2[n4++] = n8;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(IntBuffer intBuffer, int n2, int n3, IntBuffer intBuffer2, int n4, int n5, int n6, int n7) {
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = intBuffer.get(n2 + i2);
                    int n9 = n8 >>> 24;
                    if (n9 > 0 && n9 < 255) {
                        int n10 = n9 >> 1;
                        int n11 = ((n8 >> 16 & 0xFF) * 255 + n10) / n9;
                        int n12 = ((n8 >> 8 & 0xFF) * 255 + n10) / n9;
                        int n13 = ((n8 & 0xFF) * 255 + n10) / n9;
                        n8 = n9 << 24 | n11 << 16 | n12 << 8 | n13;
                    }
                    intBuffer2.put(n4 + i2, n8);
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class Accessor
    implements IntPixelAccessor {
        static final IntPixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override
        public AlphaType getAlphaType() {
            return AlphaType.PREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(int[] arrn, int n2) {
            return PixelUtils.PretoNonPre(arrn[n2]);
        }

        @Override
        public int getArgbPre(int[] arrn, int n2) {
            return arrn[n2];
        }

        @Override
        public int getArgb(IntBuffer intBuffer, int n2) {
            return PixelUtils.PretoNonPre(intBuffer.get(n2));
        }

        @Override
        public int getArgbPre(IntBuffer intBuffer, int n2) {
            return intBuffer.get(n2);
        }

        @Override
        public void setArgb(int[] arrn, int n2, int n3) {
            arrn[n2] = PixelUtils.NonPretoPre(n3);
        }

        @Override
        public void setArgbPre(int[] arrn, int n2, int n3) {
            arrn[n2] = n3;
        }

        @Override
        public void setArgb(IntBuffer intBuffer, int n2, int n3) {
            intBuffer.put(n2, PixelUtils.NonPretoPre(n3));
        }

        @Override
        public void setArgbPre(IntBuffer intBuffer, int n2, int n3) {
            intBuffer.put(n2, n3);
        }
    }
}

