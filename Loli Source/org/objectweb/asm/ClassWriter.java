/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.AnnotationWriter;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.FieldWriter;
import org.objectweb.asm.Item;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodType;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.MethodWriter;
import org.objectweb.asm.Type;

public class ClassWriter
implements ClassVisitor {
    public static final int COMPUTE_MAXS = 1;
    public static final int COMPUTE_FRAMES = 2;
    static final int ACC_SYNTHETIC_ATTRIBUTE = 262144;
    static final int NOARG_INSN = 0;
    static final int SBYTE_INSN = 1;
    static final int SHORT_INSN = 2;
    static final int VAR_INSN = 3;
    static final int IMPLVAR_INSN = 4;
    static final int TYPE_INSN = 5;
    static final int FIELDORMETH_INSN = 6;
    static final int ITFMETH_INSN = 7;
    static final int INDYMETH_INSN = 8;
    static final int LABEL_INSN = 9;
    static final int LABELW_INSN = 10;
    static final int LDC_INSN = 11;
    static final int LDCW_INSN = 12;
    static final int IINC_INSN = 13;
    static final int TABL_INSN = 14;
    static final int LOOK_INSN = 15;
    static final int MANA_INSN = 16;
    static final int WIDE_INSN = 17;
    static final byte[] TYPE;
    static final int CLASS = 7;
    static final int FIELD = 9;
    static final int METH = 10;
    static final int IMETH = 11;
    static final int STR = 8;
    static final int INT = 3;
    static final int FLOAT = 4;
    static final int LONG = 5;
    static final int DOUBLE = 6;
    static final int NAME_TYPE = 12;
    static final int UTF8 = 1;
    static final int MTYPE = 16;
    static final int MHANDLE = 15;
    static final int INDY = 18;
    static final int MHANDLE_BASE = 20;
    static final int TYPE_NORMAL = 30;
    static final int TYPE_UNINIT = 31;
    static final int TYPE_MERGED = 32;
    static final int BSM = 33;
    ClassReader cr;
    int version;
    int index = 1;
    final ByteVector pool = new ByteVector();
    Item[] items = new Item[256];
    int threshold = (int)(0.75 * (double)this.items.length);
    final Item key = new Item();
    final Item key2 = new Item();
    final Item key3 = new Item();
    final Item key4 = new Item();
    Item[] typeTable;
    private short typeCount;
    private int access;
    private int name;
    String thisName;
    private int signature;
    private int superName;
    private int interfaceCount;
    private int[] interfaces;
    private int sourceFile;
    private ByteVector sourceDebug;
    private int enclosingMethodOwner;
    private int enclosingMethod;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private Attribute attrs;
    private int innerClassesCount;
    private ByteVector innerClasses;
    int bootstrapMethodsCount;
    ByteVector bootstrapMethods;
    FieldWriter firstField;
    FieldWriter lastField;
    MethodWriter firstMethod;
    MethodWriter lastMethod;
    private final boolean computeMaxs;
    private final boolean computeFrames;
    boolean invalidFrames;

    public ClassWriter(int flags) {
        this.computeMaxs = (flags & 1) != 0;
        this.computeFrames = (flags & 2) != 0;
    }

    public ClassWriter(ClassReader classReader, int flags) {
        this(flags);
        classReader.copyPool(this);
        this.cr = classReader;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = this.newClass(name);
        this.thisName = name;
        if (signature != null) {
            this.signature = this.newUTF8(signature);
        }
        int n2 = this.superName = superName == null ? 0 : this.newClass(superName);
        if (interfaces != null && interfaces.length > 0) {
            this.interfaceCount = interfaces.length;
            this.interfaces = new int[this.interfaceCount];
            for (int i2 = 0; i2 < this.interfaceCount; ++i2) {
                this.interfaces[i2] = this.newClass(interfaces[i2]);
            }
        }
    }

    public void visitSource(String file, String debug) {
        if (file != null) {
            this.sourceFile = this.newUTF8(file);
        }
        if (debug != null) {
            this.sourceDebug = new ByteVector().putUTF8(debug);
        }
    }

    public void visitOuterClass(String owner, String name, String desc) {
        this.enclosingMethodOwner = this.newClass(owner);
        if (name != null && desc != null) {
            this.enclosingMethod = this.newNameType(name, desc);
        }
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        ByteVector bv = new ByteVector();
        bv.putShort(this.newUTF8(desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, 2);
        if (visible) {
            aw.next = this.anns;
            this.anns = aw;
        } else {
            aw.next = this.ianns;
            this.ianns = aw;
        }
        return aw;
    }

    public void visitAttribute(Attribute attr) {
        attr.next = this.attrs;
        this.attrs = attr;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        if (this.innerClasses == null) {
            this.innerClasses = new ByteVector();
        }
        ++this.innerClassesCount;
        this.innerClasses.putShort(name == null ? 0 : this.newClass(name));
        this.innerClasses.putShort(outerName == null ? 0 : this.newClass(outerName));
        this.innerClasses.putShort(innerName == null ? 0 : this.newUTF8(innerName));
        this.innerClasses.putShort(access);
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return new FieldWriter(this, access, name, desc, signature, value);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MethodWriter(this, access, name, desc, signature, exceptions, this.computeMaxs, this.computeFrames);
    }

    public void visitEnd() {
    }

    public byte[] toByteArray() {
        if (this.index > 32767) {
            throw new RuntimeException("Class file too large!");
        }
        int size = 24 + 2 * this.interfaceCount;
        int nbFields = 0;
        FieldWriter fb = this.firstField;
        while (fb != null) {
            ++nbFields;
            size += fb.getSize();
            fb = fb.next;
        }
        int nbMethods = 0;
        MethodWriter mb = this.firstMethod;
        while (mb != null) {
            ++nbMethods;
            size += mb.getSize();
            mb = mb.next;
        }
        int attributeCount = 0;
        if (this.bootstrapMethods != null) {
            ++attributeCount;
            size += 8 + this.bootstrapMethods.length;
            this.newUTF8("BootstrapMethods");
        }
        if (this.signature != 0) {
            ++attributeCount;
            size += 8;
            this.newUTF8("Signature");
        }
        if (this.sourceFile != 0) {
            ++attributeCount;
            size += 8;
            this.newUTF8("SourceFile");
        }
        if (this.sourceDebug != null) {
            ++attributeCount;
            size += this.sourceDebug.length + 4;
            this.newUTF8("SourceDebugExtension");
        }
        if (this.enclosingMethodOwner != 0) {
            ++attributeCount;
            size += 10;
            this.newUTF8("EnclosingMethod");
        }
        if ((this.access & 0x20000) != 0) {
            ++attributeCount;
            size += 6;
            this.newUTF8("Deprecated");
        }
        if ((this.access & 0x1000) != 0 && ((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
            ++attributeCount;
            size += 6;
            this.newUTF8("Synthetic");
        }
        if (this.innerClasses != null) {
            ++attributeCount;
            size += 8 + this.innerClasses.length;
            this.newUTF8("InnerClasses");
        }
        if (this.anns != null) {
            ++attributeCount;
            size += 8 + this.anns.getSize();
            this.newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.ianns != null) {
            ++attributeCount;
            size += 8 + this.ianns.getSize();
            this.newUTF8("RuntimeInvisibleAnnotations");
        }
        if (this.attrs != null) {
            attributeCount += this.attrs.getCount();
            size += this.attrs.getSize(this, null, 0, -1, -1);
        }
        ByteVector out = new ByteVector(size += this.pool.length);
        out.putInt(-889275714).putInt(this.version);
        out.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
        int mask = 0x60000 | (this.access & 0x40000) / 64;
        out.putShort(this.access & ~mask).putShort(this.name).putShort(this.superName);
        out.putShort(this.interfaceCount);
        for (int i2 = 0; i2 < this.interfaceCount; ++i2) {
            out.putShort(this.interfaces[i2]);
        }
        out.putShort(nbFields);
        fb = this.firstField;
        while (fb != null) {
            fb.put(out);
            fb = fb.next;
        }
        out.putShort(nbMethods);
        mb = this.firstMethod;
        while (mb != null) {
            mb.put(out);
            mb = mb.next;
        }
        out.putShort(attributeCount);
        if (this.bootstrapMethods != null) {
            out.putShort(this.newUTF8("BootstrapMethods"));
            out.putInt(this.bootstrapMethods.length + 2).putShort(this.bootstrapMethodsCount);
            out.putByteArray(this.bootstrapMethods.data, 0, this.bootstrapMethods.length);
        }
        if (this.signature != 0) {
            out.putShort(this.newUTF8("Signature")).putInt(2).putShort(this.signature);
        }
        if (this.sourceFile != 0) {
            out.putShort(this.newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
        }
        if (this.sourceDebug != null) {
            int len = this.sourceDebug.length - 2;
            out.putShort(this.newUTF8("SourceDebugExtension")).putInt(len);
            out.putByteArray(this.sourceDebug.data, 2, len);
        }
        if (this.enclosingMethodOwner != 0) {
            out.putShort(this.newUTF8("EnclosingMethod")).putInt(4);
            out.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
        }
        if ((this.access & 0x20000) != 0) {
            out.putShort(this.newUTF8("Deprecated")).putInt(0);
        }
        if ((this.access & 0x1000) != 0 && ((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
            out.putShort(this.newUTF8("Synthetic")).putInt(0);
        }
        if (this.innerClasses != null) {
            out.putShort(this.newUTF8("InnerClasses"));
            out.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
            out.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
        }
        if (this.anns != null) {
            out.putShort(this.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(out);
        }
        if (this.ianns != null) {
            out.putShort(this.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(out);
        }
        if (this.attrs != null) {
            this.attrs.put(this, null, 0, -1, -1, out);
        }
        if (this.invalidFrames) {
            ClassWriter cw = new ClassWriter(2);
            new ClassReader(out.data).accept(cw, 4);
            return cw.toByteArray();
        }
        return out.data;
    }

    Item newConstItem(Object cst) {
        if (cst instanceof Integer) {
            int val2 = (Integer)cst;
            return this.newInteger(val2);
        }
        if (cst instanceof Byte) {
            int val3 = ((Byte)cst).intValue();
            return this.newInteger(val3);
        }
        if (cst instanceof Character) {
            char val4 = ((Character)cst).charValue();
            return this.newInteger(val4);
        }
        if (cst instanceof Short) {
            int val5 = ((Short)cst).intValue();
            return this.newInteger(val5);
        }
        if (cst instanceof Boolean) {
            int val6 = (Boolean)cst != false ? 1 : 0;
            return this.newInteger(val6);
        }
        if (cst instanceof Float) {
            float val7 = ((Float)cst).floatValue();
            return this.newFloat(val7);
        }
        if (cst instanceof Long) {
            long val8 = (Long)cst;
            return this.newLong(val8);
        }
        if (cst instanceof Double) {
            double val9 = (Double)cst;
            return this.newDouble(val9);
        }
        if (cst instanceof String) {
            return this.newString((String)cst);
        }
        if (cst instanceof Type) {
            Type t2 = (Type)cst;
            return this.newClassItem(t2.getSort() == 10 ? t2.getInternalName() : t2.getDescriptor());
        }
        if (cst instanceof MethodType) {
            return this.newMethodTypeItem(((MethodType)cst).desc);
        }
        if (cst instanceof MethodHandle) {
            MethodHandle mHandle = (MethodHandle)cst;
            return this.newMethodHandleItem(mHandle.tag, mHandle.owner, mHandle.name, mHandle.desc);
        }
        throw new IllegalArgumentException("value " + cst);
    }

    public int newConst(Object cst) {
        return this.newConstItem((Object)cst).index;
    }

    public int newUTF8(String value) {
        this.key.set(1, value, null, null);
        Item result = this.get(this.key);
        if (result == null) {
            this.pool.putByte(1).putUTF8(value);
            result = new Item(this.index++, this.key);
            this.put(result);
        }
        return result.index;
    }

    Item newClassItem(String value) {
        this.key2.set(7, value, null, null);
        Item result = this.get(this.key2);
        if (result == null) {
            this.pool.put12(7, this.newUTF8(value));
            result = new Item(this.index++, this.key2);
            this.put(result);
        }
        return result;
    }

    public int newClass(String value) {
        return this.newClassItem((String)value).index;
    }

    Item newMethodTypeItem(String methodDesc) {
        this.key2.set(16, methodDesc, null, null);
        Item result = this.get(this.key2);
        if (result == null) {
            this.pool.put12(16, this.newUTF8(methodDesc));
            result = new Item(this.index++, this.key2);
            this.put(result);
        }
        return result;
    }

    public int newMethodType(String methodDesc) {
        return this.newMethodTypeItem((String)methodDesc).index;
    }

    Item newMethodHandleItem(int tag, String owner, String name, String desc) {
        this.key4.set(20 + tag, owner, name, desc);
        Item result = this.get(this.key4);
        if (result == null) {
            if (tag <= 4) {
                this.put112(15, tag, this.newField(owner, name, desc));
            } else {
                this.put112(15, tag, this.newMethod(owner, name, desc, tag == 9));
            }
            result = new Item(this.index++, this.key4);
            this.put(result);
        }
        return result;
    }

    public int newMethodHandle(int tag, String owner, String name, String desc) {
        return this.newMethodHandleItem((int)tag, (String)owner, (String)name, (String)desc).index;
    }

    Item newInvokeDynamicItem(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        int bootstrapMethodIndex;
        ByteVector bootstrapMethods = this.bootstrapMethods;
        if (bootstrapMethods == null) {
            bootstrapMethods = this.bootstrapMethods = new ByteVector();
        }
        int position = bootstrapMethods.length;
        int hashCode = bsm.hashCode();
        bootstrapMethods.putShort(this.newMethodHandle(bsm.tag, bsm.owner, bsm.name, bsm.desc));
        int argsLength = bsmArgs.length;
        bootstrapMethods.putShort(argsLength);
        for (int i2 = 0; i2 < argsLength; ++i2) {
            Object bsmArg = bsmArgs[i2];
            hashCode ^= bsmArg.hashCode();
            bootstrapMethods.putShort(this.newConst(bsmArg));
        }
        byte[] data = bootstrapMethods.data;
        int length = 2 + argsLength << 1;
        Item result = this.items[(hashCode &= Integer.MAX_VALUE) % this.items.length];
        block1: while (result != null) {
            if (result.type != 33 || result.hashCode != hashCode) {
                result = result.next;
                continue;
            }
            int resultPosition = result.intVal;
            for (int p2 = 0; p2 < length; ++p2) {
                if (data[position + p2] == data[resultPosition + p2]) continue;
                result = result.next;
                continue block1;
            }
        }
        if (result != null) {
            bootstrapMethodIndex = result.index;
            bootstrapMethods.length = position;
        } else {
            bootstrapMethodIndex = this.bootstrapMethodsCount++;
            result = new Item(bootstrapMethodIndex);
            result.set(position, hashCode);
            this.put(result);
        }
        this.key3.set(name, desc, bootstrapMethodIndex);
        result = this.get(this.key3);
        if (result == null) {
            this.put122(18, bootstrapMethodIndex, this.newNameType(name, desc));
            result = new Item(this.index++, this.key3);
            this.put(result);
        }
        return result;
    }

    public int newInvokeDynamic(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        return this.newInvokeDynamicItem((String)name, (String)desc, (MethodHandle)bsm, (Object[])bsmArgs).index;
    }

    Item newFieldItem(String owner, String name, String desc) {
        this.key3.set(9, owner, name, desc);
        Item result = this.get(this.key3);
        if (result == null) {
            this.put122(9, this.newClass(owner), this.newNameType(name, desc));
            result = new Item(this.index++, this.key3);
            this.put(result);
        }
        return result;
    }

    public int newField(String owner, String name, String desc) {
        return this.newFieldItem((String)owner, (String)name, (String)desc).index;
    }

    Item newMethodItem(String owner, String name, String desc, boolean itf) {
        int type = itf ? 11 : 10;
        this.key3.set(type, owner, name, desc);
        Item result = this.get(this.key3);
        if (result == null) {
            this.put122(type, this.newClass(owner), this.newNameType(name, desc));
            result = new Item(this.index++, this.key3);
            this.put(result);
        }
        return result;
    }

    public int newMethod(String owner, String name, String desc, boolean itf) {
        return this.newMethodItem((String)owner, (String)name, (String)desc, (boolean)itf).index;
    }

    Item newInteger(int value) {
        this.key.set(value);
        Item result = this.get(this.key);
        if (result == null) {
            this.pool.putByte(3).putInt(value);
            result = new Item(this.index++, this.key);
            this.put(result);
        }
        return result;
    }

    Item newFloat(float value) {
        this.key.set(value);
        Item result = this.get(this.key);
        if (result == null) {
            this.pool.putByte(4).putInt(this.key.intVal);
            result = new Item(this.index++, this.key);
            this.put(result);
        }
        return result;
    }

    Item newLong(long value) {
        this.key.set(value);
        Item result = this.get(this.key);
        if (result == null) {
            this.pool.putByte(5).putLong(value);
            result = new Item(this.index, this.key);
            this.index += 2;
            this.put(result);
        }
        return result;
    }

    Item newDouble(double value) {
        this.key.set(value);
        Item result = this.get(this.key);
        if (result == null) {
            this.pool.putByte(6).putLong(this.key.longVal);
            result = new Item(this.index, this.key);
            this.index += 2;
            this.put(result);
        }
        return result;
    }

    private Item newString(String value) {
        this.key2.set(8, value, null, null);
        Item result = this.get(this.key2);
        if (result == null) {
            this.pool.put12(8, this.newUTF8(value));
            result = new Item(this.index++, this.key2);
            this.put(result);
        }
        return result;
    }

    public int newNameType(String name, String desc) {
        return this.newNameTypeItem((String)name, (String)desc).index;
    }

    Item newNameTypeItem(String name, String desc) {
        this.key2.set(12, name, desc, null);
        Item result = this.get(this.key2);
        if (result == null) {
            this.put122(12, this.newUTF8(name), this.newUTF8(desc));
            result = new Item(this.index++, this.key2);
            this.put(result);
        }
        return result;
    }

    int addType(String type) {
        this.key.set(30, type, null, null);
        Item result = this.get(this.key);
        if (result == null) {
            result = this.addType(this.key);
        }
        return result.index;
    }

    int addUninitializedType(String type, int offset) {
        this.key.type = 31;
        this.key.intVal = offset;
        this.key.strVal1 = type;
        this.key.hashCode = Integer.MAX_VALUE & 31 + type.hashCode() + offset;
        Item result = this.get(this.key);
        if (result == null) {
            result = this.addType(this.key);
        }
        return result.index;
    }

    private Item addType(Item item) {
        this.typeCount = (short)(this.typeCount + 1);
        Item result = new Item(this.typeCount, this.key);
        this.put(result);
        if (this.typeTable == null) {
            this.typeTable = new Item[16];
        }
        if (this.typeCount == this.typeTable.length) {
            Item[] newTable = new Item[2 * this.typeTable.length];
            System.arraycopy(this.typeTable, 0, newTable, 0, this.typeTable.length);
            this.typeTable = newTable;
        }
        this.typeTable[this.typeCount] = result;
        return result;
    }

    int getMergedType(int type1, int type2) {
        this.key2.type = 32;
        this.key2.longVal = (long)type1 | (long)type2 << 32;
        this.key2.hashCode = Integer.MAX_VALUE & 32 + type1 + type2;
        Item result = this.get(this.key2);
        if (result == null) {
            String t2 = this.typeTable[type1].strVal1;
            String u2 = this.typeTable[type2].strVal1;
            this.key2.intVal = this.addType(this.getCommonSuperClass(t2, u2));
            result = new Item(0, this.key2);
            this.put(result);
        }
        return result.intVal;
    }

    protected String getCommonSuperClass(String type1, String type2) {
        Class<?> d2;
        Class<?> c2;
        try {
            c2 = Class.forName(type1.replace('/', '.'));
            d2 = Class.forName(type2.replace('/', '.'));
        }
        catch (Exception e2) {
            throw new RuntimeException(e2.toString());
        }
        if (c2.isAssignableFrom(d2)) {
            return type1;
        }
        if (d2.isAssignableFrom(c2)) {
            return type2;
        }
        if (c2.isInterface() || d2.isInterface()) {
            return "java/lang/Object";
        }
        while (!(c2 = c2.getSuperclass()).isAssignableFrom(d2)) {
        }
        return c2.getName().replace('.', '/');
    }

    private Item get(Item key) {
        Item i2 = this.items[key.hashCode % this.items.length];
        while (!(i2 == null || i2.type == key.type && key.isEqualTo(i2))) {
            i2 = i2.next;
        }
        return i2;
    }

    private void put(Item i2) {
        if (this.index + this.typeCount > this.threshold) {
            int ll = this.items.length;
            int nl = ll * 2 + 1;
            Item[] newItems = new Item[nl];
            for (int l2 = ll - 1; l2 >= 0; --l2) {
                Item j2 = this.items[l2];
                while (j2 != null) {
                    int index = j2.hashCode % newItems.length;
                    Item k2 = j2.next;
                    j2.next = newItems[index];
                    newItems[index] = j2;
                    j2 = k2;
                }
            }
            this.items = newItems;
            this.threshold = (int)((double)nl * 0.75);
        }
        int index = i2.hashCode % this.items.length;
        i2.next = this.items[index];
        this.items[index] = i2;
    }

    private void put122(int b2, int s1, int s2) {
        this.pool.put12(b2, s1).putShort(s2);
    }

    private void put112(int b1, int b2, int s2) {
        this.pool.put11(b1, b2).putShort(s2);
    }

    static {
        byte[] b2 = new byte[220];
        String s2 = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKJJJJJJJJJJJJJJJJJJ";
        for (int i2 = 0; i2 < b2.length; ++i2) {
            b2[i2] = (byte)(s2.charAt(i2) - 65);
        }
        TYPE = b2;
    }
}

