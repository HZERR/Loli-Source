/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.state.RenderState;

public class Blend
extends CoreEffect<RenderState> {
    private Mode mode;
    private float opacity;

    public Blend(Mode mode, Effect effect, Effect effect2) {
        super(effect, effect2);
        this.setMode(mode);
        this.setOpacity(1.0f);
    }

    public final Effect getBottomInput() {
        return this.getInputs().get(0);
    }

    public void setBottomInput(Effect effect) {
        this.setInput(0, effect);
    }

    public final Effect getTopInput() {
        return this.getInputs().get(1);
    }

    public void setTopInput(Effect effect) {
        this.setInput(1, effect);
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode must be non-null");
        }
        Mode mode2 = this.mode;
        this.mode = mode;
        this.updatePeerKey("Blend_" + mode.name());
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Opacity must be in the range [0,1]");
        }
        float f3 = this.opacity;
        this.opacity = f2;
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(1, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(1, effect).untransform(point2D, effect);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        Effect effect = this.getBottomInput();
        Effect effect2 = this.getTopInput();
        switch (this.getMode()) {
            case SRC_IN: 
            case SRC_OUT: {
                return true;
            }
            case SRC_ATOP: {
                return effect != null && effect.reducesOpaquePixels();
            }
            case SRC_OVER: 
            case ADD: 
            case MULTIPLY: 
            case SCREEN: 
            case OVERLAY: 
            case DARKEN: 
            case LIGHTEN: 
            case COLOR_DODGE: 
            case COLOR_BURN: 
            case HARD_LIGHT: 
            case SOFT_LIGHT: 
            case DIFFERENCE: 
            case EXCLUSION: 
            case RED: 
            case GREEN: 
            case BLUE: {
                return effect2 != null && effect2.reducesOpaquePixels() && effect != null && effect.reducesOpaquePixels();
            }
        }
        return true;
    }

    public static enum Mode {
        SRC_OVER,
        SRC_IN,
        SRC_OUT,
        SRC_ATOP,
        ADD,
        MULTIPLY,
        SCREEN,
        OVERLAY,
        DARKEN,
        LIGHTEN,
        COLOR_DODGE,
        COLOR_BURN,
        HARD_LIGHT,
        SOFT_LIGHT,
        DIFFERENCE,
        EXCLUSION,
        RED,
        GREEN,
        BLUE;

    }
}

