/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class InvokeDynamicInsnNode
extends AbstractInsnNode {
    public String name;
    public String desc;
    public MethodHandle bsm;
    public Object[] bsmArgs;

    public InvokeDynamicInsnNode(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        super(186);
        this.name = name;
        this.desc = desc;
        this.bsm = bsm;
        this.bsmArgs = bsmArgs;
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public void accept(MethodVisitor mv) {
        mv.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
        return new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs);
    }
}

