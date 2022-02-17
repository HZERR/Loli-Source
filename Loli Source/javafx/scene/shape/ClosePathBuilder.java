/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.ClosePath;
import javafx.scene.shape.PathElementBuilder;
import javafx.util.Builder;

@Deprecated
public class ClosePathBuilder<B extends ClosePathBuilder<B>>
extends PathElementBuilder<B>
implements Builder<ClosePath> {
    protected ClosePathBuilder() {
    }

    public static ClosePathBuilder<?> create() {
        return new ClosePathBuilder();
    }

    @Override
    public ClosePath build() {
        ClosePath closePath = new ClosePath();
        this.applyTo(closePath);
        return closePath;
    }
}

