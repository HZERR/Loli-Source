/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iIl0il1lAnD;
import loliland.launcher.client.lOilLanD;

final class l010OLAnD
extends iIl0il1lAnD {
    private static final String I1O1I1LaNd = "0003";

    l010OLAnD(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List O1il1llOLANd() {
        ArrayList<l010OLAnD> arrayList = new ArrayList<l010OLAnD>();
        List list = Iill1lanD.I1O1I1LaNd("prtconf -pv");
        if (list.isEmpty()) {
            return arrayList;
        }
        String string = "";
        String string2 = "";
        String string3 = "";
        String string4 = "";
        ArrayList<String> arrayList2 = new ArrayList<String>();
        for (String string5 : list) {
            if (string5.contains("Node 0x")) {
                if (I1O1I1LaNd.equals(string4)) {
                    arrayList.add(new l010OLAnD(string.isEmpty() ? "unknown" : string, string3.isEmpty() ? "unknown" : string3, string2.isEmpty() ? "unknown" : string2, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), 0L));
                }
                string = "";
                string2 = "unknown";
                string3 = "unknown";
                string4 = "";
                arrayList2.clear();
                continue;
            }
            String[] arrstring = string5.trim().split(":", 2);
            if (arrstring.length != 2) continue;
            if (arrstring[0].equals("model")) {
                string = lOilLanD.li0iOILAND(string5);
                continue;
            }
            if (arrstring[0].equals("name")) {
                if (!string.isEmpty()) continue;
                string = lOilLanD.li0iOILAND(string5);
                continue;
            }
            if (arrstring[0].equals("vendor-id")) {
                string2 = "0x" + string5.substring(string5.length() - 4);
                continue;
            }
            if (arrstring[0].equals("device-id")) {
                string3 = "0x" + string5.substring(string5.length() - 4);
                continue;
            }
            if (arrstring[0].equals("revision-id")) {
                arrayList2.add(string5.trim());
                continue;
            }
            if (!arrstring[0].equals("class-code")) continue;
            string4 = string5.substring(string5.length() - 8, string5.length() - 4);
        }
        if (I1O1I1LaNd.equals(string4)) {
            arrayList.add(new l010OLAnD(string.isEmpty() ? "unknown" : string, string3.isEmpty() ? "unknown" : string3, string2.isEmpty() ? "unknown" : string2, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), 0L));
        }
        return arrayList;
    }
}

