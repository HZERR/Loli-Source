/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.util.CheckAnnotationAdapter;
import org.objectweb.asm.util.CheckMethodAdapter;

public class CheckFieldAdapter
implements FieldVisitor {
    private final FieldVisitor fv;
    private boolean end;

    public CheckFieldAdapter(FieldVisitor fv) {
        this.fv = fv;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.checkEnd();
        CheckMethodAdapter.checkDesc(desc, false);
        return new CheckAnnotationAdapter(this.fv.visitAnnotation(desc, visible));
    }

    public void visitAttribute(Attribute attr) {
        this.checkEnd();
        if (attr == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        this.fv.visitAttribute(attr);
    }

    public void visitEnd() {
        this.checkEnd();
        this.end = true;
        this.fv.visitEnd();
    }

    private void checkEnd() {
        if (this.end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
}

