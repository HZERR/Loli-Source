/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.objectweb.asm.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MethodType {
    final String desc;

    public MethodType(String desc) {
        this.desc = desc;
    }

    public MethodType(Type returnType, Type[] argumentTypes) {
        this.desc = Type.getMethodDescriptor(returnType, argumentTypes);
    }

    public MethodType(Constructor<?> c2) {
        this.desc = Type.getConstructorDescriptor(c2);
    }

    public MethodType(Method m2) {
        this.desc = Type.getMethodDescriptor(m2);
    }

    public String getDescriptor() {
        return this.desc;
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(this.desc);
    }

    public Type getReturnType() {
        return Type.getReturnType(this.desc);
    }

    public int getArgumentsAndReturnSizes() {
        return Type.getArgumentsAndReturnSizes(this.desc);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MethodType)) {
            return false;
        }
        return this.desc.equals(((MethodType)obj).desc);
    }

    public int hashCode() {
        return ~this.desc.hashCode();
    }

    public String toString() {
        return this.desc;
    }
}

