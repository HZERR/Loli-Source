/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import javafx.css.PseudoClass;

final class PseudoClassImpl
extends PseudoClass {
    private final String pseudoClassName;
    private final int index;

    PseudoClassImpl(String string, int n2) {
        this.pseudoClassName = string;
        this.index = n2;
    }

    @Override
    public String getPseudoClassName() {
        return this.pseudoClassName;
    }

    public String toString() {
        return this.pseudoClassName;
    }

    public int getIndex() {
        return this.index;
    }
}

