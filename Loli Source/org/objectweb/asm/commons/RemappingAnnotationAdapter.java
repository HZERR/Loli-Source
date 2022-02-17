/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.commons.Remapper;

public class RemappingAnnotationAdapter
implements AnnotationVisitor {
    private final AnnotationVisitor av;
    private final Remapper renamer;

    public RemappingAnnotationAdapter(AnnotationVisitor av, Remapper renamer) {
        this.av = av;
        this.renamer = renamer;
    }

    public void visit(String name, Object value) {
        this.av.visit(name, this.renamer.mapValue(value));
    }

    public void visitEnum(String name, String desc, String value) {
        this.av.visitEnum(name, this.renamer.mapDesc(desc), value);
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationVisitor v2 = this.av.visitAnnotation(name, this.renamer.mapDesc(desc));
        return v2 == null ? null : (v2 == this.av ? this : new RemappingAnnotationAdapter(v2, this.renamer));
    }

    public AnnotationVisitor visitArray(String name) {
        AnnotationVisitor v2 = this.av.visitArray(name);
        return v2 == null ? null : (v2 == this.av ? this : new RemappingAnnotationAdapter(v2, this.renamer));
    }

    public void visitEnd() {
        this.av.visitEnd();
    }
}

