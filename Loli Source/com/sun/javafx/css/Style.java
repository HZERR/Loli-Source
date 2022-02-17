/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.Selector;

public final class Style {
    private final Selector selector;
    private final Declaration declaration;

    public Selector getSelector() {
        return this.selector;
    }

    public Declaration getDeclaration() {
        return this.declaration;
    }

    public Style(Selector selector, Declaration declaration) {
        this.selector = selector;
        this.declaration = declaration;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        Style style = (Style)object;
        if (!(this.selector == style.selector || this.selector != null && this.selector.equals(style.selector))) {
            return false;
        }
        return this.declaration == style.declaration || this.declaration != null && this.declaration.equals(style.declaration);
    }

    public int hashCode() {
        int n2 = 3;
        n2 = 83 * n2 + (this.selector != null ? this.selector.hashCode() : 0);
        n2 = 83 * n2 + (this.declaration != null ? this.declaration.hashCode() : 0);
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append(String.valueOf(this.selector)).append(" { ").append(String.valueOf(this.declaration)).append(" } ");
        return stringBuilder.toString();
    }
}

