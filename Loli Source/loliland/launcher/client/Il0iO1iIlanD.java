/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.linux.Udev;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.II0l0LaND;
import loliland.launcher.client.il000OLANd;

public class Il0iO1iIlanD
extends II0l0LaND {
    public Il0iO1iIlanD(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl) {
        List list = Il0iO1iIlanD.lIOILand();
        if (bl) {
            return list;
        }
        ArrayList<Il0iO1iIlanD> arrayList = new ArrayList<Il0iO1iIlanD>();
        for (il000OLANd il000OLANd2 : list) {
            arrayList.add(new Il0iO1iIlanD(il000OLANd2.I1O1I1LaNd(), il000OLANd2.OOOIilanD(), il000OLANd2.lI00OlAND(), il000OLANd2.lli0OiIlAND(), il000OLANd2.li0iOILAND(), il000OLANd2.O1il1llOLANd(), Collections.emptyList()));
            Il0iO1iIlanD.I1O1I1LaNd(arrayList, il000OLANd2.Oill1LAnD());
        }
        return arrayList;
    }

    private static List lIOILand() {
        Udev.UdevContext udevContext = Udev.INSTANCE.udev_new();
        Udev.UdevEnumerate udevEnumerate = Udev.INSTANCE.udev_enumerate_new(udevContext);
        Udev.INSTANCE.udev_enumerate_add_match_subsystem(udevEnumerate, "usb");
        Udev.INSTANCE.udev_enumerate_scan_devices(udevEnumerate);
        Udev.UdevListEntry udevListEntry = Udev.INSTANCE.udev_enumerate_get_list_entry(udevEnumerate);
        ArrayList<Object> arrayList = new ArrayList<Object>();
        HashMap<Object, String> hashMap = new HashMap<Object, String>();
        HashMap<Object, String> hashMap2 = new HashMap<Object, String>();
        HashMap<Object, String> hashMap3 = new HashMap<Object, String>();
        HashMap<Object, String> hashMap4 = new HashMap<Object, String>();
        HashMap<Object, String> hashMap5 = new HashMap<Object, String>();
        HashMap<String, List> hashMap6 = new HashMap<String, List>();
        Object object = udevListEntry;
        while (object != null) {
            String string2 = Udev.INSTANCE.udev_list_entry_get_name((Udev.UdevListEntry)object);
            Object object2 = Udev.INSTANCE.udev_device_new_from_syspath(udevContext, string2);
            if ("usb_device".equals(Udev.INSTANCE.udev_device_get_devtype((Udev.UdevDevice)object2))) {
                Udev.UdevDevice udevDevice;
                String string3 = Udev.INSTANCE.udev_device_get_sysattr_value((Udev.UdevDevice)object2, "product");
                if (string3 != null) {
                    hashMap.put(string2, string3);
                }
                if ((string3 = Udev.INSTANCE.udev_device_get_sysattr_value((Udev.UdevDevice)object2, "manufacturer")) != null) {
                    hashMap2.put(string2, string3);
                }
                if ((string3 = Udev.INSTANCE.udev_device_get_sysattr_value((Udev.UdevDevice)object2, "idVendor")) != null) {
                    hashMap3.put(string2, string3);
                }
                if ((string3 = Udev.INSTANCE.udev_device_get_sysattr_value((Udev.UdevDevice)object2, "idProduct")) != null) {
                    hashMap4.put(string2, string3);
                }
                if ((string3 = Udev.INSTANCE.udev_device_get_sysattr_value((Udev.UdevDevice)object2, "serial")) != null) {
                    hashMap5.put(string2, string3);
                }
                if ((udevDevice = Udev.INSTANCE.udev_device_get_parent_with_subsystem_devtype((Udev.UdevDevice)object2, "usb", "usb_device")) == null) {
                    arrayList.add(string2);
                } else {
                    String string4 = Udev.INSTANCE.udev_device_get_syspath(udevDevice);
                    hashMap6.computeIfAbsent(string4, string -> new ArrayList()).add(string2);
                }
                Udev.INSTANCE.udev_device_unref((Udev.UdevDevice)object2);
            }
            object = Udev.INSTANCE.udev_list_entry_get_next((Udev.UdevListEntry)object);
        }
        Udev.INSTANCE.udev_enumerate_unref(udevEnumerate);
        Udev.INSTANCE.udev_unref(udevContext);
        object = new ArrayList();
        for (Object object2 : arrayList) {
            object.add(Il0iO1iIlanD.I1O1I1LaNd((String)object2, "0000", "0000", hashMap, hashMap2, hashMap3, hashMap4, hashMap5, hashMap6));
        }
        return object;
    }

    private static void I1O1I1LaNd(List list, List list2) {
        for (il000OLANd il000OLANd2 : list2) {
            list.add(il000OLANd2);
            Il0iO1iIlanD.I1O1I1LaNd(list, il000OLANd2.Oill1LAnD());
        }
    }

    private static Il0iO1iIlanD I1O1I1LaNd(String string, String string2, String string3, Map map, Map map2, Map map3, Map map4, Map map5, Map map6) {
        String string4 = map3.getOrDefault(string, string2);
        String string5 = map4.getOrDefault(string, string3);
        List list = map6.getOrDefault(string, new ArrayList());
        ArrayList<Il0iO1iIlanD> arrayList = new ArrayList<Il0iO1iIlanD>();
        for (String string6 : list) {
            arrayList.add(Il0iO1iIlanD.I1O1I1LaNd(string6, string4, string5, map, map2, map3, map4, map5, map6));
        }
        Collections.sort(arrayList);
        return new Il0iO1iIlanD(map.getOrDefault(string, string4 + ":" + string5), map2.getOrDefault(string, ""), string4, string5, map5.getOrDefault(string, ""), string, arrayList);
    }
}

