/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import loliland.launcher.client.i1i1OilanD;
import loliland.launcher.client.lOlOlaND;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.liOi1OLaNd;
import loliland.launcher.client.llOlOiLand;

public final class il0ii0ILANd {
    private static final String I1O1I1LaNd = "ROOT\\Microsoft\\Windows\\Storage";
    private static final String OOOIilanD = "MSFT_StoragePool WHERE IsPrimordial=FALSE";
    private static final String lI00OlAND = "MSFT_StoragePoolToPhysicalDisk";
    private static final String lli0OiIlAND = "MSFT_PhysicalDisk";
    private static final String li0iOILAND = "MSFT_VirtualDisk";

    private il0ii0ILANd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<i1i1OilanD> wmiQuery = new WbemcliUtil.WmiQuery<i1i1OilanD>(I1O1I1LaNd, OOOIilanD, i1i1OilanD.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }

    public static WbemcliUtil.WmiResult OOOIilanD(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<liOi1OLaNd> wmiQuery = new WbemcliUtil.WmiQuery<liOi1OLaNd>(I1O1I1LaNd, lI00OlAND, liOi1OLaNd.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }

    public static WbemcliUtil.WmiResult lI00OlAND(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<lOlOlaND> wmiQuery = new WbemcliUtil.WmiQuery<lOlOlaND>(I1O1I1LaNd, lli0OiIlAND, lOlOlaND.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }

    public static WbemcliUtil.WmiResult lli0OiIlAND(li0i01LanD li0i01LanD2) {
        WbemcliUtil.WmiQuery<llOlOiLand> wmiQuery = new WbemcliUtil.WmiQuery<llOlOiLand>(I1O1I1LaNd, li0iOILAND, llOlOiLand.class);
        return li0i01LanD2.I1O1I1LaNd(wmiQuery, false);
    }
}

