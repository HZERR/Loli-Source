/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.util.CheckAnnotationAdapter;
import org.objectweb.asm.util.CheckFieldAdapter;
import org.objectweb.asm.util.CheckMethodAdapter;
import org.objectweb.asm.util.TraceMethodVisitor;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CheckClassAdapter
extends ClassAdapter {
    private int version;
    private boolean start;
    private boolean source;
    private boolean outer;
    private boolean end;
    private Map<Label, Integer> labels = new HashMap<Label, Integer>();
    private boolean checkDataFlow;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Verifies the given class.");
            System.err.println("Usage: CheckClassAdapter <fully qualified class name or class file name>");
            return;
        }
        ClassReader cr = args[0].endsWith(".class") ? new ClassReader(new FileInputStream(args[0])) : new ClassReader(args[0]);
        CheckClassAdapter.verify(cr, false, new PrintWriter(System.err));
    }

    public static void verify(ClassReader cr, ClassLoader loader, boolean dump, PrintWriter pw) {
        ClassNode cn = new ClassNode();
        cr.accept(new CheckClassAdapter(cn, false), 2);
        Type syperType = cn.superName == null ? null : Type.getObjectType(cn.superName);
        List<MethodNode> methods = cn.methods;
        ArrayList<Type> interfaces = new ArrayList<Type>();
        Iterator<String> i2 = cn.interfaces.iterator();
        while (i2.hasNext()) {
            interfaces.add(Type.getObjectType(i2.next().toString()));
        }
        for (int i3 = 0; i3 < methods.size(); ++i3) {
            MethodNode method = methods.get(i3);
            SimpleVerifier verifier = new SimpleVerifier(Type.getObjectType(cn.name), syperType, interfaces, (cn.access | 0x200) != 0);
            Analyzer<BasicValue> a2 = new Analyzer<BasicValue>(verifier);
            if (loader != null) {
                verifier.setClassLoader(loader);
            }
            try {
                a2.analyze(cn.name, method);
                if (!dump) {
                    continue;
                }
            }
            catch (Exception e2) {
                e2.printStackTrace(pw);
            }
            CheckClassAdapter.printAnalyzerResult(method, a2, pw);
        }
        pw.flush();
    }

    public static void verify(ClassReader cr, boolean dump, PrintWriter pw) {
        CheckClassAdapter.verify(cr, null, dump, pw);
    }

    static void printAnalyzerResult(MethodNode method, Analyzer<BasicValue> a2, PrintWriter pw) {
        int j2;
        Frame<BasicValue>[] frames = a2.getFrames();
        TraceMethodVisitor mv = new TraceMethodVisitor();
        pw.println(method.name + method.desc);
        for (j2 = 0; j2 < method.instructions.size(); ++j2) {
            method.instructions.get(j2).accept(mv);
            StringBuffer s2 = new StringBuffer();
            Frame<BasicValue> f2 = frames[j2];
            if (f2 == null) {
                s2.append('?');
            } else {
                int k2;
                for (k2 = 0; k2 < f2.getLocals(); ++k2) {
                    s2.append(CheckClassAdapter.getShortName(f2.getLocal(k2).toString())).append(' ');
                }
                s2.append(" : ");
                for (k2 = 0; k2 < f2.getStackSize(); ++k2) {
                    s2.append(CheckClassAdapter.getShortName(f2.getStack(k2).toString())).append(' ');
                }
            }
            while (s2.length() < method.maxStack + method.maxLocals + 1) {
                s2.append(' ');
            }
            pw.print(Integer.toString(j2 + 100000).substring(1));
            pw.print(" " + s2 + " : " + mv.buf);
        }
        for (j2 = 0; j2 < method.tryCatchBlocks.size(); ++j2) {
            method.tryCatchBlocks.get(j2).accept(mv);
            pw.print(" " + mv.buf);
        }
        pw.println();
    }

    private static String getShortName(String name) {
        int n2 = name.lastIndexOf(47);
        int k2 = name.length();
        if (name.charAt(k2 - 1) == ';') {
            --k2;
        }
        return n2 == -1 ? name : name.substring(n2 + 1, k2);
    }

    public CheckClassAdapter(ClassVisitor cv) {
        this(cv, true);
    }

    public CheckClassAdapter(ClassVisitor cv, boolean checkDataFlow) {
        super(cv);
        this.checkDataFlow = checkDataFlow;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (this.start) {
            throw new IllegalStateException("visit must be called only once");
        }
        this.start = true;
        this.checkState();
        CheckClassAdapter.checkAccess(access, 423473);
        if (name == null || !name.endsWith("package-info")) {
            CheckMethodAdapter.checkInternalName(name, "class name");
        }
        if ("java/lang/Object".equals(name)) {
            if (superName != null) {
                throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
            }
        } else {
            CheckMethodAdapter.checkInternalName(superName, "super class name");
        }
        if (signature != null) {
            CheckMethodAdapter.checkClassSignature(signature);
        }
        if ((access & 0x200) != 0 && !"java/lang/Object".equals(superName)) {
            throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
        }
        if (interfaces != null) {
            for (int i2 = 0; i2 < interfaces.length; ++i2) {
                CheckMethodAdapter.checkInternalName(interfaces[i2], "interface name at index " + i2);
            }
        }
        this.version = version;
        this.cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(String file, String debug) {
        this.checkState();
        if (this.source) {
            throw new IllegalStateException("visitSource can be called only once.");
        }
        this.source = true;
        this.cv.visitSource(file, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        this.checkState();
        if (this.outer) {
            throw new IllegalStateException("visitOuterClass can be called only once.");
        }
        this.outer = true;
        if (owner == null) {
            throw new IllegalArgumentException("Illegal outer class owner");
        }
        if (desc != null) {
            CheckMethodAdapter.checkMethodDesc(desc);
        }
        this.cv.visitOuterClass(owner, name, desc);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.checkState();
        CheckMethodAdapter.checkInternalName(name, "class name");
        if (outerName != null) {
            CheckMethodAdapter.checkInternalName(outerName, "outer class name");
        }
        if (innerName != null) {
            CheckMethodAdapter.checkIdentifier(innerName, "inner class name");
        }
        CheckClassAdapter.checkAccess(access, 30239);
        this.cv.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        this.checkState();
        CheckClassAdapter.checkAccess(access, 413919);
        CheckMethodAdapter.checkUnqualifiedName(this.version, name, "field name");
        CheckMethodAdapter.checkDesc(desc, false);
        if (signature != null) {
            CheckMethodAdapter.checkFieldSignature(signature);
        }
        if (value != null) {
            CheckMethodAdapter.checkConstant(value);
        }
        FieldVisitor av = this.cv.visitField(access, name, desc, signature, value);
        return new CheckFieldAdapter(av);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.checkState();
        CheckClassAdapter.checkAccess(access, 400895);
        CheckMethodAdapter.checkMethodIdentifier(this.version, name, "method name");
        CheckMethodAdapter.checkMethodDesc(desc);
        if (signature != null) {
            CheckMethodAdapter.checkMethodSignature(signature);
        }
        if (exceptions != null) {
            for (int i2 = 0; i2 < exceptions.length; ++i2) {
                CheckMethodAdapter.checkInternalName(exceptions[i2], "exception name at index " + i2);
            }
        }
        CheckMethodAdapter cma = this.checkDataFlow ? new CheckMethodAdapter(access, name, desc, this.cv.visitMethod(access, name, desc, signature, exceptions), this.labels) : new CheckMethodAdapter(this.cv.visitMethod(access, name, desc, signature, exceptions), this.labels);
        cma.version = this.version;
        return cma;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.checkState();
        CheckMethodAdapter.checkDesc(desc, false);
        return new CheckAnnotationAdapter(this.cv.visitAnnotation(desc, visible));
    }

    @Override
    public void visitAttribute(Attribute attr) {
        this.checkState();
        if (attr == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        this.cv.visitAttribute(attr);
    }

    @Override
    public void visitEnd() {
        this.checkState();
        this.end = true;
        this.cv.visitEnd();
    }

    private void checkState() {
        if (!this.start) {
            throw new IllegalStateException("Cannot visit member before visit has been called.");
        }
        if (this.end) {
            throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
        }
    }

    static void checkAccess(int access, int possibleAccess) {
        int abs;
        int pro;
        if ((access & ~possibleAccess) != 0) {
            throw new IllegalArgumentException("Invalid access flags: " + access);
        }
        int pub = (access & 1) == 0 ? 0 : 1;
        int pri = (access & 2) == 0 ? 0 : 1;
        int n2 = pro = (access & 4) == 0 ? 0 : 1;
        if (pub + pri + pro > 1) {
            throw new IllegalArgumentException("public private and protected are mutually exclusive: " + access);
        }
        int fin = (access & 0x10) == 0 ? 0 : 1;
        int n3 = abs = (access & 0x400) == 0 ? 0 : 1;
        if (fin + abs > 1) {
            throw new IllegalArgumentException("final and abstract are mutually exclusive: " + access);
        }
    }
}

