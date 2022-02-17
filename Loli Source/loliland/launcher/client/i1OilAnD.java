/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.OI1IILand;

public enum i1OilAnD implements OI1IILand
{
    I1O1I1LaNd("^_Total"),
    OOOIilanD("Disk Reads/sec"),
    lI00OlAND("Disk Read Bytes/sec"),
    lli0OiIlAND("Disk Writes/sec"),
    li0iOILAND("Disk Write Bytes/sec"),
    O1il1llOLANd("Current Disk Queue Length"),
    Oill1LAnD("% Disk Time");

    private final String lIOILand;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private i1OilAnD() {
        void var3_1;
        this.lIOILand = var3_1;
    }

    @Override
    public String I1O1I1LaNd() {
        return this.lIOILand;
    }
}

