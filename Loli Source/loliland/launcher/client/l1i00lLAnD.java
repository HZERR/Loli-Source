/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class l1i00lLAnD {
    private static final String[] I1O1I1LaNd = new String[]{"xrandr", "--verbose"};

    private l1i00lLAnD() {
    }

    public static List I1O1I1LaNd() {
        List list = Iill1lanD.I1O1I1LaNd(I1O1I1LaNd, null);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        StringBuilder stringBuilder = null;
        for (String string : list) {
            if (string.contains("EDID")) {
                stringBuilder = new StringBuilder();
                continue;
            }
            if (stringBuilder == null) continue;
            stringBuilder.append(string.trim());
            if (stringBuilder.length() < 256) continue;
            String string2 = stringBuilder.toString();
            byte[] arrby = lOilLanD.lI00OlAND(string2);
            if (arrby.length >= 128) {
                arrayList.add(arrby);
            }
            stringBuilder = null;
        }
        return arrayList;
    }
}

