/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

public final class SharedBuffer {
    private long nativePointer;

    SharedBuffer() {
        this.nativePointer = SharedBuffer.twkCreate();
    }

    private SharedBuffer(long l2) {
        if (l2 == 0L) {
            throw new IllegalArgumentException("nativePointer is 0");
        }
        this.nativePointer = l2;
    }

    private static SharedBuffer fwkCreate(long l2) {
        return new SharedBuffer(l2);
    }

    long size() {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        return SharedBuffer.twkSize(this.nativePointer);
    }

    int getSomeData(long l2, byte[] arrby, int n2, int n3) {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        if (l2 < 0L) {
            throw new IndexOutOfBoundsException("position is negative");
        }
        if (l2 > this.size()) {
            throw new IndexOutOfBoundsException("position is greater than size");
        }
        if (arrby == null) {
            throw new NullPointerException("buffer is null");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("offset is negative");
        }
        if (n3 < 0) {
            throw new IndexOutOfBoundsException("length is negative");
        }
        if (n3 > arrby.length - n2) {
            throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
        }
        return SharedBuffer.twkGetSomeData(this.nativePointer, l2, arrby, n2, n3);
    }

    void append(byte[] arrby, int n2, int n3) {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        if (arrby == null) {
            throw new NullPointerException("buffer is null");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("offset is negative");
        }
        if (n3 < 0) {
            throw new IndexOutOfBoundsException("length is negative");
        }
        if (n3 > arrby.length - n2) {
            throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
        }
        SharedBuffer.twkAppend(this.nativePointer, arrby, n2, n3);
    }

    void dispose() {
        if (this.nativePointer == 0L) {
            throw new IllegalStateException("nativePointer is 0");
        }
        SharedBuffer.twkDispose(this.nativePointer);
        this.nativePointer = 0L;
    }

    private static native long twkCreate();

    private static native long twkSize(long var0);

    private static native int twkGetSomeData(long var0, long var2, byte[] var4, int var5, int var6);

    private static native void twkAppend(long var0, byte[] var2, int var3, int var4);

    private static native void twkDispose(long var0);
}

