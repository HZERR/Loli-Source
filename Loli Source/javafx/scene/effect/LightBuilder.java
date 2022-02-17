/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Light;
import javafx.scene.paint.Color;

@Deprecated
public abstract class LightBuilder<B extends LightBuilder<B>> {
    private boolean __set;
    private Color color;

    protected LightBuilder() {
    }

    public void applyTo(Light light) {
        if (this.__set) {
            light.setColor(this.color);
        }
    }

    public B color(Color color) {
        this.color = color;
        this.__set = true;
        return (B)this;
    }
}

