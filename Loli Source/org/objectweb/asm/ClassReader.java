/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Item;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodType;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.MethodWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassReader {
    static final boolean SIGNATURES = true;
    static final boolean ANNOTATIONS = true;
    static final boolean FRAMES = true;
    static final boolean WRITER = true;
    static final boolean RESIZE = true;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;
    public final byte[] b;
    private final int[] items;
    private final String[] strings;
    private final int maxStringLength;
    public final int header;

    public ClassReader(byte[] b2) {
        this(b2, 0, b2.length);
    }

    public ClassReader(byte[] b2, int off, int len) {
        this.b = b2;
        this.items = new int[this.readUnsignedShort(off + 8)];
        int n2 = this.items.length;
        this.strings = new String[n2];
        int max = 0;
        int index = off + 10;
        for (int i2 = 1; i2 < n2; ++i2) {
            int size;
            this.items[i2] = index + 1;
            switch (b2[index]) {
                case 3: 
                case 4: 
                case 9: 
                case 10: 
                case 11: 
                case 12: 
                case 18: {
                    size = 5;
                    break;
                }
                case 5: 
                case 6: {
                    size = 9;
                    ++i2;
                    break;
                }
                case 1: {
                    size = 3 + this.readUnsignedShort(index + 1);
                    if (size <= max) break;
                    max = size;
                    break;
                }
                case 15: {
                    size = 4;
                    break;
                }
                default: {
                    size = 3;
                }
            }
            index += size;
        }
        this.maxStringLength = max;
        this.header = index;
    }

    public int getAccess() {
        return this.readUnsignedShort(this.header);
    }

    public String getClassName() {
        return this.readClass(this.header + 2, new char[this.maxStringLength]);
    }

    public String getSuperName() {
        int n2 = this.items[this.readUnsignedShort(this.header + 4)];
        return n2 == 0 ? null : this.readUTF8(n2, new char[this.maxStringLength]);
    }

    public String[] getInterfaces() {
        int index = this.header + 6;
        int n2 = this.readUnsignedShort(index);
        String[] interfaces = new String[n2];
        if (n2 > 0) {
            char[] buf = new char[this.maxStringLength];
            for (int i2 = 0; i2 < n2; ++i2) {
                interfaces[i2] = this.readClass(index += 2, buf);
            }
        }
        return interfaces;
    }

    void copyPool(ClassWriter classWriter) {
        char[] buf = new char[this.maxStringLength];
        int ll = this.items.length;
        Item[] items2 = new Item[ll];
        for (int i2 = 1; i2 < ll; ++i2) {
            int index = this.items[i2];
            byte tag = this.b[index - 1];
            Item item = new Item(i2);
            switch (tag) {
                case 9: 
                case 10: 
                case 11: {
                    int nameType = this.items[this.readUnsignedShort(index + 2)];
                    item.set(tag, this.readClass(index, buf), this.readUTF8(nameType, buf), this.readUTF8(nameType + 2, buf));
                    break;
                }
                case 3: {
                    item.set(this.readInt(index));
                    break;
                }
                case 4: {
                    item.set(Float.intBitsToFloat(this.readInt(index)));
                    break;
                }
                case 12: {
                    item.set(tag, this.readUTF8(index, buf), this.readUTF8(index + 2, buf), null);
                    break;
                }
                case 5: {
                    item.set(this.readLong(index));
                    ++i2;
                    break;
                }
                case 6: {
                    item.set(Double.longBitsToDouble(this.readLong(index)));
                    ++i2;
                    break;
                }
                case 1: {
                    String s2 = this.strings[i2];
                    if (s2 == null) {
                        index = this.items[i2];
                        s2 = this.strings[i2] = this.readUTF(index + 2, this.readUnsignedShort(index), buf);
                    }
                    item.set(tag, s2, null, null);
                    break;
                }
                case 15: {
                    int fieldOrMethodRef = this.items[this.readUnsignedShort(index + 1)];
                    int nameType = this.items[this.readUnsignedShort(fieldOrMethodRef + 2)];
                    item.set(20 + this.readByte(index), this.readClass(fieldOrMethodRef, buf), this.readUTF8(nameType, buf), this.readUTF8(nameType + 2, buf));
                    break;
                }
                case 18: {
                    if (classWriter.bootstrapMethods == null) {
                        this.copyBootstrapMethods(classWriter, items2, buf);
                    }
                    int nameType = this.items[this.readUnsignedShort(index + 2)];
                    item.set(this.readUTF8(nameType, buf), this.readUTF8(nameType + 2, buf), this.readUnsignedShort(index));
                    break;
                }
                default: {
                    item.set(tag, this.readUTF8(index, buf), null, null);
                }
            }
            int index2 = item.hashCode % items2.length;
            item.next = items2[index2];
            items2[index2] = item;
        }
        int off = this.items[1] - 1;
        classWriter.pool.putByteArray(this.b, off, this.header - off);
        classWriter.items = items2;
        classWriter.threshold = (int)(0.75 * (double)ll);
        classWriter.index = ll;
    }

    private void copyBootstrapMethods(ClassWriter classWriter, Item[] items2, char[] buf) {
        int j2;
        int v2 = this.header;
        v2 += 8 + (this.readUnsignedShort(v2 + 6) << 1);
        int i2 = this.readUnsignedShort(v2);
        v2 += 2;
        while (i2 > 0) {
            j2 = this.readUnsignedShort(v2 + 6);
            v2 += 8;
            while (j2 > 0) {
                v2 += 6 + this.readInt(v2 + 2);
                --j2;
            }
            --i2;
        }
        i2 = this.readUnsignedShort(v2);
        v2 += 2;
        while (i2 > 0) {
            j2 = this.readUnsignedShort(v2 + 6);
            v2 += 8;
            while (j2 > 0) {
                v2 += 6 + this.readInt(v2 + 2);
                --j2;
            }
            --i2;
        }
        i2 = this.readUnsignedShort(v2);
        v2 += 2;
        while (i2 > 0) {
            String attrName = this.readUTF8(v2, buf);
            int size = this.readInt(v2 + 2);
            if ("BootstrapMethods".equals(attrName)) {
                int boostrapMethodCount = this.readUnsignedShort(v2 + 6);
                int x2 = v2 + 8;
                for (j2 = 0; j2 < boostrapMethodCount; ++j2) {
                    int hashCode = this.readConst(this.readUnsignedShort(x2), buf).hashCode();
                    int u2 = x2 + 4;
                    for (int k2 = this.readUnsignedShort(x2 + 2); k2 > 0; --k2) {
                        hashCode ^= this.readConst(this.readUnsignedShort(u2), buf).hashCode();
                        u2 += 2;
                    }
                    Item item = new Item(j2);
                    item.set(x2 - v2 - 8, hashCode & Integer.MAX_VALUE);
                    int index2 = item.hashCode % items2.length;
                    item.next = items2[index2];
                    items2[index2] = item;
                    x2 = u2;
                }
                classWriter.bootstrapMethodsCount = boostrapMethodCount;
                ByteVector bootstrapMethods = new ByteVector(size + 62);
                bootstrapMethods.putByteArray(this.b, v2 + 8, size - 2);
                classWriter.bootstrapMethods = bootstrapMethods;
                return;
            }
            v2 += 6 + size;
            --i2;
        }
    }

    public ClassReader(InputStream is) throws IOException {
        this(ClassReader.readClass(is));
    }

    public ClassReader(String name) throws IOException {
        this(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
    }

    private static byte[] readClass(InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("Class not found");
        }
        byte[] b2 = new byte[is.available()];
        int len = 0;
        while (true) {
            int n2;
            if ((n2 = is.read(b2, len, b2.length - len)) == -1) {
                if (len < b2.length) {
                    byte[] c2 = new byte[len];
                    System.arraycopy(b2, 0, c2, 0, len);
                    b2 = c2;
                }
                return b2;
            }
            if ((len += n2) != b2.length) continue;
            int last = is.read();
            if (last < 0) {
                return b2;
            }
            byte[] c3 = new byte[b2.length + 1000];
            System.arraycopy(b2, 0, c3, 0, len);
            c3[len++] = (byte)last;
            b2 = c3;
        }
    }

    public void accept(ClassVisitor classVisitor, int flags) {
        this.accept(classVisitor, new Attribute[0], flags);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags) {
        block210: {
            b = this.b;
            c = new char[this.maxStringLength];
            anns = 0;
            ianns = 0;
            cattrs = null;
            u = this.header;
            access = this.readUnsignedShort(u);
            name = this.readClass(u + 2, c);
            v = this.items[this.readUnsignedShort(u + 4)];
            superClassName = v == 0 ? null : this.readUTF8(v, c);
            implementedItfs = new String[this.readUnsignedShort(u + 6)];
            w = 0;
            u += 8;
            for (i = 0; i < implementedItfs.length; u += 2, ++i) {
                implementedItfs[i] = this.readClass(u, c);
            }
            skipCode = (flags & 1) != 0;
            skipDebug = (flags & 2) != 0;
            unzip = (flags & 8) != 0;
            v = u;
            i = this.readUnsignedShort(v);
            v += 2;
            while (i > 0) {
                j = this.readUnsignedShort(v + 6);
                v += 8;
                while (j > 0) {
                    v += 6 + this.readInt(v + 2);
                    --j;
                }
                --i;
            }
            i = this.readUnsignedShort(v);
            v += 2;
            while (i > 0) {
                j = this.readUnsignedShort(v + 6);
                v += 8;
                while (j > 0) {
                    v += 6 + this.readInt(v + 2);
                    --j;
                }
                --i;
            }
            signature = null;
            sourceFile = null;
            sourceDebug = null;
            enclosingOwner = null;
            enclosingName = null;
            enclosingDesc = null;
            bootstrapMethods = null;
            i = this.readUnsignedShort(v);
            v += 2;
            while (i > 0) {
                attrName = this.readUTF8(v, c);
                if ("SourceFile".equals(attrName)) {
                    sourceFile = this.readUTF8(v + 6, c);
                } else if ("InnerClasses".equals(attrName)) {
                    w = v + 6;
                } else if ("EnclosingMethod".equals(attrName)) {
                    enclosingOwner = this.readClass(v + 6, c);
                    item = this.readUnsignedShort(v + 8);
                    if (item != 0) {
                        enclosingName = this.readUTF8(this.items[item], c);
                        enclosingDesc = this.readUTF8(this.items[item] + 2, c);
                    }
                } else if ("Signature".equals(attrName)) {
                    signature = this.readUTF8(v + 6, c);
                } else if ("RuntimeVisibleAnnotations".equals(attrName)) {
                    anns = v + 6;
                } else if ("Deprecated".equals(attrName)) {
                    access |= 131072;
                } else if ("Synthetic".equals(attrName)) {
                    access |= 266240;
                } else if ("SourceDebugExtension".equals(attrName)) {
                    len = this.readInt(v + 2);
                    sourceDebug = this.readUTF(v + 6, len, new char[len]);
                } else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
                    ianns = v + 6;
                } else if ("BootstrapMethods".equals(attrName)) {
                    boostrapMethodCount = this.readUnsignedShort(v + 6);
                    bootstrapMethods = new int[boostrapMethodCount];
                    x = v + 8;
                    for (j = 0; j < boostrapMethodCount; x += 2 + this.readUnsignedShort(x + 2) << 1, ++j) {
                        bootstrapMethods[j] = x;
                    }
                } else {
                    attr = this.readAttribute(attrs, attrName, v + 6, this.readInt(v + 2), c, -1, null);
                    if (attr != null) {
                        attr.next = cattrs;
                        cattrs = attr;
                    }
                }
                v += 6 + this.readInt(v + 2);
                --i;
            }
            classVisitor.visit(this.readInt(4), access, name, signature, superClassName, implementedItfs);
            if (!(skipDebug || sourceFile == null && sourceDebug == null)) {
                classVisitor.visitSource(sourceFile, sourceDebug);
            }
            if (enclosingOwner != null) {
                classVisitor.visitOuterClass(enclosingOwner, enclosingName, enclosingDesc);
            }
            i = 1;
            while (true) {
                block214: {
                    block215: {
                        block213: {
                            if (i < 0) break block213;
                            v0 = v = i == 0 ? ianns : anns;
                            if (v == 0) break block214;
                            j = this.readUnsignedShort(v);
                            v += 2;
                            break block215;
                        }
                        while (cattrs != null) {
                            attr = cattrs.next;
                            cattrs.next = null;
                            classVisitor.visitAttribute(cattrs);
                            cattrs = attr;
                        }
                        if (w != 0) {
                            i = this.readUnsignedShort(w);
                            w += 2;
                            break;
                        }
                        break block210;
                    }
                    while (j > 0) {
                        v = this.readAnnotationValues(v + 2, c, true, classVisitor.visitAnnotation(this.readUTF8(v, c), i != 0));
                        --j;
                    }
                }
                --i;
            }
            while (i > 0) {
                classVisitor.visitInnerClass(this.readUnsignedShort(w) == 0 ? null : this.readClass(w, c), this.readUnsignedShort(w + 2) == 0 ? null : this.readClass(w + 2, c), this.readUnsignedShort(w + 4) == 0 ? null : this.readUTF8(w + 4, c), this.readUnsignedShort(w + 6));
                w += 8;
                --i;
            }
        }
        i = this.readUnsignedShort(u);
        u += 2;
        block48: while (true) {
            if (i > 0) {
                access = this.readUnsignedShort(u);
                name = this.readUTF8(u + 2, c);
                desc = this.readUTF8(u + 4, c);
                fieldValueItem = 0;
                signature = null;
                anns = 0;
                ianns = 0;
                cattrs = null;
                j = this.readUnsignedShort(u + 6);
                u += 8;
            } else {
                i = this.readUnsignedShort(u);
                u += 2;
                break;
            }
            while (j > 0) {
                attrName = this.readUTF8(u, c);
                if ("ConstantValue".equals(attrName)) {
                    fieldValueItem = this.readUnsignedShort(u + 6);
                } else if ("Signature".equals(attrName)) {
                    signature = this.readUTF8(u + 6, c);
                } else if ("Deprecated".equals(attrName)) {
                    access |= 131072;
                } else if ("Synthetic".equals(attrName)) {
                    access |= 266240;
                } else if ("RuntimeVisibleAnnotations".equals(attrName)) {
                    anns = u + 6;
                } else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
                    ianns = u + 6;
                } else {
                    attr = this.readAttribute(attrs, attrName, u + 6, this.readInt(u + 2), c, -1, null);
                    if (attr != null) {
                        attr.next = cattrs;
                        cattrs = attr;
                    }
                }
                u += 6 + this.readInt(u + 2);
                --j;
            }
            fv = classVisitor.visitField(access, name, desc, signature, fieldValueItem == 0 ? null : this.readConst(fieldValueItem, c));
            if (fv == null) ** GOTO lbl203
            j = 1;
            while (true) {
                block217: {
                    block218: {
                        block216: {
                            if (j < 0) break block216;
                            v1 = v = j == 0 ? ianns : anns;
                            if (v == 0) break block217;
                            k = this.readUnsignedShort(v);
                            v += 2;
                            break block218;
                        }
                        while (cattrs != null) {
                            attr = cattrs.next;
                            cattrs.next = null;
                            fv.visitAttribute(cattrs);
                            cattrs = attr;
                        }
                        fv.visitEnd();
lbl203:
                        // 2 sources

                        --i;
                        continue block48;
                    }
                    while (k > 0) {
                        v = this.readAnnotationValues(v + 2, c, true, fv.visitAnnotation(this.readUTF8(v, c), j != 0));
                        --k;
                    }
                }
                --j;
            }
            break;
        }
        while (true) {
            block220: {
                block211: {
                    block212: {
                        block219: {
                            if (i <= 0) {
                                classVisitor.visitEnd();
                                return;
                            }
                            u0 = u + 6;
                            access = this.readUnsignedShort(u);
                            name = this.readUTF8(u + 2, c);
                            desc = this.readUTF8(u + 4, c);
                            signature = null;
                            anns = 0;
                            ianns = 0;
                            dann = 0;
                            mpanns = 0;
                            impanns = 0;
                            cattrs = null;
                            v = 0;
                            w = 0;
                            j = this.readUnsignedShort(u + 6);
                            u += 8;
                            while (j > 0) {
                                attrName = this.readUTF8(u, c);
                                attrSize = this.readInt(u + 2);
                                u += 6;
                                if ("Code".equals(attrName)) {
                                    if (!skipCode) {
                                        v = u;
                                    }
                                } else if ("Exceptions".equals(attrName)) {
                                    w = u;
                                } else if ("Signature".equals(attrName)) {
                                    signature = this.readUTF8(u, c);
                                } else if ("Deprecated".equals(attrName)) {
                                    access |= 131072;
                                } else if ("RuntimeVisibleAnnotations".equals(attrName)) {
                                    anns = u;
                                } else if ("AnnotationDefault".equals(attrName)) {
                                    dann = u;
                                } else if ("Synthetic".equals(attrName)) {
                                    access |= 266240;
                                } else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
                                    ianns = u;
                                } else if ("RuntimeVisibleParameterAnnotations".equals(attrName)) {
                                    mpanns = u;
                                } else if ("RuntimeInvisibleParameterAnnotations".equals(attrName)) {
                                    impanns = u;
                                } else {
                                    attr = this.readAttribute(attrs, attrName, u, attrSize, c, -1, null);
                                    if (attr != null) {
                                        attr.next = cattrs;
                                        cattrs = attr;
                                    }
                                }
                                u += attrSize;
                                --j;
                            }
                            if (w == 0) {
                                exceptions = null;
                            } else {
                                exceptions = new String[this.readUnsignedShort(w)];
                                w += 2;
                                for (j = 0; j < exceptions.length; w += 2, ++j) {
                                    exceptions[j] = this.readClass(w, c);
                                }
                            }
                            mv = classVisitor.visitMethod(access, name, desc, signature, exceptions);
                            if (mv == null) ** GOTO lbl327
                            if (!(mv instanceof MethodWriter)) break block219;
                            mw = (MethodWriter)mv;
                            if (mw.cw.cr != this || signature != mw.signature) break block219;
                            sameExceptions = false;
                            if (exceptions == null) {
                                sameExceptions = mw.exceptionCount == 0;
                            } else if (exceptions.length == mw.exceptionCount) {
                                sameExceptions = true;
                                for (j = exceptions.length - 1; j >= 0; --j) {
                                    if (mw.exceptions[j] == this.readUnsignedShort(w -= 2)) continue;
                                    sameExceptions = false;
                                    break;
                                }
                            }
                            if (!sameExceptions) break block219;
                            mw.classReaderOffset = u0;
                            mw.classReaderLength = u - u0;
                            break block220;
                        }
                        if (dann != 0) {
                            dv = mv.visitAnnotationDefault();
                            this.readAnnotationValue(dann, c, null, dv);
                            if (dv != null) {
                                dv.visitEnd();
                            }
                        }
                        j = 1;
                        while (true) {
                            block222: {
                                block223: {
                                    block221: {
                                        if (j < 0) break block221;
                                        v2 = w = j == 0 ? ianns : anns;
                                        if (w == 0) break block222;
                                        k = this.readUnsignedShort(w);
                                        w += 2;
                                        break block223;
                                    }
                                    if (mpanns != 0) {
                                        this.readParameterAnnotations(mpanns, desc, c, true, mv);
                                    }
                                    if (impanns != 0) {
                                        this.readParameterAnnotations(impanns, desc, c, false, mv);
                                    }
                                    while (cattrs != null) {
                                        attr = cattrs.next;
                                        cattrs.next = null;
                                        mv.visitAttribute(cattrs);
                                        cattrs = attr;
                                    }
lbl327:
                                    // 2 sources

                                    if (mv != null && v != 0) {
                                        maxStack = this.readUnsignedShort(v);
                                        maxLocals = this.readUnsignedShort(v + 2);
                                        codeLength = this.readInt(v + 4);
                                        codeStart = v += 8;
                                        codeEnd = v + codeLength;
                                        mv.visitCode();
                                        labels = new Label[codeLength + 2];
                                        this.readLabel(codeLength + 1, labels);
                                        break;
                                    }
                                    break block211;
                                }
                                while (k > 0) {
                                    w = this.readAnnotationValues(w + 2, c, true, mv.visitAnnotation(this.readUTF8(w, c), j != 0));
                                    --k;
                                }
                            }
                            --j;
                        }
                        block60: while (v < codeEnd) {
                            w = v - codeStart;
                            opcode = b[v] & 255;
                            switch (ClassWriter.TYPE[opcode]) {
                                case 0: 
                                case 4: {
                                    ++v;
                                    continue block60;
                                }
                                case 9: {
                                    this.readLabel(w + this.readShort(v + 1), labels);
                                    v += 3;
                                    continue block60;
                                }
                                case 10: {
                                    this.readLabel(w + this.readInt(v + 1), labels);
                                    v += 5;
                                    continue block60;
                                }
                                case 17: {
                                    opcode = b[v + 1] & 255;
                                    if (opcode == 132) {
                                        v += 6;
                                        continue block60;
                                    }
                                    v += 4;
                                    continue block60;
                                }
                                case 14: {
                                    v = v + 4 - (w & 3);
                                    this.readLabel(w + this.readInt(v), labels);
                                    j = this.readInt(v + 8) - this.readInt(v + 4) + 1;
                                    v += 12;
                                    while (j > 0) {
                                        this.readLabel(w + this.readInt(v), labels);
                                        v += 4;
                                        --j;
                                    }
                                    continue block60;
                                }
                                case 15: {
                                    v = v + 4 - (w & 3);
                                    this.readLabel(w + this.readInt(v), labels);
                                    j = this.readInt(v + 4);
                                    v += 8;
                                    while (j > 0) {
                                        this.readLabel(w + this.readInt(v + 4), labels);
                                        v += 8;
                                        --j;
                                    }
                                    continue block60;
                                }
                                case 1: 
                                case 3: 
                                case 11: {
                                    v += 2;
                                    continue block60;
                                }
                                case 2: 
                                case 5: 
                                case 6: 
                                case 12: 
                                case 13: {
                                    v += 3;
                                    continue block60;
                                }
                                case 7: 
                                case 8: {
                                    v += 5;
                                    continue block60;
                                }
                            }
                            v += 4;
                        }
                        j = this.readUnsignedShort(v);
                        v += 2;
                        while (j > 0) {
                            start = this.readLabel(this.readUnsignedShort(v), labels);
                            end = this.readLabel(this.readUnsignedShort(v + 2), labels);
                            handler = this.readLabel(this.readUnsignedShort(v + 4), labels);
                            type = this.readUnsignedShort(v + 6);
                            if (type == 0) {
                                mv.visitTryCatchBlock(start, end, handler, null);
                            } else {
                                mv.visitTryCatchBlock(start, end, handler, this.readUTF8(this.items[type], c));
                            }
                            v += 8;
                            --j;
                        }
                        varTable = 0;
                        varTypeTable = 0;
                        stackMap = 0;
                        stackMapSize = 0;
                        frameCount = 0;
                        frameMode = 0;
                        frameOffset = 0;
                        frameLocalCount = 0;
                        frameLocalDiff = 0;
                        frameStackCount = 0;
                        frameLocal = null;
                        frameStack = null;
                        zip = true;
                        cattrs = null;
                        j = this.readUnsignedShort(v);
                        v += 2;
                        while (j > 0) {
                            attrName = this.readUTF8(v, c);
                            if ("LocalVariableTable".equals(attrName)) {
                                if (!skipDebug) {
                                    varTable = v + 6;
                                    w = v + 8;
                                    for (k = this.readUnsignedShort(v + 6); k > 0; w += 10, --k) {
                                        label = this.readUnsignedShort(w);
                                        if (labels[label] == null) {
                                            this.readLabel((int)label, (Label[])labels).status |= 1;
                                        }
                                        if (labels[label += this.readUnsignedShort(w + 2)] != null) continue;
                                        this.readLabel((int)label, (Label[])labels).status |= 1;
                                    }
                                }
                            } else if ("LocalVariableTypeTable".equals(attrName)) {
                                varTypeTable = v + 6;
                            } else if ("LineNumberTable".equals(attrName)) {
                                if (!skipDebug) {
                                    w = v + 8;
                                    for (k = this.readUnsignedShort(v + 6); k > 0; w += 4, --k) {
                                        label = this.readUnsignedShort(w);
                                        if (labels[label] == null) {
                                            this.readLabel((int)label, (Label[])labels).status |= 1;
                                        }
                                        labels[label].line = this.readUnsignedShort(w + 2);
                                    }
                                }
                            } else if ("StackMapTable".equals(attrName)) {
                                if ((flags & 4) == 0) {
                                    stackMap = v + 8;
                                    stackMapSize = this.readInt(v + 2);
                                    frameCount = this.readUnsignedShort(v + 6);
                                }
                            } else if ("StackMap".equals(attrName)) {
                                if ((flags & 4) == 0) {
                                    stackMap = v + 8;
                                    stackMapSize = this.readInt(v + 2);
                                    frameCount = this.readUnsignedShort(v + 6);
                                    zip = false;
                                }
                            } else {
                                for (k = 0; k < attrs.length; ++k) {
                                    if (!attrs[k].type.equals(attrName) || (attr = attrs[k].read(this, v + 6, this.readInt(v + 2), c, codeStart - 8, labels)) == null) continue;
                                    attr.next = cattrs;
                                    cattrs = attr;
                                }
                            }
                            v += 6 + this.readInt(v + 2);
                            --j;
                        }
                        if (stackMap != 0) {
                            frameLocal = new Object[maxLocals];
                            frameStack = new Object[maxStack];
                            if (unzip) {
                                local = 0;
                                if ((access & 8) == 0) {
                                    frameLocal[local++] = "<init>".equals(name) != false ? Opcodes.UNINITIALIZED_THIS : this.readClass(this.header + 2, c);
                                }
                                j = 1;
                                block68: while (true) {
                                    k = j;
                                    switch (desc.charAt(j++)) {
                                        case 'B': 
                                        case 'C': 
                                        case 'I': 
                                        case 'S': 
                                        case 'Z': {
                                            frameLocal[local++] = Opcodes.INTEGER;
                                            continue block68;
                                        }
                                        case 'F': {
                                            frameLocal[local++] = Opcodes.FLOAT;
                                            continue block68;
                                        }
                                        case 'J': {
                                            frameLocal[local++] = Opcodes.LONG;
                                            continue block68;
                                        }
                                        case 'D': {
                                            frameLocal[local++] = Opcodes.DOUBLE;
                                            continue block68;
                                        }
                                        case '[': {
                                            while (desc.charAt(j) == '[') {
                                                ++j;
                                            }
                                            if (desc.charAt(j) == 'L') {
                                                ++j;
                                                while (desc.charAt(j) != ';') {
                                                    ++j;
                                                }
                                            }
                                            frameLocal[local++] = desc.substring(k, ++j);
                                            continue block68;
                                        }
                                        case 'L': {
                                            while (desc.charAt(j) != ';') {
                                                ++j;
                                            }
                                            frameLocal[local++] = desc.substring(k + 1, j++);
                                            continue block68;
                                        }
                                    }
                                    break;
                                }
                                frameLocalCount = local;
                            }
                            frameOffset = -1;
                            for (j = stackMap; j < stackMap + stackMapSize - 2; ++j) {
                                if (b[j] != 8 || (k = this.readUnsignedShort(j + 1)) < 0 || k >= codeLength || (b[codeStart + k] & 255) != 187) continue;
                                this.readLabel(k, labels);
                            }
                        }
                        v = codeStart;
                        block73: while (true) {
                            block225: {
                                block224: {
                                    if (v >= codeEnd) break block224;
                                    w = v - codeStart;
                                    l = labels[w];
                                    if (l == null) break block225;
                                    mv.visitLabel(l);
                                    if (skipDebug || l.line <= 0) break block225;
                                    mv.visitLineNumber(l.line, l);
                                    break block225;
                                }
                                l = labels[codeEnd - codeStart];
                                if (l != null) {
                                    mv.visitLabel(l);
                                }
                                if (!skipDebug && varTable != 0) {
                                    typeTable = null;
                                    if (varTypeTable != 0) {
                                        k = this.readUnsignedShort(varTypeTable) * 3;
                                        w = varTypeTable + 2;
                                        typeTable = new int[k];
                                        while (k > 0) {
                                            typeTable[--k] = w + 6;
                                            typeTable[--k] = this.readUnsignedShort(w + 8);
                                            typeTable[--k] = this.readUnsignedShort(w);
                                            w += 10;
                                        }
                                    }
                                    w = varTable + 2;
                                    break;
                                }
                                break block212;
                            }
                            while (frameLocal != null && (frameOffset == w || frameOffset == -1)) {
                                if (!zip || unzip) {
                                    mv.visitFrame(-1, frameLocalCount, frameLocal, frameStackCount, frameStack);
                                } else if (frameOffset != -1) {
                                    mv.visitFrame(frameMode, frameLocalDiff, frameLocal, frameStackCount, frameStack);
                                }
                                if (frameCount > 0) {
                                    if (zip) {
                                        tag = b[stackMap++] & 255;
                                    } else {
                                        tag = 255;
                                        frameOffset = -1;
                                    }
                                    frameLocalDiff = 0;
                                    if (tag < 64) {
                                        delta = tag;
                                        frameMode = 3;
                                        frameStackCount = 0;
                                    } else if (tag < 128) {
                                        delta = tag - 64;
                                        stackMap = this.readFrameType(frameStack, 0, stackMap, c, labels);
                                        frameMode = 4;
                                        frameStackCount = 1;
                                    } else {
                                        delta = this.readUnsignedShort(stackMap);
                                        stackMap += 2;
                                        if (tag == 247) {
                                            stackMap = this.readFrameType(frameStack, 0, stackMap, c, labels);
                                            frameMode = 4;
                                            frameStackCount = 1;
                                        } else if (tag >= 248 && tag < 251) {
                                            frameMode = 2;
                                            frameLocalDiff = 251 - tag;
                                            frameLocalCount -= frameLocalDiff;
                                            frameStackCount = 0;
                                        } else if (tag == 251) {
                                            frameMode = 3;
                                            frameStackCount = 0;
                                        } else if (tag < 255) {
                                            j = unzip != false ? frameLocalCount : 0;
                                            for (k = tag - 251; k > 0; --k) {
                                                stackMap = this.readFrameType(frameLocal, j++, stackMap, c, labels);
                                            }
                                            frameMode = 1;
                                            frameLocalDiff = tag - 251;
                                            frameLocalCount += frameLocalDiff;
                                            frameStackCount = 0;
                                        } else {
                                            frameMode = 0;
                                            frameLocalDiff = frameLocalCount = this.readUnsignedShort(stackMap);
                                            stackMap += 2;
                                            j = 0;
                                            for (n = frameLocalCount; n > 0; --n) {
                                                stackMap = this.readFrameType(frameLocal, j++, stackMap, c, labels);
                                            }
                                            n = frameStackCount = this.readUnsignedShort(stackMap);
                                            stackMap += 2;
                                            j = 0;
                                            while (n > 0) {
                                                stackMap = this.readFrameType(frameStack, j++, stackMap, c, labels);
                                                --n;
                                            }
                                        }
                                    }
                                    this.readLabel(frameOffset += delta + 1, labels);
                                    --frameCount;
                                    continue;
                                }
                                frameLocal = null;
                            }
                            opcode = b[v] & 255;
                            switch (ClassWriter.TYPE[opcode]) {
                                case 0: {
                                    mv.visitInsn(opcode);
                                    ++v;
                                    continue block73;
                                }
                                case 4: {
                                    if (opcode > 54) {
                                        mv.visitVarInsn(54 + ((opcode -= 59) >> 2), opcode & 3);
                                    } else {
                                        mv.visitVarInsn(21 + ((opcode -= 26) >> 2), opcode & 3);
                                    }
                                    ++v;
                                    continue block73;
                                }
                                case 9: {
                                    mv.visitJumpInsn(opcode, labels[w + this.readShort(v + 1)]);
                                    v += 3;
                                    continue block73;
                                }
                                case 10: {
                                    mv.visitJumpInsn(opcode - 33, labels[w + this.readInt(v + 1)]);
                                    v += 5;
                                    continue block73;
                                }
                                case 17: {
                                    opcode = b[v + 1] & 255;
                                    if (opcode == 132) {
                                        mv.visitIincInsn(this.readUnsignedShort(v + 2), this.readShort(v + 4));
                                        v += 6;
                                        continue block73;
                                    }
                                    mv.visitVarInsn(opcode, this.readUnsignedShort(v + 2));
                                    v += 4;
                                    continue block73;
                                }
                                case 14: {
                                    v = v + 4 - (w & 3);
                                    label = w + this.readInt(v);
                                    min = this.readInt(v + 4);
                                    max = this.readInt(v + 8);
                                    v += 12;
                                    table = new Label[max - min + 1];
                                    for (j = 0; j < table.length; v += 4, ++j) {
                                        table[j] = labels[w + this.readInt(v)];
                                    }
                                    mv.visitTableSwitchInsn(min, max, labels[label], table);
                                    continue block73;
                                }
                                case 15: {
                                    v = v + 4 - (w & 3);
                                    label = w + this.readInt(v);
                                    j = this.readInt(v + 4);
                                    v += 8;
                                    keys = new int[j];
                                    values = new Label[j];
                                    for (j = 0; j < keys.length; v += 8, ++j) {
                                        keys[j] = this.readInt(v);
                                        values[j] = labels[w + this.readInt(v + 4)];
                                    }
                                    mv.visitLookupSwitchInsn(labels[label], keys, values);
                                    continue block73;
                                }
                                case 3: {
                                    mv.visitVarInsn(opcode, b[v + 1] & 255);
                                    v += 2;
                                    continue block73;
                                }
                                case 1: {
                                    mv.visitIntInsn(opcode, b[v + 1]);
                                    v += 2;
                                    continue block73;
                                }
                                case 2: {
                                    mv.visitIntInsn(opcode, this.readShort(v + 1));
                                    v += 3;
                                    continue block73;
                                }
                                case 11: {
                                    mv.visitLdcInsn(this.readConst(b[v + 1] & 255, c));
                                    v += 2;
                                    continue block73;
                                }
                                case 12: {
                                    mv.visitLdcInsn(this.readConst(this.readUnsignedShort(v + 1), c));
                                    v += 3;
                                    continue block73;
                                }
                                case 6: 
                                case 7: {
                                    cpIndex = this.items[this.readUnsignedShort(v + 1)];
                                    iowner = this.readClass(cpIndex, c);
                                    cpIndex = this.items[this.readUnsignedShort(cpIndex + 2)];
                                    iname = this.readUTF8(cpIndex, c);
                                    idesc = this.readUTF8(cpIndex + 2, c);
                                    if (opcode < 182) {
                                        mv.visitFieldInsn(opcode, iowner, iname, idesc);
                                    } else {
                                        mv.visitMethodInsn(opcode, iowner, iname, idesc);
                                    }
                                    if (opcode == 185) {
                                        v += 5;
                                        continue block73;
                                    }
                                    v += 3;
                                    continue block73;
                                }
                                case 8: {
                                    cpIndex = this.items[this.readUnsignedShort(v + 1)];
                                    bsmIndex = bootstrapMethods[this.readUnsignedShort(cpIndex)];
                                    cpIndex = this.items[this.readUnsignedShort(cpIndex + 2)];
                                    iname = this.readUTF8(cpIndex, c);
                                    idesc = this.readUTF8(cpIndex + 2, c);
                                    mhIndex = this.readUnsignedShort(bsmIndex);
                                    bsm = (MethodHandle)this.readConst(mhIndex, c);
                                    bsmArgCount = this.readUnsignedShort(bsmIndex + 2);
                                    bsmArgs = new Object[bsmArgCount];
                                    bsmIndex += 4;
                                    for (a = 0; a < bsmArgCount; bsmIndex += 2, ++a) {
                                        argIndex = this.readUnsignedShort(bsmIndex);
                                        bsmArgs[a] = this.readConst(argIndex, c);
                                    }
                                    mv.visitInvokeDynamicInsn(iname, idesc, bsm, bsmArgs);
                                    v += 5;
                                    continue block73;
                                }
                                case 5: {
                                    mv.visitTypeInsn(opcode, this.readClass(v + 1, c));
                                    v += 3;
                                    continue block73;
                                }
                                case 13: {
                                    mv.visitIincInsn(b[v + 1] & 255, b[v + 2]);
                                    v += 3;
                                    continue block73;
                                }
                            }
                            mv.visitMultiANewArrayInsn(this.readClass(v + 1, c), b[v + 3] & 255);
                            v += 4;
                        }
                        for (k = this.readUnsignedShort(varTable); k > 0; w += 10, --k) {
                            start = this.readUnsignedShort(w);
                            length = this.readUnsignedShort(w + 2);
                            index = this.readUnsignedShort(w + 8);
                            vsignature = null;
                            if (typeTable != null) {
                                for (a = 0; a < typeTable.length; a += 3) {
                                    if (typeTable[a] != start || typeTable[a + 1] != index) continue;
                                    vsignature = this.readUTF8(typeTable[a + 2], c);
                                    break;
                                }
                            }
                            mv.visitLocalVariable(this.readUTF8(w + 4, c), this.readUTF8(w + 6, c), vsignature, labels[start], labels[start + length], index);
                        }
                    }
                    while (cattrs != null) {
                        attr = cattrs.next;
                        cattrs.next = null;
                        mv.visitAttribute(cattrs);
                        cattrs = attr;
                    }
                    mv.visitMaxs(maxStack, maxLocals);
                }
                if (mv != null) {
                    mv.visitEnd();
                }
            }
            --i;
        }
    }

    private void readParameterAnnotations(int v2, String desc, char[] buf, boolean visible, MethodVisitor mv) {
        AnnotationVisitor av;
        int i2;
        int n2 = this.b[v2++] & 0xFF;
        int synthetics = Type.getArgumentTypes(desc).length - n2;
        for (i2 = 0; i2 < synthetics; ++i2) {
            av = mv.visitParameterAnnotation(i2, "Ljava/lang/Synthetic;", false);
            if (av == null) continue;
            av.visitEnd();
        }
        while (i2 < n2 + synthetics) {
            int j2 = this.readUnsignedShort(v2);
            v2 += 2;
            while (j2 > 0) {
                av = mv.visitParameterAnnotation(i2, this.readUTF8(v2, buf), visible);
                v2 = this.readAnnotationValues(v2 + 2, buf, true, av);
                --j2;
            }
            ++i2;
        }
    }

    private int readAnnotationValues(int v2, char[] buf, boolean named, AnnotationVisitor av) {
        int i2 = this.readUnsignedShort(v2);
        v2 += 2;
        if (named) {
            while (i2 > 0) {
                v2 = this.readAnnotationValue(v2 + 2, buf, this.readUTF8(v2, buf), av);
                --i2;
            }
        } else {
            while (i2 > 0) {
                v2 = this.readAnnotationValue(v2, buf, null, av);
                --i2;
            }
        }
        if (av != null) {
            av.visitEnd();
        }
        return v2;
    }

    private int readAnnotationValue(int v2, char[] buf, String name, AnnotationVisitor av) {
        if (av == null) {
            switch (this.b[v2] & 0xFF) {
                case 101: {
                    return v2 + 5;
                }
                case 64: {
                    return this.readAnnotationValues(v2 + 3, buf, true, null);
                }
                case 91: {
                    return this.readAnnotationValues(v2 + 1, buf, false, null);
                }
            }
            return v2 + 3;
        }
        block5 : switch (this.b[v2++] & 0xFF) {
            case 68: 
            case 70: 
            case 73: 
            case 74: {
                av.visit(name, this.readConst(this.readUnsignedShort(v2), buf));
                v2 += 2;
                break;
            }
            case 66: {
                av.visit(name, new Byte((byte)this.readInt(this.items[this.readUnsignedShort(v2)])));
                v2 += 2;
                break;
            }
            case 90: {
                av.visit(name, this.readInt(this.items[this.readUnsignedShort(v2)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                v2 += 2;
                break;
            }
            case 83: {
                av.visit(name, new Short((short)this.readInt(this.items[this.readUnsignedShort(v2)])));
                v2 += 2;
                break;
            }
            case 67: {
                av.visit(name, new Character((char)this.readInt(this.items[this.readUnsignedShort(v2)])));
                v2 += 2;
                break;
            }
            case 115: {
                av.visit(name, this.readUTF8(v2, buf));
                v2 += 2;
                break;
            }
            case 101: {
                av.visitEnum(name, this.readUTF8(v2, buf), this.readUTF8(v2 + 2, buf));
                v2 += 4;
                break;
            }
            case 99: {
                av.visit(name, Type.getType(this.readUTF8(v2, buf)));
                v2 += 2;
                break;
            }
            case 64: {
                v2 = this.readAnnotationValues(v2 + 2, buf, true, av.visitAnnotation(name, this.readUTF8(v2, buf)));
                break;
            }
            case 91: {
                int size = this.readUnsignedShort(v2);
                v2 += 2;
                if (size == 0) {
                    return this.readAnnotationValues(v2 - 2, buf, false, av.visitArray(name));
                }
                switch (this.b[v2++] & 0xFF) {
                    case 66: {
                        byte[] bv = new byte[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            bv[i2] = (byte)this.readInt(this.items[this.readUnsignedShort(v2)]);
                            v2 += 3;
                        }
                        av.visit(name, bv);
                        --v2;
                        break block5;
                    }
                    case 90: {
                        boolean[] zv = new boolean[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            zv[i2] = this.readInt(this.items[this.readUnsignedShort(v2)]) != 0;
                            v2 += 3;
                        }
                        av.visit(name, zv);
                        --v2;
                        break block5;
                    }
                    case 83: {
                        short[] sv = new short[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            sv[i2] = (short)this.readInt(this.items[this.readUnsignedShort(v2)]);
                            v2 += 3;
                        }
                        av.visit(name, sv);
                        --v2;
                        break block5;
                    }
                    case 67: {
                        char[] cv = new char[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            cv[i2] = (char)this.readInt(this.items[this.readUnsignedShort(v2)]);
                            v2 += 3;
                        }
                        av.visit(name, cv);
                        --v2;
                        break block5;
                    }
                    case 73: {
                        int[] iv = new int[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            iv[i2] = this.readInt(this.items[this.readUnsignedShort(v2)]);
                            v2 += 3;
                        }
                        av.visit(name, iv);
                        --v2;
                        break block5;
                    }
                    case 74: {
                        long[] lv = new long[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            lv[i2] = this.readLong(this.items[this.readUnsignedShort(v2)]);
                            v2 += 3;
                        }
                        av.visit(name, lv);
                        --v2;
                        break block5;
                    }
                    case 70: {
                        float[] fv = new float[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            fv[i2] = Float.intBitsToFloat(this.readInt(this.items[this.readUnsignedShort(v2)]));
                            v2 += 3;
                        }
                        av.visit(name, fv);
                        --v2;
                        break block5;
                    }
                    case 68: {
                        double[] dv = new double[size];
                        for (int i2 = 0; i2 < size; ++i2) {
                            dv[i2] = Double.longBitsToDouble(this.readLong(this.items[this.readUnsignedShort(v2)]));
                            v2 += 3;
                        }
                        av.visit(name, dv);
                        --v2;
                        break block5;
                    }
                }
                v2 = this.readAnnotationValues(v2 - 3, buf, false, av.visitArray(name));
            }
        }
        return v2;
    }

    private int readFrameType(Object[] frame, int index, int v2, char[] buf, Label[] labels) {
        int type = this.b[v2++] & 0xFF;
        switch (type) {
            case 0: {
                frame[index] = Opcodes.TOP;
                break;
            }
            case 1: {
                frame[index] = Opcodes.INTEGER;
                break;
            }
            case 2: {
                frame[index] = Opcodes.FLOAT;
                break;
            }
            case 3: {
                frame[index] = Opcodes.DOUBLE;
                break;
            }
            case 4: {
                frame[index] = Opcodes.LONG;
                break;
            }
            case 5: {
                frame[index] = Opcodes.NULL;
                break;
            }
            case 6: {
                frame[index] = Opcodes.UNINITIALIZED_THIS;
                break;
            }
            case 7: {
                frame[index] = this.readClass(v2, buf);
                v2 += 2;
                break;
            }
            default: {
                frame[index] = this.readLabel(this.readUnsignedShort(v2), labels);
                v2 += 2;
            }
        }
        return v2;
    }

    protected Label readLabel(int offset, Label[] labels) {
        if (labels[offset] == null) {
            labels[offset] = new Label();
        }
        return labels[offset];
    }

    private Attribute readAttribute(Attribute[] attrs, String type, int off, int len, char[] buf, int codeOff, Label[] labels) {
        for (int i2 = 0; i2 < attrs.length; ++i2) {
            if (!attrs[i2].type.equals(type)) continue;
            return attrs[i2].read(this, off, len, buf, codeOff, labels);
        }
        return new Attribute(type).read(this, off, len, null, -1, null);
    }

    public int getItem(int item) {
        return this.items[item];
    }

    public int readByte(int index) {
        return this.b[index] & 0xFF;
    }

    public int readUnsignedShort(int index) {
        byte[] b2 = this.b;
        return (b2[index] & 0xFF) << 8 | b2[index + 1] & 0xFF;
    }

    public short readShort(int index) {
        byte[] b2 = this.b;
        return (short)((b2[index] & 0xFF) << 8 | b2[index + 1] & 0xFF);
    }

    public int readInt(int index) {
        byte[] b2 = this.b;
        return (b2[index] & 0xFF) << 24 | (b2[index + 1] & 0xFF) << 16 | (b2[index + 2] & 0xFF) << 8 | b2[index + 3] & 0xFF;
    }

    public long readLong(int index) {
        long l1 = this.readInt(index);
        long l0 = (long)this.readInt(index + 4) & 0xFFFFFFFFL;
        return l1 << 32 | l0;
    }

    public String readUTF8(int index, char[] buf) {
        int item = this.readUnsignedShort(index);
        String s2 = this.strings[item];
        if (s2 != null) {
            return s2;
        }
        index = this.items[item];
        this.strings[item] = this.readUTF(index + 2, this.readUnsignedShort(index), buf);
        return this.strings[item];
    }

    private String readUTF(int index, int utfLen, char[] buf) {
        int endIndex = index + utfLen;
        byte[] b2 = this.b;
        int strLen = 0;
        int st = 0;
        int cc = 0;
        while (index < endIndex) {
            int c2 = b2[index++];
            switch (st) {
                case 0: {
                    if ((c2 &= 0xFF) < 128) {
                        buf[strLen++] = (char)c2;
                        break;
                    }
                    if (c2 < 224 && c2 > 191) {
                        cc = (char)(c2 & 0x1F);
                        st = 1;
                        break;
                    }
                    cc = (char)(c2 & 0xF);
                    st = 2;
                    break;
                }
                case 1: {
                    buf[strLen++] = (char)(cc << 6 | c2 & 0x3F);
                    st = 0;
                    break;
                }
                case 2: {
                    cc = (char)(cc << 6 | c2 & 0x3F);
                    st = 1;
                }
            }
        }
        return new String(buf, 0, strLen);
    }

    public String readClass(int index, char[] buf) {
        return this.readUTF8(this.items[this.readUnsignedShort(index)], buf);
    }

    public Object readConst(int item, char[] buf) {
        int index = this.items[item];
        switch (this.b[index - 1]) {
            case 3: {
                return new Integer(this.readInt(index));
            }
            case 4: {
                return new Float(Float.intBitsToFloat(this.readInt(index)));
            }
            case 5: {
                return new Long(this.readLong(index));
            }
            case 6: {
                return new Double(Double.longBitsToDouble(this.readLong(index)));
            }
            case 7: {
                return Type.getObjectType(this.readUTF8(index, buf));
            }
            case 8: {
                return this.readUTF8(index, buf);
            }
            case 16: {
                return new MethodType(this.readUTF8(index, buf));
            }
        }
        int tag = this.readByte(index);
        int[] items = this.items;
        int cpIndex = items[this.readUnsignedShort(index + 1)];
        String owner = this.readClass(cpIndex, buf);
        cpIndex = items[this.readUnsignedShort(cpIndex + 2)];
        String name = this.readUTF8(cpIndex, buf);
        String desc = this.readUTF8(cpIndex + 2, buf);
        return new MethodHandle(tag, owner, name, desc);
    }
}

