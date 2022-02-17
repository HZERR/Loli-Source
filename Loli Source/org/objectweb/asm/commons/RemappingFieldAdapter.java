/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingAnnotationAdapter;

public class RemappingFieldAdapter
implements FieldVisitor {
    private final FieldVisitor fv;
    private final Remapper remapper;

    public RemappingFieldAdapter(FieldVisitor fv, Remapper remapper) {
        this.fv = fv;
        this.remapper = remapper;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = this.fv.visitAnnotation(this.remapper.mapDesc(desc), visible);
        return av == null ? null : new RemappingAnnotationAdapter(av, this.remapper);
    }

    public void visitAttribute(Attribute attr) {
        this.fv.visitAttribute(attr);
    }

    public void visitEnd() {
        this.fv.visitEnd();
    }
}

