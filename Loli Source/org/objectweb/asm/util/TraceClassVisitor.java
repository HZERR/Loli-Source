/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.PrintWriter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.util.TraceAbstractVisitor;
import org.objectweb.asm.util.TraceAnnotationVisitor;
import org.objectweb.asm.util.TraceFieldVisitor;
import org.objectweb.asm.util.TraceMethodVisitor;
import org.objectweb.asm.util.TraceSignatureVisitor;

public class TraceClassVisitor
extends TraceAbstractVisitor
implements ClassVisitor {
    protected final ClassVisitor cv;
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
            System.err.println("Prints a disassembled view of the given class.");
            System.err.println("Usage: TraceClassVisitor [-debug] <fully qualified class name or class file name>");
            return;
        }
        ClassReader cr = args[i2].endsWith(".class") || args[i2].indexOf(92) > -1 || args[i2].indexOf(47) > -1 ? new ClassReader(new FileInputStream(args[i2])) : new ClassReader(args[i2]);
        cr.accept(new TraceClassVisitor(new PrintWriter(System.out)), TraceClassVisitor.getDefaultAttributes(), flags);
    }

    public TraceClassVisitor(PrintWriter pw) {
        this(null, pw);
    }

    public TraceClassVisitor(ClassVisitor cv, PrintWriter pw) {
        this.cv = cv;
        this.pw = pw;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        int major = version & 0xFFFF;
        int minor = version >>> 16;
        this.buf.setLength(0);
        this.buf.append("// class version ").append(major).append('.').append(minor).append(" (").append(version).append(")\n");
        if ((access & 0x20000) != 0) {
            this.buf.append("// DEPRECATED\n");
        }
        this.buf.append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        this.appendDescriptor(5, signature);
        if (signature != null) {
            TraceSignatureVisitor sv = new TraceSignatureVisitor(access);
            SignatureReader r2 = new SignatureReader(signature);
            r2.accept(sv);
            this.buf.append("// declaration: ").append(name).append(sv.getDeclaration()).append('\n');
        }
        this.appendAccess(access & 0xFFFFFFDF);
        if ((access & 0x2000) != 0) {
            this.buf.append("@interface ");
        } else if ((access & 0x200) != 0) {
            this.buf.append("interface ");
        } else if ((access & 0x4000) == 0) {
            this.buf.append("class ");
        }
        this.appendDescriptor(0, name);
        if (superName != null && !"java/lang/Object".equals(superName)) {
            this.buf.append(" extends ");
            this.appendDescriptor(0, superName);
            this.buf.append(' ');
        }
        if (interfaces != null && interfaces.length > 0) {
            this.buf.append(" implements ");
            for (int i2 = 0; i2 < interfaces.length; ++i2) {
                this.appendDescriptor(0, interfaces[i2]);
                this.buf.append(' ');
            }
        }
        this.buf.append(" {\n\n");
        this.text.add(this.buf.toString());
        if (this.cv != null) {
            this.cv.visit(version, access, name, signature, superName, interfaces);
        }
    }

    public void visitSource(String file, String debug) {
        this.buf.setLength(0);
        if (file != null) {
            this.buf.append(this.tab).append("// compiled from: ").append(file).append('\n');
        }
        if (debug != null) {
            this.buf.append(this.tab).append("// debug info: ").append(debug).append('\n');
        }
        if (this.buf.length() > 0) {
            this.text.add(this.buf.toString());
        }
        if (this.cv != null) {
            this.cv.visitSource(file, debug);
        }
    }

    public void visitOuterClass(String owner, String name, String desc) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("OUTERCLASS ");
        this.appendDescriptor(0, owner);
        this.buf.append(' ');
        if (name != null) {
            this.buf.append(name).append(' ');
        }
        this.appendDescriptor(3, desc);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.cv != null) {
            this.cv.visitOuterClass(owner, name, desc);
        }
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.text.add("\n");
        AnnotationVisitor tav = super.visitAnnotation(desc, visible);
        if (this.cv != null) {
            ((TraceAnnotationVisitor)tav).av = this.cv.visitAnnotation(desc, visible);
        }
        return tav;
    }

    public void visitAttribute(Attribute attr) {
        this.text.add("\n");
        super.visitAttribute(attr);
        if (this.cv != null) {
            this.cv.visitAttribute(attr);
        }
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("// access flags 0x");
        this.buf.append(Integer.toHexString(access & 0xFFFFFFDF).toUpperCase()).append('\n');
        this.buf.append(this.tab);
        this.appendAccess(access);
        this.buf.append("INNERCLASS ");
        this.appendDescriptor(0, name);
        this.buf.append(' ');
        this.appendDescriptor(0, outerName);
        this.buf.append(' ');
        this.appendDescriptor(0, innerName);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        if (this.cv != null) {
            this.cv.visitInnerClass(name, outerName, innerName, access);
        }
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        this.buf.setLength(0);
        this.buf.append('\n');
        if ((access & 0x20000) != 0) {
            this.buf.append(this.tab).append("// DEPRECATED\n");
        }
        this.buf.append(this.tab).append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        if (signature != null) {
            this.buf.append(this.tab);
            this.appendDescriptor(2, signature);
            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r2 = new SignatureReader(signature);
            r2.acceptType(sv);
            this.buf.append(this.tab).append("// declaration: ").append(sv.getDeclaration()).append('\n');
        }
        this.buf.append(this.tab);
        this.appendAccess(access);
        this.appendDescriptor(1, desc);
        this.buf.append(' ').append(name);
        if (value != null) {
            this.buf.append(" = ");
            if (value instanceof String) {
                this.buf.append('\"').append(value).append('\"');
            } else {
                this.buf.append(value);
            }
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        TraceFieldVisitor tav = this.createTraceFieldVisitor();
        this.text.add(tav.getText());
        if (this.cv != null) {
            tav.fv = this.cv.visitField(access, name, desc, signature, value);
        }
        return tav;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.buf.setLength(0);
        this.buf.append('\n');
        if ((access & 0x20000) != 0) {
            this.buf.append(this.tab).append("// DEPRECATED\n");
        }
        this.buf.append(this.tab).append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        if (signature != null) {
            this.buf.append(this.tab);
            this.appendDescriptor(4, signature);
            TraceSignatureVisitor v2 = new TraceSignatureVisitor(0);
            SignatureReader r2 = new SignatureReader(signature);
            r2.accept(v2);
            String genericDecl = v2.getDeclaration();
            String genericReturn = v2.getReturnType();
            String genericExceptions = v2.getExceptions();
            this.buf.append(this.tab).append("// declaration: ").append(genericReturn).append(' ').append(name).append(genericDecl);
            if (genericExceptions != null) {
                this.buf.append(" throws ").append(genericExceptions);
            }
            this.buf.append('\n');
        }
        this.buf.append(this.tab);
        this.appendAccess(access);
        if ((access & 0x100) != 0) {
            this.buf.append("native ");
        }
        if ((access & 0x80) != 0) {
            this.buf.append("varargs ");
        }
        if ((access & 0x40) != 0) {
            this.buf.append("bridge ");
        }
        this.buf.append(name);
        this.appendDescriptor(3, desc);
        if (exceptions != null && exceptions.length > 0) {
            this.buf.append(" throws ");
            for (int i2 = 0; i2 < exceptions.length; ++i2) {
                this.appendDescriptor(0, exceptions[i2]);
                this.buf.append(' ');
            }
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        TraceMethodVisitor tcv = this.createTraceMethodVisitor();
        this.text.add(tcv.getText());
        if (this.cv != null) {
            tcv.mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
        return tcv;
    }

    public void visitEnd() {
        this.text.add("}\n");
        this.print(this.pw);
        this.pw.flush();
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }

    protected TraceFieldVisitor createTraceFieldVisitor() {
        return new TraceFieldVisitor();
    }

    protected TraceMethodVisitor createTraceMethodVisitor() {
        return new TraceMethodVisitor();
    }

    private void appendAccess(int access) {
        if ((access & 1) != 0) {
            this.buf.append("public ");
        }
        if ((access & 2) != 0) {
            this.buf.append("private ");
        }
        if ((access & 4) != 0) {
            this.buf.append("protected ");
        }
        if ((access & 0x10) != 0) {
            this.buf.append("final ");
        }
        if ((access & 8) != 0) {
            this.buf.append("static ");
        }
        if ((access & 0x20) != 0) {
            this.buf.append("synchronized ");
        }
        if ((access & 0x40) != 0) {
            this.buf.append("volatile ");
        }
        if ((access & 0x80) != 0) {
            this.buf.append("transient ");
        }
        if ((access & 0x400) != 0) {
            this.buf.append("abstract ");
        }
        if ((access & 0x800) != 0) {
            this.buf.append("strictfp ");
        }
        if ((access & 0x4000) != 0) {
            this.buf.append("enum ");
        }
    }
}

