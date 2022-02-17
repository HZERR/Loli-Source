/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.interpolator.KeyFrames;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;

public class TimelinePropertyBuilder<T> {
    private Object target;
    private final String propertyName;
    private T from;
    private boolean isFromCurrent;
    private T to;
    private PropertyInterpolator<T> interpolator;
    private PropertyGetter<T> getter;
    private PropertySetter<T> setter;
    private KeyFrames<T> keyFrames;

    TimelinePropertyBuilder(String propertyName) {
        this.propertyName = propertyName;
        this.isFromCurrent = false;
    }

    public TimelinePropertyBuilder<T> from(T startValue) {
        if (this.from != null) {
            throw new IllegalArgumentException("from() can only be called once");
        }
        if (this.isFromCurrent) {
            throw new IllegalArgumentException("from() cannot be called after fromCurrent()");
        }
        if (this.keyFrames != null) {
            throw new IllegalArgumentException("from() cannot be called after goingThrough()");
        }
        this.from = startValue;
        return this;
    }

    public TimelinePropertyBuilder<T> fromCurrent() {
        if (this.isFromCurrent) {
            throw new IllegalArgumentException("fromCurrent() can only be called once");
        }
        if (this.from != null) {
            throw new IllegalArgumentException("fromCurrent() cannot be called after from()");
        }
        if (this.keyFrames != null) {
            throw new IllegalArgumentException("fromCurrent() cannot be called after goingThrough()");
        }
        this.isFromCurrent = true;
        return this;
    }

    public TimelinePropertyBuilder<T> to(T endValue) {
        if (this.to != null) {
            throw new IllegalArgumentException("to() can only be called once");
        }
        if (this.keyFrames != null) {
            throw new IllegalArgumentException("to() cannot be called after goingThrough()");
        }
        this.to = endValue;
        return this;
    }

    public TimelinePropertyBuilder<T> on(Object object) {
        this.target = object;
        return this;
    }

    public TimelinePropertyBuilder<T> interpolatedWith(PropertyInterpolator<T> pInterpolator) {
        if (this.interpolator != null) {
            throw new IllegalArgumentException("interpolateWith() can only be called once");
        }
        this.interpolator = pInterpolator;
        return this;
    }

    public TimelinePropertyBuilder<T> setWith(PropertySetter<T> pSetter) {
        if (this.setter != null) {
            throw new IllegalArgumentException("setWith() can only be called once");
        }
        this.setter = pSetter;
        return this;
    }

    public TimelinePropertyBuilder<T> getWith(PropertyGetter<T> pGetter) {
        if (this.getter != null) {
            throw new IllegalArgumentException("getWith() can only be called once");
        }
        this.getter = pGetter;
        return this;
    }

    public TimelinePropertyBuilder<T> accessWith(PropertyAccessor<T> pAccessor) {
        if (this.setter != null || this.getter != null) {
            throw new IllegalArgumentException("accessWith() can only be called once");
        }
        this.setter = pAccessor;
        this.getter = pAccessor;
        return this;
    }

    public TimelinePropertyBuilder<T> goingThrough(KeyFrames<T> keyFrames) {
        if (this.keyFrames != null) {
            throw new IllegalArgumentException("goingThrough() can only be called once");
        }
        if (this.isFromCurrent) {
            throw new IllegalArgumentException("goingThrough() cannot be called after fromCurrent()");
        }
        if (this.from != null) {
            throw new IllegalArgumentException("goingThrough() cannot be called after from()");
        }
        if (this.to != null) {
            throw new IllegalArgumentException("goingThrough() cannot be called after to()");
        }
        this.keyFrames = keyFrames;
        return this;
    }

    AbstractFieldInfo getFieldInfo(Timeline timeline) {
        if (this.target == null) {
            this.target = timeline.mainObject;
        }
        if (this.keyFrames != null) {
            return new KeyFramesFieldInfo(this.target, this.propertyName, this.keyFrames, this.setter);
        }
        if (this.isFromCurrent) {
            if (this.interpolator == null) {
                this.interpolator = TridentConfig.getInstance().getPropertyInterpolator(this.to);
                if (this.interpolator == null) {
                    throw new IllegalArgumentException("No interpolator found for " + this.to.getClass().getName());
                }
            }
            return new GenericFieldInfoTo(this.target, this.propertyName, this.to, this.interpolator, this.getter, this.setter);
        }
        if (this.interpolator == null) {
            this.interpolator = TridentConfig.getInstance().getPropertyInterpolator(this.from, this.to);
            if (this.interpolator == null) {
                throw new IllegalArgumentException("No interpolator found for " + this.from.getClass().getName() + ":" + this.to.getClass().getName());
            }
        }
        return new GenericFieldInfo(this.target, this.propertyName, this.from, this.to, this.interpolator, this.setter);
    }

    private static <T> PropertyGetter<T> getPropertyGetter(Object obj, String fieldName, PropertyGetter<T> pGetter) {
        if (pGetter != null) {
            return pGetter;
        }
        return new DefaultPropertyGetter(obj, fieldName);
    }

    private static <T> PropertySetter<T> getPropertySetter(Object obj, String fieldName, PropertySetter<T> pSetter) {
        if (pSetter != null) {
            return pSetter;
        }
        return new DefaultPropertySetter(obj, fieldName);
    }

    private static Method getSetter(Object object, String propertyName) {
        String setterMethodName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        for (Class<?> oClazz = object.getClass(); oClazz != null; oClazz = oClazz.getSuperclass()) {
            for (Method m2 : oClazz.getMethods()) {
                if (!setterMethodName.equals(m2.getName()) || m2.getParameterTypes().length != 1 || m2.getReturnType() != Void.TYPE || Modifier.isStatic(m2.getModifiers())) continue;
                return m2;
            }
        }
        return null;
    }

