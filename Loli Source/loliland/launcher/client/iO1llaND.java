/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.IiiilAnD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i0l10iiilaNd;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.iIiO00OLaNd;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.lOOO110laNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liIli0OlAND;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.ll1l10l0LaNd;

public class iO1llaND
extends i0l10iiilaNd {
    @Override
    public ll1l10l0LaNd lli0OiIlAND() {
        return IiiilAnD.I1O1I1LaNd("netstat -st4");
    }

    @Override
    public l11IliilLANd li0iOILAND() {
        return IiiilAnD.OOOIilanD("netstat -su4");
    }

    @Override
    public l11IliilLANd OOOIilanD() {
        return IiiilAnD.OOOIilanD("netstat -su6");
    }

    @Override
    public List lI00OlAND() {
        ArrayList arrayList = new ArrayList();
        Map map = iIiO00OLaNd.OOOIilanD();
        arrayList.addAll(iO1llaND.I1O1I1LaNd("tcp", 4, map));
        arrayList.addAll(iO1llaND.I1O1I1LaNd("tcp", 6, map));
        arrayList.addAll(iO1llaND.I1O1I1LaNd("udp", 4, map));
        arrayList.addAll(iO1llaND.I1O1I1LaNd("udp", 6, map));
        return arrayList;
    }

    private static List I1O1I1LaNd(String string, int n2, Map map) {
        ArrayList<lOOO110laNd> arrayList = new ArrayList<lOOO110laNd>();
        for (String string2 : liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.Oill1LAnD + "/" + string + (n2 == 6 ? "6" : ""))) {
            String[] arrstring;
            if (string2.indexOf(58) < 0 || (arrstring = lOilLanD.OOOIilanD.split(string2.trim())).length <= 9) continue;
            O1IiIiI1LAND o1IiIiI1LAND = iO1llaND.I1O1I1LaNd(arrstring[1]);
            O1IiIiI1LAND o1IiIiI1LAND2 = iO1llaND.I1O1I1LaNd(arrstring[2]);
            liIli0OlAND liIli0OlAND2 = iO1llaND.I1O1I1LaNd(lOilLanD.O1il1llOLANd(arrstring[3], 0));
            O1IiIiI1LAND o1IiIiI1LAND3 = iO1llaND.OOOIilanD(arrstring[4]);
            int n3 = lOilLanD.lli0OiIlAND(arrstring[9], 0);
            arrayList.add(new lOOO110laNd(string + n2, (byte[])o1IiIiI1LAND.I1O1I1LaNd(), (Integer)o1IiIiI1LAND.OOOIilanD(), (byte[])o1IiIiI1LAND2.I1O1I1LaNd(), (Integer)o1IiIiI1LAND2.OOOIilanD(), liIli0OlAND2, (Integer)o1IiIiI1LAND3.I1O1I1LaNd(), (Integer)o1IiIiI1LAND3.OOOIilanD(), map.getOrDefault(n3, -1)));
        }
        return arrayList;
    }

    private static O1IiIiI1LAND I1O1I1LaNd(String string) {
        int n2 = string.indexOf(58);
        if (n2 > 0 && n2 < string.length()) {
            byte[] arrby = lOilLanD.lI00OlAND(string.substring(0, n2));
            int n3 = 0;
            while (n3 + 3 < arrby.length) {
                byte by = arrby[n3];
                arrby[n3] = arrby[n3 + 3];
                arrby[n3 + 3] = by;
                by = arrby[n3 + 1];
                arrby[n3 + 1] = arrby[n3 + 2];
                arrby[n3 + 2] = by;
                n3 += 4;
            }
            n3 = lOilLanD.O1il1llOLANd(string.substring(n2 + 1), 0);
            return new O1IiIiI1LAND(arrby, n3);
        }
        return new O1IiIiI1LAND(new byte[0], 0);
    }

    private static O1IiIiI1LAND OOOIilanD(String string) {
        int n2 = string.indexOf(58);
        if (n2 > 0 && n2 < string.length()) {
            int n3 = lOilLanD.O1il1llOLANd(string.substring(0, n2), 0);
            int n4 = lOilLanD.O1il1llOLANd(string.substring(n2 + 1), 0);
            return new O1IiIiI1LAND(n3, n4);
        }
        return new O1IiIiI1LAND(0, 0);
    }

    private static liIli0OlAND I1O1I1LaNd(int n2) {
        switch (n2) {
            case 1: {
                return liIli0OlAND.O1il1llOLANd;
            }
            case 2: {
                return liIli0OlAND.lli0OiIlAND;
            }
            case 3: {
                return liIli0OlAND.li0iOILAND;
            }
            case 4: {
                return liIli0OlAND.Oill1LAnD;
            }
            case 5: {
                return liIli0OlAND.lIOILand;
            }
            case 6: {
                return liIli0OlAND.l0illAND;
            }
            case 7: {
                return liIli0OlAND.OOOIilanD;
            }
            case 8: {
                return liIli0OlAND.lil0liLand;
            }
            case 9: {
                return liIli0OlAND.lli011lLANd;
            }
            case 10: {
                return liIli0OlAND.lI00OlAND;
            }
            case 11: {
                return liIli0OlAND.iilIi1laND;
            }
        }
        return liIli0OlAND.I1O1I1LaNd;
    }
}

