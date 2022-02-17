/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class StaticInitMerger
extends ClassAdapter {
    private String name;
    private MethodVisitor clinit;
    private final String prefix;
    private int counter;

    public StaticInitMerger(String prefix, ClassVisitor cv) {
        super(cv);
        this.prefix = prefix;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.cv.visit(version, access, name, signature, superName, interfaces);
        this.name = name;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv;
        if ("<clinit>".equals(name)) {
            int a2 = 10;
            String n2 = this.prefix + this.counter++;
            mv = this.cv.visitMethod(a2, n2, desc, signature, exceptions);
            if (this.clinit == null) {
                this.clinit = this.cv.visitMethod(a2, name, desc, null, null);
            }
            this.clinit.visitMethodInsn(184, this.name, n2, desc);
        } else {
            mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
        return mv;
    }

    public void visitEnd() {
        if (this.clinit != null) {
            this.clinit.visitInsn(177);
            this.clinit.visitMaxs(0, 0);
        }
        this.cv.visitEnd();
    }
}

