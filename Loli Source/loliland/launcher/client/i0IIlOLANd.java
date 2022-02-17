/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class i0IIlOLANd {
    private static final String I1O1I1LaNd = "geom disk list";

    private i0IIlOLANd() {
    }

    public static Map I1O1I1LaNd() {
        HashMap<String, IIOOOlIiLanD> hashMap = new HashMap<String, IIOOOlIiLanD>();
        String string = null;
        String string2 = "unknown";
        String string3 = "unknown";
        long l2 = 0L;
        List list = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd);
        for (String string4 : list) {
            String[] arrstring;
            if ((string4 = string4.trim()).startsWith("Geom name:")) {
                if (string != null) {
                    hashMap.put(string, new IIOOOlIiLanD(string2, string3, l2));
                    string2 = "unknown";
                    string3 = "unknown";
                    l2 = 0L;
                }
                string = string4.substring(string4.lastIndexOf(32) + 1);
            }
            if (string == null) continue;
            if ((string4 = string4.trim()).startsWith("Mediasize:") && (arrstring = lOilLanD.OOOIilanD.split(string4)).length > 1) {
                l2 = lOilLanD.OOOIilanD(arrstring[1], 0L);
            }
            if (string4.startsWith("descr:")) {
                string2 = string4.replace("descr:", "").trim();
            }
            if (!string4.startsWith("ident:")) continue;
            string3 = string4.replace("ident:", "").replace("(null)", "").trim();
        }
        if (string != null) {
            hashMap.put(string, new IIOOOlIiLanD(string2, string3, l2));
        }
        return hashMap;
    }
}

