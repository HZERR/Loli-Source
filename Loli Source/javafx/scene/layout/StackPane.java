/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.css.converters.EnumConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class StackPane
extends Pane {
    private boolean biasDirty = true;
    private boolean performingLayout = false;
    private Orientation bias;
    private static final String MARGIN_CONSTRAINT = "stackpane-margin";
    private static final String ALIGNMENT_CONSTRAINT = "stackpane-alignment";
    private static final Callback<Node, Insets> marginAccessor = node -> StackPane.getMargin(node);
    private ObjectProperty<Pos> alignment;

    public static void setAlignment(Node node, Pos pos) {
        StackPane.setConstraint(node, ALIGNMENT_CONSTRAINT, (Object)pos);
    }

    public static Pos getAlignment(Node node) {
        return (Pos)((Object)StackPane.getConstraint(node, ALIGNMENT_CONSTRAINT));
    }

    public static void setMargin(Node node, Insets insets) {
        StackPane.setConstraint(node, MARGIN_CONSTRAINT, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)StackPane.getConstraint(node, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node node) {
        StackPane.setAlignment(node, null);
        StackPane.setMargin(node, null);
    }

    public StackPane() {
    }

    public StackPane(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.CENTER){

                @Override
                public void invalidated() {
                    StackPane.this.requestLayout();
                }

                @Override
                public CssMetaData<StackPane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return StackPane.this;
                }

                @Override
                public String getName() {
                    return "alignment";
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos pos) {
        this.alignmentProperty().set(pos);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.CENTER : (Pos)((Object)this.alignment.get());
    }

    private Pos getAlignmentInternal() {
        Pos pos = this.getAlignment();
        return pos == null ? Pos.CENTER : pos;
    }

    @Override
    public Orientation getContentBias() {
        if (this.biasDirty) {
            this.bias = null;
            List list = this.getManagedChildren();
            for (Node node : list) {
                Orientation orientation = node.getContentBias();
                if (orientation == null) continue;
                this.bias = orientation;
                if (orientation != Orientation.HORIZONTAL) continue;
                break;
            }
            this.biasDirty = false;
        }
        return this.bias;
    }

    @Override
    protected double computeMinWidth(double d2) {
        List<Node> list = this.getManagedChildren();
        return this.getInsets().getLeft() + this.computeMaxMinAreaWidth(list, marginAccessor, d2, true) + this.getInsets().getRight();
    }

    @Override
    protected double computeMinHeight(double d2) {
        List<Node> list = this.getManagedChildren();
        return this.getInsets().getTop() + this.computeMaxMinAreaHeight(list, marginAccessor, this.getAlignmentInternal().getVpos(), d2) + this.getInsets().getBottom();
    }

    @Override
    protected double computePrefWidth(double d2) {
        List<Node> list = this.getManagedChildren();
        Insets insets = this.getInsets();
        return insets.getLeft() + this.computeMaxPrefAreaWidth(list, marginAccessor, d2 == -1.0 ? -1.0 : d2 - insets.getTop() - insets.getBottom(), true) + insets.getRight();
    }

    @Override
    protected double computePrefHeight(double d2) {
        List<Node> list = this.getManagedChildren();
        Insets insets = this.getInsets();
        return insets.getTop() + this.computeMaxPrefAreaHeight(list, marginAccessor, d2 == -1.0 ? -1.0 : d2 - insets.getLeft() - insets.getRight(), this.getAlignmentInternal().getVpos()) + insets.getBottom();
    }

    @Override
    public void requestLayout() {
        if (this.performingLayout) {
            return;
        }
        this.biasDirty = true;
        this.bias = null;
        super.requestLayout();
    }

    @Override
    protected void layoutChildren() {
        this.performingLayout = true;
        List<Node> list = this.getManagedChildren();
        Pos pos = this.getAlignmentInternal();
        HPos hPos = pos.getHpos();
        VPos vPos = pos.getVpos();
        double d2 = this.getWidth();
        double d3 = this.getHeight();
        double d4 = this.getInsets().getTop();
        double d5 = this.getInsets().getRight();
        double d6 = this.getInsets().getLeft();
        double d7 = this.getInsets().getBottom();
        double d8 = d2 - d6 - d5;
        double d9 = d3 - d4 - d7;
        double d10 = vPos == VPos.BASELINE ? this.getAreaBaselineOffset(list, marginAccessor, n2 -> d2, d9, true) : 0.0;
        int n3 = list.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            Node node = list.get(i2);
            Pos pos2 = StackPane.getAlignment(node);
            this.layoutInArea(node, d6, d4, d8, d9, d10, StackPane.getMargin(node), pos2 != null ? pos2.getHpos() : hPos, pos2 != null ? pos2.getVpos() : vPos);
        }
        this.performingLayout = false;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return StackPane.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<StackPane, Pos> ALIGNMENT = new CssMetaData<StackPane, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.CENTER){

            @Override
            public boolean isSettable(StackPane stackPane) {
                return stackPane.alignment == null || !stackPane.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(StackPane stackPane) {
                return (StyleableProperty)((Object)stackPane.alignmentProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

