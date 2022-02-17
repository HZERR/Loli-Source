/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.lOi1OOI1lanD;
import loliland.launcher.client.li0i01LanD;

public final class lI0OIILand {
    private static final String I1O1I1LaNd = "Win32_DiskPartition";

    private lI0OIILand() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<lOi1OOI1lanD> wmiQuery = new WbemcliUtil.WmiQuery<lOi1OOI1lanD>(I1O1I1LaNd, lOi1OOI1lanD.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }
}

