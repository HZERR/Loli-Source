/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class FrameNode
extends AbstractInsnNode {
    public int type;
    public List<Object> local;
    public List<Object> stack;

    private FrameNode() {
        super(-1);
    }

    public FrameNode(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super(-1);
        this.type = type;
        switch (type) {
            case -1: 
            case 0: {
                this.local = FrameNode.asList(nLocal, local);
                this.stack = FrameNode.asList(nStack, stack);
                break;
            }
            case 1: {
                this.local = FrameNode.asList(nLocal, local);
                break;
            }
            case 2: {
                this.local = Arrays.asList(new Object[nLocal]);
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                this.stack = FrameNode.asList(1, stack);
            }
        }
    }

    @Override
    public int getType() {
        return 14;
    }

    @Override
    public void accept(MethodVisitor mv) {
        switch (this.type) {
            case -1: 
            case 0: {
                mv.visitFrame(this.type, this.local.size(), FrameNode.asArray(this.local), this.stack.size(), FrameNode.asArray(this.stack));
                break;
            }
            case 1: {
                mv.visitFrame(this.type, this.local.size(), FrameNode.asArray(this.local), 0, null);
                break;
            }
            case 2: {
                mv.visitFrame(this.type, this.local.size(), null, 0, null);
                break;
            }
            case 3: {
                mv.visitFrame(this.type, 0, null, 0, null);
                break;
            }
            case 4: {
                mv.visitFrame(this.type, 0, null, 1, FrameNode.asArray(this.stack));
            }
        }
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
        int i2;
        FrameNode clone = new FrameNode();
        clone.type = this.type;
        if (this.local != null) {
            clone.local = new ArrayList<Object>();
            for (i2 = 0; i2 < this.local.size(); ++i2) {
                Object l2 = this.local.get(i2);
                if (l2 instanceof LabelNode) {
                    l2 = labels.get(l2);
                }
                clone.local.add(l2);
            }
        }
        if (this.stack != null) {
            clone.stack = new ArrayList<Object>();
            for (i2 = 0; i2 < this.stack.size(); ++i2) {
                Object s2 = this.stack.get(i2);
                if (s2 instanceof LabelNode) {
                    s2 = labels.get(s2);
                }
                clone.stack.add(s2);
            }
        }
        return clone;
    }

    private static List<Object> asList(int n2, Object[] o2) {
        return Arrays.asList(o2).subList(0, n2);
    }

    private static Object[] asArray(List<Object> l2) {
        Object[] objs = new Object[l2.size()];
        for (int i2 = 0; i2 < objs.length; ++i2) {
            Object o2 = l2.get(i2);
            if (o2 instanceof LabelNode) {
                o2 = ((LabelNode)o2).getLabel();
            }
            objs[i2] = o2;
        }
        return objs;
    }
}

