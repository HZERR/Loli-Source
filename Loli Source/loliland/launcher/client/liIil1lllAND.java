/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.Cfgmgr32Util;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.SetupApi;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.stream.Collectors;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.ll00LanD;

public final class liIil1lllAND {
    private static final int I1O1I1LaNd = 260;
    private static final SetupApi OOOIilanD = SetupApi.INSTANCE;
    private static final ll00LanD lI00OlAND = ll00LanD.INSTANCE;

    private liIil1lllAND() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static OI1l11LaNd I1O1I1LaNd(Guid.GUID gUID) {
        Object object;
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        HashMap<Integer, String> hashMap2 = new HashMap<Integer, String>();
        HashMap<Integer, String> hashMap3 = new HashMap<Integer, String>();
        HashMap<Integer, String> hashMap4 = new HashMap<Integer, String>();
        WinNT.HANDLE hANDLE = OOOIilanD.SetupDiGetClassDevs(gUID, null, null, 18);
        if (hANDLE != WinBase.INVALID_HANDLE_VALUE) {
            try {
                object = new Memory(260L);
                IntByReference intByReference = new IntByReference(260);
                ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
                SetupApi.SP_DEVINFO_DATA sP_DEVINFO_DATA = new SetupApi.SP_DEVINFO_DATA();
                sP_DEVINFO_DATA.cbSize = sP_DEVINFO_DATA.size();
                int n3 = 0;
                while (OOOIilanD.SetupDiEnumDeviceInfo(hANDLE, n3, sP_DEVINFO_DATA)) {
                    arrayDeque.add(sP_DEVINFO_DATA.DevInst);
                    int n4 = 0;
                    IntByReference intByReference2 = new IntByReference();
                    IntByReference intByReference3 = new IntByReference();
                    while (!arrayDeque.isEmpty()) {
                        n4 = (Integer)arrayDeque.poll();
                        String string = Cfgmgr32Util.CM_Get_Device_ID(n4);
                        hashMap3.put(n4, string);
                        String string2 = liIil1lllAND.I1O1I1LaNd(n4, 13, (Memory)object, intByReference);
                        if (string2.isEmpty()) {
                            string2 = liIil1lllAND.I1O1I1LaNd(n4, 1, (Memory)object, intByReference);
                        }
                        if (string2.isEmpty()) {
                            string2 = liIil1lllAND.I1O1I1LaNd(n4, 8, (Memory)object, intByReference);
                            String string3 = liIil1lllAND.I1O1I1LaNd(n4, 5, (Memory)object, intByReference);
                            if (!string3.isEmpty()) {
                                string2 = string2 + " (" + string3 + ")";
                            }
                        }
                        hashMap2.put(n4, string2);
                        hashMap4.put(n4, liIil1lllAND.I1O1I1LaNd(n4, 12, (Memory)object, intByReference));
                        if (0 != lI00OlAND.CM_Get_Child(intByReference2, n4, 0)) continue;
                        hashMap.put(intByReference2.getValue(), n4);
                        arrayDeque.add(intByReference2.getValue());
                        while (0 == lI00OlAND.CM_Get_Sibling(intByReference3, intByReference2.getValue(), 0)) {
                            hashMap.put(intByReference3.getValue(), n4);
                            arrayDeque.add(intByReference3.getValue());
                            intByReference2.setValue(intByReference3.getValue());
                        }
                    }
                    ++n3;
                }
            }
            finally {
                OOOIilanD.SetupDiDestroyDeviceInfoList(hANDLE);
            }
        }
        object = hashMap3.keySet().stream().filter(n2 -> !hashMap.containsKey(n2)).collect(Collectors.toSet());
        return new OI1l11LaNd(object, hashMap, hashMap2, hashMap3, hashMap4);
    }

    private static String I1O1I1LaNd(int n2, int n3, Memory memory, IntByReference intByReference) {
        memory.clear();
        intByReference.setValue((int)memory.size());
        lI00OlAND.CM_Get_DevNode_Registry_Property(n2, n3, null, memory, intByReference, 0);
        return memory.getWideString(0L);
    }
}

