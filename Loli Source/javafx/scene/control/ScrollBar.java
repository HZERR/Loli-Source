/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.ScrollBarSkin;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ScrollBar
extends Control {
    private DoubleProperty min;
    private DoubleProperty max;
    private DoubleProperty value;
    private ObjectProperty<Orientation> orientation;
    private DoubleProperty unitIncrement;
    private DoubleProperty blockIncrement;
    private DoubleProperty visibleAmount;
    private static final String DEFAULT_STYLE_CLASS = "scroll-bar";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public ScrollBar() {
        this.setWidth(20.0);
        this.setHeight(100.0);
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.SCROLL_BAR);
        ((StyleableProperty)((Object)this.focusTraversableProperty())).applyStyle(null, Boolean.FALSE);
        this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
    }

    public final void setMin(double d2) {
        this.minProperty().set(d2);
    }

    public final double getMin() {
        return this.min == null ? 0.0 : this.min.get();
    }

    public final DoubleProperty minProperty() {
        if (this.min == null) {
            this.min = new SimpleDoubleProperty(this, "min");
        }
        return this.min;
    }

    public final void setMax(double d2) {
        this.maxProperty().set(d2);
    }

    public final double getMax() {
        return this.max == null ? 100.0 : this.max.get();
    }

    public final DoubleProperty maxProperty() {
        if (this.max == null) {
            this.max = new SimpleDoubleProperty(this, "max", 100.0);
        }
        return this.max;
    }

    public final void setValue(double d2) {
        this.valueProperty().set(d2);
    }

    public final double getValue() {
        return this.value == null ? 0.0 : this.value.get();
    }

    public final DoubleProperty valueProperty() {
        if (this.value == null) {
            this.value = new SimpleDoubleProperty(this, "value");
        }
        return this.value;
    }

    public final void setOrientation(Orientation orientation) {
        this.orientationProperty().set(orientation);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)((Object)this.orientation.get());
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL){

                @Override
                protected void invalidated() {
                    boolean bl = this.get() == Orientation.VERTICAL;
                    ScrollBar.this.pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, bl);
                    ScrollBar.this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, !bl);
                }

                @Override
                public CssMetaData<ScrollBar, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return ScrollBar.this;
                }

                @Override
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setUnitIncrement(double d2) {
        this.unitIncrementProperty().set(d2);
    }

    public final double getUnitIncrement() {
        return this.unitIncrement == null ? 1.0 : this.unitIncrement.get();
    }

    public final DoubleProperty unitIncrementProperty() {
        if (this.unitIncrement == null) {
            this.unitIncrement = new StyleableDoubleProperty(1.0){

                @Override
                public CssMetaData<ScrollBar, Number> getCssMetaData() {
                    return StyleableProperties.UNIT_INCREMENT;
                }

                @Override
                public Object getBean() {
                    return ScrollBar.this;
                }

                @Override
                public String getName() {
                    return "unitIncrement";
                }
            };
        }
        return this.unitIncrement;
    }

    public final void setBlockIncrement(double d2) {
        this.blockIncrementProperty().set(d2);
    }

    public final double getBlockIncrement() {
        return this.blockIncrement == null ? 10.0 : this.blockIncrement.get();
    }

    public final DoubleProperty blockIncrementProperty() {
        if (this.blockIncrement == null) {
            this.blockIncrement = new StyleableDoubleProperty(10.0){

                @Override
                public CssMetaData<ScrollBar, Number> getCssMetaData() {
                    return StyleableProperties.BLOCK_INCREMENT;
                }

                @Override
                public Object getBean() {
                    return ScrollBar.this;
                }

                @Override
                public String getName() {
                    return "blockIncrement";
                }
            };
        }
        return this.blockIncrement;
    }

    public final void setVisibleAmount(double d2) {
        this.visibleAmountProperty().set(d2);
    }

    public final double getVisibleAmount() {
        return this.visibleAmount == null ? 15.0 : this.visibleAmount.get();
    }

    public final DoubleProperty visibleAmountProperty() {
        if (this.visibleAmount == null) {
            this.visibleAmount = new SimpleDoubleProperty(this, "visibleAmount");
        }
        return this.visibleAmount;
    }

    public void adjustValue(double d2) {
        double d3 = (this.getMax() - this.getMin()) * Utils.clamp(0.0, d2, 1.0) + this.getMin();
        if (Double.compare(d3, this.getValue()) != 0) {
            boolean bl;
            double d4 = d3 > this.getValue() ? this.getValue() + this.getBlockIncrement() : this.getValue() - this.getBlockIncrement();
            boolean bl2 = bl = d2 > (this.getValue() - this.getMin()) / (this.getMax() - this.getMin());
            if (bl && d4 > d3) {
                d4 = d3;
            }
            if (!bl && d4 < d3) {
                d4 = d3;
            }
            this.setValue(Utils.clamp(this.getMin(), d4, this.getMax()));
        }
    }

    public void increment() {
        this.setValue(Utils.clamp(this.getMin(), this.getValue() + this.getUnitIncrement(), this.getMax()));
    }

    public void decrement() {
        this.setValue(Utils.clamp(this.getMin(), this.getValue() - this.getUnitIncrement(), this.getMax()));
    }

    private void blockIncrement() {
        this.adjustValue(this.getValue() + this.getBlockIncrement());
    }

    private void blockDecrement() {
        this.adjustValue(this.getValue() - this.getBlockIncrement());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ScrollBarSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return ScrollBar.getClassCssMetaData();
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case VALUE: {
                return this.getValue();
            }
            case MAX_VALUE: {
                return this.getMax();
            }
            case MIN_VALUE: {
                return this.getMin();
            }
            case ORIENTATION: {
                return this.getOrientation();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case INCREMENT: {
                this.increment();
                break;
            }
            case DECREMENT: {
                this.decrement();
                break;
            }
            case BLOCK_INCREMENT: {
                this.blockIncrement();
                break;
            }
            case BLOCK_DECREMENT: {
                this.blockDecrement();
                break;
            }
            case SET_VALUE: {
                Double d2 = (Double)arrobject[0];
                if (d2 == null) break;
                this.setValue(d2);
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<ScrollBar, Orientation> ORIENTATION = new CssMetaData<ScrollBar, Orientation>("-fx-orientation", new EnumConverter<Orientation>(Orientation.class), Orientation.HORIZONTAL){

            @Override
            public Orientation getInitialValue(ScrollBar scrollBar) {
                return scrollBar.getOrientation();
            }

            @Override
            public boolean isSettable(ScrollBar scrollBar) {
                return scrollBar.orientation == null || !scrollBar.orientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(ScrollBar scrollBar) {
                return (StyleableProperty)((Object)scrollBar.orientationProperty());
            }
        };
        private static final CssMetaData<ScrollBar, Number> UNIT_INCREMENT = new CssMetaData<ScrollBar, Number>("-fx-unit-increment", SizeConverter.getInstance(), (Number)1.0){

            @Override
            public boolean isSettable(ScrollBar scrollBar) {
                return scrollBar.unitIncrement == null || !scrollBar.unitIncrement.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ScrollBar scrollBar) {
                return (StyleableProperty)((Object)scrollBar.unitIncrementProperty());
            }
        };
        private static final CssMetaData<ScrollBar, Number> BLOCK_INCREMENT = new CssMetaData<ScrollBar, Number>("-fx-block-increment", SizeConverter.getInstance(), (Number)10.0){

            @Override
            public boolean isSettable(ScrollBar scrollBar) {
                return scrollBar.blockIncrement == null || !scrollBar.blockIncrement.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ScrollBar scrollBar) {
                return (StyleableProperty)((Object)scrollBar.blockIncrementProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Control.getClassCssMetaData());
            arrayList.add(ORIENTATION);
            arrayList.add(UNIT_INCREMENT);
            arrayList.add(BLOCK_INCREMENT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

