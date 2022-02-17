/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Type {
    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final Type VOID_TYPE = new Type(0, null, 0x56050000, 1);
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    private final int sort;
    private final char[] buf;
    private final int off;
    private final int len;

    private Type(int sort, char[] buf, int off, int len) {
        this.sort = sort;
        this.buf = buf;
        this.off = off;
        this.len = len;
    }

    public static Type getType(String typeDescriptor) {
        return Type.getType(typeDescriptor.toCharArray(), 0);
    }

    public static Type getObjectType(String internalName) {
        char[] buf = internalName.toCharArray();
        return new Type(buf[0] == '[' ? 9 : 10, buf, 0, buf.length);
    }

    public static Type getType(Class<?> c2) {
        if (c2.isPrimitive()) {
            if (c2 == Integer.TYPE) {
                return INT_TYPE;
            }
            if (c2 == Void.TYPE) {
                return VOID_TYPE;
            }
            if (c2 == Boolean.TYPE) {
                return BOOLEAN_TYPE;
            }
            if (c2 == Byte.TYPE) {
                return BYTE_TYPE;
            }
            if (c2 == Character.TYPE) {
                return CHAR_TYPE;
            }
            if (c2 == Short.TYPE) {
                return SHORT_TYPE;
            }
            if (c2 == Double.TYPE) {
                return DOUBLE_TYPE;
            }
            if (c2 == Float.TYPE) {
                return FLOAT_TYPE;
            }
            return LONG_TYPE;
        }
        return Type.getType(Type.getDescriptor(c2));
    }

    public static Type[] getArgumentTypes(String methodDescriptor) {
        char car;
        char[] buf = methodDescriptor.toCharArray();
        int off = 1;
        int size = 0;
        while ((car = buf[off++]) != ')') {
            if (car == 'L') {
                while (buf[off++] != ';') {
                }
                ++size;
                continue;
            }
            if (car == '[') continue;
            ++size;
        }
        Type[] args = new Type[size];
        off = 1;
        size = 0;
        while (buf[off] != ')') {
            args[size] = Type.getType(buf, off);
            off += args[size].len + (args[size].sort == 10 ? 2 : 0);
            ++size;
        }
        return args;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class<?>[] classes = method.getParameterTypes();
        Type[] types = new Type[classes.length];
        for (int i2 = classes.length - 1; i2 >= 0; --i2) {
            types[i2] = Type.getType(classes[i2]);
        }
        return types;
    }

    public static Type getReturnType(String methodDescriptor) {
        char[] buf = methodDescriptor.toCharArray();
        return Type.getType(buf, methodDescriptor.indexOf(41) + 1);
    }

    public static Type getReturnType(Method method) {
        return Type.getType(method.getReturnType());
    }

    public static int getArgumentsAndReturnSizes(String desc) {
        int n2 = 1;
        int c2 = 1;
        while (true) {
            char car;
            if ((car = desc.charAt(c2++)) == ')') {
                car = desc.charAt(c2);
                return n2 << 2 | (car == 'V' ? 0 : (car == 'D' || car == 'J' ? 2 : 1));
            }
            if (car == 'L') {
                while (desc.charAt(c2++) != ';') {
                }
                ++n2;
                continue;
            }
            if (car == '[') {
                while ((car = desc.charAt(c2)) == '[') {
                    ++c2;
                }
                if (car != 'D' && car != 'J') continue;
                --n2;
                continue;
            }
            if (car == 'D' || car == 'J') {
                n2 += 2;
                continue;
            }
            ++n2;
        }
    }

    private static Type getType(char[] buf, int off) {
        switch (buf[off]) {
            case 'V': {
                return VOID_TYPE;
            }
            case 'Z': {
                return BOOLEAN_TYPE;
            }
            case 'C': {
                return CHAR_TYPE;
            }
            case 'B': {
                return BYTE_TYPE;
            }
            case 'S': {
                return SHORT_TYPE;
            }
            case 'I': {
                return INT_TYPE;
            }
            case 'F': {
                return FLOAT_TYPE;
            }
            case 'J': {
                return LONG_TYPE;
            }
            case 'D': {
                return DOUBLE_TYPE;
            }
            case '[': {
                int len = 1;
                while (buf[off + len] == '[') {
                    ++len;
                }
                if (buf[off + len] == 'L') {
                    ++len;
                    while (buf[off + len] != ';') {
                        ++len;
                    }
                }
                return new Type(9, buf, off, len + 1);
            }
        }
        int len = 1;
        while (buf[off + len] != ';') {
            ++len;
        }
        return new Type(10, buf, off + 1, len - 1);
    }

    public int getSort() {
        return this.sort;
    }

    public int getDimensions() {
        int i2 = 1;
        while (this.buf[this.off + i2] == '[') {
            ++i2;
        }
        return i2;
    }

    public Type getElementType() {
        return Type.getType(this.buf, this.off + this.getDimensions());
    }

    public String getClassName() {
        switch (this.sort) {
            case 0: {
                return "void";
            }
            case 1: {
                return "boolean";
            }
            case 2: {
                return "char";
            }
            case 3: {
                return "byte";
            }
            case 4: {
                return "short";
            }
            case 5: {
                return "int";
            }
            case 6: {
                return "float";
            }
            case 7: {
                return "long";
            }
            case 8: {
                return "double";
            }
            case 9: {
                StringBuffer b2 = new StringBuffer(this.getElementType().getClassName());
                for (int i2 = this.getDimensions(); i2 > 0; --i2) {
                    b2.append("[]");
                }
                return b2.toString();
            }
        }
        return new String(this.buf, this.off, this.len).replace('/', '.');
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }

    public String getDescriptor() {
        StringBuffer buf = new StringBuffer();
        this.getDescriptor(buf);
        return buf.toString();
    }

    public static String getMethodDescriptor(Type returnType, Type ... argumentTypes) {
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i2 = 0; i2 < argumentTypes.length; ++i2) {
            argumentTypes[i2].getDescriptor(buf);
        }
        buf.append(')');
        returnType.getDescriptor(buf);
        return buf.toString();
    }

    private void getDescriptor(StringBuffer buf) {
        if (this.buf == null) {
            buf.append((char)((this.off & 0xFF000000) >>> 24));
        } else if (this.sort == 9) {
            buf.append(this.buf, this.off, this.len);
        } else {
            buf.append('L');
            buf.append(this.buf, this.off, this.len);
            buf.append(';');
        }
    }

    public static String getInternalName(Class<?> c2) {
        return c2.getName().replace('.', '/');
    }

    public static String getDescriptor(Class<?> c2) {
        StringBuffer buf = new StringBuffer();
        Type.getDescriptor(buf, c2);
        return buf.toString();
    }

    public static String getConstructorDescriptor(Constructor<?> c2) {
        Class<?>[] parameters = c2.getParameterTypes();
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i2 = 0; i2 < parameters.length; ++i2) {
            Type.getDescriptor(buf, parameters[i2]);
        }
        return buf.append(")V").toString();
    }

    public static String getMethodDescriptor(Method m2) {
        Class<?>[] parameters = m2.getParameterTypes();
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i2 = 0; i2 < parameters.length; ++i2) {
            Type.getDescriptor(buf, parameters[i2]);
        }
        buf.append(')');
        Type.getDescriptor(buf, m2.getReturnType());
        return buf.toString();
    }

    private static void getDescriptor(StringBuffer buf, Class<?> c2) {
        Class<?> d2 = c2;
        while (true) {
            if (d2.isPrimitive()) {
                int car = d2 == Integer.TYPE ? 73 : (d2 == Void.TYPE ? 86 : (d2 == Boolean.TYPE ? 90 : (d2 == Byte.TYPE ? 66 : (d2 == Character.TYPE ? 67 : (d2 == Short.TYPE ? 83 : (d2 == Double.TYPE ? 68 : (d2 == Float.TYPE ? 70 : 74)))))));
                buf.append((char)car);
                return;
            }
            if (!d2.isArray()) break;
            buf.append('[');
            d2 = d2.getComponentType();
        }
        buf.append('L');
        String name = d2.getName();
        int len = name.length();
        for (int i2 = 0; i2 < len; ++i2) {
            char car = name.charAt(i2);
            buf.append(car == '.' ? (char)'/' : (char)car);
        }
        buf.append(';');
    }

    public int getSize() {
        return this.buf == null ? this.off & 0xFF : 1;
    }

    public int getOpcode(int opcode) {
        if (opcode == 46 || opcode == 79) {
            return opcode + (this.buf == null ? (this.off & 0xFF00) >> 8 : 4);
        }
        return opcode + (this.buf == null ? (this.off & 0xFF0000) >> 16 : 4);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (!(o2 instanceof Type)) {
            return false;
        }
        Type t2 = (Type)o2;
        if (this.sort != t2.sort) {
            return false;
        }
        if (this.sort == 10 || this.sort == 9) {
            if (this.len != t2.len) {
                return false;
            }
            int i2 = this.off;
            int j2 = t2.off;
            int end = i2 + this.len;
            while (i2 < end) {
                if (this.buf[i2] != t2.buf[j2]) {
                    return false;
                }
                ++i2;
                ++j2;
            }
        }
        return true;
    }

    public int hashCode() {
        int hc = 13 * this.sort;
        if (this.sort == 10 || this.sort == 9) {
            int i2;
            int end = i2 + this.len;
            for (i2 = this.off; i2 < end; ++i2) {
                hc = 17 * (hc + this.buf[i2]);
            }
        }
        return hc;
    }

    public String toString() {
        return this.getDescriptor();
    }
}

