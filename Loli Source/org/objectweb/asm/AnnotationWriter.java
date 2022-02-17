/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Item;
import org.objectweb.asm.Type;

final class AnnotationWriter
implements AnnotationVisitor {
    private final ClassWriter cw;
    private int size;
    private final boolean named;
    private final ByteVector bv;
    private final ByteVector parent;
    private final int offset;
    AnnotationWriter next;
    AnnotationWriter prev;

    AnnotationWriter(ClassWriter cw, boolean named, ByteVector bv, ByteVector parent, int offset) {
        this.cw = cw;
        this.named = named;
        this.bv = bv;
        this.parent = parent;
        this.offset = offset;
    }

    public void visit(String name, Object value) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        if (value instanceof String) {
            this.bv.put12(115, this.cw.newUTF8((String)value));
        } else if (value instanceof Byte) {
            this.bv.put12(66, this.cw.newInteger((int)((Byte)value).byteValue()).index);
        } else if (value instanceof Boolean) {
            int v2 = (Boolean)value != false ? 1 : 0;
            this.bv.put12(90, this.cw.newInteger((int)v2).index);
        } else if (value instanceof Character) {
            this.bv.put12(67, this.cw.newInteger((int)((Character)value).charValue()).index);
        } else if (value instanceof Short) {
            this.bv.put12(83, this.cw.newInteger((int)((Short)value).shortValue()).index);
        } else if (value instanceof Type) {
            this.bv.put12(99, this.cw.newUTF8(((Type)value).getDescriptor()));
        } else if (value instanceof byte[]) {
            byte[] v3 = (byte[])value;
            this.bv.put12(91, v3.length);
            for (int i2 = 0; i2 < v3.length; ++i2) {
                this.bv.put12(66, this.cw.newInteger((int)v3[i2]).index);
            }
        } else if (value instanceof boolean[]) {
            boolean[] v4 = (boolean[])value;
            this.bv.put12(91, v4.length);
            for (int i3 = 0; i3 < v4.length; ++i3) {
                this.bv.put12(90, this.cw.newInteger((int)(v4[i3] ? 1 : 0)).index);
            }
        } else if (value instanceof short[]) {
            short[] v5 = (short[])value;
            this.bv.put12(91, v5.length);
            for (int i4 = 0; i4 < v5.length; ++i4) {
                this.bv.put12(83, this.cw.newInteger((int)v5[i4]).index);
            }
        } else if (value instanceof char[]) {
            char[] v6 = (char[])value;
            this.bv.put12(91, v6.length);
            for (int i5 = 0; i5 < v6.length; ++i5) {
                this.bv.put12(67, this.cw.newInteger((int)v6[i5]).index);
            }
        } else if (value instanceof int[]) {
            int[] v7 = (int[])value;
            this.bv.put12(91, v7.length);
            for (int i6 = 0; i6 < v7.length; ++i6) {
                this.bv.put12(73, this.cw.newInteger((int)v7[i6]).index);
            }
        } else if (value instanceof long[]) {
            long[] v8 = (long[])value;
            this.bv.put12(91, v8.length);
            for (int i7 = 0; i7 < v8.length; ++i7) {
                this.bv.put12(74, this.cw.newLong((long)v8[i7]).index);
            }
        } else if (value instanceof float[]) {
            float[] v9 = (float[])value;
            this.bv.put12(91, v9.length);
            for (int i8 = 0; i8 < v9.length; ++i8) {
                this.bv.put12(70, this.cw.newFloat((float)v9[i8]).index);
            }
        } else if (value instanceof double[]) {
            double[] v10 = (double[])value;
            this.bv.put12(91, v10.length);
            for (int i9 = 0; i9 < v10.length; ++i9) {
                this.bv.put12(68, this.cw.newDouble((double)v10[i9]).index);
            }
        } else {
            Item i10 = this.cw.newConstItem(value);
            this.bv.put12(".s.IFJDCS".charAt(i10.type), i10.index);
        }
    }

    public void visitEnum(String name, String desc, String value) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        this.bv.put12(101, this.cw.newUTF8(desc)).putShort(this.cw.newUTF8(value));
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        this.bv.put12(64, this.cw.newUTF8(desc)).putShort(0);
        return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
    }

    public AnnotationVisitor visitArray(String name) {
        ++this.size;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        this.bv.put12(91, 0);
        return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
    }

    public void visitEnd() {
        if (this.parent != null) {
            byte[] data = this.parent.data;
            data[this.offset] = (byte)(this.size >>> 8);
            data[this.offset + 1] = (byte)this.size;
        }
    }

    int getSize() {
        int size = 0;
        AnnotationWriter aw = this;
        while (aw != null) {
            size += aw.bv.length;
            aw = aw.next;
        }
        return size;
    }

    void put(ByteVector out) {
        int n2 = 0;
        int size = 2;
        AnnotationWriter aw = this;
        AnnotationWriter last = null;
        while (aw != null) {
            ++n2;
            size += aw.bv.length;
            aw.visitEnd();
            aw.prev = last;
            last = aw;
            aw = aw.next;
        }
        out.putInt(size);
        out.putShort(n2);
        aw = last;
        while (aw != null) {
            out.putByteArray(aw.bv.data, 0, aw.bv.length);
            aw = aw.prev;
        }
    }

    static void put(AnnotationWriter[] panns, int off, ByteVector out) {
        int i2;
        int size = 1 + 2 * (panns.length - off);
        for (i2 = off; i2 < panns.length; ++i2) {
            size += panns[i2] == null ? 0 : panns[i2].getSize();
        }
        out.putInt(size).putByte(panns.length - off);
        for (i2 = off; i2 < panns.length; ++i2) {
            AnnotationWriter aw = panns[i2];
            AnnotationWriter last = null;
            int n2 = 0;
            while (aw != null) {
                ++n2;
                aw.visitEnd();
                aw.prev = last;
                last = aw;
                aw = aw.next;
            }
            out.putShort(n2);
            aw = last;
            while (aw != null) {
                out.putByteArray(aw.bv.data, 0, aw.bv.length);
                aw = aw.prev;
            }
        }
    }
}

