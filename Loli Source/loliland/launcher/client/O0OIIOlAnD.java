/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.IOIIO1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OlI1illAND;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lii1IO0LaNd;

final class O0OIIOlAnD
extends IOIIO1lanD {
    private final OlI1illAND I1O1I1LaNd;
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(O0OIIOlAnD::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(O0OIIOlAnD::lIOILand, lii1IO0LaNd.I1O1I1LaNd());

    O0OIIOlAnD(OlI1illAND olI1illAND) {
        this.I1O1I1LaNd = olI1illAND;
    }

    @Override
    public long OOOIilanD() {
        return (Long)((IIOOOlIiLanD)this.OOOIilanD.get()).I1O1I1LaNd();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)((IIOOOlIiLanD)this.OOOIilanD.get()).OOOIilanD();
    }

    @Override
    public long lI00OlAND() {
        return (Long)((IIOOOlIiLanD)this.OOOIilanD.get()).lI00OlAND();
    }

    @Override
    public long lli0OiIlAND() {
        return this.I1O1I1LaNd.I1O1I1LaNd() - this.I1O1I1LaNd.OOOIilanD() + this.OOOIilanD();
    }

    @Override
    public long li0iOILAND() {
        return (Long)((O1IiIiI1LAND)this.lI00OlAND.get()).I1O1I1LaNd();
    }

    @Override
    public long O1il1llOLANd() {
        return (Long)((O1IiIiI1LAND)this.lI00OlAND.get()).OOOIilanD();
    }

    private static IIOOOlIiLanD Oill1LAnD() {
        long l2 = 0L;
        long l3 = 0L;
        long l4 = 0L;
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.li0iOILAND);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length <= 1) continue;
            switch (arrstring[0]) {
                case "SwapTotal:": {
                    l3 = O0OIIOlAnD.I1O1I1LaNd(arrstring);
                    break;
                }
                case "SwapFree:": {
                    l2 = O0OIIOlAnD.I1O1I1LaNd(arrstring);
                    break;
                }
                case "CommitLimit:": {
                    l4 = O0OIIOlAnD.I1O1I1LaNd(arrstring);
                    break;
                }
            }
        }
        return new IIOOOlIiLanD(l3 - l2, l3, l4);
    }

    private static O1IiIiI1LAND lIOILand() {
        long l2 = 0L;
        long l3 = 0L;
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.II1Iland);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length <= 1) continue;
            switch (arrstring[0]) {
                case "pswpin": {
                    l2 = lOilLanD.OOOIilanD(arrstring[1], 0L);
                    break;
                }
                case "pswpout": {
                    l3 = lOilLanD.OOOIilanD(arrstring[1], 0L);
                    break;
                }
            }
        }
        return new O1IiIiI1LAND(l2, l3);
    }

    private static long I1O1I1LaNd(String[] arrstring) {
        if (arrstring.length < 2) {
            return 0L;
        }
        long l2 = lOilLanD.OOOIilanD(arrstring[1], 0L);
        if (arrstring.length > 2 && "kB".equals(arrstring[2])) {
            l2 *= 1024L;
        }
        return l2;
    }
}

