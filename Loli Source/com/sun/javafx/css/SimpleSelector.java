/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.BitSet;
import com.sun.javafx.css.Match;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.StyleClass;
import com.sun.javafx.css.StyleClassSet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;

public final class SimpleSelector
extends Selector {
    private final String name;
    private final StyleClassSet styleClassSet;
    private final String id;
    private final PseudoClassState pseudoClassState;
    private final boolean matchOnName;
    private final boolean matchOnId;
    private final boolean matchOnStyleClass;
    private final NodeOrientation nodeOrientation;

    public String getName() {
        return this.name;
    }

    public List<String> getStyleClasses() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator iterator = this.styleClassSet.iterator();
        while (iterator.hasNext()) {
            arrayList.add(((StyleClass)iterator.next()).getStyleClassName());
        }
        return Collections.unmodifiableList(arrayList);
    }

    Set<StyleClass> getStyleClassSet() {
        return this.styleClassSet;
    }

    public String getId() {
        return this.id;
    }

    Set<PseudoClass> getPseudoClassStates() {
        return this.pseudoClassState;
    }

    public List<String> getPseudoclasses() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator iterator = this.pseudoClassState.iterator();
        while (iterator.hasNext()) {
            arrayList.add(((PseudoClass)iterator.next()).getPseudoClassName());
        }
        if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            arrayList.add("dir(rtl)");
        } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
            arrayList.add("dir(ltr)");
        }
        return Collections.unmodifiableList(arrayList);
    }

    NodeOrientation getNodeOrientation() {
        return this.nodeOrientation;
    }

    public SimpleSelector(String string, List<String> list, List<String> list2, String string2) {
        Object object;
        this.name = string == null ? "*" : string;
        this.matchOnName = string != null && !"".equals(string) && !"*".equals(string);
        this.styleClassSet = new StyleClassSet();
        int n2 = list != null ? list.size() : 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string3 = list.get(i2);
            if (string3 == null || string3.isEmpty()) continue;
            object = StyleClassSet.getStyleClass(string3);
            this.styleClassSet.add(object);
        }
        this.matchOnStyleClass = this.styleClassSet.size() > 0;
        this.pseudoClassState = new PseudoClassState();
        n2 = list2 != null ? list2.size() : 0;
        NodeOrientation nodeOrientation = NodeOrientation.INHERIT;
        for (int i3 = 0; i3 < n2; ++i3) {
            object = list2.get(i3);
            if (object == null || ((String)object).isEmpty()) continue;
            if ("dir(".regionMatches(true, 0, (String)object, 0, 4)) {
                boolean bl = "dir(rtl)".equalsIgnoreCase((String)object);
                nodeOrientation = bl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT;
                continue;
            }
            PseudoClass pseudoClass = PseudoClassState.getPseudoClass((String)object);
            this.pseudoClassState.add(pseudoClass);
        }
        this.nodeOrientation = nodeOrientation;
        this.id = string2 == null ? "" : string2;
        this.matchOnId = string2 != null && !"".equals(string2);
    }

    @Override
    Match createMatch() {
        int n2 = this.matchOnId ? 1 : 0;
        int n3 = this.styleClassSet.size();
        return new Match(this, this.pseudoClassState, n2, n3);
    }

    @Override
    public boolean applies(Styleable styleable) {
        boolean bl;
        boolean bl2;
        Object object;
        Object object2;
        if (this.nodeOrientation != NodeOrientation.INHERIT && styleable instanceof Node && ((object2 = ((Node)(object = (Node)styleable)).getNodeOrientation()) == NodeOrientation.INHERIT ? ((Node)object).getEffectiveNodeOrientation() != this.nodeOrientation : object2 != this.nodeOrientation)) {
            return false;
        }
        if (this.matchOnId && !(bl2 = this.id.equals(object = styleable.getId()))) {
            return false;
        }
        if (this.matchOnName && !(bl = this.name.equals(object = styleable.getTypeSelector()))) {
            return false;
        }
        if (this.matchOnStyleClass) {
            int n2;
            object = new StyleClassSet();
            object2 = styleable.getStyleClass();
            int n3 = object2.size();
            for (n2 = 0; n2 < n3; ++n2) {
                String string = (String)object2.get(n2);
                if (string == null || string.isEmpty()) continue;
                StyleClass styleClass = StyleClassSet.getStyleClass(string);
                ((BitSet)object).add(styleClass);
            }
            n2 = this.matchStyleClasses((StyleClassSet)object) ? 1 : 0;
            if (n2 == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean applies(Styleable styleable, Set<PseudoClass>[] arrset, int n2) {
        boolean bl = this.applies(styleable);
        if (bl && arrset != null && n2 < arrset.length) {
            if (arrset[n2] == null) {
                arrset[n2] = new PseudoClassState();
            }
            arrset[n2].addAll(this.pseudoClassState);
        }
        return bl;
    }

    @Override
    public boolean stateMatches(Styleable styleable, Set<PseudoClass> set) {
        return set != null ? set.containsAll(this.pseudoClassState) : false;
    }

    private boolean matchStyleClasses(StyleClassSet styleClassSet) {
        return styleClassSet.containsAll((Collection)this.styleClassSet);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        SimpleSelector simpleSelector = (SimpleSelector)object;
        if (this.name == null ? simpleSelector.name != null : !this.name.equals(simpleSelector.name)) {
            return false;
        }
        if (this.id == null ? simpleSelector.id != null : !this.id.equals(simpleSelector.id)) {
            return false;
        }
        if (!this.styleClassSet.equals(simpleSelector.styleClassSet)) {
            return false;
        }
        return this.pseudoClassState.equals(simpleSelector.pseudoClassState);
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 31 * (n2 + this.name.hashCode());
        n2 = 31 * (n2 + this.styleClassSet.hashCode());
        n2 = 31 * (n2 + this.styleClassSet.hashCode());
        n2 = this.id != null ? 31 * (n2 + this.id.hashCode()) : 0;
        n2 = 31 * (n2 + this.pseudoClassState.hashCode());
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.name != null && !this.name.isEmpty()) {
            stringBuilder.append(this.name);
        } else {
            stringBuilder.append("*");
        }
        for (Object object : this.styleClassSet) {
            stringBuilder.append('.').append(((StyleClass)object).getStyleClassName());
        }
        if (this.id != null && !this.id.isEmpty()) {
            stringBuilder.append('#');
            stringBuilder.append(this.id);
        }
        for (PseudoClass pseudoClass : this.pseudoClassState) {
            stringBuilder.append(':').append(pseudoClass.getPseudoClassName());
        }
        return stringBuilder.toString();
    }

    @Override
    public final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        super.writeBinary(dataOutputStream, stringStore);
        dataOutputStream.writeShort(stringStore.addString(this.name));
        dataOutputStream.writeShort(this.styleClassSet.size());
        for (StyleClass styleClass : this.styleClassSet) {
            dataOutputStream.writeShort(stringStore.addString(styleClass.getStyleClassName()));
        }
        dataOutputStream.writeShort(stringStore.addString(this.id));
        int n2 = this.pseudoClassState.size() + (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT || this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT ? 1 : 0);
        dataOutputStream.writeShort(n2);
        for (PseudoClass pseudoClass : this.pseudoClassState) {
            dataOutputStream.writeShort(stringStore.addString(pseudoClass.getPseudoClassName()));
        }
        if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            dataOutputStream.writeShort(stringStore.addString("dir(rtl)"));
        } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
            dataOutputStream.writeShort(stringStore.addString("dir(ltr)"));
        }
    }

    static SimpleSelector readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        String string = arrstring[dataInputStream.readShort()];
        int n3 = dataInputStream.readShort();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i2 = 0; i2 < n3; ++i2) {
            arrayList.add(arrstring[dataInputStream.readShort()]);
        }
        String string2 = arrstring[dataInputStream.readShort()];
        int n4 = dataInputStream.readShort();
        ArrayList<String> arrayList2 = new ArrayList<String>();
        for (int i3 = 0; i3 < n4; ++i3) {
            arrayList2.add(arrstring[dataInputStream.readShort()]);
        }
        return new SimpleSelector(string, arrayList, arrayList2, string2);
    }
}

