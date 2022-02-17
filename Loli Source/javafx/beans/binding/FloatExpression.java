/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.NumberExpressionBase;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class FloatExpression
extends NumberExpressionBase
implements ObservableFloatValue {
    @Override
    public int intValue() {
        return (int)this.get();
    }

    @Override
    public long longValue() {
        return (long)this.get();
    }

    @Override
    public float floatValue() {
        return this.get();
    }

    @Override
    public double doubleValue() {
        return this.get();
    }

    @Override
    public Float getValue() {
        return Float.valueOf(this.get());
    }

    public static FloatExpression floatExpression(final ObservableFloatValue observableFloatValue) {
        if (observableFloatValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return observableFloatValue instanceof FloatExpression ? (FloatExpression)observableFloatValue : new FloatBinding(){
            {
                super.bind(observableFloatValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableFloatValue);
            }

            @Override
            protected float computeValue() {
                return observableFloatValue.get();
            }

            @Override
            public ObservableList<ObservableFloatValue> getDependencies() {
                return FXCollections.singletonObservableList(observableFloatValue);
            }
        };
    }

    public static <T extends Number> FloatExpression floatExpression(final ObservableValue<T> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return observableValue instanceof FloatExpression ? (FloatExpression)observableValue : new FloatBinding(){
            {
                super.bind(observableValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableValue);
            }

            @Override
            protected float computeValue() {
                Number number = (Number)observableValue.getValue();
                return number == null ? 0.0f : number.floatValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(observableValue);
            }
        };
    }

    @Override
    public FloatBinding negate() {
        return (FloatBinding)Bindings.negate(this);
    }

    @Override
    public DoubleBinding add(double d2) {
        return Bindings.add((ObservableNumberValue)this, d2);
    }

    @Override
    public FloatBinding add(float f2) {
        return (FloatBinding)Bindings.add((ObservableNumberValue)this, f2);
    }

    @Override
    public FloatBinding add(long l2) {
        return (FloatBinding)Bindings.add((ObservableNumberValue)this, l2);
    }

    @Override
    public FloatBinding add(int n2) {
        return (FloatBinding)Bindings.add((ObservableNumberValue)this, n2);
    }

    @Override
    public DoubleBinding subtract(double d2) {
        return Bindings.subtract((ObservableNumberValue)this, d2);
    }

    @Override
    public FloatBinding subtract(float f2) {
        return (FloatBinding)Bindings.subtract((ObservableNumberValue)this, f2);
    }

    @Override
    public FloatBinding subtract(long l2) {
        return (FloatBinding)Bindings.subtract((ObservableNumberValue)this, l2);
    }

    @Override
    public FloatBinding subtract(int n2) {
        return (FloatBinding)Bindings.subtract((ObservableNumberValue)this, n2);
    }

    @Override
    public DoubleBinding multiply(double d2) {
        return Bindings.multiply((ObservableNumberValue)this, d2);
    }

    @Override
    public FloatBinding multiply(float f2) {
        return (FloatBinding)Bindings.multiply((ObservableNumberValue)this, f2);
    }

    @Override
    public FloatBinding multiply(long l2) {
        return (FloatBinding)Bindings.multiply((ObservableNumberValue)this, l2);
    }

    @Override
    public FloatBinding multiply(int n2) {
        return (FloatBinding)Bindings.multiply((ObservableNumberValue)this, n2);
    }

    @Override
    public DoubleBinding divide(double d2) {
        return Bindings.divide((ObservableNumberValue)this, d2);
    }

    @Override
    public FloatBinding divide(float f2) {
        return (FloatBinding)Bindings.divide((ObservableNumberValue)this, f2);
    }

    @Override
    public FloatBinding divide(long l2) {
        return (FloatBinding)Bindings.divide((ObservableNumberValue)this, l2);
    }

    @Override
    public FloatBinding divide(int n2) {
        return (FloatBinding)Bindings.divide((ObservableNumberValue)this, n2);
    }

    public ObjectExpression<Float> asObject() {
        return new ObjectBinding<Float>(){
            {
                this.bind(FloatExpression.this);
            }

            @Override
            public void dispose() {
                this.unbind(FloatExpression.this);
            }

            @Override
            protected Float computeValue() {
                return FloatExpression.this.getValue();
            }
        };
    }
}

