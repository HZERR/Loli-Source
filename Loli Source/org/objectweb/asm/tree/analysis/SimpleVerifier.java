/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree.analysis;

import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.BasicVerifier;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SimpleVerifier
extends BasicVerifier {
    private final Type currentClass;
    private final Type currentSuperClass;
    private final List<Type> currentClassInterfaces;
    private final boolean isInterface;
    private ClassLoader loader = this.getClass().getClassLoader();

    public SimpleVerifier() {
        this(null, null, false);
    }

    public SimpleVerifier(Type currentClass, Type currentSuperClass, boolean isInterface) {
        this(currentClass, currentSuperClass, null, isInterface);
    }

    public SimpleVerifier(Type currentClass, Type currentSuperClass, List<Type> currentClassInterfaces, boolean isInterface) {
        this.currentClass = currentClass;
        this.currentSuperClass = currentSuperClass;
        this.currentClassInterfaces = currentClassInterfaces;
        this.isInterface = isInterface;
    }

    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public BasicValue newValue(Type type) {
        BasicValue v2;
        boolean isArray;
        if (type == null) {
            return BasicValue.UNINITIALIZED_VALUE;
        }
        boolean bl = isArray = type.getSort() == 9;
        if (isArray) {
            switch (type.getElementType().getSort()) {
                case 1: 
                case 2: 
                case 3: 
                case 4: {
                    return new BasicValue(type);
                }
            }
        }
        if (BasicValue.REFERENCE_VALUE.equals(v2 = super.newValue(type))) {
            if (isArray) {
                v2 = this.newValue(type.getElementType());
                String desc = v2.getType().getDescriptor();
                for (int i2 = 0; i2 < type.getDimensions(); ++i2) {
                    desc = '[' + desc;
                }
                v2 = new BasicValue(Type.getType(desc));
            } else {
                v2 = new BasicValue(type);
            }
        }
        return v2;
    }

    @Override
    protected boolean isArrayValue(BasicValue value) {
        Type t2 = value.getType();
        return t2 != null && ("Lnull;".equals(t2.getDescriptor()) || t2.getSort() == 9);
    }

    @Override
    protected BasicValue getElementValue(BasicValue objectArrayValue) throws AnalyzerException {
        Type arrayType = objectArrayValue.getType();
        if (arrayType != null) {
            if (arrayType.getSort() == 9) {
                return this.newValue(Type.getType(arrayType.getDescriptor().substring(1)));
            }
            if ("Lnull;".equals(arrayType.getDescriptor())) {
                return objectArrayValue;
            }
        }
        throw new Error("Internal error");
    }

    @Override
    protected boolean isSubTypeOf(BasicValue value, BasicValue expected) {
        Type expectedType = expected.getType();
        Type type = value.getType();
        switch (expectedType.getSort()) {
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                return type.equals(expectedType);
            }
            case 9: 
            case 10: {
                if ("Lnull;".equals(type.getDescriptor())) {
                    return true;
                }
                if (type.getSort() == 10 || type.getSort() == 9) {
                    return this.isAssignableFrom(expectedType, type);
                }
                return false;
            }
        }
        throw new Error("Internal error");
    }

    @Override
    public BasicValue merge(BasicValue v2, BasicValue w2) {
        if (!v2.equals(w2)) {
            Type t2 = v2.getType();
            Type u2 = w2.getType();
            if (!(t2 == null || t2.getSort() != 10 && t2.getSort() != 9 || u2 == null || u2.getSort() != 10 && u2.getSort() != 9)) {
                if ("Lnull;".equals(t2.getDescriptor())) {
                    return w2;
                }
                if ("Lnull;".equals(u2.getDescriptor())) {
                    return v2;
                }
                if (this.isAssignableFrom(t2, u2)) {
                    return v2;
                }
                if (this.isAssignableFrom(u2, t2)) {
                    return w2;
                }
                do {
                    if (t2 != null && !this.isInterface(t2)) continue;
                    return BasicValue.REFERENCE_VALUE;
                } while (!this.isAssignableFrom(t2 = this.getSuperClass(t2), u2));
                return this.newValue(t2);
            }
            return BasicValue.UNINITIALIZED_VALUE;
        }
        return v2;
    }

    protected boolean isInterface(Type t2) {
        if (this.currentClass != null && t2.equals(this.currentClass)) {
            return this.isInterface;
        }
        return this.getClass(t2).isInterface();
    }

    protected Type getSuperClass(Type t2) {
        if (this.currentClass != null && t2.equals(this.currentClass)) {
            return this.currentSuperClass;
        }
        Class<?> c2 = this.getClass(t2).getSuperclass();
        return c2 == null ? null : Type.getType(c2);
    }

    protected boolean isAssignableFrom(Type t2, Type u2) {
        if (t2.equals(u2)) {
            return true;
        }
        if (this.currentClass != null && t2.equals(this.currentClass)) {
            if (this.getSuperClass(u2) == null) {
                return false;
            }
            if (this.isInterface) {
                return u2.getSort() == 10 || u2.getSort() == 9;
            }
            return this.isAssignableFrom(t2, this.getSuperClass(u2));
        }
        if (this.currentClass != null && u2.equals(this.currentClass)) {
            if (this.isAssignableFrom(t2, this.currentSuperClass)) {
                return true;
            }
            if (this.currentClassInterfaces != null) {
                for (int i2 = 0; i2 < this.currentClassInterfaces.size(); ++i2) {
                    Type v2 = this.currentClassInterfaces.get(i2);
                    if (!this.isAssignableFrom(t2, v2)) continue;
                    return true;
                }
            }
            return false;
        }
        Class<Object> tc = this.getClass(t2);
        if (tc.isInterface()) {
            tc = Object.class;
        }
        return tc.isAssignableFrom(this.getClass(u2));
    }

    protected Class<?> getClass(Type t2) {
        try {
            if (t2.getSort() == 9) {
                return Class.forName(t2.getDescriptor().replace('/', '.'), false, this.loader);
            }
            return Class.forName(t2.getClassName(), false, this.loader);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException(e2.toString());
        }
    }
}

