/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lIOI1Land;
import loliland.launcher.client.lOilLanD;

public final class lili0l0laNd {
    private static final String[] I1O1I1LaNd = lOilLanD.OOOIilanD.split("xprop -root _NET_CLIENT_LIST_STACKING");
    private static final String[] OOOIilanD = lOilLanD.OOOIilanD.split("xwininfo -root -tree");
    private static final String[] lI00OlAND = lOilLanD.OOOIilanD.split("xprop _NET_WM_PID -id");

    private lili0l0laNd() {
    }

    public static List I1O1I1LaNd(boolean bl) {
        String string;
        Object object;
        int n2;
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        int n3 = 0;
        List list = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd, null);
        if (!list.isEmpty() && (n2 = ((String)(object = (String)list.get(0))).indexOf("0x")) >= 0) {
            for (String object2 : ((String)object).substring(n2).split(", ")) {
                hashMap.put(object2, ++n3);
            }
        }
        object = Pattern.compile("(0x\\S+) (?:\"(.+)\")?.*: \\((?:\"(.+)\" \".+\")?\\)  (\\d+)x(\\d+)\\+.+  \\+(-?\\d+)\\+(-?\\d+)");
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        String[] arrstring = new HashMap();
        LinkedHashMap<String, Rectangle> linkedHashMap = new LinkedHashMap<String, Rectangle>();
        for (String string2 : Iill1lanD.I1O1I1LaNd(OOOIilanD, null)) {
            String string3;
            Matcher matcher = ((Pattern)object).matcher(string2.trim());
            if (!matcher.matches()) continue;
            string = matcher.group(1);
            if (bl && !hashMap.containsKey(string)) continue;
            String string4 = matcher.group(2);
            if (!iiIIIlO1lANd.I1O1I1LaNd(string4)) {
                hashMap2.put(string, string4);
            }
            if (!iiIIIlO1lANd.I1O1I1LaNd(string3 = matcher.group(3))) {
                arrstring.put(string, string3);
            }
            linkedHashMap.put(string, new Rectangle(lOilLanD.lli0OiIlAND(matcher.group(6), 0), lOilLanD.lli0OiIlAND(matcher.group(7), 0), lOilLanD.lli0OiIlAND(matcher.group(4), 0), lOilLanD.lli0OiIlAND(matcher.group(5), 0)));
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            string = (String)entry.getKey();
            long l2 = lili0l0laNd.I1O1I1LaNd(string);
            boolean bl2 = hashMap.containsKey(string);
            arrayList.add(new lIOI1Land(lOilLanD.li0iOILAND(string, 0L), hashMap2.getOrDefault(string, ""), arrstring.getOrDefault(string, ""), (Rectangle)entry.getValue(), l2, hashMap.getOrDefault(string, 0), bl2));
        }
        return arrayList;
    }

    private static long I1O1I1LaNd(String string) {
        String[] arrstring = new String[lI00OlAND.length + 1];
        System.arraycopy(lI00OlAND, 0, arrstring, 0, lI00OlAND.length);
        arrstring[lili0l0laNd.lI00OlAND.length] = string;
        List list = Iill1lanD.I1O1I1LaNd(arrstring, null);
        if (list.isEmpty()) {
            return 0L;
        }
        return lOilLanD.Oill1LAnD((String)list.get(0));
    }
}

