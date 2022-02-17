/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iIl0il1lAnD;
import loliland.launcher.client.lOilLanD;

final class IiO1iILaNd
extends iIl0il1lAnD {
    IiO1iILaNd(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List O1il1llOLANd() {
        ArrayList<IiO1iILaNd> arrayList = new ArrayList<IiO1iILaNd>();
        List list = Iill1lanD.I1O1I1LaNd("system_profiler SPDisplaysDataType");
        String string = "unknown";
        String string2 = "unknown";
        String string3 = "unknown";
        ArrayList<String> arrayList2 = new ArrayList<String>();
        long l2 = 0L;
        int n2 = 0;
        for (String string4 : list) {
            String[] arrstring = string4.trim().split(":", 2);
            if (arrstring.length != 2) continue;
            String string5 = arrstring[0].toLowerCase();
            if (string5.equals("chipset model")) {
                if (n2++ > 0) {
                    arrayList.add(new IiO1iILaNd(string, string2, string3, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), l2));
                    arrayList2.clear();
                }
                string = arrstring[1].trim();
                continue;
            }
            if (string5.equals("device id")) {
                string2 = arrstring[1].trim();
                continue;
            }
            if (string5.equals("vendor")) {
                string3 = arrstring[1].trim();
                continue;
            }
            if (string5.contains("version") || string5.contains("revision")) {
                arrayList2.add(string4.trim());
                continue;
            }
            if (!string5.startsWith("vram")) continue;
            l2 = lOilLanD.lli011lLANd(arrstring[1].trim());
        }
        arrayList.add(new IiO1iILaNd(string, string2, string3, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), l2));
        return arrayList;
    }
}

