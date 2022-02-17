/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.util.HashMap;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.ASMifierAbstractVisitor;
import org.objectweb.asm.util.ASMifierAnnotationVisitor;

public class ASMifierMethodVisitor
extends ASMifierAbstractVisitor
implements MethodVisitor {
    public ASMifierMethodVisitor() {
        super("mv");
        this.labelNames = new HashMap();
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = mv.visitAnnotationDefault();\n");
        this.text.add(this.buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(0);
        this.text.add(av.getText());
        this.text.add("}\n");
        return av;
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = mv.visitParameterAnnotation(").append(parameter).append(", ");
        this.appendConstant(desc);
        this.buf.append(", ").append(visible).append(");\n");
        this.text.add(this.buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(0);
        this.text.add(av.getText());
        this.text.add("}\n");
        return av;
    }

    public void visitCode() {
        this.text.add("mv.visitCode();\n");
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        this.buf.setLength(0);
        switch (type) {
            case -1: 
            case 0: {
                this.declareFrameTypes(nLocal, local);
                this.declareFrameTypes(nStack, stack);
                if (type == -1) {
                    this.buf.append("mv.visitFrame(Opcodes.F_NEW, ");
                } else {
                    this.buf.append("mv.visitFrame(Opcodes.F_FULL, ");
                }
                this.buf.append(nLocal).append(", new Object[] {");
                this.appendFrameTypes(nLocal, local);
                this.buf.append("}, ").append(nStack).append(", new Object[] {");
                this.appendFrameTypes(nStack, stack);
                this.buf.append('}');
                break;
            }
            case 1: {
                this.declareFrameTypes(nLocal, local);
                this.buf.append("mv.visitFrame(Opcodes.F_APPEND,").append(nLocal).append(", new Object[] {");
                this.appendFrameTypes(nLocal, local);
                this.buf.append("}, 0, null");
                break;
            }
            case 2: {
                this.buf.append("mv.visitFrame(Opcodes.F_CHOP,").append(nLocal).append(", null, 0, null");
                break;
            }
            case 3: {
                this.buf.append("mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null");
                break;
            }
            case 4: {
                this.declareFrameTypes(1, stack);
                this.buf.append("mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {");
                this.appendFrameTypes(1, stack);
                this.buf.append('}');
            }
        }
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitInsn(int opcode) {
        this.buf.setLength(0);
        this.buf.append("mv.visitInsn(").append(OPCODES[opcode]).append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitIntInsn(int opcode, int operand) {
        this.buf.setLength(0);
        this.buf.append("mv.visitIntInsn(").append(OPCODES[opcode]).append(", ").append(opcode == 188 ? TYPES[operand] : Integer.toString(operand)).append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitVarInsn(int opcode, int var2) {
        this.buf.setLength(0);
        this.buf.append("mv.visitVarInsn(").append(OPCODES[opcode]).append(", ").append(var2).append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitTypeInsn(int opcode, String type) {
        this.buf.setLength(0);
        this.buf.append("mv.visitTypeInsn(").append(OPCODES[opcode]).append(", ");
        this.appendConstant(type);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.buf.setLength(0);
        this.buf.append("mv.visitFieldInsn(").append(OPCODES[opcode]).append(", ");
        this.appendConstant(owner);
        this.buf.append(", ");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.buf.setLength(0);
        this.buf.append("mv.visitMethodInsn(").append(OPCODES[opcode]).append(", ");
        this.appendConstant(owner);
        this.buf.append(", ");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        this.buf.setLength(0);
        this.buf.append("mv.visitInvokeDynamicInsn(");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(", ");
        this.appendConstant(bsm);
        this.buf.append(", new Object[]{");
        for (int i2 = 0; i2 < bsmArgs.length; ++i2) {
            this.appendConstant(bsmArgs[i2]);
            if (i2 == bsmArgs.length - 1) continue;
            this.buf.append(", ");
        }
        this.buf.append("});\n");
        this.text.add(this.buf.toString());
    }

    public void visitJumpInsn(int opcode, Label label) {
        this.buf.setLength(0);
        this.declareLabel(label);
        this.buf.append("mv.visitJumpInsn(").append(OPCODES[opcode]).append(", ");
        this.appendLabel(label);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitLabel(Label label) {
        this.buf.setLength(0);
        this.declareLabel(label);
        this.buf.append("mv.visitLabel(");
        this.appendLabel(label);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitLdcInsn(Object cst) {
        this.buf.setLength(0);
        this.buf.append("mv.visitLdcInsn(");
        this.appendConstant(cst);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitIincInsn(int var2, int increment) {
        this.buf.setLength(0);
        this.buf.append("mv.visitIincInsn(").append(var2).append(", ").append(increment).append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        int i2;
        this.buf.setLength(0);
        for (i2 = 0; i2 < labels.length; ++i2) {
            this.declareLabel(labels[i2]);
        }
        this.declareLabel(dflt);
        this.buf.append("mv.visitTableSwitchInsn(").append(min).append(", ").append(max).append(", ");
        this.appendLabel(dflt);
        this.buf.append(", new Label[] {");
        for (i2 = 0; i2 < labels.length; ++i2) {
            this.buf.append(i2 == 0 ? " " : ", ");
            this.appendLabel(labels[i2]);
        }
        this.buf.append(" });\n");
        this.text.add(this.buf.toString());
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        int i2;
        this.buf.setLength(0);
        for (i2 = 0; i2 < labels.length; ++i2) {
            this.declareLabel(labels[i2]);
        }
        this.declareLabel(dflt);
        this.buf.append("mv.visitLookupSwitchInsn(");
        this.appendLabel(dflt);
        this.buf.append(", new int[] {");
        for (i2 = 0; i2 < keys.length; ++i2) {
            this.buf.append(i2 == 0 ? " " : ", ").append(keys[i2]);
        }
        this.buf.append(" }, new Label[] {");
        for (i2 = 0; i2 < labels.length; ++i2) {
            this.buf.append(i2 == 0 ? " " : ", ");
            this.appendLabel(labels[i2]);
        }
        this.buf.append(" });\n");
        this.text.add(this.buf.toString());
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.buf.setLength(0);
        this.buf.append("mv.visitMultiANewArrayInsn(");
        this.appendConstant(desc);
        this.buf.append(", ").append(dims).append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.buf.setLength(0);
        this.declareLabel(start);
        this.declareLabel(end);
        this.declareLabel(handler);
        this.buf.append("mv.visitTryCatchBlock(");
        this.appendLabel(start);
        this.buf.append(", ");
        this.appendLabel(end);
        this.buf.append(", ");
        this.appendLabel(handler);
        this.buf.append(", ");
        this.appendConstant(type);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.buf.setLength(0);
        this.buf.append("mv.visitLocalVariable(");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(", ");
        this.appendConstant(signature);
        this.buf.append(", ");
        this.appendLabel(start);
        this.buf.append(", ");
        this.appendLabel(end);
        this.buf.append(", ").append(index).append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitLineNumber(int line, Label start) {
        this.buf.setLength(0);
        this.buf.append("mv.visitLineNumber(").append(line).append(", ");
        this.appendLabel(start);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        this.buf.setLength(0);
        this.buf.append("mv.visitMaxs(").append(maxStack).append(", ").append(maxLocals).append(");\n");
        this.text.add(this.buf.toString());
    }

    private void declareFrameTypes(int n2, Object[] o2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!(o2[i2] instanceof Label)) continue;
            this.declareLabel((Label)o2[i2]);
        }
    }

    private void appendFrameTypes(int n2, Object[] o2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            if (i2 > 0) {
                this.buf.append(", ");
            }
            if (o2[i2] instanceof String) {
                this.appendConstant(o2[i2]);
                continue;
            }
            if (o2[i2] instanceof Integer) {
                switch ((Integer)o2[i2]) {
                    case 0: {
                        this.buf.append("Opcodes.TOP");
                        break;
                    }
                    case 1: {
                        this.buf.append("Opcodes.INTEGER");
                        break;
                    }
                    case 2: {
                        this.buf.append("Opcodes.FLOAT");
                        break;
                    }
                    case 3: {
                        this.buf.append("Opcodes.DOUBLE");
                        break;
                    }
                    case 4: {
                        this.buf.append("Opcodes.LONG");
                        break;
                    }
                    case 5: {
                        this.buf.append("Opcodes.NULL");
                        break;
                    }
                    case 6: {
                        this.buf.append("Opcodes.UNINITIALIZED_THIS");
                    }
                }
                continue;
            }
            this.appendLabel((Label)o2[i2]);
        }
    }

    private void declareLabel(Label l2) {
        String name = (String)this.labelNames.get(l2);
        if (name == null) {
            name = "l" + this.labelNames.size();
            this.labelNames.put(l2, name);
            this.buf.append("Label ").append(name).append(" = new Label();\n");
        }
    }

    private void appendLabel(Label l2) {
        this.buf.append((String)this.labelNames.get(l2));
    }
}

