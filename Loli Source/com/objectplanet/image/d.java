/*
 * Decompiled with CFR 0.150.
 */
package com.objectplanet.image;

import com.objectplanet.image.b;
import java.awt.image.ColorModel;
import java.io.DataOutputStream;
import java.io.IOException;

public class d
extends b {
    d(int n2) {
        super(n2);
        this.int = 0;
    }

    public void setDimensions(int n2, int n3) {
        this.try.reset();
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(this.try);
            dataOutputStream.write("IHDR".getBytes());
            dataOutputStream.writeInt(n2);
            dataOutputStream.writeInt(n3);
            dataOutputStream.write(16);
            dataOutputStream.write(this.int);
            dataOutputStream.write(0);
            dataOutputStream.write(0);
            dataOutputStream.write(0);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void setPixels(int n2, int n3, int n4, int n5, ColorModel colorModel, int[] arrn, int n6, int n7) {
        if (colorModel == null || arrn == null) {
            return;
        }
        if (this.byte == null || this.byte.length < n7 * 2 + 1) {
            this.byte = new byte[n7 * 2 + 1];
        }
        int n8 = 0;
        for (int i2 = 0; i2 < n5; ++i2) {
            this.byte[0] = 0;
            int n9 = 1;
            for (int i3 = 0; i3 < n7; ++i3) {
                int n10 = arrn[n8] >> 16 & 0xFF;
                int n11 = arrn[n8] >> 8 & 0xFF;
                int n12 = arrn[n8] & 0xFF;
                if (n10 != n11 || n10 != n12) {
                    int n13 = (int)((double)n10 * 0.3 + (double)n11 * 0.59 + (double)n12 * 0.11);
                    this.byte[n9] = (byte)n13;
                } else {
                    this.byte[n9] = (byte)n10;
                }
                n9 += 2;
                ++n8;
            }
            try {
                this.if.write(this.byte, 0, n7 * 2 + 1);
                continue;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }
}

