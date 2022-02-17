/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class HBox
extends Pane {
    private boolean biasDirty = true;
    private boolean performingLayout = false;
    private double minBaselineComplement = Double.NaN;
    private double prefBaselineComplement = Double.NaN;
    private Orientation bias;
    private double[][] tempArray;
    private static final String MARGIN_CONSTRAINT = "hbox-margin";
    private static final String HGROW_CONSTRAINT = "hbox-hgrow";
    private static final Callback<Node, Insets> marginAccessor = node -> HBox.getMargin(node);
    private DoubleProperty spacing;
    private ObjectProperty<Pos> alignment;
    private BooleanProperty fillHeight;
    private double baselineOffset = Double.NaN;

    public static void setHgrow(Node node, Priority priority) {
        HBox.setConstraint(node, HGROW_CONSTRAINT, (Object)priority);
    }

    public static Priority getHgrow(Node node) {
        return (Priority)((Object)HBox.getConstraint(node, HGROW_CONSTRAINT));
    }

    public static void setMargin(Node node, Insets insets) {
        HBox.setConstraint(node, MARGIN_CONSTRAINT, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)HBox.getConstraint(node, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node node) {
        HBox.setHgrow(node, null);
        HBox.setMargin(node, null);
    }

    public HBox() {
    }

    public HBox(double d2) {
        this();
        this.setSpacing(d2);
    }

    public HBox(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    public HBox(double d2, Node ... arrnode) {
        this();
        this.setSpacing(d2);
        this.getChildren().addAll(arrnode);
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    HBox.this.requestLayout();
                }

                @Override
                public CssMetaData getCssMetaData() {
                    return StyleableProperties.SPACING;
                }

                @Override
                public Object getBean() {
                    return HBox.this;
                }

                @Override
                public String getName() {
                    return "spacing";
                }
            };
        }
        return this.spacing;
    }

    public final void setSpacing(double d2) {
        this.spacingProperty().set(d2);
    }

    public final double getSpacing() {
        return this.spacing == null ? 0.0 : this.spacing.get();
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT){

                @Override
                public void invalidated() {
                    HBox.this.requestLayout();
                }

                @Override
                public CssMetaData<HBox, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return HBox.this;
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
        return this.alignment == null ? Pos.TOP_LEFT : (Pos)((Object)this.alignment.get());
    }

    private Pos getAlignmentInternal() {
        Pos pos = this.getAlignment();
        return pos == null ? Pos.TOP_LEFT : pos;
    }

    public final BooleanProperty fillHeightProperty() {
        if (this.fillHeight == null) {
            this.fillHeight = new StyleableBooleanProperty(true){

                @Override
                public void invalidated() {
                    HBox.this.requestLayout();
                }

                @Override
                public CssMetaData<HBox, Boolean> getCssMetaData() {
                    return StyleableProperties.FILL_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return HBox.this;
                }

                @Override
                public String getName() {
                    return "fillHeight";
                }
            };
        }
        return this.fillHeight;
    }

    public final void setFillHeight(boolean bl) {
        this.fillHeightProperty().set(bl);
    }

    public final boolean isFillHeight() {
        return this.fillHeight == null ? true : this.fillHeight.get();
    }

    private boolean shouldFillHeight() {
        return this.isFillHeight() && this.getAlignmentInternal().getVpos() != VPos.BASELINE;
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
        Insets insets = this.getInsets();
        return this.snapSpace(insets.getLeft()) + this.computeContentWidth(this.getManagedChildren(), d2, true) + this.snapSpace(insets.getRight());
    }

    @Override
    protected double computeMinHeight(double d2) {
        Insets insets = this.getInsets();
        List<Node> list = this.getManagedChildren();
        double d3 = 0.0;
        if (d2 != -1.0 && this.getContentBias() != null) {
            double[][] arrd = this.getAreaWidths(list, -1.0, false);
            this.adjustAreaWidths(list, arrd, d2, -1.0);
            d3 = this.computeMaxMinAreaHeight(list, marginAccessor, arrd[0], this.getAlignmentInternal().getVpos());
        } else {
            d3 = this.computeMaxMinAreaHeight(list, marginAccessor, this.getAlignmentInternal().getVpos());
        }
        return this.snapSpace(insets.getTop()) + d3 + this.snapSpace(insets.getBottom());
    }

    @Override
    protected double computePrefWidth(double d2) {
        Insets insets = this.getInsets();
        return this.snapSpace(insets.getLeft()) + this.computeContentWidth(this.getManagedChildren(), d2, false) + this.snapSpace(insets.getRight());
    }

    @Override
    protected double computePrefHeight(double d2) {
        Insets insets = this.getInsets();
        List<Node> list = this.getManagedChildren();
        double d3 = 0.0;
        if (d2 != -1.0 && this.getContentBias() != null) {
            double[][] arrd = this.getAreaWidths(list, -1.0, false);
            this.adjustAreaWidths(list, arrd, d2, -1.0);
            d3 = this.computeMaxPrefAreaHeight(list, marginAccessor, arrd[0], this.getAlignmentInternal().getVpos());
        } else {
            d3 = this.computeMaxPrefAreaHeight(list, marginAccessor, this.getAlignmentInternal().getVpos());
        }
        return this.snapSpace(insets.getTop()) + d3 + this.snapSpace(insets.getBottom());
    }

    private double[][] getAreaWidths(List<Node> list, double d2, boolean bl) {
        double[][] arrd = this.getTempArray(list.size());
        double d3 = d2 == -1.0 ? -1.0 : d2 - this.snapSpace(this.getInsets().getTop()) - this.snapSpace(this.getInsets().getBottom());
        boolean bl2 = this.shouldFillHeight();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = list.get(i2);
            Insets insets = HBox.getMargin(node);
            arrd[0][i2] = bl ? this.computeChildMinAreaWidth(node, this.getMinBaselineComplement(), insets, d3, bl2) : this.computeChildPrefAreaWidth(node, this.getPrefBaselineComplement(), insets, d3, bl2);
        }
        return arrd;
    }

    private double adjustAreaWidths(List<Node> list, double[][] arrd, double d2, double d3) {
        Insets insets = this.getInsets();
        double d4 = this.snapSpace(insets.getTop());
        double d5 = this.snapSpace(insets.getBottom());
        double d6 = HBox.sum(arrd[0], list.size()) + (double)(list.size() - 1) * this.snapSpace(this.getSpacing());
        double d7 = d2 - this.snapSpace(insets.getLeft()) - this.snapSpace(insets.getRight()) - d6;
        if (d7 != 0.0) {
            double d8 = this.shouldFillHeight() && d3 != -1.0 ? d3 - d4 - d5 : -1.0;
            double d9 = this.growOrShrinkAreaWidths(list, arrd, Priority.ALWAYS, d7, d8);
            d9 = this.growOrShrinkAreaWidths(list, arrd, Priority.SOMETIMES, d9, d8);
            d6 += d7 - d9;
        }
        return d6;
    }

    private double growOrShrinkAreaWidths(List<Node> list, double[][] arrd, Priority priority, double d2, double d3) {
        Node node;
        int n2;
        int n3;
        boolean bl = d2 < 0.0;
        int n4 = 0;
        double[] arrd2 = arrd[0];
        double[] arrd3 = arrd[1];
        boolean bl2 = this.shouldFillHeight();
        if (bl) {
            n4 = list.size();
            n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                node = list.get(n2);
                arrd3[n2] = this.computeChildMinAreaWidth(node, this.getMinBaselineComplement(), HBox.getMargin(node), d3, bl2);
            }
        } else {
            n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                node = list.get(n2);
                if (HBox.getHgrow(node) == priority) {
                    arrd3[n2] = this.computeChildMaxAreaWidth(node, this.getMinBaselineComplement(), HBox.getMargin(node), d3, bl2);
                    ++n4;
                    continue;
                }
                arrd3[n2] = -1.0;
            }
        }
        double d4 = d2;
        block2: while (Math.abs(d4) > 1.0 && n4 > 0) {
            double d5 = this.snapPortion(d4 / (double)n4);
            int n5 = list.size();
            for (int i2 = 0; i2 < n5; ++i2) {
                if (arrd3[i2] == -1.0) continue;
                double d6 = arrd3[i2] - arrd2[i2];
                double d7 = Math.abs(d6) <= Math.abs(d5) ? d6 : d5;
                int n6 = i2;
                arrd2[n6] = arrd2[n6] + d7;
                if (Math.abs(d4 -= d7) < 1.0) break block2;
                if (!(Math.abs(d7) < Math.abs(d5))) continue;
                arrd3[i2] = -1.0;
                --n4;
            }
        }
        return d4;
    }

    private double computeContentWidth(List<Node> list, double d2, boolean bl) {
        return HBox.sum(this.getAreaWidths(list, d2, bl)[0], list.size()) + (double)(list.size() - 1) * this.snapSpace(this.getSpacing());
    }

    private static double sum(double[] arrd, int n2) {
        int n3 = 0;
        double d2 = 0.0;
        while (n3 != n2) {
            d2 += arrd[n3++];
        }
        return d2;
    }

    @Override
    public void requestLayout() {
        if (this.performingLayout) {
            return;
        }
        this.biasDirty = true;
        this.bias = null;
        this.minBaselineComplement = Double.NaN;
        this.prefBaselineComplement = Double.NaN;
        this.baselineOffset = Double.NaN;
        super.requestLayout();
    }

    private double getMinBaselineComplement() {
        if (Double.isNaN(this.minBaselineComplement)) {
            this.minBaselineComplement = this.getAlignmentInternal().getVpos() == VPos.BASELINE ? HBox.getMinBaselineComplement(this.getManagedChildren()) : -1.0;
        }
        return this.minBaselineComplement;
    }

    private double getPrefBaselineComplement() {
        if (Double.isNaN(this.prefBaselineComplement)) {
            this.prefBaselineComplement = this.getAlignmentInternal().getVpos() == VPos.BASELINE ? HBox.getPrefBaselineComplement(this.getManagedChildren()) : -1.0;
        }
        return this.prefBaselineComplement;
    }

    @Override
    public double getBaselineOffset() {
        List list = this.getManagedChildren();
        if (list.isEmpty()) {
            return Double.NEGATIVE_INFINITY;
        }
        if (Double.isNaN(this.baselineOffset)) {
            VPos vPos = this.getAlignmentInternal().getVpos();
            if (vPos == VPos.BASELINE) {
                double d2 = 0.0;
                int n2 = list.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Node node = (Node)list.get(i2);
                    double d3 = node.getBaselineOffset();
                    if (d3 == Double.NEGATIVE_INFINITY) {
                        this.baselineOffset = Double.NEGATIVE_INFINITY;
                        break;
                    }
                    Insets insets = HBox.getMargin(node);
                    double d4 = insets != null ? insets.getTop() : 0.0;
                    d2 = Math.max(d2, d4 + node.getLayoutBounds().getMinY() + d3);
                }
                this.baselineOffset = d2 + this.snappedTopInset();
            } else {
                this.baselineOffset = Double.NEGATIVE_INFINITY;
            }
        }
        return this.baselineOffset;
    }

    @Override
    protected void layoutChildren() {
        this.performingLayout = true;
        List<Node> list = this.getManagedChildren();
        Insets insets = this.getInsets();
        Pos pos = this.getAlignmentInternal();
        HPos hPos = pos.getHpos();
        VPos vPos = pos.getVpos();
        double d2 = this.getWidth();
        double d3 = this.getHeight();
        double d4 = this.snapSpace(insets.getTop());
        double d5 = this.snapSpace(insets.getLeft());
        double d6 = this.snapSpace(insets.getBottom());
        double d7 = this.snapSpace(insets.getRight());
        double d8 = this.snapSpace(this.getSpacing());
        boolean bl = this.shouldFillHeight();
        double[][] arrd = this.getAreaWidths(list, d3, false);
        double d9 = this.adjustAreaWidths(list, arrd, d2, d3);
        double d10 = d3 - d4 - d6;
        double d11 = d5 + HBox.computeXOffset(d2 - d5 - d7, d9, pos.getHpos());
        double d12 = d4;
        double d13 = -1.0;
        if (vPos == VPos.BASELINE) {
            double d14 = this.getMinBaselineComplement();
            d13 = this.getAreaBaselineOffset(list, marginAccessor, (Integer n2) -> arrd[0][n2], d10, bl, d14);
        }
        int n3 = list.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            Node node = list.get(i2);
            Insets insets2 = HBox.getMargin(node);
            this.layoutInArea(node, d11, d12, arrd[0][i2], d10, d13, insets2, true, bl, hPos, vPos);
            d11 += arrd[0][i2] + d8;
        }
        this.performingLayout = false;
    }

    private double[][] getTempArray(int n2) {
        if (this.tempArray == null) {
            this.tempArray = new double[2][n2];
        } else if (this.tempArray[0].length < n2) {
            this.tempArray = new double[2][Math.max(this.tempArray.length * 3, n2)];
        }
        return this.tempArray;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return HBox.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<HBox, Pos> ALIGNMENT = new CssMetaData<HBox, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            @Override
            public boolean isSettable(HBox hBox) {
                return hBox.alignment == null || !hBox.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(HBox hBox) {
                return (StyleableProperty)((Object)hBox.alignmentProperty());
            }
        };
        private static final CssMetaData<HBox, Boolean> FILL_HEIGHT = new CssMetaData<HBox, Boolean>("-fx-fill-height", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(HBox hBox) {
                return hBox.fillHeight == null || !hBox.fillHeight.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(HBox hBox) {
                return (StyleableProperty)((Object)hBox.fillHeightProperty());
            }
        };
        private static final CssMetaData<HBox, Number> SPACING = new CssMetaData<HBox, Number>("-fx-spacing", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(HBox hBox) {
                return hBox.spacing == null || !hBox.spacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(HBox hBox) {
                return (StyleableProperty)((Object)hBox.spacingProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Pane.getClassCssMetaData());
            arrayList.add(FILL_HEIGHT);
            arrayList.add(ALIGNMENT);
            arrayList.add(SPACING);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

