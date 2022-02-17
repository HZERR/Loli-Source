/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;

public final class iOIOOilanD {
    private static final Perfstat I1O1I1LaNd = Perfstat.INSTANCE;

    private iOIOOilanD() {
    }

    public static Perfstat.perfstat_netinterface_t[] I1O1I1LaNd() {
        Perfstat.perfstat_netinterface_t[] arrperfstat_netinterface_t;
        Perfstat.perfstat_id_t perfstat_id_t2;
        int n2;
        Perfstat.perfstat_netinterface_t perfstat_netinterface_t2 = new Perfstat.perfstat_netinterface_t();
        int n3 = I1O1I1LaNd.perfstat_netinterface(null, null, perfstat_netinterface_t2.size(), 0);
        if (n3 > 0 && (n2 = I1O1I1LaNd.perfstat_netinterface(perfstat_id_t2 = new Perfstat.perfstat_id_t(), arrperfstat_netinterface_t = (Perfstat.perfstat_netinterface_t[])perfstat_netinterface_t2.toArray(n3), perfstat_netinterface_t2.size(), n3)) > 0) {
            return arrperfstat_netinterface_t;
        }
        return new Perfstat.perfstat_netinterface_t[0];
    }
}

