/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.function.Supplier;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.O0OILAnd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.l0O1OIlaND;
import loliland.launcher.client.l10OilanD;
import loliland.launcher.client.l11l0lanD;
import loliland.launcher.client.lIOl0lAND;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lO011O1IlAnD;
import loliland.launcher.client.lOOl0LaNd;
import loliland.launcher.client.lOlO0I0land;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OLAnd;

final class I1i0laND
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(I1i0laND::lil0liLand);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(I1i0laND::iilIi1laND);

    I1i0laND() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String OOOIilanD() {
        return (String)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String lI00OlAND() {
        return (String)((O1IiIiI1LAND)this.OOOIilanD.get()).I1O1I1LaNd();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((O1IiIiI1LAND)this.OOOIilanD.get()).OOOIilanD();
    }

    @Override
    public lIl1OlAND Oill1LAnD() {
        return new O0OILAnd();
    }

    @Override
    public ll1OLAnd lIOILand() {
        return new l11l0lanD();
    }

    private static O1IiIiI1LAND lil0liLand() {
        String string = null;
        String string2 = null;
        WbemcliUtil.WmiResult wmiResult = lO011O1IlAnD.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 0) {
            string = ii00llanD.I1O1I1LaNd(wmiResult, l10OilanD.I1O1I1LaNd, 0);
            string2 = ii00llanD.I1O1I1LaNd(wmiResult, l10OilanD.OOOIilanD, 0);
        }
        return new O1IiIiI1LAND(iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2);
    }

    private static O1IiIiI1LAND iilIi1laND() {
        String string = null;
        String string2 = null;
        WbemcliUtil.WmiResult wmiResult = lOOl0LaNd.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 0) {
            string = ii00llanD.I1O1I1LaNd(wmiResult, lIOl0lAND.I1O1I1LaNd, 0);
            string2 = ii00llanD.I1O1I1LaNd(wmiResult, lIOl0lAND.OOOIilanD, 0);
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string)) {
            string = I1i0laND.lli011lLANd();
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string)) {
            string = "unknown";
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string2)) {
            string2 = "unknown";
        }
        return new O1IiIiI1LAND(string, string2);
    }

    private static String lli011lLANd() {
        WbemcliUtil.WmiResult wmiResult = l0O1OIlaND.I1O1I1LaNd();
        if (wmiResult.getResultCount() > 0) {
            return ii00llanD.I1O1I1LaNd(wmiResult, lOlO0I0land.I1O1I1LaNd, 0);
        }
        return null;
    }
}

