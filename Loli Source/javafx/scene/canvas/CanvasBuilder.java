/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.canvas;

import javafx.scene.NodeBuilder;
import javafx.scene.canvas.Canvas;
import javafx.util.Builder;

@Deprecated
public class CanvasBuilder<B extends CanvasBuilder<B>>
extends NodeBuilder<B>
implements Builder<Canvas> {
    private int __set;
    private double height;
    private double width;

    protected CanvasBuilder() {
    }

    public static CanvasBuilder<?> create() {
        return new CanvasBuilder();
    }

    public void applyTo(Canvas canvas) {
        super.applyTo(canvas);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            canvas.setHeight(this.height);
        }
        if ((n2 & 2) != 0) {
            canvas.setWidth(this.width);
        }
    }

    public B height(double d2) {
        this.height = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public Canvas build() {
        Canvas canvas = new Canvas();
        this.applyTo(canvas);
        return canvas;
    }
}

