/*
 * Decompiled with CFR 0.150.
 */
package javafx.css;

import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;

public abstract class StyleableDoubleProperty
extends DoublePropertyBase
implements StyleableProperty<Number> {
    private StyleOrigin origin = null;

    public StyleableDoubleProperty() {
    }

    public StyleableDoubleProperty(double d2) {
        super(d2);
    }

    @Override
    public void applyStyle(StyleOrigin styleOrigin, Number number) {
        this.setValue(number);
        this.origin = styleOrigin;
    }

    @Override
    public void bind(ObservableValue<? extends Number> observableValue) {
        super.bind(observableValue);
        this.origin = StyleOrigin.USER;
    }

    @Override
    public void set(double d2) {
        super.set(d2);
        this.origin = StyleOrigin.USER;
    }

    @Override
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}

