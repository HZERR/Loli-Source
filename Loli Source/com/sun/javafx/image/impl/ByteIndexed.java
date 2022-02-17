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
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.tk.Toolkit;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;

public class ByteIndexed {
    public static BytePixelGetter createGetter(PixelFormat<ByteBuffer> pixelFormat) {
        return new Getter(pixelFormat);
    }

    public static ByteToBytePixelConverter createToByteBgraAny(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
        return new ToByteBgraAnyConverter(bytePixelGetter, bytePixelSetter);
    }

    public static ByteToIntPixelConverter createToIntArgbAny(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
        return new ToIntArgbAnyConverter(bytePixelGetter, intPixelSetter);
    }

    static int[] getColors(BytePixelGetter bytePixelGetter, PixelSetter pixelSetter) {
        Getter getter = (Getter)bytePixelGetter;
        return pixelSetter.getAlphaType() == AlphaType.PREMULTIPLIED ? getter.getPreColors() : getter.getNonPreColors();
    }

    public static class ToIntArgbAnyConverter
    extends BaseByteToIntConverter {
        public ToIntArgbAnyConverter(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
            super(bytePixelGetter, intPixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
            int[] arrn2 = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    arrn[n4 + i2] = arrn2[arrby[n2 + i2] & 0xFF];
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
            int[] arrn = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    intBuffer.put(n4 + i2, arrn[byteBuffer.get(n2 + i2) & 0xFF]);
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    public static class ToByteBgraAnyConverter
    extends BaseByteToByteConverter {
        public ToByteBgraAnyConverter(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
            super(bytePixelGetter, bytePixelSetter);
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            int[] arrn = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrn[arrby[n2 + i2] & 0xFF];
                    arrby2[n4++] = (byte)n8;
                    arrby2[n4++] = (byte)(n8 >> 8);
                    arrby2[n4++] = (byte)(n8 >> 16);
                    arrby2[n4++] = (byte)(n8 >> 24);
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            int[] arrn = ByteIndexed.getColors(this.getGetter(), this.getSetter());
            n5 -= n6 * 4;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    int n8 = arrn[byteBuffer.get(n2 + i2) & 0xFF];
                    byteBuffer2.put(n4, (byte)n8);
                    byteBuffer2.put(n4 + 1, (byte)(n8 >> 8));
                    byteBuffer2.put(n4 + 2, (byte)(n8 >> 16));
                    byteBuffer2.put(n4 + 3, (byte)(n8 >> 24));
                    n4 += 4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    public static class Getter
    implements BytePixelGetter {
        PixelFormat<ByteBuffer> theFormat;
        private int[] precolors;
        private int[] nonprecolors;

        Getter(PixelFormat<ByteBuffer> pixelFormat) {
            this.theFormat = pixelFormat;
        }

        int[] getPreColors() {
            if (this.precolors == null) {
                this.precolors = Toolkit.getImageAccessor().getPreColors(this.theFormat);
            }
            return this.precolors;
        }

        int[] getNonPreColors() {
            if (this.nonprecolors == null) {
                this.nonprecolors = Toolkit.getImageAccessor().getNonPreColors(this.theFormat);
            }
            return this.nonprecolors;
        }

        @Override
        public AlphaType getAlphaType() {
            return this.theFormat.isPremultiplied() ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
        }

        @Override
        public int getNumElements() {
            return 1;
        }

        @Override
        public int getArgb(byte[] arrby, int n2) {
            return this.getNonPreColors()[arrby[n2] & 0xFF];
        }

        @Override
        public int getArgbPre(byte[] arrby, int n2) {
            return this.getPreColors()[arrby[n2] & 0xFF];
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2) {
            return this.getNonPreColors()[byteBuffer.get(n2) & 0xFF];
        }

        @Override
        public int getArgbPre(ByteBuffer byteBuffer, int n2) {
            return this.getPreColors()[byteBuffer.get(n2) & 0xFF];
        }
    }
}

