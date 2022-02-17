/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.LabelNode;

public class TryCatchBlockNode {
    public LabelNode start;
    public LabelNode end;
    public LabelNode handler;
    public String type;

    public TryCatchBlockNode(LabelNode start, LabelNode end, LabelNode handler, String type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

    public void accept(MethodVisitor mv) {
        mv.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), this.handler == null ? null : this.handler.getLabel(), this.type);
    }
}

