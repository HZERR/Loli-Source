/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class iilOi1LanD {
    private iilOi1LanD() {
    }

    public static String I1O1I1LaNd(int n2) {
        List list = Iill1lanD.I1O1I1LaNd("ps -axwwo cwd -p " + n2);
        if (!list.isEmpty()) {
            return (String)list.get(1);
        }
        return "";
    }

    public static long OOOIilanD(int n2) {
        long l2 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("fstat -sp " + n2);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim(), 11);
            if (arrstring.length != 11 || "pipe".contains(arrstring[4]) || "unix".contains(arrstring[4])) continue;
            ++l2;
        }
        return l2 - 1L;
    }
}

