/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.state.RenderState;

public class ColorAdjust
extends CoreEffect<RenderState> {
    private float hue = 0.0f;
    private float saturation = 0.0f;
    private float brightness = 0.0f;
    private float contrast = 0.0f;

    public ColorAdjust() {
        this(DefaultInput);
    }

    public ColorAdjust(Effect effect) {
        super(effect);
        this.updatePeerKey("ColorAdjust");
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getHue() {
        return this.hue;
    }

    public void setHue(float f2) {
        if (f2 < -1.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Hue must be in the range [-1, 1]");
        }
        float f3 = this.hue;
        this.hue = f2;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setSaturation(float f2) {
        if (f2 < -1.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Saturation must be in the range [-1, 1]");
        }
        float f3 = this.saturation;
        this.saturation = f2;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setBrightness(float f2) {
        if (f2 < -1.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Brightness must be in the range [-1, 1]");
        }
        float f3 = this.brightness;
        this.brightness = f2;
    }

    public float getContrast() {
        return this.contrast;
    }

    public void setContrast(float f2) {
        if (f2 < -1.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Contrast must be in the range [-1, 1]");
        }
        float f3 = this.contrast;
        this.contrast = f2;
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        Effect effect = this.getInput();
        return effect != null && effect.reducesOpaquePixels();
    }
}

