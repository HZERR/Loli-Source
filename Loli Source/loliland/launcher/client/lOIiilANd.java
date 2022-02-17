/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinPerf;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OI1IILand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lOIiilANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lOIiilANd.class);
    private static final String OOOIilanD = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\009";
    private static final String lI00OlAND = "Counter";
    private static final Map lli0OiIlAND = lOIiilANd.I1O1I1LaNd();

    private lOIiilANd() {
    }

    public static IIOOOlIiLanD I1O1I1LaNd(String string, Class class_) {
        O1IiIiI1LAND o1IiIiI1LAND = lOIiilANd.OOOIilanD(string, class_);
        if (o1IiIiI1LAND == null) {
            return null;
        }
        Memory memory = lOIiilANd.I1O1I1LaNd(string);
        if (memory == null) {
            return null;
        }
        WinPerf.PERF_DATA_BLOCK pERF_DATA_BLOCK = new WinPerf.PERF_DATA_BLOCK(memory.share(0L));
        long l2 = pERF_DATA_BLOCK.PerfTime100nSec.getValue();
        long l3 = WinBase.FILETIME.filetimeToDate((int)(l2 >> 32), (int)(l2 & 0xFFFFFFFFL)).getTime();
        long l4 = pERF_DATA_BLOCK.HeaderLength;
        for (int i2 = 0; i2 < pERF_DATA_BLOCK.NumObjectTypes; ++i2) {
            WinPerf.PERF_OBJECT_TYPE pERF_OBJECT_TYPE = new WinPerf.PERF_OBJECT_TYPE(memory.share(l4));
            if (pERF_OBJECT_TYPE.ObjectNameTitleIndex == (Integer)lli0OiIlAND.get(string)) {
                long l5 = l4 + (long)pERF_OBJECT_TYPE.HeaderLength;
                HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
                HashMap<Integer, Integer> hashMap2 = new HashMap<Integer, Integer>();
                for (int i3 = 0; i3 < pERF_OBJECT_TYPE.NumCounters; ++i3) {
                    WinPerf.PERF_COUNTER_DEFINITION pERF_COUNTER_DEFINITION = new WinPerf.PERF_COUNTER_DEFINITION(memory.share(l5));
                    hashMap.put(pERF_COUNTER_DEFINITION.CounterNameTitleIndex, pERF_COUNTER_DEFINITION.CounterOffset);
                    hashMap2.put(pERF_COUNTER_DEFINITION.CounterNameTitleIndex, pERF_COUNTER_DEFINITION.CounterSize);
                    l5 += (long)pERF_COUNTER_DEFINITION.ByteLength;
                }
                long l6 = l4 + (long)pERF_OBJECT_TYPE.DefinitionLength;
                ArrayList arrayList = new ArrayList(pERF_OBJECT_TYPE.NumInstances);
                for (int i4 = 0; i4 < pERF_OBJECT_TYPE.NumInstances; ++i4) {
                    WinPerf.PERF_INSTANCE_DEFINITION pERF_INSTANCE_DEFINITION = new WinPerf.PERF_INSTANCE_DEFINITION(memory.share(l6));
                    long l7 = l6 + (long)pERF_INSTANCE_DEFINITION.ByteLength;
                    EnumMap<Enum, Object> enumMap = new EnumMap<Enum, Object>(class_);
                    Enum[] arrenum = (Enum[])class_.getEnumConstants();
                    enumMap.put(arrenum[0], memory.getWideString(l6 + (long)pERF_INSTANCE_DEFINITION.NameOffset));
                    for (int i5 = 1; i5 < arrenum.length; ++i5) {
                        Enum enum_ = arrenum[i5];
                        int n2 = (Integer)lli0OiIlAND.get(((OI1IILand)((Object)enum_)).I1O1I1LaNd());
                        int n3 = hashMap2.getOrDefault(n2, 0);
                        if (n3 == 4) {
                            enumMap.put(enum_, Integer.valueOf(memory.getInt(l7 + (long)((Integer)hashMap.get(n2)).intValue())));
                            continue;
                        }
                        if (n3 == 8) {
                            enumMap.put(enum_, Long.valueOf(memory.getLong(l7 + (long)((Integer)hashMap.get(n2)).intValue())));
                            continue;
                        }
                        return null;
                    }
                    arrayList.add(enumMap);
                    l6 = l7 + (long)new WinPerf.PERF_COUNTER_BLOCK((Pointer)memory.share((long)l7)).ByteLength;
                }
                return new IIOOOlIiLanD(arrayList, l2, l3);
            }
            l4 += (long)pERF_OBJECT_TYPE.TotalByteLength;
        }
        return null;
    }

    private static O1IiIiI1LAND OOOIilanD(String string, Class class_) {
        if (!lli0OiIlAND.containsKey(string)) {
            I1O1I1LaNd.debug("Couldn't find counter index of {}.", (Object)string);
            return null;
        }
        int n2 = (Integer)lli0OiIlAND.get(string);
        Enum[] arrenum = (Enum[])class_.getEnumConstants();
        EnumMap enumMap = new EnumMap(class_);
        for (int i2 = 1; i2 < arrenum.length; ++i2) {
            Enum enum_ = arrenum[i2];
            String string2 = ((OI1IILand)((Object)enum_)).I1O1I1LaNd();
            if (!lli0OiIlAND.containsKey(string2)) {
                I1O1I1LaNd.debug("Couldn't find counter index of {}.", (Object)string2);
                return null;
            }
            enumMap.put(enum_, lli0OiIlAND.get(string2));
        }
        return new O1IiIiI1LAND(n2, enumMap);
    }

    private static Memory I1O1I1LaNd(String string) {
        IntByReference intByReference;
        int n2;
        Memory memory;
        String string2 = Integer.toString((Integer)lli0OiIlAND.get(string));
        int n3 = Advapi32.INSTANCE.RegQueryValueEx(WinReg.HKEY_PERFORMANCE_DATA, string2, 0, null, memory = new Memory(n2 = 4096), intByReference = new IntByReference(n2));
        if (n3 != 0 && n3 != 234) {
            I1O1I1LaNd.error("Error reading performance data from registry for {}.", (Object)string);
            return null;
        }
        while (n3 == 234) {
            intByReference.setValue(n2 += 4096);
            memory = new Memory(n2);
            n3 = Advapi32.INSTANCE.RegQueryValueEx(WinReg.HKEY_PERFORMANCE_DATA, string2, 0, null, memory, intByReference);
        }
        return memory;
    }

    private static Map I1O1I1LaNd() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        try {
            String[] arrstring = Advapi32Util.registryGetStringArray(WinReg.HKEY_LOCAL_MACHINE, OOOIilanD, lI00OlAND);
            for (int i2 = 1; i2 < arrstring.length; i2 += 2) {
                hashMap.putIfAbsent(arrstring[i2], Integer.parseInt(arrstring[i2 - 1]));
            }
        }
        catch (Win32Exception win32Exception) {
            I1O1I1LaNd.error("Unable to locate English counter names in registry Perflib 009. Counters may need to be rebuilt: ", win32Exception);
        }
        catch (NumberFormatException numberFormatException) {
            I1O1I1LaNd.error("Unable to parse English counter names in registry Perflib 009.");
        }
        return Collections.unmodifiableMap(hashMap);
    }
}

