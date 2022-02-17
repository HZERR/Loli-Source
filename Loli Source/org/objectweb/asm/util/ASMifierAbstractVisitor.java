/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodType;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.ASMifiable;
import org.objectweb.asm.util.ASMifierAnnotationVisitor;
import org.objectweb.asm.util.AbstractVisitor;

public class ASMifierAbstractVisitor
extends AbstractVisitor {
    protected String name;
    Map<Label, String> labelNames;

    protected ASMifierAbstractVisitor(String name) {
        this.name = name;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitAnnotation(");
        this.appendConstant(desc);
        this.buf.append(", ").append(visible).append(");\n");
        this.text.add(this.buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(0);
        this.text.add(av.getText());
        this.text.add("}\n");
        return av;
    }

    public void visitAttribute(Attribute attr) {
        this.buf.setLength(0);
        this.buf.append("// ATTRIBUTE ").append(attr.type).append('\n');
        if (attr instanceof ASMifiable) {
            this.buf.append("{\n");
            ((ASMifiable)((Object)attr)).asmify(this.buf, "attr", this.labelNames);
            this.buf.append(this.name).append(".visitAttribute(attr);\n");
            this.buf.append("}\n");
        }
        this.text.add(this.buf.toString());
    }

    public void visitEnd() {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitEnd();\n");
        this.text.add(this.buf.toString());
    }

    void appendConstant(Object cst) {
        ASMifierAbstractVisitor.appendConstant(this.buf, cst);
    }

    static void appendConstant(StringBuffer buf, Object cst) {
        if (cst == null) {
            buf.append("null");
        } else if (cst instanceof String) {
            ASMifierAbstractVisitor.appendString(buf, (String)cst);
        } else if (cst instanceof Type) {
            buf.append("Type.getType(\"");
            buf.append(((Type)cst).getDescriptor());
            buf.append("\")");
        } else if (cst instanceof MethodType) {
            buf.append("new MethodType(\"");
            buf.append(((MethodType)cst).getDescriptor());
            buf.append("\")");
        } else if (cst instanceof MethodHandle) {
            buf.append("new MethodHandle(");
            MethodHandle mHandle = (MethodHandle)cst;
            buf.append("MethodHandle.").append(METHOD_HANDLE_TAG[mHandle.getTag()]).append(", \"");
            buf.append(mHandle.getOwner()).append("\", \"");
            buf.append(mHandle.getName()).append("\", \"");
            buf.append(mHandle.getDesc()).append("\")");
        } else if (cst instanceof Byte) {
            buf.append("new Byte((byte)").append(cst).append(')');
        } else if (cst instanceof Boolean) {
            buf.append((Boolean)cst != false ? "Boolean.TRUE" : "Boolean.FALSE");
        } else if (cst instanceof Short) {
            buf.append("new Short((short)").append(cst).append(')');
        } else if (cst instanceof Character) {
            char c2 = ((Character)cst).charValue();
            buf.append("new Character((char)").append((int)c2).append(')');
        } else if (cst instanceof Integer) {
            buf.append("new Integer(").append(cst).append(')');
        } else if (cst instanceof Float) {
            buf.append("new Float(\"").append(cst).append("\")");
        } else if (cst instanceof Long) {
            buf.append("new Long(").append(cst).append("L)");
        } else if (cst instanceof Double) {
            buf.append("new Double(\"").append(cst).append("\")");
        } else if (cst instanceof byte[]) {
            byte[] v2 = (byte[])cst;
            buf.append("new byte[] {");
            for (int i2 = 0; i2 < v2.length; ++i2) {
                buf.append(i2 == 0 ? "" : ",").append(v2[i2]);
            }
            buf.append('}');
        } else if (cst instanceof boolean[]) {
            boolean[] v3 = (boolean[])cst;
            buf.append("new boolean[] {");
            for (int i3 = 0; i3 < v3.length; ++i3) {
                buf.append(i3 == 0 ? "" : ",").append(v3[i3]);
            }
            buf.append('}');
        } else if (cst instanceof short[]) {
            short[] v4 = (short[])cst;
            buf.append("new short[] {");
            for (int i4 = 0; i4 < v4.length; ++i4) {
                buf.append(i4 == 0 ? "" : ",").append("(short)").append(v4[i4]);
            }
            buf.append('}');
        } else if (cst instanceof char[]) {
            char[] v5 = (char[])cst;
            buf.append("new char[] {");
            for (int i5 = 0; i5 < v5.length; ++i5) {
                buf.append(i5 == 0 ? "" : ",").append("(char)").append((int)v5[i5]);
            }
            buf.append('}');
        } else if (cst instanceof int[]) {
            int[] v6 = (int[])cst;
            buf.append("new int[] {");
            for (int i6 = 0; i6 < v6.length; ++i6) {
                buf.append(i6 == 0 ? "" : ",").append(v6[i6]);
            }
            buf.append('}');
        } else if (cst instanceof long[]) {
            long[] v7 = (long[])cst;
            buf.append("new long[] {");
            for (int i7 = 0; i7 < v7.length; ++i7) {
                buf.append(i7 == 0 ? "" : ",").append(v7[i7]).append('L');
            }
            buf.append('}');
        } else if (cst instanceof float[]) {
            float[] v8 = (float[])cst;
            buf.append("new float[] {");
            for (int i8 = 0; i8 < v8.length; ++i8) {
                buf.append(i8 == 0 ? "" : ",").append(v8[i8]).append('f');
            }
            buf.append('}');
        } else if (cst instanceof double[]) {
            double[] v9 = (double[])cst;
            buf.append("new double[] {");
            for (int i9 = 0; i9 < v9.length; ++i9) {
                buf.append(i9 == 0 ? "" : ",").append(v9[i9]).append('d');
            }
            buf.append('}');
        }
    }
}

