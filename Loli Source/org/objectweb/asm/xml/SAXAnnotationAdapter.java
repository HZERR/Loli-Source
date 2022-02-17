/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.xml.SAXAdapter;
import org.objectweb.asm.xml.SAXClassAdapter;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public class SAXAnnotationAdapter
extends SAXAdapter
implements AnnotationVisitor {
    private final String elementName;

    public SAXAnnotationAdapter(ContentHandler h2, String elementName, int visible, String name, String desc) {
        this(h2, elementName, visible, desc, name, -1);
    }

    public SAXAnnotationAdapter(ContentHandler h2, String elementName, int visible, int parameter, String desc) {
        this(h2, elementName, visible, desc, null, parameter);
    }

    private SAXAnnotationAdapter(ContentHandler h2, String elementName, int visible, String desc, String name, int parameter) {
        super(h2);
        this.elementName = elementName;
        AttributesImpl att = new AttributesImpl();
        if (name != null) {
            att.addAttribute("", "name", "name", "", name);
        }
        if (visible != 0) {
            att.addAttribute("", "visible", "visible", "", visible > 0 ? "true" : "false");
        }
        if (parameter != -1) {
            att.addAttribute("", "parameter", "parameter", "", Integer.toString(parameter));
        }
        if (desc != null) {
            att.addAttribute("", "desc", "desc", "", desc);
        }
        this.addStart(elementName, att);
    }

    public void visit(String name, Object value) {
        Class<?> c2 = value.getClass();
        if (c2.isArray()) {
            AnnotationVisitor av = this.visitArray(name);
            if (value instanceof byte[]) {
                byte[] b2 = (byte[])value;
                for (int i2 = 0; i2 < b2.length; ++i2) {
                    av.visit(null, new Byte(b2[i2]));
                }
            } else if (value instanceof char[]) {
                char[] b3 = (char[])value;
                for (int i3 = 0; i3 < b3.length; ++i3) {
                    av.visit(null, new Character(b3[i3]));
                }
            } else if (value instanceof short[]) {
                short[] b4 = (short[])value;
                for (int i4 = 0; i4 < b4.length; ++i4) {
                    av.visit(null, new Short(b4[i4]));
                }
            } else if (value instanceof boolean[]) {
                boolean[] b5 = (boolean[])value;
                for (int i5 = 0; i5 < b5.length; ++i5) {
                    av.visit(null, b5[i5]);
                }
            } else if (value instanceof int[]) {
                int[] b6 = (int[])value;
                for (int i6 = 0; i6 < b6.length; ++i6) {
                    av.visit(null, new Integer(b6[i6]));
                }
            } else if (value instanceof long[]) {
                long[] b7 = (long[])value;
                for (int i7 = 0; i7 < b7.length; ++i7) {
                    av.visit(null, new Long(b7[i7]));
                }
            } else if (value instanceof float[]) {
                float[] b8 = (float[])value;
                for (int i8 = 0; i8 < b8.length; ++i8) {
                    av.visit(null, new Float(b8[i8]));
                }
            } else if (value instanceof double[]) {
                double[] b9 = (double[])value;
                for (int i9 = 0; i9 < b9.length; ++i9) {
                    av.visit(null, new Double(b9[i9]));
                }
            }
            av.visitEnd();
        } else {
            this.addValueElement("annotationValue", name, Type.getDescriptor(c2), value.toString());
        }
    }

    public void visitEnum(String name, String desc, String value) {
        this.addValueElement("annotationValueEnum", name, desc, value);
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return new SAXAnnotationAdapter(this.getContentHandler(), "annotationValueAnnotation", 0, name, desc);
    }

    public AnnotationVisitor visitArray(String name) {
        return new SAXAnnotationAdapter(this.getContentHandler(), "annotationValueArray", 0, name, null);
    }

    public void visitEnd() {
        this.addEnd(this.elementName);
    }

    private void addValueElement(String element, String name, String desc, String value) {
        AttributesImpl att = new AttributesImpl();
        if (name != null) {
            att.addAttribute("", "name", "name", "", name);
        }
        if (desc != null) {
            att.addAttribute("", "desc", "desc", "", desc);
        }
        if (value != null) {
            att.addAttribute("", "value", "value", "", SAXClassAdapter.encode(value));
        }
        this.addElement(element, att);
    }
}

