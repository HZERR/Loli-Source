/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.Locale;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.FloatExpression;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.binding.LongExpression;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableNumberValue;

public abstract class NumberExpressionBase
implements NumberExpression {
    public static <S extends Number> NumberExpressionBase numberExpression(ObservableNumberValue observableNumberValue) {
        if (observableNumberValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        NumberExpressionBase numberExpressionBase = (NumberExpressionBase)(observableNumberValue instanceof NumberExpressionBase ? observableNumberValue : (observableNumberValue instanceof ObservableIntegerValue ? IntegerExpression.integerExpression((ObservableIntegerValue)observableNumberValue) : (observableNumberValue instanceof ObservableDoubleValue ? DoubleExpression.doubleExpression((ObservableDoubleValue)observableNumberValue) : (observableNumberValue instanceof ObservableFloatValue ? FloatExpression.floatExpression((ObservableFloatValue)observableNumberValue) : (observableNumberValue instanceof ObservableLongValue ? LongExpression.longExpression((ObservableLongValue)observableNumberValue) : null)))));
        if (numberExpressionBase != null) {
            return numberExpressionBase;
        }
        throw new IllegalArgumentException("Unsupported Type");
    }

    @Override
    public NumberBinding add(ObservableNumberValue observableNumberValue) {
        return Bindings.add((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public NumberBinding subtract(ObservableNumberValue observableNumberValue) {
        return Bindings.subtract((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public NumberBinding multiply(ObservableNumberValue observableNumberValue) {
        return Bindings.multiply((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public NumberBinding divide(ObservableNumberValue observableNumberValue) {
        return Bindings.divide((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding isEqualTo(ObservableNumberValue observableNumberValue) {
        return Bindings.equal((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding isEqualTo(ObservableNumberValue observableNumberValue, double d2) {
        return Bindings.equal((ObservableNumberValue)this, observableNumberValue, d2);
    }

    @Override
    public BooleanBinding isEqualTo(double d2, double d3) {
        return Bindings.equal((ObservableNumberValue)this, d2, d3);
    }

    @Override
    public BooleanBinding isEqualTo(float f2, double d2) {
        return Bindings.equal((ObservableNumberValue)this, f2, d2);
    }

    @Override
    public BooleanBinding isEqualTo(long l2) {
        return Bindings.equal((ObservableNumberValue)this, l2);
    }

    @Override
    public BooleanBinding isEqualTo(long l2, double d2) {
        return Bindings.equal((ObservableNumberValue)this, l2, d2);
    }

    @Override
    public BooleanBinding isEqualTo(int n2) {
        return Bindings.equal((ObservableNumberValue)this, n2);
    }

    @Override
    public BooleanBinding isEqualTo(int n2, double d2) {
        return Bindings.equal((ObservableNumberValue)this, n2, d2);
    }

    @Override
    public BooleanBinding isNotEqualTo(ObservableNumberValue observableNumberValue) {
        return Bindings.notEqual((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding isNotEqualTo(ObservableNumberValue observableNumberValue, double d2) {
        return Bindings.notEqual((ObservableNumberValue)this, observableNumberValue, d2);
    }

    @Override
    public BooleanBinding isNotEqualTo(double d2, double d3) {
        return Bindings.notEqual((ObservableNumberValue)this, d2, d3);
    }

    @Override
    public BooleanBinding isNotEqualTo(float f2, double d2) {
        return Bindings.notEqual((ObservableNumberValue)this, f2, d2);
    }

    @Override
    public BooleanBinding isNotEqualTo(long l2) {
        return Bindings.notEqual((ObservableNumberValue)this, l2);
    }

    @Override
    public BooleanBinding isNotEqualTo(long l2, double d2) {
        return Bindings.notEqual((ObservableNumberValue)this, l2, d2);
    }

    @Override
    public BooleanBinding isNotEqualTo(int n2) {
        return Bindings.notEqual((ObservableNumberValue)this, n2);
    }

    @Override
    public BooleanBinding isNotEqualTo(int n2, double d2) {
        return Bindings.notEqual((ObservableNumberValue)this, n2, d2);
    }

    @Override
    public BooleanBinding greaterThan(ObservableNumberValue observableNumberValue) {
        return Bindings.greaterThan((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding greaterThan(double d2) {
        return Bindings.greaterThan((ObservableNumberValue)this, d2);
    }

    @Override
    public BooleanBinding greaterThan(float f2) {
        return Bindings.greaterThan((ObservableNumberValue)this, f2);
    }

    @Override
    public BooleanBinding greaterThan(long l2) {
        return Bindings.greaterThan((ObservableNumberValue)this, l2);
    }

    @Override
    public BooleanBinding greaterThan(int n2) {
        return Bindings.greaterThan((ObservableNumberValue)this, n2);
    }

    @Override
    public BooleanBinding lessThan(ObservableNumberValue observableNumberValue) {
        return Bindings.lessThan((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding lessThan(double d2) {
        return Bindings.lessThan((ObservableNumberValue)this, d2);
    }

    @Override
    public BooleanBinding lessThan(float f2) {
        return Bindings.lessThan((ObservableNumberValue)this, f2);
    }

    @Override
    public BooleanBinding lessThan(long l2) {
        return Bindings.lessThan((ObservableNumberValue)this, l2);
    }

    @Override
    public BooleanBinding lessThan(int n2) {
        return Bindings.lessThan((ObservableNumberValue)this, n2);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(ObservableNumberValue observableNumberValue) {
        return Bindings.greaterThanOrEqual((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(double d2) {
        return Bindings.greaterThanOrEqual((ObservableNumberValue)this, d2);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(float f2) {
        return Bindings.greaterThanOrEqual((ObservableNumberValue)this, f2);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(long l2) {
        return Bindings.greaterThanOrEqual((ObservableNumberValue)this, l2);
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(int n2) {
        return Bindings.greaterThanOrEqual((ObservableNumberValue)this, n2);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(ObservableNumberValue observableNumberValue) {
        return Bindings.lessThanOrEqual((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(double d2) {
        return Bindings.lessThanOrEqual((ObservableNumberValue)this, d2);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(float f2) {
        return Bindings.lessThanOrEqual((ObservableNumberValue)this, f2);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(long l2) {
        return Bindings.lessThanOrEqual((ObservableNumberValue)this, l2);
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(int n2) {
        return Bindings.lessThanOrEqual((ObservableNumberValue)this, n2);
    }

    @Override
    public StringBinding asString() {
        return (StringBinding)StringFormatter.convert(this);
    }

    @Override
    public StringBinding asString(String string) {
        return (StringBinding)Bindings.format(string, this);
    }

    @Override
    public StringBinding asString(Locale locale, String string) {
        return (StringBinding)Bindings.format(locale, string, this);
    }
}

