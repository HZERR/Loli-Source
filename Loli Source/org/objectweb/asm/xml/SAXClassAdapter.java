/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.xml.SAXAdapter;
import org.objectweb.asm.xml.SAXAnnotationAdapter;
import org.objectweb.asm.xml.SAXCodeAdapter;
import org.objectweb.asm.xml.SAXFieldAdapter;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public final class SAXClassAdapter
extends SAXAdapter
implements ClassVisitor {
    private final boolean singleDocument;
    private static final int ACCESS_CLASS = 262144;
    private static final int ACCESS_FIELD = 524288;
    private static final int ACCESS_INNER = 0x100000;

    public SAXClassAdapter(ContentHandler h2, boolean singleDocument) {
        super(h2);
        this.singleDocument = singleDocument;
        if (!singleDocument) {
            this.addDocumentStart();
        }
    }

    public void visitSource(String source, String debug) {
        AttributesImpl att = new AttributesImpl();
        if (source != null) {
            att.addAttribute("", "file", "file", "", SAXClassAdapter.encode(source));
        }
        if (debug != null) {
            att.addAttribute("", "debug", "debug", "", SAXClassAdapter.encode(debug));
        }
        this.addElement("source", att);
    }

    public void visitOuterClass(String owner, String name, String desc) {
        AttributesImpl att = new AttributesImpl();
        att.addAttribute("", "owner", "owner", "", owner);
        if (name != null) {
            att.addAttribute("", "name", "name", "", name);
        }
        if (desc != null) {
            att.addAttribute("", "desc", "desc", "", desc);
        }
        this.addElement("outerclass", att);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new SAXAnnotationAdapter(this.getContentHandler(), "annotation", visible ? 1 : -1, null, desc);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        StringBuffer sb = new StringBuffer();
        SAXClassAdapter.appendAccess(access | 0x40000, sb);
        AttributesImpl att = new AttributesImpl();
        att.addAttribute("", "access", "access", "", sb.toString());
        if (name != null) {
            att.addAttribute("", "name", "name", "", name);
        }
        if (signature != null) {
            att.addAttribute("", "signature", "signature", "", SAXClassAdapter.encode(signature));
        }
        if (superName != null) {
            att.addAttribute("", "parent", "parent", "", superName);
        }
        att.addAttribute("", "major", "major", "", Integer.toString(version & 0xFFFF));
        att.addAttribute("", "minor", "minor", "", Integer.toString(version >>> 16));
        this.addStart("class", att);
        this.addStart("interfaces", new AttributesImpl());
        if (interfaces != null && interfaces.length > 0) {
            for (int i2 = 0; i2 < interfaces.length; ++i2) {
                AttributesImpl att2 = new AttributesImpl();
                att2.addAttribute("", "name", "name", "", interfaces[i2]);
                this.addElement("interface", att2);
            }
        }
        this.addEnd("interfaces");
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        StringBuffer sb = new StringBuffer();
        SAXClassAdapter.appendAccess(access | 0x80000, sb);
        AttributesImpl att = new AttributesImpl();
        att.addAttribute("", "access", "access", "", sb.toString());
        att.addAttribute("", "name", "name", "", name);
        att.addAttribute("", "desc", "desc", "", desc);
        if (signature != null) {
            att.addAttribute("", "signature", "signature", "", SAXClassAdapter.encode(signature));
        }
        if (value != null) {
            att.addAttribute("", "value", "value", "", SAXClassAdapter.encode(value.toString()));
        }
        return new SAXFieldAdapter(this.getContentHandler(), att);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        StringBuffer sb = new StringBuffer();
        SAXClassAdapter.appendAccess(access, sb);
        AttributesImpl att = new AttributesImpl();
        att.addAttribute("", "access", "access", "", sb.toString());
        att.addAttribute("", "name", "name", "", name);
        att.addAttribute("", "desc", "desc", "", desc);
        if (signature != null) {
            att.addAttribute("", "signature", "signature", "", signature);
        }
        this.addStart("method", att);
        this.addStart("exceptions", new AttributesImpl());
        if (exceptions != null && exceptions.length > 0) {
            for (int i2 = 0; i2 < exceptions.length; ++i2) {
                AttributesImpl att2 = new AttributesImpl();
                att2.addAttribute("", "name", "name", "", exceptions[i2]);
                this.addElement("exception", att2);
            }
        }
        this.addEnd("exceptions");
        return new SAXCodeAdapter(this.getContentHandler(), access);
    }

    public final void visitInnerClass(String name, String outerName, String innerName, int access) {
        StringBuffer sb = new StringBuffer();
        SAXClassAdapter.appendAccess(access | 0x100000, sb);
        AttributesImpl att = new AttributesImpl();
        att.addAttribute("", "access", "access", "", sb.toString());
        if (name != null) {
            att.addAttribute("", "name", "name", "", name);
        }
        if (outerName != null) {
            att.addAttribute("", "outerName", "outerName", "", outerName);
        }
        if (innerName != null) {
            att.addAttribute("", "innerName", "innerName", "", innerName);
        }
        this.addElement("innerclass", att);
    }

    public final void visitEnd() {
        this.addEnd("class");
        if (!this.singleDocument) {
            this.addDocumentEnd();
        }
    }

    static final String encode(String s2) {
        StringBuffer sb = new StringBuffer();
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            char c2 = s2.charAt(i2);
            if (c2 == '\\') {
                sb.append("\\\\");
                continue;
            }
            if (c2 < ' ' || c2 > '\u007f') {
                sb.append("\\u");
                if (c2 < '\u0010') {
                    sb.append("000");
                } else if (c2 < '\u0100') {
                    sb.append("00");
                } else if (c2 < '\u1000') {
                    sb.append('0');
                }
                sb.append(Integer.toString(c2, 16));
                continue;
            }
            sb.append(c2);
        }
        return sb.toString();
    }

    static void appendAccess(int access, StringBuffer sb) {
        if ((access & 1) != 0) {
            sb.append("public ");
        }
        if ((access & 2) != 0) {
            sb.append("private ");
        }
        if ((access & 4) != 0) {
            sb.append("protected ");
        }
        if ((access & 0x10) != 0) {
            sb.append("final ");
        }
        if ((access & 8) != 0) {
            sb.append("static ");
        }
        if ((access & 0x20) != 0) {
            if ((access & 0x40000) == 0) {
                sb.append("synchronized ");
            } else {
                sb.append("super ");
            }
        }
        if ((access & 0x40) != 0) {
            if ((access & 0x80000) == 0) {
                sb.append("bridge ");
            } else {
                sb.append("volatile ");
            }
        }
        if ((access & 0x80) != 0) {
            if ((access & 0x80000) == 0) {
                sb.append("varargs ");
            } else {
                sb.append("transient ");
            }
        }
        if ((access & 0x100) != 0) {
            sb.append("native ");
        }
        if ((access & 0x800) != 0) {
            sb.append("strict ");
        }
        if ((access & 0x200) != 0) {
            sb.append("interface ");
        }
        if ((access & 0x400) != 0) {
            sb.append("abstract ");
        }
        if ((access & 0x1000) != 0) {
            sb.append("synthetic ");
        }
        if ((access & 0x2000) != 0) {
            sb.append("annotation ");
        }
        if ((access & 0x4000) != 0) {
            sb.append("enum ");
        }
        if ((access & 0x20000) != 0) {
            sb.append("deprecated ");
        }
    }
}

