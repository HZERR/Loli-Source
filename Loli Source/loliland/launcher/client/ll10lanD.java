/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i10lIIOllANd;
import loliland.launcher.client.l0IlILANd;
import loliland.launcher.client.ll10II0LaND;

public final class ll10lanD {
    private static final String I1O1I1LaNd = "Win32_Process";
    private static final String OOOIilanD = "Process";
    private static final String lI00OlAND = "Win32_Process WHERE NOT Name LIKE\"%_Total\"";

    private ll10lanD() {
    }

    public static O1IiIiI1LAND I1O1I1LaNd() {
        return i10lIIOllANd.I1O1I1LaNd(ll10II0LaND.class, OOOIilanD, lI00OlAND);
    }

    public static O1IiIiI1LAND OOOIilanD() {
        return i10lIIOllANd.I1O1I1LaNd(l0IlILANd.class, OOOIilanD, I1O1I1LaNd);
    }
}

