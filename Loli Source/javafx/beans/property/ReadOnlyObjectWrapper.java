/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;

public class ReadOnlyObjectWrapper<T>
extends SimpleObjectProperty<T> {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyObjectWrapper() {
    }

    public ReadOnlyObjectWrapper(T t2) {
        super(t2);
    }

    public ReadOnlyObjectWrapper(Object object, String string) {
        super(object, string);
    }

    public ReadOnlyObjectWrapper(Object object, String string, T t2) {
        super(object, string, t2);
    }

    public ReadOnlyObjectProperty<T> getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    private class ReadOnlyPropertyImpl
    extends ReadOnlyObjectPropertyBase<T> {
        private ReadOnlyPropertyImpl() {
        }

        @Override
        public T get() {
            return ReadOnlyObjectWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyObjectWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyObjectWrapper.this.getName();
        }
    }
}

