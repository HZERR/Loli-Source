/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.xml.SAXAdapter;
import org.objectweb.asm.xml.SAXAnnotationAdapter;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

public class SAXFieldAdapter
extends SAXAdapter
implements FieldVisitor {
    public SAXFieldAdapter(ContentHandler h2, Attributes att) {
        super(h2);
        this.addStart("field", att);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new SAXAnnotationAdapter(this.getContentHandler(), "annotation", visible ? 1 : -1, null, desc);
    }

    public void visitEnd() {
        this.addEnd("field");
    }
}

