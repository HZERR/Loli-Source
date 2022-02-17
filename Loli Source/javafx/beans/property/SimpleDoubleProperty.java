/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import javafx.beans.property.DoublePropertyBase;

public class SimpleDoubleProperty
extends DoublePropertyBase {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private final Object bean;
    private final String name;

    @Override
    public Object getBean() {
        return this.bean;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public SimpleDoubleProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleDoubleProperty(double d2) {
        this(DEFAULT_BEAN, DEFAULT_NAME, d2);
    }

    public SimpleDoubleProperty(Object object, String string) {
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }

    public SimpleDoubleProperty(Object object, String string, double d2) {
        super(d2);
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }
}

