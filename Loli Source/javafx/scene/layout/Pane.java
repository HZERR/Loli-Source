/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;

@DefaultProperty(value="children")
public class Pane
extends Region {
    static void setConstraint(Node node, Object object, Object object2) {
        if (object2 == null) {
            node.getProperties().remove(object);
        } else {
            node.getProperties().put(object, object2);
        }
        if (node.getParent() != null) {
            node.getParent().requestLayout();
        }
    }

    static Object getConstraint(Node node, Object object) {
        Object v2;
        if (node.hasProperties() && (v2 = node.getProperties().get(object)) != null) {
            return v2;
        }
        return null;
    }

    public Pane() {
    }

    public Pane(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }
}

