/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.PaneBuilder;

@Deprecated
public class BorderPaneBuilder<B extends BorderPaneBuilder<B>>
extends PaneBuilder<B> {
    private int __set;
    private Node bottom;
    private Node center;
    private Node left;
    private Node right;
    private Node top;

    protected BorderPaneBuilder() {
    }

    public static BorderPaneBuilder<?> create() {
        return new BorderPaneBuilder();
    }

    public void applyTo(BorderPane borderPane) {
        super.applyTo(borderPane);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            borderPane.setBottom(this.bottom);
        }
        if ((n2 & 2) != 0) {
            borderPane.setCenter(this.center);
        }
        if ((n2 & 4) != 0) {
            borderPane.setLeft(this.left);
        }
        if ((n2 & 8) != 0) {
            borderPane.setRight(this.right);
        }
        if ((n2 & 0x10) != 0) {
            borderPane.setTop(this.top);
        }
    }

    public B bottom(Node node) {
        this.bottom = node;
        this.__set |= 1;
        return (B)this;
    }

    public B center(Node node) {
        this.center = node;
        this.__set |= 2;
        return (B)this;
    }

    public B left(Node node) {
        this.left = node;
        this.__set |= 4;
        return (B)this;
    }

    public B right(Node node) {
        this.right = node;
        this.__set |= 8;
        return (B)this;
    }

    public B top(Node node) {
        this.top = node;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public BorderPane build() {
        BorderPane borderPane = new BorderPane();
        this.applyTo(borderPane);
        return borderPane;
    }
}

