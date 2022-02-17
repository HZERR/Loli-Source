/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.css.SubCssMetaData;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

public final class Border {
    static final CssMetaData<Node, Paint[]> BORDER_COLOR = new SubCssMetaData<Paint[]>("-fx-border-color", (StyleConverter)LayeredBorderPaintConverter.getInstance());
    static final CssMetaData<Node, BorderStrokeStyle[][]> BORDER_STYLE = new SubCssMetaData<BorderStrokeStyle[][]>("-fx-border-style", (StyleConverter)LayeredBorderStyleConverter.getInstance());
    static final CssMetaData<Node, Margins[]> BORDER_WIDTH = new SubCssMetaData<Margins[]>("-fx-border-width", (StyleConverter)Margins.SequenceConverter.getInstance());
    static final CssMetaData<Node, CornerRadii[]> BORDER_RADIUS = new SubCssMetaData<CornerRadii[]>("-fx-border-radius", (StyleConverter)CornerRadiiConverter.getInstance());
    static final CssMetaData<Node, Insets[]> BORDER_INSETS = new SubCssMetaData<Insets[]>("-fx-border-insets", (StyleConverter)InsetsConverter.SequenceConverter.getInstance());
    static final CssMetaData<Node, String[]> BORDER_IMAGE_SOURCE = new SubCssMetaData<String[]>("-fx-border-image-source", (StyleConverter)URLConverter.SequenceConverter.getInstance());
    static final CssMetaData<Node, RepeatStruct[]> BORDER_IMAGE_REPEAT = new SubCssMetaData<RepeatStruct[]>("-fx-border-image-repeat", (StyleConverter)RepeatStructConverter.getInstance(), new RepeatStruct[]{new RepeatStruct(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT)});
    static final CssMetaData<Node, BorderImageSlices[]> BORDER_IMAGE_SLICE = new SubCssMetaData<BorderImageSlices[]>("-fx-border-image-slice", (StyleConverter)SliceSequenceConverter.getInstance(), new BorderImageSlices[]{BorderImageSlices.DEFAULT});
    static final CssMetaData<Node, BorderWidths[]> BORDER_IMAGE_WIDTH = new SubCssMetaData<BorderWidths[]>("-fx-border-image-width", (StyleConverter)BorderImageWidthConverter.getInstance(), new BorderWidths[]{BorderWidths.DEFAULT});
    static final CssMetaData<Node, Insets[]> BORDER_IMAGE_INSETS = new SubCssMetaData<Insets[]>("-fx-border-image-insets", (StyleConverter)InsetsConverter.SequenceConverter.getInstance(), new Insets[]{Insets.EMPTY});
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES = Collections.unmodifiableList(Arrays.asList(BORDER_COLOR, BORDER_STYLE, BORDER_WIDTH, BORDER_RADIUS, BORDER_INSETS, BORDER_IMAGE_SOURCE, BORDER_IMAGE_REPEAT, BORDER_IMAGE_SLICE, BORDER_IMAGE_WIDTH, BORDER_IMAGE_INSETS));
    public static final Border EMPTY = new Border((BorderStroke[])null, null);
    final List<BorderStroke> strokes;
    final List<BorderImage> images;
    final Insets outsets;
    final Insets insets;
    private final int hash;

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public final List<BorderStroke> getStrokes() {
        return this.strokes;
    }

    public final List<BorderImage> getImages() {
        return this.images;
    }

    public final Insets getOutsets() {
        return this.outsets;
    }

    public final Insets getInsets() {
        return this.insets;
    }

    public final boolean isEmpty() {
        return this.strokes.isEmpty() && this.images.isEmpty();
    }

    public Border(BorderStroke ... arrborderStroke) {
        this(arrborderStroke, (BorderImage[])null);
    }

    public Border(BorderImage ... arrborderImage) {
        this((BorderStroke[])null, arrborderImage);
    }

    public Border(@NamedArg(value="strokes") List<BorderStroke> list, @NamedArg(value="images") List<BorderImage> list2) {
        this(list == null ? null : list.toArray(new BorderStroke[list.size()]), list2 == null ? null : list2.toArray(new BorderImage[list2.size()]));
    }

