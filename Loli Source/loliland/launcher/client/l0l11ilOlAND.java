/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.II0l0LaND;
import loliland.launcher.client.il000OLANd;

public class l0l11ilOlAND
extends II0l0LaND {
    private static final CoreFoundation I1O1I1LaNd = CoreFoundation.INSTANCE;
    private static final String OOOIilanD = "IOUSB";
    private static final String lI00OlAND = "IOService";

    public l0l11ilOlAND(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl) {
        List list = l0l11ilOlAND.lIOILand();
        if (bl) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (il000OLANd il000OLANd2 : list) {
            l0l11ilOlAND.I1O1I1LaNd(arrayList, il000OLANd2.Oill1LAnD());
        }
        return arrayList;
    }

    /*
     * WARNING - void declaration
     */
    private static List lIOILand() {
        Object object;
        HashMap<Long, String> hashMap = new HashMap<Long, String>();
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        HashMap hashMap4 = new HashMap();
        HashMap hashMap5 = new HashMap();
        HashMap hashMap6 = new HashMap();
        ArrayList<Long> arrayList = new ArrayList<Long>();
        IOKit.IORegistryEntry iORegistryEntry = IOKitUtil.getRoot();
        IOKit.IOIterator iOIterator = iORegistryEntry.getChildIterator(OOOIilanD);
        if (iOIterator != null) {
            void var11_12;
            object = CoreFoundation.CFStringRef.createCFString("locationID");
            CoreFoundation.CFStringRef cFStringRef = CoreFoundation.CFStringRef.createCFString("IOPropertyMatch");
            IOKit.IORegistryEntry object2 = iOIterator.next();
            while (var11_12 != null) {
                long l2 = 0L;
                IOKit.IORegistryEntry iORegistryEntry2 = var11_12.getParentEntry(lI00OlAND);
                if (iORegistryEntry2 != null) {
                    l2 = iORegistryEntry2.getRegistryEntryID();
                    hashMap.put(l2, iORegistryEntry2.getName());
                    CoreFoundation.CFTypeRef cFTypeRef = iORegistryEntry2.createCFProperty((CoreFoundation.CFStringRef)object);
                    if (cFTypeRef != null) {
                        l0l11ilOlAND.I1O1I1LaNd(l2, cFTypeRef, (CoreFoundation.CFStringRef)object, cFStringRef, hashMap3, hashMap4);
                        cFTypeRef.release();
                    }
                    iORegistryEntry2.release();
                }
                arrayList.add(l2);
                l0l11ilOlAND.I1O1I1LaNd((IOKit.IORegistryEntry)var11_12, l2, hashMap, hashMap2, hashMap3, hashMap4, hashMap5, hashMap6);
                var11_12.release();
                IOKit.IORegistryEntry iORegistryEntry3 = iOIterator.next();
            }
            ((CoreFoundation.CFTypeRef)object).release();
            cFStringRef.release();
            iOIterator.release();
        }
        iORegistryEntry.release();
        object = new ArrayList();
        for (Long l3 : arrayList) {
            object.add(l0l11ilOlAND.I1O1I1LaNd(l3, "0000", "0000", hashMap, hashMap2, hashMap3, hashMap4, hashMap5, hashMap6));
        }
        return object;
    }

    private static void I1O1I1LaNd(IOKit.IORegistryEntry iORegistryEntry, long l3, Map map, Map map2, Map map3, Map map4, Map map5, Map map6) {
        String string;
        Long l4;
        Long l5;
        long l6 = iORegistryEntry.getRegistryEntryID();
        map6.computeIfAbsent(l3, l2 -> new ArrayList()).add(l6);
        map.put(l6, iORegistryEntry.getName().trim());
        String string2 = iORegistryEntry.getStringProperty("USB Vendor Name");
        if (string2 != null) {
            map2.put(l6, string2.trim());
        }
        if ((l5 = iORegistryEntry.getLongProperty("idVendor")) != null) {
            map3.put(l6, String.format("%04x", 0xFFFFL & l5));
        }
        if ((l4 = iORegistryEntry.getLongProperty("idProduct")) != null) {
            map4.put(l6, String.format("%04x", 0xFFFFL & l4));
        }
        if ((string = iORegistryEntry.getStringProperty("USB Serial Number")) != null) {
            map5.put(l6, string.trim());
        }
        IOKit.IOIterator iOIterator = iORegistryEntry.getChildIterator(OOOIilanD);
        IOKit.IORegistryEntry iORegistryEntry2 = iOIterator.next();
        while (iORegistryEntry2 != null) {
            l0l11ilOlAND.I1O1I1LaNd(iORegistryEntry2, l6, map, map2, map3, map4, map5, map6);
            iORegistryEntry2.release();
            iORegistryEntry2 = iOIterator.next();
        }
        iOIterator.release();
    }

    private static void I1O1I1LaNd(List list, List list2) {
        for (il000OLANd il000OLANd2 : list2) {
            list.add(new l0l11ilOlAND(il000OLANd2.I1O1I1LaNd(), il000OLANd2.OOOIilanD(), il000OLANd2.lI00OlAND(), il000OLANd2.lli0OiIlAND(), il000OLANd2.li0iOILAND(), il000OLANd2.O1il1llOLANd(), Collections.emptyList()));
            l0l11ilOlAND.I1O1I1LaNd(list, il000OLANd2.Oill1LAnD());
        }
    }

    private static void I1O1I1LaNd(long l2, CoreFoundation.CFTypeRef cFTypeRef, CoreFoundation.CFStringRef cFStringRef, CoreFoundation.CFStringRef cFStringRef2, Map map, Map map2) {
        CoreFoundation.CFMutableDictionaryRef cFMutableDictionaryRef = I1O1I1LaNd.CFDictionaryCreateMutable(I1O1I1LaNd.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
        cFMutableDictionaryRef.setValue(cFStringRef, cFTypeRef);
        CoreFoundation.CFMutableDictionaryRef cFMutableDictionaryRef2 = I1O1I1LaNd.CFDictionaryCreateMutable(I1O1I1LaNd.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
        cFMutableDictionaryRef2.setValue(cFStringRef2, cFMutableDictionaryRef);
        IOKit.IOIterator iOIterator = IOKitUtil.getMatchingServices(cFMutableDictionaryRef2);
        cFMutableDictionaryRef.release();
        boolean bl = false;
        if (iOIterator != null) {
            IOKit.IORegistryEntry iORegistryEntry = iOIterator.next();
            while (iORegistryEntry != null && !bl) {
                IOKit.IORegistryEntry iORegistryEntry2 = iORegistryEntry.getParentEntry(lI00OlAND);
                if (iORegistryEntry2 != null) {
                    byte[] arrby;
                    byte[] arrby2 = iORegistryEntry2.getByteArrayProperty("vendor-id");
                    if (arrby2 != null && arrby2.length >= 2) {
                        map.put(l2, String.format("%02x%02x", arrby2[1], arrby2[0]));
                        bl = true;
                    }
                    if ((arrby = iORegistryEntry2.getByteArrayProperty("device-id")) != null && arrby.length >= 2) {
                        map2.put(l2, String.format("%02x%02x", arrby[1], arrby[0]));
                        bl = true;
                    }
                    iORegistryEntry2.release();
                }
                iORegistryEntry.release();
                iORegistryEntry = iOIterator.next();
            }
            iOIterator.release();
        }
    }

    private static l0l11ilOlAND I1O1I1LaNd(Long l2, String string, String string2, Map map, Map map2, Map map3, Map map4, Map map5, Map map6) {
        String string3 = map3.getOrDefault(l2, string);
        String string4 = map4.getOrDefault(l2, string2);
        List list = map6.getOrDefault(l2, new ArrayList());
        ArrayList<l0l11ilOlAND> arrayList = new ArrayList<l0l11ilOlAND>();
        for (Long l3 : list) {
            arrayList.add(l0l11ilOlAND.I1O1I1LaNd(l3, string3, string4, map, map2, map3, map4, map5, map6));
        }
        Collections.sort(arrayList);
        return new l0l11ilOlAND(map.getOrDefault(l2, string3 + ":" + string4), map2.getOrDefault(l2, ""), string3, string4, map5.getOrDefault(l2, ""), "0x" + Long.toHexString(l2), arrayList);
    }
}

