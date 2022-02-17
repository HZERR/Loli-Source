/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree.analysis;

import java.util.Set;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.SmallSet;
import org.objectweb.asm.tree.analysis.Value;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SourceValue
implements Value {
    public final int size;
    public final Set<AbstractInsnNode> insns;

    public SourceValue(int size) {
        this(size, SmallSet.emptySet());
    }

    public SourceValue(int size, AbstractInsnNode insn) {
        this.size = size;
        this.insns = new SmallSet<Object>(insn, null);
    }

    public SourceValue(int size, Set<AbstractInsnNode> insns) {
        this.size = size;
        this.insns = insns;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public boolean equals(Object value) {
        if (!(value instanceof SourceValue)) {
            return false;
        }
        SourceValue v2 = (SourceValue)value;
        return this.size == v2.size && ((Object)this.insns).equals(v2.insns);
    }

    public int hashCode() {
        return ((Object)this.insns).hashCode();
    }
}

