/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.property.ReadOnlyStringPropertyBase;
import javafx.beans.property.adapter.DescriptorListenerCleaner;
import javafx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import sun.reflect.misc.MethodUtil;

public final class ReadOnlyJavaBeanStringProperty
extends ReadOnlyStringPropertyBase
implements ReadOnlyJavaBeanProperty<String> {
    private final ReadOnlyPropertyDescriptor descriptor;
    private final ReadOnlyPropertyDescriptor.ReadOnlyListener<String> listener;
    private final AccessControlContext acc = AccessController.getContext();

    ReadOnlyJavaBeanStringProperty(ReadOnlyPropertyDescriptor readOnlyPropertyDescriptor, Object object) {
        this.descriptor = readOnlyPropertyDescriptor;
        ReadOnlyPropertyDescriptor readOnlyPropertyDescriptor2 = readOnlyPropertyDescriptor;
        readOnlyPropertyDescriptor2.getClass();
        this.listener = readOnlyPropertyDescriptor2.new ReadOnlyPropertyDescriptor.ReadOnlyListener<String>(object, this);
        readOnlyPropertyDescriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(readOnlyPropertyDescriptor, this.listener));
    }

    @Override
    public String get() {
        return AccessController.doPrivileged(() -> {
            try {
                return (String)MethodUtil.invoke(this.descriptor.getGetter(), this.getBean(), null);
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
    public Object getBean() {
        return this.listener.getBean();
    }

    @Override
    public String getName() {
        return this.descriptor.getName();
    }

    @Override
    public void fireValueChangedEvent() {
        super.fireValueChangedEvent();
    }

    @Override
    public void dispose() {
        this.descriptor.removeListener(this.listener);
    }
}
