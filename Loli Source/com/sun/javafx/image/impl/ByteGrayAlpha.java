/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGrayAlphaPre;
import java.nio.ByteBuffer;

public class ByteGrayAlpha {
    public static final BytePixelGetter getter = Accessor.nonpremul;
    public static final BytePixelSetter setter = Accessor.nonpremul;
    public static final BytePixelAccessor accessor = Accessor.nonpremul;

    public static ByteToBytePixelConverter ToByteGrayAlphaPreConverter() {
        return ToByteGrayAlphaPreConv.instance;
    }

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgraSameConv.nonpremul;
    }

    static class ToByteBgraSameConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter nonpremul = new ToByteBgraSameConv(false);
        static final ByteToBytePixelConverter premul = new ToByteBgraSameConv(true);

        private ToByteBgraSameConv(boolean bl) {
            super(bl ? ByteGrayAlphaPre.getter : getter, bl ? ByteBgraPre.setter : ByteBgra.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 2;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by = arrby[n2++];
                    byte by2 = arrby[n2++];
                    arrby2[n4++] = by;
                    arrby2[n4++] = by;
                    arrby2[n4++] = by;
                    arrby2[n4++] = by2;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 2;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by = byteBuffer.get(n2++);
                    byte by2 = byteBuffer.get(n2++);
                    byteBuffer2.put(n4++, by);
                    byteBuffer2.put(n4++, by);
                    byteBuffer2.put(n4++, by);
                    byteBuffer2.put(n4++, by2);
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ToByteGrayAlphaPreConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter instance = new ToByteGrayAlphaPreConv();

        private ToByteGrayAlphaPreConv() {
            super(getter, ByteGrayAlphaPre.setter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 2;
            n5 -= n6 * 2;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by;
                    int n8 = arrby[n2++] & 0xFF;
                    if ((by = arrby[n2++]) != -1) {
                        n8 = by == 0 ? 0 : (n8 * (by & 0xFF) + 127) / 255;
                    }
                    arrby2[n4++] = (byte)n8;
                    arrby2[n4++] = by;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 2;
            n5 -= n6 * 2;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by;
                    int n8 = byteBuffer.get(n2++) & 0xFF;
                    if ((by = byteBuffer.get(n2++)) != -1) {
                        n8 = by == 0 ? 0 : (n8 * (by & 0xFF) + 127) / 255;
                    }
                    byteBuffer2.put(n4++, (byte)n8);
                    byteBuffer2.put(n4++, by);
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class Accessor
    implements BytePixelAccessor {
        static final BytePixelAccessor nonpremul = new Accessor(false);
        static final BytePixelAccessor premul = new Accessor(true);
        private boolean isPremult;

        private Accessor(boolean bl) {
            this.isPremult = bl;
        }

        @Override
        public AlphaType getAlphaType() {
            return this.isPremult ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 2;
        }

        @Override
        public int getArgb(byte[] arrby, int n2) {
            int n3 = arrby[n2] & 0xFF;
            int n4 = arrby[n2 + 1] & 0xFF;
            if (this.isPremult) {
                n3 = PixelUtils.PreToNonPre(n3, n4);
            }
            return n4 << 24 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n2) {
            int n3 = arrby[n2] & 0xFF;
            int n4 = arrby[n2 + 1] & 0xFF;
            if (!this.isPremult) {
                n3 = PixelUtils.NonPretoPre(n3, n4);
            }
            return n4 << 24 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2) {
            int n3 = byteBuffer.get(n2) & 0xFF;
            int n4 = byteBuffer.get(n2 + 1) & 0xFF;
            if (this.isPremult) {
                n3 = PixelUtils.PreToNonPre(n3, n4);
            }
            return n4 << 24 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n2) {
            int n3 = byteBuffer.get(n2) & 0xFF;
            int n4 = byteBuffer.get(n2 + 1) & 0xFF;
            if (!this.isPremult) {
                n3 = PixelUtils.NonPretoPre(n3, n4);
            }
            return n4 << 24 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public void setArgb(byte[] arrby, int n2, int n3) {
            int n4 = PixelUtils.RgbToGray(n3);
            int n5 = n3 >>> 24;
            if (this.isPremult) {
                n4 = PixelUtils.NonPretoPre(n4, n5);
            }
            arrby[n2] = (byte)n4;
            arrby[n2 + 1] = (byte)n5;
        }

        @Override
        public void setArgbPre(byte[] arrby, int n2, int n3) {
            int n4 = PixelUtils.RgbToGray(n3);
            int n5 = n3 >>> 24;
            if (!this.isPremult) {
                n4 = PixelUtils.PreToNonPre(n4, n5);
            }
            arrby[n2] = (byte)n4;
            arrby[n2 + 1] = (byte)n5;
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n2, int n3) {
            int n4 = PixelUtils.RgbToGray(n3);
            int n5 = n3 >>> 24;
            if (this.isPremult) {
                n4 = PixelUtils.NonPretoPre(n4, n5);
            }
            byteBuffer.put(n2, (byte)n4);
            byteBuffer.put(n2 + 1, (byte)n5);
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n2, int n3) {
            int n4 = PixelUtils.RgbToGray(n3);
            int n5 = n3 >>> 24;
            if (!this.isPremult) {
                n4 = PixelUtils.PreToNonPre(n4, n5);
            }
            byteBuffer.put(n2, (byte)n4);
            byteBuffer.put(n2 + 1, (byte)n5);
        }
    }
}

