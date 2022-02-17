/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.BitSet;
import com.sun.javafx.css.PseudoClassImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.css.PseudoClass;

public final class PseudoClassState
extends BitSet<PseudoClass> {
    static final Map<String, Integer> pseudoClassMap = new HashMap<String, Integer>(64);
    static final List<PseudoClass> pseudoClasses = new ArrayList<PseudoClass>();

    public PseudoClassState() {
    }

    PseudoClassState(List<String> list) {
        int n2 = list != null ? list.size() : 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            PseudoClass pseudoClass = PseudoClassState.getPseudoClass(list.get(i2));
            this.add(pseudoClass);
        }
    }

    @Override
    public Object[] toArray() {
        return this.toArray(new PseudoClass[this.size()]);
    }

    @Override
    public <T> T[] toArray(T[] arrobject) {
        if (arrobject.length < this.size()) {
            arrobject = new PseudoClass[this.size()];
        }
        int n2 = 0;
        while (n2 < this.getBits().length) {
            long l2 = this.getBits()[n2];
            for (int i2 = 0; i2 < 64; ++i2) {
                long l3 = 1L << i2;
                if ((l2 & l3) != l3) continue;
                int n3 = n2 * 64 + i2;
                PseudoClass pseudoClass = PseudoClassState.getPseudoClass(n3);
                arrobject[n2++] = pseudoClass;
            }
        }
        return arrobject;
    }

    public String toString() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            arrayList.add(((PseudoClass)iterator.next()).getPseudoClassName());
        }
        return ((Object)arrayList).toString();
    }

    @Override
    protected PseudoClass cast(Object object) {
        if (object == null) {
            throw new NullPointerException("null arg");
        }
        PseudoClass pseudoClass = (PseudoClass)object;
        return pseudoClass;
    }

    @Override
    protected PseudoClass getT(int n2) {
        return PseudoClassState.getPseudoClass(n2);
    }

    @Override
    protected int getIndex(PseudoClass pseudoClass) {
        if (pseudoClass instanceof PseudoClassImpl) {
            return ((PseudoClassImpl)pseudoClass).getIndex();
        }
        String string = pseudoClass.getPseudoClassName();
        Integer n2 = pseudoClassMap.get(string);
        if (n2 == null) {
            n2 = pseudoClasses.size();
            pseudoClasses.add(new PseudoClassImpl(string, n2));
            pseudoClassMap.put(string, n2);
        }
        return n2;
    }

    public static PseudoClass getPseudoClass(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("pseudoClass cannot be null or empty String");
        }
        PseudoClass pseudoClass = null;
        Integer n2 = pseudoClassMap.get(string);
        int n3 = n2 != null ? n2 : -1;
        int n4 = pseudoClasses.size();
        assert (n3 < n4);
        if (n3 != -1 && n3 < n4) {
            pseudoClass = pseudoClasses.get(n3);
        }
        if (pseudoClass == null) {
            pseudoClass = new PseudoClassImpl(string, n4);
            pseudoClasses.add(pseudoClass);
            pseudoClassMap.put(string, n4);
        }
        return pseudoClass;
    }

    static PseudoClass getPseudoClass(int n2) {
        if (0 <= n2 && n2 < pseudoClasses.size()) {
            return pseudoClasses.get(n2);
        }
        return null;
    }
}

