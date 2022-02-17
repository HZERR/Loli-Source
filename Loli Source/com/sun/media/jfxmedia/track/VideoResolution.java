/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.track;

public class VideoResolution {
    public int width;
    public int height;

    public VideoResolution(int n2, int n3) {
        if (n2 <= 0) {
            throw new IllegalArgumentException("width <= 0");
        }
        if (n3 <= 0) {
            throw new IllegalArgumentException("height <= 0");
        }
        this.width = n2;
        this.height = n3;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String toString() {
        return "VideoResolution {width: " + this.width + " height: " + this.height + "}";
    }
}

