/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.HLineTo;
import javafx.scene.shape.PathElementBuilder;
import javafx.util.Builder;

@Deprecated
public class HLineToBuilder<B extends HLineToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<HLineTo> {
    private boolean __set;
    private double x;

    protected HLineToBuilder() {
    }

    public static HLineToBuilder<?> create() {
        return new HLineToBuilder();
    }

    public void applyTo(HLineTo hLineTo) {
        super.applyTo(hLineTo);
        if (this.__set) {
            hLineTo.setX(this.x);
        }
    }

    public B x(double d2) {
        this.x = d2;
        this.__set = true;
        return (B)this;
    }

    @Override
    public HLineTo build() {
        HLineTo hLineTo = new HLineTo();
        this.applyTo(hLineTo);
        return hLineTo;
    }
}

