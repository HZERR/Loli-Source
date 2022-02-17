/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;

public final class l01l0OilANd {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private l01l0OilANd() {
    }

    public static Perfstat.perfstat_cpu_total_t I1O1I1LaNd() {
        Perfstat.perfstat_cpu_total_t perfstat_cpu_total_t2 = new Perfstat.perfstat_cpu_total_t();
        int n2 = I1O1I1LaNd.perfstat_cpu_total(null, perfstat_cpu_total_t2, perfstat_cpu_total_t2.size(), 1);
        if (n2 > 0) {
            return perfstat_cpu_total_t2;
        }
        return new Perfstat.perfstat_cpu_total_t();
    }

    public static Perfstat.perfstat_cpu_t[] OOOIilanD() {
        Perfstat.perfstat_cpu_t[] arrperfstat_cpu_t;
        Perfstat.perfstat_id_t perfstat_id_t2;
        int n2;
        Perfstat.perfstat_cpu_t perfstat_cpu_t2 = new Perfstat.perfstat_cpu_t();
        int n3 = I1O1I1LaNd.perfstat_cpu(null, null, perfstat_cpu_t2.size(), 0);
        if (n3 > 0 && (n2 = I1O1I1LaNd.perfstat_cpu(perfstat_id_t2 = new Perfstat.perfstat_id_t(), arrperfstat_cpu_t = (Perfstat.perfstat_cpu_t[])perfstat_cpu_t2.toArray(n3), perfstat_cpu_t2.size(), n3)) > 0) {
            return arrperfstat_cpu_t;
        }
        return new Perfstat.perfstat_cpu_t[0];
    }

    public static long lI00OlAND() {
        int n2 = l01l0OilANd.I1O1I1LaNd().ncpus;
        if (n2 < 63) {
            return (1L << n2) - 1L;
        }
        return n2 == 63 ? Long.MAX_VALUE : -1L;
    }
}

