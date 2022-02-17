/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.xml;

import org.objectweb.asm.Attribute;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class SAXAdapter {
    private final ContentHandler h;

    protected SAXAdapter(ContentHandler h2) {
        this.h = h2;
    }

    protected ContentHandler getContentHandler() {
        return this.h;
    }

    protected void addDocumentStart() {
        try {
            this.h.startDocument();
        }
        catch (SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }

    protected void addDocumentEnd() {
        try {
            this.h.endDocument();
        }
        catch (SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }

    protected final void addStart(String name, Attributes attrs) {
        try {
            this.h.startElement("", name, name, attrs);
        }
        catch (SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }

    protected final void addEnd(String name) {
        try {
            this.h.endElement("", name, name);
        }
        catch (SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }

    protected final void addElement(String name, Attributes attrs) {
        this.addStart(name, attrs);
        this.addEnd(name);
    }

    public void visitAttribute(Attribute attr) {
    }
}

