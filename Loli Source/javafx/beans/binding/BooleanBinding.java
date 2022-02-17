/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class BooleanBinding
extends BooleanExpression
implements Binding<Boolean> {
    private boolean value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private ExpressionHelper<Boolean> helper = null;

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, invalidationListener);
    }

    @Override
    public void addListener(ChangeListener<? super Boolean> changeListener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> changeListener) {
        this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
    }

    protected final void bind(Observable ... arrobservable) {
        if (arrobservable != null && arrobservable.length > 0) {
            if (this.observer == null) {
                this.observer = new BindingHelperObserver(this);
            }
            for (Observable observable : arrobservable) {
                observable.addListener(this.observer);
            }
        }
    }

    protected final void unbind(Observable ... arrobservable) {
        if (this.observer != null) {
            for (Observable observable : arrobservable) {
                observable.removeListener(this.observer);
            }
            this.observer = null;
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public ObservableList<?> getDependencies() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public final boolean get() {
        if (!this.valid) {
            this.value = this.computeValue();
            this.valid = true;
        }
        return this.value;
    }

    protected void onInvalidating() {
    }

    @Override
    public final void invalidate() {
        if (this.valid) {
            this.valid = false;
            this.onInvalidating();
            ExpressionHelper.fireValueChangedEvent(this.helper);
        }
    }

    @Override
    public final boolean isValid() {
        return this.valid;
    }

    protected abstract boolean computeValue();

    public String toString() {
        return this.valid ? "BooleanBinding [value: " + this.get() + "]" : "BooleanBinding [invalid]";
    }
}

