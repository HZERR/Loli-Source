/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.Guid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import loliland.launcher.client.II0l0LaND;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.il000OLANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liIil1lllAND;

public class lOlOillIlANd
extends II0l0LaND {
    private static final Guid.GUID I1O1I1LaNd = new Guid.GUID("{3ABF6F2D-71C4-462A-8A92-1E6861E6AF27}");

    public lOlOillIlANd(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl) {
        List list = lOlOillIlANd.lIOILand();
        if (bl) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (il000OLANd il000OLANd2 : list) {
            lOlOillIlANd.I1O1I1LaNd(arrayList, il000OLANd2.Oill1LAnD());
        }
        return arrayList;
    }

    private static void I1O1I1LaNd(List list, List list2) {
        for (il000OLANd il000OLANd2 : list2) {
            list.add(new lOlOillIlANd(il000OLANd2.I1O1I1LaNd(), il000OLANd2.OOOIilanD(), il000OLANd2.lI00OlAND(), il000OLANd2.lli0OiIlAND(), il000OLANd2.li0iOILAND(), il000OLANd2.O1il1llOLANd(), Collections.emptyList()));
            lOlOillIlANd.I1O1I1LaNd(list, il000OLANd2.Oill1LAnD());
        }
    }

    private static List lIOILand() {
        OI1l11LaNd oI1l11LaNd = liIil1lllAND.I1O1I1LaNd(I1O1I1LaNd);
        Map map = (Map)oI1l11LaNd.OOOIilanD();
        Map map2 = (Map)oI1l11LaNd.lI00OlAND();
        Map map3 = (Map)oI1l11LaNd.lli0OiIlAND();
        Map map4 = (Map)oI1l11LaNd.li0iOILAND();
        ArrayList<lOlOillIlANd> arrayList = new ArrayList<lOlOillIlANd>();
        for (Integer n2 : (Set)oI1l11LaNd.I1O1I1LaNd()) {
            lOlOillIlANd lOlOillIlANd2 = lOlOillIlANd.I1O1I1LaNd(n2, map, map2, map3, map4, "0000", "0000", "");
            if (lOlOillIlANd2 == null) continue;
            arrayList.add(lOlOillIlANd2);
        }
        return arrayList;
    }

    private static lOlOillIlANd I1O1I1LaNd(Integer n2, Map map, Map map2, Map map3, Map map4, String string, String string2, String string3) {
        Object object;
        String string4 = string;
        String string5 = string2;
        String string6 = string3;
        IIOOOlIiLanD iIOOOlIiLanD = lOilLanD.l0illAND((String)map3.get(n2));
        if (iIOOOlIiLanD != null) {
            string4 = (String)iIOOOlIiLanD.I1O1I1LaNd();
            string5 = (String)iIOOOlIiLanD.OOOIilanD();
            string6 = (String)iIOOOlIiLanD.lI00OlAND();
            if (string6.isEmpty() && string4.equals(string) && string5.equals(string2)) {
                string6 = string3;
            }
        }
        Set set = map.entrySet().stream().filter(entry -> ((Integer)entry.getValue()).equals(n2)).map(Map.Entry::getKey).collect(Collectors.toSet());
        ArrayList<lOlOillIlANd> arrayList = new ArrayList<lOlOillIlANd>();
        for (Object object2 : set) {
            object = lOlOillIlANd.I1O1I1LaNd((Integer)object2, map, map2, map3, map4, string4, string5, string6);
            if (object == null) continue;
            arrayList.add((lOlOillIlANd)object);
        }
        Collections.sort(arrayList);
        if (map2.containsKey(n2)) {
            Object object2;
            Object object3 = (String)map2.get(n2);
            if (((String)object3).isEmpty()) {
                object3 = string4 + ":" + string5;
            }
            object2 = (String)map3.get(n2);
            object = (String)map4.get(n2);
            return new lOlOillIlANd((String)object3, (String)object, string4, string5, string6, (String)object2, arrayList);
        }
        return null;
    }
}

