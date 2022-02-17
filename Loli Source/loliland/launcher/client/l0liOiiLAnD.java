/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IOIIO1lanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.l0lllLanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lli10iliLaND;

final class l0liOiiLAnD
extends IOIIO1lanD {
    l0lllLanD I1O1I1LaNd;
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(l0liOiiLAnD::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(l0liOiiLAnD::lIOILand, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(l0liOiiLAnD::lil0liLand, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(l0liOiiLAnD::iilIi1laND, lii1IO0LaNd.I1O1I1LaNd());

    l0liOiiLAnD(l0lllLanD l0lllLanD2) {
        this.I1O1I1LaNd = l0lllLanD2;
    }

    @Override
    public long OOOIilanD() {
        return (Long)this.OOOIilanD.get();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)this.lI00OlAND.get();
    }

    @Override
    public long lI00OlAND() {
        return this.I1O1I1LaNd.I1O1I1LaNd() + this.I1O1I1LaNd();
    }

    @Override
    public long lli0OiIlAND() {
        return this.I1O1I1LaNd.I1O1I1LaNd() - this.I1O1I1LaNd.OOOIilanD() + this.OOOIilanD();
    }

    @Override
    public long li0iOILAND() {
        return (Long)this.lli0OiIlAND.get();
    }

    @Override
    public long O1il1llOLANd() {
        return (Long)this.li0iOILAND.get();
    }

    private static long Oill1LAnD() {
        String string = Iill1lanD.I1O1I1LaNd("swapinfo -k", 1);
        String[] arrstring = lOilLanD.OOOIilanD.split(string);
        if (arrstring.length < 5) {
            return 0L;
        }
        return lOilLanD.OOOIilanD(arrstring[2], 0L) << 10;
    }

    private static long lIOILand() {
        return lli10iliLaND.I1O1I1LaNd("vm.swap_total", 0L);
    }

    private static long lil0liLand() {
        return lli10iliLaND.I1O1I1LaNd("vm.stats.vm.v_swappgsin", 0L);
    }

    private static long iilIi1laND() {
        return lli10iliLaND.I1O1I1LaNd("vm.stats.vm.v_swappgsout", 0L);
    }
}

