/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import java.util.Collections;
import java.util.Comparator;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

public class TryCatchBlockSorter
extends MethodNode {
    private final MethodVisitor mv;

    public TryCatchBlockSorter(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        super(access, name, desc, signature, exceptions);
        this.mv = mv;
    }

    public void visitEnd() {
        Comparator<TryCatchBlockNode> comp = new Comparator<TryCatchBlockNode>(){

            @Override
            public int compare(TryCatchBlockNode t1, TryCatchBlockNode t2) {
                int len1 = this.blockLength(t1);
                int len2 = this.blockLength(t2);
                return len1 - len2;
            }

            private int blockLength(TryCatchBlockNode block) {
                int startidx = TryCatchBlockSorter.this.instructions.indexOf(block.start);
                int endidx = TryCatchBlockSorter.this.instructions.indexOf(block.end);
                return endidx - startidx;
            }
        };
        Collections.sort(this.tryCatchBlocks, comp);
        if (this.mv != null) {
            this.accept(this.mv);
        }
    }
}