    private static Method getGetter(Object object, String propertyName) {
        String getterMethodName = "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        for (Class<?> oClazz = object.getClass(); oClazz != null; oClazz = oClazz.getSuperclass()) {
            for (Method m2 : oClazz.getMethods()) {
                if (!getterMethodName.equals(m2.getName()) || m2.getParameterTypes().length != 0 || Modifier.isStatic(m2.getModifiers())) continue;
                return m2;
            }
        }
        return null;
    }

    private class KeyFramesFieldInfo
    extends AbstractFieldInfo<Object> {
        KeyFrames keyFrames;

        KeyFramesFieldInfo(Object obj, String fieldName, KeyFrames keyFrames, PropertySetter propertySetter) {
            super(obj, fieldName, null, TimelinePropertyBuilder.getPropertySetter(obj, fieldName, propertySetter));
            this.keyFrames = keyFrames;
        }

        @Override
        void onStart() {
        }

        @Override
        void updateFieldValue(float timelinePosition) {
            if (this.setter != null) {
                try {
                    Object value = this.keyFrames.getValue(timelinePosition);
                    this.setter.set(this.object, this.fieldName, value);
                }
                catch (Throwable exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    private class GenericFieldInfo
    extends AbstractFieldInfo<Object> {
        private PropertyInterpolator propertyInterpolator;

        GenericFieldInfo(Object obj, String fieldName, Object from, Object to, PropertyInterpolator propertyInterpolator, PropertySetter propertySetter) {
            super(obj, fieldName, null, TimelinePropertyBuilder.getPropertySetter(obj, fieldName, propertySetter));
            this.propertyInterpolator = propertyInterpolator;
            this.setValues(from, to);
        }

        @Override
        void onStart() {
        }

        @Override
        void updateFieldValue(float timelinePosition) {
            try {
                Object value = this.propertyInterpolator.interpolate(this.from, this.to, timelinePosition);
                this.setter.set(this.object, this.fieldName, value);
            }
            catch (Throwable exc) {
                System.err.println("Exception occurred in updating field '" + this.fieldName + "' of object " + this.object.getClass().getCanonicalName() + " at timeline position " + timelinePosition);
                exc.printStackTrace();
            }
        }
    }

    private class GenericFieldInfoTo
    extends AbstractFieldInfo<Object> {
        private PropertyInterpolator propertyInterpolator;
        private Object to;

        GenericFieldInfoTo(Object obj, String fieldName, Object to, PropertyInterpolator propertyInterpolator, PropertyGetter propertyGetter, PropertySetter propertySetter) {
            super(obj, fieldName, TimelinePropertyBuilder.getPropertyGetter(obj, fieldName, propertyGetter), TimelinePropertyBuilder.getPropertySetter(obj, fieldName, propertySetter));
            this.propertyInterpolator = propertyInterpolator;
            this.to = to;
        }

        @Override
        void onStart() {
            this.from = this.getter.get(this.object, this.fieldName);
        }

        @Override
        void updateFieldValue(float timelinePosition) {
            try {
                Object value = this.propertyInterpolator.interpolate(this.from, this.to, timelinePosition);
                this.setter.set(this.object, this.fieldName, value);
            }
            catch (Throwable exc) {
                System.err.println("Exception occurred in updating field '" + this.fieldName + "' of object " + this.object.getClass().getCanonicalName() + " at timeline position " + timelinePosition);
                exc.printStackTrace();
            }
        }
    }

    abstract class AbstractFieldInfo<T> {
        protected Object object;
        protected String fieldName;
        protected PropertyGetter getter;
        protected PropertySetter setter;
        protected T from;
        protected T to;

        AbstractFieldInfo(Object obj, String fieldName, PropertyGetter<T> pGetter, PropertySetter<T> pSetter) {
            this.object = obj;
            this.fieldName = fieldName;
            this.getter = pGetter;
            this.setter = pSetter;
        }

        void setValues(T from, T to) {
            this.from = from;
            this.to = to;
        }

        abstract void onStart();

        abstract void updateFieldValue(float var1);
    }

    public static class DefaultPropertyGetter<T>
    implements PropertyGetter<T> {
        private Method getterMethod;

        public DefaultPropertyGetter(Object obj, String fieldName) {
            this.getterMethod = TimelinePropertyBuilder.getGetter(obj, fieldName);
        }

        @Override
        public T get(Object obj, String fieldName) {
            try {
                return (T)this.getterMethod.invoke(obj, new Object[0]);
            }
            catch (Throwable t2) {
                throw new RuntimeException("Unable to get the value of the field '" + fieldName + "'", t2);
            }
        }
    }

    public static class DefaultPropertySetter<T>
    implements PropertySetter<T> {
        private Method setterMethod;

        public DefaultPropertySetter(Object obj, String fieldName) {
            this.setterMethod = TimelinePropertyBuilder.getSetter(obj, fieldName);
        }

        @Override
        public void set(Object obj, String fieldName, T value) {
            try {
                this.setterMethod.invoke(obj, value);
            }
            catch (Throwable t2) {
                throw new RuntimeException("Unable to set the value of the field '" + fieldName + "'", t2);
            }
        }
    }

    public static interface PropertyAccessor<T>
    extends PropertyGetter<T>,
    PropertySetter<T> {
    }

    public static interface PropertyGetter<T> {
        public T get(Object var1, String var2);
    }

    public static interface PropertySetter<T> {
        public void set(Object var1, String var2, T var3);
    }
}

