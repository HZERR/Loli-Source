/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.iio.common.PushbroomScaler;
import java.nio.ByteBuffer;

public class RoughScaler
implements PushbroomScaler {
    protected int numBands;
    protected int destWidth;
    protected int destHeight;
    protected double scaleY;
    protected ByteBuffer destBuf;
    protected int[] colPositions;
    protected int sourceLine;
    protected int nextSourceLine;
    protected int destLine;

    public RoughScaler(int n2, int n3, int n4, int n5, int n6) {
        if (n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0 || n6 <= 0) {
            throw new IllegalArgumentException();
        }
        this.numBands = n4;
        this.destWidth = n5;
        this.destHeight = n6;
        this.destBuf = ByteBuffer.wrap(new byte[n6 * n5 * n4]);
        double d2 = (double)n2 / (double)n5;
        this.scaleY = (double)n3 / (double)n6;
        this.colPositions = new int[n5];
        for (int i2 = 0; i2 < n5; ++i2) {
            int n7 = (int)(((double)i2 + 0.5) * d2);
            this.colPositions[i2] = n7 * n4;
        }
        this.sourceLine = 0;
        this.destLine = 0;
        this.nextSourceLine = (int)(0.5 * this.scaleY);
    }

    @Override
    public ByteBuffer getDestination() {
        return this.destBuf;
    }

    @Override
    public boolean putSourceScanline(byte[] arrby, int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("off < 0!");
        }
        if (this.destLine < this.destHeight) {
            if (this.sourceLine == this.nextSourceLine) {
                int n3;
                assert (this.destBuf.hasArray()) : "destBuf.hasArray() == false => destBuf is direct";
                byte[] arrby2 = this.destBuf.array();
                int n4 = n3 = this.destLine * this.destWidth * this.numBands;
                for (int i2 = 0; i2 < this.destWidth; ++i2) {
                    int n5 = n2 + this.colPositions[i2];
                    for (int i3 = 0; i3 < this.numBands; ++i3) {
                        arrby2[n4++] = arrby[n5 + i3];
                    }
                }
                while ((int)(((double)(++this.destLine) + 0.5) * this.scaleY) == this.sourceLine) {
                    System.arraycopy(arrby2, n3, arrby2, n4, this.destWidth * this.numBands);
                    n4 += this.destWidth * this.numBands;
                }
                this.nextSourceLine = (int)(((double)this.destLine + 0.5) * this.scaleY);
            }
            ++this.sourceLine;
        }
        return this.destLine == this.destHeight;
    }
}

