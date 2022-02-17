/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.ImageData
 *  org.eclipse.swt.graphics.PaletteData
 *  org.eclipse.swt.graphics.RGB
 */
package javafx.embed.swt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class SWTFXUtils {
    private static int blitSrc;
    private static boolean blitSrcCache;
    private static int alphaOpaque;
    private static boolean alphaOpaqueCache;
    private static int msbFirst;
    private static boolean msbFirstCache;
    private static Method blitDirect;
    private static Method blitPalette;
    private static Method getByteOrderMethod;

    private SWTFXUtils() {
    }

    public static WritableImage toFXImage(ImageData imageData, WritableImage writableImage) {
        Object object;
        int n2;
        byte[] arrby = SWTFXUtils.convertImage(imageData);
        if (arrby == null) {
            return null;
        }
        int n3 = imageData.width;
        int n4 = imageData.height;
        if (writableImage != null) {
            int n5 = (int)writableImage.getWidth();
            n2 = (int)writableImage.getHeight();
            if (n5 < n3 || n2 < n4) {
                writableImage = null;
            } else if (n3 < n5 || n4 < n2) {
                object = new int[n5];
                PixelWriter pixelWriter = writableImage.getPixelWriter();
                WritablePixelFormat<IntBuffer> writablePixelFormat = PixelFormat.getIntArgbPreInstance();
                if (n3 < n5) {
                    pixelWriter.setPixels(n3, 0, n5 - n3, n4, writablePixelFormat, (int[])object, 0, 0);
                }
                if (n4 < n2) {
                    pixelWriter.setPixels(0, n4, n5, n2 - n4, writablePixelFormat, (int[])object, 0, 0);
                }
            }
        }
        if (writableImage == null) {
            writableImage = new WritableImage(n3, n4);
        }
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        n2 = n3 * 4;
        object = PixelFormat.getByteBgraInstance();
        pixelWriter.setPixels(0, 0, n3, n4, (PixelFormat<ByteBuffer>)object, arrby, 0, n2);
        return writableImage;
    }

    public static ImageData fromFXImage(Image image, ImageData imageData) {
        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            return null;
        }
        int n2 = (int)image.getWidth();
        int n3 = (int)image.getHeight();
        int n4 = n2 * 4;
        int n5 = n4 * n3;
        byte[] arrby = new byte[n5];
        WritablePixelFormat<ByteBuffer> writablePixelFormat = PixelFormat.getByteBgraInstance();
        pixelReader.getPixels(0, 0, n2, n3, writablePixelFormat, arrby, 0, n4);
        byte[] arrby2 = new byte[n2 * n3];
        int n6 = 0;
        int n7 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n8 = 0;
            while (n8 < n2) {
                byte by = arrby[n6 + 3];
                arrby[n6 + 3] = 0;
                arrby2[n7++] = by;
                ++n8;
                n6 += 4;
            }
        }
        PaletteData paletteData = new PaletteData(65280, 0xFF0000, -16777216);
        imageData = new ImageData(n2, n3, 32, paletteData, 4, arrby);
        imageData.alphaData = arrby2;
        return imageData;
    }

    private static int BLIT_SRC() throws Exception {
        if (!blitSrcCache) {
            blitSrc = SWTFXUtils.readValue("BLIT_SRC");
            blitSrcCache = true;
        }
        return blitSrc;
    }

    private static int ALPHA_OPAQUE() throws Exception {
        if (!alphaOpaqueCache) {
            alphaOpaque = SWTFXUtils.readValue("ALPHA_OPAQUE");
            alphaOpaqueCache = true;
        }
        return alphaOpaque;
    }

    private static int MSB_FIRST() throws Exception {
        if (!msbFirstCache) {
            msbFirst = SWTFXUtils.readValue("MSB_FIRST");
            msbFirstCache = true;
        }
        return msbFirst;
    }

    private static int readValue(String string) throws Exception {
        Class<ImageData> class_ = ImageData.class;
        return AccessController.doPrivileged(() -> {
            Field field = class_.getDeclaredField(string);
            field.setAccessible(true);
            return field.getInt(class_);
        });
    }

    private static void blit(int n2, byte[] arrby, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13, byte[] arrby2, int n14, int n15, int n16, byte[] arrby3, int n17, int n18, int n19, int n20, int n21, int n22, int n23, int n24, int n25, int n26, boolean bl, boolean bl2) throws Exception {
        Class<ImageData> class_ = ImageData.class;
        if (blitDirect == null) {
            Class<Integer> class_2 = Integer.TYPE;
            Class<Boolean> class_3 = Boolean.TYPE;
            Class<byte[]> class_4 = byte[].class;
            Class[] arrclass = new Class[]{class_2, class_4, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_4, class_2, class_2, class_2, class_4, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_3, class_3};
            blitDirect = AccessController.doPrivileged(() -> {
                Method method = class_.getDeclaredMethod("blit", arrclass);
                method.setAccessible(true);
                return method;
            });
        }
        if (blitDirect != null) {
            blitDirect.invoke(class_, n2, arrby, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, arrby2, n14, n15, n16, arrby3, n17, n18, n19, n20, n21, n22, n23, n24, n25, n26, bl, bl2);
        }
    }

    private static void blit(int n2, byte[] arrby, int n3, int n4, int n5, int n6, int n7, int n8, int n9, byte[] arrby2, byte[] arrby3, byte[] arrby4, int n10, byte[] arrby5, int n11, int n12, int n13, byte[] arrby6, int n14, int n15, int n16, int n17, int n18, int n19, int n20, int n21, int n22, int n23, boolean bl, boolean bl2) throws Exception {
        Class<ImageData> class_ = ImageData.class;
        if (blitPalette == null) {
            Class<Integer> class_2 = Integer.TYPE;
            Class<Boolean> class_3 = Boolean.TYPE;
            Class<byte[]> class_4 = byte[].class;
            Class[] arrclass = new Class[]{class_2, class_4, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_4, class_4, class_4, class_2, class_4, class_2, class_2, class_2, class_4, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_2, class_3, class_3};
            blitPalette = AccessController.doPrivileged(() -> {
                Method method = class_.getDeclaredMethod("blit", arrclass);
                method.setAccessible(true);
                return method;
            });
        }
        if (blitPalette != null) {
            blitPalette.invoke(class_, n2, arrby, n3, n4, n5, n6, n7, n8, n9, arrby2, arrby3, arrby4, n10, arrby5, n11, n12, n13, arrby6, n14, n15, n16, n17, n18, n19, n20, n21, n22, n23, bl, bl2);
        }
    }

    private static int getByteOrder(ImageData imageData) throws Exception {
        Class<ImageData> class_ = ImageData.class;
        if (getByteOrderMethod != null) {
            getByteOrderMethod = AccessController.doPrivileged(() -> {
                Method method = class_.getDeclaredMethod("getByteOrder", new Class[0]);
                method.setAccessible(true);
                return method;
            });
        }
        if (getByteOrderMethod != null) {
            return (Integer)getByteOrderMethod.invoke((Object)imageData, new Object[0]);
        }
        return SWTFXUtils.MSB_FIRST();
    }

    private static byte[] convertImage(ImageData imageData) {
        byte[] arrby = null;
        try {
            int n2;
            byte[] arrby2;
            Object object;
            int n3;
            PaletteData paletteData = imageData.palette;
            if ((imageData.depth != 1 && imageData.depth != 2 && imageData.depth != 4 && imageData.depth != 8 || paletteData.isDirect) && imageData.depth != 8 && (imageData.depth != 16 && imageData.depth != 24 && imageData.depth != 32 || !paletteData.isDirect)) {
                return null;
            }
            int n4 = SWTFXUtils.BLIT_SRC();
            int n5 = SWTFXUtils.ALPHA_OPAQUE();
            int n6 = SWTFXUtils.MSB_FIRST();
            int n7 = imageData.width;
            int n8 = imageData.height;
            int n9 = SWTFXUtils.getByteOrder(imageData);
            int n10 = 3;
            int n11 = 65280;
            int n12 = 0xFF0000;
            int n13 = -16777216;
            int n14 = n7 * n8 * 4;
            int n15 = n7 * 4;
            arrby = new byte[n14];
            if (paletteData.isDirect) {
                SWTFXUtils.blit(n4, imageData.data, imageData.depth, imageData.bytesPerLine, n9, 0, 0, n7, n8, paletteData.redMask, paletteData.greenMask, paletteData.blueMask, n5, null, 0, 0, 0, arrby, 32, n15, n6, 0, 0, n7, n8, n11, n12, n13, false, false);
            } else {
                RGB[] arrrGB = paletteData.getRGBs();
                n3 = arrrGB.length;
                object = new byte[n3];
                arrby2 = new byte[n3];
                byte[] arrby3 = new byte[n3];
                for (n2 = 0; n2 < arrrGB.length; ++n2) {
                    RGB rGB = arrrGB[n2];
                    if (rGB == null) continue;
                    object[n2] = (byte)rGB.red;
                    arrby2[n2] = (byte)rGB.green;
                    arrby3[n2] = (byte)rGB.blue;
                }
                SWTFXUtils.blit(n4, imageData.data, imageData.depth, imageData.bytesPerLine, n9, 0, 0, n7, n8, object, arrby2, arrby3, n5, null, 0, 0, 0, arrby, 32, n15, n6, 0, 0, n7, n8, n11, n12, n13, false, false);
            }
            int n16 = imageData.getTransparencyType();
            int n17 = n3 = n16 != 0 ? 1 : 0;
            if (n16 == 2 || imageData.transparentPixel != -1) {
                object = (Object)imageData.getTransparencyMask();
                arrby2 = object.data;
                int n18 = object.bytesPerLine;
                n2 = 0;
                int n19 = 0;
                for (int i2 = 0; i2 < n8; ++i2) {
                    for (int i3 = 0; i3 < n7; ++i3) {
                        byte by = arrby2[n19 + (i3 >> 3)];
                        int n20 = 1 << 7 - (i3 & 7);
                        arrby[n2 + n10] = (by & n20) != 0 ? -1 : 0;
                        n2 += 4;
                    }
                    n19 += n18;
                }
            } else if (imageData.alpha != -1) {
                n3 = 1;
                int n21 = imageData.alpha;
                byte by = (byte)n21;
                for (int i4 = 0; i4 < arrby.length; i4 += 4) {
                    arrby[i4 + n10] = by;
                }
            } else if (imageData.alphaData != null) {
                n3 = 1;
                object = new byte[imageData.alphaData.length];
                System.arraycopy(imageData.alphaData, 0, object, 0, ((byte[])object).length);
                int n22 = 0;
                int n23 = 0;
                for (n2 = 0; n2 < n8; ++n2) {
                    for (int i5 = 0; i5 < n7; ++i5) {
                        arrby[n22 + n10] = object[n23];
                        n22 += 4;
                        ++n23;
                    }
                }
            }
            if (n3 == 0) {
                for (int i6 = 0; i6 < arrby.length; i6 += 4) {
                    arrby[i6 + n10] = -1;
                }
            }
        }
        catch (Exception exception) {
            return null;
        }
        return arrby;
    }
}

