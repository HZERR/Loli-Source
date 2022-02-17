/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.PathElementBuilder;
import javafx.scene.shape.VLineTo;
import javafx.util.Builder;

@Deprecated
public class VLineToBuilder<B extends VLineToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<VLineTo> {
    private boolean __set;
    private double y;

    protected VLineToBuilder() {
    }

    public static VLineToBuilder<?> create() {
        return new VLineToBuilder();
    }

    public void applyTo(VLineTo vLineTo) {
        super.applyTo(vLineTo);
        if (this.__set) {
            vLineTo.setY(this.y);
        }
    }

    public B y(double d2) {
        this.y = d2;
        this.__set = true;
        return (B)this;
    }

    @Override
    public VLineTo build() {
        VLineTo vLineTo = new VLineTo();
        this.applyTo(vLineTo);
        return vLineTo;
    }
}

