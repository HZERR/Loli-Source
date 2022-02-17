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
import com.sun.scenario.effect.impl.state.RenderState;

public class Reflection
extends CoreEffect<RenderState> {
    private float topOffset = 0.0f;
    private float topOpacity = 0.5f;
    private float bottomOpacity = 0.0f;
    private float fraction = 0.75f;

    public Reflection() {
        this(DefaultInput);
    }

    public Reflection(Effect effect) {
        super(effect);
        this.updatePeerKey("Reflection");
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getTopOffset() {
        return this.topOffset;
    }

    public void setTopOffset(float f2) {
        float f3 = this.topOffset;
        this.topOffset = f2;
    }

    public float getTopOpacity() {
        return this.topOpacity;
    }

    public void setTopOpacity(float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Top opacity must be in the range [0,1]");
        }
        float f3 = this.topOpacity;
        this.topOpacity = f2;
    }

    public float getBottomOpacity() {
        return this.bottomOpacity;
    }

    public void setBottomOpacity(float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Bottom opacity must be in the range [0,1]");
        }
        float f3 = this.bottomOpacity;
        this.bottomOpacity = f2;
    }

    public float getFraction() {
        return this.fraction;
    }

    public void setFraction(float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Fraction must be in the range [0,1]");
        }
        float f3 = this.fraction;
        this.fraction = f2;
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        BaseBounds baseBounds = effect2.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        baseBounds.roundOut();
        float f2 = baseBounds.getMinX();
        float f3 = baseBounds.getMaxY() + this.topOffset;
        float f4 = baseBounds.getMaxX();
        float f5 = f3 + this.fraction * baseBounds.getHeight();
        BaseBounds baseBounds2 = new RectBounds(f2, f3, f4, f5);
        baseBounds2 = ((BaseBounds)baseBounds2).deriveWithUnion(baseBounds);
        return Reflection.transformBounds(baseTransform, baseBounds2);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).untransform(point2D, effect);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        Effect effect = this.getInput();
        return effect != null && effect.reducesOpaquePixels();
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        BaseBounds baseBounds = effect2.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        float f2 = baseBounds.getMaxY();
        float f3 = 2.0f * f2 + this.getTopOffset();
        float f4 = f2 + this.getTopOffset() + this.fraction * baseBounds.getHeight();
        DirtyRegionContainer dirtyRegionContainer2 = dirtyRegionPool.checkOut();
        for (int i2 = 0; i2 < dirtyRegionContainer.size(); ++i2) {
            RectBounds rectBounds = dirtyRegionContainer.getDirtyRegion(i2);
            float f5 = f3 - ((BaseBounds)rectBounds).getMaxY();
            float f6 = Math.min(f4, f5 + ((BaseBounds)rectBounds).getHeight());
            dirtyRegionContainer2.addDirtyRegion(new RectBounds(((BaseBounds)rectBounds).getMinX(), f5, ((BaseBounds)rectBounds).getMaxX(), f6));
        }
        dirtyRegionContainer.merge(dirtyRegionContainer2);
        dirtyRegionPool.checkIn(dirtyRegionContainer2);
        return dirtyRegionContainer;
    }
}

