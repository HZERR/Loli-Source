/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.geom.Vec2d;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class BorderPane
extends Pane {
    private static final String MARGIN = "borderpane-margin";
    private static final String ALIGNMENT = "borderpane-alignment";
    private ObjectProperty<Node> center;
    private ObjectProperty<Node> top;
    private ObjectProperty<Node> bottom;
    private ObjectProperty<Node> left;
    private ObjectProperty<Node> right;

    public static void setAlignment(Node node, Pos pos) {
        BorderPane.setConstraint(node, ALIGNMENT, (Object)pos);
    }

    public static Pos getAlignment(Node node) {
        return (Pos)((Object)BorderPane.getConstraint(node, ALIGNMENT));
    }

    public static void setMargin(Node node, Insets insets) {
        BorderPane.setConstraint(node, MARGIN, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)BorderPane.getConstraint(node, MARGIN);
    }

    private static Insets getNodeMargin(Node node) {
        Insets insets = BorderPane.getMargin(node);
        return insets != null ? insets : Insets.EMPTY;
    }

    public static void clearConstraints(Node node) {
        BorderPane.setAlignment(node, null);
        BorderPane.setMargin(node, null);
    }

    public BorderPane() {
    }

    public BorderPane(Node node) {
        this.setCenter(node);
    }

    public BorderPane(Node node, Node node2, Node node3, Node node4, Node node5) {
        this.setCenter(node);
        this.setTop(node2);
        this.setRight(node3);
        this.setBottom(node4);
        this.setLeft(node5);
    }

    public final ObjectProperty<Node> centerProperty() {
        if (this.center == null) {
            this.center = new BorderPositionProperty("center");
        }
        return this.center;
    }

    public final void setCenter(Node node) {
        this.centerProperty().set(node);
    }

    public final Node getCenter() {
        return this.center == null ? null : (Node)this.center.get();
    }

    public final ObjectProperty<Node> topProperty() {
        if (this.top == null) {
            this.top = new BorderPositionProperty("top");
        }
        return this.top;
    }

    public final void setTop(Node node) {
        this.topProperty().set(node);
    }

    public final Node getTop() {
        return this.top == null ? null : (Node)this.top.get();
    }

    public final ObjectProperty<Node> bottomProperty() {
        if (this.bottom == null) {
            this.bottom = new BorderPositionProperty("bottom");
        }
        return this.bottom;
    }

    public final void setBottom(Node node) {
        this.bottomProperty().set(node);
    }

    public final Node getBottom() {
        return this.bottom == null ? null : (Node)this.bottom.get();
    }

    public final ObjectProperty<Node> leftProperty() {
        if (this.left == null) {
            this.left = new BorderPositionProperty("left");
        }
        return this.left;
    }

    public final void setLeft(Node node) {
        this.leftProperty().set(node);
    }

    public final Node getLeft() {
        return this.left == null ? null : (Node)this.left.get();
    }

    public final ObjectProperty<Node> rightProperty() {
        if (this.right == null) {
            this.right = new BorderPositionProperty("right");
        }
        return this.right;
    }

    public final void setRight(Node node) {
        this.rightProperty().set(node);
    }

    public final Node getRight() {
        return this.right == null ? null : (Node)this.right.get();
    }

    @Override
    public Orientation getContentBias() {
        Node node = this.getCenter();
        if (node != null && node.isManaged() && node.getContentBias() != null) {
            return node.getContentBias();
        }
        Node node2 = this.getRight();
        if (node2 != null && node2.isManaged() && node2.getContentBias() == Orientation.VERTICAL) {
            return node2.getContentBias();
        }
        Node node3 = this.getLeft();
        if (node3 != null && node3.isManaged() && node3.getContentBias() == Orientation.VERTICAL) {
            return node3.getContentBias();
        }
        Node node4 = this.getBottom();
        if (node4 != null && node4.isManaged() && node4.getContentBias() == Orientation.HORIZONTAL) {
            return node4.getContentBias();
        }
        Node node5 = this.getTop();
        if (node5 != null && node5.isManaged() && node5.getContentBias() == Orientation.HORIZONTAL) {
            return node5.getContentBias();
        }
        return null;
    }

    @Override
    protected double computeMinWidth(double d2) {
        double d3;
        double d4;
        double d5;
        double d6 = this.getAreaWidth(this.getTop(), -1.0, true);
        double d7 = this.getAreaWidth(this.getBottom(), -1.0, true);
        if (d2 != -1.0 && (this.childHasContentBias(this.getLeft(), Orientation.VERTICAL) || this.childHasContentBias(this.getRight(), Orientation.VERTICAL) || this.childHasContentBias(this.getCenter(), Orientation.VERTICAL))) {
            double d8 = this.getAreaHeight(this.getTop(), -1.0, false);
            double d9 = this.getAreaHeight(this.getBottom(), -1.0, false);
            double d10 = Math.max(0.0, d2 - d8 - d9);
            d5 = this.getAreaWidth(this.getLeft(), d10, false);
            d4 = this.getAreaWidth(this.getRight(), d10, false);
            d3 = this.getAreaWidth(this.getCenter(), d10, true);
        } else {
            d5 = this.getAreaWidth(this.getLeft(), -1.0, false);
            d4 = this.getAreaWidth(this.getRight(), -1.0, false);
            d3 = this.getAreaWidth(this.getCenter(), -1.0, true);
        }
        Insets insets = this.getInsets();
        return insets.getLeft() + Math.max(d5 + d3 + d4, Math.max(d6, d7)) + insets.getRight();
    }

    @Override
    protected double computeMinHeight(double d2) {
        double d3;
        double d4;
        Insets insets = this.getInsets();
        double d5 = this.getAreaHeight(this.getTop(), d2, false);
        double d6 = this.getAreaHeight(this.getBottom(), d2, false);
        double d7 = this.getAreaHeight(this.getLeft(), -1.0, true);
        double d8 = this.getAreaHeight(this.getRight(), -1.0, true);
        if (d2 != -1.0 && this.childHasContentBias(this.getCenter(), Orientation.HORIZONTAL)) {
            d4 = this.getAreaWidth(this.getLeft(), -1.0, false);
            double d9 = this.getAreaWidth(this.getRight(), -1.0, false);
            d3 = this.getAreaHeight(this.getCenter(), Math.max(0.0, d2 - d4 - d9), true);
        } else {
            d3 = this.getAreaHeight(this.getCenter(), -1.0, true);
        }
        d4 = Math.max(d3, Math.max(d8, d7));
        return insets.getTop() + d5 + d4 + d6 + insets.getBottom();
    }

    @Override
    protected double computePrefWidth(double d2) {
        double d3;
        double d4;
        double d5;
        double d6 = this.getAreaWidth(this.getTop(), -1.0, false);
        double d7 = this.getAreaWidth(this.getBottom(), -1.0, false);
        if (d2 != -1.0 && (this.childHasContentBias(this.getLeft(), Orientation.VERTICAL) || this.childHasContentBias(this.getRight(), Orientation.VERTICAL) || this.childHasContentBias(this.getCenter(), Orientation.VERTICAL))) {
            double d8 = this.getAreaHeight(this.getTop(), -1.0, false);
            double d9 = this.getAreaHeight(this.getBottom(), -1.0, false);
            double d10 = Math.max(0.0, d2 - d8 - d9);
            d5 = this.getAreaWidth(this.getLeft(), d10, false);
            d4 = this.getAreaWidth(this.getRight(), d10, false);
            d3 = this.getAreaWidth(this.getCenter(), d10, false);
        } else {
            d5 = this.getAreaWidth(this.getLeft(), -1.0, false);
            d4 = this.getAreaWidth(this.getRight(), -1.0, false);
            d3 = this.getAreaWidth(this.getCenter(), -1.0, false);
        }
        Insets insets = this.getInsets();
        return insets.getLeft() + Math.max(d5 + d3 + d4, Math.max(d6, d7)) + insets.getRight();
    }

    @Override
    protected double computePrefHeight(double d2) {
        double d3;
        double d4;
        Insets insets = this.getInsets();
        double d5 = this.getAreaHeight(this.getTop(), d2, false);
        double d6 = this.getAreaHeight(this.getBottom(), d2, false);
        double d7 = this.getAreaHeight(this.getLeft(), -1.0, false);
        double d8 = this.getAreaHeight(this.getRight(), -1.0, false);
        if (d2 != -1.0 && this.childHasContentBias(this.getCenter(), Orientation.HORIZONTAL)) {
            d4 = this.getAreaWidth(this.getLeft(), -1.0, false);
            double d9 = this.getAreaWidth(this.getRight(), -1.0, false);
            d3 = this.getAreaHeight(this.getCenter(), Math.max(0.0, d2 - d4 - d9), false);
        } else {
            d3 = this.getAreaHeight(this.getCenter(), -1.0, false);
        }
        d4 = Math.max(d3, Math.max(d8, d7));
        return insets.getTop() + d5 + d4 + d6 + insets.getBottom();
    }

    @Override
    protected void layoutChildren() {
        Object object;
        double d2;
        double d3;
        double d4;
        Insets insets = this.getInsets();
        double d5 = this.getWidth();
        double d6 = this.getHeight();
        Orientation orientation = this.getContentBias();
        if (orientation == null) {
            d4 = this.minWidth(-1.0);
            d3 = this.minHeight(-1.0);
            d5 = d5 < d4 ? d4 : d5;
            d6 = d6 < d3 ? d3 : d6;
        } else if (orientation == Orientation.HORIZONTAL) {
            d4 = this.minWidth(-1.0);
            d5 = d5 < d4 ? d4 : d5;
            d3 = this.minHeight(d5);
            d6 = d6 < d3 ? d3 : d6;
        } else {
            d4 = this.minHeight(-1.0);
            d6 = d6 < d4 ? d4 : d6;
            d3 = this.minWidth(d6);
            d5 = d5 < d3 ? d3 : d5;
        }
        d4 = insets.getLeft();
        d3 = insets.getTop();
        double d7 = d5 - d4 - insets.getRight();
        double d8 = d6 - d3 - insets.getBottom();
        Node node = this.getCenter();
        Node node2 = this.getRight();
        Node node3 = this.getBottom();
        Node node4 = this.getLeft();
        Node node5 = this.getTop();
        double d9 = 0.0;
        if (node5 != null && node5.isManaged()) {
            Insets insets2 = BorderPane.getNodeMargin(node5);
            double d10 = this.adjustWidthByMargin(d7, insets2);
            d2 = this.adjustHeightByMargin(d8, insets2);
            d9 = this.snapSize(node5.prefHeight(d10));
            d9 = Math.min(d9, d2);
            Vec2d vec2d = BorderPane.boundedNodeSizeWithBias(node5, d10, d9, true, true, TEMP_VEC2D);
            d9 = this.snapSize(vec2d.y);
            node5.resize(this.snapSize(vec2d.x), d9);
            d9 = this.snapSpace(insets2.getBottom()) + d9 + this.snapSpace(insets2.getTop());
            object = BorderPane.getAlignment(node5);
            BorderPane.positionInArea(node5, d4, d3, d7, d9, 0.0, insets2, object != null ? ((Pos)((Object)object)).getHpos() : HPos.LEFT, object != null ? ((Pos)((Object)object)).getVpos() : VPos.TOP, this.isSnapToPixel());
        }
        double d11 = 0.0;
        if (node3 != null && node3.isManaged()) {
            Insets insets3 = BorderPane.getNodeMargin(node3);
            d2 = this.adjustWidthByMargin(d7, insets3);
            double d12 = this.adjustHeightByMargin(d8 - d9, insets3);
            d11 = this.snapSize(node3.prefHeight(d2));
            d11 = Math.min(d11, d12);
            Vec2d vec2d = BorderPane.boundedNodeSizeWithBias(node3, d2, d11, true, true, TEMP_VEC2D);
            d11 = this.snapSize(vec2d.y);
            node3.resize(this.snapSize(vec2d.x), d11);
            d11 = this.snapSpace(insets3.getBottom()) + d11 + this.snapSpace(insets3.getTop());
            Pos pos = BorderPane.getAlignment(node3);
            BorderPane.positionInArea(node3, d4, d3 + d8 - d11, d7, d11, 0.0, insets3, pos != null ? pos.getHpos() : HPos.LEFT, pos != null ? pos.getVpos() : VPos.BOTTOM, this.isSnapToPixel());
        }
        double d13 = 0.0;
        if (node4 != null && node4.isManaged()) {
            Insets insets4 = BorderPane.getNodeMargin(node4);
            double d14 = this.adjustWidthByMargin(d7, insets4);
            double d15 = this.adjustHeightByMargin(d8 - d9 - d11, insets4);
            d13 = this.snapSize(node4.prefWidth(d15));
            d13 = Math.min(d13, d14);
            Vec2d vec2d = BorderPane.boundedNodeSizeWithBias(node4, d13, d15, true, true, TEMP_VEC2D);
            d13 = this.snapSize(vec2d.x);
            node4.resize(d13, this.snapSize(vec2d.y));
            d13 = this.snapSpace(insets4.getLeft()) + d13 + this.snapSpace(insets4.getRight());
            Pos pos = BorderPane.getAlignment(node4);
            BorderPane.positionInArea(node4, d4, d3 + d9, d13, d8 - d9 - d11, 0.0, insets4, pos != null ? pos.getHpos() : HPos.LEFT, pos != null ? pos.getVpos() : VPos.TOP, this.isSnapToPixel());
        }
        double d16 = 0.0;
        if (node2 != null && node2.isManaged()) {
            object = BorderPane.getNodeMargin(node2);
            double d17 = this.adjustWidthByMargin(d7 - d13, (Insets)object);
            double d18 = this.adjustHeightByMargin(d8 - d9 - d11, (Insets)object);
            d16 = this.snapSize(node2.prefWidth(d18));
            d16 = Math.min(d16, d17);
            Vec2d vec2d = BorderPane.boundedNodeSizeWithBias(node2, d16, d18, true, true, TEMP_VEC2D);
            d16 = this.snapSize(vec2d.x);
            node2.resize(d16, this.snapSize(vec2d.y));
            d16 = this.snapSpace(((Insets)object).getLeft()) + d16 + this.snapSpace(((Insets)object).getRight());
            Pos pos = BorderPane.getAlignment(node2);
            BorderPane.positionInArea(node2, d4 + d7 - d16, d3 + d9, d16, d8 - d9 - d11, 0.0, (Insets)object, pos != null ? pos.getHpos() : HPos.RIGHT, pos != null ? pos.getVpos() : VPos.TOP, this.isSnapToPixel());
        }
        if (node != null && node.isManaged()) {
            object = BorderPane.getAlignment(node);
            this.layoutInArea(node, d4 + d13, d3 + d9, d7 - d13 - d16, d8 - d9 - d11, 0.0, BorderPane.getNodeMargin(node), object != null ? ((Pos)((Object)object)).getHpos() : HPos.CENTER, object != null ? ((Pos)((Object)object)).getVpos() : VPos.CENTER);
        }
    }

    private double getAreaWidth(Node node, double d2, boolean bl) {
        if (node != null && node.isManaged()) {
            Insets insets = BorderPane.getNodeMargin(node);
            return bl ? this.computeChildMinAreaWidth(node, -1.0, insets, d2, false) : this.computeChildPrefAreaWidth(node, -1.0, insets, d2, false);
        }
        return 0.0;
    }

    private double getAreaHeight(Node node, double d2, boolean bl) {
        if (node != null && node.isManaged()) {
            Insets insets = BorderPane.getNodeMargin(node);
            return bl ? this.computeChildMinAreaHeight(node, -1.0, insets, d2) : this.computeChildPrefAreaHeight(node, -1.0, insets, d2);
        }
        return 0.0;
    }

    private boolean childHasContentBias(Node node, Orientation orientation) {
        if (node != null && node.isManaged()) {
            return node.getContentBias() == orientation;
        }
        return false;
    }

    private final class BorderPositionProperty
    extends ObjectPropertyBase<Node> {
        private Node oldValue = null;
        private final String propertyName;
        private boolean isBeingInvalidated;

        BorderPositionProperty(String string) {
            this.propertyName = string;
            BorderPane.this.getChildren().addListener(new ListChangeListener<Node>(){

                @Override
                public void onChanged(ListChangeListener.Change<? extends Node> change) {
                    if (BorderPositionProperty.this.oldValue == null || BorderPositionProperty.this.isBeingInvalidated) {
                        return;
                    }
                    while (change.next()) {
                        if (!change.wasRemoved()) continue;
                        List<? extends Node> list = change.getRemoved();
                        int n2 = list.size();
                        for (int i2 = 0; i2 < n2; ++i2) {
                            if (list.get(i2) != BorderPositionProperty.this.oldValue) continue;
                            BorderPositionProperty.this.oldValue = null;
                            BorderPositionProperty.this.set(null);
                        }
                    }
                }
            });
        }

        @Override
        protected void invalidated() {
            ObservableList<Node> observableList = BorderPane.this.getChildren();
            this.isBeingInvalidated = true;
            try {
                Node node;
                if (this.oldValue != null) {
                    observableList.remove(this.oldValue);
                }
                this.oldValue = node = (Node)this.get();
                if (node != null) {
                    observableList.add(node);
                }
            }
            finally {
                this.isBeingInvalidated = false;
            }
        }

        @Override
        public Object getBean() {
            return BorderPane.this;
        }

        @Override
        public String getName() {
            return this.propertyName;
        }
    }
}

