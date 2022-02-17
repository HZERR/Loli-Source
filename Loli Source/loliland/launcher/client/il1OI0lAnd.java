/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.iiOlLand;
import loliland.launcher.client.li0i01LanD;

public final class il1OI0lAnd {
    private static final String I1O1I1LaNd = "Win32_Fan";

    private il1OI0lAnd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<iiOlLand> wmiQuery = new WbemcliUtil.WmiQuery<iiOlLand>(I1O1I1LaNd, iiOlLand.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

