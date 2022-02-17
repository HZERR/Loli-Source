/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import com.sun.javafx.property.PropertyReference;
import com.sun.javafx.scene.control.Logging;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;

public class TreeItemPropertyValueFactory<S, T>
implements Callback<TreeTableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {
    private final String property;
    private Class<?> columnClass;
    private String previousProperty;
    private PropertyReference<T> propertyRef;

    public TreeItemPropertyValueFactory(@NamedArg(value="property") String string) {
        this.property = string;
    }

    @Override
    public ObservableValue<T> call(TreeTableColumn.CellDataFeatures<S, T> cellDataFeatures) {
        TreeItem<S> treeItem = cellDataFeatures.getValue();
        return this.getCellDataReflectively(treeItem.getValue());
    }

    public final String getProperty() {
        return this.property;
    }

    private ObservableValue<T> getCellDataReflectively(S s2) {
        if (this.getProperty() == null || this.getProperty().isEmpty() || s2 == null) {
            return null;
        }
        try {
            if (this.columnClass == null || this.previousProperty == null || !this.columnClass.equals(s2.getClass()) || !this.previousProperty.equals(this.getProperty())) {
                this.columnClass = s2.getClass();
                this.previousProperty = this.getProperty();
                this.propertyRef = new PropertyReference(s2.getClass(), this.getProperty());
            }
            return this.propertyRef.getProperty(s2);
        }
        catch (IllegalStateException illegalStateException) {
            try {
                T t2 = this.propertyRef.get(s2);
                return new ReadOnlyObjectWrapper<T>(t2);
            }
            catch (IllegalStateException illegalStateException2) {
                PlatformLogger platformLogger = Logging.getControlsLogger();
                if (platformLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                    platformLogger.finest("Can not retrieve property '" + this.getProperty() + "' in TreeItemPropertyValueFactory: " + this + " with provided class type: " + s2.getClass(), illegalStateException);
                }
                return null;
            }
        }
    }
}

