/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class LocalVariablesSorter
extends MethodAdapter {
    private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");
    private int[] mapping = new int[40];
    private Object[] newLocals = new Object[20];
    protected final int firstLocal;
    protected int nextLocal;
    private boolean changed;

    public LocalVariablesSorter(int access, String desc, MethodVisitor mv) {
        super(mv);
        Type[] args = Type.getArgumentTypes(desc);
        this.nextLocal = (8 & access) == 0 ? 1 : 0;
        for (int i2 = 0; i2 < args.length; ++i2) {
            this.nextLocal += args[i2].getSize();
        }
        this.firstLocal = this.nextLocal;
    }

    public void visitVarInsn(int opcode, int var2) {
        Type type;
        switch (opcode) {
            case 22: 
            case 55: {
                type = Type.LONG_TYPE;
                break;
            }
            case 24: 
            case 57: {
                type = Type.DOUBLE_TYPE;
                break;
            }
            case 23: 
            case 56: {
                type = Type.FLOAT_TYPE;
                break;
            }
            case 21: 
            case 54: {
                type = Type.INT_TYPE;
                break;
            }
            default: {
                type = OBJECT_TYPE;
            }
        }
        this.mv.visitVarInsn(opcode, this.remap(var2, type));
    }

    public void visitIincInsn(int var2, int increment) {
        this.mv.visitIincInsn(this.remap(var2, Type.INT_TYPE), increment);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        this.mv.visitMaxs(maxStack, this.nextLocal);
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        int newIndex = this.remap(index, Type.getType(desc));
        this.mv.visitLocalVariable(name, desc, signature, start, end, newIndex);
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        int number;
        if (type != -1) {
            throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
        }
        if (!this.changed) {
            this.mv.visitFrame(type, nLocal, local, nStack, stack);
            return;
        }
        Object[] oldLocals = new Object[this.newLocals.length];
        System.arraycopy(this.newLocals, 0, oldLocals, 0, oldLocals.length);
        int index = 0;
        for (number = 0; number < nLocal; ++number) {
            int size;
            Object t2 = local[number];
            int n2 = size = t2 == Opcodes.LONG || t2 == Opcodes.DOUBLE ? 2 : 1;
            if (t2 != Opcodes.TOP) {
                Type typ = OBJECT_TYPE;
                if (t2 == Opcodes.INTEGER) {
                    typ = Type.INT_TYPE;
                } else if (t2 == Opcodes.FLOAT) {
                    typ = Type.FLOAT_TYPE;
                } else if (t2 == Opcodes.LONG) {
                    typ = Type.LONG_TYPE;
                } else if (t2 == Opcodes.DOUBLE) {
                    typ = Type.DOUBLE_TYPE;
                } else if (t2 instanceof String) {
                    typ = Type.getObjectType((String)t2);
                }
                this.setFrameLocal(this.remap(index, typ), t2);
            }
            index += size;
        }
        index = 0;
        number = 0;
        int i2 = 0;
        while (index < this.newLocals.length) {
            Object t3;
            if ((t3 = this.newLocals[index++]) != null && t3 != Opcodes.TOP) {
                this.newLocals[i2] = t3;
                number = i2 + 1;
                if (t3 == Opcodes.LONG || t3 == Opcodes.DOUBLE) {
                    ++index;
                }
            } else {
                this.newLocals[i2] = Opcodes.TOP;
            }
            ++i2;
        }
        this.mv.visitFrame(type, number, this.newLocals, nStack, stack);
        this.newLocals = oldLocals;
    }

    public int newLocal(Type type) {
        Object t2;
        switch (type.getSort()) {
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                t2 = Opcodes.INTEGER;
                break;
            }
            case 6: {
                t2 = Opcodes.FLOAT;
                break;
            }
            case 7: {
                t2 = Opcodes.LONG;
                break;
            }
            case 8: {
                t2 = Opcodes.DOUBLE;
                break;
            }
            case 9: {
                t2 = type.getDescriptor();
                break;
            }
            default: {
                t2 = type.getInternalName();
            }
        }
        int local = this.nextLocal;
        this.nextLocal += type.getSize();
        this.setLocalType(local, type);
        this.setFrameLocal(local, t2);
        return local;
    }

    protected void setLocalType(int local, Type type) {
    }

    private void setFrameLocal(int local, Object type) {
        int l2 = this.newLocals.length;
        if (local >= l2) {
            Object[] a2 = new Object[Math.max(2 * l2, local + 1)];
            System.arraycopy(this.newLocals, 0, a2, 0, l2);
            this.newLocals = a2;
        }
        this.newLocals[local] = type;
    }

    private int remap(int var2, Type type) {
        int value;
        int size;
        if (var2 + type.getSize() <= this.firstLocal) {
            return var2;
        }
        int key = 2 * var2 + type.getSize() - 1;
        if (key >= (size = this.mapping.length)) {
            int[] newMapping = new int[Math.max(2 * size, key + 1)];
            System.arraycopy(this.mapping, 0, newMapping, 0, size);
            this.mapping = newMapping;
        }
        if ((value = this.mapping[key]) == 0) {
            value = this.newLocalMapping(type);
            this.setLocalType(value, type);
            this.mapping[key] = value + 1;
        } else {
            --value;
        }
        if (value != var2) {
            this.changed = true;
        }
        return value;
    }

    protected int newLocalMapping(Type type) {
        int local = this.nextLocal;
        this.nextLocal += type.getSize();
        return local;
    }
}

