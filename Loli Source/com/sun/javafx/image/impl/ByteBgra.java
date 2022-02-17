/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteBgra {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteBgraConv;

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        if (ToByteBgraConv == null) {
            ToByteBgraConv = BaseByteToByteConverter.create(accessor);
        }
        return ToByteBgraConv;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgraPreConv.instance;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntArgbSameConv.nonpremul;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntArgbPreConv.instance;
    }

    static class ToIntArgbPreConv
    extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter instance = new ToIntArgbPreConv();

        private ToIntArgbPreConv() {
            super(getter, IntArgbPre.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8;
                    int n9 = arrby[n2++] & 0xFF;
                    int n10 = arrby[n2++] & 0xFF;
                    int n11 = arrby[n2++] & 0xFF;
                    if ((n8 = arrby[n2++] & 0xFF) < 255) {
                        if (n8 == 0) {
                            n11 = 0;
                            n10 = 0;
                            n9 = 0;
                        } else {
                            n9 = (n9 * n8 + 127) / 255;
                            n10 = (n10 * n8 + 127) / 255;
                            n11 = (n11 * n8 + 127) / 255;
                        }
                    }
                    arrn[n4++] = n8 << 24 | n11 << 16 | n10 << 8 | n9;
                }
                n4 += n5;
                n2 += n3;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = byteBuffer.get(n2) & 0xFF;
                    int n9 = byteBuffer.get(n2 + 1) & 0xFF;
                    int n10 = byteBuffer.get(n2 + 2) & 0xFF;
                    int n11 = byteBuffer.get(n2 + 3) & 0xFF;
                    n2 += 4;
                    if (n11 < 255) {
                        if (n11 == 0) {
                            n10 = 0;
                            n9 = 0;
                            n8 = 0;
                        } else {
                            n8 = (n8 * n11 + 127) / 255;
                            n9 = (n9 * n11 + 127) / 255;
                            n10 = (n10 * n11 + 127) / 255;
                        }
                    }
                    intBuffer.put(n4 + i2, n11 << 24 | n10 << 16 | n9 << 8 | n8);
                }
                n4 += n5;
                n2 += n3;
            }
        }
    }

    static class ToIntArgbSameConv
    extends BaseByteToIntConverter {
        static final ByteToIntPixelConverter nonpremul = new ToIntArgbSameConv(false);
        static final ByteToIntPixelConverter premul = new ToIntArgbSameConv(true);

        private ToIntArgbSameConv(boolean bl) {
            super(bl ? ByteBgraPre.getter : getter, bl ? IntArgbPre.setter : IntArgb.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    arrn[n4++] = arrby[n2++] & 0xFF | (arrby[n2++] & 0xFF) << 8 | (arrby[n2++] & 0xFF) << 16 | arrby[n2++] << 24;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    intBuffer.put(n4 + i2, byteBuffer.get(n2) & 0xFF | (byteBuffer.get(n2 + 1) & 0xFF) << 8 | (byteBuffer.get(n2 + 2) & 0xFF) << 16 | byteBuffer.get(n2 + 3) << 24);
                    n2 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ToByteBgraPreConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter instance = new ToByteBgraPreConv();

        private ToByteBgraPreConv() {
            super(getter, ByteBgraPre.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 4;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8;
                    byte by = arrby[n2++];
                    byte by2 = arrby[n2++];
                    byte by3 = arrby[n2++];
                    if ((n8 = arrby[n2++] & 0xFF) < 255) {
                        if (n8 == 0) {
                            by3 = 0;
                            by2 = 0;
                            by = 0;
                        } else {
                            by = (byte)(((by & 0xFF) * n8 + 127) / 255);
                            by2 = (byte)(((by2 & 0xFF) * n8 + 127) / 255);
                            by3 = (byte)(((by3 & 0xFF) * n8 + 127) / 255);
                        }
                    }
                    arrby2[n4++] = by;
                    arrby2[n4++] = by2;
                    arrby2[n4++] = by3;
                    arrby2[n4++] = (byte)n8;
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
                    byte by = byteBuffer.get(n2);
                    byte by2 = byteBuffer.get(n2 + 1);
                    byte by3 = byteBuffer.get(n2 + 2);
                    int n8 = byteBuffer.get(n2 + 3) & 0xFF;
                    n2 += 4;
                    if (n8 < 255) {
                        if (n8 == 0) {
                            by3 = 0;
                            by2 = 0;
                            by = 0;
                        } else {
                            by = (byte)(((by & 0xFF) * n8 + 127) / 255);
                            by2 = (byte)(((by2 & 0xFF) * n8 + 127) / 255);
                            by3 = (byte)(((by3 & 0xFF) * n8 + 127) / 255);
                        }
                    }
                    byteBuffer2.put(n4, by);
                    byteBuffer2.put(n4 + 1, by2);
                    byteBuffer2.put(n4 + 2, by3);
                    byteBuffer2.put(n4 + 3, (byte)n8);
                    n4 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

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
            return arrby[n2] & 0xFF | (arrby[n2 + 1] & 0xFF) << 8 | (arrby[n2 + 2] & 0xFF) << 16 | arrby[n2 + 3] << 24;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n2) {
            return PixelUtils.NonPretoPre(this.getArgb(arrby, n2));
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2) {
            return byteBuffer.get(n2) & 0xFF | (byteBuffer.get(n2 + 1) & 0xFF) << 8 | (byteBuffer.get(n2 + 2) & 0xFF) << 16 | byteBuffer.get(n2 + 3) << 24;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n2) {
            return PixelUtils.NonPretoPre(this.getArgb(byteBuffer, n2));
        }

        @Override
        public void setArgb(byte[] arrby, int n2, int n3) {
            arrby[n2] = (byte)n3;
            arrby[n2 + 1] = (byte)(n3 >> 8);
            arrby[n2 + 2] = (byte)(n3 >> 16);
            arrby[n2 + 3] = (byte)(n3 >> 24);
        }

        @Override
        public void setArgbPre(byte[] arrby, int n2, int n3) {
            this.setArgb(arrby, n2, PixelUtils.PretoNonPre(n3));
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n2, int n3) {
            byteBuffer.put(n2, (byte)n3);
            byteBuffer.put(n2 + 1, (byte)(n3 >> 8));
            byteBuffer.put(n2 + 2, (byte)(n3 >> 16));
            byteBuffer.put(n2 + 3, (byte)(n3 >> 24));
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n2, int n3) {
            this.setArgb(byteBuffer, n2, PixelUtils.PretoNonPre(n3));
        }
    }
}

