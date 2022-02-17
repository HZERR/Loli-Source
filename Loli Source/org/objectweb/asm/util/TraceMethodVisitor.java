/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.util.AbstractVisitor;
import org.objectweb.asm.util.TraceAbstractVisitor;
import org.objectweb.asm.util.TraceAnnotationVisitor;
import org.objectweb.asm.util.TraceSignatureVisitor;
import org.objectweb.asm.util.Traceable;

public class TraceMethodVisitor
extends TraceAbstractVisitor
implements MethodVisitor {
    protected MethodVisitor mv;
    protected String tab2 = "    ";
    protected String tab3 = "      ";
    protected String ltab = "   ";
    protected final Map<Label, String> labelNames = new HashMap<Label, String>();

    public TraceMethodVisitor() {
        this(null);
    }

    public TraceMethodVisitor(MethodVisitor mv) {
        this.mv = mv;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        if (this.mv != null) {
            ((TraceAnnotationVisitor)av).av = this.mv.visitAnnotation(desc, visible);
        }
        return av;
    }

    public void visitAttribute(Attribute attr) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("ATTRIBUTE ");
        this.appendDescriptor(-1, attr.type);
        if (attr instanceof Traceable) {
            ((Traceable)((Object)attr)).trace(this.buf, this.labelNames);
        } else {
            this.buf.append(" : unknown\n");
        }
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitAttribute(attr);
        }
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.text.add(this.tab2 + "default=");
        TraceAnnotationVisitor tav = this.createTraceAnnotationVisitor();
        this.text.add(tav.getText());
        this.text.add("\n");
        if (this.mv != null) {
            tav.av = this.mv.visitAnnotationDefault();
        }
        return tav;
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append('@');
        this.appendDescriptor(1, desc);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        TraceAnnotationVisitor tav = this.createTraceAnnotationVisitor();
        this.text.add(tav.getText());
        this.text.add(visible ? ") // parameter " : ") // invisible, parameter ");
        this.text.add(new Integer(parameter));
        this.text.add("\n");
        if (this.mv != null) {
            tav.av = this.mv.visitParameterAnnotation(parameter, desc, visible);
        }
        return tav;
    }

    public void visitCode() {
        if (this.mv != null) {
            this.mv.visitCode();
        }
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        this.buf.setLength(0);
        this.buf.append(this.ltab);
        this.buf.append("FRAME ");
        switch (type) {
            case -1: 
            case 0: {
                this.buf.append("FULL [");
                this.appendFrameTypes(nLocal, local);
                this.buf.append("] [");
                this.appendFrameTypes(nStack, stack);
                this.buf.append(']');
                break;
            }
            case 1: {
                this.buf.append("APPEND [");
                this.appendFrameTypes(nLocal, local);
                this.buf.append(']');
                break;
            }
            case 2: {
                this.buf.append("CHOP ").append(nLocal);
                break;
            }
            case 3: {
                this.buf.append("SAME");
                break;
            }
            case 4: {
                this.buf.append("SAME1 ");
                this.appendFrameTypes(1, stack);
            }
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    public void visitInsn(int opcode) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitInsn(opcode);
        }
    }

    public void visitIntInsn(int opcode, int operand) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append(' ').append(opcode == 188 ? TYPES[operand] : Integer.toString(operand)).append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitIntInsn(opcode, operand);
        }
    }

    public void visitVarInsn(int opcode, int var2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append(' ').append(var2).append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitVarInsn(opcode, var2);
        }
    }

    public void visitTypeInsn(int opcode, String type) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append(' ');
        this.appendDescriptor(0, type);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitTypeInsn(opcode, type);
        }
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append(' ');
        this.appendDescriptor(0, owner);
        this.buf.append('.').append(name).append(" : ");
        this.appendDescriptor(1, desc);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append(' ');
        this.appendDescriptor(0, owner);
        this.buf.append('.').append(name).append(' ');
        this.appendDescriptor(3, desc);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("INVOKEDYNAMIC").append(' ');
        this.buf.append(name).append(' ');
        this.appendDescriptor(3, desc);
        this.buf.append(" [").append(bsm).append(", ");
        for (int i2 = 0; i2 < bsmArgs.length; ++i2) {
            Object cst = bsmArgs[i2];
            if (cst instanceof String) {
                AbstractVisitor.appendString(this.buf, (String)cst);
            } else if (cst instanceof Type) {
                this.buf.append(((Type)cst).getDescriptor()).append(".class");
            } else {
                this.buf.append(cst);
            }
            this.buf.append(", ");
        }
        this.buf.setLength(this.buf.length() - 2);
        this.buf.append("]\n");
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        }
    }

    public void visitJumpInsn(int opcode, Label label) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[opcode]).append(' ');
        this.appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitJumpInsn(opcode, label);
        }
    }

    public void visitLabel(Label label) {
        this.buf.setLength(0);
        this.buf.append(this.ltab);
        this.appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitLabel(label);
        }
    }

    public void visitLdcInsn(Object cst) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LDC ");
        if (cst instanceof String) {
            AbstractVisitor.appendString(this.buf, (String)cst);
        } else if (cst instanceof Type) {
            this.buf.append(((Type)cst).getDescriptor()).append(".class");
        } else {
            this.buf.append(cst);
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitLdcInsn(cst);
        }
    }

    public void visitIincInsn(int var2, int increment) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("IINC ").append(var2).append(' ').append(increment).append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitIincInsn(var2, increment);
        }
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("TABLESWITCH\n");
        for (int i2 = 0; i2 < labels.length; ++i2) {
            this.buf.append(this.tab3).append(min + i2).append(": ");
            this.appendLabel(labels[i2]);
            this.buf.append('\n');
        }
        this.buf.append(this.tab3).append("default: ");
        this.appendLabel(dflt);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LOOKUPSWITCH\n");
        for (int i2 = 0; i2 < labels.length; ++i2) {
            this.buf.append(this.tab3).append(keys[i2]).append(": ");
            this.appendLabel(labels[i2]);
            this.buf.append('\n');
        }
        this.buf.append(this.tab3).append("default: ");
        this.appendLabel(dflt);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("MULTIANEWARRAY ");
        this.appendDescriptor(1, desc);
        this.buf.append(' ').append(dims).append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(desc, dims);
        }
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("TRYCATCHBLOCK ");
        this.appendLabel(start);
        this.buf.append(' ');
        this.appendLabel(end);
        this.buf.append(' ');
        this.appendLabel(handler);
        this.buf.append(' ');
        this.appendDescriptor(0, type);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitTryCatchBlock(start, end, handler, type);
        }
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LOCALVARIABLE ").append(name).append(' ');
        this.appendDescriptor(1, desc);
        this.buf.append(' ');
        this.appendLabel(start);
        this.buf.append(' ');
        this.appendLabel(end);
        this.buf.append(' ').append(index).append('\n');
        if (signature != null) {
            this.buf.append(this.tab2);
            this.appendDescriptor(2, signature);
            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r2 = new SignatureReader(signature);
            r2.acceptType(sv);
            this.buf.append(this.tab2).append("// declaration: ").append(sv.getDeclaration()).append('\n');
        }
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

    public void visitLineNumber(int line, Label start) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LINENUMBER ").append(line).append(' ');
        this.appendLabel(start);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitLineNumber(line, start);
        }
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("MAXSTACK = ").append(maxStack).append('\n');
        this.text.add(this.buf.toString());
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("MAXLOCALS = ").append(maxLocals).append('\n');
        this.text.add(this.buf.toString());
        if (this.mv != null) {
            this.mv.visitMaxs(maxStack, maxLocals);
        }
    }

    public void visitEnd() {
        super.visitEnd();
        if (this.mv != null) {
            this.mv.visitEnd();
        }
    }

    private void appendFrameTypes(int n2, Object[] o2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            if (i2 > 0) {
                this.buf.append(' ');
            }
            if (o2[i2] instanceof String) {
                String desc = (String)o2[i2];
                if (desc.startsWith("[")) {
                    this.appendDescriptor(1, desc);
                    continue;
                }
                this.appendDescriptor(0, desc);
                continue;
            }
            if (o2[i2] instanceof Integer) {
                switch ((Integer)o2[i2]) {
                    case 0: {
                        this.appendDescriptor(1, "T");
                        break;
                    }
                    case 1: {
                        this.appendDescriptor(1, "I");
                        break;
                    }
                    case 2: {
                        this.appendDescriptor(1, "F");
                        break;
                    }
                    case 3: {
                        this.appendDescriptor(1, "D");
                        break;
                    }
                    case 4: {
                        this.appendDescriptor(1, "J");
                        break;
                    }
                    case 5: {
                        this.appendDescriptor(1, "N");
                        break;
                    }
                    case 6: {
                        this.appendDescriptor(1, "U");
                    }
                }
                continue;
            }
            this.appendLabel((Label)o2[i2]);
        }
    }

    protected void appendLabel(Label l2) {
        String name = this.labelNames.get(l2);
        if (name == null) {
            name = "L" + this.labelNames.size();
            this.labelNames.put(l2, name);
        }
        this.buf.append(name);
    }
}

