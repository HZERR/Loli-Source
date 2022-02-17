/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.gif;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.gif.GIFDescriptor;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class GIFImageLoader2
extends ImageLoaderImpl {
    static final byte[] FILE_SIG87 = new byte[]{71, 73, 70, 56, 55, 97};
    static final byte[] FILE_SIG89 = new byte[]{71, 73, 70, 56, 57, 97};
    static final byte[] NETSCAPE_SIG = new byte[]{78, 69, 84, 83, 67, 65, 80, 69, 50, 46, 48};
    static final int DEFAULT_FPS = 25;
    InputStream stream = null;
    int screenW;
    int screenH;
    int bgColor;
    byte[][] globalPalette;
    byte[] image;
    int loopCount = 1;

    public GIFImageLoader2(InputStream inputStream) throws IOException {
        super(GIFDescriptor.getInstance());
        this.stream = inputStream;
        this.readGlobalHeader();
    }

    private void readGlobalHeader() throws IOException {
        byte[] arrby = this.readBytes(new byte[6]);
        if (!Arrays.equals(FILE_SIG87, arrby) && !Arrays.equals(FILE_SIG89, arrby)) {
            throw new IOException("Bad GIF signature!");
        }
        this.screenW = this.readShort();
        this.screenH = this.readShort();
        int n2 = this.readByte();
        this.bgColor = this.readByte();
        int n3 = this.readByte();
        if ((n2 & 0x80) != 0) {
            this.globalPalette = this.readPalete(2 << (n2 & 7), -1);
        }
        this.image = new byte[this.screenW * this.screenH * 4];
    }

    private byte[][] readPalete(int n2, int n3) throws IOException {
        byte[][] arrby = new byte[4][n2];
        byte[] arrby2 = this.readBytes(new byte[n2 * 3]);
        int n4 = 0;
        for (int i2 = 0; i2 != n2; ++i2) {
            for (int i3 = 0; i3 != 3; ++i3) {
                arrby[i3][i2] = arrby2[n4++];
            }
            arrby[3][i2] = i2 == n3 ? 0 : -1;
        }
        return arrby;
    }

    private void consumeAnExtension() throws IOException {
        int n2 = this.readByte();
        while (n2 != 0) {
            this.skipBytes(n2);
            n2 = this.readByte();
        }
    }

    private void readAppExtension() throws IOException {
        int n2 = this.readByte();
        byte[] arrby = this.readBytes(new byte[n2]);
        if (Arrays.equals(NETSCAPE_SIG, arrby)) {
            int n3 = this.readByte();
            while (n3 != 0) {
                byte[] arrby2 = this.readBytes(new byte[n3]);
                byte by = arrby2[0];
                if (n3 == 3 && by == 1) {
                    this.loopCount = arrby2[1] & 0xFF | (arrby2[2] & 0xFF) << 8;
                }
                n3 = this.readByte();
            }
        } else {
            this.consumeAnExtension();
        }
    }

    private int readControlCode() throws IOException {
        int n2 = this.readByte();
        int n3 = this.readByte();
        int n4 = this.readShort();
        int n5 = this.readByte();
        if (n2 != 4 || this.readByte() != 0) {
            throw new IOException("Bad GIF GraphicControlExtension");
        }
        return ((n3 & 0x1F) << 24) + (n5 << 16) + n4;
    }

    private int waitForImageFrame() throws IOException {
        int n2;
        int n3 = 0;
        block9: while (true) {
            n2 = this.stream.read();
            switch (n2) {
                case 44: {
                    return n3;
                }
                case 33: {
                    switch (this.readByte()) {
                        case 249: {
                            n3 = this.readControlCode();
                            continue block9;
                        }
                        case 255: {
                            this.readAppExtension();
                            continue block9;
                        }
                    }
                    this.consumeAnExtension();
                    continue block9;
                }
                case -1: 
                case 59: {
                    return -1;
                }
            }
            break;
        }
        throw new IOException("Unexpected GIF control characher 0x" + String.format("%02X", n2));
    }

    private void decodeImage(byte[] arrby, int n2, int n3, int[] arrn) throws IOException {
        LZWDecoder lZWDecoder = new LZWDecoder();
        byte[] arrby2 = lZWDecoder.getString();
        int n4 = 0;
        int n5 = 0;
        int n6 = n2;
        block0: while (true) {
            int n7;
            if ((n7 = lZWDecoder.readString()) == -1) {
                lZWDecoder.waitForTerminator();
                return;
            }
            int n8 = 0;
            while (true) {
                if (n8 == n7) continue block0;
                int n9 = n6 < n7 - n8 ? n6 : n7 - n8;
                System.arraycopy(arrby2, n8, arrby, n5, n9);
                n5 += n9;
                n8 += n9;
                if ((n6 -= n9) != 0) continue;
                if (++n4 == n3) {
                    lZWDecoder.waitForTerminator();
                    return;
                }
                int n10 = arrn == null ? n4 : arrn[n4];
                n5 = n10 * n2;
                n6 = n2;
            }
            break;
        }
    }

    private int[] computeInterlaceReIndex(int n2) {
        int n3;
        int[] arrn = new int[n2];
        int n4 = 0;
        for (n3 = 0; n3 < n2; n3 += 8) {
            arrn[n4++] = n3;
        }
        for (n3 = 4; n3 < n2; n3 += 8) {
            arrn[n4++] = n3;
        }
        for (n3 = 2; n3 < n2; n3 += 4) {
            arrn[n4++] = n3;
        }
        for (n3 = 1; n3 < n2; n3 += 2) {
            arrn[n4++] = n3;
        }
        return arrn;
    }

    @Override
    public ImageFrame load(int n2, int n3, int n4, boolean bl, boolean bl2) throws IOException {
        int n5 = this.waitForImageFrame();
        if (n5 < 0) {
            return null;
        }
        int n6 = this.readShort();
        int n7 = this.readShort();
        int n8 = this.readShort();
        int n9 = this.readShort();
        if (n6 + n8 > this.screenW || n7 + n9 > this.screenH) {
            throw new IOException("Wrong GIF image frame size");
        }
        int n10 = this.readByte();
        boolean bl3 = (n5 >>> 24 & 1) == 1;
        int n11 = bl3 ? n5 >>> 16 & 0xFF : -1;
        boolean bl4 = (n10 & 0x80) != 0;
        boolean bl5 = (n10 & 0x40) != 0;
        byte[][] arrby = bl4 ? this.readPalete(2 << (n10 & 7), n11) : this.globalPalette;
        int[] arrn = ImageTools.computeDimensions(this.screenW, this.screenH, n3, n4, bl);
        n3 = arrn[0];
        n4 = arrn[1];
        ImageMetadata imageMetadata = this.updateMetadata(n3, n4, n5 & 0xFFFF);
        int n12 = n5 >>> 26 & 7;
        byte[] arrby2 = new byte[n8 * n9];
        this.decodeImage(arrby2, n8, n9, bl5 ? this.computeInterlaceReIndex(n9) : null);
        ByteBuffer byteBuffer = this.decodePalette(arrby2, arrby, n11, n6, n7, n8, n9, n12);
        if (this.screenW != n3 || this.screenH != n4) {
            byteBuffer = ImageTools.scaleImage(byteBuffer, this.screenW, this.screenH, 4, n3, n4, bl2);
        }
        return new ImageFrame(ImageStorage.ImageType.RGBA, byteBuffer, n3, n4, n3 * 4, null, imageMetadata);
    }

    private int readByte() throws IOException {
        int n2 = this.stream.read();
        if (n2 < 0) {
            throw new EOFException();
        }
        return n2;
    }

    private int readShort() throws IOException {
        int n2 = this.readByte();
        int n3 = this.readByte();
        return n2 + (n3 << 8);
    }

    private byte[] readBytes(byte[] arrby) throws IOException {
        return this.readBytes(arrby, 0, arrby.length);
    }

    private byte[] readBytes(byte[] arrby, int n2, int n3) throws IOException {
        while (n3 > 0) {
            int n4 = this.stream.read(arrby, n2, n3);
            if (n4 < 0) {
                throw new EOFException();
            }
            n2 += n4;
            n3 -= n4;
        }
        return arrby;
    }

    private void skipBytes(int n2) throws IOException {
        ImageTools.skipFully(this.stream, n2);
    }

    @Override
    public void dispose() {
    }

    private void restoreToBackground(byte[] arrby, int n2, int n3, int n4, int n5) {
        for (int i2 = 0; i2 != n5; ++i2) {
            int n6 = ((n3 + i2) * this.screenW + n2) * 4;
            for (int i3 = 0; i3 != n4; ++i3) {
                arrby[n6 + 3] = 0;
                n6 += 4;
            }
        }
    }

    private ByteBuffer decodePalette(byte[] arrby, byte[][] arrby2, int n2, int n3, int n4, int n5, int n6, int n7) {
        byte[] arrby3 = n7 == 3 ? (byte[])this.image.clone() : this.image;
        for (int i2 = 0; i2 != n6; ++i2) {
            int n8;
            int n9;
            int n10 = ((n4 + i2) * this.screenW + n3) * 4;
            int n11 = i2 * n5;
            if (n2 < 0) {
                for (n9 = 0; n9 != n5; ++n9) {
                    n8 = 0xFF & arrby[n11 + n9];
                    arrby3[n10 + 0] = arrby2[0][n8];
                    arrby3[n10 + 1] = arrby2[1][n8];
                    arrby3[n10 + 2] = arrby2[2][n8];
                    arrby3[n10 + 3] = arrby2[3][n8];
                    n10 += 4;
                }
                continue;
            }
            for (n9 = 0; n9 != n5; ++n9) {
                n8 = 0xFF & arrby[n11 + n9];
                if (n8 != n2) {
                    arrby3[n10 + 0] = arrby2[0][n8];
                    arrby3[n10 + 1] = arrby2[1][n8];
                    arrby3[n10 + 2] = arrby2[2][n8];
                    arrby3[n10 + 3] = arrby2[3][n8];
                }
                n10 += 4;
            }
        }
        if (n7 != 3) {
            arrby3 = (byte[])arrby3.clone();
        }
        if (n7 == 2) {
            this.restoreToBackground(this.image, n3, n4, n5, n6);
        }
        return ByteBuffer.wrap(arrby3);
    }

    private ImageMetadata updateMetadata(int n2, int n3, int n4) {
        ImageMetadata imageMetadata = new ImageMetadata(null, true, null, null, null, n4 != 0 ? n4 * 10 : 40, this.loopCount, n2, n3, null, null, null);
        this.updateImageMetadata(imageMetadata);
        return imageMetadata;
    }

    class LZWDecoder {
        private final int initCodeSize;
        private final int clearCode;
        private final int eofCode;
        private int codeSize;
        private int codeMask;
        private int tableIndex;
        private int oldCode;
        private int blockLength = 0;
        private int blockPos = 0;
        private byte[] block = new byte[255];
        private int inData = 0;
        private int inBits = 0;
        private int[] prefix = new int[4096];
        private byte[] suffix = new byte[4096];
        private byte[] initial = new byte[4096];
        private int[] length = new int[4096];
        private byte[] string = new byte[4096];

        public LZWDecoder() throws IOException {
            this.initCodeSize = GIFImageLoader2.this.readByte();
            this.clearCode = 1 << this.initCodeSize;
            this.eofCode = this.clearCode + 1;
            this.initTable();
        }

        public final int readString() throws IOException {
            int n2;
            int n3;
            int n4;
            int n5 = this.getCode();
            if (n5 == this.eofCode) {
                return -1;
            }
            if (n5 == this.clearCode) {
                this.initTable();
                n5 = this.getCode();
                if (n5 == this.eofCode) {
                    return -1;
                }
            } else {
                n4 = this.tableIndex;
                if (n5 < n4) {
                    n3 = n5;
                } else {
                    n3 = this.oldCode;
                    if (n5 != n4) {
                        throw new IOException("Bad GIF LZW: Out-of-sequence code!");
                    }
                }
                this.prefix[n4] = n2 = this.oldCode;
                this.suffix[n4] = this.initial[n3];
                this.initial[n4] = this.initial[n2];
                this.length[n4] = this.length[n2] + 1;
                ++this.tableIndex;
                if (this.tableIndex == 1 << this.codeSize && this.tableIndex < 4096) {
                    ++this.codeSize;
                    this.codeMask = (1 << this.codeSize) - 1;
                }
            }
            n3 = n5;
            n4 = this.length[n3];
            for (n2 = n4 - 1; n2 >= 0; --n2) {
                this.string[n2] = this.suffix[n3];
                n3 = this.prefix[n3];
            }
            this.oldCode = n5;
            return n4;
        }

        public final byte[] getString() {
            return this.string;
        }

        public final void waitForTerminator() throws IOException {
            GIFImageLoader2.this.consumeAnExtension();
        }

        private void initTable() {
            int n2;
            int n3 = 1 << this.initCodeSize;
            for (n2 = 0; n2 < n3; ++n2) {
                this.prefix[n2] = -1;
                this.suffix[n2] = (byte)n2;
                this.initial[n2] = (byte)n2;
                this.length[n2] = 1;
            }
            for (n2 = n3; n2 < 4096; ++n2) {
                this.prefix[n2] = -1;
                this.length[n2] = 1;
            }
            this.codeSize = this.initCodeSize + 1;
            this.codeMask = (1 << this.codeSize) - 1;
            this.tableIndex = n3 + 2;
            this.oldCode = 0;
        }

        private int getCode() throws IOException {
            while (this.inBits < this.codeSize) {
                this.inData |= this.nextByte() << this.inBits;
                this.inBits += 8;
            }
            int n2 = this.inData & this.codeMask;
            this.inBits -= this.codeSize;
            this.inData >>>= this.codeSize;
            return n2;
        }

        private int nextByte() throws IOException {
            if (this.blockPos == this.blockLength) {
                this.readData();
            }
            return this.block[this.blockPos++] & 0xFF;
        }

        private void readData() throws IOException {
            this.blockPos = 0;
            this.blockLength = GIFImageLoader2.this.readByte();
            if (this.blockLength <= 0) {
                throw new EOFException();
            }
            GIFImageLoader2.this.readBytes(this.block, 0, this.blockLength);
        }
    }
}

