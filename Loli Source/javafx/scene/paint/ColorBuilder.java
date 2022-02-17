/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
public class ColorBuilder<B extends ColorBuilder<B>>
implements Builder<Color> {
    private double blue;
    private double green;
    private double opacity = 1.0;
    private double red;

    protected ColorBuilder() {
    }

    public static ColorBuilder<?> create() {
        return new ColorBuilder();
    }

    public B blue(double d2) {
        this.blue = d2;
        return (B)this;
    }

    public B green(double d2) {
        this.green = d2;
        return (B)this;
    }

    public B opacity(double d2) {
        this.opacity = d2;
        return (B)this;
    }

    public B red(double d2) {
        this.red = d2;
        return (B)this;
    }

    @Override
    public Color build() {
        Color color = new Color(this.red, this.green, this.blue, this.opacity);
        return color;
    }
}

