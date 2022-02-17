/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.O1llI1LAnd;
import loliland.launcher.client.li0i01LanD;

public final class IliIIi1lanD {
    private static final String I1O1I1LaNd = "Win32_OperatingSystem";

    private IliIIi1lanD() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<O1llI1LAnd> wmiQuery = new WbemcliUtil.WmiQuery<O1llI1LAnd>(I1O1I1LaNd, O1llI1LAnd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

