/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.PaneBuilder;
import javafx.scene.layout.StackPane;

@Deprecated
public class StackPaneBuilder<B extends StackPaneBuilder<B>>
extends PaneBuilder<B> {
    private boolean __set;
    private Pos alignment;

    protected StackPaneBuilder() {
    }

    public static StackPaneBuilder<?> create() {
        return new StackPaneBuilder();
    }

    public void applyTo(StackPane stackPane) {
        super.applyTo(stackPane);
        if (this.__set) {
            stackPane.setAlignment(this.alignment);
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set = true;
        return (B)this;
    }

    @Override
    public StackPane build() {
        StackPane stackPane = new StackPane();
        this.applyTo(stackPane);
        return stackPane;
    }
}

