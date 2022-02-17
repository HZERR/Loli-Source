/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;

public class ReadOnlyIntegerWrapper
extends SimpleIntegerProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyIntegerWrapper() {
    }

    public ReadOnlyIntegerWrapper(int n2) {
        super(n2);
    }

    public ReadOnlyIntegerWrapper(Object object, String string) {
        super(object, string);
    }

    public ReadOnlyIntegerWrapper(Object object, String string, int n2) {
        super(object, string, n2);
    }

    public ReadOnlyIntegerProperty getReadOnlyProperty() {
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
    extends ReadOnlyIntegerPropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        @Override
        public int get() {
            return ReadOnlyIntegerWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyIntegerWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyIntegerWrapper.this.getName();
        }
    }
}

