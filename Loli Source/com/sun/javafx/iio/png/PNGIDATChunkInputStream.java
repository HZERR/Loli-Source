/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.png;

import com.sun.javafx.iio.common.ImageTools;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PNGIDATChunkInputStream
extends InputStream {
    static final int IDAT_TYPE = 1229209940;
    private DataInputStream source;
    private int numBytesAvailable = 0;
    private boolean foundAllIDATChunks = false;
    private int nextChunkLength = 0;
    private int nextChunkType = 0;

    PNGIDATChunkInputStream(DataInputStream dataInputStream, int n2) throws IOException {
        if (n2 < 0) {
            throw new IOException("Invalid chunk length");
        }
        this.source = dataInputStream;
        this.numBytesAvailable = n2;
    }

    private void nextChunk() throws IOException {
        if (!this.foundAllIDATChunks) {
            ImageTools.skipFully(this.source, 4L);
            int n2 = this.source.readInt();
            if (n2 < 0) {
                throw new IOException("Invalid chunk length");
            }
            int n3 = this.source.readInt();
            if (n3 == 1229209940) {
                this.numBytesAvailable += n2;
            } else {
                this.foundAllIDATChunks = true;
                this.nextChunkLength = n2;
                this.nextChunkType = n3;
            }
        }
    }

    boolean isFoundAllIDATChunks() {
        return this.foundAllIDATChunks;
    }

    int getNextChunkLength() {
        return this.nextChunkLength;
    }

    int getNextChunkType() {
        return this.nextChunkType;
    }

    @Override
    public int read() throws IOException {
        if (this.numBytesAvailable == 0) {
            this.nextChunk();
        }
        if (this.numBytesAvailable == 0) {
            return -1;
        }
        --this.numBytesAvailable;
        return this.source.read();
    }

    @Override
    public int read(byte[] arrby, int n2, int n3) throws IOException {
        if (this.numBytesAvailable == 0) {
            this.nextChunk();
            if (this.numBytesAvailable == 0) {
                return -1;
            }
        }
        int n4 = 0;
        while (this.numBytesAvailable > 0 && n3 > 0) {
            int n5 = n3 < this.numBytesAvailable ? n3 : this.numBytesAvailable;
            int n6 = this.source.read(arrby, n2, n5);
            if (n6 == -1) {
                throw new EOFException();
            }
            this.numBytesAvailable -= n6;
            n2 += n6;
            n4 += n6;
            if (this.numBytesAvailable != 0 || (n3 -= n6) <= 0) continue;
            this.nextChunk();
        }
        return n4;
    }
}

