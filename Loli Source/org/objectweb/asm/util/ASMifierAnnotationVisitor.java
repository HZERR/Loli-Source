/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.util.ASMifierAbstractVisitor;
import org.objectweb.asm.util.AbstractVisitor;

public class ASMifierAnnotationVisitor
extends AbstractVisitor
implements AnnotationVisitor {
    protected final int id;

    public ASMifierAnnotationVisitor(int id) {
        this.id = id;
    }

    public void visit(String name, Object value) {
        this.buf.setLength(0);
        this.buf.append("av").append(this.id).append(".visit(");
        ASMifierAbstractVisitor.appendConstant(this.buf, name);
        this.buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(this.buf, value);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public void visitEnum(String name, String desc, String value) {
        this.buf.setLength(0);
        this.buf.append("av").append(this.id).append(".visitEnum(");
        ASMifierAbstractVisitor.appendConstant(this.buf, name);
        this.buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(this.buf, desc);
        this.buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(this.buf, value);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
        this.buf.append(this.id).append(".visitAnnotation(");
        ASMifierAbstractVisitor.appendConstant(this.buf, name);
        this.buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(this.buf, desc);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(this.id + 1);
        this.text.add(av.getText());
        this.text.add("}\n");
        return av;
    }

    public AnnotationVisitor visitArray(String name) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
        this.buf.append(this.id).append(".visitArray(");
        ASMifierAbstractVisitor.appendConstant(this.buf, name);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(this.id + 1);
        this.text.add(av.getText());
        this.text.add("}\n");
        return av;
    }

    public void visitEnd() {
        this.buf.setLength(0);
        this.buf.append("av").append(this.id).append(".visitEnd();\n");
        this.text.add(this.buf.toString());
    }
}

