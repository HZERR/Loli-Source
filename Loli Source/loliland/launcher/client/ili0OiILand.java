/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class ili0OiILand {
    private ili0OiILand() {
    }

    public static Map I1O1I1LaNd(int n2) {
        List list = Iill1lanD.I1O1I1LaNd("lsof -F n -d cwd" + (n2 < 0 ? "" : " -p " + n2));
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        Integer n3 = -1;
        for (String string : list) {
            if (string.isEmpty()) continue;
            switch (string.charAt(0)) {
                case 'p': {
                    n3 = lOilLanD.lli0OiIlAND(string.substring(1), -1);
                    break;
                }
                case 'n': {
                    hashMap.put(n3, string.substring(1));
                    break;
                }
            }
        }
        return hashMap;
    }

    public static String OOOIilanD(int n2) {
        List list = Iill1lanD.I1O1I1LaNd("lsof -F n -d cwd -p " + n2);
        for (String string : list) {
            if (string.isEmpty() || string.charAt(0) != 'n') continue;
            return string.substring(1).trim();
        }
        return "";
    }

    public static long lI00OlAND(int n2) {
        int n3 = Iill1lanD.I1O1I1LaNd("lsof -p " + n2).size();
        return n3 > 0 ? (long)n3 - 1L : 0L;
    }
}

