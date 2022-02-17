/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.function.Supplier;
import loliland.launcher.client.I100I0lLand;
import loliland.launcher.client.O11i1I1lAnd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1O0iiland;
import loliland.launcher.client.llllLANd;

final class l11l0lanD
extends ll1O0iiland {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(l11l0lanD::li0iOILAND);

    l11l0lanD() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String OOOIilanD() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String lI00OlAND() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).lli0OiIlAND();
    }

    private static O11i1I1lAnd li0iOILAND() {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        WbemcliUtil.WmiResult wmiResult = I100I0lLand.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 0) {
            string = ii00llanD.I1O1I1LaNd(wmiResult, llllLANd.I1O1I1LaNd, 0);
            string2 = ii00llanD.I1O1I1LaNd(wmiResult, llllLANd.OOOIilanD, 0);
            string3 = ii00llanD.I1O1I1LaNd(wmiResult, llllLANd.lI00OlAND, 0);
            string4 = ii00llanD.I1O1I1LaNd(wmiResult, llllLANd.lli0OiIlAND, 0);
        }
        return new O11i1I1lAnd(iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3, iiIIIlO1lANd.I1O1I1LaNd(string4) ? "unknown" : string4);
    }
}

