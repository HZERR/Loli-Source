/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.AnnotationWriter;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Edge;
import org.objectweb.asm.Frame;
import org.objectweb.asm.Handler;
import org.objectweb.asm.Item;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

class MethodWriter
implements MethodVisitor {
    static final int ACC_CONSTRUCTOR = 262144;
    static final int SAME_FRAME = 0;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
    static final int RESERVED = 128;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
    static final int CHOP_FRAME = 248;
    static final int SAME_FRAME_EXTENDED = 251;
    static final int APPEND_FRAME = 252;
    static final int FULL_FRAME = 255;
    private static final int FRAMES = 0;
    private static final int MAXS = 1;
    private static final int NOTHING = 2;
    MethodWriter next;
    final ClassWriter cw;
    private int access;
    private final int name;
    private final int desc;
    private final String descriptor;
    String signature;
    int classReaderOffset;
    int classReaderLength;
    int exceptionCount;
    int[] exceptions;
    private ByteVector annd;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter[] panns;
    private AnnotationWriter[] ipanns;
    private int synthetics;
    private Attribute attrs;
    private ByteVector code = new ByteVector();
    private int maxStack;
    private int maxLocals;
    private int currentLocals;
    private int frameCount;
    private ByteVector stackMap;
    private int previousFrameOffset;
    private int[] previousFrame;
    private int frameIndex;
    private int[] frame;
    private int handlerCount;
    private Handler firstHandler;
    private Handler lastHandler;
    private int localVarCount;
    private ByteVector localVar;
    private int localVarTypeCount;
    private ByteVector localVarType;
    private int lineNumberCount;
    private ByteVector lineNumber;
    private Attribute cattrs;
    private boolean resize;
    private int subroutines;
    private final int compute;
    private Label labels;
    private Label previousBlock;
    private Label currentBlock;
    private int stackSize;
    private int maxStackSize;

    MethodWriter(ClassWriter cw, int access, String name, String desc, String signature, String[] exceptions, boolean computeMaxs, boolean computeFrames) {
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
        } else {
            cw.lastMethod.next = this;
        }
        cw.lastMethod = this;
        this.cw = cw;
        this.access = access;
        this.name = cw.newUTF8(name);
        this.desc = cw.newUTF8(desc);
        this.descriptor = desc;
        this.signature = signature;
        if (exceptions != null && exceptions.length > 0) {
            this.exceptionCount = exceptions.length;
            this.exceptions = new int[this.exceptionCount];
            for (int i2 = 0; i2 < this.exceptionCount; ++i2) {
                this.exceptions[i2] = cw.newClass(exceptions[i2]);
            }
        }
        int n2 = computeFrames ? 0 : (this.compute = computeMaxs ? 1 : 2);
        if (computeMaxs || computeFrames) {
            if (computeFrames && "<init>".equals(name)) {
                this.access |= 0x40000;
            }
            int size = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
            if ((access & 8) != 0) {
                --size;
            }
            this.maxLocals = size;
            this.currentLocals = size;
            this.labels = new Label();
            this.labels.status |= 8;
            this.visitLabel(this.labels);
        }
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.annd = new ByteVector();
        return new AnnotationWriter(this.cw, false, this.annd, null, 0);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        ByteVector bv = new ByteVector();
        bv.putShort(this.cw.newUTF8(desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
        if (visible) {
            aw.next = this.anns;
            this.anns = aw;
        } else {
            aw.next = this.ianns;
            this.ianns = aw;
        }
        return aw;
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        ByteVector bv = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(desc)) {
            this.synthetics = Math.max(this.synthetics, parameter + 1);
            return new AnnotationWriter(this.cw, false, bv, null, 0);
        }
        bv.putShort(this.cw.newUTF8(desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
        if (visible) {
            if (this.panns == null) {
                this.panns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            aw.next = this.panns[parameter];
            this.panns[parameter] = aw;
        } else {
            if (this.ipanns == null) {
                this.ipanns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            aw.next = this.ipanns[parameter];
            this.ipanns[parameter] = aw;
        }
        return aw;
    }

    public void visitAttribute(Attribute attr) {
        if (attr.isCodeAttribute()) {
            attr.next = this.cattrs;
            this.cattrs = attr;
        } else {
            attr.next = this.attrs;
            this.attrs = attr;
        }
    }

    public void visitCode() {
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        if (this.compute == 0) {
            return;
        }
        if (type == -1) {
            int i2;
            this.currentLocals = nLocal;
            this.startFrame(this.code.length, nLocal, nStack);
            for (i2 = 0; i2 < nLocal; ++i2) {
                this.frame[this.frameIndex++] = local[i2] instanceof String ? 0x1700000 | this.cw.addType((String)local[i2]) : (local[i2] instanceof Integer ? (Integer)local[i2] : 0x1800000 | this.cw.addUninitializedType("", ((Label)local[i2]).position));
            }
            for (i2 = 0; i2 < nStack; ++i2) {
                this.frame[this.frameIndex++] = stack[i2] instanceof String ? 0x1700000 | this.cw.addType((String)stack[i2]) : (stack[i2] instanceof Integer ? (Integer)stack[i2] : 0x1800000 | this.cw.addUninitializedType("", ((Label)stack[i2]).position));
            }
            this.endFrame();
        } else {
            int delta;
            if (this.stackMap == null) {
                this.stackMap = new ByteVector();
                delta = this.code.length;
            } else {
                delta = this.code.length - this.previousFrameOffset - 1;
                if (delta < 0) {
                    if (type == 3) {
                        return;
                    }
                    throw new IllegalStateException();
                }
            }
            switch (type) {
                case 0: {
                    int i3;
                    this.currentLocals = nLocal;
                    this.stackMap.putByte(255).putShort(delta).putShort(nLocal);
                    for (i3 = 0; i3 < nLocal; ++i3) {
                        this.writeFrameType(local[i3]);
                    }
                    this.stackMap.putShort(nStack);
                    for (i3 = 0; i3 < nStack; ++i3) {
                        this.writeFrameType(stack[i3]);
                    }
                    break;
                }
                case 1: {
                    this.currentLocals += nLocal;
                    this.stackMap.putByte(251 + nLocal).putShort(delta);
                    for (int i4 = 0; i4 < nLocal; ++i4) {
                        this.writeFrameType(local[i4]);
                    }
                    break;
                }
                case 2: {
                    this.currentLocals -= nLocal;
                    this.stackMap.putByte(251 - nLocal).putShort(delta);
                    break;
                }
                case 3: {
                    if (delta < 64) {
                        this.stackMap.putByte(delta);
                        break;
                    }
                    this.stackMap.putByte(251).putShort(delta);
                    break;
                }
                case 4: {
                    if (delta < 64) {
                        this.stackMap.putByte(64 + delta);
                    } else {
                        this.stackMap.putByte(247).putShort(delta);
                    }
                    this.writeFrameType(stack[0]);
                }
            }
            this.previousFrameOffset = this.code.length;
            ++this.frameCount;
        }
        this.maxStack = Math.max(this.maxStack, nStack);
        this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
    }

    public void visitInsn(int opcode) {
        this.code.putByte(opcode);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, null, null);
            } else {
                int size = this.stackSize + Frame.SIZE[opcode];
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
            if (opcode >= 172 && opcode <= 177 || opcode == 191) {
                this.noSuccessor();
            }
        }
    }

    public void visitIntInsn(int opcode, int operand) {
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, operand, null, null);
            } else if (opcode != 188) {
                int size = this.stackSize + 1;
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        if (opcode == 17) {
            this.code.put12(opcode, operand);
        } else {
            this.code.put11(opcode, operand);
        }
    }

    public void visitVarInsn(int opcode, int var2) {
        int n2;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, var2, null, null);
            } else if (opcode == 169) {
                this.currentBlock.status |= 0x100;
                this.currentBlock.inputStackTop = this.stackSize;
                this.noSuccessor();
            } else {
                int size = this.stackSize + Frame.SIZE[opcode];
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        if (this.compute != 2 && (n2 = opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57 ? var2 + 2 : var2 + 1) > this.maxLocals) {
            this.maxLocals = n2;
        }
        if (var2 < 4 && opcode != 169) {
            int opt = opcode < 54 ? 26 + (opcode - 21 << 2) + var2 : 59 + (opcode - 54 << 2) + var2;
            this.code.putByte(opt);
        } else if (var2 >= 256) {
            this.code.putByte(196).put12(opcode, var2);
        } else {
            this.code.put11(opcode, var2);
        }
        if (opcode >= 54 && this.compute == 0 && this.handlerCount > 0) {
            this.visitLabel(new Label());
        }
    }

    public void visitTypeInsn(int opcode, String type) {
        Item i2 = this.cw.newClassItem(type);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, this.code.length, this.cw, i2);
            } else if (opcode == 187) {
                int size = this.stackSize + 1;
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        this.code.put12(opcode, i2.index);
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        Item i2 = this.cw.newFieldItem(owner, name, desc);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, this.cw, i2);
            } else {
                int size;
                char c2 = desc.charAt(0);
                switch (opcode) {
                    case 178: {
                        size = this.stackSize + (c2 == 'D' || c2 == 'J' ? 2 : 1);
                        break;
                    }
                    case 179: {
                        size = this.stackSize + (c2 == 'D' || c2 == 'J' ? -2 : -1);
                        break;
                    }
                    case 180: {
                        size = this.stackSize + (c2 == 'D' || c2 == 'J' ? 1 : 0);
                        break;
                    }
                    default: {
                        size = this.stackSize + (c2 == 'D' || c2 == 'J' ? -3 : -2);
                    }
                }
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        this.code.put12(opcode, i2.index);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        boolean itf = opcode == 185;
        Item i2 = this.cw.newMethodItem(owner, name, desc, itf);
        int argSize = i2.intVal;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, this.cw, i2);
            } else {
                int size;
                if (argSize == 0) {
                    i2.intVal = argSize = Type.getArgumentsAndReturnSizes(desc);
                }
                if ((size = opcode == 184 ? this.stackSize - (argSize >> 2) + (argSize & 3) + 1 : this.stackSize - (argSize >> 2) + (argSize & 3)) > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        if (itf) {
            if (argSize == 0) {
                i2.intVal = argSize = Type.getArgumentsAndReturnSizes(desc);
            }
            this.code.put12(185, i2.index).put11(argSize >> 2, 0);
        } else {
            this.code.put12(opcode, i2.index);
        }
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        Item i2 = this.cw.newInvokeDynamicItem(name, desc, bsm, bsmArgs);
        int argSize = i2.intVal;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(186, 0, this.cw, i2);
            } else {
                int size;
                if (argSize == 0) {
                    i2.intVal = argSize = Type.getArgumentsAndReturnSizes(desc);
                }
                if ((size = this.stackSize - (argSize >> 2) + (argSize & 3) + 1) > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        this.code.put12(186, i2.index);
        this.code.putShort(0);
    }

    public void visitJumpInsn(int opcode, Label label) {
        Label nextInsn = null;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, null, null);
                label.getFirst().status |= 0x10;
                this.addSuccessor(0, label);
                if (opcode != 167) {
                    nextInsn = new Label();
                }
            } else if (opcode == 168) {
                if ((label.status & 0x200) == 0) {
                    label.status |= 0x200;
                    ++this.subroutines;
                }
                this.currentBlock.status |= 0x80;
                this.addSuccessor(this.stackSize + 1, label);
                nextInsn = new Label();
            } else {
                this.stackSize += Frame.SIZE[opcode];
                this.addSuccessor(this.stackSize, label);
            }
        }
        if ((label.status & 2) != 0 && label.position - this.code.length < -32768) {
            if (opcode == 167) {
                this.code.putByte(200);
            } else if (opcode == 168) {
                this.code.putByte(201);
            } else {
                if (nextInsn != null) {
                    nextInsn.status |= 0x10;
                }
                this.code.putByte(opcode <= 166 ? (opcode + 1 ^ 1) - 1 : opcode ^ 1);
                this.code.putShort(8);
                this.code.putByte(200);
            }
            label.put(this, this.code, this.code.length - 1, true);
        } else {
            this.code.putByte(opcode);
            label.put(this, this.code, this.code.length - 1, false);
        }
        if (this.currentBlock != null) {
            if (nextInsn != null) {
                this.visitLabel(nextInsn);
            }
            if (opcode == 167) {
                this.noSuccessor();
            }
        }
    }

    public void visitLabel(Label label) {
        this.resize |= label.resolve(this, this.code.length, this.code.data);
        if ((label.status & 1) != 0) {
            return;
        }
        if (this.compute == 0) {
            if (this.currentBlock != null) {
                if (label.position == this.currentBlock.position) {
                    this.currentBlock.status |= label.status & 0x10;
                    label.frame = this.currentBlock.frame;
                    return;
                }
                this.addSuccessor(0, label);
            }
            this.currentBlock = label;
            if (label.frame == null) {
                label.frame = new Frame();
                label.frame.owner = label;
            }
            if (this.previousBlock != null) {
                if (label.position == this.previousBlock.position) {
                    this.previousBlock.status |= label.status & 0x10;
                    label.frame = this.previousBlock.frame;
                    this.currentBlock = this.previousBlock;
                    return;
                }
                this.previousBlock.successor = label;
            }
            this.previousBlock = label;
        } else if (this.compute == 1) {
            if (this.currentBlock != null) {
                this.currentBlock.outputStackMax = this.maxStackSize;
                this.addSuccessor(this.stackSize, label);
            }
            this.currentBlock = label;
            this.stackSize = 0;
            this.maxStackSize = 0;
            if (this.previousBlock != null) {
                this.previousBlock.successor = label;
            }
            this.previousBlock = label;
        }
    }

    public void visitLdcInsn(Object cst) {
        Item i2 = this.cw.newConstItem(cst);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(18, 0, this.cw, i2);
            } else {
                int size = i2.type == 5 || i2.type == 6 ? this.stackSize + 2 : this.stackSize + 1;
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        int index = i2.index;
        if (i2.type == 5 || i2.type == 6) {
            this.code.put12(20, index);
        } else if (index >= 256) {
            this.code.put12(19, index);
        } else {
            this.code.put11(18, index);
        }
    }

    public void visitIincInsn(int var2, int increment) {
        int n2;
        if (this.currentBlock != null && this.compute == 0) {
            this.currentBlock.frame.execute(132, var2, null, null);
        }
        if (this.compute != 2 && (n2 = var2 + 1) > this.maxLocals) {
            this.maxLocals = n2;
        }
        if (var2 > 255 || increment > 127 || increment < -128) {
            this.code.putByte(196).put12(132, var2).putShort(increment);
        } else {
            this.code.putByte(132).put11(var2, increment);
        }
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        int source = this.code.length;
        this.code.putByte(170);
        this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this, this.code, source, true);
        this.code.putInt(min).putInt(max);
        for (int i2 = 0; i2 < labels.length; ++i2) {
            labels[i2].put(this, this.code, source, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        int source = this.code.length;
        this.code.putByte(171);
        this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this, this.code, source, true);
        this.code.putInt(labels.length);
        for (int i2 = 0; i2 < labels.length; ++i2) {
            this.code.putInt(keys[i2]);
            labels[i2].put(this, this.code, source, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }

    private void visitSwitchInsn(Label dflt, Label[] labels) {
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(171, 0, null, null);
                this.addSuccessor(0, dflt);
                dflt.getFirst().status |= 0x10;
                for (int i2 = 0; i2 < labels.length; ++i2) {
                    this.addSuccessor(0, labels[i2]);
                    labels[i2].getFirst().status |= 0x10;
                }
            } else {
                --this.stackSize;
                this.addSuccessor(this.stackSize, dflt);
                for (int i3 = 0; i3 < labels.length; ++i3) {
                    this.addSuccessor(this.stackSize, labels[i3]);
                }
            }
            this.noSuccessor();
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        Item i2 = this.cw.newClassItem(desc);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(197, dims, this.cw, i2);
            } else {
                this.stackSize += 1 - dims;
            }
        }
        this.code.put12(197, i2.index).putByte(dims);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        ++this.handlerCount;
        Handler h2 = new Handler();
        h2.start = start;
        h2.end = end;
        h2.handler = handler;
        h2.desc = type;
        int n2 = h2.type = type != null ? this.cw.newClass(type) : 0;
        if (this.lastHandler == null) {
            this.firstHandler = h2;
        } else {
            this.lastHandler.next = h2;
        }
        this.lastHandler = h2;
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        char c2;
        int n2;
        if (signature != null) {
            if (this.localVarType == null) {
                this.localVarType = new ByteVector();
            }
            ++this.localVarTypeCount;
            this.localVarType.putShort(start.position).putShort(end.position - start.position).putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(signature)).putShort(index);
        }
        if (this.localVar == null) {
            this.localVar = new ByteVector();
        }
        ++this.localVarCount;
        this.localVar.putShort(start.position).putShort(end.position - start.position).putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(desc)).putShort(index);
        if (this.compute != 2 && (n2 = index + ((c2 = desc.charAt(0)) == 'J' || c2 == 'D' ? 2 : 1)) > this.maxLocals) {
            this.maxLocals = n2;
        }
    }

    public void visitLineNumber(int line, Label start) {
        if (this.lineNumber == null) {
            this.lineNumber = new ByteVector();
        }
        ++this.lineNumberCount;
        this.lineNumber.putShort(start.position);
        this.lineNumber.putShort(line);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        if (this.compute == 0) {
            Handler handler = this.firstHandler;
            while (handler != null) {
                Label l2 = handler.start.getFirst();
                Label h2 = handler.handler.getFirst();
                Label e2 = handler.end.getFirst();
                String t2 = handler.desc == null ? "java/lang/Throwable" : handler.desc;
                int kind = 0x1700000 | this.cw.addType(t2);
                h2.status |= 0x10;
                while (l2 != e2) {
                    Edge b2 = new Edge();
                    b2.info = kind;
                    b2.successor = h2;
                    b2.next = l2.successors;
                    l2.successors = b2;
                    l2 = l2.successor;
                }
                handler = handler.next;
            }
            Frame f2 = this.labels.frame;
            Type[] args = Type.getArgumentTypes(this.descriptor);
            f2.initInputFrame(this.cw, this.access, args, this.maxLocals);
            this.visitFrame(f2);
            int max = 0;
            Label changed = this.labels;
            while (changed != null) {
                Label l3 = changed;
                changed = changed.next;
                l3.next = null;
                f2 = l3.frame;
                if ((l3.status & 0x10) != 0) {
                    l3.status |= 0x20;
                }
                l3.status |= 0x40;
                int blockMax = f2.inputStack.length + l3.outputStackMax;
                if (blockMax > max) {
                    max = blockMax;
                }
                Edge e3 = l3.successors;
                while (e3 != null) {
                    Label n2 = e3.successor.getFirst();
                    boolean change = f2.merge(this.cw, n2.frame, e3.info);
                    if (change && n2.next == null) {
                        n2.next = changed;
                        changed = n2;
                    }
                    e3 = e3.next;
                }
            }
            Label l4 = this.labels;
            while (l4 != null) {
                int start;
                Label k2;
                int end;
                f2 = l4.frame;
                if ((l4.status & 0x20) != 0) {
                    this.visitFrame(f2);
                }
                if ((l4.status & 0x40) == 0 && (end = ((k2 = l4.successor) == null ? this.code.length : k2.position) - 1) >= (start = l4.position)) {
                    max = Math.max(max, 1);
                    for (int i2 = start; i2 < end; ++i2) {
                        this.code.data[i2] = 0;
                    }
                    this.code.data[end] = -65;
                    this.startFrame(start, 0, 1);
                    this.frame[this.frameIndex++] = 0x1700000 | this.cw.addType("java/lang/Throwable");
                    this.endFrame();
                }
                l4 = l4.successor;
            }
            this.maxStack = max;
        } else if (this.compute == 1) {
            Handler handler = this.firstHandler;
            while (handler != null) {
                Label l5 = handler.start;
                Label h3 = handler.handler;
                Label e4 = handler.end;
                while (l5 != e4) {
                    Edge b3 = new Edge();
                    b3.info = Integer.MAX_VALUE;
                    b3.successor = h3;
                    if ((l5.status & 0x80) == 0) {
                        b3.next = l5.successors;
                        l5.successors = b3;
                    } else {
                        b3.next = l5.successors.next.next;
                        l5.successors.next.next = b3;
                    }
                    l5 = l5.successor;
                }
                handler = handler.next;
            }
            if (this.subroutines > 0) {
                int id = 0;
                this.labels.visitSubroutine(null, 1L, this.subroutines);
                Label l6 = this.labels;
                while (l6 != null) {
                    if ((l6.status & 0x80) != 0) {
                        Label subroutine = l6.successors.next.successor;
                        if ((subroutine.status & 0x400) == 0) {
                            subroutine.visitSubroutine(null, (long)(++id) / 32L << 32 | 1L << id % 32, this.subroutines);
                        }
                    }
                    l6 = l6.successor;
                }
                l6 = this.labels;
                while (l6 != null) {
                    if ((l6.status & 0x80) != 0) {
                        Label L = this.labels;
                        while (L != null) {
                            L.status &= 0xFFFFF7FF;
                            L = L.successor;
                        }
                        Label subroutine = l6.successors.next.successor;
                        subroutine.visitSubroutine(l6, 0L, this.subroutines);
                    }
                    l6 = l6.successor;
                }
            }
            int max = 0;
            Label stack = this.labels;
            while (stack != null) {
                Label l7 = stack;
                stack = stack.next;
                int start = l7.inputStackTop;
                int blockMax = start + l7.outputStackMax;
                if (blockMax > max) {
                    max = blockMax;
                }
                Edge b4 = l7.successors;
                if ((l7.status & 0x80) != 0) {
                    b4 = b4.next;
                }
                while (b4 != null) {
                    l7 = b4.successor;
                    if ((l7.status & 8) == 0) {
                        l7.inputStackTop = b4.info == Integer.MAX_VALUE ? 1 : start + b4.info;
                        l7.status |= 8;
                        l7.next = stack;
                        stack = l7;
                    }
                    b4 = b4.next;
                }
            }
            this.maxStack = Math.max(maxStack, max);
        } else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }

    public void visitEnd() {
    }

    private void addSuccessor(int info, Label successor) {
        Edge b2 = new Edge();
        b2.info = info;
        b2.successor = successor;
        b2.next = this.currentBlock.successors;
        this.currentBlock.successors = b2;
    }

    private void noSuccessor() {
        if (this.compute == 0) {
            Label l2 = new Label();
            l2.frame = new Frame();
            l2.frame.owner = l2;
            l2.resolve(this, this.code.length, this.code.data);
            this.previousBlock.successor = l2;
            this.previousBlock = l2;
        } else {
            this.currentBlock.outputStackMax = this.maxStackSize;
        }
        this.currentBlock = null;
    }

    private void visitFrame(Frame f2) {
        int t2;
        int i2;
        int nTop = 0;
        int nLocal = 0;
        int nStack = 0;
        int[] locals = f2.inputLocals;
        int[] stacks = f2.inputStack;
        for (i2 = 0; i2 < locals.length; ++i2) {
            t2 = locals[i2];
            if (t2 == 0x1000000) {
                ++nTop;
            } else {
                nLocal += nTop + 1;
                nTop = 0;
            }
            if (t2 != 0x1000004 && t2 != 0x1000003) continue;
            ++i2;
        }
        for (i2 = 0; i2 < stacks.length; ++i2) {
            t2 = stacks[i2];
            ++nStack;
            if (t2 != 0x1000004 && t2 != 0x1000003) continue;
            ++i2;
        }
        this.startFrame(f2.owner.position, nLocal, nStack);
        i2 = 0;
        while (nLocal > 0) {
            t2 = locals[i2];
            this.frame[this.frameIndex++] = t2;
            if (t2 == 0x1000004 || t2 == 0x1000003) {
                ++i2;
            }
            ++i2;
            --nLocal;
        }
        for (i2 = 0; i2 < stacks.length; ++i2) {
            t2 = stacks[i2];
            this.frame[this.frameIndex++] = t2;
            if (t2 != 0x1000004 && t2 != 0x1000003) continue;
            ++i2;
        }
        this.endFrame();
    }

    private void startFrame(int offset, int nLocal, int nStack) {
        int n2 = 3 + nLocal + nStack;
        if (this.frame == null || this.frame.length < n2) {
            this.frame = new int[n2];
        }
        this.frame[0] = offset;
        this.frame[1] = nLocal;
        this.frame[2] = nStack;
        this.frameIndex = 3;
    }

    private void endFrame() {
        if (this.previousFrame != null) {
            if (this.stackMap == null) {
                this.stackMap = new ByteVector();
            }
            this.writeFrame();
            ++this.frameCount;
        }
        this.previousFrame = this.frame;
        this.frame = null;
    }

    private void writeFrame() {
        int clocalsSize = this.frame[1];
        int cstackSize = this.frame[2];
        if ((this.cw.version & 0xFFFF) < 50) {
            this.stackMap.putShort(this.frame[0]).putShort(clocalsSize);
            this.writeFrameTypes(3, 3 + clocalsSize);
            this.stackMap.putShort(cstackSize);
            this.writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
            return;
        }
        int localsSize = this.previousFrame[1];
        int type = 255;
        int k2 = 0;
        int delta = this.frameCount == 0 ? this.frame[0] : this.frame[0] - this.previousFrame[0] - 1;
        if (cstackSize == 0) {
            k2 = clocalsSize - localsSize;
            switch (k2) {
                case -3: 
                case -2: 
                case -1: {
                    type = 248;
                    localsSize = clocalsSize;
                    break;
                }
                case 0: {
                    type = delta < 64 ? 0 : 251;
                    break;
                }
                case 1: 
                case 2: 
                case 3: {
                    type = 252;
                }
            }
        } else if (clocalsSize == localsSize && cstackSize == 1) {
            int n2 = type = delta < 63 ? 64 : 247;
        }
        if (type != 255) {
            int l2 = 3;
            for (int j2 = 0; j2 < localsSize; ++j2) {
                if (this.frame[l2] != this.previousFrame[l2]) {
                    type = 255;
                    break;
                }
                ++l2;
            }
        }
        switch (type) {
            case 0: {
                this.stackMap.putByte(delta);
                break;
            }
            case 64: {
                this.stackMap.putByte(64 + delta);
                this.writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            }
            case 247: {
                this.stackMap.putByte(247).putShort(delta);
                this.writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            }
            case 251: {
                this.stackMap.putByte(251).putShort(delta);
                break;
            }
            case 248: {
                this.stackMap.putByte(251 + k2).putShort(delta);
                break;
            }
            case 252: {
                this.stackMap.putByte(251 + k2).putShort(delta);
                this.writeFrameTypes(3 + localsSize, 3 + clocalsSize);
                break;
            }
            default: {
                this.stackMap.putByte(255).putShort(delta).putShort(clocalsSize);
                this.writeFrameTypes(3, 3 + clocalsSize);
                this.stackMap.putShort(cstackSize);
                this.writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
            }
        }
    }

    private void writeFrameTypes(int start, int end) {
        for (int i2 = start; i2 < end; ++i2) {
            int t2 = this.frame[i2];
            int d2 = t2 & 0xF0000000;
            if (d2 == 0) {
                int v2 = t2 & 0xFFFFF;
                switch (t2 & 0xFF00000) {
                    case 0x1700000: {
                        this.stackMap.putByte(7).putShort(this.cw.newClass(this.cw.typeTable[v2].strVal1));
                        break;
                    }
                    case 0x1800000: {
                        this.stackMap.putByte(8).putShort(this.cw.typeTable[v2].intVal);
                        break;
                    }
                    default: {
                        this.stackMap.putByte(v2);
                        break;
                    }
                }
                continue;
            }
            StringBuffer buf = new StringBuffer();
            d2 >>= 28;
            while (d2-- > 0) {
                buf.append('[');
            }
            if ((t2 & 0xFF00000) == 0x1700000) {
                buf.append('L');
                buf.append(this.cw.typeTable[t2 & 1048575].strVal1);
                buf.append(';');
            } else {
                switch (t2 & 0xF) {
                    case 1: {
                        buf.append('I');
                        break;
                    }
                    case 2: {
                        buf.append('F');
                        break;
                    }
                    case 3: {
                        buf.append('D');
                        break;
                    }
                    case 9: {
                        buf.append('Z');
                        break;
                    }
                    case 10: {
                        buf.append('B');
                        break;
                    }
                    case 11: {
                        buf.append('C');
                        break;
                    }
                    case 12: {
                        buf.append('S');
                        break;
                    }
                    default: {
                        buf.append('J');
                    }
                }
            }
            this.stackMap.putByte(7).putShort(this.cw.newClass(buf.toString()));
        }
    }

    private void writeFrameType(Object type) {
        if (type instanceof String) {
            this.stackMap.putByte(7).putShort(this.cw.newClass((String)type));
        } else if (type instanceof Integer) {
            this.stackMap.putByte((Integer)type);
        } else {
            this.stackMap.putByte(8).putShort(((Label)type).position);
        }
    }

    final int getSize() {
        int i2;
        if (this.classReaderOffset != 0) {
            return 6 + this.classReaderLength;
        }
        if (this.resize) {
            this.resizeInstructions();
        }
        int size = 8;
        if (this.code.length > 0) {
            if (this.code.length > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            this.cw.newUTF8("Code");
            size += 18 + this.code.length + 8 * this.handlerCount;
            if (this.localVar != null) {
                this.cw.newUTF8("LocalVariableTable");
                size += 8 + this.localVar.length;
            }
            if (this.localVarType != null) {
                this.cw.newUTF8("LocalVariableTypeTable");
                size += 8 + this.localVarType.length;
            }
            if (this.lineNumber != null) {
                this.cw.newUTF8("LineNumberTable");
                size += 8 + this.lineNumber.length;
            }
            if (this.stackMap != null) {
                boolean zip = (this.cw.version & 0xFFFF) >= 50;
                this.cw.newUTF8(zip ? "StackMapTable" : "StackMap");
                size += 8 + this.stackMap.length;
            }
            if (this.cattrs != null) {
                size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
        }
        if (this.exceptionCount > 0) {
            this.cw.newUTF8("Exceptions");
            size += 8 + 2 * this.exceptionCount;
        }
        if ((this.access & 0x1000) != 0 && ((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
            this.cw.newUTF8("Synthetic");
            size += 6;
        }
        if ((this.access & 0x20000) != 0) {
            this.cw.newUTF8("Deprecated");
            size += 6;
        }
        if (this.signature != null) {
            this.cw.newUTF8("Signature");
            this.cw.newUTF8(this.signature);
            size += 8;
        }
        if (this.annd != null) {
            this.cw.newUTF8("AnnotationDefault");
            size += 6 + this.annd.length;
        }
        if (this.anns != null) {
            this.cw.newUTF8("RuntimeVisibleAnnotations");
            size += 8 + this.anns.getSize();
        }
        if (this.ianns != null) {
            this.cw.newUTF8("RuntimeInvisibleAnnotations");
            size += 8 + this.ianns.getSize();
        }
        if (this.panns != null) {
            this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
            size += 7 + 2 * (this.panns.length - this.synthetics);
            for (i2 = this.panns.length - 1; i2 >= this.synthetics; --i2) {
                size += this.panns[i2] == null ? 0 : this.panns[i2].getSize();
            }
        }
        if (this.ipanns != null) {
            this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
            size += 7 + 2 * (this.ipanns.length - this.synthetics);
            for (i2 = this.ipanns.length - 1; i2 >= this.synthetics; --i2) {
                size += this.ipanns[i2] == null ? 0 : this.ipanns[i2].getSize();
            }
        }
        if (this.attrs != null) {
            size += this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        return size;
    }

    final void put(ByteVector out) {
        int mask = 0x60000 | (this.access & 0x40000) / 64;
        out.putShort(this.access & ~mask).putShort(this.name).putShort(this.desc);
        if (this.classReaderOffset != 0) {
            out.putByteArray(this.cw.cr.b, this.classReaderOffset, this.classReaderLength);
            return;
        }
        int attributeCount = 0;
        if (this.code.length > 0) {
            ++attributeCount;
        }
        if (this.exceptionCount > 0) {
            ++attributeCount;
        }
        if ((this.access & 0x1000) != 0 && ((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
            ++attributeCount;
        }
        if ((this.access & 0x20000) != 0) {
            ++attributeCount;
        }
        if (this.signature != null) {
            ++attributeCount;
        }
        if (this.annd != null) {
            ++attributeCount;
        }
        if (this.anns != null) {
            ++attributeCount;
        }
        if (this.ianns != null) {
            ++attributeCount;
        }
        if (this.panns != null) {
            ++attributeCount;
        }
        if (this.ipanns != null) {
            ++attributeCount;
        }
        if (this.attrs != null) {
            attributeCount += this.attrs.getCount();
        }
        out.putShort(attributeCount);
        if (this.code.length > 0) {
            int size = 12 + this.code.length + 8 * this.handlerCount;
            if (this.localVar != null) {
                size += 8 + this.localVar.length;
            }
            if (this.localVarType != null) {
                size += 8 + this.localVarType.length;
            }
            if (this.lineNumber != null) {
                size += 8 + this.lineNumber.length;
            }
            if (this.stackMap != null) {
                size += 8 + this.stackMap.length;
            }
            if (this.cattrs != null) {
                size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
            out.putShort(this.cw.newUTF8("Code")).putInt(size);
            out.putShort(this.maxStack).putShort(this.maxLocals);
            out.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            out.putShort(this.handlerCount);
            if (this.handlerCount > 0) {
                Handler h2 = this.firstHandler;
                while (h2 != null) {
                    out.putShort(h2.start.position).putShort(h2.end.position).putShort(h2.handler.position).putShort(h2.type);
                    h2 = h2.next;
                }
            }
            attributeCount = 0;
            if (this.localVar != null) {
                ++attributeCount;
            }
            if (this.localVarType != null) {
                ++attributeCount;
            }
            if (this.lineNumber != null) {
                ++attributeCount;
            }
            if (this.stackMap != null) {
                ++attributeCount;
            }
            if (this.cattrs != null) {
                attributeCount += this.cattrs.getCount();
            }
            out.putShort(attributeCount);
            if (this.localVar != null) {
                out.putShort(this.cw.newUTF8("LocalVariableTable"));
                out.putInt(this.localVar.length + 2).putShort(this.localVarCount);
                out.putByteArray(this.localVar.data, 0, this.localVar.length);
            }
            if (this.localVarType != null) {
                out.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
                out.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
                out.putByteArray(this.localVarType.data, 0, this.localVarType.length);
            }
            if (this.lineNumber != null) {
                out.putShort(this.cw.newUTF8("LineNumberTable"));
                out.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
                out.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
            }
            if (this.stackMap != null) {
                boolean zip = (this.cw.version & 0xFFFF) >= 50;
                out.putShort(this.cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
                out.putInt(this.stackMap.length + 2).putShort(this.frameCount);
                out.putByteArray(this.stackMap.data, 0, this.stackMap.length);
            }
            if (this.cattrs != null) {
                this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, out);
            }
        }
        if (this.exceptionCount > 0) {
            out.putShort(this.cw.newUTF8("Exceptions")).putInt(2 * this.exceptionCount + 2);
            out.putShort(this.exceptionCount);
            for (int i2 = 0; i2 < this.exceptionCount; ++i2) {
                out.putShort(this.exceptions[i2]);
            }
        }
        if ((this.access & 0x1000) != 0 && ((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
            out.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.access & 0x20000) != 0) {
            out.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        if (this.signature != null) {
            out.putShort(this.cw.newUTF8("Signature")).putInt(2).putShort(this.cw.newUTF8(this.signature));
        }
        if (this.annd != null) {
            out.putShort(this.cw.newUTF8("AnnotationDefault"));
            out.putInt(this.annd.length);
            out.putByteArray(this.annd.data, 0, this.annd.length);
        }
        if (this.anns != null) {
            out.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(out);
        }
        if (this.ianns != null) {
            out.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(out);
        }
        if (this.panns != null) {
            out.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.put(this.panns, this.synthetics, out);
        }
        if (this.ipanns != null) {
            out.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.put(this.ipanns, this.synthetics, out);
        }
        if (this.attrs != null) {
            this.attrs.put(this.cw, null, 0, -1, -1, out);
        }
    }

    private void resizeInstructions() {
        int i2;
        int newOffset;
        int label;
        int u2;
        byte[] b2 = this.code.data;
        int[] allIndexes = new int[]{};
        int[] allSizes = new int[]{};
        boolean[] resize = new boolean[this.code.length];
        int state = 3;
        do {
            if (state == 3) {
                state = 2;
            }
            u2 = 0;
            while (u2 < b2.length) {
                int opcode = b2[u2] & 0xFF;
                int insert = 0;
                switch (ClassWriter.TYPE[opcode]) {
                    case 0: 
                    case 4: {
                        ++u2;
                        break;
                    }
                    case 9: {
                        if (opcode > 201) {
                            opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                            label = u2 + MethodWriter.readUnsignedShort(b2, u2 + 1);
                        } else {
                            label = u2 + MethodWriter.readShort(b2, u2 + 1);
                        }
                        newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, u2, label);
                        if (!(newOffset >= -32768 && newOffset <= 32767 || resize[u2])) {
                            insert = opcode == 167 || opcode == 168 ? 2 : 5;
                            resize[u2] = true;
                        }
                        u2 += 3;
                        break;
                    }
                    case 10: {
                        u2 += 5;
                        break;
                    }
                    case 14: {
                        if (state == 1) {
                            newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, 0, u2);
                            insert = -(newOffset & 3);
                        } else if (!resize[u2]) {
                            insert = u2 & 3;
                            resize[u2] = true;
                        }
                        u2 = u2 + 4 - (u2 & 3);
                        u2 += 4 * (MethodWriter.readInt(b2, u2 + 8) - MethodWriter.readInt(b2, u2 + 4) + 1) + 12;
                        break;
                    }
                    case 15: {
                        if (state == 1) {
                            newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, 0, u2);
                            insert = -(newOffset & 3);
                        } else if (!resize[u2]) {
                            insert = u2 & 3;
                            resize[u2] = true;
                        }
                        u2 = u2 + 4 - (u2 & 3);
                        u2 += 8 * MethodWriter.readInt(b2, u2 + 4) + 8;
                        break;
                    }
                    case 17: {
                        opcode = b2[u2 + 1] & 0xFF;
                        if (opcode == 132) {
                            u2 += 6;
                            break;
                        }
                        u2 += 4;
                        break;
                    }
                    case 1: 
                    case 3: 
                    case 11: {
                        u2 += 2;
                        break;
                    }
                    case 2: 
                    case 5: 
                    case 6: 
                    case 12: 
                    case 13: {
                        u2 += 3;
                        break;
                    }
                    case 7: 
                    case 8: {
                        u2 += 5;
                        break;
                    }
                    default: {
                        u2 += 4;
                    }
                }
                if (insert == 0) continue;
                int[] newIndexes = new int[allIndexes.length + 1];
                int[] newSizes = new int[allSizes.length + 1];
                System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
                System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
                newIndexes[allIndexes.length] = u2;
                newSizes[allSizes.length] = insert;
                allIndexes = newIndexes;
                allSizes = newSizes;
                if (insert <= 0) continue;
                state = 3;
            }
            if (state >= 3) continue;
            --state;
        } while (state != 0);
        ByteVector newCode = new ByteVector(this.code.length);
        u2 = 0;
        block24: while (u2 < this.code.length) {
            int opcode = b2[u2] & 0xFF;
            switch (ClassWriter.TYPE[opcode]) {
                case 0: 
                case 4: {
                    newCode.putByte(opcode);
                    ++u2;
                    continue block24;
                }
                case 9: {
                    if (opcode > 201) {
                        opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                        label = u2 + MethodWriter.readUnsignedShort(b2, u2 + 1);
                    } else {
                        label = u2 + MethodWriter.readShort(b2, u2 + 1);
                    }
                    newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, u2, label);
                    if (resize[u2]) {
                        if (opcode == 167) {
                            newCode.putByte(200);
                        } else if (opcode == 168) {
                            newCode.putByte(201);
                        } else {
                            newCode.putByte(opcode <= 166 ? (opcode + 1 ^ 1) - 1 : opcode ^ 1);
                            newCode.putShort(8);
                            newCode.putByte(200);
                            newOffset -= 3;
                        }
                        newCode.putInt(newOffset);
                    } else {
                        newCode.putByte(opcode);
                        newCode.putShort(newOffset);
                    }
                    u2 += 3;
                    continue block24;
                }
                case 10: {
                    label = u2 + MethodWriter.readInt(b2, u2 + 1);
                    newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, u2, label);
                    newCode.putByte(opcode);
                    newCode.putInt(newOffset);
                    u2 += 5;
                    continue block24;
                }
                case 14: {
                    int v2 = u2;
                    u2 = u2 + 4 - (v2 & 3);
                    newCode.putByte(170);
                    newCode.putByteArray(null, 0, (4 - newCode.length % 4) % 4);
                    label = v2 + MethodWriter.readInt(b2, u2);
                    newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, v2, label);
                    newCode.putInt(newOffset);
                    int j2 = MethodWriter.readInt(b2, u2 += 4);
                    newCode.putInt(j2);
                    newCode.putInt(MethodWriter.readInt(b2, (u2 += 4) - 4));
                    for (j2 = MethodWriter.readInt(b2, u2 += 4) - j2 + 1; j2 > 0; --j2) {
                        label = v2 + MethodWriter.readInt(b2, u2);
                        u2 += 4;
                        newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, v2, label);
                        newCode.putInt(newOffset);
                    }
                    continue block24;
                }
                case 15: {
                    int j2;
                    int v2 = u2;
                    u2 = u2 + 4 - (v2 & 3);
                    newCode.putByte(171);
                    newCode.putByteArray(null, 0, (4 - newCode.length % 4) % 4);
                    label = v2 + MethodWriter.readInt(b2, u2);
                    newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, v2, label);
                    newCode.putInt(newOffset);
                    u2 += 4;
                    newCode.putInt(j2);
                    for (j2 = MethodWriter.readInt(b2, u2 += 4); j2 > 0; --j2) {
                        newCode.putInt(MethodWriter.readInt(b2, u2));
                        label = v2 + MethodWriter.readInt(b2, u2 += 4);
                        u2 += 4;
                        newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, v2, label);
                        newCode.putInt(newOffset);
                    }
                    continue block24;
                }
                case 17: {
                    opcode = b2[u2 + 1] & 0xFF;
                    if (opcode == 132) {
                        newCode.putByteArray(b2, u2, 6);
                        u2 += 6;
                        continue block24;
                    }
                    newCode.putByteArray(b2, u2, 4);
                    u2 += 4;
                    continue block24;
                }
                case 1: 
                case 3: 
                case 11: {
                    newCode.putByteArray(b2, u2, 2);
                    u2 += 2;
                    continue block24;
                }
                case 2: 
                case 5: 
                case 6: 
                case 12: 
                case 13: {
                    newCode.putByteArray(b2, u2, 3);
                    u2 += 3;
                    continue block24;
                }
                case 7: 
                case 8: {
                    newCode.putByteArray(b2, u2, 5);
                    u2 += 5;
                    continue block24;
                }
            }
            newCode.putByteArray(b2, u2, 4);
            u2 += 4;
        }
        if (this.frameCount > 0) {
            if (this.compute == 0) {
                this.frameCount = 0;
                this.stackMap = null;
                this.previousFrame = null;
                this.frame = null;
                Frame f2 = new Frame();
                f2.owner = this.labels;
                Type[] args = Type.getArgumentTypes(this.descriptor);
                f2.initInputFrame(this.cw, this.access, args, this.maxLocals);
                this.visitFrame(f2);
                Label l2 = this.labels;
                while (l2 != null) {
                    u2 = l2.position - 3;
                    if ((l2.status & 0x20) != 0 || u2 >= 0 && resize[u2]) {
                        MethodWriter.getNewOffset(allIndexes, allSizes, l2);
                        this.visitFrame(l2.frame);
                    }
                    l2 = l2.successor;
                }
            } else {
                this.cw.invalidFrames = true;
            }
        }
        Handler h2 = this.firstHandler;
        while (h2 != null) {
            MethodWriter.getNewOffset(allIndexes, allSizes, h2.start);
            MethodWriter.getNewOffset(allIndexes, allSizes, h2.end);
            MethodWriter.getNewOffset(allIndexes, allSizes, h2.handler);
            h2 = h2.next;
        }
        for (i2 = 0; i2 < 2; ++i2) {
            ByteVector bv;
            ByteVector byteVector = bv = i2 == 0 ? this.localVar : this.localVarType;
            if (bv == null) continue;
            b2 = bv.data;
            for (u2 = 0; u2 < bv.length; u2 += 10) {
                label = MethodWriter.readUnsignedShort(b2, u2);
                newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, 0, label);
                MethodWriter.writeShort(b2, u2, newOffset);
                newOffset = MethodWriter.getNewOffset(allIndexes, allSizes, 0, label += MethodWriter.readUnsignedShort(b2, u2 + 2)) - newOffset;
                MethodWriter.writeShort(b2, u2 + 2, newOffset);
            }
        }
        if (this.lineNumber != null) {
            b2 = this.lineNumber.data;
            for (u2 = 0; u2 < this.lineNumber.length; u2 += 4) {
                MethodWriter.writeShort(b2, u2, MethodWriter.getNewOffset(allIndexes, allSizes, 0, MethodWriter.readUnsignedShort(b2, u2)));
            }
        }
        Attribute attr = this.cattrs;
        while (attr != null) {
            Label[] labels = attr.getLabels();
            if (labels != null) {
                for (i2 = labels.length - 1; i2 >= 0; --i2) {
                    MethodWriter.getNewOffset(allIndexes, allSizes, labels[i2]);
                }
            }
            attr = attr.next;
        }
        this.code = newCode;
    }

    static int readUnsignedShort(byte[] b2, int index) {
        return (b2[index] & 0xFF) << 8 | b2[index + 1] & 0xFF;
    }

    static short readShort(byte[] b2, int index) {
        return (short)((b2[index] & 0xFF) << 8 | b2[index + 1] & 0xFF);
    }

    static int readInt(byte[] b2, int index) {
        return (b2[index] & 0xFF) << 24 | (b2[index + 1] & 0xFF) << 16 | (b2[index + 2] & 0xFF) << 8 | b2[index + 3] & 0xFF;
    }

    static void writeShort(byte[] b2, int index, int s2) {
        b2[index] = (byte)(s2 >>> 8);
        b2[index + 1] = (byte)s2;
    }

    static int getNewOffset(int[] indexes, int[] sizes, int begin, int end) {
        int offset = end - begin;
        for (int i2 = 0; i2 < indexes.length; ++i2) {
            if (begin < indexes[i2] && indexes[i2] <= end) {
                offset += sizes[i2];
                continue;
            }
            if (end >= indexes[i2] || indexes[i2] > begin) continue;
            offset -= sizes[i2];
        }
        return offset;
    }

    static void getNewOffset(int[] indexes, int[] sizes, Label label) {
        if ((label.status & 4) == 0) {
            label.position = MethodWriter.getNewOffset(indexes, sizes, 0, label.position);
            label.status |= 4;
        }
    }
}

