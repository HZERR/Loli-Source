/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElementBuilder;
import javafx.util.Builder;

@Deprecated
public class MoveToBuilder<B extends MoveToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<MoveTo> {
    private int __set;
    private double x;
    private double y;

    protected MoveToBuilder() {
    }

    public static MoveToBuilder<?> create() {
        return new MoveToBuilder();
    }

    public void applyTo(MoveTo moveTo) {
        super.applyTo(moveTo);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            moveTo.setX(this.x);
        }
        if ((n2 & 2) != 0) {
            moveTo.setY(this.y);
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
    public MoveTo build() {
        MoveTo moveTo = new MoveTo();
        this.applyTo(moveTo);
        return moveTo;
    }
}

