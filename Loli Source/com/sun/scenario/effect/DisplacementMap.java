/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;

public class DisplacementMap
extends CoreEffect<RenderState> {
    private FloatMap mapData;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private boolean wrap;

    public DisplacementMap(FloatMap floatMap) {
        this(floatMap, DefaultInput);
    }

    public DisplacementMap(FloatMap floatMap, Effect effect) {
        super(effect);
        this.setMapData(floatMap);
        this.updatePeerKey("DisplacementMap");
    }

    public final FloatMap getMapData() {
        return this.mapData;
    }

    public void setMapData(FloatMap floatMap) {
        if (floatMap == null) {
            throw new IllegalArgumentException("Map data must be non-null");
        }
        FloatMap floatMap2 = this.mapData;
        this.mapData = floatMap;
    }

    public final Effect getContentInput() {
        return this.getInputs().get(0);
    }

    public void setContentInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float f2) {
        float f3 = this.scaleX;
        this.scaleX = f2;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float f2) {
        float f3 = this.scaleY;
        this.scaleY = f2;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float f2) {
        float f3 = this.offsetX;
        this.offsetX = f2;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float f2) {
        float f3 = this.offsetY;
        this.offsetY = f2;
    }

    public boolean getWrap() {
        return this.wrap;
    }

    public void setWrap(boolean bl) {
        boolean bl2 = this.wrap;
        this.wrap = bl;
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        BaseBounds baseBounds = this.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        float f2 = baseBounds.getWidth();
        float f3 = baseBounds.getHeight();
        float f4 = (point2D.x - baseBounds.getMinX()) / f2;
        float f5 = (point2D.y - baseBounds.getMinY()) / f3;
        if (f4 >= 0.0f && f5 >= 0.0f && f4 < 1.0f && f5 < 1.0f) {
            int n2 = (int)(f4 * (float)this.mapData.getWidth());
            int n3 = (int)(f5 * (float)this.mapData.getHeight());
            float f6 = this.mapData.getSample(n2, n3, 0);
            float f7 = this.mapData.getSample(n2, n3, 1);
            f4 += this.scaleX * (f6 + this.offsetX);
            f5 += this.scaleY * (f7 + this.offsetY);
            if (this.wrap) {
                f4 = (float)((double)f4 - Math.floor(f4));
                f5 = (float)((double)f5 - Math.floor(f5));
            }
            point2D = new Point2D(f4 * f2 + baseBounds.getMinX(), f5 * f3 + baseBounds.getMinY());
        }
        return this.getDefaultedInput(0, effect).untransform(point2D, effect);
    }

    @Override
    public ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData ... arrimageData) {
        return super.filterImageDatas(filterContext, baseTransform, null, renderState, arrimageData);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        DirtyRegionContainer dirtyRegionContainer = dirtyRegionPool.checkOut();
        dirtyRegionContainer.deriveWithNewRegion((RectBounds)this.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect));
        return dirtyRegionContainer;
    }
}

