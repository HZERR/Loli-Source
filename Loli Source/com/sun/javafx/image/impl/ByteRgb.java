/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.image.impl.ByteArgb;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteRgb {
    public static final BytePixelGetter getter = Getter.instance;

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

    public static ByteToBytePixelConverter ToByteArgbConverter() {
        return ToByteFrgbConv.nonpremult;
    }

    public static final ByteToBytePixelConverter ToByteBgrConverter() {
        return SwapThreeByteConverter.rgbToBgrInstance;
    }

    static class SwapThreeByteConverter
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter rgbToBgrInstance = new SwapThreeByteConverter(getter, ByteBgr.accessor);

        public SwapThreeByteConverter(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
            super(bytePixelGetter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            n3 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    arrby2[n4++] = arrby[n2 + 2];
                    arrby2[n4++] = arrby[n2 + 1];
                    arrby2[n4++] = arrby[n2];
                    n2 += 3;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            n3 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byteBuffer2.put(n4++, byteBuffer.get(n2 + 2));
                    byteBuffer2.put(n4++, byteBuffer.get(n2 + 1));
                    byteBuffer2.put(n4++, byteBuffer.get(n2));
                    n2 += 3;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ToByteFrgbConv
    extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter nonpremult = new ToByteFrgbConv(ByteArgb.setter);

        private ToByteFrgbConv(BytePixelSetter bytePixelSetter) {
            super(getter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            n3 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    arrby2[n4++] = -1;
                    arrby2[n4++] = arrby[n2++];
                    arrby2[n4++] = arrby[n2++];
                    arrby2[n4++] = arrby[n2++];
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            n3 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byteBuffer2.put(n4++, (byte)-1);
                    byteBuffer2.put(n4++, byteBuffer.get(n2++));
                    byteBuffer2.put(n4++, byteBuffer.get(n2++));
                    byteBuffer2.put(n4++, byteBuffer.get(n2++));
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
            n3 -= n6 * 3;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrby[n2++] & 0xFF;
                    int n9 = arrby[n2++] & 0xFF;
                    int n10 = arrby[n2++] & 0xFF;
                    arrn[n4 + i2] = 0xFF000000 | n8 << 16 | n9 << 8 | n10;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = byteBuffer.get(n2) & 0xFF;
                    int n9 = byteBuffer.get(n2 + 1) & 0xFF;
                    int n10 = byteBuffer.get(n2 + 2) & 0xFF;
                    n2 += 3;
                    intBuffer.put(n4 + i2, 0xFF000000 | n8 << 16 | n9 << 8 | n10);
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

        private ToByteBgrfConv(BytePixelSetter bytePixelSetter) {
            super(getter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    arrby2[n4++] = arrby[n2 + 2];
                    arrby2[n4++] = arrby[n2 + 1];
                    arrby2[n4++] = arrby[n2];
                    arrby2[n4++] = -1;
                    n2 += 3;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6 * 3;
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    byteBuffer2.put(n4, byteBuffer.get(n2 + 2));
                    byteBuffer2.put(n4 + 1, byteBuffer.get(n2 + 1));
                    byteBuffer2.put(n4 + 2, byteBuffer.get(n2));
                    byteBuffer2.put(n4 + 3, (byte)-1);
                    n2 += 3;
                    n4 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class Getter
    implements BytePixelGetter {
        static final BytePixelGetter instance = new Getter();

        private Getter() {
        }

        @Override
        public AlphaType getAlphaType() {
            return AlphaType.OPAQUE;
        }

        @Override
        public int getNumElements() {
            return 3;
        }

        @Override
        public int getArgb(byte[] arrby, int n2) {
            return arrby[n2 + 2] & 0xFF | (arrby[n2 + 1] & 0xFF) << 8 | (arrby[n2] & 0xFF) << 16 | 0xFF000000;
        }

        @Override
        public int getArgbPre(byte[] arrby, int n2) {
            return arrby[n2 + 2] & 0xFF | (arrby[n2 + 1] & 0xFF) << 8 | (arrby[n2] & 0xFF) << 16 | 0xFF000000;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2) {
            return byteBuffer.get(n2 + 2) & 0xFF | (byteBuffer.get(n2 + 1) & 0xFF) << 8 | (byteBuffer.get(n2) & 0xFF) << 16 | 0xFF000000;
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n2) {
            return byteBuffer.get(n2 + 2) & 0xFF | (byteBuffer.get(n2 + 1) & 0xFF) << 8 | (byteBuffer.get(n2) & 0xFF) << 16 | 0xFF000000;
        }
    }
}

