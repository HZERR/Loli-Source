/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Method {
    private final String name;
    private final String desc;
    private static final Map<String, String> DESCRIPTORS = new HashMap<String, String>();

    public Method(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Method(String name, Type returnType, Type[] argumentTypes) {
        this(name, Type.getMethodDescriptor(returnType, argumentTypes));
    }

    public static Method getMethod(java.lang.reflect.Method m2) {
        return new Method(m2.getName(), Type.getMethodDescriptor(m2));
    }

    public static Method getMethod(Constructor<?> c2) {
        return new Method("<init>", Type.getConstructorDescriptor(c2));
    }

    public static Method getMethod(String method) throws IllegalArgumentException {
        return Method.getMethod(method, false);
    }

    public static Method getMethod(String method, boolean defaultPackage) throws IllegalArgumentException {
        int p2;
        int space = method.indexOf(32);
        int start = method.indexOf(40, space) + 1;
        int end = method.indexOf(41, start);
        if (space == -1 || start == -1 || end == -1) {
            throw new IllegalArgumentException();
        }
        String returnType = method.substring(0, space);
        String methodName = method.substring(space + 1, start - 1).trim();
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        do {
            String s2;
            if ((p2 = method.indexOf(44, start)) == -1) {
                s2 = Method.map(method.substring(start, end).trim(), defaultPackage);
            } else {
                s2 = Method.map(method.substring(start, p2).trim(), defaultPackage);
                start = p2 + 1;
            }
            sb.append(s2);
        } while (p2 != -1);
        sb.append(')');
        sb.append(Method.map(returnType, defaultPackage));
        return new Method(methodName, sb.toString());
    }

    private static String map(String type, boolean defaultPackage) {
        if ("".equals(type)) {
            return type;
        }
        StringBuffer sb = new StringBuffer();
        int index = 0;
        while ((index = type.indexOf("[]", index) + 1) > 0) {
            sb.append('[');
        }
        String t2 = type.substring(0, type.length() - sb.length() * 2);
        String desc = DESCRIPTORS.get(t2);
        if (desc != null) {
            sb.append(desc);
        } else {
            sb.append('L');
            if (t2.indexOf(46) < 0) {
                if (!defaultPackage) {
                    sb.append("java/lang/");
                }
                sb.append(t2);
            } else {
                sb.append(t2.replace('.', '/'));
            }
            sb.append(';');
        }
        return sb.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getDescriptor() {
        return this.desc;
    }

    public Type getReturnType() {
        return Type.getReturnType(this.desc);
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(this.desc);
    }

    public String toString() {
        return this.name + this.desc;
    }

    public boolean equals(Object o2) {
        if (!(o2 instanceof Method)) {
            return false;
        }
        Method other = (Method)o2;
        return this.name.equals(other.name) && this.desc.equals(other.desc);
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.desc.hashCode();
    }

    static {
        DESCRIPTORS.put("void", "V");
        DESCRIPTORS.put("byte", "B");
        DESCRIPTORS.put("char", "C");
        DESCRIPTORS.put("double", "D");
        DESCRIPTORS.put("float", "F");
        DESCRIPTORS.put("int", "I");
        DESCRIPTORS.put("long", "J");
        DESCRIPTORS.put("short", "S");
        DESCRIPTORS.put("boolean", "Z");
    }
}

