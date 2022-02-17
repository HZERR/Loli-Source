/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ImageFrame {
    private ImageStorage.ImageType imageType;
    private ByteBuffer imageData;
    private int width;
    private int height;
    private int stride;
    private float pixelScale;
    private byte[][] palette;
    private ImageMetadata metadata;

    public ImageFrame(ImageStorage.ImageType imageType, ByteBuffer byteBuffer, int n2, int n3, int n4, byte[][] arrby, ImageMetadata imageMetadata) {
        this(imageType, byteBuffer, n2, n3, n4, arrby, 1.0f, imageMetadata);
    }

    public ImageFrame(ImageStorage.ImageType imageType, ByteBuffer byteBuffer, int n2, int n3, int n4, byte[][] arrby, float f2, ImageMetadata imageMetadata) {
        this.imageType = imageType;
        this.imageData = byteBuffer;
        this.width = n2;
        this.height = n3;
        this.stride = n4;
        this.palette = arrby;
        this.pixelScale = f2;
        this.metadata = imageMetadata;
    }

    public ImageStorage.ImageType getImageType() {
        return this.imageType;
    }

    public Buffer getImageData() {
        return this.imageData;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getStride() {
        return this.stride;
    }

    public byte[][] getPalette() {
        return this.palette;
    }

    public void setPixelScale(float f2) {
        this.pixelScale = f2;
    }

    public float getPixelScale() {
        return this.pixelScale;
    }

    public ImageMetadata getMetadata() {
        return this.metadata;
    }
}

