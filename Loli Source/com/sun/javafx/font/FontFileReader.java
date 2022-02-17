/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontConstants;
import com.sun.javafx.font.PrismFontFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedActionException;

class FontFileReader
implements FontConstants {
    String filename;
    long filesize;
    RandomAccessFile raFile;
    private static final int READBUFFERSIZE = 1024;
    private byte[] readBuffer;
    private int readBufferLen;
    private int readBufferStart;

    public FontFileReader(String string) {
        this.filename = string;
    }

    public String getFilename() {
        return this.filename;
    }

    public synchronized boolean openFile() throws PrivilegedActionException {
        if (this.raFile != null) {
            return false;
        }
        this.raFile = AccessController.doPrivileged(() -> {
            try {
                return new RandomAccessFile(this.filename, "r");
            }
            catch (FileNotFoundException fileNotFoundException) {
                return null;
            }
        });
        if (this.raFile != null) {
            try {
                this.filesize = this.raFile.length();
                return true;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return false;
    }

    public synchronized void closeFile() throws IOException {
        if (this.raFile != null) {
            this.raFile.close();
            this.raFile = null;
            this.readBuffer = null;
        }
    }

    public synchronized long getLength() {
        return this.filesize;
    }

    public synchronized void reset() throws IOException {
        if (this.raFile != null) {
            this.raFile.seek(0L);
        }
    }

    private synchronized int readFromFile(byte[] arrby, long l2, int n2) {
        try {
            this.raFile.seek(l2);
            int n3 = this.raFile.read(arrby, 0, n2);
            return n3;
        }
        catch (IOException iOException) {
            if (PrismFontFactory.debugFonts) {
                iOException.printStackTrace();
            }
            return 0;
        }
    }

    public synchronized Buffer readBlock(int n2, int n3) {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
            this.readBufferLen = 0;
        }
        if (n3 <= 1024) {
            if (this.readBufferStart <= n2 && this.readBufferStart + this.readBufferLen >= n2 + n3) {
                return new Buffer(this.readBuffer, n2 - this.readBufferStart);
            }
            this.readBufferStart = n2;
            this.readBufferLen = (long)(n2 + 1024) > this.filesize ? (int)this.filesize - n2 : 1024;
            this.readFromFile(this.readBuffer, this.readBufferStart, this.readBufferLen);
            return new Buffer(this.readBuffer, 0);
        }
        byte[] arrby = new byte[n3];
        this.readFromFile(arrby, n2, n3);
        return new Buffer(arrby, 0);
    }

    static class Buffer {
        byte[] data;
        int pos;
        int orig;

        Buffer(byte[] arrby, int n2) {
            this.orig = this.pos = n2;
            this.data = arrby;
        }

        int getInt(int n2) {
            n2 += this.orig;
            int n3 = this.data[n2++] & 0xFF;
            n3 <<= 8;
            n3 |= this.data[n2++] & 0xFF;
            n3 <<= 8;
            n3 |= this.data[n2++] & 0xFF;
            n3 <<= 8;
            return n3 |= this.data[n2++] & 0xFF;
        }

        int getInt() {
            int n2 = this.data[this.pos++] & 0xFF;
            n2 <<= 8;
            n2 |= this.data[this.pos++] & 0xFF;
            n2 <<= 8;
            n2 |= this.data[this.pos++] & 0xFF;
            n2 <<= 8;
            return n2 |= this.data[this.pos++] & 0xFF;
        }

        short getShort(int n2) {
            n2 += this.orig;
            int n3 = this.data[n2++] & 0xFF;
            n3 <<= 8;
            return (short)(n3 |= this.data[n2++] & 0xFF);
        }

        short getShort() {
            int n2 = this.data[this.pos++] & 0xFF;
            n2 <<= 8;
            return (short)(n2 |= this.data[this.pos++] & 0xFF);
        }

        char getChar(int n2) {
            n2 += this.orig;
            int n3 = this.data[n2++] & 0xFF;
            n3 <<= 8;
            return (char)(n3 |= this.data[n2++] & 0xFF);
        }

        char getChar() {
            int n2 = this.data[this.pos++] & 0xFF;
            n2 <<= 8;
            return (char)(n2 |= this.data[this.pos++] & 0xFF);
        }

        void position(int n2) {
            this.pos = this.orig + n2;
        }

        int capacity() {
            return this.data.length - this.orig;
        }

        byte get() {
            return this.data[this.pos++];
        }

        byte get(int n2) {
            return this.data[n2 += this.orig];
        }

        void skip(int n2) {
            this.pos += n2;
        }

        void get(int n2, byte[] arrby, int n3, int n4) {
            System.arraycopy(this.data, this.orig + n2, arrby, n3, n4);
        }
    }
}

