/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.ios;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageDescriptor;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;

public class IosImageLoader
extends ImageLoaderImpl {
    public static final int GRAY = 0;
    public static final int GRAY_ALPHA = 1;
    public static final int GRAY_ALPHA_PRE = 2;
    public static final int PALETTE = 3;
    public static final int PALETTE_ALPHA = 4;
    public static final int PALETTE_ALPHA_PRE = 5;
    public static final int PALETTE_TRANS = 6;
    public static final int RGB = 7;
    public static final int RGBA = 8;
    public static final int RGBA_PRE = 9;
    private static final Map<Integer, ImageStorage.ImageType> colorSpaceMapping = new HashMap<Integer, ImageStorage.ImageType>();
    private long structPointer;
    private int inWidth;
    private int inHeight;
    private int nImages;
    private boolean isDisposed = false;
    private int delayTime;
    private int loopCount;

    private static native void initNativeLoading();

    private native long loadImage(InputStream var1, boolean var2) throws IOException;

    private native long loadImageFromURL(String var1, boolean var2) throws IOException;

    private native void resizeImage(long var1, int var3, int var4);

    private native byte[] getImageBuffer(long var1, int var3);

    private native int getNumberOfComponents(long var1);

    private native int getColorSpaceCode(long var1);

    private native int getDelayTime(long var1);

    private static native void disposeLoader(long var0);

    private void setInputParameters(int n2, int n3, int n4, int n5) {
        this.inWidth = n2;
        this.inHeight = n3;
        this.nImages = n4;
        this.loopCount = n5;
    }

    private void updateProgress(float f2) {
        this.updateImageProgress(f2);
    }

    private boolean shouldReportProgress() {
        return this.listeners != null && !this.listeners.isEmpty();
    }

    private void checkNativePointer() throws IOException {
        if (this.structPointer == 0L) {
            throw new IOException("Unable to initialize image native loader!");
        }
    }

    private void retrieveDelayTime() {
        if (this.nImages > 1) {
            this.delayTime = this.getDelayTime(this.structPointer);
        }
    }

    public IosImageLoader(String string, ImageDescriptor imageDescriptor) throws IOException {
        super(imageDescriptor);
        try {
            URL uRL = new URL(string);
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException("Image loader: Malformed URL!");
        }
        try {
            this.structPointer = this.loadImageFromURL(string, this.shouldReportProgress());
        }
        catch (IOException iOException) {
            this.dispose();
            throw iOException;
        }
        this.checkNativePointer();
        this.retrieveDelayTime();
    }

    public IosImageLoader(InputStream inputStream, ImageDescriptor imageDescriptor) throws IOException {
        super(imageDescriptor);
        if (inputStream == null) {
            throw new IllegalArgumentException("Image loader: input stream == null");
        }
        try {
            this.structPointer = this.loadImage(inputStream, this.shouldReportProgress());
        }
        catch (IOException iOException) {
            this.dispose();
            throw iOException;
        }
        this.checkNativePointer();
        this.retrieveDelayTime();
    }

    @Override
    public synchronized void dispose() {
        if (!this.isDisposed && this.structPointer != 0L) {
            this.isDisposed = true;
            IosImageLoader.disposeLoader(this.structPointer);
            this.structPointer = 0L;
        }
    }

    protected void finalize() {
        this.dispose();
    }

    @Override
    public ImageFrame load(int n2, int n3, int n4, boolean bl, boolean bl2) throws IOException {
        if (n2 >= this.nImages) {
            this.dispose();
            return null;
        }
        int[] arrn = ImageTools.computeDimensions(this.inWidth, this.inHeight, n3, n4, bl);
        n3 = arrn[0];
        n4 = arrn[1];
        ImageMetadata imageMetadata = new ImageMetadata(null, true, null, null, null, this.delayTime == 0 ? null : Integer.valueOf(this.delayTime), this.nImages > 1 ? Integer.valueOf(this.loopCount) : null, n3, n4, null, null, null);
        this.updateImageMetadata(imageMetadata);
        this.resizeImage(this.structPointer, n3, n4);
        int n5 = this.getNumberOfComponents(this.structPointer);
        int n6 = this.getColorSpaceCode(this.structPointer);
        ImageStorage.ImageType imageType = colorSpaceMapping.get(n6);
        byte[] arrby = this.getImageBuffer(this.structPointer, n2);
        return new ImageFrame(imageType, ByteBuffer.wrap(arrby), n3, n4, n3 * n5, null, imageMetadata);
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("nativeiio");
            return null;
        });
        colorSpaceMapping.put(0, ImageStorage.ImageType.GRAY);
        colorSpaceMapping.put(1, ImageStorage.ImageType.GRAY_ALPHA);
        colorSpaceMapping.put(2, ImageStorage.ImageType.GRAY_ALPHA_PRE);
        colorSpaceMapping.put(3, ImageStorage.ImageType.PALETTE);
        colorSpaceMapping.put(4, ImageStorage.ImageType.PALETTE_ALPHA);
        colorSpaceMapping.put(5, ImageStorage.ImageType.PALETTE_ALPHA_PRE);
        colorSpaceMapping.put(6, ImageStorage.ImageType.PALETTE_TRANS);
        colorSpaceMapping.put(7, ImageStorage.ImageType.RGB);
        colorSpaceMapping.put(8, ImageStorage.ImageType.RGBA);
        colorSpaceMapping.put(9, ImageStorage.ImageType.RGBA_PRE);
        IosImageLoader.initNativeLoading();
    }
}

