/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.property.DoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeCellSkin<T>
extends CellSkinBase<TreeCell<T>, TreeCellBehavior<T>> {
    private static final Map<TreeView<?>, Double> maxDisclosureWidthMap = new WeakHashMap();
    private DoubleProperty indent = null;
    private boolean disclosureNodeDirty = true;
    private TreeItem<?> treeItem;
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;
    private MultiplePropertyChangeListenerHandler treeItemListener = new MultiplePropertyChangeListenerHandler(string -> {
        if ("EXPANDED".equals(string)) {
            this.updateDisclosureNodeRotation(true);
        }
        return null;
    });

    public final void setIndent(double d2) {
        this.indentProperty().set(d2);
    }

    public final double getIndent() {
        return this.indent == null ? 10.0 : this.indent.get();
    }

    public final DoubleProperty indentProperty() {
        if (this.indent == null) {
            this.indent = new StyleableDoubleProperty(10.0){

                @Override
                public Object getBean() {
                    return TreeCellSkin.this;
                }

                @Override
                public String getName() {
                    return "indent";
                }

                @Override
                public CssMetaData<TreeCell<?>, Number> getCssMetaData() {
                    return StyleableProperties.INDENT;
                }
            };
        }
        return this.indent;
    }

    public TreeCellSkin(TreeCell<T> treeCell) {
        super(treeCell, new TreeCellBehavior<T>(treeCell));
        this.fixedCellSize = treeCell.getTreeView().getFixedCellSize();
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        this.updateTreeItem();
        this.updateDisclosureNodeRotation(false);
        this.registerChangeListener(treeCell.treeItemProperty(), "TREE_ITEM");
        this.registerChangeListener(treeCell.textProperty(), "TEXT");
        this.registerChangeListener(treeCell.getTreeView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("TREE_ITEM".equals(string)) {
            this.updateTreeItem();
            this.disclosureNodeDirty = true;
            ((TreeCell)this.getSkinnable()).requestLayout();
        } else if ("TEXT".equals(string)) {
            ((TreeCell)this.getSkinnable()).requestLayout();
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.fixedCellSize = ((TreeCell)this.getSkinnable()).getTreeView().getFixedCellSize();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    private void updateDisclosureNodeRotation(boolean bl) {
    }

    private void updateTreeItem() {
        if (this.treeItem != null) {
            this.treeItemListener.unregisterChangeListener(this.treeItem.expandedProperty());
        }
        this.treeItem = ((TreeCell)this.getSkinnable()).getTreeItem();
        if (this.treeItem != null) {
            this.treeItemListener.registerChangeListener(this.treeItem.expandedProperty(), "EXPANDED");
        }
        this.updateDisclosureNodeRotation(false);
    }

    private void updateDisclosureNode() {
        if (((TreeCell)this.getSkinnable()).isEmpty()) {
            return;
        }
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        if (node == null) {
            return;
        }
        boolean bl = this.treeItem != null && !this.treeItem.isLeaf();
        node.setVisible(bl);
        if (!bl) {
            this.getChildren().remove(node);
        } else if (node.getParent() == null) {
            this.getChildren().add(node);
            node.toFront();
        } else {
            node.toBack();
        }
        if (node.getScene() != null) {
            node.applyCss();
        }
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        this.updateDisclosureNode();
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6;
        TreeView treeView = ((TreeCell)this.getSkinnable()).getTreeView();
        if (treeView == null) {
            return;
        }
        if (this.disclosureNodeDirty) {
            this.updateDisclosureNode();
            this.disclosureNodeDirty = false;
        }
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        int n2 = treeView.getTreeItemLevel(this.treeItem);
        if (!treeView.isShowRoot()) {
            --n2;
        }
        double d7 = this.getIndent() * (double)n2;
        d2 += d7;
        boolean bl = node != null && this.treeItem != null && !this.treeItem.isLeaf();
        double d8 = d6 = maxDisclosureWidthMap.containsKey(treeView) ? maxDisclosureWidthMap.get(treeView) : 18.0;
        if (bl) {
            if (node == null || node.getScene() == null) {
                this.updateChildren();
            }
            if (node != null) {
                d8 = node.prefWidth(d5);
                if (d8 > d6) {
                    maxDisclosureWidthMap.put(treeView, d8);
                }
                double d9 = node.prefHeight(d8);
                node.resize(d8, d9);
                this.positionInArea(node, d2, d3, d8, d9, 0.0, HPos.CENTER, VPos.CENTER);
            }
        }
        int n3 = this.treeItem != null && this.treeItem.getGraphic() == null ? 0 : 3;
        d2 += d8 + (double)n3;
        d4 -= d7 + d8 + (double)n3;
        Node node2 = ((TreeCell)this.getSkinnable()).getGraphic();
        if (node2 != null && !this.getChildren().contains(node2)) {
            this.getChildren().add(node2);
        }
        this.layoutLabelInArea(d2, d3, d4, d5);
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        double d7 = super.computeMinHeight(d2, d3, d4, d5, d6);
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        return node == null ? d7 : Math.max(node.minHeight(-1.0), d7);
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        TreeCell treeCell = (TreeCell)this.getSkinnable();
        double d7 = super.computePrefHeight(d2, d3, d4, d5, d6);
        Node node = treeCell.getDisclosureNode();
        double d8 = node == null ? d7 : Math.max(node.prefHeight(-1.0), d7);
        return this.snapSize(Math.max(treeCell.getMinHeight(), d8));
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = super.computePrefWidth(d2, d3, d4, d5, d6);
        double d8 = this.snappedLeftInset() + this.snappedRightInset();
        TreeView treeView = ((TreeCell)this.getSkinnable()).getTreeView();
        if (treeView == null) {
            return d8;
        }
        if (this.treeItem == null) {
            return d8;
        }
        d8 = d7;
        int n2 = treeView.getTreeItemLevel(this.treeItem);
        if (!treeView.isShowRoot()) {
            --n2;
        }
        d8 += this.getIndent() * (double)n2;
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        double d9 = node == null ? 0.0 : node.prefWidth(-1.0);
        double d10 = maxDisclosureWidthMap.containsKey(treeView) ? maxDisclosureWidthMap.get(treeView) : 0.0;
        return d8 += Math.max(d10, d9);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TreeCellSkin.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<TreeCell<?>, Number> INDENT = new CssMetaData<TreeCell<?>, Number>("-fx-indent", SizeConverter.getInstance(), 10.0){

            @Override
            public boolean isSettable(TreeCell<?> treeCell) {
                DoubleProperty doubleProperty = ((TreeCellSkin)treeCell.getSkin()).indentProperty();
                return doubleProperty == null || !doubleProperty.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TreeCell<?> treeCell) {
                TreeCellSkin treeCellSkin = (TreeCellSkin)treeCell.getSkin();
                return (StyleableProperty)((Object)treeCellSkin.indentProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(CellSkinBase.getClassCssMetaData());
            arrayList.add(INDENT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

