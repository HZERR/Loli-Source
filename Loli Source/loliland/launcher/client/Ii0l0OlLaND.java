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

public class Ii0l0OlLaND
extends II0l0LaND {
    private static final String I1O1I1LaNd = "000c";

    public Ii0l0OlLaND(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl) {
        List list = Ii0l0OlLaND.lIOILand();
        if (bl) {
            return list;
        }
        ArrayList<Ii0l0OlLaND> arrayList = new ArrayList<Ii0l0OlLaND>();
        for (il000OLANd il000OLANd2 : list) {
            arrayList.add(new Ii0l0OlLaND(il000OLANd2.I1O1I1LaNd(), il000OLANd2.OOOIilanD(), il000OLANd2.lI00OlAND(), il000OLANd2.lli0OiIlAND(), il000OLANd2.li0iOILAND(), il000OLANd2.O1il1llOLANd(), Collections.emptyList()));
            Ii0l0OlLaND.I1O1I1LaNd(arrayList, il000OLANd2.Oill1LAnD());
        }
        return arrayList;
    }

    private static List lIOILand() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        HashMap<String, String> hashMap3 = new HashMap<String, String>();
        HashMap<String, List> hashMap4 = new HashMap<String, List>();
        HashMap<String, String> hashMap5 = new HashMap<String, String>();
        List list = Iill1lanD.I1O1I1LaNd("prtconf -pv");
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        HashMap<Integer, String> hashMap6 = new HashMap<Integer, String>();
        String string2 = "";
        int n2 = 0;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Object object : list) {
            if (((String)object).contains("Node 0x")) {
                string2 = ((String)object).replaceFirst("^\\s*", "");
                int n3 = ((String)object).length() - string2.length();
                if (n2 == 0) {
                    n2 = n3;
                }
                hashMap6.put(n3, string2);
                if (n3 > n2) {
                    hashMap4.computeIfAbsent((String)hashMap6.get(n3 - n2), string -> new ArrayList()).add(string2);
                    continue;
                }
                arrayList.add(string2);
                continue;
            }
            if (string2.isEmpty()) continue;
            if (((String)(object = ((String)object).trim())).startsWith("model:")) {
                hashMap.put(string2, lOilLanD.li0iOILAND((String)object));
                continue;
            }
            if (((String)object).startsWith("name:")) {
                hashMap.putIfAbsent(string2, lOilLanD.li0iOILAND((String)object));
                continue;
            }
            if (((String)object).startsWith("vendor-id:")) {
                hashMap2.put(string2, ((String)object).substring(((String)object).length() - 4));
                continue;
            }
            if (((String)object).startsWith("device-id:")) {
                hashMap3.put(string2, ((String)object).substring(((String)object).length() - 4));
                continue;
            }
            if (((String)object).startsWith("class-code:")) {
                hashMap5.putIfAbsent(string2, ((String)object).substring(((String)object).length() - 8, ((String)object).length() - 4));
                continue;
            }
            if (!((String)object).startsWith("device_type:")) continue;
            hashMap5.putIfAbsent(string2, lOilLanD.li0iOILAND((String)object));
        }
        ArrayList arrayList2 = new ArrayList();
        for (String string3 : arrayList) {
            if (!I1O1I1LaNd.equals(hashMap5.getOrDefault(string3, "")) && !"usb".equals(hashMap5.getOrDefault(string3, ""))) continue;
            arrayList2.add(Ii0l0OlLaND.I1O1I1LaNd(string3, "0000", "0000", hashMap, hashMap2, hashMap3, hashMap4));
        }
        return arrayList2;
    }

    private static void I1O1I1LaNd(List list, List list2) {
        for (il000OLANd il000OLANd2 : list2) {
            list.add(il000OLANd2);
            Ii0l0OlLaND.I1O1I1LaNd(list, il000OLANd2.Oill1LAnD());
        }
    }

    private static Ii0l0OlLaND I1O1I1LaNd(String string, String string2, String string3, Map map, Map map2, Map map3, Map map4) {
        String string4 = map2.getOrDefault(string, string2);
        String string5 = map3.getOrDefault(string, string3);
        List list = map4.getOrDefault(string, new ArrayList());
        ArrayList<Ii0l0OlLaND> arrayList = new ArrayList<Ii0l0OlLaND>();
        for (String string6 : list) {
            arrayList.add(Ii0l0OlLaND.I1O1I1LaNd(string6, string4, string5, map, map2, map3, map4));
        }
        Collections.sort(arrayList);
        return new Ii0l0OlLaND(map.getOrDefault(string, string4 + ":" + string5), "", string4, string5, "", string, arrayList);
    }
}

