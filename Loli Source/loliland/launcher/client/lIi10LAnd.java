/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.llllil1LaND;

final class lIi10LAnd
extends llllil1LaND {
    private final Supplier I1O1I1LaNd;

    lIi10LAnd(Supplier supplier) {
        this.I1O1I1LaNd = supplier;
    }

    @Override
    public double lli0OiIlAND() {
        return 0.0;
    }

    @Override
    public int[] li0iOILAND() {
        int n2 = 0;
        for (String string : (List)this.I1O1I1LaNd.get()) {
            if (!string.contains("Air Mover")) continue;
            ++n2;
        }
        return new int[n2];
    }

    @Override
    public double O1il1llOLANd() {
        return 0.0;
    }
}

