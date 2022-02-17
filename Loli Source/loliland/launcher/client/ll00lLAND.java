/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.ilI1l1IOLanD;
import loliland.launcher.client.lOilLanD;

final class ll00lLAND
extends ilI1l1IOLanD {
    private static final String I1O1I1LaNd = "lshal";

    ll00lLAND(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public static List lli0OiIlAND() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        hashMap.clear();
        hashMap2.clear();
        ArrayList<String> arrayList = new ArrayList<String>();
        String string = "";
        for (Object object : Iill1lanD.I1O1I1LaNd(I1O1I1LaNd)) {
            if (((String)(object = ((String)object).trim())).startsWith("udi =")) {
                string = lOilLanD.li0iOILAND((String)object);
                continue;
            }
            if (string.isEmpty() || ((String)object).isEmpty()) continue;
            if (((String)object).contains("freebsd.driver =") && "pcm".equals(lOilLanD.li0iOILAND((String)object))) {
                arrayList.add(string);
                continue;
            }
            if (((String)object).contains("info.product")) {
                hashMap2.put(string, lOilLanD.I1O1I1LaNd((String)object, '\''));
                continue;
            }
            if (!((String)object).contains("info.vendor")) continue;
            hashMap.put(string, lOilLanD.I1O1I1LaNd((String)object, '\''));
        }
        ArrayList arrayList2 = new ArrayList();
        for (String string2 : arrayList) {
            arrayList2.add(new ll00lLAND((String)hashMap2.get(string2), (String)hashMap.get(string2) + " " + (String)hashMap2.get(string2), (String)hashMap2.get(string2)));
        }
        return arrayList2;
    }
}

