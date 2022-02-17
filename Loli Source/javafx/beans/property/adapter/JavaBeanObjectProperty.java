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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.adapter.DescriptorListenerCleaner;
import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

public final class JavaBeanObjectProperty<T>
extends ObjectProperty<T>
implements JavaBeanProperty<T> {
    private final PropertyDescriptor descriptor;
    private final PropertyDescriptor.Listener<T> listener;
    private ObservableValue<? extends T> observable = null;
    private ExpressionHelper<T> helper = null;
    private final AccessControlContext acc = AccessController.getContext();

    JavaBeanObjectProperty(PropertyDescriptor propertyDescriptor, Object object) {
        this.descriptor = propertyDescriptor;
        PropertyDescriptor propertyDescriptor2 = propertyDescriptor;
        propertyDescriptor2.getClass();
        this.listener = propertyDescriptor2.new PropertyDescriptor.Listener(object, this);
        propertyDescriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(propertyDescriptor, this.listener));
    }

    @Override
    public T get() {
        return (T)AccessController.doPrivileged(() -> {
            try {
                return MethodUtil.invoke(this.descriptor.getGetter(), this.getBean(), null);
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
    public void set(T t2) {
        if (this.isBound()) {
            throw new RuntimeException("A bound value cannot be set.");
        }
        AccessController.doPrivileged(() -> {
            try {
                MethodUtil.invoke(this.descriptor.getSetter(), this.getBean(), new Object[]{t2});
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
    public void bind(ObservableValue<? extends T> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!observableValue.equals(this.observable)) {
            this.unbind();
            this.set(observableValue.getValue());
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
    public void addListener(ChangeListener<? super T> changeListener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> changeListener) {
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
        StringBuilder stringBuilder = new StringBuilder("ObjectProperty [");
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

