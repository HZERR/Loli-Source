/*
 * Decompiled with CFR 0.150.
 */
package javafx.css;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;

public abstract class StyleableObjectProperty<T>
extends ObjectPropertyBase<T>
implements StyleableProperty<T> {
    private StyleOrigin origin = null;

    public StyleableObjectProperty() {
    }

    public StyleableObjectProperty(T t2) {
        super(t2);
    }

    @Override
    public void applyStyle(StyleOrigin styleOrigin, T t2) {
        this.set(t2);
        this.origin = styleOrigin;
    }

    @Override
    public void bind(ObservableValue<? extends T> observableValue) {
        super.bind(observableValue);
        this.origin = StyleOrigin.USER;
    }

    @Override
    public void set(T t2) {
        super.set(t2);
        this.origin = StyleOrigin.USER;
    }

    @Override
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}

