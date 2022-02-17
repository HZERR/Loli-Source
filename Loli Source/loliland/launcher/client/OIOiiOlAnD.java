/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.VersionHelpers;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.O0li0i11LANd;
import loliland.launcher.client.O1iIland;
import loliland.launcher.client.iIl0il1lAnD;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lOilLanD;

final class OIOiiOlAnD
extends iIl0il1lAnD {
    private static final boolean I1O1I1LaNd = VersionHelpers.IsWindowsVistaOrGreater();

    OIOiiOlAnD(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List O1il1llOLANd() {
        ArrayList<OIOiiOlAnD> arrayList = new ArrayList<OIOiiOlAnD>();
        if (I1O1I1LaNd) {
            WbemcliUtil.WmiResult wmiResult = O1iIland.I1O1I1LaNd();
            for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                String string;
                String string2 = ii00llanD.I1O1I1LaNd(wmiResult, O0li0i11LANd.lli0OiIlAND, i2);
                IIOOOlIiLanD iIOOOlIiLanD = lOilLanD.l0illAND(ii00llanD.I1O1I1LaNd(wmiResult, O0li0i11LANd.li0iOILAND, i2));
                String string3 = iIOOOlIiLanD == null ? "unknown" : (String)iIOOOlIiLanD.OOOIilanD();
                String string4 = ii00llanD.I1O1I1LaNd(wmiResult, O0li0i11LANd.I1O1I1LaNd, i2);
                if (iIOOOlIiLanD != null) {
                    if (iiIIIlO1lANd.I1O1I1LaNd(string4)) {
                        string3 = (String)iIOOOlIiLanD.I1O1I1LaNd();
                    } else {
                        string4 = string4 + " (" + (String)iIOOOlIiLanD.I1O1I1LaNd() + ")";
                    }
                }
                string = !iiIIIlO1lANd.I1O1I1LaNd(string = ii00llanD.I1O1I1LaNd(wmiResult, O0li0i11LANd.lI00OlAND, i2)) ? "DriverVersion=" + string : "unknown";
                long l2 = ii00llanD.Oill1LAnD(wmiResult, O0li0i11LANd.OOOIilanD, i2);
                arrayList.add(new OIOiiOlAnD(iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, string3, iiIIIlO1lANd.I1O1I1LaNd(string4) ? "unknown" : string4, string, l2));
            }
        }
        return arrayList;
    }
}

