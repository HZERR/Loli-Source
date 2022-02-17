/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.bmp.BMPDescriptor;
import com.sun.javafx.iio.bmp.BitmapInfoHeader;
import com.sun.javafx.iio.bmp.LEInputStream;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class BMPImageLoader
extends ImageLoaderImpl {
    static final short BM = 19778;
    static final int BFH_SIZE = 14;
    final LEInputStream data;
    int bfSize;
    int bfOffBits;
    byte[] bgra_palette;
    BitmapInfoHeader bih;
    int[] bitMasks;
    int[] bitOffsets;

    BMPImageLoader(InputStream inputStream) throws IOException {
        super(BMPDescriptor.theInstance);
        this.data = new LEInputStream(inputStream);
        if (this.data.readShort() != 19778) {
            throw new IOException("Invalid BMP file signature");
        }
        this.readHeader();
    }

    private void readHeader() throws IOException {
        this.bfSize = this.data.readInt();
        this.data.skipBytes(4);
        this.bfOffBits = this.data.readInt();
        this.bih = new BitmapInfoHeader(this.data);
        if (this.bfOffBits < this.bih.biSize + 14) {
            throw new IOException("Invalid bitmap bits offset");
        }
        if (this.bih.biSize + 14 != this.bfOffBits) {
            int n2 = this.bfOffBits - this.bih.biSize - 14;
            int n3 = n2 / 4;
            this.bgra_palette = new byte[n3 * 4];
            int n4 = this.data.in.read(this.bgra_palette);
            if (n4 < n2) {
                this.data.skipBytes(n2 - n4);
            }
        }
        if (this.bih.biCompression == 3) {
            this.parseBitfields();
        } else if (this.bih.biCompression == 0 && this.bih.biBitCount == 16) {
            this.bitMasks = new int[]{31744, 992, 31};
            this.bitOffsets = new int[]{10, 5, 0};
        }
    }

    private void parseBitfields() throws IOException {
        if (this.bgra_palette.length != 12) {
            throw new IOException("Invalid bit masks");
        }
        this.bitMasks = new int[3];
        this.bitOffsets = new int[3];
        for (int i2 = 0; i2 < 3; ++i2) {
            int n2;
            this.bitMasks[i2] = n2 = BMPImageLoader.getDWord(this.bgra_palette, i2 * 4);
            int n3 = 0;
            if (n2 != 0) {
                while ((n2 & 1) == 0) {
                    ++n3;
                    n2 >>>= 1;
                }
                if (!BMPImageLoader.isPow2Minus1(n2)) {
                    throw new IOException("Bit mask is not contiguous");
                }
            }
            this.bitOffsets[i2] = n3;
        }
        if (!BMPImageLoader.checkDisjointMasks(this.bitMasks[0], this.bitMasks[1], this.bitMasks[2])) {
            throw new IOException("Bit masks overlap");
        }
    }

    static boolean checkDisjointMasks(int n2, int n3, int n4) {
        return (n2 & n3 | n2 & n4 | n3 & n4) == 0;
    }

    static boolean isPow2Minus1(int n2) {
        return (n2 & n2 + 1) == 0;
    }

    @Override
    public void dispose() {
    }

    private void readRLE(byte[] arrby, int n2, int n3, boolean bl) throws IOException {
        int n4 = this.bih.biSizeImage;
        if (n4 == 0) {
            n4 = this.bfSize - this.bfOffBits;
        }
        byte[] arrby2 = new byte[n4];
        ImageTools.readFully(this.data.in, arrby2);
        boolean bl2 = this.bih.biHeight > 0;
        int n5 = bl2 ? n3 - 1 : 0;
        int n6 = 0;
        int n7 = n5 * n2;
        block5: while (n6 < n4) {
            int n8;
            int n9;
            int n10;
            int n11 = BMPImageLoader.getByte(arrby2, n6++);
            int n12 = BMPImageLoader.getByte(arrby2, n6++);
            if (n11 == 0) {
                switch (n12) {
                    case 0: {
                        n7 = (n5 += bl2 ? -1 : 1) * n2;
                        break;
                    }
                    case 1: {
                        return;
                    }
                    case 2: {
                        n10 = BMPImageLoader.getByte(arrby2, n6++);
                        n9 = BMPImageLoader.getByte(arrby2, n6++);
                        n5 += n9;
                        n7 += n9 * n2;
                        n7 += n10 * 3;
                        break;
                    }
                    default: {
                        n8 = 0;
                        for (int i2 = 0; i2 < n12; ++i2) {
                            int n13;
                            if (bl) {
                                if ((i2 & 1) == 0) {
                                    n8 = BMPImageLoader.getByte(arrby2, n6++);
                                    n13 = (n8 & 0xF0) >> 4;
                                } else {
                                    n13 = n8 & 0xF;
                                }
                            } else {
                                n13 = BMPImageLoader.getByte(arrby2, n6++);
                            }
                            n7 = this.setRGBFromPalette(arrby, n7, n13);
                        }
                        if (bl) {
                            if ((n12 & 3) != 1 && (n12 & 3) != 2) continue block5;
                            ++n6;
                            break;
                        }
                        if ((n12 & 1) != 1) continue block5;
                        ++n6;
                        break;
                    }
                }
                continue;
            }
            if (bl) {
                n10 = (n12 & 0xF0) >> 4;
                n9 = n12 & 0xF;
                for (n8 = 0; n8 < n11; ++n8) {
                    n7 = this.setRGBFromPalette(arrby, n7, (n8 & 1) == 0 ? n10 : n9);
                }
                continue;
            }
            for (n10 = 0; n10 < n11; ++n10) {
                n7 = this.setRGBFromPalette(arrby, n7, n12);
            }
        }
    }

    private int setRGBFromPalette(byte[] arrby, int n2, int n3) {
        arrby[n2++] = this.bgra_palette[(n3 *= 4) + 2];
        arrby[n2++] = this.bgra_palette[n3 + 1];
        arrby[n2++] = this.bgra_palette[n3];
        return n2;
    }

    private void readPackedBits(byte[] arrby, int n2, int n3) throws IOException {
        int n4 = 8 / this.bih.biBitCount;
        int n5 = (this.bih.biWidth + n4 - 1) / n4;
        int n6 = n5 + 3 & 0xFFFFFFFC;
        int n7 = (1 << this.bih.biBitCount) - 1;
        byte[] arrby2 = new byte[n6];
        for (int i2 = 0; i2 != n3; ++i2) {
            ImageTools.readFully(this.data.in, arrby2);
            int n8 = this.bih.biHeight < 0 ? i2 : n3 - i2 - 1;
            int n9 = n8 * n2;
            for (int i3 = 0; i3 != this.bih.biWidth; ++i3) {
                int n10 = i3 * this.bih.biBitCount;
                byte by = arrby2[n10 / 8];
                int n11 = 8 - (n10 & 7) - this.bih.biBitCount;
                int n12 = by >> n11 & n7;
                n9 = this.setRGBFromPalette(arrby, n9, n12);
            }
        }
    }

    private static int getDWord(byte[] arrby, int n2) {
        return arrby[n2] & 0xFF | (arrby[n2 + 1] & 0xFF) << 8 | (arrby[n2 + 2] & 0xFF) << 16 | (arrby[n2 + 3] & 0xFF) << 24;
    }

    private static int getWord(byte[] arrby, int n2) {
        return arrby[n2] & 0xFF | (arrby[n2 + 1] & 0xFF) << 8;
    }

    private static int getByte(byte[] arrby, int n2) {
        return arrby[n2] & 0xFF;
    }

    private static byte convertFrom5To8Bit(int n2, int n3, int n4) {
        int n5 = (n2 & n3) >>> n4;
        return (byte)(n5 << 3 | n5 >> 2);
    }

    private static byte convertFromXTo8Bit(int n2, int n3, int n4) {
        int n5 = (n2 & n3) >>> n4;
        return (byte)((double)n5 * 255.0 / (double)(n3 >>> n4));
    }

    private void read16Bit(byte[] arrby, int n2, int n3, BitConverter bitConverter) throws IOException {
        int n4 = this.bih.biWidth * 2;
        int n5 = n4 + 3 & 0xFFFFFFFC;
        byte[] arrby2 = new byte[n5];
        for (int i2 = 0; i2 != n3; ++i2) {
            ImageTools.readFully(this.data.in, arrby2);
            int n6 = this.bih.biHeight < 0 ? i2 : n3 - i2 - 1;
            int n7 = n6 * n2;
            for (int i3 = 0; i3 != this.bih.biWidth; ++i3) {
                int n8 = BMPImageLoader.getWord(arrby2, i3 * 2);
                for (int i4 = 0; i4 < 3; ++i4) {
                    arrby[n7++] = bitConverter.convert(n8, this.bitMasks[i4], this.bitOffsets[i4]);
                }
            }
        }
    }

    private void read32BitRGB(byte[] arrby, int n2, int n3) throws IOException {
        int n4 = this.bih.biWidth * 4;
        byte[] arrby2 = new byte[n4];
        for (int i2 = 0; i2 != n3; ++i2) {
            ImageTools.readFully(this.data.in, arrby2);
            int n5 = this.bih.biHeight < 0 ? i2 : n3 - i2 - 1;
            int n6 = n5 * n2;
            for (int i3 = 0; i3 != this.bih.biWidth; ++i3) {
                int n7 = i3 * 4;
                arrby[n6++] = arrby2[n7 + 2];
                arrby[n6++] = arrby2[n7 + 1];
                arrby[n6++] = arrby2[n7];
            }
        }
    }

    private void read32BitBF(byte[] arrby, int n2, int n3) throws IOException {
        int n4 = this.bih.biWidth * 4;
        byte[] arrby2 = new byte[n4];
        for (int i2 = 0; i2 != n3; ++i2) {
            ImageTools.readFully(this.data.in, arrby2);
            int n5 = this.bih.biHeight < 0 ? i2 : n3 - i2 - 1;
            int n6 = n5 * n2;
            for (int i3 = 0; i3 != this.bih.biWidth; ++i3) {
                int n7 = i3 * 4;
                int n8 = BMPImageLoader.getDWord(arrby2, n7);
                for (int i4 = 0; i4 < 3; ++i4) {
                    arrby[n6++] = BMPImageLoader.convertFromXTo8Bit(n8, this.bitMasks[i4], this.bitOffsets[i4]);
                }
            }
        }
    }

    private void read24Bit(byte[] arrby, int n2, int n3) throws IOException {
        int n4 = n2 + 3 & 0xFFFFFFFC;
        int n5 = n4 - n2;
        for (int i2 = 0; i2 != n3; ++i2) {
            int n6 = this.bih.biHeight < 0 ? i2 : n3 - i2 - 1;
            int n7 = n6 * n2;
            ImageTools.readFully(this.data.in, arrby, n7, n2);
            this.data.skipBytes(n5);
            BMPImageLoader.BGRtoRGB(arrby, n7, n2);
        }
    }

    static void BGRtoRGB(byte[] arrby, int n2, int n3) {
        for (int i2 = n3 / 3; i2 != 0; --i2) {
            byte by = arrby[n2];
            byte by2 = arrby[n2 + 2];
            arrby[n2 + 2] = by;
            arrby[n2] = by2;
            n2 += 3;
        }
    }

    @Override
    public ImageFrame load(int n2, int n3, int n4, boolean bl, boolean bl2) throws IOException {
        if (0 != n2) {
            return null;
        }
        int n5 = Math.abs(this.bih.biHeight);
        int[] arrn = ImageTools.computeDimensions(this.bih.biWidth, n5, n3, n4, bl);
        n3 = arrn[0];
        n4 = arrn[1];
        ImageMetadata imageMetadata = new ImageMetadata(null, Boolean.TRUE, null, null, null, null, null, n3, n4, null, null, null);
        this.updateImageMetadata(imageMetadata);
        int n6 = 3;
        int n7 = this.bih.biWidth * n6;
        byte[] arrby = new byte[n7 * n5];
        switch (this.bih.biBitCount) {
            case 1: {
                this.readPackedBits(arrby, n7, n5);
                break;
            }
            case 4: {
                if (this.bih.biCompression == 2) {
                    this.readRLE(arrby, n7, n5, true);
                    break;
                }
                this.readPackedBits(arrby, n7, n5);
                break;
            }
            case 8: {
                if (this.bih.biCompression == 1) {
                    this.readRLE(arrby, n7, n5, false);
                    break;
                }
                this.readPackedBits(arrby, n7, n5);
                break;
            }
            case 16: {
                if (this.bih.biCompression == 3) {
                    this.read16Bit(arrby, n7, n5, BMPImageLoader::convertFromXTo8Bit);
                    break;
                }
                this.read16Bit(arrby, n7, n5, BMPImageLoader::convertFrom5To8Bit);
                break;
            }
            case 32: {
                if (this.bih.biCompression == 3) {
                    this.read32BitBF(arrby, n7, n5);
                    break;
                }
                this.read32BitRGB(arrby, n7, n5);
                break;
            }
            case 24: {
                this.read24Bit(arrby, n7, n5);
                break;
            }
            default: {
                throw new IOException("Unknown BMP bit depth");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
        if (this.bih.biWidth != n3 || n5 != n4) {
            byteBuffer = ImageTools.scaleImage(byteBuffer, this.bih.biWidth, n5, n6, n3, n4, bl2);
        }
        return new ImageFrame(ImageStorage.ImageType.RGB, byteBuffer, n3, n4, n3 * n6, null, imageMetadata);
    }

    @FunctionalInterface
    private static interface BitConverter {
        public byte convert(int var1, int var2, int var3);
    }
}

