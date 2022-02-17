/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class l0illAND {
    private l0illAND() {
    }

    public static String I1O1I1LaNd() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/sys_vendor").trim();
        if (!string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String OOOIilanD() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/product_name").trim();
        String string2 = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/product_version").trim();
        if (string.isEmpty()) {
            if (!string2.isEmpty()) {
                return string2;
            }
        } else {
            if (!string2.isEmpty() && !"None".equals(string2)) {
                return string + " (version: " + string2 + ")";
            }
            return string;
        }
        return null;
    }

    public static String lI00OlAND() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/product_serial");
        if (!string.isEmpty() && !"None".equals(string)) {
            return string;
        }
        return l0illAND.lIOILand();
    }

    public static String lli0OiIlAND() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/product_uuid");
        if (!string.isEmpty() && !"None".equals(string)) {
            return string;
        }
        return null;
    }

    public static String li0iOILAND() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/board_vendor").trim();
        if (!string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String O1il1llOLANd() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/board_name").trim();
        if (!string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String Oill1LAnD() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/board_version").trim();
        if (!string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String lIOILand() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/board_serial").trim();
        if (!string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String lil0liLand() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/bios_vendor").trim();
        if (string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String iilIi1laND() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/modalias").trim();
        if (!string.isEmpty()) {
            return string;
        }
        return null;
    }

    public static String I1O1I1LaNd(String string) {
        String string2 = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/bios_version").trim();
        if (!string2.isEmpty()) {
            return string2 + (iiIIIlO1lANd.I1O1I1LaNd(string) ? "" : " (revision " + string + ")");
        }
        return null;
    }

    public static String lli011lLANd() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/devices/virtual/dmi/id/bios_date").trim();
        if (!string.isEmpty()) {
            return lOilLanD.lIOILand(string);
        }
        return null;
    }
}

