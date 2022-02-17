/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public abstract class SkinBase<C extends Control>
implements Skin<C> {
    private C control;
    private ObservableList<Node> children;
    private static final EventHandler<MouseEvent> mouseEventConsumer = mouseEvent -> mouseEvent.consume();

    protected SkinBase(C c2) {
        if (c2 == null) {
            throw new IllegalArgumentException("Cannot pass null for control");
        }
        this.control = c2;
        this.children = ((Control)c2).getControlChildren();
        this.consumeMouseEvents(true);
    }

    @Override
    public final C getSkinnable() {
        return this.control;
    }

    @Override
    public final Node getNode() {
        return this.control;
    }

    @Override
    public void dispose() {
        this.control = null;
    }

    public final ObservableList<Node> getChildren() {
        return this.children;
    }

    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            this.layoutInArea(node, d2, d3, d4, d5, -1.0, HPos.CENTER, VPos.CENTER);
        }
    }

    protected final void consumeMouseEvents(boolean bl) {
        if (bl) {
            ((Node)this.control).addEventHandler(MouseEvent.ANY, mouseEventConsumer);
        } else {
            ((Node)this.control).removeEventHandler(MouseEvent.ANY, mouseEventConsumer);
        }
    }

    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        boolean bl = true;
        for (int i2 = 0; i2 < this.children.size(); ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            double d9 = node.getLayoutBounds().getMinX() + node.getLayoutX();
            if (!bl) {
                d7 = Math.min(d7, d9);
                d8 = Math.max(d8, d9 + node.minWidth(-1.0));
                continue;
            }
            d7 = d9;
            d8 = d9 + node.minWidth(-1.0);
            bl = false;
        }
        double d10 = d8 - d7;
        return d6 + d10 + d4;
    }

    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        boolean bl = true;
        for (int i2 = 0; i2 < this.children.size(); ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            double d9 = node.getLayoutBounds().getMinY() + node.getLayoutY();
            if (!bl) {
                d7 = Math.min(d7, d9);
                d8 = Math.max(d8, d9 + node.minHeight(-1.0));
                continue;
            }
            d7 = d9;
            d8 = d9 + node.minHeight(-1.0);
            bl = false;
        }
        double d10 = d8 - d7;
        return d3 + d10 + d5;
    }

    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return Double.MAX_VALUE;
    }

    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        boolean bl = true;
        for (int i2 = 0; i2 < this.children.size(); ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            double d9 = node.getLayoutBounds().getMinX() + node.getLayoutX();
            if (!bl) {
                d7 = Math.min(d7, d9);
                d8 = Math.max(d8, d9 + node.prefWidth(-1.0));
                continue;
            }
            d7 = d9;
            d8 = d9 + node.prefWidth(-1.0);
            bl = false;
        }
        return d8 - d7;
    }

    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        double d8 = 0.0;
        boolean bl = true;
        for (int i2 = 0; i2 < this.children.size(); ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            double d9 = node.getLayoutBounds().getMinY() + node.getLayoutY();
            if (!bl) {
                d7 = Math.min(d7, d9);
                d8 = Math.max(d8, d9 + node.prefHeight(-1.0));
                continue;
            }
            d7 = d9;
            d8 = d9 + node.prefHeight(-1.0);
            bl = false;
        }
        return d8 - d7;
    }

    protected double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            double d6;
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged() || (d6 = node.getBaselineOffset()) == Double.NEGATIVE_INFINITY) continue;
            return node.getLayoutBounds().getMinY() + node.getLayoutY() + d6;
        }
        return Double.NEGATIVE_INFINITY;
    }

    protected double snappedTopInset() {
        return ((Region)this.control).snappedTopInset();
    }

    protected double snappedBottomInset() {
        return ((Region)this.control).snappedBottomInset();
    }

    protected double snappedLeftInset() {
        return ((Region)this.control).snappedLeftInset();
    }

    protected double snappedRightInset() {
        return ((Region)this.control).snappedRightInset();
    }

    protected double snapSpace(double d2) {
        return ((Region)this.control).isSnapToPixel() ? (double)Math.round(d2) : d2;
    }

    protected double snapSize(double d2) {
        return ((Region)this.control).isSnapToPixel() ? Math.ceil(d2) : d2;
    }

    protected double snapPosition(double d2) {
        return ((Region)this.control).isSnapToPixel() ? (double)Math.round(d2) : d2;
    }

    protected void positionInArea(Node node, double d2, double d3, double d4, double d5, double d6, HPos hPos, VPos vPos) {
        this.positionInArea(node, d2, d3, d4, d5, d6, Insets.EMPTY, hPos, vPos);
    }

    protected void positionInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, HPos hPos, VPos vPos) {
        Region.positionInArea(node, d2, d3, d4, d5, d6, insets, hPos, vPos, ((Region)this.control).isSnapToPixel());
    }

    protected void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, HPos hPos, VPos vPos) {
        this.layoutInArea(node, d2, d3, d4, d5, d6, Insets.EMPTY, true, true, hPos, vPos);
    }

    protected void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, HPos hPos, VPos vPos) {
        this.layoutInArea(node, d2, d3, d4, d5, d6, insets, true, true, hPos, vPos);
    }

    protected void layoutInArea(Node node, double d2, double d3, double d4, double d5, double d6, Insets insets, boolean bl, boolean bl2, HPos hPos, VPos vPos) {
        Region.layoutInArea(node, d2, d3, d4, d5, d6, insets, bl, bl2, hPos, vPos, ((Region)this.control).isSnapToPixel());
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return SkinBase.getClassCssMetaData();
    }

    public final void pseudoClassStateChanged(PseudoClass pseudoClass, boolean bl) {
        Skinnable skinnable = this.getSkinnable();
        if (skinnable != null) {
            ((Node)((Object)skinnable)).pseudoClassStateChanged(pseudoClass, bl);
        }
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        return null;
    }

    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
    }

    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES = Collections.unmodifiableList(Control.getClassCssMetaData());

        private StyleableProperties() {
        }
    }
}

