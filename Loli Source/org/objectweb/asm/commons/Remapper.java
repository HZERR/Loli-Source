/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodType;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.RemappingSignatureAdapter;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

public abstract class Remapper {
    public String mapDesc(String desc) {
        Type t2 = Type.getType(desc);
        switch (t2.getSort()) {
            case 9: {
                String s2 = this.mapDesc(t2.getElementType().getDescriptor());
                for (int i2 = 0; i2 < t2.getDimensions(); ++i2) {
                    s2 = '[' + s2;
                }
                return s2;
            }
            case 10: {
                String newType = this.map(t2.getInternalName());
                if (newType == null) break;
                return 'L' + newType + ';';
            }
        }
        return desc;
    }

    private Type mapType(Type t2) {
        switch (t2.getSort()) {
            case 9: {
                String s2 = this.mapDesc(t2.getElementType().getDescriptor());
                for (int i2 = 0; i2 < t2.getDimensions(); ++i2) {
                    s2 = '[' + s2;
                }
                return Type.getType(s2);
            }
            case 10: {
                String s2 = this.map(t2.getInternalName());
                if (s2 == null) break;
                return Type.getObjectType(s2);
            }
        }
        return t2;
    }

    public String mapType(String type) {
        if (type == null) {
            return null;
        }
        return this.mapType(Type.getObjectType(type)).getInternalName();
    }

    public String[] mapTypes(String[] types) {
        String[] newTypes = null;
        boolean needMapping = false;
        for (int i2 = 0; i2 < types.length; ++i2) {
            String type = types[i2];
            String newType = this.map(type);
            if (newType != null && newTypes == null) {
                newTypes = new String[types.length];
                if (i2 > 0) {
                    System.arraycopy(types, 0, newTypes, 0, i2);
                }
                needMapping = true;
            }
            if (!needMapping) continue;
            newTypes[i2] = newType == null ? type : newType;
        }
        return needMapping ? newTypes : types;
    }

    public String mapMethodDesc(String desc) {
        if ("()V".equals(desc)) {
            return desc;
        }
        Type[] args = Type.getArgumentTypes(desc);
        String s2 = "(";
        for (int i2 = 0; i2 < args.length; ++i2) {
            s2 = s2 + this.mapDesc(args[i2].getDescriptor());
        }
        Type returnType = Type.getReturnType(desc);
        if (returnType == Type.VOID_TYPE) {
            return s2 + ")V";
        }
        return s2 + ')' + this.mapDesc(returnType.getDescriptor());
    }

    public Object mapValue(Object value) {
        if (value instanceof Type) {
            return this.mapType((Type)value);
        }
        if (value instanceof MethodType) {
            return new MethodType(this.mapMethodDesc(((MethodType)value).getDescriptor()));
        }
        if (value instanceof MethodHandle) {
            MethodHandle mHandle = (MethodHandle)value;
            return new MethodHandle(mHandle.getTag(), this.mapType(mHandle.getOwner()), this.mapMethodName(mHandle.getOwner(), mHandle.getName(), mHandle.getDesc()), this.mapMethodDesc(mHandle.getDesc()));
        }
        return value;
    }

    public String mapSignature(String signature, boolean typeSignature) {
        if (signature == null) {
            return null;
        }
        SignatureReader r2 = new SignatureReader(signature);
        SignatureWriter w2 = new SignatureWriter();
        SignatureVisitor a2 = this.createRemappingSignatureAdapter(w2);
        if (typeSignature) {
            r2.acceptType(a2);
        } else {
            r2.accept(a2);
        }
        return w2.toString();
    }

    protected SignatureVisitor createRemappingSignatureAdapter(SignatureVisitor v2) {
        return new RemappingSignatureAdapter(v2, this);
    }

    public String mapMethodName(String owner, String name, String desc) {
        return name;
    }

    public String mapInvokeDynamicMethodName(String name, String desc) {
        return name;
    }

    public String mapFieldName(String owner, String name, String desc) {
        return name;
    }

    public String map(String typeName) {
        return typeName;
    }
}

