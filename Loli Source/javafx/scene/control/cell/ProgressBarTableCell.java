/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProgressBarTableCell<S>
extends TableCell<S, Double> {
    private final ProgressBar progressBar;
    private ObservableValue<Double> observable;

    public static <S> Callback<TableColumn<S, Double>, TableCell<S, Double>> forTableColumn() {
        return tableColumn -> new ProgressBarTableCell();
    }

    public ProgressBarTableCell() {
        this.getStyleClass().add("progress-bar-table-cell");
        this.progressBar = new ProgressBar();
        this.progressBar.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public void updateItem(Double d2, boolean bl) {
        super.updateItem(d2, bl);
        if (bl) {
            this.setGraphic(null);
        } else {
            this.progressBar.progressProperty().unbind();
            TableColumn tableColumn = this.getTableColumn();
            ObservableValue<Object> observableValue = this.observable = tableColumn == null ? null : tableColumn.getCellObservableValue(this.getIndex());
            if (this.observable != null) {
                this.progressBar.progressProperty().bind(this.observable);
            } else if (d2 != null) {
                this.progressBar.setProgress(d2);
            }
            this.setGraphic(this.progressBar);
        }
    }
}

