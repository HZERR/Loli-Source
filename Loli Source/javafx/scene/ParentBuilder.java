/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.NodeBuilder;
import javafx.scene.Parent;

@Deprecated
public abstract class ParentBuilder<B extends ParentBuilder<B>>
extends NodeBuilder<B> {
    private int __set;
    private Collection<? extends String> stylesheets;

    protected ParentBuilder() {
    }

    public void applyTo(Parent parent) {
        super.applyTo(parent);
        int n2 = this.__set;
        if ((n2 & 2) != 0) {
            parent.getStylesheets().addAll(this.stylesheets);
        }
    }

    public B stylesheets(Collection<? extends String> collection) {
        this.stylesheets = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B stylesheets(String ... arrstring) {
        return this.stylesheets(Arrays.asList(arrstring));
    }
}

