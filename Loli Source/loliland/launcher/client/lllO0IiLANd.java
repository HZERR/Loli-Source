/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.Wtsapi32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import loliland.launcher.client.Il0iIland;
import loliland.launcher.client.O1iOOlllANd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiO0ilaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lllO0IiLANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lllO0IiLANd.class);
    private static final boolean OOOIilanD = VersionHelpers.IsWindows7OrGreater();

    private lllO0IiLANd() {
    }

    public static Map I1O1I1LaNd(Collection collection) {
        if (OOOIilanD) {
            return lllO0IiLANd.OOOIilanD(collection);
        }
        return lllO0IiLANd.lI00OlAND(collection);
    }

    private static Map OOOIilanD(Collection collection) {
        HashMap<Integer, Il0iIland> hashMap = new HashMap<Integer, Il0iIland>();
        PointerByReference pointerByReference = new PointerByReference();
        IntByReference intByReference = new IntByReference(0);
        if (!Wtsapi32.INSTANCE.WTSEnumerateProcessesEx(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, new IntByReference(1), -2, pointerByReference, intByReference)) {
            I1O1I1LaNd.error("Failed to enumerate Processes. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return hashMap;
        }
        Pointer pointer = pointerByReference.getValue();
        Wtsapi32.WTS_PROCESS_INFO_EX wTS_PROCESS_INFO_EX = new Wtsapi32.WTS_PROCESS_INFO_EX(pointer);
        Wtsapi32.WTS_PROCESS_INFO_EX[] arrwTS_PROCESS_INFO_EX = (Wtsapi32.WTS_PROCESS_INFO_EX[])wTS_PROCESS_INFO_EX.toArray(intByReference.getValue());
        for (int i2 = 0; i2 < arrwTS_PROCESS_INFO_EX.length; ++i2) {
            if (collection != null && !collection.contains(arrwTS_PROCESS_INFO_EX[i2].ProcessId)) continue;
            hashMap.put(arrwTS_PROCESS_INFO_EX[i2].ProcessId, new Il0iIland(arrwTS_PROCESS_INFO_EX[i2].pProcessName, "", arrwTS_PROCESS_INFO_EX[i2].NumberOfThreads, (long)arrwTS_PROCESS_INFO_EX[i2].PagefileUsage & 0xFFFFFFFFL, arrwTS_PROCESS_INFO_EX[i2].KernelTime.getValue() / 10000L, arrwTS_PROCESS_INFO_EX[i2].UserTime.getValue() / 10000L, arrwTS_PROCESS_INFO_EX[i2].HandleCount));
        }
        if (!Wtsapi32.INSTANCE.WTSFreeMemoryEx(1, pointer, intByReference.getValue())) {
            I1O1I1LaNd.warn("Failed to Free Memory for Processes. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
        }
        return hashMap;
    }

    private static Map lI00OlAND(Collection collection) {
        HashMap<Integer, Il0iIland> hashMap = new HashMap<Integer, Il0iIland>();
        WbemcliUtil.WmiResult wmiResult = O1iOOlllANd.I1O1I1LaNd(collection);
        for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
            hashMap.put(ii00llanD.O1il1llOLANd(wmiResult, iiO0ilaNd.I1O1I1LaNd, i2), new Il0iIland(ii00llanD.I1O1I1LaNd(wmiResult, iiO0ilaNd.OOOIilanD, i2), ii00llanD.I1O1I1LaNd(wmiResult, iiO0ilaNd.lIOILand, i2), ii00llanD.O1il1llOLANd(wmiResult, iiO0ilaNd.li0iOILAND, i2), 1024L * ((long)ii00llanD.O1il1llOLANd(wmiResult, iiO0ilaNd.O1il1llOLANd, i2) & 0xFFFFFFFFL), ii00llanD.li0iOILAND(wmiResult, iiO0ilaNd.lI00OlAND, i2) / 10000L, ii00llanD.li0iOILAND(wmiResult, iiO0ilaNd.lli0OiIlAND, i2) / 10000L, ii00llanD.O1il1llOLANd(wmiResult, iiO0ilaNd.Oill1LAnD, i2)));
        }
        return hashMap;
    }
}

