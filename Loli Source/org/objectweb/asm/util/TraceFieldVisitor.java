/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.util.TraceAbstractVisitor;
import org.objectweb.asm.util.TraceAnnotationVisitor;

public class TraceFieldVisitor
extends TraceAbstractVisitor
implements FieldVisitor {
    protected FieldVisitor fv;

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        if (this.fv != null) {
            ((TraceAnnotationVisitor)av).av = this.fv.visitAnnotation(desc, visible);
        }
        return av;
    }

    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
        if (this.fv != null) {
            this.fv.visitAttribute(attr);
        }
    }

    public void visitEnd() {
        super.visitEnd();
        if (this.fv != null) {
            this.fv.visitEnd();
        }
    }
}

