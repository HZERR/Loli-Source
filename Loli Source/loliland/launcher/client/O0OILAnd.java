/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.function.Supplier;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.OlO11Land;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.il1iIIILaND;
import loliland.launcher.client.l0O1OIlaND;
import loliland.launcher.client.lii1IO0LaNd;

final class O0OILAnd
extends il1iIIILaND {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(O0OILAnd::O1il1llOLANd);

    O0OILAnd() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String OOOIilanD() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String lI00OlAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lli0OiIlAND();
    }

    @Override
    public String li0iOILAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).li0iOILAND();
    }

    private static OI1l11LaNd O1il1llOLANd() {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        String string5 = null;
        WbemcliUtil.WmiResult wmiResult = l0O1OIlaND.OOOIilanD();
        if (wmiResult.getResultCount() > 0) {
            string = ii00llanD.I1O1I1LaNd(wmiResult, OlO11Land.I1O1I1LaNd, 0);
            string2 = ii00llanD.I1O1I1LaNd(wmiResult, OlO11Land.OOOIilanD, 0);
            string3 = ii00llanD.I1O1I1LaNd(wmiResult, OlO11Land.lI00OlAND, 0);
            string4 = ii00llanD.I1O1I1LaNd(wmiResult, OlO11Land.lli0OiIlAND, 0);
            string5 = ii00llanD.OOOIilanD(wmiResult, OlO11Land.li0iOILAND, 0);
        }
        return new OI1l11LaNd(iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3, iiIIIlO1lANd.I1O1I1LaNd(string4) ? "unknown" : string4, iiIIIlO1lANd.I1O1I1LaNd(string5) ? "unknown" : string5);
    }
}

