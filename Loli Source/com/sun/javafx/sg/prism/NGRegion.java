/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.EffectFilter;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.javafx.sg.prism.RegionImageCache;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Offset;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class NGRegion
extends NGGroup {
    private static final Affine2D SCRATCH_AFFINE = new Affine2D();
    private static final Rectangle TEMP_RECT = new Rectangle();
    private static WeakHashMap<Screen, RegionImageCache> imageCacheMap = new WeakHashMap();
    private static final int CACHE_SLICE_V = 1;
    private static final int CACHE_SLICE_H = 2;
    private Background background = Background.EMPTY;
    private Insets backgroundInsets = Insets.EMPTY;
    private Border border = Border.EMPTY;
    private List<CornerRadii> normalizedFillCorners;
    private List<CornerRadii> normalizedStrokeCorners;
    private Shape shape;
    private NGShape ngShape;
    private boolean scaleShape = true;
    private boolean centerShape = true;
    private boolean cacheShape = false;
    private float opaqueTop = Float.NaN;
    private float opaqueRight = Float.NaN;
    private float opaqueBottom = Float.NaN;
    private float opaqueLeft = Float.NaN;
    private float width;
    private float height;
    private int cacheMode;
    private Integer cacheKey;
    private static final Offset nopEffect = new Offset(0, 0, null);
    private EffectFilter nopEffectFilter;

    static Paint getPlatformPaint(javafx.scene.paint.Paint paint) {
        return (Paint)Toolkit.getPaintAccessor().getPlatformPaint(paint);
    }

    public void updateShape(Object object, boolean bl, boolean bl2, boolean bl3) {
        this.ngShape = object == null ? null : (NGShape)((javafx.scene.shape.Shape)object).impl_getPeer();
        this.shape = object == null ? null : this.ngShape.getShape();
        this.scaleShape = bl;
        this.centerShape = bl2;
        this.cacheShape = bl3;
        this.invalidateOpaqueRegion();
        this.cacheKey = null;
        this.visualsChanged();
    }

    public void setSize(float f2, float f3) {
        this.width = f2;
        this.height = f3;
        this.invalidateOpaqueRegion();
        this.cacheKey = null;
        if (this.background != null && this.background.isFillPercentageBased()) {
            this.backgroundInsets = null;
        }
    }

    public void imagesUpdated() {
        this.visualsChanged();
    }

    public void updateBorder(Border border) {
        Border border2 = this.border;
        Border border3 = this.border = border == null ? Border.EMPTY : border;
        if (!this.border.getOutsets().equals(border2.getOutsets())) {
            this.geometryChanged();
        } else {
            this.visualsChanged();
        }
    }

    public void updateStrokeCorners(List<CornerRadii> list) {
        this.normalizedStrokeCorners = list;
    }

    private CornerRadii getNormalizedStrokeRadii(int n2) {
        return this.normalizedStrokeCorners == null ? this.border.getStrokes().get(n2).getRadii() : this.normalizedStrokeCorners.get(n2);
    }

    public void updateBackground(Background background) {
        Background background2 = this.background;
        this.background = background == null ? Background.EMPTY : background;
        List<BackgroundFill> list = this.background.getFills();
        this.cacheMode = 0;
        if (!(PrismSettings.disableRegionCaching || list.isEmpty() || this.shape != null && !this.cacheShape)) {
            this.cacheMode = 3;
            int n2 = list.size();
            for (int i2 = 0; i2 < n2 && this.cacheMode != 0; ++i2) {
                BackgroundFill backgroundFill = list.get(i2);
                javafx.scene.paint.Paint paint = backgroundFill.getFill();
                if (this.shape == null) {
                    if (paint instanceof LinearGradient) {
                        LinearGradient linearGradient = (LinearGradient)paint;
                        if (linearGradient.getStartX() != linearGradient.getEndX()) {
                            this.cacheMode &= 0xFFFFFFFD;
                        }
                        if (linearGradient.getStartY() == linearGradient.getEndY()) continue;
                        this.cacheMode &= 0xFFFFFFFE;
                        continue;
                    }
                    if (paint instanceof Color) continue;
                    this.cacheMode = 0;
                    continue;
                }
                if (!(paint instanceof ImagePattern)) continue;
                this.cacheMode = 0;
            }
        }
        this.backgroundInsets = null;
        this.cacheKey = null;
        if (!this.background.getOutsets().equals(background2.getOutsets())) {
            this.geometryChanged();
        } else {
            this.visualsChanged();
        }
    }

    public void updateFillCorners(List<CornerRadii> list) {
        this.normalizedFillCorners = list;
    }

    private CornerRadii getNormalizedFillRadii(int n2) {
        return this.normalizedFillCorners == null ? this.background.getFills().get(n2).getRadii() : this.normalizedFillCorners.get(n2);
    }

    public void setOpaqueInsets(float f2, float f3, float f4, float f5) {
        this.opaqueTop = f2;
        this.opaqueRight = f3;
        this.opaqueBottom = f4;
        this.opaqueLeft = f5;
        this.invalidateOpaqueRegion();
    }

    @Override
    public void clearDirtyTree() {
        super.clearDirtyTree();
        if (this.ngShape != null) {
            this.ngShape.clearDirtyTree();
        }
    }

    private RegionImageCache getImageCache(Graphics graphics) {
        RTTexture rTTexture;
        Screen screen = graphics.getAssociatedScreen();
        RegionImageCache regionImageCache = imageCacheMap.get(screen);
        if (regionImageCache != null && (rTTexture = regionImageCache.getBackingStore()).isSurfaceLost()) {
            imageCacheMap.remove(screen);
            regionImageCache = null;
        }
        if (regionImageCache == null) {
            regionImageCache = new RegionImageCache(graphics.getResourceFactory());
            imageCacheMap.put(screen, regionImageCache);
        }
        return regionImageCache;
    }

    private Integer getCacheKey(int n2, int n3) {
        if (this.cacheKey == null) {
            int n4 = 31 * n2;
            n4 = n4 * 37 + n3;
            n4 = n4 * 47 + this.background.hashCode();
            if (this.shape != null) {
                n4 = n4 * 73 + this.shape.hashCode();
            }
            this.cacheKey = n4;
        }
        return this.cacheKey;
    }

    @Override
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && !Float.isNaN(this.opaqueTop) && !Float.isNaN(this.opaqueRight) && !Float.isNaN(this.opaqueBottom) && !Float.isNaN(this.opaqueLeft);
    }

    @Override
    protected RectBounds computeOpaqueRegion(RectBounds rectBounds) {
        return (RectBounds)rectBounds.deriveWithNewBounds(this.opaqueLeft, this.opaqueTop, 0.0f, this.width - this.opaqueRight, this.height - this.opaqueBottom, 0.0f);
    }

    @Override
    protected NGNode.RenderRootResult computeRenderRoot(NodePath nodePath, RectBounds rectBounds, int n2, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        NGNode.RenderRootResult renderRootResult = super.computeRenderRoot(nodePath, rectBounds, n2, baseTransform, generalTransform3D);
        if (renderRootResult == NGNode.RenderRootResult.NO_RENDER_ROOT) {
            renderRootResult = this.computeNodeRenderRoot(nodePath, rectBounds, n2, baseTransform, generalTransform3D);
        }
        return renderRootResult;
    }

    @Override
    protected boolean hasVisuals() {
        return !this.border.isEmpty() || !this.background.isEmpty();
    }

    @Override
    protected boolean hasOverlappingContents() {
        return true;
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if (!graphics.getTransformNoClone().is2D() && this.isContentBounds2D()) {
            assert (this.getEffectFilter() == null);
            if (this.nopEffectFilter == null) {
                this.nopEffectFilter = new EffectFilter(nopEffect, this);
            }
            this.nopEffectFilter.render(graphics);
            return;
        }
        if (this.shape != null) {
            this.renderAsShape(graphics);
        } else if (this.width > 0.0f && this.height > 0.0f) {
            this.renderAsRectangle(graphics);
        }
        super.renderContent(graphics);
    }

    private void renderAsShape(Graphics graphics) {
        Object object;
        if (!this.background.isEmpty()) {
            RegionImageCache regionImageCache;
            object = this.background.getOutsets();
            Shape shape = this.resizeShape((float)(-((Insets)object).getTop()), (float)(-((Insets)object).getRight()), (float)(-((Insets)object).getBottom()), (float)(-((Insets)object).getLeft()));
            RectBounds rectBounds = shape.getBounds();
            int n2 = Math.round(rectBounds.getWidth());
            int n3 = Math.round(rectBounds.getHeight());
            RTTexture rTTexture = null;
            Rectangle rectangle = null;
            if (this.cacheMode != 0 && graphics.getTransformNoClone().isTranslateOrIdentity() && (regionImageCache = this.getImageCache(graphics)).isImageCachable(n2, n3)) {
                Integer n4 = this.getCacheKey(n2, n3);
                rectangle = TEMP_RECT;
                rectangle.setBounds(0, 0, n2 + 1, n3 + 1);
                boolean bl = regionImageCache.getImageLocation(n4, rectangle, this.background, this.shape, graphics);
                if (!rectangle.isEmpty()) {
                    rTTexture = regionImageCache.getBackingStore();
                }
                if (rTTexture != null && bl) {
                    Graphics graphics2 = rTTexture.createGraphics();
                    graphics2.translate((float)rectangle.x - rectBounds.getMinX(), (float)rectangle.y - rectBounds.getMinY());
                    this.renderBackgroundShape(graphics2);
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter("Rendering region shape image to cache");
                    }
                }
            }
            if (rTTexture != null) {
                float f2 = rectBounds.getMinX();
                float f3 = rectBounds.getMinY();
                float f4 = rectBounds.getMaxX();
                float f5 = rectBounds.getMaxY();
                float f6 = rectangle.x;
                float f7 = rectangle.y;
                float f8 = f6 + (float)n2;
                float f9 = f7 + (float)n3;
                graphics.drawTexture(rTTexture, f2, f3, f4, f5, f6, f7, f8, f9);
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.incrementCounter("Cached region shape image used");
                }
            } else {
                this.renderBackgroundShape(graphics);
            }
        }
        if (!this.border.isEmpty()) {
            object = this.border.getStrokes();
            int n5 = object.size();
            for (int i2 = 0; i2 < n5; ++i2) {
                BorderStroke borderStroke = (BorderStroke)object.get(i2);
                this.setBorderStyle(graphics, borderStroke, -1.0, false);
                Insets insets = borderStroke.getInsets();
                graphics.draw(this.resizeShape((float)insets.getTop(), (float)insets.getRight(), (float)insets.getBottom(), (float)insets.getLeft()));
            }
        }
    }

    private void renderBackgroundShape(Graphics graphics) {
        Object object;
        Object object2;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("NGRegion renderBackgroundShape slow path");
            PulseLogger.addMessage("Slow shape path for " + this.getName());
        }
        List<BackgroundFill> list = this.background.getFills();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            BackgroundFill backgroundFill = list.get(i2);
            object2 = NGRegion.getPlatformPaint(backgroundFill.getFill());
            assert (object2 != null);
            graphics.setPaint((Paint)object2);
            object = backgroundFill.getInsets();
            graphics.fill(this.resizeShape((float)((Insets)object).getTop(), (float)((Insets)object).getRight(), (float)((Insets)object).getBottom(), (float)((Insets)object).getLeft()));
        }
        List<BackgroundImage> list2 = this.background.getImages();
        int n3 = list2.size();
        for (n2 = 0; n2 < n3; ++n2) {
            object2 = list2.get(n2);
            object = (Image)((BackgroundImage)object2).getImage().impl_getPlatformImage();
            if (object == null) continue;
            Shape shape = this.resizeShape(0.0f, 0.0f, 0.0f, 0.0f);
            RectBounds rectBounds = shape.getBounds();
            com.sun.prism.paint.ImagePattern imagePattern = ((BackgroundImage)object2).getSize().isCover() ? new com.sun.prism.paint.ImagePattern((Image)object, rectBounds.getMinX(), rectBounds.getMinY(), rectBounds.getWidth(), rectBounds.getHeight(), false, false) : new com.sun.prism.paint.ImagePattern((Image)object, rectBounds.getMinX(), rectBounds.getMinY(), ((Image)object).getWidth(), ((Image)object).getHeight(), false, false);
            graphics.setPaint(imagePattern);
            graphics.fill(shape);
        }
    }

    private void renderAsRectangle(Graphics graphics) {
        if (!this.background.isEmpty()) {
            this.renderBackgroundRectangle(graphics);
        }
        if (!this.border.isEmpty()) {
            this.renderBorderRectangle(graphics);
        }
    }

    private void renderBackgroundRectangle(Graphics graphics) {
        Object object;
        int n2;
        Object object2;
        if (this.backgroundInsets == null) {
            this.updateBackgroundInsets();
        }
        double d2 = this.backgroundInsets.getLeft() + 1.0;
        double d3 = this.backgroundInsets.getRight() + 1.0;
        double d4 = this.backgroundInsets.getTop() + 1.0;
        double d5 = this.backgroundInsets.getBottom() + 1.0;
        int n3 = this.roundUp(this.width);
        if ((this.cacheMode & 2) != 0) {
            n3 = Math.min(n3, (int)(d2 + d3));
        }
        int n4 = this.roundUp(this.height);
        if ((this.cacheMode & 1) != 0) {
            n4 = Math.min(n4, (int)(d4 + d5));
        }
        Insets insets = this.background.getOutsets();
        int n5 = this.roundUp(insets.getTop());
        int n6 = this.roundUp(insets.getRight());
        int n7 = this.roundUp(insets.getBottom());
        int n8 = this.roundUp(insets.getLeft());
        int n9 = n8 + n3 + n6;
        int n10 = n5 + n4 + n7;
        boolean bl = this.background.getFills().size() > 1 && this.cacheMode != 0 && graphics.getTransformNoClone().isTranslateOrIdentity();
        RTTexture rTTexture = null;
        Rectangle rectangle = null;
        if (bl && ((RegionImageCache)(object2 = this.getImageCache(graphics))).isImageCachable(n9, n10)) {
            Integer n11 = this.getCacheKey(n9, n10);
            rectangle = TEMP_RECT;
            rectangle.setBounds(0, 0, n9 + 1, n10 + 1);
            n2 = ((RegionImageCache)object2).getImageLocation(n11, rectangle, this.background, this.shape, graphics) ? 1 : 0;
            if (!rectangle.isEmpty()) {
                rTTexture = ((RegionImageCache)object2).getBackingStore();
            }
            if (rTTexture != null && n2 != 0) {
                object = rTTexture.createGraphics();
                object.translate(rectangle.x + n8, rectangle.y + n5);
                this.renderBackgroundRectanglesDirectly((Graphics)object, n3, n4);
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.incrementCounter("Rendering region background image to cache");
                }
            }
        }
        if (rTTexture != null) {
            this.renderBackgroundRectangleFromCache(graphics, rTTexture, rectangle, n9, n10, d4, d3, d5, d2, n5, n6, n7, n8);
        } else {
            this.renderBackgroundRectanglesDirectly(graphics, this.width, this.height);
        }
        object2 = this.background.getImages();
        n2 = object2.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            double d6;
            double d7;
            double d8;
            double d9;
            double d10;
            double d11;
            object = (BackgroundImage)object2.get(i2);
            Image image = (Image)((BackgroundImage)object).getImage().impl_getPlatformImage();
            if (image == null) continue;
            int n12 = (int)((BackgroundImage)object).getImage().getWidth();
            int n13 = (int)((BackgroundImage)object).getImage().getHeight();
            int n14 = image.getWidth();
            int n15 = image.getHeight();
            if (n14 == 0 || n15 == 0) continue;
            BackgroundSize backgroundSize = ((BackgroundImage)object).getSize();
            if (backgroundSize.isCover()) {
                float f2 = Math.max(this.width / (float)n14, this.height / (float)n15);
                Texture texture = graphics.getResourceFactory().getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
                graphics.drawTexture(texture, 0.0f, 0.0f, this.width, this.height, 0.0f, 0.0f, this.width / f2, this.height / f2);
                texture.unlock();
                continue;
            }
            double d12 = backgroundSize.isWidthAsPercentage() ? backgroundSize.getWidth() * (double)this.width : backgroundSize.getWidth();
            double d13 = d11 = backgroundSize.isHeightAsPercentage() ? backgroundSize.getHeight() * (double)this.height : backgroundSize.getHeight();
            if (backgroundSize.isContain()) {
                float f3 = this.width / (float)n12;
                float f4 = this.height / (float)n13;
                float f5 = Math.min(f3, f4);
                d10 = Math.ceil(f5 * (float)n12);
                d9 = Math.ceil(f5 * (float)n13);
            } else if (backgroundSize.getWidth() >= 0.0 && backgroundSize.getHeight() >= 0.0) {
                d10 = d12;
                d9 = d11;
            } else if (d12 >= 0.0) {
                d10 = d12;
                double d14 = d10 / (double)n12;
                d9 = (double)n13 * d14;
            } else if (d11 >= 0.0) {
                d9 = d11;
                double d15 = d9 / (double)n13;
                d10 = (double)n12 * d15;
            } else {
                d10 = n12;
                d9 = n13;
            }
            BackgroundPosition backgroundPosition = ((BackgroundImage)object).getPosition();
            if (backgroundPosition.getHorizontalSide() == Side.LEFT) {
                d8 = backgroundPosition.getHorizontalPosition();
                d7 = backgroundPosition.isHorizontalAsPercentage() ? d8 * (double)this.width - d8 * d10 : d8;
            } else if (backgroundPosition.isHorizontalAsPercentage()) {
                d8 = 1.0 - backgroundPosition.getHorizontalPosition();
                d7 = d8 * (double)this.width - d8 * d10;
            } else {
                d7 = (double)this.width - d10 - backgroundPosition.getHorizontalPosition();
            }
            if (backgroundPosition.getVerticalSide() == Side.TOP) {
                d8 = backgroundPosition.getVerticalPosition();
                d6 = backgroundPosition.isVerticalAsPercentage() ? d8 * (double)this.height - d8 * d9 : d8;
            } else if (backgroundPosition.isVerticalAsPercentage()) {
                d8 = 1.0 - backgroundPosition.getVerticalPosition();
                d6 = d8 * (double)this.height - d8 * d9;
            } else {
                d6 = (double)this.height - d9 - backgroundPosition.getVerticalPosition();
            }
            this.paintTiles(graphics, image, ((BackgroundImage)object).getRepeatX(), ((BackgroundImage)object).getRepeatY(), backgroundPosition.getHorizontalSide(), backgroundPosition.getVerticalSide(), 0.0f, 0.0f, this.width, this.height, 0, 0, n14, n15, (float)d7, (float)d6, (float)d10, (float)d9);
        }
    }

    private void renderBackgroundRectangleFromCache(Graphics graphics, RTTexture rTTexture, Rectangle rectangle, int n2, int n3, double d2, double d3, double d4, double d5, int n4, int n5, int n6, int n7) {
        double d6;
        float f2 = (float)n7 + this.width + (float)n5;
        float f3 = (float)n4 + this.height + (float)n6;
        boolean bl = (float)n2 == f2;
        boolean bl2 = (float)n3 == f3;
        float f4 = (float)(-n7) - 0.49609375f;
        float f5 = (float)(-n4) - 0.49609375f;
        float f6 = this.width + (float)n5 + 0.49609375f;
        float f7 = this.height + (float)n6 + 0.49609375f;
        float f8 = (float)rectangle.x - 0.49609375f;
        float f9 = (float)rectangle.y - 0.49609375f;
        float f10 = (float)(rectangle.x + n2) + 0.49609375f;
        float f11 = (float)(rectangle.y + n3) + 0.49609375f;
        double d7 = d5;
        double d8 = d3;
        double d9 = d2;
        double d10 = d4;
        if (d5 + d3 > (double)this.width) {
            d6 = (double)this.width / (d5 + d3);
            d7 *= d6;
            d8 *= d6;
        }
        if (d2 + d4 > (double)this.height) {
            d6 = (double)this.height / (d2 + d4);
            d9 *= d6;
            d10 *= d6;
        }
        if (bl && bl2) {
            graphics.drawTexture(rTTexture, f4, f5, f6, f7, f8, f9, f10, f11);
        } else if (bl2) {
            float f12 = 0.49609375f + (float)(d7 + (double)n7);
            float f13 = 0.49609375f + (float)(d8 + (double)n5);
            float f14 = f4 + f12;
            float f15 = f6 - f13;
            float f16 = f8 + f12;
            float f17 = f10 - f13;
            graphics.drawTexture3SliceH(rTTexture, f4, f5, f6, f7, f8, f9, f10, f11, f14, f15, f16, f17);
        } else if (bl) {
            float f18 = 0.49609375f + (float)(d9 + (double)n4);
            float f19 = 0.49609375f + (float)(d10 + (double)n6);
            float f20 = f5 + f18;
            float f21 = f7 - f19;
            float f22 = f9 + f18;
            float f23 = f11 - f19;
            graphics.drawTexture3SliceV(rTTexture, f4, f5, f6, f7, f8, f9, f10, f11, f20, f21, f22, f23);
        } else {
            float f24 = 0.49609375f + (float)(d7 + (double)n7);
            float f25 = 0.49609375f + (float)(d9 + (double)n4);
            float f26 = 0.49609375f + (float)(d8 + (double)n5);
            float f27 = 0.49609375f + (float)(d10 + (double)n6);
            float f28 = f4 + f24;
            float f29 = f6 - f26;
            float f30 = f8 + f24;
            float f31 = f10 - f26;
            float f32 = f5 + f25;
            float f33 = f7 - f27;
            float f34 = f9 + f25;
            float f35 = f11 - f27;
            graphics.drawTexture9Slice(rTTexture, f4, f5, f6, f7, f8, f9, f10, f11, f28, f32, f29, f33, f30, f34, f31, f35);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("Cached region background image used");
        }
    }

    private void renderBackgroundRectanglesDirectly(Graphics graphics, float f2, float f3) {
        List<BackgroundFill> list = this.background.getFills();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            BackgroundFill backgroundFill = list.get(i2);
            Insets insets = backgroundFill.getInsets();
            float f4 = (float)insets.getTop();
            float f5 = (float)insets.getLeft();
            float f6 = (float)insets.getBottom();
            float f7 = (float)insets.getRight();
            float f8 = f2 - f5 - f7;
            float f9 = f3 - f4 - f6;
            if (!(f8 > 0.0f) || !(f9 > 0.0f)) continue;
            Paint paint = NGRegion.getPlatformPaint(backgroundFill.getFill());
            graphics.setPaint(paint);
            CornerRadii cornerRadii = this.getNormalizedFillRadii(i2);
            if (cornerRadii.isUniform() && (PlatformImpl.isCaspian() || PlatformUtil.isEmbedded() || PlatformUtil.isIOS() || !(cornerRadii.getTopLeftHorizontalRadius() > 0.0) || !(cornerRadii.getTopLeftHorizontalRadius() <= 4.0))) {
                float f10 = (float)cornerRadii.getTopLeftHorizontalRadius();
                float f11 = (float)cornerRadii.getTopLeftVerticalRadius();
                if (f10 == 0.0f && f11 == 0.0f) {
                    graphics.fillRect(f5, f4, f8, f9);
                    continue;
                }
                float f12 = f10 + f10;
                float f13 = f11 + f11;
                if (f12 > f8) {
                    f12 = f8;
                }
                if (f13 > f9) {
                    f13 = f9;
                }
                graphics.fillRoundRect(f5, f4, f8, f9, f12, f13);
                continue;
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter("NGRegion renderBackgrounds slow path");
                PulseLogger.addMessage("Slow background path for " + this.getName());
            }
            graphics.fill(this.createPath(f2, f3, f4, f5, f6, f7, cornerRadii));
        }
    }

    private void renderBorderRectangle(Graphics graphics) {
        float f2;
        float f3;
        float f4;
        Object object;
        Object object2;
        Object object3;
        Object object4;
        Object object5;
        int n2;
        List<BorderImage> list = this.border.getImages();
        List list2 = list.isEmpty() ? this.border.getStrokes() : Collections.emptyList();
        int n3 = list2.size();
        for (n2 = 0; n2 < n3; ++n2) {
            double d2;
            object5 = (BorderStroke)list2.get(n2);
            object4 = ((BorderStroke)object5).getWidths();
            CornerRadii cornerRadii = this.getNormalizedStrokeRadii(n2);
            Insets insets = ((BorderStroke)object5).getInsets();
            javafx.scene.paint.Paint paint = ((BorderStroke)object5).getTopStroke();
            object3 = ((BorderStroke)object5).getRightStroke();
            object2 = ((BorderStroke)object5).getBottomStroke();
            object = ((BorderStroke)object5).getLeftStroke();
            float f5 = (float)insets.getTop();
            float f6 = (float)insets.getRight();
            float f7 = (float)insets.getBottom();
            float f8 = (float)insets.getLeft();
            float f9 = (float)(((BorderWidths)object4).isTopAsPercentage() ? (double)this.height * ((BorderWidths)object4).getTop() : ((BorderWidths)object4).getTop());
            float f10 = (float)(((BorderWidths)object4).isRightAsPercentage() ? (double)this.width * ((BorderWidths)object4).getRight() : ((BorderWidths)object4).getRight());
            float f11 = (float)(((BorderWidths)object4).isBottomAsPercentage() ? (double)this.height * ((BorderWidths)object4).getBottom() : ((BorderWidths)object4).getBottom());
            float f12 = (float)(((BorderWidths)object4).isLeftAsPercentage() ? (double)this.width * ((BorderWidths)object4).getLeft() : ((BorderWidths)object4).getLeft());
            BorderStrokeStyle borderStrokeStyle = ((BorderStroke)object5).getTopStyle();
            BorderStrokeStyle borderStrokeStyle2 = ((BorderStroke)object5).getRightStyle();
            BorderStrokeStyle borderStrokeStyle3 = ((BorderStroke)object5).getBottomStyle();
            BorderStrokeStyle borderStrokeStyle4 = ((BorderStroke)object5).getLeftStyle();
            StrokeType strokeType = borderStrokeStyle.getType();
            StrokeType strokeType2 = borderStrokeStyle2.getType();
            StrokeType strokeType3 = borderStrokeStyle3.getType();
            StrokeType strokeType4 = borderStrokeStyle4.getType();
            float f13 = f5 + (strokeType == StrokeType.OUTSIDE ? -f9 / 2.0f : (strokeType == StrokeType.INSIDE ? f9 / 2.0f : 0.0f));
            float f14 = f8 + (strokeType4 == StrokeType.OUTSIDE ? -f12 / 2.0f : (strokeType4 == StrokeType.INSIDE ? f12 / 2.0f : 0.0f));
            float f15 = f7 + (strokeType3 == StrokeType.OUTSIDE ? -f11 / 2.0f : (strokeType3 == StrokeType.INSIDE ? f11 / 2.0f : 0.0f));
            float f16 = f6 + (strokeType2 == StrokeType.OUTSIDE ? -f10 / 2.0f : (strokeType2 == StrokeType.INSIDE ? f10 / 2.0f : 0.0f));
            f4 = (float)cornerRadii.getTopLeftHorizontalRadius();
            if (((BorderStroke)object5).isStrokeUniform()) {
                if (paint instanceof Color && ((Color)paint).getOpacity() == 0.0 || borderStrokeStyle == BorderStrokeStyle.NONE) continue;
                f3 = this.width - f14 - f16;
                f2 = this.height - f13 - f15;
                double d3 = 2.0 * cornerRadii.getTopLeftHorizontalRadius();
                double d4 = d3 * Math.PI;
                double d5 = d4 + 2.0 * ((double)f3 - d3) + 2.0 * ((double)f2 - d3);
                if (!(f3 >= 0.0f) || !(f2 >= 0.0f)) continue;
                this.setBorderStyle(graphics, (BorderStroke)object5, d5, true);
                if (cornerRadii.isUniform() && f4 == 0.0f) {
                    graphics.drawRect(f14, f13, f3, f2);
                    continue;
                }
                if (cornerRadii.isUniform()) {
                    float f17 = f4 + f4;
                    if (f17 > f3) {
                        f17 = f3;
                    }
                    if (f17 > f2) {
                        f17 = f2;
                    }
                    graphics.drawRoundRect(f14, f13, f3, f2, f17, f17);
                    continue;
                }
                graphics.draw(this.createPath(this.width, this.height, f13, f14, f15, f16, cornerRadii));
                continue;
            }
            if (cornerRadii.isUniform() && f4 == 0.0f) {
                if (!(paint instanceof Color && ((Color)paint).getOpacity() == 0.0 || borderStrokeStyle == BorderStrokeStyle.NONE)) {
                    graphics.setPaint(NGRegion.getPlatformPaint(paint));
                    if (BorderStrokeStyle.SOLID == borderStrokeStyle) {
                        graphics.fillRect(f8, f5, this.width - f8 - f6, f9);
                    } else {
                        graphics.setStroke(this.createStroke(borderStrokeStyle, f9, this.width, true));
                        graphics.drawLine(f14, f13, this.width - f16, f13);
                    }
                }
                if (!(object3 instanceof Color && ((Color)object3).getOpacity() == 0.0 || borderStrokeStyle2 == BorderStrokeStyle.NONE)) {
                    graphics.setPaint(NGRegion.getPlatformPaint((javafx.scene.paint.Paint)object3));
                    if (BorderStrokeStyle.SOLID == borderStrokeStyle2) {
                        graphics.fillRect(this.width - f6 - f10, f5, f10, this.height - f5 - f7);
                    } else {
                        graphics.setStroke(this.createStroke(borderStrokeStyle2, f10, this.height, true));
                        graphics.drawLine(this.width - f16, f13, this.width - f16, this.height - f15);
                    }
                }
                if (!(object2 instanceof Color && ((Color)object2).getOpacity() == 0.0 || borderStrokeStyle3 == BorderStrokeStyle.NONE)) {
                    graphics.setPaint(NGRegion.getPlatformPaint((javafx.scene.paint.Paint)object2));
                    if (BorderStrokeStyle.SOLID == borderStrokeStyle3) {
                        graphics.fillRect(f8, this.height - f7 - f11, this.width - f8 - f6, f11);
                    } else {
                        graphics.setStroke(this.createStroke(borderStrokeStyle3, f11, this.width, true));
                        graphics.drawLine(f14, this.height - f15, this.width - f16, this.height - f15);
                    }
                }
                if (object instanceof Color && ((Color)object).getOpacity() == 0.0 || borderStrokeStyle4 == BorderStrokeStyle.NONE) continue;
                graphics.setPaint(NGRegion.getPlatformPaint((javafx.scene.paint.Paint)object));
                if (BorderStrokeStyle.SOLID == borderStrokeStyle4) {
                    graphics.fillRect(f8, f5, f12, this.height - f5 - f7);
                    continue;
                }
                graphics.setStroke(this.createStroke(borderStrokeStyle4, f12, this.height, true));
                graphics.drawLine(f14, f13, f14, this.height - f15);
                continue;
            }
            Path2D[] arrpath2D = this.createPaths(f13, f14, f15, f16, cornerRadii);
            if (borderStrokeStyle != BorderStrokeStyle.NONE) {
                double d6 = cornerRadii.getTopLeftHorizontalRadius() + cornerRadii.getTopRightHorizontalRadius();
                d2 = (double)this.width + d6 * -0.21460183660255172;
                graphics.setStroke(this.createStroke(borderStrokeStyle, f9, d2, true));
                graphics.setPaint(NGRegion.getPlatformPaint(paint));
                graphics.draw(arrpath2D[0]);
            }
            if (borderStrokeStyle2 != BorderStrokeStyle.NONE) {
                double d7 = cornerRadii.getTopRightVerticalRadius() + cornerRadii.getBottomRightVerticalRadius();
                d2 = (double)this.height + d7 * -0.21460183660255172;
                graphics.setStroke(this.createStroke(borderStrokeStyle2, f10, d2, true));
                graphics.setPaint(NGRegion.getPlatformPaint((javafx.scene.paint.Paint)object3));
                graphics.draw(arrpath2D[1]);
            }
            if (borderStrokeStyle3 != BorderStrokeStyle.NONE) {
                double d8 = cornerRadii.getBottomLeftHorizontalRadius() + cornerRadii.getBottomRightHorizontalRadius();
                d2 = (double)this.width + d8 * -0.21460183660255172;
                graphics.setStroke(this.createStroke(borderStrokeStyle3, f11, d2, true));
                graphics.setPaint(NGRegion.getPlatformPaint((javafx.scene.paint.Paint)object2));
                graphics.draw(arrpath2D[2]);
            }
            if (borderStrokeStyle4 == BorderStrokeStyle.NONE) continue;
            double d9 = cornerRadii.getTopLeftVerticalRadius() + cornerRadii.getBottomLeftVerticalRadius();
            d2 = (double)this.height + d9 * -0.21460183660255172;
            graphics.setStroke(this.createStroke(borderStrokeStyle4, f12, d2, true));
            graphics.setPaint(NGRegion.getPlatformPaint((javafx.scene.paint.Paint)object));
            graphics.draw(arrpath2D[3]);
        }
        n3 = list.size();
        for (n2 = 0; n2 < n3; ++n2) {
            object5 = list.get(n2);
            object4 = (Image)((BorderImage)object5).getImage().impl_getPlatformImage();
            if (object4 == null) continue;
            int n4 = ((Image)object4).getWidth();
            int n5 = ((Image)object4).getHeight();
            float f18 = ((Image)object4).getPixelScale();
            object3 = ((BorderImage)object5).getWidths();
            object2 = ((BorderImage)object5).getInsets();
            object = ((BorderImage)object5).getSlices();
            int n6 = (int)Math.round(((Insets)object2).getTop());
            int n7 = (int)Math.round(((Insets)object2).getRight());
            int n8 = (int)Math.round(((Insets)object2).getBottom());
            int n9 = (int)Math.round(((Insets)object2).getLeft());
            int n10 = this.widthSize(((BorderWidths)object3).isTopAsPercentage(), ((BorderWidths)object3).getTop(), this.height);
            int n11 = this.widthSize(((BorderWidths)object3).isRightAsPercentage(), ((BorderWidths)object3).getRight(), this.width);
            int n12 = this.widthSize(((BorderWidths)object3).isBottomAsPercentage(), ((BorderWidths)object3).getBottom(), this.height);
            int n13 = this.widthSize(((BorderWidths)object3).isLeftAsPercentage(), ((BorderWidths)object3).getLeft(), this.width);
            int n14 = this.sliceSize(((BorderWidths)object).isTopAsPercentage(), ((BorderWidths)object).getTop(), n5, f18);
            int n15 = this.sliceSize(((BorderWidths)object).isRightAsPercentage(), ((BorderWidths)object).getRight(), n4, f18);
            int n16 = this.sliceSize(((BorderWidths)object).isBottomAsPercentage(), ((BorderWidths)object).getBottom(), n5, f18);
            int n17 = this.sliceSize(((BorderWidths)object).isLeftAsPercentage(), ((BorderWidths)object).getLeft(), n4, f18);
            if ((float)(n9 + n13 + n7 + n11) > this.width || (float)(n6 + n10 + n8 + n12) > this.height) continue;
            int n18 = n9 + n13;
            int n19 = n6 + n10;
            int n20 = Math.round(this.width) - n7 - n11 - n18;
            int n21 = Math.round(this.height) - n8 - n12 - n19;
            int n22 = n20 + n18;
            int n23 = n21 + n19;
            int n24 = n4 - n17 - n15;
            int n25 = n5 - n14 - n16;
            this.paintTiles(graphics, (Image)object4, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n9, (float)n6, (float)n13, (float)n10, 0, 0, n17, n14, 0.0f, 0.0f, (float)n13, (float)n10);
            f4 = ((BorderImage)object5).getRepeatX() == BorderRepeat.STRETCH ? (float)n20 : (float)(n14 > 0 ? n24 * n10 / n14 : 0);
            f3 = n10;
            this.paintTiles(graphics, (Image)object4, ((BorderImage)object5).getRepeatX(), BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n18, (float)n6, (float)n20, (float)n10, n17, 0, n24, n14, ((float)n20 - f4) / 2.0f, 0.0f, f4, f3);
            this.paintTiles(graphics, (Image)object4, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n22, (float)n6, (float)n11, (float)n10, n4 - n15, 0, n15, n14, 0.0f, 0.0f, (float)n11, (float)n10);
            f4 = n13;
            f3 = ((BorderImage)object5).getRepeatY() == BorderRepeat.STRETCH ? (float)n21 : (float)(n17 > 0 ? n13 * n25 / n17 : 0);
            this.paintTiles(graphics, (Image)object4, BorderRepeat.STRETCH, ((BorderImage)object5).getRepeatY(), Side.LEFT, Side.TOP, (float)n9, (float)n19, (float)n13, (float)n21, 0, n14, n17, n25, 0.0f, ((float)n21 - f3) / 2.0f, f4, f3);
            f4 = n11;
            f3 = ((BorderImage)object5).getRepeatY() == BorderRepeat.STRETCH ? (float)n21 : (float)(n15 > 0 ? n11 * n25 / n15 : 0);
            this.paintTiles(graphics, (Image)object4, BorderRepeat.STRETCH, ((BorderImage)object5).getRepeatY(), Side.LEFT, Side.TOP, (float)n22, (float)n19, (float)n11, (float)n21, n4 - n15, n14, n15, n25, 0.0f, ((float)n21 - f3) / 2.0f, f4, f3);
            this.paintTiles(graphics, (Image)object4, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n9, (float)n23, (float)n13, (float)n12, 0, n5 - n16, n17, n16, 0.0f, 0.0f, (float)n13, (float)n12);
            f4 = ((BorderImage)object5).getRepeatX() == BorderRepeat.STRETCH ? (float)n20 : (float)(n16 > 0 ? n24 * n12 / n16 : 0);
            f3 = n12;
            this.paintTiles(graphics, (Image)object4, ((BorderImage)object5).getRepeatX(), BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n18, (float)n23, (float)n20, (float)n12, n17, n5 - n16, n24, n16, ((float)n20 - f4) / 2.0f, 0.0f, f4, f3);
            this.paintTiles(graphics, (Image)object4, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n22, (float)n23, (float)n11, (float)n12, n4 - n15, n5 - n16, n15, n16, 0.0f, 0.0f, (float)n11, (float)n12);
            if (!((BorderImage)object5).isFilled()) continue;
            f2 = ((BorderImage)object5).getRepeatX() == BorderRepeat.STRETCH ? (float)n20 : (float)n24;
            float f19 = ((BorderImage)object5).getRepeatY() == BorderRepeat.STRETCH ? (float)n21 : (float)n25;
            this.paintTiles(graphics, (Image)object4, ((BorderImage)object5).getRepeatX(), ((BorderImage)object5).getRepeatY(), Side.LEFT, Side.TOP, (float)n18, (float)n19, (float)n20, (float)n21, n17, n14, n24, n25, 0.0f, 0.0f, f2, f19);
        }
    }

    private void updateBackgroundInsets() {
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        List<BackgroundFill> list = this.background.getFills();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            BackgroundFill backgroundFill = list.get(i2);
            Insets insets = backgroundFill.getInsets();
            CornerRadii cornerRadii = this.getNormalizedFillRadii(i2);
            f2 = (float)Math.max((double)f2, insets.getTop() + Math.max(cornerRadii.getTopLeftVerticalRadius(), cornerRadii.getTopRightVerticalRadius()));
            f3 = (float)Math.max((double)f3, insets.getRight() + Math.max(cornerRadii.getTopRightHorizontalRadius(), cornerRadii.getBottomRightHorizontalRadius()));
            f4 = (float)Math.max((double)f4, insets.getBottom() + Math.max(cornerRadii.getBottomRightVerticalRadius(), cornerRadii.getBottomLeftVerticalRadius()));
            f5 = (float)Math.max((double)f5, insets.getLeft() + Math.max(cornerRadii.getTopLeftHorizontalRadius(), cornerRadii.getBottomLeftHorizontalRadius()));
        }
        this.backgroundInsets = new Insets(this.roundUp(f2), this.roundUp(f3), this.roundUp(f4), this.roundUp(f5));
    }

    private int widthSize(boolean bl, double d2, float f2) {
        return (int)Math.round(bl ? d2 * (double)f2 : d2);
    }

    private int sliceSize(boolean bl, double d2, float f2, float f3) {
        if (bl) {
            d2 *= (double)f2;
        }
        if (d2 > (double)f2) {
            d2 = f2;
        }
        return (int)Math.round(d2 * (double)f3);
    }

    private int roundUp(double d2) {
        return d2 - (double)((int)d2) == 0.0 ? (int)d2 : (int)(d2 + 1.0);
    }

    private BasicStroke createStroke(BorderStrokeStyle borderStrokeStyle, double d2, double d3, boolean bl) {
        BasicStroke basicStroke;
        int n2;
        int n3 = borderStrokeStyle.getLineCap() == StrokeLineCap.BUTT ? 0 : (borderStrokeStyle.getLineCap() == StrokeLineCap.SQUARE ? 2 : 1);
        int n4 = borderStrokeStyle.getLineJoin() == StrokeLineJoin.BEVEL ? 2 : (borderStrokeStyle.getLineJoin() == StrokeLineJoin.MITER ? 0 : 1);
        if (bl) {
            n2 = 0;
        } else if (this.scaleShape) {
            n2 = 1;
        } else {
            switch (borderStrokeStyle.getType()) {
                case INSIDE: {
                    n2 = 1;
                    break;
                }
                case OUTSIDE: {
                    n2 = 2;
                    break;
                }
                default: {
                    n2 = 0;
                }
            }
        }
        if (borderStrokeStyle == BorderStrokeStyle.NONE) {
            throw new AssertionError((Object)"Should never have been asked to draw a border with NONE");
        }
        if (d2 <= 0.0) {
            basicStroke = new BasicStroke((float)d2, n3, n4, (float)borderStrokeStyle.getMiterLimit());
        } else if (borderStrokeStyle.getDashArray().size() > 0) {
            float f2;
            double[] arrd;
            List<Double> list = borderStrokeStyle.getDashArray();
            if (list == BorderStrokeStyle.DOTTED.getDashArray()) {
                if (d3 > 0.0) {
                    double d4 = d3 % (d2 * 2.0);
                    double d5 = d3 / (d2 * 2.0);
                    double d6 = d2 * 2.0 + d4 / d5;
                    arrd = new double[]{0.0, d6};
                    f2 = 0.0f;
                } else {
                    arrd = new double[]{0.0, d2 * 2.0};
                    f2 = 0.0f;
                }
            } else if (list == BorderStrokeStyle.DASHED.getDashArray()) {
                if (d3 > 0.0) {
                    double d7 = d2 * 2.0;
                    double d8 = d2 * 1.4;
                    double d9 = d7 + d8;
                    double d10 = d3 / d9;
                    double d11 = (int)d10;
                    if (d11 > 0.0) {
                        double d12 = d11 * d7;
                        d8 = (d3 - d12) / d11;
                    }
                    arrd = new double[]{d7, d8};
                    f2 = (float)(d7 * 0.6);
                } else {
                    arrd = new double[]{2.0 * d2, 1.4 * d2};
                    f2 = 0.0f;
                }
            } else {
                arrd = new double[list.size()];
                for (int i2 = 0; i2 < arrd.length; ++i2) {
                    arrd[i2] = list.get(i2);
                }
                f2 = (float)borderStrokeStyle.getDashOffset();
            }
            basicStroke = new BasicStroke(n2, (float)d2, n3, n4, (float)borderStrokeStyle.getMiterLimit(), arrd, f2);
        } else {
            basicStroke = new BasicStroke(n2, (float)d2, n3, n4, (float)borderStrokeStyle.getMiterLimit());
        }
        return basicStroke;
    }

    private void setBorderStyle(Graphics graphics, BorderStroke borderStroke, double d2, boolean bl) {
        BorderWidths borderWidths = borderStroke.getWidths();
        BorderStrokeStyle borderStrokeStyle = borderStroke.getTopStyle();
        double d3 = borderWidths.isTopAsPercentage() ? (double)this.height * borderWidths.getTop() : borderWidths.getTop();
        Paint paint = NGRegion.getPlatformPaint(borderStroke.getTopStroke());
        if (borderStrokeStyle == null) {
            borderStrokeStyle = borderStroke.getLeftStyle();
            d3 = borderWidths.isLeftAsPercentage() ? (double)this.width * borderWidths.getLeft() : borderWidths.getLeft();
            paint = NGRegion.getPlatformPaint(borderStroke.getLeftStroke());
            if (borderStrokeStyle == null) {
                borderStrokeStyle = borderStroke.getBottomStyle();
                d3 = borderWidths.isBottomAsPercentage() ? (double)this.height * borderWidths.getBottom() : borderWidths.getBottom();
                paint = NGRegion.getPlatformPaint(borderStroke.getBottomStroke());
                if (borderStrokeStyle == null) {
                    borderStrokeStyle = borderStroke.getRightStyle();
                    d3 = borderWidths.isRightAsPercentage() ? (double)this.width * borderWidths.getRight() : borderWidths.getRight();
                    paint = NGRegion.getPlatformPaint(borderStroke.getRightStroke());
                }
            }
        }
        if (borderStrokeStyle == null || borderStrokeStyle == BorderStrokeStyle.NONE) {
            return;
        }
        graphics.setStroke(this.createStroke(borderStrokeStyle, d3, d2, bl));
        graphics.setPaint(paint);
    }

    private void doCorner(Path2D path2D, CornerRadii cornerRadii, float f2, float f3, int n2, float f4, float f5, boolean bl) {
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        switch (n2 & 3) {
            case 0: {
                f11 = (float)cornerRadii.getTopLeftHorizontalRadius();
                f10 = (float)cornerRadii.getTopLeftVerticalRadius();
                f9 = 0.0f;
                f8 = f10;
                f7 = f11;
                f6 = 0.0f;
                break;
            }
            case 1: {
                f11 = (float)cornerRadii.getTopRightHorizontalRadius();
                f10 = (float)cornerRadii.getTopRightVerticalRadius();
                f9 = -f11;
                f8 = 0.0f;
                f7 = 0.0f;
                f6 = f10;
                break;
            }
            case 2: {
                f11 = (float)cornerRadii.getBottomRightHorizontalRadius();
                f10 = (float)cornerRadii.getBottomRightVerticalRadius();
                f9 = 0.0f;
                f8 = -f10;
                f7 = -f11;
                f6 = 0.0f;
                break;
            }
            case 3: {
                f11 = (float)cornerRadii.getBottomLeftHorizontalRadius();
                f10 = (float)cornerRadii.getBottomLeftVerticalRadius();
                f9 = f11;
                f8 = 0.0f;
                f7 = 0.0f;
                f6 = -f10;
                break;
            }
            default: {
                return;
            }
        }
        if (f11 > 0.0f && f10 > 0.0f) {
            path2D.appendOvalQuadrant(f2 + f9, f3 + f8, f2, f3, f2 + f7, f3 + f6, f4, f5, bl ? Path2D.CornerPrefix.MOVE_THEN_CORNER : Path2D.CornerPrefix.LINE_THEN_CORNER);
        } else if (bl) {
            path2D.moveTo(f2, f3);
        } else {
            path2D.lineTo(f2, f3);
        }
    }

    private Path2D createPath(float f2, float f3, float f4, float f5, float f6, float f7, CornerRadii cornerRadii) {
        float f8 = f2 - f7;
        float f9 = f3 - f6;
        Path2D path2D = new Path2D();
        this.doCorner(path2D, cornerRadii, f5, f4, 0, 0.0f, 1.0f, true);
        this.doCorner(path2D, cornerRadii, f8, f4, 1, 0.0f, 1.0f, false);
        this.doCorner(path2D, cornerRadii, f8, f9, 2, 0.0f, 1.0f, false);
        this.doCorner(path2D, cornerRadii, f5, f9, 3, 0.0f, 1.0f, false);
        path2D.closePath();
        return path2D;
    }

    private Path2D makeRoundedEdge(CornerRadii cornerRadii, float f2, float f3, float f4, float f5, int n2) {
        Path2D path2D = new Path2D();
        this.doCorner(path2D, cornerRadii, f2, f3, n2, 0.5f, 1.0f, true);
        this.doCorner(path2D, cornerRadii, f4, f5, n2 + 1, 0.0f, 0.5f, false);
        return path2D;
    }

    private Path2D[] createPaths(float f2, float f3, float f4, float f5, CornerRadii cornerRadii) {
        float f6 = this.width - f5;
        float f7 = this.height - f4;
        return new Path2D[]{this.makeRoundedEdge(cornerRadii, f3, f2, f6, f2, 0), this.makeRoundedEdge(cornerRadii, f6, f2, f6, f7, 1), this.makeRoundedEdge(cornerRadii, f6, f7, f3, f7, 2), this.makeRoundedEdge(cornerRadii, f3, f7, f3, f2, 3)};
    }

    private Shape resizeShape(float f2, float f3, float f4, float f5) {
        RectBounds rectBounds = this.shape.getBounds();
        if (this.scaleShape) {
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(f5, f2);
            float f6 = this.width - f5 - f3;
            float f7 = this.height - f2 - f4;
            SCRATCH_AFFINE.scale(f6 / rectBounds.getWidth(), f7 / rectBounds.getHeight());
            if (this.centerShape) {
                SCRATCH_AFFINE.translate(-rectBounds.getMinX(), -rectBounds.getMinY());
            }
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        if (this.centerShape) {
            float f8 = rectBounds.getWidth();
            float f9 = rectBounds.getHeight();
            float f10 = f8 - f5 - f3;
            float f11 = f9 - f2 - f4;
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(f5 + (this.width - f8) / 2.0f - rectBounds.getMinX(), f2 + (this.height - f9) / 2.0f - rectBounds.getMinY());
            if (f11 != f9 || f10 != f8) {
                SCRATCH_AFFINE.translate(rectBounds.getMinX(), rectBounds.getMinY());
                SCRATCH_AFFINE.scale(f10 / f8, f11 / f9);
                SCRATCH_AFFINE.translate(-rectBounds.getMinX(), -rectBounds.getMinY());
            }
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        if (f2 != 0.0f || f3 != 0.0f || f4 != 0.0f || f5 != 0.0f) {
            float f12 = rectBounds.getWidth() - f5 - f3;
            float f13 = rectBounds.getHeight() - f2 - f4;
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(f5, f2);
            SCRATCH_AFFINE.translate(rectBounds.getMinX(), rectBounds.getMinY());
            SCRATCH_AFFINE.scale(f12 / rectBounds.getWidth(), f13 / rectBounds.getHeight());
            SCRATCH_AFFINE.translate(-rectBounds.getMinX(), -rectBounds.getMinY());
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        return this.shape;
    }

    private void paintTiles(Graphics graphics, Image image, BorderRepeat borderRepeat, BorderRepeat borderRepeat2, Side side, Side side2, float f2, float f3, float f4, float f5, int n2, int n3, int n4, int n5, float f6, float f7, float f8, float f9) {
        BackgroundRepeat backgroundRepeat = null;
        BackgroundRepeat backgroundRepeat2 = null;
        switch (borderRepeat) {
            case REPEAT: {
                backgroundRepeat = BackgroundRepeat.REPEAT;
                break;
            }
            case STRETCH: {
                backgroundRepeat = BackgroundRepeat.NO_REPEAT;
                break;
            }
            case ROUND: {
                backgroundRepeat = BackgroundRepeat.ROUND;
                break;
            }
            case SPACE: {
                backgroundRepeat = BackgroundRepeat.SPACE;
            }
        }
        switch (borderRepeat2) {
            case REPEAT: {
                backgroundRepeat2 = BackgroundRepeat.REPEAT;
                break;
            }
            case STRETCH: {
                backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
                break;
            }
            case ROUND: {
                backgroundRepeat2 = BackgroundRepeat.ROUND;
                break;
            }
            case SPACE: {
                backgroundRepeat2 = BackgroundRepeat.SPACE;
            }
        }
        this.paintTiles(graphics, image, backgroundRepeat, backgroundRepeat2, side, side2, f2, f3, f4, f5, n2, n3, n4, n5, f6, f7, f8, f9);
    }

    private void paintTiles(Graphics graphics, Image image, BackgroundRepeat backgroundRepeat, BackgroundRepeat backgroundRepeat2, Side side, Side side2, float f2, float f3, float f4, float f5, int n2, int n3, int n4, int n5, float f6, float f7, float f8, float f9) {
        if (f4 <= 0.0f || f5 <= 0.0f || n4 <= 0 || n5 <= 0) {
            return;
        }
        assert (n2 >= 0 && n3 >= 0 && n4 > 0 && n5 > 0);
        if (f6 == 0.0f && f7 == 0.0f && backgroundRepeat == BackgroundRepeat.REPEAT && backgroundRepeat2 == BackgroundRepeat.REPEAT) {
            if (n2 != 0 || n3 != 0 || n4 != image.getWidth() || n5 != image.getHeight()) {
                image = image.createSubImage(n2, n3, n4, n5);
            }
            graphics.setPaint(new com.sun.prism.paint.ImagePattern(image, 0.0f, 0.0f, f8, f9, false, false));
            graphics.fillRect(f2, f3, f4, f5);
        } else {
            float f10;
            int n6;
            float f11;
            int n7;
            float f12;
            float f13;
            if (backgroundRepeat == BackgroundRepeat.SPACE && f4 < f8 * 2.0f) {
                backgroundRepeat = BackgroundRepeat.NO_REPEAT;
            }
            if (backgroundRepeat2 == BackgroundRepeat.SPACE && f5 < f9 * 2.0f) {
                backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
            }
            if (backgroundRepeat == BackgroundRepeat.REPEAT) {
                f13 = 0.0f;
                if (f6 != 0.0f) {
                    f12 = f6 % f8;
                    f13 = f6 = f12 == 0.0f ? 0.0f : (f6 < 0.0f ? f12 : f12 - f8);
                }
                n7 = (int)Math.max(1.0, Math.ceil((f4 - f13) / f8));
                f11 = side == Side.RIGHT ? -f8 : f8;
            } else if (backgroundRepeat == BackgroundRepeat.SPACE) {
                f6 = 0.0f;
                n7 = (int)(f4 / f8);
                f13 = f4 % f8;
                f11 = f8 + f13 / (float)(n7 - 1);
            } else if (backgroundRepeat == BackgroundRepeat.ROUND) {
                f6 = 0.0f;
                n7 = (int)(f4 / f8);
                f11 = f8 = f4 / (float)((int)(f4 / f8));
            } else {
                n7 = 1;
                float f14 = f11 = side == Side.RIGHT ? -f8 : f8;
            }
            if (backgroundRepeat2 == BackgroundRepeat.REPEAT) {
                f13 = 0.0f;
                if (f7 != 0.0f) {
                    f12 = f7 % f9;
                    f13 = f7 = f12 == 0.0f ? 0.0f : (f7 < 0.0f ? f12 : f12 - f9);
                }
                n6 = (int)Math.max(1.0, Math.ceil((f5 - f13) / f9));
                f10 = side2 == Side.BOTTOM ? -f9 : f9;
            } else if (backgroundRepeat2 == BackgroundRepeat.SPACE) {
                f7 = 0.0f;
                n6 = (int)(f5 / f9);
                f13 = f5 % f9;
                f10 = f9 + f13 / (float)(n6 - 1);
            } else if (backgroundRepeat2 == BackgroundRepeat.ROUND) {
                f7 = 0.0f;
                n6 = (int)(f5 / f9);
                f10 = f9 = f5 / (float)((int)(f5 / f9));
            } else {
                n6 = 1;
                f10 = side2 == Side.BOTTOM ? -f9 : f9;
            }
            Texture texture = graphics.getResourceFactory().getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
            int n8 = n2 + n4;
            int n9 = n3 + n5;
            float f15 = f2 + f4;
            float f16 = f3 + f5;
            float f17 = f3 + f7;
            for (int i2 = 0; i2 < n6; ++i2) {
                float f18 = f17 + f9;
                float f19 = f2 + f6;
                for (int i3 = 0; i3 < n7; ++i3) {
                    float f20;
                    float f21;
                    float f22 = f19 + f8;
                    boolean bl = false;
                    float f23 = f19 < f2 ? f2 : f19;
                    float f24 = f21 = f17 < f3 ? f3 : f17;
                    if (f23 > f15 || f21 > f16) {
                        bl = true;
                    }
                    float f25 = f22 > f15 ? f15 : f22;
                    float f26 = f20 = f18 > f16 ? f16 : f18;
                    if (f25 < f2 || f20 < f3) {
                        bl = true;
                    }
                    if (!bl) {
                        float f27 = f19 < f2 ? (float)n2 + (float)n4 * (-f6 / f8) : (float)n2;
                        float f28 = f17 < f3 ? (float)n3 + (float)n5 * (-f7 / f9) : (float)n3;
                        float f29 = f22 > f15 ? (float)n8 - (float)n4 * ((f22 - f15) / f8) : (float)n8;
                        float f30 = f18 > f16 ? (float)n9 - (float)n5 * ((f18 - f16) / f9) : (float)n9;
                        graphics.drawTexture(texture, f23, f21, f25, f20, f27, f28, f29, f30);
                    }
                    f19 += f11;
                }
                f17 += f10;
            }
            texture.unlock();
        }
    }

    final Border getBorder() {
        return this.border;
    }

    final Background getBackground() {
        return this.background;
    }

    final float getWidth() {
        return this.width;
    }

    final float getHeight() {
        return this.height;
    }
}

