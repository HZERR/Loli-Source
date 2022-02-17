/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.l0lllAND;
import loliland.launcher.client.lOilLanD;

public final class li0ii01laND {
    private static final String I1O1I1LaNd = "geom part list";
    private static final String OOOIilanD = "stat -f %i /dev/";

    private li0ii01laND() {
    }

    public static Map I1O1I1LaNd() {
        Map map = l0lllAND.I1O1I1LaNd();
        HashMap<String, List<Object>> hashMap = new HashMap<String, List<Object>>();
        String string = null;
        List<Object> list = new ArrayList();
        Object object = null;
        Object object2 = "unknown";
        Object object3 = "unknown";
        Object object4 = "unknown";
        long l2 = 0L;
        String string2 = "";
        List list2 = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd);
        for (String string3 : list2) {
            Object object5;
            if ((string3 = string3.trim()).startsWith("Geom name:")) {
                if (string != null && !list.isEmpty()) {
                    hashMap.put(string, list);
                    list = new ArrayList();
                }
                string = string3.substring(string3.lastIndexOf(32) + 1);
            }
            if (string == null) continue;
            if (string3.contains("Name:")) {
                if (object != null) {
                    int n2 = lOilLanD.lli0OiIlAND(Iill1lanD.OOOIilanD(OOOIilanD + object), 0);
                    list.add(new i0Oi1LANd((String)object2, (String)object, (String)object3, (String)object4, l2, 0, n2, string2));
                    object = null;
                    object2 = "unknown";
                    object3 = "unknown";
                    object4 = "unknown";
                    l2 = 0L;
                }
                if (((String)(object5 = string3.substring(string3.lastIndexOf(32) + 1))).startsWith(string)) {
                    object = object5;
                    object2 = object5;
                    string2 = map.getOrDefault(object5, "");
                }
            }
            if (object == null || ((String[])(object5 = lOilLanD.OOOIilanD.split(string3))).length < 2) continue;
            if (string3.startsWith("Mediasize:")) {
                l2 = lOilLanD.OOOIilanD((String)object5[1], 0L);
                continue;
            }
            if (string3.startsWith("rawuuid:")) {
                object4 = object5[1];
                continue;
            }
            if (!string3.startsWith("type:")) continue;
            object3 = object5[1];
        }
        if (string != null) {
            if (object != null) {
                int n3 = lOilLanD.lli0OiIlAND(Iill1lanD.OOOIilanD(OOOIilanD + object), 0);
                list.add(new i0Oi1LANd((String)object2, (String)object, (String)object3, (String)object4, l2, 0, n3, string2));
            }
            if (!list.isEmpty()) {
                list = list.stream().sorted(Comparator.comparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList());
                hashMap.put(string, list);
            }
        }
        return hashMap;
    }
}

