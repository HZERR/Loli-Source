/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.util.Builder;

@Deprecated
public class ColorAdjustBuilder<B extends ColorAdjustBuilder<B>>
implements Builder<ColorAdjust> {
    private int __set;
    private double brightness;
    private double contrast;
    private double hue;
    private Effect input;
    private double saturation;

    protected ColorAdjustBuilder() {
    }

    public static ColorAdjustBuilder<?> create() {
        return new ColorAdjustBuilder();
    }

    public void applyTo(ColorAdjust colorAdjust) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            colorAdjust.setBrightness(this.brightness);
        }
        if ((n2 & 2) != 0) {
            colorAdjust.setContrast(this.contrast);
        }
        if ((n2 & 4) != 0) {
            colorAdjust.setHue(this.hue);
        }
        if ((n2 & 8) != 0) {
            colorAdjust.setInput(this.input);
        }
        if ((n2 & 0x10) != 0) {
            colorAdjust.setSaturation(this.saturation);
        }
    }

    public B brightness(double d2) {
        this.brightness = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B contrast(double d2) {
        this.contrast = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B hue(double d2) {
        this.hue = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 8;
        return (B)this;
    }

    public B saturation(double d2) {
        this.saturation = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public ColorAdjust build() {
        ColorAdjust colorAdjust = new ColorAdjust();
        this.applyTo(colorAdjust);
        return colorAdjust;
    }
}

