/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.iOIl0LAnD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class l0iIlIO1laNd {
    private l0iIlIO1laNd() {
    }

    public static Map I1O1I1LaNd() {
        HashMap hashMap = new HashMap();
        iOIl0LAnD[] arriOIl0LAnD = (iOIl0LAnD[])iOIl0LAnD.class.getEnumConstants();
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lli0OiIlAND);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
            EnumMap<iOIl0LAnD, Long> enumMap = new EnumMap<iOIl0LAnD, Long>(iOIl0LAnD.class);
            String string2 = null;
            for (int i2 = 0; i2 < arriOIl0LAnD.length && i2 < arrstring.length; ++i2) {
                if (arriOIl0LAnD[i2] == iOIl0LAnD.lI00OlAND) {
                    string2 = arrstring[i2];
                    continue;
                }
                enumMap.put(arriOIl0LAnD[i2], lOilLanD.OOOIilanD(arrstring[i2], 0L));
            }
            if (string2 == null) continue;
            hashMap.put(string2, enumMap);
        }
        return hashMap;
    }
}

