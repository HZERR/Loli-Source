/*
 * Decompiled with CFR 0.150.
 */
package javafx.util.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javafx.util.converter.DateTimeStringConverter;

public class DateStringConverter
extends DateTimeStringConverter {
    public DateStringConverter() {
        this(null, null, null, 2);
    }

    public DateStringConverter(int n2) {
        this(null, null, null, n2);
    }

    public DateStringConverter(Locale locale) {
        this(locale, null, null, 2);
    }

    public DateStringConverter(Locale locale, int n2) {
        this(locale, null, null, n2);
    }

    public DateStringConverter(String string) {
        this(null, string, null, 2);
    }

    public DateStringConverter(Locale locale, String string) {
        this(locale, string, null, 2);
    }

    public DateStringConverter(DateFormat dateFormat) {
        this(null, null, dateFormat, 2);
    }

    private DateStringConverter(Locale locale, String string, DateFormat dateFormat, int n2) {
        super(locale, string, dateFormat, n2, 2);
    }

    @Override
    protected DateFormat getDateFormat() {
        DateFormat dateFormat = null;
        if (this.dateFormat != null) {
            return this.dateFormat;
        }
        dateFormat = this.pattern != null ? new SimpleDateFormat(this.pattern, this.locale) : DateFormat.getDateInstance(this.dateStyle, this.locale);
        dateFormat.setLenient(false);
        return dateFormat;
    }
}

