/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.IOI0I0l1laNd;
import loliland.launcher.client.IOlli01lAnd;
import loliland.launcher.client.Il0iIland;
import loliland.launcher.client.O1I01lLANd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.O1iOOlllANd;
import loliland.launcher.client.O1iOl0lAND;
import loliland.launcher.client.O1liliLANd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.iOO1IiI1lAnD;
import loliland.launcher.client.iOOl1lAnd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.l11OOlIIlaND;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll0iOlLAnd;
import loliland.launcher.client.lllO0IiLANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class lIIi01O1laNd
extends O1I01lLANd {
    private static final Logger OOOIilanD = LoggerFactory.getLogger(lIIi01O1laNd.class);
    public static final String I1O1I1LaNd = "oshi.os.windows.commandline.batch";
    private static final boolean lI00OlAND = l10lO11lanD.I1O1I1LaNd("oshi.os.windows.commandline.batch", false);
    private static final boolean lli0OiIlAND = l10lO11lanD.I1O1I1LaNd("oshi.os.windows.procstate.suspended", false);
    private static final boolean li0iOILAND = VersionHelpers.IsWindowsVistaOrGreater();
    private static final boolean O1il1llOLANd = VersionHelpers.IsWindows7OrGreater();
    private Supplier Oill1LAnD = lii1IO0LaNd.I1O1I1LaNd(this::l1i00lLAnD);
    private Supplier lIOILand = lii1IO0LaNd.I1O1I1LaNd(this::lili0l0laNd);
    private Supplier lil0liLand = lii1IO0LaNd.I1O1I1LaNd(this::ll1ILAnd);
    private String iilIi1laND;
    private String lli011lLANd;
    private String l0illAND;
    private OOOOO10iLAND IO11O0LANd = OOOOO10iLAND.lIOILand;
    private int l11lLANd;
    private int lO110l1LANd;
    private int l0iIlIO1laNd;
    private long iOIl0LAnD;
    private long iIiO00OLaNd;
    private long ii1li00Land;
    private long IOI1LaNd;
    private long lI00ilAND;
    private long l0l00lAND;
    private long iOl10IlLAnd;
    private long lIiIii1LAnD;
    private long II1Iland;
    private int l0IO0LAnd;
    private long liOIOOLANd;

    public lIIi01O1laNd(int n2, IOI0I0l1laNd iOI0I0l1laNd, Map map, Map map2, Map map3) {
        super(n2);
        if (n2 == iOI0I0l1laNd.lli011lLANd()) {
            String string = new File(".").getAbsolutePath();
            this.l0illAND = string.isEmpty() ? "" : string.substring(0, string.length() - 1);
        }
        this.l0IO0LAnd = iOI0I0l1laNd.O1il1llOLANd();
        this.I1O1I1LaNd((l11OOlIIlaND)map.get(n2), (Il0iIland)map2.get(n2), map3);
    }

    @Override
    public String lI00OlAND() {
        return this.iilIi1laND;
    }

    @Override
    public String lli0OiIlAND() {
        return this.lli011lLANd;
    }

    @Override
    public String li0iOILAND() {
        return (String)this.lil0liLand.get();
    }

    @Override
    public String O1il1llOLANd() {
        return this.l0illAND;
    }

    @Override
    public String Oill1LAnD() {
        return (String)((O1IiIiI1LAND)this.Oill1LAnD.get()).I1O1I1LaNd();
    }

    @Override
    public String lIOILand() {
        return (String)((O1IiIiI1LAND)this.Oill1LAnD.get()).OOOIilanD();
    }

    @Override
    public String lil0liLand() {
        return (String)((O1IiIiI1LAND)this.lIOILand.get()).I1O1I1LaNd();
    }

    @Override
    public String iilIi1laND() {
        return (String)((O1IiIiI1LAND)this.lIOILand.get()).OOOIilanD();
    }

    @Override
    public OOOOO10iLAND lli011lLANd() {
        return this.IO11O0LANd;
    }

    @Override
    public int l0illAND() {
        return this.l11lLANd;
    }

    @Override
    public int IO11O0LANd() {
        return this.lO110l1LANd;
    }

    @Override
    public int l11lLANd() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public long lO110l1LANd() {
        return this.iOIl0LAnD;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.iIiO00OLaNd;
    }

    @Override
    public long iOIl0LAnD() {
        return this.ii1li00Land;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.IOI1LaNd;
    }

    @Override
    public long ii1li00Land() {
        return this.l0l00lAND;
    }

    @Override
    public long IOI1LaNd() {
        return this.lI00ilAND;
    }

    @Override
    public long lI00ilAND() {
        return this.iOl10IlLAnd;
    }

    @Override
    public long l0l00lAND() {
        return this.lIiIii1LAnD;
    }

    @Override
    public long iOl10IlLAnd() {
        return this.II1Iland;
    }

    @Override
    public int lIiIii1LAnD() {
        return this.l0IO0LAnd;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long II1Iland() {
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, this.I1O1I1LaNd());
        if (hANDLE != null) {
            try {
                BaseTSD.ULONG_PTRByReference uLONG_PTRByReference = new BaseTSD.ULONG_PTRByReference();
                BaseTSD.ULONG_PTRByReference uLONG_PTRByReference2 = new BaseTSD.ULONG_PTRByReference();
                if (Kernel32.INSTANCE.GetProcessAffinityMask(hANDLE, uLONG_PTRByReference, uLONG_PTRByReference2)) {
                    long l2 = Pointer.nativeValue(uLONG_PTRByReference.getValue().toPointer());
                    return l2;
                }
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
            Kernel32.INSTANCE.CloseHandle(hANDLE);
        }
        return 0L;
    }

    @Override
    public long II1i1l0laND() {
        return this.liOIOOLANd;
    }

    @Override
    public List liOIOOLANd() {
        Map map = O1liliLANd.I1O1I1LaNd(Collections.singleton(this.I1O1I1LaNd()));
        if (map != null) {
            map = O1liliLANd.OOOIilanD(Collections.singleton(this.I1O1I1LaNd()));
        }
        if (map == null) {
            return Collections.emptyList();
        }
        return map.entrySet().stream().map(entry -> new iOOl1lAnd(this.I1O1I1LaNd(), (Integer)entry.getKey(), this.iilIi1laND, (O1iOl0lAND)entry.getValue())).collect(Collectors.toList());
    }

    @Override
    public boolean l0IO0LAnd() {
        Set<Integer> set = Collections.singleton(this.I1O1I1LaNd());
        Map map = IOlli01lAnd.I1O1I1LaNd(null);
        if (map == null) {
            map = IOlli01lAnd.OOOIilanD(set);
        }
        Map map2 = null;
        if (lli0OiIlAND && (map2 = O1liliLANd.I1O1I1LaNd(null)) == null) {
            map2 = O1liliLANd.OOOIilanD(null);
        }
        Map map3 = lllO0IiLANd.I1O1I1LaNd(set);
        return this.I1O1I1LaNd((l11OOlIIlaND)map.get(this.I1O1I1LaNd()), (Il0iIland)map3.get(this.I1O1I1LaNd()), map2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean I1O1I1LaNd(l11OOlIIlaND l11OOlIIlaND2, Il0iIland il0iIland, Map map) {
        WinNT.HANDLE hANDLE;
        Object object;
        this.iilIi1laND = l11OOlIIlaND2.I1O1I1LaNd();
        this.lli011lLANd = il0iIland.OOOIilanD();
        this.l11lLANd = l11OOlIIlaND2.OOOIilanD();
        this.lO110l1LANd = il0iIland.lI00OlAND();
        this.l0iIlIO1laNd = l11OOlIIlaND2.lI00OlAND();
        this.iOIl0LAnD = il0iIland.lli0OiIlAND();
        this.iIiO00OLaNd = l11OOlIIlaND2.lli0OiIlAND();
        this.ii1li00Land = il0iIland.li0iOILAND();
        this.IOI1LaNd = il0iIland.O1il1llOLANd();
        this.lI00ilAND = l11OOlIIlaND2.li0iOILAND();
        this.l0l00lAND = l11OOlIIlaND2.O1il1llOLANd();
        this.iOl10IlLAnd = l11OOlIIlaND2.Oill1LAnD();
        this.lIiIii1LAnD = l11OOlIIlaND2.lIOILand();
        this.II1Iland = il0iIland.Oill1LAnD();
        this.liOIOOLANd = l11OOlIIlaND2.lil0liLand();
        this.IO11O0LANd = OOOOO10iLAND.OOOIilanD;
        if (map != null) {
            int n2 = this.I1O1I1LaNd();
            object = map.values().iterator();
            while (object.hasNext()) {
                O1iOl0lAND o1iOl0lAND = (O1iOl0lAND)object.next();
                if (o1iOl0lAND.lI00OlAND() != n2) continue;
                if (o1iOl0lAND.lil0liLand() == 5) {
                    this.IO11O0LANd = OOOOO10iLAND.lil0liLand;
                    continue;
                }
                this.IO11O0LANd = OOOOO10iLAND.OOOIilanD;
                break;
            }
        }
        if ((hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, this.I1O1I1LaNd())) != null) {
            try {
                if (li0iOILAND && this.l0IO0LAnd == 64 && Kernel32.INSTANCE.IsWow64Process(hANDLE, (IntByReference)(object = new IntByReference(0))) && ((IntByReference)object).getValue() > 0) {
                    this.l0IO0LAnd = 32;
                }
                object = new WinNT.HANDLEByReference();
                try {
                    if (O1il1llOLANd) {
                        this.lli011lLANd = Kernel32Util.QueryFullProcessImageName(hANDLE, 0);
                    }
                }
                catch (Win32Exception win32Exception) {
                    this.IO11O0LANd = OOOOO10iLAND.lIOILand;
                }
                finally {
                    WinNT.HANDLE hANDLE2 = ((WinNT.HANDLEByReference)object).getValue();
                    if (hANDLE2 != null) {
                        Kernel32.INSTANCE.CloseHandle(hANDLE2);
                    }
                }
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
        }
        return !this.IO11O0LANd.equals((Object)OOOOO10iLAND.lIOILand);
    }

    private String ll1ILAnd() {
        if (lI00OlAND) {
            return ll0iOlLAnd.I1O1I1LaNd().I1O1I1LaNd(this.I1O1I1LaNd(), this.IOI1LaNd());
        }
        WbemcliUtil.WmiResult wmiResult = O1iOOlllANd.I1O1I1LaNd(Collections.singleton(this.I1O1I1LaNd()));
        if (wmiResult.getResultCount() > 0) {
            return ii00llanD.I1O1I1LaNd(wmiResult, iOO1IiI1lAnD.OOOIilanD, 0);
        }
        return "unknown";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private O1IiIiI1LAND l1i00lLAnD() {
        O1IiIiI1LAND o1IiIiI1LAND = null;
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, this.I1O1I1LaNd());
        if (hANDLE != null) {
            WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
            try {
                if (Advapi32.INSTANCE.OpenProcessToken(hANDLE, 10, hANDLEByReference)) {
                    Advapi32Util.Account account = Advapi32Util.getTokenAccount(hANDLEByReference.getValue());
                    o1IiIiI1LAND = new O1IiIiI1LAND(account.name, account.sidString);
                } else {
                    int n2 = Kernel32.INSTANCE.GetLastError();
                    if (n2 != 5) {
                        OOOIilanD.error("Failed to get process token for process {}: {}", (Object)this.I1O1I1LaNd(), (Object)Kernel32.INSTANCE.GetLastError());
                    }
                }
            }
            catch (Win32Exception win32Exception) {
                OOOIilanD.warn("Failed to query user info for process {} ({}): {}", this.I1O1I1LaNd(), this.lI00OlAND(), win32Exception.getMessage());
            }
            finally {
                WinNT.HANDLE hANDLE2 = hANDLEByReference.getValue();
                if (hANDLE2 != null) {
                    Kernel32.INSTANCE.CloseHandle(hANDLE2);
                }
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
        }
        if (o1IiIiI1LAND == null) {
            return new O1IiIiI1LAND("unknown", "unknown");
        }
        return o1IiIiI1LAND;
    }

    private O1IiIiI1LAND lili0l0laNd() {
        O1IiIiI1LAND o1IiIiI1LAND = null;
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, this.I1O1I1LaNd());
        if (hANDLE != null) {
            Object object;
            WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
            if (Advapi32.INSTANCE.OpenProcessToken(hANDLE, 10, hANDLEByReference)) {
                object = Advapi32Util.getTokenPrimaryGroup(hANDLEByReference.getValue());
                o1IiIiI1LAND = new O1IiIiI1LAND(((Advapi32Util.Account)object).name, ((Advapi32Util.Account)object).sidString);
            } else {
                int n2 = Kernel32.INSTANCE.GetLastError();
                if (n2 != 5) {
                    OOOIilanD.error("Failed to get process token for process {}: {}", (Object)this.I1O1I1LaNd(), (Object)Kernel32.INSTANCE.GetLastError());
                }
            }
            object = hANDLEByReference.getValue();
            if (object != null) {
                Kernel32.INSTANCE.CloseHandle((WinNT.HANDLE)object);
            }
            Kernel32.INSTANCE.CloseHandle(hANDLE);
        }
        if (o1IiIiI1LAND == null) {
            return new O1IiIiI1LAND("unknown", "unknown");
        }
        return o1IiIiI1LAND;
    }
}

