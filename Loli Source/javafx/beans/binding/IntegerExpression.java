/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.NumberExpressionBase;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class IntegerExpression
extends NumberExpressionBase
implements ObservableIntegerValue {
    @Override
    public int intValue() {
        return this.get();
    }

    @Override
    public long longValue() {
        return this.get();
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
    public Integer getValue() {
        return this.get();
    }

    public static IntegerExpression integerExpression(final ObservableIntegerValue observableIntegerValue) {
        if (observableIntegerValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return observableIntegerValue instanceof IntegerExpression ? (IntegerExpression)observableIntegerValue : new IntegerBinding(){
            {
                super.bind(observableIntegerValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableIntegerValue);
            }

            @Override
            protected int computeValue() {
                return observableIntegerValue.get();
            }

            @Override
            public ObservableList<ObservableIntegerValue> getDependencies() {
                return FXCollections.singletonObservableList(observableIntegerValue);
            }
        };
    }

    public static <T extends Number> IntegerExpression integerExpression(final ObservableValue<T> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return observableValue instanceof IntegerExpression ? (IntegerExpression)observableValue : new IntegerBinding(){
            {
                super.bind(observableValue);
            }

            @Override
            public void dispose() {
                super.unbind(observableValue);
            }

            @Override
            protected int computeValue() {
                Number number = (Number)observableValue.getValue();
                return number == null ? 0 : number.intValue();
            }

            @Override
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(observableValue);
            }
        };
    }

    @Override
    public IntegerBinding negate() {
        return (IntegerBinding)Bindings.negate(this);
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
    public LongBinding add(long l2) {
        return (LongBinding)Bindings.add((ObservableNumberValue)this, l2);
    }

    @Override
    public IntegerBinding add(int n2) {
        return (IntegerBinding)Bindings.add((ObservableNumberValue)this, n2);
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
    public LongBinding subtract(long l2) {
        return (LongBinding)Bindings.subtract((ObservableNumberValue)this, l2);
    }

    @Override
    public IntegerBinding subtract(int n2) {
        return (IntegerBinding)Bindings.subtract((ObservableNumberValue)this, n2);
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
    public LongBinding multiply(long l2) {
        return (LongBinding)Bindings.multiply((ObservableNumberValue)this, l2);
    }

    @Override
    public IntegerBinding multiply(int n2) {
        return (IntegerBinding)Bindings.multiply((ObservableNumberValue)this, n2);
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
    public LongBinding divide(long l2) {
        return (LongBinding)Bindings.divide((ObservableNumberValue)this, l2);
    }

    @Override
    public IntegerBinding divide(int n2) {
        return (IntegerBinding)Bindings.divide((ObservableNumberValue)this, n2);
    }

    public ObjectExpression<Integer> asObject() {
        return new ObjectBinding<Integer>(){
            {
                this.bind(IntegerExpression.this);
            }

            @Override
            public void dispose() {
                this.unbind(IntegerExpression.this);
            }

            @Override
            protected Integer computeValue() {
                return IntegerExpression.this.getValue();
            }
        };
    }
}

