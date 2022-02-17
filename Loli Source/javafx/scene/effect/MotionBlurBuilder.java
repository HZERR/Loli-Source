/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.util.Builder;

@Deprecated
public class MotionBlurBuilder<B extends MotionBlurBuilder<B>>
implements Builder<MotionBlur> {
    private int __set;
    private double angle;
    private Effect input;
    private double radius;

    protected MotionBlurBuilder() {
    }

    public static MotionBlurBuilder<?> create() {
        return new MotionBlurBuilder();
    }

    public void applyTo(MotionBlur motionBlur) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            motionBlur.setAngle(this.angle);
        }
        if ((n2 & 2) != 0) {
            motionBlur.setInput(this.input);
        }
        if ((n2 & 4) != 0) {
            motionBlur.setRadius(this.radius);
        }
    }

    public B angle(double d2) {
        this.angle = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set |= 2;
        return (B)this;
    }

    public B radius(double d2) {
        this.radius = d2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public MotionBlur build() {
        MotionBlur motionBlur = new MotionBlur();
        this.applyTo(motionBlur);
        return motionBlur;
    }
}

