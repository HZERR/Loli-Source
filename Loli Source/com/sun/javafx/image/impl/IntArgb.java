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
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.javafx.image.impl.IntTo4ByteSameConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class IntArgb {
    public static final IntPixelGetter getter = Accessor.instance;
    public static final IntPixelSetter setter = Accessor.instance;
    public static final IntPixelAccessor accessor = Accessor.instance;
    private static IntToBytePixelConverter ToByteBgraObj;
    private static IntToIntPixelConverter ToIntArgbObj;

    public static IntToBytePixelConverter ToByteBgraConverter() {
        if (ToByteBgraObj == null) {
            ToByteBgraObj = new IntTo4ByteSameConverter(getter, ByteBgra.setter);
        }
        return ToByteBgraObj;
    }

    public static IntToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgraPreConv.instance;
    }

    public static IntToIntPixelConverter ToIntArgbConverter() {
        if (ToIntArgbObj == null) {
            ToIntArgbObj = BaseIntToIntConverter.create(accessor);
        }
        return ToIntArgbObj;
    }

    public static IntToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntArgbPreConv.instance;
    }

    static class ToByteBgraPreConv
    extends BaseIntToByteConverter {
        public static final IntToBytePixelConverter instance = new ToByteBgraPreConv();

        private ToByteBgraPreConv() {
            super(getter, ByteBgraPre.setter);
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrn[n2++];
                    int n9 = n8 >>> 24;
                    int n10 = n8 >> 16;
                    int n11 = n8 >> 8;
                    int n12 = n8;
                    if (n9 < 255) {
                        if (n9 == 0) {
                            n10 = 0;
                            n11 = 0;
                            n12 = 0;
                        } else {
                            n12 = ((n12 & 0xFF) * n9 + 127) / 255;
                            n11 = ((n11 & 0xFF) * n9 + 127) / 255;
                            n10 = ((n10 & 0xFF) * n9 + 127) / 255;
                        }
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
                    int n10 = n8 >> 16;
                    int n11 = n8 >> 8;
                    int n12 = n8;
                    if (n9 < 255) {
                        if (n9 == 0) {
                            n10 = 0;
                            n11 = 0;
                            n12 = 0;
                        } else {
                            n12 = ((n12 & 0xFF) * n9 + 127) / 255;
                            n11 = ((n11 & 0xFF) * n9 + 127) / 255;
                            n10 = ((n10 & 0xFF) * n9 + 127) / 255;
                        }
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

    public static class ToIntArgbPreConv
    extends BaseIntToIntConverter {
        public static final IntToIntPixelConverter instance = new ToIntArgbPreConv();

        private ToIntArgbPreConv() {
            super(getter, IntArgbPre.setter);
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, int[] arrn2, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8;
                    int n9;
                    if ((n9 = (n8 = arrn[n2++]) >>> 24) < 255) {
                        if (n9 == 0) {
                            n8 = 0;
                        } else {
                            int n10 = ((n8 >> 16 & 0xFF) * n9 + 127) / 255;
                            int n11 = ((n8 >> 8 & 0xFF) * n9 + 127) / 255;
                            int n12 = ((n8 & 0xFF) * n9 + 127) / 255;
                            n8 = n9 << 24 | n10 << 16 | n11 << 8 | n12;
                        }
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
                    if (n9 < 255) {
                        if (n9 == 0) {
                            n8 = 0;
                        } else {
                            int n10 = ((n8 >> 16 & 0xFF) * n9 + 127) / 255;
                            int n11 = ((n8 >> 8 & 0xFF) * n9 + 127) / 255;
                            int n12 = ((n8 & 0xFF) * n9 + 127) / 255;
                            n8 = n9 << 24 | n10 << 16 | n11 << 8 | n12;
                        }
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
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(int[] arrn, int n2) {
            return arrn[n2];
        }

        @Override
        public int getArgbPre(int[] arrn, int n2) {
            return PixelUtils.NonPretoPre(arrn[n2]);
        }

        @Override
        public int getArgb(IntBuffer intBuffer, int n2) {
            return intBuffer.get(n2);
        }

        @Override
        public int getArgbPre(IntBuffer intBuffer, int n2) {
            return PixelUtils.NonPretoPre(intBuffer.get(n2));
        }

        @Override
        public void setArgb(int[] arrn, int n2, int n3) {
            arrn[n2] = n3;
        }

        @Override
        public void setArgbPre(int[] arrn, int n2, int n3) {
            arrn[n2] = PixelUtils.PretoNonPre(n3);
        }

        @Override
        public void setArgb(IntBuffer intBuffer, int n2, int n3) {
            intBuffer.put(n2, n3);
        }

        @Override
        public void setArgbPre(IntBuffer intBuffer, int n2, int n3) {
            intBuffer.put(n2, PixelUtils.PretoNonPre(n3));
        }
    }
}

