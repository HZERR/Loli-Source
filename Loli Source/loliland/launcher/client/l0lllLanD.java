/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.l0liOiiLAnD;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilO00LANd;
import loliland.launcher.client.lli10iliLaND;

final class l0lllLanD
extends lI01110LaNd {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(l0lllLanD::Oill1LAnD);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(l0lllLanD::lIOILand);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::lil0liLand);

    l0lllLanD() {
    }

    @Override
    public long OOOIilanD() {
        return (Long)this.I1O1I1LaNd.get();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)this.OOOIilanD.get();
    }

    @Override
    public long lI00OlAND() {
        return (Long)this.lI00OlAND.get();
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.lli0OiIlAND.get();
    }

    private long O1il1llOLANd() {
        int n2 = lli10iliLaND.I1O1I1LaNd("vm.stats.vm.v_inactive_count", 0);
        int n3 = lli10iliLaND.I1O1I1LaNd("vm.stats.vm.v_free_count", 0);
        return (long)(n2 + n3) * this.lI00OlAND();
    }

    private static long Oill1LAnD() {
        return lli10iliLaND.I1O1I1LaNd("hw.physmem", 0L);
    }

    private static long lIOILand() {
        return lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("sysconf PAGESIZE"), 4096L);
    }

    private lilO00LANd lil0liLand() {
        return new l0liOiiLAnD(this);
    }
}

