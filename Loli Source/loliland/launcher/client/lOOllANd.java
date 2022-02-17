/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;

public final class lOOllANd {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private lOOllANd() {
    }

    public static Perfstat.perfstat_memory_total_t I1O1I1LaNd() {
        Perfstat.perfstat_memory_total_t perfstat_memory_total_t2 = new Perfstat.perfstat_memory_total_t();
        int n2 = I1O1I1LaNd.perfstat_memory_total(null, perfstat_memory_total_t2, perfstat_memory_total_t2.size(), 1);
        if (n2 > 0) {
            return perfstat_memory_total_t2;
        }
        return new Perfstat.perfstat_memory_total_t();
    }
}

