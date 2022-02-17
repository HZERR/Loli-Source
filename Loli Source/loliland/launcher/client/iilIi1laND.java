/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class iilIi1laND {
    private iilIi1laND() {
    }

    public static String I1O1I1LaNd() {
        String string = "system.hardware.serial =";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshal")) {
            if (!string2.contains(string)) continue;
            return lOilLanD.li0iOILAND(string2);
        }
        return null;
    }

    public static String OOOIilanD() {
        String string = "system.hardware.uuid =";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshal")) {
            if (!string2.contains(string)) continue;
            return lOilLanD.li0iOILAND(string2);
        }
        return null;
    }
}

