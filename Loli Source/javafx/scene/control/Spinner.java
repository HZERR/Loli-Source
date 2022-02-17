/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.SpinnerSkin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class Spinner<T>
extends Control {
    private static final String DEFAULT_STYLE_CLASS = "spinner";
    public static final String STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL = "arrows-on-right-horizontal";
    public static final String STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL = "arrows-on-left-vertical";
    public static final String STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL = "arrows-on-left-horizontal";
    public static final String STYLE_CLASS_SPLIT_ARROWS_VERTICAL = "split-arrows-vertical";
    public static final String STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL = "split-arrows-horizontal";
    private ReadOnlyObjectWrapper<T> value = new ReadOnlyObjectWrapper(this, "value");
    private ObjectProperty<SpinnerValueFactory<T>> valueFactory = new SimpleObjectProperty<SpinnerValueFactory<T>>(this, "valueFactory"){

        @Override
        protected void invalidated() {
            Spinner.this.value.unbind();
            SpinnerValueFactory spinnerValueFactory = (SpinnerValueFactory)this.get();
            if (spinnerValueFactory != null) {
                Spinner.this.value.bind(spinnerValueFactory.valueProperty());
            }
        }
    };
    private BooleanProperty editable;
    private TextField textField;
    private ReadOnlyObjectWrapper<TextField> editor;

    public Spinner() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.SPINNER);
        this.getEditor().setOnAction(actionEvent -> {
            StringConverter<T> stringConverter;
            String string = this.getEditor().getText();
            SpinnerValueFactory<T> spinnerValueFactory = this.getValueFactory();
            if (spinnerValueFactory != null && (stringConverter = spinnerValueFactory.getConverter()) != null) {
                T t2 = stringConverter.fromString(string);
                spinnerValueFactory.setValue(t2);
            }
        });
        this.getEditor().editableProperty().bind(this.editableProperty());
        this.value.addListener((observableValue, object, object2) -> this.setText(object2));
        this.getProperties().addListener(change -> {
            if (change.wasAdded() && change.getKey() == "FOCUSED") {
                this.setFocused((Boolean)change.getValueAdded());
                this.getProperties().remove("FOCUSED");
            }
        });
    }

    public Spinner(@NamedArg(value="min") int n2, @NamedArg(value="max") int n3, @NamedArg(value="initialValue") int n4) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(n2, n3, n4));
    }

    public Spinner(@NamedArg(value="min") int n2, @NamedArg(value="max") int n3, @NamedArg(value="initialValue") int n4, @NamedArg(value="amountToStepBy") int n5) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(n2, n3, n4, n5));
    }

    public Spinner(@NamedArg(value="min") double d2, @NamedArg(value="max") double d3, @NamedArg(value="initialValue") double d4) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(d2, d3, d4));
    }

    public Spinner(@NamedArg(value="min") double d2, @NamedArg(value="max") double d3, @NamedArg(value="initialValue") double d4, @NamedArg(value="amountToStepBy") double d5) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(d2, d3, d4, d5));
    }

    Spinner(@NamedArg(value="min") LocalDate localDate, @NamedArg(value="max") LocalDate localDate2, @NamedArg(value="initialValue") LocalDate localDate3) {
        this(new SpinnerValueFactory.LocalDateSpinnerValueFactory(localDate, localDate2, localDate3));
    }

    Spinner(@NamedArg(value="min") LocalDate localDate, @NamedArg(value="max") LocalDate localDate2, @NamedArg(value="initialValue") LocalDate localDate3, @NamedArg(value="amountToStepBy") long l2, @NamedArg(value="temporalUnit") TemporalUnit temporalUnit) {
        this(new SpinnerValueFactory.LocalDateSpinnerValueFactory(localDate, localDate2, localDate3, l2, temporalUnit));
    }

    Spinner(@NamedArg(value="min") LocalTime localTime, @NamedArg(value="max") LocalTime localTime2, @NamedArg(value="initialValue") LocalTime localTime3) {
        this(new SpinnerValueFactory.LocalTimeSpinnerValueFactory(localTime, localTime2, localTime3));
    }

    Spinner(@NamedArg(value="min") LocalTime localTime, @NamedArg(value="max") LocalTime localTime2, @NamedArg(value="initialValue") LocalTime localTime3, @NamedArg(value="amountToStepBy") long l2, @NamedArg(value="temporalUnit") TemporalUnit temporalUnit) {
        this(new SpinnerValueFactory.LocalTimeSpinnerValueFactory(localTime, localTime2, localTime3, l2, temporalUnit));
    }

    public Spinner(@NamedArg(value="items") ObservableList<T> observableList) {
        this(new SpinnerValueFactory.ListSpinnerValueFactory<T>(observableList));
    }

    public Spinner(@NamedArg(value="valueFactory") SpinnerValueFactory<T> spinnerValueFactory) {
        this();
        this.setValueFactory(spinnerValueFactory);
    }

    public void increment() {
        this.increment(1);
    }

    public void increment(int n2) {
        SpinnerValueFactory<T> spinnerValueFactory = this.getValueFactory();
        if (spinnerValueFactory == null) {
            throw new IllegalStateException("Can't increment Spinner with a null SpinnerValueFactory");
        }
        this.commitEditorText();
        spinnerValueFactory.increment(n2);
    }

    public void decrement() {
        this.decrement(1);
    }

    public void decrement(int n2) {
        SpinnerValueFactory<T> spinnerValueFactory = this.getValueFactory();
        if (spinnerValueFactory == null) {
            throw new IllegalStateException("Can't decrement Spinner with a null SpinnerValueFactory");
        }
        this.commitEditorText();
        spinnerValueFactory.decrement(n2);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SpinnerSkin(this);
    }

    public final T getValue() {
        return this.value.get();
    }

    public final ReadOnlyObjectProperty<T> valueProperty() {
        return this.value;
    }

    public final void setValueFactory(SpinnerValueFactory<T> spinnerValueFactory) {
        this.valueFactory.setValue(spinnerValueFactory);
    }

    public final SpinnerValueFactory<T> getValueFactory() {
        return (SpinnerValueFactory)this.valueFactory.get();
    }

    public final ObjectProperty<SpinnerValueFactory<T>> valueFactoryProperty() {
        return this.valueFactory;
    }

    public final void setEditable(boolean bl) {
        this.editableProperty().set(bl);
    }

    public final boolean isEditable() {
        return this.editable == null ? true : this.editable.get();
    }

    public final BooleanProperty editableProperty() {
        if (this.editable == null) {
            this.editable = new SimpleBooleanProperty(this, "editable", false);
        }
        return this.editable;
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (this.editor == null) {
            this.editor = new ReadOnlyObjectWrapper(this, "editor");
            this.textField = new ComboBoxPopupControl.FakeFocusTextField();
            this.editor.set(this.textField);
        }
        return this.editor.getReadOnlyProperty();
    }

    public final TextField getEditor() {
        return (TextField)this.editorProperty().get();
    }

    private void setText(T t2) {
        StringConverter<T> stringConverter;
        String string = null;
        SpinnerValueFactory<T> spinnerValueFactory = this.getValueFactory();
        if (spinnerValueFactory != null && (stringConverter = spinnerValueFactory.getConverter()) != null) {
            string = stringConverter.toString(t2);
        }
        this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
        if (string == null) {
            if (t2 == null) {
                this.getEditor().clear();
                return;
            }
            string = t2.toString();
        }
        this.getEditor().setText(string);
    }

    static int wrapValue(int n2, int n3, int n4) {
        if (n4 == 0) {
            throw new RuntimeException();
        }
        int n5 = n2 % n4;
        if (n5 > n3 && n4 < n3) {
            n5 = n5 + n4 - n3;
        } else if (n5 < n3 && n4 > n3) {
            n5 = n5 + n4 - n3;
        }
        return n5;
    }

    static BigDecimal wrapValue(BigDecimal bigDecimal, BigDecimal bigDecimal2, BigDecimal bigDecimal3) {
        if (bigDecimal3.doubleValue() == 0.0) {
            throw new RuntimeException();
        }
        if (bigDecimal.compareTo(bigDecimal2) < 0) {
            return bigDecimal3;
        }
        if (bigDecimal.compareTo(bigDecimal3) > 0) {
            return bigDecimal2;
        }
        return bigDecimal;
    }

    private void commitEditorText() {
        StringConverter<T> stringConverter;
        if (!this.isEditable()) {
            return;
        }
        String string = this.getEditor().getText();
        SpinnerValueFactory<T> spinnerValueFactory = this.getValueFactory();
        if (spinnerValueFactory != null && (stringConverter = spinnerValueFactory.getConverter()) != null) {
            T t2 = stringConverter.fromString(string);
            spinnerValueFactory.setValue(t2);
        }
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case TEXT: {
                StringConverter<T> stringConverter;
                T t2 = this.getValue();
                SpinnerValueFactory<T> spinnerValueFactory = this.getValueFactory();
                if (spinnerValueFactory != null && (stringConverter = spinnerValueFactory.getConverter()) != null) {
                    return stringConverter.toString(t2);
                }
                return t2 != null ? t2.toString() : "";
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case INCREMENT: {
                this.increment();
                break;
            }
            case DECREMENT: {
                this.decrement();
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, new Object[0]);
            }
        }
    }
}

