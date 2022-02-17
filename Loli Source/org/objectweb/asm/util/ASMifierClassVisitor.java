/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.PrintWriter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.ASMifierAbstractVisitor;
import org.objectweb.asm.util.ASMifierAnnotationVisitor;
import org.objectweb.asm.util.ASMifierFieldVisitor;
import org.objectweb.asm.util.ASMifierMethodVisitor;

public class ASMifierClassVisitor
extends ASMifierAbstractVisitor
implements ClassVisitor {
    private static final int ACCESS_CLASS = 262144;
    private static final int ACCESS_FIELD = 524288;
    private static final int ACCESS_INNER = 0x100000;
    protected final PrintWriter pw;

    public static void main(String[] args) throws Exception {
        int i2 = 0;
        int flags = 2;
        boolean ok = true;
        if (args.length < 1 || args.length > 2) {
            ok = false;
        }
        if (ok && "-debug".equals(args[0])) {
            i2 = 1;
            flags = 0;
            if (args.length != 2) {
                ok = false;
            }
        }
        if (!ok) {
            System.err.println("Prints the ASM code to generate the given class.");
            System.err.println("Usage: ASMifierClassVisitor [-debug] <fully qualified class name or class file name>");
            return;
        }
        ClassReader cr = args[i2].endsWith(".class") || args[i2].indexOf(92) > -1 || args[i2].indexOf(47) > -1 ? new ClassReader(new FileInputStream(args[i2])) : new ClassReader(args[i2]);
        cr.accept(new ASMifierClassVisitor(new PrintWriter(System.out)), ASMifierClassVisitor.getDefaultAttributes(), flags);
    }

    public ASMifierClassVisitor(PrintWriter pw) {
        super("cw");
        this.pw = pw;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String simpleName;
        int n2 = name.lastIndexOf(47);
        if (n2 == -1) {
            simpleName = name;
        } else {
            this.text.add("package asm." + name.substring(0, n2).replace('/', '.') + ";\n");
            simpleName = name.substring(n2 + 1);
        }
        this.text.add("import java.util.*;\n");
        this.text.add("import org.objectweb.asm.*;\n");
        this.text.add("import org.objectweb.asm.attrs.*;\n");
        this.text.add("public class " + simpleName + "Dump implements Opcodes {\n\n");
        this.text.add("public static byte[] dump () throws Exception {\n\n");
        this.text.add("ClassWriter cw = new ClassWriter(0);\n");
        this.text.add("FieldVisitor fv;\n");
        this.text.add("MethodVisitor mv;\n");
        this.text.add("AnnotationVisitor av0;\n\n");
        this.buf.setLength(0);
        this.buf.append("cw.visit(");
        switch (version) {
            case 196653: {
                this.buf.append("V1_1");
                break;
            }
            case 46: {
                this.buf.append("V1_2");
                break;
            }
            case 47: {
                this.buf.append("V1_3");
                break;
            }
            case 48: {
                this.buf.append("V1_4");
                break;
            }
            case 49: {
                this.buf.append("V1_5");
                break;
            }
            case 50: {
                this.buf.append("V1_6");
                break;
            }
            case 51: {
                this.buf.append("V1_7");
                break;
            }
            default: {
                this.buf.append(version);
            }
        }
        this.buf.append(", ");
        this.appendAccess(access | 0x40000);
        this.buf.append(", ");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(signature);
        this.buf.append(", ");
        this.appendConstant(superName);
        this.buf.append(", ");
        if (interfaces != null && interfaces.length > 0) {
            this.buf.append("new String[] {");
            for (int i2 = 0; i2 < interfaces.length; ++i2) {
                this.buf.append(i2 == 0 ? " " : ", ");
                this.appendConstant(interfaces[i2]);
            }
            this.buf.append(" }");
        } else {
            this.buf.append("null");
        }
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    public void visitSource(String file, String debug) {
        this.buf.setLength(0);
        this.buf.append("cw.visitSource(");
        this.appendConstant(file);
        this.buf.append(", ");
        this.appendConstant(debug);
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    public void visitOuterClass(String owner, String name, String desc) {
        this.buf.setLength(0);
        this.buf.append("cw.visitOuterClass(");
        this.appendConstant(owner);
        this.buf.append(", ");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.buf.setLength(0);
        this.buf.append("cw.visitInnerClass(");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(outerName);
        this.buf.append(", ");
        this.appendConstant(innerName);
        this.buf.append(", ");
        this.appendAccess(access | 0x100000);
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("fv = cw.visitField(");
        this.appendAccess(access | 0x80000);
        this.buf.append(", ");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(", ");
        this.appendConstant(signature);
        this.buf.append(", ");
        this.appendConstant(value);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifierFieldVisitor aav = new ASMifierFieldVisitor();
        this.text.add(aav.getText());
        this.text.add("}\n");
        return aav;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("mv = cw.visitMethod(");
        this.appendAccess(access);
        this.buf.append(", ");
        this.appendConstant(name);
        this.buf.append(", ");
        this.appendConstant(desc);
        this.buf.append(", ");
        this.appendConstant(signature);
        this.buf.append(", ");
        if (exceptions != null && exceptions.length > 0) {
            this.buf.append("new String[] {");
            for (int i2 = 0; i2 < exceptions.length; ++i2) {
                this.buf.append(i2 == 0 ? " " : ", ");
                this.appendConstant(exceptions[i2]);
            }
            this.buf.append(" }");
        } else {
            this.buf.append("null");
        }
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifierMethodVisitor acv = this.createASMifierMethodVisitor();
        this.text.add(acv.getText());
        this.text.add("}\n");
        return acv;
    }

    protected ASMifierMethodVisitor createASMifierMethodVisitor() {
        return new ASMifierMethodVisitor();
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("av0 = cw.visitAnnotation(");
        this.appendConstant(desc);
        this.buf.append(", ");
        this.buf.append(visible);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(0);
        this.text.add(av.getText());
        this.text.add("}\n");
        return av;
    }

    public void visitEnd() {
        this.text.add("cw.visitEnd();\n\n");
        this.text.add("return cw.toByteArray();\n");
        this.text.add("}\n");
        this.text.add("}\n");
        ASMifierClassVisitor.printList(this.pw, this.text);
        this.pw.flush();
    }

    void appendAccess(int access) {
        boolean first = true;
        if ((access & 1) != 0) {
            this.buf.append("ACC_PUBLIC");
            first = false;
        }
        if ((access & 2) != 0) {
            this.buf.append("ACC_PRIVATE");
            first = false;
        }
        if ((access & 4) != 0) {
            this.buf.append("ACC_PROTECTED");
            first = false;
        }
        if ((access & 0x10) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_FINAL");
            first = false;
        }
        if ((access & 8) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_STATIC");
            first = false;
        }
        if ((access & 0x20) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            if ((access & 0x40000) == 0) {
                this.buf.append("ACC_SYNCHRONIZED");
            } else {
                this.buf.append("ACC_SUPER");
            }
            first = false;
        }
        if ((access & 0x40) != 0 && (access & 0x80000) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_VOLATILE");
            first = false;
        }
        if ((access & 0x40) != 0 && (access & 0x40000) == 0 && (access & 0x80000) == 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_BRIDGE");
            first = false;
        }
        if ((access & 0x80) != 0 && (access & 0x40000) == 0 && (access & 0x80000) == 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_VARARGS");
            first = false;
        }
        if ((access & 0x80) != 0 && (access & 0x80000) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_TRANSIENT");
            first = false;
        }
        if ((access & 0x100) != 0 && (access & 0x40000) == 0 && (access & 0x80000) == 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_NATIVE");
            first = false;
        }
        if ((access & 0x4000) != 0 && ((access & 0x40000) != 0 || (access & 0x80000) != 0 || (access & 0x100000) != 0)) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_ENUM");
            first = false;
        }
        if ((access & 0x2000) != 0 && (access & 0x40000) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_ANNOTATION");
            first = false;
        }
        if ((access & 0x400) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_ABSTRACT");
            first = false;
        }
        if ((access & 0x200) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_INTERFACE");
            first = false;
        }
        if ((access & 0x800) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_STRICT");
            first = false;
        }
        if ((access & 0x1000) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_SYNTHETIC");
            first = false;
        }
        if ((access & 0x20000) != 0) {
            if (!first) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_DEPRECATED");
            first = false;
        }
        if (first) {
            this.buf.append('0');
        }
    }
}

