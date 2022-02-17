/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.iio.bmp.BMPImageLoaderFactory;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import com.sun.javafx.iio.ios.IosImageLoaderFactory;
import com.sun.javafx.iio.jpeg.JPEGImageLoaderFactory;
import com.sun.javafx.iio.png.PNGImageLoaderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageStorage {
    private static final HashMap<ImageFormatDescription.Signature, ImageLoaderFactory> loaderFactoriesBySignature;
    private static final ImageLoaderFactory[] loaderFactories;
    private static final boolean isIOS;
    private static int maxSignatureLength;

    public static ImageFormatDescription[] getSupportedDescriptions() {
        ImageFormatDescription[] arrimageFormatDescription = new ImageFormatDescription[loaderFactories.length];
        for (int i2 = 0; i2 < loaderFactories.length; ++i2) {
            arrimageFormatDescription[i2] = loaderFactories[i2].getFormatDescription();
        }
        return arrimageFormatDescription;
    }

    public static int getNumBands(ImageType imageType) {
        int n2 = -1;
        switch (imageType) {
            case GRAY: 
            case PALETTE: 
            case PALETTE_ALPHA: 
            case PALETTE_ALPHA_PRE: 
            case PALETTE_TRANS: {
                n2 = 1;
                break;
            }
            case GRAY_ALPHA: 
            case GRAY_ALPHA_PRE: {
                n2 = 2;
                break;
            }
            case RGB: {
                n2 = 3;
                break;
            }
            case RGBA: 
            case RGBA_PRE: {
                n2 = 4;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown ImageType " + (Object)((Object)imageType));
            }
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void addImageLoaderFactory(ImageLoaderFactory imageLoaderFactory) {
        ImageFormatDescription imageFormatDescription = imageLoaderFactory.getFormatDescription();
        for (ImageFormatDescription.Signature signature : imageFormatDescription.getSignatures()) {
            loaderFactoriesBySignature.put(signature, imageLoaderFactory);
        }
        Class<ImageStorage> class_ = ImageStorage.class;
        synchronized (ImageStorage.class) {
            maxSignatureLength = -1;
            // ** MonitorExit[var2_2] (shouldn't be in output)
            return;
        }
    }

    public static ImageFrame[] loadAll(InputStream inputStream, ImageLoadListener imageLoadListener, int n2, int n3, boolean bl, float f2, boolean bl2) throws ImageStorageException {
        ImageLoader imageLoader = null;
        try {
            imageLoader = isIOS ? IosImageLoaderFactory.getInstance().createImageLoader(inputStream) : ImageStorage.getLoaderBySignature(inputStream, imageLoadListener);
        }
        catch (IOException iOException) {
            throw new ImageStorageException(iOException.getMessage(), iOException);
        }
        ImageFrame[] arrimageFrame = null;
        if (imageLoader == null) {
            throw new ImageStorageException("No loader for image data");
        }
        arrimageFrame = ImageStorage.loadAll(imageLoader, n2, n3, bl, f2, bl2);
        return arrimageFrame;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ImageFrame[] loadAll(String string, ImageLoadListener imageLoadListener, int n2, int n3, boolean bl, float f2, boolean bl2) throws ImageStorageException {
        ImageFrame[] arrimageFrame;
        block16: {
            if (string == null || string.isEmpty()) {
                throw new ImageStorageException("URL can't be null or empty");
            }
            arrimageFrame = null;
            InputStream inputStream = null;
            ImageLoader imageLoader = null;
            try {
                float f3 = 1.0f;
                try {
                    if (f2 >= 1.5f) {
                        try {
                            String string2 = ImageTools.getScaledImageName(string);
                            inputStream = ImageTools.createInputStream(string2);
                            f3 = 2.0f;
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    if (inputStream == null) {
                        inputStream = ImageTools.createInputStream(string);
                    }
                    imageLoader = isIOS ? IosImageLoaderFactory.getInstance().createImageLoader(inputStream) : ImageStorage.getLoaderBySignature(inputStream, imageLoadListener);
                }
                catch (IOException iOException) {
                    throw new ImageStorageException(iOException.getMessage(), iOException);
                }
                if (imageLoader != null) {
                    arrimageFrame = ImageStorage.loadAll(imageLoader, n2, n3, bl, f3, bl2);
                    break block16;
                }
                throw new ImageStorageException("No loader for image data");
            }
            finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                catch (IOException iOException) {}
            }
        }
        return arrimageFrame;
    }

    private static synchronized int getMaxSignatureLength() {
        if (maxSignatureLength < 0) {
            maxSignatureLength = 0;
            for (ImageFormatDescription.Signature signature : loaderFactoriesBySignature.keySet()) {
                int n2 = signature.getLength();
                if (maxSignatureLength >= n2) continue;
                maxSignatureLength = n2;
            }
        }
        return maxSignatureLength;
    }

    private static ImageFrame[] loadAll(ImageLoader imageLoader, int n2, int n3, boolean bl, float f2, boolean bl2) throws ImageStorageException {
        ImageFrame[] arrimageFrame = null;
        ArrayList<ImageFrame> arrayList = new ArrayList<ImageFrame>();
        int n4 = 0;
        ImageFrame imageFrame = null;
        while (true) {
            try {
                imageFrame = imageLoader.load(n4++, n2, n3, bl, bl2);
            }
            catch (Exception exception) {
                if (n4 > 1) break;
                throw new ImageStorageException(exception.getMessage(), exception);
            }
            if (imageFrame == null) break;
            imageFrame.setPixelScale(f2);
            arrayList.add(imageFrame);
        }
        int n5 = arrayList.size();
        if (n5 > 0) {
            arrimageFrame = new ImageFrame[n5];
            arrayList.toArray(arrimageFrame);
        }
        return arrimageFrame;
    }

    private static ImageLoader getLoaderBySignature(InputStream inputStream, ImageLoadListener imageLoadListener) throws IOException {
        byte[] arrby = new byte[ImageStorage.getMaxSignatureLength()];
        ImageTools.readFully(inputStream, arrby);
        for (Map.Entry<ImageFormatDescription.Signature, ImageLoaderFactory> entry : loaderFactoriesBySignature.entrySet()) {
            if (!entry.getKey().matches(arrby)) continue;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(byteArrayInputStream, inputStream);
            ImageLoader imageLoader = entry.getValue().createImageLoader(sequenceInputStream);
            if (imageLoadListener != null) {
                imageLoader.addListener(imageLoadListener);
            }
            return imageLoader;
        }
        return null;
    }

    private ImageStorage() {
    }

    static {
        isIOS = PlatformUtil.isIOS();
        loaderFactories = isIOS ? new ImageLoaderFactory[]{IosImageLoaderFactory.getInstance()} : new ImageLoaderFactory[]{GIFImageLoaderFactory.getInstance(), JPEGImageLoaderFactory.getInstance(), PNGImageLoaderFactory.getInstance(), BMPImageLoaderFactory.getInstance()};
        loaderFactoriesBySignature = new HashMap(loaderFactories.length);
        for (int i2 = 0; i2 < loaderFactories.length; ++i2) {
            ImageStorage.addImageLoaderFactory(loaderFactories[i2]);
        }
    }

    public static enum ImageType {
        GRAY,
        GRAY_ALPHA,
        GRAY_ALPHA_PRE,
        PALETTE,
        PALETTE_ALPHA,
        PALETTE_ALPHA_PRE,
        PALETTE_TRANS,
        RGB,
        RGBA,
        RGBA_PRE;

    }
}

