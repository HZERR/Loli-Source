/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import java.io.DataOutput;
import java.io.IOException;

class r {
    int a = -1;

    r() {
    }

    r(int n2) {
        this.a(n2);
    }

    void a(int n2) {
        if (n2 < 0 || n2 > 65535) {
            n2 = 0;
        }
        this.a = n2;
    }

    void a(DataOutput dataOutput) throws IOException {
        if (this.a > -1) {
            dataOutput.writeLong(2449689153111348035L);
            dataOutput.writeLong(4706337692427748097L);
            dataOutput.writeLong(0x21FE096769L | (long)(this.a & 0xFF) << 56 | (long)(this.a >> 8 & 0xFF) << 48);
            dataOutput.writeLong(7364628274668654592L);
        } else {
            dataOutput.writeLong(2449408885625599082L);
            dataOutput.writeLong(7311150136474738688L);
        }
    }
}

