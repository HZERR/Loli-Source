/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import java.util.Collections;
import java.util.Map;
import org.objectweb.asm.commons.Remapper;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SimpleRemapper
extends Remapper {
    private final Map<String, String> mapping;

    public SimpleRemapper(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public SimpleRemapper(String oldName, String newName) {
        this.mapping = Collections.singletonMap(oldName, newName);
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        String s2 = this.map(owner + '.' + name + desc);
        return s2 == null ? name : s2;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        String s2 = this.map(owner + '.' + name);
        return s2 == null ? name : s2;
    }

    @Override
    public String map(String key) {
        return this.mapping.get(key);
    }
}

