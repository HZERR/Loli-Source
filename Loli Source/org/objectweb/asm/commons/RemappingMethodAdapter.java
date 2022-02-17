/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingAnnotationAdapter;

public class RemappingMethodAdapter
extends LocalVariablesSorter {
    protected final Remapper remapper;

    public RemappingMethodAdapter(int access, String desc, MethodVisitor mv, Remapper renamer) {
        super(access, desc, mv);
        this.remapper = renamer;
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, this.remapper.mapType(owner), this.remapper.mapFieldName(owner, name, desc), this.remapper.mapDesc(desc));
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        super.visitMethodInsn(opcode, this.remapper.mapType(owner), this.remapper.mapMethodName(owner, name, desc), this.remapper.mapMethodDesc(desc));
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        for (int i2 = 0; i2 < bsmArgs.length; ++i2) {
            bsmArgs[i2] = this.remapper.mapValue(bsmArgs[i2]);
        }
        super.visitInvokeDynamicInsn(this.remapper.mapInvokeDynamicMethodName(name, desc), this.remapper.mapMethodDesc(desc), (MethodHandle)this.remapper.mapValue(bsm), bsmArgs);
    }

    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, this.remapper.mapType(type));
    }

    public void visitLdcInsn(Object cst) {
        super.visitLdcInsn(this.remapper.mapValue(cst));
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        super.visitMultiANewArrayInsn(this.remapper.mapDesc(desc), dims);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type == null ? null : this.remapper.mapType(type));
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, this.remapper.mapDesc(desc), this.remapper.mapSignature(signature, true), start, end, index);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = this.mv.visitAnnotation(this.remapper.mapDesc(desc), visible);
        return av == null ? av : new RemappingAnnotationAdapter(av, this.remapper);
    }

    public AnnotationVisitor visitAnnotationDefault() {
        AnnotationVisitor av = this.mv.visitAnnotationDefault();
        return av == null ? av : new RemappingAnnotationAdapter(av, this.remapper);
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        AnnotationVisitor av = this.mv.visitParameterAnnotation(parameter, this.remapper.mapDesc(desc), visible);
        return av == null ? av : new RemappingAnnotationAdapter(av, this.remapper);
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super.visitFrame(type, nLocal, this.remapEntries(nLocal, local), nStack, this.remapEntries(nStack, stack));
    }

    private Object[] remapEntries(int n2, Object[] entries) {
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!(entries[i2] instanceof String)) continue;
            Object[] newEntries = new Object[n2];
            if (i2 > 0) {
                System.arraycopy(entries, 0, newEntries, 0, i2);
            }
            do {
                Object t2 = entries[i2];
                Object object = newEntries[i2++] = t2 instanceof String ? this.remapper.mapType((String)t2) : t2;
            } while (i2 < n2);
            return newEntries;
        }
        return entries;
    }
}

