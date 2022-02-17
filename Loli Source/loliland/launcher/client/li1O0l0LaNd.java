/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.lilILAND;

public final class li1O0l0LaNd {
    private static final String I1O1I1LaNd = "Win32_LogicalDisk";

    private li1O0l0LaNd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(String string, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder(I1O1I1LaNd);
        boolean bl2 = false;
        if (bl) {
            stringBuilder.append(" WHERE DriveType != 4");
            bl2 = true;
        }
        if (string != null) {
            stringBuilder.append(bl2 ? " AND" : " WHERE").append(" Name=\"").append(string).append('\"');
        }
        WbemcliUtil.WmiQuery<lilILAND> wmiQuery = new WbemcliUtil.WmiQuery<lilILAND>(stringBuilder.toString(), lilILAND.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

