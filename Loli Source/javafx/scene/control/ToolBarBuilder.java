/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.ToolBar;
import javafx.util.Builder;

@Deprecated
public class ToolBarBuilder<B extends ToolBarBuilder<B>>
extends ControlBuilder<B>
implements Builder<ToolBar> {
    private int __set;
    private Collection<? extends Node> items;
    private Orientation orientation;

    protected ToolBarBuilder() {
    }

    public static ToolBarBuilder<?> create() {
        return new ToolBarBuilder();
    }

    public void applyTo(ToolBar toolBar) {
        super.applyTo(toolBar);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            toolBar.getItems().addAll(this.items);
        }
        if ((n2 & 2) != 0) {
            toolBar.setOrientation(this.orientation);
        }
    }

    public B items(Collection<? extends Node> collection) {
        this.items = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B items(Node ... arrnode) {
        return this.items(Arrays.asList(arrnode));
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public ToolBar build() {
        ToolBar toolBar = new ToolBar();
        this.applyTo(toolBar);
        return toolBar;
    }
}

