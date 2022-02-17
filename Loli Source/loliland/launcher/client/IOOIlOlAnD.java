/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.llllil1LaND;

final class IOOIlOlAnD
extends llllil1LaND {
    IOOIlOlAnD() {
    }

    @Override
    public double lli0OiIlAND() {
        double d2 = 0.0;
        for (String string : Iill1lanD.I1O1I1LaNd("/usr/sbin/prtpicl -v -c temperature-sensor")) {
            int n2;
            if (!string.trim().startsWith("Temperature:") || !((double)(n2 = lOilLanD.I1O1I1LaNd(string, 0)) > d2)) continue;
            d2 = n2;
        }
        if (d2 > 1000.0) {
            d2 /= 1000.0;
        }
        return d2;
    }

    @Override
    public int[] li0iOILAND() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (String string : Iill1lanD.I1O1I1LaNd("/usr/sbin/prtpicl -v -c fan")) {
            if (!string.trim().startsWith("Speed:")) continue;
            arrayList.add(lOilLanD.I1O1I1LaNd(string, 0));
        }
        Object object = new int[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            object[i2] = (Integer)arrayList.get(i2);
        }
        return object;
    }

    @Override
    public double O1il1llOLANd() {
        double d2 = 0.0;
        for (String string : Iill1lanD.I1O1I1LaNd("/usr/sbin/prtpicl -v -c voltage-sensor")) {
            if (!string.trim().startsWith("Voltage:")) continue;
            d2 = lOilLanD.OOOIilanD(string.replace("Voltage:", "").trim(), 0.0);
            break;
        }
        return d2;
    }
}

