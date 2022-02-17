/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MemberNode;

public class FieldNode
extends MemberNode
implements FieldVisitor {
    public int access;
    public String name;
    public String desc;
    public String signature;
    public Object value;

    public FieldNode(int access, String name, String desc, String signature, Object value) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.value = value;
    }

    public void accept(ClassVisitor cv) {
        AnnotationNode an;
        int i2;
        FieldVisitor fv = cv.visitField(this.access, this.name, this.desc, this.signature, this.value);
        if (fv == null) {
            return;
        }
        int n2 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
        for (i2 = 0; i2 < n2; ++i2) {
            an = (AnnotationNode)this.visibleAnnotations.get(i2);
            an.accept(fv.visitAnnotation(an.desc, true));
        }
        n2 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
        for (i2 = 0; i2 < n2; ++i2) {
            an = (AnnotationNode)this.invisibleAnnotations.get(i2);
            an.accept(fv.visitAnnotation(an.desc, false));
        }
        n2 = this.attrs == null ? 0 : this.attrs.size();
        for (i2 = 0; i2 < n2; ++i2) {
            fv.visitAttribute((Attribute)this.attrs.get(i2));
        }
        fv.visitEnd();
    }
}

