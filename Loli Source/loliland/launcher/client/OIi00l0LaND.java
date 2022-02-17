/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.I111i0OILaNd;
import loliland.launcher.client.lIOi0LANd;
import loliland.launcher.client.li0i01LanD;

public final class OIi00l0LaND {
    private static final String I1O1I1LaNd = "Win32_PhysicalMemory";

    private OIi00l0LaND() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<I111i0OILaNd> wmiQuery = new WbemcliUtil.WmiQuery<I111i0OILaNd>(I1O1I1LaNd, I111i0OILaNd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }

    public static WbemcliUtil.WmiResult OOOIilanD() {
        WbemcliUtil.WmiQuery<lIOi0LANd> wmiQuery = new WbemcliUtil.WmiQuery<lIOi0LANd>(I1O1I1LaNd, lIOi0LANd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

