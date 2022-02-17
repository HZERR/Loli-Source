/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O11i1I1lAnd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.lOilLanD;

public final class l0lOiOlAnd {
    private l0lOiOlAnd() {
    }

    public static O11i1I1lAnd I1O1I1LaNd(String string) {
        ArrayList<i0Oi1LANd> arrayList = new ArrayList<i0Oi1LANd>();
        String string2 = "total sectors:";
        long l2 = 1L;
        String string3 = "bytes/sector:";
        int n2 = 1;
        String string4 = "label:";
        String string5 = "";
        String string6 = "duid:";
        String string7 = "";
        for (String string8 : Iill1lanD.I1O1I1LaNd("disklabel -n " + string)) {
            if (string8.contains(string2)) {
                l2 = lOilLanD.Oill1LAnD(string8);
            } else if (string8.contains(string3)) {
                n2 = lOilLanD.Oill1LAnD(string8);
            } else if (string8.contains(string4)) {
                string5 = string8.split(string4)[1].trim();
            } else if (string8.contains(string6)) {
                string7 = string8.split(string6)[1].trim();
            }
            if (string8.trim().indexOf(58) != 1) continue;
            String[] arrstring = lOilLanD.OOOIilanD.split(string8.trim(), 9);
            String string9 = arrstring[0].substring(0, 1);
            O1IiIiI1LAND o1IiIiI1LAND = l0lOiOlAnd.I1O1I1LaNd(string, string9);
            if (arrstring.length <= 4) continue;
            arrayList.add(new i0Oi1LANd(string + string9, string9, arrstring[3], string7 + "." + string9, lOilLanD.OOOIilanD(arrstring[1], 0L) * (long)n2, (Integer)o1IiIiI1LAND.I1O1I1LaNd(), (Integer)o1IiIiI1LAND.OOOIilanD(), arrstring.length > 5 ? arrstring[arrstring.length - 1] : ""));
        }
        if (arrayList.isEmpty()) {
            return l0lOiOlAnd.OOOIilanD(string);
        }
        return new O11i1I1lAnd(string5, string7, l2 * (long)n2, arrayList);
    }

    private static O11i1I1lAnd OOOIilanD(String string) {
        ArrayList<i0Oi1LANd> arrayList = new ArrayList<i0Oi1LANd>();
        for (String string2 : Iill1lanD.I1O1I1LaNd("df")) {
            if (!string2.startsWith("/dev/" + string)) continue;
            String[] arrstring = lOilLanD.OOOIilanD.split(string2);
            String string3 = arrstring[0].substring(5 + string.length());
            O1IiIiI1LAND o1IiIiI1LAND = l0lOiOlAnd.I1O1I1LaNd(string, string3);
            if (arrstring.length <= 5) continue;
            long l2 = lOilLanD.OOOIilanD(arrstring[1], 1L) * 512L;
            arrayList.add(new i0Oi1LANd(arrstring[0], arrstring[0].substring(5), "unknown", "unknown", l2, (Integer)o1IiIiI1LAND.I1O1I1LaNd(), (Integer)o1IiIiI1LAND.OOOIilanD(), arrstring[5]));
        }
        return new O11i1I1lAnd("unknown", "unknown", 0L, arrayList);
    }

    private static O1IiIiI1LAND I1O1I1LaNd(String string, String string2) {
        int n2 = 0;
        int n3 = 0;
        String string3 = Iill1lanD.OOOIilanD("stat -f %Hr,%Lr /dev/" + string + string2);
        int n4 = string3.indexOf(44);
        if (n4 > 0 && n4 < string3.length()) {
            n2 = lOilLanD.lli0OiIlAND(string3.substring(0, n4), 0);
            n3 = lOilLanD.lli0OiIlAND(string3.substring(n4 + 1), 0);
        }
        return new O1IiIiI1LAND(n2, n3);
    }
}

