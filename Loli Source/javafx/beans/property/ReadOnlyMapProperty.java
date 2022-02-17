/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.property;

import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.MapExpression;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableMap;

public abstract class ReadOnlyMapProperty<K, V>
extends MapExpression<K, V>
implements ReadOnlyProperty<ObservableMap<K, V>> {
    public void bindContentBidirectional(ObservableMap<K, V> observableMap) {
        Bindings.bindContentBidirectional(this, observableMap);
    }

    public void unbindContentBidirectional(Object object) {
        Bindings.unbindContentBidirectional(this, object);
    }

    public void bindContent(ObservableMap<K, V> observableMap) {
        Bindings.bindContent(this, observableMap);
    }

    public void unbindContent(Object object) {
        Bindings.unbindContent(this, object);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Map)) {
            return false;
        }
        Map map = (Map)object;
        if (map.size() != this.size()) {
            return false;
        }
        try {
            for (Map.Entry entry : this.entrySet()) {
                Object k2 = entry.getKey();
                Object v2 = entry.getValue();
                if (!(v2 == null ? map.get(k2) != null || !map.containsKey(k2) : !v2.equals(map.get(k2)))) continue;
                return false;
            }
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int n2 = 0;
        for (Map.Entry entry : this.entrySet()) {
            n2 += entry.hashCode();
        }
        return n2;
    }

    public String toString() {
        Object object = this.getBean();
        String string = this.getName();
        StringBuilder stringBuilder = new StringBuilder("ReadOnlyMapProperty [");
        if (object != null) {
            stringBuilder.append("bean: ").append(object).append(", ");
        }
        if (string != null && !string.equals("")) {
            stringBuilder.append("name: ").append(string).append(", ");
        }
        stringBuilder.append("value: ").append(this.get()).append("]");
        return stringBuilder.toString();
    }
}

