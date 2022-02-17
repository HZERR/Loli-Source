/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.iOI1I1iLaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class liIOI1LANd {
    private static final String I1O1I1LaNd = "\\";
    private static final String OOOIilanD = "Console";
    private static final String lI00OlAND = "Volatile Environment";
    private static final String lli0OiIlAND = "CLIENTNAME";
    private static final String li0iOILAND = "SESSIONNAME";
    private static final Logger O1il1llOLANd = LoggerFactory.getLogger(liIOI1LANd.class);

    private liIOI1LANd() {
    }

    public static List I1O1I1LaNd() {
        ArrayList<iOI1I1iLaNd> arrayList = new ArrayList<iOI1I1iLaNd>();
        for (String string : Advapi32Util.registryGetKeys(WinReg.HKEY_USERS)) {
            if (string.startsWith(".") || string.endsWith("_Classes")) continue;
            try {
                Advapi32Util.Account account = Advapi32Util.getAccountBySid(string);
                String string2 = account.name;
                String string3 = OOOIilanD;
                String string4 = account.domain;
                long l2 = 0L;
                String string5 = string + I1O1I1LaNd + lI00OlAND;
                if (Advapi32Util.registryKeyExists(WinReg.HKEY_USERS, string5)) {
                    WinReg.HKEY hKEY = Advapi32Util.registryGetKey(WinReg.HKEY_USERS, string5, 131097).getValue();
                    Advapi32Util.InfoKey infoKey = Advapi32Util.registryQueryInfoKey(hKEY, 0);
                    l2 = infoKey.lpftLastWriteTime.toTime();
                    for (String string6 : Advapi32Util.registryGetKeys(hKEY)) {
                        String string7;
                        String string8 = string5 + I1O1I1LaNd + string6;
                        if (Advapi32Util.registryValueExists(WinReg.HKEY_USERS, string8, li0iOILAND) && !(string7 = Advapi32Util.registryGetStringValue(WinReg.HKEY_USERS, string8, li0iOILAND)).isEmpty()) {
                            string3 = string7;
                        }
                        if (!Advapi32Util.registryValueExists(WinReg.HKEY_USERS, string8, lli0OiIlAND) || (string7 = Advapi32Util.registryGetStringValue(WinReg.HKEY_USERS, string8, lli0OiIlAND)).isEmpty() || OOOIilanD.equals(string7)) continue;
                        string4 = string7;
                    }
                    Advapi32Util.registryCloseKey(hKEY);
                }
                arrayList.add(new iOI1I1iLaNd(string2, string3, l2, string4));
            }
            catch (Win32Exception win32Exception) {
                O1il1llOLANd.warn("Error querying SID {} from registry: {}", (Object)string, (Object)win32Exception.getMessage());
            }
        }
        return arrayList;
    }
}

