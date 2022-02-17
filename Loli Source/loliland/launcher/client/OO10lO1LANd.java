/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;

public final class OO10lO1LANd {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private OO10lO1LANd() {
    }

    public static Perfstat.perfstat_protocol_t[] I1O1I1LaNd() {
        Perfstat.perfstat_protocol_t[] arrperfstat_protocol_t;
        Perfstat.perfstat_id_t perfstat_id_t2;
        int n2;
        Perfstat.perfstat_protocol_t perfstat_protocol_t2 = new Perfstat.perfstat_protocol_t();
        int n3 = I1O1I1LaNd.perfstat_protocol(null, null, perfstat_protocol_t2.size(), 0);
        if (n3 > 0 && (n2 = I1O1I1LaNd.perfstat_protocol(perfstat_id_t2 = new Perfstat.perfstat_id_t(), arrperfstat_protocol_t = (Perfstat.perfstat_protocol_t[])perfstat_protocol_t2.toArray(n3), perfstat_protocol_t2.size(), n3)) > 0) {
            return arrperfstat_protocol_t;
        }
        return new Perfstat.perfstat_protocol_t[0];
    }
}

