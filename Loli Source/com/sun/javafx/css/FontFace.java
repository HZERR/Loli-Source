/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.StringStore;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FontFace {
    private final Map<String, String> descriptors;
    private final List<FontFaceSrc> sources;

    public FontFace(Map<String, String> map, List<FontFaceSrc> list) {
        this.descriptors = map;
        this.sources = list;
    }

    public Map<String, String> getDescriptors() {
        return this.descriptors;
    }

    public List<FontFaceSrc> getSources() {
        return this.sources;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("@font-face { ");
        for (Map.Entry<String, String> object : this.descriptors.entrySet()) {
            stringBuilder.append(object.getKey());
            stringBuilder.append(" : ");
            stringBuilder.append(object.getValue());
            stringBuilder.append("; ");
        }
        stringBuilder.append("src : ");
        for (FontFaceSrc fontFaceSrc : this.sources) {
            stringBuilder.append((Object)fontFaceSrc.getType());
            stringBuilder.append(" \"");
            stringBuilder.append(fontFaceSrc.getSrc());
            stringBuilder.append("\", ");
        }
        stringBuilder.append("; ");
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        Object object;
        Set<Map.Entry<String, String>> set = this.getDescriptors() != null ? this.getDescriptors().entrySet() : null;
        int n2 = set != null ? set.size() : 0;
        dataOutputStream.writeShort(n2);
        if (set != null) {
            object = set.iterator();
            while (object.hasNext()) {
                Map.Entry entry = (Map.Entry)object.next();
                int n3 = stringStore.addString((String)entry.getKey());
                dataOutputStream.writeInt(n3);
                n3 = stringStore.addString((String)entry.getValue());
                dataOutputStream.writeInt(n3);
            }
        }
        n2 = (object = this.getSources()) != null ? object.size() : 0;
        dataOutputStream.writeShort(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            FontFaceSrc fontFaceSrc = (FontFaceSrc)object.get(i2);
            fontFaceSrc.writeBinary(dataOutputStream, stringStore);
        }
    }

    static final FontFace readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        Object object;
        int n3;
        int n4 = dataInputStream.readShort();
        HashMap<String, String> hashMap = new HashMap<String, String>(n4);
        for (int i2 = 0; i2 < n4; ++i2) {
            n3 = dataInputStream.readInt();
            object = arrstring[n3];
            n3 = dataInputStream.readInt();
            String string = arrstring[n3];
            hashMap.put((String)object, string);
        }
        n4 = dataInputStream.readShort();
        ArrayList<FontFaceSrc> arrayList = new ArrayList<FontFaceSrc>(n4);
        for (n3 = 0; n3 < n4; ++n3) {
            object = FontFaceSrc.readBinary(n2, dataInputStream, arrstring);
            arrayList.add((FontFaceSrc)object);
        }
        return new FontFace(hashMap, arrayList);
    }

    public static class FontFaceSrc {
        private final FontFaceSrcType type;
        private final String src;
        private final String format;

        public FontFaceSrc(FontFaceSrcType fontFaceSrcType, String string, String string2) {
            this.type = fontFaceSrcType;
            this.src = string;
            this.format = string2;
        }

        public FontFaceSrc(FontFaceSrcType fontFaceSrcType, String string) {
            this.type = fontFaceSrcType;
            this.src = string;
            this.format = null;
        }

        public FontFaceSrcType getType() {
            return this.type;
        }

        public String getSrc() {
            return this.src;
        }

        public String getFormat() {
            return this.format;
        }

        final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
            dataOutputStream.writeInt(stringStore.addString(this.type.name()));
            dataOutputStream.writeInt(stringStore.addString(this.src));
            dataOutputStream.writeInt(stringStore.addString(this.format));
        }

        static final FontFaceSrc readBinary(int n2, DataInputStream dataInputStream, String[] arrstring) throws IOException {
            int n3 = dataInputStream.readInt();
            FontFaceSrcType fontFaceSrcType = arrstring[n3] != null ? FontFaceSrcType.valueOf(arrstring[n3]) : null;
            n3 = dataInputStream.readInt();
            String string = arrstring[n3];
            n3 = dataInputStream.readInt();
            String string2 = arrstring[n3];
            return new FontFaceSrc(fontFaceSrcType, string, string2);
        }
    }

    public static enum FontFaceSrcType {
        URL,
        LOCAL,
        REFERENCE;

    }
}

