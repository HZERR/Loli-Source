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
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class VBox
extends Pane {
    private boolean biasDirty = true;
    private boolean performingLayout = false;
    private Orientation bias;
    private double[][] tempArray;
    private static final String MARGIN_CONSTRAINT = "vbox-margin";
    private static final String VGROW_CONSTRAINT = "vbox-vgrow";
    private static final Callback<Node, Insets> marginAccessor = node -> VBox.getMargin(node);
    private DoubleProperty spacing;
    private ObjectProperty<Pos> alignment;
    private BooleanProperty fillWidth;

    public static void setVgrow(Node node, Priority priority) {
        VBox.setConstraint(node, VGROW_CONSTRAINT, (Object)priority);
    }

    public static Priority getVgrow(Node node) {
        return (Priority)((Object)VBox.getConstraint(node, VGROW_CONSTRAINT));
    }

    public static void setMargin(Node node, Insets insets) {
        VBox.setConstraint(node, MARGIN_CONSTRAINT, insets);
    }

    public static Insets getMargin(Node node) {
        return (Insets)VBox.getConstraint(node, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node node) {
        VBox.setVgrow(node, null);
        VBox.setMargin(node, null);
    }

    public VBox() {
    }

    public VBox(double d2) {
        this();
        this.setSpacing(d2);
    }

    public VBox(Node ... arrnode) {
        this.getChildren().addAll(arrnode);
    }

    public VBox(double d2, Node ... arrnode) {
        this();
        this.setSpacing(d2);
        this.getChildren().addAll(arrnode);
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty(){

                @Override
                public void invalidated() {
                    VBox.this.requestLayout();
                }

                @Override
                public Object getBean() {
                    return VBox.this;
                }

                @Override
                public String getName() {
                    return "spacing";
                }

                @Override
                public CssMetaData<VBox, Number> getCssMetaData() {
                    return StyleableProperties.SPACING;
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
                    VBox.this.requestLayout();
                }

                @Override
                public Object getBean() {
                    return VBox.this;
                }

                @Override
                public String getName() {
                    return "alignment";
                }

                @Override
                public CssMetaData<VBox, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
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

    public final BooleanProperty fillWidthProperty() {
        if (this.fillWidth == null) {
            this.fillWidth = new StyleableBooleanProperty(true){

                @Override
                public void invalidated() {
                    VBox.this.requestLayout();
                }

                @Override
                public Object getBean() {
                    return VBox.this;
                }

                @Override
                public String getName() {
                    return "fillWidth";
                }

                @Override
                public CssMetaData<VBox, Boolean> getCssMetaData() {
                    return StyleableProperties.FILL_WIDTH;
                }
            };
        }
        return this.fillWidth;
    }

    public final void setFillWidth(boolean bl) {
        this.fillWidthProperty().set(bl);
    }

    public final boolean isFillWidth() {
        return this.fillWidth == null ? true : this.fillWidth.get();
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
        List<Node> list = this.getManagedChildren();
        double d3 = 0.0;
        if (d2 != -1.0 && this.getContentBias() != null) {
            double[][] arrd = this.getAreaHeights(list, -1.0, false);
            this.adjustAreaHeights(list, arrd, d2, -1.0);
            d3 = this.computeMaxMinAreaWidth(list, marginAccessor, arrd[0], false);
        } else {
            d3 = this.computeMaxMinAreaWidth(list, marginAccessor);
        }
        return this.snapSpace(insets.getLeft()) + d3 + this.snapSpace(insets.getRight());
    }

    @Override
    protected double computeMinHeight(double d2) {
        Insets insets = this.getInsets();
        return this.snapSpace(insets.getTop()) + this.computeContentHeight(this.getManagedChildren(), d2, true) + this.snapSpace(insets.getBottom());
    }

    @Override
    protected double computePrefWidth(double d2) {
        Insets insets = this.getInsets();
        List<Node> list = this.getManagedChildren();
        double d3 = 0.0;
        if (d2 != -1.0 && this.getContentBias() != null) {
            double[][] arrd = this.getAreaHeights(list, -1.0, false);
            this.adjustAreaHeights(list, arrd, d2, -1.0);
            d3 = this.computeMaxPrefAreaWidth(list, marginAccessor, arrd[0], false);
        } else {
            d3 = this.computeMaxPrefAreaWidth(list, marginAccessor);
        }
        return this.snapSpace(insets.getLeft()) + d3 + this.snapSpace(insets.getRight());
    }

    @Override
    protected double computePrefHeight(double d2) {
        Insets insets = this.getInsets();
        double d3 = this.snapSpace(insets.getTop()) + this.computeContentHeight(this.getManagedChildren(), d2, false) + this.snapSpace(insets.getBottom());
        return d3;
    }

    private double[][] getAreaHeights(List<Node> list, double d2, boolean bl) {
        double[][] arrd = this.getTempArray(list.size());
        double d3 = d2 == -1.0 ? -1.0 : d2 - this.snapSpace(this.getInsets().getLeft()) - this.snapSpace(this.getInsets().getRight());
        boolean bl2 = this.isFillWidth();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = list.get(i2);
            Insets insets = VBox.getMargin(node);
            if (bl) {
                if (d3 != -1.0 && bl2) {
                    arrd[0][i2] = this.computeChildMinAreaHeight(node, -1.0, insets, d3);
                    continue;
                }
                arrd[0][i2] = this.computeChildMinAreaHeight(node, -1.0, insets, -1.0);
                continue;
            }
            arrd[0][i2] = d3 != -1.0 && bl2 ? this.computeChildPrefAreaHeight(node, -1.0, insets, d3) : this.computeChildPrefAreaHeight(node, -1.0, insets, -1.0);
        }
        return arrd;
    }

    private double adjustAreaHeights(List<Node> list, double[][] arrd, double d2, double d3) {
        Insets insets = this.getInsets();
        double d4 = this.snapSpace(insets.getLeft());
        double d5 = this.snapSpace(insets.getRight());
        double d6 = VBox.sum(arrd[0], list.size()) + (double)(list.size() - 1) * this.snapSpace(this.getSpacing());
        double d7 = d2 - this.snapSpace(insets.getTop()) - this.snapSpace(insets.getBottom()) - d6;
        if (d7 != 0.0) {
            double d8 = this.isFillWidth() && d3 != -1.0 ? d3 - d4 - d5 : -1.0;
            double d9 = this.growOrShrinkAreaHeights(list, arrd, Priority.ALWAYS, d7, d8);
            d9 = this.growOrShrinkAreaHeights(list, arrd, Priority.SOMETIMES, d9, d8);
            d6 += d7 - d9;
        }
        return d6;
    }

    private double growOrShrinkAreaHeights(List<Node> list, double[][] arrd, Priority priority, double d2, double d3) {
        Node node;
        int n2;
        int n3;
        boolean bl = d2 < 0.0;
        int n4 = 0;
        double[] arrd2 = arrd[0];
        double[] arrd3 = arrd[1];
        if (bl) {
            n4 = list.size();
            n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                node = list.get(n2);
                arrd3[n2] = this.computeChildMinAreaHeight(node, -1.0, VBox.getMargin(node), d3);
            }
        } else {
            n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                node = list.get(n2);
                if (VBox.getVgrow(node) == priority) {
                    arrd3[n2] = this.computeChildMaxAreaHeight(node, -1.0, VBox.getMargin(node), d3);
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

    private double computeContentHeight(List<Node> list, double d2, boolean bl) {
        return VBox.sum(this.getAreaHeights(list, d2, bl)[0], list.size()) + (double)(list.size() - 1) * this.snapSpace(this.getSpacing());
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
        super.requestLayout();
    }

    @Override
    protected void layoutChildren() {
        this.performingLayout = true;
        List<Node> list = this.getManagedChildren();
        Insets insets = this.getInsets();
        double d2 = this.getWidth();
        double d3 = this.getHeight();
        double d4 = this.snapSpace(insets.getTop());
        double d5 = this.snapSpace(insets.getLeft());
        double d6 = this.snapSpace(insets.getBottom());
        double d7 = this.snapSpace(insets.getRight());
        double d8 = this.snapSpace(this.getSpacing());
        HPos hPos = this.getAlignmentInternal().getHpos();
        VPos vPos = this.getAlignmentInternal().getVpos();
        boolean bl = this.isFillWidth();
        double[][] arrd = this.getAreaHeights(list, d2, false);
        double d9 = d2 - d5 - d7;
        double d10 = this.adjustAreaHeights(list, arrd, d3, d2);
        double d11 = d5;
        double d12 = d4 + VBox.computeYOffset(d3 - d4 - d6, d10, vPos);
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = list.get(i2);
            this.layoutInArea(node, d11, d12, d9, arrd[0][i2], arrd[0][i2], VBox.getMargin(node), bl, true, hPos, vPos);
            d12 += arrd[0][i2] + d8;
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
        return VBox.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<VBox, Pos> ALIGNMENT = new CssMetaData<VBox, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            @Override
            public boolean isSettable(VBox vBox) {
                return vBox.alignment == null || !vBox.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(VBox vBox) {
                return (StyleableProperty)((Object)vBox.alignmentProperty());
            }
        };
        private static final CssMetaData<VBox, Boolean> FILL_WIDTH = new CssMetaData<VBox, Boolean>("-fx-fill-width", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(VBox vBox) {
                return vBox.fillWidth == null || !vBox.fillWidth.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(VBox vBox) {
                return (StyleableProperty)((Object)vBox.fillWidthProperty());
            }
        };
        private static final CssMetaData<VBox, Number> SPACING = new CssMetaData<VBox, Number>("-fx-spacing", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(VBox vBox) {
                return vBox.spacing == null || !vBox.spacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(VBox vBox) {
                return (StyleableProperty)((Object)vBox.spacingProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(ALIGNMENT);
            arrayList.add(FILL_WIDTH);
            arrayList.add(SPACING);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

