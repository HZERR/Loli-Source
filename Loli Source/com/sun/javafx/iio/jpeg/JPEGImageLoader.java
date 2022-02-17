/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.jpeg;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.jpeg.JPEGDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;

public class JPEGImageLoader
extends ImageLoaderImpl {
    public static final int JCS_UNKNOWN = 0;
    public static final int JCS_GRAYSCALE = 1;
    public static final int JCS_RGB = 2;
    public static final int JCS_YCbCr = 3;
    public static final int JCS_CMYK = 4;
    public static final int JCS_YCC = 5;
    public static final int JCS_RGBA = 6;
    public static final int JCS_YCbCrA = 7;
    public static final int JCS_YCCA = 10;
    public static final int JCS_YCCK = 11;
    private long structPointer = 0L;
    private int inWidth;
    private int inHeight;
    private int inColorSpaceCode;
    private int outColorSpaceCode;
    private byte[] iccData;
    private int outWidth;
    private int outHeight;
    private ImageStorage.ImageType outImageType;
    private boolean isDisposed = false;
    private Lock accessLock = new Lock();

    private static native void initJPEGMethodIDs(Class var0);

    private static native void disposeNative(long var0);

    private native long initDecompressor(InputStream var1) throws IOException;

    private native int startDecompression(long var1, int var3, int var4, int var5);

    private native boolean decompressIndirect(long var1, boolean var3, byte[] var4) throws IOException;

    private void setInputAttributes(int n2, int n3, int n4, int n5, int n6, byte[] arrby) {
        this.inWidth = n2;
        this.inHeight = n3;
        this.inColorSpaceCode = n4;
        this.outColorSpaceCode = n5;
        this.iccData = arrby;
        block0 : switch (n5) {
            case 1: {
                this.outImageType = ImageStorage.ImageType.GRAY;
                break;
            }
            case 2: 
            case 3: 
            case 5: {
                this.outImageType = ImageStorage.ImageType.RGB;
                break;
            }
            case 4: 
            case 6: 
            case 7: 
            case 10: 
            case 11: {
                this.outImageType = ImageStorage.ImageType.RGBA_PRE;
                break;
            }
            case 0: {
                switch (n6) {
                    case 1: {
                        this.outImageType = ImageStorage.ImageType.GRAY;
                        break block0;
                    }
                    case 3: {
                        this.outImageType = ImageStorage.ImageType.RGB;
                        break block0;
                    }
                    case 4: {
                        this.outImageType = ImageStorage.ImageType.RGBA_PRE;
                        break block0;
                    }
                }
                assert (false);
                break;
            }
            default: {
                assert (false);
                break;
            }
        }
    }

    private void setOutputAttributes(int n2, int n3) {
        this.outWidth = n2;
        this.outHeight = n3;
    }

    private void updateImageProgress(int n2) {
        this.updateImageProgress(100.0f * (float)n2 / (float)this.outHeight);
    }

    JPEGImageLoader(InputStream inputStream) throws IOException {
        super(JPEGDescriptor.getInstance());
        if (inputStream == null) {
            throw new IllegalArgumentException("input == null!");
        }
        try {
            this.structPointer = this.initDecompressor(inputStream);
        }
        catch (IOException iOException) {
            this.dispose();
            throw iOException;
        }
        if (this.structPointer == 0L) {
            throw new IOException("Unable to initialize JPEG decompressor");
        }
    }

    @Override
    public synchronized void dispose() {
        if (!this.accessLock.isLocked() && !this.isDisposed && this.structPointer != 0L) {
            this.isDisposed = true;
            JPEGImageLoader.disposeNative(this.structPointer);
            this.structPointer = 0L;
        }
    }

    protected void finalize() {
        this.dispose();
    }

    @Override
    public ImageFrame load(int n2, int n3, int n4, boolean bl, boolean bl2) throws IOException {
        int n5;
        if (n2 != 0) {
            return null;
        }
        this.accessLock.lock();
        int[] arrn = ImageTools.computeDimensions(this.inWidth, this.inHeight, n3, n4, bl);
        n3 = arrn[0];
        n4 = arrn[1];
        ImageMetadata imageMetadata = new ImageMetadata(null, true, null, null, null, null, null, n3, n4, null, null, null);
        this.updateImageMetadata(imageMetadata);
        ByteBuffer byteBuffer = null;
        try {
            n5 = this.startDecompression(this.structPointer, this.outColorSpaceCode, n3, n4);
            byte[] arrby = new byte[this.outWidth * this.outHeight * n5];
            byteBuffer = ByteBuffer.wrap(arrby);
            this.decompressIndirect(this.structPointer, this.listeners != null && !this.listeners.isEmpty(), byteBuffer.array());
        }
        catch (IOException iOException) {
            throw iOException;
        }
        finally {
            this.accessLock.unlock();
            this.dispose();
        }
        if (byteBuffer == null) {
            throw new IOException("Error decompressing JPEG stream!");
        }
        if (this.outWidth != n3 || this.outHeight != n4) {
            byteBuffer = ImageTools.scaleImage(byteBuffer, this.outWidth, this.outHeight, n5, n3, n4, bl2);
        }
        return new ImageFrame(this.outImageType, byteBuffer, n3, n4, n3 * n5, null, imageMetadata);
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_iio");
            return null;
        });
        JPEGImageLoader.initJPEGMethodIDs(InputStream.class);
    }

    private static class Lock {
        private boolean locked = false;

        public synchronized boolean isLocked() {
            return this.locked;
        }

        public synchronized void lock() {
            if (this.locked) {
                throw new IllegalStateException("Recursive loading is not allowed.");
            }
            this.locked = true;
        }

        public synchronized void unlock() {
            if (!this.locked) {
                throw new IllegalStateException("Invalid loader state.");
            }
            this.locked = false;
        }
    }
}

