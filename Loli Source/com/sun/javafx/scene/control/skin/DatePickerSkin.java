/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DatePickerBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerHijrahContent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.HijrahChronology;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class DatePickerSkin
extends ComboBoxPopupControl<LocalDate> {
    private DatePicker datePicker;
    private TextField displayNode;
    private DatePickerContent datePickerContent;

    public DatePickerSkin(DatePicker datePicker) {
        super(datePicker, new DatePickerBehavior(datePicker));
        this.datePicker = datePicker;
        this.arrow.paddingProperty().addListener(new InvalidationListener(){
            private boolean rounding = false;

            @Override
            public void invalidated(Observable observable) {
                Insets insets;
                Insets insets2;
                if (!this.rounding && !(insets2 = new Insets(Math.round((insets = DatePickerSkin.this.arrow.getPadding()).getTop()), Math.round(insets.getRight()), Math.round(insets.getBottom()), Math.round(insets.getLeft()))).equals(insets)) {
                    this.rounding = true;
                    DatePickerSkin.this.arrow.setPadding(insets2);
                    this.rounding = false;
                }
            }
        });
        this.registerChangeListener(datePicker.chronologyProperty(), "CHRONOLOGY");
        this.registerChangeListener(datePicker.converterProperty(), "CONVERTER");
        this.registerChangeListener(datePicker.dayCellFactoryProperty(), "DAY_CELL_FACTORY");
        this.registerChangeListener(datePicker.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS");
        this.registerChangeListener(datePicker.valueProperty(), "VALUE");
    }

    @Override
    public Node getPopupContent() {
        if (this.datePickerContent == null) {
            this.datePickerContent = this.datePicker.getChronology() instanceof HijrahChronology ? new DatePickerHijrahContent(this.datePicker) : new DatePickerContent(this.datePicker);
        }
        return this.datePickerContent;
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        return 50.0;
    }

    @Override
    protected void focusLost() {
    }

    @Override
    public void show() {
        super.show();
        this.datePickerContent.clearFocus();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        if ("CHRONOLOGY".equals(string) || "DAY_CELL_FACTORY".equals(string)) {
            this.updateDisplayNode();
            this.datePickerContent = null;
            this.popup = null;
        } else if ("CONVERTER".equals(string)) {
            this.updateDisplayNode();
        } else if ("EDITOR".equals(string)) {
            this.getEditableInputNode();
        } else if ("SHOWING".equals(string)) {
            if (this.datePicker.isShowing()) {
                if (this.datePickerContent != null) {
                    LocalDate localDate = (LocalDate)this.datePicker.getValue();
                    this.datePickerContent.displayedYearMonthProperty().set(localDate != null ? YearMonth.from(localDate) : YearMonth.now());
                    this.datePickerContent.updateValues();
                }
                this.show();
            } else {
                this.hide();
            }
        } else if ("SHOW_WEEK_NUMBERS".equals(string)) {
            if (this.datePickerContent != null) {
                this.datePickerContent.updateGrid();
                this.datePickerContent.updateWeeknumberDateCells();
            }
        } else if ("VALUE".equals(string)) {
            this.updateDisplayNode();
            if (this.datePickerContent != null) {
                LocalDate localDate = (LocalDate)this.datePicker.getValue();
                this.datePickerContent.displayedYearMonthProperty().set(localDate != null ? YearMonth.from(localDate) : YearMonth.now());
                this.datePickerContent.updateValues();
            }
            this.datePicker.fireEvent(new ActionEvent());
        } else {
            super.handleControlPropertyChanged(string);
        }
    }

    @Override
    protected TextField getEditor() {
        return ((DatePicker)this.getSkinnable()).getEditor();
    }

    @Override
    protected StringConverter<LocalDate> getConverter() {
        return ((DatePicker)this.getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        if (this.displayNode == null) {
            this.displayNode = this.getEditableInputNode();
            this.displayNode.getStyleClass().add("date-picker-display-node");
            this.updateDisplayNode();
        }
        this.displayNode.setEditable(this.datePicker.isEditable());
        return this.displayNode;
    }

    public void syncWithAutoUpdate() {
        if (!this.getPopup().isShowing() && this.datePicker.isShowing()) {
            this.datePicker.hide();
        }
    }
}

