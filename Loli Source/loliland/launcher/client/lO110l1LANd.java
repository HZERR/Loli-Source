/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class lO110l1LANd {
    private lO110l1LANd() {
    }

    public static long[] I1O1I1LaNd() {
        long[] arrl = new long[OOiO1lAnd.values().length];
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.iOIl0LAnD);
        if (list.isEmpty()) {
            return arrl;
        }
        String string = (String)list.get(0);
        String[] arrstring = lOilLanD.OOOIilanD.split(string);
        if (arrstring.length <= OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()) {
            return arrl;
        }
        for (int i2 = 0; i2 < OOiO1lAnd.values().length; ++i2) {
            arrl[i2] = lOilLanD.OOOIilanD(arrstring[i2 + 1], 0L);
        }
        return arrl;
    }

    public static long[][] I1O1I1LaNd(int n2) {
        long[][] arrl = new long[n2][OOiO1lAnd.values().length];
        int n3 = 0;
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.iOIl0LAnD);
        for (String string : list) {
            if (!string.startsWith("cpu") || string.startsWith("cpu ")) continue;
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length <= OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()) {
                return arrl;
            }
            for (int i2 = 0; i2 < OOiO1lAnd.values().length; ++i2) {
                arrl[n3][i2] = lOilLanD.OOOIilanD(arrstring[i2 + 1], 0L);
            }
            if (++n3 < n2) continue;
            break;
        }
        return arrl;
    }

    public static long OOOIilanD() {
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.iOIl0LAnD);
        for (String string : list) {
            String[] arrstring;
            if (!string.startsWith("ctxt ") || (arrstring = lOilLanD.OOOIilanD.split(string)).length != 2) continue;
            return lOilLanD.OOOIilanD(arrstring[1], 0L);
        }
        return 0L;
    }

    public static long lI00OlAND() {
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.iOIl0LAnD);
        for (String string : list) {
            String[] arrstring;
            if (!string.startsWith("intr ") || (arrstring = lOilLanD.OOOIilanD.split(string)).length <= 2) continue;
            return lOilLanD.OOOIilanD(arrstring[1], 0L);
        }
        return 0L;
    }

    public static long lli0OiIlAND() {
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.iOIl0LAnD);
        for (String string : list) {
            if (!string.startsWith("btime")) continue;
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            return lOilLanD.OOOIilanD(arrstring[1], 0L);
        }
        return 0L;
    }
}

