/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.llllLANd;

public final class I100I0lLand {
    private static final String I1O1I1LaNd = "Win32_BaseBoard";

    private I100I0lLand() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<llllLANd> wmiQuery = new WbemcliUtil.WmiQuery<llllLANd>(I1O1I1LaNd, llllLANd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

