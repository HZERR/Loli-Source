/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DelegateEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.GaussianShadow;

public class GeneralShadow
extends DelegateEffect {
    private AbstractShadow shadow;

    public GeneralShadow() {
        this(DefaultInput);
    }

    public GeneralShadow(Effect effect) {
        super(effect);
        this.shadow = new GaussianShadow(10.0f, Color4f.BLACK, effect);
    }

    public AbstractShadow.ShadowMode getShadowMode() {
        return this.shadow.getMode();
    }

    public void setShadowMode(AbstractShadow.ShadowMode shadowMode) {
        AbstractShadow.ShadowMode shadowMode2 = this.shadow.getMode();
        this.shadow = this.shadow.implFor(shadowMode);
    }

    @Override
    protected Effect getDelegate() {
        return this.shadow;
    }

    public final Effect getInput() {
        return this.shadow.getInput();
    }

    public void setInput(Effect effect) {
        this.shadow.setInput(effect);
    }

    public float getRadius() {
        return this.shadow.getGaussianRadius();
    }

    public void setRadius(float f2) {
        float f3 = this.shadow.getGaussianRadius();
        this.shadow.setGaussianRadius(f2);
    }

    public float getGaussianRadius() {
        return this.shadow.getGaussianRadius();
    }

    public float getGaussianWidth() {
        return this.shadow.getGaussianWidth();
    }

    public float getGaussianHeight() {
        return this.shadow.getGaussianHeight();
    }

    public void setGaussianRadius(float f2) {
        this.setRadius(f2);
    }

    public void setGaussianWidth(float f2) {
        float f3 = this.shadow.getGaussianWidth();
        this.shadow.setGaussianWidth(f2);
    }

    public void setGaussianHeight(float f2) {
        float f3 = this.shadow.getGaussianHeight();
        this.shadow.setGaussianHeight(f2);
    }

    public float getSpread() {
        return this.shadow.getSpread();
    }

    public void setSpread(float f2) {
        float f3 = this.shadow.getSpread();
        this.shadow.setSpread(f2);
    }

    public Color4f getColor() {
        return this.shadow.getColor();
    }

    public void setColor(Color4f color4f) {
        Color4f color4f2 = this.shadow.getColor();
        this.shadow.setColor(color4f);
    }
}

