/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import java.util.Collection;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

@DefaultProperty(value="children")
public class Group
extends Parent {
    private BooleanProperty autoSizeChildren;

    public Group() {
    }

    public Group(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    public Group(Collection<Node> collection) {
        this.getChildren().addAll(collection);
    }

    public final void setAutoSizeChildren(boolean bl) {
        this.autoSizeChildrenProperty().set(bl);
    }

    public final boolean isAutoSizeChildren() {
        return this.autoSizeChildren == null ? true : this.autoSizeChildren.get();
    }

    public final BooleanProperty autoSizeChildrenProperty() {
        if (this.autoSizeChildren == null) {
            this.autoSizeChildren = new BooleanPropertyBase(true){

                @Override
                protected void invalidated() {
                    Group.this.requestLayout();
                }

                @Override
                public Object getBean() {
                    return Group.this;
                }

                @Override
                public String getName() {
                    return "autoSizeChildren";
                }
            };
        }
        return this.autoSizeChildren;
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    @Deprecated
    protected Bounds impl_computeLayoutBounds() {
        this.layout();
        return super.impl_computeLayoutBounds();
    }

    @Override
    public double prefWidth(double d2) {
        double d3;
        if (this.isAutoSizeChildren()) {
            this.layout();
        }
        return Double.isNaN(d3 = this.getLayoutBounds().getWidth()) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public double prefHeight(double d2) {
        double d3;
        if (this.isAutoSizeChildren()) {
            this.layout();
        }
        return Double.isNaN(d3 = this.getLayoutBounds().getHeight()) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public double minHeight(double d2) {
        return this.prefHeight(d2);
    }

    @Override
    public double minWidth(double d2) {
        return this.prefWidth(d2);
    }

    @Override
    protected void layoutChildren() {
        if (this.isAutoSizeChildren()) {
            super.layoutChildren();
        }
    }
}

