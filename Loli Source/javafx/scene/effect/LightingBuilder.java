/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.util.Builder;

@Deprecated
public class LightingBuilder<B extends LightingBuilder<B>>
implements Builder<Lighting> {
    private int __set;
    private Effect bumpInput;
    private Effect contentInput;
    private double diffuseConstant;
    private Light light;
    private double specularConstant;
    private double specularExponent;
    private double surfaceScale;

    protected LightingBuilder() {
    }

    public static LightingBuilder<?> create() {
        return new LightingBuilder();
    }

    public void applyTo(Lighting lighting) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            lighting.setBumpInput(this.bumpInput);
        }
        if ((n2 & 2) != 0) {
            lighting.setContentInput(this.contentInput);
        }
        if ((n2 & 4) != 0) {
            lighting.setDiffuseConstant(this.diffuseConstant);
        }
        if ((n2 & 8) != 0) {
            lighting.setLight(this.light);
        }
        if ((n2 & 0x10) != 0) {
            lighting.setSpecularConstant(this.specularConstant);
        }
        if ((n2 & 0x20) != 0) {
            lighting.setSpecularExponent(this.specularExponent);
        }
        if ((n2 & 0x40) != 0) {
            lighting.setSurfaceScale(this.surfaceScale);
        }
    }

    public B bumpInput(Effect effect) {
        this.bumpInput = effect;
        this.__set |= 1;
        return (B)this;
    }

    public B contentInput(Effect effect) {
        this.contentInput = effect;
        this.__set |= 2;
        return (B)this;
    }

    public B diffuseConstant(double d2) {
        this.diffuseConstant = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B light(Light light) {
        this.light = light;
        this.__set |= 8;
        return (B)this;
    }

    public B specularConstant(double d2) {
        this.specularConstant = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B specularExponent(double d2) {
        this.specularExponent = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B surfaceScale(double d2) {
        this.surfaceScale = d2;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public Lighting build() {
        Lighting lighting = new Lighting();
        this.applyTo(lighting);
        return lighting;
    }
}

