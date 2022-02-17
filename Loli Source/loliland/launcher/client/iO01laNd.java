/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.Oii011ILaND;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lOOllANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liIII0IiLaNd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilO00LANd;

final class iO01laNd
extends lI01110LaNd {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(iO01laNd::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier OOOIilanD;
    private static final long lI00OlAND = 4096L;
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::Oill1LAnD);

    iO01laNd(Supplier supplier) {
        this.OOOIilanD = supplier;
    }

    @Override
    public long OOOIilanD() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).real_avail * 4096L;
    }

    @Override
    public long I1O1I1LaNd() {
        return ((Perfstat.perfstat_memory_total_t)this.I1O1I1LaNd.get()).real_total * 4096L;
    }

    @Override
    public long lI00OlAND() {
        return 4096L;
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.lli0OiIlAND.get();
    }

    @Override
    public List li0iOILAND() {
        ArrayList<Oii011ILaND> arrayList = new ArrayList<Oii011ILaND>();
        boolean bl = false;
        String string = "unknown";
        String string2 = "";
        long l2 = 0L;
        for (String string3 : (List)this.OOOIilanD.get()) {
            String string4 = string3.trim();
            if (string4.endsWith("memory-module")) {
                bl = true;
                continue;
            }
            if (!bl) continue;
            if (string4.startsWith("Node:")) {
                string = string4.substring(5).trim();
                if (!string.startsWith("IBM,")) continue;
                string = string.substring(4);
                continue;
            }
            if (string4.startsWith("Physical Location:")) {
                string2 = "/" + string4.substring(18).trim();
                continue;
            }
            if (string4.startsWith("Size")) {
                l2 = lOilLanD.OOOIilanD(lOilLanD.iOIl0LAnD(string4.substring(4).trim()), 0L) << 20;
                continue;
            }
            if (!string4.startsWith("Hardware Location Code")) continue;
            if (l2 > 0L) {
                arrayList.add(new Oii011ILaND(string + string2, l2, 0L, "IBM", "unknown"));
            }
            string = "unknown";
            string2 = "";
            l2 = 0L;
            bl = false;
        }
        return arrayList;
    }

    private static Perfstat.perfstat_memory_total_t O1il1llOLANd() {
        return lOOllANd.I1O1I1LaNd();
    }

    private lilO00LANd Oill1LAnD() {
        return new liIII0IiLaNd(this.I1O1I1LaNd);
    }
}

