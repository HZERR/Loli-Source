/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.DiskArbitration;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.l000iLAnd;
import loliland.launcher.client.lOOIIlAnd;
import loliland.launcher.client.liOIOOLANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class liOIIlLANd
extends OI1i0ilanD {
    private static final CoreFoundation I1O1I1LaNd = CoreFoundation.INSTANCE;
    private static final DiskArbitration OOOIilanD = DiskArbitration.INSTANCE;
    private static final Logger lI00OlAND = LoggerFactory.getLogger(liOIIlLANd.class);
    private long lli0OiIlAND = 0L;
    private long li0iOILAND = 0L;
    private long O1il1llOLANd = 0L;
    private long Oill1LAnD = 0L;
    private long lIOILand = 0L;
    private long lil0liLand = 0L;
    private long iilIi1laND = 0L;
    private List lli011lLANd;

    private liOIIlLANd(String string, String string2, String string3, long l2, DiskArbitration.DASessionRef dASessionRef, Map map, Map map2) {
        super(string, string2, string3, l2);
        this.I1O1I1LaNd(dASessionRef, map, map2);
    }

    @Override
    public long li0iOILAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public long O1il1llOLANd() {
        return this.li0iOILAND;
    }

    @Override
    public long Oill1LAnD() {
        return this.O1il1llOLANd;
    }

    @Override
    public long lIOILand() {
        return this.Oill1LAnD;
    }

    @Override
    public long lil0liLand() {
        return this.lIOILand;
    }

    @Override
    public long iilIi1laND() {
        return this.lil0liLand;
    }

    @Override
    public long l0illAND() {
        return this.iilIi1laND;
    }

    @Override
    public List lli011lLANd() {
        return this.lli011lLANd;
    }

    @Override
    public boolean IO11O0LANd() {
        DiskArbitration.DASessionRef dASessionRef = OOOIilanD.DASessionCreate(I1O1I1LaNd.CFAllocatorGetDefault());
        if (dASessionRef == null) {
            lI00OlAND.error("Unable to open session to DiskArbitration framework.");
            return false;
        }
        Map map = liOIIlLANd.lO110l1LANd();
        boolean bl = this.I1O1I1LaNd(dASessionRef, liOIOOLANd.I1O1I1LaNd(), map);
        dASessionRef.release();
        for (CoreFoundation.CFTypeRef cFTypeRef : map.values()) {
            cFTypeRef.release();
        }
        return bl;
    }

    private boolean I1O1I1LaNd(DiskArbitration.DASessionRef dASessionRef, Map map, Map map2) {
        IOKit.IOIterator iOIterator;
        String string = this.I1O1I1LaNd();
        CoreFoundation.CFMutableDictionaryRef cFMutableDictionaryRef = IOKitUtil.getBSDNameMatchingDict(string);
        if (cFMutableDictionaryRef != null && (iOIterator = IOKitUtil.getMatchingServices(cFMutableDictionaryRef)) != null) {
            IOKit.IORegistryEntry iORegistryEntry = iOIterator.next();
            if (iORegistryEntry != null) {
                if (iORegistryEntry.conformsTo("IOMedia")) {
                    Object object;
                    Object object2;
                    CoreFoundation.CFNumberRef cFNumberRef;
                    Object object3;
                    Object object4;
                    Object object5;
                    IOKit.IORegistryEntry iORegistryEntry2 = iORegistryEntry.getParentEntry("IOService");
                    if (iORegistryEntry2 != null && (iORegistryEntry2.conformsTo("IOBlockStorageDriver") || iORegistryEntry2.conformsTo("AppleAPFSContainerScheme"))) {
                        object5 = iORegistryEntry2.createCFProperties();
                        object4 = ((CoreFoundation.CFDictionaryRef)object5).getValue((PointerType)map2.get((Object)lOOIIlAnd.OOOIilanD));
                        object3 = new CoreFoundation.CFDictionaryRef((Pointer)object4);
                        this.iilIi1laND = System.currentTimeMillis();
                        object4 = ((CoreFoundation.CFDictionaryRef)object3).getValue((PointerType)map2.get((Object)lOOIIlAnd.lI00OlAND));
                        cFNumberRef = new CoreFoundation.CFNumberRef((Pointer)object4);
                        this.lli0OiIlAND = cFNumberRef.longValue();
                        object4 = ((CoreFoundation.CFDictionaryRef)object3).getValue((PointerType)map2.get((Object)lOOIIlAnd.lli0OiIlAND));
                        cFNumberRef.setPointer((Pointer)object4);
                        this.li0iOILAND = cFNumberRef.longValue();
                        object4 = ((CoreFoundation.CFDictionaryRef)object3).getValue((PointerType)map2.get((Object)lOOIIlAnd.O1il1llOLANd));
                        cFNumberRef.setPointer((Pointer)object4);
                        this.O1il1llOLANd = cFNumberRef.longValue();
                        object4 = ((CoreFoundation.CFDictionaryRef)object3).getValue((PointerType)map2.get((Object)lOOIIlAnd.Oill1LAnD));
                        cFNumberRef.setPointer((Pointer)object4);
                        this.Oill1LAnD = cFNumberRef.longValue();
                        object2 = ((CoreFoundation.CFDictionaryRef)object3).getValue((PointerType)map2.get((Object)lOOIIlAnd.li0iOILAND));
                        object = ((CoreFoundation.CFDictionaryRef)object3).getValue((PointerType)map2.get((Object)lOOIIlAnd.lIOILand));
                        if (object2 != null && object != null) {
                            cFNumberRef.setPointer((Pointer)object2);
                            long l2 = cFNumberRef.longValue();
                            cFNumberRef.setPointer((Pointer)object);
                            this.lil0liLand = (l2 += cFNumberRef.longValue()) / 1000000L;
                        }
                        ((CoreFoundation.CFTypeRef)object5).release();
                    } else {
                        lI00OlAND.debug("Unable to find block storage driver properties for {}", (Object)string);
                    }
                    object5 = new ArrayList();
                    object4 = iORegistryEntry.createCFProperties();
                    object3 = ((CoreFoundation.CFDictionaryRef)object4).getValue((PointerType)map2.get((Object)lOOIIlAnd.lil0liLand));
                    cFNumberRef = new CoreFoundation.CFNumberRef((Pointer)object3);
                    object3 = ((CoreFoundation.CFDictionaryRef)object4).getValue((PointerType)map2.get((Object)lOOIIlAnd.iilIi1laND));
                    object2 = new CoreFoundation.CFBooleanRef((Pointer)object3);
                    object = I1O1I1LaNd.CFDictionaryCreateMutable(I1O1I1LaNd.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                    ((CoreFoundation.CFMutableDictionaryRef)object).setValue((PointerType)map2.get((Object)lOOIIlAnd.lil0liLand), cFNumberRef);
                    ((CoreFoundation.CFMutableDictionaryRef)object).setValue((PointerType)map2.get((Object)lOOIIlAnd.lli011lLANd), (PointerType)object2);
                    cFMutableDictionaryRef = I1O1I1LaNd.CFDictionaryCreateMutable(I1O1I1LaNd.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                    cFMutableDictionaryRef.setValue((PointerType)map2.get((Object)lOOIIlAnd.I1O1I1LaNd), (PointerType)object);
                    IOKit.IOIterator iOIterator2 = IOKitUtil.getMatchingServices(cFMutableDictionaryRef);
                    ((CoreFoundation.CFTypeRef)object4).release();
                    ((CoreFoundation.CFTypeRef)object).release();
                    if (iOIterator2 != null) {
                        IOKit.IORegistryEntry iORegistryEntry3 = IOKit.INSTANCE.IOIteratorNext(iOIterator2);
                        while (iORegistryEntry3 != null) {
                            Object object6;
                            String string2;
                            String string3 = string2 = iORegistryEntry3.getStringProperty("BSD Name");
                            String string4 = "";
                            DiskArbitration.DADiskRef dADiskRef = OOOIilanD.DADiskCreateFromBSDName(I1O1I1LaNd.CFAllocatorGetDefault(), dASessionRef, string2);
                            if (dADiskRef != null) {
                                object6 = OOOIilanD.DADiskCopyDescription(dADiskRef);
                                if (object6 != null) {
                                    object3 = ((CoreFoundation.CFDictionaryRef)object6).getValue((PointerType)map2.get((Object)lOOIIlAnd.l0illAND));
                                    string4 = l000iLAnd.I1O1I1LaNd((Pointer)object3);
                                    object3 = ((CoreFoundation.CFDictionaryRef)object6).getValue((PointerType)map2.get((Object)lOOIIlAnd.IO11O0LANd));
                                    string3 = object3 == null ? string4 : l000iLAnd.I1O1I1LaNd((Pointer)object3);
                                    ((CoreFoundation.CFTypeRef)object6).release();
                                }
                                dADiskRef.release();
                            }
                            object6 = map.getOrDefault(string2, "");
                            Long l3 = iORegistryEntry3.getLongProperty("Size");
                            Integer n2 = iORegistryEntry3.getIntegerProperty("BSD Major");
                            Integer n3 = iORegistryEntry3.getIntegerProperty("BSD Minor");
                            String string5 = iORegistryEntry3.getStringProperty("UUID");
                            object5.add(new i0Oi1LANd(string2, string3, string4, string5 == null ? "unknown" : string5, l3 == null ? 0L : l3, n2 == null ? 0 : n2, n3 == null ? 0 : n3, (String)object6));
                            iORegistryEntry3.release();
                            iORegistryEntry3 = IOKit.INSTANCE.IOIteratorNext(iOIterator2);
                        }
                        iOIterator2.release();
                    }
                    this.lli011lLANd = Collections.unmodifiableList(object5.stream().sorted(Comparator.comparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList()));
                    if (iORegistryEntry2 != null) {
                        iORegistryEntry2.release();
                    }
                } else {
                    lI00OlAND.error("Unable to find IOMedia device or parent for {}", (Object)string);
                }
                iORegistryEntry.release();
            }
            iOIterator.release();
            return true;
        }
        return false;
    }

    public static List l11lLANd() {
        Object object;
        Map map = liOIOOLANd.I1O1I1LaNd();
        Map map2 = liOIIlLANd.lO110l1LANd();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        DiskArbitration.DASessionRef dASessionRef = OOOIilanD.DASessionCreate(I1O1I1LaNd.CFAllocatorGetDefault());
        if (dASessionRef == null) {
            lI00OlAND.error("Unable to open session to DiskArbitration framework.");
            return Collections.emptyList();
        }
        ArrayList<String> arrayList2 = new ArrayList<String>();
        IOKit.IOIterator iOIterator = IOKitUtil.getMatchingServices("IOMedia");
        if (iOIterator != null) {
            Object object2 = iOIterator.next();
            while (object2 != null) {
                Boolean object3 = ((IOKit.IORegistryEntry)object2).getBooleanProperty("Whole");
                if (object3 != null && object3.booleanValue()) {
                    object = OOOIilanD.DADiskCreateFromIOMedia(I1O1I1LaNd.CFAllocatorGetDefault(), dASessionRef, (IOKit.IOObject)object2);
                    arrayList2.add(OOOIilanD.DADiskGetBSDName((DiskArbitration.DADiskRef)object));
                    ((CoreFoundation.CFTypeRef)object).release();
                }
                ((IOKit.IOObject)object2).release();
                object2 = iOIterator.next();
            }
            iOIterator.release();
        }
        for (String string : arrayList2) {
            Object object2;
            object = "";
            String string2 = "";
            long l2 = 0L;
            String string3 = "/dev/" + string;
            DiskArbitration.DADiskRef dADiskRef = OOOIilanD.DADiskCreateFromBSDName(I1O1I1LaNd.CFAllocatorGetDefault(), dASessionRef, string3);
            if (dADiskRef == null) continue;
            CoreFoundation.CFDictionaryRef cFDictionaryRef = OOOIilanD.DADiskCopyDescription(dADiskRef);
            if (cFDictionaryRef != null) {
                object2 = cFDictionaryRef.getValue((PointerType)map2.get((Object)lOOIIlAnd.lO110l1LANd));
                object = l000iLAnd.I1O1I1LaNd((Pointer)object2);
                object2 = cFDictionaryRef.getValue((PointerType)map2.get((Object)lOOIIlAnd.l11lLANd));
                CoreFoundation.CFNumberRef cFNumberRef = new CoreFoundation.CFNumberRef((Pointer)object2);
                l2 = cFNumberRef.longValue();
                cFDictionaryRef.release();
                if (!"Disk Image".equals(object)) {
                    CoreFoundation.CFStringRef cFStringRef = CoreFoundation.CFStringRef.createCFString((String)object);
                    CoreFoundation.CFMutableDictionaryRef cFMutableDictionaryRef = I1O1I1LaNd.CFDictionaryCreateMutable(I1O1I1LaNd.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                    cFMutableDictionaryRef.setValue((PointerType)map2.get((Object)lOOIIlAnd.l0iIlIO1laNd), cFStringRef);
                    CoreFoundation.CFMutableDictionaryRef cFMutableDictionaryRef2 = I1O1I1LaNd.CFDictionaryCreateMutable(I1O1I1LaNd.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                    cFMutableDictionaryRef2.setValue((PointerType)map2.get((Object)lOOIIlAnd.I1O1I1LaNd), cFMutableDictionaryRef);
                    IOKit.IOIterator iOIterator2 = IOKitUtil.getMatchingServices(cFMutableDictionaryRef2);
                    cFStringRef.release();
                    cFMutableDictionaryRef.release();
                    if (iOIterator2 != null) {
                        IOKit.IORegistryEntry iORegistryEntry = iOIterator2.next();
                        while (iORegistryEntry != null) {
                            string2 = iORegistryEntry.getStringProperty("Serial Number");
                            iORegistryEntry.release();
                            if (string2 != null) break;
                            iORegistryEntry.release();
                            iORegistryEntry = iOIterator2.next();
                        }
                        iOIterator2.release();
                    }
                    if (string2 == null) {
                        string2 = "";
                    }
                }
            }
            dADiskRef.release();
            if (l2 <= 0L) continue;
            object2 = new liOIIlLANd(string, ((String)object).trim(), string2.trim(), l2, dASessionRef, map, map2);
            arrayList.add(object2);
        }
        dASessionRef.release();
        for (CoreFoundation.CFTypeRef cFTypeRef : map2.values()) {
            cFTypeRef.release();
        }
        return arrayList;
    }

    private static Map lO110l1LANd() {
        EnumMap<lOOIIlAnd, CoreFoundation.CFStringRef> enumMap = new EnumMap<lOOIIlAnd, CoreFoundation.CFStringRef>(lOOIIlAnd.class);
        for (lOOIIlAnd lOOIIlAnd2 : lOOIIlAnd.values()) {
            enumMap.put(lOOIIlAnd2, CoreFoundation.CFStringRef.createCFString(lOOIIlAnd2.I1O1I1LaNd()));
        }
        return enumMap;
    }
}

