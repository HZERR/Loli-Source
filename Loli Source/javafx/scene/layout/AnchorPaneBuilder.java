/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.PaneBuilder;

@Deprecated
public class AnchorPaneBuilder<B extends AnchorPaneBuilder<B>>
extends PaneBuilder<B> {
    protected AnchorPaneBuilder() {
    }

    public static AnchorPaneBuilder<?> create() {
        return new AnchorPaneBuilder();
    }

    @Override
    public AnchorPane build() {
        AnchorPane anchorPane = new AnchorPane();
        this.applyTo(anchorPane);
        return anchorPane;
    }
}

