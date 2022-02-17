/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RegionBuilder;

@Deprecated
public class PaneBuilder<B extends PaneBuilder<B>>
extends RegionBuilder<B> {
    private boolean __set;
    private Collection<? extends Node> children;

    protected PaneBuilder() {
    }

    public static PaneBuilder<?> create() {
        return new PaneBuilder();
    }

    public void applyTo(Pane pane) {
        super.applyTo(pane);
        if (this.__set) {
            pane.getChildren().addAll(this.children);
        }
    }

    public B children(Collection<? extends Node> collection) {
        this.children = collection;
        this.__set = true;
        return (B)this;
    }

    public B children(Node ... arrnode) {
        return this.children(Arrays.asList(arrnode));
    }

    @Override
    public Pane build() {
        Pane pane = new Pane();
        this.applyTo(pane);
        return pane;
    }
}

