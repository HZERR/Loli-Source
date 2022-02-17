/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.IliO01LAnd;
import loliland.launcher.client.li0i01LanD;

public final class O1iO000Land {
    public static final String I1O1I1LaNd = "ROOT\\WMI";
    private static final String OOOIilanD = "MSAcpi_ThermalZoneTemperature";

    private O1iO000Land() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<IliO01LAnd> wmiQuery = new WbemcliUtil.WmiQuery<IliO01LAnd>(I1O1I1LaNd, OOOIilanD, IliO01LAnd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

