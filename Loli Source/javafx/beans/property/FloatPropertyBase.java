/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;

public abstract class FloatPropertyBase
extends FloatProperty {
    private float value;
    private ObservableFloatValue observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Number> helper = null;

    public FloatPropertyBase() {
    }

    public FloatPropertyBase(float f2) {
        this.value = f2;
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, invalidationListener);
    }

    @Override
    public void addListener(ChangeListener<? super Number> changeListener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> changeListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
    }

    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper);
    }

    private void markInvalid() {
        if (this.valid) {
            this.valid = false;
            this.invalidated();
            this.fireValueChangedEvent();
        }
    }

    protected void invalidated() {
    }

    @Override
    public float get() {
        this.valid = true;
        return this.observable == null ? this.value : this.observable.get();
    }

    @Override
    public void set(float f2) {
        if (this.isBound()) {
            throw new RuntimeException((this.getBean() != null && this.getName() != null ? this.getBean().getClass().getSimpleName() + "." + this.getName() + " : " : "") + "A bound value cannot be set.");
        }
        if (this.value != f2) {
            this.value = f2;
            this.markInvalid();
        }
    }

    @Override
    public boolean isBound() {
        return this.observable != null;
    }

    @Override
    public void bind(final ObservableValue<? extends Number> observableValue) {
        ObservableFloatValue observableFloatValue;
        if (observableValue == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (observableValue instanceof ObservableFloatValue) {
            observableFloatValue = (ObservableFloatValue)observableValue;
        } else if (observableValue instanceof ObservableNumberValue) {
            final ObservableNumberValue observableNumberValue = (ObservableNumberValue)observableValue;
            observableFloatValue = new FloatBinding(){
                {
                    super.bind(observableValue);
                }

                @Override
                protected float computeValue() {
                    return observableNumberValue.floatValue();
                }
            };
        } else {
            observableFloatValue = new FloatBinding(){
                {
                    super.bind(observableValue);
                }

                @Override
                protected float computeValue() {
                    Number number = (Number)observableValue.getValue();
                    return number == null ? 0.0f : number.floatValue();
                }
            };
        }
        if (!observableFloatValue.equals(this.observable)) {
            this.unbind();
            this.observable = observableFloatValue;
            if (this.listener == null) {
                this.listener = new Listener(this);
            }
            this.observable.addListener(this.listener);
            this.markInvalid();
        }
    }

    @Override
    public void unbind() {
        if (this.observable != null) {
            this.value = this.observable.get();
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override
    public String toString() {
        Object object = this.getBean();
        String string = this.getName();
        StringBuilder stringBuilder = new StringBuilder("FloatProperty [");
        if (object != null) {
            stringBuilder.append("bean: ").append(object).append(", ");
        }
        if (string != null && !string.equals("")) {
            stringBuilder.append("name: ").append(string).append(", ");
        }
        if (this.isBound()) {
            stringBuilder.append("bound, ");
            if (this.valid) {
                stringBuilder.append("value: ").append(this.get());
            } else {
                stringBuilder.append("invalid");
            }
        } else {
            stringBuilder.append("value: ").append(this.get());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static class Listener
    implements InvalidationListener {
        private final WeakReference<FloatPropertyBase> wref;

        public Listener(FloatPropertyBase floatPropertyBase) {
            this.wref = new WeakReference<FloatPropertyBase>(floatPropertyBase);
        }

        @Override
        public void invalidated(Observable observable) {
            FloatPropertyBase floatPropertyBase = (FloatPropertyBase)this.wref.get();
            if (floatPropertyBase == null) {
                observable.removeListener(this);
            } else {
                floatPropertyBase.markInvalid();
            }
        }
    }
}

