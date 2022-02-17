/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm;

public class ByteVector {
    byte[] data;
    int length;

    public ByteVector() {
        this.data = new byte[64];
    }

    public ByteVector(int initialSize) {
        this.data = new byte[initialSize];
    }

    public ByteVector putByte(int b2) {
        int length = this.length;
        if (length + 1 > this.data.length) {
            this.enlarge(1);
        }
        this.data[length++] = (byte)b2;
        this.length = length;
        return this;
    }

    ByteVector put11(int b1, int b2) {
        int length = this.length;
        if (length + 2 > this.data.length) {
            this.enlarge(2);
        }
        byte[] data = this.data;
        data[length++] = (byte)b1;
        data[length++] = (byte)b2;
        this.length = length;
        return this;
    }

    public ByteVector putShort(int s2) {
        int length = this.length;
        if (length + 2 > this.data.length) {
            this.enlarge(2);
        }
        byte[] data = this.data;
        data[length++] = (byte)(s2 >>> 8);
        data[length++] = (byte)s2;
        this.length = length;
        return this;
    }

    ByteVector put12(int b2, int s2) {
        int length = this.length;
        if (length + 3 > this.data.length) {
            this.enlarge(3);
        }
        byte[] data = this.data;
        data[length++] = (byte)b2;
        data[length++] = (byte)(s2 >>> 8);
        data[length++] = (byte)s2;
        this.length = length;
        return this;
    }

    public ByteVector putInt(int i2) {
        int length = this.length;
        if (length + 4 > this.data.length) {
            this.enlarge(4);
        }
        byte[] data = this.data;
        data[length++] = (byte)(i2 >>> 24);
        data[length++] = (byte)(i2 >>> 16);
        data[length++] = (byte)(i2 >>> 8);
        data[length++] = (byte)i2;
        this.length = length;
        return this;
    }

    public ByteVector putLong(long l2) {
        int length = this.length;
        if (length + 8 > this.data.length) {
            this.enlarge(8);
        }
        byte[] data = this.data;
        int i2 = (int)(l2 >>> 32);
        data[length++] = (byte)(i2 >>> 24);
        data[length++] = (byte)(i2 >>> 16);
        data[length++] = (byte)(i2 >>> 8);
        data[length++] = (byte)i2;
        i2 = (int)l2;
        data[length++] = (byte)(i2 >>> 24);
        data[length++] = (byte)(i2 >>> 16);
        data[length++] = (byte)(i2 >>> 8);
        data[length++] = (byte)i2;
        this.length = length;
        return this;
    }

    public ByteVector putUTF8(String s2) {
        int len = this.length;
        int charLength = s2.length();
        if (len + 2 + charLength > this.data.length) {
            this.enlarge(2 + charLength);
        }
        byte[] data = this.data;
        data[len++] = (byte)(charLength >>> 8);
        data[len++] = (byte)charLength;
        for (int i2 = 0; i2 < charLength; ++i2) {
            int j2;
            char c2 = s2.charAt(i2);
            if (c2 >= '\u0001' && c2 <= '\u007f') {
                data[len++] = (byte)c2;
                continue;
            }
            int byteLength = i2;
            for (j2 = i2; j2 < charLength; ++j2) {
                c2 = s2.charAt(j2);
                if (c2 >= '\u0001' && c2 <= '\u007f') {
                    ++byteLength;
                    continue;
                }
                if (c2 > '\u07ff') {
                    byteLength += 3;
                    continue;
                }
                byteLength += 2;
            }
            data[this.length] = (byte)(byteLength >>> 8);
            data[this.length + 1] = (byte)byteLength;
            if (this.length + 2 + byteLength > data.length) {
                this.length = len;
                this.enlarge(2 + byteLength);
                data = this.data;
            }
            for (j2 = i2; j2 < charLength; ++j2) {
                c2 = s2.charAt(j2);
                if (c2 >= '\u0001' && c2 <= '\u007f') {
                    data[len++] = (byte)c2;
                    continue;
                }
                if (c2 > '\u07ff') {
                    data[len++] = (byte)(0xE0 | c2 >> 12 & 0xF);
                    data[len++] = (byte)(0x80 | c2 >> 6 & 0x3F);
                    data[len++] = (byte)(0x80 | c2 & 0x3F);
                    continue;
                }
                data[len++] = (byte)(0xC0 | c2 >> 6 & 0x1F);
                data[len++] = (byte)(0x80 | c2 & 0x3F);
            }
            break;
        }
        this.length = len;
        return this;
    }

    public ByteVector putByteArray(byte[] b2, int off, int len) {
        if (this.length + len > this.data.length) {
            this.enlarge(len);
        }
        if (b2 != null) {
            System.arraycopy(b2, off, this.data, this.length, len);
        }
        this.length += len;
        return this;
    }

    private void enlarge(int size) {
        int length1 = 2 * this.data.length;
        int length2 = this.length + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        System.arraycopy(this.data, 0, newData, 0, this.length);
        this.data = newData;
    }
}

