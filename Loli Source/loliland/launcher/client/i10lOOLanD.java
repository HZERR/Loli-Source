/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iIl0il1lAnD;
import loliland.launcher.client.lOilLanD;

final class i10lOOLanD
extends iIl0il1lAnD {
    private static final String I1O1I1LaNd = "0x03";

    i10lOOLanD(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List O1il1llOLANd() {
        ArrayList<i10lOOLanD> arrayList = new ArrayList<i10lOOLanD>();
        List list = Iill1lanD.I1O1I1LaNd("pciconf -lv");
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        String string = "unknown";
        String string2 = "unknown";
        String string3 = "unknown";
        String string4 = "";
        Object object = "unknown";
        for (String string5 : list) {
            String[] arrstring;
            if (string5.contains("class=0x")) {
                if (I1O1I1LaNd.equals(string4)) {
                    arrayList.add(new i10lOOLanD(string.isEmpty() ? "unknown" : string, string3.isEmpty() ? "unknown" : string3, string2.isEmpty() ? "unknown" : string2, (String)(((String)object).isEmpty() ? "unknown" : object), 0L));
                }
                arrstring = lOilLanD.OOOIilanD.split(string5);
                for (String string6 : arrstring) {
                    String[] arrstring2 = string6.split("=");
                    if (arrstring2.length <= 1) continue;
                    if (arrstring2[0].equals("class") && arrstring2[1].length() >= 4) {
                        string4 = arrstring2[1].substring(0, 4);
                        continue;
                    }
                    if (arrstring2[0].equals("chip") && arrstring2[1].length() >= 10) {
                        string3 = arrstring2[1].substring(0, 6);
                        string2 = "0x" + arrstring2[1].substring(6, 10);
                        continue;
                    }
                    if (!arrstring2[0].contains("rev")) continue;
                    object = string6;
                }
                string = "unknown";
                continue;
            }
            arrstring = string5.trim().split("=", 2);
            if (arrstring.length != 2) continue;
            String string7 = arrstring[0].trim();
            if (string7.equals("vendor")) {
                string2 = lOilLanD.li0iOILAND(string5) + (string2.equals("unknown") ? "" : " (" + string2 + ")");
                continue;
            }
            if (!string7.equals("device")) continue;
            string = lOilLanD.li0iOILAND(string5);
        }
        if (I1O1I1LaNd.equals(string4)) {
            arrayList.add(new i10lOOLanD(string.isEmpty() ? "unknown" : string, string3.isEmpty() ? "unknown" : string3, string2.isEmpty() ? "unknown" : string2, (String)(((String)object).isEmpty() ? "unknown" : object), 0L));
        }
        return arrayList;
    }
}

