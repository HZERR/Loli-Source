/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.beans.NamedArg;
import javafx.scene.control.TableColumnBase;

public class ResizeFeaturesBase<S> {
    private final TableColumnBase<S, ?> column;
    private final Double delta;

    public ResizeFeaturesBase(@NamedArg(value="column") TableColumnBase<S, ?> tableColumnBase, @NamedArg(value="delta") Double d2) {
        this.column = tableColumnBase;
        this.delta = d2;
    }

    public TableColumnBase<S, ?> getColumn() {
        return this.column;
    }

    public Double getDelta() {
        return this.delta;
    }
}

