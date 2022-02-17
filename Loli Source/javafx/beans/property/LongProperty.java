/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.value.WritableLongValue;

public abstract class LongProperty
extends ReadOnlyLongProperty
implements Property<Number>,
WritableLongValue {
    @Override
    public void setValue(Number number) {
        if (number == null) {
            Logging.getLogger().fine("Attempt to set long property to null, using default value instead.", new NullPointerException());
            this.set(0L);
        } else {
            this.set(number.longValue());
        }
    }

    @Override
    public void bindBidirectional(Property<Number> property) {
        Bindings.bindBidirectional(this, property);
    }

    @Override
    public void unbindBidirectional(Property<Number> property) {
        Bindings.unbindBidirectional(this, property);
    }

    @Override
    public String toString() {
        Object object = this.getBean();
        String string = this.getName();
        StringBuilder stringBuilder = new StringBuilder("LongProperty [");
        if (object != null) {
            stringBuilder.append("bean: ").append(object).append(", ");
        }
        if (string != null && !string.equals("")) {
            stringBuilder.append("name: ").append(string).append(", ");
        }
        stringBuilder.append("value: ").append(this.get()).append("]");
        return stringBuilder.toString();
    }

    public static LongProperty longProperty(final Property<Long> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return new LongPropertyBase(){
            {
                BidirectionalBinding.bindNumber(this, property);
            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return property.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    BidirectionalBinding.unbindNumber(property, this);
                }
                finally {
                    super.finalize();
                }
            }
        };
    }

    @Override
    public ObjectProperty<Long> asObject() {
        return new ObjectPropertyBase<Long>(){
            {
                BidirectionalBinding.bindNumber(this, LongProperty.this);
            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return LongProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    BidirectionalBinding.unbindNumber(this, LongProperty.this);
                }
                finally {
                    super.finalize();
                }
            }
        };
    }
}

