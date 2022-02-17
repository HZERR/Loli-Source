/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import javafx.beans.property.ObjectPropertyBase;

public class SimpleObjectProperty<T>
extends ObjectPropertyBase<T> {
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

    public SimpleObjectProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleObjectProperty(T t2) {
        this(DEFAULT_BEAN, DEFAULT_NAME, t2);
    }

    public SimpleObjectProperty(Object object, String string) {
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }

    public SimpleObjectProperty(Object object, String string, T t2) {
        super(t2);
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }
}

