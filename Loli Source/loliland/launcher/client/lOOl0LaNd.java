/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.lIOl0lAND;
import loliland.launcher.client.li0i01LanD;

public final class lOOl0LaNd {
    private static final String I1O1I1LaNd = "Win32_ComputerSystemProduct";

    private lOOl0LaNd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<lIOl0lAND> wmiQuery = new WbemcliUtil.WmiQuery<lIOl0lAND>(I1O1I1LaNd, lIOl0lAND.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

