/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Builder;

@Deprecated
public class PropertyValueFactoryBuilder<S, T, B extends PropertyValueFactoryBuilder<S, T, B>>
implements Builder<PropertyValueFactory<S, T>> {
    private String property;

    protected PropertyValueFactoryBuilder() {
    }

    public static <S, T> PropertyValueFactoryBuilder<S, T, ?> create() {
        return new PropertyValueFactoryBuilder();
    }

    public B property(String string) {
        this.property = string;
        return (B)this;
    }

    @Override
    public PropertyValueFactory<S, T> build() {
        PropertyValueFactory propertyValueFactory = new PropertyValueFactory(this.property);
        return propertyValueFactory;
    }
}

