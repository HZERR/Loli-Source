/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MemberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MethodNode
extends MemberNode
implements MethodVisitor {
    public int access;
    public String name;
    public String desc;
    public String signature;
    public List<String> exceptions;
    public Object annotationDefault;
    public List<AnnotationNode>[] visibleParameterAnnotations;
    public List<AnnotationNode>[] invisibleParameterAnnotations;
    public InsnList instructions = new InsnList();
    public List<TryCatchBlockNode> tryCatchBlocks;
    public int maxStack;
    public int maxLocals;
    public List<LocalVariableNode> localVariables;

    public MethodNode() {
    }

    public MethodNode(int access, String name, String desc, String signature, String[] exceptions) {
        this();
        boolean isAbstract;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = new ArrayList<String>(exceptions == null ? 0 : exceptions.length);
        boolean bl = isAbstract = (access & 0x400) != 0;
        if (!isAbstract) {
            this.localVariables = new ArrayList<LocalVariableNode>(5);
        }
        this.tryCatchBlocks = new ArrayList<TryCatchBlockNode>();
        if (exceptions != null) {
            this.exceptions.addAll(Arrays.asList(exceptions));
        }
    }

    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationNode((List<Object>)new ArrayList<Object>(0){

            @Override
            public boolean add(Object o2) {
                MethodNode.this.annotationDefault = o2;
                return super.add(o2);
            }
        });
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        AnnotationNode an = new AnnotationNode(desc);
        if (visible) {
            if (this.visibleParameterAnnotations == null) {
                int params = Type.getArgumentTypes(this.desc).length;
                this.visibleParameterAnnotations = new List[params];
            }
            if (this.visibleParameterAnnotations[parameter] == null) {
                this.visibleParameterAnnotations[parameter] = new ArrayList<AnnotationNode>(1);
            }
            this.visibleParameterAnnotations[parameter].add(an);
        } else {
            if (this.invisibleParameterAnnotations == null) {
                int params = Type.getArgumentTypes(this.desc).length;
                this.invisibleParameterAnnotations = new List[params];
            }
            if (this.invisibleParameterAnnotations[parameter] == null) {
                this.invisibleParameterAnnotations[parameter] = new ArrayList<AnnotationNode>(1);
            }
            this.invisibleParameterAnnotations[parameter].add(an);
        }
        return an;
    }

    public void visitCode() {
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        this.instructions.add(new FrameNode(type, nLocal, local == null ? null : this.getLabelNodes(local), nStack, stack == null ? null : this.getLabelNodes(stack)));
    }

    public void visitInsn(int opcode) {
        this.instructions.add(new InsnNode(opcode));
    }

    public void visitIntInsn(int opcode, int operand) {
        this.instructions.add(new IntInsnNode(opcode, operand));
    }

    public void visitVarInsn(int opcode, int var2) {
        this.instructions.add(new VarInsnNode(opcode, var2));
    }

    public void visitTypeInsn(int opcode, String type) {
        this.instructions.add(new TypeInsnNode(opcode, type));
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.instructions.add(new FieldInsnNode(opcode, owner, name, desc));
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.instructions.add(new MethodInsnNode(opcode, owner, name, desc));
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        this.instructions.add(new InvokeDynamicInsnNode(name, desc, bsm, bsmArgs));
    }

    public void visitJumpInsn(int opcode, Label label) {
        this.instructions.add(new JumpInsnNode(opcode, this.getLabelNode(label)));
    }

    public void visitLabel(Label label) {
        this.instructions.add(this.getLabelNode(label));
    }

    public void visitLdcInsn(Object cst) {
        this.instructions.add(new LdcInsnNode(cst));
    }

    public void visitIincInsn(int var2, int increment) {
        this.instructions.add(new IincInsnNode(var2, increment));
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        this.instructions.add(new TableSwitchInsnNode(min, max, this.getLabelNode(dflt), this.getLabelNodes(labels)));
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.instructions.add(new LookupSwitchInsnNode(this.getLabelNode(dflt), keys, this.getLabelNodes(labels)));
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.instructions.add(new MultiANewArrayInsnNode(desc, dims));
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.tryCatchBlocks.add(new TryCatchBlockNode(this.getLabelNode(start), this.getLabelNode(end), this.getLabelNode(handler), type));
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.localVariables.add(new LocalVariableNode(name, desc, signature, this.getLabelNode(start), this.getLabelNode(end), index));
    }

    public void visitLineNumber(int line, Label start) {
        this.instructions.add(new LineNumberNode(line, this.getLabelNode(start)));
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }

    protected LabelNode getLabelNode(Label l2) {
        if (!(l2.info instanceof LabelNode)) {
            l2.info = new LabelNode(l2);
        }
        return (LabelNode)l2.info;
    }

    private LabelNode[] getLabelNodes(Label[] l2) {
        LabelNode[] nodes = new LabelNode[l2.length];
        for (int i2 = 0; i2 < l2.length; ++i2) {
            nodes[i2] = this.getLabelNode(l2[i2]);
        }
        return nodes;
    }

    private Object[] getLabelNodes(Object[] objs) {
        Object[] nodes = new Object[objs.length];
        for (int i2 = 0; i2 < objs.length; ++i2) {
            Object o2 = objs[i2];
            if (o2 instanceof Label) {
                o2 = this.getLabelNode((Label)o2);
            }
            nodes[i2] = o2;
        }
        return nodes;
    }

    public void accept(ClassVisitor cv) {
        String[] exceptions = new String[this.exceptions.size()];
        this.exceptions.toArray(exceptions);
        MethodVisitor mv = cv.visitMethod(this.access, this.name, this.desc, this.signature, exceptions);
        if (mv != null) {
            this.accept(mv);
        }
    }

    public void accept(MethodVisitor mv) {
        AnnotationNode an;
        int j2;
        List<AnnotationNode> l2;
        AnnotationNode an2;
        int i2;
        if (this.annotationDefault != null) {
            AnnotationVisitor av = mv.visitAnnotationDefault();
            AnnotationNode.accept(av, null, this.annotationDefault);
            if (av != null) {
                av.visitEnd();
            }
        }
        int n2 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
        for (i2 = 0; i2 < n2; ++i2) {
            an2 = (AnnotationNode)this.visibleAnnotations.get(i2);
            an2.accept(mv.visitAnnotation(an2.desc, true));
        }
        n2 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
        for (i2 = 0; i2 < n2; ++i2) {
            an2 = (AnnotationNode)this.invisibleAnnotations.get(i2);
            an2.accept(mv.visitAnnotation(an2.desc, false));
        }
        n2 = this.visibleParameterAnnotations == null ? 0 : this.visibleParameterAnnotations.length;
        for (i2 = 0; i2 < n2; ++i2) {
            l2 = this.visibleParameterAnnotations[i2];
            if (l2 == null) continue;
            for (j2 = 0; j2 < l2.size(); ++j2) {
                an = l2.get(j2);
                an.accept(mv.visitParameterAnnotation(i2, an.desc, true));
            }
        }
        n2 = this.invisibleParameterAnnotations == null ? 0 : this.invisibleParameterAnnotations.length;
        for (i2 = 0; i2 < n2; ++i2) {
            l2 = this.invisibleParameterAnnotations[i2];
            if (l2 == null) continue;
            for (j2 = 0; j2 < l2.size(); ++j2) {
                an = l2.get(j2);
                an.accept(mv.visitParameterAnnotation(i2, an.desc, false));
            }
        }
        n2 = this.attrs == null ? 0 : this.attrs.size();
        for (i2 = 0; i2 < n2; ++i2) {
            mv.visitAttribute((Attribute)this.attrs.get(i2));
        }
        if (this.instructions.size() > 0) {
            mv.visitCode();
            n2 = this.tryCatchBlocks == null ? 0 : this.tryCatchBlocks.size();
            for (i2 = 0; i2 < n2; ++i2) {
                this.tryCatchBlocks.get(i2).accept(mv);
            }
            this.instructions.accept(mv);
            n2 = this.localVariables == null ? 0 : this.localVariables.size();
            for (i2 = 0; i2 < n2; ++i2) {
                this.localVariables.get(i2).accept(mv);
            }
            mv.visitMaxs(this.maxStack, this.maxLocals);
        }
        mv.visitEnd();
    }
}

