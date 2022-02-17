/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.DateCellSkin;
import java.time.LocalDate;
import javafx.scene.control.Cell;
import javafx.scene.control.Skin;

public class DateCell
extends Cell<LocalDate> {
    private static final String DEFAULT_STYLE_CLASS = "date-cell";

    public DateCell() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override
    public void updateItem(LocalDate localDate, boolean bl) {
        super.updateItem(localDate, bl);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DateCellSkin(this);
    }
}

