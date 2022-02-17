/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import loliland.launcher.client.I0Oiland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.llOIlIilAND;

public final class O1O1i10LaNd
extends I0Oiland {
    private static final String I1O1I1LaNd = "/sys/class/power_supply/";

    public O1O1i10LaNd(String string, String string2, double d2, double d3, double d4, double d5, double d6, double d7, boolean bl, boolean bl2, boolean bl3, llOIlIilAND llOIlIilAND2, int n2, int n3, int n4, int n5, String string3, LocalDate localDate, String string4, String string5, double d8) {
        super(string, string2, d2, d3, d4, d5, d6, d7, bl, bl2, bl3, llOIlIilAND2, n2, n3, n4, n5, string3, localDate, string4, string5, d8);
    }

    public static List iOl10IlLAnd() {
        double d2 = -1.0;
        double d3 = -1.0;
        double d4 = -1.0;
        double d5 = 0.0;
        double d6 = -1.0;
        double d7 = 0.0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        llOIlIilAND llOIlIilAND2 = llOIlIilAND.lI00OlAND;
        int n2 = -1;
        int n3 = -1;
        int n4 = -1;
        int n5 = -1;
        LocalDate localDate = null;
        double d8 = 0.0;
        File file = new File(I1O1I1LaNd);
        String[] arrstring = file.list();
        ArrayList<O1O1i10LaNd> arrayList = new ArrayList<O1O1i10LaNd>();
        if (arrstring != null) {
            for (String string : arrstring) {
                List list;
                if (string.startsWith("ADP") || string.startsWith("AC") || (list = liOIOOlLAnD.I1O1I1LaNd(I1O1I1LaNd + string + "/uevent", false)).isEmpty()) continue;
                HashMap<String, String> hashMap = new HashMap<String, String>();
                for (String string2 : list) {
                    String[] arrstring2 = string2.split("=");
                    if (arrstring2.length <= 1 || arrstring2[1].isEmpty()) continue;
                    hashMap.put(arrstring2[0], arrstring2[1]);
                }
                String string3 = hashMap.getOrDefault("POWER_SUPPLY_NAME", string);
                String string4 = (String)hashMap.get("POWER_SUPPLY_STATUS");
                bl2 = "Charging".equals(string4);
                bl3 = "Discharging".equals(string4);
                if (hashMap.containsKey("POWER_SUPPLY_CAPACITY")) {
                    d2 = (double)lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_CAPACITY"), -100) / 100.0;
                }
                if (hashMap.containsKey("POWER_SUPPLY_ENERGY_NOW")) {
                    n2 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_ENERGY_NOW"), -1);
                } else if (hashMap.containsKey("POWER_SUPPLY_CHARGE_NOW")) {
                    n2 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_CHARGE_NOW"), -1);
                }
                if (hashMap.containsKey("POWER_SUPPLY_ENERGY_FULL")) {
                    n2 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_ENERGY_FULL"), 1);
                } else if (hashMap.containsKey("POWER_SUPPLY_CHARGE_FULL")) {
                    n2 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_CHARGE_FULL"), 1);
                }
                if (hashMap.containsKey("POWER_SUPPLY_ENERGY_FULL_DESIGN")) {
                    n3 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_ENERGY_FULL_DESIGN"), 1);
                } else if (hashMap.containsKey("POWER_SUPPLY_CHARGE_FULL_DESIGN")) {
                    n3 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_CHARGE_FULL_DESIGN"), 1);
                }
                if (hashMap.containsKey("POWER_SUPPLY_VOLTAGE_NOW")) {
                    d6 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_VOLTAGE_NOW"), -1);
                }
                if (hashMap.containsKey("POWER_SUPPLY_POWER_NOW")) {
                    d5 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_POWER_NOW"), -1);
                }
                if (d6 > 0.0) {
                    d7 = d5 / d6;
                }
                if (hashMap.containsKey("POWER_SUPPLY_CYCLE_COUNT")) {
                    n5 = lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_CYCLE_COUNT"), -1);
                }
                String string5 = hashMap.getOrDefault("POWER_SUPPLY_TECHNOLOGY", "unknown");
                String string6 = hashMap.getOrDefault("POWER_SUPPLY_MODEL_NAME", "unknown");
                String string7 = hashMap.getOrDefault("POWER_SUPPLY_MANUFACTURER", "unknown");
                String string8 = hashMap.getOrDefault("POWER_SUPPLY_SERIAL_NUMBER", "unknown");
                if (lOilLanD.lli0OiIlAND((String)hashMap.get("POWER_SUPPLY_PRESENT"), 1) <= 0) continue;
                arrayList.add(new O1O1i10LaNd(string3, string6, d2, d3, d4, d5, d6, d7, bl, bl2, bl3, llOIlIilAND2, n2, n3, n4, n5, string5, localDate, string7, string8, d8));
            }
        }
        return arrayList;
    }
}

