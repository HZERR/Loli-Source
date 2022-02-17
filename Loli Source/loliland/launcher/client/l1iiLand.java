/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.llllil1LaND;

final class l1iiLand
extends llllil1LaND {
    private static final String I1O1I1LaNd = "temp";
    private static final String OOOIilanD = "fan";
    private static final String lI00OlAND = "in";
    private static final String[] lli0OiIlAND = new String[]{"temp", "fan", "in"};
    private static final String li0iOILAND = "hwmon";
    private static final String O1il1llOLANd = "/sys/class/hwmon/hwmon";
    private static final String Oill1LAnD = "thermal_zone";
    private static final String lIOILand = "/sys/class/thermal/thermal_zone";
    private static final boolean lil0liLand = l1iiLand.lil0liLand() > 0.0;
    private final Map iilIi1laND = new HashMap();

    l1iiLand() {
        if (!lil0liLand) {
            this.Oill1LAnD();
            if (!this.iilIi1laND.containsKey(I1O1I1LaNd)) {
                this.lIOILand();
            }
        }
    }

    private void Oill1LAnD() {
        String[] arrstring = lli0OiIlAND;
        int n2 = arrstring.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string;
            String string2 = string = arrstring[i2];
            this.I1O1I1LaNd(O1il1llOLANd, string, file -> {
                try {
                    return file.getName().startsWith(string2) && file.getName().endsWith("_input") && liOIOOlLAnD.lli0OiIlAND(file.getCanonicalPath()) > 0;
                }
                catch (IOException iOException) {
                    return false;
                }
            });
        }
    }

    private void lIOILand() {
        this.I1O1I1LaNd(lIOILand, I1O1I1LaNd, file -> file.getName().equals(I1O1I1LaNd));
    }

    private void I1O1I1LaNd(String string, String string2, FileFilter fileFilter) {
        int n2 = 0;
        while (Paths.get(string + n2, new String[0]).toFile().isDirectory()) {
            String string3 = string + n2;
            File file = new File(string3);
            File[] arrfile = file.listFiles(fileFilter);
            if (arrfile != null && arrfile.length > 0) {
                this.iilIi1laND.put(string2, String.format("%s/%s", string3, string2));
            }
            ++n2;
        }
    }

    @Override
    public double lli0OiIlAND() {
        if (lil0liLand) {
            return l1iiLand.lil0liLand();
        }
        String string = (String)this.iilIi1laND.get(I1O1I1LaNd);
        if (string != null) {
            long l2 = 0L;
            if (string.contains(li0iOILAND)) {
                l2 = liOIOOlLAnD.OOOIilanD(String.format("%s1_input", string));
                if (l2 > 0L) {
                    return (double)l2 / 1000.0;
                }
                long l3 = 0L;
                int n2 = 0;
                for (int i2 = 2; i2 <= 6; ++i2) {
                    l2 = liOIOOlLAnD.OOOIilanD(String.format("%s%d_input", string, i2));
                    if (l2 <= 0L) continue;
                    l3 += l2;
                    ++n2;
                }
                if (n2 > 0) {
                    return (double)l3 / ((double)n2 * 1000.0);
                }
            } else if (string.contains(Oill1LAnD) && (l2 = liOIOOlLAnD.OOOIilanD(string)) > 0L) {
                return (double)l2 / 1000.0;
            }
        }
        return 0.0;
    }

    private static double lil0liLand() {
        String string = Iill1lanD.OOOIilanD("vcgencmd measure_temp");
        if (string.startsWith("temp=")) {
            return lOilLanD.OOOIilanD(string.replaceAll("[^\\d|\\.]+", ""), 0.0);
        }
        return 0.0;
    }

    @Override
    public int[] li0iOILAND() {
        String string;
        if (!lil0liLand && (string = (String)this.iilIi1laND.get(OOOIilanD)) != null) {
            Object object;
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            int n2 = 1;
            while (new File((String)(object = String.format("%s%d_input", string, n2))).exists()) {
                arrayList.add(liOIOOlLAnD.lli0OiIlAND((String)object));
                ++n2;
            }
            object = new int[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); ++i2) {
                object[i2] = (Integer)arrayList.get(i2);
            }
            return object;
        }
        return new int[0];
    }

    @Override
    public double O1il1llOLANd() {
        if (lil0liLand) {
            return l1iiLand.iilIi1laND();
        }
        String string = (String)this.iilIi1laND.get(lI00OlAND);
        if (string != null) {
            return (double)liOIOOlLAnD.lli0OiIlAND(String.format("%s1_input", string)) / 1000.0;
        }
        return 0.0;
    }

    private static double iilIi1laND() {
        String string = Iill1lanD.OOOIilanD("vcgencmd measure_volts core");
        if (string.startsWith("volt=")) {
            return lOilLanD.OOOIilanD(string.replaceAll("[^\\d|\\.]+", ""), 0.0);
        }
        return 0.0;
    }
}

