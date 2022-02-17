/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.lOOliLANd;
import loliland.launcher.client.li0i01LanD;

public final class I0Oi1O0OlaND {
    private static final String I1O1I1LaNd = "Win32_LogicalDiskToPartition";

    private I0Oi1O0OlaND() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<lOOliLANd> wmiQuery = new WbemcliUtil.WmiQuery<lOOliLANd>(I1O1I1LaNd, lOOliLANd.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }
}

