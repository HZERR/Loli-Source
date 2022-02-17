/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.Il0O0LANd;
import loliland.launcher.client.l10II00OLaND;
import loliland.launcher.client.lOOOliOlanD;
import loliland.launcher.client.li0i01LanD;

public final class i11IllAnd {
    private static final String I1O1I1LaNd = "Win32_Processor";

    private i11IllAnd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd() {
        WbemcliUtil.WmiQuery<lOOOliOlanD> wmiQuery = new WbemcliUtil.WmiQuery<lOOOliOlanD>(I1O1I1LaNd, lOOOliOlanD.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }

    public static WbemcliUtil.WmiResult OOOIilanD() {
        WbemcliUtil.WmiQuery<Il0O0LANd> wmiQuery = new WbemcliUtil.WmiQuery<Il0O0LANd>(I1O1I1LaNd, Il0O0LANd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }

    public static WbemcliUtil.WmiResult lI00OlAND() {
        WbemcliUtil.WmiQuery<l10II00OLaND> wmiQuery = new WbemcliUtil.WmiQuery<l10II00OLaND>(I1O1I1LaNd, l10II00OLaND.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

