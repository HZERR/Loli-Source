/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class lli011lLANd {
    private lli011lLANd() {
    }

    public static String I1O1I1LaNd() {
        String string = "product:";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshw -C system")) {
            if (!string2.contains(string)) continue;
            return string2.split(string)[1].trim();
        }
        return null;
    }

    public static String OOOIilanD() {
        String string = "serial:";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshw -C system")) {
            if (!string2.contains(string)) continue;
            return string2.split(string)[1].trim();
        }
        return null;
    }

    public static String lI00OlAND() {
        String string = "uuid:";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshw -C system")) {
            if (!string2.contains(string)) continue;
            return string2.split(string)[1].trim();
        }
        return null;
    }

    public static long lli0OiIlAND() {
        String string = "capacity:";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshw -class processor")) {
            if (!string2.contains(string)) continue;
            return lOilLanD.I1O1I1LaNd(string2.split(string)[1].trim());
        }
        return -1L;
    }
}

