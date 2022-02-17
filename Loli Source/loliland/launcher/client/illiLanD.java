/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.Arrays;

public final class illiLanD {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private illiLanD() {
    }

    public static Perfstat.perfstat_process_t[] I1O1I1LaNd() {
        Perfstat.perfstat_process_t[] arrperfstat_process_t;
        Perfstat.perfstat_id_t perfstat_id_t2;
        int n2;
        Perfstat.perfstat_process_t perfstat_process_t2 = new Perfstat.perfstat_process_t();
        int n3 = I1O1I1LaNd.perfstat_process(null, null, perfstat_process_t2.size(), 0);
        if (n3 > 0 && (n2 = I1O1I1LaNd.perfstat_process(perfstat_id_t2 = new Perfstat.perfstat_id_t(), arrperfstat_process_t = (Perfstat.perfstat_process_t[])perfstat_process_t2.toArray(n3), perfstat_process_t2.size(), n3)) > 0) {
            return Arrays.copyOf(arrperfstat_process_t, n2);
        }
        return new Perfstat.perfstat_process_t[0];
    }
}

