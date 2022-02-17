/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberExpressionBase;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class DoubleExpression
extends NumberExpressionBase
implements ObservableDoubleValue {
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
        return (float)this.get();
    }

    @Override
    public double doubleValue() {
        return this.get();
    }

    @Override
    public Double getValue() {
        return this.get();
    }

    public static DoubleExpression doubleExpression(final ObservableDoubleValue observableDoubleValue) {
        if (observableDoubleValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return observableDoubleValue instanceof DoubleExpression ? (DoubleExpression)observableDoubleValue : new DoubleBinding(){
            {
                super.bind(observableDoubleValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableDoubleValue);
            }

            @Override
            protected double computeValue() {
                return observableDoubleValue.get();
            }

            @Override
            public ObservableList<ObservableDoubleValue> getDependencies() {
                return FXCollections.singletonObservableList(observableDoubleValue);
            }
        };
    }

    public static <T extends Number> DoubleExpression doubleExpression(final ObservableValue<T> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return observableValue instanceof DoubleExpression ? (DoubleExpression)observableValue : new DoubleBinding(){
            {
                super.bind(observableValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableValue);
            }

            @Override
            protected double computeValue() {
                Number number = (Number)observableValue.getValue();
                return number == null ? 0.0 : number.doubleValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(observableValue);
            }
        };
    }

    @Override
    public DoubleBinding negate() {
        return (DoubleBinding)Bindings.negate(this);
    }

    @Override
    public DoubleBinding add(ObservableNumberValue observableNumberValue) {
        return (DoubleBinding)Bindings.add((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public DoubleBinding add(double d2) {
        return Bindings.add((ObservableNumberValue)this, d2);
    }

    @Override
    public DoubleBinding add(float f2) {
        return (DoubleBinding)Bindings.add((ObservableNumberValue)this, f2);
    }

    @Override
    public DoubleBinding add(long l2) {
        return (DoubleBinding)Bindings.add((ObservableNumberValue)this, l2);
    }

    @Override
    public DoubleBinding add(int n2) {
        return (DoubleBinding)Bindings.add((ObservableNumberValue)this, n2);
    }

    @Override
    public DoubleBinding subtract(ObservableNumberValue observableNumberValue) {
        return (DoubleBinding)Bindings.subtract((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public DoubleBinding subtract(double d2) {
        return Bindings.subtract((ObservableNumberValue)this, d2);
    }

    @Override
    public DoubleBinding subtract(float f2) {
        return (DoubleBinding)Bindings.subtract((ObservableNumberValue)this, f2);
    }

    @Override
    public DoubleBinding subtract(long l2) {
        return (DoubleBinding)Bindings.subtract((ObservableNumberValue)this, l2);
    }

    @Override
    public DoubleBinding subtract(int n2) {
        return (DoubleBinding)Bindings.subtract((ObservableNumberValue)this, n2);
    }

    @Override
    public DoubleBinding multiply(ObservableNumberValue observableNumberValue) {
        return (DoubleBinding)Bindings.multiply((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public DoubleBinding multiply(double d2) {
        return Bindings.multiply((ObservableNumberValue)this, d2);
    }

    @Override
    public DoubleBinding multiply(float f2) {
        return (DoubleBinding)Bindings.multiply((ObservableNumberValue)this, f2);
    }

    @Override
    public DoubleBinding multiply(long l2) {
        return (DoubleBinding)Bindings.multiply((ObservableNumberValue)this, l2);
    }

    @Override
    public DoubleBinding multiply(int n2) {
        return (DoubleBinding)Bindings.multiply((ObservableNumberValue)this, n2);
    }

    @Override
    public DoubleBinding divide(ObservableNumberValue observableNumberValue) {
        return (DoubleBinding)Bindings.divide((ObservableNumberValue)this, observableNumberValue);
    }

    @Override
    public DoubleBinding divide(double d2) {
        return Bindings.divide((ObservableNumberValue)this, d2);
    }

    @Override
    public DoubleBinding divide(float f2) {
        return (DoubleBinding)Bindings.divide((ObservableNumberValue)this, f2);
    }

    @Override
    public DoubleBinding divide(long l2) {
        return (DoubleBinding)Bindings.divide((ObservableNumberValue)this, l2);
    }

    @Override
    public DoubleBinding divide(int n2) {
        return (DoubleBinding)Bindings.divide((ObservableNumberValue)this, n2);
    }

    public ObjectExpression<Double> asObject() {
        return new ObjectBinding<Double>(){
            {
                this.bind(DoubleExpression.this);
            }

            @Override
            public void dispose() {
                this.unbind(DoubleExpression.this);
            }

            @Override
            protected Double computeValue() {
                return DoubleExpression.this.getValue();
            }
        };
    }
}

