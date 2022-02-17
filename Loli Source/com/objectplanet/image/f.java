/*
 * Decompiled with CFR 0.150.
 */
package com.objectplanet.image;

import com.objectplanet.image.d;
import java.awt.image.ColorModel;
import java.io.IOException;

public class f
extends d {
    f(int n2) {
        super(n2);
        this.int = 4;
    }

    public void setPixels(int n2, int n3, int n4, int n5, ColorModel colorModel, int[] arrn, int n6, int n7) {
        if (colorModel == null || arrn == null) {
            return;
        }
        if (this.byte == null || this.byte.length < n7 * 2 + 1) {
            this.byte = new byte[n7 * 4 + 1];
        }
        int n8 = 0;
        for (int i2 = 0; i2 < n5; ++i2) {
            this.byte[0] = 0;
            int n9 = 1;
            for (int i3 = 0; i3 < n7; ++i3) {
                int n10 = arrn[n8] >> 24 & 0xFF;
                int n11 = arrn[n8] >> 16 & 0xFF;
                int n12 = arrn[n8] >> 8 & 0xFF;
                int n13 = arrn[n8] & 0xFF;
                if (n11 != n12 || n11 != n13) {
                    int n14 = (int)((double)n11 * 0.3 + (double)n12 * 0.59 + (double)n13 * 0.11);
                    this.byte[n9] = (byte)n14;
                } else {
                    this.byte[n9] = (byte)n11;
                }
                this.byte[n9 + 2] = (byte)(n10 % 256);
                this.byte[n9 + 3] = (byte)(n10 / 256);
                n9 += 4;
                ++n8;
            }
            try {
                this.if.write(this.byte, 0, n7 * 4 + 1);
                continue;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }
}

