/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.l10OilanD;
import loliland.launcher.client.li0i01LanD;

public final class lO011O1IlAnD {
    private static final String I1O1I1LaNd = "Win32_ComputerSystem";

    private lO011O1IlAnD() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<l10OilanD> wmiQuery = new WbemcliUtil.WmiQuery<l10OilanD>(I1O1I1LaNd, l10OilanD.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

