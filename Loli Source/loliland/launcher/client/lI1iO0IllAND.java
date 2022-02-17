/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.IOIIO1lanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liO0O1O1lAND;
import loliland.launcher.client.lii1IO0LaNd;

final class lI1iO0IllAND
extends IOIIO1lanD {
    liO0O1O1lAND I1O1I1LaNd;
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(lI1iO0IllAND::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(lI1iO0IllAND::lIOILand, lii1IO0LaNd.I1O1I1LaNd());

    lI1iO0IllAND(liO0O1O1lAND liO0O1O1lAND2) {
        this.I1O1I1LaNd = liO0O1O1lAND2;
    }

    @Override
    public long OOOIilanD() {
        return (long)((Integer)((IIOOOlIiLanD)this.OOOIilanD.get()).I1O1I1LaNd()).intValue() * this.I1O1I1LaNd.lI00OlAND();
    }

    @Override
    public long I1O1I1LaNd() {
        return (long)((Integer)((IIOOOlIiLanD)this.OOOIilanD.get()).OOOIilanD()).intValue() * this.I1O1I1LaNd.lI00OlAND();
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
        return (long)((Integer)((IIOOOlIiLanD)this.OOOIilanD.get()).lI00OlAND()).intValue() * this.I1O1I1LaNd.lI00OlAND();
    }

    @Override
    public long O1il1llOLANd() {
        return (long)((Integer)this.lI00OlAND.get()).intValue() * this.I1O1I1LaNd.lI00OlAND();
    }

    private static IIOOOlIiLanD Oill1LAnD() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        for (String string : Iill1lanD.I1O1I1LaNd("vmstat -s")) {
            if (string.contains("swap pages in use")) {
                n2 = lOilLanD.Oill1LAnD(string);
                continue;
            }
            if (string.contains("swap pages")) {
                n3 = lOilLanD.Oill1LAnD(string);
                continue;
            }
            if (!string.contains("pagein operations")) continue;
            n4 = lOilLanD.Oill1LAnD(string);
        }
        return new IIOOOlIiLanD(n2, n3, n4);
    }

    private static int lIOILand() {
        for (String string : Iill1lanD.I1O1I1LaNd("systat -ab uvm")) {
            if (!string.contains("pdpageouts")) continue;
            return lOilLanD.Oill1LAnD(string);
        }
        return 0;
    }
}

