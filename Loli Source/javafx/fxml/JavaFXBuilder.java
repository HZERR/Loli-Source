/*
 * Decompiled with CFR 0.150.
 */
package javafx.fxml;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.util.Builder;
import sun.reflect.misc.MethodUtil;

final class JavaFXBuilder {
    private static final Object[] NO_ARGS = new Object[0];
    private static final Class<?>[] NO_SIG = new Class[0];
    private final Class<?> builderClass;
    private final Method createMethod;
    private final Method buildMethod;
    private final Map<String, Method> methods = new HashMap<String, Method>();
    private final Map<String, Method> getters = new HashMap<String, Method>();
    private final Map<String, Method> setters = new HashMap<String, Method>();

    JavaFXBuilder() {
        this.builderClass = null;
        this.createMethod = null;
        this.buildMethod = null;
    }

    JavaFXBuilder(Class<?> class_) throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.builderClass = class_;
        this.createMethod = MethodUtil.getMethod(class_, "create", NO_SIG);
        this.buildMethod = MethodUtil.getMethod(class_, "build", NO_SIG);
        assert (Modifier.isStatic(this.createMethod.getModifiers()));
        assert (!Modifier.isStatic(this.buildMethod.getModifiers()));
    }

    Builder<Object> createBuilder() {
        return new ObjectBuilder();
    }

    private Method findMethod(String string) {
        if (string.length() > 1 && Character.isUpperCase(string.charAt(1))) {
            string = Character.toUpperCase(string.charAt(0)) + string.substring(1);
        }
        for (Method method : MethodUtil.getMethods(this.builderClass)) {
            if (!method.getName().equals(string)) continue;
            return method;
        }
        throw new IllegalArgumentException("Method " + string + " could not be found at class " + this.builderClass.getName());
    }

    public Class<?> getTargetClass() {
        return this.buildMethod.getReturnType();
    }

    final class ObjectBuilder
    extends AbstractMap<String, Object>
    implements Builder<Object> {
        private final Map<String, Object> containers = new HashMap<String, Object>();
        private Object builder = null;
        private Map<Object, Object> properties;

        private ObjectBuilder() {
            try {
                this.builder = MethodUtil.invoke(JavaFXBuilder.this.createMethod, null, NO_ARGS);
            }
            catch (Exception exception) {
                throw new RuntimeException("Creation of the builder " + JavaFXBuilder.this.builderClass.getName() + " failed.", exception);
            }
        }

        @Override
        public Object build() {
            Object object;
            for (Map.Entry<String, Object> entry : this.containers.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
            try {
                object = MethodUtil.invoke(JavaFXBuilder.this.buildMethod, this.builder, NO_ARGS);
                if (this.properties != null && object instanceof Node) {
                    ((Node)object).getProperties().putAll(this.properties);
                }
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
            finally {
                this.builder = null;
            }
            return object;
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsKey(Object object) {
            return this.getTemporaryContainer(object.toString()) != null;
        }

        @Override
        public boolean containsValue(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(Object object) {
            return this.getTemporaryContainer(object.toString());
        }

        @Override
        public Object put(String string, Object object) {
            if (Node.class.isAssignableFrom(JavaFXBuilder.this.getTargetClass()) && "properties".equals(string)) {
                this.properties = (Map)object;
                return null;
            }
            try {
                Method method = (Method)JavaFXBuilder.this.methods.get(string);
                if (method == null) {
                    method = JavaFXBuilder.this.findMethod(string);
                    JavaFXBuilder.this.methods.put(string, method);
                }
                try {
                    Class<?> class_ = method.getParameterTypes()[0];
                    if (class_.isArray()) {
                        List<String> list = object instanceof List ? (List<String>)object : Arrays.asList(object.toString().split(","));
                        Class<?> class_2 = class_.getComponentType();
                        Object object2 = Array.newInstance(class_2, list.size());
                        for (int i2 = 0; i2 < list.size(); ++i2) {
                            Array.set(object2, i2, BeanAdapter.coerce(list.get(i2), class_2));
                        }
                        object = object2;
                    }
                    MethodUtil.invoke(method, this.builder, new Object[]{BeanAdapter.coerce(object, class_)});
                }
                catch (Exception exception) {
                    Logger.getLogger(JavaFXBuilder.class.getName()).log(Level.WARNING, "Method " + method.getName() + " failed", exception);
                }
                return null;
            }
            catch (Exception exception) {
                Logger.getLogger(JavaFXBuilder.class.getName()).log(Level.WARNING, "Failed to set " + JavaFXBuilder.this.getTargetClass() + "." + string + " using " + JavaFXBuilder.this.builderClass, exception);
                return null;
            }
        }

        Object getReadOnlyProperty(String string) {
            GenericDeclaration genericDeclaration;
            Class<?> class_;
            if (JavaFXBuilder.this.setters.get(string) != null) {
                return null;
            }
            Method method = (Method)JavaFXBuilder.this.getters.get(string);
            if (method == null) {
                class_ = null;
                genericDeclaration = JavaFXBuilder.this.getTargetClass();
                String string2 = Character.toUpperCase(string.charAt(0)) + string.substring(1);
                try {
                    method = MethodUtil.getMethod(genericDeclaration, "get" + string2, NO_SIG);
                    class_ = MethodUtil.getMethod(genericDeclaration, "set" + string2, new Class[]{method.getReturnType()});
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (method != null) {
                    JavaFXBuilder.this.getters.put(string, method);
                    JavaFXBuilder.this.setters.put(string, class_);
                }
                if (class_ != null) {
                    return null;
                }
            }
            if (method == null) {
                genericDeclaration = JavaFXBuilder.this.findMethod(string);
                if (genericDeclaration == null) {
                    return null;
                }
                class_ = ((Method)genericDeclaration).getParameterTypes()[0];
                if (class_.isArray()) {
                    class_ = List.class;
                }
            } else {
                class_ = method.getReturnType();
            }
            if (ObservableMap.class.isAssignableFrom(class_)) {
                return FXCollections.observableMap(new HashMap());
            }
            if (Map.class.isAssignableFrom(class_)) {
                return new HashMap();
            }
            if (ObservableList.class.isAssignableFrom(class_)) {
                return FXCollections.observableArrayList();
            }
            if (List.class.isAssignableFrom(class_)) {
                return new ArrayList();
            }
            if (Set.class.isAssignableFrom(class_)) {
                return new HashSet();
            }
            return null;
        }

        public Object getTemporaryContainer(String string) {
            Object object = this.containers.get(string);
            if (object == null && (object = this.getReadOnlyProperty(string)) != null) {
                this.containers.put(string, object);
            }
            return object;
        }

        @Override
        public Object remove(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> map) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<String> keySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<Object> values() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Map.Entry<String, Object>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }
}

