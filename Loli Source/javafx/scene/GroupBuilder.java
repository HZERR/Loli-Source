/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.ParentBuilder;
import javafx.util.Builder;

@Deprecated
public class GroupBuilder<B extends GroupBuilder<B>>
extends ParentBuilder<B>
implements Builder<Group> {
    private int __set;
    private boolean autoSizeChildren;
    private Collection<? extends Node> children;

    protected GroupBuilder() {
    }

    public static GroupBuilder<?> create() {
        return new GroupBuilder();
    }

    public void applyTo(Group group) {
        super.applyTo(group);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            group.setAutoSizeChildren(this.autoSizeChildren);
        }
        if ((n2 & 2) != 0) {
            group.getChildren().addAll(this.children);
        }
    }

    public B autoSizeChildren(boolean bl) {
        this.autoSizeChildren = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B children(Collection<? extends Node> collection) {
        this.children = collection;
        this.__set |= 2;
        return (B)this;
    }

    public B children(Node ... arrnode) {
        return this.children(Arrays.asList(arrnode));
    }

    @Override
    public Group build() {
        Group group = new Group();
        this.applyTo(group);
        return group;
    }
}

