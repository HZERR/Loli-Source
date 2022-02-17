/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.O11i1I1lAnd;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class l11lLANd {
    private l11lLANd() {
    }

    public static String I1O1I1LaNd() {
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lI00OlAND);
        for (String string : list) {
            if (!string.startsWith("CPU implementer")) continue;
            int n2 = lOilLanD.I1O1I1LaNd(string, 0);
            switch (n2) {
                case 65: {
                    return "ARM";
                }
                case 66: {
                    return "Broadcom";
                }
                case 67: {
                    return "Cavium";
                }
                case 68: {
                    return "DEC";
                }
                case 78: {
                    return "Nvidia";
                }
                case 80: {
                    return "APM";
                }
                case 81: {
                    return "Qualcomm";
                }
                case 83: {
                    return "Samsung";
                }
                case 86: {
                    return "Marvell";
                }
                case 102: {
                    return "Faraday";
                }
                case 105: {
                    return "Intel";
                }
            }
            return null;
        }
        return null;
    }

    public static O11i1I1lAnd OOOIilanD() {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lI00OlAND);
        for (String string5 : list) {
            String[] arrstring = lOilLanD.I1O1I1LaNd.split(string5);
            if (arrstring.length < 2) continue;
            switch (arrstring[0]) {
                case "Hardware": {
                    string2 = arrstring[1];
                    break;
                }
                case "Revision": {
                    string3 = arrstring[1];
                    if (string3.length() <= 1) break;
                    string = l11lLANd.I1O1I1LaNd(string3.charAt(1));
                    break;
                }
                case "Serial": {
                    string4 = arrstring[1];
                    break;
                }
            }
        }
        return new O11i1I1lAnd(string, string2, string3, string4);
    }

    private static String I1O1I1LaNd(char c2) {
        switch (c2) {
            case '0': {
                return "Sony UK";
            }
            case '1': {
                return "Egoman";
            }
            case '2': {
                return "Embest";
            }
            case '3': {
                return "Sony Japan";
            }
            case '4': {
                return "Embest";
            }
            case '5': {
                return "Stadium";
            }
        }
        return "unknown";
    }
}

