/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.iio.common.PushbroomScaler;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class SmoothMinifier
implements PushbroomScaler {
    protected int sourceWidth;
    protected int sourceHeight;
    protected int numBands;
    protected int destWidth;
    protected int destHeight;
    protected double scaleY;
    protected ByteBuffer destBuf;
    protected int boxHeight;
    protected byte[][] sourceData;
    protected int[] leftPoints;
    protected int[] rightPoints;
    protected int[] topPoints;
    protected int[] bottomPoints;
    protected int sourceLine;
    protected int sourceDataLine;
    protected int destLine;
    protected int[] tmpBuf;

    SmoothMinifier(int n2, int n3, int n4, int n5, int n6) {
        int n7;
        int n8;
        if (n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0 || n6 <= 0 || n5 > n2 || n6 > n3) {
            throw new IllegalArgumentException();
        }
        this.sourceWidth = n2;
        this.sourceHeight = n3;
        this.numBands = n4;
        this.destWidth = n5;
        this.destHeight = n6;
        this.destBuf = ByteBuffer.wrap(new byte[n6 * n5 * n4]);
        double d2 = (double)n2 / (double)n5;
        this.scaleY = (double)n3 / (double)n6;
        int n9 = (n2 + n5 - 1) / n5;
        this.boxHeight = (n3 + n6 - 1) / n6;
        int n10 = n9 / 2;
        int n11 = n9 - n10 - 1;
        int n12 = this.boxHeight / 2;
        int n13 = this.boxHeight - n12 - 1;
        this.sourceData = new byte[this.boxHeight][n5 * n4];
        this.leftPoints = new int[n5];
        this.rightPoints = new int[n5];
        for (n8 = 0; n8 < n5; ++n8) {
            n7 = (int)((double)n8 * d2);
            this.leftPoints[n8] = n7 - n10;
            this.rightPoints[n8] = n7 + n11;
        }
        this.topPoints = new int[n6];
        this.bottomPoints = new int[n6];
        for (n8 = 0; n8 < n6; ++n8) {
            n7 = (int)((double)n8 * this.scaleY);
            this.topPoints[n8] = n7 - n12;
            this.bottomPoints[n8] = n7 + n13;
        }
        this.sourceLine = 0;
        this.sourceDataLine = 0;
        this.destLine = 0;
        this.tmpBuf = new int[n5 * n4];
    }

    @Override
    public ByteBuffer getDestination() {
        return this.destBuf;
    }

    @Override
    public boolean putSourceScanline(byte[] arrby, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        if (n2 < 0) {
            throw new IllegalArgumentException("off < 0!");
        }
        if (this.numBands == 1) {
            n9 = arrby[n2] & 0xFF;
            n8 = arrby[n2 + this.sourceWidth - 1] & 0xFF;
            for (n7 = 0; n7 < this.destWidth; ++n7) {
                n6 = 0;
                n5 = this.rightPoints[n7];
                for (n4 = this.leftPoints[n7]; n4 <= n5; ++n4) {
                    if (n4 < 0) {
                        n6 += n9;
                        continue;
                    }
                    if (n4 >= this.sourceWidth) {
                        n6 += n8;
                        continue;
                    }
                    n6 += arrby[n2 + n4] & 0xFF;
                }
                this.sourceData[this.sourceDataLine][n7] = (byte)(n6 /= n5 - this.leftPoints[n7] + 1);
            }
        } else {
            n9 = n2 + (this.sourceWidth - 1) * this.numBands;
            for (n8 = 0; n8 < this.destWidth; ++n8) {
                n7 = this.leftPoints[n8];
                n6 = this.rightPoints[n8];
                n5 = n6 - n7 + 1;
                n4 = n8 * this.numBands;
                for (n3 = 0; n3 < this.numBands; ++n3) {
                    int n10 = arrby[n2 + n3] & 0xFF;
                    int n11 = arrby[n9 + n3] & 0xFF;
                    int n12 = 0;
                    for (int i2 = n7; i2 <= n6; ++i2) {
                        if (i2 < 0) {
                            n12 += n10;
                            continue;
                        }
                        if (i2 >= this.sourceWidth) {
                            n12 += n11;
                            continue;
                        }
                        n12 += arrby[n2 + i2 * this.numBands + n3] & 0xFF;
                    }
                    this.sourceData[this.sourceDataLine][n4 + n3] = (byte)(n12 /= n5);
                }
            }
        }
        if (this.sourceLine == this.bottomPoints[this.destLine] || this.destLine == this.destHeight - 1 && this.sourceLine == this.sourceHeight - 1) {
            assert (this.destBuf.hasArray()) : "destBuf.hasArray() == false => destBuf is direct";
            byte[] arrby2 = this.destBuf.array();
            n8 = this.destLine * this.destWidth * this.numBands;
            Arrays.fill(this.tmpBuf, 0);
            for (n7 = this.topPoints[this.destLine]; n7 <= this.bottomPoints[this.destLine]; ++n7) {
                n6 = 0;
                n6 = n7 < 0 ? 0 - this.sourceLine + this.sourceDataLine : (n7 >= this.sourceHeight ? (this.sourceHeight - 1 - this.sourceLine + this.sourceDataLine) % this.boxHeight : (n7 - this.sourceLine + this.sourceDataLine) % this.boxHeight);
                if (n6 < 0) {
                    n6 += this.boxHeight;
                }
                byte[] arrby3 = this.sourceData[n6];
                n4 = arrby3.length;
                for (n3 = 0; n3 < n4; ++n3) {
                    int n13 = n3;
                    this.tmpBuf[n13] = this.tmpBuf[n13] + (arrby3[n3] & 0xFF);
                }
            }
            n7 = this.tmpBuf.length;
            for (n6 = 0; n6 < n7; ++n6) {
                arrby2[n8 + n6] = (byte)(this.tmpBuf[n6] / this.boxHeight);
            }
            if (this.destLine < this.destHeight - 1) {
                ++this.destLine;
            }
        }
        if (++this.sourceLine != this.sourceHeight) {
            this.sourceDataLine = (this.sourceDataLine + 1) % this.boxHeight;
        }
        return this.destLine == this.destHeight;
    }
}

