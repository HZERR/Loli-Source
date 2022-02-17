/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O0OIIOlAnD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilO00LANd;

public final class OlI1illAND
extends lI01110LaNd {
    public static final long I1O1I1LaNd = lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("getconf PAGE_SIZE"), 4096L);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(OlI1illAND::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::Oill1LAnD);

    @Override
    public long OOOIilanD() {
        return (Long)((O1IiIiI1LAND)this.OOOIilanD.get()).I1O1I1LaNd();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)((O1IiIiI1LAND)this.OOOIilanD.get()).OOOIilanD();
    }

    @Override
    public long lI00OlAND() {
        return I1O1I1LaNd;
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.lI00OlAND.get();
    }

    private static O1IiIiI1LAND O1il1llOLANd() {
        long l2 = 0L;
        long l3 = 0L;
        long l4 = 0L;
        long l5 = 0L;
        long l6 = 0L;
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.li0iOILAND);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string, 2);
            if (arrstring.length <= 1) continue;
            switch (arrstring[0]) {
                case "MemTotal:": {
                    l6 = lOilLanD.lli011lLANd(arrstring[1]);
                    break;
                }
                case "MemAvailable:": {
                    long l7 = lOilLanD.lli011lLANd(arrstring[1]);
                    return new O1IiIiI1LAND(l7, l6);
                }
                case "MemFree:": {
                    l2 = lOilLanD.lli011lLANd(arrstring[1]);
                    break;
                }
                case "Active(file):": {
                    l3 = lOilLanD.lli011lLANd(arrstring[1]);
                    break;
                }
                case "Inactive(file):": {
                    l4 = lOilLanD.lli011lLANd(arrstring[1]);
                    break;
                }
                case "SReclaimable:": {
                    l5 = lOilLanD.lli011lLANd(arrstring[1]);
                    break;
                }
            }
        }
        return new O1IiIiI1LAND(l2 + l3 + l4 + l5, l6);
    }

    private lilO00LANd Oill1LAnD() {
        return new O0OIIOlAnD(this);
    }
}

