/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

public final class MethodHandle {
    final int tag;
    final String owner;
    final String name;
    final String desc;

    public MethodHandle(int tag, String owner, String name, String desc) {
        this.tag = tag;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public int getTag() {
        return this.tag;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MethodHandle)) {
            return false;
        }
        MethodHandle mHandle = (MethodHandle)obj;
        return this.tag == mHandle.tag && this.owner.equals(mHandle.owner) && this.name.equals(mHandle.name) && this.desc.equals(mHandle.desc);
    }

    public int hashCode() {
        return this.tag + this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode();
    }

    public String toString() {
        return this.owner + '.' + this.name + this.desc + " (" + this.tag + ')';
    }
}

