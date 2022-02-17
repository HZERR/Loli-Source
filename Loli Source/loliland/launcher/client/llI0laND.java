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
import loliland.launcher.client.lOilLanD;

public class llI0laND
extends II0l0LaND {
    public llI0laND(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl) {
        List list = llI0laND.lIOILand();
        if (bl) {
            return list;
        }
        ArrayList<llI0laND> arrayList = new ArrayList<llI0laND>();
        for (il000OLANd il000OLANd2 : list) {
            arrayList.add(new llI0laND(il000OLANd2.I1O1I1LaNd(), il000OLANd2.OOOIilanD(), il000OLANd2.lI00OlAND(), il000OLANd2.lli0OiIlAND(), il000OLANd2.li0iOILAND(), il000OLANd2.O1il1llOLANd(), Collections.emptyList()));
            llI0laND.I1O1I1LaNd(arrayList, il000OLANd2.Oill1LAnD());
        }
        return arrayList;
    }

    private static List lIOILand() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        HashMap<String, String> hashMap3 = new HashMap<String, String>();
        HashMap<String, String> hashMap4 = new HashMap<String, String>();
        HashMap<String, String> hashMap5 = new HashMap<String, String>();
        HashMap<String, String> hashMap6 = new HashMap<String, String>();
        HashMap<String, List<Object>> hashMap7 = new HashMap<String, List<Object>>();
        List list = Iill1lanD.I1O1I1LaNd("lshal");
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        String string2 = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Object object : list) {
            String string3;
            if (((String)object).startsWith("udi =")) {
                string2 = lOilLanD.li0iOILAND((String)object);
                continue;
            }
            if (string2.isEmpty() || ((String)(object = ((String)object).trim())).isEmpty()) continue;
            if (((String)object).startsWith("freebsd.driver =") && "usbus".equals(lOilLanD.li0iOILAND((String)object))) {
                arrayList.add(string2);
                continue;
            }
            if (((String)object).contains(".parent =")) {
                string3 = lOilLanD.li0iOILAND((String)object);
                if (string2.replace(string3, "").startsWith("_if")) continue;
                hashMap6.put(string2, string3);
                hashMap7.computeIfAbsent(string3, string -> new ArrayList()).add(string2);
                continue;
            }
            if (((String)object).contains(".product =")) {
                hashMap.put(string2, lOilLanD.li0iOILAND((String)object));
                continue;
            }
            if (((String)object).contains(".vendor =")) {
                hashMap2.put(string2, lOilLanD.li0iOILAND((String)object));
                continue;
            }
            if (((String)object).contains(".serial =")) {
                string3 = lOilLanD.li0iOILAND((String)object);
                hashMap5.put(string2, string3.startsWith("0x") ? lOilLanD.lli0OiIlAND(string3.replace("0x", "")) : string3);
                continue;
            }
            if (((String)object).contains(".vendor_id =")) {
                hashMap3.put(string2, String.format("%04x", lOilLanD.Oill1LAnD((String)object)));
                continue;
            }
            if (!((String)object).contains(".product_id =")) continue;
            hashMap4.put(string2, String.format("%04x", lOilLanD.Oill1LAnD((String)object)));
        }
        ArrayList arrayList2 = new ArrayList();
        for (String string3 : arrayList) {
            String string4 = (String)hashMap6.get(string3);
            hashMap7.put(string4, (List<Object>)hashMap7.get(string3));
            arrayList2.add(llI0laND.I1O1I1LaNd(string4, "0000", "0000", hashMap, hashMap2, hashMap3, hashMap4, hashMap5, hashMap7));
        }
        return arrayList2;
    }

    private static void I1O1I1LaNd(List list, List list2) {
        for (il000OLANd il000OLANd2 : list2) {
            list.add(il000OLANd2);
            llI0laND.I1O1I1LaNd(list, il000OLANd2.Oill1LAnD());
        }
    }

    private static llI0laND I1O1I1LaNd(String string, String string2, String string3, Map map, Map map2, Map map3, Map map4, Map map5, Map map6) {
        String string4 = map3.getOrDefault(string, string2);
        String string5 = map4.getOrDefault(string, string3);
        List list = map6.getOrDefault(string, new ArrayList());
        ArrayList<llI0laND> arrayList = new ArrayList<llI0laND>();
        for (String string6 : list) {
            arrayList.add(llI0laND.I1O1I1LaNd(string6, string4, string5, map, map2, map3, map4, map5, map6));
        }
        Collections.sort(arrayList);
        return new llI0laND(map.getOrDefault(string, string4 + ":" + string5), map2.getOrDefault(string, ""), string4, string5, map5.getOrDefault(string, ""), string, arrayList);
    }
}

