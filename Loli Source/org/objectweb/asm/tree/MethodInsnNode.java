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
public class MethodInsnNode
extends AbstractInsnNode {
    public String owner;
    public String name;
    public String desc;

    public MethodInsnNode(int opcode, String owner, String name, String desc) {
        super(opcode);
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public void accept(MethodVisitor mv) {
        mv.visitMethodInsn(this.opcode, this.owner, this.name, this.desc);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
        return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc);
    }
}

