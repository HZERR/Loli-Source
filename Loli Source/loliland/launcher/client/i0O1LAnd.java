/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.lOIOlOIIlAnd;
import loliland.launcher.client.li0i01LanD;

public final class i0O1LAnd {
    private static final String I1O1I1LaNd = "Hardware";

    private i0O1LAnd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(li0i01LanD li0i01LanD2, String string, String string2) {
        StringBuilder stringBuilder = new StringBuilder(I1O1I1LaNd);
        stringBuilder.append(" WHERE ").append(string).append("Type=\"").append(string2).append('\"');
        WbemcliUtil.WmiQuery<lOIOlOIIlAnd> wmiQuery = new WbemcliUtil.WmiQuery<lOIOlOIIlAnd>("ROOT\\OpenHardwareMonitor", stringBuilder.toString(), lOIOlOIIlAnd.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }
}

