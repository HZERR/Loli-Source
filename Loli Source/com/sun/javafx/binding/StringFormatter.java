/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import com.sun.javafx.binding.StringConstant;
import java.util.ArrayList;
import java.util.Locale;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class StringFormatter
extends StringBinding {
    private static Object extractValue(Object object) {
        return object instanceof ObservableValue ? ((ObservableValue)object).getValue() : object;
    }

    private static Object[] extractValues(Object[] arrobject) {
        int n2 = arrobject.length;
        Object[] arrobject2 = new Object[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrobject2[i2] = StringFormatter.extractValue(arrobject[i2]);
        }
        return arrobject2;
    }

    private static ObservableValue<?>[] extractDependencies(Object ... arrobject) {
        ArrayList<ObservableValue> arrayList = new ArrayList<ObservableValue>();
        for (Object object : arrobject) {
            if (!(object instanceof ObservableValue)) continue;
            arrayList.add((ObservableValue)object);
        }
        return arrayList.toArray(new ObservableValue[arrayList.size()]);
    }

    public static StringExpression convert(final ObservableValue<?> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("ObservableValue must be specified");
        }
        if (observableValue instanceof StringExpression) {
            return (StringExpression)observableValue;
        }
        return new StringBinding(){
            {
                super.bind(observableValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableValue);
            }

            @Override
            protected String computeValue() {
                Object t2 = observableValue.getValue();
                return t2 == null ? "null" : t2.toString();
            }

            @Override
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.singletonObservableList(observableValue);
            }
        };
    }

    public static StringExpression concat(final Object ... arrobject) {
        if (arrobject == null || arrobject.length == 0) {
            return StringConstant.valueOf("");
        }
        if (arrobject.length == 1) {
            Object object = arrobject[0];
            return object instanceof ObservableValue ? StringFormatter.convert((ObservableValue)object) : StringConstant.valueOf(object.toString());
        }
        if (StringFormatter.extractDependencies(arrobject).length == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object object : arrobject) {
                stringBuilder.append(object);
            }
            return StringConstant.valueOf(stringBuilder.toString());
        }
        return new StringFormatter(){
            {
                super.bind(StringFormatter.extractDependencies(arrobject));
            }

            @Override
            public void dispose() {
                super.unbind(StringFormatter.extractDependencies(arrobject));
            }

            @Override
            protected String computeValue() {
                StringBuilder stringBuilder = new StringBuilder();
                for (Object object : arrobject) {
                    stringBuilder.append(StringFormatter.extractValue(object));
                }
                return stringBuilder.toString();
            }

            @Override
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(StringFormatter.extractDependencies(arrobject)));
            }
        };
    }

    public static StringExpression format(final Locale locale, final String string, final Object ... arrobject) {
        if (string == null) {
            throw new NullPointerException("Format cannot be null.");
        }
        if (StringFormatter.extractDependencies(arrobject).length == 0) {
            return StringConstant.valueOf(String.format(locale, string, arrobject));
        }
        StringFormatter stringFormatter = new StringFormatter(){
            {
                super.bind(StringFormatter.extractDependencies(arrobject));
            }

            @Override
            public void dispose() {
                super.unbind(StringFormatter.extractDependencies(arrobject));
            }

            @Override
            protected String computeValue() {
                Object[] arrobject2 = StringFormatter.extractValues(arrobject);
                return String.format(locale, string, arrobject2);
            }

            @Override
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(StringFormatter.extractDependencies(arrobject)));
            }
        };
        stringFormatter.get();
        return stringFormatter;
    }

    public static StringExpression format(final String string, final Object ... arrobject) {
        if (string == null) {
            throw new NullPointerException("Format cannot be null.");
        }
        if (StringFormatter.extractDependencies(arrobject).length == 0) {
            return StringConstant.valueOf(String.format(string, arrobject));
        }
        StringFormatter stringFormatter = new StringFormatter(){
            {
                super.bind(StringFormatter.extractDependencies(arrobject));
            }

            @Override
            public void dispose() {
                super.unbind(StringFormatter.extractDependencies(arrobject));
            }

            @Override
            protected String computeValue() {
                Object[] arrobject2 = StringFormatter.extractValues(arrobject);
                return String.format(string, arrobject2);
            }

            @Override
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(StringFormatter.extractDependencies(arrobject)));
            }
        };
        stringFormatter.get();
        return stringFormatter;
    }
}

