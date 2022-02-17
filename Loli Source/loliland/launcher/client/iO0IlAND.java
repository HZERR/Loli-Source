/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.IlIIOLaNd;
import loliland.launcher.client.l00i1O0LAnD;
import loliland.launcher.client.lI10lAnd;
import loliland.launcher.client.lOilLanD;

public class iO0IlAND
extends lI10lAnd {
    public static final String li0iOILAND = "oshi.os.aix.filesystem.path.excludes";
    public static final String O1il1llOLANd = "oshi.os.aix.filesystem.path.includes";
    public static final String Oill1LAnD = "oshi.os.aix.filesystem.volume.excludes";
    public static final String lIOILand = "oshi.os.aix.filesystem.volume.includes";
    private static final List lil0liLand = l00i1O0LAnD.I1O1I1LaNd("oshi.os.aix.filesystem.path.excludes");
    private static final List iilIi1laND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.aix.filesystem.path.includes");
    private static final List lli011lLANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.aix.filesystem.volume.excludes");
    private static final List l0illAND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.aix.filesystem.volume.includes");

    @Override
    public List I1O1I1LaNd(boolean bl) {
        return iO0IlAND.I1O1I1LaNd(null, bl);
    }

    static List I1O1I1LaNd(String string) {
        return iO0IlAND.I1O1I1LaNd(string, false);
    }

    private static List I1O1I1LaNd(String string, boolean bl) {
        String[] arrstring;
        ArrayList<IlIIOLaNd> arrayList = new ArrayList<IlIIOLaNd>();
        HashMap<String, Long> hashMap = new HashMap<String, Long>();
        HashMap<String, Long> hashMap2 = new HashMap<String, Long>();
        String string2 = "df -i" + (bl ? " -l" : "");
        for (String string3 : Iill1lanD.I1O1I1LaNd(string2)) {
            if (!string3.startsWith("/") || (arrstring = lOilLanD.OOOIilanD.split(string3)).length <= 5) continue;
            hashMap2.put(arrstring[0], lOilLanD.OOOIilanD(arrstring[1], 0L));
            hashMap.put(arrstring[0], lOilLanD.OOOIilanD(arrstring[3], 0L));
        }
        for (String string3 : Iill1lanD.I1O1I1LaNd("mount")) {
            arrstring = lOilLanD.OOOIilanD.split("x" + string3);
            if (arrstring.length <= 7) continue;
            String string4 = arrstring[1];
            String string5 = arrstring[2];
            String string6 = arrstring[3];
            String string7 = arrstring[4];
            if (bl && lI00OlAND.contains(string6) || !string5.equals("/") && (lli0OiIlAND.contains(string6) || l00i1O0LAnD.I1O1I1LaNd(string5, string4, iilIi1laND, lil0liLand, l0illAND, lli011lLANd))) continue;
            String string8 = string5.substring(string5.lastIndexOf(47) + 1);
            if (string8.isEmpty()) {
                string8 = string4.substring(string4.lastIndexOf(47) + 1);
            }
            if (string != null && !string.equals(string8)) continue;
            File file = new File(string5);
            long l2 = file.getTotalSpace();
            long l3 = file.getUsableSpace();
            long l4 = file.getFreeSpace();
            String string9 = string4.startsWith("/dev") || string5.equals("/") ? "Local Disk" : (string4.equals("tmpfs") ? "Ram Disk" : (lI00OlAND.contains(string6) ? "Network Disk" : "Mount Point"));
            arrayList.add(new IlIIOLaNd(string8, string4, string8, string5, string7, "", "", string9, string6, l4, l3, l2, hashMap.getOrDefault(string4, 0L), hashMap2.getOrDefault(string4, 0L)));
        }
        return arrayList;
    }

    @Override
    public long OOOIilanD() {
        boolean bl = false;
        long l2 = 0L;
        for (String string : Iill1lanD.I1O1I1LaNd("lsof -nl")) {
            if (!bl) {
                bl = string.startsWith("COMMAND");
                continue;
            }
            ++l2;
        }
        return l2;
    }

    @Override
    public long lI00OlAND() {
        return lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("ulimit -n"), 0L);
    }
}

