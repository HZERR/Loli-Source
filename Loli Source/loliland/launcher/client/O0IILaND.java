/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.II0l0LaND;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.il000OLANd;

public class O0IILaND
extends II0l0LaND {
    public O0IILaND(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl) {
        List list = O0IILaND.lIOILand();
        if (bl) {
            return list;
        }
        ArrayList<O0IILaND> arrayList = new ArrayList<O0IILaND>();
        for (il000OLANd il000OLANd2 : list) {
            arrayList.add(new O0IILaND(il000OLANd2.I1O1I1LaNd(), il000OLANd2.OOOIilanD(), il000OLANd2.lI00OlAND(), il000OLANd2.lli0OiIlAND(), il000OLANd2.li0iOILAND(), il000OLANd2.O1il1llOLANd(), Collections.emptyList()));
            O0IILaND.I1O1I1LaNd(arrayList, il000OLANd2.Oill1LAnD());
        }
        return arrayList;
    }

    private static List lIOILand() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        HashMap<String, String> hashMap3 = new HashMap<String, String>();
        HashMap<String, String> hashMap4 = new HashMap<String, String>();
        HashMap<String, String> hashMap5 = new HashMap<String, String>();
        HashMap<String, List> hashMap6 = new HashMap<String, List>();
        ArrayList<String> arrayList = new ArrayList<String>();
        String string2 = "";
        String string3 = "";
        for (Object object : Iill1lanD.I1O1I1LaNd("usbdevs -v")) {
            if (((String)object).startsWith("Controller ")) {
                string3 = ((String)object).substring(11);
                continue;
            }
            if (((String)object).startsWith("addr ")) {
                if (((String)object).indexOf(58) != 7 || ((String)object).indexOf(44) < 18) continue;
                string2 = string3 + ((String)object).substring(0, 7);
                String[] arrstring = ((String)object).substring(8).trim().split(",");
                if (arrstring.length <= 1) continue;
                String string4 = arrstring[0].trim();
                int n2 = string4.indexOf(58);
                int n3 = string4.indexOf(32);
                if (n2 >= 0 && n3 >= 0) {
                    hashMap3.put(string2, string4.substring(0, n2));
                    hashMap4.put(string2, string4.substring(n2 + 1, n3));
                    hashMap2.put(string2, string4.substring(n3 + 1));
                }
                hashMap.put(string2, arrstring[1].trim());
                hashMap6.computeIfAbsent(string3, string -> new ArrayList()).add(string2);
                if (string3.contains("addr")) continue;
                string3 = string2;
                arrayList.add(string3);
                continue;
            }
            if (string2.isEmpty()) continue;
            int n4 = ((String)object).indexOf("iSerial ");
            if (n4 >= 0) {
                hashMap5.put(string2, ((String)object).substring(n4 + 8).trim());
            }
            string2 = "";
        }
        ArrayList arrayList2 = new ArrayList();
        for (String string5 : arrayList) {
            arrayList2.add(O0IILaND.I1O1I1LaNd(string5, "0000", "0000", hashMap, hashMap2, hashMap3, hashMap4, hashMap5, hashMap6));
        }
        return arrayList2;
    }

    private static void I1O1I1LaNd(List list, List list2) {
        for (il000OLANd il000OLANd2 : list2) {
            list.add(il000OLANd2);
            O0IILaND.I1O1I1LaNd(list, il000OLANd2.Oill1LAnD());
        }
    }

    private static O0IILaND I1O1I1LaNd(String string, String string2, String string3, Map map, Map map2, Map map3, Map map4, Map map5, Map map6) {
        String string4 = map3.getOrDefault(string, string2);
        String string5 = map4.getOrDefault(string, string3);
        List list = map6.getOrDefault(string, new ArrayList());
        ArrayList<O0IILaND> arrayList = new ArrayList<O0IILaND>();
        for (String string6 : list) {
            arrayList.add(O0IILaND.I1O1I1LaNd(string6, string4, string5, map, map2, map3, map4, map5, map6));
        }
        Collections.sort(arrayList);
        return new O0IILaND(map.getOrDefault(string, string4 + ":" + string5), map2.getOrDefault(string, ""), string4, string5, map5.getOrDefault(string, ""), string, arrayList);
    }
}

