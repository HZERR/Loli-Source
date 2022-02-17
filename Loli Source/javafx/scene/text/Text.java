/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.text;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.TransformedShape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.scene.text.TextLine;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;

@DefaultProperty(value="text")
public class Text
extends javafx.scene.shape.Shape {
    private TextLayout layout;
    private static final PathElement[] EMPTY_PATH_ELEMENT_ARRAY = new PathElement[0];
    private boolean isSpan;
    private TextSpan textSpan;
    private GlyphList[] textRuns = null;
    private BaseBounds spanBounds = new RectBounds();
    private boolean spanBoundsInvalid = true;
    private StringProperty text;
    private DoubleProperty x;
    private DoubleProperty y;
    private ObjectProperty<Font> font;
    private ObjectProperty<TextBoundsType> boundsType;
    private DoubleProperty wrappingWidth;
    private ObjectProperty<FontSmoothingType> fontSmoothingType;
    private TextAttribute attributes;
    private static final VPos DEFAULT_TEXT_ORIGIN = VPos.BASELINE;
    private static final TextBoundsType DEFAULT_BOUNDS_TYPE = TextBoundsType.LOGICAL;
    private static final boolean DEFAULT_UNDERLINE = false;
    private static final boolean DEFAULT_STRIKETHROUGH = false;
    private static final TextAlignment DEFAULT_TEXT_ALIGNMENT = TextAlignment.LEFT;
    private static final double DEFAULT_LINE_SPACING = 0.0;
    private static final int DEFAULT_CARET_POSITION = -1;
    private static final int DEFAULT_SELECTION_START = -1;
    private static final int DEFAULT_SELECTION_END = -1;
    private static final Color DEFAULT_SELECTION_FILL = Color.WHITE;
    private static final boolean DEFAULT_CARET_BIAS = true;

    @Override
    @Deprecated
    protected final NGNode impl_createPeer() {
        return new NGText();
    }

    public Text() {
        this.setAccessibleRole(AccessibleRole.TEXT);
        InvalidationListener invalidationListener = observable -> this.checkSpan();
        this.parentProperty().addListener(invalidationListener);
        this.managedProperty().addListener(invalidationListener);
        this.effectiveNodeOrientationProperty().addListener(observable -> this.checkOrientation());
        this.setPickOnBounds(true);
    }

    public Text(String string) {
        this();
        this.setText(string);
    }

    public Text(double d2, double d3, String string) {
        this(string);
        this.setX(d2);
        this.setY(d3);
    }

    private boolean isSpan() {
        return this.isSpan;
    }

    private void checkSpan() {
        boolean bl = this.isSpan = this.isManaged() && this.getParent() instanceof TextFlow;
        if (this.isSpan() && !this.pickOnBoundsProperty().isBound()) {
            this.setPickOnBounds(false);
        }
    }

    private void checkOrientation() {
        if (!this.isSpan()) {
            NodeOrientation nodeOrientation = this.getEffectiveNodeOrientation();
            boolean bl = nodeOrientation == NodeOrientation.RIGHT_TO_LEFT;
            int n2 = bl ? 2048 : 1024;
            TextLayout textLayout = this.getTextLayout();
            if (textLayout.setDirection(n2)) {
                this.needsTextLayout();
            }
        }
    }

    @Override
    public boolean usesMirroring() {
        return false;
    }

    private void needsFullTextLayout() {
        if (this.isSpan()) {
            this.textSpan = null;
        } else {
            TextLayout textLayout = this.getTextLayout();
            String string = this.getTextInternal();
            Object object = this.getFontInternal();
            textLayout.setContent(string, object);
        }
        this.needsTextLayout();
    }

    private void needsTextLayout() {
        this.textRuns = null;
        this.impl_geomChanged();
        this.impl_markDirty(DirtyBits.NODE_CONTENTS);
    }

    TextSpan getTextSpan() {
        if (this.textSpan == null) {
            this.textSpan = new TextSpan(){

                @Override
                public String getText() {
                    return Text.this.getTextInternal();
                }

                @Override
                public Object getFont() {
                    return Text.this.getFontInternal();
                }

                @Override
                public RectBounds getBounds() {
                    return null;
                }
            };
        }
        return this.textSpan;
    }

    private TextLayout getTextLayout() {
        if (this.isSpan()) {
            this.layout = null;
            TextFlow textFlow = (TextFlow)this.getParent();
            return textFlow.getTextLayout();
        }
        if (this.layout == null) {
            TextLayoutFactory textLayoutFactory = Toolkit.getToolkit().getTextLayoutFactory();
            this.layout = textLayoutFactory.createLayout();
            String string = this.getTextInternal();
            Object object = this.getFontInternal();
            TextAlignment textAlignment = this.getTextAlignment();
            if (textAlignment == null) {
                textAlignment = DEFAULT_TEXT_ALIGNMENT;
            }
            this.layout.setContent(string, object);
            this.layout.setAlignment(textAlignment.ordinal());
            this.layout.setLineSpacing((float)this.getLineSpacing());
            this.layout.setWrapWidth((float)this.getWrappingWidth());
            if (this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                this.layout.setDirection(2048);
            } else {
                this.layout.setDirection(1024);
            }
        }
        return this.layout;
    }

    void layoutSpan(GlyphList[] arrglyphList) {
        GlyphList glyphList;
        int n2;
        TextSpan textSpan = this.getTextSpan();
        int n3 = 0;
        for (n2 = 0; n2 < arrglyphList.length; ++n2) {
            glyphList = arrglyphList[n2];
            if (glyphList.getTextSpan() != textSpan) continue;
            ++n3;
        }
        this.textRuns = new GlyphList[n3];
        n3 = 0;
        for (n2 = 0; n2 < arrglyphList.length; ++n2) {
            glyphList = arrglyphList[n2];
            if (glyphList.getTextSpan() != textSpan) continue;
            this.textRuns[n3++] = glyphList;
        }
        this.spanBoundsInvalid = true;
        this.impl_geomChanged();
        this.impl_markDirty(DirtyBits.NODE_CONTENTS);
    }

    BaseBounds getSpanBounds() {
        if (this.spanBoundsInvalid) {
            GlyphList[] arrglyphList = this.getRuns();
            if (arrglyphList.length != 0) {
                float f2 = Float.POSITIVE_INFINITY;
                float f3 = Float.POSITIVE_INFINITY;
                float f4 = 0.0f;
                float f5 = 0.0f;
                for (int i2 = 0; i2 < arrglyphList.length; ++i2) {
                    GlyphList glyphList = arrglyphList[i2];
                    com.sun.javafx.geom.Point2D point2D = glyphList.getLocation();
                    float f6 = glyphList.getWidth();
                    float f7 = glyphList.getLineBounds().getHeight();
                    f2 = Math.min(point2D.x, f2);
                    f3 = Math.min(point2D.y, f3);
                    f4 = Math.max(point2D.x + f6, f4);
                    f5 = Math.max(point2D.y + f7, f5);
                }
                this.spanBounds = this.spanBounds.deriveWithNewBounds(f2, f3, 0.0f, f4, f5, 0.0f);
            } else {
                this.spanBounds = this.spanBounds.makeEmpty();
            }
            this.spanBoundsInvalid = false;
        }
        return this.spanBounds;
    }

    private GlyphList[] getRuns() {
        if (this.textRuns != null) {
            return this.textRuns;
        }
        if (this.isSpan()) {
            this.getParent().layout();
        } else {
            TextLayout textLayout = this.getTextLayout();
            this.textRuns = textLayout.getRuns();
        }
        return this.textRuns;
    }

    private Shape getShape() {
        TextLayout textLayout = this.getTextLayout();
        int n2 = 1;
        if (this.isStrikethrough()) {
            n2 |= 4;
        }
        if (this.isUnderline()) {
            n2 |= 2;
        }
        TextSpan textSpan = null;
        if (this.isSpan()) {
            n2 |= 0x10;
            textSpan = this.getTextSpan();
        } else {
            n2 |= 8;
        }
        return textLayout.getShape(n2, textSpan);
    }

    private BaseBounds getVisualBounds() {
        if (this.impl_mode == NGShape.Mode.FILL || this.getStrokeType() == StrokeType.INSIDE) {
            int n2 = 1;
            if (this.isStrikethrough()) {
                n2 |= 4;
            }
            if (this.isUnderline()) {
                n2 |= 2;
            }
            return this.getTextLayout().getVisualBounds(n2);
        }
        return this.getShape().getBounds();
    }

    private BaseBounds getLogicalBounds() {
        TextLayout textLayout = this.getTextLayout();
        return textLayout.getBounds();
    }

    public final void setText(String string) {
        if (string == null) {
            string = "";
        }
        this.textProperty().set(string);
    }

    public final String getText() {
        return this.text == null ? "" : (String)this.text.get();
    }

    private String getTextInternal() {
        String string = this.getText();
        return string == null ? "" : string;
    }

    public final StringProperty textProperty() {
        if (this.text == null) {
            this.text = new StringPropertyBase(""){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "text";
                }

                @Override
                public void invalidated() {
                    Text.this.needsFullTextLayout();
                    Text.this.setImpl_selectionStart(-1);
                    Text.this.setImpl_selectionEnd(-1);
                    Text.this.setImpl_caretPosition(-1);
                    Text.this.setImpl_caretBias(true);
                    String string = this.get();
                    if (string == null && !this.isBound()) {
                        this.set("");
                    }
                    Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
                }
            };
        }
        return this.text;
    }

    public final void setX(double d2) {
        this.xProperty().set(d2);
    }

    public final double getX() {
        return this.x == null ? 0.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "x";
                }

                @Override
                public void invalidated() {
                    Text.this.impl_geomChanged();
                }
            };
        }
        return this.x;
    }

    public final void setY(double d2) {
        this.yProperty().set(d2);
    }

    public final double getY() {
        return this.y == null ? 0.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "y";
                }

                @Override
                public void invalidated() {
                    Text.this.impl_geomChanged();
                }
            };
        }
        return this.y;
    }

    public final void setFont(Font font) {
        this.fontProperty().set(font);
    }

    public final Font getFont() {
        return this.font == null ? Font.getDefault() : (Font)this.font.get();
    }

    private Object getFontInternal() {
        Font font = this.getFont();
        if (font == null) {
            font = Font.getDefault();
        }
        return font.impl_getNativeFont();
    }

    public final ObjectProperty<Font> fontProperty() {
        if (this.font == null) {
            this.font = new StyleableObjectProperty<Font>(Font.getDefault()){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "font";
                }

                @Override
                public CssMetaData<Text, Font> getCssMetaData() {
                    return StyleableProperties.FONT;
                }

                @Override
                public void invalidated() {
                    Text.this.needsFullTextLayout();
                    Text.this.impl_markDirty(DirtyBits.TEXT_FONT);
                }
            };
        }
        return this.font;
    }

    public final void setTextOrigin(VPos vPos) {
        this.textOriginProperty().set(vPos);
    }

    public final VPos getTextOrigin() {
        if (this.attributes == null || this.attributes.textOrigin == null) {
            return DEFAULT_TEXT_ORIGIN;
        }
        return this.attributes.getTextOrigin();
    }

    public final ObjectProperty<VPos> textOriginProperty() {
        return this.getTextAttribute().textOriginProperty();
    }

    public final void setBoundsType(TextBoundsType textBoundsType) {
        this.boundsTypeProperty().set(textBoundsType);
    }

    public final TextBoundsType getBoundsType() {
        return this.boundsType == null ? DEFAULT_BOUNDS_TYPE : (TextBoundsType)((Object)this.boundsTypeProperty().get());
    }

    public final ObjectProperty<TextBoundsType> boundsTypeProperty() {
        if (this.boundsType == null) {
            this.boundsType = new StyleableObjectProperty<TextBoundsType>(DEFAULT_BOUNDS_TYPE){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "boundsType";
                }

                @Override
                public CssMetaData<Text, TextBoundsType> getCssMetaData() {
                    return StyleableProperties.BOUNDS_TYPE;
                }

                @Override
                public void invalidated() {
                    TextLayout textLayout = Text.this.getTextLayout();
                    int n2 = 0;
                    if (Text.this.boundsType.get() == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
                        n2 |= 0x4000;
                    }
                    if (textLayout.setBoundsType(n2)) {
                        Text.this.needsTextLayout();
                    } else {
                        Text.this.impl_geomChanged();
                    }
                }
            };
        }
        return this.boundsType;
    }

    public final void setWrappingWidth(double d2) {
        this.wrappingWidthProperty().set(d2);
    }

    public final double getWrappingWidth() {
        return this.wrappingWidth == null ? 0.0 : this.wrappingWidth.get();
    }

    public final DoubleProperty wrappingWidthProperty() {
        if (this.wrappingWidth == null) {
            this.wrappingWidth = new DoublePropertyBase(){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "wrappingWidth";
                }

                @Override
                public void invalidated() {
                    if (!Text.this.isSpan()) {
                        TextLayout textLayout = Text.this.getTextLayout();
                        if (textLayout.setWrapWidth((float)this.get())) {
                            Text.this.needsTextLayout();
                        } else {
                            Text.this.impl_geomChanged();
                        }
                    }
                }
            };
        }
        return this.wrappingWidth;
    }

    public final void setUnderline(boolean bl) {
        this.underlineProperty().set(bl);
    }

    public final boolean isUnderline() {
        if (this.attributes == null || this.attributes.underline == null) {
            return false;
        }
        return this.attributes.isUnderline();
    }

    public final BooleanProperty underlineProperty() {
        return this.getTextAttribute().underlineProperty();
    }

    public final void setStrikethrough(boolean bl) {
        this.strikethroughProperty().set(bl);
    }

    public final boolean isStrikethrough() {
        if (this.attributes == null || this.attributes.strikethrough == null) {
            return false;
        }
        return this.attributes.isStrikethrough();
    }

    public final BooleanProperty strikethroughProperty() {
        return this.getTextAttribute().strikethroughProperty();
    }

    public final void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignmentProperty().set(textAlignment);
    }

    public final TextAlignment getTextAlignment() {
        if (this.attributes == null || this.attributes.textAlignment == null) {
            return DEFAULT_TEXT_ALIGNMENT;
        }
        return this.attributes.getTextAlignment();
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        return this.getTextAttribute().textAlignmentProperty();
    }

    public final void setLineSpacing(double d2) {
        this.lineSpacingProperty().set(d2);
    }

    public final double getLineSpacing() {
        if (this.attributes == null || this.attributes.lineSpacing == null) {
            return 0.0;
        }
        return this.attributes.getLineSpacing();
    }

    public final DoubleProperty lineSpacingProperty() {
        return this.getTextAttribute().lineSpacingProperty();
    }

    @Override
    public final double getBaselineOffset() {
        return this.baselineOffsetProperty().get();
    }

    public final ReadOnlyDoubleProperty baselineOffsetProperty() {
        return this.getTextAttribute().baselineOffsetProperty();
    }

    public final void setFontSmoothingType(FontSmoothingType fontSmoothingType) {
        this.fontSmoothingTypeProperty().set(fontSmoothingType);
    }

    public final FontSmoothingType getFontSmoothingType() {
        return this.fontSmoothingType == null ? FontSmoothingType.GRAY : (FontSmoothingType)((Object)this.fontSmoothingType.get());
    }

    public final ObjectProperty<FontSmoothingType> fontSmoothingTypeProperty() {
        if (this.fontSmoothingType == null) {
            this.fontSmoothingType = new StyleableObjectProperty<FontSmoothingType>(FontSmoothingType.GRAY){

                @Override
                public Object getBean() {
                    return Text.this;
                }

                @Override
                public String getName() {
                    return "fontSmoothingType";
                }

                @Override
                public CssMetaData<Text, FontSmoothingType> getCssMetaData() {
                    return StyleableProperties.FONT_SMOOTHING_TYPE;
                }

                @Override
                public void invalidated() {
                    Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                    Text.this.impl_geomChanged();
                }
            };
        }
        return this.fontSmoothingType;
    }

    @Override
    @Deprecated
    protected final void impl_geomChanged() {
        super.impl_geomChanged();
        if (this.attributes != null) {
            if (this.attributes.impl_caretBinding != null) {
                this.attributes.impl_caretBinding.invalidate();
            }
            if (this.attributes.impl_selectionBinding != null) {
                this.attributes.impl_selectionBinding.invalidate();
            }
        }
        this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
    }

    @Deprecated
    public final PathElement[] getImpl_selectionShape() {
        return (PathElement[])this.impl_selectionShapeProperty().get();
    }

    @Deprecated
    public final ReadOnlyObjectProperty<PathElement[]> impl_selectionShapeProperty() {
        return this.getTextAttribute().impl_selectionShapeProperty();
    }

    @Deprecated
    public final void setImpl_selectionStart(int n2) {
        if (n2 == -1 && (this.attributes == null || this.attributes.impl_selectionStart == null)) {
            return;
        }
        this.impl_selectionStartProperty().set(n2);
    }

    @Deprecated
    public final int getImpl_selectionStart() {
        if (this.attributes == null || this.attributes.impl_selectionStart == null) {
            return -1;
        }
        return this.attributes.getImpl_selectionStart();
    }

    @Deprecated
    public final IntegerProperty impl_selectionStartProperty() {
        return this.getTextAttribute().impl_selectionStartProperty();
    }

    @Deprecated
    public final void setImpl_selectionEnd(int n2) {
        if (n2 == -1 && (this.attributes == null || this.attributes.impl_selectionEnd == null)) {
            return;
        }
        this.impl_selectionEndProperty().set(n2);
    }

    @Deprecated
    public final int getImpl_selectionEnd() {
        if (this.attributes == null || this.attributes.impl_selectionEnd == null) {
            return -1;
        }
        return this.attributes.getImpl_selectionEnd();
    }

    @Deprecated
    public final IntegerProperty impl_selectionEndProperty() {
        return this.getTextAttribute().impl_selectionEndProperty();
    }

    @Deprecated
    public final ObjectProperty<Paint> impl_selectionFillProperty() {
        return this.getTextAttribute().impl_selectionFillProperty();
    }

    @Deprecated
    public final PathElement[] getImpl_caretShape() {
        return (PathElement[])this.impl_caretShapeProperty().get();
    }

    @Deprecated
    public final ReadOnlyObjectProperty<PathElement[]> impl_caretShapeProperty() {
        return this.getTextAttribute().impl_caretShapeProperty();
    }

    @Deprecated
    public final void setImpl_caretPosition(int n2) {
        if (n2 == -1 && (this.attributes == null || this.attributes.impl_caretPosition == null)) {
            return;
        }
        this.impl_caretPositionProperty().set(n2);
    }

    @Deprecated
    public final int getImpl_caretPosition() {
        if (this.attributes == null || this.attributes.impl_caretPosition == null) {
            return -1;
        }
        return this.attributes.getImpl_caretPosition();
    }

    @Deprecated
    public final IntegerProperty impl_caretPositionProperty() {
        return this.getTextAttribute().impl_caretPositionProperty();
    }

    @Deprecated
    public final void setImpl_caretBias(boolean bl) {
        if (bl && (this.attributes == null || this.attributes.impl_caretBias == null)) {
            return;
        }
        this.impl_caretBiasProperty().set(bl);
    }

    @Deprecated
    public final boolean isImpl_caretBias() {
        if (this.attributes == null || this.attributes.impl_caretBias == null) {
            return true;
        }
        return this.getTextAttribute().isImpl_caretBias();
    }

    @Deprecated
    public final BooleanProperty impl_caretBiasProperty() {
        return this.getTextAttribute().impl_caretBiasProperty();
    }

    @Deprecated
    public final HitInfo impl_hitTestChar(Point2D point2D) {
        if (point2D == null) {
            return null;
        }
        TextLayout textLayout = this.getTextLayout();
        double d2 = point2D.getX() - this.getX();
        double d3 = point2D.getY() - this.getY() + (double)this.getYRendering();
        return textLayout.getHitInfo((float)d2, (float)d3);
    }

    private PathElement[] getRange(int n2, int n3, int n4) {
        int n5 = this.getTextInternal().length();
        if (0 <= n2 && n2 < n3 && n3 <= n5) {
            TextLayout textLayout = this.getTextLayout();
            float f2 = (float)this.getX();
            float f3 = (float)this.getY() - this.getYRendering();
            return textLayout.getRange(n2, n3, n4, f2, f3);
        }
        return EMPTY_PATH_ELEMENT_ARRAY;
    }

    @Deprecated
    public final PathElement[] impl_getRangeShape(int n2, int n3) {
        return this.getRange(n2, n3, 1);
    }

    @Deprecated
    public final PathElement[] impl_getUnderlineShape(int n2, int n3) {
        return this.getRange(n2, n3, 2);
    }

    @Deprecated
    public final void impl_displaySoftwareKeyboard(boolean bl) {
    }

    private float getYAdjustment(BaseBounds baseBounds) {
        VPos vPos = this.getTextOrigin();
        if (vPos == null) {
            vPos = DEFAULT_TEXT_ORIGIN;
        }
        switch (vPos) {
            case TOP: {
                return -baseBounds.getMinY();
            }
            case BASELINE: {
                return 0.0f;
            }
            case CENTER: {
                return -baseBounds.getMinY() - baseBounds.getHeight() / 2.0f;
            }
            case BOTTOM: {
                return -baseBounds.getMinY() - baseBounds.getHeight();
            }
        }
        return 0.0f;
    }

    private float getYRendering() {
        if (this.isSpan()) {
            return 0.0f;
        }
        BaseBounds baseBounds = this.getLogicalBounds();
        VPos vPos = this.getTextOrigin();
        if (vPos == null) {
            vPos = DEFAULT_TEXT_ORIGIN;
        }
        if (this.getBoundsType() == TextBoundsType.VISUAL) {
            BaseBounds baseBounds2 = this.getVisualBounds();
            float f2 = baseBounds2.getMinY() - baseBounds.getMinY();
            switch (vPos) {
                case TOP: {
                    return f2;
                }
                case BASELINE: {
                    return -baseBounds2.getMinY() + f2;
                }
                case CENTER: {
                    return baseBounds2.getHeight() / 2.0f + f2;
                }
                case BOTTOM: {
                    return baseBounds2.getHeight() + f2;
                }
            }
            return 0.0f;
        }
        switch (vPos) {
            case TOP: {
                return 0.0f;
            }
            case BASELINE: {
                return -baseBounds.getMinY();
            }
            case CENTER: {
                return baseBounds.getHeight() / 2.0f;
            }
            case BOTTOM: {
                return baseBounds.getHeight();
            }
        }
        return 0.0f;
    }

    @Override
    @Deprecated
    protected final Bounds impl_computeLayoutBounds() {
        if (this.isSpan()) {
            BaseBounds baseBounds = this.getSpanBounds();
            double d2 = baseBounds.getWidth();
            double d3 = baseBounds.getHeight();
            return new BoundingBox(0.0, 0.0, d2, d3);
        }
        if (this.getBoundsType() == TextBoundsType.VISUAL) {
            return super.impl_computeLayoutBounds();
        }
        BaseBounds baseBounds = this.getLogicalBounds();
        double d4 = (double)baseBounds.getMinX() + this.getX();
        double d5 = (double)baseBounds.getMinY() + this.getY() + (double)this.getYAdjustment(baseBounds);
        double d6 = baseBounds.getWidth();
        double d7 = baseBounds.getHeight();
        double d8 = this.getWrappingWidth();
        if (d8 != 0.0) {
            d6 = d8;
        }
        return new BoundingBox(d4, d5, d6, d7);
    }

    @Override
    @Deprecated
    public final BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        Object object;
        if (this.isSpan()) {
            if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
                return super.impl_computeGeomBounds(baseBounds, baseTransform);
            }
            TextLayout textLayout = this.getTextLayout();
            baseBounds = textLayout.getBounds(this.getTextSpan(), baseBounds);
            BaseBounds baseBounds2 = this.getSpanBounds();
            float f2 = baseBounds.getMinX() - baseBounds2.getMinX();
            float f3 = baseBounds.getMinY() - baseBounds2.getMinY();
            float f4 = f2 + baseBounds.getWidth();
            float f5 = f3 + baseBounds.getHeight();
            baseBounds = baseBounds.deriveWithNewBounds(f2, f3, 0.0f, f4, f5, 0.0f);
            return baseTransform.transform(baseBounds, baseBounds);
        }
        if (this.getBoundsType() == TextBoundsType.VISUAL) {
            if (this.getTextInternal().length() == 0 || this.impl_mode == NGShape.Mode.EMPTY) {
                return baseBounds.makeEmpty();
            }
            if (this.impl_mode == NGShape.Mode.FILL || this.getStrokeType() == StrokeType.INSIDE) {
                BaseBounds baseBounds3 = this.getVisualBounds();
                float f6 = baseBounds3.getMinX() + (float)this.getX();
                float f7 = this.getYAdjustment(baseBounds3);
                float f8 = baseBounds3.getMinY() + f7 + (float)this.getY();
                baseBounds.deriveWithNewBounds(f6, f8, 0.0f, f6 + baseBounds3.getWidth(), f8 + baseBounds3.getHeight(), 0.0f);
                return baseTransform.transform(baseBounds, baseBounds);
            }
            return super.impl_computeGeomBounds(baseBounds, baseTransform);
        }
        BaseBounds baseBounds4 = this.getLogicalBounds();
        float f9 = baseBounds4.getMinX() + (float)this.getX();
        float f10 = this.getYAdjustment(baseBounds4);
        float f11 = baseBounds4.getMinY() + f10 + (float)this.getY();
        float f12 = baseBounds4.getWidth();
        float f13 = baseBounds4.getHeight();
        float f14 = (float)this.getWrappingWidth();
        if (f14 > f12) {
            f12 = f14;
        } else if (f14 > 0.0f && (object = this.getEffectiveNodeOrientation()) == NodeOrientation.RIGHT_TO_LEFT) {
            f9 -= f12 - f14;
        }
        baseBounds4 = new RectBounds(f9, f11, f9 + f12, f11 + f13);
        if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
            baseBounds = super.impl_computeGeomBounds(baseBounds, BaseTransform.IDENTITY_TRANSFORM);
        } else {
            object = this.getTextLayout();
            baseBounds = object.getBounds(null, baseBounds);
            f9 = baseBounds.getMinX() + (float)this.getX();
            f12 = baseBounds.getWidth();
            baseBounds = baseBounds.deriveWithNewBounds(f9, f11, 0.0f, f9 + f12, f11 + f13, 0.0f);
        }
        baseBounds = baseBounds.deriveWithUnion(baseBounds4);
        return baseTransform.transform(baseBounds, baseBounds);
    }

    @Override
    @Deprecated
    protected final boolean impl_computeContains(double d2, double d3) {
        double d4 = d2 + (double)this.getSpanBounds().getMinX();
        double d5 = d3 + (double)this.getSpanBounds().getMinY();
        GlyphList[] arrglyphList = this.getRuns();
        if (arrglyphList.length != 0) {
            for (int i2 = 0; i2 < arrglyphList.length; ++i2) {
                GlyphList glyphList = arrglyphList[i2];
                com.sun.javafx.geom.Point2D point2D = glyphList.getLocation();
                float f2 = glyphList.getWidth();
                RectBounds rectBounds = glyphList.getLineBounds();
                float f3 = rectBounds.getHeight();
                if (!((double)point2D.x <= d4) || !(d4 < (double)(point2D.x + f2)) || !((double)point2D.y <= d5) || !(d5 < (double)(point2D.y + f3))) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public final Shape impl_configShape() {
        float f2;
        float f3;
        if (this.impl_mode == NGShape.Mode.EMPTY || this.getTextInternal().length() == 0) {
            return new Path2D();
        }
        Shape shape = this.getShape();
        if (this.isSpan()) {
            BaseBounds baseBounds = this.getSpanBounds();
            f3 = -baseBounds.getMinX();
            f2 = -baseBounds.getMinY();
        } else {
            f3 = (float)this.getX();
            f2 = this.getYAdjustment(this.getVisualBounds()) + (float)this.getY();
        }
        return TransformedShape.translatedShape(shape, f3, f2);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Text.getClassCssMetaData();
    }

    private void updatePGText() {
        Object object;
        NGText nGText = (NGText)this.impl_getPeer();
        if (this.impl_isDirty(DirtyBits.TEXT_ATTRS)) {
            nGText.setUnderline(this.isUnderline());
            nGText.setStrikethrough(this.isStrikethrough());
            object = this.getFontSmoothingType();
            if (object == null) {
                object = FontSmoothingType.GRAY;
            }
            nGText.setFontSmoothingType(((Enum)object).ordinal());
        }
        if (this.impl_isDirty(DirtyBits.TEXT_FONT)) {
            nGText.setFont(this.getFontInternal());
        }
        if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            nGText.setGlyphs(this.getRuns());
        }
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            if (this.isSpan()) {
                object = this.getSpanBounds();
                nGText.setLayoutLocation(((BaseBounds)object).getMinX(), ((BaseBounds)object).getMinY());
            } else {
                float f2 = (float)this.getX();
                float f3 = (float)this.getY();
                float f4 = this.getYRendering();
                nGText.setLayoutLocation(-f2, f4 - f3);
            }
        }
        if (this.impl_isDirty(DirtyBits.TEXT_SELECTION)) {
            Object object2 = null;
            int n2 = this.getImpl_selectionStart();
            int n3 = this.getImpl_selectionEnd();
            int n4 = this.getTextInternal().length();
            if (0 <= n2 && n2 < n3 && n3 <= n4) {
                Paint paint = (Paint)this.impl_selectionFillProperty().get();
                object2 = paint != null ? Toolkit.getPaintAccessor().getPlatformPaint(paint) : null;
            }
            nGText.setSelection(n2, n3, object2);
        }
    }

    @Override
    @Deprecated
    public final void impl_updatePeer() {
        super.impl_updatePeer();
        this.updatePGText();
    }

    private TextAttribute getTextAttribute() {
        if (this.attributes == null) {
            this.attributes = new TextAttribute();
        }
        return this.attributes;
    }

    @Override
    public String toString() {
        double d2;
        StringBuilder stringBuilder = new StringBuilder("Text[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("text=\"").append(this.getText()).append("\"");
        stringBuilder.append(", x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", alignment=").append((Object)this.getTextAlignment());
        stringBuilder.append(", origin=").append((Object)this.getTextOrigin());
        stringBuilder.append(", boundsType=").append((Object)this.getBoundsType());
        double d3 = this.getLineSpacing();
        if (d3 != 0.0) {
            stringBuilder.append(", lineSpacing=").append(d3);
        }
        if ((d2 = this.getWrappingWidth()) != 0.0) {
            stringBuilder.append(", wrappingWidth=").append(d2);
        }
        stringBuilder.append(", font=").append(this.getFont());
        stringBuilder.append(", fontSmoothingType=").append((Object)this.getFontSmoothingType());
        if (this.isStrikethrough()) {
            stringBuilder.append(", strikethrough");
        }
        if (this.isUnderline()) {
            stringBuilder.append(", underline");
        }
        stringBuilder.append(", fill=").append(this.getFill());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case TEXT: {
                String string = this.getAccessibleText();
                if (string != null && !string.isEmpty()) {
                    return string;
                }
                return this.getText();
            }
            case FONT: {
                return this.getFont();
            }
            case CARET_OFFSET: {
                int n2 = this.getImpl_caretPosition();
                if (n2 >= 0) {
                    return n2;
                }
                return this.getText().length();
            }
            case SELECTION_START: {
                int n3 = this.getImpl_selectionStart();
                if (n3 >= 0) {
                    return n3;
                }
                n3 = this.getImpl_caretPosition();
                if (n3 >= 0) {
                    return n3;
                }
                return this.getText().length();
            }
            case SELECTION_END: {
                int n4 = this.getImpl_selectionEnd();
                if (n4 >= 0) {
                    return n4;
                }
                n4 = this.getImpl_caretPosition();
                if (n4 >= 0) {
                    return n4;
                }
                return this.getText().length();
            }
            case LINE_FOR_OFFSET: {
                TextLine textLine;
                int n5 = (Integer)arrobject[0];
                if (n5 > this.getTextInternal().length()) {
                    return null;
                }
                TextLine[] arrtextLine = this.getTextLayout().getLines();
                int n6 = 0;
                for (int i2 = 1; i2 < arrtextLine.length && (textLine = arrtextLine[i2]).getStart() <= n5; ++i2) {
                    ++n6;
                }
                return n6;
            }
            case LINE_START: {
                int n7 = (Integer)arrobject[0];
                TextLine[] arrtextLine = this.getTextLayout().getLines();
                if (0 <= n7 && n7 < arrtextLine.length) {
                    TextLine textLine = arrtextLine[n7];
                    return textLine.getStart();
                }
                return null;
            }
            case LINE_END: {
                int n8 = (Integer)arrobject[0];
                TextLine[] arrtextLine = this.getTextLayout().getLines();
                if (0 <= n8 && n8 < arrtextLine.length) {
                    TextLine textLine = arrtextLine[n8];
                    return textLine.getStart() + textLine.getLength();
                }
                return null;
            }
            case OFFSET_AT_POINT: {
                Point2D point2D = (Point2D)arrobject[0];
                point2D = this.screenToLocal(point2D);
                return this.impl_hitTestChar(point2D).getCharIndex();
            }
            case BOUNDS_FOR_RANGE: {
                int n9 = (Integer)arrobject[0];
                int n10 = (Integer)arrobject[1];
                PathElement[] arrpathElement = this.impl_getRangeShape(n9, n10 + 1);
                Bounds[] arrbounds = new Bounds[arrpathElement.length / 5];
                int n11 = 0;
                for (int i3 = 0; i3 < arrbounds.length; ++i3) {
                    MoveTo moveTo = (MoveTo)arrpathElement[n11];
                    LineTo lineTo = (LineTo)arrpathElement[n11 + 1];
                    LineTo lineTo2 = (LineTo)arrpathElement[n11 + 2];
                    BoundingBox boundingBox = new BoundingBox(moveTo.getX(), moveTo.getY(), lineTo.getX() - moveTo.getX(), lineTo2.getY() - lineTo.getY());
                    arrbounds[i3] = this.localToScreen(boundingBox);
                    n11 += 5;
                }
                return arrbounds;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    static /* synthetic */ TextBoundsType access$2600() {
        return DEFAULT_BOUNDS_TYPE;
    }

    private final class TextAttribute {
        private ObjectProperty<VPos> textOrigin;
        private BooleanProperty underline;
        private BooleanProperty strikethrough;
        private ObjectProperty<TextAlignment> textAlignment;
        private DoubleProperty lineSpacing;
        private ReadOnlyDoubleWrapper baselineOffset;
        @Deprecated
        private ObjectProperty<PathElement[]> impl_selectionShape;
        private ObjectBinding<PathElement[]> impl_selectionBinding;
        private ObjectProperty<Paint> selectionFill;
        @Deprecated
        private IntegerProperty impl_selectionStart;
        @Deprecated
        private IntegerProperty impl_selectionEnd;
        @Deprecated
        private ObjectProperty<PathElement[]> impl_caretShape;
        private ObjectBinding<PathElement[]> impl_caretBinding;
        @Deprecated
        private IntegerProperty impl_caretPosition;
        @Deprecated
        private BooleanProperty impl_caretBias;

        private TextAttribute() {
        }

        public final VPos getTextOrigin() {
            return this.textOrigin == null ? DEFAULT_TEXT_ORIGIN : (VPos)((Object)this.textOrigin.get());
        }

        public final ObjectProperty<VPos> textOriginProperty() {
            if (this.textOrigin == null) {
                this.textOrigin = new StyleableObjectProperty<VPos>(DEFAULT_TEXT_ORIGIN){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "textOrigin";
                    }

                    @Override
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.TEXT_ORIGIN;
                    }

                    @Override
                    public void invalidated() {
                        Text.this.impl_geomChanged();
                    }
                };
            }
            return this.textOrigin;
        }

        public final boolean isUnderline() {
            return this.underline == null ? false : this.underline.get();
        }

        public final BooleanProperty underlineProperty() {
            if (this.underline == null) {
                this.underline = new StyleableBooleanProperty(){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "underline";
                    }

                    @Override
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.UNDERLINE;
                    }

                    @Override
                    public void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                        if (Text.this.getBoundsType() == TextBoundsType.VISUAL) {
                            Text.this.impl_geomChanged();
                        }
                    }
                };
            }
            return this.underline;
        }

        public final boolean isStrikethrough() {
            return this.strikethrough == null ? false : this.strikethrough.get();
        }

        public final BooleanProperty strikethroughProperty() {
            if (this.strikethrough == null) {
                this.strikethrough = new StyleableBooleanProperty(){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "strikethrough";
                    }

                    @Override
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.STRIKETHROUGH;
                    }

                    @Override
                    public void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                        if (Text.this.getBoundsType() == TextBoundsType.VISUAL) {
                            Text.this.impl_geomChanged();
                        }
                    }
                };
            }
            return this.strikethrough;
        }

        public final TextAlignment getTextAlignment() {
            return this.textAlignment == null ? DEFAULT_TEXT_ALIGNMENT : (TextAlignment)((Object)this.textAlignment.get());
        }

        public final ObjectProperty<TextAlignment> textAlignmentProperty() {
            if (this.textAlignment == null) {
                this.textAlignment = new StyleableObjectProperty<TextAlignment>(DEFAULT_TEXT_ALIGNMENT){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "textAlignment";
                    }

                    @Override
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.TEXT_ALIGNMENT;
                    }

                    @Override
                    public void invalidated() {
                        if (!Text.this.isSpan()) {
                            TextLayout textLayout;
                            TextAlignment textAlignment = (TextAlignment)((Object)this.get());
                            if (textAlignment == null) {
                                textAlignment = DEFAULT_TEXT_ALIGNMENT;
                            }
                            if ((textLayout = Text.this.getTextLayout()).setAlignment(textAlignment.ordinal())) {
                                Text.this.needsTextLayout();
                            }
                        }
                    }
                };
            }
            return this.textAlignment;
        }

        public final double getLineSpacing() {
            return this.lineSpacing == null ? 0.0 : this.lineSpacing.get();
        }

        public final DoubleProperty lineSpacingProperty() {
            if (this.lineSpacing == null) {
                this.lineSpacing = new StyleableDoubleProperty(0.0){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "lineSpacing";
                    }

                    @Override
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.LINE_SPACING;
                    }

                    @Override
                    public void invalidated() {
                        TextLayout textLayout;
                        if (!Text.this.isSpan() && (textLayout = Text.this.getTextLayout()).setLineSpacing((float)this.get())) {
                            Text.this.needsTextLayout();
                        }
                    }
                };
            }
            return this.lineSpacing;
        }

        public final ReadOnlyDoubleProperty baselineOffsetProperty() {
            if (this.baselineOffset == null) {
                this.baselineOffset = new ReadOnlyDoubleWrapper(Text.this, "baselineOffset"){
                    {
                        this.bind(new DoubleBinding(){
                            {
                                this.bind(Text.this.fontProperty());
                            }

                            @Override
                            protected double computeValue() {
                                BaseBounds baseBounds = Text.this.getLogicalBounds();
                                return -baseBounds.getMinY();
                            }
                        });
                    }
                };
            }
            return this.baselineOffset.getReadOnlyProperty();
        }

        @Deprecated
        public final ReadOnlyObjectProperty<PathElement[]> impl_selectionShapeProperty() {
            if (this.impl_selectionShape == null) {
                this.impl_selectionBinding = new ObjectBinding<PathElement[]>(){
                    {
                        this.bind(TextAttribute.this.impl_selectionStartProperty(), TextAttribute.this.impl_selectionEndProperty());
                    }

                    @Override
                    protected PathElement[] computeValue() {
                        int n2 = TextAttribute.this.getImpl_selectionStart();
                        int n3 = TextAttribute.this.getImpl_selectionEnd();
                        return Text.this.getRange(n2, n3, 1);
                    }
                };
                this.impl_selectionShape = new SimpleObjectProperty<PathElement[]>(Text.this, "impl_selectionShape");
                this.impl_selectionShape.bind(this.impl_selectionBinding);
            }
            return this.impl_selectionShape;
        }

        @Deprecated
        public final ObjectProperty<Paint> impl_selectionFillProperty() {
            if (this.selectionFill == null) {
                this.selectionFill = new ObjectPropertyBase<Paint>((Paint)DEFAULT_SELECTION_FILL){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "impl_selectionFill";
                    }

                    @Override
                    protected void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                    }
                };
            }
            return this.selectionFill;
        }

        @Deprecated
        public final int getImpl_selectionStart() {
            return this.impl_selectionStart == null ? -1 : this.impl_selectionStart.get();
        }

        @Deprecated
        public final IntegerProperty impl_selectionStartProperty() {
            if (this.impl_selectionStart == null) {
                this.impl_selectionStart = new IntegerPropertyBase(-1){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "impl_selectionStart";
                    }

                    @Override
                    protected void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                        Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_START);
                    }
                };
            }
            return this.impl_selectionStart;
        }

        @Deprecated
        public final int getImpl_selectionEnd() {
            return this.impl_selectionEnd == null ? -1 : this.impl_selectionEnd.get();
        }

        @Deprecated
        public final IntegerProperty impl_selectionEndProperty() {
            if (this.impl_selectionEnd == null) {
                this.impl_selectionEnd = new IntegerPropertyBase(-1){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "impl_selectionEnd";
                    }

                    @Override
                    protected void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                        Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_END);
                    }
                };
            }
            return this.impl_selectionEnd;
        }

        @Deprecated
        public final ReadOnlyObjectProperty<PathElement[]> impl_caretShapeProperty() {
            if (this.impl_caretShape == null) {
                this.impl_caretBinding = new ObjectBinding<PathElement[]>(){
                    {
                        this.bind(TextAttribute.this.impl_caretPositionProperty(), TextAttribute.this.impl_caretBiasProperty());
                    }

                    @Override
                    protected PathElement[] computeValue() {
                        int n2 = TextAttribute.this.getImpl_caretPosition();
                        int n3 = Text.this.getTextInternal().length();
                        if (0 <= n2 && n2 <= n3) {
                            boolean bl = TextAttribute.this.isImpl_caretBias();
                            float f2 = (float)Text.this.getX();
                            float f3 = (float)Text.this.getY() - Text.this.getYRendering();
                            TextLayout textLayout = Text.this.getTextLayout();
                            return textLayout.getCaretShape(n2, bl, f2, f3);
                        }
                        return EMPTY_PATH_ELEMENT_ARRAY;
                    }
                };
                this.impl_caretShape = new SimpleObjectProperty<PathElement[]>(Text.this, "impl_caretShape");
                this.impl_caretShape.bind(this.impl_caretBinding);
            }
            return this.impl_caretShape;
        }

        @Deprecated
        public final int getImpl_caretPosition() {
            return this.impl_caretPosition == null ? -1 : this.impl_caretPosition.get();
        }

        @Deprecated
        public final IntegerProperty impl_caretPositionProperty() {
            if (this.impl_caretPosition == null) {
                this.impl_caretPosition = new IntegerPropertyBase(-1){

                    @Override
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override
                    public String getName() {
                        return "impl_caretPosition";
                    }

                    @Override
                    protected void invalidated() {
                        Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_END);
                    }
                };
            }
            return this.impl_caretPosition;
        }

        @Deprecated
        public final boolean isImpl_caretBias() {
            return this.impl_caretBias == null ? true : this.impl_caretBias.get();
        }

        @Deprecated
        public final BooleanProperty impl_caretBiasProperty() {
            if (this.impl_caretBias == null) {
                this.impl_caretBias = new SimpleBooleanProperty(Text.this, "impl_caretBias", true);
            }
            return this.impl_caretBias;
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<Text, Font> FONT = new FontCssMetaData<Text>("-fx-font", Font.getDefault()){

            @Override
            public boolean isSettable(Text text) {
                return text.font == null || !text.font.isBound();
            }

            @Override
            public StyleableProperty<Font> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.fontProperty());
            }
        };
        private static final CssMetaData<Text, Boolean> UNDERLINE = new CssMetaData<Text, Boolean>("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE){

            @Override
            public boolean isSettable(Text text) {
                return text.attributes == null || text.attributes.underline == null || !text.attributes.underline.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.underlineProperty());
            }
        };
        private static final CssMetaData<Text, Boolean> STRIKETHROUGH = new CssMetaData<Text, Boolean>("-fx-strikethrough", BooleanConverter.getInstance(), Boolean.FALSE){

            @Override
            public boolean isSettable(Text text) {
                return text.attributes == null || text.attributes.strikethrough == null || !text.attributes.strikethrough.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.strikethroughProperty());
            }
        };
        private static final CssMetaData<Text, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<Text, TextAlignment>("-fx-text-alignment", new EnumConverter<TextAlignment>(TextAlignment.class), TextAlignment.LEFT){

            @Override
            public boolean isSettable(Text text) {
                return text.attributes == null || text.attributes.textAlignment == null || !text.attributes.textAlignment.isBound();
            }

            @Override
            public StyleableProperty<TextAlignment> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.textAlignmentProperty());
            }
        };
        private static final CssMetaData<Text, VPos> TEXT_ORIGIN = new CssMetaData<Text, VPos>("-fx-text-origin", new EnumConverter<VPos>(VPos.class), VPos.BASELINE){

            @Override
            public boolean isSettable(Text text) {
                return text.attributes == null || text.attributes.textOrigin == null || !text.attributes.textOrigin.isBound();
            }

            @Override
            public StyleableProperty<VPos> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.textOriginProperty());
            }
        };
        private static final CssMetaData<Text, FontSmoothingType> FONT_SMOOTHING_TYPE = new CssMetaData<Text, FontSmoothingType>("-fx-font-smoothing-type", new EnumConverter<FontSmoothingType>(FontSmoothingType.class), FontSmoothingType.GRAY){

            @Override
            public boolean isSettable(Text text) {
                return text.fontSmoothingType == null || !text.fontSmoothingType.isBound();
            }

            @Override
            public StyleableProperty<FontSmoothingType> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.fontSmoothingTypeProperty());
            }
        };
        private static final CssMetaData<Text, Number> LINE_SPACING = new CssMetaData<Text, Number>("-fx-line-spacing", SizeConverter.getInstance(), (Number)0){

            @Override
            public boolean isSettable(Text text) {
                return text.attributes == null || text.attributes.lineSpacing == null || !text.attributes.lineSpacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.lineSpacingProperty());
            }
        };
        private static final CssMetaData<Text, TextBoundsType> BOUNDS_TYPE = new CssMetaData<Text, TextBoundsType>("-fx-bounds-type", new EnumConverter<TextBoundsType>(TextBoundsType.class), Text.access$2600()){

            @Override
            public boolean isSettable(Text text) {
                return text.boundsType == null || !text.boundsType.isBound();
            }

            @Override
            public StyleableProperty<TextBoundsType> getStyleableProperty(Text text) {
                return (StyleableProperty)((Object)text.boundsTypeProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(javafx.scene.shape.Shape.getClassCssMetaData());
            arrayList.add(FONT);
            arrayList.add(UNDERLINE);
            arrayList.add(STRIKETHROUGH);
            arrayList.add(TEXT_ALIGNMENT);
            arrayList.add(TEXT_ORIGIN);
            arrayList.add(FONT_SMOOTHING_TYPE);
            arrayList.add(LINE_SPACING);
            arrayList.add(BOUNDS_TYPE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

