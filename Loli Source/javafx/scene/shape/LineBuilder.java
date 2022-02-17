/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.Line;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class LineBuilder<B extends LineBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Line> {
    private int __set;
    private double endX;
    private double endY;
    private double startX;
    private double startY;

    protected LineBuilder() {
    }

    public static LineBuilder<?> create() {
        return new LineBuilder();
    }

    public void applyTo(Line line) {
        super.applyTo(line);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            line.setEndX(this.endX);
        }
        if ((n2 & 2) != 0) {
            line.setEndY(this.endY);
        }
        if ((n2 & 4) != 0) {
            line.setStartX(this.startX);
        }
        if ((n2 & 8) != 0) {
            line.setStartY(this.startY);
        }
    }

    public B endX(double d2) {
        this.endX = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B endY(double d2) {
        this.endY = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B startX(double d2) {
        this.startX = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B startY(double d2) {
        this.startY = d2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public Line build() {
        Line line = new Line();
        this.applyTo(line);
        return line;
    }
}

