/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;

public final class Iil0IOlAND {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private Iil0IOlAND() {
    }

    public static Perfstat.perfstat_disk_t[] I1O1I1LaNd() {
        Perfstat.perfstat_disk_t[] arrperfstat_disk_t;
        Perfstat.perfstat_id_t perfstat_id_t2;
        int n2;
        Perfstat.perfstat_disk_t perfstat_disk_t2 = new Perfstat.perfstat_disk_t();
        int n3 = I1O1I1LaNd.perfstat_disk(null, null, perfstat_disk_t2.size(), 0);
        if (n3 > 0 && (n2 = I1O1I1LaNd.perfstat_disk(perfstat_id_t2 = new Perfstat.perfstat_id_t(), arrperfstat_disk_t = (Perfstat.perfstat_disk_t[])perfstat_disk_t2.toArray(n3), perfstat_disk_t2.size(), n3)) > 0) {
            return arrperfstat_disk_t;
        }
        return new Perfstat.perfstat_disk_t[0];
    }
}

