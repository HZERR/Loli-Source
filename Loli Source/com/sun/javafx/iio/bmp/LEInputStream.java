/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.common.ImageTools;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class LEInputStream {
    public final InputStream in;

    LEInputStream(InputStream inputStream) {
        this.in = inputStream;
    }

    public final short readShort() throws IOException {
        int n2;
        int n3 = this.in.read();
        if ((n3 | (n2 = this.in.read())) < 0) {
            throw new EOFException();
        }
        return (short)((n2 << 8) + n3);
    }

    public final int readInt() throws IOException {
        int n2;
        int n3;
        int n4;
        int n5 = this.in.read();
        if ((n5 | (n4 = this.in.read()) | (n3 = this.in.read()) | (n2 = this.in.read())) < 0) {
            throw new EOFException();
        }
        return (n2 << 24) + (n3 << 16) + (n4 << 8) + n5;
    }

    public final void skipBytes(int n2) throws IOException {
        ImageTools.skipFully(this.in, n2);
    }
}

