/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class ilIlllanD {
    private static final String I1O1I1LaNd = "lshal";

    private ilIlllanD() {
    }

    public static Map I1O1I1LaNd() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        List list = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd);
        String string = null;
        for (String string2 : list) {
            if (string2.startsWith("udi ")) {
                String string3 = lOilLanD.li0iOILAND(string2);
                string = string3.substring(string3.lastIndexOf(47) + 1);
                continue;
            }
            if (!(string2 = string2.trim()).startsWith("block.major") || string == null) continue;
            hashMap.put(string, lOilLanD.Oill1LAnD(string2));
        }
        return hashMap;
    }
}

