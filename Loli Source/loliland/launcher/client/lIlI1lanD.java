/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.linux.Udev;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lO1OIlI1laNd;
import loliland.launcher.client.lOilLanD;

final class lIlI1lanD
extends lO1OIlI1laNd {
    private static final String I1O1I1LaNd = "block";
    private static final String OOOIilanD = "DM_UUID";
    private static final String lI00OlAND = "DM_VG_NAME";
    private static final String lli0OiIlAND = "DM_LV_NAME";
    private static final String li0iOILAND = "/dev/";

    lIlI1lanD(String string, Map map, Set set) {
        super(string, map, set);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static List lli0OiIlAND() {
        Object object;
        HashMap<String, Map> hashMap = new HashMap<String, Map>();
        HashMap<String, Set> hashMap2 = new HashMap<String, Set>();
        for (Object object2 : Iill1lanD.I1O1I1LaNd("pvs -o vg_name,pv_name")) {
            object = lOilLanD.OOOIilanD.split(((String)object2).trim());
            if (((String[])object).length != 2 || !object[1].startsWith(li0iOILAND)) continue;
            hashMap2.computeIfAbsent((String)object[0], string -> new HashSet()).add(object[1]);
        }
        Udev.UdevContext udevContext = Udev.INSTANCE.udev_new();
        try {
            Object object2;
            object2 = udevContext.enumerateNew();
            try {
                ((Udev.UdevEnumerate)object2).addMatchSubsystem(I1O1I1LaNd);
                ((Udev.UdevEnumerate)object2).scanDevices();
                for (object = ((Udev.UdevEnumerate)object2).getListEntry(); object != null; object = ((Udev.UdevListEntry)object).getNext()) {
                    String string2 = ((Udev.UdevListEntry)object).getName();
                    Udev.UdevDevice udevDevice = udevContext.deviceNewFromSyspath(string2);
                    if (udevDevice == null) continue;
                    try {
                        String string3;
                        String string4 = udevDevice.getDevnode();
                        if (string4 == null || !string4.startsWith("/dev/dm") || (string3 = udevDevice.getPropertyValue(OOOIilanD)) == null || !string3.startsWith("LVM-")) continue;
                        String string5 = udevDevice.getPropertyValue(lI00OlAND);
                        String string6 = udevDevice.getPropertyValue(lli0OiIlAND);
                        if (iiIIIlO1lANd.I1O1I1LaNd(string5) || iiIIIlO1lANd.I1O1I1LaNd(string6)) continue;
                        hashMap.computeIfAbsent(string5, string -> new HashMap());
                        Map map = (Map)hashMap.get(string5);
                        hashMap2.computeIfAbsent(string5, string -> new HashSet());
                        Set set = (Set)hashMap2.get(string5);
                        File file = new File(string2 + "/slaves");
                        File[] arrfile = file.listFiles();
                        if (arrfile == null) continue;
                        for (File file2 : arrfile) {
                            String string7 = file2.getName();
                            map.computeIfAbsent(string6, string -> new HashSet()).add(li0iOILAND + string7);
                            set.add(li0iOILAND + string7);
                        }
                        continue;
                    }
                    finally {
                        udevDevice.unref();
                    }
                }
            }
            finally {
                ((Udev.UdevEnumerate)object2).unref();
            }
        }
        finally {
            udevContext.unref();
        }
        return hashMap.entrySet().stream().map(entry -> new lIlI1lanD((String)entry.getKey(), (Map)entry.getValue(), (Set)hashMap2.get(entry.getKey()))).collect(Collectors.toList());
    }
}

