/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.Combinator;
import com.sun.javafx.css.Match;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import com.sun.javafx.css.StringStore;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.css.Styleable;

public final class CompoundSelector
extends Selector {
    private final List<SimpleSelector> selectors;
    private final List<Combinator> relationships;
    private int hash = -1;

    public List<SimpleSelector> getSelectors() {
        return this.selectors;
    }

    public List<Combinator> getRelationships() {
        return this.relationships;
    }

    public CompoundSelector(List<SimpleSelector> list, List<Combinator> list2) {
        this.selectors = list != null ? Collections.unmodifiableList(list) : Collections.EMPTY_LIST;
        this.relationships = list2 != null ? Collections.unmodifiableList(list2) : Collections.EMPTY_LIST;
    }

    private CompoundSelector() {
        this(null, null);
    }

    @Override
    Match createMatch() {
        PseudoClassState pseudoClassState = new PseudoClassState();
        int n2 = 0;
        int n3 = 0;
        int n4 = this.selectors.size();
        for (int i2 = 0; i2 < n4; ++i2) {
            Selector selector = this.selectors.get(i2);
            Match match = selector.createMatch();
            pseudoClassState.addAll((Collection)match.pseudoClasses);
            n2 += match.idCount;
            n3 += match.styleClassCount;
        }
        return new Match(this, pseudoClassState, n2, n3);
    }

    @Override
    public boolean applies(Styleable styleable) {
        return this.applies(styleable, this.selectors.size() - 1, null, 0);
    }

    @Override
    boolean applies(Styleable styleable, Set<PseudoClass>[] arrset, int n2) {
        assert (arrset == null || n2 < arrset.length);
        if (arrset != null && arrset.length <= n2) {
            return false;
        }
        PseudoClassState[] arrpseudoClassState = arrset != null ? new PseudoClassState[arrset.length] : null;
        boolean bl = this.applies(styleable, this.selectors.size() - 1, arrpseudoClassState, n2);
        if (bl && arrpseudoClassState != null) {
            for (int i2 = 0; i2 < arrset.length; ++i2) {
                Set<PseudoClass> set = arrset[i2];
                PseudoClassState pseudoClassState = arrpseudoClassState[i2];
                if (set != null) {
                    set.addAll(pseudoClassState);
                    continue;
                }
                arrset[i2] = pseudoClassState;
            }
        }
        return bl;
    }

    private boolean applies(Styleable styleable, int n2, Set<PseudoClass>[] arrset, int n3) {
        if (n2 < 0) {
            return false;
        }
        if (!this.selectors.get(n2).applies(styleable, arrset, n3)) {
            return false;
        }
        if (n2 == 0) {
            return true;
        }
        Combinator combinator = this.relationships.get(n2 - 1);
        if (combinator == Combinator.CHILD) {
            Styleable styleable2 = styleable.getStyleableParent();
            if (styleable2 == null) {
                return false;
            }
            return this.applies(styleable2, n2 - 1, arrset, ++n3);
        }
        for (Styleable styleable3 = styleable.getStyleableParent(); styleable3 != null; styleable3 = styleable3.getStyleableParent()) {
            boolean bl;
            if (!(bl = this.applies(styleable3, n2 - 1, arrset, ++n3))) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean stateMatches(Styleable styleable, Set<PseudoClass> set) {
        return this.stateMatches(styleable, set, this.selectors.size() - 1);
    }

    private boolean stateMatches(Styleable styleable, Set<PseudoClass> set, int n2) {
        if (n2 < 0) {
            return false;
        }
        if (!this.selectors.get(n2).stateMatches(styleable, set)) {
            return false;
        }
        if (n2 == 0) {
            return true;
        }
        Combinator combinator = this.relationships.get(n2 - 1);
        if (combinator == Combinator.CHILD) {
            Styleable styleable2 = styleable.getStyleableParent();
            if (styleable2 == null) {
                return false;
            }
            if (this.selectors.get(n2 - 1).applies(styleable2)) {
                ObservableSet<PseudoClass> observableSet = styleable2.getPseudoClassStates();
                return this.stateMatches(styleable2, observableSet, n2 - 1);
            }
        } else {
            for (Styleable styleable3 = styleable.getStyleableParent(); styleable3 != null; styleable3 = styleable3.getStyleableParent()) {
                if (!this.selectors.get(n2 - 1).applies(styleable3)) continue;
                ObservableSet<PseudoClass> observableSet = styleable3.getPseudoClassStates();
                return this.stateMatches(styleable3, observableSet, n2 - 1);
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == -1) {
            int n2;
            int n3 = this.selectors.size();
            for (n2 = 0; n2 < n3; ++n2) {
                this.hash = 31 * (this.hash + this.selectors.get(n2).hashCode());
            }
            n3 = this.relationships.size();
            for (n2 = 0; n2 < n3; ++n2) {
                this.hash = 31 * (this.hash + this.relationships.get(n2).hashCode());
            }
        }
        return this.hash;
    }

    public boolean equals(Object object) {
        int n2;
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CompoundSelector compoundSelector = (CompoundSelector)object;
        if (compoundSelector.selectors.size() != this.selectors.size()) {
            return false;
        }
        int n3 = this.selectors.size();
        for (n2 = 0; n2 < n3; ++n2) {
            if (compoundSelector.selectors.get(n2).equals(this.selectors.get(n2))) continue;
            return false;
        }
        if (compoundSelector.relationships.size() != this.relationships.size()) {
            return false;
        }
        n3 = this.relationships.size();
        for (n2 = 0; n2 < n3; ++n2) {
            if (compoundSelector.relationships.get(n2).equals((Object)this.relationships.get(n2))) continue;
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.selectors.get(0));
        for (int i2 = 1; i2 < this.selectors.size(); ++i2) {
            stringBuilder.append((Object)this.relationships.get(i2 - 1));
            stringBuilder.append(this.selectors.get(i2));
        }
        return stringBuilder.toString();
    }

    @Override
    public final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        int n2;
        super.writeBinary(dataOutputStream, stringStore);
        dataOutputStream.writeShort(this.selectors.size());
        for (n2 = 0; n2 < this.selectors.size(); ++n2) {
            this.selectors.get(n2).writeBinary(dataOutputStream, stringStore);
        }
        dataOutputStream.writeShort(this.relationships.size());
        for (n2 = 0; n2 < this.relationships.size(); ++n2) {
            dataOutputStream.writeByte(this.relationships.get(n2).ordinal());
        }
    }

    public static CompoundSelector readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        int n3;
        int n4 = dataInputStream.readShort();
        ArrayList<SimpleSelector> arrayList = new ArrayList<SimpleSelector>();
        for (n3 = 0; n3 < n4; ++n3) {
            arrayList.add((SimpleSelector)Selector.readBinary(n2, dataInputStream, arrstring));
        }
        n3 = dataInputStream.readShort();
        ArrayList<Combinator> arrayList2 = new ArrayList<Combinator>();
        for (int i2 = 0; i2 < n3; ++i2) {
            byte by = dataInputStream.readByte();
            if (by == Combinator.CHILD.ordinal()) {
                arrayList2.add(Combinator.CHILD);
                continue;
            }
            if (by == Combinator.DESCENDANT.ordinal()) {
                arrayList2.add(Combinator.DESCENDANT);
                continue;
            }
            assert (false) : "error deserializing CompoundSelector: Combinator = " + by;
            arrayList2.add(Combinator.DESCENDANT);
        }
        return new CompoundSelector(arrayList, arrayList2);
    }
}

