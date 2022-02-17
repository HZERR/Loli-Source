/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.PathElementBuilder;
import javafx.util.Builder;

@Deprecated
public class LineToBuilder<B extends LineToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<LineTo> {
    private int __set;
    private double x;
    private double y;

    protected LineToBuilder() {
    }

    public static LineToBuilder<?> create() {
        return new LineToBuilder();
    }

    public void applyTo(LineTo lineTo) {
        super.applyTo(lineTo);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            lineTo.setX(this.x);
        }
        if ((n2 & 2) != 0) {
            lineTo.setY(this.y);
        }
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public LineTo build() {
        LineTo lineTo = new LineTo();
        this.applyTo(lineTo);
        return lineTo;
    }
}

