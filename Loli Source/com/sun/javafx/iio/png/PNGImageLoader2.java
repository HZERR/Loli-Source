/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.png.PNGDescriptor;
import com.sun.javafx.iio.png.PNGIDATChunkInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public final class PNGImageLoader2
extends ImageLoaderImpl {
    static final byte[] FILE_SIG = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
    static final int IHDR_TYPE = 1229472850;
    static final int PLTE_TYPE = 1347179589;
    static final int IDAT_TYPE = 1229209940;
    static final int IEND_TYPE = 1229278788;
    static final int tRNS_TYPE = 1951551059;
    static final int PNG_COLOR_GRAY = 0;
    static final int PNG_COLOR_RGB = 2;
    static final int PNG_COLOR_PALETTE = 3;
    static final int PNG_COLOR_GRAY_ALPHA = 4;
    static final int PNG_COLOR_RGB_ALPHA = 6;
    static final int[] numBandsPerColorType = new int[]{1, -1, 3, 1, 2, -1, 4};
    static final int PNG_FILTER_NONE = 0;
    static final int PNG_FILTER_SUB = 1;
    static final int PNG_FILTER_UP = 2;
    static final int PNG_FILTER_AVERAGE = 3;
    static final int PNG_FILTER_PAETH = 4;
    private final DataInputStream stream;
    private int width;
    private int height;
    private int bitDepth;
    private int colorType;
    private boolean isInterlaced;
    private boolean tRNS_present = false;
    private boolean tRNS_GRAY_RGB = false;
    private int trnsR;
    private int trnsG;
    private int trnsB;
    private byte[][] palette;
    private static final int[] starting_y = new int[]{0, 0, 4, 0, 2, 0, 1, 0};
    private static final int[] starting_x = new int[]{0, 4, 0, 2, 0, 1, 0, 0};
    private static final int[] increment_y = new int[]{8, 8, 8, 4, 4, 2, 2, 1};
    private static final int[] increment_x = new int[]{8, 8, 4, 4, 2, 2, 1, 1};

    public PNGImageLoader2(InputStream inputStream) throws IOException {
        super(PNGDescriptor.getInstance());
        this.stream = new DataInputStream(inputStream);
        byte[] arrby = this.readBytes(new byte[8]);
        if (!Arrays.equals(FILE_SIG, arrby)) {
            throw new IOException("Bad PNG signature!");
        }
        this.readHeader();
    }

    private void readHeader() throws IOException {
        int[] arrn = this.readChunk();
        if (arrn[1] != 1229472850 && arrn[0] != 13) {
            throw new IOException("Bad PNG header!");
        }
        this.width = this.stream.readInt();
        this.height = this.stream.readInt();
        if (this.width == 0 || this.height == 0) {
            throw new IOException("Bad PNG image size!");
        }
        this.bitDepth = this.stream.readByte();
        if (this.bitDepth != 1 && this.bitDepth != 2 && this.bitDepth != 4 && this.bitDepth != 8 && this.bitDepth != 16) {
            throw new IOException("Bad PNG bit depth");
        }
        this.colorType = this.stream.readByte();
        if (this.colorType > 6 || this.colorType == 1 || this.colorType == 5) {
            throw new IOException("Bad PNG color type");
        }
        if (this.colorType != 3 && this.colorType != 0 && this.bitDepth < 8 || this.colorType == 3 && this.bitDepth == 16) {
            throw new IOException("Bad color type/bit depth combination!");
        }
        byte by = this.stream.readByte();
        if (by != 0) {
            throw new IOException("Bad PNG comression!");
        }
        byte by2 = this.stream.readByte();
        if (by2 != 0) {
            throw new IOException("Bad PNG filter method!");
        }
        byte by3 = this.stream.readByte();
        if (by3 != 0 && by3 != 1) {
            throw new IOException("Unknown interlace method (not 0 or 1)!");
        }
        int n2 = this.stream.readInt();
        this.isInterlaced = by3 == 1;
    }

    private int[] readChunk() throws IOException {
        return new int[]{this.stream.readInt(), this.stream.readInt()};
    }

    private byte[] readBytes(byte[] arrby) throws IOException {
        return this.readBytes(arrby, 0, arrby.length);
    }

    private byte[] readBytes(byte[] arrby, int n2, int n3) throws IOException {
        this.stream.readFully(arrby, n2, n3);
        return arrby;
    }

    private void skip(int n2) throws IOException {
        if (n2 != this.stream.skipBytes(n2)) {
            throw new EOFException();
        }
    }

    private void readPaletteChunk(int n2) throws IOException {
        int n3 = n2 / 3;
        int n4 = 1 << this.bitDepth;
        if (n3 > n4) {
            this.emitWarning("PLTE chunk contains too many entries for bit depth, ignoring extras.");
            n3 = n4;
        }
        this.palette = new byte[3][n4];
        byte[] arrby = this.readBytes(new byte[n2]);
        int n5 = 0;
        for (int i2 = 0; i2 != n3; ++i2) {
            for (int i3 = 0; i3 != 3; ++i3) {
                this.palette[i3][i2] = arrby[n5++];
            }
        }
    }

    private void parsePaletteChunk(int n2) throws IOException {
        if (this.palette != null) {
            this.emitWarning("A PNG image may not contain more than one PLTE chunk.\nThe chunk wil be ignored.");
            this.skip(n2);
            return;
        }
        switch (this.colorType) {
            case 3: {
                this.readPaletteChunk(n2);
                return;
            }
            case 0: 
            case 4: {
                this.emitWarning("A PNG gray or gray alpha image cannot have a PLTE chunk.\nThe chunk wil be ignored.");
            }
        }
        this.skip(n2);
    }

    private boolean readPaletteTransparency(int n2) throws IOException {
        if (this.palette == null) {
            this.emitWarning("tRNS chunk without prior PLTE chunk, ignoring it.");
            this.skip(n2);
            return false;
        }
        byte[][] arrarrby = new byte[4][];
        System.arraycopy(this.palette, 0, arrarrby, 0, 3);
        int n3 = this.palette[0].length;
        arrarrby[3] = new byte[n3];
        int n4 = n2 < n3 ? n2 : n3;
        this.readBytes(arrarrby[3], 0, n4);
        for (int i2 = n4; i2 < n3; ++i2) {
            arrarrby[3][i2] = -1;
        }
        if (n4 < n2) {
            this.skip(n2 - n4);
        }
        this.palette = arrarrby;
        return true;
    }

    private boolean readGrayTransparency(int n2) throws IOException {
        if (n2 == 2) {
            this.trnsG = this.stream.readShort();
            return true;
        }
        return false;
    }

    private boolean readRgbTransparency(int n2) throws IOException {
        if (n2 == 6) {
            this.trnsR = this.stream.readShort();
            this.trnsG = this.stream.readShort();
            this.trnsB = this.stream.readShort();
            return true;
        }
        return false;
    }

    private void parseTransparencyChunk(int n2) throws IOException {
        switch (this.colorType) {
            case 3: {
                this.tRNS_present = this.readPaletteTransparency(n2);
                break;
            }
            case 0: {
                this.tRNS_GRAY_RGB = this.tRNS_present = this.readGrayTransparency(n2);
                break;
            }
            case 2: {
                this.tRNS_GRAY_RGB = this.tRNS_present = this.readRgbTransparency(n2);
                break;
            }
            default: {
                this.emitWarning("TransparencyChunk may not present when alpha explicitly defined");
                this.skip(n2);
            }
        }
    }

    private int parsePngMeta() throws IOException {
        while (true) {
            int[] arrn;
            if ((arrn = this.readChunk())[0] < 0) {
                throw new IOException("Invalid chunk length");
            }
            switch (arrn[1]) {
                case 1229209940: {
                    return arrn[0];
                }
                case 1229278788: {
                    return 0;
                }
                case 1347179589: {
                    this.parsePaletteChunk(arrn[0]);
                    break;
                }
                case 1951551059: {
                    this.parseTransparencyChunk(arrn[0]);
                    break;
                }
                default: {
                    this.skip(arrn[0]);
                }
            }
            int n2 = this.stream.readInt();
        }
    }

    @Override
    public void dispose() {
    }

    private ImageStorage.ImageType getType() {
        switch (this.colorType) {
            case 0: {
                return this.tRNS_present ? ImageStorage.ImageType.GRAY_ALPHA : ImageStorage.ImageType.GRAY;
            }
            case 2: {
                return this.tRNS_present ? ImageStorage.ImageType.RGBA : ImageStorage.ImageType.RGB;
            }
            case 3: {
                return ImageStorage.ImageType.PALETTE;
            }
            case 4: {
                return ImageStorage.ImageType.GRAY_ALPHA;
            }
            case 6: {
                return ImageStorage.ImageType.RGBA;
            }
        }
        throw new RuntimeException();
    }

    private void doSubFilter(byte[] arrby, int n2) {
        int n3 = arrby.length;
        for (int i2 = n2; i2 != n3; ++i2) {
            arrby[i2] = (byte)(arrby[i2] + arrby[i2 - n2]);
        }
    }

    private void doUpFilter(byte[] arrby, byte[] arrby2) {
        int n2 = arrby.length;
        for (int i2 = 0; i2 != n2; ++i2) {
            arrby[i2] = (byte)(arrby[i2] + arrby2[i2]);
        }
    }

    private void doAvrgFilter(byte[] arrby, byte[] arrby2, int n2) {
        int n3;
        int n4 = arrby.length;
        for (n3 = 0; n3 != n2; ++n3) {
            arrby[n3] = (byte)(arrby[n3] + (arrby2[n3] & 0xFF) / 2);
        }
        for (n3 = n2; n3 != n4; ++n3) {
            arrby[n3] = (byte)(arrby[n3] + ((arrby[n3 - n2] & 0xFF) + (arrby2[n3] & 0xFF)) / 2);
        }
    }

    private static int paethPr(int n2, int n3, int n4) {
        int n5 = Math.abs(n3 - n4);
        int n6 = Math.abs(n2 - n4);
        int n7 = Math.abs(n3 - n4 + n2 - n4);
        return n5 <= n6 && n5 <= n7 ? n2 : (n6 <= n7 ? n3 : n4);
    }

    private void doPaethFilter(byte[] arrby, byte[] arrby2, int n2) {
        int n3;
        int n4 = arrby.length;
        for (n3 = 0; n3 != n2; ++n3) {
            arrby[n3] = (byte)(arrby[n3] + arrby2[n3]);
        }
        for (n3 = n2; n3 != n4; ++n3) {
            arrby[n3] = (byte)(arrby[n3] + PNGImageLoader2.paethPr(arrby[n3 - n2] & 0xFF, arrby2[n3] & 0xFF, arrby2[n3 - n2] & 0xFF));
        }
    }

    private void doFilter(byte[] arrby, byte[] arrby2, int n2, int n3) {
        switch (n2) {
            case 1: {
                this.doSubFilter(arrby, n3);
                break;
            }
            case 2: {
                this.doUpFilter(arrby, arrby2);
                break;
            }
            case 3: {
                this.doAvrgFilter(arrby, arrby2, n3);
                break;
            }
            case 4: {
                this.doPaethFilter(arrby, arrby2, n3);
            }
        }
    }

    private void downsample16to8trns_gray(byte[] arrby, byte[] arrby2, int n2, int n3) {
        int n4 = arrby.length / 2;
        int n5 = n2;
        for (int i2 = 0; i2 < n4; ++i2) {
            short s2 = (short)((arrby[i2 * 2] & 0xFF) * 256 + (arrby[i2 * 2 + 1] & 0xFF));
            arrby2[n5 + 0] = arrby[i2 * 2];
            arrby2[n5 + 1] = s2 == this.trnsG ? 0 : -1;
            n5 += n3 * 2;
        }
    }

    private void downsample16to8trns_rgb(byte[] arrby, byte[] arrby2, int n2, int n3) {
        int n4 = arrby.length / 2 / 3;
        int n5 = n2;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n6 = i2 * 6;
            short s2 = (short)((arrby[n6 + 0] & 0xFF) * 256 + (arrby[n6 + 1] & 0xFF));
            short s3 = (short)((arrby[n6 + 2] & 0xFF) * 256 + (arrby[n6 + 3] & 0xFF));
            short s4 = (short)((arrby[n6 + 4] & 0xFF) * 256 + (arrby[n6 + 5] & 0xFF));
            arrby2[n5 + 0] = arrby[n6 + 0];
            arrby2[n5 + 1] = arrby[n6 + 2];
            arrby2[n5 + 2] = arrby[n6 + 4];
            arrby2[n5 + 3] = s2 == this.trnsR && s3 == this.trnsG && s4 == this.trnsB ? 0 : -1;
            n5 += n3 * 4;
        }
    }

    private void downsample16to8_plain(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        int n5 = arrby.length / 2 / n4 * n4;
        int n6 = n3 * n4;
        int n7 = n2;
        for (int i2 = 0; i2 != n5; i2 += n4) {
            for (int i3 = 0; i3 != n4; ++i3) {
                arrby2[n7 + i3] = arrby[(i2 + i3) * 2];
            }
            n7 += n6;
        }
    }

    private void downsample16to8(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        if (!this.tRNS_GRAY_RGB) {
            this.downsample16to8_plain(arrby, arrby2, n2, n3, n4);
        } else if (this.colorType == 0) {
            this.downsample16to8trns_gray(arrby, arrby2, n2, n3);
        } else if (this.colorType == 2) {
            this.downsample16to8trns_rgb(arrby, arrby2, n2, n3);
        }
    }

    private void copyTrns_gray(byte[] arrby, byte[] arrby2, int n2, int n3) {
        byte by = (byte)this.trnsG;
        int n4 = n2;
        int n5 = arrby.length;
        for (int i2 = 0; i2 < n5; ++i2) {
            byte by2;
            arrby2[n4] = by2 = arrby[i2];
            arrby2[n4 + 1] = by2 == by ? 0 : -1;
            n4 += 2 * n3;
        }
    }

    private void copyTrns_rgb(byte[] arrby, byte[] arrby2, int n2, int n3) {
        byte by = (byte)this.trnsR;
        byte by2 = (byte)this.trnsG;
        byte by3 = (byte)this.trnsB;
        int n4 = arrby.length / 3;
        int n5 = n2;
        for (int i2 = 0; i2 < n4; ++i2) {
            byte by4 = arrby[i2 * 3];
            byte by5 = arrby[i2 * 3 + 1];
            byte by6 = arrby[i2 * 3 + 2];
            arrby2[n5 + 0] = by4;
            arrby2[n5 + 1] = by5;
            arrby2[n5 + 2] = by6;
            arrby2[n5 + 3] = by4 == by && by5 == by2 && by6 == by3 ? 0 : -1;
            n5 += n3 * 4;
        }
    }

    private void copy_plain(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        int n5 = arrby.length;
        int n6 = n3 * n4;
        int n7 = n2;
        for (int i2 = 0; i2 != n5; i2 += n4) {
            for (int i3 = 0; i3 != n4; ++i3) {
                arrby2[n7 + i3] = arrby[i2 + i3];
            }
            n7 += n6;
        }
    }

    private void copy(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        if (!this.tRNS_GRAY_RGB) {
            if (n3 == 1) {
                System.arraycopy(arrby, 0, arrby2, n2, arrby.length);
            } else {
                this.copy_plain(arrby, arrby2, n2, n3, n4);
            }
        } else if (this.colorType == 0) {
            this.copyTrns_gray(arrby, arrby2, n2, n3);
        } else if (this.colorType == 2) {
            this.copyTrns_rgb(arrby, arrby2, n2, n3);
        }
    }

    private void upsampleTo8Palette(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        int n5 = 8 / this.bitDepth;
        int n6 = (1 << this.bitDepth) - 1;
        int n7 = 0;
        for (int i2 = 0; i2 < n3; i2 += n5) {
            int n8 = n3 - i2 < n5 ? n3 - i2 : n5;
            int n9 = arrby[n7] >> (n5 - n8) * this.bitDepth;
            for (int i3 = n8 - 1; i3 >= 0; --i3) {
                arrby2[n2 + (i2 + i3) * n4] = (byte)(n9 & n6);
                n9 >>= this.bitDepth;
            }
            ++n7;
        }
    }

    private void upsampleTo8Gray(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        int n5 = 8 / this.bitDepth;
        int n6 = (1 << this.bitDepth) - 1;
        int n7 = n6 / 2;
        int n8 = 0;
        for (int i2 = 0; i2 < n3; i2 += n5) {
            int n9 = n3 - i2 < n5 ? n3 - i2 : n5;
            int n10 = arrby[n8] >> (n5 - n9) * this.bitDepth;
            for (int i3 = n9 - 1; i3 >= 0; --i3) {
                arrby2[n2 + (i2 + i3) * n4] = (byte)(((n10 & n6) * 255 + n7) / n6);
                n10 >>= this.bitDepth;
            }
            ++n8;
        }
    }

    private void upsampleTo8GrayTrns(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        int n5 = 8 / this.bitDepth;
        int n6 = (1 << this.bitDepth) - 1;
        int n7 = n6 / 2;
        int n8 = 0;
        for (int i2 = 0; i2 < n3; i2 += n5) {
            int n9 = n3 - i2 < n5 ? n3 - i2 : n5;
            int n10 = arrby[n8] >> (n5 - n9) * this.bitDepth;
            for (int i3 = n9 - 1; i3 >= 0; --i3) {
                int n11 = n2 + (i2 + i3) * n4 * 2;
                int n12 = n10 & n6;
                arrby2[n11] = (byte)((n12 * 255 + n7) / n6);
                arrby2[n11 + 1] = n12 == this.trnsG ? 0 : -1;
                n10 >>= this.bitDepth;
            }
            ++n8;
        }
    }

    private void upsampleTo8(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5) {
        if (this.colorType == 3) {
            this.upsampleTo8Palette(arrby, arrby2, n2, n3, n4);
        } else if (n5 == 1) {
            this.upsampleTo8Gray(arrby, arrby2, n2, n3, n4);
        } else if (this.tRNS_GRAY_RGB && n5 == 2) {
            this.upsampleTo8GrayTrns(arrby, arrby2, n2, n3, n4);
        }
    }

    private static int mipSize(int n2, int n3, int[] arrn, int[] arrn2) {
        return (n2 - arrn[n3] + arrn2[n3] - 1) / arrn2[n3];
    }

    private static int mipPos(int n2, int n3, int[] arrn, int[] arrn2) {
        return arrn[n3] + n2 * arrn2[n3];
    }

    private void loadMip(byte[] arrby, InputStream inputStream, int n2) throws IOException {
        int n3 = PNGImageLoader2.mipSize(this.width, n2, starting_x, increment_x);
        int n4 = PNGImageLoader2.mipSize(this.height, n2, starting_y, increment_y);
        int n5 = (n3 * this.bitDepth * numBandsPerColorType[this.colorType] + 7) / 8;
        byte[] arrby2 = new byte[n5];
        byte[] arrby3 = new byte[n5];
        int n6 = this.bpp();
        int n7 = numBandsPerColorType[this.colorType] * this.bytesPerColor();
        for (int i2 = 0; i2 != n4; ++i2) {
            int n8 = inputStream.read();
            if (n8 == -1) {
                throw new EOFException();
            }
            if (inputStream.read(arrby2) != n5) {
                throw new EOFException();
            }
            this.doFilter(arrby2, arrby3, n8, n7);
            int n9 = (PNGImageLoader2.mipPos(i2, n2, starting_y, increment_y) * this.width + starting_x[n2]) * n6;
            int n10 = increment_x[n2];
            if (this.bitDepth == 16) {
                this.downsample16to8(arrby2, arrby, n9, n10, n6);
            } else if (this.bitDepth < 8) {
                this.upsampleTo8(arrby2, arrby, n9, n3, n10, n6);
            } else {
                this.copy(arrby2, arrby, n9, n10, n6);
            }
            byte[] arrby4 = arrby2;
            arrby2 = arrby3;
            arrby3 = arrby4;
        }
    }

    private void load(byte[] arrby, InputStream inputStream) throws IOException {
        if (this.isInterlaced) {
            for (int i2 = 0; i2 != 7; ++i2) {
                if (this.width <= starting_x[i2] || this.height <= starting_y[i2]) continue;
                this.loadMip(arrby, inputStream, i2);
            }
        } else {
            this.loadMip(arrby, inputStream, 7);
        }
    }

    private ImageFrame decodePalette(byte[] arrby, ImageMetadata imageMetadata) {
        int n2;
        int n3 = this.tRNS_present ? 4 : 3;
        byte[] arrby2 = new byte[this.width * this.height * n3];
        int n4 = this.width * this.height;
        if (this.tRNS_present) {
            int n5 = 0;
            for (n2 = 0; n2 != n4; ++n2) {
                int n6 = 0xFF & arrby[n2];
                arrby2[n5 + 0] = this.palette[0][n6];
                arrby2[n5 + 1] = this.palette[1][n6];
                arrby2[n5 + 2] = this.palette[2][n6];
                arrby2[n5 + 3] = this.palette[3][n6];
                n5 += 4;
            }
        } else {
            int n7 = 0;
            for (n2 = 0; n2 != n4; ++n2) {
                int n8 = 0xFF & arrby[n2];
                arrby2[n7 + 0] = this.palette[0][n8];
                arrby2[n7 + 1] = this.palette[1][n8];
                arrby2[n7 + 2] = this.palette[2][n8];
                n7 += 3;
            }
        }
        ImageStorage.ImageType imageType = this.tRNS_present ? ImageStorage.ImageType.RGBA : ImageStorage.ImageType.RGB;
        return new ImageFrame(imageType, ByteBuffer.wrap(arrby2), this.width, this.height, this.width * n3, null, imageMetadata);
    }

    private int bpp() {
        return numBandsPerColorType[this.colorType] + (this.tRNS_GRAY_RGB ? 1 : 0);
    }

    private int bytesPerColor() {
        return this.bitDepth == 16 ? 2 : 1;
    }

    @Override
    public ImageFrame load(int n2, int n3, int n4, boolean bl, boolean bl2) throws IOException {
        ImageFrame imageFrame;
        if (n2 != 0) {
            return null;
        }
        int n5 = this.parsePngMeta();
        if (n5 == 0) {
            this.emitWarning("No image data in PNG");
            return null;
        }
        int[] arrn = ImageTools.computeDimensions(this.width, this.height, n3, n4, bl);
        n3 = arrn[0];
        n4 = arrn[1];
        ImageMetadata imageMetadata = new ImageMetadata(null, true, null, null, null, null, null, n3, n4, null, null, null);
        this.updateImageMetadata(imageMetadata);
        int n6 = this.bpp();
        ByteBuffer byteBuffer = ByteBuffer.allocate(n6 * this.width * this.height);
        PNGIDATChunkInputStream pNGIDATChunkInputStream = new PNGIDATChunkInputStream(this.stream, n5);
        Inflater inflater = new Inflater();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new InflaterInputStream(pNGIDATChunkInputStream, inflater));
        try {
            this.load(byteBuffer.array(), bufferedInputStream);
        }
        catch (IOException iOException) {
            throw iOException;
        }
        finally {
            if (inflater != null) {
                inflater.end();
            }
        }
        ImageFrame imageFrame2 = imageFrame = this.colorType == 3 ? this.decodePalette(byteBuffer.array(), imageMetadata) : new ImageFrame(this.getType(), byteBuffer, this.width, this.height, n6 * this.width, this.palette, imageMetadata);
        if (this.width != n3 || this.height != n4) {
            imageFrame = ImageTools.scaleImageFrame(imageFrame, n3, n4, bl2);
        }
        return imageFrame;
    }
}

