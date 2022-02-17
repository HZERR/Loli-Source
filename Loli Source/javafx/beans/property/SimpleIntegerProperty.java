/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import javafx.beans.property.IntegerPropertyBase;

public class SimpleIntegerProperty
extends IntegerPropertyBase {
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

    public SimpleIntegerProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleIntegerProperty(int n2) {
        this(DEFAULT_BEAN, DEFAULT_NAME, n2);
    }

    public SimpleIntegerProperty(Object object, String string) {
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }

    public SimpleIntegerProperty(Object object, String string, int n2) {
        super(n2);
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }
}

