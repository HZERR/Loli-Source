/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lOilLanD;

public final class liO1Oli1lanD {
    private liO1Oli1lanD() {
    }

    public static Map I1O1I1LaNd() {
        int n2 = 0;
        int n3 = 0;
        HashMap<Integer, O1IiIiI1LAND> hashMap = new HashMap<Integer, O1IiIiI1LAND>();
        List list = Iill1lanD.I1O1I1LaNd("lssrad -av");
        if (!list.isEmpty()) {
            list.remove(0);
        }
        for (String string : list) {
            String string2 = string.trim();
            if (string2.isEmpty()) continue;
            if (Character.isDigit(string.charAt(0))) {
                n2 = lOilLanD.lli0OiIlAND(string2, 0);
                continue;
            }
            if (string2.contains(".")) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string2, 3);
                n3 = lOilLanD.lli0OiIlAND(arrstring[0], 0);
                string2 = arrstring.length > 2 ? arrstring[2] : "";
            }
            for (Integer n4 : lOilLanD.l0iIlIO1laNd(string2)) {
                hashMap.put(n4, new O1IiIiI1LAND(n2, n3));
            }
        }
        return hashMap;
    }
}

