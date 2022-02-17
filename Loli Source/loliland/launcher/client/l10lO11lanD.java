/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Properties;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class l10lO11lanD {
    private static final String I1O1I1LaNd = "oshi.properties";
    private static final Properties OOOIilanD = liOIOOlLAnD.O1il1llOLANd("oshi.properties");

    private l10lO11lanD() {
    }

    public static String I1O1I1LaNd(String string, String string2) {
        return OOOIilanD.getProperty(string, string2);
    }

    public static int I1O1I1LaNd(String string, int n2) {
        String string2 = OOOIilanD.getProperty(string);
        return string2 == null ? n2 : lOilLanD.lli0OiIlAND(string2, n2);
    }

    public static double I1O1I1LaNd(String string, double d2) {
        String string2 = OOOIilanD.getProperty(string);
        return string2 == null ? d2 : lOilLanD.OOOIilanD(string2, d2);
    }

    public static boolean I1O1I1LaNd(String string, boolean bl) {
        String string2 = OOOIilanD.getProperty(string);
        return string2 == null ? bl : Boolean.parseBoolean(string2);
    }

    public static void I1O1I1LaNd(String string, Object object) {
        if (object == null) {
            OOOIilanD.remove(string);
        } else {
            OOOIilanD.setProperty(string, object.toString());
        }
    }

    public static void I1O1I1LaNd(String string) {
        OOOIilanD.remove(string);
    }

    public static void I1O1I1LaNd() {
        OOOIilanD.clear();
    }

    public static void I1O1I1LaNd(Properties properties) {
        OOOIilanD.putAll(properties);
    }
}

