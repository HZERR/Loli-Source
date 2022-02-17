/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.l00i1O0LAnD;
import loliland.launcher.client.lI10lAnd;
import loliland.launcher.client.lIl0iilland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lOlIIILANd;

public class lOliOILaND
extends lI10lAnd {
    public static final String li0iOILAND = "oshi.os.openbsd.filesystem.path.excludes";
    public static final String O1il1llOLANd = "oshi.os.openbsd.filesystem.path.includes";
    public static final String Oill1LAnD = "oshi.os.openbsd.filesystem.volume.excludes";
    public static final String lIOILand = "oshi.os.openbsd.filesystem.volume.includes";
    private static final List lil0liLand = l00i1O0LAnD.I1O1I1LaNd("oshi.os.openbsd.filesystem.path.excludes");
    private static final List iilIi1laND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.openbsd.filesystem.path.includes");
    private static final List lli011lLANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.openbsd.filesystem.volume.excludes");
    private static final List l0illAND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.openbsd.filesystem.volume.includes");

    @Override
    public List I1O1I1LaNd(boolean bl) {
        return lOliOILaND.I1O1I1LaNd(null, bl);
    }

    static List I1O1I1LaNd(String string) {
        return lOliOILaND.I1O1I1LaNd(string, false);
    }

    private static List I1O1I1LaNd(String string, boolean bl) {
        String[] arrstring;
        ArrayList<lOlIIILANd> arrayList = new ArrayList<lOlIIILANd>();
        HashMap<String, Long> hashMap = new HashMap<String, Long>();
        HashMap<String, Long> hashMap2 = new HashMap<String, Long>();
        String string2 = "df -i" + (bl ? " -l" : "");
        for (String string3 : Iill1lanD.I1O1I1LaNd(string2)) {
            if (!string3.startsWith("/") || (arrstring = lOilLanD.OOOIilanD.split(string3)).length <= 6) continue;
            hashMap2.put(arrstring[0], lOilLanD.OOOIilanD(arrstring[5], 0L));
            hashMap.put(arrstring[0], lOilLanD.OOOIilanD(arrstring[6], 0L));
        }
        for (String string3 : Iill1lanD.I1O1I1LaNd("mount -v")) {
            arrstring = lOilLanD.OOOIilanD.split(string3, 7);
            if (arrstring.length != 7) continue;
            String string4 = arrstring[0];
            String string5 = arrstring[1];
            String string6 = arrstring[3];
            String string7 = arrstring[5];
            String string8 = arrstring[6];
            if (bl && lI00OlAND.contains(string7) || !string6.equals("/") && (lli0OiIlAND.contains(string7) || l00i1O0LAnD.I1O1I1LaNd(string6, string4, iilIi1laND, lil0liLand, l0illAND, lli011lLANd))) continue;
            String string9 = string6.substring(string6.lastIndexOf(47) + 1);
            if (string9.isEmpty()) {
                string9 = string4.substring(string4.lastIndexOf(47) + 1);
            }
            if (string != null && !string.equals(string9)) continue;
            File file = new File(string6);
            long l2 = file.getTotalSpace();
            long l3 = file.getUsableSpace();
            long l4 = file.getFreeSpace();
            String string10 = string4.startsWith("/dev") || string6.equals("/") ? "Local Disk" : (string4.equals("tmpfs") ? "Ram Disk (dynamic)" : (string4.equals("mfs") ? "Ram Disk (fixed)" : (lI00OlAND.contains(string7) ? "Network Disk" : "Mount Point")));
            arrayList.add(new lOlIIILANd(string9, string4, string9, string6, string8, string5, "", string10, string7, l4, l3, l2, hashMap.getOrDefault(string4, 0L), hashMap2.getOrDefault(string4, 0L) + hashMap.getOrDefault(string4, 0L)));
        }
        return arrayList;
    }

    @Override
    public long OOOIilanD() {
        return lIl0iilland.I1O1I1LaNd("kern.nfiles", 0);
    }

    @Override
    public long lI00OlAND() {
        return lIl0iilland.I1O1I1LaNd("kern.maxfiles", 0);
    }
}

