/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.lOililLAnd;
import loliland.launcher.client.li0i01LanD;

public final class iIllllOlANd {
    private static final String I1O1I1LaNd = "Win32_DiskDriveToDiskPartition";

    private iIllllOlANd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<lOililLAnd> wmiQuery = new WbemcliUtil.WmiQuery<lOililLAnd>(I1O1I1LaNd, lOililLAnd.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }
}

