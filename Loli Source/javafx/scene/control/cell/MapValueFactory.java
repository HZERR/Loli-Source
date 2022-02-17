/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import java.util.Map;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class MapValueFactory<T>
implements Callback<TableColumn.CellDataFeatures<Map, T>, ObservableValue<T>> {
    private final Object key;

    public MapValueFactory(@NamedArg(value="key") Object object) {
        this.key = object;
    }

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<Map, T> cellDataFeatures) {
        Map map = cellDataFeatures.getValue();
        Object v2 = map.get(this.key);
        if (v2 instanceof ObservableValue) {
            return (ObservableValue)v2;
        }
        if (v2 instanceof Boolean) {
            return new ReadOnlyBooleanWrapper((Boolean)v2);
        }
        if (v2 instanceof Integer) {
            return new ReadOnlyIntegerWrapper((Integer)v2);
        }
        if (v2 instanceof Float) {
            return new ReadOnlyFloatWrapper(((Float)v2).floatValue());
        }
        if (v2 instanceof Long) {
            return new ReadOnlyLongWrapper((Long)v2);
        }
        if (v2 instanceof Double) {
            return new ReadOnlyDoubleWrapper((Double)v2);
        }
        if (v2 instanceof String) {
            return new ReadOnlyStringWrapper((String)v2);
        }
        return new ReadOnlyObjectWrapper(v2);
    }
}

