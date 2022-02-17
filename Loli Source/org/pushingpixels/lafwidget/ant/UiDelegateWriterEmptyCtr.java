/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class UiDelegateWriterEmptyCtr
extends ClassWriter
implements Opcodes {
    public UiDelegateWriterEmptyCtr() {
        super(false);
    }

    public static byte[] createClass(String packageName, String className, String superClassName) {
        packageName = packageName.replace('.', '/');
        superClassName = superClassName.replace('.', '/');
        UiDelegateWriterEmptyCtr cw = new UiDelegateWriterEmptyCtr();
        cw.visit(46, 33, packageName + "/" + className, null, superClassName, null);
        cw.visitSource(className + ".java", null);
        MethodVisitor mv = cw.visitMethod(9, "createUI", "(Ljavax/swing/JComponent;)Ljavax/swing/plaf/ComponentUI;", null, null);
        mv.visitCode();
        mv.visitTypeInsn(187, packageName + "/" + className);
        mv.visitInsn(89);
        mv.visitMethodInsn(183, packageName + "/" + className, "<init>", "()V");
        mv.visitInsn(176);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
        mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, superClassName, "<init>", "()V");
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }
}

