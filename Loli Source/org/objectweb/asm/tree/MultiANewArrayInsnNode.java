/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class MultiANewArrayInsnNode
extends AbstractInsnNode {
    public String desc;
    public int dims;

    public MultiANewArrayInsnNode(String desc, int dims) {
        super(197);
        this.desc = desc;
        this.dims = dims;
    }

    @Override
    public int getType() {
        return 13;
    }

    @Override
    public void accept(MethodVisitor mv) {
        mv.visitMultiANewArrayInsn(this.desc, this.dims);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
        return new MultiANewArrayInsnNode(this.desc, this.dims);
    }
}

