/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.SharedBuffer;
import java.io.InputStream;

public final class SimpleSharedBufferInputStream
extends InputStream {
    private final SharedBuffer sharedBuffer;
    private long position;

    public SimpleSharedBufferInputStream(SharedBuffer sharedBuffer) {
        if (sharedBuffer == null) {
            throw new NullPointerException("sharedBuffer is null");
        }
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public int read() {
        byte[] arrby = new byte[1];
        int n2 = this.sharedBuffer.getSomeData(this.position, arrby, 0, 1);
        if (n2 != 0) {
            ++this.position;
            return arrby[0] & 0xFF;
        }
        return -1;
    }

    @Override
    public int read(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            throw new NullPointerException("b is null");
        }
        if (n2 < 0) {
            throw new IndexOutOfBoundsException("off is negative");
        }
        if (n3 < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        if (n3 > arrby.length - n2) {
            throw new IndexOutOfBoundsException("len is greater than b.length - off");
        }
        if (n3 == 0) {
            return 0;
        }
        int n4 = this.sharedBuffer.getSomeData(this.position, arrby, n2, n3);
        if (n4 != 0) {
            this.position += (long)n4;
            return n4;
        }
        return -1;
    }

    @Override
    public long skip(long l2) {
        long l3 = this.sharedBuffer.size() - this.position;
        if (l2 < l3) {
            l3 = l2 < 0L ? 0L : l2;
        }
        this.position += l3;
        return l3;
    }

    @Override
    public int available() {
        return (int)Math.min(this.sharedBuffer.size() - this.position, Integer.MAX_VALUE);
    }
}

