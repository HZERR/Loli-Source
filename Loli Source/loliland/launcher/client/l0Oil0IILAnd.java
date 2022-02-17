/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lOilLanD;

public final class l0Oil0IILAnd {
    private l0Oil0IILAnd() {
    }

    public static List I1O1I1LaNd() {
        return Iill1lanD.I1O1I1LaNd("lscfg -vp");
    }

    public static IIOOOlIiLanD I1O1I1LaNd(List list) {
        String string = "WAY BACKPLANE";
        String string2 = "Part Number";
        String string3 = "Serial Number";
        String string4 = "Version";
        String string5 = "Physical Location";
        String string6 = null;
        String string7 = null;
        String string8 = null;
        boolean bl = false;
        for (String string9 : list) {
            if (!bl && string9.contains("WAY BACKPLANE")) {
                bl = true;
                continue;
            }
            if (!bl) continue;
            if (string9.contains("Part Number")) {
                string6 = lOilLanD.iOIl0LAnD(string9.split("Part Number")[1].trim());
                continue;
            }
            if (string9.contains("Serial Number")) {
                string7 = lOilLanD.iOIl0LAnD(string9.split("Serial Number")[1].trim());
                continue;
            }
            if (string9.contains("Version")) {
                string8 = lOilLanD.iOIl0LAnD(string9.split("Version")[1].trim());
                continue;
            }
            if (!string9.contains("Physical Location")) continue;
            break;
        }
        return new IIOOOlIiLanD(string6, string7, string8);
    }

    public static O1IiIiI1LAND I1O1I1LaNd(String string) {
        String string2 = "Machine Type and Model";
        String string3 = "Serial Number";
        String string4 = null;
        String string5 = null;
        for (String string6 : Iill1lanD.I1O1I1LaNd("lscfg -vl " + string)) {
            if (string6.contains(string2)) {
                string4 = lOilLanD.iOIl0LAnD(string6.split(string2)[1].trim());
                continue;
            }
            if (!string6.contains(string3)) continue;
            string5 = lOilLanD.iOIl0LAnD(string6.split(string3)[1].trim());
        }
        return new O1IiIiI1LAND(string4, string5);
    }
}

