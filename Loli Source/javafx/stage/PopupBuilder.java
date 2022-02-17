/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.stage.Popup;
import javafx.stage.PopupWindowBuilder;
import javafx.util.Builder;

@Deprecated
public class PopupBuilder<B extends PopupBuilder<B>>
extends PopupWindowBuilder<B>
implements Builder<Popup> {
    private boolean __set;
    private Collection<? extends Node> content;

    protected PopupBuilder() {
    }

    public static PopupBuilder<?> create() {
        return new PopupBuilder();
    }

    public void applyTo(Popup popup) {
        super.applyTo(popup);
        if (this.__set) {
            popup.getContent().addAll(this.content);
        }
    }

    public B content(Collection<? extends Node> collection) {
        this.content = collection;
        this.__set = true;
        return (B)this;
    }

    public B content(Node ... arrnode) {
        return this.content(Arrays.asList(arrnode));
    }

    @Override
    public Popup build() {
        Popup popup = new Popup();
        this.applyTo(popup);
        return popup;
    }
}

