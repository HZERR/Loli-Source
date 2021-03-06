/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

class DatePickerHijrahContent
extends DatePickerContent {
    private Label hijrahMonthYearLabel;

    DatePickerHijrahContent(DatePicker datePicker) {
        super(datePicker);
    }

    @Override
    protected Chronology getPrimaryChronology() {
        return IsoChronology.INSTANCE;
    }

    @Override
    protected BorderPane createMonthYearPane() {
        BorderPane borderPane = super.createMonthYearPane();
        this.hijrahMonthYearLabel = new Label();
        this.hijrahMonthYearLabel.getStyleClass().add("secondary-label");
        borderPane.setBottom(this.hijrahMonthYearLabel);
        BorderPane.setAlignment(this.hijrahMonthYearLabel, Pos.CENTER);
        return borderPane;
    }

    @Override
    protected void updateMonthYearPane() {
        super.updateMonthYearPane();
        Locale locale = this.getLocale();
        HijrahChronology hijrahChronology = HijrahChronology.INSTANCE;
        long l2 = -1L;
        long l3 = -1L;
        String string = null;
        String string2 = null;
        String string3 = null;
        YearMonth yearMonth = (YearMonth)this.displayedYearMonthProperty().get();
        for (DateCell dateCell : this.dayCells) {
            LocalDate localDate = this.dayCellDate(dateCell);
            if (!yearMonth.equals(YearMonth.from(localDate))) continue;
            try {
                HijrahDate hijrahDate = hijrahChronology.date(localDate);
                long l4 = hijrahDate.getLong(ChronoField.MONTH_OF_YEAR);
                long l5 = hijrahDate.getLong(ChronoField.YEAR);
                if (string3 != null && l4 == l2) continue;
                String string4 = this.monthFormatter.withLocale(locale).withChronology(hijrahChronology).withDecimalStyle(DecimalStyle.of(locale)).format(hijrahDate);
                String string5 = this.yearFormatter.withLocale(locale).withChronology(hijrahChronology).withDecimalStyle(DecimalStyle.of(locale)).format(hijrahDate);
                if (string3 == null) {
                    l2 = l4;
                    l3 = l5;
                    string = string4;
                    string2 = string5;
                    string3 = string + " " + string2;
                    continue;
                }
                if (l5 > l3) {
                    string3 = string + " " + string2 + " - " + string4 + " " + string5;
                    break;
                }
                string3 = string + " - " + string4 + " " + string2;
                break;
            }
            catch (DateTimeException dateTimeException) {
            }
        }
        this.hijrahMonthYearLabel.setText(string3);
    }

    @Override
    protected void createDayCells() {
        super.createDayCells();
        for (DateCell dateCell : this.dayCells) {
            Text text = new Text();
            dateCell.getProperties().put("DateCell.secondaryText", text);
        }
    }

    @Override
    void updateDayCells() {
        super.updateDayCells();
        Locale locale = this.getLocale();
        HijrahChronology hijrahChronology = HijrahChronology.INSTANCE;
        int n2 = -1;
        int n3 = -1;
        int n4 = -1;
        boolean bl = false;
        for (DateCell dateCell : this.dayCells) {
            Text text = (Text)dateCell.getProperties().get("DateCell.secondaryText");
            dateCell.getStyleClass().add("hijrah-day-cell");
            text.getStyleClass().setAll("text", "secondary-text");
            try {
                HijrahDate hijrahDate = hijrahChronology.date(this.dayCellDate(dateCell));
                String string = this.dayCellFormatter.withLocale(locale).withChronology(hijrahChronology).withDecimalStyle(DecimalStyle.of(locale)).format(hijrahDate);
                text.setText(string);
                dateCell.requestLayout();
            }
            catch (DateTimeException dateTimeException) {
                text.setText(" ");
                dateCell.setDisable(true);
            }
        }
    }
}

