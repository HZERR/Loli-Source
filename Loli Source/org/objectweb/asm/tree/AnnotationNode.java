/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class AnnotationNode
implements AnnotationVisitor {
    public String desc;
    public List<Object> values;

    public AnnotationNode(String desc) {
        this.desc = desc;
    }

    AnnotationNode(List<Object> values) {
        this.values = values;
    }

    @Override
    public void visit(String name, Object value) {
        if (this.values == null) {
            this.values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(name);
        }
        this.values.add(value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        if (this.values == null) {
            this.values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(name);
        }
        this.values.add(new String[]{desc, value});
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        if (this.values == null) {
            this.values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(name);
        }
        AnnotationNode annotation = new AnnotationNode(desc);
        this.values.add(annotation);
        return annotation;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        if (this.values == null) {
            this.values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(name);
        }
        ArrayList<Object> array = new ArrayList<Object>();
        this.values.add(array);
        return new AnnotationNode(array);
    }

    @Override
    public void visitEnd() {
    }

    public void accept(AnnotationVisitor av) {
        if (av != null) {
            if (this.values != null) {
                for (int i2 = 0; i2 < this.values.size(); i2 += 2) {
                    String name = (String)this.values.get(i2);
                    Object value = this.values.get(i2 + 1);
                    AnnotationNode.accept(av, name, value);
                }
            }
            av.visitEnd();
        }
    }

    static void accept(AnnotationVisitor av, String name, Object value) {
        if (av != null) {
            if (value instanceof String[]) {
                String[] typeconst = (String[])value;
                av.visitEnum(name, typeconst[0], typeconst[1]);
            } else if (value instanceof AnnotationNode) {
                AnnotationNode an = (AnnotationNode)value;
                an.accept(av.visitAnnotation(name, an.desc));
            } else if (value instanceof List) {
                AnnotationVisitor v2 = av.visitArray(name);
                List array = (List)value;
                for (int j2 = 0; j2 < array.size(); ++j2) {
                    AnnotationNode.accept(v2, null, array.get(j2));
                }
                v2.visitEnd();
            } else {
                av.visit(name, value);
            }
        }
    }
}

