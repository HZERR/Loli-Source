/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringStore {
    private final Map<String, Integer> stringMap = new HashMap<String, Integer>();
    public final List<String> strings = new ArrayList<String>();

    public int addString(String string) {
        Integer n2 = this.stringMap.get(string);
        if (n2 == null) {
            n2 = this.strings.size();
            this.strings.add(string);
            this.stringMap.put(string, n2);
        }
        return n2;
    }

    public void writeBinary(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(this.strings.size());
        if (this.stringMap.containsKey(null)) {
            Integer n2 = this.stringMap.get(null);
            dataOutputStream.writeShort(n2);
        } else {
            dataOutputStream.writeShort(-1);
        }
        for (int i2 = 0; i2 < this.strings.size(); ++i2) {
            String string = this.strings.get(i2);
            if (string == null) continue;
            dataOutputStream.writeUTF(string);
        }
    }

    static String[] readBinary(DataInputStream dataInputStream) throws IOException {
        int n2 = dataInputStream.readShort();
        short s2 = dataInputStream.readShort();
        Object[] arrobject = new String[n2];
        Arrays.fill(arrobject, null);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (i2 == s2) continue;
            arrobject[i2] = dataInputStream.readUTF();
        }
        return arrobject;
    }
}

