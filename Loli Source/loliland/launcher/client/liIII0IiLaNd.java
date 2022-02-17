/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import loliland.launcher.client.IOIIO1lanD;

final class liIII0IiLaNd
extends IOIIO1lanD {
    private final Supplier I1O1I1LaNd;
    private static final long OOOIilanD = 4096L;

    liIII0IiLaNd(Supplier supplier) {
        this.I1O1I1LaNd = supplier;
    }

    @Override
    public long OOOIilanD() {
        Perfstat.perfstat_memory_total_t perfstat_memory_total_t2 = (Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get();
        return (perfstat_memory_total_t2.pgsp_total - perfstat_memory_total_t2.pgsp_free) * 4096L;
    }

    @Override
    public long I1O1I1LaNd() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).pgsp_total * 4096L;
    }

    @Override
    public long lI00OlAND() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).virt_total * 4096L;
    }

    @Override
    public long lli0OiIlAND() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).virt_active * 4096L;
    }

    @Override
    public long li0iOILAND() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).pgspins;
    }

    @Override
    public long O1il1llOLANd() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).pgspouts;
    }
}

