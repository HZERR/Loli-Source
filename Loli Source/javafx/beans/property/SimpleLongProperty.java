/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import javafx.beans.property.LongPropertyBase;

public class SimpleLongProperty
extends LongPropertyBase {
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

    public SimpleLongProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleLongProperty(long l2) {
        this(DEFAULT_BEAN, DEFAULT_NAME, l2);
    }

    public SimpleLongProperty(Object object, String string) {
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }

    public SimpleLongProperty(Object object, String string, long l2) {
        super(l2);
        this.bean = object;
        this.name = string == null ? DEFAULT_NAME : string;
    }
}

