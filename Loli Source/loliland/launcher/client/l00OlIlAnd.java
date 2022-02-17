/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.lOilLanD;

public final class l00OlIlAnd {
    private l00OlIlAnd() {
    }

    public static List I1O1I1LaNd(String string, Map map) {
        String string2;
        Object object222;
        String string22 = "PV STATE:";
        String string3 = "PP SIZE:";
        long l2 = 0L;
        for (Object object222 : Iill1lanD.I1O1I1LaNd("lspv -L " + string)) {
            if (((String)object222).startsWith(string22)) {
                if (((String)object222).contains("active")) continue;
                return Collections.emptyList();
            }
            if (!((String)object222).contains(string3)) continue;
            l2 = lOilLanD.Oill1LAnD((String)object222);
        }
        if (l2 == 0L) {
            return Collections.emptyList();
        }
        l2 <<= 20;
        HashMap hashMap = new HashMap();
        object222 = new HashMap();
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        for (Object object3 : Iill1lanD.I1O1I1LaNd("lspv -p " + string)) {
            String[] object4 = lOilLanD.OOOIilanD.split(((String)object3).trim());
            if (object4.length < 6 || !"used".equals(object4[1])) continue;
            string2 = object4[object4.length - 3];
            hashMap.put(string2, object4[object4.length - 1]);
            object222.put(string2, object4[object4.length - 2]);
            int n2 = 1 + lOilLanD.li0iOILAND(object4[0], 2) - lOilLanD.li0iOILAND(object4[0], 1);
            hashMap2.put(string2, n2 + hashMap2.getOrDefault(string2, 0));
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : hashMap.entrySet()) {
            string2 = "N/A".equals(entry.getValue()) ? "" : (String)entry.getValue();
            String string4 = (String)entry.getKey();
            String string5 = (String)object222.get(string4);
            long l3 = l2 * (long)((Integer)hashMap2.get(string4)).intValue();
            O1IiIiI1LAND o1IiIiI1LAND = (O1IiIiI1LAND)map.get(string4);
            int n3 = o1IiIiI1LAND == null ? lOilLanD.Oill1LAnD(string4) : (Integer)o1IiIiI1LAND.I1O1I1LaNd();
            int n4 = o1IiIiI1LAND == null ? lOilLanD.Oill1LAnD(string4) : (Integer)o1IiIiI1LAND.OOOIilanD();
            arrayList.add(new i0Oi1LANd(string4, string4, string5, "", l3, n3, n4, string2));
        }
        return arrayList;
    }
}

