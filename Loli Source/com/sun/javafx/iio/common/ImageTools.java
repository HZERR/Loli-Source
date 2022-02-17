/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ImageTools {
    public static final int PROGRESS_INTERVAL = 5;

    public static int readFully(InputStream inputStream, byte[] arrby, int n2, int n3) throws IOException {
        if (n3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n4 = n3;
        if (n2 < 0 || n3 < 0 || n2 + n3 > arrby.length || n2 + n3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > b.length!");
        }
        while (n3 > 0) {
            int n5 = inputStream.read(arrby, n2, n3);
            if (n5 == -1) {
                throw new EOFException();
            }
            n2 += n5;
            n3 -= n5;
        }
        return n4;
    }

    public static int readFully(InputStream inputStream, byte[] arrby) throws IOException {
        return ImageTools.readFully(inputStream, arrby, 0, arrby.length);
    }

    public static void skipFully(InputStream inputStream, long l2) throws IOException {
        while (l2 > 0L) {
            long l3 = inputStream.skip(l2);
            if (l3 <= 0L) {
                if (inputStream.read() == -1) {
                    throw new EOFException();
                }
                --l2;
                continue;
            }
            l2 -= l3;
        }
    }

    public static ImageStorage.ImageType getConvertedType(ImageStorage.ImageType imageType) {
        ImageStorage.ImageType imageType2 = imageType;
        switch (imageType) {
            case GRAY: {
                imageType2 = ImageStorage.ImageType.GRAY;
                break;
            }
            case GRAY_ALPHA: 
            case GRAY_ALPHA_PRE: 
            case PALETTE_ALPHA: 
            case PALETTE_ALPHA_PRE: 
            case PALETTE_TRANS: 
            case RGBA: {
                imageType2 = ImageStorage.ImageType.RGBA_PRE;
                break;
            }
            case PALETTE: 
            case RGB: {
                imageType2 = ImageStorage.ImageType.RGB;
                break;
            }
            case RGBA_PRE: {
                imageType2 = ImageStorage.ImageType.RGBA_PRE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported ImageType " + (Object)((Object)imageType));
            }
        }
        return imageType2;
    }

    public static byte[] createImageArray(ImageStorage.ImageType imageType, int n2, int n3) {
        int n4 = 0;
        switch (imageType) {
            case GRAY: 
            case PALETTE_ALPHA: 
            case PALETTE_ALPHA_PRE: 
            case PALETTE: {
                n4 = 1;
                break;
            }
            case GRAY_ALPHA: 
            case GRAY_ALPHA_PRE: {
                n4 = 2;
                break;
            }
            case RGB: {
                n4 = 3;
                break;
            }
            case RGBA: 
            case RGBA_PRE: {
                n4 = 4;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported ImageType " + (Object)((Object)imageType));
            }
        }
        return new byte[n2 * n3 * n4];
    }

    public static ImageFrame convertImageFrame(ImageFrame imageFrame) {
        ImageFrame imageFrame2;
        ImageStorage.ImageType imageType = imageFrame.getImageType();
        ImageStorage.ImageType imageType2 = ImageTools.getConvertedType(imageType);
        if (imageType2 == imageType) {
            imageFrame2 = imageFrame;
        } else {
            byte[] arrby = null;
            Buffer buffer = imageFrame.getImageData();
            if (!(buffer instanceof ByteBuffer)) {
                throw new IllegalArgumentException("!(frame.getImageData() instanceof ByteBuffer)");
            }
            ByteBuffer byteBuffer = (ByteBuffer)buffer;
            if (byteBuffer.hasArray()) {
                arrby = byteBuffer.array();
            } else {
                arrby = new byte[byteBuffer.capacity()];
                byteBuffer.get(arrby);
            }
            int n2 = imageFrame.getWidth();
            int n3 = imageFrame.getHeight();
            int n4 = imageFrame.getStride();
            byte[] arrby2 = ImageTools.createImageArray(imageType2, n2, n3);
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(arrby2);
            int n5 = arrby2.length / n3;
            byte[][] arrby3 = imageFrame.getPalette();
            ImageMetadata imageMetadata = imageFrame.getMetadata();
            int n6 = imageMetadata.transparentIndex != null ? imageMetadata.transparentIndex : 0;
            ImageTools.convert(n2, n3, imageType, arrby, 0, n4, arrby2, 0, n5, arrby3, n6, false);
            ImageMetadata imageMetadata2 = new ImageMetadata(imageMetadata.gamma, imageMetadata.blackIsZero, null, imageMetadata.backgroundColor, null, imageMetadata.delayTime, imageMetadata.loopCount, imageMetadata.imageWidth, imageMetadata.imageHeight, imageMetadata.imageLeftPosition, imageMetadata.imageTopPosition, imageMetadata.disposalMethod);
            imageFrame2 = new ImageFrame(imageType2, byteBuffer2, n2, n3, n5, null, imageMetadata2);
        }
        return imageFrame2;
    }

    public static byte[] convert(int n2, int n3, ImageStorage.ImageType imageType, byte[] arrby, int n4, int n5, byte[] arrby2, int n6, int n7, byte[][] arrby3, int n8, boolean bl) {
        if (imageType == ImageStorage.ImageType.GRAY || imageType == ImageStorage.ImageType.RGB || imageType == ImageStorage.ImageType.RGBA_PRE) {
            if (arrby != arrby2) {
                int n9 = n2;
                if (imageType == ImageStorage.ImageType.RGB) {
                    n9 *= 3;
                } else if (imageType == ImageStorage.ImageType.RGBA_PRE) {
                    n9 *= 4;
                }
                if (n3 == 1) {
                    System.arraycopy(arrby, n4, arrby2, n6, n9);
                } else {
                    int n10 = n4;
                    int n11 = n6;
                    for (int i2 = 0; i2 < n3; ++i2) {
                        System.arraycopy(arrby, n10, arrby2, n11, n9);
                        n10 += n5;
                        n11 += n7;
                    }
                }
            }
        } else if (imageType == ImageStorage.ImageType.GRAY_ALPHA || imageType == ImageStorage.ImageType.GRAY_ALPHA_PRE) {
            int n12 = n4;
            int n13 = n6;
            if (imageType == ImageStorage.ImageType.GRAY_ALPHA) {
                for (int i3 = 0; i3 < n3; ++i3) {
                    int n14 = n12;
                    int n15 = n13;
                    for (int i4 = 0; i4 < n2; ++i4) {
                        byte by = arrby[n14++];
                        int n16 = arrby[n14++] & 0xFF;
                        float f2 = (float)n16 / 255.0f;
                        by = (byte)(f2 * (float)(by & 0xFF));
                        arrby2[n15++] = by;
                        arrby2[n15++] = by;
                        arrby2[n15++] = by;
                        arrby2[n15++] = (byte)n16;
                    }
                    n12 += n5;
                    n13 += n7;
                }
            } else {
                for (int i5 = 0; i5 < n3; ++i5) {
                    int n17 = n12;
                    int n18 = n13;
                    for (int i6 = 0; i6 < n2; ++i6) {
                        byte by = arrby[n17++];
                        arrby2[n18++] = by;
                        arrby2[n18++] = by;
                        arrby2[n18++] = by;
                        arrby2[n18++] = arrby[n17++];
                    }
                    n12 += n5;
                    n13 += n7;
                }
            }
        } else if (imageType == ImageStorage.ImageType.PALETTE) {
            int n19 = n4;
            int n20 = n6;
            byte[] arrby4 = arrby3[0];
            byte[] arrby5 = arrby3[1];
            byte[] arrby6 = arrby3[2];
            int n21 = n19;
            int n22 = n20;
            for (int i7 = 0; i7 < n2; ++i7) {
                int n23 = arrby[n21++] & 0xFF;
                arrby2[n22++] = arrby4[n23];
                arrby2[n22++] = arrby5[n23];
                arrby2[n22++] = arrby6[n23];
                n20 += n7;
            }
        } else if (imageType == ImageStorage.ImageType.PALETTE_ALPHA) {
            int n24 = n4;
            int n25 = n6;
            byte[] arrby7 = arrby3[0];
            byte[] arrby8 = arrby3[1];
            byte[] arrby9 = arrby3[2];
            byte[] arrby10 = arrby3[3];
            int n26 = n24;
            int n27 = n25;
            for (int i8 = 0; i8 < n2; ++i8) {
                int n28 = arrby[n26++] & 0xFF;
                byte by = arrby7[n28];
                byte by2 = arrby8[n28];
                byte by3 = arrby9[n28];
                int n29 = arrby10[n28] & 0xFF;
                float f3 = (float)n29 / 255.0f;
                arrby2[n27++] = (byte)(f3 * (float)(by & 0xFF));
                arrby2[n27++] = (byte)(f3 * (float)(by2 & 0xFF));
                arrby2[n27++] = (byte)(f3 * (float)(by3 & 0xFF));
                arrby2[n27++] = (byte)n29;
            }
            n24 += n5;
            n25 += n7;
        } else if (imageType == ImageStorage.ImageType.PALETTE_ALPHA_PRE) {
            int n30 = n4;
            int n31 = n6;
            byte[] arrby11 = arrby3[0];
            byte[] arrby12 = arrby3[1];
            byte[] arrby13 = arrby3[2];
            byte[] arrby14 = arrby3[3];
            for (int i9 = 0; i9 < n3; ++i9) {
                int n32 = n30;
                int n33 = n31;
                for (int i10 = 0; i10 < n2; ++i10) {
                    int n34 = arrby[n32++] & 0xFF;
                    arrby2[n33++] = arrby11[n34];
                    arrby2[n33++] = arrby12[n34];
                    arrby2[n33++] = arrby13[n34];
                    arrby2[n33++] = arrby14[n34];
                }
                n30 += n5;
                n31 += n7;
            }
        } else if (imageType == ImageStorage.ImageType.PALETTE_TRANS) {
            int n35 = n4;
            int n36 = n6;
            for (int i11 = 0; i11 < n3; ++i11) {
                int n37 = n35;
                int n38 = n36;
                byte[] arrby15 = arrby3[0];
                byte[] arrby16 = arrby3[1];
                byte[] arrby17 = arrby3[2];
                for (int i12 = 0; i12 < n2; ++i12) {
                    int n39;
                    if ((n39 = arrby[n37++] & 0xFF) == n8) {
                        if (bl) {
                            n38 += 4;
                            continue;
                        }
                        arrby2[n38++] = 0;
                        arrby2[n38++] = 0;
                        arrby2[n38++] = 0;
                        arrby2[n38++] = 0;
                        continue;
                    }
                    arrby2[n38++] = arrby15[n39];
                    arrby2[n38++] = arrby16[n39];
                    arrby2[n38++] = arrby17[n39];
                    arrby2[n38++] = -1;
                }
                n35 += n5;
                n36 += n7;
            }
        } else if (imageType == ImageStorage.ImageType.RGBA) {
            int n40 = n4;
            int n41 = n6;
            for (int i13 = 0; i13 < n3; ++i13) {
                int n42 = n40;
                int n43 = n41;
                for (int i14 = 0; i14 < n2; ++i14) {
                    byte by = arrby[n42++];
                    byte by4 = arrby[n42++];
                    byte by5 = arrby[n42++];
                    int n44 = arrby[n42++] & 0xFF;
                    float f4 = (float)n44 / 255.0f;
                    arrby2[n43++] = (byte)(f4 * (float)(by & 0xFF));
                    arrby2[n43++] = (byte)(f4 * (float)(by4 & 0xFF));
                    arrby2[n43++] = (byte)(f4 * (float)(by5 & 0xFF));
                    arrby2[n43++] = (byte)n44;
                }
                n40 += n5;
                n41 += n7;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported ImageType " + (Object)((Object)imageType));
        }
        return arrby2;
    }

    public static String getScaledImageName(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = string.lastIndexOf(47);
        String string2 = n2 < 0 ? string : string.substring(n2 + 1);
        int n3 = string2.lastIndexOf(".");
        if (n3 < 0) {
            n3 = string2.length();
        }
        if (n2 >= 0) {
            stringBuilder.append(string.substring(0, n2 + 1));
        }
        stringBuilder.append(string2.substring(0, n3));
        stringBuilder.append("@2x");
        stringBuilder.append(string2.substring(n3));
        return stringBuilder.toString();
    }

    public static InputStream createInputStream(String string) throws IOException {
        Serializable serializable;
        InputStream inputStream = null;
        try {
            serializable = new File(string);
            if (((File)serializable).exists()) {
                inputStream = new FileInputStream((File)serializable);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (inputStream == null) {
            serializable = new URL(string);
            inputStream = ((URL)serializable).openStream();
        }
        return inputStream;
    }

    private static void computeUpdatedPixels(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int[] arrn, int n11) {
        boolean bl = false;
        int n12 = -1;
        int n13 = -1;
        int n14 = -1;
        for (int i2 = 0; i2 < n9; ++i2) {
            int n15 = n8 + i2 * n10;
            if (n15 < n2 || (n15 - n2) % n7 != 0) continue;
            if (n15 >= n2 + n3) break;
            int n16 = n4 + (n15 - n2) / n7;
            if (n16 < n5) continue;
            if (n16 > n6) break;
            if (!bl) {
                n12 = n16;
                bl = true;
            } else if (n13 == -1) {
                n13 = n16;
            }
            n14 = n16;
        }
        arrn[n11] = n12;
        arrn[n11 + 2] = !bl ? 0 : n14 - n12 + 1;
        arrn[n11 + 4] = Math.max(n13 - n12, 1);
    }

    public static int[] computeUpdatedPixels(Rectangle rectangle, Point2D point2D, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13) {
        int[] arrn = new int[6];
        ImageTools.computeUpdatedPixels(rectangle.x, rectangle.width, (int)(point2D.x + 0.5f), n2, n4, n6, n8, n10, n12, arrn, 0);
        ImageTools.computeUpdatedPixels(rectangle.y, rectangle.height, (int)(point2D.y + 0.5f), n3, n5, n7, n9, n11, n13, arrn, 1);
        return arrn;
    }

    public static int[] computeDimensions(int n2, int n3, int n4, int n5, boolean bl) {
        int n6;
        int n7 = n4 < 0 ? 0 : n4;
        int n8 = n6 = n5 < 0 ? 0 : n5;
        if (n7 == 0 && n6 == 0) {
            n7 = n2;
            n6 = n3;
        } else if (n7 != n2 || n6 != n3) {
            if (bl) {
                if (n7 == 0) {
                    n7 = (int)((float)n2 * (float)n6 / (float)n3);
                } else if (n6 == 0) {
                    n6 = (int)((float)n3 * (float)n7 / (float)n2);
                } else {
                    float f2 = Math.min((float)n7 / (float)n2, (float)n6 / (float)n3);
                    n7 = (int)((float)n2 * f2);
                    n6 = (int)((float)n3 * f2);
                }
            } else {
                if (n6 == 0) {
                    n6 = n3;
                }
                if (n7 == 0) {
                    n7 = n2;
                }
            }
            if (n7 == 0) {
                n7 = 1;
            }
            if (n6 == 0) {
                n6 = 1;
            }
        }
        return new int[]{n7, n6};
    }

    public static ImageFrame scaleImageFrame(ImageFrame imageFrame, int n2, int n3, boolean bl) {
        int n4 = ImageStorage.getNumBands(imageFrame.getImageType());
        ByteBuffer byteBuffer = ImageTools.scaleImage((ByteBuffer)imageFrame.getImageData(), imageFrame.getWidth(), imageFrame.getHeight(), n4, n2, n3, bl);
        return new ImageFrame(imageFrame.getImageType(), byteBuffer, n2, n3, n2 * n4, null, imageFrame.getMetadata());
    }

    public static ByteBuffer scaleImage(ByteBuffer byteBuffer, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        PushbroomScaler pushbroomScaler = ScalerFactory.createScaler(n2, n3, n4, n5, n6, bl);
        int n7 = n2 * n4;
        if (byteBuffer.hasArray()) {
            byte[] arrby = byteBuffer.array();
            for (int i2 = 0; i2 != n3; ++i2) {
                pushbroomScaler.putSourceScanline(arrby, i2 * n7);
            }
        } else {
            byte[] arrby = new byte[n7];
            for (int i3 = 0; i3 != n3; ++i3) {
                byteBuffer.get(arrby);
                pushbroomScaler.putSourceScanline(arrby, 0);
            }
        }
        return pushbroomScaler.getDestination();
    }
}

