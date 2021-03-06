/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;

public class BindingHelperObserver
implements InvalidationListener {
    private final WeakReference<Binding<?>> ref;

    public BindingHelperObserver(Binding<?> binding) {
        if (binding == null) {
            throw new NullPointerException("Binding has to be specified.");
        }
        this.ref = new WeakReference(binding);
    }

    @Override
    public void invalidated(Observable observable) {
        Binding binding = (Binding)this.ref.get();
        if (binding == null) {
            observable.removeListener(this);
        } else {
            binding.invalidate();
        }
    }
}

