/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

class Subroutine {
    LabelNode start;
    boolean[] access;
    List<JumpInsnNode> callers;

    private Subroutine() {
    }

    Subroutine(LabelNode start, int maxLocals, JumpInsnNode caller) {
        this.start = start;
        this.access = new boolean[maxLocals];
        this.callers = new ArrayList<JumpInsnNode>();
        this.callers.add(caller);
    }

    public Subroutine copy() {
        Subroutine result = new Subroutine();
        result.start = this.start;
        result.access = new boolean[this.access.length];
        System.arraycopy(this.access, 0, result.access, 0, this.access.length);
        result.callers = new ArrayList<JumpInsnNode>(this.callers);
        return result;
    }

    public boolean merge(Subroutine subroutine) throws AnalyzerException {
        int i2;
        boolean changes = false;
        for (i2 = 0; i2 < this.access.length; ++i2) {
            if (!subroutine.access[i2] || this.access[i2]) continue;
            this.access[i2] = true;
            changes = true;
        }
        if (subroutine.start == this.start) {
            for (i2 = 0; i2 < subroutine.callers.size(); ++i2) {
                JumpInsnNode caller = subroutine.callers.get(i2);
                if (this.callers.contains(caller)) continue;
                this.callers.add(caller);
                changes = true;
            }
        }
        return changes;
    }
}

