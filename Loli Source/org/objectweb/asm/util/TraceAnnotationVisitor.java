/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.TraceAbstractVisitor;

public class TraceAnnotationVisitor
extends TraceAbstractVisitor
implements AnnotationVisitor {
    protected AnnotationVisitor av;
    private int valueNumber = 0;

    public void visit(String name, Object value) {
        this.buf.setLength(0);
        this.appendComa(this.valueNumber++);
        if (name != null) {
            this.buf.append(name).append('=');
        }
        if (value instanceof String) {
            this.visitString((String)value);
        } else if (value instanceof Type) {
            this.visitType((Type)value);
        } else if (value instanceof Byte) {
            this.visitByte((Byte)value);
        } else if (value instanceof Boolean) {
            this.visitBoolean((Boolean)value);
        } else if (value instanceof Short) {
            this.visitShort((Short)value);
        } else if (value instanceof Character) {
            this.visitChar(((Character)value).charValue());
        } else if (value instanceof Integer) {
            this.visitInt((Integer)value);
        } else if (value instanceof Float) {
            this.visitFloat(((Float)value).floatValue());
        } else if (value instanceof Long) {
            this.visitLong((Long)value);
        } else if (value instanceof Double) {
            this.visitDouble((Double)value);
        } else if (value.getClass().isArray()) {
            this.buf.append('{');
            if (value instanceof byte[]) {
                byte[] v2 = (byte[])value;
                for (int i2 = 0; i2 < v2.length; ++i2) {
                    this.appendComa(i2);
                    this.visitByte(v2[i2]);
                }
            } else if (value instanceof boolean[]) {
                boolean[] v3 = (boolean[])value;
                for (int i3 = 0; i3 < v3.length; ++i3) {
                    this.appendComa(i3);
                    this.visitBoolean(v3[i3]);
                }
            } else if (value instanceof short[]) {
                short[] v4 = (short[])value;
                for (int i4 = 0; i4 < v4.length; ++i4) {
                    this.appendComa(i4);
                    this.visitShort(v4[i4]);
                }
            } else if (value instanceof char[]) {
                char[] v5 = (char[])value;
                for (int i5 = 0; i5 < v5.length; ++i5) {
                    this.appendComa(i5);
                    this.visitChar(v5[i5]);
                }
            } else if (value instanceof int[]) {
                int[] v6 = (int[])value;
                for (int i6 = 0; i6 < v6.length; ++i6) {
                    this.appendComa(i6);
                    this.visitInt(v6[i6]);
                }
            } else if (value instanceof long[]) {
                long[] v7 = (long[])value;
                for (int i7 = 0; i7 < v7.length; ++i7) {
                    this.appendComa(i7);
                    this.visitLong(v7[i7]);
                }
            } else if (value instanceof float[]) {
                float[] v8 = (float[])value;
                for (int i8 = 0; i8 < v8.length; ++i8) {
                    this.appendComa(i8);
                    this.visitFloat(v8[i8]);
                }
            } else if (value instanceof double[]) {
                double[] v9 = (double[])value;
                for (int i9 = 0; i9 < v9.length; ++i9) {
                    this.appendComa(i9);
                    this.visitDouble(v9[i9]);
                }
            }
            this.buf.append('}');
        }
        this.text.add(this.buf.toString());
        if (this.av != null) {
            this.av.visit(name, value);
        }
    }

    private void visitInt(int value) {
        this.buf.append(value);
    }

    private void visitLong(long value) {
        this.buf.append(value).append('L');
    }

    private void visitFloat(float value) {
        this.buf.append(value).append('F');
    }

    private void visitDouble(double value) {
        this.buf.append(value).append('D');
    }

    private void visitChar(char value) {
        this.buf.append("(char)").append((int)value);
    }

    private void visitShort(short value) {
        this.buf.append("(short)").append(value);
    }

    private void visitByte(byte value) {
        this.buf.append("(byte)").append(value);
    }

    private void visitBoolean(boolean value) {
        this.buf.append(value);
    }

    private void visitString(String value) {
        TraceAnnotationVisitor.appendString(this.buf, value);
    }

    private void visitType(Type value) {
        this.buf.append(value.getClassName()).append(".class");
    }

    public void visitEnum(String name, String desc, String value) {
        this.buf.setLength(0);
        this.appendComa(this.valueNumber++);
        if (name != null) {
            this.buf.append(name).append('=');
        }
        this.appendDescriptor(1, desc);
        this.buf.append('.').append(value);
        this.text.add(this.buf.toString());
        if (this.av != null) {
            this.av.visitEnum(name, desc, value);
        }
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        this.buf.setLength(0);
        this.appendComa(this.valueNumber++);
        if (name != null) {
            this.buf.append(name).append('=');
        }
        this.buf.append('@');
        this.appendDescriptor(1, desc);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        TraceAnnotationVisitor tav = this.createTraceAnnotationVisitor();
        this.text.add(tav.getText());
        this.text.add(")");
        if (this.av != null) {
            tav.av = this.av.visitAnnotation(name, desc);
        }
        return tav;
    }

    public AnnotationVisitor visitArray(String name) {
        this.buf.setLength(0);
        this.appendComa(this.valueNumber++);
        if (name != null) {
            this.buf.append(name).append('=');
        }
        this.buf.append('{');
        this.text.add(this.buf.toString());
        TraceAnnotationVisitor tav = this.createTraceAnnotationVisitor();
        this.text.add(tav.getText());
        this.text.add("}");
        if (this.av != null) {
            tav.av = this.av.visitArray(name);
        }
        return tav;
    }

    public void visitEnd() {
        if (this.av != null) {
            this.av.visitEnd();
        }
    }

    private void appendComa(int i2) {
        if (i2 != 0) {
            this.buf.append(", ");
        }
    }
}

