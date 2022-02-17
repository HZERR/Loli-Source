/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.text;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TextFlow
extends Pane {
    private TextLayout layout;
    private boolean needsContent;
    private boolean inLayout;
    private ObjectProperty<TextAlignment> textAlignment;
    private DoubleProperty lineSpacing;

    public TextFlow() {
        this.effectiveNodeOrientationProperty().addListener(observable -> this.checkOrientation());
        this.setAccessibleRole(AccessibleRole.TEXT);
    }

    public TextFlow(Node ... arrnode) {
        this();
        this.getChildren().addAll(arrnode);
    }

    private void checkOrientation() {
        NodeOrientation nodeOrientation = this.getEffectiveNodeOrientation();
        boolean bl = nodeOrientation == NodeOrientation.RIGHT_TO_LEFT;
        int n2 = bl ? 2048 : 1024;
        TextLayout textLayout = this.getTextLayout();
        if (textLayout.setDirection(n2)) {
            this.requestLayout();
        }
    }

    @Override
    public boolean usesMirroring() {
        return false;
    }

    @Override
    protected void setWidth(double d2) {
        if (d2 != this.getWidth()) {
            TextLayout textLayout = this.getTextLayout();
            Insets insets = this.getInsets();
            double d3 = this.snapSpace(insets.getLeft());
            double d4 = this.snapSpace(insets.getRight());
            double d5 = Math.max(1.0, d2 - d3 - d4);
            textLayout.setWrapWidth((float)d5);
            super.setWidth(d2);
        }
    }

    @Override
    protected double computePrefWidth(double d2) {
        TextLayout textLayout = this.getTextLayout();
        textLayout.setWrapWidth(0.0f);
        double d3 = textLayout.getBounds().getWidth();
        Insets insets = this.getInsets();
        double d4 = this.snapSpace(insets.getLeft());
        double d5 = this.snapSpace(insets.getRight());
        double d6 = Math.max(1.0, this.getWidth() - d4 - d5);
        textLayout.setWrapWidth((float)d6);
        return d4 + d3 + d5;
    }

    @Override
    protected double computePrefHeight(double d2) {
        double d3;
        TextLayout textLayout = this.getTextLayout();
        Insets insets = this.getInsets();
        double d4 = this.snapSpace(insets.getLeft());
        double d5 = this.snapSpace(insets.getRight());
        if (d2 == -1.0) {
            textLayout.setWrapWidth(0.0f);
        } else {
            d3 = Math.max(1.0, d2 - d4 - d5);
            textLayout.setWrapWidth((float)d3);
        }
        d3 = textLayout.getBounds().getHeight();
        double d6 = Math.max(1.0, this.getWidth() - d4 - d5);
        textLayout.setWrapWidth((float)d6);
        double d7 = this.snapSpace(insets.getTop());
        double d8 = this.snapSpace(insets.getBottom());
        return d7 + d3 + d8;
    }

    @Override
    protected double computeMinHeight(double d2) {
        return this.computePrefHeight(d2);
    }

    @Override
    public void requestLayout() {
        if (this.inLayout) {
            return;
        }
        this.needsContent = true;
        super.requestLayout();
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected void layoutChildren() {
        Object object;
        Node node;
        this.inLayout = true;
        Insets insets = this.getInsets();
        double d2 = this.snapSpace(insets.getTop());
        double d3 = this.snapSpace(insets.getLeft());
        GlyphList[] arrglyphList = this.getTextLayout().getRuns();
        for (int i2 = 0; i2 < arrglyphList.length; ++i2) {
            GlyphList glyphList = arrglyphList[i2];
            Object object2 = glyphList.getTextSpan();
            if (!(object2 instanceof EmbeddedSpan)) continue;
            node = ((EmbeddedSpan)object2).getNode();
            object = glyphList.getLocation();
            double d4 = -glyphList.getLineBounds().getMinY();
            this.layoutInArea(node, d3 + (double)((Point2D)object).x, d2 + (double)((Point2D)object).y, glyphList.getWidth(), glyphList.getHeight(), d4, null, true, true, HPos.CENTER, VPos.BASELINE);
        }
        List list = this.getManagedChildren();
        for (Object object2 : list) {
            if (!(object2 instanceof Text)) continue;
            node = (Text)object2;
            ((Text)node).layoutSpan(arrglyphList);
            object = ((Text)node).getSpanBounds();
            node.relocate(d3 + (double)((BaseBounds)object).getMinX(), d2 + (double)((BaseBounds)object).getMinY());
        }
        this.inLayout = false;
    }

    TextLayout getTextLayout() {
        Object object;
        if (this.layout == null) {
            object = Toolkit.getToolkit().getTextLayoutFactory();
            this.layout = object.createLayout();
            this.needsContent = true;
        }
        if (this.needsContent) {
            object = this.getManagedChildren();
            TextSpan[] arrtextSpan = new TextSpan[object.size()];
            for (int i2 = 0; i2 < arrtextSpan.length; ++i2) {
                Node node = (Node)object.get(i2);
                if (node instanceof Text) {
                    arrtextSpan[i2] = ((Text)node).getTextSpan();
                    continue;
                }
                double d2 = node.getBaselineOffset();
                if (d2 == Double.NEGATIVE_INFINITY) {
                    d2 = node.getLayoutBounds().getHeight();
                }
                double d3 = this.computeChildPrefAreaWidth(node, null);
                double d4 = this.computeChildPrefAreaHeight(node, null);
                arrtextSpan[i2] = new EmbeddedSpan(node, d2, d3, d4);
            }
            this.layout.setContent(arrtextSpan);
            this.needsContent = false;
        }
        return this.layout;
    }

    public final void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignmentProperty().set(textAlignment);
    }

    public final TextAlignment getTextAlignment() {
        return this.textAlignment == null ? TextAlignment.LEFT : (TextAlignment)((Object)this.textAlignment.get());
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        if (this.textAlignment == null) {
            this.textAlignment = new StyleableObjectProperty<TextAlignment>(TextAlignment.LEFT){

                @Override
                public Object getBean() {
                    return TextFlow.this;
                }

                @Override
                public String getName() {
                    return "textAlignment";
                }

                @Override
                public CssMetaData<TextFlow, TextAlignment> getCssMetaData() {
                    return StyleableProperties.TEXT_ALIGNMENT;
                }

                @Override
                public void invalidated() {
                    TextAlignment textAlignment = (TextAlignment)((Object)this.get());
                    if (textAlignment == null) {
                        textAlignment = TextAlignment.LEFT;
                    }
                    TextLayout textLayout = TextFlow.this.getTextLayout();
                    textLayout.setAlignment(textAlignment.ordinal());
                    TextFlow.this.requestLayout();
                }
            };
        }
        return this.textAlignment;
    }

    public final void setLineSpacing(double d2) {
        this.lineSpacingProperty().set(d2);
    }

    public final double getLineSpacing() {
        return this.lineSpacing == null ? 0.0 : this.lineSpacing.get();
    }

    public final DoubleProperty lineSpacingProperty() {
        if (this.lineSpacing == null) {
            this.lineSpacing = new StyleableDoubleProperty(0.0){

                @Override
                public Object getBean() {
                    return TextFlow.this;
                }

                @Override
                public String getName() {
                    return "lineSpacing";
                }

                @Override
                public CssMetaData<TextFlow, Number> getCssMetaData() {
                    return StyleableProperties.LINE_SPACING;
                }

                @Override
                public void invalidated() {
                    TextLayout textLayout = TextFlow.this.getTextLayout();
                    if (textLayout.setLineSpacing((float)this.get())) {
                        TextFlow.this.requestLayout();
                    }
                }
            };
        }
        return this.lineSpacing;
    }

    @Override
    public final double getBaselineOffset() {
        Insets insets = this.getInsets();
        double d2 = this.snapSpace(insets.getTop());
        return d2 - (double)this.getTextLayout().getBounds().getMinY();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TextFlow.getClassCssMetaData();
    }

    private static double snapSpace(double d2, boolean bl) {
        return bl ? (double)Math.round(d2) : d2;
    }

    static double boundedSize(double d2, double d3, double d4) {
        double d5 = d3 >= d2 ? d3 : d2;
        double d6 = d2 >= d4 ? d2 : d4;
        return d5 <= d6 ? d5 : d6;
    }

    double computeChildPrefAreaWidth(Node node, Insets insets) {
        return this.computeChildPrefAreaWidth(node, insets, -1.0);
    }

    double computeChildPrefAreaWidth(Node node, Insets insets, double d2) {
        boolean bl = this.isSnapToPixel();
        double d3 = insets != null ? TextFlow.snapSpace(insets.getTop(), bl) : 0.0;
        double d4 = insets != null ? TextFlow.snapSpace(insets.getBottom(), bl) : 0.0;
        double d5 = insets != null ? TextFlow.snapSpace(insets.getLeft(), bl) : 0.0;
        double d6 = insets != null ? TextFlow.snapSpace(insets.getRight(), bl) : 0.0;
        double d7 = -1.0;
        if (node.getContentBias() == Orientation.VERTICAL) {
            d7 = this.snapSize(TextFlow.boundedSize(node.minHeight(-1.0), d2 != -1.0 ? d2 - d3 - d4 : node.prefHeight(-1.0), node.maxHeight(-1.0)));
        }
        return d5 + this.snapSize(TextFlow.boundedSize(node.minWidth(d7), node.prefWidth(d7), node.maxWidth(d7))) + d6;
    }

    double computeChildPrefAreaHeight(Node node, Insets insets) {
        return this.computeChildPrefAreaHeight(node, insets, -1.0);
    }

    double computeChildPrefAreaHeight(Node node, Insets insets, double d2) {
        boolean bl = this.isSnapToPixel();
        double d3 = insets != null ? TextFlow.snapSpace(insets.getTop(), bl) : 0.0;
        double d4 = insets != null ? TextFlow.snapSpace(insets.getBottom(), bl) : 0.0;
        double d5 = insets != null ? TextFlow.snapSpace(insets.getLeft(), bl) : 0.0;
        double d6 = insets != null ? TextFlow.snapSpace(insets.getRight(), bl) : 0.0;
        double d7 = -1.0;
        if (node.getContentBias() == Orientation.HORIZONTAL) {
            d7 = this.snapSize(TextFlow.boundedSize(node.minWidth(-1.0), d2 != -1.0 ? d2 - d5 - d6 : node.prefWidth(-1.0), node.maxWidth(-1.0)));
        }
        return d3 + this.snapSize(TextFlow.boundedSize(node.minHeight(d7), node.prefHeight(d7), node.maxHeight(d7))) + d4;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case TEXT: {
                String string = this.getAccessibleText();
                if (string != null && !string.isEmpty()) {
                    return string;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (Node node : this.getChildren()) {
                    Object object = node.queryAccessibleAttribute(AccessibleAttribute.TEXT, arrobject);
                    if (object == null) continue;
                    stringBuilder.append(object.toString());
                }
                return stringBuilder.toString();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    private static class StyleableProperties {
        private static final CssMetaData<TextFlow, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<TextFlow, TextAlignment>("-fx-text-alignment", new EnumConverter<TextAlignment>(TextAlignment.class), TextAlignment.LEFT){

            @Override
            public boolean isSettable(TextFlow textFlow) {
                return textFlow.textAlignment == null || !textFlow.textAlignment.isBound();
            }

            @Override
            public StyleableProperty<TextAlignment> getStyleableProperty(TextFlow textFlow) {
                return (StyleableProperty)((Object)textFlow.textAlignmentProperty());
            }
        };
        private static final CssMetaData<TextFlow, Number> LINE_SPACING = new CssMetaData<TextFlow, Number>("-fx-line-spacing", SizeConverter.getInstance(), (Number)0){

            @Override
            public boolean isSettable(TextFlow textFlow) {
                return textFlow.lineSpacing == null || !textFlow.lineSpacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TextFlow textFlow) {
                return (StyleableProperty)((Object)textFlow.lineSpacingProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Pane.getClassCssMetaData());
            arrayList.add(TEXT_ALIGNMENT);
            arrayList.add(LINE_SPACING);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    private static class EmbeddedSpan
    implements TextSpan {
        RectBounds bounds;
        Node node;

        public EmbeddedSpan(Node node, double d2, double d3, double d4) {
            this.node = node;
            this.bounds = new RectBounds(0.0f, (float)(-d2), (float)d3, (float)(d4 - d2));
        }

        @Override
        public String getText() {
            return "\ufffc";
        }

        @Override
        public Object getFont() {
            return null;
        }

        @Override
        public RectBounds getBounds() {
            return this.bounds;
        }

        public Node getNode() {
            return this.node;
        }
    }
}

