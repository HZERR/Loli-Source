/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lOilLanD;

public final class liIIllIland {
    private liIIllIland() {
    }

    public static Map I1O1I1LaNd() {
        HashMap<String, O1IiIiI1LAND> hashMap = new HashMap<String, O1IiIiI1LAND>();
        for (String string : Iill1lanD.I1O1I1LaNd("ls -l /dev")) {
            int n2;
            if (string.isEmpty() || string.charAt(0) != 'b' || (n2 = string.lastIndexOf(32)) <= 0 || n2 >= string.length()) continue;
            String string2 = string.substring(n2 + 1);
            int n3 = lOilLanD.li0iOILAND(string, 2);
            int n4 = lOilLanD.li0iOILAND(string, 3);
            hashMap.put(string2, new O1IiIiI1LAND(n3, n4));
        }
        return hashMap;
    }
}

