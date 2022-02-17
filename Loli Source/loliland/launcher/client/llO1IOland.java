/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.mac.IOKit;
import loliland.launcher.client.l1i0iO00LAnd;
import loliland.launcher.client.llllil1LaND;

final class llO1IOland
extends llllil1LaND {
    private int I1O1I1LaNd = 0;

    llO1IOland() {
    }

    @Override
    public double lli0OiIlAND() {
        IOKit.IOConnect iOConnect = l1i0iO00LAnd.I1O1I1LaNd();
        double d2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, "TC0P");
        l1i0iO00LAnd.I1O1I1LaNd(iOConnect);
        if (d2 > 0.0) {
            return d2;
        }
        return 0.0;
    }

    @Override
    public int[] li0iOILAND() {
        IOKit.IOConnect iOConnect = l1i0iO00LAnd.I1O1I1LaNd();
        if (this.I1O1I1LaNd == 0) {
            this.I1O1I1LaNd = (int)l1i0iO00LAnd.OOOIilanD(iOConnect, "FNum");
        }
        int[] arrn = new int[this.I1O1I1LaNd];
        for (int i2 = 0; i2 < this.I1O1I1LaNd; ++i2) {
            arrn[i2] = (int)l1i0iO00LAnd.I1O1I1LaNd(iOConnect, String.format("F%dAc", i2));
        }
        l1i0iO00LAnd.I1O1I1LaNd(iOConnect);
        return arrn;
    }

    @Override
    public double O1il1llOLANd() {
        IOKit.IOConnect iOConnect = l1i0iO00LAnd.I1O1I1LaNd();
        double d2 = l1i0iO00LAnd.I1O1I1LaNd(iOConnect, "VC0C") / 1000.0;
        l1i0iO00LAnd.I1O1I1LaNd(iOConnect);
        return d2;
    }
}

