/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lOilLanD;

public final class lil0liLand {
    private lil0liLand() {
    }

    public static String I1O1I1LaNd() {
        String string = "Serial Number:";
        for (String string2 : Iill1lanD.I1O1I1LaNd("dmidecode -t system")) {
            if (!string2.contains(string)) continue;
            return string2.split(string)[1].trim();
        }
        return null;
    }

    public static String OOOIilanD() {
        String string = "UUID:";
        for (String string2 : Iill1lanD.I1O1I1LaNd("dmidecode -t system")) {
            if (!string2.contains(string)) continue;
            return string2.split(string)[1].trim();
        }
        return null;
    }

    public static O1IiIiI1LAND lI00OlAND() {
        String string = null;
        String string2 = null;
        String string3 = "SMBIOS";
        String string4 = "Bios Revision:";
        for (String string5 : Iill1lanD.I1O1I1LaNd("dmidecode -t bios")) {
            String[] arrstring;
            if (string5.contains("SMBIOS") && (arrstring = lOilLanD.OOOIilanD.split(string5)).length >= 2) {
                string = arrstring[0] + " " + arrstring[1];
            }
            if (!string5.contains("Bios Revision:")) continue;
            string2 = string5.split("Bios Revision:")[1].trim();
            break;
        }
        return new O1IiIiI1LAND(string, string2);
    }
}

