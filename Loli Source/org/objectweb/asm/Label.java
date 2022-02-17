/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

import org.objectweb.asm.ByteVector;
import org.objectweb.asm.Edge;
import org.objectweb.asm.Frame;
import org.objectweb.asm.MethodWriter;

public class Label {
    static final int DEBUG = 1;
    static final int RESOLVED = 2;
    static final int RESIZED = 4;
    static final int PUSHED = 8;
    static final int TARGET = 16;
    static final int STORE = 32;
    static final int REACHABLE = 64;
    static final int JSR = 128;
    static final int RET = 256;
    static final int SUBROUTINE = 512;
    static final int VISITED = 1024;
    static final int VISITED2 = 2048;
    public Object info;
    int status;
    int line;
    int position;
    private int referenceCount;
    private int[] srcAndRefPositions;
    int inputStackTop;
    int outputStackMax;
    Frame frame;
    Label successor;
    Edge successors;
    Label next;

    public int getOffset() {
        if ((this.status & 2) == 0) {
            throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        return this.position;
    }

    void put(MethodWriter owner, ByteVector out, int source, boolean wideOffset) {
        if ((this.status & 2) == 0) {
            if (wideOffset) {
                this.addReference(-1 - source, out.length);
                out.putInt(-1);
            } else {
                this.addReference(source, out.length);
                out.putShort(-1);
            }
        } else if (wideOffset) {
            out.putInt(this.position - source);
        } else {
            out.putShort(this.position - source);
        }
    }

    private void addReference(int sourcePosition, int referencePosition) {
        if (this.srcAndRefPositions == null) {
            this.srcAndRefPositions = new int[6];
        }
        if (this.referenceCount >= this.srcAndRefPositions.length) {
            int[] a2 = new int[this.srcAndRefPositions.length + 6];
            System.arraycopy(this.srcAndRefPositions, 0, a2, 0, this.srcAndRefPositions.length);
            this.srcAndRefPositions = a2;
        }
        this.srcAndRefPositions[this.referenceCount++] = sourcePosition;
        this.srcAndRefPositions[this.referenceCount++] = referencePosition;
    }

    boolean resolve(MethodWriter owner, int position, byte[] data) {
        boolean needUpdate = false;
        this.status |= 2;
        this.position = position;
        int i2 = 0;
        while (i2 < this.referenceCount) {
            int offset;
            int source = this.srcAndRefPositions[i2++];
            int reference = this.srcAndRefPositions[i2++];
            if (source >= 0) {
                offset = position - source;
                if (offset < -32768 || offset > 32767) {
                    int opcode = data[reference - 1] & 0xFF;
                    data[reference - 1] = opcode <= 168 ? (byte)(opcode + 49) : (byte)(opcode + 20);
                    needUpdate = true;
                }
                data[reference++] = (byte)(offset >>> 8);
                data[reference] = (byte)offset;
                continue;
            }
            offset = position + source + 1;
            data[reference++] = (byte)(offset >>> 24);
            data[reference++] = (byte)(offset >>> 16);
            data[reference++] = (byte)(offset >>> 8);
            data[reference] = (byte)offset;
        }
        return needUpdate;
    }

    Label getFirst() {
        return this.frame == null ? this : this.frame.owner;
    }

    boolean inSubroutine(long id) {
        if ((this.status & 0x400) != 0) {
            return (this.srcAndRefPositions[(int)(id >>> 32)] & (int)id) != 0;
        }
        return false;
    }

    boolean inSameSubroutine(Label block) {
        if ((this.status & 0x400) == 0 || (block.status & 0x400) == 0) {
            return false;
        }
        for (int i2 = 0; i2 < this.srcAndRefPositions.length; ++i2) {
            if ((this.srcAndRefPositions[i2] & block.srcAndRefPositions[i2]) == 0) continue;
            return true;
        }
        return false;
    }

    void addToSubroutine(long id, int nbSubroutines) {
        if ((this.status & 0x400) == 0) {
            this.status |= 0x400;
            this.srcAndRefPositions = new int[(nbSubroutines - 1) / 32 + 1];
        }
        int n2 = (int)(id >>> 32);
        this.srcAndRefPositions[n2] = this.srcAndRefPositions[n2] | (int)id;
    }

    void visitSubroutine(Label JSR, long id, int nbSubroutines) {
        Label stack = this;
        while (stack != null) {
            Edge e2;
            Label l2 = stack;
            stack = l2.next;
            l2.next = null;
            if (JSR != null) {
                if ((l2.status & 0x800) != 0) continue;
                l2.status |= 0x800;
                if ((l2.status & 0x100) != 0 && !l2.inSameSubroutine(JSR)) {
                    e2 = new Edge();
                    e2.info = l2.inputStackTop;
                    e2.successor = JSR.successors.successor;
                    e2.next = l2.successors;
                    l2.successors = e2;
                }
            } else {
                if (l2.inSubroutine(id)) continue;
                l2.addToSubroutine(id, nbSubroutines);
            }
            e2 = l2.successors;
            while (e2 != null) {
                if (((l2.status & 0x80) == 0 || e2 != l2.successors.next) && e2.successor.next == null) {
                    e2.successor.next = stack;
                    stack = e2.successor;
                }
                e2 = e2.next;
            }
        }
    }

    public String toString() {
        return "L" + System.identityHashCode(this);
    }
}

