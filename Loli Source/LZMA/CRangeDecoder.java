/*
 * Decompiled with CFR 0.150.
 */
package LZMA;

import LZMA.LzmaException;
import java.io.IOException;
import java.io.InputStream;

class CRangeDecoder {
    static final int kNumTopBits = 24;
    static final int kTopValue = 0x1000000;
    static final int kTopValueMask = -16777216;
    static final int kNumBitModelTotalBits = 11;
    static final int kBitModelTotal = 2048;
    static final int kNumMoveBits = 5;
    InputStream inStream;
    int Range;
    int Code;
    byte[] buffer = new byte[16384];
    int buffer_size;
    int buffer_ind;
    static final int kNumPosBitsMax = 4;
    static final int kNumPosStatesMax = 16;
    static final int kLenNumLowBits = 3;
    static final int kLenNumLowSymbols = 8;
    static final int kLenNumMidBits = 3;
    static final int kLenNumMidSymbols = 8;
    static final int kLenNumHighBits = 8;
    static final int kLenNumHighSymbols = 256;
    static final int LenChoice = 0;
    static final int LenChoice2 = 1;
    static final int LenLow = 2;
    static final int LenMid = 130;
    static final int LenHigh = 258;
    static final int kNumLenProbs = 514;

    CRangeDecoder(InputStream inputStream) throws IOException {
        this.inStream = inputStream;
        this.Code = 0;
        this.Range = -1;
        for (int i2 = 0; i2 < 5; ++i2) {
            this.Code = this.Code << 8 | this.Readbyte();
        }
    }

    int Readbyte() throws IOException {
        if (this.buffer_size == this.buffer_ind) {
            this.buffer_size = this.inStream.read(this.buffer);
            this.buffer_ind = 0;
            if (this.buffer_size < 1) {
                throw new LzmaException("LZMA : Data Error");
            }
        }
        return this.buffer[this.buffer_ind++] & 0xFF;
    }

    int DecodeDirectBits(int n2) throws IOException {
        int n3 = 0;
        for (int i2 = n2; i2 > 0; --i2) {
            this.Range >>>= 1;
            int n4 = this.Code - this.Range >>> 31;
            this.Code -= this.Range & n4 - 1;
            n3 = n3 << 1 | 1 - n4;
            if (this.Range >= 0x1000000) continue;
            this.Code = this.Code << 8 | this.Readbyte();
            this.Range <<= 8;
        }
        return n3;
    }

    int BitDecode(int[] arrn, int n2) throws IOException {
        int n3 = (this.Range >>> 11) * arrn[n2];
        if (((long)this.Code & 0xFFFFFFFFL) < ((long)n3 & 0xFFFFFFFFL)) {
            this.Range = n3;
            int n4 = n2;
            arrn[n4] = arrn[n4] + (2048 - arrn[n2] >>> 5);
            if ((this.Range & 0xFF000000) == 0) {
                this.Code = this.Code << 8 | this.Readbyte();
                this.Range <<= 8;
            }
            return 0;
        }
        this.Range -= n3;
        this.Code -= n3;
        int n5 = n2;
        arrn[n5] = arrn[n5] - (arrn[n2] >>> 5);
        if ((this.Range & 0xFF000000) == 0) {
            this.Code = this.Code << 8 | this.Readbyte();
            this.Range <<= 8;
        }
        return 1;
    }

    int BitTreeDecode(int[] arrn, int n2, int n3) throws IOException {
        int n4 = 1;
        for (int i2 = n3; i2 > 0; --i2) {
            n4 = n4 + n4 + this.BitDecode(arrn, n2 + n4);
        }
        return n4 - (1 << n3);
    }

    int ReverseBitTreeDecode(int[] arrn, int n2, int n3) throws IOException {
        int n4 = 1;
        int n5 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n6 = this.BitDecode(arrn, n2 + n4);
            n4 = n4 + n4 + n6;
            n5 |= n6 << i2;
        }
        return n5;
    }

    byte LzmaLiteralDecode(int[] arrn, int n2) throws IOException {
        int n3 = 1;
        while ((n3 = n3 + n3 | this.BitDecode(arrn, n2 + n3)) < 256) {
        }
        return (byte)n3;
    }

    byte LzmaLiteralDecodeMatch(int[] arrn, int n2, byte by) throws IOException {
        int n3 = 1;
        do {
            int n4 = by >> 7 & 1;
            by = (byte)(by << 1);
            int n5 = this.BitDecode(arrn, n2 + (1 + n4 << 8) + n3);
            n3 = n3 << 1 | n5;
            if (n4 == n5) continue;
            while (n3 < 256) {
                n3 = n3 + n3 | this.BitDecode(arrn, n2 + n3);
            }
            break;
        } while (n3 < 256);
        return (byte)n3;
    }

    int LzmaLenDecode(int[] arrn, int n2, int n3) throws IOException {
        if (this.BitDecode(arrn, n2 + 0) == 0) {
            return this.BitTreeDecode(arrn, n2 + 2 + (n3 << 3), 3);
        }
        if (this.BitDecode(arrn, n2 + 1) == 0) {
            return 8 + this.BitTreeDecode(arrn, n2 + 130 + (n3 << 3), 3);
        }
        return 16 + this.BitTreeDecode(arrn, n2 + 258, 8);
    }
}

