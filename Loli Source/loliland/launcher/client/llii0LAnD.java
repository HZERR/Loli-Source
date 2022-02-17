/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.lii1IlAND;

public final class llii0LAnD {
    private static final String I1O1I1LaNd = "Win32_DiskDrive";

    private llii0LAnD() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<lii1IlAND> wmiQuery = new WbemcliUtil.WmiQuery<lii1IlAND>(I1O1I1LaNd, lii1IlAND.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }
}

