/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.WritableBooleanValue;

public abstract class BooleanProperty
extends ReadOnlyBooleanProperty
implements Property<Boolean>,
WritableBooleanValue {
    @Override
    public void setValue(Boolean bl) {
        if (bl == null) {
            Logging.getLogger().fine("Attempt to set boolean property to null, using default value instead.", new NullPointerException());
            this.set(false);
        } else {
            this.set(bl);
        }
    }

    @Override
    public void bindBidirectional(Property<Boolean> property) {
        Bindings.bindBidirectional(this, property);
    }

    @Override
    public void unbindBidirectional(Property<Boolean> property) {
        Bindings.unbindBidirectional(this, property);
    }

    @Override
    public String toString() {
        Object object = this.getBean();
        String string = this.getName();
        StringBuilder stringBuilder = new StringBuilder("BooleanProperty [");
        if (object != null) {
            stringBuilder.append("bean: ").append(object).append(", ");
        }
        if (string != null && !string.equals("")) {
            stringBuilder.append("name: ").append(string).append(", ");
        }
        stringBuilder.append("value: ").append(this.get()).append("]");
        return stringBuilder.toString();
    }

    public static BooleanProperty booleanProperty(final Property<Boolean> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof BooleanProperty ? (BooleanProperty)property : new BooleanPropertyBase(){
            {
                BidirectionalBinding.bind(this, property);
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
                    BidirectionalBinding.unbind(property, this);
                }
                finally {
                    super.finalize();
                }
            }
        };
    }

    @Override
    public ObjectProperty<Boolean> asObject() {
        return new ObjectPropertyBase<Boolean>(){
            {
                BidirectionalBinding.bind(this, BooleanProperty.this);
            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return BooleanProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    BidirectionalBinding.unbind(this, BooleanProperty.this);
                }
                finally {
                    super.finalize();
                }
            }
        };
    }
}

