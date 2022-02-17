/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AnchorPane
extends Pane {
    private static final String TOP_ANCHOR = "pane-top-anchor";
    private static final String LEFT_ANCHOR = "pane-left-anchor";
    private static final String BOTTOM_ANCHOR = "pane-bottom-anchor";
    private static final String RIGHT_ANCHOR = "pane-right-anchor";

    public static void setTopAnchor(Node node, Double d2) {
        AnchorPane.setConstraint(node, TOP_ANCHOR, d2);
    }

    public static Double getTopAnchor(Node node) {
        return (Double)AnchorPane.getConstraint(node, TOP_ANCHOR);
    }

    public static void setLeftAnchor(Node node, Double d2) {
        AnchorPane.setConstraint(node, LEFT_ANCHOR, d2);
    }

    public static Double getLeftAnchor(Node node) {
        return (Double)AnchorPane.getConstraint(node, LEFT_ANCHOR);
    }

    public static void setBottomAnchor(Node node, Double d2) {
        AnchorPane.setConstraint(node, BOTTOM_ANCHOR, d2);
    }

    public static Double getBottomAnchor(Node node) {
        return (Double)AnchorPane.getConstraint(node, BOTTOM_ANCHOR);
    }

    public static void setRightAnchor(Node node, Double d2) {
        AnchorPane.setConstraint(node, RIGHT_ANCHOR, d2);
    }

    public static Double getRightAnchor(Node node) {
        return (Double)AnchorPane.getConstraint(node, RIGHT_ANCHOR);
    }

    public static void clearConstraints(Node node) {
        AnchorPane.setTopAnchor(node, null);
        AnchorPane.setRightAnchor(node, null);
        AnchorPane.setBottomAnchor(node, null);
        AnchorPane.setLeftAnchor(node, null);
    }

    public AnchorPane() {
    }

    public AnchorPane(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    @Override
    protected double computeMinWidth(double d2) {
        return this.computeWidth(true, d2);
    }

    @Override
    protected double computeMinHeight(double d2) {
        return this.computeHeight(true, d2);
    }

    @Override
    protected double computePrefWidth(double d2) {
        return this.computeWidth(false, d2);
    }

    @Override
    protected double computePrefHeight(double d2) {
        return this.computeHeight(false, d2);
    }

    private double computeWidth(boolean bl, double d2) {
        double d3 = 0.0;
        double d4 = d2 != -1.0 ? d2 - this.getInsets().getTop() - this.getInsets().getBottom() : -1.0;
        List list = this.getManagedChildren();
        for (Node node : list) {
            Double d5 = AnchorPane.getLeftAnchor(node);
            Double d6 = AnchorPane.getRightAnchor(node);
            double d7 = d5 != null ? d5 : (d6 != null ? 0.0 : node.getLayoutBounds().getMinX() + node.getLayoutX());
            double d8 = d6 != null ? d6 : 0.0;
            double d9 = -1.0;
            if (node.getContentBias() == Orientation.VERTICAL && d4 != -1.0) {
                d9 = this.computeChildHeight(node, AnchorPane.getTopAnchor(node), AnchorPane.getBottomAnchor(node), d4, -1.0);
            }
            d3 = Math.max(d3, d7 + (bl && d5 != null && d6 != null ? node.minWidth(d9) : this.computeChildPrefAreaWidth(node, -1.0, null, d9, false)) + d8);
        }
        Insets insets = this.getInsets();
        return insets.getLeft() + d3 + insets.getRight();
    }

    private double computeHeight(boolean bl, double d2) {
        double d3 = 0.0;
        double d4 = d2 != -1.0 ? d2 - this.getInsets().getLeft() - this.getInsets().getRight() : -1.0;
        List list = this.getManagedChildren();
        for (Node node : list) {
            Double d5 = AnchorPane.getTopAnchor(node);
            Double d6 = AnchorPane.getBottomAnchor(node);
            double d7 = d5 != null ? d5 : (d6 != null ? 0.0 : node.getLayoutBounds().getMinY() + node.getLayoutY());
            double d8 = d6 != null ? d6 : 0.0;
            double d9 = -1.0;
            if (node.getContentBias() == Orientation.HORIZONTAL && d4 != -1.0) {
                d9 = this.computeChildWidth(node, AnchorPane.getLeftAnchor(node), AnchorPane.getRightAnchor(node), d4, -1.0);
            }
            d3 = Math.max(d3, d7 + (bl && d5 != null && d6 != null ? node.minHeight(d9) : this.computeChildPrefAreaHeight(node, -1.0, null, d9)) + d8);
        }
        Insets insets = this.getInsets();
        return insets.getTop() + d3 + insets.getBottom();
    }

    private double computeChildWidth(Node node, Double d2, Double d3, double d4, double d5) {
        if (d2 != null && d3 != null && node.isResizable()) {
            Insets insets = this.getInsets();
            return d4 - insets.getLeft() - insets.getRight() - d2 - d3;
        }
        return this.computeChildPrefAreaWidth(node, -1.0, Insets.EMPTY, d5, true);
    }

    private double computeChildHeight(Node node, Double d2, Double d3, double d4, double d5) {
        if (d2 != null && d3 != null && node.isResizable()) {
            Insets insets = this.getInsets();
            return d4 - insets.getTop() - insets.getBottom() - d2 - d3;
        }
        return this.computeChildPrefAreaHeight(node, -1.0, Insets.EMPTY, d5);
    }

    @Override
    protected void layoutChildren() {
        Insets insets = this.getInsets();
        List list = this.getManagedChildren();
        for (Node node : list) {
            double d2;
            double d3;
            Double d4 = AnchorPane.getTopAnchor(node);
            Double d5 = AnchorPane.getBottomAnchor(node);
            Double d6 = AnchorPane.getLeftAnchor(node);
            Double d7 = AnchorPane.getRightAnchor(node);
            Bounds bounds = node.getLayoutBounds();
            Orientation orientation = node.getContentBias();
            double d8 = node.getLayoutX() + bounds.getMinX();
            double d9 = node.getLayoutY() + bounds.getMinY();
            if (orientation == Orientation.VERTICAL) {
                d3 = this.computeChildHeight(node, d4, d5, this.getHeight(), -1.0);
                d2 = this.computeChildWidth(node, d6, d7, this.getWidth(), d3);
            } else if (orientation == Orientation.HORIZONTAL) {
                d2 = this.computeChildWidth(node, d6, d7, this.getWidth(), -1.0);
                d3 = this.computeChildHeight(node, d4, d5, this.getHeight(), d2);
            } else {
                d2 = this.computeChildWidth(node, d6, d7, this.getWidth(), -1.0);
                d3 = this.computeChildHeight(node, d4, d5, this.getHeight(), -1.0);
            }
            if (d6 != null) {
                d8 = insets.getLeft() + d6;
            } else if (d7 != null) {
                d8 = this.getWidth() - insets.getRight() - d7 - d2;
            }
            if (d4 != null) {
                d9 = insets.getTop() + d4;
            } else if (d5 != null) {
                d9 = this.getHeight() - insets.getBottom() - d5 - d3;
            }
            node.resizeRelocate(d8, d9, d2, d3);
        }
    }
}

