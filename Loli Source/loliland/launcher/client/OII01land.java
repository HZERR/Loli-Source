/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.ii01I1iLand;
import loliland.launcher.client.l00i1O0LAnD;
import loliland.launcher.client.lI10lAnd;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lO0li10llaND;
import loliland.launcher.client.lOilLanD;

public class OII01land
extends lI10lAnd {
    public static final String li0iOILAND = "oshi.os.solaris.filesystem.path.excludes";
    public static final String O1il1llOLANd = "oshi.os.solaris.filesystem.path.includes";
    public static final String Oill1LAnD = "oshi.os.solaris.filesystem.volume.excludes";
    public static final String lIOILand = "oshi.os.solaris.filesystem.volume.includes";
    private static final List lil0liLand = l00i1O0LAnD.I1O1I1LaNd("oshi.os.solaris.filesystem.path.excludes");
    private static final List iilIi1laND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.solaris.filesystem.path.includes");
    private static final List lli011lLANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.solaris.filesystem.volume.excludes");
    private static final List l0illAND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.solaris.filesystem.volume.includes");

    @Override
    public List I1O1I1LaNd(boolean bl) {
        return OII01land.I1O1I1LaNd(null, bl);
    }

    static List I1O1I1LaNd(String string) {
        return OII01land.I1O1I1LaNd(string, false);
    }

    private static List I1O1I1LaNd(String string, boolean bl) {
        ArrayList<ii01I1iLand> arrayList = new ArrayList<ii01I1iLand>();
        HashMap<String, Long> hashMap = new HashMap<String, Long>();
        HashMap<String, Long> hashMap2 = new HashMap<String, Long>();
        String string2 = null;
        String string3 = null;
        String string4 = null;
        String string5 = "df -g" + (bl ? " -l" : "");
        for (String string6 : Iill1lanD.I1O1I1LaNd(string5)) {
            if (string6.startsWith("/")) {
                string2 = lOilLanD.OOOIilanD.split(string6)[0];
                string3 = null;
                continue;
            }
            if (string6.contains("available") && string6.contains("total files")) {
                string3 = lOilLanD.I1O1I1LaNd(string6, "available", "total files").trim();
                continue;
            }
            if (!string6.contains("free files")) continue;
            string4 = lOilLanD.I1O1I1LaNd(string6, "", "free files").trim();
            if (string2 == null || string3 == null) continue;
            hashMap.put(string2, lOilLanD.OOOIilanD(string4, 0L));
            hashMap2.put(string2, lOilLanD.OOOIilanD(string3, 0L));
            string2 = null;
        }
        for (String string6 : Iill1lanD.I1O1I1LaNd("cat /etc/mnttab")) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string6);
            if (arrstring.length < 5) continue;
            String string7 = arrstring[0];
            String string8 = arrstring[1];
            String string9 = arrstring[2];
            String string10 = arrstring[3];
            if (bl && lI00OlAND.contains(string9) || !string8.equals("/") && (lli0OiIlAND.contains(string9) || l00i1O0LAnD.I1O1I1LaNd(string8, string7, iilIi1laND, lil0liLand, l0illAND, lli011lLANd))) continue;
            String string11 = string8.substring(string8.lastIndexOf(47) + 1);
            if (string11.isEmpty()) {
                string11 = string7.substring(string7.lastIndexOf(47) + 1);
            }
            if (string != null && !string.equals(string11)) continue;
            File file = new File(string8);
            long l2 = file.getTotalSpace();
            long l3 = file.getUsableSpace();
            long l4 = file.getFreeSpace();
            String string12 = string7.startsWith("/dev") || string8.equals("/") ? "Local Disk" : (string7.equals("tmpfs") ? "Ram Disk" : (lI00OlAND.contains(string9) ? "Network Disk" : "Mount Point"));
            arrayList.add(new ii01I1iLand(string11, string7, string11, string8, string10, "", "", string12, string9, l4, l3, l2, hashMap.containsKey(string8) ? (Long)hashMap.get(string8) : 0L, hashMap2.containsKey(string8) ? (Long)hashMap2.get(string8) : 0L));
        }
        return arrayList;
    }

    @Override
    public long OOOIilanD() {
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd(null, -1, "file_cache");
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                long l2 = lO0li10llaND.OOOIilanD(kstat, "buf_inuse");
                return l2;
            }
        }
        return 0L;
    }

    @Override
    public long lI00OlAND() {
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd(null, -1, "file_cache");
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                long l2 = lO0li10llaND.OOOIilanD(kstat, "buf_max");
                return l2;
            }
        }
        return 0L;
    }
}

