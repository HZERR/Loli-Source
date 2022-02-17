/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.PdhUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.O1O00OllAND;
import loliland.launcher.client.OI1IILand;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.iiO101LanD;
import loliland.launcher.client.il000IOOlaNd;
import loliland.launcher.client.l1Oili01LaND;
import loliland.launcher.client.li0i01LanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class i10lIIOllANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(i10lIIOllANd.class);
    private static final Set OOOIilanD = ConcurrentHashMap.newKeySet();

    private i10lIIOllANd() {
    }

    public static O1IiIiI1LAND I1O1I1LaNd(Class class_, String string, String string2) {
        if (!OOOIilanD.contains(string)) {
            O1IiIiI1LAND o1IiIiI1LAND = i10lIIOllANd.I1O1I1LaNd(class_, string);
            if (!((List)o1IiIiI1LAND.I1O1I1LaNd()).isEmpty()) {
                return o1IiIiI1LAND;
            }
            I1O1I1LaNd.warn("Disabling further attempts to query {}.", (Object)string);
            OOOIilanD.add(string);
        }
        return i10lIIOllANd.OOOIilanD(class_, string2);
    }

    public static O1IiIiI1LAND I1O1I1LaNd(Class class_, String string) {
        PdhUtil.PdhEnumObjectItems pdhEnumObjectItems;
        Enum[] arrenum = (Enum[])class_.getEnumConstants();
        if (arrenum.length < 2) {
            throw new IllegalArgumentException("Enum " + class_.getName() + " must have at least two elements, an instance filter and a counter.");
        }
        String string3 = ((OI1IILand)((Object)((Enum[])class_.getEnumConstants())[0])).I1O1I1LaNd().toLowerCase();
        String string4 = iiO101LanD.I1O1I1LaNd(string);
        try {
            pdhEnumObjectItems = PdhUtil.PdhEnumObjectItems(null, null, string4, 100);
        }
        catch (PdhUtil.PdhException pdhException) {
            return new O1IiIiI1LAND(Collections.emptyList(), Collections.emptyMap());
        }
        List<String> list = pdhEnumObjectItems.getInstances();
        list.removeIf(string2 -> !iiIIIlO1lANd.I1O1I1LaNd(string2.toLowerCase(), string3));
        EnumMap enumMap = new EnumMap(class_);
        try (il000IOOlaNd il000IOOlaNd2 = new il000IOOlaNd();){
            ArrayList<Object> arrayList;
            Enum enum_;
            int n2;
            EnumMap enumMap2 = new EnumMap(class_);
            for (n2 = 1; n2 < arrenum.length; ++n2) {
                enum_ = arrenum[n2];
                arrayList = new ArrayList<Object>(list.size());
                for (String object : list) {
                    l1Oili01LaND l1Oili01LaND2 = O1O00OllAND.I1O1I1LaNd(string, object, ((OI1IILand)((Object)enum_)).I1O1I1LaNd());
                    if (!il000IOOlaNd2.I1O1I1LaNd(l1Oili01LaND2)) {
                        O1IiIiI1LAND o1IiIiI1LAND = new O1IiIiI1LAND(Collections.emptyList(), Collections.emptyMap());
                        return o1IiIiI1LAND;
                    }
                    arrayList.add(l1Oili01LaND2);
                }
                enumMap2.put(enum_, arrayList);
            }
            if (0L < il000IOOlaNd2.OOOIilanD()) {
                for (n2 = 1; n2 < arrenum.length; ++n2) {
                    enum_ = arrenum[n2];
                    arrayList = new ArrayList();
                    for (l1Oili01LaND l1Oili01LaND3 : (List)enumMap2.get(enum_)) {
                        arrayList.add(il000IOOlaNd2.lI00OlAND(l1Oili01LaND3));
                    }
                    enumMap.put(enum_, arrayList);
                }
            }
        }
        return new O1IiIiI1LAND(list, enumMap);
    }

    public static O1IiIiI1LAND OOOIilanD(Class class_, String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        EnumMap enumMap = new EnumMap(class_);
        WbemcliUtil.WmiQuery wmiQuery = new WbemcliUtil.WmiQuery(string, class_);
        WbemcliUtil.WmiResult wmiResult = li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
        if (wmiResult.getResultCount() > 0) {
            for (Enum enum_ : (Enum[])class_.getEnumConstants()) {
                if (enum_.ordinal() == 0) {
                    for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                        arrayList.add(ii00llanD.I1O1I1LaNd(wmiResult, enum_, i2));
                    }
                    continue;
                }
                ArrayList<Long> arrayList2 = new ArrayList<Long>();
                block8: for (int i3 = 0; i3 < wmiResult.getResultCount(); ++i3) {
                    switch (wmiResult.getCIMType(enum_)) {
                        case 18: {
                            arrayList2.add(Long.valueOf(ii00llanD.lil0liLand(wmiResult, enum_, i3)));
                            continue block8;
                        }
                        case 19: {
                            arrayList2.add(ii00llanD.Oill1LAnD(wmiResult, enum_, i3));
                            continue block8;
                        }
                        case 21: {
                            arrayList2.add(ii00llanD.li0iOILAND(wmiResult, enum_, i3));
                            continue block8;
                        }
                        case 101: {
                            arrayList2.add(ii00llanD.lI00OlAND(wmiResult, enum_, i3).toInstant().toEpochMilli());
                            continue block8;
                        }
                        default: {
                            throw new ClassCastException("Unimplemented CIM Type Mapping.");
                        }
                    }
                }
                enumMap.put(enum_, arrayList2);
            }
        }
        return new O1IiIiI1LAND(arrayList, enumMap);
    }
}

