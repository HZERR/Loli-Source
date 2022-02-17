/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property.adapter;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.adapter.DescriptorListenerCleaner;
import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

public final class JavaBeanDoubleProperty
extends DoubleProperty
implements JavaBeanProperty<Number> {
    private final PropertyDescriptor descriptor;
    private final PropertyDescriptor.Listener<Number> listener;
    private ObservableValue<? extends Number> observable = null;
    private ExpressionHelper<Number> helper = null;
    private final AccessControlContext acc = AccessController.getContext();

    JavaBeanDoubleProperty(PropertyDescriptor propertyDescriptor, Object object) {
        this.descriptor = propertyDescriptor;
        PropertyDescriptor propertyDescriptor2 = propertyDescriptor;
        propertyDescriptor2.getClass();
        this.listener = propertyDescriptor2.new PropertyDescriptor.Listener<Number>(object, this);
        propertyDescriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(propertyDescriptor, this.listener));
    }

    @Override
    public double get() {
        return AccessController.doPrivileged(() -> {
            try {
                return ((Number)MethodUtil.invoke(this.descriptor.getGetter(), this.getBean(), null)).doubleValue();
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new UndeclaredThrowableException(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new UndeclaredThrowableException(invocationTargetException);
            }
        }, this.acc);
    }

    @Override
    public void set(double d2) {
        if (this.isBound()) {
            throw new RuntimeException("A bound value cannot be set.");
        }
        AccessController.doPrivileged(() -> {
            try {
                MethodUtil.invoke(this.descriptor.getSetter(), this.getBean(), new Object[]{d2});
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new UndeclaredThrowableException(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new UndeclaredThrowableException(invocationTargetException);
            }
            return null;
        }, this.acc);
    }

    @Override
    public void bind(ObservableValue<? extends Number> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!observableValue.equals(this.observable)) {
            this.unbind();
            this.set(observableValue.getValue().doubleValue());
            this.observable = observableValue;
            this.observable.addListener(this.listener);
        }
    }

    @Override
    public void unbind() {
        if (this.observable != null) {
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override
    public boolean isBound() {
        return this.observable != null;
    }

    @Override
    public Object getBean() {
        return this.listener.getBean();
    }

    @Override
    public String getName() {
        return this.descriptor.getName();
    }

    @Override
    public void addListener(ChangeListener<? super Number> changeListener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> changeListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
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
    public void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper);
    }

    @Override
    public void dispose() {
        this.descriptor.removeListener(this.listener);
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
        if (this.isBound()) {
            stringBuilder.append("bound, ");
        }
        stringBuilder.append("value: ").append(this.get());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

