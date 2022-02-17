/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.PdhUtil;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.Win32Exception;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import loliland.launcher.client.O1O00OllAND;
import loliland.launcher.client.iOl1LaNd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.il000IOOlaNd;
import loliland.launcher.client.l1Oili01LaND;
import loliland.launcher.client.li0i01LanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class iiO101LanD {
    private static final Logger li0iOILAND = LoggerFactory.getLogger(iiO101LanD.class);
    private static final boolean O1il1llOLANd = VersionHelpers.IsWindowsVistaOrGreater();
    private static final Set Oill1LAnD = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap lIOILand = O1il1llOLANd ? null : new ConcurrentHashMap();
    public static final String I1O1I1LaNd = "_Total";
    public static final String OOOIilanD = "*_Total";
    public static final String lI00OlAND = "^_Total";
    public static final String lli0OiIlAND = "^*_Total";

    private iiO101LanD() {
    }

    public static Map I1O1I1LaNd(Class class_, String string, String string2) {
        if (!Oill1LAnD.contains(string)) {
            Map map = iiO101LanD.I1O1I1LaNd(class_, string);
            if (!map.isEmpty()) {
                return map;
            }
            li0iOILAND.warn("Disabling further attempts to query {}.", (Object)string);
            Oill1LAnD.add(string);
        }
        return iiO101LanD.OOOIilanD(class_, string2);
    }

    public static Map I1O1I1LaNd(Class class_, String string) {
        Enum[] arrenum = (Enum[])class_.getEnumConstants();
        String string2 = iiO101LanD.I1O1I1LaNd(string);
        EnumMap<Enum, l1Oili01LaND> enumMap = new EnumMap<Enum, l1Oili01LaND>(class_);
        EnumMap<Enum, Long> enumMap2 = new EnumMap<Enum, Long>(class_);
        try (il000IOOlaNd il000IOOlaNd2 = new il000IOOlaNd();){
            for (Enum enum_ : arrenum) {
                l1Oili01LaND l1Oili01LaND2 = O1O00OllAND.I1O1I1LaNd(string2, ((iOl1LaNd)((Object)enum_)).I1O1I1LaNd(), ((iOl1LaNd)((Object)enum_)).OOOIilanD());
                enumMap.put(enum_, l1Oili01LaND2);
                if (il000IOOlaNd2.I1O1I1LaNd(l1Oili01LaND2)) continue;
                EnumMap<Enum, Long> enumMap3 = enumMap2;
                return enumMap3;
            }
            if (0L < il000IOOlaNd2.OOOIilanD()) {
                for (Enum enum_ : arrenum) {
                    enumMap2.put(enum_, il000IOOlaNd2.lI00OlAND((l1Oili01LaND)enumMap.get(enum_)));
                }
            }
        }
        return enumMap2;
    }

    public static Map OOOIilanD(Class class_, String string) {
        WbemcliUtil.WmiQuery wmiQuery = new WbemcliUtil.WmiQuery(string, class_);
        WbemcliUtil.WmiResult wmiResult = li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
        EnumMap<Enum, Long> enumMap = new EnumMap<Enum, Long>(class_);
        if (wmiResult.getResultCount() > 0) {
            block6: for (Enum enum_ : (Enum[])class_.getEnumConstants()) {
                switch (wmiResult.getCIMType(enum_)) {
                    case 18: {
                        enumMap.put(enum_, Long.valueOf(ii00llanD.lil0liLand(wmiResult, enum_, 0)));
                        continue block6;
                    }
                    case 19: {
                        enumMap.put(enum_, ii00llanD.Oill1LAnD(wmiResult, enum_, 0));
                        continue block6;
                    }
                    case 21: {
                        enumMap.put(enum_, ii00llanD.li0iOILAND(wmiResult, enum_, 0));
                        continue block6;
                    }
                    case 101: {
                        enumMap.put(enum_, ii00llanD.lI00OlAND(wmiResult, enum_, 0).toInstant().toEpochMilli());
                        continue block6;
                    }
                    default: {
                        throw new ClassCastException("Unimplemented CIM Type Mapping.");
                    }
                }
            }
        }
        return enumMap;
    }

    public static String I1O1I1LaNd(String string) {
        return O1il1llOLANd ? string : lIOILand.computeIfAbsent(string, iiO101LanD::OOOIilanD);
    }

    private static String OOOIilanD(String string) {
        String string2 = string;
        try {
            string2 = PdhUtil.PdhLookupPerfNameByIndex(null, PdhUtil.PdhLookupPerfIndexByEnglishName(string));
        }
        catch (Win32Exception win32Exception) {
            li0iOILAND.warn("Unable to locate English counter names in registry Perflib 009. Assuming English counters. Error {}. {}", (Object)String.format("0x%x", win32Exception.getHR().intValue()), (Object)"See https://support.microsoft.com/en-us/help/300956/how-to-manually-rebuild-performance-counter-library-values");
        }
        catch (PdhUtil.PdhException pdhException) {
            li0iOILAND.warn("Unable to localize {} performance counter.  Error {}.", (Object)string, (Object)String.format("0x%x", pdhException.getErrorCode()));
        }
        if (string2.isEmpty()) {
            return string;
        }
        li0iOILAND.debug("Localized {} to {}", (Object)string, (Object)string2);
        return string2;
    }
}

