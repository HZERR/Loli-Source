/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;

public abstract class WritablePixelFormat<T extends Buffer>
extends PixelFormat<T> {
    WritablePixelFormat(PixelFormat.Type type) {
        super(type);
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    public abstract void setArgb(T var1, int var2, int var3, int var4, int var5);

    static class ByteBgraPre
    extends WritablePixelFormat<ByteBuffer> {
        static final ByteBgraPre INSTANCE = new ByteBgraPre();

        private ByteBgraPre() {
            super(PixelFormat.Type.BYTE_BGRA_PRE);
        }

        @Override
        public boolean isPremultiplied() {
            return true;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2, int n3, int n4) {
            int n5 = n3 * n4 + n2 * 4;
            int n6 = byteBuffer.get(n5) & 0xFF;
            int n7 = byteBuffer.get(n5 + 1) & 0xFF;
            int n8 = byteBuffer.get(n5 + 2) & 0xFF;
            int n9 = byteBuffer.get(n5 + 3) & 0xFF;
            if (n9 > 0 && n9 < 255) {
                int n10 = n9 >> 1;
                n8 = n8 >= n9 ? 255 : (n8 * 255 + n10) / n9;
                n7 = n7 >= n9 ? 255 : (n7 * 255 + n10) / n9;
                n6 = n6 >= n9 ? 255 : (n6 * 255 + n10) / n9;
            }
            return n9 << 24 | n8 << 16 | n7 << 8 | n6;
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n2, int n3, int n4, int n5) {
            int n6;
            int n7;
            int n8;
            int n9 = n3 * n4 + n2 * 4;
            int n10 = n5 >>> 24;
            if (n10 > 0) {
                n8 = n5 >> 16 & 0xFF;
                n7 = n5 >> 8 & 0xFF;
                n6 = n5 & 0xFF;
                if (n10 < 255) {
                    n8 = (n8 * n10 + 127) / 255;
                    n7 = (n7 * n10 + 127) / 255;
                    n6 = (n6 * n10 + 127) / 255;
                }
            } else {
                n6 = 0;
                n7 = 0;
                n8 = 0;
                n10 = 0;
            }
            byteBuffer.put(n9, (byte)n6);
            byteBuffer.put(n9 + 1, (byte)n7);
            byteBuffer.put(n9 + 2, (byte)n8);
            byteBuffer.put(n9 + 3, (byte)n10);
        }
    }

    static class ByteBgra
    extends WritablePixelFormat<ByteBuffer> {
        static final ByteBgra INSTANCE = new ByteBgra();

        private ByteBgra() {
            super(PixelFormat.Type.BYTE_BGRA);
        }

        @Override
        public boolean isPremultiplied() {
            return false;
        }

        @Override
        public int getArgb(ByteBuffer byteBuffer, int n2, int n3, int n4) {
            int n5 = n3 * n4 + n2 * 4;
            int n6 = byteBuffer.get(n5) & 0xFF;
            int n7 = byteBuffer.get(n5 + 1) & 0xFF;
            int n8 = byteBuffer.get(n5 + 2) & 0xFF;
            int n9 = byteBuffer.get(n5 + 3) & 0xFF;
            return n9 << 24 | n8 << 16 | n7 << 8 | n6;
        }

        @Override
        public void setArgb(ByteBuffer byteBuffer, int n2, int n3, int n4, int n5) {
            int n6 = n3 * n4 + n2 * 4;
            byteBuffer.put(n6, (byte)n5);
            byteBuffer.put(n6 + 1, (byte)(n5 >> 8));
            byteBuffer.put(n6 + 2, (byte)(n5 >> 16));
            byteBuffer.put(n6 + 3, (byte)(n5 >> 24));
        }
    }

    static class IntArgbPre
    extends WritablePixelFormat<IntBuffer> {
        static final IntArgbPre INSTANCE = new IntArgbPre();

        private IntArgbPre() {
            super(PixelFormat.Type.INT_ARGB_PRE);
        }

        @Override
        public boolean isPremultiplied() {
            return true;
        }

        @Override
        public int getArgb(IntBuffer intBuffer, int n2, int n3, int n4) {
            return IntArgbPre.PretoNonPre(intBuffer.get(n3 * n4 + n2));
        }

        @Override
        public void setArgb(IntBuffer intBuffer, int n2, int n3, int n4, int n5) {
            intBuffer.put(n3 * n4 + n2, IntArgbPre.NonPretoPre(n5));
        }
    }

    static class IntArgb
    extends WritablePixelFormat<IntBuffer> {
        static final IntArgb INSTANCE = new IntArgb();

        private IntArgb() {
            super(PixelFormat.Type.INT_ARGB);
        }

        @Override
        public boolean isPremultiplied() {
            return false;
        }

        @Override
        public int getArgb(IntBuffer intBuffer, int n2, int n3, int n4) {
            return intBuffer.get(n3 * n4 + n2);
        }

        @Override
        public void setArgb(IntBuffer intBuffer, int n2, int n3, int n4, int n5) {
            intBuffer.put(n3 * n4 + n2, n5);
        }
    }
}

