/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Arrays;

public enum O0I1il01lANd {
    I1O1I1LaNd(1),
    OOOIilanD(2),
    lI00OlAND(3),
    lli0OiIlAND(4),
    li0iOILAND(5),
    O1il1llOLANd(6),
    Oill1LAnD(7);

    private final int lIOILand;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private O0I1il01lANd() {
        void var3_1;
        this.lIOILand = var3_1;
    }

    public int I1O1I1LaNd() {
        return this.lIOILand;
    }

    public static O0I1il01lANd I1O1I1LaNd(int n2) {
        return Arrays.stream(O0I1il01lANd.values()).filter(o0I1il01lANd -> o0I1il01lANd.I1O1I1LaNd() == n2).findFirst().orElse(lli0OiIlAND);
    }
}

