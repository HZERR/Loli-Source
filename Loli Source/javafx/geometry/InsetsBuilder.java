/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.geometry.Insets;
import javafx.util.Builder;

@Deprecated
public class InsetsBuilder<B extends InsetsBuilder<B>>
implements Builder<Insets> {
    private double bottom;
    private double left;
    private double right;
    private double top;

    protected InsetsBuilder() {
    }

    public static InsetsBuilder<?> create() {
        return new InsetsBuilder();
    }

    public B bottom(double d2) {
        this.bottom = d2;
        return (B)this;
    }

    public B left(double d2) {
        this.left = d2;
        return (B)this;
    }

    public B right(double d2) {
        this.right = d2;
        return (B)this;
    }

    public B top(double d2) {
        this.top = d2;
        return (B)this;
    }

    @Override
    public Insets build() {
        Insets insets = new Insets(this.top, this.right, this.bottom, this.left);
        return insets;
    }
}

