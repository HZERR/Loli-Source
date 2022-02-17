/*
 * Decompiled with CFR 0.150.
 */
package javafx.css;

import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;

public abstract class StyleableIntegerProperty
extends IntegerPropertyBase
implements StyleableProperty<Number> {
    private StyleOrigin origin = null;

    public StyleableIntegerProperty() {
    }

    public StyleableIntegerProperty(int n2) {
        super(n2);
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
    public void set(int n2) {
        super.set(n2);
        this.origin = StyleOrigin.USER;
    }

    @Override
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}