    public Border(@NamedArg(value="strokes") BorderStroke[] arrborderStroke, @NamedArg(value="images") BorderImage[] arrborderImage) {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        Object object;
        int n2;
        int n3;
        Object[] arrobject;
        double d10 = 0.0;
        double d11 = 0.0;
        double d12 = 0.0;
        double d13 = 0.0;
        double d14 = 0.0;
        double d15 = 0.0;
        double d16 = 0.0;
        double d17 = 0.0;
        if (arrborderStroke == null || arrborderStroke.length == 0) {
            this.strokes = Collections.emptyList();
        } else {
            arrobject = new BorderStroke[arrborderStroke.length];
            n3 = 0;
            for (n2 = 0; n2 < arrborderStroke.length; ++n2) {
                object = arrborderStroke[n2];
                if (object == null) continue;
                arrobject[n3++] = object;
                d9 = ((BorderStroke)object).innerEdge.getTop();
                d8 = ((BorderStroke)object).innerEdge.getRight();
                d7 = ((BorderStroke)object).innerEdge.getBottom();
                d6 = ((BorderStroke)object).innerEdge.getLeft();
                d10 = d10 >= d9 ? d10 : d9;
                d11 = d11 >= d8 ? d11 : d8;
                d12 = d12 >= d7 ? d12 : d7;
                d13 = d13 >= d6 ? d13 : d6;
                d5 = ((BorderStroke)object).outerEdge.getTop();
                d4 = ((BorderStroke)object).outerEdge.getRight();
                d3 = ((BorderStroke)object).outerEdge.getBottom();
                d2 = ((BorderStroke)object).outerEdge.getLeft();
                d14 = d14 >= d5 ? d14 : d5;
                d15 = d15 >= d4 ? d15 : d4;
                d16 = d16 >= d3 ? d16 : d3;
                d17 = d17 >= d2 ? d17 : d2;
            }
            this.strokes = new UnmodifiableArrayList<Object>(arrobject, n3);
        }
        if (arrborderImage == null || arrborderImage.length == 0) {
            this.images = Collections.emptyList();
        } else {
            arrobject = new BorderImage[arrborderImage.length];
            n3 = 0;
            for (n2 = 0; n2 < arrborderImage.length; ++n2) {
                object = arrborderImage[n2];
                if (object == null) continue;
                arrobject[n3++] = object;
                d9 = ((BorderImage)object).innerEdge.getTop();
                d8 = ((BorderImage)object).innerEdge.getRight();
                d7 = ((BorderImage)object).innerEdge.getBottom();
                d6 = ((BorderImage)object).innerEdge.getLeft();
                d10 = d10 >= d9 ? d10 : d9;
                d11 = d11 >= d8 ? d11 : d8;
                d12 = d12 >= d7 ? d12 : d7;
                d13 = d13 >= d6 ? d13 : d6;
                d5 = ((BorderImage)object).outerEdge.getTop();
                d4 = ((BorderImage)object).outerEdge.getRight();
                d3 = ((BorderImage)object).outerEdge.getBottom();
                d2 = ((BorderImage)object).outerEdge.getLeft();
                d14 = d14 >= d5 ? d14 : d5;
                d15 = d15 >= d4 ? d15 : d4;
                d16 = d16 >= d3 ? d16 : d3;
                d17 = d17 >= d2 ? d17 : d2;
            }
            this.images = new UnmodifiableArrayList<Object>(arrobject, n3);
        }
        this.outsets = new Insets(d14, d15, d16, d17);
        this.insets = new Insets(d10, d11, d12, d13);
        int n4 = this.strokes.hashCode();
        this.hash = n4 = 31 * n4 + this.images.hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Border border = (Border)object;
        if (this.hash != border.hash) {
            return false;
        }
        if (!this.images.equals(border.images)) {
            return false;
        }
        return this.strokes.equals(border.strokes);
    }

    public int hashCode() {
        return this.hash;
    }
}

