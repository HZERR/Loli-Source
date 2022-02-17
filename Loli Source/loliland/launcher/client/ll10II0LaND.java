/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.OI1IILand;

public enum ll10II0LaND implements OI1IILand
{
    I1O1I1LaNd("^*_Total"),
    OOOIilanD("Priority Base"),
    lI00OlAND("Elapsed Time"),
    lli0OiIlAND("ID Process"),
    li0iOILAND("Creating Process ID"),
    O1il1llOLANd("IO Read Bytes/sec"),
    Oill1LAnD("IO Write Bytes/sec"),
    lIOILand("Working Set - Private"),
    lil0liLand("Page Faults/sec");

    private final String iilIi1laND;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private ll10II0LaND() {
        void var3_1;
        this.iilIi1laND = var3_1;
    }

    @Override
    public String I1O1I1LaNd() {
        return this.iilIi1laND;
    }
}

