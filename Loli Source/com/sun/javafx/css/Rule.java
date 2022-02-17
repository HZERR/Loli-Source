/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.Stylesheet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.scene.Node;

public final class Rule {
    private List<Selector> selectors = null;
    private List<Declaration> declarations = null;
    private Observables observables = null;
    private Stylesheet stylesheet;
    private byte[] serializedDecls;
    private final int bssVersion;

    public List<Selector> getUnobservedSelectorList() {
        if (this.selectors == null) {
            this.selectors = new ArrayList<Selector>();
        }
        return this.selectors;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<Declaration> getUnobservedDeclarationList() {
        if (this.declarations == null && this.serializedDecls != null) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.serializedDecls);
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                int n2 = dataInputStream.readShort();
                this.declarations = new ArrayList<Declaration>(n2);
                for (int i2 = 0; i2 < n2; ++i2) {
                    Declaration declaration = Declaration.readBinary(this.bssVersion, dataInputStream, this.stylesheet.getStringStore());
                    declaration.rule = this;
                    if (this.stylesheet != null && this.stylesheet.getUrl() != null) {
                        String string = this.stylesheet.getUrl();
                        declaration.fixUrl(string);
                    }
                    this.declarations.add(declaration);
                }
            }
            catch (IOException iOException) {
                this.declarations = new ArrayList<Declaration>();
                assert (false);
                iOException.getMessage();
            }
            finally {
                this.serializedDecls = null;
            }
        }
        return this.declarations;
    }

    public final ObservableList<Declaration> getDeclarations() {
        if (this.observables == null) {
            this.observables = new Observables(this);
        }
        return this.observables.getDeclarations();
    }

    public final ObservableList<Selector> getSelectors() {
        if (this.observables == null) {
            this.observables = new Observables(this);
        }
        return this.observables.getSelectors();
    }

    public Stylesheet getStylesheet() {
        return this.stylesheet;
    }

    void setStylesheet(Stylesheet stylesheet) {
        this.stylesheet = stylesheet;
        if (stylesheet != null && stylesheet.getUrl() != null) {
            String string = stylesheet.getUrl();
            int n2 = this.declarations != null ? this.declarations.size() : 0;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.declarations.get(i2).fixUrl(string);
            }
        }
    }

    public StyleOrigin getOrigin() {
        return this.stylesheet != null ? this.stylesheet.getOrigin() : null;
    }

    public Rule(List<Selector> list, List<Declaration> list2) {
        int n2;
        this.selectors = list;
        this.declarations = list2;
        this.serializedDecls = null;
        this.bssVersion = 5;
        int n3 = list != null ? list.size() : 0;
        for (n2 = 0; n2 < n3; ++n2) {
            Selector selector = list.get(n2);
            selector.setRule(this);
        }
        n2 = list2 != null ? list2.size() : 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            Declaration declaration = list2.get(i2);
            declaration.rule = this;
        }
    }

    private Rule(List<Selector> list, byte[] arrby, int n2) {
        this.selectors = list;
        this.declarations = null;
        this.serializedDecls = arrby;
        this.bssVersion = n2;
        int n3 = list != null ? list.size() : 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            Selector selector = list.get(i2);
            selector.setRule(this);
        }
    }

    long applies(Node node, Set<PseudoClass>[] arrset) {
        long l2 = 0L;
        for (int i2 = 0; i2 < this.selectors.size(); ++i2) {
            Selector selector = this.selectors.get(i2);
            if (!selector.applies(node, arrset, 0)) continue;
            l2 |= 1L << i2;
        }
        return l2;
    }

    public String toString() {
        int n2;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.selectors.size() > 0) {
            stringBuilder.append(this.selectors.get(0));
        }
        for (n2 = 1; n2 < this.selectors.size(); ++n2) {
            stringBuilder.append(',');
            stringBuilder.append(this.selectors.get(n2));
        }
        stringBuilder.append("{\n");
        n2 = this.declarations != null ? this.declarations.size() : 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append("\t");
            stringBuilder.append(this.declarations.get(i2));
            stringBuilder.append("\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        Object object;
        int n2 = this.selectors != null ? this.selectors.size() : 0;
        dataOutputStream.writeShort(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            object = this.selectors.get(i2);
            ((Selector)object).writeBinary(dataOutputStream, stringStore);
        }
        List<Declaration> list = this.getUnobservedDeclarationList();
        if (list != null) {
            object = new ByteArrayOutputStream(5192);
            DataOutputStream dataOutputStream2 = new DataOutputStream((OutputStream)object);
            int n3 = list.size();
            dataOutputStream2.writeShort(n3);
            for (int i3 = 0; i3 < n3; ++i3) {
                Declaration declaration = this.declarations.get(i3);
                declaration.writeBinary(dataOutputStream2, stringStore);
            }
            dataOutputStream.writeInt(((ByteArrayOutputStream)object).size());
            dataOutputStream.write(((ByteArrayOutputStream)object).toByteArray());
        } else {
            dataOutputStream.writeShort(0);
        }
    }

    static Rule readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        ArrayList<Declaration> arrayList;
        int n3;
        int n4 = dataInputStream.readShort();
        ArrayList<Selector> arrayList2 = new ArrayList<Selector>(n4);
        for (n3 = 0; n3 < n4; ++n3) {
            arrayList = Selector.readBinary(n2, dataInputStream, arrstring);
            arrayList2.add((Selector)((Object)arrayList));
        }
        if (n2 < 4) {
            n3 = dataInputStream.readShort();
            arrayList = new ArrayList<Declaration>(n3);
            for (int i2 = 0; i2 < n3; ++i2) {
                Declaration declaration = Declaration.readBinary(n2, dataInputStream, arrstring);
                arrayList.add(declaration);
            }
            return new Rule(arrayList2, arrayList);
        }
        n3 = dataInputStream.readInt();
        arrayList = (ArrayList<Declaration>)new byte[n3];
        if (n3 > 0) {
            dataInputStream.readFully((byte[])arrayList);
        }
        return new Rule((List<Selector>)arrayList2, (byte[])arrayList, n2);
    }

    private static final class Observables {
        private final Rule rule;
        private final ObservableList<Selector> selectorObservableList;
        private final ObservableList<Declaration> declarationObservableList;

        private Observables(Rule rule) {
            this.rule = rule;
            this.selectorObservableList = new TrackableObservableList<Selector>(rule.getUnobservedSelectorList()){

                @Override
                protected void onChanged(ListChangeListener.Change<Selector> change) {
                    while (change.next()) {
                        Selector selector;
                        int n2;
                        int n3;
                        List<Selector> list;
                        if (change.wasAdded()) {
                            list = change.getAddedSubList();
                            n3 = list.size();
                            for (n2 = 0; n2 < n3; ++n2) {
                                selector = list.get(n2);
                                selector.setRule(rule);
                            }
                        }
                        if (!change.wasRemoved()) continue;
                        list = change.getAddedSubList();
                        n3 = list.size();
                        for (n2 = 0; n2 < n3; ++n2) {
                            selector = list.get(n2);
                            if (selector.getRule() != rule) continue;
                            selector.setRule(null);
                        }
                    }
                }
            };
            this.declarationObservableList = new TrackableObservableList<Declaration>(rule.getUnobservedDeclarationList()){

                @Override
                protected void onChanged(ListChangeListener.Change<Declaration> change) {
                    while (change.next()) {
                        Declaration declaration;
                        int n2;
                        int n3;
                        List<Declaration> list;
                        if (change.wasAdded()) {
                            list = change.getAddedSubList();
                            n3 = list.size();
                            for (n2 = 0; n2 < n3; ++n2) {
                                declaration = list.get(n2);
                                declaration.rule = rule;
                                Stylesheet stylesheet = rule.stylesheet;
                                if (stylesheet == null || stylesheet.getUrl() == null) continue;
                                String string = stylesheet.getUrl();
                                declaration.fixUrl(string);
                            }
                        }
                        if (!change.wasRemoved()) continue;
                        list = change.getRemoved();
                        n3 = list.size();
                        for (n2 = 0; n2 < n3; ++n2) {
                            declaration = list.get(n2);
                            if (declaration.rule != rule) continue;
                            declaration.rule = null;
                        }
                    }
                }
            };
        }

        private ObservableList<Selector> getSelectors() {
            return this.selectorObservableList;
        }

        private ObservableList<Declaration> getDeclarations() {
            return this.declarationObservableList;
        }
    }
}

