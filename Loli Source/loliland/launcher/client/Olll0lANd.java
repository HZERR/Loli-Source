/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.OI1IILand;

public enum Olll0lANd implements OI1IILand
{
    I1O1I1LaNd("^*_Total"),
    OOOIilanD("% User Time"),
    lI00OlAND("% Privileged Time"),
    lli0OiIlAND("Elapsed Time"),
    li0iOILAND("Priority Current"),
    O1il1llOLANd("Start Address"),
    Oill1LAnD("Thread State"),
    lIOILand("Thread Wait Reason"),
    lil0liLand("ID Process"),
    iilIi1laND("ID Thread"),
    lli011lLANd("Context Switches/sec");

    private final String l0illAND;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private Olll0lANd() {
        void var3_1;
        this.l0illAND = var3_1;
    }

    @Override
    public String I1O1I1LaNd() {
        return this.l0illAND;
    }
}

