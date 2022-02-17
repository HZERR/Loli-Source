/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.I1IllaND;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.l0I0OLaNd;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilO00LANd;

final class ll0i0I1ILAnD
extends lI01110LaNd {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(I1IllaND::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(ll0i0I1ILAnD::O1il1llOLANd);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::Oill1LAnD);

    ll0i0I1ILAnD() {
    }

    @Override
    public long OOOIilanD() {
        return (Long)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).I1O1I1LaNd() * this.lI00OlAND();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).OOOIilanD() * this.lI00OlAND();
    }

    @Override
    public long lI00OlAND() {
        return (Long)this.OOOIilanD.get();
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.lI00OlAND.get();
    }

    private static long O1il1llOLANd() {
        return lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("pagesize"), 4096L);
    }

    private lilO00LANd Oill1LAnD() {
        return new l0I0OLaNd(this);
    }
}

