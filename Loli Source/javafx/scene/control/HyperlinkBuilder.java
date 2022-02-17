/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.ButtonBaseBuilder;
import javafx.scene.control.Hyperlink;
import javafx.util.Builder;

@Deprecated
public class HyperlinkBuilder<B extends HyperlinkBuilder<B>>
extends ButtonBaseBuilder<B>
implements Builder<Hyperlink> {
    private boolean __set;
    private boolean visited;

    protected HyperlinkBuilder() {
    }

    public static HyperlinkBuilder<?> create() {
        return new HyperlinkBuilder();
    }

    public void applyTo(Hyperlink hyperlink) {
        super.applyTo(hyperlink);
        if (this.__set) {
            hyperlink.setVisited(this.visited);
        }
    }

    public B visited(boolean bl) {
        this.visited = bl;
        this.__set = true;
        return (B)this;
    }

    @Override
    public Hyperlink build() {
        Hyperlink hyperlink = new Hyperlink();
        this.applyTo(hyperlink);
        return hyperlink;
    }
}

