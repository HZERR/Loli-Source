/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.InputField;
import com.sun.javafx.scene.control.skin.IntegerFieldSkin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Skin;

class IntegerField
extends InputField {
    private IntegerProperty value = new SimpleIntegerProperty(this, "value");
    private IntegerProperty maxValue = new SimpleIntegerProperty(this, "maxValue", -1);

    public final int getValue() {
        return this.value.get();
    }

    public final void setValue(int n2) {
        this.value.set(n2);
    }

    public final IntegerProperty valueProperty() {
        return this.value;
    }

    public final int getMaxValue() {
        return this.maxValue.get();
    }

    public final void setMaxValue(int n2) {
        this.maxValue.set(n2);
    }

    public final IntegerProperty maxValueProperty() {
        return this.maxValue;
    }

    public IntegerField() {
        this(-1);
    }

    public IntegerField(int n2) {
        this.getStyleClass().setAll("integer-field");
        this.setMaxValue(n2);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new IntegerFieldSkin(this);
    }
}

