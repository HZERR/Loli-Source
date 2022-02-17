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
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteGray {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteGrayObj;

    public static ByteToBytePixelConverter ToByteGrayConverter() {
        if (ToByteGrayObj == null) {
            ToByteGrayObj = BaseByteToByteConverter.create(accessor);
        }
        return ToByteGrayObj;
    }

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgrfConv.nonpremult;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgrfConv.premult;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntFrgbConv.nonpremult;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntFrgbConv.premult;
    }

    public static ByteToBytePixelConverter ToByteBgrConverter() {
        return ToByteRgbAnyConv.bgr;
    }

    static class ToByteRgbAnyConv
    extends BaseByteToByteConverter {
        static ToByteRgbAnyConv bgr = new ToByteRgbAnyConv(ByteBgr.setter);

        private ToByteRgbAnyConv(BytePixelSetter bytePixelSetter) {
            super(getter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n5 -= n6 * 3;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrby[n2 + i2] & 0xFF;
                    arrby2[n4++] = (byte)n8;
                    arrby2[n4++] = (byte)n8;
                    arrby2[n4++] = (byte)n8;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n5 -= n6 * 3;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = byteBuffer.get(n2 + i2) & 0xFF;
                    byteBuffer2.put(n4++, (byte)n8);
                    byteBuffer2.put(n4++, (byte)n8);
                    byteBuffer2.put(n4++, (byte)n8);
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ToIntFrgbConv
    extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter nonpremult = new ToIntFrgbConv(IntArgb.setter);
        public static final ByteToIntPixelConverter premult = new ToIntFrgbConv(IntArgbPre.setter);

        private ToIntFrgbConv(IntPixelSetter intPixelSetter) {
            super(getter, intPixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrby[n2 + i2] & 0xFF;
                    arrn[n4 + i2] = 0xFF000000 | n8 << 16 | n8 << 8 | n8;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = byteBuffer.get(n2 + i2) & 0xFF;
                    intBuffer.put(n4 + i2, 0xFF000000 | n8 << 16 | n8 << 8 | n8);
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ToByteBgrfConv
    extends BaseByteToByteConverter {
        public static final ByteToBytePixelConverter nonpremult = new ToByteBgrfConv(ByteBgra.setter);
        public static final ByteToBytePixelConverter premult = new ToByteBgrfConv(ByteBgraPre.setter);

        ToByteBgrfConv(BytePixelSetter bytePixelSetter) {
            super(getter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by = arrby[n2 + i2];
                    arrby2[n4++] = by;
                    arrby2[n4++] = by;
                    arrby2[n4++] = by;
                    arrby2[n4++] = -1;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byte by = byteBuffer.get(n2 + i2);
                    byteBuffer2.put(n4, by);
                    byteBuffer2.put(n4 + 1, by);
                    byteBuffer2.put(n4 + 2, by);
                    byteBuffer2.put(n4 + 3, (byte)-1);
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
            return AlphaType.OPAQUE;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(byte[] arrby, int n2) {
            int n3 = arrby[n2] & 0xFF;
            return 0xFF000000 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n2) {
            int n3 = arrby[n2] & 0xFF;
            return 0xFF000000 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2) {
            int n3 = byteBuffer.get(n2) & 0xFF;
            return 0xFF000000 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n2) {
            int n3 = byteBuffer.get(n2) & 0xFF;
            return 0xFF000000 | n3 << 16 | n3 << 8 | n3;
        }

        @Override
        public void setArgb(byte[] arrby, int n2, int n3) {
            arrby[n2] = (byte)PixelUtils.RgbToGray(n3);
        }

        @Override
        public void setArgbPre(byte[] arrby, int n2, int n3) {
            this.setArgb(arrby, n2, PixelUtils.PretoNonPre(n3));
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n2, int n3) {
            byteBuffer.put(n2, (byte)PixelUtils.RgbToGray(n3));
        }

        @Override
        public void setArgbPre(ByteBuffer byteBuffer, int n2, int n3) {
            this.setArgb(byteBuffer, n2, PixelUtils.PretoNonPre(n3));
        }
    }
}

