/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.O0li0i11LANd;
import loliland.launcher.client.li0i01LanD;

public final class O1iIland {
    private static final String I1O1I1LaNd = "Win32_VideoController";

    private O1iIland() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<O0li0i11LANd> wmiQuery = new WbemcliUtil.WmiQuery<O0li0i11LANd>(I1O1I1LaNd, O0li0i11LANd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

