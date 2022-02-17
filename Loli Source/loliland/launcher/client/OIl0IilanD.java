/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.lOilLanD;

public final class OIl0IilanD {
    private static final String I1O1I1LaNd = "iostat -Er";
    private static final String OOOIilanD = "iostat -er";
    private static final String lI00OlAND = "iostat -ern";
    private static final String lli0OiIlAND = "device";

    private OIl0IilanD() {
    }

    public static Map I1O1I1LaNd() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        List list = Iill1lanD.I1O1I1LaNd(OOOIilanD);
        List list2 = Iill1lanD.I1O1I1LaNd(lI00OlAND);
        for (int i2 = 0; i2 < list.size() && i2 < list2.size(); ++i2) {
            String string;
            String[] arrstring;
            String string2 = (String)list.get(i2);
            String[] arrstring2 = string2.split(",");
            if (arrstring2.length < 5 || lli0OiIlAND.equals(arrstring2[0]) || (arrstring = (string = (String)list2.get(i2)).split(",")).length < 5 || lli0OiIlAND.equals(arrstring[4])) continue;
            hashMap.put(arrstring2[0], arrstring[4]);
        }
        return hashMap;
    }

    public static Map I1O1I1LaNd(Set set) {
        HashMap<String, OI1l11LaNd> hashMap = new HashMap<String, OI1l11LaNd>();
        List list = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd);
        String string = null;
        String string2 = "";
        String string3 = "";
        String string4 = "";
        String string5 = "";
        long l2 = 0L;
        for (String string6 : list) {
            String[] arrstring;
            for (String string7 : arrstring = string6.split(",")) {
                String[] arrstring2;
                if (set.contains(string7 = string7.trim())) {
                    if (string != null) {
                        hashMap.put(string, new OI1l11LaNd(string2, string3, string4, string5, l2));
                    }
                    string = string7;
                    string2 = "";
                    string3 = "";
                    string4 = "";
                    string5 = "";
                    l2 = 0L;
                    continue;
                }
                if (string7.startsWith("Model:")) {
                    string2 = string7.replace("Model:", "").trim();
                    continue;
                }
                if (string7.startsWith("Serial No:")) {
                    string5 = string7.replace("Serial No:", "").trim();
                    continue;
                }
                if (string7.startsWith("Vendor:")) {
                    string3 = string7.replace("Vendor:", "").trim();
                    continue;
                }
                if (string7.startsWith("Product:")) {
                    string4 = string7.replace("Product:", "").trim();
                    continue;
                }
                if (!string7.startsWith("Size:") || (arrstring2 = string7.split("<")).length <= 1) continue;
                arrstring2 = lOilLanD.OOOIilanD.split(arrstring2[1]);
                l2 = lOilLanD.OOOIilanD(arrstring2[0], 0L);
            }
            if (string == null) continue;
            hashMap.put(string, new OI1l11LaNd(string2, string3, string4, string5, l2));
        }
        return hashMap;
    }
}

