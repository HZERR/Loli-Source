/*
 * Decompiled with CFR 0.150.
 */
package LZMA;

import LZMA.CRangeDecoder;
import LZMA.LzmaException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LzmaInputStream
extends FilterInputStream {
    boolean isClosed = false;
    CRangeDecoder RangeDecoder;
    byte[] dictionary;
    int dictionarySize;
    int dictionaryPos;
    int GlobalPos;
    int rep0;
    int rep1;
    int rep2;
    int rep3;
    int lc;
    int lp;
    int pb;
    int State;
    boolean PreviousIsMatch;
    int RemainLen;
    int[] probs;
    byte[] uncompressed_buffer;
    int uncompressed_size;
    int uncompressed_offset;
    long GlobalNowPos;
    long GlobalOutSize;
    static final int LZMA_BASE_SIZE = 1846;
    static final int LZMA_LIT_SIZE = 768;
    static final int kBlockSize = 65536;
    static final int kNumStates = 12;
    static final int kStartPosModelIndex = 4;
    static final int kEndPosModelIndex = 14;
    static final int kNumFullDistances = 128;
    static final int kNumPosSlotBits = 6;
    static final int kNumLenToPosStates = 4;
    static final int kNumAlignBits = 4;
    static final int kAlignTableSize = 16;
    static final int kMatchMinLen = 2;
    static final int IsMatch = 0;
    static final int IsRep = 192;
    static final int IsRepG0 = 204;
    static final int IsRepG1 = 216;
    static final int IsRepG2 = 228;
    static final int IsRep0Long = 240;
    static final int PosSlot = 432;
    static final int SpecPos = 688;
    static final int Align = 802;
    static final int LenCoder = 818;
    static final int RepLenCoder = 1332;
    static final int Literal = 1846;

    public LzmaInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        this.readHeader();
        this.fill_buffer();
    }

    private void LzmaDecode(int n2) throws IOException {
        int n3;
        int n4 = (1 << this.pb) - 1;
        int n5 = (1 << this.lp) - 1;
        this.uncompressed_size = 0;
        if (this.RemainLen == -1) {
            return;
        }
        while (this.RemainLen > 0 && this.uncompressed_size < n2) {
            n3 = this.dictionaryPos - this.rep0;
            if (n3 < 0) {
                n3 += this.dictionarySize;
            }
            this.uncompressed_buffer[this.uncompressed_size++] = this.dictionary[this.dictionaryPos] = this.dictionary[n3];
            if (++this.dictionaryPos == this.dictionarySize) {
                this.dictionaryPos = 0;
            }
            --this.RemainLen;
        }
        byte by = this.dictionaryPos == 0 ? this.dictionary[this.dictionarySize - 1] : this.dictionary[this.dictionaryPos - 1];
        while (this.uncompressed_size < n2) {
            int n6;
            int n7;
            n3 = this.uncompressed_size + this.GlobalPos & n4;
            if (this.RangeDecoder.BitDecode(this.probs, 0 + (this.State << 4) + n3) == 0) {
                n7 = 1846 + 768 * (((this.uncompressed_size + this.GlobalPos & n5) << this.lc) + ((by & 0xFF) >> 8 - this.lc));
                this.State = this.State < 4 ? 0 : (this.State < 10 ? (this.State -= 3) : (this.State -= 6));
                if (this.PreviousIsMatch) {
                    n6 = this.dictionaryPos - this.rep0;
                    if (n6 < 0) {
                        n6 += this.dictionarySize;
                    }
                    byte by2 = this.dictionary[n6];
                    by = this.RangeDecoder.LzmaLiteralDecodeMatch(this.probs, n7, by2);
                    this.PreviousIsMatch = false;
                } else {
                    by = this.RangeDecoder.LzmaLiteralDecode(this.probs, n7);
                }
                this.uncompressed_buffer[this.uncompressed_size++] = by;
                this.dictionary[this.dictionaryPos] = by;
                if (++this.dictionaryPos != this.dictionarySize) continue;
                this.dictionaryPos = 0;
                continue;
            }
            this.PreviousIsMatch = true;
            if (this.RangeDecoder.BitDecode(this.probs, 192 + this.State) == 1) {
                if (this.RangeDecoder.BitDecode(this.probs, 204 + this.State) == 0) {
                    if (this.RangeDecoder.BitDecode(this.probs, 240 + (this.State << 4) + n3) == 0) {
                        if (this.uncompressed_size + this.GlobalPos == 0) {
                            throw new LzmaException("LZMA : Data Error");
                        }
                        this.State = this.State < 7 ? 9 : 11;
                        n7 = this.dictionaryPos - this.rep0;
                        if (n7 < 0) {
                            n7 += this.dictionarySize;
                        }
                        this.dictionary[this.dictionaryPos] = by = this.dictionary[n7];
                        if (++this.dictionaryPos == this.dictionarySize) {
                            this.dictionaryPos = 0;
                        }
                        this.uncompressed_buffer[this.uncompressed_size++] = by;
                        continue;
                    }
                } else {
                    if (this.RangeDecoder.BitDecode(this.probs, 216 + this.State) == 0) {
                        n7 = this.rep1;
                    } else {
                        if (this.RangeDecoder.BitDecode(this.probs, 228 + this.State) == 0) {
                            n7 = this.rep2;
                        } else {
                            n7 = this.rep3;
                            this.rep3 = this.rep2;
                        }
                        this.rep2 = this.rep1;
                    }
                    this.rep1 = this.rep0;
                    this.rep0 = n7;
                }
                this.RemainLen = this.RangeDecoder.LzmaLenDecode(this.probs, 1332, n3);
                this.State = this.State < 7 ? 8 : 11;
            } else {
                this.rep3 = this.rep2;
                this.rep2 = this.rep1;
                this.rep1 = this.rep0;
                this.State = this.State < 7 ? 7 : 10;
                this.RemainLen = this.RangeDecoder.LzmaLenDecode(this.probs, 818, n3);
                n7 = this.RangeDecoder.BitTreeDecode(this.probs, 432 + ((this.RemainLen < 4 ? this.RemainLen : 3) << 6), 6);
                if (n7 >= 4) {
                    n6 = (n7 >> 1) - 1;
                    this.rep0 = (2 | n7 & 1) << n6;
                    if (n7 < 14) {
                        this.rep0 += this.RangeDecoder.ReverseBitTreeDecode(this.probs, 688 + this.rep0 - n7 - 1, n6);
                    } else {
                        this.rep0 += this.RangeDecoder.DecodeDirectBits(n6 - 4) << 4;
                        this.rep0 += this.RangeDecoder.ReverseBitTreeDecode(this.probs, 802, 4);
                    }
                } else {
                    this.rep0 = n7;
                }
                ++this.rep0;
            }
            if (this.rep0 == 0) {
                this.RemainLen = -1;
                break;
            }
            if (this.rep0 > this.uncompressed_size + this.GlobalPos) {
                throw new LzmaException("LZMA : Data Error");
            }
            this.RemainLen += 2;
            do {
                if ((n7 = this.dictionaryPos - this.rep0) < 0) {
                    n7 += this.dictionarySize;
                }
                this.dictionary[this.dictionaryPos] = by = this.dictionary[n7];
                if (++this.dictionaryPos == this.dictionarySize) {
                    this.dictionaryPos = 0;
                }
                this.uncompressed_buffer[this.uncompressed_size++] = by;
                --this.RemainLen;
            } while (this.RemainLen > 0 && this.uncompressed_size < n2);
        }
        this.GlobalPos += this.uncompressed_size;
    }

    private void fill_buffer() throws IOException {
        if (this.GlobalNowPos < this.GlobalOutSize) {
            this.uncompressed_offset = 0;
            long l2 = this.GlobalOutSize - this.GlobalNowPos;
            int n2 = l2 > 65536L ? 65536 : (int)l2;
            this.LzmaDecode(n2);
            if (this.uncompressed_size == 0) {
                this.GlobalOutSize = this.GlobalNowPos;
            } else {
                this.GlobalNowPos += (long)this.uncompressed_size;
            }
        }
    }

    private void readHeader() throws IOException {
        int n2;
        int n3;
        int n4;
        byte[] arrby = new byte[5];
        if (5 != this.in.read(arrby)) {
            throw new LzmaException("LZMA header corrupted : Properties error");
        }
        this.GlobalOutSize = 0L;
        for (n4 = 0; n4 < 8; ++n4) {
            n3 = this.in.read();
            if (n3 == -1) {
                throw new LzmaException("LZMA header corrupted : Size error");
            }
            this.GlobalOutSize += (long)n3 << n4 * 8;
        }
        if (this.GlobalOutSize == -1L) {
            this.GlobalOutSize = Long.MAX_VALUE;
        }
        if ((n4 = arrby[0] & 0xFF) >= 225) {
            throw new LzmaException("LZMA header corrupted : Properties error");
        }
        this.pb = 0;
        while (n4 >= 45) {
            ++this.pb;
            n4 -= 45;
        }
        this.lp = 0;
        while (n4 >= 9) {
            ++this.lp;
            n4 -= 9;
        }
        this.lc = n4;
        n3 = 1846 + (768 << this.lc + this.lp);
        this.probs = new int[n3];
        this.dictionarySize = 0;
        for (n2 = 0; n2 < 4; ++n2) {
            this.dictionarySize += (arrby[1 + n2] & 0xFF) << n2 * 8;
        }
        this.dictionary = new byte[this.dictionarySize];
        if (this.dictionary == null) {
            throw new LzmaException("LZMA : can't allocate");
        }
        n2 = 1846 + (768 << this.lc + this.lp);
        this.RangeDecoder = new CRangeDecoder(this.in);
        this.dictionaryPos = 0;
        this.GlobalPos = 0;
        this.rep3 = 1;
        this.rep2 = 1;
        this.rep1 = 1;
        this.rep0 = 1;
        this.State = 0;
        this.PreviousIsMatch = false;
        this.RemainLen = 0;
        this.dictionary[this.dictionarySize - 1] = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.probs[i2] = 1024;
        }
        this.uncompressed_buffer = new byte[65536];
        this.uncompressed_size = 0;
        this.uncompressed_offset = 0;
        this.GlobalNowPos = 0L;
    }

    public int read(byte[] arrby, int n2, int n3) throws IOException {
        if (this.isClosed) {
            throw new IOException("stream closed");
        }
        if ((n2 | n3 | n2 + n3 | arrby.length - (n2 + n3)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (n3 == 0) {
            return 0;
        }
        if (this.uncompressed_offset == this.uncompressed_size) {
            this.fill_buffer();
        }
        if (this.uncompressed_offset == this.uncompressed_size) {
            return -1;
        }
        int n4 = Math.min(n3, this.uncompressed_size - this.uncompressed_offset);
        System.arraycopy(this.uncompressed_buffer, this.uncompressed_offset, arrby, n2, n4);
        this.uncompressed_offset += n4;
        return n4;
    }

    public void close() throws IOException {
        this.isClosed = true;
        super.close();
    }
}

