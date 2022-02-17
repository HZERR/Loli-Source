/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Pdh;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.l1Oili01LaND;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class O1O00OllAND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(O1O00OllAND.class);
    private static final BaseTSD.DWORD_PTR OOOIilanD = new BaseTSD.DWORD_PTR(0L);
    private static final WinDef.DWORDByReference lI00OlAND = new WinDef.DWORDByReference(new WinDef.DWORD(16L));
    private static final Pdh lli0OiIlAND = Pdh.INSTANCE;
    private static final boolean li0iOILAND = VersionHelpers.IsWindowsVistaOrGreater();

    private O1O00OllAND() {
    }

    public static l1Oili01LaND I1O1I1LaNd(String string, String string2, String string3) {
        return new l1Oili01LaND(string, string2, string3);
    }

    public static long I1O1I1LaNd(WinNT.HANDLEByReference hANDLEByReference) {
        WinDef.LONGLONGByReference lONGLONGByReference = new WinDef.LONGLONGByReference();
        int n2 = li0iOILAND ? lli0OiIlAND.PdhCollectQueryDataWithTime(hANDLEByReference.getValue(), lONGLONGByReference) : lli0OiIlAND.PdhCollectQueryData(hANDLEByReference.getValue());
        int n3 = 0;
        while (n2 == -2147481643 && n3++ < 3) {
            iiIIIlO1lANd.I1O1I1LaNd(1 << n3);
            n2 = li0iOILAND ? lli0OiIlAND.PdhCollectQueryDataWithTime(hANDLEByReference.getValue(), lONGLONGByReference) : lli0OiIlAND.PdhCollectQueryData(hANDLEByReference.getValue());
        }
        if (n2 != 0) {
            if (I1O1I1LaNd.isWarnEnabled()) {
                I1O1I1LaNd.warn("Failed to update counter. Error code: {}", (Object)String.format(i00ilaNd.lI00OlAND(n2), new Object[0]));
            }
            return 0L;
        }
        return li0iOILAND ? lOilLanD.I1O1I1LaNd(lONGLONGByReference.getValue().longValue(), true) : System.currentTimeMillis();
    }

    public static boolean OOOIilanD(WinNT.HANDLEByReference hANDLEByReference) {
        int n2 = lli0OiIlAND.PdhOpenQuery(null, OOOIilanD, hANDLEByReference);
        if (n2 != 0) {
            if (I1O1I1LaNd.isErrorEnabled()) {
                I1O1I1LaNd.error("Failed to open PDH Query. Error code: {}", (Object)String.format(i00ilaNd.lI00OlAND(n2), new Object[0]));
            }
            return false;
        }
        return true;
    }

    public static boolean lI00OlAND(WinNT.HANDLEByReference hANDLEByReference) {
        return 0 == lli0OiIlAND.PdhCloseQuery(hANDLEByReference.getValue());
    }

    public static long lli0OiIlAND(WinNT.HANDLEByReference hANDLEByReference) {
        Pdh.PDH_RAW_COUNTER pDH_RAW_COUNTER = new Pdh.PDH_RAW_COUNTER();
        int n2 = lli0OiIlAND.PdhGetRawCounterValue(hANDLEByReference.getValue(), lI00OlAND, pDH_RAW_COUNTER);
        if (n2 != 0) {
            if (I1O1I1LaNd.isWarnEnabled()) {
                I1O1I1LaNd.warn("Failed to get counter. Error code: {}", (Object)String.format(i00ilaNd.lI00OlAND(n2), new Object[0]));
            }
            return n2;
        }
        return pDH_RAW_COUNTER.FirstValue;
    }

    public static boolean I1O1I1LaNd(WinNT.HANDLEByReference hANDLEByReference, String string, WinNT.HANDLEByReference hANDLEByReference2) {
        int n2;
        int n3 = n2 = li0iOILAND ? lli0OiIlAND.PdhAddEnglishCounter(hANDLEByReference.getValue(), string, OOOIilanD, hANDLEByReference2) : lli0OiIlAND.PdhAddCounter(hANDLEByReference.getValue(), string, OOOIilanD, hANDLEByReference2);
        if (n2 != 0) {
            if (I1O1I1LaNd.isWarnEnabled()) {
                I1O1I1LaNd.warn("Failed to add PDH Counter: {}, Error code: {}", (Object)string, (Object)String.format(i00ilaNd.lI00OlAND(n2), new Object[0]));
            }
            return false;
        }
        return true;
    }

    public static boolean li0iOILAND(WinNT.HANDLEByReference hANDLEByReference) {
        return 0 == lli0OiIlAND.PdhRemoveCounter(hANDLEByReference.getValue());
    }
}

