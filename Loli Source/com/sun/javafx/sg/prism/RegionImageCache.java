/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.RectanglePacker;
import java.util.HashMap;
import javafx.scene.layout.Background;

class RegionImageCache {
    private static final int MAX_SIZE = 90000;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private HashMap<Integer, CachedImage> imageMap = new HashMap();
    private RTTexture backingStore;
    private RectanglePacker hPacker;
    private RectanglePacker vPacker;

    RegionImageCache(ResourceFactory resourceFactory) {
        int n2;
        Texture.WrapMode wrapMode;
        if (resourceFactory.isWrapModeSupported(Texture.WrapMode.CLAMP_TO_ZERO)) {
            wrapMode = Texture.WrapMode.CLAMP_TO_ZERO;
            n2 = 0;
        } else {
            wrapMode = Texture.WrapMode.CLAMP_NOT_NEEDED;
            n2 = 1;
        }
        this.backingStore = resourceFactory.createRTTexture(2048, 1024, wrapMode);
        this.backingStore.contentsUseful();
        this.backingStore.makePermanent();
        resourceFactory.setRegionTexture(this.backingStore);
        this.hPacker = new RectanglePacker(this.backingStore, n2, n2, 1024 - n2, 1024 - n2, false);
        this.vPacker = new RectanglePacker(this.backingStore, 1024, n2, 1024, 1024 - n2, true);
    }

    boolean isImageCachable(int n2, int n3) {
        return 0 < n2 && n2 < 1024 && 0 < n3 && n3 < 1024 && n2 * n3 < 90000;
    }

    RTTexture getBackingStore() {
        return this.backingStore;
    }

    boolean getImageLocation(Integer n2, Rectangle rectangle, Background background, Shape shape, Graphics graphics) {
        RectanglePacker rectanglePacker;
        CachedImage cachedImage = this.imageMap.get(n2);
        if (cachedImage != null) {
            if (cachedImage.equals(rectangle.width, rectangle.height, background, shape)) {
                rectangle.x = cachedImage.x;
                rectangle.y = cachedImage.y;
                return false;
            }
            rectangle.height = -1;
            rectangle.width = -1;
            return false;
        }
        boolean bl = rectangle.height > 64;
        RectanglePacker rectanglePacker2 = rectanglePacker = bl ? this.vPacker : this.hPacker;
        if (!rectanglePacker.add(rectangle)) {
            graphics.sync();
            this.vPacker.clear();
            this.hPacker.clear();
            this.imageMap.clear();
            rectanglePacker.add(rectangle);
            this.backingStore.createGraphics().clear();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter("Region image cache flushed");
            }
        }
        this.imageMap.put(n2, new CachedImage(rectangle, background, shape));
        return true;
    }

    static class CachedImage {
        Background background;
        Shape shape;
        int x;
        int y;
        int width;
        int height;

        CachedImage(Rectangle rectangle, Background background, Shape shape) {
            this.x = rectangle.x;
            this.y = rectangle.y;
            this.width = rectangle.width;
            this.height = rectangle.height;
            this.background = background;
            this.shape = shape;
        }

        public boolean equals(int n2, int n3, Background background, Shape shape) {
            return this.width == n2 && this.height == n3 && (this.background == null ? background == null : this.background.equals(background)) && (this.shape == null ? shape == null : this.shape.equals(shape));
        }
    }
}

