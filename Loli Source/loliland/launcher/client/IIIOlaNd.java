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
import loliland.launcher.client.lOiO0l0lAnd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lli10iliLaND;

public final class IIIOlaNd
extends lI10lAnd {
    public static final String li0iOILAND = "oshi.os.freebsd.filesystem.path.excludes";
    public static final String O1il1llOLANd = "oshi.os.freebsd.filesystem.path.includes";
    public static final String Oill1LAnD = "oshi.os.freebsd.filesystem.volume.excludes";
    public static final String lIOILand = "oshi.os.freebsd.filesystem.volume.includes";
    private static final List lil0liLand = l00i1O0LAnD.I1O1I1LaNd("oshi.os.freebsd.filesystem.path.excludes");
    private static final List iilIi1laND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.freebsd.filesystem.path.includes");
    private static final List lli011lLANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.freebsd.filesystem.volume.excludes");
    private static final List l0illAND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.freebsd.filesystem.volume.includes");

    @Override
    public List I1O1I1LaNd(boolean bl) {
        String[] arrstring;
        Object object2;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String string = "";
        for (Object object2 : Iill1lanD.I1O1I1LaNd("geom part list")) {
            if (((String)object2).contains("Name: ")) {
                string = ((String)object2).substring(((String)object2).lastIndexOf(32) + 1);
            }
            if (string.isEmpty() || !((String)(object2 = ((String)object2).trim())).startsWith("rawuuid:")) continue;
            hashMap.put(string, ((String)object2).substring(((String)object2).lastIndexOf(32) + 1));
            string = "";
        }
        ArrayList arrayList = new ArrayList();
        object2 = new HashMap();
        HashMap<String, Long> hashMap2 = new HashMap<String, Long>();
        for (String string2 : Iill1lanD.I1O1I1LaNd("df -i")) {
            if (!string2.startsWith("/") || (arrstring = lOilLanD.OOOIilanD.split(string2)).length <= 7) continue;
            object2.put(arrstring[0], lOilLanD.OOOIilanD(arrstring[6], 0L));
            hashMap2.put(arrstring[0], (Long)object2.get(arrstring[0]) + lOilLanD.OOOIilanD(arrstring[5], 0L));
        }
        for (String string2 : Iill1lanD.I1O1I1LaNd("mount -p")) {
            arrstring = lOilLanD.OOOIilanD.split(string2);
            if (arrstring.length < 5) continue;
            String string3 = arrstring[0];
            String string4 = arrstring[1];
            String string5 = arrstring[2];
            String string6 = arrstring[3];
            if (bl && lI00OlAND.contains(string5) || !string4.equals("/") && (lli0OiIlAND.contains(string5) || l00i1O0LAnD.I1O1I1LaNd(string4, string3, iilIi1laND, lil0liLand, l0illAND, lli011lLANd))) continue;
            String string7 = string4.substring(string4.lastIndexOf(47) + 1);
            if (string7.isEmpty()) {
                string7 = string3.substring(string3.lastIndexOf(47) + 1);
            }
            File file = new File(string4);
            long l2 = file.getTotalSpace();
            long l3 = file.getUsableSpace();
            long l4 = file.getFreeSpace();
            String string8 = string3.startsWith("/dev") || string4.equals("/") ? "Local Disk" : (string3.equals("tmpfs") ? "Ram Disk" : (lI00OlAND.contains(string5) ? "Network Disk" : "Mount Point"));
            String string9 = hashMap.getOrDefault(string7, "");
            arrayList.add(new lOiO0l0lAnd(string7, string3, string7, string4, string6, string9, "", string8, string5, l4, l3, l2, object2.containsKey(string4) ? (Long)object2.get(string4) : 0L, hashMap2.containsKey(string4) ? (Long)hashMap2.get(string4) : 0L));
        }
        return arrayList;
    }

    @Override
    public long OOOIilanD() {
        return lli10iliLaND.I1O1I1LaNd("kern.openfiles", 0);
    }

    @Override
    public long lI00OlAND() {
        return lli10iliLaND.I1O1I1LaNd("kern.maxfiles", 0);
    }
}

