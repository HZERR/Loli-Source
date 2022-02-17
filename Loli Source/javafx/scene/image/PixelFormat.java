/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import javafx.scene.image.WritablePixelFormat;

public abstract class PixelFormat<T extends Buffer> {
    private Type type;

    PixelFormat(Type type) {
        this.type = type;
    }

    public static WritablePixelFormat<IntBuffer> getIntArgbInstance() {
        return WritablePixelFormat.IntArgb.INSTANCE;
    }

    public static WritablePixelFormat<IntBuffer> getIntArgbPreInstance() {
        return WritablePixelFormat.IntArgbPre.INSTANCE;
    }

    public static WritablePixelFormat<ByteBuffer> getByteBgraInstance() {
        return WritablePixelFormat.ByteBgra.INSTANCE;
    }

    public static WritablePixelFormat<ByteBuffer> getByteBgraPreInstance() {
        return WritablePixelFormat.ByteBgraPre.INSTANCE;
    }

    public static PixelFormat<ByteBuffer> getByteRgbInstance() {
        return ByteRgb.instance;
    }

    public static PixelFormat<ByteBuffer> createByteIndexedPremultipliedInstance(int[] arrn) {
        return IndexedPixelFormat.createByte(arrn, true);
    }

    public static PixelFormat<ByteBuffer> createByteIndexedInstance(int[] arrn) {
        return IndexedPixelFormat.createByte(arrn, false);
    }

    public Type getType() {
        return this.type;
    }

    public abstract boolean isWritable();

    public abstract boolean isPremultiplied();

    static int NonPretoPre(int n2) {
        int n3 = n2 >>> 24;
        if (n3 == 255) {
            return n2;
        }
        if (n3 == 0) {
            return 0;
        }
        int n4 = n2 >> 16 & 0xFF;
        int n5 = n2 >> 8 & 0xFF;
        int n6 = n2 & 0xFF;
        n4 = (n4 * n3 + 127) / 255;
        n5 = (n5 * n3 + 127) / 255;
        n6 = (n6 * n3 + 127) / 255;
        return n3 << 24 | n4 << 16 | n5 << 8 | n6;
    }

    static int PretoNonPre(int n2) {
        int n3 = n2 >>> 24;
        if (n3 == 255 || n3 == 0) {
            return n2;
        }
        int n4 = n2 >> 16 & 0xFF;
        int n5 = n2 >> 8 & 0xFF;
        int n6 = n2 & 0xFF;
        int n7 = n3 >> 1;
        n4 = n4 >= n3 ? 255 : (n4 * 255 + n7) / n3;
        n5 = n5 >= n3 ? 255 : (n5 * 255 + n7) / n3;
        n6 = n6 >= n3 ? 255 : (n6 * 255 + n7) / n3;
        return n3 << 24 | n4 << 16 | n5 << 8 | n6;
    }

    public abstract int getArgb(T var1, int var2, int var3, int var4);

    static class IndexedPixelFormat
    extends PixelFormat<ByteBuffer> {
        int[] precolors;
        int[] nonprecolors;
        boolean premult;

        static PixelFormat createByte(int[] arrn, boolean bl) {
            return new IndexedPixelFormat(Type.BYTE_INDEXED, bl, Arrays.copyOf(arrn, 256));
        }

        private IndexedPixelFormat(Type type, boolean bl, int[] arrn) {
            super(type);
            if (bl) {
                this.precolors = arrn;
            } else {
                this.nonprecolors = arrn;
            }
            this.premult = bl;
        }

        @Override
        public boolean isWritable() {
            return false;
        }

        @Override
        public boolean isPremultiplied() {
            return this.premult;
        }

        int[] getPreColors() {
            if (this.precolors == null) {
                int[] arrn = new int[this.nonprecolors.length];
                for (int i2 = 0; i2 < arrn.length; ++i2) {
                    arrn[i2] = IndexedPixelFormat.NonPretoPre(this.nonprecolors[i2]);
                }
                this.precolors = arrn;
            }
            return this.precolors;
        }

        int[] getNonPreColors() {
            if (this.nonprecolors == null) {
                int[] arrn = new int[this.precolors.length];
                for (int i2 = 0; i2 < arrn.length; ++i2) {
                    arrn[i2] = IndexedPixelFormat.PretoNonPre(this.precolors[i2]);
                }
                this.nonprecolors = arrn;
            }
            return this.nonprecolors;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2, int n3, int n4) {
            return this.getNonPreColors()[byteBuffer.get(n3 * n4 + n2) & 0xFF];
        }
    }

    static class ByteRgb
    extends PixelFormat<ByteBuffer> {
        static final ByteRgb instance = new ByteRgb();

        private ByteRgb() {
            super(Type.BYTE_RGB);
        }

        @Override
        public boolean isWritable() {
            return true;
        }

        @Override
        public boolean isPremultiplied() {
            return false;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2, int n3, int n4) {
            int n5 = n3 * n4 + n2 * 3;
            int n6 = byteBuffer.get(n5) & 0xFF;
            int n7 = byteBuffer.get(n5 + 1) & 0xFF;
            int n8 = byteBuffer.get(n5 + 2) & 0xFF;
            return 0xFF000000 | n6 << 16 | n7 << 8 | n8;
        }
    }

    public static enum Type {
        INT_ARGB_PRE,
        INT_ARGB,
        BYTE_BGRA_PRE,
        BYTE_BGRA,
        BYTE_RGB,
        BYTE_INDEXED;

    }
}

