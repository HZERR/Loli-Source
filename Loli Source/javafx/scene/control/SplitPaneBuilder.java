/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.SplitPane;
import javafx.util.Builder;

@Deprecated
public class SplitPaneBuilder<B extends SplitPaneBuilder<B>>
extends ControlBuilder<B>
implements Builder<SplitPane> {
    private int __set;
    private double[] dividerPositions;
    private Collection<? extends Node> items;
    private Orientation orientation;

    protected SplitPaneBuilder() {
    }

    public static SplitPaneBuilder<?> create() {
        return new SplitPaneBuilder();
    }

    public void applyTo(SplitPane splitPane) {
        super.applyTo(splitPane);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            splitPane.setDividerPositions(this.dividerPositions);
        }
        if ((n2 & 2) != 0) {
            splitPane.getItems().addAll(this.items);
        }
        if ((n2 & 4) != 0) {
            splitPane.setOrientation(this.orientation);
        }
    }

    public B dividerPositions(double[] arrd) {
        this.dividerPositions = arrd;
        this.__set |= 1;
        return (B)this;
    }

    public B items(Collection<? extends Node> collection) {
        this.items = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B items(Node ... arrnode) {
        return this.items(Arrays.asList(arrnode));
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public SplitPane build() {
        SplitPane splitPane = new SplitPane();
        this.applyTo(splitPane);
        return splitPane;
    }
}

