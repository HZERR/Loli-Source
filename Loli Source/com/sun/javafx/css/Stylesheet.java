/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.FontFace;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.parser.CSSParser;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;

public class Stylesheet {
    static final int BINARY_CSS_VERSION = 5;
    private final String url;
    private StyleOrigin origin = StyleOrigin.AUTHOR;
    private final ObservableList<Rule> rules = new TrackableObservableList<Rule>(){

        @Override
        protected void onChanged(ListChangeListener.Change<Rule> change) {
            change.reset();
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Rule rule : change.getAddedSubList()) {
                        rule.setStylesheet(Stylesheet.this);
                    }
                    continue;
                }
                if (!change.wasRemoved()) continue;
                for (Rule rule : change.getRemoved()) {
                    if (rule.getStylesheet() != Stylesheet.this) continue;
                    rule.setStylesheet(null);
                }
            }
        }
    };
    private final List<FontFace> fontFaces = new ArrayList<FontFace>();
    private String[] stringStore;

    public String getUrl() {
        return this.url;
    }

    public StyleOrigin getOrigin() {
        return this.origin;
    }

    public void setOrigin(StyleOrigin styleOrigin) {
        this.origin = styleOrigin;
    }

    public Stylesheet() {
        this(null);
    }

    public Stylesheet(String string) {
        this.url = string;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    public List<FontFace> getFontFaces() {
        return this.fontFaces;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Stylesheet) {
            Stylesheet stylesheet = (Stylesheet)object;
            if (this.url == null && stylesheet.url == null) {
                return true;
            }
            if (this.url == null || stylesheet.url == null) {
                return false;
            }
            return this.url.equals(stylesheet.url);
        }
        return false;
    }

    public int hashCode() {
        int n2 = 7;
        n2 = 13 * n2 + (this.url != null ? this.url.hashCode() : 0);
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/* ");
        if (this.url != null) {
            stringBuilder.append(this.url);
        }
        if (this.rules.isEmpty()) {
            stringBuilder.append(" */");
        } else {
            stringBuilder.append(" */\n");
            for (int i2 = 0; i2 < this.rules.size(); ++i2) {
                stringBuilder.append(this.rules.get(i2));
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }

    final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        int n2 = stringStore.addString(this.origin.name());
        dataOutputStream.writeShort(n2);
        dataOutputStream.writeShort(this.rules.size());
        for (Rule rule : this.rules) {
            rule.writeBinary(dataOutputStream, stringStore);
        }
        List<FontFace> list = this.getFontFaces();
        int n3 = list != null ? list.size() : 0;
        dataOutputStream.writeShort(n3);
        for (int i2 = 0; i2 < n3; ++i2) {
            FontFace fontFace = (FontFace)list.get(i2);
            fontFace.writeBinary(dataOutputStream, stringStore);
        }
    }

    final void readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        this.stringStore = arrstring;
        short s2 = dataInputStream.readShort();
        this.setOrigin(StyleOrigin.valueOf(arrstring[s2]));
        int n3 = dataInputStream.readShort();
        ArrayList<Rule> arrayList = new ArrayList<Rule>(n3);
        for (int i2 = 0; i2 < n3; ++i2) {
            arrayList.add(Rule.readBinary(n2, dataInputStream, arrstring));
        }
        this.rules.addAll((Collection<Rule>)arrayList);
        if (n2 >= 5) {
            List<FontFace> list = this.getFontFaces();
            int n4 = dataInputStream.readShort();
            for (int i3 = 0; i3 < n4; ++i3) {
                FontFace fontFace = FontFace.readBinary(n2, dataInputStream, arrstring);
                list.add(fontFace);
            }
        }
    }

    final String[] getStringStore() {
        return this.stringStore;
    }

    public static Stylesheet loadBinary(URL uRL) throws IOException {
        Stylesheet stylesheet;
        block19: {
            if (uRL == null) {
                return null;
            }
            stylesheet = null;
            try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(uRL.openStream(), 40960));){
                short s2 = dataInputStream.readShort();
                if (s2 > 5) {
                    throw new IOException(uRL.toString() + " wrong binary CSS version: " + s2 + ". Expected version less than or equal to" + 5);
                }
                String[] arrstring = StringStore.readBinary(dataInputStream);
                stylesheet = new Stylesheet(uRL.toExternalForm());
                try {
                    dataInputStream.mark(Integer.MAX_VALUE);
                    stylesheet.readBinary(s2, dataInputStream, arrstring);
                }
                catch (Exception exception) {
                    stylesheet = new Stylesheet(uRL.toExternalForm());
                    dataInputStream.reset();
                    if (s2 == 2) {
                        stylesheet.readBinary(3, dataInputStream, arrstring);
                        break block19;
                    }
                    stylesheet.readBinary(5, dataInputStream, arrstring);
                }
            }
            catch (FileNotFoundException fileNotFoundException) {
                // empty catch block
            }
        }
        return stylesheet;
    }

    public static void convertToBinary(File file, File file2) throws IOException {
        if (file == null || file2 == null) {
            throw new IllegalArgumentException("parameters may not be null");
        }
        if (file.getAbsolutePath().equals(file2.getAbsolutePath())) {
            throw new IllegalArgumentException("source and destination may not be the same");
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("cannot read source file");
        }
        if (file2.exists() ? !file2.canWrite() : !file2.createNewFile()) {
            throw new IllegalArgumentException("cannot write destination file");
        }
        URI uRI = file.toURI();
        Stylesheet stylesheet = new CSSParser().parse(uRI.toURL());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        StringStore stringStore = new StringStore();
        stylesheet.writeBinary(dataOutputStream, stringStore);
        dataOutputStream.flush();
        dataOutputStream.close();
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        DataOutputStream dataOutputStream2 = new DataOutputStream(fileOutputStream);
        dataOutputStream2.writeShort(5);
        stringStore.writeBinary(dataOutputStream2);
        dataOutputStream2.write(byteArrayOutputStream.toByteArray());
        dataOutputStream2.flush();
        dataOutputStream2.close();
    }

    public void importStylesheet(Stylesheet stylesheet) {
        if (stylesheet == null) {
            return;
        }
        List<Rule> list = stylesheet.getRules();
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList<Rule> arrayList = new ArrayList<Rule>(list.size());
        for (Rule rule : list) {
            ObservableList<Selector> observableList = rule.getSelectors();
            List<Declaration> list2 = rule.getUnobservedDeclarationList();
            arrayList.add(new Rule(observableList, list2));
        }
        this.rules.addAll((Collection<Rule>)arrayList);
    }
}

