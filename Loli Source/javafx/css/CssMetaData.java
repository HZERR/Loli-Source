/*
 * Decompiled with CFR 0.150.
 */
package javafx.css;

import java.util.Collections;
import java.util.List;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

public abstract class CssMetaData<S extends Styleable, V> {
    private final String property;
    private final StyleConverter<?, V> converter;
    private final V initialValue;
    private final List<CssMetaData<? extends Styleable, ?>> subProperties;
    private final boolean inherits;

    @Deprecated
    public void set(S s2, V v2, StyleOrigin styleOrigin) {
        StyleableProperty<V> styleableProperty = this.getStyleableProperty(s2);
        StyleOrigin styleOrigin2 = styleableProperty.getStyleOrigin();
        Object t2 = styleableProperty.getValue();
        if (styleOrigin2 != styleOrigin || (t2 != null ? !t2.equals(v2) : v2 != null)) {
            styleableProperty.applyStyle(styleOrigin, v2);
        }
    }

    public abstract boolean isSettable(S var1);

    public abstract StyleableProperty<V> getStyleableProperty(S var1);

    public final String getProperty() {
        return this.property;
    }

    public final StyleConverter<?, V> getConverter() {
        return this.converter;
    }

    public V getInitialValue(S s2) {
        return this.initialValue;
    }

    public final List<CssMetaData<? extends Styleable, ?>> getSubProperties() {
        return this.subProperties;
    }

    public final boolean isInherits() {
        return this.inherits;
    }

    protected CssMetaData(String string, StyleConverter<?, V> styleConverter, V v2, boolean bl, List<CssMetaData<? extends Styleable, ?>> list) {
        this.property = string;
        this.converter = styleConverter;
        this.initialValue = v2;
        this.inherits = bl;
        List<CssMetaData<Styleable, ?>> list2 = this.subProperties = list != null ? Collections.unmodifiableList(list) : null;
        if (this.property == null || this.converter == null) {
            throw new IllegalArgumentException("neither property nor converter can be null");
        }
    }

    protected CssMetaData(String string, StyleConverter<?, V> styleConverter, V v2, boolean bl) {
        this(string, styleConverter, v2, bl, null);
    }

    protected CssMetaData(String string, StyleConverter<?, V> styleConverter, V v2) {
        this(string, styleConverter, v2, false, null);
    }

    protected CssMetaData(String string, StyleConverter<?, V> styleConverter) {
        this(string, styleConverter, null, false, null);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CssMetaData cssMetaData = (CssMetaData)object;
        return !(this.property == null ? cssMetaData.property != null : !this.property.equals(cssMetaData.property));
    }

    public int hashCode() {
        int n2 = 3;
        n2 = 19 * n2 + (this.property != null ? this.property.hashCode() : 0);
        return n2;
    }

    public String toString() {
        return "CSSProperty {" + "property: " + this.property + ", converter: " + this.converter.toString() + ", initalValue: " + String.valueOf(this.initialValue) + ", inherits: " + this.inherits + ", subProperties: " + (this.subProperties != null ? this.subProperties.toString() : "[]") + "}";
    }
}

