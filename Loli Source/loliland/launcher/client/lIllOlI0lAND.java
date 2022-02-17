/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class lIllOlI0lAND {
    private lIllOlI0lAND() {
    }

    public static Map I1O1I1LaNd(int n2) {
        List list = Iill1lanD.I1O1I1LaNd("procstat -f " + (n2 < 0 ? "-a" : Integer.valueOf(n2)));
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim(), 10);
            if (arrstring.length != 10 || !arrstring[2].equals("cwd")) continue;
            hashMap.put(lOilLanD.lli0OiIlAND(arrstring[0], -1), arrstring[9]);
        }
        return hashMap;
    }

    public static String OOOIilanD(int n2) {
        List list = Iill1lanD.I1O1I1LaNd("procstat -f " + n2);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim(), 10);
            if (arrstring.length != 10 || !arrstring[2].equals("cwd")) continue;
            return arrstring[9];
        }
        return "";
    }

    public static long lI00OlAND(int n2) {
        long l2 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("procstat -f " + n2);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim(), 10);
            if (arrstring.length != 10 || "Vd-".contains(arrstring[4])) continue;
            ++l2;
        }
        return l2;
    }
}

