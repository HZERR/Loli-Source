/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.WritableDoubleValue;

public abstract class DoubleProperty
extends ReadOnlyDoubleProperty
implements Property<Number>,
WritableDoubleValue {
    @Override
    public void setValue(Number number) {
        if (number == null) {
            Logging.getLogger().fine("Attempt to set double property to null, using default value instead.", new NullPointerException());
            this.set(0.0);
        } else {
            this.set(number.doubleValue());
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
        StringBuilder stringBuilder = new StringBuilder("DoubleProperty [");
        if (object != null) {
            stringBuilder.append("bean: ").append(object).append(", ");
        }
        if (string != null && !string.equals("")) {
            stringBuilder.append("name: ").append(string).append(", ");
        }
        stringBuilder.append("value: ").append(this.get()).append("]");
        return stringBuilder.toString();
    }

    public static DoubleProperty doubleProperty(final Property<Double> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return new DoublePropertyBase(){
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
    public ObjectProperty<Double> asObject() {
        return new ObjectPropertyBase<Double>(){
            {
                BidirectionalBinding.bindNumber(this, DoubleProperty.this);
            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return DoubleProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    BidirectionalBinding.unbindNumber(this, DoubleProperty.this);
                }
                finally {
                    super.finalize();
                }
            }
        };
    }
}

