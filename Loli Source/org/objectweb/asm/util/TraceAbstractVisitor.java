/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.util.AbstractVisitor;
import org.objectweb.asm.util.TraceAnnotationVisitor;
import org.objectweb.asm.util.Traceable;

public abstract class TraceAbstractVisitor
extends AbstractVisitor {
    public static final int INTERNAL_NAME = 0;
    public static final int FIELD_DESCRIPTOR = 1;
    public static final int FIELD_SIGNATURE = 2;
    public static final int METHOD_DESCRIPTOR = 3;
    public static final int METHOD_SIGNATURE = 4;
    public static final int CLASS_SIGNATURE = 5;
    public static final int TYPE_DECLARATION = 6;
    public static final int CLASS_DECLARATION = 7;
    public static final int PARAMETERS_DECLARATION = 8;
    protected String tab = "  ";

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append('@');
        this.appendDescriptor(1, desc);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        TraceAnnotationVisitor tav = this.createTraceAnnotationVisitor();
        this.text.add(tav.getText());
        this.text.add(visible ? ")\n" : ") // invisible\n");
        return tav;
    }

    public void visitAttribute(Attribute attr) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("ATTRIBUTE ");
        this.appendDescriptor(-1, attr.type);
        if (attr instanceof Traceable) {
            ((Traceable)((Object)attr)).trace(this.buf, null);
        } else {
            this.buf.append(" : unknown\n");
        }
        this.text.add(this.buf.toString());
    }

    public void visitEnd() {
    }

    protected TraceAnnotationVisitor createTraceAnnotationVisitor() {
        return new TraceAnnotationVisitor();
    }

    protected void appendDescriptor(int type, String desc) {
        if (type == 5 || type == 2 || type == 4) {
            if (desc != null) {
                this.buf.append("// signature ").append(desc).append('\n');
            }
        } else {
            this.buf.append(desc);
        }
    }
}

