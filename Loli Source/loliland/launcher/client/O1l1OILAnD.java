/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i10lIIOllANd;
import loliland.launcher.client.i1OilAnD;

public final class O1l1OILAnD {
    private static final String I1O1I1LaNd = "PhysicalDisk";
    private static final String OOOIilanD = "Win32_PerfRawData_PerfDisk_PhysicalDisk WHERE NOT Name=\"_Total\"";

    private O1l1OILAnD() {
    }

    public static O1IiIiI1LAND I1O1I1LaNd() {
        return i10lIIOllANd.I1O1I1LaNd(i1OilAnD.class, I1O1I1LaNd, OOOIilanD);
    }
}

