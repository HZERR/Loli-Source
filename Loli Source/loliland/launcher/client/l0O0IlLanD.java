/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.iIl0il1lAnD;
import loliland.launcher.client.lOilLanD;

final class l0O0IlLanD
extends iIl0il1lAnD {
    l0O0IlLanD(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List O1il1llOLANd() {
        List list = l0O0IlLanD.Oill1LAnD();
        if (list.isEmpty()) {
            list = l0O0IlLanD.lIOILand();
        }
        return list;
    }

    private static List Oill1LAnD() {
        ArrayList<l0O0IlLanD> arrayList = new ArrayList<l0O0IlLanD>();
        List list = Iill1lanD.I1O1I1LaNd("lspci -vnnm");
        String string = "unknown";
        String string2 = "unknown";
        String string3 = "unknown";
        ArrayList<String> arrayList2 = new ArrayList<String>();
        boolean bl = false;
        String string4 = null;
        for (String string5 : list) {
            O1IiIiI1LAND o1IiIiI1LAND;
            String[] arrstring = string5.trim().split(":", 2);
            String string6 = arrstring[0];
            if (string6.equals("Class") && string5.contains("VGA")) {
                bl = true;
            } else if (string6.equals("Device") && !bl && arrstring.length > 1) {
                string4 = arrstring[1].trim();
            }
            if (!bl) continue;
            if (arrstring.length < 2) {
                arrayList.add(new l0O0IlLanD(string, string2, string3, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), l0O0IlLanD.I1O1I1LaNd(string4)));
                arrayList2.clear();
                bl = false;
                continue;
            }
            if (string6.equals("Device")) {
                o1IiIiI1LAND = lOilLanD.l11lLANd(arrstring[1].trim());
                if (o1IiIiI1LAND == null) continue;
                string = (String)o1IiIiI1LAND.I1O1I1LaNd();
                string2 = "0x" + (String)o1IiIiI1LAND.OOOIilanD();
                continue;
            }
            if (string6.equals("Vendor")) {
                o1IiIiI1LAND = lOilLanD.l11lLANd(arrstring[1].trim());
                if (o1IiIiI1LAND != null) {
                    string3 = (String)o1IiIiI1LAND.I1O1I1LaNd() + " (0x" + (String)o1IiIiI1LAND.OOOIilanD() + ")";
                    continue;
                }
                string3 = arrstring[1].trim();
                continue;
            }
            if (!string6.equals("Rev:")) continue;
            arrayList2.add(string5.trim());
        }
        if (bl) {
            arrayList.add(new l0O0IlLanD(string, string2, string3, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), l0O0IlLanD.I1O1I1LaNd(string4)));
        }
        return arrayList;
    }

    private static long I1O1I1LaNd(String string) {
        long l2 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("lspci -v -s " + string);
        for (String string2 : list) {
            if (!string2.contains(" prefetchable")) continue;
            l2 += lOilLanD.lO110l1LANd(string2);
        }
        return l2;
    }

    private static List lIOILand() {
        ArrayList<l0O0IlLanD> arrayList = new ArrayList<l0O0IlLanD>();
        List list = Iill1lanD.I1O1I1LaNd("lshw -C display");
        String string = "unknown";
        String string2 = "unknown";
        String string3 = "unknown";
        ArrayList<String> arrayList2 = new ArrayList<String>();
        long l2 = 0L;
        int n2 = 0;
        for (String string4 : list) {
            String[] arrstring = string4.trim().split(":");
            if (arrstring[0].startsWith("*-display")) {
                if (n2++ <= 0) continue;
                arrayList.add(new l0O0IlLanD(string, string2, string3, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), l2));
                arrayList2.clear();
                continue;
            }
            if (arrstring.length != 2) continue;
            String string5 = arrstring[0];
            if (string5.equals("product")) {
                string = arrstring[1].trim();
                continue;
            }
            if (string5.equals("vendor")) {
                string3 = arrstring[1].trim();
                continue;
            }
            if (string5.equals("version")) {
                arrayList2.add(string4.trim());
                continue;
            }
            if (!string5.startsWith("resources")) continue;
            l2 = lOilLanD.IO11O0LANd(arrstring[1].trim());
        }
        arrayList.add(new l0O0IlLanD(string, string2, string3, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)", ", arrayList2), l2));
        return arrayList;
    }
}

