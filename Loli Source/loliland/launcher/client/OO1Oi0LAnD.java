/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;

public final class OO1Oi0LAnD {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private OO1Oi0LAnD() {
    }

    public static Perfstat.perfstat_partition_config_t I1O1I1LaNd() {
        Perfstat.perfstat_partition_config_t perfstat_partition_config_t2 = new Perfstat.perfstat_partition_config_t();
        int n2 = I1O1I1LaNd.perfstat_partition_config(null, perfstat_partition_config_t2, perfstat_partition_config_t2.size(), 1);
        if (n2 > 0) {
            return perfstat_partition_config_t2;
        }
        return new Perfstat.perfstat_partition_config_t();
    }
}

