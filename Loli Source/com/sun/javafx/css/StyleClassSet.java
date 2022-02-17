/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.BitSet;
import com.sun.javafx.css.StyleClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class StyleClassSet
extends BitSet<StyleClass> {
    static final Map<String, Integer> styleClassMap = new HashMap<String, Integer>(64);
    static final List<StyleClass> styleClasses = new ArrayList<StyleClass>();

    public StyleClassSet() {
    }

    StyleClassSet(List<String> list) {
        int n2 = list != null ? list.size() : 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = list.get(i2);
            if (string == null || string.isEmpty()) continue;
            StyleClass styleClass = StyleClassSet.getStyleClass(string);
            this.add(styleClass);
        }
    }

    @Override
    public Object[] toArray() {
        return this.toArray(new StyleClass[this.size()]);
    }

    @Override
    public <T> T[] toArray(T[] arrobject) {
        if (arrobject.length < this.size()) {
            arrobject = new StyleClass[this.size()];
        }
        int n2 = 0;
        while (n2 < this.getBits().length) {
            long l2 = this.getBits()[n2];
            for (int i2 = 0; i2 < 64; ++i2) {
                long l3 = 1L << i2;
                if ((l2 & l3) != l3) continue;
                int n3 = n2 * 64 + i2;
                StyleClass styleClass = StyleClassSet.getStyleClass(n3);
                arrobject[n2++] = styleClass;
            }
        }
        return arrobject;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("style-classes: [");
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(((StyleClass)iterator.next()).getStyleClassName());
            if (!iterator.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    @Override
    protected StyleClass cast(Object object) {
        if (object == null) {
            throw new NullPointerException("null arg");
        }
        StyleClass styleClass = (StyleClass)object;
        return styleClass;
    }

    @Override
    protected StyleClass getT(int n2) {
        return StyleClassSet.getStyleClass(n2);
    }

    @Override
    protected int getIndex(StyleClass styleClass) {
        return styleClass.getIndex();
    }

    static StyleClass getStyleClass(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("styleClass cannot be null or empty String");
        }
        StyleClass styleClass = null;
        Integer n2 = styleClassMap.get(string);
        int n3 = n2 != null ? n2 : -1;
        int n4 = styleClasses.size();
        assert (n3 < n4);
        if (n3 != -1 && n3 < n4) {
            styleClass = styleClasses.get(n3);
        }
        if (styleClass == null) {
            styleClass = new StyleClass(string, n4);
            styleClasses.add(styleClass);
            styleClassMap.put(string, n4);
        }
        return styleClass;
    }

    static StyleClass getStyleClass(int n2) {
        if (0 <= n2 && n2 < styleClasses.size()) {
            return styleClasses.get(n2);
        }
        return null;
    }
}

