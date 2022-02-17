/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.ilI1l1IOLanD;

final class ll0IlILANd
extends ilI1l1IOLanD {
    private static final String I1O1I1LaNd = "SYSTEM\\CurrentControlSet\\Control\\Class\\{4d36e96c-e325-11ce-bfc1-08002be10318}\\";

    ll0IlILANd(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public static List lli0OiIlAND() {
        String[] arrstring;
        ArrayList<ll0IlILANd> arrayList = new ArrayList<ll0IlILANd>();
        for (String string : arrstring = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, I1O1I1LaNd)) {
            String string2 = I1O1I1LaNd + string;
            try {
                if (!Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, string2, "Driver")) continue;
                arrayList.add(new ll0IlILANd(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, string2, "Driver") + " " + Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, string2, "DriverVersion"), Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, string2, "ProviderName") + " " + Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, string2, "DriverDesc"), Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, string2, "DriverDesc")));
            }
            catch (Win32Exception win32Exception) {
                if (win32Exception.getErrorCode() == 5) continue;
                throw win32Exception;
            }
        }
        return arrayList;
    }
}

