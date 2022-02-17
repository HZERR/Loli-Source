/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Arrays;
import loliland.launcher.client.l0O0IlaND;
import loliland.launcher.client.l0OI0lAnd;

public abstract class IOll1OIlaND
implements l0OI0lAnd {
    private final byte[] I1O1I1LaNd;

    protected IOll1OIlaND(byte[] arrby) {
        this.I1O1I1LaNd = Arrays.copyOf(arrby, arrby.length);
    }

    @Override
    public byte[] I1O1I1LaNd() {
        return Arrays.copyOf(this.I1O1I1LaNd, this.I1O1I1LaNd.length);
    }

    public String toString() {
        return l0O0IlaND.lO110l1LANd(this.I1O1I1LaNd);
    }
}

