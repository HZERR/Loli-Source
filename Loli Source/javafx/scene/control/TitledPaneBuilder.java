/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.LabeledBuilder;
import javafx.scene.control.TitledPane;
import javafx.util.Builder;

@Deprecated
public class TitledPaneBuilder<B extends TitledPaneBuilder<B>>
extends LabeledBuilder<B>
implements Builder<TitledPane> {
    private int __set;
    private boolean animated;
    private boolean collapsible;
    private Node content;
    private boolean expanded;

    protected TitledPaneBuilder() {
    }

    public static TitledPaneBuilder<?> create() {
        return new TitledPaneBuilder();
    }

    public void applyTo(TitledPane titledPane) {
        super.applyTo(titledPane);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            titledPane.setAnimated(this.animated);
        }
        if ((n2 & 2) != 0) {
            titledPane.setCollapsible(this.collapsible);
        }
        if ((n2 & 4) != 0) {
            titledPane.setContent(this.content);
        }
        if ((n2 & 8) != 0) {
            titledPane.setExpanded(this.expanded);
        }
    }

    public B animated(boolean bl) {
        this.animated = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B collapsible(boolean bl) {
        this.collapsible = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B content(Node node) {
        this.content = node;
        this.__set |= 4;
        return (B)this;
    }

    public B expanded(boolean bl) {
        this.expanded = bl;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public TitledPane build() {
        TitledPane titledPane = new TitledPane();
        this.applyTo(titledPane);
        return titledPane;
    }
}

