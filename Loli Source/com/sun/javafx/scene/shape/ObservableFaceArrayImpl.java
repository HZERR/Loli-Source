/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.scene.shape.ObservableFaceArray;

public class ObservableFaceArrayImpl
extends ObservableIntegerArrayImpl
implements ObservableFaceArray {
    public ObservableFaceArrayImpl() {
    }

    public ObservableFaceArrayImpl(int ... arrn) {
        super(arrn);
    }

    public ObservableFaceArrayImpl(ObservableFaceArray observableFaceArray) {
        super(observableFaceArray);
    }
}

