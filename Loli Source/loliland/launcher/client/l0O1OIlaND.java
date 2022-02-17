/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.OlO11Land;
import loliland.launcher.client.lOlO0I0land;
import loliland.launcher.client.li0i01LanD;

public final class l0O1OIlaND {
    private static final String I1O1I1LaNd = "Win32_BIOS where PrimaryBIOS=true";

    private l0O1OIlaND() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<lOlO0I0land> wmiQuery = new WbemcliUtil.WmiQuery<lOlO0I0land>(I1O1I1LaNd, lOlO0I0land.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }

    public static WbemcliUtil.WmiResult OOOIilanD() {
        WbemcliUtil.WmiQuery<OlO11Land> wmiQuery = new WbemcliUtil.WmiQuery<OlO11Land>(I1O1I1LaNd, OlO11Land.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

