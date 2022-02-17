/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

public class InfoClassVisitor
extends EmptyVisitor
implements Opcodes {
    protected Set<String> methods = new HashSet<String>();
    protected Set<String> fields = new HashSet<String>();

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.methods.add(name);
        return this;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        this.fields.add(name);
        return this;
    }

    public Set<String> getMethods() {
        return Collections.unmodifiableSet(this.methods);
    }

    public Set<String> getFields() {
        return Collections.unmodifiableSet(this.fields);
    }
}

