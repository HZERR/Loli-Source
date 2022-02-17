/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SerialVersionUIDAdder
extends ClassAdapter {
    private boolean computeSVUID;
    private boolean hasSVUID;
    private int access;
    private String name;
    private String[] interfaces;
    private Collection<Item> svuidFields = new ArrayList<Item>();
    private boolean hasStaticInitializer;
    private Collection<Item> svuidConstructors = new ArrayList<Item>();
    private Collection<Item> svuidMethods = new ArrayList<Item>();

    public SerialVersionUIDAdder(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        boolean bl = this.computeSVUID = (access & 0x200) == 0;
        if (this.computeSVUID) {
            this.name = name;
            this.access = access;
            this.interfaces = interfaces;
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (this.computeSVUID) {
            if ("<clinit>".equals(name)) {
                this.hasStaticInitializer = true;
            }
            int mods = access & 0xD3F;
            if ((access & 2) == 0) {
                if ("<init>".equals(name)) {
                    this.svuidConstructors.add(new Item(name, mods, desc));
                } else if (!"<clinit>".equals(name)) {
                    this.svuidMethods.add(new Item(name, mods, desc));
                }
            }
        }
        return this.cv.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (this.computeSVUID) {
            if ("serialVersionUID".equals(name)) {
                this.computeSVUID = false;
                this.hasSVUID = true;
            }
            if ((access & 2) == 0 || (access & 0x88) == 0) {
                int mods = access & 0xDF;
                this.svuidFields.add(new Item(name, mods, desc));
            }
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitInnerClass(String aname, String outerName, String innerName, int attr_access) {
        if (this.name != null && this.name.equals(aname)) {
            this.access = attr_access;
        }
        super.visitInnerClass(aname, outerName, innerName, attr_access);
    }

    @Override
    public void visitEnd() {
        if (this.computeSVUID && !this.hasSVUID) {
            try {
                this.cv.visitField(24, "serialVersionUID", "J", null, new Long(this.computeSVUID()));
            }
            catch (Throwable e2) {
                throw new RuntimeException("Error while computing SVUID for " + this.name, e2);
            }
        }
        super.visitEnd();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected long computeSVUID() throws IOException {
        FilterOutputStream dos = null;
        long svuid = 0L;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            ((DataOutputStream)dos).writeUTF(this.name.replace('/', '.'));
            ((DataOutputStream)dos).writeInt(this.access & 0x611);
            Arrays.sort(this.interfaces);
            for (int i2 = 0; i2 < this.interfaces.length; ++i2) {
                ((DataOutputStream)dos).writeUTF(this.interfaces[i2].replace('/', '.'));
            }
            SerialVersionUIDAdder.writeItems(this.svuidFields, (DataOutput)((Object)dos), false);
            if (this.hasStaticInitializer) {
                ((DataOutputStream)dos).writeUTF("<clinit>");
                ((DataOutputStream)dos).writeInt(8);
                ((DataOutputStream)dos).writeUTF("()V");
            }
            SerialVersionUIDAdder.writeItems(this.svuidConstructors, (DataOutput)((Object)dos), true);
            SerialVersionUIDAdder.writeItems(this.svuidMethods, (DataOutput)((Object)dos), true);
            ((DataOutputStream)dos).flush();
            byte[] hashBytes = this.computeSHAdigest(bos.toByteArray());
            for (int i3 = Math.min(hashBytes.length, 8) - 1; i3 >= 0; --i3) {
                svuid = svuid << 8 | (long)(hashBytes[i3] & 0xFF);
            }
        }
        finally {
            if (dos != null) {
                dos.close();
            }
        }
        return svuid;
    }

    protected byte[] computeSHAdigest(byte[] value) {
        try {
            return MessageDigest.getInstance("SHA").digest(value);
        }
        catch (Exception e2) {
            throw new UnsupportedOperationException(e2.toString());
        }
    }

    private static void writeItems(Collection<Item> itemCollection, DataOutput dos, boolean dotted) throws IOException {
        int size = itemCollection.size();
        Object[] items = itemCollection.toArray(new Item[size]);
        Arrays.sort(items);
        for (int i2 = 0; i2 < size; ++i2) {
            dos.writeUTF(((Item)items[i2]).name);
            dos.writeInt(((Item)items[i2]).access);
            dos.writeUTF(dotted ? ((Item)items[i2]).desc.replace('/', '.') : ((Item)items[i2]).desc);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class Item
    implements Comparable<Item> {
        final String name;
        final int access;
        final String desc;

        Item(String name, int access, String desc) {
            this.name = name;
            this.access = access;
            this.desc = desc;
        }

        @Override
        public int compareTo(Item other) {
            int retVal = this.name.compareTo(other.name);
            if (retVal == 0) {
                retVal = this.desc.compareTo(other.desc);
            }
            return retVal;
        }
    }
}

