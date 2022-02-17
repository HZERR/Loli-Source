/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.lOilLanD;

public final class IIOlO0ilAnD {
    private static final String I1O1I1LaNd = "prtvtoc /dev/dsk/";

    private IIOlO0ilAnD() {
    }

    public static List I1O1I1LaNd(String string, int n2) {
        ArrayList<i0Oi1LANd> arrayList = new ArrayList<i0Oi1LANd>();
        List list = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd + string);
        if (list.size() > 1) {
            int n3 = 0;
            for (String string2 : list) {
                String string3;
                String string4;
                String[] arrstring;
                if (string2.startsWith("*")) {
                    if (!string2.endsWith("bytes/sector") || (arrstring = lOilLanD.OOOIilanD.split(string2)).length <= 0) continue;
                    n3 = lOilLanD.lli0OiIlAND(arrstring[1], 0);
                    continue;
                }
                if (n3 <= 0 || (arrstring = lOilLanD.OOOIilanD.split(string2.trim())).length < 6 || "2".equals(arrstring[0])) continue;
                String string5 = string + "s" + arrstring[0];
                int n4 = lOilLanD.lli0OiIlAND(arrstring[0], 0);
                switch (lOilLanD.lli0OiIlAND(arrstring[1], 0)) {
                    case 1: 
                    case 24: {
                        string4 = "boot";
                        break;
                    }
                    case 2: {
                        string4 = "root";
                        break;
                    }
                    case 3: {
                        string4 = "swap";
                        break;
                    }
                    case 4: {
                        string4 = "usr";
                        break;
                    }
                    case 5: {
                        string4 = "backup";
                        break;
                    }
                    case 6: {
                        string4 = "stand";
                        break;
                    }
                    case 7: {
                        string4 = "var";
                        break;
                    }
                    case 8: {
                        string4 = "home";
                        break;
                    }
                    case 9: {
                        string4 = "altsctr";
                        break;
                    }
                    case 10: {
                        string4 = "cache";
                        break;
                    }
                    case 11: {
                        string4 = "reserved";
                        break;
                    }
                    case 12: {
                        string4 = "system";
                        break;
                    }
                    case 14: {
                        string4 = "public region";
                        break;
                    }
                    case 15: {
                        string4 = "private region";
                        break;
                    }
                    default: {
                        string4 = "unknown";
                    }
                }
                switch (arrstring[2]) {
                    case "00": {
                        string3 = "wm";
                        break;
                    }
                    case "10": {
                        string3 = "rm";
                        break;
                    }
                    case "01": {
                        string3 = "wu";
                        break;
                    }
                    default: {
                        string3 = "ru";
                    }
                }
                long l2 = (long)n3 * lOilLanD.OOOIilanD(arrstring[4], 0L);
                String string6 = "";
                if (arrstring.length > 6) {
                    string6 = arrstring[6];
                }
                arrayList.add(new i0Oi1LANd(string5, string4, string3, "", l2, n2, n4, string6));
            }
        }
        return arrayList;
    }
}

