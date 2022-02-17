/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.InputField;
import com.sun.javafx.scene.control.skin.WebColorFieldSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

class WebColorField
extends InputField {
    private ObjectProperty<Color> value = new SimpleObjectProperty<Color>(this, "value");

    public final Color getValue() {
        return (Color)this.value.get();
    }

    public final void setValue(Color color) {
        this.value.set(color);
    }

    public final ObjectProperty<Color> valueProperty() {
        return this.value;
    }

    public WebColorField() {
        this.getStyleClass().setAll("webcolor-field");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new WebColorFieldSkin(this);
    }
}

