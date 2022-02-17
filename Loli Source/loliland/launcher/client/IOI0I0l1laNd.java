/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.W32ServiceManager;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import loliland.launcher.client.IOi0I0LAnD;
import loliland.launcher.client.IOilIl10LanD;
import loliland.launcher.client.IOlli01lAnd;
import loliland.launcher.client.Iil1laND;
import loliland.launcher.client.IliIIi1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.O1liliLANd;
import loliland.launcher.client.O1llI1LAnd;
import loliland.launcher.client.i0I0land;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.i11IllAnd;
import loliland.launcher.client.iIO11lOlLANd;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l10II00OLaND;
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.l1IlliI0lANd;
import loliland.launcher.client.l1OOOLanD;
import loliland.launcher.client.lIIi01O1laNd;
import loliland.launcher.client.lIilLaNd;
import loliland.launcher.client.liIOI1LANd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lii1LAND;
import loliland.launcher.client.lli1OiIland;
import loliland.launcher.client.lllO0IiLANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOI0I0l1laNd
extends lIilLaNd {
    private static final Logger lli0OiIlAND = LoggerFactory.getLogger(IOI0I0l1laNd.class);
    public static final String lI00OlAND = "oshi.os.windows.procstate.suspended";
    private static final boolean li0iOILAND = l10lO11lanD.I1O1I1LaNd("oshi.os.windows.procstate.suspended", false);
    private static final boolean O1il1llOLANd = VersionHelpers.IsWindowsVistaOrGreater();
    private static final int Oill1LAnD = 20;
    private static Supplier lIOILand = lii1IO0LaNd.I1O1I1LaNd(IOI0I0l1laNd::II1i1l0laND, TimeUnit.HOURS.toNanos(1L));
    private static final long lil0liLand = IOI0I0l1laNd.l0IO0LAnd();
    private Supplier iilIi1laND = lii1IO0LaNd.I1O1I1LaNd(IOI0I0l1laNd::lI00ilAND, lii1IO0LaNd.I1O1I1LaNd());
    private Supplier lli011lLANd = lii1IO0LaNd.I1O1I1LaNd(IOI0I0l1laNd::l0l00lAND, lii1IO0LaNd.I1O1I1LaNd());
    private Supplier l0illAND = lii1IO0LaNd.I1O1I1LaNd(IOI0I0l1laNd::iOl10IlLAnd, lii1IO0LaNd.I1O1I1LaNd());
    private Supplier IO11O0LANd = lii1IO0LaNd.I1O1I1LaNd(IOI0I0l1laNd::lIiIii1LAnD, lii1IO0LaNd.I1O1I1LaNd());

    @Override
    public String OOOIilanD() {
        return "Microsoft";
    }

    @Override
    public O1IiIiI1LAND li0iOILAND() {
        String string = System.getProperty("os.name");
        if (string.startsWith("Windows ")) {
            string = string.substring(8);
        }
        String string2 = null;
        int n2 = 0;
        String string3 = null;
        WbemcliUtil.WmiResult wmiResult = IliIIi1lanD.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 0) {
            string2 = ii00llanD.I1O1I1LaNd(wmiResult, O1llI1LAnd.lli0OiIlAND, 0);
            if (!string2.isEmpty() && !"unknown".equals(string2)) {
                string = string + " " + string2.replace("Service Pack ", "SP");
            }
            n2 = ii00llanD.O1il1llOLANd(wmiResult, O1llI1LAnd.li0iOILAND, 0);
            string3 = ii00llanD.I1O1I1LaNd(wmiResult, O1llI1LAnd.lI00OlAND, 0);
        }
        String string4 = IOI0I0l1laNd.li0iOILAND(n2);
        return new O1IiIiI1LAND("Windows", new l111llanD(string, string4, string3));
    }

    private static String li0iOILAND(int n2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if ((n2 & 2) != 0) {
            arrayList.add("Enterprise");
        }
        if ((n2 & 4) != 0) {
            arrayList.add("BackOffice");
        }
        if ((n2 & 8) != 0) {
            arrayList.add("Communications Server");
        }
        if ((n2 & 0x80) != 0) {
            arrayList.add("Datacenter");
        }
        if ((n2 & 0x200) != 0) {
            arrayList.add("Home");
        }
        if ((n2 & 0x400) != 0) {
            arrayList.add("Web Server");
        }
        if ((n2 & 0x2000) != 0) {
            arrayList.add("Storage Server");
        }
        if ((n2 & 0x4000) != 0) {
            arrayList.add("Compute Cluster");
        }
        if ((n2 & 0x8000) != 0) {
            arrayList.add("Home Server");
        }
        return String.join((CharSequence)",", arrayList);
    }

    @Override
    protected int I1O1I1LaNd(int n2) {
        WbemcliUtil.WmiResult wmiResult;
        if (n2 < 64 && System.getenv("ProgramFiles(x86)") != null && O1il1llOLANd && (wmiResult = i11IllAnd.lI00OlAND()).getResultCount() > 0) {
            return ii00llanD.lil0liLand(wmiResult, l10II00OLaND.I1O1I1LaNd, 0);
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean l0iIlIO1laNd() {
        WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
        boolean bl = Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 8, hANDLEByReference);
        if (!bl) {
            lli0OiIlAND.error("OpenProcessToken failed. Error: {}", (Object)Native.getLastError());
            return false;
        }
        try {
            l1OOOLanD l1OOOLanD2 = new l1OOOLanD();
            if (Advapi32.INSTANCE.GetTokenInformation(hANDLEByReference.getValue(), 20, l1OOOLanD2, l1OOOLanD2.size(), new IntByReference())) {
                boolean bl2 = l1OOOLanD2.TokenIsElevated > 0;
                return bl2;
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hANDLEByReference.getValue());
        }
        return false;
    }

    @Override
    public l0l0iIlaND lIOILand() {
        return new l1IlliI0lANd();
    }

    @Override
    public lli1OiIland lil0liLand() {
        return new Iil1laND();
    }

    @Override
    public List ii1li00Land() {
        List list = liIOI1LANd.I1O1I1LaNd();
        list.addAll(lii1LAND.I1O1I1LaNd());
        list.addAll(IOilIl10LanD.I1O1I1LaNd());
        return list;
    }

    @Override
    public List I1O1I1LaNd(Collection collection) {
        return this.OOOIilanD(collection);
    }

    @Override
    public List Oill1LAnD() {
        return this.OOOIilanD(null);
    }

    @Override
    public List OOOIilanD(int n2) {
        Set set = IOI0I0l1laNd.I1O1I1LaNd(IOI0I0l1laNd.IOI1LaNd(), n2, false);
        return this.OOOIilanD(set);
    }

    @Override
    public List lI00OlAND(int n2) {
        Set set = IOI0I0l1laNd.I1O1I1LaNd(IOI0I0l1laNd.IOI1LaNd(), n2, true);
        return this.OOOIilanD(set);
    }

    private static Map IOI1LaNd() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        Tlhelp32.PROCESSENTRY32.ByReference byReference = new Tlhelp32.PROCESSENTRY32.ByReference();
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0L));
        try {
            while (Kernel32.INSTANCE.Process32Next(hANDLE, byReference)) {
                hashMap.put(byReference.th32ProcessID.intValue(), byReference.th32ParentProcessID.intValue());
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hANDLE);
        }
        return hashMap;
    }

    @Override
    public iiO1LAnD lli0OiIlAND(int n2) {
        List list = this.OOOIilanD(Arrays.asList(n2));
        return list.isEmpty() ? null : (iiO1LAnD)list.get(0);
    }

    private List OOOIilanD(Collection collection) {
        Map map = (Map)this.iilIi1laND.get();
        if (map == null || map.isEmpty()) {
            map = collection == null ? (Map)this.lli011lLANd.get() : IOlli01lAnd.OOOIilanD(collection);
        }
        Map map2 = null;
        if (li0iOILAND && ((map2 = (Map)this.l0illAND.get()) == null || map2.isEmpty())) {
            map2 = collection == null ? (Map)this.IO11O0LANd.get() : O1liliLANd.OOOIilanD(collection);
        }
        Map map3 = lllO0IiLANd.I1O1I1LaNd(collection);
        HashSet hashSet = new HashSet(map3.keySet());
        hashSet.retainAll(map.keySet());
        ArrayList<lIIi01O1laNd> arrayList = new ArrayList<lIIi01O1laNd>();
        for (Integer n2 : hashSet) {
            arrayList.add(new lIIi01O1laNd(n2, this, map, map3, map2));
        }
        return arrayList;
    }

    private static Map lI00ilAND() {
        return IOlli01lAnd.I1O1I1LaNd(null);
    }

    private static Map l0l00lAND() {
        return IOlli01lAnd.OOOIilanD(null);
    }

    private static Map iOl10IlLAnd() {
        return O1liliLANd.I1O1I1LaNd(null);
    }

    private static Map lIiIii1LAnD() {
        return O1liliLANd.OOOIilanD(null);
    }

    @Override
    public int lli011lLANd() {
        return Kernel32.INSTANCE.GetCurrentProcessId();
    }

    @Override
    public int l0illAND() {
        Psapi.PERFORMANCE_INFORMATION pERFORMANCE_INFORMATION = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(pERFORMANCE_INFORMATION, pERFORMANCE_INFORMATION.size())) {
            lli0OiIlAND.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return 0;
        }
        return pERFORMANCE_INFORMATION.ProcessCount.intValue();
    }

    @Override
    public int IO11O0LANd() {
        Psapi.PERFORMANCE_INFORMATION pERFORMANCE_INFORMATION = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(pERFORMANCE_INFORMATION, pERFORMANCE_INFORMATION.size())) {
            lli0OiIlAND.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return 0;
        }
        return pERFORMANCE_INFORMATION.ThreadCount.intValue();
    }

    @Override
    public long l11lLANd() {
        return IOI0I0l1laNd.II1Iland();
    }

    private static long II1Iland() {
        if (O1il1llOLANd) {
            return Kernel32.INSTANCE.GetTickCount64() / 1000L;
        }
        return (long)Kernel32.INSTANCE.GetTickCount() / 1000L;
    }

    @Override
    public long lO110l1LANd() {
        return lil0liLand;
    }

    private static long l0IO0LAnd() {
        String string = (String)lIOILand.get();
        if (string != null) {
            try {
                Advapi32Util.EventLogIterator eventLogIterator = new Advapi32Util.EventLogIterator(null, string, 8);
                long l2 = 0L;
                while (eventLogIterator.hasNext()) {
                    Advapi32Util.EventLogRecord eventLogRecord = eventLogIterator.next();
                    if (eventLogRecord.getStatusCode() == 12) {
                        return eventLogRecord.getRecord().TimeGenerated.longValue();
                    }
                    if (eventLogRecord.getStatusCode() != 6005) continue;
                    if (l2 > 0L) {
                        return l2;
                    }
                    l2 = eventLogRecord.getRecord().TimeGenerated.longValue();
                }
                if (l2 > 0L) {
                    return l2;
                }
            }
            catch (Win32Exception win32Exception) {
                lli0OiIlAND.warn("Can't open event log \"{}\".", (Object)string);
            }
        }
        return System.currentTimeMillis() / 1000L - IOI0I0l1laNd.II1Iland();
    }

    @Override
    public i0O00iOland iOIl0LAnD() {
        return new iIO11lOlLANd();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean liOIOOLANd() {
        WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
        boolean bl = Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 40, hANDLEByReference);
        if (!bl) {
            lli0OiIlAND.error("OpenProcessToken failed. Error: {}", (Object)Native.getLastError());
            return false;
        }
        try {
            WinNT.LUID lUID = new WinNT.LUID();
            bl = Advapi32.INSTANCE.LookupPrivilegeValue(null, "SeDebugPrivilege", lUID);
            if (!bl) {
                lli0OiIlAND.error("LookupPrivilegeValue failed. Error: {}", (Object)Native.getLastError());
                boolean bl2 = false;
                return bl2;
            }
            WinNT.TOKEN_PRIVILEGES tOKEN_PRIVILEGES = new WinNT.TOKEN_PRIVILEGES(1);
            tOKEN_PRIVILEGES.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(lUID, new WinDef.DWORD(2L));
            bl = Advapi32.INSTANCE.AdjustTokenPrivileges(hANDLEByReference.getValue(), false, tOKEN_PRIVILEGES, 0, null, null);
            int n2 = Native.getLastError();
            if (!bl) {
                lli0OiIlAND.error("AdjustTokenPrivileges failed. Error: {}", (Object)n2);
                boolean bl3 = false;
                return bl3;
            }
            if (n2 == 1300) {
                lli0OiIlAND.debug("Debug privileges not enabled.");
                boolean bl4 = false;
                return bl4;
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hANDLEByReference.getValue());
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public iOI10lAnd[] iIiO00OLaNd() {
        try (W32ServiceManager w32ServiceManager = new W32ServiceManager();){
            w32ServiceManager.open(4);
            Winsvc.ENUM_SERVICE_STATUS_PROCESS[] arreNUM_SERVICE_STATUS_PROCESS = w32ServiceManager.enumServicesStatusExProcess(48, 3, null);
            iOI10lAnd[] arriOI10lAnd2 = new iOI10lAnd[arreNUM_SERVICE_STATUS_PROCESS.length];
            for (int i2 = 0; i2 < arreNUM_SERVICE_STATUS_PROCESS.length; ++i2) {
                i0I0land i0I0land2;
                switch (arreNUM_SERVICE_STATUS_PROCESS[i2].ServiceStatusProcess.dwCurrentState) {
                    case 1: {
                        i0I0land2 = i0I0land.OOOIilanD;
                        break;
                    }
                    case 4: {
                        i0I0land2 = i0I0land.I1O1I1LaNd;
                        break;
                    }
                    default: {
                        i0I0land2 = i0I0land.lI00OlAND;
                    }
                }
                arriOI10lAnd2[i2] = new iOI10lAnd(arreNUM_SERVICE_STATUS_PROCESS[i2].lpDisplayName, arreNUM_SERVICE_STATUS_PROCESS[i2].ServiceStatusProcess.dwProcessId, i0I0land2);
            }
            iOI10lAnd[] arriOI10lAnd = arriOI10lAnd2;
            return arriOI10lAnd;
        }
        catch (Win32Exception win32Exception) {
            lli0OiIlAND.error("Win32Exception: {}", (Object)win32Exception.getMessage());
            return new iOI10lAnd[0];
        }
    }

    private static String II1i1l0laND() {
        String string = l10lO11lanD.I1O1I1LaNd("oshi.os.windows.eventlog", "System");
        if (string.isEmpty()) {
            return null;
        }
        WinNT.HANDLE hANDLE = Advapi32.INSTANCE.OpenEventLog(null, string);
        if (hANDLE == null) {
            lli0OiIlAND.warn("Unable to open configured system Event log \"{}\". Calculating boot time from uptime.", (Object)string);
            return null;
        }
        return string;
    }

    @Override
    public List I1O1I1LaNd(boolean bl) {
        return IOi0I0LAnD.I1O1I1LaNd(bl);
    }

    static {
        IOI0I0l1laNd.liOIOOLANd();
    }
}

