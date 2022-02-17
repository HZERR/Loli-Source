/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Iterator;
import javafx.scene.Parent;

public abstract class ConstraintsBase {
    public static final double CONSTRAIN_TO_PREF = Double.NEGATIVE_INFINITY;
    WeakReferenceQueue impl_nodes = new WeakReferenceQueue();

    ConstraintsBase() {
    }

    void add(Parent parent) {
        this.impl_nodes.add(parent);
    }

    void remove(Parent parent) {
        this.impl_nodes.remove(parent);
    }

    protected void requestLayout() {
        Iterator iterator = this.impl_nodes.iterator();
        while (iterator.hasNext()) {
            ((Parent)iterator.next()).requestLayout();
        }
    }
}

